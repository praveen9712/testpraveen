
package com.qhrtech.emr.restapi.endpoints.materials;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentReminderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.scheduling.ReminderData;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppointmentMaterialsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.Map;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class AppointmentMaterialsEndpointTest
    extends AbstractEndpointTest<AppointmentMaterialsEndpoint> {

  private static final Integer TEST_ID = 100;
  private static final int DEFAULT_PAGE_SIZE = 25;
  private final AppointmentReminderManager reminderManager;

  public AppointmentMaterialsEndpointTest() {
    super(new AppointmentMaterialsEndpoint(), AppointmentMaterialsEndpoint.class);
    reminderManager = mock(AppointmentReminderManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(AppointmentReminderManager.class, reminderManager);
  }

  @Test
  public void getAppointmentsNoStartDate() {
    when().get(getBaseUrl() + "/v1/materials/appointments")
        .then()
        .assertThat().statusCode(400);
  }

  @Test
  public void getAppointments() throws ProtossException {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null,
        null,
        null, false, null, DEFAULT_PAGE_SIZE);
    when().get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null, null,
        null, false, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsWithBillOnlyTrue() throws Exception {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null,
        null,
        null, true, null, DEFAULT_PAGE_SIZE);
    when()
        .get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01&includeBillOnly=true")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null, null,
        null, true, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsWithBillOnlyFalse() throws Exception {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null,
        null,
        null, false, null, DEFAULT_PAGE_SIZE);
    when()
        .get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01&includeBillOnly=false")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null, null,
        null, false, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsWithInvalidPageSize() throws ProtossException {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    Integer[] pageArray = {-1, 0, DEFAULT_PAGE_SIZE};
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null,
        null,
        null, false, null, DEFAULT_PAGE_SIZE);
    when()
        .get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01&pageSize="
            + pageArray[TestUtilities.nextInt(3)])
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null, null,
        null, false, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsWithNullPageSize() throws ProtossException {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null,
        null,
        null, false, null, DEFAULT_PAGE_SIZE);
    when().get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("2000-01-01"), null, null,
        null, false, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsDateSwap() throws ProtossException {
    ReminderData reminderData = getFixture(ReminderData.class);
    Envelope<ReminderData> expected = new Envelope<>();
    expected.setContents(Collections.singletonList(reminderData));
    doReturn(expected).when(reminderManager).getReminderData(LocalDate.parse("1999-01-01"),
        LocalDate.parse("2000-01-01"), null,
        null, false, null, DEFAULT_PAGE_SIZE);
    when().get(getBaseUrl() + "/v1/materials/appointments?startDate=2000-01-01&endDate=1999-01-01")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(LocalDate.parse("1999-01-01"),
        LocalDate.parse("2000-01-01"), null,
        null, false, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointment() throws ProtossException {
    ReminderData expected = getFixture(ReminderData.class);
    doReturn(expected).when(reminderManager).getReminderData(TEST_ID);
    when().get(getBaseUrl() + "/v1/materials/appointments/100")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(TEST_ID);
  }

  @Test
  public void getAppointmentNotFound() throws ProtossException {
    doReturn(null).when(reminderManager).getReminderData(TEST_ID);
    when().get(getBaseUrl() + "/v1/materials/appointments/100")
        .then()
        .assertThat().statusCode(404);
  }

  @Test
  public void getAppointmentWithAlert() throws ProtossException {
    int id = TestUtilities.nextId();
    ReminderData expected = getFixture(ReminderData.class);

    AppointmentMaterialsDto expectedDto = mapDto(expected, AppointmentMaterialsDto.class);
    doReturn(expected).when(reminderManager).getReminderData(id);
    AppointmentMaterialsDto actualDto =
        given()
            .pathParam("id", id)
            .when()
            .get(getBaseUrl() + "/v1/materials/appointments/{id}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AppointmentMaterialsDto.class);
    Assert.assertEquals(expectedDto, actualDto);
    verify(reminderManager).getReminderData(id);
    Assert.assertNotNull(expected.getAlerts());
  }
}
