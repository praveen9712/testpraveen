
package com.qhrtech.emr.restapi.endpoints.provider;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentHistoryManager;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentHistory;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppointmentHistoryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.junit.Test;


public class AppointmentHistoryEndpointTest
    extends AbstractEndpointTest<AppointmentHistoryEndpoint> {

  private final AppointmentHistoryManager appointHistoryManager;

  public AppointmentHistoryEndpointTest() {

    super(new AppointmentHistoryEndpoint(), AppointmentHistoryEndpoint.class);
    appointHistoryManager = mock(AppointmentHistoryManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(AppointmentHistoryManager.class, appointHistoryManager);
    return servicesMap;
  }

  @Test
  public void testGetAppointmentHistory() throws Exception {
    // setup random data
    List<AppointmentHistory> protossResults =
        getFixtures(AppointmentHistory.class, ArrayList::new, 2);
    int appointmentId = TestUtilities.nextId();
    // mock dependencies
    when(appointHistoryManager.getHistoryForAppointment(appointmentId)).thenReturn(protossResults);
    List<AppointmentHistoryDto> expected =
        mapDto(protossResults, AppointmentHistoryDto.class, ArrayList::new);
    // test
    List<AppointmentHistoryDto> actual = toCollection(
        given().pathParam("appointmentId", appointmentId)
            .when()
            .get(
                getBaseUrl() + "/v1/provider-portal/scheduler/appointments/{appointmentId}/history")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentHistoryDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(appointHistoryManager).getHistoryForAppointment(appointmentId);
  }

  @Test
  public void testGetAppointmentHistoryEmpty() throws Exception {
    // setup random data
    int appointmentId = TestUtilities.nextId();
    List<AppointmentHistory> protossResults = new ArrayList<>();
    // mock dependencies
    when(appointHistoryManager.getHistoryForAppointment(appointmentId)).thenReturn(protossResults);

    // test
    given().pathParam("appointmentId", appointmentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/scheduler/appointments/{appointmentId}/history")
        .then()
        .assertThat()
        .statusCode(404);
    verify(appointHistoryManager).getHistoryForAppointment(appointmentId);

  }

  @Test
  public void testGetAppointmentHistoryWithStartDate() throws Exception {
    List<AppointmentHistory> protossResults =
        getFixtures(AppointmentHistory.class, ArrayList::new, 2);

    Calendar startDateC = nextUtcCalendar();
    LocalDate startDateL = LocalDate.fromCalendarFields(startDateC);
    LocalDate endDateL = startDateL.plusYears(10);
    String startDate = startDateL.toString();
    String endDate = endDateL.toString();

    // mock dependencies
    when(appointHistoryManager.getHistoryByDate(startDateL, endDateL))
        .thenReturn(protossResults);
    List<AppointmentHistoryDto> expected =
        mapDto(protossResults, AppointmentHistoryDto.class, ArrayList::new);
    // test
    List<AppointmentHistoryDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/appointments/history")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentHistoryDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(appointHistoryManager).getHistoryByDate(startDateL, endDateL);
  }

  @Test
  public void testGetAppointmentHistoryWithStartDateMoreThanEndDate() throws Exception {
    List<AppointmentHistory> protossResults =
        getFixtures(AppointmentHistory.class, ArrayList::new, 2);

    Calendar startDateC = nextUtcCalendar();
    LocalDate startDateL = LocalDate.fromCalendarFields(startDateC);
    LocalDate endDateL = startDateL.minusDays(10);
    String startDate = startDateL.toString();
    String endDate = endDateL.toString();

    // mock dependencies
    when(appointHistoryManager.getHistoryByDate(endDateL, startDateL))
        .thenReturn(protossResults);
    List<AppointmentHistoryDto> expected =
        mapDto(protossResults, AppointmentHistoryDto.class, ArrayList::new);
    // test
    List<AppointmentHistoryDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/appointments/history")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentHistoryDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(appointHistoryManager).getHistoryByDate(endDateL, startDateL);
  }

  @Test
  public void testGetAppointmentStartDateNull() throws Exception {
    // test
    given()
        .queryParam("endDate", "2010-01-01")
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/scheduler/appointments/history")
        .then()
        .assertThat()
        .statusCode(400);
    verify(appointHistoryManager, never()).getHistoryByDate(any(LocalDate.class),
        any(LocalDate.class));
  }
}
