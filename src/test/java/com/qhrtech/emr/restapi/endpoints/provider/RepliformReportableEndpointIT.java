
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.repliform.DefaultPatientRepliformReportableManager;
import com.qhrtech.emr.accuro.api.repliform.PatientRepliformReportableManager;
import com.qhrtech.emr.accuro.db.repliform.PatientRepliformReportableFixture;
import com.qhrtech.emr.accuro.model.repliform.RepliformData;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.repliform.PatientRepliformReportableDto;
import com.qhrtech.emr.restapi.models.dto.repliform.RepliformDataDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public class RepliformReportableEndpointIT
    extends AbstractEndpointIntegrationTest<RepliformReportableEndpoint> {

  private final PatientRepliformReportableManager patientRepliformReportableManager;
  private final ApiSecurityContext context;

  public RepliformReportableEndpointIT() throws IOException {
    super(new RepliformReportableEndpoint(), RepliformReportableEndpoint.class);
    patientRepliformReportableManager = new DefaultPatientRepliformReportableManager(getDs());
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(PatientRepliformReportableManager.class, patientRepliformReportableManager);
    return servicesMap;
  }

  @Test
  public void testGetPatient() throws Exception {
    // setup random data
    PatientRepliformReportableFixture fixture = new PatientRepliformReportableFixture();
    fixture.setUp(getConnection());
    RepliformData expected = fixture.get();
    int chartItemId = fixture.getPatientFormId();
    PatientRepliformReportableDto expectedDto = new PatientRepliformReportableDto();
    RepliformDataDto repliformDataDto = mapDto(expected, RepliformDataDto.class);

    // test
    PatientRepliformReportableDto actual =
        given()
            .queryParam("chartItemId", chartItemId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patient-repliforms-reportables")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientRepliformReportableDto.class);

    expectedDto.setRepliformDataList(Collections.singletonList(repliformDataDto));
    expectedDto.setPatientFormId(chartItemId);
    expectedDto.setDateOfService(actual.getDateOfService());
    // assertions
    assertEquals(expectedDto, actual);
  }

  @Test
  public void testGetPatientNull() throws Exception {
    // setup random data
    int chartItemId = RandomUtils.nextInt();
    // test
    given()
        .queryParam("chartItemId", chartItemId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patient-repliforms-reportables")
        .then()
        .assertThat()
        .statusCode(200);
  }
}
