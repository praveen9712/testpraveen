
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.security.DefaultProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.db.security.permissions.ProviderPermissionFixture;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PermissionsType;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PermissionsEndpointIT extends
    AbstractEndpointIntegrationTest<PermissionsEndpoint> {

  private final ProviderPermissionManager providerPermissionManager;
  private final AuditLogUser user;
  private ApiSecurityContext context;
  private static final String endpointUrl = "/v1/accessible-providers";

  public PermissionsEndpointIT() throws IOException {
    super(new PermissionsEndpoint(), PermissionsEndpoint.class);
    user = getFixture(AuditLogUser.class);
    providerPermissionManager = new DefaultProviderPermissionManager(ds,
        getSecurityContext().getAuthorizationContext(), user);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(ProviderPermissionManager.class, providerPermissionManager);
  }

  @Test
  public void testGetAccessibleProviders() throws Exception {

    PermissionsType permissionsType = getFixture(PermissionsType.class);

    try (Connection conn = getConnection()) {
      ProviderPermissionFixture providerPermissionFixture = new ProviderPermissionFixture();
      providerPermissionFixture.setUp(conn);
      AuditLogUser user =
          new AuditLogUser(providerPermissionFixture.getUserId(), -1, null, null, null);
      context.setUser(user);

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

      assertTrue(actual.isEmpty());
    }
  }


}
