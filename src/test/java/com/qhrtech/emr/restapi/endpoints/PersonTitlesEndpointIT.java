
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.demographics.DefaultPersonTitleManager;
import com.qhrtech.emr.accuro.api.demographics.PersonTitleManager;
import com.qhrtech.emr.accuro.db.demographics.CustomTitleFixture;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class PersonTitlesEndpointIT extends
    AbstractEndpointIntegrationTest<PersonTitlesEndpoint> {

  private PersonTitleManager personTitleManager;

  public PersonTitlesEndpointIT() throws IOException {
    super(new PersonTitlesEndpoint(), PersonTitlesEndpoint.class);
    personTitleManager = new DefaultPersonTitleManager(ds);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(PersonTitleManager.class, personTitleManager);
  }

  @Test
  public void getPersonTitlesTest() throws Exception {
    String customTitle = null;
    try (Connection conn = getConnection()) {
      CustomTitleFixture fixture = new CustomTitleFixture();
      fixture.setUp(conn);
      customTitle = fixture.get();
    } catch (Exception e) {
      fail("Fail to create custom title");
    }

    ArrayList<String> actual = toCollection(given()
        .queryParam("title", customTitle)
        .when()
        .get(getBaseUrl() + "/v1/enumerations/person-titles")
        .then()
        .assertThat().statusCode(200)
        .extract()
        .as(String[].class),
        ArrayList::new);

    Assert.assertTrue(actual.contains(customTitle));

  }
}

