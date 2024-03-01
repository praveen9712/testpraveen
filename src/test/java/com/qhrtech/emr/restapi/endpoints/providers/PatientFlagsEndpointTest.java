
package com.qhrtech.emr.restapi.endpoints.providers;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.patient.PatientFlagManager;
import com.qhrtech.emr.accuro.model.patient.PatientRoleFlag;
import com.qhrtech.emr.accuro.model.patient.PatientUserFlag;
import com.qhrtech.emr.accuro.model.patient.PatientUserRoleFlagId;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.provider.PatientFlagsEndpoint;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PatientRoleFlagDto;
import com.qhrtech.emr.restapi.models.dto.PatientUserFlagDto;
import com.qhrtech.emr.restapi.models.dto.PatientUserRoleFlagIdDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

public class PatientFlagsEndpointTest extends AbstractEndpointTest<PatientFlagsEndpoint> {

  private static final String ENDPOINT_STRING = "/v2/provider-portal/patients";
  private PatientFlagManager patientFlagManager;
  private ApiSecurityContext context;

  public PatientFlagsEndpointTest() {
    super(new PatientFlagsEndpoint(), PatientFlagsEndpoint.class);
    patientFlagManager = mock(PatientFlagManager.class);
    context = new ApiSecurityContext();
    AuditLogUser user = new AuditLogUser(
        TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(PatientFlagManager.class, patientFlagManager);
  }

  @Test
  public void getPatientRoleFlags() throws Exception {
    Set<PatientRoleFlag> protosResult = getFixtures(PatientRoleFlag.class, HashSet::new, 5);
    Set<PatientRoleFlagDto> expected = mapDto(protosResult, PatientRoleFlagDto.class, HashSet::new);
    int patientId = TestUtilities.nextInt();
    when(patientFlagManager.getPatientRoleFlags(patientId)).thenReturn(protosResult);
    Set<PatientRoleFlagDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientRoleFlagDto[].class),
        HashSet::new);

