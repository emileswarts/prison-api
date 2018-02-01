package net.syscon.elite.service.impl;

import net.syscon.elite.api.model.KeyWorkerAllocationDetail;
import net.syscon.elite.api.model.Keyworker;
import net.syscon.elite.api.model.NewAllocation;
import net.syscon.elite.api.model.OffenderSummary;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.repository.KeyWorkerAllocationRepository;
import net.syscon.elite.repository.impl.KeyWorkerAllocation;
import net.syscon.elite.security.AuthenticationFacade;
import net.syscon.elite.security.VerifyAgencyAccess;
import net.syscon.elite.security.VerifyBookingAccess;
import net.syscon.elite.service.AllocationException;
import net.syscon.elite.service.BookingService;
import net.syscon.elite.service.EntityNotFoundException;
import net.syscon.elite.service.KeyWorkerAllocationService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@Validated
public class KeyWorkerAllocationServiceImpl implements KeyWorkerAllocationService{

    private final KeyWorkerAllocationRepository repository;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;

    public KeyWorkerAllocationServiceImpl(KeyWorkerAllocationRepository repository,
            AuthenticationFacade authenticationFacade, BookingService bookingService) {
        this.repository = repository;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
    }

    @Override
    @Transactional
    public void createAllocation(KeyWorkerAllocation allocation, String username) {
        repository.getCurrentAllocationForOffenderBooking(allocation.getBookingId())
                .orElseThrow(AllocationException.withMessage(String.format("Existing allocation found for offenderBookingId %s", allocation.getBookingId())));
        repository.createAllocation(allocation, username);
    }

    @Override
    @Transactional
    public void deactivateAllocationForKeyWorker(Long staffId, String reason, String username) {
        repository.deactivateAllocationsForKeyWorker(staffId, reason, username);
    }

    @Override
    @Transactional
    @VerifyBookingAccess
    public void deactivateAllocationForOffenderBooking(Long bookingId, String reason, String username) {
        repository.deactivateAllocationForOffenderBooking(bookingId, reason, username);
    }

    @Override
    @Transactional
    public void allocate(@Valid NewAllocation newAllocation) {

        final Long bookingId = newAllocation.getBookingId();
        final String agencyId = bookingService.getBookingAgency(bookingId);
        if (!bookingService.isSystemUser()) {
            bookingService.verifyBookingAccess(bookingId);
        }
        validateStaffId(bookingId, newAllocation.getStaffId());
        final String username = authenticationFacade.getCurrentUsername();

        // Remove current allocation if any
        deactivateAllocationForOffenderBooking(bookingId, "OVERRIDDEN", username);

        KeyWorkerAllocation allocation = KeyWorkerAllocation.builder()//
                .bookingId(bookingId)//
                .staffId(newAllocation.getStaffId())//
                .agencyId(agencyId)//
                .reason(newAllocation.getReason())//
                .active("Y")//
                .assigned(LocalDateTime.now())//
                .type(newAllocation.getType())//
                .build();

        repository.createAllocation(allocation, username);
    }

    /**
     * Check that staffId denotes a keyworker at the offender's prison
     * @param bookingId
     * @param staffId
     */
    private void validateStaffId(Long bookingId, Long staffId) {
        repository.checkAvailableKeyworker(bookingId, staffId);
    }

    @Override
    @VerifyBookingAccess
    public List<KeyWorkerAllocation> getAllocationHistoryForPrisoner(Long bookingId, String orderByFields, Order order) {

        String sortFields = StringUtils.defaultString(orderByFields, "assigned");
        Order sortOrder = ObjectUtils.defaultIfNull(order, Order.DESC);
        return repository.getAllocationHistoryForPrisoner(bookingId, sortFields, sortOrder);
    }

    @Override
    @VerifyBookingAccess
    public KeyWorkerAllocation getCurrentAllocationForOffenderBooking(Long bookingId) {
        KeyWorkerAllocation keyWorkerAllocation = repository.getCurrentAllocationForOffenderBooking(bookingId)
                .orElseThrow(EntityNotFoundException.withMessage(String.format("Active allocation not found for offenderBookingId %s", bookingId)));
        return keyWorkerAllocation;
    }

    @Override
    @VerifyBookingAccess
    public KeyWorkerAllocation getLatestAllocationForOffenderBooking(Long bookingId) {
        KeyWorkerAllocation keyWorkerAllocation = repository.getLatestAllocationForOffenderBooking(bookingId)
                .orElseThrow(EntityNotFoundException.withMessage(String.format("Allocation not found for offenderBookingId %s", bookingId)));
        return keyWorkerAllocation;
    }

    @Override
    @VerifyAgencyAccess
    public Page<OffenderSummary> getUnallocatedOffenders(String agencyId, Long pageOffset, Long pageLimit, String sortFields, Order sortOrder) {
        String sortFieldsDefaulted = StringUtils.defaultString(sortFields, "lastName");
        Order sortOrderDefaulted = ObjectUtils.defaultIfNull(sortOrder, Order.ASC);
        Long pageOffsetDefaulted = ObjectUtils.defaultIfNull(pageOffset, 0L);
        Long pageLimitDefaulted = ObjectUtils.defaultIfNull(pageLimit, 10L);

        return repository.getUnallocatedOffenders(Collections.singleton(agencyId), pageOffsetDefaulted, pageLimitDefaulted, sortFieldsDefaulted, sortOrderDefaulted);
    }

    @Override
    @VerifyAgencyAccess
    public Page<KeyWorkerAllocationDetail> getAllocations(String agencyId, LocalDate fromDate, LocalDate toDate, String allocationType, Long pageOffset, Long pageLimit, String sortFields, Order sortOrder) {
        validateAllocationsRequestDateRange(fromDate, toDate);

        String sortFieldsDefaulted = StringUtils.defaultString(sortFields, "lastName,firstName");
        Order sortOrderDefaulted = ObjectUtils.defaultIfNull(sortOrder, Order.ASC);
        Long pageOffsetDefaulted = ObjectUtils.defaultIfNull(pageOffset, 0L);
        Long pageLimitDefaulted = ObjectUtils.defaultIfNull(pageLimit, 10L);

        return repository.getAllocatedOffenders(Collections.singleton(agencyId), fromDate, toDate, allocationType, pageOffsetDefaulted, pageLimitDefaulted,  sortFieldsDefaulted, sortOrderDefaulted);
    }

    @Override
    @VerifyAgencyAccess
    public List<Keyworker> getAvailableKeyworkers(String agencyId) {
        return repository.getAvailableKeyworkers(agencyId);
    }

    private void validateAllocationsRequestDateRange(LocalDate fromDate, LocalDate toDate) {
        // Validate date range
        if (Objects.nonNull(toDate) && toDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Invalid date range: toDate cannot be in the future.");
        }
        if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && toDate.isBefore(fromDate)) {
            throw new BadRequestException("Invalid date range: toDate is before fromDate.");
        }
    }
}
