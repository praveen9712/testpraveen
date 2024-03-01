
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AccuroSettingsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class AccuroSettingsEndpointTest extends AbstractEndpointTest<AccuroSettingsEndpoint> {

  private AccuroPreferenceManager manager;
  private AuditLogUser user;

  ApiSecurityContext context;

  public AccuroSettingsEndpointTest() {
    super(new AccuroSettingsEndpoint(), AccuroSettingsEndpoint.class);
    manager = mock(AccuroPreferenceManager.class);
    user = getFixture(AuditLogUser.class);
  }


  @Override
  protected ApiSecurityContext getSecurityContext() {
    context = new ApiSecurityContext();
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
    String timeZone = "Canada/Pacific";

    when(manager.getSystemPreference("TimeZone")).thenReturn(timeZone);
    AccuroSettingsDto expected = new AccuroSettingsDto();
    expected.setProvince(context.getAccuroApiContext().getAccuroProvince().name());
    expected.setMode(context.getAccuroApiContext().getAccuroMode().toString());
    expected.setTimeZone(timeZone);

    AccuroSettingsDto accuroSettingsDto = given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/accuro-settings")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(AccuroSettingsDto.class);
    Assert.assertNotNull(accuroSettingsDto);
    Assert.assertEquals(expected, accuroSettingsDto);


  }


}
