
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.repliform.PatientRepliformReportableManager;
import com.qhrtech.emr.accuro.model.repliform.PatientRepliformReportable;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.repliform.PatientRepliformReportableDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public class RepliformReportableEndpointTest
    extends AbstractEndpointTest<RepliformReportableEndpoint> {

  private final PatientRepliformReportableManager patientRepliformReportableManager;
  private final AuditLogUser user;
  private final ApiSecurityContext context;

  public RepliformReportableEndpointTest() {
    super(new RepliformReportableEndpoint(), RepliformReportableEndpoint.class);
    patientRepliformReportableManager = mock(PatientRepliformReportableManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
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
    PatientRepliformReportable expected = getFixture(PatientRepliformReportable.class);
    int chartItemId = RandomUtils.nextInt();
    expected.setPatientFormId(chartItemId);
    // mock dependencies
    when(patientRepliformReportableManager.getByPatientFormId(chartItemId)).thenReturn(expected);
    PatientRepliformReportableDto expectedDto =
        mapDto(expected, PatientRepliformReportableDto.class);

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
    // assertions
    assertEquals(expectedDto, actual);
    verify(patientRepliformReportableManager).getByPatientFormId(chartItemId);
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
        .statusCode(404);
    verify(patientRepliformReportableManager).getByPatientFormId(chartItemId);
  }

  @Test
  public void testGetPatientNullException() throws Exception {
    // setup random data
    Integer chartItemId = null;
    // test
    given()
        .queryParam("chartItemId", chartItemId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patient-repliforms-reportables")
        .then()
        .assertThat()
        .statusCode(400);
  }
}
