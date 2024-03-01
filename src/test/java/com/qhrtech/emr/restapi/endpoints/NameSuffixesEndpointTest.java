
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class NameSuffixesEndpointTest extends AbstractEndpointTest<NameSuffixesEndpoint> {

  private SelectionListManager selectionListManager;

  public NameSuffixesEndpointTest() {
    super(new NameSuffixesEndpoint(), NameSuffixesEndpoint.class);
    selectionListManager = mock(SelectionListManager.class);
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
  public void getNameSuffixesTest() throws ProtossException {

    List<String> customSuffixes = new ArrayList<>();

    String suffix = TestUtilities.nextString(10);
    customSuffixes.add(suffix);

    List<String> defaultSuffixes = new ArrayList<>();
    defaultSuffixes.add(0, "Sr.");
    defaultSuffixes.add(1, "Jr.");
    defaultSuffixes.add(2, "III");
    defaultSuffixes.add(3, "IV");
    defaultSuffixes.add(4, "V");

    when(selectionListManager.getSelectionList(SelectionListName.NAME_SUFFIXES.getListName()))
        .thenReturn(customSuffixes);

    ArrayList<String> actual = toCollection(given()
        .when()
        .get(getBaseUrl() + "/v1/enumerations/name-suffixes")
        .then()
        .assertThat().statusCode(200)
        .extract()
        .as(String[].class),
        ArrayList::new);

    verify(selectionListManager).getSelectionList(SelectionListName.NAME_SUFFIXES.getListName());

    Assert.assertTrue(actual.containsAll(defaultSuffixes));
    Assert.assertTrue(actual.contains(suffix));

  }


}

