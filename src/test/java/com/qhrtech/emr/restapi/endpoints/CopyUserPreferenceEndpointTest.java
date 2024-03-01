
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.security.UserPreferencesManager;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class CopyUserPreferenceEndpointTest
    extends AbstractEndpointTest<CopyUserPreferenceEndpoint> {

  private final UserPreferencesManager manager;
  private final AuditLogUser user;
  private ApiSecurityContext context;
  private static final String endpointUrl = "/v1/users/{userId}/copy-user-preferences";


  public CopyUserPreferenceEndpointTest() {
    super(new CopyUserPreferenceEndpoint(), CopyUserPreferenceEndpoint.class);
    manager = mock(UserPreferencesManager.class);
    user = getFixture(AuditLogUser.class);

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
    servicesMap.put(UserPreferencesManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testCopyUserPermissionsSuccess() throws Exception {

    int sourceUser = TestUtilities.nextId();
    int userId = TestUtilities.nextId();

    doNothing().when(manager).copyUserPreferences(sourceUser, userId);

    given()
        .pathParam("userId", userId)
        .queryParam("sourceUser", sourceUser)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    verify(manager).copyUserPreferences(sourceUser, userId);

  }
}
