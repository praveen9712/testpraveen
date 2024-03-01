
package com.qhrtech.emr.restapi.endpoints;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.tasks.TaskManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.AccuroUserSearchInfo;
import com.qhrtech.emr.accuro.model.tasks.Task;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
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
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class UserSecurityEndpointTest extends AbstractEndpointTest<UserSecurityEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private final AccuroUserManager userManger;
  private final OfficeManager officeManager;
  private final TaskManager userTaskManager;


  public UserSecurityEndpointTest() {
    super(new UserSecurityEndpoint(), UserSecurityEndpoint.class);
    userManger = mock(AccuroUserManager.class);
    officeManager = mock(OfficeManager.class);
    userTaskManager = mock(TaskManager.class);

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
    map.put(AccuroUserManager.class, userManger);
    map.put(OfficeManager.class, officeManager);
    map.put(TaskManager.class, userTaskManager);
    return map;
  }

  @Test
  public void testGetUserById() throws ProtossException {
    AccuroUser protossResult = getFixture(AccuroUser.class);
    int userId = protossResult.getUserId();
    when(userManger.getAccuroUser(userId)).thenReturn(protossResult);

    AccuroUserDto expected =
        mapDto(protossResult, AccuroUserDto.class);

    // test
    AccuroUserDto actual =
        given().pathParams("id", userId)
            .when()
            .get(getBaseUrl() + "/v1/users/{id}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(AccuroUserDto.class);

    assertEquals(actual, expected);
    verify(userManger).getAccuroUser(userId);
  }

  @Test
  public void testGetUserByIdNotFound() throws ProtossException {
    int userId = RandomUtils.nextInt();

    // test
    given().pathParams("id", userId)
        .when()
        .get(getBaseUrl() + "/v1/users/{id}")
        .then()
        .assertThat().statusCode(404);

    verify(userManger).getAccuroUser(userId);
  }

  @Test
  public void testSearchUserWithAllParams() throws Exception {

    String userName = TestUtilities.nextString(10);
    String firstName = TestUtilities.nextString(20);
    String lastName = TestUtilities.nextString(20);
    Long systemId = TestUtilities.nextLong();
    String externalUserId = TestUtilities.nextString(10);
    String globalSearch = TestUtilities.nextString(10);

    String activeDirectoryUser = TestUtilities.nextString(20);
    int startingId =
        getProtossEnvelope().getContents().stream().min(Comparator.comparing(AccuroUser::getUserId))
            .orElseThrow(RuntimeException::new).getUserId();

    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setUserName(userName)
        .setExtIdentitySystemId(systemId)
        .setIdentity(externalUserId)
        .setFirstName(firstName)
        .setLastName(lastName)
        .setActiveDirectoryUser(activeDirectoryUser)
        .setStartingId(startingId - 1)
        .setGlobalSearchField(globalSearch)
        .build();

    Envelope<AccuroUser> protossEnvelope = getProtossEnvelope();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(userManger)
        .searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("userName", userName)
            .queryParam("systemId", systemId)
            .queryParam("externalUserId", externalUserId)
            .queryParam("firstName", firstName)
            .queryParam("lastName", lastName)
            .queryParam("globalSearch", globalSearch)
            .queryParam("activeDirectoryUser", activeDirectoryUser)
            .queryParam("startingId", startingId - 1)
            .queryParam("pageSize", DEFAULT_PAGE_SIZE)
            .when()
            .get(getBaseUrl() + "/v1/users")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);
    // assertions
    EnvelopeDto<AccuroUserDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(userManger).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testSearchUserWithValidationOnFirstName() throws Exception {
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setFirstName("a")
        .build();
    given()
        .queryParam("firstName", "a")
        .when()
        .get(getBaseUrl() + "/v1/users")
        .then()
        .assertThat().statusCode(400);

    verify(userManger, never()).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchUserWithValidationOnLastName() throws Exception {
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setLastName("b")
        .build();
    given()
        .queryParam("lastName", "b")
        .when()
        .get(getBaseUrl() + "/v1/users")
        .then()
        .assertThat().statusCode(400);

    verify(userManger, never()).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testSearchUserWithOnlySystemId() throws Exception {
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setExtIdentitySystemId(2L)
        .build();
    given()
        .queryParam("systemId", 2L)
        .when()
        .get(getBaseUrl() + "/v1/users")
        .then()
        .assertThat().statusCode(400);

    verify(userManger, never()).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testSearchUserWithSystemIdAndExternalUserId() throws Exception {

    Long systemId = TestUtilities.nextLong();
    String externalUserId = TestUtilities.nextString(10);
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setExtIdentitySystemId(systemId)
        .setIdentity(externalUserId)
        .build();
    Envelope<AccuroUser> protossEnvelope = getProtossEnvelope();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(userManger)
        .searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .queryParam("systemId", systemId)
            .queryParam("externalUserId", externalUserId)
            .when()
            .get(getBaseUrl() + "/v1/users")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AccuroUserDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(userManger).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testSearchUserWithGlobalSearch() throws Exception {

    String globalSearch = TestUtilities.nextString(10);
    AccuroUserSearchInfo accuroUserSearchInfo = new AccuroUserSearchInfo.Builder()
        .setGlobalSearchField(globalSearch)
        .build();
    Envelope<AccuroUser> protossEnvelope = getProtossEnvelope();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(userManger)
        .searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .queryParam("globalSearch", globalSearch)
            .when()
            .get(getBaseUrl() + "/v1/users")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AccuroUserDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(userManger).searchAccuroUser(accuroUserSearchInfo, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetOffices() throws Exception {

    Envelope<Office> protossEnvelope = getProtossEnvelopeForOffice();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(officeManager)
        .getOfficesForUser(1, null, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .pathParam("userId", 1)
            .when()
            .get(getBaseUrl() + "/v1/users/{userId}/offices")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<OfficeDto> actual = refTypeOffice(rawActual);
    assertEquals(getExpectedOffices(protossEnvelope), actual);
    verify(officeManager).getOfficesForUser(1, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetOfficesMaxPageSize() throws Exception {

    Envelope<Office> protossEnvelope = getProtossEnvelopeForOffice();
    int startingId =
        protossEnvelope.getContents().stream().min(Comparator.comparing(Office::getOfficeId))
            .orElseThrow(RuntimeException::new).getOfficeId();
    // mock dependencies
    doReturn(protossEnvelope)
        .when(officeManager)
        .getOfficesForUser(1, startingId, 50);

    EnvelopeDto rawActual =
        given()
            .pathParam("userId", 1)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", 100)
            .when()
            .get(getBaseUrl() + "/v1/users/{userId}/offices")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<OfficeDto> actual = refTypeOffice(rawActual);
    assertEquals(getExpectedOffices(protossEnvelope), actual);
    verify(officeManager).getOfficesForUser(1, startingId, 50);
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
    Integer userId = TestUtilities.nextInt();

    // mock dependencies
    when(userTaskManager.getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        pageSize))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .pathParam("userId", userId)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/users/{userId}/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        pageSize);
  }

  @Test
  public void testGetTasksWithWrongDate() throws Exception {
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
    String createdStartDate = "2019-01-01";
    String createdEndDate = "2018-01-30";
    Integer startingId = null;
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;
    Integer userId = TestUtilities.nextInt();

    // mock dependencies
    when(userTaskManager.getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        pageSize))
            .thenReturn(protossEnvelope);
    // test

    given()
        .pathParam("userId", userId)
        .queryParam("includeCompleted", true)
        .queryParam("taskDueDate", taskDueDate)
        .queryParam("createdStartDate", createdStartDate)
        .queryParam("createdEndDate", createdEndDate)
        .queryParam("startingId", startingId)
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v1/users/{userId}/tasks")
        .then()
        .assertThat()
        .statusCode(400);

    verify(userTaskManager, never()).getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
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
    Integer userId = TestUtilities.nextInt();

    // mock dependencies
    when(userTaskManager.getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        50))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .pathParam("userId", userId)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/users/{userId}/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
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
    Integer userId = TestUtilities.nextInt();

    // mock dependencies
    when(userTaskManager.getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), null,
        25))
            .thenReturn(protossEnvelope);
    // test
    EnvelopeDto rawActual =
        given()
            .pathParam("userId", userId)
            .queryParam("includeCompleted", true)
            .queryParam("taskDueDate", taskDueDate)
            .queryParam("createdStartDate", createdStartDate)
            .queryParam("createdEndDate", createdEndDate)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v1/users/{userId}/tasks")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<UserTaskDto> actual = reType(rawActual);

    // assertions
    assertEquals(taskDtoEnvelopeDto, actual);
    verify(userTaskManager).getTasksForUser(userId, null, null, true, false,
        dueDate, LocalDate.parse(createdStartDate), LocalDate.parse(createdEndDate), startingId,
        25);
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
  public void testUpdateUser() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    Integer oldUserId = TestUtilities.nextInt();
    Integer newUserId = TestUtilities.nextInt();

    given()
        .pathParam("oldUserId", oldUserId)
        .queryParam("newUserId", newUserId)
        .body(Collections.singletonList(taskId))
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{oldUserId}/assign-tasks")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).updateTaskUser(oldUserId, newUserId, Collections.singletonList(taskId),
        true, null, null, null);
  }

  @Test
  public void testUpdateUserWithQueryParam() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    Integer oldUserId = TestUtilities.nextInt();
    Integer newUserId = TestUtilities.nextInt();
    LocalDate localDate = new LocalDate();

    String startDate = localDate.toString();

    given()
        .pathParam("existingUserId", oldUserId)
        .queryParam("newUserId", newUserId)
        .queryParam("taskDueDateStart", startDate)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{existingUserId}/assign-tasks")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(userTaskManager).updateTaskUser(oldUserId, newUserId, new ArrayList<>(),
        false, localDate, null, null);
  }

  @Test
  public void testUpdateUserWithTaskIdsAndFilters() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    Integer oldUserId = TestUtilities.nextInt();
    Integer newUserId = TestUtilities.nextInt();

    LocalDate localDate = new LocalDate();

    String startDate = localDate.toString();

    given()
        .pathParam("existingUserId", oldUserId)
        .queryParam("newUserId", newUserId)
        .queryParam("taskDueDateStart", startDate)
        .body(Collections.singletonList(taskId))
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{existingUserId}/assign-tasks")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager, never()).updateTaskUser(oldUserId, newUserId,
        Collections.singletonList(taskId),
        true, localDate, null, null);
  }

  @Test
  public void testUpdateUserWithSameUserIds() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    Integer oldUserId = TestUtilities.nextInt();
    Integer newUserId = oldUserId;

    LocalDate localDate = new LocalDate();

    String startDate = localDate.toString();

    given()
        .pathParam("oldUserId", oldUserId)
        .queryParam("newUserId", newUserId)
        .queryParam("startDate", startDate)
        .body(Collections.singletonList(taskId))
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{oldUserId}/assign-tasks")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager, never()).updateTaskUser(oldUserId, newUserId,
        Collections.singletonList(taskId),
        false, localDate, null, null);
  }

  @Test
  public void testUpdateUserWithWrongDates() throws Exception {
    // setup random data
    UserTaskDto userTaskDto = getFixture(UserTaskDto.class);
    int taskId = userTaskDto.getId();
    Integer oldUserId = TestUtilities.nextInt();
    Integer newUserId = oldUserId;

    LocalDate localDate = new LocalDate();

    String createdStartDate = "2019-01-01";
    String createdEndDate = "2018-01-30";

    given()
        .pathParam("oldUserId", oldUserId)
        .queryParam("newUserId", newUserId)
        .queryParam("taskDueDateStart", createdStartDate)
        .queryParam("taskDueDateEnd", createdEndDate)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/users/{oldUserId}/assign-tasks")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(userTaskManager, never()).updateTaskUser(oldUserId, newUserId,
        Collections.singletonList(taskId),
        false, localDate, null, null);
  }

  private Envelope<AccuroUser> getProtossEnvelope() {

    // setup random data
    List<AccuroUser> protossResults =
        getFixtures(AccuroUser.class, ArrayList::new, 10);
    Envelope<AccuroUser> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(AccuroUser::getUserId))
        .orElseThrow(RuntimeException::new).getUserId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setTotal(protossResults.size());
    return protossEnvelope;
  }

  private Envelope<Office> getProtossEnvelopeForOffice() {

    // setup random data
    List<Office> protossResults =
        getFixtures(Office.class, ArrayList::new, 10);
    Envelope<Office> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(Office::getOfficeId))
        .orElseThrow(RuntimeException::new).getOfficeId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setTotal(protossResults.size());
    return protossEnvelope;
  }

  private EnvelopeDto<AccuroUserDto> getExpected(Envelope<AccuroUser> protossEnvelope) {
    List<AccuroUserDto> accuroUsers =
        mapDto(protossEnvelope.getContents(), AccuroUserDto.class, ArrayList::new);

    EnvelopeDto<AccuroUserDto> expected = new EnvelopeDto<>();

    expected.setContents(accuroUsers);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  private EnvelopeDto<OfficeDto> getExpectedOffices(Envelope<Office> protossEnvelope) {
    List<OfficeDto> officeDtos =
        mapDto(protossEnvelope.getContents(), OfficeDto.class, ArrayList::new);

    EnvelopeDto<OfficeDto> expected = new EnvelopeDto<>();

    expected.setContents(officeDtos);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  private EnvelopeDto<AccuroUserDto> refType(EnvelopeDto rawEnvelope) {
    List<AccuroUserDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<AccuroUserDto>>() {});
    EnvelopeDto<AccuroUserDto> e = (EnvelopeDto<AccuroUserDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  private EnvelopeDto<OfficeDto> refTypeOffice(EnvelopeDto rawEnvelope) {
    List<OfficeDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<OfficeDto>>() {});
    EnvelopeDto<OfficeDto> e = (EnvelopeDto<OfficeDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

}

