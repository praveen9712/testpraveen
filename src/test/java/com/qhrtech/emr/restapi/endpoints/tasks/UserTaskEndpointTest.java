
package com.qhrtech.emr.restapi.endpoints.tasks;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.api.tasks.TaskManager;
import com.qhrtech.emr.accuro.api.tasks.TaskReasonManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.tasks.Task;
import com.qhrtech.emr.accuro.model.tasks.TaskReason;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.tasks.TaskReasonDto;
import com.qhrtech.emr.restapi.models.dto.tasks.UserTaskDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class UserTaskEndpointTest extends AbstractEndpointTest<UserTaskEndpoint> {

  private final TaskManager userTaskManager;
  private final TaskReasonManager taskReasonManager;
  private final UserAuthenticationManager userAuthManager;
  ApiSecurityContext context;
  private AuditLogUser user;

  public UserTaskEndpointTest() {
    super(new UserTaskEndpoint(), UserTaskEndpoint.class);
    userTaskManager = mock(TaskManager.class);
    taskReasonManager = mock(TaskReasonManager.class);
    userAuthManager = mock(UserAuthenticationManager.class);
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
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(TaskManager.class, userTaskManager);
    servicesMap.put(TaskReasonManager.class, taskReasonManager);
    servicesMap.put(UserAuthenticationManager.class, userAuthManager);
    return servicesMap;
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Test
  public void testGetById() throws Exception {
    // setup random data
    Task expected = getFixture(Task.class);
    int taskId = RandomUtils.nextInt();
    expected.setId(taskId);
    // mock dependencies
    when(userTaskManager.getById(taskId, user)).thenReturn(expected);
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    UserTaskDto expectedDto = mapDto(expected, UserTaskDto.class);

    // test
    UserTaskDto actual =
        given()
            .pathParam("taskId", taskId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(UserTaskDto.class);
    // assertions
    assertEquals(expectedDto, actual);
    verify(userTaskManager).getById(taskId, user);
  }

  @Test
  public void testGetByIdNoUser() throws Exception {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    // setup random data
    Task expected = getFixture(Task.class);
    int taskId = RandomUtils.nextInt();
    expected.setId(taskId);
    // mock dependencies
    when(userTaskManager.getById(taskId, context.getUser())).thenReturn(expected);
    UserTaskDto expectedDto = mapDto(expected, UserTaskDto.class);

    // test
    UserTaskDto actual =
        given()
            .pathParam("taskId", taskId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(UserTaskDto.class);
    // assertions
    assertEquals(expectedDto, actual);
    verify(userTaskManager).getById(taskId, context.getUser());
  }

  @Test
  public void testGetByIdTaskNotFound() throws Exception {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    // setup random data
    Task expected = getFixture(Task.class);
    int taskId = RandomUtils.nextInt();
    expected.setId(taskId);
    // mock dependencies
    when(userTaskManager.getById(taskId, context.getUser())).thenReturn(null);

    // test

    given()
        .pathParam("taskId", taskId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(404);
    // assertions
    verify(userTaskManager).getById(taskId, context.getUser());
  }

  @Test
  public void testGetTasks() throws Exception {
    // setup random data
    List<Task> protossTasks = getFixtures(Task.class, ArrayList::new, 5);
    Envelope<Task> protossEnvelope = new Envelope<>();
    long lastId = protossTasks.stream().max(Comparator.comparing(Task::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossTasks);

    String date = "2000-01-01";
    EnvelopeDto<UserTaskDto> taskDtoEnvelopeDto = new EnvelopeDto<>();
    List<UserTaskDto> expected =
        mapDto(protossTasks, UserTaskDto.class, ArrayList::new);
    taskDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    taskDtoEnvelopeDto.setTotal(expected.size());
    taskDtoEnvelopeDto.setCount(expected.size());
    taskDtoEnvelopeDto.setLastId(lastId);

    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    String createdStartDate = "2018-01-01";
    String createdEndDate = "2018-01-30";
    Integer startingId = null;
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;
    Integer patientId = TestUtilities.nextInt();

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        pageSize))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), startingId,
        pageSize);
  }

  @Test
  public void testGetTasksWithMaxPageSize() throws Exception {
    // setup random data
    List<Task> protossTasks = getFixtures(Task.class, ArrayList::new, 5);
    Envelope<Task> protossEnvelope = new Envelope<>();
    long lastId = protossTasks.stream().max(Comparator.comparing(Task::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossTasks);

    String date = "2000-01-01";
    EnvelopeDto<UserTaskDto> taskDtoEnvelopeDto = new EnvelopeDto<>();
    List<UserTaskDto> expected =
        mapDto(protossTasks, UserTaskDto.class, ArrayList::new);
    taskDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    taskDtoEnvelopeDto.setTotal(expected.size());
    taskDtoEnvelopeDto.setCount(expected.size());
    taskDtoEnvelopeDto.setLastId(lastId);

    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    String createdStartDate = "2018-01-01";
    String createdEndDate = "2018-01-30";
    Integer startingId = null;
    // random number between 1-49
    int pageSize = 60;
    Integer patientId = TestUtilities.nextInt();

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        50))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), startingId,
        50);
  }

  @Test
  public void testGetTasksWithNullPageSize() throws Exception {
    // setup random data
    List<Task> protossTasks = getFixtures(Task.class, ArrayList::new, 5);
    Envelope<Task> protossEnvelope = new Envelope<>();
    long lastId = protossTasks.stream().max(Comparator.comparing(Task::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossTasks);

    String date = "2000-01-01";
    EnvelopeDto<UserTaskDto> taskDtoEnvelopeDto = new EnvelopeDto<>();
    List<UserTaskDto> expected =
        mapDto(protossTasks, UserTaskDto.class, ArrayList::new);
    taskDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    taskDtoEnvelopeDto.setTotal(expected.size());
    taskDtoEnvelopeDto.setCount(expected.size());
    taskDtoEnvelopeDto.setLastId(lastId);

    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    String createdStartDate = "2018-01-01";
    String createdEndDate = "2018-01-30";
    Integer startingId = null;
    // random number between 1-49
    Integer pageSize = null;
    Integer patientId = TestUtilities.nextInt();

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        25))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), startingId,
        25);
  }

  @Test
  public void testGetTasksWithStartAndEndDate() throws Exception {
    // setup random data
    List<Task> protossTasks = getFixtures(Task.class, ArrayList::new, 5);
    Envelope<Task> protossEnvelope = new Envelope<>();
    long lastId = protossTasks.stream().max(Comparator.comparing(Task::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossTasks);

    String date = "2000-01-01";
    EnvelopeDto<UserTaskDto> taskDtoEnvelopeDto = new EnvelopeDto<>();
    List<UserTaskDto> expected =
        mapDto(protossTasks, UserTaskDto.class, ArrayList::new);
    taskDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    taskDtoEnvelopeDto.setTotal(expected.size());
    taskDtoEnvelopeDto.setCount(expected.size());
    taskDtoEnvelopeDto.setLastId(lastId);

    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    String createdStartDate = "2018-01-30";
    String createdEndDate = "2018-01-01";
    Integer startingId = null;
    // random number between 1-49
    Integer pageSize = null;
    Integer patientId = TestUtilities.nextInt();

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdEndDate), LocalDate.parse(createdStartDate), null,
        25))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(user, patientId, reason, true,
        dueDate, LocalDate.parse(createdEndDate), LocalDate.parse(createdStartDate), startingId,
        25);
  }


  @Test
  public void testGetTasksWithDiff() throws Exception {
    // setup random data
    Integer patientId = TestUtilities.nextInt();
    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    String createdStartDate = "2000-01-01";
    String createdEndDate = "2018-01-01";
    Integer startingId = null;
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(400)
            .extract().as(EnvelopeDto.class);

    // assertions
    verifyNoInteractions(userTaskManager);
  }

  @Test
  public void testGetTasksWithNullStartAndEndDate() throws Exception {
    Integer patientId = TestUtilities.nextInt();
    String reason = TestUtilities.nextString(10);
    Calendar taskDueDateC = nextUtcCalendar();
    LocalDate dueDate = LocalDate.fromCalendarFields(taskDueDateC);
    String taskDueDate = dueDate.toString();
    Integer startingId = null;
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("patientId", patientId)
            .queryParam("reason", reason)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(400)
            .extract().as(EnvelopeDto.class);
  }

  private EnvelopeDto<UserTaskDto> reType(EnvelopeDto rawEnvelope) {
    List<UserTaskDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<UserTaskDto>>() {});
    EnvelopeDto<UserTaskDto> e = (EnvelopeDto<UserTaskDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  // avoid validation exception
  private LocalDate getRandomLocalDate() {
    LocalDate localDate = TestUtilities.nextLocalDate();
    int year = TestUtilities.nextInt(9999 - 1753) + 1753;
    return new LocalDate(year, localDate.getMonthOfYear(), localDate.getDayOfMonth());
  }

  @Test
  public void testCreate() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    Task userTask = mapDto(userTaskDto, Task.class);
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.create(userTask, user))
        .thenReturn(expectedId);
    // test
    Integer actualId =
        given()
            .body(userTaskDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/tasks")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);
    // assertions
    assertEquals(actualId, expectedId);
    verify(userTaskManager).create(userTask, user);
  }

  @Test
  public void testCreateNoBody() throws DatabaseInteractionException {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/tasks")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verifyNoInteractions(userTaskManager);
  }

  @Test
  public void testUpdate() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = userTaskDto.getId();

    Task userTask = mapDto(userTaskDto, Task.class);
    when(userTaskManager.getById(taskId, user))
        .thenReturn(userTask);
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).update(mapDto(userTaskDto, Task.class), user);
  }

  @Test
  public void testUpdateTaskIdNotMatch() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = TestUtilities.nextInt();

    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verifyNoInteractions(userTaskManager);
  }

  @Test
  public void testUpdateNoBody() {

    given()
        .pathParam("taskId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verifyNoInteractions(userTaskManager);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = userTaskDto.getId();

    when(userTaskManager.getById(taskId, user))
        .thenReturn(null);
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager).getById(taskId, user);
    verify(userTaskManager, times(0)).update(mapDto(userTaskDto, Task.class), user);
  }

  @Test
  public void testPatch() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = userTaskDto.getId();

    Task userTask = mapDto(userTaskDto, Task.class);
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(userTaskManager.getById(taskId, user))
        .thenReturn(userTask);

    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).update(mapDto(userTaskDto, Task.class), user);
  }

  @Test
  public void testPatchNoBody() throws Exception {

    given()
        .pathParam("taskId", TestUtilities.nextInt())
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verifyNoInteractions(userTaskManager);
  }

  @Test
  public void testPatchNoUser() throws Exception {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));

    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = userTaskDto.getId();

    Task userTask = mapDto(userTaskDto, Task.class);
    when(userTaskManager.getById(taskId, context.getUser()))
        .thenReturn(userTask);

    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).update(mapDto(userTaskDto, Task.class), context.getUser());
  }

  @Test
  public void testPatchNoUserTaskExisting() throws Exception {
    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = userTaskDto.getId();

    when(userTaskManager.getById(taskId, context.getUser()))
        .thenReturn(null);

    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager).getById(taskId, context.getUser());
    verify(userTaskManager, times(0)).update(mapDto(userTaskDto, Task.class), context.getUser());
  }

  @Test
  public void testPatchNoUserTaskExistingIdNotMatchWithBody() throws Exception {
    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    userTaskDto.setTaskDueDate(getRandomLocalDate());
    int taskId = TestUtilities.nextInt();

    when(userTaskManager.getById(taskId, context.getUser()))
        .thenReturn(null);

    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto)
        .when()
        .contentType("application/merge-patch+json")
        .patch(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager).getById(taskId, context.getUser());
    verify(userTaskManager, times(0)).update(mapDto(userTaskDto, Task.class), context.getUser());
  }

  @Test
  public void testDeleteTask() throws Exception {
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", 1)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).delete(1, user);
  }

  @Test
  public void testAddUsers() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto.getAssignedToUserIds())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}/users")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).addTaskUsers(taskId, userTaskDto.getAssignedToUserIds(), user);
  }

  @Test
  public void testAddRoles() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    given()
        .pathParam("taskId", taskId)
        .body(userTaskDto.getAssignedToRoleIds())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/tasks/{taskId}/roles")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).addTaskRoles(taskId, userTaskDto.getAssignedToRoleIds(), user);
  }

  @Test
  public void testGetReasons() throws Exception {
    // setup random data
    List<TaskReason> protossReasons = getFixtures(TaskReason.class, ArrayList::new, 3);

    List<TaskReasonDto> expected =
        mapDto(protossReasons, TaskReasonDto.class, ArrayList::new);

    Integer officeId = protossReasons.get(0).getOfficeId();

    // mock dependencies
    when(userAuthManager.getOfficeIds(user.getUserId())).thenReturn(Collections.singleton(1));
    when(taskReasonManager.getAll(officeId)).thenReturn(protossReasons);

    // test
    List<TaskReasonDto> actual = toCollection(
        given()
            .queryParam("officeId", officeId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/tasks/reasons")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(TaskReasonDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(taskReasonManager).getAll(officeId);
  }


}
