package net.syscon.elite.service.impl;

import com.microsoft.applicationinsights.TelemetryClient;
import net.syscon.elite.api.model.*;
import net.syscon.elite.repository.BookingRepository;
import net.syscon.elite.security.AuthenticationFacade;
import net.syscon.elite.service.*;
import net.syscon.elite.service.support.ReferenceDomain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Test cases for {@link BookingServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class  BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AgencyService agencyService;

    @Mock
    private ReferenceDomainService referenceDomainService;

    @Mock
    private LocationService locationService;

    @Mock
    private TelemetryClient telemetryClient;

    @Mock
    private AuthenticationFacade securityUtils;

    private BookingService bookingService;

    private void programMocks(final String appointmentType, final long bookingId, final String agencyId,
                              final long eventId, final String principal, final ScheduledEvent expectedEvent, final Location location,
                              final NewAppointment newAppointment) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(principal, "credentials"));

        Mockito.when(locationService.getLocation(newAppointment.getLocationId())).thenReturn(location);
        Mockito.when(locationService.getUserLocations(principal)).thenReturn(Collections.singletonList(location));

        Mockito.when(referenceDomainService.getReferenceCodeByDomainAndCode(
                ReferenceDomain.INTERNAL_SCHEDULE_REASON.getDomain(), newAppointment.getAppointmentType(), false))
                .thenReturn(Optional.of(ReferenceCode.builder().code(appointmentType).build()));
    
        Mockito.when(bookingRepository.createBookingAppointment(bookingId, newAppointment, agencyId))
                .thenReturn(eventId);

        Mockito.when(bookingRepository.getBookingAppointment(bookingId, eventId)).thenReturn(expectedEvent);
    }

    @Before
    public void init() {
        bookingService = new BookingServiceImpl(
                bookingRepository,
                null,
                agencyService,
                null,
                locationService,
                referenceDomainService,
                null,
                telemetryClient,
                securityUtils,"1",
                10);
    }

    @Test
    public void testCreateBookingAppointment() {

        final var appointmentType = "MEDE";
        final var locationId = -20L;
        final var bookingId = 100L;
        final var agencyId = "LEI";
        final var eventId = -10L;
        final var principal = "ME";
        final var expectedEvent = ScheduledEvent.builder().bookingId(bookingId).build();
        final var location = Location.builder().locationId(locationId).agencyId(agencyId).build();
        final var agency = Agency.builder().agencyId(agencyId).build();

        final var newAppointment = NewAppointment.builder()
                .appointmentType(appointmentType)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .comment("comment")
                .locationId(locationId).build();

        programMocks(appointmentType, bookingId, agencyId, eventId, principal, expectedEvent, location,
                newAppointment);

        final var actualEvent = bookingService.createBookingAppointment(bookingId, principal, newAppointment);

        assertThat(actualEvent).isEqualTo(expectedEvent);
    }

    @Test
    public void testCreateBookingAppointmentInvalidStartTime() {

        final var bookingId = 100L;
        final var principal = "ME";

        final var newAppointment = NewAppointment.builder().startTime(LocalDateTime.now().plusDays(-1))
                .endTime(LocalDateTime.now().plusDays(2)).build();

        try {
            bookingService.createBookingAppointment(bookingId, principal, newAppointment);
            fail("Should have thrown exception");
        } catch (final BadRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Appointment time is in the past.");
        }
    }

    @Test
    public void testCreateBookingAppointmentInvalidEndTime() {

        final var bookingId = 100L;
        final var principal = "ME";

        final var newAppointment = NewAppointment.builder().startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(1)).build();

        try {
            bookingService.createBookingAppointment(bookingId, principal, newAppointment);
            fail("Should have thrown exception");
        } catch (final BadRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Appointment end time is before the start time.");
        }
    }

    @Test
    public void testCreateBookingAppointmentInvalidLocation() {

        final var appointmentType = "MEDE";
        final var locationId = -20L;
        final var bookingId = 100L;
        final var agencyId = "LEI";
        final var eventId = -10L;
        final var principal = "ME";
        final var expectedEvent = ScheduledEvent.builder().bookingId(bookingId).build();
        final var location = Location.builder().locationId(locationId).agencyId(agencyId).build();
        final var agency = Agency.builder().agencyId(agencyId).build();

        final var newAppointment = NewAppointment.builder()
                .appointmentType(appointmentType)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .comment("comment")
                .locationId(locationId).build();

        programMocks(appointmentType, bookingId, agencyId, eventId, principal, expectedEvent, location,
                newAppointment);

        Mockito.when(locationService.getLocation(newAppointment.getLocationId()))
                .thenThrow(new EntityNotFoundException("test"));

        try {
            bookingService.createBookingAppointment(bookingId, principal, newAppointment);
            fail("Should have thrown exception");
        } catch (final BadRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Location does not exist or is not in your caseload.");
        }
    }

    @Test
    public void testCreateBookingAppointmentInvalidAppointmentType() {

        final var appointmentType = "MEDE";
        final var locationId = -20L;
        final var bookingId = 100L;
        final var agencyId = "LEI";
        final var eventId = -10L;
        final var principal = "ME";
        final var expectedEvent = ScheduledEvent.builder().bookingId(bookingId).build();
        final var location = Location.builder().locationId(locationId).agencyId(agencyId).build();
        final var agency = Agency.builder().agencyId(agencyId).build();

        final var newAppointment = NewAppointment.builder().appointmentType(appointmentType)
                .startTime(LocalDateTime.now().plusDays(1)).endTime(LocalDateTime.now().plusDays(2)).comment("comment")
                .locationId(locationId).build();

        programMocks(appointmentType, bookingId, agencyId, eventId, principal, expectedEvent, location,
                newAppointment);

        Mockito.when(referenceDomainService.getReferenceCodeByDomainAndCode(
                ReferenceDomain.INTERNAL_SCHEDULE_REASON.getDomain(), newAppointment.getAppointmentType(), false))
                .thenReturn(Optional.empty());

        try {
            bookingService.createBookingAppointment(bookingId, principal, newAppointment);
            fail("Should have thrown exception");
        } catch (final BadRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Event type not recognised.");
        }
    }
}
