
package com.qhrtech.emr.restapi.endpoints.waitlist;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.waitlist.ConsultStatusManager;
import com.qhrtech.emr.accuro.model.waitlist.ConsultStatus;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.waitlist.ConsultStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ConsultStatusEndpointTest extends AbstractEndpointTest<ConsultStatusEndpoint> {

  private final ConsultStatusManager consultStatusManager;

  public ConsultStatusEndpointTest() {
    super(new ConsultStatusEndpoint(), ConsultStatusEndpoint.class);
    consultStatusManager = mock(ConsultStatusManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(ConsultStatusManager.class, consultStatusManager);
  }

  @Test
  public void testGetAll() throws Exception {
    List<ConsultStatus> protossResult = getFixtures(ConsultStatus.class, ArrayList::new, 5);

    List<ConsultStatusDto> expectedResult =
        mapDto(protossResult, ConsultStatusDto.class, ArrayList::new);
    when(consultStatusManager.getAll()).thenReturn(protossResult);

    List<ConsultStatusDto> actualResult = toCollection(
        given()
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ConsultStatusDto[].class),
        ArrayList::new);

    // Assert
    assertEquals(expectedResult, actualResult);
    verify(consultStatusManager).getAll();

  }

  @Test
  public void testGetAllEmpty() throws Exception {
    when(consultStatusManager.getAll()).thenReturn(new ArrayList<>());
    List<ConsultStatusDto> actualDto = toCollection(
        given()
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ConsultStatusDto[].class),
        ArrayList::new);
    assertTrue(actualDto.isEmpty());
    verify(consultStatusManager).getAll();
  }

  @Test
  public void testGetById() throws Exception {
    ConsultStatus protossResult = getFixture(ConsultStatus.class);
    int statusId = protossResult.getId();
    ConsultStatusDto expected = mapDto(protossResult, ConsultStatusDto.class);
    when(consultStatusManager.getById(statusId)).thenReturn(protossResult);

    ConsultStatusDto actual =
        given()
            .pathParam("statusId", statusId)
            .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-statuses/{statusId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ConsultStatusDto.class);
    assertEquals(expected, actual);
    verify(consultStatusManager).getById(statusId);
  }

  @Test
  public void testGetByIdNotFound() throws Exception {

    int statusId = RandomUtils.nextInt();
    when(consultStatusManager.getById(statusId)).thenReturn(null);

    given()
        .pathParam("statusId", statusId)
        .get(getBaseUrl() + "/v1/provider-portal/waitlist-consult-statuses/{statusId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(consultStatusManager).getById(statusId);
  }
}
