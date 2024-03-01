
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.office.DefaultOfficeManager;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.DefaultFeaturePermissionManager;
import com.qhrtech.emr.accuro.api.security.DefaultProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserPermissionManager;
import com.qhrtech.emr.accuro.api.security.FeaturePermissionManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.UserPermissionManager;
import com.qhrtech.emr.accuro.api.security.roles.DefaultRoleManager;
import com.qhrtech.emr.accuro.api.security.roles.RoleManager;
import com.qhrtech.emr.accuro.db.security.AccuroUserFixture;
import com.qhrtech.emr.accuro.db.security.features.FeaturePermissionFixture;
import com.qhrtech.emr.accuro.db.security.permissions.ProviderPermissionFixture;
import com.qhrtech.emr.accuro.db.security.roles.RoleFixture;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class CopyUserPermissionsEndpointIT extends
    AbstractEndpointIntegrationTest<CopyUserPermissionsEndpoint> {

  private final UserPermissionManager userPermissionManager;
  private final OfficeManager officeManager;
  private final FeaturePermissionManager featurePermissionManager;
  private final ProviderPermissionManager providerPermissionManager;
  private final RoleManager roleManager;
  private final AuditLogUser user;
  private ApiSecurityContext context;
  private static final String endpointUrl = "/v1/users/{userId}/copy-user-permissions";

  public CopyUserPermissionsEndpointIT() throws IOException {
    super(new CopyUserPermissionsEndpoint(), CopyUserPermissionsEndpoint.class);
    userPermissionManager = new DefaultUserPermissionManager(ds);
    officeManager = new DefaultOfficeManager(ds);
    featurePermissionManager = new DefaultFeaturePermissionManager(ds);
    roleManager = new DefaultRoleManager(ds);
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
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(UserPermissionManager.class, userPermissionManager);
    servicesMap.put(OfficeManager.class, officeManager);
    return servicesMap;
  }

  @Test
  public void copyFeaturePermissionsTest() throws Exception {

    try (Connection conn = getConnection()) {
      FeaturePermissionFixture featurePermissionFixture = new FeaturePermissionFixture();
      featurePermissionFixture.setUp(conn);
      AccuroUserFixture accuroUserFixture = new AccuroUserFixture();
      accuroUserFixture.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture.get();
      AuditLogUser user = new AuditLogUser(accuroUser.getUserId(), -1, null, null, null);

      given()
          .pathParam("userId", accuroUser.getUserId())
          .queryParam("sourceUser", featurePermissionFixture.getRoleFixture().getUser().getUserId())
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Status.NO_CONTENT.getStatusCode());
      assertEquals(featurePermissionFixture.getFeaturePermissions(),
          featurePermissionManager.getUserFeaturePermissions(user));
    }
  }

  @Test
  public void copyProviderPermissionsTest() throws Exception {

    try (Connection conn = getConnection()) {
      ProviderPermissionFixture providerPermissionFixture;
      AccuroUserFixture accuroUserFixture2 = new AccuroUserFixture();
      accuroUserFixture2.setUp(conn);
      AccuroUser accuroUser1 = accuroUserFixture2.get();
      providerPermissionFixture = new ProviderPermissionFixture();
      providerPermissionFixture.setUserId(accuroUser1.getUserId());
      providerPermissionFixture.setUp(conn);
      AccuroUserFixture accuroUserFixture1 = new AccuroUserFixture();
      accuroUserFixture1.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture1.get();
      AuditLogUser user = new AuditLogUser(accuroUser.getUserId(), -1, null, null, null);
      AuditLogUser user1 = new AuditLogUser(accuroUser1.getUserId(), -1, null, null, null);

      given()
          .pathParam("userId", accuroUser.getUserId())
          .queryParam("sourceUser", accuroUser1.getUserId())
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Status.NO_CONTENT.getStatusCode());
      assertEquals(providerPermissionManager.getPermissionsForUser(user),
          providerPermissionManager.getPermissionsForUser(user1));
      assertEquals(providerPermissionManager.getPermissionsOverrideForUser(user),
          providerPermissionManager.getPermissionsOverrideForUser(user1));
      assertEquals(providerPermissionManager.getPermissionTemplateForUser(user),
          providerPermissionManager.getPermissionTemplateForUser(user1));
    }
  }

  @Test
  public void copyProviderPermissionsForOfficeTest() throws Exception {

    try (Connection conn = getConnection()) {
      ProviderPermissionFixture providerPermissionFixture;
      AccuroUserFixture accuroUserFixture2 = new AccuroUserFixture();
      accuroUserFixture2.setUp(conn);
      AccuroUser accuroUser1 = accuroUserFixture2.get();
      providerPermissionFixture = new ProviderPermissionFixture();
      providerPermissionFixture.setUserId(accuroUser1.getUserId());
      providerPermissionFixture.setUp(conn);
      AccuroUserFixture accuroUserFixture1 = new AccuroUserFixture();
      accuroUserFixture1.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture1.get();
      AuditLogUser user = new AuditLogUser(accuroUser.getUserId(), -1, null, null, null);
      AuditLogUser user1 = new AuditLogUser(accuroUser1.getUserId(), -1, null, null, null);

      Integer officeId = providerPermissionFixture.getOfficeId();

      given()
          .pathParam("userId", accuroUser.getUserId())
          .queryParam("sourceUser", accuroUser1.getUserId())
          .queryParam("includedOfficeId", officeId)
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Status.NO_CONTENT.getStatusCode());
      assertEquals(providerPermissionManager.getPermissionsForUser(user),
          providerPermissionManager.getPermissionsForUser(user1));
      assertEquals(providerPermissionManager.getPermissionsOverrideForUser(user),
          providerPermissionManager.getPermissionsOverrideForUser(user1));
      assertEquals(providerPermissionManager.getPermissionTemplateForUser(user),
          providerPermissionManager.getPermissionTemplateForUser(user1));
    }
  }

  @Test
  public void copyUserRolesTest() throws Exception {

    try (Connection conn = getConnection()) {
      AccuroUserFixture accuroUserFixture1 = new AccuroUserFixture();
      accuroUserFixture1.setUp(conn);
      AccuroUserFixture accuroUserFixture2 = new AccuroUserFixture();
      accuroUserFixture2.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture1.get();
      AccuroUser accuroUser1 = accuroUserFixture2.get();
      RoleFixture roleFixture = new RoleFixture();
      roleFixture.setUserId(accuroUser.getUserId());
      AuditLogUser user = new AuditLogUser(accuroUser.getUserId(), -1, null, null, null);
      AuditLogUser user1 = new AuditLogUser(accuroUser1.getUserId(), -1, null, null, null);

      roleFixture.setUp(conn);
      given()
          .pathParam("userId", accuroUser1.getUserId())
          .queryParam("sourceUser", accuroUser.getUserId())
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Status.NO_CONTENT.getStatusCode());

      assertEquals(roleManager.getRoles(user),
          roleManager.getRoles(user1));
    }
  }

  @Test
  public void copyUserPermissionsExp() throws Exception {

    try (Connection conn = getConnection()) {
      AccuroUserFixture accuroUserFixture1 = new AccuroUserFixture();
      accuroUserFixture1.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture1.get();
      RoleFixture roleFixture = new RoleFixture();
      roleFixture.setUserId(accuroUser.getUserId());
      AuditLogUser user = new AuditLogUser(accuroUser.getUserId(), -1, null, null, null);

      roleFixture.setUp(conn);
      given()
          .pathParam("userId", -1)
          .queryParam("sourceUser", accuroUser.getUserId())
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(400);
    }
  }
}
