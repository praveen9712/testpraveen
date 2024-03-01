
package com.qhrtech.emr.restapi.endpoints.patient;

import static com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities.nextUtcCalendar;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentManager;
import com.qhrtech.emr.accuro.model.scheduling.Appointment;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppointmentDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class ScheduleEndpointTest extends AbstractEndpointTest<ScheduleEndpoint> {
  private final AppointmentManager appointmentManager;
  ApiSecurityContext context;
  private AuditLogUser user;

  public ScheduleEndpointTest() {
    super(new ScheduleEndpoint(), ScheduleEndpoint.class);
    appointmentManager = mock(AppointmentManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    context.setPatientId(RandomUtils.nextInt());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(AppointmentManager.class, appointmentManager);
    return servicesMap;
  }

  @Test
  public void testGetAppointmentHistory() throws Exception {
    // setup random data
    List<Appointment> protossResults =
        getFixtures(Appointment.class, ArrayList::new, 2);
    Calendar startDateC = nextUtcCalendar();
    LocalDate startDateL = LocalDate.fromCalendarFields(startDateC);
    LocalDate endDateL = startDateL.plusYears(10);
    String startDate = startDateL.toString();
    String endDate = endDateL.toString();
    Integer providerId = RandomUtils.nextInt();
    Integer resourceId = RandomUtils.nextInt();
    Integer patientId = context.getPatientId();

    // mock dependencies
    when(appointmentManager.getAppointments(startDateL, endDateL, patientId,
        providerId, resourceId, null)).thenReturn(protossResults);
    List<AppointmentDto> expected =
        mapDto(protossResults, AppointmentDto.class, ArrayList::new);
    // test
    List<AppointmentDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("provider", providerId)
            .queryParam("resource", resourceId)
            .when()
            .get(
                getBaseUrl() + "/v1/patient-portal/scheduler/appointments")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(appointmentManager).getAppointments(startDateL, endDateL, patientId, providerId,
        resourceId, null);
  }

  @Test
  public void testGetAppointmentHistoryReverseStartandEndDate() throws Exception {
    // setup random data
    List<Appointment> protossResults =
        getFixtures(Appointment.class, ArrayList::new, 2);
    Calendar startDateC = nextUtcCalendar();
    LocalDate startDateL = LocalDate.fromCalendarFields(startDateC);
    LocalDate endDateL = startDateL.minusYears(10);
    String startDate = startDateL.toString();
    String endDate = endDateL.toString();
    Integer providerId = RandomUtils.nextInt();
    Integer resourceId = RandomUtils.nextInt();
    Integer patientId = context.getPatientId();

    // mock dependencies
    when(appointmentManager.getAppointments(endDateL, startDateL, patientId, providerId,
        resourceId, null)).thenReturn(protossResults);
    List<AppointmentDto> expected =
        mapDto(protossResults, AppointmentDto.class, ArrayList::new);
    // test
    List<AppointmentDto> actual = toCollection(
        given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("provider", providerId)
            .queryParam("resource", resourceId)
            .when()
            .get(
                getBaseUrl() + "/v1/patient-portal/scheduler/appointments")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(appointmentManager).getAppointments(endDateL, startDateL, patientId, providerId,
        resourceId, null);
  }

  @Test
  public void testGetAppointment() throws Exception {
    // setup random data
    Appointment appointment = getFixture(Appointment.class);
    Integer appointmentId = RandomUtils.nextInt();
    appointment.setPatientId(context.getPatientId());

    // mock dependencies
    when(appointmentManager.getVisibleAppointmentById(appointmentId)).thenReturn(appointment);
    AppointmentDto expected = mapDto(appointment, AppointmentDto.class);
    // test
    AppointmentDto actual =
        given()
            .pathParams("appointmentId", appointmentId)
            .when()
            .get(
                getBaseUrl() + "/v1/patient-portal/scheduler/appointments/{appointmentId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentDto.class);
    // assertions
    assertEquals(expected, actual);
    verify(appointmentManager).getVisibleAppointmentById(appointmentId);
  }

  @Test
  public void testGetAppointmentNullAppointment() throws Exception {
    // setup random data
    Appointment appointment = getFixture(Appointment.class);
    Integer appointmentId = RandomUtils.nextInt();
    appointment.setPatientId(context.getPatientId());

    // mock dependencies
    when(appointmentManager.getVisibleAppointmentById(appointmentId)).thenReturn(null);
    // test
    given()
        .pathParams("appointmentId", appointmentId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/scheduler/appointments/{appointmentId}")
        .then()
        .assertThat()
        .statusCode(404);
    // assertions
    verify(appointmentManager).getVisibleAppointmentById(appointmentId);
  }

  @Test
  public void testGetAppointmentNotEqualPatientId() throws Exception {
    // setup random data
    Appointment appointment = getFixture(Appointment.class);
    Integer appointmentId = RandomUtils.nextInt();

    // mock dependencies
    when(appointmentManager.getVisibleAppointmentById(appointmentId)).thenReturn(appointment);
    // test
    given()
        .pathParams("appointmentId", appointmentId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/scheduler/appointments/{appointmentId}")
        .then()
        .assertThat()
        .statusCode(404);
    // assertions
    verify(appointmentManager).getVisibleAppointmentById(appointmentId);
  }

}
