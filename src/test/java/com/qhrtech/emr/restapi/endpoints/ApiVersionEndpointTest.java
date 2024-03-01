
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

/**
 * This endpoint test class has unit test and cannot completely test the results expected from the
 * ApiVersionEndpoint endpoint as the endpoint fetches the data from the MANIFEST file which is
 * created during the build creation. Through this test, we are are just testing that endpoint is
 * called when correct endpoint path given in the GET request.
 **/
public class ApiVersionEndpointTest extends AbstractEndpointTest<ApiVersionEndpoint> {


  public ApiVersionEndpointTest() {
    super(new ApiVersionEndpoint(), ApiVersionEndpoint.class);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {

    return Collections.EMPTY_MAP;
  }

  @Test
  public void getApiVersion() throws Exception {

    given()
        .when()
        .get(getBaseUrl() + "/v1/api-version")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }
}
