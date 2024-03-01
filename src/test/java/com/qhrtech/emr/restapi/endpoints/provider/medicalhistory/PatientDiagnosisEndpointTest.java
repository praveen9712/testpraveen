
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisManager;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosis;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

public class PatientDiagnosisEndpointTest extends AbstractEndpointTest<PatientDiagnosisEndpoint> {
  private final PatientDiagnosisManager patientDiagnosisManager;
  private final DiagnosisStatusManager diagnosisStatusManager;

  public PatientDiagnosisEndpointTest() {
    super(new PatientDiagnosisEndpoint(), PatientDiagnosisEndpoint.class);
    patientDiagnosisManager = mock(PatientDiagnosisManager.class);
    diagnosisStatusManager = mock(DiagnosisStatusManager.class);
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
    servicesMap.put(PatientDiagnosisManager.class, patientDiagnosisManager);
    servicesMap.put(DiagnosisStatusManager.class, diagnosisStatusManager);
    return servicesMap;
  }

  @Test
  public void getByDiagnosisId() throws Exception {
    // setup random data
    PatientDiagnosis diagnosis = getFixture(PatientDiagnosis.class);
    Set<DiagnosisStatus> diagnosisStatuses = getFixtures(DiagnosisStatus.class, HashSet::new, 4);
    DiagnosisStatus diagnosisStatus = new DiagnosisStatus();
    diagnosisStatus.setStatusId(diagnosis.getDiagnosisStatusId());
    diagnosisStatus.setStatusName(TestUtilities.nextString(10));
    diagnosisStatuses.add(diagnosisStatus);

    int diagnosisId = diagnosis.getId();
    // mock dependencies
    when(patientDiagnosisManager.getById(diagnosisId)).thenReturn(diagnosis);
    when(diagnosisStatusManager.getAll()).thenReturn(diagnosisStatuses);

    PatientDiagnosisDto expected = mapDto(diagnosis, PatientDiagnosisDto.class);
    expected.setDiagnosisStatus(mapDto(diagnosisStatus, DiagnosisStatusDto.class));

    int patientId = diagnosis.getPatientId();
    // test
    PatientDiagnosisDto actual = given()
        .pathParam("patientId", patientId)
        .pathParam("diagnosisId", diagnosisId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/diagnoses/{diagnosisId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(PatientDiagnosisDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(patientDiagnosisManager).getById(diagnosisId);
    verify(diagnosisStatusManager).getAll();
    verify(patientDiagnosisManager, never()).getForPatient(patientId);
  }

  @Test
  public void getByDiagnosisIdWithNullStatus() throws Exception {
    // setup random data
    PatientDiagnosis diagnosis = getFixture(PatientDiagnosis.class);
    Set<DiagnosisStatus> diagnosisStatuses = getFixtures(DiagnosisStatus.class, HashSet::new, 4);

    int diagnosisId = diagnosis.getId();
    // mock dependencies
    when(patientDiagnosisManager.getById(diagnosisId)).thenReturn(diagnosis);
    when(diagnosisStatusManager.getAll()).thenReturn(diagnosisStatuses);

    PatientDiagnosisDto expected = mapDto(diagnosis, PatientDiagnosisDto.class);

    int patientId = diagnosis.getPatientId();
    // test
    PatientDiagnosisDto actual = given()
        .pathParam("patientId", patientId)
        .pathParam("diagnosisId", diagnosisId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/diagnoses/{diagnosisId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(PatientDiagnosisDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(patientDiagnosisManager).getById(diagnosisId);
    verify(diagnosisStatusManager).getAll();
    verify(patientDiagnosisManager, never()).getForPatient(patientId);
  }

  @Test
  public void getByDiagnosisIdForWrongPatient() throws Exception {
    // setup random data
    PatientDiagnosis diagnosis = getFixture(PatientDiagnosis.class);
    int patientId = TestUtilities.nextId();
    int diagnosisId = diagnosis.getId();

    // mock dependencies
    when(patientDiagnosisManager.getById(diagnosisId)).thenReturn(diagnosis);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("diagnosisId", diagnosisId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/diagnoses/{diagnosisId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(patientDiagnosisManager).getById(diagnosisId);
    verify(patientDiagnosisManager, never()).getForPatient(patientId);
  }

  @Test
  public void getByDiagnosisIdNotFound() throws Exception {
    // setup random data
    int patientId = TestUtilities.nextId();
    int diagnosisId = TestUtilities.nextId();

    // mock dependencies
    when(patientDiagnosisManager.getById(diagnosisId)).thenReturn(null);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("diagnosisId", diagnosisId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/diagnoses/{diagnosisId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(patientDiagnosisManager).getById(diagnosisId);
    verify(patientDiagnosisManager, never()).getForPatient(patientId);
  }

  @Test
  public void getForPatient() throws Exception {
    // setup random data, assigning a random status to each diagnosis
    Set<PatientDiagnosis> diagnoses = getFixtures(PatientDiagnosis.class, HashSet::new, 10);
    Set<DiagnosisStatus> statuses = getFixtures(DiagnosisStatus.class, HashSet::new, 4);
    diagnoses
        .forEach(d -> d.setDiagnosisStatusId(TestUtilities.nextElement(statuses).getStatusId()));
    Map<Integer, DiagnosisStatus> statusesById =
        statuses.stream().collect(Collectors.toMap(DiagnosisStatus::getStatusId,
            Function.identity()));
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientDiagnosisManager.getForPatient(patientId))
        .thenReturn(new HashSet<>(diagnoses));
    when(diagnosisStatusManager.getAll()).thenReturn(statuses);

    // Add the diagnosis status.
    Set<PatientDiagnosisDto> expected = new HashSet<>();
    for (PatientDiagnosis diagnosis : diagnoses) {
      PatientDiagnosisDto dto = mapDto(diagnosis, PatientDiagnosisDto.class);
      DiagnosisStatus status = statusesById.get(diagnosis.getDiagnosisStatusId());
      DiagnosisStatusDto statusDto = mapDto(status, DiagnosisStatusDto.class);
      dto.setDiagnosisStatus(statusDto);
      expected.add(dto);
    }

    // test
    Set<PatientDiagnosisDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/diagnoses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientDiagnosisDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(patientDiagnosisManager).getForPatient(patientId);
    verify(diagnosisStatusManager).getAll();
  }

}
