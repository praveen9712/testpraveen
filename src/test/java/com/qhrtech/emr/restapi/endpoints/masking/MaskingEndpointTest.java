
package com.qhrtech.emr.restapi.endpoints.masking;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.masking.MaskingManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.db.RandomUtils;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.masking.Mask;
import com.qhrtech.emr.accuro.model.masking.MaskAuthorization;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.masking.MaskAuthorizationDto;
import com.qhrtech.emr.restapi.models.dto.masking.MaskDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class MaskingEndpointTest extends AbstractEndpointTest<MaskingEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  MaskingManager maskingManager;
  PatientManager patientManager;
  private AuditLogUser user;

  public MaskingEndpointTest() {
    super(new MaskingEndpoint(), MaskingEndpoint.class);

    maskingManager = mock(MaskingManager.class);
    patientManager = mock(PatientManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(MaskingManager.class, maskingManager);
    servicesMap.put(PatientManager.class, patientManager);

    return servicesMap;
  }

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    super.setUp();
    try {
      when(patientManager.getPatientById(anyInt())).thenReturn(new Patient());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetPatientMask()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {
    Mask protossResult = getFixture(Mask.class);
    int maskId = protossResult.getMaskId();
    int patientId = protossResult.getPatientId();
    when(maskingManager.getMaskById(maskId)).thenReturn(protossResult);

    MaskDto expected = mapDto(protossResult, MaskDto.class);

    MaskDto actual = given().pathParam("maskId", maskId)
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(MaskDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(maskingManager).getMaskById(maskId);

  }

  @Test
  public void testGetPatientMaskDifferentMaskFound()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {
    Mask protossResult = getFixture(Mask.class);
    int maskId = protossResult.getMaskId();
    int patientId = TestUtilities.nextInt();
    when(maskingManager.getMaskById(maskId)).thenReturn(protossResult);

    given().pathParam("maskId", maskId)
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(maskingManager).getMaskById(maskId);

  }

  @Test
  public void testGetPatientMaskNotFound()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {
    Mask protossResult = getFixture(Mask.class);
    int maskId = protossResult.getMaskId();
    int patientId = protossResult.getPatientId();
    when(maskingManager.getMaskById(maskId)).thenReturn(null);

    given().pathParam("maskId", maskId)
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager).getMaskById(maskId);

  }

  @Test
  public void testSearchMaskAllParam()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    int patientId = TestUtilities.nextInt();
    int userId = TestUtilities.nextInt();
    String fieldName = TestUtilities.nextString(10);

    Envelope<Mask> protossEnvelope = getProtossEnvelope();
    int startingId =
        protossEnvelope.getContents().stream().min(Comparator.comparing(Mask::getMaskId))
            .orElseThrow(RuntimeException::new).getMaskId();

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, userId, fieldName,
        startingId - 1, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual = given()
        .pathParam("patientId", patientId)
        .queryParam("userId", userId)
        .queryParam("fieldName", fieldName)
        .queryParam("startingId", startingId - 1)
        .queryParam("pageSize", DEFAULT_PAGE_SIZE)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<MaskDto> actual = refType(rawActual);

    assertEquals(getExpected(protossEnvelope), actual);

    verify(maskingManager).getMasks(patientId, userId, fieldName, startingId - 1,
        DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchMaskNofilter()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    int patientId = TestUtilities.nextInt();
    Envelope<Mask> protossEnvelope = getProtossEnvelope();

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, null, null,
        null, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual = given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<MaskDto> actual = refType(rawActual);

    assertEquals(getExpected(protossEnvelope), actual);

    verify(maskingManager).getMasks(patientId, null, null, null,
        DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchMaskNegativePageSize()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    int patientId = TestUtilities.nextInt();
    Envelope<Mask> protossEnvelope = getProtossEnvelope();
    int pageSize = RandomUtils.getInt(-1000, -1);

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, null, null, null,
        DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual = given()
        .pathParam("patientId", patientId)
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<MaskDto> actual = refType(rawActual);

    assertEquals(getExpected(protossEnvelope), actual);

    verify(maskingManager).getMasks(patientId, null, null, null,
        DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchMaskMaxPageSize()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    int patientId = TestUtilities.nextInt();
    Envelope<Mask> protossEnvelope = getProtossEnvelope();
    int pageSize = RandomUtils.getInt(100, 1000);

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, null, null, null,
        MAX_PAGE_SIZE);

    EnvelopeDto rawActual = given()
        .pathParam("patientId", patientId)
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<MaskDto> actual = refType(rawActual);

    assertEquals(getExpected(protossEnvelope), actual);

    verify(maskingManager).getMasks(patientId, null, null, null,
        MAX_PAGE_SIZE);

  }


  @Test
  public void testCreate() throws TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, BusinessLogicException, ForbiddenException {
    MaskDto maskDto = getFixture(MaskDto.class);
    int patientId = maskDto.getPatientId();
    Integer expectedId = TestUtilities.nextInt();

    Mask mask = mapDto(maskDto, Mask.class);

    Envelope<Mask> protossEnvelope = getProtossEnvelope();
    protossEnvelope.setContents(Collections.EMPTY_LIST);

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, null, maskDto.getFieldName(),
        null,
        null);

    when(maskingManager.createMask(mask)).thenReturn(expectedId);
    // test
    Integer actualId = given()
        .pathParam("patientId", patientId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(201)
        .extract().as(Integer.class);

    // assertions
    assertEquals(actualId, expectedId);
    verify(maskingManager).createMask(mask);

  }

  @Test
  public void testCreateAlreadyExist()
      throws TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, BusinessLogicException, ForbiddenException {
    MaskDto maskDto = getFixture(MaskDto.class);
    int patientId = maskDto.getPatientId();


    Mask mask = mapDto(maskDto, Mask.class);

    Envelope<Mask> protossEnvelope = getProtossEnvelope();
    List<Mask> existingMasks = new ArrayList<>();
    existingMasks.add(mask);
    protossEnvelope.setContents(existingMasks);

    doReturn(protossEnvelope).when(maskingManager).getMasks(patientId, null, maskDto.getFieldName(),
        null,
        null);

    Integer expectedId = TestUtilities.nextInt();
    when(maskingManager.createMask(mask)).thenReturn(expectedId);
    // test
    given()
        .pathParam("patientId", patientId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(maskingManager, times(0)).createMask(mask);

  }

  @Test
  public void testCreatePatientIdNotMatchWithUrl()
      throws TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, BusinessLogicException, ForbiddenException {
    MaskDto maskDto = getFixture(MaskDto.class);
    int patientId = TestUtilities.nextInt();
    Mask mask = mapDto(maskDto, Mask.class);
    // test
    given()
        .pathParam("patientId", patientId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(maskingManager, times(0)).createMask(mask);

  }

  @Test
  public void testCreateNullObject() {
    int patientId = TestUtilities.nextInt();

    given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void testUpdateMaskWithoutReason()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {
    MaskDto maskDto = getFixture(MaskDto.class);
    maskDto.setUserId(user.getUserId());
    int patientId = maskDto.getPatientId();
    int maskId = maskDto.getMaskId();

    Mask mask = mapDto(maskDto, Mask.class);

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(maskingManager).updateMask(mask, null);
  }

  @Test
  public void testUpdateMaskIdNotMatchWithBody() {
    MaskDto maskDto = getFixture(MaskDto.class);
    maskDto.setUserId(user.getUserId());
    int patientId = maskDto.getPatientId();
    int maskId = TestUtilities.nextInt();

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(maskingManager);

  }

  @Test
  public void testUpdateMaskIdPatientNotMatchWithBody() {
    MaskDto maskDto = getFixture(MaskDto.class);
    maskDto.setUserId(user.getUserId());
    int patientId = TestUtilities.nextInt();
    int maskId = maskDto.getMaskId();

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(maskingManager);

  }

  @Test
  public void testUpdateMaskWithoutBody() {
    MaskDto maskDto = getFixture(MaskDto.class);
    maskDto.setUserId(user.getUserId());
    int patientId = maskDto.getPatientId();
    int maskId = maskDto.getMaskId();

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(maskingManager);

  }

  @Test
  public void testUpdateMask()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {
    MaskDto maskDto = getFixture(MaskDto.class);
    int patientId = maskDto.getPatientId();
    int maskId = maskDto.getMaskId();
    String reason = TestUtilities.nextString(10);

    Mask mask = mapDto(maskDto, Mask.class);

    when(maskingManager.getMaskById(maskId)).thenReturn(mask);

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .queryParam("reason", reason)
        .body(maskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/{maskId}")
        .then()
        .assertThat()
        .statusCode(204);
    verify(maskingManager).updateMask(mask, reason);
  }

  @Test
  public void testGetAuthorizationById()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    Mask mask = getFixture(Mask.class);
    MaskAuthorization protossAuth = mask.getMaskAuthorizations().get(0);
    protossAuth.setMaskId(mask.getMaskId());
    int authorizationId = protossAuth.getId();
    int patientId = mask.getPatientId();
    int maskId = protossAuth.getMaskId();

    when(maskingManager.getMaskById(maskId)).thenReturn(mask);

    MaskAuthorizationDto expected = mapDto(protossAuth, MaskAuthorizationDto.class);

    MaskAuthorizationDto actual = given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authorizationId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks"
            + "/{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(MaskAuthorizationDto.class);

    verify(maskingManager).getMaskById(maskId);
    assertEquals(expected, actual);

  }

  @Test
  public void testGetAuthorizationByIdNotFound()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    MaskAuthorization protossAuth = getFixture(MaskAuthorization.class);
    int authorizationId = protossAuth.getId();
    int patientId = TestUtilities.nextInt();
    int maskId = protossAuth.getMaskId();

    when(maskingManager.getMaskById(maskId)).thenReturn(null);

    given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authorizationId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks"
            + "/{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager).getMaskById(maskId);

  }

  @Test
  public void testGetAuthorizations()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {
    Mask mask = getFixture(Mask.class);
    List<MaskAuthorization> protossResult = mask.getMaskAuthorizations();
    int maskId = mask.getMaskId();
    int patientId = mask.getPatientId();


    when(maskingManager.getMaskById(maskId)).thenReturn(mask);

    List<MaskAuthorizationDto> expected =
        mapDto(protossResult, MaskAuthorizationDto.class, ArrayList::new);

    List<MaskAuthorizationDto> actual = toCollection(given()
        .pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(MaskAuthorizationDto[].class), ArrayList::new);

    verify(maskingManager).getMaskById(maskId);
    assertEquals(expected, actual);

  }

  @Test
  public void testCreateAuthorizationNullRequest() {
    int maskId = TestUtilities.nextInt();
    int patientId = TestUtilities.nextInt();

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(400);
    verifyNoInteractions(maskingManager);

  }

  @Test
  public void testCreateAuthorizationMaskIdNotMatch() {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = TestUtilities.nextInt();
    int patientId = TestUtilities.nextInt();

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(400);
    verifyNoInteractions(maskingManager);

  }

  @Test
  public void testCreateAuthorizationPatientNotMatch()
      throws BusinessLogicException, TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ForbiddenException {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();

    int expected = authorizationDto.getId();

    MaskAuthorization protossResult = mapDto(authorizationDto, MaskAuthorization.class);

    Mask mask = getFixture(Mask.class);
    mask.setUserId(user.getUserId()); // mask owner to avoid reason

    when(maskingManager.getMaskById(maskId)).thenReturn(mask);
    when(maskingManager.createMaskAuthorization(protossResult, null)).thenReturn(expected);

    int patientId = TestUtilities.nextInt();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(404);
    verify(maskingManager).getMaskById(maskId);
    verify(maskingManager, times(0)).createMaskAuthorization(protossResult, null);


  }

  @Test
  public void testCreateAuthorizationDoesNotExist()
      throws BusinessLogicException, TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ForbiddenException {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();

    Mask mask = getFixture(Mask.class);
    mask.setUserId(user.getUserId()); // mask owner to avoid reason

    int patientId = mask.getPatientId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager).getMaskById(maskId);
    verify(maskingManager, never()).createMaskAuthorization(any(), any());
  }

  @Test
  public void testCreateAuthorizationAlreadyExist()
      throws BusinessLogicException, TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ForbiddenException {
    Mask mask = getFixture(Mask.class);
    MaskAuthorization authorization = mask.getMaskAuthorizations().get(0);
    authorization.setRoleId(null);
    MaskAuthorizationDto authorizationDto = mapDto(authorization, MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();

    int patientId = mask.getPatientId();

    mask.setUserId(user.getUserId()); // mask owner to avoid reason
    mask.setPatientId(patientId);
    when(maskingManager.getMaskById(maskId)).thenReturn(mask);

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(400);
    verify(maskingManager).getMaskById(maskId);

    verify(maskingManager, times(0)).createMaskAuthorization(authorization, null);

  }

  @Test
  public void testCreateAuthorization()
      throws BusinessLogicException, TimeZoneNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ForbiddenException {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();

    int patientId = TestUtilities.nextInt();
    Mask mask = getFixture(Mask.class);
    mask.setUserId(user.getUserId()); // mask owner to avoid reason
    mask.setPatientId(patientId);

    MaskAuthorization protossResult = mapDto(authorizationDto, MaskAuthorization.class);
    int expected = authorizationDto.getId();
    when(maskingManager.getMaskById(maskId)).thenReturn(mask);
    when(maskingManager.createMaskAuthorization(protossResult, null)).thenReturn(expected);

    int actual = given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations")
        .then()
        .assertThat()
        .statusCode(201)
        .extract().as(Integer.class);
    verify(maskingManager).getMaskById(maskId);
    verify(maskingManager).createMaskAuthorization(protossResult, null);
    assertEquals(expected, actual);

  }

  @Test
  public void testUpdateAuthorization()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException, BusinessLogicException, NoDataFoundException {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    authorizationDto.setRoleId(null);
    int maskId = authorizationDto.getMaskId();
    int expected = authorizationDto.getId();
    Mask mask = getFixture(Mask.class);
    mask.setMaskId(maskId);
    int patientId = TestUtilities.nextInt();
    mask.setPatientId(patientId);

    MaskAuthorization protossResult = mapDto(authorizationDto, MaskAuthorization.class);

    when(maskingManager.createMaskAuthorization(protossResult, null)).thenReturn(expected);
    when(maskingManager.getMaskById(maskId)).thenReturn(mask);

    int authId = authorizationDto.getId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(204);
    verify(maskingManager).updateMaskAuthorization(protossResult, null);
  }

  @Test
  public void testUpdateAuthorizationMaskIdNotEqual() {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    authorizationDto.setRoleId(null);
    int patientId = TestUtilities.nextInt();

    int authId = authorizationDto.getId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", TestUtilities.nextInt())
        .pathParam("authorizationId", authId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(maskingManager);
  }

  @Test
  public void testUpdateAuthorizationAuthIdNotEqual() {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    authorizationDto.setRoleId(null);
    int patientId = TestUtilities.nextInt();

    int maskId = authorizationDto.getMaskId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", TestUtilities.nextInt())
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(maskingManager);
  }

  @Test
  public void testUpdateAuthorizationUserRoleBothUpdate() {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();

    int patientId = TestUtilities.nextInt();
    int authId = authorizationDto.getId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .body(authorizationDto)
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(400);
    verifyNoInteractions(maskingManager);
  }

  @Test
  public void testUpdateAuthorizationNullBody() {
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);
    int maskId = authorizationDto.getMaskId();
    int patientId = TestUtilities.nextInt();
    int authId = authorizationDto.getId();

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(400);
    verifyNoInteractions(maskingManager);
  }

  private EnvelopeDto<MaskDto> refType(
      EnvelopeDto rawEnvelope) {
    List<MaskDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<MaskDto>>() {});
    EnvelopeDto<MaskDto> e =
        (EnvelopeDto<MaskDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  @Test
  public void testDeleteAuths()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {

    Mask existingMask = getFixture(Mask.class);
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);

    int maskId = existingMask.getMaskId();
    authorizationDto.setMaskId(maskId);

    int authId = authorizationDto.getId();
    MaskAuthorization existingAuth = existingMask.getMaskAuthorizations().get(0);
    existingAuth.setId(authId);
    existingMask.setMaskAuthorizations(Collections.singletonList(existingAuth));

    doReturn(existingMask).when(maskingManager).getMaskById(maskId);
    int patientId = existingMask.getPatientId();
    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(maskingManager).deleteMaskAuthorization(authId, null);
    verify(maskingManager).getMaskById(maskId);
  }

  @Test
  public void testDeleteAuthsNotFoundDiffPatientMask()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {

    Mask existingMask = getFixture(Mask.class);
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);

    int maskId = existingMask.getMaskId();
    authorizationDto.setMaskId(maskId);
    int patientId = existingMask.getPatientId();
    int authId = authorizationDto.getId();

    doReturn(existingMask).when(maskingManager).getMaskById(maskId);

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager, times(0)).deleteMaskAuthorization(authId, null);
    verify(maskingManager).getMaskById(maskId);
  }

  @Test
  public void testDeleteAuthsPatientNotMatch()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {

    Mask existingMask = getFixture(Mask.class);
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);

    int maskId = existingMask.getMaskId();
    authorizationDto.setMaskId(maskId);
    int patientId = TestUtilities.nextInt();
    int authId = authorizationDto.getId();

    doReturn(existingMask).when(maskingManager).getMaskById(maskId);

    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager, times(0)).deleteMaskAuthorization(authId, null);
    verify(maskingManager).getMaskById(maskId);
  }

  @Test
  public void testDeleteAuthsNotFound()
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException,
      BusinessLogicException, NoDataFoundException, TimeZoneNotFoundException {

    Mask existingMask = getFixture(Mask.class);
    MaskAuthorizationDto authorizationDto = getFixture(MaskAuthorizationDto.class);

    int maskId = existingMask.getMaskId();
    authorizationDto.setMaskId(maskId);
    int patientId = TestUtilities.nextInt();
    int authId = authorizationDto.getId();

    doReturn(null).when(maskingManager).getMaskById(maskId);


    given().pathParam("patientId", patientId)
        .pathParam("maskId", maskId)
        .pathParam("authorizationId", authId)
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/masks/"
            + "{maskId}/authorizations/{authorizationId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(maskingManager, times(0)).deleteMaskAuthorization(authId, null);
    verify(maskingManager).getMaskById(maskId);
  }

  private Envelope<Mask> getProtossEnvelope() {
    List<Mask> protossResults = getFixtures(Mask.class, ArrayList::new, 10);

    Envelope<Mask> protossEnvelope = new Envelope<>();

    long lastId = protossResults.stream().max(Comparator.comparing(Mask::getMaskId))
        .orElseThrow(RuntimeException::new).getMaskId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setTotal(protossResults.size());
    protossEnvelope.setLastId(lastId);

    return protossEnvelope;

  }

  private EnvelopeDto<MaskDto> getExpected(Envelope<Mask> protossEnvelope) {
    List<MaskDto> masks = mapDto(protossEnvelope.getContents(), MaskDto.class, ArrayList::new);

    EnvelopeDto<MaskDto> expected = new EnvelopeDto<>();

    expected.setContents(masks);
    expected.setLastId(protossEnvelope.getLastId());
    expected.setCount(protossEnvelope.getCount());
    expected.setTotal(protossEnvelope.getTotal());

    return expected;

  }

}
