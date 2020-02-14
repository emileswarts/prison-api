package net.syscon.elite.service.curfews;

import lombok.val;
import net.syscon.elite.api.model.*;
import net.syscon.elite.repository.OffenderCurfewRepository;
import net.syscon.elite.service.*;
import net.syscon.elite.service.support.OffenderCurfew;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@Validated
public class OffenderCurfewService {

    private static final String HDC_APPROVE_DOMAIN = "HDC_APPROVE";
    private static final String HDC_REJECT_REASON_DOMAIN = "HDC_REJ_RSN";
    /**
     * Comparator for sorting OffenderSentenceCalc instances by HDCED (HomeDetentionCurfewEligibilityDate). Nulls sort high.
     */
    static final Comparator<OffenderSentenceCalc<? extends BaseSentenceDetail>> OSC_BY_HDCED_COMPARATOR =
            comparing(
                    OffenderSentenceCalc::getSentenceDetail,
                    comparing(bsd -> bsd == null ? null : bsd.getHomeDetentionCurfewEligibilityDate(),
                            nullsLast(naturalOrder())
                    )
            );

    /**
     * A Comparator to be used to find the current OffenderCurfew for a particular OffenderBookId.
     * Compares by assessmentDate (nulls sort high), and then by offenderCurfewId.  This is meant to be used to select
     * a single OffenderCurfew instance from a set whose members all have the same offenderBookId.
     */
    static final Comparator<OffenderCurfew> OFFENDER_CURFEW_COMPARATOR =
            comparing(OffenderCurfew::getAssessmentDate, nullsLast(naturalOrder()))
                    .thenComparing(OffenderCurfew::getOffenderCurfewId);

    private final OffenderCurfewRepository offenderCurfewRepository;
    private final CaseloadToAgencyMappingService caseloadToAgencyMappingService;
    private final BookingService bookingService;
    private final ReferenceDomainService referenceDomainService;

    public OffenderCurfewService(
            final OffenderCurfewRepository offenderCurfewRepository,
            final CaseloadToAgencyMappingService caseloadToAgencyMappingService,
            final BookingService bookingService,
            final ReferenceDomainService referenceDomainService) {
        this.offenderCurfewRepository = offenderCurfewRepository;
        this.caseloadToAgencyMappingService = caseloadToAgencyMappingService;
        this.bookingService = bookingService;
        this.referenceDomainService = referenceDomainService;
    }

    public List<OffenderSentenceCalc> getHomeDetentionCurfewCandidates(final String username) {

        final var agencyIds = agencyIdsFor(username);

        final var offenderBookingIdsForNewHDCProcess =
                curfewBookingIds(
                        currentOffenderCurfews(
                                offenderCurfewRepository.offenderCurfews(agencyIds)));

        return bookingService.getOffenderSentenceCalculationsForAgency(agencyIds)
                .stream()
                .filter(offenderSentenceCalculation -> (offenderSentenceCalculation.getHomeDetCurfEligibilityDate() != null) &&
                        offenderBookingIdsForNewHDCProcess.contains(offenderSentenceCalculation.getBookingId()))
                .map(os -> OffenderSentenceCalc.builder()
                        .bookingId(os.getBookingId())
                        .offenderNo(os.getOffenderNo())
                        .firstName(os.getFirstName())
                        .lastName(os.getLastName())
                        .agencyLocationId(os.getAgencyLocationId())
                        .sentenceDetail(BaseSentenceDetail.builder()
                                .sentenceExpiryDate(os.getSentenceExpiryDate())
                                .homeDetentionCurfewEligibilityDate(os.getHomeDetCurfEligibilityDate())
                                .homeDetentionCurfewActualDate(os.getHomeDetCurfActualDate())
                                .automaticReleaseDate(os.getAutomaticReleaseDate())
                                .conditionalReleaseDate(os.getConditionalReleaseDate())
                                .nonParoleDate(os.getNonParoleDate())
                                .postRecallReleaseDate(os.getPostRecallReleaseDate())
                                .licenceExpiryDate(os.getLicenceExpiryDate())
                                .paroleEligibilityDate(os.getParoleEligibilityDate())
                                .actualParoleDate(os.getActualParolDate())
                                .releaseOnTemporaryLicenceDate(os.getRotl())
                                .earlyRemovalSchemeEligibilityDate(os.getErsed())
                                .earlyTermDate(os.getEarlyTermDate())
                                .midTermDate(os.getMidTermDate())
                                .lateTermDate(os.getLateTermDate())
                                .topupSupervisionExpiryDate(os.getTopupSupervisionExpiryDate())
                                .tariffDate(os.getTariffDate())
                                .build())
                        .build())
                .sorted(OSC_BY_HDCED_COMPARATOR)
                .collect(Collectors.toList());
    }

