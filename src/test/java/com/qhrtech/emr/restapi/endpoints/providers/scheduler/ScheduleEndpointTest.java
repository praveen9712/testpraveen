
package com.qhrtech.emr.restapi.endpoints.providers.scheduler;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.scheduling.PriorityManager;
import com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.StatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentReason;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentType;
import com.qhrtech.emr.accuro.model.scheduling.Priority;
import com.qhrtech.emr.accuro.model.scheduling.Status;
import com.qhrtech.emr.restapi.endpoints.provider.scheduler.ScheduleEndpoint;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppointmentReasonDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentTypeDto;
import com.qhrtech.emr.restapi.models.dto.PriorityDto;
import com.qhrtech.emr.restapi.models.dto.StatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ScheduleEndpointTest extends AbstractEndpointTest<ScheduleEndpoint> {

  private ScheduleSettingsManager scheduleSettingsManager;
  private PriorityManager priorityManager;
  private StatusManager statusManager;

  public ScheduleEndpointTest() {
    super(new ScheduleEndpoint(), ScheduleEndpoint.class);
    scheduleSettingsManager = mock(ScheduleSettingsManager.class);
    priorityManager = mock(PriorityManager.class);
    statusManager = mock(StatusManager.class);
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
    servicesMap.put(ScheduleSettingsManager.class, scheduleSettingsManager);
    servicesMap.put(PriorityManager.class, priorityManager);
    servicesMap.put(StatusManager.class, statusManager);
    return servicesMap;
  }

  @Test
  public void testGetAppointmentTypes() throws ProtossException {
    List<AppointmentType> protossResults = getFixtures(AppointmentType.class, ArrayList::new, 2);
    List<AppointmentTypeDto> expected =
        mapDto(protossResults, AppointmentTypeDto.class, ArrayList::new);
    // mock dependencies
    when(scheduleSettingsManager.getTypes()).thenReturn(protossResults);

    List<AppointmentTypeDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/types")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentTypeDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(scheduleSettingsManager).getTypes();
  }

  @Test
  public void testGetAppointmentReasons() throws ProtossException {
    List<AppointmentReason> protossResults =
        getFixtures(AppointmentReason.class, ArrayList::new, 2);
    List<AppointmentReasonDto> expected =
        mapDto(protossResults, AppointmentReasonDto.class, ArrayList::new);
    // mock dependencies
    when(scheduleSettingsManager.getReasons()).thenReturn(protossResults);

    List<AppointmentReasonDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/reasons")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentReasonDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);
    verify(scheduleSettingsManager).getReasons();
  }

  @Test
  public void testGetAppointmentStatuses() throws ProtossException {
    List<Status> protossResults = getFixtures(Status.class, ArrayList::new, 2);
    List<StatusDto> expected = mapDto(protossResults, StatusDto.class, ArrayList::new);
    // mock dependencies
    when(statusManager.getStatusList()).thenReturn(protossResults);

    List<StatusDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(StatusDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);
    verify(statusManager).getStatusList();
  }

  @Test
  public void testGetPriorities() throws ProtossException {
    List<Priority> protossResults = getFixtures(Priority.class, ArrayList::new, 2);
    List<PriorityDto> expected = mapDto(protossResults, PriorityDto.class, ArrayList::new);
    // mock dependencies
    when(priorityManager.getPriorities()).thenReturn(protossResults);

    List<PriorityDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/scheduler/priorities")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PriorityDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);
    verify(priorityManager).getPriorities();
  }
}
