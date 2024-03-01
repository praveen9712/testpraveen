
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PatientPrescriptionEndpointV2Test
    extends AbstractEndpointTest<PatientPrescriptionEndpointV2> {

  private final PrescriptionMedicationManager prescriptionMedicationManager;
  private final int defaultPageSize = 25;
  private final int maximumPageSize = 50;
  private final boolean includeCcddMapping = TestUtilities.nextBoolean();

  public PatientPrescriptionEndpointV2Test() {
    super(new PatientPrescriptionEndpointV2(), PatientPrescriptionEndpointV2.class);
    prescriptionMedicationManager = mock(PrescriptionMedicationManager.class);
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
    servicesMap.put(PrescriptionMedicationManager.class, prescriptionMedicationManager);
    return servicesMap;
  }

  @Test
  public void testGetForPatient() throws Exception {
    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(patientId, null, true);

    // test
    EnvelopeDto actual =
        reType(given().pathParam("patientId", patientId)
            .queryParam("includeCcddMapping", true)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .getPrescriptionsByPatientId(patientId, null, defaultPageSize, null, true);
  }

  @Test
  public void testGetForPatientWithNoResult() throws Exception {
    // setup random data and mock dependency
    Envelope<PrescriptionMedication> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(new ArrayList<>());

    EnvelopeDto<PrescriptionMedicationDto> expected = new EnvelopeDto<>();
    expected.setContents(new ArrayList<>());
    expected.setCount(0);
    expected.setTotal(0);
    expected.setLastId(0L);
    int patientId = TestUtilities.nextId();
    int pageSize = defaultPageSize;
    when(prescriptionMedicationManager.getPrescriptionsByPatientId(patientId, null, pageSize, null,
        includeCcddMapping))
            .thenReturn(protossEnvelope);

    // test
    EnvelopeDto actual =
        reType(given().pathParam("patientId", patientId)
            .queryParam("includeCcddMapping", includeCcddMapping)
            .when()
            .queryParam("pageSize", pageSize)
            .get(getBaseUrl() + "/v2/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .getPrescriptionsByPatientId(patientId, null, pageSize, null, includeCcddMapping);
  }

  @Test
  public void testGetForPatientWithMaxPage() throws Exception {
    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    Integer pageSize = 60;
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(patientId, pageSize, includeCcddMapping);

    // test
    EnvelopeDto actual =
        reType(given().pathParam("patientId", patientId)
            .queryParam("includeCcddMapping", includeCcddMapping)
            .when()
            .queryParam("pageSize", pageSize)
            .get(getBaseUrl() + "/v2/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    assertEquals(maximumPageSize, actual.getContents().size());
    verify(prescriptionMedicationManager)
        .getPrescriptionsByPatientId(patientId, null, maximumPageSize, null, includeCcddMapping);
  }

  private EnvelopeDto<PrescriptionMedicationDto> getPrescriptionsFromProtoss(int patientId,
      Integer pageSize, Boolean includeCcddMapping) throws ProtossException {
    if (pageSize == null || pageSize <= 0) {
      pageSize = defaultPageSize;
    }

    if (pageSize > maximumPageSize) {
      pageSize = maximumPageSize;
    }

    List<PrescriptionMedication> prescriptionsFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, pageSize);

    Envelope<PrescriptionMedication> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(prescriptionsFromProtoss);
    protossEnvelope.setCount(prescriptionsFromProtoss.size());
    protossEnvelope.setTotal(prescriptionsFromProtoss.size());
    protossEnvelope.setLastId((long) (prescriptionsFromProtoss.size()));

    ArrayList<PrescriptionMedicationDto> expectedContents =
        mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);

    when(prescriptionMedicationManager.getPrescriptionsByPatientId(patientId, null, pageSize, null,
        includeCcddMapping))
            .thenReturn(protossEnvelope);

    EnvelopeDto<PrescriptionMedicationDto> expected = new EnvelopeDto<>();
    expected.setContents(expectedContents);
    expected.setCount(expectedContents.size());
    expected.setTotal(expectedContents.size());
    expected.setLastId((long) expectedContents.size());

    return expected;
  }

  private EnvelopeDto<PrescriptionMedicationDto> reType(EnvelopeDto rawEnvelope) {
    List<PrescriptionMedicationDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<PrescriptionMedicationDto>>() {});
    EnvelopeDto<PrescriptionMedicationDto> e = (EnvelopeDto<PrescriptionMedicationDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }
}
