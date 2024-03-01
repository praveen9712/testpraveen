
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.demographics.LocationManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.demographics.Location;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.ProviderEnrollmentTerminationReason;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PatientDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PhoneDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class PatientEndpointV2Test extends AbstractEndpointTest<PatientEndpointV2> {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private final PatientManager patientManager;
  private final SystemInformationManager systemInformationManager;
  private final LocationManager locationManager;
  private final AuditLogUser user;
  private final ApiSecurityContext context;

  public PatientEndpointV2Test() {
    super(new PatientEndpointV2(), PatientEndpointV2.class);
    patientManager = mock(PatientManager.class);
    systemInformationManager = mock(SystemInformationManager.class);
    locationManager = mock(LocationManager.class);

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
    servicesMap.put(PatientManager.class, patientManager);
    servicesMap.put(SystemInformationManager.class, systemInformationManager);
    servicesMap.put(LocationManager.class, locationManager);
    return servicesMap;
  }

  @Test
  public void testGetPatient() throws Exception {
    // setup random data
    Patient expected = getFixture(Patient.class);
    int patientId = RandomUtils.nextInt();
    expected.setPatientId(patientId);
    // mock dependencies
    when(patientManager.getPatientById(patientId)).thenReturn(expected);
    PatientDto expectedDto = mapDto(expected, PatientDto.class);

    // test
    PatientDto actual =
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientDto.class);
    // assertions
    assertEquals(expectedDto, actual);
    verify(patientManager).getPatientById(patientId);
  }

  @Test
  public void testGetPatientNull() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(404);
    verify(patientManager).getPatientById(patientId);
  }


  @Test
  public void testGetPatientsDefault() throws Exception {
    Envelope<Patient> protossEnvelope = getProtossEnvelope();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(patientManager)
        .getPatients(null, null, null, null, null, null, null, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual =
        given()
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/patients/search")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);
    // assertions
    EnvelopeDto<PatientDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(patientManager)
        .getPatients(null, null, null, null, null, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetPatientsWithUuidOnly() throws Exception {
    Envelope<Patient> protossEnvelope = getProtossEnvelope();
    String patientUuid = TestUtilities.nextString(35);
    // mock dependencies
    doReturn(protossEnvelope)
        .when(patientManager)
        .getPatients(null, null, null, null, null, patientUuid, null, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientUuid", patientUuid)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/patients/search")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);
    // assertions
    EnvelopeDto<PatientDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(patientManager)
        .getPatients(null, null, null, null, null, patientUuid, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetPatientsWithAllParams() throws Exception {

    String phn = TestUtilities.nextString(10);
    String firstName = TestUtilities.nextString(20);
    String lastName = TestUtilities.nextString(20);
    String phone = String.valueOf(TestUtilities.nextInt());
    String fileNumber = TestUtilities.nextString(10);
    String patientUuid = TestUtilities.nextString(35);

    int startingId =
        getProtossEnvelope().getContents().stream().min(Comparator.comparing(Patient::getPatientId))
            .orElseThrow(RuntimeException::new).getPatientId();

    Envelope<Patient> protossEnvelope = getProtossEnvelope();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(patientManager)
        .getPatients(phn, firstName, lastName, phone, fileNumber, patientUuid, startingId - 1,
            DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("phn", phn)
            .queryParam("firstName", firstName)
            .queryParam("lastName", lastName)
            .queryParam("phone", phone)
            .queryParam("fileNumber", fileNumber)
            .queryParam("patientUuid", patientUuid)
            .queryParam("startingId", startingId - 1)
            .queryParam("pageSize", DEFAULT_PAGE_SIZE)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/patients/search")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);
    // assertions
    EnvelopeDto<PatientDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(patientManager).getPatients(phn, firstName, lastName, phone, fileNumber, patientUuid,
        startingId - 1, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetPatientsWithValidationOnFirstName() throws Exception {
    given()
        .queryParam("firstName", "a")
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/patients/search")
        .then()
        .assertThat().statusCode(400);

    verify(patientManager, never()).getPatients(anyString(), anyString(), anyString(), anyString(),
        anyInt(), anyInt());
  }

  @Test
  public void testGetPatientsWithValidationOnLastName() throws Exception {
    given()
        .queryParam("lastName", "b")
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/patients/search")
        .then()
        .assertThat().statusCode(400);

    verify(patientManager, never()).getPatients(anyString(), anyString(), anyString(), anyString(),
        anyInt(), anyInt());
  }

  @Test
  public void testGetPatientsWithValidationOnPhone() throws Exception {
    given()
        .queryParam("phone", "12")
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/patients/search")
        .then()
        .assertThat().statusCode(400);

    verify(patientManager, never()).getPatients(anyString(), anyString(), anyString(), anyString(),
        anyInt(), anyInt());
  }

  @Test
  public void testCreatePatient() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().setNextKinPhone(phoneDto);
    formatPhoneNumber(patientDto);

    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int expected = patientDto.getPatientId();
    doReturn(expected).when(patientManager).createPatient(any(), any());

    int actual =
        given()
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v2/provider-portal/patients")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);

    assertEquals(expected, actual);
    verify(systemInformationManager).getProvince();
    verify(patientManager).createPatient(any(), any());
  }

  @Test
  public void testCreatePatientNextKinPhone() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    formatPhoneNumber(patientDto);

    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int expected = patientDto.getPatientId();
    doReturn(expected).when(patientManager).createPatient(any(), any());

    given()
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v2/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager).getProvince();
  }

  @Test
  public void testCreatePatientWithEmptyPhoneNumber() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber("");
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().setNextKinPhone(phoneDto);
    formatPhoneNumber(patientDto);

    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int expected = patientDto.getPatientId();
    doReturn(expected).when(patientManager).createPatient(any(), any());

    int actual =
        given()
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v2/provider-portal/patients")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);

    assertEquals(expected, actual);
    verify(systemInformationManager).getProvince();
    verify(patientManager).createPatient(any(), any());
  }

  @Test
  public void testCreatePatientWithInvalidAdmissionDateOnOntario() throws Exception {
    AccuroProvince province = AccuroProvince.ON;
    PatientDto patientDto = generatePatientDto(province);
    LocalDate dischargeDate = new LocalDate(patientDto.getOntarioDetails().getAdmissionDate());
    patientDto.getOntarioDetails()
        .setDischargeDate(dischargeDate.minusDays(TestUtilities.nextInt(10) + 1));
    Patient patient = mapDto(patientDto, Patient.class);

    int expected = patient.getPatientId();
    doReturn(expected).when(patientManager).createPatient(patient, user);
    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    given()
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v2/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager).getProvince();
    verify(locationManager, never()).getLocationsById(Collections.singletonList(locationId));
    verify(patientManager, never()).createPatient(any(), any());
  }

  @Test
  public void testCreatePatientWithNullPatient() throws Exception {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v2/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).createPatient(any(), any());
  }

  @Test
  public void testUpdatePatient() throws Exception {
    AccuroProvince province = randomProvince();
    doReturn(province).when(systemInformationManager).getProvince();

    PatientDto patientDto = generatePatientDto(province);
    formatPhoneNumber(patientDto);

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int patientId = patientDto.getPatientId();
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);

    assertEquals(true, actual);
    verify(systemInformationManager).getProvince();
    verify(patientManager).updatePatient(any(), any(), any());
  }

  @Test
  public void testUpdatePatientNextKinPhone() throws Exception {
    AccuroProvince province = randomProvince();
    doReturn(province).when(systemInformationManager).getProvince();

    PatientDto patientDto = generatePatientDto(province);
    formatPhoneNumber(patientDto);

    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int patientId = patientDto.getPatientId();

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager).getProvince();
  }

  @Test
  public void testUpdatePatientWithEmptyPhoneNumber() throws Exception {
    AccuroProvince province = randomProvince();
    doReturn(province).when(systemInformationManager).getProvince();

    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber("");
    formatPhoneNumber(patientDto);

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    int patientId = patientDto.getPatientId();
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);

    assertEquals(true, actual);
    verify(systemInformationManager).getProvince();
    verify(patientManager).updatePatient(any(), any(), any());
  }

  @Test
  public void testUpdatePatientWithDifferentPatientId() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);

    given()
        .pathParam("patientId", TestUtilities.nextId())
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testUpdatePatientWithNullPatient() throws Exception {
    given()
        .pathParam("patientId", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatient() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    Patient patient = mapDto(patientDto, Patient.class);

    int patientId = patient.getPatientId();

    doReturn(patient).when(patientManager).getPatientById(patientId);
    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(patientManager).getPatientById(patientId);
    verify(systemInformationManager).getProvince();
    verify(patientManager).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatientNextKinPhone() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    Patient patient = mapDto(patientDto, Patient.class);

    int patientId = patient.getPatientId();

    doReturn(patient).when(patientManager).getPatientById(patientId);
    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void testPatchPatientWithValidPhoneNumber() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    Patient patient = mapDto(patientDto, Patient.class);

    int patientId = patient.getPatientId();

    doReturn(patient).when(patientManager).getPatientById(patientId);
    doReturn(province).when(systemInformationManager).getProvince();

    Integer locationId = patientDto.getDemographics().getHealthCard().getLocationId();
    Location location = getFixture(Location.class);
    location.setLocationId(locationId);
    doReturn(Collections.singletonList(location)).when(locationManager)
        .getLocationsById(Collections.singletonList(locationId));

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(patientManager).getPatientById(patientId);
    verify(systemInformationManager).getProvince();
    verify(patientManager).updatePatient(any(), any(), any());
  }


  @Test
  public void testPatchPatientWithInvalidEntity() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    patientDto.setPaperChartNote(TestUtilities.nextString(1000));
    Patient patient = mapDto(patientDto, Patient.class);

    int patientId = patient.getPatientId();

    doReturn(patient).when(patientManager).getPatientById(patientId);

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientManager).getPatientById(patientId);
    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatientWithInvalidPhoneNumber() throws Exception {
    AccuroProvince province = randomProvince();
    PatientDto patientDto = generatePatientDto(province);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(9, false, true));
    patientDto.setPaperChartNote(TestUtilities.nextString(1000));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    Patient patient = mapDto(patientDto, Patient.class);

    int patientId = patient.getPatientId();

    doReturn(patient).when(patientManager).getPatientById(patientId);

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientManager).getPatientById(patientId);
    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatientWithInvalidPatientId() throws Exception {
    int patientId = TestUtilities.nextId();

    given()
        .pathParam("patientId", patientId)
        .body(generatePatientDto(TestUtilities.nextValue(AccuroProvince.class)))
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientManager).getPatientById(patientId);
    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatientWithNullBody() throws Exception {
    given()
        .pathParam("patientId", TestUtilities.nextId())
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v2/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientManager, never()).getPatientById(anyInt());
    verify(systemInformationManager, never()).getProvince();
    verify(locationManager, never()).getLocationsById(any());
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  private PatientDto generatePatientDto(AccuroProvince province) {
    PatientDto patientDto = getFixture(PatientDto.class);

    if (AccuroProvince.ON.equals(province)) {
      patientDto.getDemographics().getHealthCard().setPhn(randomStringNumber(12));
      LocalDate admissionDate = TestUtilities.nextLocalDate();
      LocalDate dischargeDate = new LocalDate(admissionDate);
      dischargeDate.plusMonths(TestUtilities.nextInt(5) + 1);
      patientDto.getOntarioDetails().setAdmissionDate(admissionDate);
      patientDto.getOntarioDetails().setDischargeDate(dischargeDate);
    } else if (AccuroProvince.MB.equals(province)) {
      String phn = TestUtilities.nextString(TestUtilities.nextInt(2));
      int l = phn.length();
      phn += randomStringNumber(9 - l);
      patientDto.getDemographics().getHealthCard().setPhn(phn);
    } else if (AccuroProvince.BC.equals(province) || AccuroProvince.NS.equals(province)) {
      patientDto.getDemographics().getHealthCard().setPhn(randomStringNumber(10));
    } else if (AccuroProvince.AB.equals(province) || AccuroProvince.SK.equals(province)) {
      patientDto.getDemographics().getHealthCard().setPhn(randomStringNumber(9));
    }

    patientDto.setEnrolledProvideTerminationReason(TestUtilities.nextValue(
        ProviderEnrollmentTerminationReason.class));

    return patientDto;
  }

  private String randomStringNumber(int l) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < l; i++) {
      result.append(TestUtilities.nextInt(10));
    }
    return result.toString();
  }

  private AccuroProvince randomProvince() {
    List<AccuroProvince> accuroProvinces = new ArrayList<>();
    for (AccuroProvince province : AccuroProvince.values()) {
      if (!AccuroProvince.Unknown.equals(province)) {
        accuroProvinces.add(province);
      }
    }
    return TestUtilities.nextElement(accuroProvinces);
  }

  private EnvelopeDto<PatientDto> refType(EnvelopeDto rawEnvelope) {
    List<PatientDto> contents = getObjectMapper().convertValue(rawEnvelope.getContents(),
        new TypeReference<List<PatientDto>>() {});
    EnvelopeDto<PatientDto> e = (EnvelopeDto<PatientDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  private Envelope<Patient> getProtossEnvelope() {
    // setup random data
    List<Patient> protossResults =
        getFixtures(Patient.class, ArrayList::new, 10);
    Envelope<Patient> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(Patient::getPatientId))
        .orElseThrow(RuntimeException::new).getPatientId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setTotal(protossResults.size());
    return protossEnvelope;
  }

  private EnvelopeDto<PatientDto> getExpected(
      Envelope<Patient> protossEnvelope) {
    List<PatientDto> patients =
        mapDto(protossEnvelope.getContents(), PatientDto.class,
            ArrayList::new);

    EnvelopeDto<PatientDto> expected = new EnvelopeDto<>();

    expected.setContents(patients);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  private void formatPhoneNumber(PatientDto patientDto) {
    if (patientDto.getDemographics().getPhones() != null) {
      patientDto.getDemographics()
          .setPhones(patientDto.getDemographics().getPhones().stream().map(phoneDto -> {
            if (StringUtils.isNotBlank(phoneDto.getNumber())) {
              phoneDto.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(phoneDto.getNumber()));
            }
            return phoneDto;
          }).collect(Collectors.toUnmodifiableList()));
    }
  }
}
