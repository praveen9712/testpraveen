
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.scheduling.SiteManager;
import com.qhrtech.emr.accuro.model.scheduling.Site;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class SiteEndpointTest extends AbstractEndpointTest<SiteEndpoint> {

  private final SiteManager siteManager;

  public SiteEndpointTest() {
    super(new SiteEndpoint(), SiteEndpoint.class);
    siteManager = mock(SiteManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(SiteManager.class, siteManager);
  }

  @Test
  public void testGetSiteNotSite() throws Exception {
    doReturn(null).when(siteManager).getSiteById(anyInt());
    when()
        .get(getBaseUrl() + "/v1/sites/2")
        .then()
        .assertThat().statusCode(404);
  }

  @Test
  public void testGetSite() throws Exception {
    Site site = getFixture(Site.class);
    doReturn(site).when(siteManager).getSiteById(anyInt());

    Response response = when().get(getBaseUrl() + "/v1/sites/2");

    response.then().assertThat().statusCode(200);

    // Check the Result
    JSONObject body = new JSONObject(response.body().asString());
    assertEquals(body.getInt("siteId"), site.getSiteId().intValue());
    assertEquals(body.getString("name"), site.getName());
    assertEquals(body.getString("abbreviation"), site.getAbbreviation());
    assertEquals(body.getString("shortName"), site.getShortName());
    assertEquals(body.getInt("siteOfficeId"), site.getSiteOfficeId().intValue());

    verify(siteManager).getSiteById(anyInt());
  }

  @Test
  public void testGetSites() throws Exception {
    Site site = getFixture(Site.class);
    doReturn(Collections.singletonList(site)).when(siteManager).getSites();

    Response response = when().get(getBaseUrl() + "/v1/sites");

    response.then().assertThat().statusCode(200);

    // Check the Result
    JSONArray body = new JSONArray(response.body().asString());

    assertEquals(1, body.length());

    JSONObject jsonObj = body.getJSONObject(0);
    assertEquals(jsonObj.getInt("siteId"), site.getSiteId().intValue());
    assertEquals(jsonObj.getString("name"), site.getName());
    assertEquals(jsonObj.getString("abbreviation"), site.getAbbreviation());
    assertEquals(jsonObj.getString("shortName"), site.getShortName());
    assertEquals(jsonObj.getInt("siteOfficeId"), site.getSiteOfficeId().intValue());

    verify(siteManager).getSites();
  }

}
