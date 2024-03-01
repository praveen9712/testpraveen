
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.immunization.PatientImmunizationManager;
import com.qhrtech.emr.accuro.model.immunization.PatientImmunization;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientImmunizationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class PatientImmunizationEndpointTest
    extends AbstractEndpointTest<PatientImmunizationEndpoint> {

  private final PatientImmunizationManager patientImmunizationManager;

  public PatientImmunizationEndpointTest() {
    super(new PatientImmunizationEndpoint(), PatientImmunizationEndpoint.class);
    patientImmunizationManager = mock(PatientImmunizationManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(PatientImmunizationManager.class, patientImmunizationManager);
    return servicesMap;
  }

  @Test
  public void testGetAllForPatient() throws Exception {
    // setup random data
    Set<PatientImmunization> protossResults =
        getFixtures(PatientImmunization.class, HashSet::new, 5);
    // years and months will always be numeric
    protossResults
        .forEach(r -> r.getImmunizationAge().setYears(String.valueOf(TestUtilities.nextInt())));
    protossResults
        .forEach(r -> r.getImmunizationAge().setMonths(String.valueOf(TestUtilities.nextInt())));

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientImmunizationManager.getAll(patientId)).thenReturn(protossResults);

    Set<PatientImmunizationDto> expected =
        mapDto(protossResults, PatientImmunizationDto.class, HashSet::new);

    // test
    Set<PatientImmunizationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/immunizations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientImmunizationDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(patientImmunizationManager).getAll(patientId);
  }

  @Test
  public void testGetOne() throws Exception {
    // setup random data
    PatientImmunization protossResult = getFixture(PatientImmunization.class);
    // years and months will always be numeric
    protossResult.getImmunizationAge().setMonths(String.valueOf(TestUtilities.nextInt()));
    protossResult.getImmunizationAge().setYears(String.valueOf(TestUtilities.nextInt()));
    int patientId = protossResult.getPatientId();
    int immunizationId = protossResult.getAdminVaccineUniqueId();

    // mock dependencies
    when(patientImmunizationManager.get(immunizationId)).thenReturn(protossResult);

    PatientImmunizationDto expected = mapDto(protossResult, PatientImmunizationDto.class);

    // test
    PatientImmunizationDto actual = given()
        .pathParam("patientId", patientId)
        .pathParam("immunizationId", immunizationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/immunizations/{immunizationId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(PatientImmunizationDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(patientImmunizationManager).get(immunizationId);
  }

  @Test
  public void testGetOneNotFound() throws Exception {
    // setup random data
    int patientId = TestUtilities.nextId();
    int immunizationId = TestUtilities.nextId();

    // mock dependencies
    when(patientImmunizationManager.get(immunizationId)).thenReturn(null);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("immunizationId", immunizationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/immunizations/{immunizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(patientImmunizationManager).get(immunizationId);
  }

  @Test
  public void testGetOneWrongPatient() throws Exception {
    // setup random data
    PatientImmunization protossResult = getFixture(PatientImmunization.class);

    // years and months will always be numeric
    protossResult.getImmunizationAge().setMonths(String.valueOf(TestUtilities.nextInt()));
    protossResult.getImmunizationAge().setYears(String.valueOf(TestUtilities.nextInt()));

    int unexpectedPatientId = TestUtilities.nextId();
    int immunizationId = protossResult.getAdminVaccineUniqueId();
    int expectedPatientId = TestUtilities.nextId();
    protossResult.setPatientId(unexpectedPatientId);

    // mock dependencies
    when(patientImmunizationManager.get(immunizationId)).thenReturn(protossResult);

    // test
    given()
        .pathParam("patientId", expectedPatientId)
        .pathParam("immunizationId", immunizationId)
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/immunizations/{immunizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(patientImmunizationManager).get(immunizationId);
  }
}
