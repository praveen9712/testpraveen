
package com.qhrtech.emr.restapi.endpoints.waitlist;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.api.waitlist.WaitlistProviderManager;
import com.qhrtech.emr.accuro.api.waitlist.WaitlistRequestManager;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.waitlist.WaitlistRequest;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.waitlist.WaitlistRequestDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;
import org.mockito.Mockito;

public class WaitlistRequestEndpointTest extends AbstractEndpointTest<WaitlistRequestEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;
  private WaitlistRequestManager waitlistRequestManager;
  private WaitlistProviderManager providerManager;
  private SelectionListManager selectionListManager;
  private AuditLogUser user;

  public WaitlistRequestEndpointTest() {
    super(new WaitlistRequestEndpoint(), WaitlistRequestEndpoint.class);
    waitlistRequestManager = mock(WaitlistRequestManager.class);
    providerManager = mock(WaitlistProviderManager.class);
    selectionListManager = mock(SelectionListManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(WaitlistRequestManager.class, waitlistRequestManager);
    map.put(WaitlistProviderManager.class, providerManager);
    map.put(SelectionListManager.class, selectionListManager);
    return map;
  }

  @Test
  public void testGetWaitlistRequestsNoParam() throws Exception {

    Envelope<WaitlistRequest> protossEnvelope = getProtossEnvelope();

    Mockito.when(waitlistRequestManager.search(null, null, null, null, DEFAULT_PAGE_SIZE))
        .thenReturn(protossEnvelope);

    // test
    EnvelopeDto actual = given()
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assert
    EnvelopeDto<WaitlistRequestDto> actualEnvelope = refType(actual);
    assertEquals(getExpected(protossEnvelope), actualEnvelope);
    verify(waitlistRequestManager).search(null, null, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetWaitlistRequestsPageSizeNegative() throws Exception {

    Envelope<WaitlistRequest> protossEnvelope = getProtossEnvelope();

    Integer pageSize = Math.abs(TestUtilities.nextInt()) * -1;

    Mockito.when(waitlistRequestManager.search(null, null, null, null, DEFAULT_PAGE_SIZE))
        .thenReturn(protossEnvelope);

    // test
    EnvelopeDto actual = given()
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assert
    EnvelopeDto<WaitlistRequestDto> actualEnvelope = refType(actual);
    assertEquals(getExpected(protossEnvelope), actualEnvelope);
    verify(waitlistRequestManager).search(null, null, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetWaitlistRequestsPageSizeGreaterThanDefault() throws Exception {

    Envelope<WaitlistRequest> protossEnvelope = getProtossEnvelope();

    Integer pageSize = ThreadLocalRandom.current().nextInt(100, 1000 + 1);

    Mockito.when(waitlistRequestManager.search(null, null, null, null, MAX_PAGE_SIZE))
        .thenReturn(protossEnvelope);

    // test
    EnvelopeDto actual = given()
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assert
    EnvelopeDto<WaitlistRequestDto> actualEnvelope = refType(actual);
    assertEquals(getExpected(protossEnvelope), actualEnvelope);
    verify(waitlistRequestManager).search(null, null, null, null, MAX_PAGE_SIZE);
  }

  @Test
  public void testGetWaitlistRequestsAllParam() throws Exception {

    Integer providerId = Math.abs(TestUtilities.nextInt());
    Integer patientId = Math.abs(TestUtilities.nextInt());
    String consultStatus = TestUtilities.nextString(20);

    int startingId =
        getProtossEnvelope().getContents().stream()
            .min(Comparator.comparing(WaitlistRequest::getRequestId))
            .orElseThrow(RuntimeException::new).getRequestId();

    Envelope<WaitlistRequest> protossEnvelope = getProtossEnvelope();
    Mockito
        .when(waitlistRequestManager.search(providerId,
            patientId, consultStatus,
            startingId, DEFAULT_PAGE_SIZE))
        .thenReturn(protossEnvelope);

    // test
    EnvelopeDto actual = given()
        .queryParam("waitlistProviderId", providerId)
        .queryParam("patientId", patientId)
        .queryParam("consultStatus", consultStatus)
        .queryParam("startingId", startingId)
        .queryParam("pageSize", DEFAULT_PAGE_SIZE)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat().statusCode(200)
        .extract().as(EnvelopeDto.class);


    // assert
    EnvelopeDto<WaitlistRequestDto> actualEnvelope = refType(actual);

    assertEquals(getExpected(protossEnvelope), actualEnvelope);
    verify(waitlistRequestManager)
        .search(providerId, patientId, consultStatus,
            startingId, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetById() throws Exception {
    WaitlistRequest protossResult = getFixture(WaitlistRequest.class);
    WaitlistRequestDto expected = mapDto(protossResult, WaitlistRequestDto.class);
    when(waitlistRequestManager.getById(protossResult.getRequestId())).thenReturn(protossResult);

    WaitlistRequestDto actual = given()
        .pathParam("id", protossResult.getRequestId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/waitlists/{id}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(WaitlistRequestDto.class);

    assertEquals(expected, actual);

    verify(waitlistRequestManager).getById(protossResult.getRequestId());


  }

  private Envelope<WaitlistRequest> getProtossEnvelope() {
    List<WaitlistRequest> protossResults = getFixtures(WaitlistRequest.class, ArrayList::new, 10);
    Envelope<WaitlistRequest> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(WaitlistRequest::getRequestId))
        .orElseThrow(RuntimeException::new).getRequestId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setTotal(protossResults.size());
    return protossEnvelope;
  }

  private EnvelopeDto<WaitlistRequestDto> getExpected(Envelope<WaitlistRequest> protossEnvelope) {
    List<WaitlistRequestDto> requestDtoList =
        mapDto(protossEnvelope.getContents(), WaitlistRequestDto.class, ArrayList::new);

    EnvelopeDto<WaitlistRequestDto> expected = new EnvelopeDto<>();
    expected.setContents(requestDtoList);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  private EnvelopeDto<WaitlistRequestDto> refType(EnvelopeDto rawEnvelope) {
    List<WaitlistRequestDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<WaitlistRequestDto>>() {});
    EnvelopeDto<WaitlistRequestDto> e = (EnvelopeDto<WaitlistRequestDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }


  @Test
  public void testCreate() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);
    WaitlistRequest request = mapDto(requestDto, WaitlistRequest.class);
    int expected = requestDto.getRequestId();
    doReturn(expected).when(waitlistRequestManager).create(request, user);

    int actual = given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(200)
        .extract()
        .as(Integer.class);

    assertEquals(expected, actual);
    verify(waitlistRequestManager).create(request, user);
  }

  @Test
  public void testCreateWithEmptyBody() throws Exception {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testUpdate() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);
    WaitlistRequest request = mapDto(requestDto, WaitlistRequest.class);

    doReturn(request).when(waitlistRequestManager).getById(request.getRequestId());
    doNothing().when(waitlistRequestManager).update(request, user);

    given()
        .pathParam("waitlistId", requestDto.getRequestId())
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/waitlists/{waitlistId}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(waitlistRequestManager).getById(request.getRequestId());
    verify(waitlistRequestManager).update(request, user);
  }

  @Test
  public void testUpdateWithEmptyBody() throws Exception {
    given()
        .pathParam("waitlistId", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/waitlists/{waitlistId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).getById(anyInt());
    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testUpdateWithDifferentIds() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);

    given()
        .pathParam("waitlistId", TestUtilities.nextId())
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/waitlists/{waitlistId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).getById(anyInt());
    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testUpdateWithInvalidId() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);
    given()
        .pathParam("waitlistId", requestDto.getRequestId())
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/waitlists/{waitlistId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(waitlistRequestManager).getById(requestDto.getRequestId());
    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithEmptyPriority() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);

    requestDto.setConsultPriority("");

    given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithNullBookedDate() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);

    requestDto.setBookedDate(null);

    given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithEmptyConsultType() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);
    requestDto.setConsultType("");

    given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithEmptyConsultStatus() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);

    requestDto.setConsultStatus("");

    given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(400);

    verify(waitlistRequestManager, never()).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithNullComplaint() throws Exception {
    WaitlistRequestDto requestDto = getFixture(WaitlistRequestDto.class);

    requestDto.setComplaint(null);

    given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/waitlists")
        .then()
        .assertThat()
        .statusCode(200);

    verify(waitlistRequestManager).create(any(WaitlistRequest.class),
        any(AuditLogUser.class));
  }
}
