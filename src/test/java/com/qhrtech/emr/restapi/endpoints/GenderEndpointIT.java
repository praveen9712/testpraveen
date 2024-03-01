
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.demographics.DefaultGenderManager;
import com.qhrtech.emr.accuro.api.demographics.GenderManager;
import com.qhrtech.emr.accuro.db.demographics.GenderFixture;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.GenderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class GenderEndpointIT extends AbstractEndpointIntegrationTest<GenderEndpoint> {

  private GenderManager genderManagerWithDataSource;

  public GenderEndpointIT() throws IOException {
    super(new GenderEndpoint(), GenderEndpoint.class);
    genderManagerWithDataSource = new DefaultGenderManager(ds);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(GenderManager.class, genderManagerWithDataSource);
  }

  @Test
  public void getGenders() {
    Set<GenderDto> expected = toCollection(when().get(getBaseUrl() + "/v1/genders")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(GenderDto[].class), HashSet::new);

    try (Connection conn = getConnection()) {
      GenderFixture fixture = new GenderFixture();
      fixture.setUp(conn);
      expected.add(mapDto(fixture.get(), GenderDto.class));
    } catch (Exception e) {
      fail("Fail to create new gender");
    }

    Set<GenderDto> actual = toCollection(when().get(getBaseUrl() + "/v1/genders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(GenderDto[].class), HashSet::new);

    assertEquals(expected, actual);
  }

}