    Assert.assertEquals(expected, actual);
    verify(patientFlagManager).getPatientRoleFlags(patientId);
  }

  @Test
  public void getPatientRoleFlagsNoFlags() throws Exception {
    Set<PatientRoleFlag> protosResult = new HashSet<>();
    int patientId = TestUtilities.nextInt();
    when(patientFlagManager.getPatientRoleFlags(patientId)).thenReturn(protosResult);
    Set<PatientRoleFlagDto> roleFlag = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientRoleFlagDto[].class),
        HashSet::new);
    assertTrue(roleFlag.isEmpty());
    verify(patientFlagManager).getPatientRoleFlags(patientId);
  }

  @Test
  public void testDeletePatientRoleFlag() throws Exception {

    PatientRoleFlag roleFlag = getFixture(PatientRoleFlag.class);
    int patientId = TestUtilities.nextInt();
    when(patientFlagManager.getPatientRoleFlags(patientId))
        .thenReturn(Collections.singleton(roleFlag));
    given()
        .pathParams("patientId", patientId, "roleId", roleFlag.getRoleId())
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags/{roleId}")
        .then().assertThat().statusCode(200);
    verify(patientFlagManager).getPatientRoleFlags(patientId);
    verify(patientFlagManager).deletePatientRoleFlag(patientId, roleFlag.getRoleId(),
        context.getUser());
  }

  @Test
  public void testDeletePatientRoleFlagNoFlags() throws Exception {
    PatientRoleFlag roleFlag = getFixture(PatientRoleFlag.class);
    PatientRoleFlag roleFlag1 = getFixture(PatientRoleFlag.class);
    int patientId = TestUtilities.nextInt();
    when(patientFlagManager.getPatientRoleFlags(patientId))
        .thenReturn(Collections.singleton(roleFlag1));
    given()
        .pathParams("patientId", patientId, "roleId", roleFlag.getRoleId())
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags/{roleId}")
        .then().assertThat().statusCode(404);
    verify(patientFlagManager).getPatientRoleFlags(patientId);
    verify(patientFlagManager, never()).deletePatientRoleFlag(patientId, roleFlag.getRoleId(),
        context.getUser());
  }

  @Test
  public void testCreatePatientRoleFlags() throws Exception {
    int patientId = RandomUtils.nextInt();
    Set<PatientRoleFlag> patientRoleFlagSet = getFixtures(PatientRoleFlag.class, HashSet::new, 3);
    Set<PatientUserRoleFlagId> protossExpected = new HashSet<>();
    for (PatientRoleFlag patientRoleFlag : patientRoleFlagSet) {

      PatientUserRoleFlagId patientUserRoleFlagId = new PatientUserRoleFlagId();
      patientUserRoleFlagId.setPatientId(patientId);
      patientUserRoleFlagId.setFlagId(patientRoleFlag.getRoleId());
      protossExpected.add(patientUserRoleFlagId);
    }
    when(
        patientFlagManager.createPatientRoleFlags(patientId, patientRoleFlagSet, context.getUser()))
            .thenReturn(protossExpected);
    Set<PatientRoleFlagDto> patientRoleFlagDto =
        mapDto(patientRoleFlagSet, PatientRoleFlagDto.class, HashSet::new);
    Set<PatientUserRoleFlagId> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .body(patientRoleFlagDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags")
            .then().assertThat().statusCode(200)
            .extract().as(PatientUserRoleFlagId[].class),
        HashSet::new);
    Assert.assertEquals(protossExpected, actual);
    verify(patientFlagManager).createPatientRoleFlags(patientId,
        patientRoleFlagSet, context.getUser());
  }

  @Test
  public void testCreatePatientRoleFlagsExisting() throws Exception {
    int patientId = RandomUtils.nextInt();
    Set<PatientRoleFlag> patientRoleFlagSet = getFixtures(PatientRoleFlag.class, HashSet::new, 3);

    when(patientFlagManager.getPatientRoleFlags(patientId)).thenReturn(patientRoleFlagSet);

    Set<PatientRoleFlagDto> patientRoleFlagDto =
        mapDto(patientRoleFlagSet, PatientRoleFlagDto.class, HashSet::new);
    given()
        .pathParam("patientId", patientId)
        .body(patientRoleFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags")
        .then().assertThat().statusCode(400);

    verify(patientFlagManager).getPatientRoleFlags(patientId);
    verify(patientFlagManager, never()).createPatientRoleFlags(patientId,
        patientRoleFlagSet, context.getUser());
  }

  @Test
  public void testUpdatePatientRoleFlags() throws Exception {
    Set<PatientRoleFlag> patientRoleFlagSet = getFixtures(PatientRoleFlag.class, HashSet::new, 3);
    int patientId = RandomUtils.nextInt();

    Set<PatientRoleFlagDto> roleFlags =
        mapDto(patientRoleFlagSet, PatientRoleFlagDto.class, HashSet::new);

    given()
        .pathParam("patientId", patientId)
        .body(roleFlags)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/role-flags")
        .then()
        .assertThat()
        .statusCode(200);

    verify(patientFlagManager).updatePatientRoleFlags(patientId, patientRoleFlagSet,
        context.getUser());
  }

  @Test
  public void getPatientUserFlags() throws Exception, SQLException {
    PatientUserFlag protossResult = getFixture(PatientUserFlag.class);
    protossResult.setUserId(context.getUser().getUserId());

    Set<PatientUserFlag> expectedSet = new HashSet<>();
    expectedSet.add(protossResult);

    int patientId = RandomUtils.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(expectedSet);

    Set<PatientUserFlagDto> actualFlag = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientUserFlagDto[].class),
        HashSet::new);
    Set<PatientUserFlagDto> expectedProtoss =
        mapDto(expectedSet, PatientUserFlagDto.class, HashSet::new);
    Assert.assertEquals(expectedProtoss, actualFlag);
    verify(patientFlagManager).getPatientUserFlags(patientId);
  }

  @Test
  public void getPatientUserFlagsNoUser() throws Exception {
    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));

    PatientUserFlag protossResult = getFixture(PatientUserFlag.class);

    Set<PatientUserFlag> expectedSet = new HashSet<>();
    expectedSet.add(protossResult);

    int patientId = RandomUtils.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(expectedSet);

    Set<PatientUserFlagDto> actualFlag = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientUserFlagDto[].class),
        HashSet::new);
    Set<PatientUserFlagDto> expectedProtoss =
        mapDto(expectedSet, PatientUserFlagDto.class, HashSet::new);
    Assert.assertEquals(expectedProtoss, actualFlag);
    verify(patientFlagManager).getPatientUserFlags(patientId);
  }

  @Test
  public void getPatientUserFlagsNoFlagsFound() throws Exception {
    PatientUserFlag protossResult = getFixture(PatientUserFlag.class);

    Set<PatientUserFlag> protossSet = Collections.singleton(protossResult);

    int patientId = RandomUtils.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(protossSet);

    Set<PatientUserFlagDto> actualFlag = toCollection(
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientUserFlagDto[].class),
        HashSet::new);
    assertTrue(actualFlag.isEmpty());
    verify(patientFlagManager).getPatientUserFlags(patientId);
  }

  @Test
  public void testDeletePatientUserFlag() throws Exception {
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    userFlag.setUserId(context.getUser().getUserId());
    int patientId = RandomUtils.nextInt();
    int userId = context.getUser().getUserId();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(Collections.singleton(userFlag));

    given()
        .pathParams("patientId", patientId, "userId", userId)
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags/{userId}")
        .then().assertThat().statusCode(200);
    verify(patientFlagManager).getPatientUserFlags(patientId);
    verify(patientFlagManager).deletePatientUserFlag(patientId, userId, context.getUser());
  }

  @Test
  public void testDeletePatientUserFlagWithDifferentUser() throws Exception {
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    int patientId = RandomUtils.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(Collections.singleton(userFlag));

    given()
        .pathParams("patientId", patientId, "userId", userFlag.getUserId())
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags/{userId}")
        .then().assertThat().statusCode(400);
    verify(patientFlagManager, never()).getPatientUserFlags(patientId);
    verify(patientFlagManager, never()).deletePatientUserFlag(patientId, userFlag.getUserId(),
        context.getUser());
  }

  @Test
  public void testDeletePatientUserFlagNoUser() throws Exception {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));

    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    int patientId = RandomUtils.nextInt();
    Integer userId = TestUtilities.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(Collections.singleton(userFlag));

    given()
        .pathParams("patientId", patientId, "userId", userId)
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags/{userId}")
        .then().assertThat().statusCode(200);
    verify(patientFlagManager).getPatientUserFlags(patientId);
    verify(patientFlagManager).deletePatientUserFlag(patientId, userId, context.getUser());
  }

  @Test
  public void testDeletePatientUserFlagNotFound() throws Exception {
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    int patientId = RandomUtils.nextInt();
    int userId = context.getUser().getUserId();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(Collections.singleton(userFlag));

    given()
        .pathParams("patientId", patientId, "userId", userId)
        .when()
        .delete(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags/{userId}")
        .then().assertThat().statusCode(404);
    verify(patientFlagManager).getPatientUserFlags(patientId);
    verify(patientFlagManager, never()).deletePatientUserFlag(patientId, userId, context.getUser());
  }

  @Test
  public void testCreatePatientUserFlags() throws Exception {
    int userId = context.getUser().getUserId();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    userFlag.setUserId(userId);
    PatientUserRoleFlagId patientUserRoleFlagId = new PatientUserRoleFlagId();
    patientUserRoleFlagId.setFlagId(userId);
    int patientId = RandomUtils.nextInt();
    patientUserRoleFlagId.setPatientId(patientId);
    Set<PatientUserRoleFlagId> expected = Collections.singleton(patientUserRoleFlagId);
    when(patientFlagManager.createPatientUserFlags(patientId,
        Collections.singleton(userFlag),
        context.getUser())).thenReturn(expected);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    PatientUserRoleFlagIdDto actual =
        given()
            .pathParam("patientId", patientId)
            .body(userFlagDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
            .then().assertThat().statusCode(200)
            .extract().as(PatientUserRoleFlagIdDto.class);
    PatientUserRoleFlagIdDto expectedDto =
        mapDto(patientUserRoleFlagId, PatientUserRoleFlagIdDto.class);
    Assert.assertEquals(expectedDto, actual);
    verify(patientFlagManager).createPatientUserFlags(patientId,
        Collections.singleton(userFlag), context.getUser());
  }

  @Test
  public void testCreatePatientUserFlagsWithDifferentUserId() throws Exception {
    int userId = context.getUser().getUserId();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    PatientUserRoleFlagId patientUserRoleFlagId = new PatientUserRoleFlagId();
    patientUserRoleFlagId.setFlagId(userId);
    int patientId = RandomUtils.nextInt();
    patientUserRoleFlagId.setPatientId(patientId);
    Set<PatientUserRoleFlagId> expected = Collections.singleton(patientUserRoleFlagId);
    when(patientFlagManager.createPatientUserFlags(patientId,
        Collections.singleton(userFlag),
        context.getUser())).thenReturn(expected);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);

    given()
        .pathParam("patientId", patientId)
        .body(userFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
        .then().assertThat().statusCode(400);
    PatientUserRoleFlagIdDto expectedDto =
        mapDto(patientUserRoleFlagId, PatientUserRoleFlagIdDto.class);

    verify(patientFlagManager, never()).createPatientUserFlags(patientId,
        Collections.singleton(userFlag), context.getUser());
  }

  @Test
  public void testCreatePatientUserFlagsNoUser() throws Exception {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    PatientUserRoleFlagId patientUserRoleFlagId = new PatientUserRoleFlagId();
    patientUserRoleFlagId.setFlagId(TestUtilities.nextInt());
    int patientId = RandomUtils.nextInt();
    patientUserRoleFlagId.setPatientId(patientId);
    Set<PatientUserRoleFlagId> expected = Collections.singleton(patientUserRoleFlagId);
    when(patientFlagManager.createPatientUserFlags(patientId,
        Collections.singleton(userFlag),
        context.getUser())).thenReturn(expected);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    PatientUserRoleFlagIdDto actual =
        given()
            .pathParam("patientId", patientId)
            .body(userFlagDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
            .then().assertThat().statusCode(200)
            .extract().as(PatientUserRoleFlagIdDto.class);
    PatientUserRoleFlagIdDto expectedDto =
        mapDto(patientUserRoleFlagId, PatientUserRoleFlagIdDto.class);
    Assert.assertEquals(expectedDto, actual);
    verify(patientFlagManager).createPatientUserFlags(patientId,
        Collections.singleton(userFlag), context.getUser());
  }

  @Test
  public void testCreatePatientUserFlagsExisting() throws Exception, SQLException {
    int userId = context.getUser().getUserId();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    userFlag.setUserId(userId);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    int patientId = RandomUtils.nextInt();
    when(patientFlagManager.getPatientUserFlags(patientId))
        .thenReturn(Collections.singleton(userFlag));
    given()
        .pathParam("patientId", patientId)
        .body(userFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
        .then().assertThat().statusCode(400);
    verify(patientFlagManager).getPatientUserFlags(patientId);
    verify(patientFlagManager, never()).createPatientUserFlags(patientId,
        Collections.singleton(userFlag), context.getUser());
  }

  @Test
  public void testUpdatePatientUserFlags() throws Exception {
    int patientId = RandomUtils.nextInt();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    userFlag.setUserId(context.getUser().getUserId());
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    given()
        .pathParam("patientId", patientId)
        .body(userFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(200);

    verify(patientFlagManager).updatePatientUserFlags(patientId, Collections.singleton(userFlag),
        context.getUser());
  }

  @Test
  public void testUpdatePatientUserFlagsWithDifferentUser() throws Exception {
    int patientId = RandomUtils.nextInt();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    given()
        .pathParam("patientId", patientId)
        .body(userFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientFlagManager, never()).updatePatientUserFlags(patientId,
        Collections.singleton(userFlag),
        context.getUser());
  }

  @Test
  public void testUpdatePatientUserFlagsNoUser() throws Exception {
    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    int patientId = RandomUtils.nextInt();
    PatientUserFlag userFlag = getFixture(PatientUserFlag.class);
    PatientUserFlagDto userFlagDto = mapDto(userFlag, PatientUserFlagDto.class);
    given()
        .pathParam("patientId", patientId)
        .body(userFlagDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_STRING + "/{patientId}/user-flags")
        .then()
        .assertThat()
        .statusCode(200);

    verify(patientFlagManager).updatePatientUserFlags(patientId, Collections.singleton(userFlag),
        context.getUser());
  }
}
