
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.customfield.CustomFieldManager;
import com.qhrtech.emr.accuro.api.customfield.PatientCustomFieldManager;
import com.qhrtech.emr.accuro.api.logging.LogManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientProfilePictureManager;
import com.qhrtech.emr.accuro.model.customfield.CustomField;
import com.qhrtech.emr.accuro.model.customfield.PatientCustomField;
import com.qhrtech.emr.accuro.model.demographics.Email;
import com.qhrtech.emr.accuro.model.demographics.Flag;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.logging.PatientActivity;
import com.qhrtech.emr.accuro.model.patient.Alias;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.patient.PatientProfilePicture;
import com.qhrtech.emr.accuro.model.patient.PatientProviderEnrollmentTerminationReason;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.config.serialization.AccuroCalendarBasedDeserializer;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AliasDto;
import com.qhrtech.emr.restapi.models.dto.EmailDto;
import com.qhrtech.emr.restapi.models.dto.PatientActivityDto;
import com.qhrtech.emr.restapi.models.dto.PatientActivityType;
import com.qhrtech.emr.restapi.models.dto.PatientDto;
import com.qhrtech.emr.restapi.models.dto.PatientFlagDto;
import com.qhrtech.emr.restapi.models.dto.PhoneDto;
import com.qhrtech.emr.restapi.models.dto.ProviderEnrollmentTerminationReason;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
import io.restassured.http.ContentType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PatientEndpointTest extends AbstractEndpointTest<PatientEndpoint> {

  private static final int MAX_DEFAULT_WIDTH = 100;
  private static final int MAX_DEFAULT_HEIGHT = 100;

  private final PatientManager patientManager;
  private final PatientCustomFieldManager patientCustomFieldManager;
  private final CustomFieldManager customFieldManager;
  private final LogManager logManager;
  private final PatientProfilePictureManager profilePictureManagerMock;
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  ApiSecurityContext context;
  private AuditLogUser user;

  public PatientEndpointTest() {
    super(new PatientEndpoint(), PatientEndpoint.class);
    patientManager = mock(PatientManager.class);
    patientCustomFieldManager = mock(PatientCustomFieldManager.class);
    customFieldManager = mock(CustomFieldManager.class);
    logManager = mock(LogManager.class);
    profilePictureManagerMock = mock(PatientProfilePictureManager.class);

    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
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
    servicesMap.put(PatientCustomFieldManager.class, patientCustomFieldManager);
    servicesMap.put(LogManager.class, logManager);
    servicesMap.put(PatientProfilePictureManager.class, profilePictureManagerMock);
    servicesMap.put(CustomFieldManager.class, customFieldManager);
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
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
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
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(404);
    verify(patientManager).getPatientById(patientId);
  }

  @Test
  public void testCreatePatient() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().setNextKinPhone(phoneDto);
    formatPhoneNumber(patientDto);
    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.createPatientBypassValidation(patient, user))
        .thenReturn(expectedId);

    // test
    Integer actualId =
        given()
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);
    // assertions
    assertEquals(actualId, expectedId);
    verify(patientManager).createPatientBypassValidation(patient, user);
  }

  @Test
  public void testCreatePatientWithNextKinPhone() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    formatPhoneNumber(patientDto);
    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.createPatientBypassValidation(patient, user))
        .thenReturn(expectedId);

    // test

    given()
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400);


  }

  @Test
  public void testCreatePatientWithEmptyPhoneNumber() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber("");
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    patientDto.getDemographics().setNextKinPhone(phoneDto);
    formatPhoneNumber(patientDto);
    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.createPatientBypassValidation(patient, user))
        .thenReturn(expectedId);

    // test
    Integer actualId =
        given()
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);
    // assertions
    assertEquals(actualId, expectedId);
    verify(patientManager).createPatientBypassValidation(patient, user);
  }


  @Test
  public void testCreatPatientPatientNull() throws Exception {

    Object response = given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Patient information is missing"));
    // assertions
    verify(patientManager, never()).createPatient(any(), any());
  }

  @Test
  public void testCreatPatientLocationNull() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    patientDto.getDemographics().getAddresses().get(2).setLocationId(null);

    Object response = given()
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("Location id on non primary or secondary address must be supplied"));
    // assertions
    verify(patientManager, never()).createPatient(any(), any());
  }

  @Test
  public void updatePatient() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    patientDto.setUuid(null);
    formatPhoneNumber(patientDto);
    int patientId = patientDto.getPatientId();
    patientDto.setEnrolledProvideTerminationReason(
        ProviderEnrollmentTerminationReason.ADDED_IN_ERROR);
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientBypassValidation(mapDto(patientDto, Patient.class), user,
        PatientProviderEnrollmentTerminationReason
            .lookupByCode(patientDto.getEnrolledProvideTerminationReason().getCode()));
  }

  @Test
  public void updatePatientWithNextKinPhone() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    patientDto.setUuid(null);
    formatPhoneNumber(patientDto);
    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    int patientId = patientDto.getPatientId();
    patientDto.setEnrolledProvideTerminationReason(
        ProviderEnrollmentTerminationReason.ADDED_IN_ERROR);
    // test

    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void updatePatientWithEmptyPhoneNumber() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    patientDto.setUuid(null);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber("");
    patientDto.getDemographics()
        .setPhones(Collections.singletonList(phoneDto));
    int patientId = patientDto.getPatientId();
    patientDto.setEnrolledProvideTerminationReason(
        ProviderEnrollmentTerminationReason.ADDED_IN_ERROR);
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(patientDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientBypassValidation(mapDto(patientDto, Patient.class), user,
        PatientProviderEnrollmentTerminationReason
            .lookupByCode(patientDto.getEnrolledProvideTerminationReason().getCode()));
  }

  @Test
  public void updatePatientNullPatient() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = patientDto.getPatientId();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Patient information is missing"));
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void updatePatientNotEqualPatientId() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid patient ID"));
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testUpdatePatientLocationNull() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = patientDto.getPatientId();
    patientDto.getDemographics().getAddresses().get(2).setLocationId(null);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString()
        .contains("Location id on non primary or secondary address must be supplied"));
    verify(patientManager, never()).updatePatient(any(), any(), any());
  }

  @Test
  public void testPatchPatientEndpointWithFullObject() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = patientDto.getPatientId();
    patientDto.setUuid(null);

    patientDto.setEnrolledProvideTerminationReason(
        ProviderEnrollmentTerminationReason.ADDED_IN_ERROR);

    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(patientManager).updatePatientBypassValidation(mapDto(patientDto, Patient.class), user,
        PatientProviderEnrollmentTerminationReason
            .lookupByCode(patientDto.getEnrolledProvideTerminationReason().getCode()));
  }

  @Test
  public void testPatchPatientEndpointWithNextKinPhone() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);

    patientDto.getDemographics().getNextKinPhone().setNumber("12345");
    patientDto.setEnrolledProvideTerminationReason(
        ProviderEnrollmentTerminationReason.ADDED_IN_ERROR);
    int patientId = patientDto.getPatientId();
    patientDto.setUuid(null);
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void testPatchPatientEndpointWithInvalidPhoneNumbeer() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(9, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    int patientId = patientDto.getPatientId();
    patientDto.setUuid(null);

    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(patientManager, times(0)).updatePatient(mapDto(patientDto, Patient.class), user, null);
  }

  @Test
  public void testPatchPatientEndpointWithValidPhoneNumbeer() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber(RandomStringUtils.random(10, false, true));
    patientDto.getDemographics().setPhones(Collections.singletonList(phoneDto));
    int patientId = patientDto.getPatientId();
    patientDto.setUuid(null);
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patientDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(patientManager, times(0)).updatePatient(mapDto(patientDto, Patient.class), user, null);

  }


  @Test
  public void testPatchPatientEndpoint() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    patientDto.setNovaScotiaDetails(null);
    patientDto.setUuid(null);
    int patientId = patientDto.getPatientId();

    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    PatchMerge patchMerge = new PatchMerge();
    patchMerge.setPatientId(patientId);
    patchMerge.setOccupation("TestOccupation");
    patientDto.setOccupation(patchMerge.getOccupation());

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patchMerge)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(patientManager, times(0)).updatePatient(mapDto(patientDto, Patient.class), user, null);
  }

  @Test
  public void testPatchPatientEndpointPatientIdNotMatch() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = patientDto.getPatientId();
    Patient patient = mapDto(patientDto, Patient.class);

    when(patientManager.getPatientById(patientId)).thenReturn(patient);

    PatchMerge patchMerge = new PatchMerge();
    patchMerge.setPatientId(RandomUtils.nextInt());
    patchMerge.setOccupation("TestOccupation");
    patientDto.setOccupation(patchMerge.getOccupation());

    // test
    given()
        .pathParam("patientId", patientId)
        .body(patchMerge)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(patientManager, times(0)).updatePatient(mapDto(patientDto, Patient.class), user, null);

  }

  @Test
  public void testGetPatientEmails() throws Exception {
    // setup random data
    PatientDto patientDto = getFixture(PatientDto.class);
    int patientId = patientDto.getPatientId();
    List<Email> protossResults =
        getFixtures(Email.class, ArrayList::new, 2);
    List<EmailDto> expected =
        mapDto(protossResults, EmailDto.class, ArrayList::new);
    // mock dependencies
    when(patientManager.getPatientEmails(patientId)).thenReturn(protossResults);

    // test
    List<EmailDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EmailDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(patientManager).getPatientEmails(patientId);
  }

  @Test
  public void testGetPatientEmailsPatientIdNull() throws Exception {
    // setup random data
    Integer patientId = RandomUtils.nextInt() * -1;

    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientEmails(anyInt());
  }

  @Test
  public void testUpdatePatientEmail() throws Exception {
    // setup random data
    EmailDto emailDto = getFixture(EmailDto.class);
    int patientId = RandomUtils.nextInt();

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(emailDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientEmail(patientId, mapDto(emailDto, Email.class));
  }

  @Test
  public void testUpdatePatientEmailNegativePatientId() throws Exception {
    // setup random data
    EmailDto emailDto = getFixture(EmailDto.class);
    int patientId = RandomUtils.nextInt() * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(emailDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientEmail(anyInt(), any());

  }

  @Test
  public void testUpdatePatientEmailPatientEmailNull() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Patient email information is missing"));
    verify(patientManager, never()).updatePatientEmail(anyInt(), any());
  }

  @Test
  public void testCreatePatientEmail() throws Exception {
    // setup random data
    Email email = getFixture(Email.class);
    int patientId = RandomUtils.nextInt();
    EmailDto emailDto = mapDto(email, EmailDto.class);
    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    when(patientManager.createPatientEmail(patientId, email)).thenReturn(expectedId);

    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .body(emailDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(patientManager).createPatientEmail(patientId, email);
  }

  @Test
  public void testCreatePatientEmailNegativePatiendId() throws Exception {
    // setup random data
    Email email = getFixture(Email.class);
    int patientId = RandomUtils.nextInt() * -1;
    EmailDto emailDto = mapDto(email, EmailDto.class);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(emailDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientEmail(anyInt(), any());
  }

  @Test
  public void testCreatePatientEmailNullPatientEmail() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Patient email information is missing"));
    verify(patientManager, never()).updatePatientEmail(anyInt(), any());
  }

  @Test
  public void testDeletePatientEmail() {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Integer emailId = RandomUtils.nextInt();

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .queryParam("emailId", emailId)
            .when()
            .contentType(ContentType.JSON)
            .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
  }

  @Test
  public void testDeletePatientEmailNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Integer emailId = RandomUtils.nextInt();

    given()
        .pathParam("patientId", patientId)
        .queryParam("emailId", emailId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/emails")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).deletePatientEmail(anyInt());
  }

  @Test
  public void testGetPatientPhones() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    List<Phone> protossResults =
        getFixtures(Phone.class, ArrayList::new, 2);
    List<PhoneDto> expected =
        mapDto(protossResults, PhoneDto.class, ArrayList::new);

    // mock dependencies
    when(patientManager.getPatientPhones(patientId)).thenReturn(protossResults);
    // test
    List<PhoneDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PhoneDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(patientManager).getPatientPhones(anyInt());
  }

  @Test
  public void testGetPatientPhonesNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientPhones(anyInt());
  }

  @Test
  public void testUpdatePatientPhone() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    PhoneDto phoneDto = getFixture(PhoneDto.class);

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(phoneDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientPhone(patientId, mapDto(phoneDto, Phone.class));
  }

  @Test
  public void testUpdatePatientPhoneNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    PhoneDto phoneDto = getFixture(PhoneDto.class);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(phoneDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientPhone(anyInt(), any());
  }

  @Test
  public void testUpdatePatientPhoneNullPhoneDto() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing phone information"));
    verify(patientManager, never()).updatePatientPhone(anyInt(), any());
  }

  @Test
  public void testUpdatePatientPhoneEmptyPhoneNumber() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    PhoneDto phoneDto = getFixture(PhoneDto.class);
    phoneDto.setNumber("");

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(phoneDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientPhone(patientId, mapDto(phoneDto, Phone.class));
  }

  @Test
  public void testCreatePatientPhone() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Phone phone = getFixture(Phone.class);

    String number =
        PhoneNumberFormatterUtils.formatPhoneNumber(RandomStringUtils.random(10, false, true));
    phone.setNumber(number);
    PhoneDto phoneDto = mapDto(phone, PhoneDto.class);

    Integer id = RandomUtils.nextInt();

    // mock dependencies
    when(patientManager.createPatientPhone(patientId, phone)).thenReturn(id);
    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .body(phoneDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(id, actual);
    verify(patientManager).createPatientPhone(patientId, phone);
  }

  @Test
  public void testCreatePatientPhoneNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Phone phone = getFixture(Phone.class);
    PhoneDto phoneDto = mapDto(phone, PhoneDto.class);
    phoneDto.setNumber(null);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(phoneDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).createPatientPhone(anyInt(), any());
  }

  @Test
  public void testCreatePatientPhoneNullPatientPhone() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing phone information"));
    verify(patientManager, never()).createPatientPhone(anyInt(), any());
  }

  @Test
  public void testDeletePatientPhone() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Integer phoneId = RandomUtils.nextInt();

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .queryParam("phoneId", phoneId)
            .when()
            .contentType(ContentType.JSON)
            .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).deletePatientPhone(phoneId);
  }

  @Test
  public void testDeletePatientPhoneNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Integer phoneId = RandomUtils.nextInt();

    // test
    given()
        .pathParam("patientId", patientId)
        .queryParam("phoneId", phoneId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/phones")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
  }

  @Test
  public void testGetPatientAliases() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    List<Alias> protossResults =
        getFixtures(Alias.class, ArrayList::new, 2);
    List<AliasDto> expected =
        mapDto(protossResults, AliasDto.class, ArrayList::new);

    // mock dependencies
    when(patientManager.getPatientAliases(patientId)).thenReturn(protossResults);
    // test
    List<AliasDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AliasDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(patientManager).getPatientAliases(patientId);
  }

  @Test
  public void testGetPatientAliasesNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientAliases(anyInt());
  }

  @Test
  public void testUpdatePatientAlias() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    AliasDto aliasDto = getFixture(AliasDto.class);
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(aliasDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientAlias(mapDto(aliasDto, Alias.class));
  }

  @Test
  public void testUpdatePatientAliasNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    AliasDto aliasDto = getFixture(AliasDto.class);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(aliasDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID."));
    // assertions
    verify(patientManager, never()).updatePatientAlias(any());
  }

  @Test
  public void testUpdatePatientAliasNullpatientAlias() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Missing alias information"));
    // assertions
    verify(patientManager, never()).updatePatientAlias(any());
  }

  @Test
  public void testCreatePatientAlias() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Alias alias = getFixture(Alias.class);
    AliasDto aliasDto = mapDto(alias, AliasDto.class);
    Integer id = RandomUtils.nextInt();
    // mock dependencies
    when(patientManager.createPatientAlias(patientId, alias)).thenReturn(id);
    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .body(aliasDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(id, actual);
    verify(patientManager).createPatientAlias(patientId, alias);
  }

  @Test
  public void testCreatePatientAliasNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Alias alias = getFixture(Alias.class);
    AliasDto aliasDto = mapDto(alias, AliasDto.class);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(aliasDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).createPatientAlias(anyInt(), any());
  }

  @Test
  public void testCreatePatientAliasNullPatientAlias() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing alias information"));
    verify(patientManager, never()).createPatientAlias(anyInt(), any());
  }

  @Test
  public void testDeletePatientAlias() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    int aliasId = RandomUtils.nextInt();
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .queryParam("aliasId", aliasId)
            .when()
            .contentType(ContentType.JSON)
            .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).deletePatientAlias(aliasId);
  }

  @Test
  public void testDeletePatientAliasNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    int aliasId = RandomUtils.nextInt();
    // test
    given()
        .pathParam("patientId", patientId)
        .queryParam("aliasId", aliasId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/aliases")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).deletePatientAlias(anyInt());
  }

  @Test
  public void testGetPatientPrivateInsurerIds() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Set<Integer> expectedResults = getFixtures(Integer.class, HashSet::new, 2);
    // mock dependencies
    when(patientManager.getPatientPrivateInsurerIDs(patientId)).thenReturn(expectedResults);
    // test
    Set<Integer> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/insurers")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer[].class),
        HashSet::new);
    // assertions
    assertEquals(expectedResults, actual);
    verify(patientManager).getPatientPrivateInsurerIDs(patientId);
  }

  @Test
  public void testGetPatientPrivateInsurerIdsNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/insurers")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientPrivateInsurerIDs(anyInt());
  }

  @Test
  public void testUpdatePatientPrivateInsurerIds() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Set<Integer> expectedResults = getFixtures(Integer.class, HashSet::new, 2);
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(expectedResults)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/insurers")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientPrivateInsurerIDs(patientId, expectedResults);
  }

  @Test
  public void testUpdatePatientPrivateInsurerIdsNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Set<Integer> expectedResults = getFixtures(Integer.class, HashSet::new, 2);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(expectedResults)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/insurers")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientPrivateInsurerIDs(anyInt(),
        anySetOf(Integer.class));
  }

  @Test
  public void testUpdatePatientPrivateInsurerIdsEmptyPrivateInsurerIDs() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/insurers")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing insurer id's"));
    verify(patientManager, never()).updatePatientPrivateInsurerIDs(anyInt(),
        anySetOf(Integer.class));
  }

  @Test
  public void testGetPatientUserFlags() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Map<Integer, Flag> flagMap = new HashMap<>();
    Flag flag = getFixture(Flag.class);
    // set flagUser to null as the result does not include this field.
    flag.setFlagUser(null);
    flagMap.put(RandomUtils.nextInt(), flag);
    Map<Integer, PatientFlagDto> expectedPatientUserFlagsDto =
        mapDto(flagMap, PatientFlagDto.class, HashMap::new);
    // mock dependencies
    when(patientManager.getPatientUserFlags(patientId)).thenReturn(flagMap);

    // test
    Map<String, LinkedHashMap<String, String>> actual =
        given()
            .pathParam("patientId", patientId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/user-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(expectedPatientUserFlagsDto.getClass());

    Map<Integer, PatientFlagDto> actualMap = new HashMap<>();
    for (String key : actual.keySet()) {
      PatientFlagDto patientFlagDto = new PatientFlagDto();
      LinkedHashMap<String, String> linkedHashMap = actual.get(key);
      patientFlagDto.setMessage(linkedHashMap.get("message"));
      AccuroCalendar calendar =
          AccuroCalendarBasedDeserializer.parse(linkedHashMap.get("lastUpdated"), null);
      patientFlagDto.setLastUpdated(calendar.getCalender());
      actualMap.put(Integer.parseInt(key), patientFlagDto);
    }
    // assertions
    Assert.assertTrue(expectedPatientUserFlagsDto.equals(actualMap));
    verify(patientManager).getPatientUserFlags(patientId);
  }

  @Test
  public void testGetPatientUserFlagsNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientUserFlags(anyInt());
  }

  @Test
  public void testUpdatePatientUserFlags() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    PatientFlagDto patientFlagDto = getFixture(PatientFlagDto.class);
    Map<Integer, PatientFlagDto> userFlagsMap = new HashMap<>();
    userFlagsMap.put(RandomUtils.nextInt(), patientFlagDto);
    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(userFlagsMap)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/user-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager, never()).getPatientUserFlags(patientId);
  }

  @Test
  public void testUpdatePatientUserFlagsNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    PatientFlagDto patientFlagDto = getFixture(PatientFlagDto.class);
    Map<Integer, PatientFlagDto> userFlagsMap = new HashMap<>();
    userFlagsMap.put(RandomUtils.nextInt(), patientFlagDto);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(userFlagsMap)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).getPatientUserFlags(anyInt());
  }

  @Test
  public void testUpdatePatientUserFlagsNullUserFlags() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing user flags"));
    verify(patientManager, never()).getPatientUserFlags(anyInt());
  }

  @Test
  public void testGetPatientRoleFlags() throws Exception {
    // setup random data
    Map<Integer, Flag> flagMap = new HashMap<>();
    Flag flag = getFixture(Flag.class);
    // set flagUser to null as the result does not include this field.
    flag.setFlagUser(null);
    flagMap.put(RandomUtils.nextInt(), flag);
    Map<Integer, PatientFlagDto> expectedPatientRoleFlagsDto =
        mapDto(flagMap, PatientFlagDto.class, HashMap::new);
    int patientId = RandomUtils.nextInt();
    // mock dependencies
    when(patientManager.getPatientRoleFlags(anyInt())).thenReturn(flagMap);
    // test
    Map<String, LinkedHashMap<String, String>> actual =
        given()
            .pathParam("patientId", patientId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/role-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(expectedPatientRoleFlagsDto.getClass());
    Map<Integer, PatientFlagDto> actualMap = new HashMap<>();
    for (String key : actual.keySet()) {
      PatientFlagDto patientFlagDto = new PatientFlagDto();
      LinkedHashMap<String, String> linkedHashMap = actual.get(key);
      patientFlagDto.setMessage(linkedHashMap.get("message"));
      AccuroCalendar calendar =
          AccuroCalendarBasedDeserializer.parse(linkedHashMap.get("lastUpdated"), null);
      patientFlagDto.setLastUpdated(calendar.getCalender());
      actualMap.put(Integer.parseInt(key), patientFlagDto);
    }
    // assertions
    Assert.assertTrue(expectedPatientRoleFlagsDto.equals(actualMap));
    verify(patientManager).getPatientRoleFlags(patientId);
  }

  @Test
  public void testGetPatientRoleFlagsNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/role-flags")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientRoleFlags(anyInt());
  }

  @Test
  public void testUpdatePatientRoleFlags() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    Map<Integer, PatientFlagDto> flagMap = new HashMap<>();
    PatientFlagDto patientFlagDto = getFixture(PatientFlagDto.class);
    flagMap.put(RandomUtils.nextInt(), patientFlagDto);

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(flagMap)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/role-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientRoleFlags(patientId,
        mapDto(flagMap, Flag.class, HashMap::new));
  }

  @Test
  public void testUpdatePatientRoleFlagsNullPatiendId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Map<Integer, PatientFlagDto> flagMap = new HashMap<>();
    PatientFlagDto patientFlagDto = getFixture(PatientFlagDto.class);
    flagMap.put(RandomUtils.nextInt(), patientFlagDto);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(flagMap)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/role-flags")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientRoleFlags(anyInt(), anyMap());
  }

  @Test
  public void testUpdatePatientRoleFlagsNullRoleFlags() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/role-flags")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing role flags"));
    verify(patientManager, never()).updatePatientRoleFlags(anyInt(), anyMap());
  }

  @Test
  public void testGetPatientCustomProperties() throws Exception {
    // setup random data
    final int patientId = RandomUtils.nextInt();
    final int officeId = RandomUtils.nextInt();

    // Setup Custom Fields
    PatientCustomField customField1 = new PatientCustomField();
    PatientCustomField customField2 = new PatientCustomField();
    customField1.setName(RandomStringUtils.random(10));
    customField1.setValue(RandomStringUtils.random(10));
    customField2.setName(RandomStringUtils.random(10));
    customField2.setValue(RandomStringUtils.random(10));
    List<PatientCustomField> customFields = new ArrayList<>();
    customFields.add(customField1);
    customFields.add(customField2);

    Map<String, String> expected = mapCustomFields(customFields);

    // mock dependencies
    when(patientCustomFieldManager.getPatientCustomFields(patientId, officeId, user))
        .thenReturn(customFields);
    // test
    Map<String, String> actual =
        given()
            .pathParam("patientId", patientId)
            .queryParam("officeId", officeId)
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(expected.getClass());
    // assertions
    assertEquals(expected, actual);
    verify(patientCustomFieldManager).getPatientCustomFields(patientId, officeId, user);
  }

  @Test
  public void testGetPatientCustomPropertiesNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).getPatientCustomProperties(anyInt());
  }

  @Test
  public void testGetPatientCustomPropertiesForClientCredentials() throws Exception {
    // setup random data

    context.setUser(new AuditLogUser(null, 1, "testId", "TestName", "TestInfo"));
    int patientId = RandomUtils.nextInt();
    int officeId = RandomUtils.nextInt();

    // test
    given()
        .pathParam("patientId", patientId)
        .queryParam("officeId", officeId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400).extract();
    // assertions
    verifyNoInteractions(patientManager);
  }


  @Test
  public void testPatientLabs() throws Exception {
    int patientId = RandomUtils.nextInt() * -1;
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void testUpdatePatientCustomProperties() throws Exception {
    // setup random data

    Map<String, String> customPropertiesMap = new HashMap<>();
    String key1 = RandomStringUtils.random(10);
    String value1 = RandomStringUtils.random(10);
    customPropertiesMap.put(key1, value1);

    CustomField customField = new CustomField();
    customField.setName(key1);

    when(customFieldManager.getCustomFields(null, true, user))
        .thenReturn(Collections.singletonList(customField));

    Map<String, String> allCustomProperties = new HashMap<>();
    String key2 = RandomStringUtils.random(10);
    String value2 = RandomStringUtils.random(10);
    allCustomProperties.put(key2, value2);
    allCustomProperties.put(key1, RandomStringUtils.random(10));
    int patientId = RandomUtils.nextInt();
    when(patientManager.getPatientCustomProperties(patientId)).thenReturn(allCustomProperties);
    when(patientManager.getPatientById(patientId)).thenReturn(new Patient());

    Map<String, String> finalCustomProperties = new HashMap<>();
    finalCustomProperties.put(key2, value2);
    finalCustomProperties.put(key1, value1);

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(customPropertiesMap)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientCustomProperties(patientId, finalCustomProperties);
  }

  @Test
  public void testUpdatePatientCustomPropertiesForClientCredentials() throws Exception {
    // setup random data
    context.setUser(new AuditLogUser(null, 1, "testId", "TestName", "TestInfo"));
    int patientId = RandomUtils.nextInt();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    verify(patientManager, never()).updatePatientCustomProperties(anyInt(), anyMap());
  }

  @Test
  public void testUpdatePatientCustomPropertiesWithNoAccess() throws Exception {
    // setup random data

    Map<String, String> customPropertiesMap = new HashMap<>();
    String key1 = RandomStringUtils.random(10);
    String value1 = RandomStringUtils.random(10);
    customPropertiesMap.put(key1, value1);

    Map<String, String> allCustomProperties = new HashMap<>();
    String key2 = RandomStringUtils.random(10);
    String value2 = RandomStringUtils.random(10);
    allCustomProperties.put(key2, value2);
    allCustomProperties.put(key1, RandomStringUtils.random(10));
    int patientId = RandomUtils.nextInt();
    when(patientManager.getPatientCustomProperties(patientId)).thenReturn(allCustomProperties);

    CustomField customField = new CustomField();
    customField.setName(key2);

    when(customFieldManager.getCustomFields(null, true, user))
        .thenReturn(Collections.singletonList(customField));

    Map<String, String> finalCustomProperties = new HashMap<>();
    finalCustomProperties.put(key2, value2);
    finalCustomProperties.put(key1, value1);

    // test

    given()
        .pathParam("patientId", patientId)
        .body(customPropertiesMap)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientManager, never()).updatePatientCustomProperties(patientId, finalCustomProperties);
  }

  @Test
  public void testUpdatePatientCustomPropertiesNegativePatientId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt() * -1;
    Map<String, String> customPropertiesMap = new HashMap<>();
    customPropertiesMap.put(RandomStringUtils.random(10), RandomStringUtils.random(10));

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(customPropertiesMap)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(patientManager, never()).updatePatientCustomProperties(anyInt(), anyMap());
  }

  @Test
  public void testUpdatePatientCustomPropertiesNullCustomProperties() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Missing custom properties"));
    verify(patientManager, never()).updatePatientCustomProperties(anyInt(), anyMap());
  }

  @Test
  public void testPatchPatientCustomProperties() throws Exception {
    // setup random data

    String key1 = "key1";
    String value1 = "value1";

    String key2 = "key2";
    String value2 = "value2";

    Map<String, String> customPropertiesMap = new HashMap<>();
    customPropertiesMap.put(key1, value1);

    Map<String, String> existingPropertiesMap = new HashMap<>();
    existingPropertiesMap.put(key2, value2);

    Map<String, String> finalCustomProperties = new HashMap<>();
    finalCustomProperties.put(key1, value1);
    finalCustomProperties.put(key2, value2);
    CustomField customField1 = new CustomField();
    customField1.setName(key1);
    List<CustomField> customFieldList =
        new ArrayList<>(Arrays.asList(customField1));
    when(customFieldManager.getCustomFields(null, true, user))
        .thenReturn(customFieldList);
    int patientId = RandomUtils.nextInt();
    when(patientManager.getPatientCustomProperties(patientId))
        .thenReturn(existingPropertiesMap);
    when(patientManager.getPatientById(patientId)).thenReturn(new Patient());

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(customPropertiesMap)
            .when()
            .contentType("application/merge-patch+json")
            .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientCustomProperties(patientId, finalCustomProperties);
  }

  @Test
  public void testPatchPatientCustomPropertiesWithRemoveProperty() throws Exception {
    // setup random data

    String key1 = "key1";
    String value1 = "value1";

    String key2 = "key2";
    String value2 = "value2";

    Map<String, String> customPropertiesMap = new HashMap<>();
    customPropertiesMap.put(key1, value1);
    customPropertiesMap.put(key2, null);

    Map<String, String> existingPropertiesMap = new HashMap<>();
    existingPropertiesMap.put(key2, value2);

    Map<String, String> finalCustomProperties = new HashMap<>();
    finalCustomProperties.put(key1, value1);
    CustomField customField1 = new CustomField();
    customField1.setName(key1);
    CustomField customField2 = new CustomField();
    customField2.setName(key2);
    List<CustomField> customFieldList =
        new ArrayList<>(Arrays.asList(customField1, customField2));
    when(customFieldManager.getCustomFields(null, true, user))
        .thenReturn(customFieldList);
    int patientId = RandomUtils.nextInt();
    when(patientManager.getPatientCustomProperties(patientId))
        .thenReturn(existingPropertiesMap);
    when(patientManager.getPatientById(patientId)).thenReturn(new Patient());

    // test
    Boolean actual =
        given()
            .pathParam("patientId", patientId)
            .body(customPropertiesMap)
            .when()
            .contentType("application/merge-patch+json")
            .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Boolean.class);
    // assertions
    Assert.assertTrue(actual);
    verify(patientManager).updatePatientCustomProperties(patientId, finalCustomProperties);
  }

  @Test
  public void testPatchPatientCustomPropertiesForClientCredentials() throws Exception {
    context.setUser(new AuditLogUser(null, 1, "testId", "TestName", "TestInfo"));
    given()
        .pathParam("patientId", 1)
        .body(Collections.emptyMap())
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/custom-properties")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    // assertions
    verify(patientManager, never()).updatePatientCustomProperties(anyInt(), anyMap());
  }

  @Test
  public void testGetActivity() throws Exception {
    // setup random data
    List<PatientActivity> protossResults =
        getFixtures(PatientActivity.class, ArrayList::new, 2);
    List<PatientActivityDto> expected =
        mapDto(protossResults, PatientActivityDto.class, ArrayList::new);
    int patientId = RandomUtils.nextInt();
    String recordId = RandomStringUtils.randomAlphanumeric(10);
    // mock dependencies
    when(logManager.getPatientActivity(anyString())).thenReturn(protossResults);

    // test
    List<PatientActivityDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("recordId", recordId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/activity/{recordId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientActivityDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(logManager).getPatientActivity(recordId);
  }

  @Test
  public void testGetActivityEmptyRecordId() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    String recoredId = " ";
    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("recordId", recoredId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/activity/{recordId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(logManager, never()).getPatientActivity(anyString());
  }

  @Test
  public void testGetActivities() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    List<String> recordIds = getFixtures(String.class, ArrayList::new, 2);
    List<PatientActivity> patientActivities = getFixtures(PatientActivity.class, ArrayList::new, 2);
    Map<String, List<PatientActivity>> activityMap = new HashMap<>();
    activityMap.put(RandomStringUtils.randomAlphabetic(5), patientActivities);
    // mock dependencies
    Map<String, List<PatientActivityDto>> expectedMap =
        mapDto(activityMap, PatientActivityDto.class, ArrayList::new, HashMap::new);
    when(logManager.getPatientActivity(recordIds)).thenReturn(activityMap);
    // test
    // Map<String, LinkedHashMap<String, String>> actual =
    Map<String, List<LinkedHashMap<String, String>>> actual =
        given()
            .pathParam("patientId", patientId)
            .body(recordIds)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/activity/search")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(activityMap.getClass());

    Map<String, List<PatientActivityDto>> actualMap = new HashMap<>();
    for (String key : actual.keySet()) {
      List<LinkedHashMap<String, String>> hashMapList = actual.get(key);
      List<PatientActivityDto> dtoList = new ArrayList<>();

      for (LinkedHashMap<String, String> linkedHashMap : hashMapList) {
        PatientActivityDto patientActivityDto = new PatientActivityDto();
        patientActivityDto.setDescription(linkedHashMap.get("description"));
        patientActivityDto
            .setPatientId(Integer.parseInt(String.valueOf(linkedHashMap.get("patientId"))));
        patientActivityDto.setRecordId(linkedHashMap.get("recordId"));
        patientActivityDto.setType(PatientActivityType.lookup(linkedHashMap.get("type")));
        patientActivityDto
            .setTimeUtc(AccuroCalendarBasedDeserializer.parse(linkedHashMap.get("timeUtc"),
                null).getCalender());
        dtoList.add(patientActivityDto);
      }
      actualMap.put(key, dtoList);
    }
    // assertions
    assertEquals(expectedMap, actualMap);
    verify(logManager).getPatientActivity(recordIds);
  }

  @Test
  public void testGetActivitiesNullRecordIds() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/activity/search")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(logManager, never()).getPatientActivity(anyList());
  }

  @Test
  public void testGetActivitiesEmptyRecordIds() throws Exception {
    // setup random data
    int patientId = RandomUtils.nextInt();
    List<String> recordIds = new ArrayList<>();
    // test
    given()
        .pathParam("patientId", patientId)
        .body(recordIds)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/activity/search")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(logManager, never()).getPatientActivity(anyList());
  }

  @Test
  public void testGetPatientByPhn() throws Exception {
    // setup random data
    List<Patient> protossResults =
        getFixtures(Patient.class, ArrayList::new, 2);
    List<PatientDto> expected =
        mapDto(protossResults, PatientDto.class, ArrayList::new);
    String phn = RandomStringUtils.randomAlphabetic(6);
    // mock dependencies
    when(patientManager.getPatientsByPHN(phn)).thenReturn(protossResults);

    // test
    List<PatientDto> actual = toCollection(
        given()
            .queryParam("phn", phn)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/search")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(patientManager).getPatientsByPHN(phn);
  }

  @Test
  public void testGetPatientByPhnEmptyList() throws Exception {
    // setup random data
    String phn = " ";
    // test
    given()
        .queryParam("phn", phn)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/search")
        .then()
        .assertThat()
        .statusCode(200);
    // assertions
    verify(patientManager, never()).getPatientsByPHN(anyString());
  }

  @Test
  public void testGetProfilePicture() throws DataAccessException, IOException {
    PatientProfilePicture protossResult = getFixture(PatientProfilePicture.class);
    int patientId = protossResult.getPatientId();
    byte[] byteImage = getImageInBytes();
    protossResult.setProfileImage(byteImage);
    when(profilePictureManagerMock.getProfilePictureByPatientId(patientId))
        .thenReturn(protossResult);

    // test
    String actual = given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat()
        .statusCode(200).extract().asString();

    String expected = new String(byteImage);
    // assert
    assertEquals(expected, actual);
    verify(profilePictureManagerMock).getProfilePictureByPatientId(patientId);

  }

  @Test
  public void testGetProfilePictureNotFound() throws DataAccessException {
    int patientId = TestUtilities.nextInt();
    when(profilePictureManagerMock.getProfilePictureByPatientId(patientId)).thenReturn(null);
    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat()
        .statusCode(404);
    // assert
    verify(profilePictureManagerMock).getProfilePictureByPatientId(patientId);
  }

  @Test
  public void testGetProfilePictureDifferentPatient() throws DataAccessException, IOException {
    PatientProfilePicture protossResult = getFixture(PatientProfilePicture.class);
    int patientId = TestUtilities.nextInt();

    when(profilePictureManagerMock.getProfilePictureByPatientId(patientId))
        .thenReturn(protossResult);

    // test
    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat()
        .statusCode(404);

    // assert
    verify(profilePictureManagerMock).getProfilePictureByPatientId(patientId);

  }

  @Test
  public void testUploadProfilePicture()
      throws IOException, DataAccessException {

    int patientId = TestUtilities.nextInt();
    BufferedImage testImage =
        new BufferedImage(MAX_DEFAULT_WIDTH, MAX_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(testImage, "png", baos);
    byte[] byteImage = baos.toByteArray();

    PatientProfilePicture patientProfilePicture = new PatientProfilePicture();
    patientProfilePicture
        .setProfileImage(byteImage);
    patientProfilePicture.setPatientId(patientId);

    File outputfile = temporaryFolder.newFile("test.png");
    ImageIO.write(testImage, "png", outputfile);

    int expectedId = TestUtilities.nextInt();
    when(profilePictureManagerMock.uploadProfilePicture(patientProfilePicture, user))
        .thenReturn(expectedId);

    int actualId = given()
        .multiPart("image", outputfile)
        .pathParam("patientId", patientId)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat().statusCode(200).extract().as(Integer.class);

    verify(profilePictureManagerMock).uploadProfilePicture(patientProfilePicture, user);
    assertEquals(expectedId, actualId);
  }

  @Test
  public void testUploadProfilePictureInvalidImage()
      throws IOException, DataAccessException {

    int patientId = TestUtilities.nextInt();

    File outputfile = temporaryFolder.newFile("test.txt");

    given()
        .multiPart("image", outputfile)
        .pathParam("patientId", patientId)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat().statusCode(400);

    verify(profilePictureManagerMock, times(0))
        .uploadProfilePicture(any(PatientProfilePicture.class), any(AuditLogUser.class));
  }

  @Test
  public void testUploadProfilePictureWithoutResizing()
      throws IOException, DataAccessException {

    int patientId = TestUtilities.nextInt();
    BufferedImage testImage =
        new BufferedImage(MAX_DEFAULT_WIDTH, MAX_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);

    PatientProfilePicture patientProfilePicture = new PatientProfilePicture();
    patientProfilePicture
        .setProfileImage(getImageInBytes());
    patientProfilePicture.setPatientId(patientId);

    File outputfile = temporaryFolder.newFile("test.png");
    ImageIO.write(testImage, "png", outputfile);

    int expectedId = TestUtilities.nextInt();
    when(profilePictureManagerMock.uploadProfilePicture(patientProfilePicture, user))
        .thenReturn(expectedId);

    int actualId = given()
        .multiPart("image", outputfile)
        .pathParam("patientId", patientId)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat().statusCode(200).extract().as(Integer.class);

    verify(profilePictureManagerMock).uploadProfilePicture(patientProfilePicture, user);
    assertEquals(expectedId, actualId);
  }

  @Test
  public void testDeleteProfilePicture() throws DataAccessException {
    PatientProfilePicture picture = new PatientProfilePicture();
    int patientId = TestUtilities.nextInt();
    picture.setPatientId(patientId);
    picture.setProfileImage(RandomUtils.nextBytes(RandomUtils.nextInt(1, 8000)));
    doNothing().when(profilePictureManagerMock).deleteProfilePicture(patientId, user);
    when(profilePictureManagerMock.getProfilePictureByPatientId(patientId)).thenReturn(picture);
    given()
        .pathParam("patientId", patientId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat()
        .statusCode(200);

    verify(profilePictureManagerMock).deleteProfilePicture(patientId, user);

  }

  @Test
  public void testDeleteProfilePictureNotFound() throws Exception {
    int patientId = TestUtilities.nextInt();

    when(profilePictureManagerMock.getProfilePictureByPatientId(patientId)).thenReturn(null);
    given()
        .pathParam("patientId", patientId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/profile-picture")
        .then()
        .assertThat()
        .statusCode(404);

    verify(profilePictureManagerMock, never()).deleteProfilePicture(patientId, user);
  }

  private byte[] getImageInBytes() throws IOException {
    BufferedImage testImage =
        new BufferedImage(MAX_DEFAULT_WIDTH, MAX_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(testImage, "png", baos); // write to png lossless format.
    return baos.toByteArray();
  }

  private Map<String, String> mapCustomFields(List<PatientCustomField> customFields) {
    Map<String, String> customFieldMap = new HashMap<>();
    for (PatientCustomField customField : customFields) {
      customFieldMap.put(customField.getName(), customField.getValue());
    }
    return customFieldMap;
  }

  private class PatchMerge {

    Integer patientId;
    String occupation;

    public Integer getPatientId() {
      return patientId;
    }

    public void setPatientId(Integer patientId) {
      this.patientId = patientId;
    }

    public String getOccupation() {
      return occupation;
    }

    public void setOccupation(String occupation) {
      this.occupation = occupation;
    }
  }

  private void formatPhoneNumber(PatientDto patientDto) {
    if (patientDto.getDemographics().getPhones() != null) {
      List<PhoneDto> phoneDtoList =
          patientDto.getDemographics().getPhones().stream().map(phoneDto -> {
            if (StringUtils.isNotBlank(phoneDto.getNumber())) {
              phoneDto.setNumber(PhoneNumberFormatterUtils.formatPhoneNumber(phoneDto.getNumber()));
            }
            return phoneDto;
          }).collect(Collectors.toUnmodifiableList());
      patientDto.getDemographics().setPhones(phoneDtoList);
    }
  }

}
