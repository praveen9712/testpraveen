
package com.qhrtech.emr.restapi.endpoints.materials.v2;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentReminderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.scheduling.ReminderData;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AppointmentMaterialsDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.junit.Test;

public class AppointmentMaterialsEndpointV2Test
    extends AbstractEndpointTest<AppointmentMaterialsEndpointV2> {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;
  private final AppointmentReminderManager reminderManager;

  public AppointmentMaterialsEndpointV2Test() {
    super(new AppointmentMaterialsEndpointV2(), AppointmentMaterialsEndpointV2.class);
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
    given().when().get(getBaseUrl() + "/v2/materials/appointments")
        .then()
        .assertThat().statusCode(400);
  }

  @Test
  public void getAppointments() throws ProtossException {
    // set up data
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setCount(10);
    protossEnvelope.setTotal(10);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossResults);
    String date = "2000-01-01";

    // mocks
    when(reminderManager.getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, null, null, DEFAULT_PAGE_SIZE)).thenReturn(protossEnvelope);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    EnvelopeDto rawActual = given()
        .queryParam("startDate", date)
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments")
        .then()
        .assertThat()
        .statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, null, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void getAppointmentByAccessionNumber() throws ProtossException {
    // set up data
    List<ReminderData> protossResults = Collections.singletonList(getFixture(ReminderData.class));
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    long lastId = protossResults.get(0).getDbRowVersion();
    protossEnvelope.setCount(1);
    protossEnvelope.setTotal(1);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossResults);
    String date = "2000-01-01";
    String accessionNumber = protossResults.get(0).getAccessionNumber();

    // mocks
    when(reminderManager.getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, accessionNumber, null, DEFAULT_PAGE_SIZE)).thenReturn(protossEnvelope);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    EnvelopeDto rawActual = given()
        .queryParam("startDate", date)
        .queryParam("accessionNumber", accessionNumber)
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments")
        .then()
        .assertThat()
        .statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, accessionNumber, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void getAppointmentsWithBillonly() throws ProtossException {
    // set up data
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setCount(10);
    protossEnvelope.setTotal(10);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossResults);
    String date = "2000-01-01";

    // mocks
    when(reminderManager.getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        true, false, null, null, DEFAULT_PAGE_SIZE)).thenReturn(protossEnvelope);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    EnvelopeDto rawActual = given()
        .queryParam("startDate", date)
        .queryParam("includeBillOnly", true)
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments")
        .then()
        .assertThat()
        .statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        true, false, null, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void getAppointmentsWithBillonlyFalse() throws ProtossException {
    // set up data
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setCount(10);
    protossEnvelope.setTotal(10);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossResults);
    String date = "2000-01-01";

    // mocks
    when(reminderManager.getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, null, null, DEFAULT_PAGE_SIZE)).thenReturn(protossEnvelope);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    EnvelopeDto rawActual = given()
        .queryParam("startDate", date)
        .queryParam("includeBillOnly", false)
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments")
        .then()
        .assertThat()
        .statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(date), null, null,
        Collections.emptyList(), true,
        false, false, null, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void getAppointmentsWithInvalidPageSize() throws ProtossException {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 1);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(1);
    protossEnvelope.setCount(1);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    String startdate = "2000-01-01";
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        null,
        null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);

    int pageSize = TestUtilities.nextElement(Arrays.asList(-1, 0));
    EnvelopeDto rawActual =
        given()
            .queryParam("startDate", startdate)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse("2000-01-01"), null, null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsWithNullPageSize() throws ProtossException {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(10);
    protossEnvelope.setCount(10);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    String startdate = "2000-01-01";
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        null,
        null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .queryParam("startDate", startdate)
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse("2000-01-01"), null, null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsDateSwap() throws Exception {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(10);
    protossEnvelope.setCount(10);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    String startdate = "2000-01-01";
    String endDate = "2010-01-01";
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        LocalDate.parse(endDate),
        null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            // passing the date params in reverse order to test the swap logic
            .queryParam("startDate", endDate)
            .queryParam("endDate", startdate)
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(startdate),
        LocalDate.parse(endDate), null,
        Collections.emptyList(), true, false, false, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsMultipleOffices() throws Exception {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(10);
    protossEnvelope.setCount(10);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    String startdate = "2000-01-01";
    String endDate = "2018-01-01";
    List<Integer> officeIds = Arrays.asList(TestUtilities.nextId(), TestUtilities.nextId());
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        LocalDate.parse(endDate),
        null,
        officeIds, true, false, false, null, null, DEFAULT_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .queryParam("startDate", startdate)
            .queryParam("endDate", endDate)
            .queryParam("officeId", officeIds.get(0))
            .queryParam("officeId", officeIds.get(1))
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(startdate),
        LocalDate.parse(endDate), null,
        officeIds, true, false, false, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void getAppointmentsAllParameters() throws Exception {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(10);
    protossEnvelope.setCount(10);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    long startingId = TestUtilities.nextLong();
    int providerId = TestUtilities.nextId();
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;

    String startdate = "2000-01-01";
    String endDate = "2018-01-01";
    List<Integer> officeIds = Arrays.asList(TestUtilities.nextId(), TestUtilities.nextId());
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        LocalDate.parse(endDate),
        providerId,
        officeIds,
        false,
        false, false, null,
        startingId,
        pageSize);

    EnvelopeDto rawActual =
        given()
            .queryParam("startDate", startdate)
            .queryParam("endDate", endDate)
            .queryParam("officeId", officeIds.get(0))
            .queryParam("officeId", officeIds.get(1))
            .queryParam("includeNullOffice", false)
            .queryParam("startingId", startingId)
            .queryParam("providerId", providerId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(startdate),
        LocalDate.parse(endDate), providerId,
        officeIds, false, false, false, null, startingId, pageSize);
  }

  @Test
  public void getAppointmentsAllParametersWithMaxPageSize() throws Exception {
    List<ReminderData> protossResults = getFixtures(ReminderData.class, ArrayList::new, 10);
    Envelope<ReminderData> protossEnvelope = new Envelope<>();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setTotal(10);
    protossEnvelope.setCount(10);
    long lastId = protossResults.stream().max(Comparator.comparing(ReminderData::getDbRowVersion))
        .orElseThrow(RuntimeException::new).getDbRowVersion();
    protossEnvelope.setLastId(lastId);

    EnvelopeDto<AppointmentMaterialsDto> expected = new EnvelopeDto<>();
    List<AppointmentMaterialsDto> expectedDto =
        mapDto(protossResults, AppointmentMaterialsDto.class, ArrayList::new);
    expected.setContents(expectedDto);
    expected.setTotal(protossResults.size());
    expected.setCount(protossResults.size());
    expected.setLastId(lastId);

    long startingId = TestUtilities.nextLong();
    int providerId = TestUtilities.nextId();
    int pageSize = 60;

    String startdate = "2000-01-01";
    String endDate = "2018-01-01";
    List<Integer> officeIds = Arrays.asList(TestUtilities.nextId(), TestUtilities.nextId());
    doReturn(protossEnvelope).when(reminderManager).getAppointmentMaterials(
        LocalDate.parse(startdate),
        LocalDate.parse(endDate),
        providerId,
        officeIds,
        false,
        false, false, null,
        startingId,
        MAX_PAGE_SIZE);

    EnvelopeDto rawActual =
        given()
            .queryParam("startDate", startdate)
            .queryParam("endDate", endDate)
            .queryParam("officeId", officeIds.get(0))
            .queryParam("officeId", officeIds.get(1))
            .queryParam("includeNullOffice", false)
            .queryParam("startingId", startingId)
            .queryParam("providerId", providerId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v2/materials/appointments")
            .then()
            .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    EnvelopeDto<AppointmentMaterialsDto> actual = reType(rawActual);

    assertEquals(expected, actual);
    verify(reminderManager).getAppointmentMaterials(LocalDate.parse(startdate),
        LocalDate.parse(endDate), providerId,
        officeIds, false, false, false, null, startingId, MAX_PAGE_SIZE);
  }

  private EnvelopeDto<AppointmentMaterialsDto> reType(EnvelopeDto rawEnvelope) {
    List<AppointmentMaterialsDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<AppointmentMaterialsDto>>() {});
    EnvelopeDto<AppointmentMaterialsDto> e = (EnvelopeDto<AppointmentMaterialsDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  @Test
  public void getAppointment() throws ProtossException {

    ReminderData expected = getFixture(ReminderData.class);
    doReturn(expected).when(reminderManager).getReminderData(expected.getAppointmentId());
    RestAssured
        .given()
        .pathParam("id", expected.getAppointmentId())
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments/{id}")
        .then()
        .assertThat().statusCode(200);
    verify(reminderManager).getReminderData(expected.getAppointmentId());
  }

  @Test
  public void getAppointmentNotFound() throws ProtossException {
    int id = TestUtilities.nextInt();
    doReturn(null).when(reminderManager).getReminderData(id);
    RestAssured
        .given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v2/materials/appointments/{id}")
        .then()
        .assertThat().statusCode(404);
    verify(reminderManager).getReminderData(id);
  }

}
