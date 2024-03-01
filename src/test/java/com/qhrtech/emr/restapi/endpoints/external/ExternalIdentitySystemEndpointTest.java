
package com.qhrtech.emr.restapi.endpoints.external;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.identity.ExternalUserIdentityManager;
import com.qhrtech.emr.accuro.model.identity.ExternalIdentitySystem;
import com.qhrtech.emr.accuro.model.identity.ExternalUserIdentity;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.external.ExternalIdentitySystemDto;
import com.qhrtech.emr.restapi.models.dto.external.Vendor;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ExternalIdentitySystemEndpointTest
    extends AbstractEndpointTest<ExternalIdentitySystemEndpoint> {

  private ExternalUserIdentityManager userIdentityManager;

  public ExternalIdentitySystemEndpointTest() {
    super(new ExternalIdentitySystemEndpoint(), ExternalIdentitySystemEndpoint.class);
    userIdentityManager = mock(ExternalUserIdentityManager.class);
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
    return services;
  }

  @Test
  public void testCreate() throws Exception {
    ExternalIdentitySystemDto systemDto = getFixture(ExternalIdentitySystemDto.class);
    systemDto.setVendorName(TestUtilities.nextValue(Vendor.class).getVendorName());

    long expected = systemDto.getSystemId();

    when(userIdentityManager.createIdentitySystem(any())).thenReturn(expected);

    long actual = given()
        .body(systemDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/external-id-systems")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract()
        .as(Long.class);

    assertEquals(expected, actual);
    verify(userIdentityManager).createIdentitySystem(any());
  }

  @Test
  public void testCreateNull() throws Exception {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/external-id-systems")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).createIdentitySystem(any());
  }

  @Test
  public void testCreateInvalidVendor() throws Exception {
    ExternalIdentitySystemDto systemDto = getFixture(ExternalIdentitySystemDto.class);

    given()
        .body(systemDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/external-id-systems")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).createIdentitySystem(any());
  }

  @Test
  public void testUpdate() throws Exception {
    ExternalIdentitySystemDto systemDto = getFixture(ExternalIdentitySystemDto.class);
    systemDto.setVendorName(TestUtilities.nextValue(Vendor.class).getVendorName());
    ExternalIdentitySystem system = mapDto(systemDto, ExternalIdentitySystem.class);

    doNothing().when(userIdentityManager).updateIdentitySystem(system);

    given()
        .pathParam("id", systemDto.getSystemId())
        .body(systemDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userIdentityManager).updateIdentitySystem(system);
  }

  @Test
  public void testUpdateNull() throws Exception {
    ExternalIdentitySystemDto systemDto = getFixture(ExternalIdentitySystemDto.class);

    given()
        .pathParam("id", TestUtilities.nextId())
        .body(systemDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateIdentitySystem(any());
  }

  @Test
  public void testUpdateInvalidVendor() throws Exception {
    ExternalIdentitySystemDto systemDto = getFixture(ExternalIdentitySystemDto.class);
    ExternalIdentitySystem system = mapDto(systemDto, ExternalIdentitySystem.class);

    doNothing().when(userIdentityManager).updateIdentitySystem(system);

    given()
        .pathParam("id", systemDto.getSystemId())
        .body(systemDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateIdentitySystem(system);
  }

  @Test
  public void testUpdateDifferentId() throws Exception {
    given()
        .pathParam("id", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).updateIdentitySystem(any());
  }

  @Test
  public void testDelete() throws Exception {
    long id = TestUtilities.nextId();

    doNothing().when(userIdentityManager).deleteExtIdentitySystem(id);

    given()
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(userIdentityManager).deleteExtIdentitySystem(id);
  }

  @Test
  public void testDeleteBadRequest() throws Exception {
    long id = TestUtilities.nextId();

    doNothing().when(userIdentityManager).deleteExtIdentitySystem(id);

    when(userIdentityManager.getUserIdentityBySystemId(id)).thenReturn(
        Collections.singleton(getFixture(ExternalUserIdentity.class)));

    given()
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(userIdentityManager, never()).deleteExtIdentitySystem(id);
  }

  @Test
  public void testGetById() throws Exception {
    ExternalIdentitySystemDto expected = getFixture(ExternalIdentitySystemDto.class);
    ExternalIdentitySystem system = mapDto(expected, ExternalIdentitySystem.class);

    doReturn(system).when(userIdentityManager).getIdentitySystemById(system.getSystemId());

    ExternalIdentitySystemDto actual = given()
        .pathParam("id", expected.getSystemId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalIdentitySystemDto.class);

    assertEquals(expected, actual);
    verify(userIdentityManager).getIdentitySystemById(system.getSystemId());
  }

  @Test
  public void testGetByInvalidId() throws Exception {
    ExternalIdentitySystemDto system = getFixture(ExternalIdentitySystemDto.class);

    given()
        .pathParam("id", system.getSystemId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-systems/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(userIdentityManager).getIdentitySystemById(system.getSystemId());
  }

  @Test
  public void testGetAll() throws Exception {
    Set<ExternalIdentitySystemDto> expected =
        getFixtures(ExternalIdentitySystemDto.class, HashSet::new, 5);
    Set<ExternalIdentitySystem> system =
        mapDto(expected, ExternalIdentitySystem.class, HashSet::new);

    doReturn(system).when(userIdentityManager).getIdentitySystems();

    Set<ExternalIdentitySystemDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/external-id-systems")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ExternalIdentitySystemDto[].class), HashSet::new);

    assertEquals(expected, actual);
    verify(userIdentityManager).getIdentitySystems();
  }
}
