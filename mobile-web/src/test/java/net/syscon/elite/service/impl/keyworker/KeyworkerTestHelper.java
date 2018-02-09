package net.syscon.elite.service.impl.keyworker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils;
import net.syscon.elite.api.model.Keyworker;
import net.syscon.elite.api.model.NewAllocation;
import net.syscon.elite.api.model.OffenderSummary;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.repository.impl.KeyWorkerAllocation;
import net.syscon.elite.service.KeyWorkerAllocationService;
import net.syscon.elite.service.keyworker.AllocationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class KeyworkerTestHelper {
    public static final int CAPACITY_TIER_1 = 6;
    public static final int CAPACITY_TIER_2 = 9;
    public static final int FULLY_ALLOCATED = CAPACITY_TIER_2;

    // Initialises mock logging appender
    public static void initMockLogging(Appender mockAppender) {
        // Set-up mock appender to enable verification of log output
        ch.qos.logback.classic.Logger elite = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("net.syscon.elite");

        when(mockAppender.getName()).thenReturn("MOCK");
        elite.addAppender(mockAppender);
    }

    public static void verifyLog(Appender mockAppender, Level level, String message, Object... args) {
        verify(mockAppender, atLeastOnce()).doAppend(argThat(new ArgumentMatcher() {

            @Override
            public boolean matches(final Object argument) {
                LoggingEvent loggingEvent = (LoggingEvent) argument;

                // Ignore debug logging
                if (!StringUtils.equals(Level.DEBUG.toString(), loggingEvent.getLevel().toString())) {
                    assertThat(loggingEvent.toString()).isEqualTo(formatLogMessage(level, message, args));
                }

                return true;
            }
        }));
    }

    public static void verifyException(Throwable thrown, Class<? extends Throwable> expectedException, String expectedMessage) {
        assertThat(thrown).isInstanceOf(expectedException).hasMessage(expectedMessage);
    }

    private static String formatLogMessage(Level level, String message, Object... args) {
        LoggingEvent loggingEvent = new LoggingEvent();

        loggingEvent.setLevel(level);
        loggingEvent.setMessage(message);
        loggingEvent.setArgumentArray(args);

        return loggingEvent.toString();
    }

    // Provides a Key worker with specified staff id and number of allocations
    public static Keyworker getKeyworker(long staffId, int numberOfAllocations) {
        return Keyworker.builder()
                .staffId(staffId)
                .numberAllocated(numberOfAllocations)
                .firstName(RandomStringUtils.random(35))
                .lastName(RandomStringUtils.random(35))
                .build();
    }

    // Provides list of Key workers with varying number of allocations (within specified range)
    public static List<Keyworker> getKeyworkers(long total, int minAllocations, int maxAllocations) {
        List<Keyworker> keyworkers = new ArrayList<>();

        for (long i = 1; i <= total; i++) {
            keyworkers.add(Keyworker.builder()
                    .staffId(i)
                    .numberAllocated(RandomUtils.nextInt(minAllocations, maxAllocations + 1))
                    .build());
        }

        return keyworkers;
    }

    public static OffenderSummary getOffender(long bookingId, String agencyId) {
        return OffenderSummary.builder()
                .bookingId(bookingId)
                .agencyLocationId(agencyId)
                .build();
    }

    public static void verifyAutoAllocation(NewAllocation newAlloc, long bookingId, long staffId) {
        assertThat(newAlloc.getBookingId()).isEqualTo(bookingId);
        assertThat(newAlloc.getStaffId()).isEqualTo(staffId);
        assertThat(newAlloc.getType()).isEqualTo(AllocationType.AUTO.getIndicator());
        assertThat(newAlloc.getReason()).isEqualTo(AllocationService.ALLOCATION_REASON_AUTO);
    }

    public static void verifyManualAllocation(NewAllocation newAlloc, long bookingId, long staffId) {
        assertThat(newAlloc.getBookingId()).isEqualTo(bookingId);
        assertThat(newAlloc.getStaffId()).isEqualTo(staffId);
        assertThat(newAlloc.getType()).isEqualTo(AllocationType.MANUAL.getIndicator());
        assertThat(newAlloc.getReason()).isEqualTo(AllocationService.ALLOCATION_REASON_MANUAL);
    }

    public static void mockPrisonerAllocationHistory(KeyWorkerAllocationService keyWorkerAllocationService,
                                                     KeyWorkerAllocation... allocations) {
        List<KeyWorkerAllocation> allocationHistory =
                (allocations == null) ? Collections.emptyList() : Arrays.asList(allocations);

        when(keyWorkerAllocationService
                .getAllocationHistoryForPrisoner(anyLong(), anyString(), any(Order.class)))
                .thenReturn(allocationHistory);
    }

    public static KeyworkerPool initKeyworkerPool(KeyWorkerAllocationService keyWorkerAllocationService,
                                                  Collection<Keyworker> keyworkers, Collection<Integer> capacityTiers) {
        KeyworkerPool keyworkerPool = new KeyworkerPool(keyworkers, capacityTiers);

        keyworkerPool.setKeyWorkerAllocationService(keyWorkerAllocationService);

        return keyworkerPool;
    }

    // Provides a previous Key worker allocation between specified offender and Key worker with an assigned datetime 7
    // days prior to now.
    public static KeyWorkerAllocation getPreviousKeyworkerAllocation(long bookingId, long staffId) {
        return getPreviousKeyworkerAllocation(bookingId, staffId, LocalDateTime.now().minusDays(7));
    }

    // Provides a previous Key worker allocation between specified offender and Key worker, assigned at specified datetime.
    public static KeyWorkerAllocation getPreviousKeyworkerAllocation(long bookingId, long staffId, LocalDateTime assigned) {
        Validate.notNull(assigned, "Allocation must have assigned datetime.");

        return KeyWorkerAllocation.builder()
                .bookingId(bookingId)
                .staffId(staffId)
                .assigned(assigned)
                .type(AllocationType.AUTO.getIndicator())
                .build();
    }
}
