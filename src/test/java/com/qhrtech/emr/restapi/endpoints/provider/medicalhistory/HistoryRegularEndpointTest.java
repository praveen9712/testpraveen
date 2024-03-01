
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryRegularItemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryTypeManager;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryRegularItem;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryType;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryRegularItemDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;

public class HistoryRegularEndpointTest
    extends AbstractEndpointTest<HistoryRegularEndpoint> {

  private final HistoryTypeManager historyTypeManager;
  private final HistoryRegularItemManager historyItemManager;

  public HistoryRegularEndpointTest() {
    super(new HistoryRegularEndpoint(), HistoryRegularEndpoint.class);
    historyItemManager = mock(HistoryRegularItemManager.class);
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
    Map<Class, Object> services = new HashMap<>();
    services.put(HistoryTypeManager.class, historyTypeManager);
    services.put(HistoryRegularItemManager.class, historyItemManager);
    return services;
  }

  @Test
  public void getAll() throws Exception {

    List<HistoryRegularItem> protossResults =
        getFixtures(HistoryRegularItem.class, ArrayList::new, 25);
    List<HistoryType> protossTypes = getFixtures(HistoryType.class, ArrayList::new, 5);

    // protoss will return lists, so the order must be accounted for in the tests.
    protossTypes.sort(Comparator.comparing(HistoryType::getId));

    // spread the 25 items across 5 types
    // the current api implementation grabs all types, then all items for each type, so
    // we need to carefully structure the order of the expected results to match the implementation
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        HistoryType type = protossTypes.get(i);
        protossResults.get((i * 5) + j).setTypeId(type.getId());
      }
    }

    // map those items to types
    Map<Integer, List<HistoryRegularItem>> itemsBytype = protossResults.stream().collect(
        Collectors.groupingBy(HistoryRegularItem::getTypeId));

    // mock dependencies
    when(historyTypeManager.getAllHistoryTypes()).thenReturn(protossTypes);
    for (HistoryType type : protossTypes) {
      int typeId = type.getId();
      List<HistoryRegularItem> items = itemsBytype.get(typeId);
      when(historyItemManager.getForType(typeId)).thenReturn(items);
    }

    // map to the expected shape
    List<HistoryRegularItemDto> expected =
        mapDto(protossResults, HistoryRegularItemDto.class, ArrayList::new);

    List<HistoryRegularItemDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/history-regular")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(HistoryRegularItemDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(historyTypeManager).getAllHistoryTypes();
    for (HistoryType type : protossTypes) {
      verify(historyItemManager).getForType(type.getId());
    }
  }

  @Test
  public void getForHistoryType() throws Exception {
    // setup random data
    List<HistoryRegularItem> protossResult =
        getFixtures(HistoryRegularItem.class, ArrayList::new, 5);

    int typeId = protossResult.get(0).getTypeId();

    // mock dependencies
    when(historyItemManager.getForType(typeId)).thenReturn(protossResult);

    List<HistoryRegularItemDto> expected =
        mapDto(protossResult, HistoryRegularItemDto.class, ArrayList::new);

    List<HistoryRegularItemDto> actual = toCollection(
        given().queryParam("historyTypeId", typeId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/history-regular")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(HistoryRegularItemDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(historyItemManager).getForType(typeId);
  }

  @Test
  public void getByItemId() throws Exception {
    // setup random data
    HistoryRegularItem protossResult = getFixture(HistoryRegularItem.class);
    int historyRegularId = protossResult.getId();

    // mock dependencies
    when(historyItemManager.getById(historyRegularId)).thenReturn(protossResult);

    HistoryRegularItemDto expected = mapDto(protossResult, HistoryRegularItemDto.class);
    HistoryRegularItemDto actual = given()
        .pathParam("historyRegularId", historyRegularId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/history-regular/{historyRegularId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(HistoryRegularItemDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(historyItemManager).getById(historyRegularId);
    verifyZeroInteractions(historyTypeManager);
  }

  @Test
  public void getByItemIdNotFound() throws Exception {
    // setup random data
    int itemId = TestUtilities.nextId();

    // mock dependencies
    when(historyItemManager.getById(itemId)).thenReturn(null);

    given()
        .pathParam("historyRegularId", itemId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/history-regular/{historyRegularId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(historyItemManager).getById(itemId);
    verifyZeroInteractions(historyTypeManager);
  }

}
