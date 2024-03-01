
package com.qhrtech.emr.restapi.endpoints.provider.security;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.security.roles.RoleManager;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.accuro.permissions.roles.Role;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.security.RoleDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class RoleEndpointTest extends AbstractEndpointTest<RoleEndpoint> {

  private final RoleManager roleManager;

  public RoleEndpointTest() {
    super(new RoleEndpoint(), RoleEndpoint.class);
    roleManager = mock(RoleManager.class);
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
    services.put(RoleManager.class, roleManager);
    return services;
  }

  @Test
  public void testGetAll() throws Exception {
    Set<Role> roles = getFixtures(Role.class, HashSet::new, 5);
    Set<RoleDto> expected = mapDto(roles, RoleDto.class, HashSet::new);

    doReturn(roles).when(roleManager).getAllRoles();

    Set<RoleDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/roles")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(RoleDto[].class),
        HashSet::new);

    assertEquals(expected, actual);
    verify(roleManager).getAllRoles();
  }

  @Test
  public void testGetAllInActive() throws Exception {
    Set<Role> roles = getFixtures(Role.class, HashSet::new, 4);
    Role inActiveRole = getFixture(Role.class);
    inActiveRole.setActive(false);
    roles.add(inActiveRole);
    Set<RoleDto> expected = mapDto(roles, RoleDto.class, HashSet::new);

    doReturn(roles).when(roleManager).getAllRoles();

    Set<RoleDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/roles")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(RoleDto[].class),
        HashSet::new);

    RoleDto inActiveRoleDto = mapDto(inActiveRole, RoleDto.class);

    assertFalse(actual.contains(inActiveRoleDto));
    assertNotEquals(expected, actual);
    verify(roleManager).getAllRoles();
  }

  @Test
  public void testGetById() throws Exception {
    Role role = getFixture(Role.class);
    RoleDto expected = mapDto(role, RoleDto.class);

    doReturn(role).when(roleManager).getRoleById(expected.getRoleId());

    RoleDto actual =
        given()
            .pathParam("id", expected.getRoleId())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/roles/{id}")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(RoleDto.class);

    assertEquals(expected, actual);
    verify(roleManager).getRoleById(expected.getRoleId());
  }

  @Test
  public void testGetByInvalidId() throws Exception {
    int id = TestUtilities.nextId();

    given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/roles/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(roleManager).getRoleById(id);
  }
}
