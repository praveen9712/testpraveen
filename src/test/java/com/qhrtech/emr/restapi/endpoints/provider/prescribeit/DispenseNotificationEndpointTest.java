
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DispenseNotificationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.prescribeIt.DispenseNotification;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class DispenseNotificationEndpointTest
    extends AbstractEndpointTest<DispenseNotificationEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  DispenseNotificationManager manager;
  String baseUrl = getBaseUrl() + "/v1/provider-portal/dispense-notifications";


  public DispenseNotificationEndpointTest() {
    super(new DispenseNotificationEndpoint(), DispenseNotificationEndpoint.class);

    manager = mock(DispenseNotificationManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(DispenseNotificationManager.class, manager);
    return map;
  }

  @Test
  public void getDispenseNotificationById() throws ProtossException {
    DispenseNotificationDto expected = getFixture(DispenseNotificationDto.class);
    expected.setCancelled(false);
    DispenseNotification protossResult = mapDto(expected, DispenseNotification.class);
    int id = expected.getId();
    when(manager.getDispenseNotification(id)).thenReturn(protossResult);

    // test
    DispenseNotificationDto actual =
        given().pathParams("id", id)
            .when()
            .get(baseUrl + "/{id}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(DispenseNotificationDto.class);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotification(id);
  }

  @Test
  public void getDispenseNotificationByIdCancelled() throws ProtossException {
    DispenseNotificationDto expected = getFixture(DispenseNotificationDto.class);
    expected.setCancelled(true);
    DispenseNotification protossResult = mapDto(expected, DispenseNotification.class);
    int id = expected.getId();
    when(manager.getDispenseNotification(id)).thenReturn(protossResult);

    // test
    given().pathParams("id", id)
        .when()
        .get(baseUrl + "/{id}")
        .then()
        .assertThat().statusCode(404);

    verify(manager).getDispenseNotification(id);
  }

  @Test
  public void create() throws ProtossException {
    DispenseNotification notification = getFixture(DispenseNotification.class);
    notification.setQuantityRemainingValue(new BigDecimal("1.95"));
    int expected = notification.getId();
    DispenseNotificationDto notificationDto = mapDto(notification, DispenseNotificationDto.class);
    DispenseNotification notification1 = mapDto(notificationDto, DispenseNotification.class);
    when(manager.createDispenseNotification(notification1)).thenReturn(expected);

    // test
    int actual =
        given()
            .body(notificationDto)
            .when()
            .contentType(ContentType.JSON)
            .post(baseUrl)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .as(Integer.class);

    assertEquals(actual, expected);
    verify(manager).createDispenseNotification(notification1);
  }

  @Test
  public void getDispenseNotificationsWithAllParams() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = TestUtilities.nextId();
    UUID prescriptionUuid = UUID.randomUUID();
    UUID patientUuid = UUID.randomUUID();
    String identifierSystem = TestUtilities.nextString(255);
    String identifierValue = TestUtilities.nextString(255);

    when(
        manager.getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null, startingId,
            pageSize, false, identifierValue, identifierSystem)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("identifierValue", identifierValue)
            .queryParam("identifierSystem", identifierSystem)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null,
        startingId,
        pageSize, false, identifierValue, identifierSystem);
  }


  @Test
  public void getDispenseNotificationsWithNullRxUuid() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = TestUtilities.nextId();
    UUID patientUuid = UUID.randomUUID();
    String identifierSystem = TestUtilities.nextString(255);
    String identifierValue = TestUtilities.nextString(255);

    when(
        manager.getDispenseNotifications(null, patientUuid, null, startingId,
            pageSize, false, identifierValue, identifierSystem)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("identifierValue", identifierValue)
            .queryParam("identifierSystem", identifierSystem)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(null, patientUuid, null,
        startingId,
        pageSize, false, identifierValue, identifierSystem);
  }

  @Test
  public void getDispenseNotificationsWithInvalidUuid() {
    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = TestUtilities.nextId();
    String prescriptionUuid = TestUtilities.nextString(10);
    UUID patientUuid = UUID.randomUUID();

    // test
    given()
        .when()
        .queryParam("prescriptionUuid", prescriptionUuid)
        .queryParam("patientUuid", patientUuid)
        .queryParam("pageSize", pageSize)
        .queryParam("startingId", startingId)
        .get(baseUrl)
        .then()
        .assertThat().statusCode(400);

    verifyNoInteractions(manager);
  }

  @Test
  public void getDispenseNotificationsNoStartingId() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    UUID prescriptionUuid = UUID.randomUUID();
    UUID patientUuid = UUID.randomUUID();
    String identifierSystem = TestUtilities.nextString(255);
    String identifierValue = TestUtilities.nextString(255);

    when(
        manager.getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null, null,
            pageSize, false, identifierValue, identifierSystem)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("identifierValue", identifierValue)
            .queryParam("identifierSystem", identifierSystem)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null,
        null,
        pageSize, false, identifierValue, identifierSystem);
  }

  @Test
  public void getDispenseNotificationsNoRxUuid() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    UUID patientUuid = UUID.randomUUID();
    String identifierSystem = TestUtilities.nextString(255);
    String identifierValue = TestUtilities.nextString(255);

    when(
        manager.getDispenseNotifications(null, patientUuid, null, null,
            pageSize, false, identifierValue, identifierSystem)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("identifierValue", identifierValue)
            .queryParam("identifierSystem", identifierSystem)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(null, patientUuid, null,
        null,
        pageSize, false, identifierValue, identifierSystem);
  }

  @Test
  public void getDispenseNotificationsWithAllParamsPageSizeNegative() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(-50, -1);
    Integer startingId = TestUtilities.nextId();
    UUID prescriptionUuid = UUID.randomUUID();
    UUID patientUuid = UUID.randomUUID();

    when(
        manager.getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null, startingId,
            DEFAULT_PAGE_SIZE, false, null, null)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null,
        startingId,
        DEFAULT_PAGE_SIZE, false, null, null);
  }

  @Test
  public void getDispenseNotificationsWithAllParamsPageSizeMax() throws ProtossException {
    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(50, 100);
    Integer startingId = TestUtilities.nextId();
    UUID prescriptionUuid = UUID.randomUUID();
    UUID patientUuid = UUID.randomUUID();

    when(
        manager.getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null, startingId,
            MAX_PAGE_SIZE, false, null, null)).thenReturn(protossResult);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(manager).getDispenseNotifications(prescriptionUuid.toString(), patientUuid, null,
        startingId,
        MAX_PAGE_SIZE, false, null, null);
  }


  @Test
  public void testDelete()
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException,
      BusinessLogicException, TimeZoneNotFoundException {
    DispenseNotificationDto expected = getFixture(DispenseNotificationDto.class);
    expected.setCancelled(false);
    DispenseNotification protossResult = mapDto(expected, DispenseNotification.class);
    int id = expected.getId();
    when(manager.getDispenseNotification(id)).thenReturn(protossResult);

    given()
        .pathParams("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(204);

    verify(manager).cancelDispenseNotification(id);

  }

  @Test
  public void testUpdate()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException, NoDataFoundException,
      TimeZoneNotFoundException, InsufficientRolesException, InsufficientPermissionsException {
    DispenseNotification notification = getFixture(DispenseNotification.class);
    notification.setCancelled(false);
    notification.setQuantityRemainingValue(new BigDecimal("1.95"));
    int id = notification.getId();
    DispenseNotification protossResult = mapDto(notification, DispenseNotification.class);
    when(manager.getDispenseNotification(id)).thenReturn(protossResult);
    DispenseNotificationDto notificationDto = mapDto(protossResult, DispenseNotificationDto.class);

    // test
    given()
        .pathParam("id", id)
        .body(notificationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(204);
    verify(manager).updateDispenseNotification(
        mapDto(notificationDto, DispenseNotification.class));
  }

  @Test
  public void testUpdateWithId() {
    DispenseNotification notification = getFixture(DispenseNotification.class);
    int id = notification.getId();
    DispenseNotificationDto notificationDto = mapDto(notification, DispenseNotificationDto.class);

    // test
    given()
        .pathParam("id", TestUtilities.nextInt())
        .body(notificationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());


  }

  @Test
  public void testUpdateWithEmptyBody()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, NoDataFoundException,
      InsufficientRolesException, TimeZoneNotFoundException, InsufficientPermissionsException {
    int id = TestUtilities.nextInt();

    given()
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .put(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).updateDispenseNotification(any());
  }

  private Envelope<DispenseNotification> getEnvelopeForProtoss() {
    Envelope<DispenseNotification> envelope = new Envelope<>();
    List<DispenseNotification> notifications =
        getFixtures(DispenseNotification.class, ArrayList::new, 5);
    List<DispenseNotification> finalList = new ArrayList<>();
    for (DispenseNotification notification : notifications) {
      notification.setQuantityRemainingValue(new BigDecimal("1.21"));
      finalList.add(notification);
    }

    envelope.setContents(finalList);
    envelope.setCount(finalList.size());
    envelope.setLastId(TestUtilities.nextLong());
    envelope.setTotal(TestUtilities.nextId());
    return envelope;
  }


  private EnvelopeDto<DispenseNotificationDto> transformForApi(
      Envelope<DispenseNotification> protossEnvelop) {
    EnvelopeDto<DispenseNotificationDto> envelope = new EnvelopeDto<>();
    envelope.setContents(
        mapDto(protossEnvelop.getContents(), DispenseNotificationDto.class, ArrayList::new));
    envelope.setCount(protossEnvelop.getCount());
    envelope.setLastId(protossEnvelop.getLastId());
    envelope.setTotal(protossEnvelop.getTotal());
    return envelope;
  }

  private EnvelopeDto<DispenseNotificationDto> refType(EnvelopeDto rawEnvelope) {
    List<DispenseNotificationDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<DispenseNotificationDto>>() {});
    EnvelopeDto<DispenseNotificationDto> envelope =
        (EnvelopeDto<DispenseNotificationDto>) rawEnvelope;
    envelope.setContents(contents);
    return envelope;
  }


}
