
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class SelectionListEndpointTest extends AbstractEndpointTest<SelectionListEndpoint> {

  private final SelectionListManager selectionListManager;
  ApiSecurityContext context;
  private AuditLogUser user;
  private String baseUri = "/v1/provider-portal/selection-lists";

  public SelectionListEndpointTest() {
    super(new SelectionListEndpoint(), SelectionListEndpoint.class);
    selectionListManager = mock(SelectionListManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
    context.setPatientId(RandomUtils.nextInt());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(SelectionListManager.class, selectionListManager);
    return servicesMap;
  }

  @Test
  public void testGetSelectionLists() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName listName = TestUtilities.nextValue(SelectionListName.class);

    when(selectionListManager.getSelectionList(listName.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .pathParam("list-name", listName.getResourceName())
            .get(getBaseUrl() + baseUri + "/{list-name}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(listName.getListName());
  }

  @Test
  public void testGetChartUnlockReason() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName listName = SelectionListName.CHART_UNLOCK_REASON;

    when(selectionListManager.getSelectionList(listName.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/chart-unlock-reasons")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(listName.getListName());
  }

  @Test
  public void testGetSelectionListsWithInvalidListName() throws ProtossException {
    String listName = TestUtilities.nextString(10);

    given()
        .pathParam("list-name", listName)
        .get(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(404);

    verify(selectionListManager, never()).getSelectionList(anyString());
  }

  @Test
  public void testGetReferralStatuses() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName status = SelectionListName.REFERRAL_STATUS;

    when(selectionListManager.getSelectionList(status.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/referral-statuses")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(status.getListName());
  }

  @Test
  public void testGetReferralTypes() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName type = SelectionListName.REFERRAL_TYPE;

    when(selectionListManager.getSelectionList(type.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/referral-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(type.getListName());
  }

  @Test
  public void testUpdateReferralType() throws ProtossException {
    List<String> referralTypes =
        getFixtures(String.class, ArrayList::new, 5);

    given()
        .contentType(ContentType.JSON)
        .body(referralTypes)
        .put(getBaseUrl() + baseUri + "/referral-types")
        .then()
        .assertThat().statusCode(204);

    verify(selectionListManager)
        .setSelectionList(SelectionListName.REFERRAL_TYPE.getListName(), referralTypes);
  }

  @Test
  public void testUpdateReferralStatuses() throws ProtossException {
    List<String> orderStatus =
        getFixtures(String.class, ArrayList::new, 5);

    given()
        .contentType(ContentType.JSON)
        .body(orderStatus)
        .put(getBaseUrl() + baseUri + "/referral-statuses")
        .then()
        .assertThat().statusCode(204);

    verify(selectionListManager)
        .setSelectionList(SelectionListName.REFERRAL_STATUS.getListName(), orderStatus);
  }

  @Test
  public void testUpdateSelectionList() throws ProtossException {
    List<String> selectionList =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName listName = TestUtilities.nextValue(SelectionListName.class);

    given()
        .pathParam("list-name", listName.getResourceName())
        .contentType(ContentType.JSON)
        .body(selectionList)
        .put(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(204);

    verify(selectionListManager)
        .setSelectionList(listName.getListName(), selectionList);
  }

  @Test
  public void testUpdateReferralTypeWithInvalidName() throws ProtossException {
    List<String> referralTypes =
        getFixtures(String.class, ArrayList::new, 5);

    String listName = TestUtilities.nextString(10);

    given()
        .contentType(ContentType.JSON)
        .pathParam("list-name", listName)
        .body(referralTypes)
        .put(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(404);

    verify(selectionListManager, never()).setSelectionList(anyString(), anyListOf(String.class));
  }

  @Test
  public void testUpdateSelectionListForNull() throws ProtossException {
    given()
        .pathParam("list-name", TestUtilities.nextString(10))
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(400);

    verify(selectionListManager, never()).setSelectionList(anyString(), anyListOf(String.class));
  }

  @Test
  public void testUpdateSelectionListDuplicate() throws ProtossException {
    List<String> selectionList =
        getFixtures(String.class, ArrayList::new, 5);
    String duplicateSelection = TestUtilities.nextString(10);
    selectionList.add(duplicateSelection);
    selectionList.add(duplicateSelection);

    String listName = TestUtilities.nextString(10);

    given()
        .pathParam("list-name", listName)
        .contentType(ContentType.JSON)
        .body(selectionList)
        .put(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(400);

    verify(selectionListManager, never()).setSelectionList(anyString(), anyListOf(String.class));
  }

  @Test
  public void testGetMaskRemovalReasons() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName maskRemovalReasons = SelectionListName.MASK_REMOVAL_REASON;

    when(selectionListManager.getSelectionList(maskRemovalReasons.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/mask-removal-reasons")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(maskRemovalReasons.getListName());
  }

  @Test
  public void testGetAddressTypes() throws ProtossException {
    List<String> expected =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName type = SelectionListName.ADDRESS_TYPE;

    when(selectionListManager.getSelectionList(type.getListName()))
        .thenReturn(expected);
    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/address-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);

    verify(selectionListManager).getSelectionList(type.getListName());
  }

}
