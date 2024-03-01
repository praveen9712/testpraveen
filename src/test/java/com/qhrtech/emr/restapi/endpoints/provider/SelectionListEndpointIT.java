
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultSelectionListManager;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.db.RandomUtils;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class SelectionListEndpointIT
    extends AbstractEndpointIntegrationTest<SelectionListEndpoint> {

  public static final String REFERRAL = "Referral";
  SelectionListManager selectionListManager;
  private String baseUri = "/v1/provider-portal/selection-lists";

  public SelectionListEndpointIT() throws IOException {
    super(new SelectionListEndpoint(), SelectionListEndpoint.class);
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
    return Collections.singletonMap(SelectionListManager.class,
        selectionListManager);
  }

  @Test
  public void testGetChartUnlockReasons() throws DatabaseInteractionException {

    SelectionListName listName = SelectionListName.CHART_UNLOCK_REASON;
    List<String> expected = createSelectionList(listName.getListName());

    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/chart-unlock-reasons")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSelectionList() throws DatabaseInteractionException {

    SelectionListName listName = TestUtilities.nextValue(SelectionListName.class);
    List<String> expected = createSelectionList(listName.getListName());

    // In case of Referral type protoss adds this value if not present.
    if (listName.equals(SelectionListName.REFERRAL_TYPE)) {
      expected.add(0, REFERRAL);
    }

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
  }

  @Test
  public void testGetSelectionListReferalStatus() throws DatabaseInteractionException {

    SelectionListName listName = SelectionListName.REFERRAL_STATUS;
    List<String> expected = createSelectionList(listName.getListName());

    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/referral-statuses")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testUpdateSelectionListReferralStatus() throws DatabaseInteractionException {

    List<String> orderStatus =
        getFixtures(String.class, ArrayList::new, 5);
    given()
        .contentType(ContentType.JSON)
        .body(orderStatus)
        .put(getBaseUrl() + baseUri + "/referral-statuses")
        .then()
        .assertThat().statusCode(204);

    List<String> actual =
        selectionListManager.getSelectionList(SelectionListName.REFERRAL_STATUS.getListName());

    assertEquals(orderStatus, actual);
  }

  @Test
  public void testUpdateSelectionListReferralType() throws DatabaseInteractionException {

    List<String> referralTypes =
        getFixtures(String.class, ArrayList::new, 5);
    // Protoss add this referral type if not present.
    referralTypes.add(0, REFERRAL);
    given()
        .contentType(ContentType.JSON)
        .body(referralTypes)
        .put(getBaseUrl() + baseUri + "/referral-types")
        .then()
        .assertThat().statusCode(204);

    List<String> actual =
        selectionListManager.getSelectionList(SelectionListName.REFERRAL_TYPE.getListName());

    assertEquals(referralTypes, actual);
  }

  @Test
  public void testUpdateSelectionList() throws ProtossException {
    List<String> selectionList =
        getFixtures(String.class, ArrayList::new, 5);

    SelectionListName listName = TestUtilities.nextValue(SelectionListName.class);
    // In case of Referral type protoss adds this value if not present.
    if (listName.equals(SelectionListName.REFERRAL_TYPE)) {
      selectionList.add(0, REFERRAL);
    }

    given()
        .pathParam("list-name", listName.getResourceName())
        .contentType(ContentType.JSON)
        .body(selectionList)
        .put(getBaseUrl() + baseUri + "/{list-name}")
        .then()
        .assertThat().statusCode(204);

    List<String> actual =
        selectionListManager.getSelectionList(listName.getListName());
    assertEquals(selectionList, actual);
  }


  @Test
  public void testGetSelectionListReferalType() throws DatabaseInteractionException {

    SelectionListName listName = SelectionListName.REFERRAL_TYPE;
    List<String> expected = createSelectionList(listName.getListName());
    // Protoss add this referral type if not present.
    expected.add(0, REFERRAL);

    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/referral-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSelectionListMaskRemovalReason() throws DatabaseInteractionException {

    SelectionListName listName = SelectionListName.MASK_REMOVAL_REASON;
    List<String> expected = createSelectionList(listName.getListName());

    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/mask-removal-reasons")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSelectionListAddressTypes() throws DatabaseInteractionException {

    SelectionListName listName = SelectionListName.ADDRESS_TYPE;
    List<String> expected = createSelectionList(listName.getListName());

    List<String> actual = toCollection(
        given()
            .get(getBaseUrl() + baseUri + "/address-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(String[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }


  private List<String> createSelectionList(String listName)
      throws DatabaseInteractionException {
    List<String> selectionList = new ArrayList<>();
    selectionList.add(RandomUtils.getString(20));
    selectionList.add(RandomUtils.getString(30));
    selectionList.add(RandomUtils.getString(50));

    selectionListManager.setSelectionList(listName, selectionList);
    return selectionList;
  }



}
