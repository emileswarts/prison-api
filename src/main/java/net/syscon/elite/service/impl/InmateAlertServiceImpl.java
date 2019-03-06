package net.syscon.elite.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.syscon.elite.api.model.Alert;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.Page;
import net.syscon.elite.repository.InmateAlertRepository;
import net.syscon.elite.security.VerifyAgencyAccess;
import net.syscon.elite.security.VerifyBookingAccess;
import net.syscon.elite.service.EntityNotFoundException;
import net.syscon.elite.service.InmateAlertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class InmateAlertServiceImpl implements InmateAlertService {

    private final InmateAlertRepository inmateAlertRepository;

    @Autowired
    public InmateAlertServiceImpl(final InmateAlertRepository inmateAlertRepository) {
        this.inmateAlertRepository = inmateAlertRepository;
    }

    @Override
    @VerifyBookingAccess
    public Page<Alert> getInmateAlerts(final Long bookingId, final String query, final String orderBy, final Order order, final long offset, final long limit) {
        final var orderByBlank = StringUtils.isBlank(orderBy);

        final var alerts = inmateAlertRepository.getInmateAlerts(//
                bookingId, query, //
                orderByBlank ? "dateExpires,dateCreated" : orderBy, //
                orderByBlank ? Order.DESC : order, //
                offset, limit);

        alerts.getItems().forEach(alert -> alert.setExpired(isExpiredAlert(alert)));

        log.info("Returning {} of {} matching Alerts starting at {} for bookingId {}", alerts.getItems().size(), alerts.getTotalRecords(), alerts.getPageOffset(), bookingId);
        return alerts;
    }

    private boolean isExpiredAlert(final Alert alert) {
        var expiredAlert = false;

        if (alert.getDateExpires() != null) {
            expiredAlert = alert.getDateExpires().compareTo(LocalDate.now()) <= 0;
        }

        return expiredAlert;
    }

    @Override
    @VerifyBookingAccess
    public Alert getInmateAlert(final Long bookingId, final Long alertSeqId) {
        final var alert = inmateAlertRepository.getInmateAlerts(bookingId, alertSeqId)
                .orElseThrow(EntityNotFoundException.withId(alertSeqId));

        alert.setExpired(isExpiredAlert(alert));

        log.info("Returning Alert having alertSeqId {}, for bookingId {}", alertSeqId, bookingId);
        return alert;
    }

    @Override
    @VerifyAgencyAccess(overrideRoles = {"SYSTEM_READ_ONLY", "SYSTEM_USER"})
    public List<Alert> getInmateAlertsByOffenderNosAtAgency(final String agencyId, final List<String> offenderNos) {

        final var alerts = inmateAlertRepository.getInmateAlertsByOffenderNos(agencyId, offenderNos);
        alerts.forEach(alert -> alert.setExpired(isExpiredAlert(alert)));

        log.info("Returning {} matching Alerts for Offender Numbers {} in Agency '{}'", alerts.size(), offenderNos, agencyId);
        return alerts;
    }

    @Override
    @PreAuthorize("hasAnyRole('SYSTEM_READ_ONLY', 'SYSTEM_USER')")
    public List<Alert> getInmateAlertsByOffenderNos(final List<String> offenderNos) {

        final var alerts = inmateAlertRepository.getInmateAlertsByOffenderNos(null, offenderNos);
        alerts.forEach(alert -> alert.setExpired(isExpiredAlert(alert)));

        log.info("Returning {} matching Alerts for Offender Numbers {}", alerts.size(), offenderNos);
        return alerts;
    }
}
