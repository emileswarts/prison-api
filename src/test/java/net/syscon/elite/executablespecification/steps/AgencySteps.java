package net.syscon.elite.executablespecification.steps;

import net.syscon.elite.api.model.Agency;
import net.syscon.elite.api.model.Location;
import net.syscon.elite.api.model.WhereaboutsConfig;
import net.syscon.elite.api.support.Order;
import net.syscon.elite.api.support.TimeSlot;
import net.syscon.elite.test.EliteClientException;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * BDD step implementations for Agencies service.
 */
public class AgencySteps extends CommonSteps {
    private static final String API_REF_PREFIX = API_PREFIX + "agencies/";
    public static final String API_AGENCY_URL = API_REF_PREFIX + "{agencyId}";
    private static final String API_LOCATIONS_URL = API_REF_PREFIX + "{agencyId}/locations";
    private static final String API_EVENT_LOCATIONS_URL = API_REF_PREFIX + "{agencyId}/eventLocations";
    private static final String API_BOOKED_EVENT_LOCATIONS_URL = API_REF_PREFIX + "{agencyId}/eventLocationsBooked";
    private static final String API_CASELOAD_URL = API_REF_PREFIX + "caseload/{caseload}";
    private static final String WHEREABOUTS_API_URL = API_REF_PREFIX + "{agencyId}/locations/whereabouts";
    private List<Agency> agencies;
    private Agency agency;
    private List<Location> locations;
    private WhereaboutsConfig whereaboutsConfig;

    private void dispatchPagedListRequest(final String resourcePath, final Long offset, final Long limit, final Object... params) {
        init();

        final HttpEntity<?> httpEntity;

        if (Objects.nonNull(offset) && Objects.nonNull(limit)) {
            applyPagination(offset, limit);
            httpEntity = createEntity(null, addPaginationHeaders());
        } else {
            httpEntity = createEntity();
        }

        final var url = resourcePath;

        try {
            final var response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<Agency>>() {},
                    params);

            agencies = response.getBody();

            buildResourceData(response);
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    private void dispatchListRequest(final String resourcePath, final String agencyId, final String eventType, final Map<String, String> headers) {
        init();

        final var urlModifier = StringUtils.isBlank(eventType) ? "" : "?eventType=" + eventType;
        final var url = resourcePath + urlModifier;

        try {
            final var response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createEntity(null, headers),
                    new ParameterizedTypeReference<List<Location>>() {},
                    agencyId);

            locations = response.getBody();

            buildResourceData(response);
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    private void dispatchBookedLocationsRequest(final String resourcePath, final String agencyId, final String bookedOnDay, final TimeSlot timeSlot) {
        init();

        var urlModifier = "?bookedOnDay=" + bookedOnDay;
        if (timeSlot != null) {
            urlModifier += "&timeSlot=" + timeSlot.name();
        }
        final var url = resourcePath + urlModifier;

        try {
            final var response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createEntity(),
                    new ParameterizedTypeReference<List<Location>>() {},
                    agencyId);

            locations = response.getBody();

            buildResourceData(response);
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    private void dispatchObjectRequest(final String resourcePath, final String agencyId) {
        init();

        final var urlModifier = "";

        final var url = resourcePath + urlModifier;

        try {
            final var response = restTemplate.exchange(url, HttpMethod.GET, createEntity(),
                    new ParameterizedTypeReference<Agency>() {
                    }, agencyId);

            agency = response.getBody();
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    private void dispatchObjectRequestForCaseload(final String resourcePath, final String caseload) {
        init();
        try {
            final var response = restTemplate.exchange(resourcePath, HttpMethod.GET, createEntity(),
                    new ParameterizedTypeReference<List<Agency>>() {}, caseload);

            agencies = response.getBody();
            buildResourceData(response);
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    @Override
    protected void init() {
        super.init();
        agency = null;
        agencies = null;
        locations = null;
    }

    @Step("Submit request for all agencies")
    public void getAllAgencies() {
        dispatchPagedListRequest(API_REF_PREFIX, 0L, 1000L);
    }

    @Step("Submit request for agency locations")
    public void getLocations(final String agencyId, final String eventType, final String sortFields, final Order sortOrder) {
        final var headers = buildSortHeaders(sortFields, sortOrder);

        dispatchListRequest(API_LOCATIONS_URL, agencyId, eventType, headers);
    }

    @Step("Submit request for any event locations")
    public void getLocationsForAnyEvents(final String agencyId) {
        dispatchListRequest(API_EVENT_LOCATIONS_URL, agencyId, null, null);
    }

    @Step("Submit request for booked agency locations")
    public void getBookedLocations(final String agencyId, final String bookedOnDay, final TimeSlot timeSlot) {
        dispatchBookedLocationsRequest(API_BOOKED_EVENT_LOCATIONS_URL, agencyId, bookedOnDay, timeSlot);
    }

    public void verifyAgencyList(final List<Agency> expected) {

        final var expectedIterator = expected.iterator();
        final var actualIterator = agencies.iterator();
        while (expectedIterator.hasNext()) {
            final var expectedThis = expectedIterator.next();
            final var actualThis = actualIterator.next();
            assertEquals(expectedThis.getAgencyId(), actualThis.getAgencyId());
            assertEquals(expectedThis.getAgencyType(), actualThis.getAgencyType());
            assertEquals(expectedThis.getDescription(), actualThis.getDescription());
        }
        assertFalse("Too many actual events", actualIterator.hasNext());
    }

    @Step("Verify agency property")
    public void verifyField(final String field, final String value) throws ReflectiveOperationException {
        super.verifyField(agency, field, value);
    }

    @Step("Submit request for agency details")
    public void getAgency(final String agencyId) {
        dispatchObjectRequest(API_AGENCY_URL, agencyId);
    }

    public void verifyLocationList(final List<Location> expected) {
        final var expectedIterator = expected.iterator();
        final var actualIterator = locations.iterator();
        while (expectedIterator.hasNext()) {
            final var expectedThis = expectedIterator.next();
            final var actualThis = actualIterator.next();
            assertEquals(expectedThis.getLocationId(), actualThis.getLocationId());
            assertEquals(expectedThis.getLocationPrefix(), actualThis.getLocationPrefix());
            assertEquals(expectedThis.getDescription(), actualThis.getDescription());
            assertEquals(expectedThis.getUserDescription(), actualThis.getUserDescription());
        }
        assertFalse("Too many actual events", actualIterator.hasNext());
    }

    public void getAgenciesByCaseload(final String caseload) {
        dispatchObjectRequestForCaseload(API_CASELOAD_URL, caseload);
    }

    public void aRequestIsMadeToGetWhereabouts(final String agencyId) {
        dispatchWhereaboutsCall(WHEREABOUTS_API_URL, agencyId);
    }

    private void dispatchWhereaboutsCall(final String url, final String agencyId) {
        init();
        try {
            final var response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    createEntity(),
                    new ParameterizedTypeReference<WhereaboutsConfig>() {
                    }, agencyId);
            whereaboutsConfig = response.getBody();
        } catch (final EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    @Step("Verify whereabouts property")
    public void verifyWhereaboutsField(final String field, final String value) throws ReflectiveOperationException {
        super.verifyField(whereaboutsConfig, field, value);
    }
}
