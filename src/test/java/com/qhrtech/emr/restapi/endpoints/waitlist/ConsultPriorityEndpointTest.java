
package com.qhrtech.emr.restapi.endpoints.waitlist;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.waitlist.ConsultPriorityManager;
import com.qhrtech.emr.accuro.model.waitlist.ConsultPriority;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.waitlist.ConsultPriorityDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ConsultPriorityEndpointTest extends AbstractEndpointTest<ConsultPriorityEndpoint> {

  private final ConsultPriorityManager consultPriorityManager;

  public ConsultPriorityEndpointTest() {
    super(new ConsultPriorityEndpoint(), ConsultPriorityEndpoint.class);
    consultPriorityManager = mock(ConsultPriorityManager.class);
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
    servicesMap.put(ConsultPriorityManager.class, consultPriorityManager);
    return servicesMap;
  }

  @Test
  public void getAll() throws Exception {
    // set up random data
    List<ConsultPriority> protossResult = getFixtures(ConsultPriority.class, ArrayList::new, 5);

    // mock dependencies
    when(consultPriorityManager.getAll()).thenReturn(protossResult);

    List<ConsultPriorityDto> expected =
        mapDto(protossResult, ConsultPriorityDto.class, ArrayList::new);

    // test
    List<ConsultPriorityDto> actual = toCollection(
        RestAssured.when()
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-priorities")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(ConsultPriorityDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(consultPriorityManager).getAll();
  }

  @Test
  public void getById() throws Exception {
    // set up random data
    ConsultPriority protossResult = getFixture(ConsultPriority.class);
    int id = protossResult.getId();

    // mock dependencies
    when(consultPriorityManager.getById(id)).thenReturn(protossResult);

    ConsultPriorityDto expected = mapDto(protossResult, ConsultPriorityDto.class);

    // test
    ConsultPriorityDto actual = given()
        .pathParam("priorityId", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-priorities/{priorityId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(ConsultPriorityDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(consultPriorityManager).getById(id);
  }

  @Test
  public void getByIdNotFound() throws Exception {

    int id = TestUtilities.nextId();

    // mock dependencies
    when(consultPriorityManager.getById(id)).thenReturn(null);

    // test
    given()
        .pathParam("priorityId", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-priorities/{priorityId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(consultPriorityManager).getById(id);
  }


}
