package net.syscon.elite.executablespecification.steps;

import net.syscon.elite.api.model.PrisonContactDetail;
import net.syscon.elite.api.model.Telephone;
import net.syscon.elite.test.EliteClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PrisonContactDetailsSteps extends CommonSteps {
    private static final String PRISON_CONTACT_DETAILS_LIST_URL = API_PREFIX + "agencies/prison";
    private static final String PRISON_CONTACT_DETAILS_URL = API_PREFIX + "agencies/prison/{agencyId}";

    private PrisonContactDetail details;
    private List<PrisonContactDetail> detailsList;

    public void getPrisonContactDetails() {
        doListApiCall();
    }

    public void getPrisonContactDetails(String agencyId) {
        doSingleResultApiCall(agencyId);
    }

    public void verifyAListOfPrisonContactDetailsIsReturned() {
        assertThat(detailsList).extracting("agencyId")
                .containsExactly(
                        "BXI",
                        "BMI"
                );
    }

    private void doListApiCall() {
        init();

        try {
            ResponseEntity<List<PrisonContactDetail>> response =
                    restTemplate.exchange(
                            PRISON_CONTACT_DETAILS_LIST_URL,
                            HttpMethod.GET,
                            createEntity(),
                            new ParameterizedTypeReference<List<PrisonContactDetail>>() {});

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            detailsList = response.getBody();

        } catch (EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

   private void doSingleResultApiCall(String agencyId) {
        init();
        try {
            ResponseEntity<PrisonContactDetail> response = restTemplate.exchange(PRISON_CONTACT_DETAILS_URL, HttpMethod.GET, createEntity(),
                    PrisonContactDetail.class, agencyId);
            details = response.getBody();
        } catch (EliteClientException ex) {
            setErrorResponse(ex.getErrorResponse());
        }
    }

    protected void init() {
        super.init();
        details = null;
        detailsList = null;
    }

    public void verifyPrisonContactDetails() {
        assertThat(details.getAgencyId()).isEqualTo("BMI");
        assertThat(details.getAddressType()).isEqualTo("BUS");
        assertThat(details.getPremise()).isEqualTo("Birmingham HMP");
        assertThat(details.getLocality()).isEqualTo("Ambley");
        assertThat(details.getCity()).isEqualTo("Birmingham");
        assertThat(details.getCountry()).isEqualTo("England");
        assertThat(details.getPostCode()).isEqualTo("BM1 23V");
        assertThat(details.getPhones()).containsExactly(Telephone.builder().number("0114 2345345").ext("345").type("BUS").build());
    }

    public void verifyADummyListOfPrisonContactDetailsIsReturned() {
        assertThat(detailsList).extracting("agencyId")
                .containsExactly(
                        "123"
                );
    }
}
