package net.syscon.elite.executablespecification.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import net.syscon.elite.api.model.Alert;
import net.syscon.elite.test.EliteClientException;
import net.thucydides.core.annotations.Step;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * BDD step implementations for Booking alias feature.
 */
public class BookingAlertSteps extends CommonSteps {
    private static final String API_REQUEST_BASE_URL = API_PREFIX + "bookings/{bookingId}/alerts";
    private static final String API_REQUEST_ALERT_URL = API_PREFIX + "bookings/{bookingId}/alerts/{alertId}";

    private List<Alert> alerts;
    private Alert alert;

    @Step("Retrieve alerts for offender booking")
    public void getAlerts(Long bookingId) {
        doListApiCall(bookingId, null);
    }

    public void verifyNumber(int number) {
        assertEquals(number, alerts.size());
    }

    @Step("Verify returned offender alias first names")
    public void verifyCodeList(String codes) {
        verifyPropertyValues(alerts, Alert::getAlertCode, codes);
    }

    private void doListApiCall(Long bookingId, String query) {
        init();
        String requestUrl = API_REQUEST_BASE_URL + StringUtils.trimToEmpty(query);
        try {
            ResponseEntity<List<Alert>> response = restTemplate.exchange(requestUrl, HttpMethod.GET, createEntity(),
                    new ParameterizedTypeReference<List<Alert>>() {
                    }, bookingId.toString());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            alerts = response.getBody();
            buildResourceData(response);
        } catch (EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    private void doSingleResultApiCall(Long bookingId, Long alertId) {
        init();
        try {
            ResponseEntity<Alert> response = restTemplate.exchange(API_REQUEST_ALERT_URL, HttpMethod.GET,
                    createEntity(null, null), new ParameterizedTypeReference<Alert>() {
                    }, bookingId, alertId);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            alert = response.getBody();
        } catch (EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    protected void init() {
        super.init();
        alerts = null;
        alert = null;
    }

    public void getAlert(Long bookingId, Long alertId) {
        doSingleResultApiCall(bookingId, alertId);
    }

    public void verifyAlertField(String field, String value) throws ReflectiveOperationException {
        verifyField(alert, field, value);
    }

    public void getAlertsInDifferentCaseload() {
        doListApiCall(-16L, null);
    }

    public void getAlertInDifferentCaseload() {
        doSingleResultApiCall(-16L, 1L);
    }
}