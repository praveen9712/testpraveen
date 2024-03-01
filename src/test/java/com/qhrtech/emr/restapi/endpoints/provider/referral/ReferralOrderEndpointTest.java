
package com.qhrtech.emr.restapi.endpoints.provider.referral;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.referral.ReferralOrderManager;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.referral.ReferralOrder;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.referrals.ReferralOrderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.DateFormatter;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class ReferralOrderEndpointTest extends AbstractEndpointTest<ReferralOrderEndpoint> {

  private final ReferralOrderManager referralOrderManager;
  private final String commonUrl = "/v1/provider-portal/referral-orders";
  private final int defaultPageSize = 10;
  private final int maximumPageSize = 50;
  private AuditLogUser user;

  public ReferralOrderEndpointTest() {
    super(new ReferralOrderEndpoint(), ReferralOrderEndpoint.class);
    referralOrderManager = mock(ReferralOrderManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(getFixture(AuditLogUser.class));

    user = context.getUser();

    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> services = new HashMap<>();
    services.put(ReferralOrderManager.class, referralOrderManager);
    return services;
  }

  @Test
  public void testGetFromDate() throws Exception {
    int pageSize = TestUtilities.nextInt(maximumPageSize);
    pageSize = pageSize == 0 ? 1 : pageSize;
    int startingId = TestUtilities.nextInt();
    LocalDateTime localSince = TestUtilities.nextLocalDateTime();
    String fromDate = localSince.toString();
    localSince = DateFormatter.toLocalDatetimeOptionalTime(fromDate);

    LocalDateTime lastModified = TestUtilities.nextLocalDateTime();
    String lastModifiedDate = lastModified.toString();
    lastModified = DateFormatter.toLocalDatetimeOptionalTime(lastModifiedDate);

    boolean reconciled = TestUtilities.nextBoolean();
    List<ReferralOrder> orders = getFixtures(ReferralOrder.class, ArrayList::new, 5);

    Envelope<ReferralOrder> protossResult = getEnvelopeForProtoss(orders);
    EnvelopeDto<ReferralOrderDto> expected = transformForApi(protossResult);

    doReturn(protossResult).when(referralOrderManager)
        .getReferralOrders(localSince, lastModified, lastModified, startingId, pageSize,
            reconciled);

    EnvelopeDto actual = given()
        .when()
        .queryParam("fromDate", fromDate)
        .queryParam("lastModifiedStart", lastModifiedDate)
        .queryParam("lastModifiedEnd", lastModifiedDate)
        .queryParam("startingId", startingId)
        .queryParam("pageSize", pageSize)
        .queryParam("reconciled", reconciled)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeDto.class);

    assertEquals(expected.getCount(), actual.getCount());
    assertEquals(expected.getTotal(), actual.getTotal());
    assertEquals(expected.getLastId(), actual.getLastId());

    verify(referralOrderManager).getReferralOrders(localSince, lastModified, lastModified,
        startingId, pageSize,
        reconciled);
  }


  @Test
  public void testGetFromDateWithHalfData() throws Exception {

    LocalDateTime lastModified = TestUtilities.nextLocalDateTime();
    String lastModifiedDate = lastModified.toString();

    given()
        .when()
        .queryParam("lastModifiedStart", lastModifiedDate)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    given()
        .when()
        .queryParam("lastModifiedEnd", lastModifiedDate)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testGetFromDateWithDateRangeMoreThan30Days() throws Exception {


    given()
        .when()
        .queryParam("lastModifiedStart", "2022-05-01")
        .queryParam("lastModifiedEnd", "2022-10-01")
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    given()
        .when()
        .queryParam("lastModifiedStart", "2022-10-01")
        .queryParam("lastModifiedEnd", "2022-05-01")
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }



  @Test
  public void testGetFromDateWithInValidDate() throws Exception {

    given()
        .when()
        .queryParam("lastModifiedStart", "testData")
        .queryParam("lastModifiedEnd", "2022-10-01")
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void testGetDateFormat() {
    String fromDate = "20000-12-30T11:11:11.999";

    given()
        .when()
        .queryParam("fromDate", fromDate)
        .queryParam("lastModifiedDate", fromDate)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testGetFromDateForEmptyReturn() throws Exception {
    Envelope protossResult = new Envelope<>();
    protossResult.setContents(Collections.emptyList());
    protossResult.setCount(0);
    protossResult.setTotal(0);
    protossResult.setLastId(0L);
    int startingId = TestUtilities.nextInt();
    boolean reconciled = TestUtilities.nextBoolean();

    doReturn(protossResult).when(referralOrderManager)
        .getReferralOrders(null, null, null, startingId, maximumPageSize, reconciled);

    EnvelopeDto expected = new EnvelopeDto<>();
    expected.setContents(Collections.emptyList());
    expected.setCount(0);
    expected.setTotal(0);
    expected.setLastId(0L);

    int pageSize = maximumPageSize + TestUtilities.nextInt(maximumPageSize) + 1;

    EnvelopeDto actual = given()
        .when()
        .queryParam("startingId", startingId)
        .queryParam("pageSize", pageSize)
        .queryParam("reconciled", reconciled)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeDto.class);

    assertEquals(expected, actual);

    verify(referralOrderManager, never()).getReferralOrders(null, null, null, startingId, pageSize,
        reconciled);
    verify(referralOrderManager).getReferralOrders(null, null, null, startingId, maximumPageSize,
        reconciled);
  }

  @Test
  public void testGetFromDateForEmptyReturnWithoutLimitSize() throws Exception {
    Envelope protossResult = new Envelope<>();
    protossResult.setContents(Collections.emptyList());
    protossResult.setCount(0);
    protossResult.setTotal(0);
    protossResult.setLastId(0L);
    int startingId = TestUtilities.nextInt();
    boolean reconciled = TestUtilities.nextBoolean();
    doReturn(protossResult).when(referralOrderManager)
        .getReferralOrders(null, null, null, startingId, defaultPageSize, reconciled);

    EnvelopeDto expected = new EnvelopeDto<>();
    expected.setContents(Collections.emptyList());
    expected.setCount(0);
    expected.setTotal(0);
    expected.setLastId(0L);

    EnvelopeDto actual = given()
        .when()
        .queryParam("startingId", startingId)
        .queryParam("reconciled", reconciled)
        .get(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeDto.class);

    assertEquals(expected, actual);

    verify(referralOrderManager).getReferralOrders(null, null, null, startingId, defaultPageSize,
        reconciled);
    verify(referralOrderManager, never()).getReferralOrders(null, null, null, startingId,
        maximumPageSize,
        reconciled);
  }

  @Test
  public void testGetById() throws Exception {
    ReferralOrder order = getFixture(ReferralOrder.class);

    doReturn(order).when(referralOrderManager).getById(order.getId());

    ReferralOrderDto expected = mapDto(order, ReferralOrderDto.class);

    ReferralOrderDto actual =
        given()
            .pathParam("id", order.getId())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(ReferralOrderDto.class);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetByIdForNotFound() {
    int id = TestUtilities.nextId();

    given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testUpdate() throws Exception {
    ReferralOrderDto orderDto = generateReferralOrderForUpdate();
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    doNothing().when(referralOrderManager).update(order, user);
    doReturn(order).when(referralOrderManager).getById(orderDto.getId());

    given()
        .pathParam("id", orderDto.getId())
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(referralOrderManager).update(order, user);
    verify(referralOrderManager).getById(orderDto.getId());
  }

  @Test
  public void testUpdateIllegalArgumentException() throws Exception {
    ReferralOrderDto orderDto = generateReferralOrderForUpdate();
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    doThrow(new IllegalArgumentException("Test Exception")).when(referralOrderManager).update(order,
        user);
    doReturn(order).when(referralOrderManager).getById(orderDto.getId());

    given()
        .pathParam("id", orderDto.getId())
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager).update(order, user);
    verify(referralOrderManager).getById(orderDto.getId());
  }

  @Test
  public void testUpdateWithNullReferralOrder() throws Exception {
    ReferralOrderDto orderDto = generateReferralOrderForUpdate();
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    doNothing().when(referralOrderManager).update(order, user);
    doReturn(order).when(referralOrderManager).getById(orderDto.getId());

    given()
        .pathParam("id", orderDto.getId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager, never()).update(order, user);
    verify(referralOrderManager, never()).getById(orderDto.getId());
  }

  @Test
  public void testUpdateForUnmatchedOrderId() throws Exception {
    ReferralOrderDto orderDto = getFixture(ReferralOrderDto.class);
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    given()
        .pathParam("id", orderDto.getId() + 1)
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager, never()).update(order, user);
    verify(referralOrderManager, never()).getById(orderDto.getId());
  }

  @Test
  public void testUpdateForNotExistingOrder() throws Exception {
    ReferralOrderDto orderDto = getFixture(ReferralOrderDto.class);
    orderDto.setModifiedByUser(user.getUserId());
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    given()
        .pathParam("id", orderDto.getId())
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager, never()).update(order, user);
    verify(referralOrderManager).getById(orderDto.getId());
  }

  @Test
  public void testUpdateForInvalidFields() throws Exception {
    ReferralOrderDto orderDto = generateReferralOrderForUpdate();
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    ReferralOrder previous = SerializationUtils.clone(order);
    previous.setReconciled(!orderDto.isReconciled());

    doReturn(previous).when(referralOrderManager).getById(orderDto.getId());

    given()
        .pathParam("id", orderDto.getId())
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager, never()).update(mapDto(orderDto, ReferralOrder.class), user);
    verify(referralOrderManager).getById(orderDto.getId());
  }

  @Test
  public void testCreate() throws Exception {
    ReferralOrderDto orderDto = getFixture(ReferralOrderDto.class);
    orderDto.setModifiedByUser(user.getUserId());
    orderDto.setErefOutboundId(null);
    orderDto.setErefApptStatus(null);
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    int expected = orderDto.getId();

    doReturn(expected).when(referralOrderManager).create(order, user);

    int actual = given()
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/referral-orders")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract()
        .as(Integer.class);

    assertEquals(expected, actual);
    verify(referralOrderManager).create(order, user);
  }

  @Test
  public void testCreateIllegalArgumentException() throws Exception {
    ReferralOrderDto orderDto = getFixture(ReferralOrderDto.class);
    orderDto.setModifiedByUser(user.getUserId());
    orderDto.setErefOutboundId(null);
    orderDto.setErefApptStatus(null);
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    doThrow(new IllegalArgumentException("Test Exception")).when(referralOrderManager).create(order,
        user);

    given()
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/referral-orders")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager).create(order, user);
  }

  @Test
  public void testCreateWithNullReferralOrder() {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/referral-orders")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testCreateForUnsupportedFeature() throws Exception {
    ReferralOrderDto orderDto = getFixture(ReferralOrderDto.class);
    orderDto.setModifiedByUser(user.getUserId());
    ReferralOrder order = mapDto(orderDto, ReferralOrder.class);

    given()
        .body(orderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/referral-orders")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(referralOrderManager, never()).create(order, user);
  }

  @Test
  public void testDelete() throws Exception {
    int id = TestUtilities.nextId();

    doNothing().when(referralOrderManager).delete(id);

    given()
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/referral-orders/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(referralOrderManager).delete(id);
  }


  private ReferralOrderDto generateReferralOrderForUpdate() {
    ReferralOrderDto order = getFixture(ReferralOrderDto.class);
    order.setModifiedByUser(user.getUserId());
    order.setOrder(null);
    order.setCcRecipients(null);
    order.setChartItemId(null);
    order.setChartItemType(null);
    order.setDate(null);
    order.setContactType(null);
    order.setContactId(null);
    order.setErefOutboundId(null);
    order.setFaxLogId(null);
    order.setErefApptStatus(null);

    return order;
  }


  private Envelope<ReferralOrder> getEnvelopeForProtoss(
      List<ReferralOrder> referralOrders) {
    Envelope<ReferralOrder> envelope = new Envelope<>();
    envelope.setContents(referralOrders);
    envelope.setCount(referralOrders.size());
    envelope.setLastId((long) referralOrders.get(2).getId());
    envelope.setTotal(referralOrders.size());
    return envelope;
  }

  private EnvelopeDto<ReferralOrderDto> transformForApi(
      Envelope<ReferralOrder> protossEnvelop) {
    EnvelopeDto<ReferralOrderDto> envelope = new EnvelopeDto<>();
    envelope.setContents(
        mapDto(protossEnvelop.getContents(), ReferralOrderDto.class, ArrayList::new));
    envelope.setCount(protossEnvelop.getCount());
    envelope.setLastId(protossEnvelop.getLastId());
    envelope.setTotal(protossEnvelop.getTotal());
    return envelope;
  }
}
