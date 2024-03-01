
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.junit.Test;

public class AccuroPreferencesEndpointTest extends
    AbstractEndpointTest<AccuroPreferencesEndpoint> {

  private static final String endpointUrl = "/v1/accuro-preferences";
  private final AccuroPreferenceManager accuroPreferenceManager;
  private final ApiSecurityContext context;

  public AccuroPreferencesEndpointTest() {
    super(new AccuroPreferencesEndpoint(), AccuroPreferencesEndpoint.class);
    accuroPreferenceManager = mock(AccuroPreferenceManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(AccuroPreferenceManager.class, accuroPreferenceManager);
    return servicesMap;
  }

  @Test
  public void testGetPreferenceValue() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);
    String value = TestUtilities.nextString(10);

    doReturn(value).when(accuroPreferenceManager).getPreference(path, name);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .when()
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(200)
        .extract().asString();

    verify(accuroPreferenceManager).getPreference(path, name);
  }

  @Test
  public void testGetPreferenceValueNoResponse() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .when()
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(404);

  }

  @Test
  public void testGetPreferenceValueWithNullPath() throws Exception {

    String name = TestUtilities.nextString(10);
    given()
        .queryParam("name", name)
        .when()
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void testGetPreferenceValueWithInvalidPathOrName() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);

    doThrow(DatabaseInteractionException.class).when(accuroPreferenceManager)
        .getPreference(path, name);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .when()
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(500);

    verify(accuroPreferenceManager).getPreference(path, name);
  }

  @Test
  public void testCreateOrUpdatePreferences() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);
    String value = TestUtilities.nextString(10);

    doNothing().when(accuroPreferenceManager).createOrUpdatePreferenceValue(path, name, value);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .contentType(MediaType.APPLICATION_JSON) // set the content type to JSON
        .body(value)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(204);

    verify(accuroPreferenceManager).createOrUpdatePreferenceValue(path, name, value);
  }

  @Test
  public void testCreateOrUpdatePreferencesWithNoBody() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .contentType(MediaType.APPLICATION_JSON) // set the content type to JSON
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void testCreateOrUpdatePreferencesWithInvalidPathOrName() throws Exception {
    String path = TestUtilities.nextString(10);
    String name = TestUtilities.nextString(10);
    String value = TestUtilities.nextString(10);

    doThrow(NoDataFoundException.class).when(accuroPreferenceManager)
        .createOrUpdatePreferenceValue(path, name, value);

    given()
        .queryParam("path", path)
        .queryParam("name", name)
        .contentType(MediaType.APPLICATION_JSON) // set the content type to JSON
        .body(value)
        .when()
        .put(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(400);

    verify(accuroPreferenceManager).createOrUpdatePreferenceValue(path, name, value);
  }

}
