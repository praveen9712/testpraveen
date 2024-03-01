
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.demographics.PersonTitleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PersonTitlesEndpointTest extends AbstractEndpointTest<PersonTitlesEndpoint> {

  private PersonTitleManager personTitleManager;

  public PersonTitlesEndpointTest() {
    super(new PersonTitlesEndpoint(), PersonTitlesEndpoint.class);
    personTitleManager = mock(PersonTitleManager.class);
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
  public void getPersonTitlesTestWithoutParam() throws ProtossException {

    List<String> personTitles = new ArrayList<>();

    String title = TestUtilities.nextString(10);
    personTitles.add(title);

    when(personTitleManager.search(null)).thenReturn(personTitles);

    toCollection(given()
        .when()
        .get(getBaseUrl() + "/v1/enumerations/person-titles")
        .then()
        .assertThat().statusCode(200)
        .extract()
        .as(String[].class),
        ArrayList::new);

    verify(personTitleManager).search(null);
  }

}

