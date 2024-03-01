
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryTypeManager;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryType;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryTypeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class HistoryTypeEndpointTest extends AbstractEndpointTest<HistoryTypeEndpoint> {

  private final HistoryTypeManager historyTypeManager;

  public HistoryTypeEndpointTest() {
    super(new HistoryTypeEndpoint(), HistoryTypeEndpoint.class);
    historyTypeManager = mock(HistoryTypeManager.class);
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
    servicesMap.put(HistoryTypeManager.class, historyTypeManager);
    return servicesMap;
  }

  @Test
  public void getAll() throws Exception {
    // set up random data
    List<HistoryType> protossResult = getFixtures(HistoryType.class, ArrayList::new, 5);

    // mock dependencies
    when(historyTypeManager.getAllHistoryTypes()).thenReturn(protossResult);

    List<HistoryTypeDto> expected = mapDto(protossResult, HistoryTypeDto.class, ArrayList::new);

    // test
    List<HistoryTypeDto> actual = toCollection(
        RestAssured.when()
            .get(getBaseUrl() + "/v1/provider-portal/history-types")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(HistoryTypeDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(historyTypeManager).getAllHistoryTypes();
  }

  @Test
  public void getById() throws Exception {
    // set up random data
    HistoryType protossResult = getFixture(HistoryType.class);
    int typeId = protossResult.getId();

    // mock dependencies
    when(historyTypeManager.getHistoryTypeByTypeId(typeId)).thenReturn(protossResult);

    HistoryTypeDto expected = mapDto(protossResult, HistoryTypeDto.class);

    // test
    HistoryTypeDto actual = given()
        .pathParam("typeId", typeId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/history-types/{typeId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(HistoryTypeDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(historyTypeManager).getHistoryTypeByTypeId(typeId);
  }

  @Test
  public void getByIdNotFound() throws Exception {
    // set up random data
    int typeId = TestUtilities.nextId();

    // mock dependencies
    when(historyTypeManager.getHistoryTypeByTypeId(typeId)).thenReturn(null);

    // test
    given()
        .pathParam("id", typeId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/history-types/{id}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(historyTypeManager).getHistoryTypeByTypeId(typeId);
  }


}
