
package com.qhrtech.emr.restapi.endpoints.waitlist;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class WaitlistConsultTypeEndpointTest
    extends AbstractEndpointTest<WaitlistConsultTypeEndpoint> {


  private SelectionListManager selectionListManager;


  public WaitlistConsultTypeEndpointTest() {
    super(new WaitlistConsultTypeEndpoint(), WaitlistConsultTypeEndpoint.class);
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
  public void testGetConsultType() throws Exception {
    List<String> expected = getFixtures(String.class, ArrayList::new, 5);
    when(selectionListManager.getSelectionList(SelectionListName.SWL_CONSULT_TYPE.getListName()))
        .thenReturn(expected);

    List<String> actual = toCollection(
        given().when()
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(selectionListManager).getSelectionList(SelectionListName.SWL_CONSULT_TYPE.getListName());

  }

  @Test
  public void testGetConsultTypeNoRecord() throws Exception {
    when(selectionListManager.getSelectionList(SelectionListName.SWL_CONSULT_TYPE.getListName()))
        .thenReturn(null);

    given().when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-types")
        .then()
        .assertThat().statusCode(204);

    verify(selectionListManager).getSelectionList(SelectionListName.SWL_CONSULT_TYPE.getListName());
  }
}
