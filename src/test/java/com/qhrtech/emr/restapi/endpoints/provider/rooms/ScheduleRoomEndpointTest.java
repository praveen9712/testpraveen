
package com.qhrtech.emr.restapi.endpoints.provider.rooms;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.scheduling.rooms.ScheduleRoomManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.scheduling.rooms.ScheduleRoom;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.rooms.ScheduleRoomDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ScheduleRoomEndpointTest extends AbstractEndpointTest<ScheduleRoomEndpoint> {

  private final ScheduleRoomManager scheduleRoomManager;
  private final String commonUrl = "/v1/provider-portal/scheduler";
  private AuditLogUser user;

  public ScheduleRoomEndpointTest() {
    super(new ScheduleRoomEndpoint(), ScheduleRoomEndpoint.class);
    scheduleRoomManager = mock(ScheduleRoomManager.class);
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
    services.put(ScheduleRoomManager.class, scheduleRoomManager);
    return services;
  }

  @Test
  public void testGetById() throws Exception {
    ScheduleRoom room = getFixture(ScheduleRoom.class);
    ScheduleRoomDto expected = mapDto(room, ScheduleRoomDto.class);

    doReturn(room).when(scheduleRoomManager).getById(room.getId(), user.getUserId());

    ScheduleRoomDto actual = given()
        .when()
        .pathParam("id", room.getId())
        .get(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ScheduleRoomDto.class);

    assertEquals(expected, actual);
    verify(scheduleRoomManager).getById(room.getId(), user.getUserId());
  }

  @Test
  public void testGetByIdInsufficientPermissionsException()
      throws DataAccessException, ForbiddenException, SupportingResourceNotFoundException {

    int roomId = TestUtilities.nextInt();
    doThrow(new InsufficientPermissionsException("")).when(scheduleRoomManager)
        .getById(roomId, user.getUserId());

    given()
        .when()
        .pathParam("id", roomId)
        .get(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.FORBIDDEN.getStatusCode());

    verify(scheduleRoomManager).getById(roomId, user.getUserId());
  }

  @Test
  public void testGetByInvalidId() throws Exception {
    int invalidId = TestUtilities.nextId();

    given()
        .when()
        .pathParam("id", invalidId)
        .get(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(scheduleRoomManager).getById(invalidId, user.getUserId());
  }

  @Test
  public void testGetByAppointmentId() throws Exception {
    List<ScheduleRoom> rooms = getFixtures(ScheduleRoom.class, ArrayList::new, 5);
    List<ScheduleRoomDto> expected = mapDto(rooms, ScheduleRoomDto.class, ArrayList::new);
    int appointmentId = TestUtilities.nextId();

    doReturn(rooms).when(scheduleRoomManager).getByAppointmentId(appointmentId, user.getUserId());

    List<ScheduleRoomDto> actual = toCollection(
        given()
            .when()
            .pathParam("id", appointmentId)
            .get(getBaseUrl() + commonUrl + "/appointments/{id}/schedule-rooms")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(ScheduleRoomDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(scheduleRoomManager).getByAppointmentId(appointmentId, user.getUserId());
  }

  @Test
  public void testCreate() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    ScheduleRoom room = mapDto(roomDto, ScheduleRoom.class);
    room.setTmAvatar("");
    room.setTmRow(0);
    room.setIconColor(-16777216);
    int expected = room.getId();

    doReturn(expected).when(scheduleRoomManager).create(room, user);

    int actual = given()
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + commonUrl + "/schedule-rooms")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract()
        .as(Integer.class);

    assertEquals(expected, actual);
    verify(scheduleRoomManager).create(room, user);
  }

  @Test
  public void testCreateInsufficientPermissionsException() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    ScheduleRoom room = mapDto(roomDto, ScheduleRoom.class);
    room.setTmAvatar("");
    room.setTmRow(0);
    room.setIconColor(-16777216);
    doThrow(new InsufficientPermissionsException("")).when(scheduleRoomManager).create(room, user);

    given()
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + commonUrl + "/schedule-rooms")
        .then()
        .assertThat()
        .statusCode(Status.FORBIDDEN.getStatusCode());

    verify(scheduleRoomManager).create(room, user);
  }

  @Test
  public void testCreateWithNullValue() throws Exception {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + commonUrl + "/schedule-rooms")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(scheduleRoomManager, never()).create(any(), any());
  }

  @Test
  public void testUpdate() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    ScheduleRoom room = mapDto(roomDto, ScheduleRoom.class);

    doReturn(room).when(scheduleRoomManager).getById(room.getId(), user.getUserId());

    given()
        .pathParams("id", room.getId())
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(scheduleRoomManager).getById(room.getId(), user.getUserId());
    verify(scheduleRoomManager).update(room, user);
  }

  @Test
  public void testUpdateInsufficientPermissionsException() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    ScheduleRoom room = mapDto(roomDto, ScheduleRoom.class);

    doReturn(room).when(scheduleRoomManager).getById(room.getId(), user.getUserId());
    doThrow(new InsufficientPermissionsException("")).when(scheduleRoomManager).update(room, user);

    given()
        .pathParams("id", room.getId())
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.FORBIDDEN.getStatusCode());

    verify(scheduleRoomManager).getById(room.getId(), user.getUserId());
    verify(scheduleRoomManager).update(room, user);
  }

  @Test
  public void testUpdateWithNullValue() throws Exception {
    given()
        .pathParams("id", TestUtilities.nextId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(scheduleRoomManager, never()).getById(anyInt(), anyInt());
    verify(scheduleRoomManager, never()).update(any(), any());
  }

  @Test
  public void testUpdateWithInvalidId() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    ScheduleRoom room = mapDto(roomDto, ScheduleRoom.class);

    given()
        .pathParams("id", room.getId())
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(scheduleRoomManager).getById(room.getId(), user.getUserId());
    verify(scheduleRoomManager, never()).update(room, user);
  }

  @Test
  public void testUpdateWithDifferentId() throws Exception {
    ScheduleRoomDto roomDto = getFixture(ScheduleRoomDto.class);
    int id = roomDto.getId() + 1;

    given()
        .pathParams("id", id)
        .body(roomDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(scheduleRoomManager, never()).update(any(), any());
  }

  @Test
  public void testDelete() throws Exception {
    int id = TestUtilities.nextId();

    given()
        .pathParams("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(scheduleRoomManager).delete(id, user);
  }

  @Test
  public void testDeleteInsufficientPermissionsException() throws Exception {
    int id = TestUtilities.nextId();

    doThrow(new InsufficientPermissionsException("")).when(scheduleRoomManager).delete(id, user);
    given()
        .pathParams("id", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + commonUrl + "/schedule-rooms/{id}")
        .then()
        .assertThat()
        .statusCode(Status.FORBIDDEN.getStatusCode());

    verify(scheduleRoomManager).delete(id, user);
  }
}
