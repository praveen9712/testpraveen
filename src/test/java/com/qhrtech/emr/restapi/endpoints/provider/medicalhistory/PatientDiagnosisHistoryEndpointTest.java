
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisHistoryManager;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosisHistory;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisHistoryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class PatientDiagnosisHistoryEndpointTest extends
    AbstractEndpointTest<PatientDiagnosisHistoryEndpoint> {

  private final PatientDiagnosisHistoryManager patientDiagnosisHistoryManager;
  private final DiagnosisStatusManager diagnosisStatusManager;
  private final String diagnosisUriPath =
      "/v1/provider-portal/patients/{patientId}/diagnoses/{diagnosisId}";

  public PatientDiagnosisHistoryEndpointTest() {
    super(new PatientDiagnosisHistoryEndpoint(), PatientDiagnosisHistoryEndpoint.class);
    patientDiagnosisHistoryManager = mock(PatientDiagnosisHistoryManager.class);
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
    servicesMap.put(PatientDiagnosisHistoryManager.class, patientDiagnosisHistoryManager);
    servicesMap.put(DiagnosisStatusManager.class, diagnosisStatusManager);
    return servicesMap;
  }

  @Test
  public void getById() throws Exception {
    PatientDiagnosisHistory protossResult = getFixture(PatientDiagnosisHistory.class);

    Set<DiagnosisStatus> diagnosisStatuses = getFixtures(DiagnosisStatus.class, HashSet::new, 4);
    DiagnosisStatus diagnosisStatus = diagnosisStatuses.iterator().next();
    protossResult.setDiagnosisStatusId(diagnosisStatus.getStatusId());

    PatientDiagnosisHistoryDto expected = mapDto(protossResult, PatientDiagnosisHistoryDto.class);
    expected.setDiagnosisStatus(mapDto(diagnosisStatus, DiagnosisStatusDto.class));

    when(diagnosisStatusManager.getAll()).thenReturn(diagnosisStatuses);
    when(patientDiagnosisHistoryManager.getById(expected.getId())).thenReturn(protossResult);

    PatientDiagnosisHistoryDto actual = given()
        .pathParam("patientId", expected.getPatientId())
        .pathParam("diagnosisId", expected.getPatientDiagnosisId())
        .pathParam("historyId", expected.getId())
        .when()
        .get(getBaseUrl()
            + diagnosisUriPath
            + "/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(PatientDiagnosisHistoryDto.class);

    assertEquals(expected, actual);
    verify(diagnosisStatusManager).getAll();
    verify(patientDiagnosisHistoryManager).getById(expected.getId());
  }

  @Test
  public void getForPatientDiagnosis() throws Exception {
    List<PatientDiagnosisHistory> protossResult =
        getFixtures(PatientDiagnosisHistory.class, ArrayList::new, 5);
    int patientId = TestUtilities.nextId();
    int diagnosisId = TestUtilities.nextId();

    List<DiagnosisStatus> diagnosisStatuses = getFixtures(DiagnosisStatus.class, ArrayList::new, 4);

    List<PatientDiagnosisHistoryDto> expected = new ArrayList<>();
    for (PatientDiagnosisHistory historyEntry : protossResult) {
      // set up patient id and patient diagnosis id
      historyEntry.setPatientId(patientId);
      historyEntry.setPatientDiagnosisId(diagnosisId);

      // set diagnosis status and data transfer
      DiagnosisStatus diagnosisStatus =
          diagnosisStatuses.get(TestUtilities.nextInt(diagnosisStatuses.size()));
      historyEntry.setDiagnosisStatusId(diagnosisStatus.getStatusId());
      PatientDiagnosisHistoryDto historyEntryDto =
          mapDto(historyEntry, PatientDiagnosisHistoryDto.class);
      historyEntryDto.setDiagnosisStatus(mapDto(diagnosisStatus, DiagnosisStatusDto.class));

      // add the entry
      expected.add(historyEntryDto);
    }

    when(diagnosisStatusManager.getAll()).thenReturn(new HashSet<>(diagnosisStatuses));
    when(patientDiagnosisHistoryManager.getForPatientDiagnosis(diagnosisId))
        .thenReturn(protossResult);

    List<PatientDiagnosisHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("diagnosisId", diagnosisId)
            .when()
            .get(getBaseUrl()
                + diagnosisUriPath
                + "/histories")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientDiagnosisHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(diagnosisStatusManager).getAll();
    verify(patientDiagnosisHistoryManager).getForPatientDiagnosis(diagnosisId);
  }
}
