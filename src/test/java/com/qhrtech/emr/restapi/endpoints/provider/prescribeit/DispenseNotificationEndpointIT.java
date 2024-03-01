
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultDispenseNotificationManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DispenseNotificationManager;
import com.qhrtech.emr.accuro.db.messaging.eprescription.DispenseNotificationFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.prescribeIt.DispenseNotification;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationAnnotationDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class DispenseNotificationEndpointIT extends
    AbstractEndpointIntegrationTest<DispenseNotificationEndpoint> {

  private DispenseNotificationManager manager;

  String baseUrl = getBaseUrl() + "/v1/provider-portal/dispense-notifications";

  public DispenseNotificationEndpointIT() throws IOException {

    super(new DispenseNotificationEndpoint(), DispenseNotificationEndpoint.class);
    manager = new DefaultDispenseNotificationManager(ds, null, defaultUser());

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(DispenseNotificationManager.class, manager);
  }

  @Test
  public void testGetById()
      throws Exception {

    DispenseNotification notification = setUpAndGetDispenseNotification();

    DispenseNotificationDto actual = given()
        .pathParam("id", notification.getId())
        .when()
        .get(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(DispenseNotificationDto.class);

    DispenseNotificationDto expected =
        mapDto(notification, DispenseNotificationDto.class);
    expected.setCreatedDate(actual.getCreatedDate());

    assertEquals(expected, actual);
  }

  @Test
  public void testCreate()
      throws Exception {

    DispenseNotification notification = setUpAndGetDispenseNotification();

    DispenseNotificationDto expected =
        mapDto(notification, DispenseNotificationDto.class);

    expected.setUuid(UUID.randomUUID());
    expected.setAuthorizingRequestUuid(null);
    int id = given()
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(baseUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    DispenseNotification dispenseNotification =
        manager.getDispenseNotification(id);

    DispenseNotificationDto actual = mapDto(dispenseNotification, DispenseNotificationDto.class);

    expected.setCreatedDate(actual.getCreatedDate());
    expected.setId(id);

    for (DispenseNotificationAnnotationDto annotationDto : expected.getNotes()) {
      annotationDto.setDispenseNotificationId(actual.getId());
    }

    assertEquals(expected, actual);
  }

  @Test
  public void testGetAllDispenseNotifications() {

    List<DispenseNotification> notifications = new ArrayList<>();

    DispenseNotificationFixture fixture = new DispenseNotificationFixture();
    try (Connection conn = getConnection()) {

      fixture.setUp(conn);
      notifications = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new dispense notification");
    }

    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = 1;
    String prescriptionUuid = notifications.get(0).getPrescribeITAuthorizingRequestUuid();
    Integer prescriptionId = fixture.getPrescriptionId();
    UUID patientUuid = notifications.get(0).getPatient();
    String identifierValue = notifications.get(0).getIdentifierValue();
    String identifierSystem = notifications.get(0).getIdentifierSystem();

    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss(notifications);

    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("prescriptionId", prescriptionId)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("identifierValue", identifierValue)
            .queryParam("identifierSystem", identifierSystem)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
  }

  @Test
  public void testGetAllDispenseNotificationsAllActive() {

    List<DispenseNotification> notifications = new ArrayList<>();

    DispenseNotificationFixture fixture = new DispenseNotificationFixture();
    try (Connection conn = getConnection()) {

      fixture.setUp(conn);
      notifications = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new dispense notification");
    }

    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = 1;
    String prescriptionUuid = notifications.get(0).getPrescribeITAuthorizingRequestUuid();
    Integer prescriptionId = fixture.getPrescriptionId();
    UUID patientUuid = notifications.get(0).getPatient();

    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss(notifications);

    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("prescriptionId", prescriptionId)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
  }

  @Test
  public void testGetAllDispenseNotificationsWithOnlyPrescriptionUuid()
      throws Exception {

    List<DispenseNotification> notifications = setUpAndGetDispenseNotificationList();

    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = 1;
    String prescriptionUuid = notifications.get(0).getPrescribeITAuthorizingRequestUuid();
    Integer prescriptionId = null;
    UUID patientUuid = null;

    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss(notifications);

    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("prescriptionId", prescriptionId)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
  }

  @Test
  public void testGetAllDispenseNotificationsWithOnlyPatientUuid()
      throws Exception {

    List<DispenseNotification> notifications = setUpAndGetDispenseNotificationList();

    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = 1;
    String prescriptionUuid = null;
    Integer prescriptionId = null;
    UUID patientUuid = notifications.get(0).getPatient();

    Envelope<DispenseNotification> protossResult = getEnvelopeForProtoss(notifications);

    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("prescriptionUuid", prescriptionUuid)
            .queryParam("patientUuid", patientUuid)
            .queryParam("prescriptionId", prescriptionId)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<DispenseNotificationDto> expected = transformForApi(protossResult);

    EnvelopeDto<DispenseNotificationDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
  }

  @Test
  public void testDelete() {
    DispenseNotification notification = setUpAndGetDispenseNotification();

    given()
        .pathParam("id", notification.getId())
        .when()
        .contentType(ContentType.JSON)
        .delete(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(204);

    try {
      manager.getDispenseNotification(notification.getId());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }

  }

  @Test
  public void testDeleteDispenseNotificationNotFound() {

    given()
        .pathParam("id", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .delete(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testUpdateJob()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    DispenseNotification notification = setUpAndGetDispenseNotification();

    DispenseNotificationDto expected =
        mapDto(notification, DispenseNotificationDto.class);

    expected.setUuid(UUID.randomUUID());
    expected.setAuthorizingRequestUuid(null);

    given()
        .pathParam("id", expected.getId())
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .put(baseUrl + "/{id}")
        .then()
        .assertThat()
        .statusCode(204);

    DispenseNotification dispenseNotification =
        manager.getDispenseNotification(expected.getId());

    DispenseNotificationDto actual = mapDto(dispenseNotification, DispenseNotificationDto.class);

    expected.setCreatedDate(actual.getCreatedDate());
    expected.setId(actual.getId());

    for (DispenseNotificationAnnotationDto annotationDto : expected.getNotes()) {
      annotationDto.setDispenseNotificationId(actual.getId());
    }

    assertEquals(expected, actual);

  }


  private DispenseNotification setUpAndGetDispenseNotification() {
    DispenseNotification notification = new DispenseNotification();

    try (Connection conn = getConnection()) {
      DispenseNotificationFixture fixture = new DispenseNotificationFixture();
      fixture.setUp(conn);
      notification = fixture.get().get(0);
    } catch (Exception e) {
      fail("Fail to create new dispense notification");
    }
    return notification;
  }

  private List<DispenseNotification> setUpAndGetDispenseNotificationList() {
    List<DispenseNotification> notifications = new ArrayList<>();

    try (Connection conn = getConnection()) {
      DispenseNotificationFixture fixture = new DispenseNotificationFixture();
      fixture.setUp(conn);
      notifications = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new dispense notification");
    }
    return notifications;
  }

  private Envelope<DispenseNotification> getEnvelopeForProtoss(
      List<DispenseNotification> notifications) {
    Envelope<DispenseNotification> envelope = new Envelope<>();
    envelope.setContents(notifications);
    envelope.setCount(notifications.size());
    envelope.setLastId((long) notifications.get(2).getId());
    envelope.setTotal(notifications.size());
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
