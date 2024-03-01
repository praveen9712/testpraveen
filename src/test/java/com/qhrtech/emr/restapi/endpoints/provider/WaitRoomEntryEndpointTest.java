
package com.qhrtech.emr.restapi.endpoints.provider;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ModuleAccessException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.scheduling.rooms.WaitRoomEntry;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.WaitRoomEntryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class WaitRoomEntryEndpointTest extends AbstractEndpointTest<WaitRoomEntryEndpoint> {

  ApiSecurityContext context;
  private AuditLogUser user;
  private WaitRoomEntryManager waitRoomEntryManager;

  public WaitRoomEntryEndpointTest() {
    super(new WaitRoomEntryEndpoint(), WaitRoomEntryEndpoint.class);

    waitRoomEntryManager = mock(WaitRoomEntryManager.class);

    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(WaitRoomEntryManager.class, waitRoomEntryManager);
  }

  @Test
  public void testGetWaitRoomEntryById()
      throws ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {
    WaitRoomEntry protossResult = getFixture(WaitRoomEntry.class);
    protossResult.setCashCollected(false);
    int waitRoomId = protossResult.getId();
    when(waitRoomEntryManager.getById(waitRoomId)).thenReturn(protossResult);
    WaitRoomEntryDto expected = mapDto(protossResult, WaitRoomEntryDto.class);
    // test
    WaitRoomEntryDto actual = given()
        .pathParam("id", waitRoomId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{id}")
        .then()
        .assertThat()
        .statusCode(200).extract().as(WaitRoomEntryDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(waitRoomEntryManager).getById(waitRoomId);
  }

  @Test
  public void testSearch()
      throws TimeZoneNotFoundException, ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {
    Boolean completed = TestUtilities.nextBoolean();
    Integer providerId = TestUtilities.nextInt();
    Integer patientId = TestUtilities.nextInt();
    Integer officeId = TestUtilities.nextInt();
    Integer roomId = TestUtilities.nextInt();
    Integer appointmentId = TestUtilities.nextInt();

    Calendar startDateC = nextUtcCalendar();
    LocalDateTime startLocal = LocalDateTime.fromCalendarFields(startDateC);
    LocalDateTime endLocal = LocalDateTime.fromCalendarFields(startDateC).plusYears(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();

    List<WaitRoomEntry> protossResult = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);

    when(waitRoomEntryManager.getWaitRoomEntries(
        completed, providerId, patientId, officeId, roomId, appointmentId, startLocal, endLocal))
            .thenReturn(protossResult);

    List<WaitRoomEntryDto> expected =
        mapDto(protossResult, WaitRoomEntryDto.class, ArrayList::new);

    // test
    List<WaitRoomEntryDto> actual = toCollection(given()
        .queryParam("isCompleted", completed)
        .queryParam("providerId", providerId)
        .queryParam("patientId", patientId)
        .queryParam("officeId", officeId)
        .queryParam("roomId", roomId)
        .queryParam("appointmentId", appointmentId)
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/wait-room-entries")
        .then().assertThat().statusCode(200)
        .extract().as(WaitRoomEntryDto[].class), ArrayList::new);

    // verify
    assertEquals(expected, actual);
    verify(waitRoomEntryManager).getWaitRoomEntries(
        completed, providerId, patientId, officeId, roomId, appointmentId, startLocal, endLocal);
  }

  @Test
  public void testSearchStartNull()
      throws TimeZoneNotFoundException, ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {
    Boolean completed = TestUtilities.nextBoolean();
    Integer providerId = TestUtilities.nextInt();
    Integer patientId = TestUtilities.nextInt();
    Integer officeId = TestUtilities.nextInt();
    Integer roomId = TestUtilities.nextInt();
    Integer appointmentId = TestUtilities.nextInt();

    Calendar startDateC = nextUtcCalendar();
    LocalDateTime endLocal = LocalDateTime.fromCalendarFields(startDateC).plusYears(10);
    String endDate = endLocal.toString();

    List<WaitRoomEntry> protossResult = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);

    when(waitRoomEntryManager.getWaitRoomEntries(
        completed, providerId, patientId, officeId, roomId, appointmentId, endLocal, null))
            .thenReturn(protossResult);

    List<WaitRoomEntryDto> expected =
        mapDto(protossResult, WaitRoomEntryDto.class, ArrayList::new);

    // test
    List<WaitRoomEntryDto> actual = toCollection(given()
        .queryParam("isCompleted", completed)
        .queryParam("providerId", providerId)
        .queryParam("patientId", patientId)
        .queryParam("officeId", officeId)
        .queryParam("roomId", roomId)
        .queryParam("appointmentId", appointmentId)
        .queryParam("endDate", endDate)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/wait-room-entries/")
        .then().assertThat().statusCode(200)
        .extract().as(WaitRoomEntryDto[].class), ArrayList::new);

    // verify
    assertEquals(expected, actual);
    verify(waitRoomEntryManager).getWaitRoomEntries(
        completed, providerId, patientId, officeId, roomId, appointmentId, endLocal, null);
  }

  @Test
  public void testGetWaitRoomEntryByIdNotFound()
      throws ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {
    int waitRoomId = RandomUtils.nextInt();
    when(waitRoomEntryManager.getById(waitRoomId)).thenReturn(null);

    // test
    given()
        .pathParam("id", waitRoomId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{id}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(waitRoomEntryManager).getById(waitRoomId);
  }

  @Test
  public void testCreateWaitRoomEntry()
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {
    WaitRoomEntry entry = getFixture(WaitRoomEntry.class);
    int expectedId = entry.getId();
    WaitRoomEntryDto entryDto = mapDto(entry, WaitRoomEntryDto.class);
    when(waitRoomEntryManager.create(entry, user)).thenReturn(expectedId);

    // test
    int actualId = given()
        .body(entryDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/wait-room-entries")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(Integer.class);

    // assertions
    assertEquals(expectedId, actualId);
    verify(waitRoomEntryManager).create(entry, user);
  }

  @Test
  public void testCreateWaitRoomEntryExisting()
      throws ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException, SupportingResourceNotFoundException,
      TimeZoneNotFoundException, NoDataFoundException, InsufficientPermissionsException {
    WaitRoomEntry entry = getFixture(WaitRoomEntry.class);
    entry.setCompletedDate(null);
    WaitRoomEntryDto entryDto = mapDto(entry, WaitRoomEntryDto.class);
    when(waitRoomEntryManager.getForAppointment(entry.getAppointmentId())).thenReturn(
        Collections.singletonList(entry));

    // test
    given()
        .body(entryDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/wait-room-entries")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(waitRoomEntryManager, never()).create(entry, user);
    verify(waitRoomEntryManager).getForAppointment(entry.getAppointmentId());
  }

  @Test
  public void testCreateWaitRoomEntryWithNull()
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {
    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/wait-room-entries")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(waitRoomEntryManager, never()).create(null, user);

  }

  @Test
  public void testUpdateWaitRoomEntryWithNull()
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {
    // test
    given()
        .pathParam("waitroomId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{waitroomId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(waitRoomEntryManager, times(0)).create(null, user);

  }

  @Test
  public void testUpdateWaitRoomEntryArrivalNull()
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {

    WaitRoomEntry entry = getFixture(WaitRoomEntry.class);
    entry.setArrivedTime(null);
    WaitRoomEntryDto entryDto = mapDto(entry, WaitRoomEntryDto.class);

    // test
    given()
        .pathParam("waitroomId", entry.getId())
        .body(entryDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{waitroomId}")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(waitRoomEntryManager, never()).update(entry, user);

  }

  @Test
  public void testUpdateWaitRoomEntry()
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {

    WaitRoomEntry entry = getFixture(WaitRoomEntry.class);
    WaitRoomEntryDto entryDto = mapDto(entry, WaitRoomEntryDto.class);

    doNothing().when(waitRoomEntryManager).update(entry, user);
    // test
    given()
        .pathParam("waitroomId", entry.getId())
        .body(entryDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{waitroomId}")
        .then()
        .assertThat()
        .statusCode(200);

    // assertions
    verify(waitRoomEntryManager).update(entry, user);

  }

  @Test
  public void testUpdateWaitRoomEntryIdsMismatch() {

    WaitRoomEntryDto entryDto = getFixture(WaitRoomEntryDto.class);
    // test
    given()
        .pathParam("waitroomId", TestUtilities.nextInt())
        .body(entryDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{waitroomId}")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verifyZeroInteractions(waitRoomEntryManager);

  }

  @Test
  public void testDeleteWaitRoom()
      throws TimeZoneNotFoundException, ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException, NoDataFoundException {
    int waitRoomId = RandomUtils.nextInt();
    Boolean patientLeft = RandomUtils.nextBoolean();

    // test
    given()
        .pathParam("waitRoomId", waitRoomId)
        .pathParam("patientLeft", patientLeft)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/wait-room-entries/{waitRoomId}/{patientLeft}")
        .then()
        .assertThat()
        .statusCode(200);

    verify(waitRoomEntryManager).delete(waitRoomId, patientLeft, user);
  }

}
