
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.demographics.GenderManager;
import com.qhrtech.emr.accuro.model.demographics.Gender;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class GenderEndpointTest extends AbstractEndpointTest<GenderEndpoint> {

  private GenderManager genderManager;

  public GenderEndpointTest() {
    super(new GenderEndpoint(), GenderEndpoint.class);
    genderManager = mock(GenderManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(GenderManager.class, genderManager);
  }

  @Test
  public void getOneGender() throws ProtossException {

    Gender expected = getFixture(Gender.class);

    when(genderManager.getGenders()).thenReturn(Collections.singleton(expected));

    when().get(getBaseUrl() + "/v1/genders")
        .then()
        .assertThat().statusCode(200)
        .and().body("builtIn", hasItems(expected.isBuiltIn()))
        .and().body("id", hasItems(expected.getId()))
        .and().body("name", hasItems(expected.getName()))
        .and().body("shortName", hasItems(expected.getShortName()));

    verify(genderManager).getGenders();
  }

  @Test
  public void getMultipleGenders() throws ProtossException {
    Gender first = getFixture(Gender.class);
    Gender second = getFixture(Gender.class);

    Set<Gender> genders = new HashSet<>();
    genders.add(first);
    genders.add(second);

    when(genderManager.getGenders()).thenReturn(genders);

    when().get(getBaseUrl() + "/v1/genders")
        .then()
        .assertThat().statusCode(200)
        .and().body("builtIn", hasItems(first.isBuiltIn(), second.isBuiltIn()))
        .and().body("id", hasItems(first.getId(), second.getId()))
        .and().body("name", hasItems(first.getName(), second.getName()))
        .and().body("shortName", hasItems(first.getShortName(), second.getShortName()));

    verify(genderManager).getGenders();
  }

  @Test
  public void getGendersEmptyResponse() throws ProtossException {

    when(genderManager.getGenders()).thenReturn(Collections.emptySet());

    when().get(getBaseUrl() + "/v1/genders")
        .then()
        .assertThat().statusCode(200)
        .and().body("", hasSize(0));
  }

}

