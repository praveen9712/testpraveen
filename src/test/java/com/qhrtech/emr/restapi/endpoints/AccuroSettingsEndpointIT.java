
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.api.preferences.DefaultAccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AccuroSettingsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class AccuroSettingsEndpointIT extends
    AbstractEndpointIntegrationTest<AccuroSettingsEndpoint> {

  private AccuroPreferenceManager manager;
  private AuditLogUser user;

  public AccuroSettingsEndpointIT() throws IOException {
    super(new AccuroSettingsEndpoint(), AccuroSettingsEndpoint.class);
    manager = new DefaultAccuroPreferenceManager(getDs());
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
  }


  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    context.setAccuroApiContext(getFixture(AccuroApiContext.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(AccuroPreferenceManager.class, manager);
    return managerMap;
  }


  @Test
  public void testgetAccuroSettings() throws Exception {

    AccuroSettingsDto accuroSettingsDto = given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/accuro-settings")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(AccuroSettingsDto.class);

    Assert.assertNotNull(accuroSettingsDto);


  }


}
