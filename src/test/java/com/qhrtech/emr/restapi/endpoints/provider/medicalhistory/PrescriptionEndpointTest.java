
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PrescriptionEndpointTest extends AbstractEndpointTest<PrescriptionEndpoint> {

  private final PrescriptionMedicationManager prescriptionMedicationManager;
  private final int defaultPageSize = 25;
  private final int maximumPageSize = 50;

  public PrescriptionEndpointTest() {
    super(new PrescriptionEndpoint(), PrescriptionEndpoint.class);
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
  public void testGetPrescriptions() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);
    int patientId = TestUtilities.nextId();
    int pageSize = TestUtilities.nextInt(1, 50);
    int startingId = TestUtilities.nextId();
    boolean external = TestUtilities.nextBoolean();

    boolean includeCcddMappings = TestUtilities.nextBoolean();
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(rxUuid, patientId, pageSize, startingId, external,
            includeCcddMappings);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("patientId", patientId)
            .queryParam("rxUuid", rxUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("external", external)
            .queryParam("includeCcddMappings", includeCcddMappings)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            patientId,
            pageSize,
            startingId,
            external,
            includeCcddMappings);
  }

  @Test
  public void testGetPrescriptionsWithNullPageSize() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);
    int patientId = TestUtilities.nextId();
    Integer pageSize = null;
    int startingId = TestUtilities.nextId();
    boolean external = TestUtilities.nextBoolean();

    boolean includeCcddMappings = TestUtilities.nextBoolean();
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(rxUuid, patientId, pageSize, startingId, external,
            includeCcddMappings);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("patientId", patientId)
            .queryParam("rxUuid", rxUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("external", external)
            .queryParam("includeCcddMappings", includeCcddMappings)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            patientId,
            defaultPageSize,
            startingId,
            external,
            includeCcddMappings);
  }

  @Test
  public void testGetPrescriptionsWithOnlyPrescriptionUuid() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);

    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(rxUuid, null, defaultPageSize, null, null,
            null);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("rxUuid", rxUuid)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            null,
            defaultPageSize,
            null,
            null,
            null);
  }

  @Test
  public void testGetPrescriptionsWithNegativePageSize() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);
    int patientId = TestUtilities.nextId();
    Integer pageSize = Math.abs(TestUtilities.nextInt(1000) + 1) * (-1);
    int startingId = TestUtilities.nextId();
    boolean external = TestUtilities.nextBoolean();

    boolean includeCcddMappings = TestUtilities.nextBoolean();
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(rxUuid, patientId, pageSize, startingId, external,
            includeCcddMappings);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("patientId", patientId)
            .queryParam("rxUuid", rxUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("external", external)
            .queryParam("includeCcddMappings", includeCcddMappings)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            patientId,
            defaultPageSize,
            startingId,
            external,
            includeCcddMappings);
  }

  @Test
  public void testGetPrescriptionsWithHugePageSize() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);
    int patientId = TestUtilities.nextId();
    Integer pageSize = Math.abs(TestUtilities.nextInt(1000) + maximumPageSize + 1);
    int startingId = TestUtilities.nextId();
    boolean external = TestUtilities.nextBoolean();

    boolean includeCcddMappings = TestUtilities.nextBoolean();
    EnvelopeDto<PrescriptionMedicationDto> expected =
        getPrescriptionsFromProtoss(rxUuid, patientId, pageSize, startingId, external,
            includeCcddMappings);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("patientId", patientId)
            .queryParam("rxUuid", rxUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("external", external)
            .queryParam("includeCcddMappings", includeCcddMappings)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            patientId,
            maximumPageSize,
            startingId,
            external,
            includeCcddMappings);
  }

  @Test
  public void testGetEmptyPrescriptions() throws Exception {
    // setup random data and mock dependency
    String rxUuid = TestUtilities.nextString(10);
    int patientId = TestUtilities.nextId();
    int pageSize = TestUtilities.nextInt(1, 50);
    int startingId = TestUtilities.nextId();
    boolean external = TestUtilities.nextBoolean();

    boolean includeCcddMappings = TestUtilities.nextBoolean();
    Envelope<PrescriptionMedication> envelope = new Envelope<>();
    envelope.setContents(Collections.emptyList());

    when(prescriptionMedicationManager
        .searchPrescriptions(rxUuid,
            patientId,
            pageSize,
            startingId,
            external,
            includeCcddMappings)).thenReturn(envelope);

    EnvelopeDto<PrescriptionMedicationDto> expected = new EnvelopeDto<>();
    expected.setContents(Collections.emptyList());
    expected.setCount(0);
    expected.setTotal(0);
    expected.setLastId(0L);

    // test
    EnvelopeDto actual =
        reType(given()
            .queryParam("patientId", patientId)
            .queryParam("rxUuid", rxUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("external", external)
            .queryParam("includeCcddMappings", includeCcddMappings)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescriptions/")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(EnvelopeDto.class));

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager)
        .searchPrescriptions(rxUuid,
            patientId,
            pageSize,
            startingId,
            external,
            includeCcddMappings);
  }

  private EnvelopeDto<PrescriptionMedicationDto> getPrescriptionsFromProtoss(
      String rxUuid,
      Integer patientId,
      Integer pageSize,
      Integer startingId,
      Boolean external,
      Boolean includeCcddMappings) throws ProtossException {
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

    when(prescriptionMedicationManager.searchPrescriptions(rxUuid, patientId, pageSize, startingId,
        external,
        includeCcddMappings))
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
