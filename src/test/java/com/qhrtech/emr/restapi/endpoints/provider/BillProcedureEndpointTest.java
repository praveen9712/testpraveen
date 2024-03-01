
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.billing.BillProcedureManager;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class BillProcedureEndpointTest extends AbstractEndpointTest<BillProcedureEndpoint> {
  private final BillProcedureManager billProcedureManager;

  public BillProcedureEndpointTest() {

    super(new BillProcedureEndpoint(), BillProcedureEndpoint.class);
    billProcedureManager = mock(BillProcedureManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(BillProcedureManager.class, billProcedureManager);
    return servicesMap;
  }

  @Test
  public void testGetBillProcedure() throws Exception {
    // setup random data
    int appointmentId = TestUtilities.nextId();
    List<String> protossResults = getFixtures(String.class, ArrayList::new, 4);
    // mock dependencies
    when(billProcedureManager.getProcedureCodesByAppointmentId(appointmentId))
        .thenReturn(protossResults);
    Set<String> expected = new HashSet(protossResults);

    // test
    Set<String> actual = toCollection(
        given()
            .pathParam("appointmentId", appointmentId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/appointments/{appointmentId}/billprocedures/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(String[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(billProcedureManager).getProcedureCodesByAppointmentId(appointmentId);
  }
}