    private Set<Long> curfewBookingIds(Stream<OffenderCurfew> ocs) {
        return ocs.map(OffenderCurfew::getOffenderBookId).collect(toSet());
    }

    @PreAuthorize("hasRole('SYSTEM_USER')")
    public HomeDetentionCurfew getLatestHomeDetentionCurfew(final long bookingId) {
        return offenderCurfewRepository.getLatestHomeDetentionCurfew(bookingId, StatusTrackingCodes.REFUSED_REASON_CODES)
                .orElseThrow(() -> new EntityNotFoundException("No 'latest' Home Detention Curfew found for bookingId " + bookingId));
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('write') && hasRole('SYSTEM_USER')")
    public void setHdcChecks(final long bookingId, @Valid final HdcChecks hdcChecks) {
        withCurrentCurfewState(bookingId).setHdcChecks(hdcChecks);
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('write') && hasRole('SYSTEM_USER')")
    public void deleteHdcChecks(Long bookingId) {
        withCurrentCurfewState(bookingId).deleteHdcChecks();
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('write') && hasRole('SYSTEM_USER')")
    public void setApprovalStatus(final long bookingId, @Valid final ApprovalStatus approvalStatus) {

        if (!referenceDomainService.isReferenceCodeActive(HDC_APPROVE_DOMAIN, approvalStatus.getApprovalStatus())) {
            throw new IllegalArgumentException(String.format("Approval status code '%1$s' is not a valid NOMIS value.", approvalStatus.getApprovalStatus()));
        }
        final var refusedReason = approvalStatus.getRefusedReason();
        if (refusedReason != null) {
            if (!referenceDomainService.isReferenceCodeActive(HDC_REJECT_REASON_DOMAIN, refusedReason)) {
                throw new IllegalArgumentException(String.format("Refused reason code '%1$s' is not a valid NOMIS value.", refusedReason));
            }
        }

        withCurrentCurfewState(bookingId).setApprovalStatus(approvalStatus);
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('write') && hasRole('SYSTEM_USER')")
    public void deleteApprovalStatus(Long bookingId) {
        withCurrentCurfewState(bookingId).deleteApprovalStatus();
    }

    private CurfewState withCurrentCurfewState(long bookingId) {
        val currentCurfew = getLatestHomeDetentionCurfew(bookingId);

        return CurfewState
                .getState(currentCurfew)
                .with(new CurfewActions(currentCurfew.getId(), offenderCurfewRepository));
    }


    private Set<String> agencyIdsFor(final String username) {
        return caseloadToAgencyMappingService.agenciesForUsersWorkingCaseload(username)
                .stream()
                .map(Agency::getAgencyId)
                .collect(toSet());
    }

    /**
     * Given a Collection of OffenderCurfew where there may be more than one per offenderBookId, select, for each
     * offenderBookId the 'current' OffenderCurfew.  This is the instance that sorts highest by OFFENDER_CURFEW_COMPARATOR.
     *
     * @param curfews The curfews to sift
     * @return The current curfew for each offenderBookId
     */
    static Stream<OffenderCurfew> currentOffenderCurfews(final Collection<OffenderCurfew> curfews) {

        final var currentByOffenderBookdId = curfews
                .stream()
                .collect(
                        groupingBy(
                                OffenderCurfew::getOffenderBookId,
                                maxBy(OFFENDER_CURFEW_COMPARATOR)
                        ));

        return currentByOffenderBookdId
                .values()
                .stream()
                .map(opt -> opt.orElseThrow(() -> new NullPointerException("Impossible")));
    }
}