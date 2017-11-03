package net.syscon.elite.service.impl;

import net.syscon.elite.api.model.CaseNote;
import net.syscon.elite.api.model.NewCaseNote;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.repository.CaseNoteRepository;
import net.syscon.elite.security.UserSecurityUtils;
import net.syscon.elite.service.BookingService;
import net.syscon.elite.service.CaseNoteService;
import net.syscon.elite.service.EntityNotFoundException;
import net.syscon.elite.service.validation.ReferenceCodesValid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Transactional
@Service
@Validated
public class CaseNoteServiceImpl implements CaseNoteService {
	private static final String AMEND_CASE_NOTE_FORMAT = "%s ...[%s updated the case notes on %s] %s";

	@Value("${api.caseNote.sourceCode:AUTO}")
	private String caseNoteSource;

    private final CaseNoteRepository caseNoteRepository;
    private final CaseNoteTransformer transformer;
    private final BookingService bookingService;

    public CaseNoteServiceImpl(CaseNoteRepository caseNoteRepository, CaseNoteTransformer transformer,
            BookingService bookingService) {
        this.caseNoteRepository = caseNoteRepository;
        this.transformer = transformer;
        this.bookingService = bookingService;
    }

    @Transactional(readOnly = true)
	@Override
	public Page<CaseNote> getCaseNotes(long bookingId, String query, LocalDate from, LocalDate to, String orderBy, Order order, long offset, long limit) {
        bookingService.verifyBookingAccess(bookingId);

        String colSort = orderBy;

        if (StringUtils.isBlank(orderBy)) {
			colSort = "occurrenceDateTime";
			order = Order.DESC;
		}

		Page<CaseNote> caseNotePage = caseNoteRepository.getCaseNotes(
				bookingId,
				query,
				from,
				to,
				colSort,
				order,
				offset,
				limit);

		List<CaseNote> transformedCaseNotes =
				caseNotePage.getItems().stream().map(transformer::transform).collect(Collectors.toList());

		return new Page<>(transformedCaseNotes, caseNotePage.getTotalRecords(), caseNotePage.getPageOffset(), caseNotePage.getPageLimit());
	}

	@Override
	@Transactional(readOnly = true)
	public CaseNote getCaseNote(final long bookingId, final long caseNoteId) {
        bookingService.verifyBookingAccess(bookingId);

		CaseNote caseNote = caseNoteRepository.getCaseNote(bookingId, caseNoteId).orElseThrow(EntityNotFoundException.withId(caseNoteId));

		return transformer.transform(caseNote);
	}

	@Override
    public CaseNote createCaseNote(final long bookingId, @Valid @ReferenceCodesValid final NewCaseNote caseNote) {
        bookingService.verifyBookingAccess(bookingId);

		//TODO: First - check Booking Id Sealed status. If status is not sealed then allow to add Case Note.
        Long caseNoteId = caseNoteRepository.createCaseNote(bookingId, caseNote, caseNoteSource);

        return getCaseNote(bookingId, caseNoteId);
    }

	@Override
	public CaseNote updateCaseNote(final long bookingId, final long caseNoteId, @Valid final String newCaseNoteText) {
        bookingService.verifyBookingAccess(bookingId);

        CaseNote caseNote = caseNoteRepository.getCaseNote(bookingId, caseNoteId).orElseThrow(EntityNotFoundException.withId(caseNoteId));

        String amendedText = format(AMEND_CASE_NOTE_FORMAT,
                caseNote.getText(),
                UserSecurityUtils.getCurrentUsername(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                newCaseNoteText);

        caseNoteRepository.updateCaseNote(bookingId, caseNoteId, amendedText, UserSecurityUtils.getCurrentUsername());

        return getCaseNote(bookingId, caseNoteId);
	}
}
