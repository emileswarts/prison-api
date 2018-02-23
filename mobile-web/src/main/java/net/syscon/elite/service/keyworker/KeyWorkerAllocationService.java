package net.syscon.elite.service.keyworker;

import net.syscon.elite.api.model.KeyWorkerAllocationDetail;
import net.syscon.elite.api.model.Keyworker;
import net.syscon.elite.api.model.NewAllocation;
import net.syscon.elite.api.model.OffenderSummary;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.repository.impl.KeyWorkerAllocation;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Key Worker Allocation service interface.
 */
public interface KeyWorkerAllocationService {

    void createAllocation(KeyWorkerAllocation allocation, String username);

    void deactivateAllocationForKeyWorker(Long staffId, String reason, String username);

    void deactivateAllocationForOffenderBooking(Long bookingId, String reason, String username);

    void allocate(@Valid NewAllocation newAllocation);

    List<KeyWorkerAllocation> getAllocationHistoryForPrisoner(Long bookingId, String orderByFields, Order order);

    KeyWorkerAllocation getCurrentAllocationForOffenderBooking(Long bookingId);

    KeyWorkerAllocation getLatestAllocationForOffenderBooking(Long bookingId);

    Page<OffenderSummary> getUnallocatedOffenders(String agencyId, Long pageOffset, Long pageLimit, String sortFields, Order sortOrder);

    Page<KeyWorkerAllocationDetail> getAllocations(String agencyId, LocalDate fromDate, LocalDate toDate, String allocationType, Long pageOffset, Long pageLimit, String sortFields, Order sortOrder);

    List<Keyworker> getAvailableKeyworkers(String agencyId);

    Keyworker getKeyworkerDetails(Long staffId);

    List<KeyWorkerAllocation> getAllocationsForKeyworker(Long staffId);
}