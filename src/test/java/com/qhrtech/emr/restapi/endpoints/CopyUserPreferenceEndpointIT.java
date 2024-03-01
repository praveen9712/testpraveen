
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.security.DefaultUserPreferencesManager;
import com.qhrtech.emr.accuro.api.security.UserPreferencesManager;
import com.qhrtech.emr.accuro.db.security.AccuroUserFixture;
import com.qhrtech.emr.accuro.db.security.UserPreferenceFixture;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.UserPreference;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class CopyUserPreferenceEndpointIT
    extends AbstractEndpointIntegrationTest<CopyUserPreferenceEndpoint> {

  private final UserPreferencesManager manager;
  private static final String endpointUrl = "/v1/users/{userId}/copy-user-preferences";


  public CopyUserPreferenceEndpointIT() throws IOException {
    super(new CopyUserPreferenceEndpoint(), CopyUserPreferenceEndpoint.class);
    manager = new DefaultUserPreferencesManager(ds, getSecurityContext().getAuthorizationContext(),
        defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(UserPreferencesManager.class, manager);
    return servicesMap;
  }

  @Test
  public void copyFeaturePermissionsTest() throws Exception {

    try (Connection conn = getConnection()) {

      UserPreferenceFixture preferenceFixture = new UserPreferenceFixture();
      preferenceFixture.setUp(conn);
      UserPreference sourcePref = preferenceFixture.get();
      AccuroUserFixture accuroUserFixture = new AccuroUserFixture();
      accuroUserFixture.setUp(conn);
      AccuroUser accuroUser = accuroUserFixture.get();

      given()
          .pathParam("userId", accuroUser.getUserId())
          .queryParam("sourceUser", sourcePref.getUserId())
          .when()
          .put(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      List<UserPreference> destPref =
          manager.getUserPreferencesByUserId(sourcePref.getUserId());

      assertTrue(destPref.contains(sourcePref));
    }
  }
}
