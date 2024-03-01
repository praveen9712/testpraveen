
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.immunization.ImmunizationScheduleManager;
import com.qhrtech.emr.accuro.model.immunization.ImmunizationSchedule;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ImmunizationScheduleDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class PatientImmunizationScheduleEndpointTest
    extends AbstractEndpointTest<PatientImmunizationScheduleEndpoint> {

  private final ImmunizationScheduleManager immunizationScheduleManager;

  public PatientImmunizationScheduleEndpointTest() {
    super(new PatientImmunizationScheduleEndpoint(), PatientImmunizationScheduleEndpoint.class);
    immunizationScheduleManager = mock(ImmunizationScheduleManager.class);
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
    servicesMap.put(ImmunizationScheduleManager.class, immunizationScheduleManager);
    return servicesMap;
  }

  @Test
  public void testGetImmunizationSchedules() throws Exception {

    int patientId = TestUtilities.nextId();

    // setup random data
    Set<ImmunizationSchedule> protossResults =
        getFixtures(ImmunizationSchedule.class, HashSet::new, 3);

    // mock dependencies
    when(immunizationScheduleManager.getByPatientId(patientId)).thenReturn(protossResults);

    Set<ImmunizationScheduleDto> expected =
        mapDto(protossResults, ImmunizationScheduleDto.class, HashSet::new);

    // test
    Set<ImmunizationScheduleDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/immunization-schedules/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ImmunizationScheduleDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(immunizationScheduleManager).getByPatientId(patientId);
  }

  @Test
  public void testGetImmunizationSchedule() throws Exception {

    // setup random data
    Set<ImmunizationSchedule> protossResults =
        getFixtures(ImmunizationSchedule.class, HashSet::new, 3);

    Set<ImmunizationScheduleDto> expected =
        mapDto(protossResults, ImmunizationScheduleDto.class, HashSet::new);

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(immunizationScheduleManager.getByPatientId(patientId)).thenReturn(protossResults);

    int id = expected.iterator().next().getId();

    // test
    ImmunizationScheduleDto actual =
        given().pathParam("patientId", patientId).pathParam("id", id)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/immunization-schedules/{id}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ImmunizationScheduleDto.class);

    // assertions
    assertTrue(expected.contains(actual));
    assertEquals(id, actual.getId());
    verify(immunizationScheduleManager).getByPatientId(patientId);
  }

  @Test
  public void testGetImmunizationScheduleNotFound() throws Exception {

    // setup random data
    Set<ImmunizationSchedule> protossResults =
        getFixtures(ImmunizationSchedule.class, HashSet::new, 1);

    Set<ImmunizationScheduleDto> expected =
        mapDto(protossResults, ImmunizationScheduleDto.class, HashSet::new);

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(immunizationScheduleManager.getByPatientId(patientId)).thenReturn(protossResults);

    int id = expected.iterator().next().getId() + 1;

    // test
    given().pathParam("patientId", patientId).pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/immunization-schedules/{id}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(immunizationScheduleManager).getByPatientId(patientId);
  }

}
