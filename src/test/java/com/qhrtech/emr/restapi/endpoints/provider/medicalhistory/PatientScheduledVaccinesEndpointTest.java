
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.immunization.ImmunizationScheduleManager;
import com.qhrtech.emr.accuro.api.immunization.ScheduleVaccineManager;
import com.qhrtech.emr.accuro.api.medicalhistory.VaccineManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.immunization.ImmunizationAge;
import com.qhrtech.emr.accuro.model.immunization.ImmunizationSchedule;
import com.qhrtech.emr.accuro.model.immunization.ScheduleVaccine;
import com.qhrtech.emr.accuro.model.medicalhistory.Vaccine;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ScheduleVaccineDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.VaccineDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

public class PatientScheduledVaccinesEndpointTest
    extends AbstractEndpointTest<PatientScheduledVaccinesEndpoint> {

  private final ScheduleVaccineManager scheduleVaccineManager;
  private final VaccineManager vaccineManager;
  private final ImmunizationScheduleManager immScheduleManager;
  private final PatientManager patientManager;

  public PatientScheduledVaccinesEndpointTest() {
    super(new PatientScheduledVaccinesEndpoint(), PatientScheduledVaccinesEndpoint.class);
    scheduleVaccineManager = mock(ScheduleVaccineManager.class);
    vaccineManager = mock(VaccineManager.class);
    immScheduleManager = mock(ImmunizationScheduleManager.class);
    patientManager = mock(PatientManager.class);
  }

  private static void prepScheduleVaccine(ScheduleVaccine vaccine, int scheduleId,
      Set<Integer> possibleVaccineIds) {
    vaccine.setImmunizationScheduleId(scheduleId);
    vaccine.setVaccineId(TestUtilities.nextElement(possibleVaccineIds));
    ImmunizationAge age = vaccine.getImmunizationAge();
    age.setYears(String.valueOf(TestUtilities.nextInt()));
    age.setMonths(String.valueOf(TestUtilities.nextInt()));

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
    servicesMap.put(ScheduleVaccineManager.class, scheduleVaccineManager);
    servicesMap.put(VaccineManager.class, vaccineManager);
    servicesMap.put(ImmunizationScheduleManager.class, immScheduleManager);
    servicesMap.put(PatientManager.class, patientManager);
    return servicesMap;
  }

  @Test
  public void getByScheduleIdInvalidPatient() throws Exception {
    // set up random data
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(immScheduleManager.getByPatientId(patientId)).thenReturn(Collections.emptySet());

    // run test
    given()
        .pathParam("patientId", patientId)
        .pathParam("scheduleId", TestUtilities.nextId())
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients"
            + "/{patientId}/immunization-schedules/{scheduleId}/scheduled-vaccines/")
        .then()
        .assertThat()
        .statusCode(404);

    // run assertions
    verify(immScheduleManager).getByPatientId(patientId);
    verifyZeroInteractions(vaccineManager);
    verifyZeroInteractions(scheduleVaccineManager);
  }

  @Test
  public void getByScheduleId() throws Exception {
    // set up random data
    int scheduleId = TestUtilities.nextId();
    int patientId = TestUtilities.nextId();

    Map<Integer, Vaccine> integerVaccineMap = getFixtures(Vaccine.class, HashSet::new, 10)
        .stream()
        .collect(Collectors.toMap(Vaccine::getVaccineId, Function.identity()));

    ImmunizationSchedule schedule = getFixture(ImmunizationSchedule.class);
    schedule.setId(scheduleId);
    Set<ScheduleVaccine> protossResults = getFixtures(ScheduleVaccine.class, HashSet::new, 5);
    protossResults.forEach(v -> prepScheduleVaccine(v, scheduleId, integerVaccineMap.keySet()));

    // mock dependencies
    when(immScheduleManager.getByPatientId(patientId)).thenReturn(Collections.singleton(schedule));
    when(scheduleVaccineManager.getByScheduleId(scheduleId)).thenReturn(protossResults);
    when(vaccineManager.getAllVaccines()).thenReturn(new ArrayList<>(integerVaccineMap.values()));

    // build expected result
    Set<ScheduleVaccineDto> expected = new HashSet<>();
    for (ScheduleVaccine scheduleVaccine : protossResults) {
      ScheduleVaccineDto scheduleVaccineDto = mapDto(scheduleVaccine, ScheduleVaccineDto.class);
      Vaccine vaccine = integerVaccineMap.get(scheduleVaccine.getVaccineId());
      if (vaccine != null) {
        VaccineDto vaccineDto = mapDto(vaccine, VaccineDto.class);
        scheduleVaccineDto.setVaccine(vaccineDto);
      }
      expected.add(scheduleVaccineDto);
    }

    // run test
    Set<ScheduleVaccineDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("scheduleId", scheduleId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients"
                + "/{patientId}/immunization-schedules/{scheduleId}/scheduled-vaccines/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ScheduleVaccineDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(immScheduleManager).getByPatientId(patientId);
    verify(scheduleVaccineManager).getByScheduleId(scheduleId);
    verify(vaccineManager).getAllVaccines();
  }

  @Test
  public void getPatientSchedulesInvalidPatient() throws Exception {
    // set up data
    int patientId = TestUtilities.nextId();

    // set up mocks
    when(patientManager.getPatientById(patientId)).thenReturn(null);

    // run test
    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients"
            + "/{patientId}/scheduled-vaccines/")
        .then()
        .assertThat()
        .statusCode(404);

    // run assertions
    verify(patientManager).getPatientById(patientId);
    verifyZeroInteractions(vaccineManager);
    verifyZeroInteractions(scheduleVaccineManager);
  }

  @Test
  public void getPatientSchedulesNoneFound() throws Exception {
    // set up data
    int patientId = TestUtilities.nextId();
    List<Vaccine> vaccines = getFixtures(Vaccine.class, ArrayList::new, 2);

    // set up mocks
    when(patientManager.getPatientById(patientId)).thenReturn(getFixture(Patient.class));
    when(vaccineManager.getAllVaccines()).thenReturn(vaccines);
    when(scheduleVaccineManager.getByPatientId(patientId)).thenReturn(Collections.emptySet());

    // run test
    Set<ScheduleVaccineDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients"
                + "/{patientId}/scheduled-vaccines/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ScheduleVaccineDto[].class),
        HashSet::new);

    // run assertions
    assertEquals(Collections.emptySet(), actual);
    verify(patientManager).getPatientById(patientId);
    verify(vaccineManager).getAllVaccines();
    verify(scheduleVaccineManager).getByPatientId(patientId);
  }

  @Test
  public void getByPatientId() throws Exception {
    // set up random data
    int patientId = TestUtilities.nextId();
    Map<Integer, Vaccine> integerVaccineMap = getFixtures(Vaccine.class, HashSet::new, 10)
        .stream()
        .collect(Collectors.toMap(Vaccine::getVaccineId, Function.identity()));
    Set<ScheduleVaccine> protossResults = getFixtures(ScheduleVaccine.class, HashSet::new, 5);
    protossResults.forEach(
        v -> prepScheduleVaccine(v, v.getImmunizationScheduleId(), integerVaccineMap.keySet()));

    // set up mocks
    when(patientManager.getPatientById(patientId)).thenReturn(getFixture(Patient.class));
    when(vaccineManager.getAllVaccines()).thenReturn(new ArrayList<>(integerVaccineMap.values()));
    when(scheduleVaccineManager.getByPatientId(patientId)).thenReturn(protossResults);

    // set up expected result
    Set<ScheduleVaccineDto> expected = new HashSet<>();
    for (ScheduleVaccine scheduleVaccine : protossResults) {
      ScheduleVaccineDto scheduleVaccineDto = mapDto(scheduleVaccine, ScheduleVaccineDto.class);
      Vaccine vaccine = integerVaccineMap.get(scheduleVaccine.getVaccineId());
      if (vaccine != null) {
        VaccineDto vaccineDto = mapDto(vaccine, VaccineDto.class);
        scheduleVaccineDto.setVaccine(vaccineDto);
      }
      expected.add(scheduleVaccineDto);
    }

    // run tests
    Set<ScheduleVaccineDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients"
                + "/{patientId}/scheduled-vaccines/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ScheduleVaccineDto[].class),
        HashSet::new);

    // run assertions
    assertEquals(expected, actual);
    verify(patientManager).getPatientById(patientId);
    verify(vaccineManager).getAllVaccines();
    verify(scheduleVaccineManager).getByPatientId(patientId);
  }
}
