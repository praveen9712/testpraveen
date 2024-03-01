
package com.qhrtech.emr.restapi.endpoints.provider;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.provider.ProviderScheduleCalenderManager;
import com.qhrtech.emr.accuro.api.scheduling.AvailabilityManager;
import com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.StatusManager;
import com.qhrtech.emr.accuro.api.scheduling.SuggestionManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.provider.ProviderScheduleCalendar;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentReason;
import com.qhrtech.emr.accuro.model.scheduling.Status;
import com.qhrtech.emr.accuro.model.scheduling.availability.AppliedAvailability;
import com.qhrtech.emr.accuro.model.scheduling.availability.Availability;
import com.qhrtech.emr.accuro.model.scheduling.availability.AvailabilityTemplate;
import com.qhrtech.emr.accuro.model.scheduling.suggestions.AppliedSuggestion;
import com.qhrtech.emr.accuro.model.scheduling.suggestions.ScheduleSuggestion;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppliedAvailabilityDto;
import com.qhrtech.emr.restapi.models.dto.AppliedSuggestionDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentReasonDto;
import com.qhrtech.emr.restapi.models.dto.AvailabilityDto;
import com.qhrtech.emr.restapi.models.dto.AvailabilityTemplateDto;
import com.qhrtech.emr.restapi.models.dto.ProviderScheduleCalendarDto;
import com.qhrtech.emr.restapi.models.dto.ScheduleSuggestionDto;
import com.qhrtech.emr.restapi.models.dto.StatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class ScheduleEndpointTest extends AbstractEndpointTest<ScheduleEndpoint> {

  private ProviderScheduleCalenderManager providerScheduleCalenderManager;
  private SuggestionManager suggestionManager;
  private ScheduleSettingsManager scheduleSettingsManager;
  private StatusManager statusManager;
  private AvailabilityManager availabilityManager;
  private UserAuthenticationManager userAuthenticationManager;

  public ScheduleEndpointTest() {
    super(new ScheduleEndpoint(), ScheduleEndpoint.class);
    providerScheduleCalenderManager = mock(ProviderScheduleCalenderManager.class);
    suggestionManager = mock(SuggestionManager.class);
    scheduleSettingsManager = mock(ScheduleSettingsManager.class);
    statusManager = mock(StatusManager.class);
    availabilityManager = mock(AvailabilityManager.class);
    userAuthenticationManager = mock(UserAuthenticationManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    AuditLogUser user = new AuditLogUser(null,
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(ProviderScheduleCalenderManager.class, providerScheduleCalenderManager);
    servicesMap.put(SuggestionManager.class, suggestionManager);
    servicesMap.put(ScheduleSettingsManager.class, scheduleSettingsManager);
    servicesMap.put(StatusManager.class, statusManager);
    servicesMap.put(AvailabilityManager.class, availabilityManager);
    servicesMap.put(UserAuthenticationManager.class, userAuthenticationManager);
    return servicesMap;
  }

  @Test
  public void createAppointmentWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);


    given()
        .body(appointmentDto)
        .when()
        .contentType(ContentType.JSON)
        .post(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/appointments")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void updateAppointmentWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);

    given()
        .body(appointmentDto)
        .when()
        .contentType(ContentType.JSON)
        .put(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/appointments/" + appointmentDto.getAppointmentId())
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void deleteAppointmentWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);

    given()
        .body(appointmentDto)
        .when()
        .contentType(ContentType.JSON)
        .delete(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/appointments/" + appointmentDto.getAppointmentId())
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void createLockWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);
    int id = appointmentDto.getAppointmentId();


    given()
        .when()
        .contentType(ContentType.JSON)
        .post(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/appointments/" + id + "/lock")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void DeleteLockWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);
    int id = appointmentDto.getAppointmentId();


    given()
        .when()
        .contentType(ContentType.JSON)
        .delete(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/appointments/" + id + "/lock")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void createScheduleLockWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);
    int id = appointmentDto.getAppointmentId();


    given()
        .when()
        .contentType(ContentType.JSON)
        .post(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/lock")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void DeleteScheduleLockWithoutUser() throws DatabaseInteractionException {

    AppointmentDto appointmentDto = getFixture(AppointmentDto.class);
    appointmentDto.setAppointmentDetails(null);
    int id = appointmentDto.getAppointmentId();
    UUID lockUuid = UUID.randomUUID();

    given()
        .body("\"" + lockUuid + "\"")
        .when()
        .contentType(ContentType.JSON)
        .delete(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/lock")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void testGetAvailabilityTemplates() throws DatabaseInteractionException {

    Set<AvailabilityTemplateDto> availabilityTemplateDtos =
        getFixtures(AvailabilityTemplateDto.class, HashSet::new, 5);
    Set<AvailabilityTemplate> protossResult =
        mapDto(availabilityTemplateDtos, AvailabilityTemplate.class, HashSet::new);

    when(availabilityManager.getTemplates()).thenReturn(protossResult);

    Set<AvailabilityTemplateDto> actual = toCollection(
        given()
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/availability-templates")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AvailabilityTemplateDto[].class),
        HashSet::new);

    verify(availabilityManager).getTemplates();
    assertTrue(actual.containsAll(availabilityTemplateDtos));

  }

  @Test
  public void testGetTemplateAvailabilities() throws DatabaseInteractionException {

    Set<AvailabilityDto> availabilityDtos = getFixtures(AvailabilityDto.class, HashSet::new, 5);
    Set<Availability> protossResult = mapDto(availabilityDtos, Availability.class, HashSet::new);

    int templateId = RandomUtils.nextInt();
    when(availabilityManager.getAvailabilities(templateId)).thenReturn(protossResult);

    Set<AvailabilityDto> actual = toCollection(
        given()
            .pathParam("templateId", templateId)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/availability-templates/{templateId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AvailabilityDto[].class),
        HashSet::new);

    verify(availabilityManager).getAvailabilities(templateId);
    assertTrue(actual.containsAll(availabilityDtos));

  }

  @Test
  public void testGetAppliedAvailabilities()
      throws DatabaseInteractionException, InsufficientRolesException,
      InsufficientPermissionsException, InsufficientFeatureAccessException {

    Set<AppliedAvailabilityDto> availabilityDtos =
        getFixtures(AppliedAvailabilityDto.class, HashSet::new, 5);
    Set<AppliedAvailability> protossResult =
        mapDto(availabilityDtos, AppliedAvailability.class, HashSet::new);

    int providerId = RandomUtils.nextInt();
    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();

    when(availabilityManager.getAppliedAvailabilities(providerId, startLocal, endLocal))
        .thenReturn(protossResult);

    Set<AppliedAvailabilityDto> actual = toCollection(
        given()
            .queryParam("providerId", providerId)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/applied-availabilities")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppliedAvailabilityDto[].class),
        HashSet::new);

    verify(availabilityManager).getAppliedAvailabilities(providerId, startLocal, endLocal);
    assertTrue(actual.containsAll(availabilityDtos));

  }

  @Test
  public void testGetAppliedAvailabilitiesNoProviderId() {

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();

    given()
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
        .when()
        .get(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/applied-availabilities")
        .then()
        .assertThat()
        .statusCode(400);
    verifyNoInteractions(availabilityManager);
  }

  @Test
  public void testGetAppliedAvailabilitiesNoStartDate() {

    int providerId = RandomUtils.nextInt();
    Calendar startDateC = nextUtcCalendar();
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String endDate = endLocal.toString();
    given()
        .queryParam("providerId", providerId)
        .queryParam("endDate", endDate)
        .when()
        .get(
            getBaseUrl()
                + "/v1/provider-portal/scheduler/applied-availabilities")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(availabilityManager);

  }

  @Test
  public void testUpdateStatusIdNotMatch() {
    StatusDto statusDto = getFixture(StatusDto.class);
    int statusId = RandomUtils.nextInt();

    given()
        .body(statusDto)
        .pathParam("statusId", statusId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/statuses/{statusId}")
        .then()
        .assertThat().statusCode(400);

    verifyNoInteractions(statusManager);
  }

  @Test
  public void testUpdateStatus() throws InsufficientRolesException, DatabaseInteractionException,
      InsufficientPermissionsException, InsufficientFeatureAccessException {
    StatusDto statusDto = getFixture(StatusDto.class);
    int statusId = statusDto.getStatusId();

    given()
        .body(statusDto)
        .pathParam("statusId", statusId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/statuses/{statusId}")
        .then()
        .assertThat().statusCode(200);

    Status status = mapDto(statusDto, Status.class);
    verify(statusManager).updateStatus(status);
  }

  @Test
  public void testUpdateReasonReasonIdNotMatch() {
    AppointmentReasonDto reasonDto = getFixture(AppointmentReasonDto.class);
    int reasonId = TestUtilities.nextInt();
    given()
        .pathParam("reasonId", reasonId)
        .body(reasonDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/reasons/{reasonId}")
        .then()
        .assertThat().statusCode(400);

    verifyNoInteractions(scheduleSettingsManager);

  }

  @Test
  public void testUpdateReason()
      throws SaveException, InsufficientRolesException, DatabaseInteractionException,
      InsufficientPermissionsException, NoDataFoundException, InsufficientFeatureAccessException {
    AppointmentReasonDto reasonDto = getFixture(AppointmentReasonDto.class);
    int reasonId = reasonDto.getReasonId();
    given()
        .pathParam("reasonId", reasonId)
        .body(reasonDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/reasons/{reasonId}")
        .then()
        .assertThat().statusCode(200);
    AppointmentReason reason = mapDto(reasonDto, AppointmentReason.class);
    verify(scheduleSettingsManager).updateReason(reason);

  }


  @Test
  public void testGetAppliedSuggestions()
      throws InsufficientRolesException, DatabaseInteractionException {
    Set<AppliedSuggestionDto> expected = getFixtures(AppliedSuggestionDto.class, HashSet::new, 5);
    Set<AppliedSuggestion> protossResult = mapDto(expected, AppliedSuggestion.class, HashSet::new);

    Integer providerId = RandomUtils.nextInt();
    Integer resourceId = RandomUtils.nextInt();
    Integer subColumn = RandomUtils.nextInt();

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();

    Collection<Integer> providerIds =
        providerId == null ? null : Collections.singletonList(providerId);
    Collection<Integer> resourceIds =
        resourceId == null ? null : Collections.singletonList(resourceId);

    when(suggestionManager.getAppliedSuggestions(startLocal, endLocal, providerIds,
        resourceIds, subColumn)).thenReturn(protossResult);

    Set<AppliedSuggestionDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("provider", providerId)
            .queryParam("resource", resourceId)
            .queryParam("subColumn", subColumn)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/applied-suggestions")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppliedSuggestionDto[].class),
        HashSet::new);


    verify(suggestionManager).getAppliedSuggestions(startLocal, endLocal, providerIds,
        resourceIds, subColumn);
    assertTrue(actual.containsAll(expected));

  }

  @Test
  public void testGetAppliedSuggestionsStartDateAfterEndDate()
      throws InsufficientRolesException, DatabaseInteractionException {
    Set<AppliedSuggestionDto> expected = getFixtures(AppliedSuggestionDto.class, HashSet::new, 5);
    Set<AppliedSuggestion> protossResult = mapDto(expected, AppliedSuggestion.class, HashSet::new);

    Integer providerId = RandomUtils.nextInt();
    Integer resourceId = RandomUtils.nextInt();
    Integer subColumn = RandomUtils.nextInt();

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();

    Collection<Integer> providerIds =
        providerId == null ? null : Collections.singletonList(providerId);
    Collection<Integer> resourceIds =
        resourceId == null ? null : Collections.singletonList(resourceId);

    when(suggestionManager.getAppliedSuggestions(startLocal, endLocal, providerIds,
        resourceIds, subColumn)).thenReturn(protossResult);

    Set<AppliedSuggestionDto> actual = toCollection(
        given()
            .queryParam("startDate", endDate)
            .queryParam("endDate", startDate)
            .queryParam("provider", providerId)
            .queryParam("resource", resourceId)
            .queryParam("subColumn", subColumn)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/applied-suggestions")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppliedSuggestionDto[].class),
        HashSet::new);


    verify(suggestionManager).getAppliedSuggestions(startLocal, endLocal, providerIds,
        resourceIds, subColumn);
    assertTrue(actual.containsAll(expected));

  }

  @Test
  public void testGetAppliedSuggestionsNoParams() {

    given()
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/scheduler/applied-suggestions")
        .then()
        .assertThat()
        .statusCode(400);

    verifyNoInteractions(suggestionManager);

  }

  @Test
  public void testGetAllSuggestions() throws DatabaseInteractionException {
    Set<ScheduleSuggestionDto> expected =
        getFixtures(ScheduleSuggestionDto.class, HashSet::new, 10);

    Set<ScheduleSuggestion> protossResult =
        mapDto(expected, ScheduleSuggestion.class, HashSet::new);

    when(suggestionManager.getSuggestions()).thenReturn(protossResult);

    Set<ScheduleSuggestionDto> actual = toCollection(
        given()
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/suggestions")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ScheduleSuggestionDto[].class),
        HashSet::new);

    verify(suggestionManager).getSuggestions();
    assertTrue(actual.containsAll(expected));

  }


  @Test
  public void testCreateProviderCalendarNote() throws ProtossException {
    ProviderScheduleCalendarDto scheduleCalendarDto = getFixture(ProviderScheduleCalendarDto.class);
    int providerId = scheduleCalendarDto.getProviderId();

    ProviderScheduleCalendar providerScheduleCalendar =
        mapDto(scheduleCalendarDto, ProviderScheduleCalendar.class);
    doNothing().when(providerScheduleCalenderManager)
        .create(providerScheduleCalendar);

    given()
        .pathParam("providerId", providerId)
        .body(scheduleCalendarDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat().statusCode(200);

    verify(providerScheduleCalenderManager).create(providerScheduleCalendar);

  }

  @Test
  public void testCreateProviderCalendarNoteException() throws ProtossException {
    ProviderScheduleCalendarDto scheduleCalendarDto = getFixture(ProviderScheduleCalendarDto.class);
    int providerId = TestUtilities.nextInt();

    given()
        .pathParam("providerId", providerId)
        .body(scheduleCalendarDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat().statusCode(400);

    verify(providerScheduleCalenderManager, never()).create(any(ProviderScheduleCalendar.class));

  }

  @Test
  public void testGetProviderCalendarNotes() throws Exception {
    // setup random data
    List<ProviderScheduleCalendar> protossResults =
        getFixtures(ProviderScheduleCalendar.class, ArrayList::new, 2);

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();
    int providerId = RandomUtils.nextInt();

    // mock dependencies
    when(providerScheduleCalenderManager.getForProvider(providerId, startLocal, endLocal))
        .thenReturn(protossResults);
    Set<ProviderScheduleCalendarDto> expected =
        mapDto(protossResults, ProviderScheduleCalendarDto.class, HashSet::new);
    // test
    Set<ProviderScheduleCalendarDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .pathParam("providerId", providerId)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ProviderScheduleCalendarDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(providerScheduleCalenderManager).getForProvider(providerId,
        startLocal, endLocal);
  }

  @Test
  public void testGetProviderCalendarNotesReverseDates() throws Exception {
    // setup random data
    List<ProviderScheduleCalendar> protossResults =
        getFixtures(ProviderScheduleCalendar.class, ArrayList::new, 2);

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).minusMonths(10);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();
    int providerId = RandomUtils.nextInt();

    // mock dependencies
    when(providerScheduleCalenderManager.getForProvider(providerId, startLocal, endLocal))
        .thenReturn(protossResults);
    Set<ProviderScheduleCalendarDto> expected =
        mapDto(protossResults, ProviderScheduleCalendarDto.class, HashSet::new);
    // test
    Set<ProviderScheduleCalendarDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .pathParam("providerId", providerId)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ProviderScheduleCalendarDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(providerScheduleCalenderManager).getForProvider(providerId,
        startLocal, endLocal);
  }

  @Test
  public void testGetProviderCalendarNotesNoEndDate() throws Exception {
    // setup random data
    List<ProviderScheduleCalendar> protossResults =
        Collections.singletonList(getFixture(ProviderScheduleCalendar.class));

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    String startDate = startLocal.toString();
    int providerId = RandomUtils.nextInt();

    // mock dependencies
    when(providerScheduleCalenderManager.getForProvider(providerId, startLocal, null))
        .thenReturn(protossResults);
    Set<ProviderScheduleCalendarDto> expected =
        mapDto(protossResults, ProviderScheduleCalendarDto.class, HashSet::new);
    // test
    Set<ProviderScheduleCalendarDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .pathParam("providerId", providerId)
            .when()
            .get(
                getBaseUrl()
                    + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ProviderScheduleCalendarDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(providerScheduleCalenderManager).getForProvider(providerId,
        startLocal, null);
  }

  @Test
  public void testGetProviderCalendarNotesDatesMoreThanYear() {

    Calendar startDateC = nextUtcCalendar();
    LocalDate startLocal = LocalDate.fromCalendarFields(startDateC);
    LocalDate endLocal = LocalDate.fromCalendarFields(startDateC).plusMonths(13);
    String startDate = startLocal.toString();
    String endDate = endLocal.toString();
    int providerId = RandomUtils.nextInt();

    // test
    given()
        .queryParam("startDate", startDate)
        .queryParam("endDate", endDate)
        .pathParam("providerId", providerId)
        .when()
        .get(
            getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verifyZeroInteractions(providerScheduleCalenderManager);
  }

  @Test
  public void testGetProviderCalendarNotesDatesNull() throws ProtossException {

    int providerId = RandomUtils.nextInt();
    doThrow(IllegalArgumentException.class).when(providerScheduleCalenderManager)
        .getForProvider(providerId, null, null);
    // test
    given()
        .pathParam("providerId", providerId)
        .when()
        .get(
            getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);
    verify(providerScheduleCalenderManager).getForProvider(providerId, null, null);
  }

  @Test
  public void testUpdateProviderCalendarNotes() throws ProtossException {
    ProviderScheduleCalendarDto calendarDto = getFixture(ProviderScheduleCalendarDto.class);
    ProviderScheduleCalendar protossModel = mapDto(calendarDto, ProviderScheduleCalendar.class);
    doNothing().when(providerScheduleCalenderManager)
        .update(protossModel);

    given()
        .pathParam("providerId", calendarDto.getProviderId())
        .body(calendarDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(200);

    verify(providerScheduleCalenderManager)
        .update(protossModel);
  }

  @Test
  public void testUpdateProviderCalendarNotesException() throws ProtossException {
    ProviderScheduleCalendarDto calendarDto = getFixture(ProviderScheduleCalendarDto.class);

    given()
        .pathParam("providerId", TestUtilities.nextInt())
        .body(calendarDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);

    verify(providerScheduleCalenderManager, never())
        .update(any(ProviderScheduleCalendar.class));
  }

  @Test
  public void testUpdateProviderCalendarNotesNullNotes() throws ProtossException {
    ProviderScheduleCalendarDto calendarDto = getFixture(ProviderScheduleCalendarDto.class);
    calendarDto.setNote(null);

    given()
        .pathParam("providerId", TestUtilities.nextInt())
        .body(calendarDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);

    verify(providerScheduleCalenderManager, never())
        .update(any(ProviderScheduleCalendar.class));
  }

  @Test
  public void testUpdateProviderCalendarNotesNullObject() throws ProtossException {

    given()
        .pathParam("providerId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);

    verify(providerScheduleCalenderManager, never())
        .update(any(ProviderScheduleCalendar.class));
  }

  @Test
  public void testDeleteProviderCalendarNotes() throws ProtossException {
    Calendar startDateC = nextUtcCalendar();
    LocalDate localDate = LocalDate.fromCalendarFields(startDateC);
    String date = localDate.toString();
    int providerId = TestUtilities.nextInt();

    ProviderScheduleCalendar protossResult = getFixture(ProviderScheduleCalendar.class);

    when(providerScheduleCalenderManager.getForProvider(providerId, localDate, null))
        .thenReturn(Collections.singletonList(protossResult));
    doNothing().when(providerScheduleCalenderManager).delete(providerId, localDate);

    given()
        .pathParam("providerId", providerId)
        .queryParam("date", date)
        .when()
        .delete(
            getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(200);

    verify(providerScheduleCalenderManager).delete(providerId, localDate);

  }

  @Test
  public void testDeleteProviderCalendarNotesException() {

    int providerId = TestUtilities.nextInt();

    given()
        .pathParam("providerId", providerId)
        .when()
        .delete(
            getBaseUrl() + "/v1/provider-portal/scheduler/providers/{providerId}/calendar-notes")
        .then()
        .assertThat()
        .statusCode(400);

    verifyZeroInteractions(providerScheduleCalenderManager);
  }

}
