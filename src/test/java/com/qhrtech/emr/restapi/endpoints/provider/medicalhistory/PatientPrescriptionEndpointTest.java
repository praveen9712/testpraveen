
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.prescription.AnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.StatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.WellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.medications.LimitedUseCode;
import com.qhrtech.emr.accuro.model.prescription.Annotation;
import com.qhrtech.emr.accuro.model.prescription.Dosage;
import com.qhrtech.emr.accuro.model.prescription.Interaction;
import com.qhrtech.emr.accuro.model.prescription.InteractionManagementDetails;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionIndication;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.model.prescription.StatusHistory;
import com.qhrtech.emr.accuro.model.prescription.WellnetPrescriptionLink;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LimitedUseCodeDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.AnnotationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.DosageDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.InteractionDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.InteractionManagementDetailsDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionDetailsDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionIndicationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.StatusHistoryDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.WellnetPrescriptionLinkDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class PatientPrescriptionEndpointTest
    extends AbstractEndpointTest<PatientPrescriptionEndpoint> {

  private final PrescriptionMedicationManager prescriptionMedicationManager;
  private final DosageManager dosageManager;
  private final PrescriptionIndicationManager prescriptionIndicationManager;
  private final AnnotationManager annotationManager;
  private final StatusHistoryManager statusHistoryManager;
  private final InteractionManager interactionManager;
  private final InteractionManagementDetailsManager interactionManagementDetailsManager;
  private final WellnetPrescriptionLinkManager wellnetPrescriptionLinkManager;
  private final LimitedUseCodeManager limitedUseCodeManager;
  private final PrescriptionDetailsService prescriptionDetailsService;
  private final SystemInformationManager systemInformationManager;
  private final String datePattern = "yyyyMMdd";

  public PatientPrescriptionEndpointTest() {
    super(new PatientPrescriptionEndpoint(), PatientPrescriptionEndpoint.class);
    prescriptionMedicationManager = mock(PrescriptionMedicationManager.class);
    dosageManager = mock(DosageManager.class);
    prescriptionIndicationManager = mock(PrescriptionIndicationManager.class);
    annotationManager = mock(AnnotationManager.class);
    statusHistoryManager = mock(StatusHistoryManager.class);
    interactionManager = mock(InteractionManager.class);
    interactionManagementDetailsManager = mock(InteractionManagementDetailsManager.class);
    wellnetPrescriptionLinkManager = mock(WellnetPrescriptionLinkManager.class);
    limitedUseCodeManager = mock(LimitedUseCodeManager.class);
    prescriptionDetailsService = mock(PrescriptionDetailsService.class);
    systemInformationManager = mock(SystemInformationManager.class);
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
    servicesMap.put(DosageManager.class, dosageManager);
    servicesMap.put(PrescriptionIndicationManager.class, prescriptionIndicationManager);
    servicesMap.put(AnnotationManager.class, annotationManager);
    servicesMap.put(StatusHistoryManager.class, statusHistoryManager);
    servicesMap.put(InteractionManager.class, interactionManager);
    servicesMap.put(InteractionManagementDetailsManager.class, interactionManagementDetailsManager);
    servicesMap.put(WellnetPrescriptionLinkManager.class, wellnetPrescriptionLinkManager);
    servicesMap.put(LimitedUseCodeManager.class, limitedUseCodeManager);
    servicesMap.put(SystemInformationManager.class, systemInformationManager);
    return servicesMap;
  }

  @Test
  public void testGetForPatient() throws Exception {
    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    when(systemInformationManager.getProvince()).thenReturn(province);

    List<PrescriptionMedication> prescriptionsFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, 5);
    prescriptionsFromProtoss.forEach(p -> {
      p.setPatientId(patientId);
      p.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));
      p.setDin(String.valueOf(TestUtilities.nextInt()));
    });
    when(prescriptionMedicationManager.getForPatient(patientId, true))
        .thenReturn(prescriptionsFromProtoss);

    ArrayList<PrescriptionMedicationDto> expected =
        mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);

    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("includeCcddMapping", true)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getForPatient(patientId, true);

  }

  @Test
  public void testGetForPatientWithoutCcddMapping() throws Exception {
    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    when(systemInformationManager.getProvince()).thenReturn(province);

    List<PrescriptionMedication> prescriptionsFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, 5);
    prescriptionsFromProtoss.forEach(p -> {
      p.setPatientId(patientId);
      p.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));
      p.setDin(String.valueOf(TestUtilities.nextInt()));
    });

    when(prescriptionMedicationManager.getForPatient(patientId, false))
        .thenReturn(prescriptionsFromProtoss);

    ArrayList<PrescriptionMedicationDto> expected =
        mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);

    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("includeCcddMapping", false)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getForPatient(patientId, false);

  }

  @Test
  public void testGetForPatientWithNullCcddMapping() throws Exception {
    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    when(systemInformationManager.getProvince()).thenReturn(province);

    List<PrescriptionMedication> prescriptionsFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, 5);
    prescriptionsFromProtoss.forEach(p -> {
      p.setPatientId(patientId);
      p.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));
      p.setDin(String.valueOf(TestUtilities.nextInt()));
    });

    when(prescriptionMedicationManager.getForPatient(patientId, false))
        .thenReturn(prescriptionsFromProtoss);

    ArrayList<PrescriptionMedicationDto> expected =
        mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);

    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getForPatient(patientId, false);

  }

  @Test
  public void testGetExternalMedicationsForPatient() throws Exception {

    // setup random data and mock dependency
    int patientId = TestUtilities.nextId();
    Boolean external = RandomUtils.nextBoolean();

    List<PrescriptionMedication> prescriptionsFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, 5);
    prescriptionsFromProtoss.forEach(p -> {
      p.setPatientId(patientId);
      p.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));
      p.setDin(String.valueOf(TestUtilities.nextInt()));
    });

    List<PrescriptionMedication> filteredList = prescriptionsFromProtoss
        .stream()
        .filter(p -> external == null || external == p.isExternalRx())
        .collect(Collectors.toList());
    boolean includeCcddMapping = TestUtilities.nextBoolean();
    when(prescriptionMedicationManager.getForPatient(patientId, includeCcddMapping))
        .thenReturn(filteredList);

    ArrayList<PrescriptionMedicationDto> expected =
        mapDto(filteredList, PrescriptionMedicationDto.class, ArrayList::new);

    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("external", external)
            .queryParam("includeCcddMapping", includeCcddMapping)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);

    verify(prescriptionMedicationManager).getForPatient(patientId, includeCcddMapping);


  }

  @Test
  public void testGetForPatientNotFound() throws Exception {
    // setup random date and mock dependency
    int patientId = TestUtilities.nextId();

    when(prescriptionMedicationManager.getForPatient(patientId))
        .thenReturn(Collections.emptyList());

    boolean includeCcddMapping = TestUtilities.nextBoolean();
    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("external", true)
            .queryParam("includeCcddMapping", includeCcddMapping)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(Collections.emptyList(), actual);

    verify(prescriptionMedicationManager).getForPatient(patientId, includeCcddMapping);
  }

  @Test
  public void testGetByPrescriptionId() throws Exception {
    // setup random date on Protoss side
    // AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    AccuroProvince province = AccuroProvince.ON;
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);

    prescriptionFromProtoss.setDin(Integer.toString(TestUtilities.nextId()));
    prescriptionFromProtoss.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));

    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    // mock dependency
    when(systemInformationManager.getProvince()).thenReturn(province);
    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);

    PrescriptionMedicationDto expected =
        mapDto(prescriptionFromProtoss, PrescriptionMedicationDto.class);

    PrescriptionDetailsDto prescriptionDetailsDto = getFixture(PrescriptionDetailsDto.class);
    when(prescriptionDetailsService.getPrescriptionDetails(prescriptionFromProtoss,
        true))
            .thenReturn(prescriptionDetailsDto);
    expected.setPrescriptionDetails(prescriptionDetailsDto);

    // mock and set dosages
    expected.setDosages(mockAndGetDosages(prescriptionId));

    // mock and set indications
    expected.setIndications(mockAndGetIndications(prescriptionId));

    // mock and set annotations
    expected.setAnnotations(mockAndGetAnnotations(prescriptionId));

    // mock and set status histories
    expected.setStatusHistories(mockAndGetStatusHistories(prescriptionId));

    // mock and set interactions
    expected.setInteractions(mockAndGetInteractions(prescriptionId));

    // mock and set Wellnet prescription links
    expected.setWellnetPrescriptionLinks(mockAndGetWellnetLinks(prescriptionId));

    if (AccuroProvince.ON.equals(province)) {
      int din = Integer.parseInt(expected.getDin());
      expected.setLimitedUseCodes(mockAndGetLimitedUseCodes(din));
    }

    // test
    PrescriptionMedicationDto actual =
        given()
            .pathParams("patientId", prescriptionFromProtoss.getPatientId())
            .pathParam("prescriptionId", prescriptionId)
            .queryParam("includeCcddMapping", true)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/prescriptions/{prescriptionId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getById(prescriptionId);
    verify(prescriptionDetailsService).getPrescriptionDetails(prescriptionFromProtoss,
        true);
    verify(dosageManager).getForPrescription(prescriptionId);
    verify(prescriptionIndicationManager).get(prescriptionId);
    verify(annotationManager).getForPrescription(prescriptionId);
    verify(statusHistoryManager).getByPrescriptionId(prescriptionId);
    verify(interactionManager).getForPrescription(prescriptionId);
    for (InteractionDto interaction : expected.getInteractions()) {
      verify(interactionManagementDetailsManager).getByInteractionId(interaction.getId());
    }
    verify(wellnetPrescriptionLinkManager).getByPrescriptionId(prescriptionId);
    if (province.equals(AccuroProvince.ON)) {
      int din = Integer.parseInt(expected.getDin());
      verify(limitedUseCodeManager).getLimitedUseCodeByDrugId(din);
    }
  }

  @Test
  public void testGetByPrescriptionIdWithoutCcddMapping() throws Exception {
    // setup random date on Protoss side
    // AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    AccuroProvince province = AccuroProvince.ON;
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);

    prescriptionFromProtoss.setDin(Integer.toString(TestUtilities.nextId()));
    prescriptionFromProtoss.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));

    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    // mock dependency
    when(systemInformationManager.getProvince()).thenReturn(province);
    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);

    PrescriptionMedicationDto expected =
        mapDto(prescriptionFromProtoss, PrescriptionMedicationDto.class);

    PrescriptionDetailsDto prescriptionDetailsDto = getFixture(PrescriptionDetailsDto.class);
    when(prescriptionDetailsService.getPrescriptionDetails(prescriptionFromProtoss,
        false))
            .thenReturn(prescriptionDetailsDto);
    expected.setPrescriptionDetails(prescriptionDetailsDto);

    // mock and set dosages
    expected.setDosages(mockAndGetDosages(prescriptionId));

    // mock and set indications
    expected.setIndications(mockAndGetIndications(prescriptionId));

    // mock and set annotations
    expected.setAnnotations(mockAndGetAnnotations(prescriptionId));

    // mock and set status histories
    expected.setStatusHistories(mockAndGetStatusHistories(prescriptionId));

    // mock and set interactions
    expected.setInteractions(mockAndGetInteractions(prescriptionId));

    // mock and set Wellnet prescription links
    expected.setWellnetPrescriptionLinks(mockAndGetWellnetLinks(prescriptionId));

    if (AccuroProvince.ON.equals(province)) {
      int din = Integer.parseInt(expected.getDin());
      expected.setLimitedUseCodes(mockAndGetLimitedUseCodes(din));
    }

    // test
    PrescriptionMedicationDto actual =
        given()
            .pathParams("patientId", prescriptionFromProtoss.getPatientId())
            .pathParam("prescriptionId", prescriptionId)
            .queryParam("includeCcddMapping", false)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/prescriptions/{prescriptionId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getById(prescriptionId);
    verify(prescriptionDetailsService).getPrescriptionDetails(prescriptionFromProtoss,
        false);
    verify(dosageManager).getForPrescription(prescriptionId);
    verify(prescriptionIndicationManager).get(prescriptionId);
    verify(annotationManager).getForPrescription(prescriptionId);
    verify(statusHistoryManager).getByPrescriptionId(prescriptionId);
    verify(interactionManager).getForPrescription(prescriptionId);
    for (InteractionDto interaction : expected.getInteractions()) {
      verify(interactionManagementDetailsManager).getByInteractionId(interaction.getId());
    }
    verify(wellnetPrescriptionLinkManager).getByPrescriptionId(prescriptionId);
    if (province.equals(AccuroProvince.ON)) {
      int din = Integer.parseInt(expected.getDin());
      verify(limitedUseCodeManager).getLimitedUseCodeByDrugId(din);
    }
  }

  @Test
  public void testGetByPrescriptionIdWithNullCcddMapping() throws Exception {
    // setup random date on Protoss side
    // AccuroProvince province = TestUtilities.nextValue(AccuroProvince.class);
    AccuroProvince province = AccuroProvince.ON;
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);

    prescriptionFromProtoss.setDin(Integer.toString(TestUtilities.nextId()));
    prescriptionFromProtoss.setStartDate(TestUtilities.nextLocalDate().toString(datePattern));

    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    // mock dependency
    when(systemInformationManager.getProvince()).thenReturn(province);
    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);

    PrescriptionMedicationDto expected =
        mapDto(prescriptionFromProtoss, PrescriptionMedicationDto.class);

    PrescriptionDetailsDto prescriptionDetailsDto = getFixture(PrescriptionDetailsDto.class);
    when(prescriptionDetailsService.getPrescriptionDetails(prescriptionFromProtoss,
        false))
            .thenReturn(prescriptionDetailsDto);
    expected.setPrescriptionDetails(prescriptionDetailsDto);

    // mock and set dosages
    expected.setDosages(mockAndGetDosages(prescriptionId));

    // mock and set indications
    expected.setIndications(mockAndGetIndications(prescriptionId));

    // mock and set annotations
    expected.setAnnotations(mockAndGetAnnotations(prescriptionId));

    // mock and set status histories
    expected.setStatusHistories(mockAndGetStatusHistories(prescriptionId));

    // mock and set interactions
    expected.setInteractions(mockAndGetInteractions(prescriptionId));

    // mock and set Wellnet prescription links
    expected.setWellnetPrescriptionLinks(mockAndGetWellnetLinks(prescriptionId));

    if (AccuroProvince.ON.equals(province)) {
      int din = Integer.parseInt(expected.getDin());
      expected.setLimitedUseCodes(mockAndGetLimitedUseCodes(din));
    }

    // test
    PrescriptionMedicationDto actual =
        given()
            .pathParams("patientId", prescriptionFromProtoss.getPatientId())
            .pathParam("prescriptionId", prescriptionId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/prescriptions/{prescriptionId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getById(prescriptionId);
    verify(prescriptionDetailsService).getPrescriptionDetails(prescriptionFromProtoss,
        false);
    verify(dosageManager).getForPrescription(prescriptionId);
    verify(prescriptionIndicationManager).get(prescriptionId);
    verify(annotationManager).getForPrescription(prescriptionId);
    verify(statusHistoryManager).getByPrescriptionId(prescriptionId);
    verify(interactionManager).getForPrescription(prescriptionId);
    for (InteractionDto interaction : expected.getInteractions()) {
      verify(interactionManagementDetailsManager).getByInteractionId(interaction.getId());
    }
    verify(wellnetPrescriptionLinkManager).getByPrescriptionId(prescriptionId);
    if (province.equals(AccuroProvince.ON)) {
      int din = Integer.parseInt(expected.getDin());
      verify(limitedUseCodeManager).getLimitedUseCodeByDrugId(din);
    }
  }

  @Test
  public void testGetByPrescriptionIdNotFound() throws Exception {
    // setup random date on Protoss side
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);
    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(null);

    // test
    given()
        .pathParams("patientId", prescriptionFromProtoss.getPatientId())
        .pathParam("prescriptionId", prescriptionId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/prescriptions/{prescriptionId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(prescriptionMedicationManager).getById(prescriptionId);
  }

  @Test
  public void testGetByPrescriptionIdWithWrongPatient() throws Exception {
    // setup random date on Protoss side
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);
    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    // mock dependency
    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);

    // test
    given()
        .pathParams("patientId", prescriptionFromProtoss.getPatientId() + 1)
        .pathParam("prescriptionId", prescriptionId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/prescriptions/{prescriptionId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(prescriptionMedicationManager).getById(prescriptionId);
  }

  @Test
  public void testUpdateOrderStatus()
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException {
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);
    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    StatusHistory protosStatusHistory = getFixture(StatusHistory.class);
    protosStatusHistory.setPrescriptionId(prescriptionId);
    StatusHistoryDto statusHistoryDto = mapDto(protosStatusHistory, StatusHistoryDto.class);

    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);
    int patientId = prescriptionFromProtoss.getPatientId();
    given()
        .pathParams("patientId", patientId)
        .pathParam("prescriptionId", prescriptionId)
        .contentType(ContentType.JSON)
        .body(statusHistoryDto)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/"
            + "{prescriptionId}/order-statuses")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());

    verify(prescriptionMedicationManager).getById(prescriptionId);
    verify(prescriptionMedicationManager).updatePrescriptionStatus(protosStatusHistory);
  }

  @Test
  public void testUpdateOrderStatusPatientNotMatch()
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException {
    PrescriptionMedication prescriptionFromProtoss = getFixture(PrescriptionMedication.class);
    int prescriptionId = prescriptionFromProtoss.getPrescriptionId();

    StatusHistory protosStatusHistory = getFixture(StatusHistory.class);
    protosStatusHistory.setPrescriptionId(prescriptionId);
    StatusHistoryDto statusHistoryDto = mapDto(protosStatusHistory, StatusHistoryDto.class);

    when(prescriptionMedicationManager.getById(prescriptionId)).thenReturn(prescriptionFromProtoss);

    given()
        .pathParams("patientId", TestUtilities.nextInt())
        .pathParam("prescriptionId", prescriptionId)
        .contentType(ContentType.JSON)
        .body(statusHistoryDto)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/"
            + "{prescriptionId}/order-statuses")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(prescriptionMedicationManager).getById(prescriptionId);
    verifyNoInteractions(statusHistoryManager);

  }

  @Test
  public void testUpdateOrderStatusPrescriptionIdNotMatch() {

    StatusHistoryDto statusHistoryDto = getFixture(StatusHistoryDto.class);

    given()
        .pathParams("patientId", TestUtilities.nextInt())
        .pathParam("prescriptionId", TestUtilities.nextInt())
        .contentType(ContentType.JSON)
        .body(statusHistoryDto)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/"
            + "{prescriptionId}/order-statuses")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(prescriptionMedicationManager);
    verifyNoInteractions(statusHistoryManager);

  }

  private Set<DosageDto> mockAndGetDosages(int prescriptionId) throws ProtossException {
    List<Dosage> dosagesFromProtoss = getFixtures(Dosage.class, ArrayList::new, 5);
    when(dosageManager.getForPrescription(prescriptionId)).thenReturn(dosagesFromProtoss);
    return mapDto(dosagesFromProtoss, DosageDto.class, HashSet::new);
  }

  private Set<PrescriptionIndicationDto> mockAndGetIndications(int prescriptionId)
      throws ProtossException {
    Set<PrescriptionIndication> indicationsFromProtoss =
        getFixtures(PrescriptionIndication.class, HashSet::new, 5);
    when(prescriptionIndicationManager.get(prescriptionId)).thenReturn(indicationsFromProtoss);
    return mapDto(indicationsFromProtoss, PrescriptionIndicationDto.class, HashSet::new);
  }

  private List<AnnotationDto> mockAndGetAnnotations(int prescriptionId) throws ProtossException {
    List<Annotation> annotationsFromProtoss = getFixtures(Annotation.class, ArrayList::new, 5);
    when(annotationManager.getForPrescription(prescriptionId)).thenReturn(annotationsFromProtoss);
    return mapDto(annotationsFromProtoss, AnnotationDto.class, ArrayList::new);
  }

  private Set<StatusHistoryDto> mockAndGetStatusHistories(int prescriptionId)
      throws ProtossException {
    Set<StatusHistory> statusHistoriesFromProtoss =
        getFixtures(StatusHistory.class, HashSet::new, 5);
    when(statusHistoryManager.getByPrescriptionId(prescriptionId))
        .thenReturn(statusHistoriesFromProtoss);
    return mapDto(statusHistoriesFromProtoss, StatusHistoryDto.class, HashSet::new);
  }

  private Set<InteractionDto> mockAndGetInteractions(int prescriptionId)
      throws ProtossException {
    Set<Interaction> interactionsFromProtoss = getFixtures(Interaction.class, HashSet::new, 5);
    interactionsFromProtoss.forEach(i -> i.setPrescriptionId(prescriptionId));
    when(interactionManager.getForPrescription(prescriptionId))
        .thenReturn(interactionsFromProtoss);

    Set<InteractionDto> interactions = new HashSet<>();
    for (Interaction interaction : interactionsFromProtoss) {
      int interactionId = interaction.getId();
      Set<InteractionManagementDetails> interactionManagementDetailsFromProtoss =
          getFixtures(InteractionManagementDetails.class, HashSet::new, 5);
      interactionManagementDetailsFromProtoss.forEach(i -> {
        i.setPrescriptionId(prescriptionId);
        i.setInteractionId(interactionId);
      });
      when(interactionManagementDetailsManager.getByInteractionId(interactionId))
          .thenReturn(interactionManagementDetailsFromProtoss);
      InteractionDto interactionDto = mapDto(interaction, InteractionDto.class);
      interactionDto
          .setInteractionManagementDetails(mapDto(interactionManagementDetailsFromProtoss,
              InteractionManagementDetailsDto.class, HashSet::new));
      interactions.add(interactionDto);
    }
    return interactions;
  }

  private Set<WellnetPrescriptionLinkDto> mockAndGetWellnetLinks(int prescriptionId)
      throws ProtossException {
    Set<WellnetPrescriptionLink> wellnetPrescriptionLinksFromProtoss =
        getFixtures(WellnetPrescriptionLink.class, HashSet::new, 5);
    when(wellnetPrescriptionLinkManager.getByPrescriptionId(prescriptionId))
        .thenReturn(wellnetPrescriptionLinksFromProtoss);
    return mapDto(wellnetPrescriptionLinksFromProtoss, WellnetPrescriptionLinkDto.class,
        HashSet::new);
  }

  private List<LimitedUseCodeDto> mockAndGetLimitedUseCodes(int din) throws ProtossException {
    List<LimitedUseCode> limitedUseCodesFromProtoss =
        getFixtures(LimitedUseCode.class, ArrayList::new, 5);
    when(limitedUseCodeManager.getLimitedUseCodeByDrugId(din))
        .thenReturn(limitedUseCodesFromProtoss);
    return mapDto(limitedUseCodesFromProtoss, LimitedUseCodeDto.class, ArrayList::new);
  }

}
