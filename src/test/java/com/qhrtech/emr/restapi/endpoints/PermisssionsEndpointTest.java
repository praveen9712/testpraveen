
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PermissionsType;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PermisssionsEndpointTest extends AbstractEndpointTest<PermissionsEndpoint> {

  private ProviderPermissionManager manager;
  private AuditLogUser user;

  public PermisssionsEndpointTest() {
    super(new PermissionsEndpoint(), PermissionsEndpoint.class);
    manager = mock(ProviderPermissionManager.class);
    user = getFixture(AuditLogUser.class);
  }


  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(ProviderPermissionManager.class, manager);
    return managerMap;
  }

  @Test
  public void testGetAccessibleProviders() {
    PermissionsType permissionsType = getFixture(PermissionsType.class);
    int provider1 = TestUtilities.nextInt();
    int provider2 = TestUtilities.nextInt();

    Set<Integer> permissibleProviders = new HashSet<>(Arrays.asList(provider1, provider2));
    doReturn(permissibleProviders).when(manager)
        .getAccessibleProvidersForUser(
            com.qhrtech.emr.accuro.permissions.PermissionsType.valueOf(permissionsType.name()));

    // test
    ArrayList<Integer> actual = toCollection(
        given()
            .when()
            .queryParam("permissionsType", permissionsType)
            .get(getBaseUrl() + "/v1/accessible-providers")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(Integer[].class),
        ArrayList::new);

    List<Integer> expected = permissibleProviders.stream().sorted().collect(Collectors.toList());

    // assertions
    assertEquals(expected, actual);
    verify(manager).getAccessibleProvidersForUser(
        com.qhrtech.emr.accuro.permissions.PermissionsType.valueOf(permissionsType.name()));
  }


  @Test
  public void testGetAccessibleProvidersNull() {

    // test
    given()
        .when()
        .get(getBaseUrl() + "/v1/accessible-providers")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    // assertions
    verifyNoInteractions(manager);
  }


}
