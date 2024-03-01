
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlManager;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegular;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryText;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrl;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PatientHistoryEndpointTest extends AbstractEndpointTest<PatientHistoryEndpoint> {

  private static final int PATIENT_ID = TestUtilities.nextId();
  private static final int ITEM_ID = TestUtilities.nextId();

  private final PatientHistoryRegularManager patientHistoryRegularManager;
  private final PatientHistoryTextManager patientHistoryTextManager;
  private final PatientHistoryUrlManager patientHistoryUrlManager;
  private final PatientHistoryTrackingManager patientHistoryTrackingManager;

  public PatientHistoryEndpointTest() {
    super(new PatientHistoryEndpoint(), PatientHistoryEndpoint.class);
    patientHistoryRegularManager = mock(PatientHistoryRegularManager.class);
    patientHistoryTextManager = mock(PatientHistoryTextManager.class);
    patientHistoryUrlManager = mock(PatientHistoryUrlManager.class);
    patientHistoryTrackingManager = mock(PatientHistoryTrackingManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> services = new HashMap<>();
    services.put(PatientHistoryRegularManager.class, patientHistoryRegularManager);
    services.put(PatientHistoryTextManager.class, patientHistoryTextManager);
    services.put(PatientHistoryUrlManager.class, patientHistoryUrlManager);
    services.put(PatientHistoryTrackingManager.class, patientHistoryTrackingManager);
    return services;
  }

  @Test
  public void testGetAll() throws Exception {

    List<PatientHistoryRegular> regularResults =
        getFixtures(PatientHistoryRegular.class, ArrayList::new, 5);
    regularResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });
    List<PatientHistoryText> textResults =
        getFixtures(PatientHistoryText.class, ArrayList::new, 5);
    textResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });
    List<PatientHistoryTracking> trackingResults =
        getFixtures(PatientHistoryTracking.class, ArrayList::new, 5);
    trackingResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });
    List<PatientHistoryUrl> urlResults =
        getFixtures(PatientHistoryUrl.class, ArrayList::new, 5);
    urlResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });

    // map to the expected shape
    List<PatientHistoryRegularDto> regularResultsDto =
        mapDto(regularResults, PatientHistoryRegularDto.class, ArrayList::new);
    List<PatientHistoryTextDto> textResultsDto =
        mapDto(textResults, PatientHistoryTextDto.class, ArrayList::new);
    List<PatientHistoryTrackingDto> trackingResultsDto =
        mapDto(trackingResults, PatientHistoryTrackingDto.class, ArrayList::new);
    List<PatientHistoryUrlDto> urlResultsDto =
        mapDto(urlResults, PatientHistoryUrlDto.class, ArrayList::new);

    List<AbstractPatientHistoryItemDto> resultsExpected = new ArrayList<>();
    resultsExpected.addAll(regularResultsDto);
    resultsExpected.addAll(textResultsDto);
    resultsExpected.addAll(trackingResultsDto);
    resultsExpected.addAll(urlResultsDto);

    // mock dependencies
    when(patientHistoryRegularManager.getForPatient(PATIENT_ID)).thenReturn(regularResults);
    when(patientHistoryTextManager.getForPatient(PATIENT_ID)).thenReturn(textResults);
    when(patientHistoryUrlManager.getForPatient(PATIENT_ID)).thenReturn(urlResults);
    when(patientHistoryTrackingManager.getForPatient(PATIENT_ID)).thenReturn(trackingResults);

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertEquals(resultsExpected, actual);
    verify(patientHistoryRegularManager).getForPatient(PATIENT_ID);
    verify(patientHistoryTextManager).getForPatient(PATIENT_ID);
    verify(patientHistoryUrlManager).getForPatient(PATIENT_ID);
    verify(patientHistoryTrackingManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void tetGetRegular() throws Exception {

    List<AbstractPatientHistoryItemDto> resultsExpected = new ArrayList<>();

    List<PatientHistoryRegular> regularResults =
        getFixtures(PatientHistoryRegular.class, ArrayList::new, 5);
    regularResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });

    // map to the expected shape
    List<PatientHistoryRegularDto> regularResultsDto =
        mapDto(regularResults, PatientHistoryRegularDto.class, ArrayList::new);

    resultsExpected.addAll(regularResultsDto);

    // mock dependencies
    when(patientHistoryRegularManager.getForPatient(PATIENT_ID)).thenReturn(regularResults);

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .queryParam("historyType", "REGULAR")
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertEquals(resultsExpected, actual);
    verify(patientHistoryRegularManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void tetGetText() throws Exception {

    List<AbstractPatientHistoryItemDto> resultsExpected = new ArrayList<>();

    List<PatientHistoryText> textResults =
        getFixtures(PatientHistoryText.class, ArrayList::new, 5);

    textResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });

    // map to the expected shape
    List<PatientHistoryTextDto> textResultsDto =
        mapDto(textResults, PatientHistoryTextDto.class, ArrayList::new);

    resultsExpected.addAll(textResultsDto);

    // mock dependencies
    when(patientHistoryTextManager.getForPatient(PATIENT_ID)).thenReturn(textResults);

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .queryParam("historyType", "FREE_TEXT")
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertEquals(resultsExpected, actual);
    verify(patientHistoryTextManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void tetGetUrl() throws Exception {

    List<AbstractPatientHistoryItemDto> resultsExpected = new ArrayList<>();

    List<PatientHistoryUrl> urlResults =
        getFixtures(PatientHistoryUrl.class, ArrayList::new, 5);

    urlResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });
    // map to the expected shape
    List<PatientHistoryUrlDto> urlResultsDto =
        mapDto(urlResults, PatientHistoryUrlDto.class, ArrayList::new);

    resultsExpected.addAll(urlResultsDto);

    // mock dependencies
    when(patientHistoryUrlManager.getForPatient(PATIENT_ID)).thenReturn(urlResults);

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .queryParam("historyType", "URL")
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertEquals(resultsExpected, actual);
    verify(patientHistoryUrlManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void tetGetTracking() throws Exception {

    List<AbstractPatientHistoryItemDto> resultsExpected = new ArrayList<>();

    List<PatientHistoryTracking> trackingResults =
        getFixtures(PatientHistoryTracking.class, ArrayList::new, 5);

    trackingResults.forEach(p -> {
      p.setPatientId(PATIENT_ID);
    });

    // map to the expected shape
    List<PatientHistoryTrackingDto> trackingResultsDto =
        mapDto(trackingResults, PatientHistoryTrackingDto.class, ArrayList::new);

    resultsExpected.addAll(trackingResultsDto);

    // mock dependencies
    when(patientHistoryTrackingManager.getForPatient(PATIENT_ID)).thenReturn(trackingResults);

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .queryParam("historyType", "TRACKING")
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertEquals(resultsExpected, actual);
    verify(patientHistoryTrackingManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void testGetPatientHistoryItemsEmpty() throws Exception {

    // mock dependencies
    when(patientHistoryRegularManager.getForPatient(PATIENT_ID)).thenReturn(new ArrayList<>());
    when(patientHistoryTextManager.getForPatient(PATIENT_ID)).thenReturn(new ArrayList<>());
    when(patientHistoryUrlManager.getForPatient(PATIENT_ID)).thenReturn(new ArrayList<>());
    when(patientHistoryTrackingManager.getForPatient(PATIENT_ID)).thenReturn(new ArrayList<>());

    List<AbstractPatientHistoryItemDto> actual = toCollection(
        given()
            .pathParam("patientId", PATIENT_ID)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/history-items")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto[].class),
        ArrayList::new);

    assertTrue(actual.isEmpty());
    verify(patientHistoryRegularManager).getForPatient(PATIENT_ID);
    verify(patientHistoryTextManager).getForPatient(PATIENT_ID);
    verify(patientHistoryUrlManager).getForPatient(PATIENT_ID);
    verify(patientHistoryTrackingManager).getForPatient(PATIENT_ID);
  }

  @Test
  public void tetGetTrackingById() throws Exception {

    PatientHistoryTracking trackingResult = getFixture(PatientHistoryTracking.class);
    trackingResult.setPatientId(PATIENT_ID);
    trackingResult.setId(ITEM_ID);

    // map to the expected shape
    PatientHistoryTrackingDto resultExpected =
        mapDto(trackingResult, PatientHistoryTrackingDto.class);

    // mock dependencies
    when(patientHistoryTrackingManager.getById(ITEM_ID)).thenReturn(trackingResult);

    AbstractPatientHistoryItemDto actual =
        given()
            .pathParam("patientId", PATIENT_ID)
            .pathParam("historyItemId", ITEM_ID)
            .queryParam("historyType", "TRACKING")
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto.class);

    assertEquals(resultExpected, actual);
    verify(patientHistoryTrackingManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetRegularById() throws Exception {

    PatientHistoryRegular regularResult = getFixture(PatientHistoryRegular.class);
    regularResult.setPatientId(PATIENT_ID);
    regularResult.setId(ITEM_ID);

    // map to the expected shape
    PatientHistoryRegularDto resultExpected =
        mapDto(regularResult, PatientHistoryRegularDto.class);

    // mock dependencies
    when(patientHistoryRegularManager.getById(ITEM_ID)).thenReturn(regularResult);

    AbstractPatientHistoryItemDto actual =
        given()
            .pathParam("patientId", PATIENT_ID)
            .pathParam("historyItemId", ITEM_ID)
            .queryParam("historyType", "REGULAR")
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto.class);

    assertEquals(resultExpected, actual);
    verify(patientHistoryRegularManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetTextById() throws Exception {

    PatientHistoryText textResult = getFixture(PatientHistoryText.class);
    textResult.setPatientId(PATIENT_ID);
    textResult.setHistoryTextId(ITEM_ID);

    // map to the expected shape
    PatientHistoryTextDto resultExpected =
        mapDto(textResult, PatientHistoryTextDto.class);

    // mock dependencies
    when(patientHistoryTextManager.getById(ITEM_ID)).thenReturn(textResult);

    AbstractPatientHistoryItemDto actual =
        given()
            .pathParam("patientId", PATIENT_ID)
            .pathParam("historyItemId", ITEM_ID)
            .queryParam("historyType", "FREE_TEXT")
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto.class);

    assertEquals(resultExpected, actual);
    verify(patientHistoryTextManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetUrlById() throws Exception {

    PatientHistoryUrl urlResult = getFixture(PatientHistoryUrl.class);
    urlResult.setPatientId(PATIENT_ID);
    urlResult.setId(ITEM_ID);

    // map to the expected shape
    PatientHistoryUrlDto resultExpected =
        mapDto(urlResult, PatientHistoryUrlDto.class);

    // mock dependencies
    when(patientHistoryUrlManager.getById(ITEM_ID)).thenReturn(urlResult);

    AbstractPatientHistoryItemDto actual =
        given()
            .pathParam("patientId", PATIENT_ID)
            .pathParam("historyItemId", ITEM_ID)
            .queryParam("historyType", "URL")
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AbstractPatientHistoryItemDto.class);

    assertEquals(resultExpected, actual);
    verify(patientHistoryUrlManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetPatientHistoryItemByIdNotFound() throws Exception {

    PatientHistoryUrl urlResult = getFixture(PatientHistoryUrl.class);
    urlResult.setPatientId(PATIENT_ID + 1);
    urlResult.setId(ITEM_ID);

    // mock dependencies
    when(patientHistoryUrlManager.getById(ITEM_ID)).thenReturn(urlResult);

    given()
        .pathParam("patientId", PATIENT_ID)
        .pathParam("historyItemId", ITEM_ID)
        .queryParam("historyType", "URL")
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(patientHistoryUrlManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetPatientHistoryItemByIdAsNullNotFound() throws Exception {

    // mock dependencies
    when(patientHistoryUrlManager.getById(ITEM_ID)).thenReturn(null);

    given()
        .pathParam("patientId", PATIENT_ID)
        .pathParam("historyItemId", ITEM_ID)
        .queryParam("historyType", "URL")
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(patientHistoryUrlManager).getById(ITEM_ID);
  }

  @Test
  public void tetGetPatientHistoryItemByIdWithNullHistoryType() throws Exception {
    given()
        .pathParam("patientId", PATIENT_ID)
        .pathParam("historyItemId", ITEM_ID)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/history-items/{historyItemId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(patientHistoryRegularManager, never()).getById(anyInt());
    verify(patientHistoryTextManager, never()).getById(anyInt());
    verify(patientHistoryTrackingManager, never()).getById(anyInt());
    verify(patientHistoryUrlManager, never()).getById(anyInt());
  }

}
