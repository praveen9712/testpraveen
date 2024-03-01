
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medications.MedicationLogEntryManager;
import com.qhrtech.emr.accuro.model.medications.MedicationLogEntry;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionHistoryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PrescriptionHistoryEndpointTest
    extends AbstractEndpointTest<PrescriptionHistoryEndpoint> {

  private final MedicationLogEntryManager medicationLogEntryManager;
  private final String commonUrl = "/v1/provider-portal/patients/{patientId}/prescriptions";

  public PrescriptionHistoryEndpointTest() {
    super(new PrescriptionHistoryEndpoint(), PrescriptionHistoryEndpoint.class);
    medicationLogEntryManager = mock(MedicationLogEntryManager.class);
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
    servicesMap.put(MedicationLogEntryManager.class, medicationLogEntryManager);
    return servicesMap;
  }

  @Test
  public void getByPatientId() throws Exception {
    // set up random data
    int patientId = TestUtilities.nextId();

    List<MedicationLogEntry> protossResults =
        getFixtures(MedicationLogEntry.class, ArrayList::new, 5);
    protossResults.forEach(e -> e.setPatientId(patientId));

    // mock dependency
    when(medicationLogEntryManager.getAll()).thenReturn(protossResults);

    // build expected result
    List<PrescriptionHistoryDto> expected =
        mapDto(protossResults, PrescriptionHistoryDto.class, ArrayList::new);

    // run test
    List<PrescriptionHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + commonUrl + "/histories/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(PrescriptionHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(medicationLogEntryManager).getAll();
  }

  @Test
  public void getByPrescriptionId() throws Exception {
    // set up random data
    int patientId = TestUtilities.nextId();
    int prescriptionId = TestUtilities.nextId();

    List<MedicationLogEntry> protossResults =
        getFixtures(MedicationLogEntry.class, ArrayList::new, 5);
    protossResults.forEach(e -> {
      e.setPatientId(patientId);
      e.setPrescriptionId(prescriptionId);
    });

    // mock dependency
    when(medicationLogEntryManager.getByPrescriptionId(prescriptionId)).thenReturn(protossResults);

    // build expected result
    List<PrescriptionHistoryDto> expected =
        mapDto(protossResults, PrescriptionHistoryDto.class, ArrayList::new);

    // run test
    List<PrescriptionHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("prescriptionId", prescriptionId)
            .when()
            .get(getBaseUrl() + commonUrl + "/{prescriptionId}/histories/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(PrescriptionHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(medicationLogEntryManager).getByPrescriptionId(prescriptionId);
  }

  @Test
  public void getByHistoryId() throws Exception {
    // set up random data
    MedicationLogEntry protossResults = getFixture(MedicationLogEntry.class);
    int patientId = protossResults.getPatientId();
    int prescriptionId = protossResults.getPrescriptionId();
    int historyId = protossResults.getId();

    // mock dependency
    when(medicationLogEntryManager.get(historyId)).thenReturn(protossResults);

    // build expected result
    PrescriptionHistoryDto expected = mapDto(protossResults, PrescriptionHistoryDto.class);

    // run test
    PrescriptionHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("prescriptionId", prescriptionId)
            .pathParam("historyId", historyId)
            .when()
            .get(getBaseUrl() + commonUrl + "/{prescriptionId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(PrescriptionHistoryDto.class);

    assertEquals(expected, actual);
    verify(medicationLogEntryManager).get(historyId);
  }

  @Test
  public void nullReturnForGetByHistoryId() throws Exception {
    // set up random data
    int patientId = TestUtilities.nextId();
    int prescriptionId = TestUtilities.nextId();
    int historyId = TestUtilities.nextId();

    // mock dependency
    when(medicationLogEntryManager.get(historyId)).thenReturn(null);

    // run test
    given()
        .pathParam("patientId", patientId)
        .pathParam("prescriptionId", prescriptionId)
        .pathParam("historyId", historyId)
        .when()
        .get(getBaseUrl() + commonUrl + "/{prescriptionId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(medicationLogEntryManager).get(historyId);
  }

  @Test
  public void patientIdNotMatchForGetByHistoryId() throws Exception {
    // set up random data
    MedicationLogEntry protossResults = getFixture(MedicationLogEntry.class);
    int patientId = protossResults.getPatientId();
    int prescriptionId = protossResults.getPrescriptionId();
    int historyId = protossResults.getId();

    // mock dependency
    when(medicationLogEntryManager.get(historyId)).thenReturn(protossResults);

    // build expected result
    PrescriptionHistoryDto expected = mapDto(protossResults, PrescriptionHistoryDto.class);

    // run test
    PrescriptionHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("prescriptionId", prescriptionId)
            .pathParam("historyId", historyId)
            .when()
            .get(getBaseUrl() + commonUrl + "/{prescriptionId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(PrescriptionHistoryDto.class);

    assertEquals(expected, actual);
    verify(medicationLogEntryManager).get(historyId);
  }

  @Test
  public void getSummary() throws Exception {
    // set up random data as expected
    String expected = TestUtilities.nextString(100);
    int patientId = TestUtilities.nextId();
    int prescriptionId = TestUtilities.nextId();

    // mock dependency
    when(medicationLogEntryManager.getChangesByPrescriptionId(prescriptionId)).thenReturn(expected);

    // run test
    String actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("prescriptionId", prescriptionId)
            .when()
            .get(getBaseUrl() + commonUrl + "/{prescriptionId}/histories/summaries")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().asString();

    assertEquals(expected, actual);
    verify(medicationLogEntryManager).getChangesByPrescriptionId(prescriptionId);
  }
}
