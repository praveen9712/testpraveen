
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeMapDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public class FeatureAccessEndpointTest extends AbstractEndpointTest<FeatureAccessEndpoint> {

  private static final Integer DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;
  private ApiSecurityContext context = new ApiSecurityContext();
  private AuditLogUser user;
  private Set<String> scopes;
  private String grantType;
  private String tenant;
  private String oauthId;
  private AccuroApiContext accuroApiContext;
  private AccuroApiContextManager contextManager;


  public FeatureAccessEndpointTest() {
    super(new FeatureAccessEndpoint(), FeatureAccessEndpoint.class);

    contextManager = mock(AccuroApiContextManager.class);

    user = getFixture(AuditLogUser.class);
    scopes = getFixtures(String.class, HashSet::new, 5);
    grantType = TestUtilities.nextString(10);
    tenant = TestUtilities.nextString(5);
    oauthId = TestUtilities.nextString(100);
    accuroApiContext = getFixture(AccuroApiContext.class);

    context.setScopes(scopes);
    context.setGrantType(grantType);
    context.setUser(user);
    context.setTenantId(tenant);
    context.setOauthClientId(oauthId);
    context.setAccuroApiContext(accuroApiContext);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> serviceMap = new HashMap<>();
    serviceMap.put(AccuroApiContextManager.class, contextManager);
    return serviceMap;
  }

  @Test
  public void testGetFeaturesAccess() {

    Integer pageSize = 60;
    Map<Integer, Set<FeatureType>> featurePermissions =
        context.getAccuroApiContext().getUserPermissions().getFeaturePermissions();
    AccuroUser currentUser = context.getAccuroApiContext().getAccuroUser();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, pageSize, null);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", currentUser.getUserId())
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  @Test
  public void testGetFeaturesAccessNotLoggedUser()
      throws DatabaseInteractionException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException {

    Integer pageSize = 60;

    int randomUserId = RandomUtils.nextInt();

    AccuroApiContext newUserContext = getFixture(AccuroApiContext.class);

    when(contextManager.getAccuroApiContext(randomUserId, false)).thenReturn(newUserContext);
    Map<Integer, Set<FeatureType>> featurePermissions =
        newUserContext.getUserPermissions().getFeaturePermissions();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, pageSize, null);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", randomUserId)
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  @Test
  public void testGetFeaturesAccessNotAdmin() {

    Integer pageSize = 60;

    int randomUserId = RandomUtils.nextInt();
    context.getAccuroApiContext().getAccuroUser().setSystemAdmin(false);

    given()
        .pathParam("userId", randomUserId)
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.FORBIDDEN.getStatusCode());

    verifyZeroInteractions(contextManager);

  }


  @Test
  public void testGetFeaturesAccessPageSizeTwo() {

    Integer pageSize = 2;
    Map<Integer, Set<FeatureType>> featurePermissions =
        context.getAccuroApiContext().getUserPermissions().getFeaturePermissions();
    AccuroUser currentUser = context.getAccuroApiContext().getAccuroUser();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, pageSize, null);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", currentUser.getUserId())
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  @Test
  public void testGetFeaturesAccessDefaultPage() {

    Map<Integer, Set<FeatureType>> featurePermissions =
        context.getAccuroApiContext().getUserPermissions().getFeaturePermissions();
    AccuroUser currentUser = context.getAccuroApiContext().getAccuroUser();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, null, null);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", currentUser.getUserId())
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  @Test
  public void testGetFeaturesAccessNegativePage() {

    Integer pageSize = -10;
    Map<Integer, Set<FeatureType>> featurePermissions =
        context.getAccuroApiContext().getUserPermissions().getFeaturePermissions();
    AccuroUser currentUser = context.getAccuroApiContext().getAccuroUser();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, pageSize, null);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", currentUser.getUserId())
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  @Test
  public void testGetFeaturesAccessStartingIdNotNull() {

    Integer pageSize = 2;
    Map<Integer, Set<FeatureType>> featurePermissions =
        context.getAccuroApiContext().getUserPermissions().getFeaturePermissions();
    AccuroUser currentUser = context.getAccuroApiContext().getAccuroUser();

    Set<Integer> keySet = featurePermissions.keySet();
    Integer startingId = keySet.stream().findAny().get();

    EnvelopeMapDto expected =
        getPaginatedResult(featurePermissions, pageSize, startingId);

    EnvelopeMapDto rawActual = given()
        .pathParam("userId", currentUser.getUserId())
        .queryParam("pageSize", pageSize)
        .queryParam("startingId", startingId)
        .get(getBaseUrl() + "/v1/security/users/{userId}/features")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeMapDto.class);

    // assertions
    EnvelopeMapDto<Integer, Set<FeatureType>> actual = refType(rawActual);
    assertEquals(expected, actual);
    assertEquals(expected.getContents().size(), actual.getContents().size());
  }

  private EnvelopeMapDto refType(EnvelopeMapDto rawEnvelope) {


    Map<Integer, Set<FeatureType>> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<Map<Integer, Set<FeatureType>>>() {});

    EnvelopeMapDto<Integer, Set<FeatureType>> e =
        (EnvelopeMapDto<Integer, Set<FeatureType>>) rawEnvelope;

    e.setContents(contents);
    return e;
  }

  private EnvelopeMapDto getPaginatedResult(
      Map<Integer, Set<FeatureType>> featureAccess, Integer givenPageSize,
      Integer givenStartingId) {

    Integer pageSize =
        givenPageSize == null ? DEFAULT_PAGE_SIZE : givenPageSize;

    if (pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    } else if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    Integer startingId = givenStartingId == null ? null
        : givenStartingId;

    TreeMap<Integer, Set<FeatureType>> sorted = new TreeMap<>(featureAccess);
    SortedMap<Integer, Set<FeatureType>> result;
    Set<Integer> keySet = sorted.keySet();
    List<Integer> keyList = new ArrayList<>();
    keyList.addAll(keySet);
    Collections.sort(keyList);

    if (startingId != null) {
      int lastKey = keyList.indexOf(startingId) + pageSize;
      int endingKey = lastKey > keyList.size() - 1 ? keyList.size() - 1 : lastKey;
      result = sorted.subMap(startingId, false, keyList.get(endingKey), true);
    } else {
      int endingKey = pageSize > keyList.size() ? keyList.size() - 1 : pageSize - 1;
      result = sorted.subMap(keyList.get(0), true, keyList.get(endingKey), true);
    }

    EnvelopeMapDto envelopeDto = new EnvelopeMapDto();
    envelopeDto.setContents(result);
    envelopeDto.setCount(result.size());
    envelopeDto.setTotal(featureAccess.size());
    Integer lastOfficeId = result.isEmpty() ? 0 : result.lastKey();
    envelopeDto.setLastId(Long.valueOf(lastOfficeId));
    return envelopeDto;
  }

}
