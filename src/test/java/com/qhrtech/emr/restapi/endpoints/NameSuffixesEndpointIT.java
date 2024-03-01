
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultSelectionListManager;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class NameSuffixesEndpointIT extends
    AbstractEndpointIntegrationTest<NameSuffixesEndpoint> {

  private SelectionListManager selectionListManager;

  public NameSuffixesEndpointIT() throws IOException {
    super(new NameSuffixesEndpoint(), NameSuffixesEndpoint.class);
    selectionListManager = new DefaultSelectionListManager(ds);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(SelectionListManager.class, selectionListManager);
  }

  @Test
  public void getNameSuffixesTest() {

    List<String> defaultSuffixes = new ArrayList<>();
    defaultSuffixes.add(0, "Sr.");
    defaultSuffixes.add(1, "Jr.");
    defaultSuffixes.add(2, "III");
    defaultSuffixes.add(3, "IV");
    defaultSuffixes.add(4, "V");

    ArrayList<String> actual = toCollection(given()
        .when()
        .get(getBaseUrl() + "/v1/enumerations/name-suffixes")
        .then()
        .assertThat().statusCode(200)
        .extract()
        .as(String[].class),
        ArrayList::new);

    Assert.assertTrue(actual.containsAll(defaultSuffixes));
  }
}
