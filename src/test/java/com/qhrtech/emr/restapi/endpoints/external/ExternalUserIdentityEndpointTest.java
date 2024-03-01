
package com.qhrtech.emr.restapi.endpoints.external;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.identity.ExternalUserIdentityManager;
import com.qhrtech.emr.accuro.api.security.UserInfoManager;
import com.qhrtech.emr.accuro.model.identity.ExternalIdentitySystem;
import com.qhrtech.emr.accuro.model.identity.ExternalUserIdentity;
import com.qhrtech.emr.accuro.model.security.UserInfo;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.external.ExternalUserIdentityDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ExternalUserIdentityEndpointTest
    extends AbstractEndpointTest<ExternalUserIdentityEndpoint> {

  private ExternalUserIdentityManager userIdentityManager;
  private UserInfoManager userInfoManager;

  public ExternalUserIdentityEndpointTest() {
    super(new ExternalUserIdentityEndpoint(), ExternalUserIdentityEndpoint.class);
    userIdentityManager = mock(ExternalUserIdentityManager.class);
    userInfoManager = mock(UserInfoManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(getFixture(AuditLogUser.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> services = new HashMap<>();
    services.put(ExternalUserIdentityManager.class, userIdentityManager);
    services.put(UserInfoManager.class, userInfoManager);
    return services;
  }

  @Test
  public void testCreate() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    doNothing().when(userIdentityManager).createUserIdentity(any());
    doReturn(getFixture(ExternalIdentitySystem.class))
        .when(userIdentityManager)
        .getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    doReturn(null)
        .when(userIdentityManager)
        .getUserIdentity(userIdentityDto.getExtIdSystemId(), userIdentityDto.getIdentity());

    UserInfo userInfo = getFixture(UserInfo.class);
    doReturn(userInfo).when(userInfoManager).getById(userIdentityDto.getUserId());

    given()
        .pathParam("externalSystemId", userIdentity.getExtIdSystemId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode());

    verify(userIdentityManager).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager).createUserIdentity(any());
  }

  @Test
  public void testCreateNull() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    given()
        .pathParam("externalSystemId", userIdentity.getExtIdSystemId())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager, never()).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager, never()).createUserIdentity(any());
  }

  @Test
  public void testCreateWithDifferentSystemId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);

    given()
        .pathParam("externalSystemId", TestUtilities.nextId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager, never()).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager, never()).createUserIdentity(any());
  }

  @Test
  public void testCreateWithInvalidSystemId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    given()
        .pathParam("externalSystemId", userIdentity.getExtIdSystemId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager, never()).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager, never()).createUserIdentity(any());
  }

  @Test
  public void testCreateExistedUser() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    doNothing().when(userIdentityManager).createUserIdentity(any());
    doReturn(getFixture(ExternalIdentitySystem.class))
        .when(userIdentityManager)
        .getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    doReturn(userIdentity)
        .when(userIdentityManager)
        .getUserIdentity(userIdentityDto.getExtIdSystemId(), userIdentityDto.getIdentity());

    given()
        .pathParam("externalSystemId", userIdentity.getExtIdSystemId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager, never()).createUserIdentity(any());
  }

  @Test
  public void testCreateWithInvalidAccuroUserId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    doNothing().when(userIdentityManager).createUserIdentity(any());
    doReturn(getFixture(ExternalIdentitySystem.class))
        .when(userIdentityManager)
        .getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    doReturn(null)
        .when(userIdentityManager)
        .getUserIdentity(userIdentityDto.getExtIdSystemId(), userIdentityDto.getIdentity());

    given()
        .pathParam("externalSystemId", userIdentity.getExtIdSystemId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager).getIdentitySystemById(userIdentityDto.getExtIdSystemId());
    verify(userIdentityManager).getUserIdentity(userIdentityDto.getExtIdSystemId(),
        userIdentityDto.getIdentity());
    verify(userIdentityManager, never()).createUserIdentity(any());
  }

  @Test
  public void testUpdate() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    doNothing().when(userIdentityManager).updateUserIdentity(any());
    doReturn(getFixture(ExternalIdentitySystem.class)).when(userIdentityManager)
        .getIdentitySystemById(userIdentity.getExtIdSystemId());

    UserInfo userInfo = getFixture(UserInfo.class);
    doReturn(userInfo).when(userInfoManager).getById(userIdentityDto.getUserId());

    given()
        .pathParam("externalSystemId", userIdentityDto.getExtIdSystemId())
        .pathParam("externalUserId", userIdentityDto.getIdentity())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userIdentityManager).updateUserIdentity(any());
  }

  @Test
  public void testUpdateNull() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);

    given()
        .pathParam("externalSystemId", userIdentityDto.getExtIdSystemId())
        .pathParam("externalUserId", userIdentityDto.getIdentity())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateUserIdentity(any());
  }

  @Test
  public void testUpdateDifferentSystemId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);

    given()
        .pathParam("externalSystemId", TestUtilities.nextId())
        .pathParam("externalUserId", userIdentityDto.getIdentity())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateUserIdentity(any());
  }

  @Test
  public void testUpdateDifferentUserId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);

    given()
        .pathParam("externalSystemId", userIdentityDto.getExtIdSystemId())
        .pathParam("externalUserId", TestUtilities.nextId())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateUserIdentity(any());
  }

  @Test
  public void testUpdateInvalidSystemId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);

    doNothing().when(userIdentityManager).updateUserIdentity(any());

    given()
        .pathParam("externalSystemId", userIdentityDto.getExtIdSystemId())
        .pathParam("externalUserId", userIdentityDto.getIdentity())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateUserIdentity(any());
  }

  @Test
  public void testUpdateWithInvalidAccuroUserId() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    userIdentityDto.setValidFromInUtc(null);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);

    doNothing().when(userIdentityManager).updateUserIdentity(any());
    doReturn(getFixture(ExternalIdentitySystem.class)).when(userIdentityManager)
        .getIdentitySystemById(userIdentity.getExtIdSystemId());

    given()
        .pathParam("externalSystemId", userIdentityDto.getExtIdSystemId())
        .pathParam("externalUserId", userIdentityDto.getIdentity())
        .body(userIdentityDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateUserIdentity(any());
  }

  @Test
  public void testDelete() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    ExternalUserIdentity userIdentity = mapDto(userIdentityDto, ExternalUserIdentity.class);
    long externalSystemId = userIdentityDto.getExtIdSystemId();
    String externalUserId = userIdentityDto.getIdentity();

    doReturn(userIdentity)
        .when(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);
    doNothing().when(userIdentityManager).deleteUserIdentity(externalSystemId, externalUserId);

    given()
        .pathParam("externalSystemId", externalSystemId)
        .pathParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);
    verify(userIdentityManager).deleteUserIdentity(externalSystemId, externalUserId);
  }

  @Test
  public void testDeleteNull() throws Exception {
    ExternalUserIdentityDto userIdentityDto = getFixture(ExternalUserIdentityDto.class);
    long externalSystemId = userIdentityDto.getExtIdSystemId();
    String externalUserId = userIdentityDto.getIdentity();

    given()
        .pathParam("externalSystemId", externalSystemId)
        .pathParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);
    verify(userIdentityManager, never()).deleteUserIdentity(externalSystemId, externalUserId);
  }

  @Test
  public void testGetByIdentifier() throws Exception {
    ExternalUserIdentityDto expected = getFixture(ExternalUserIdentityDto.class);
    ExternalUserIdentity userIdentity = mapDto(expected, ExternalUserIdentity.class);

    long externalSystemId = expected.getExtIdSystemId();
    String externalUserId = expected.getIdentity();

    doReturn(userIdentity)
        .when(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);

    ExternalUserIdentityDto actual = given()
        .pathParam("externalSystemId", externalSystemId)
        .pathParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto.class);

    assertEquals(expected, actual);
    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);
  }

  @Test
  public void testGetByInvalidIdentifier() throws Exception {
    ExternalUserIdentityDto identity = getFixture(ExternalUserIdentityDto.class);

    long externalSystemId = identity.getExtIdSystemId();
    String externalUserId = identity.getIdentity();

    given()
        .pathParam("externalSystemId", externalSystemId)
        .pathParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl()
            + "/v1/external-id-systems/{externalSystemId}"
            + "/external-id-users/{externalUserId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId);
  }

  @Test
  public void testGetExternalUserIdentities() throws Exception {
    int accuroUserId = TestUtilities.nextId();
    String externalUserId = TestUtilities.nextString(10);
    long externalSystemId = TestUtilities.nextId();
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setIdentity(externalUserId);
      user.setExtIdSystemId(externalSystemId);
      user.setUserId(accuroUserId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(externalSystemId, externalUserId, accuroUserId);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("accuroUserId", accuroUserId)
        .queryParam("externalUserId", externalUserId)
        .queryParam("systemId", externalSystemId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId, accuroUserId);
  }

  @Test
  public void testGetExternalUserIdentitiesWithoutAccuroUserId() throws Exception {
    String externalUserId = TestUtilities.nextString(10);
    long externalSystemId = TestUtilities.nextId();
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setIdentity(externalUserId);
      user.setExtIdSystemId(externalSystemId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(externalSystemId, externalUserId, null);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("externalUserId", externalUserId)
        .queryParam("systemId", externalSystemId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(externalSystemId, externalUserId, null);
  }

  @Test
  public void testGetExternalUserIdentitiesWithoutExternalSystemId() throws Exception {
    int accuroUserId = TestUtilities.nextId();
    String externalUserId = TestUtilities.nextString(10);
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setIdentity(externalUserId);
      user.setUserId(accuroUserId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(null, externalUserId, accuroUserId);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("accuroUserId", accuroUserId)
        .queryParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(null, externalUserId, accuroUserId);
  }

  @Test
  public void testGetExternalUserIdentitiesWithoutExternalUserId() throws Exception {
    int accuroUserId = TestUtilities.nextId();
    long externalSystemId = TestUtilities.nextId();
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setExtIdSystemId(externalSystemId);
      user.setUserId(accuroUserId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(externalSystemId, null, accuroUserId);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("accuroUserId", accuroUserId)
        .queryParam("systemId", externalSystemId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(externalSystemId, null, accuroUserId);
  }

  @Test
  public void testGetExternalUserIdentitiesWithAccuroUserIdOnly() throws Exception {
    int accuroUserId = TestUtilities.nextId();
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setUserId(accuroUserId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(null, null, accuroUserId);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("accuroUserId", accuroUserId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(null, null, accuroUserId);
  }

  @Test
  public void testGetExternalUserIdentitiesWithExternalUserIdOnly() throws Exception {
    String externalUserId = TestUtilities.nextString(10);
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    for (ExternalUserIdentityDto user : expected) {
      user.setIdentity(externalUserId);
    }
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentity(null, externalUserId, null);

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .queryParam("externalUserId", externalUserId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentity(null, externalUserId, null);
  }

  @Test
  public void testGetExternalUserIdentitiesWithSystemIdOnly() throws Exception {
    long externalSystemId = TestUtilities.nextId();

    given()
        .queryParam("systemId", externalSystemId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());

  }


  @Test
  public void testGetAllExternalUserIdentities() throws Exception {
    Set<ExternalUserIdentityDto> expected =
        getFixtures(ExternalUserIdentityDto.class, HashSet::new, 5);
    List<ExternalUserIdentity> userIdentities =
        mapDto(expected, ExternalUserIdentity.class, ArrayList::new);

    doReturn(userIdentities)
        .when(userIdentityManager).getUserIdentities();

    List<ExternalUserIdentityDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-users")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalUserIdentityDto[].class), ArrayList::new);

    assertTrue(actual.containsAll(expected));
    verify(userIdentityManager).getUserIdentities();
  }
}
