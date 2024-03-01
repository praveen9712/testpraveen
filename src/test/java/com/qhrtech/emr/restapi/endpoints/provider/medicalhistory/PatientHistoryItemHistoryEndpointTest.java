
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlHistoryManager;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegularHistory;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTextHistory;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrlHistory;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AbstractPatientHistoryItemHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryRegularHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTextHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryTrackingHistoryDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientHistoryUrlHistoryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PatientHistoryItemHistoryEndpointTest extends
    AbstractEndpointTest<PatientHistoryItemHistoryEndpoint> {
  private final PatientHistoryRegularHistoryManager regularHistoryManager;
  private final PatientHistoryTrackingManager trackingHistoryManager;
  private final PatientHistoryUrlHistoryManager urlHistoryManager;
  private final PatientHistoryTextHistoryManager textHistoryManager;


  public PatientHistoryItemHistoryEndpointTest() {
    super(new PatientHistoryItemHistoryEndpoint(), PatientHistoryItemHistoryEndpoint.class);
    regularHistoryManager = mock(PatientHistoryRegularHistoryManager.class);
    trackingHistoryManager = mock(PatientHistoryTrackingManager.class);
    urlHistoryManager = mock(PatientHistoryUrlHistoryManager.class);
    textHistoryManager = mock(PatientHistoryTextHistoryManager.class);
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
    servicesMap.put(PatientHistoryRegularHistoryManager.class, regularHistoryManager);
    servicesMap.put(PatientHistoryTrackingManager.class, trackingHistoryManager);
    servicesMap.put(PatientHistoryUrlHistoryManager.class, urlHistoryManager);
    servicesMap.put(PatientHistoryTextHistoryManager.class, textHistoryManager);
    return servicesMap;
  }

  @Test
  public void getRegularHistoryById() throws Exception {
    PatientHistoryRegularHistory protossResult = getFixture(PatientHistoryRegularHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = protossResult.getPatientHistoryRegularId();
    int historyId = protossResult.getId();
    String historyType = "REGULAR";

    AbstractPatientHistoryItemHistoryDto expected =
        mapDto(protossResult, PatientHistoryRegularHistoryDto.class);

    when(regularHistoryManager.getById(historyId)).thenReturn(protossResult);

    AbstractPatientHistoryItemHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .pathParam("historyId", historyId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto.class);

    assertEquals(expected, actual);
    verify(regularHistoryManager).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getRegularHistoryByIdWithUnmatchedHistoryItemId() throws Exception {
    PatientHistoryRegularHistory protossResult = getFixture(PatientHistoryRegularHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = TestUtilities.nextId();
    int historyId = protossResult.getId();
    String historyType = "REGULAR";

    when(regularHistoryManager.getById(historyId)).thenReturn(protossResult);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(regularHistoryManager).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getTrackingHistoryById() throws Exception {
    PatientHistoryTracking protossResult = getFixture(PatientHistoryTracking.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = protossResult.getHistoryTrackingItem().getId();
    int historyId = protossResult.getId();
    String historyType = "TRACKING";

    AbstractPatientHistoryItemHistoryDto expected =
        mapDto(protossResult, PatientHistoryTrackingHistoryDto.class);

    when(trackingHistoryManager.getHistoryById(historyId)).thenReturn(protossResult);

    AbstractPatientHistoryItemHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .pathParam("historyId", historyId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto.class);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getTrackingHistoryByIdWithUnmatchedHistoryItemId() throws Exception {
    PatientHistoryTracking protossResult = getFixture(PatientHistoryTracking.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = TestUtilities.nextId();
    int historyId = protossResult.getId();
    String historyType = "TRACKING";

    when(trackingHistoryManager.getHistoryById(historyId)).thenReturn(protossResult);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getUrlHistoryById() throws Exception {
    PatientHistoryUrlHistory protossResult = getFixture(PatientHistoryUrlHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = protossResult.getPatientHistoryUrlId();
    int historyId = protossResult.getId();
    String historyType = "URL";

    AbstractPatientHistoryItemHistoryDto expected =
        mapDto(protossResult, PatientHistoryUrlHistoryDto.class);

    when(urlHistoryManager.getById(historyId)).thenReturn(protossResult);

    AbstractPatientHistoryItemHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .pathParam("historyId", historyId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto.class);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getUrlHistoryByIdWithUnmatchedHistoryItemId() throws Exception {
    PatientHistoryUrlHistory protossResult = getFixture(PatientHistoryUrlHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = TestUtilities.nextId();
    int historyId = protossResult.getId();
    String historyType = "URL";

    when(urlHistoryManager.getById(historyId)).thenReturn(protossResult);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getTextHistoryById() throws Exception {
    PatientHistoryTextHistory protossResult = getFixture(PatientHistoryTextHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = protossResult.getPatientHistoryTextId();
    int historyId = protossResult.getId();
    String historyType = "FREE_TEXT";

    AbstractPatientHistoryItemHistoryDto expected =
        mapDto(protossResult, PatientHistoryTextHistoryDto.class);

    when(textHistoryManager.getById(historyId)).thenReturn(protossResult);

    AbstractPatientHistoryItemHistoryDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .pathParam("historyId", historyId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto.class);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager).getById(historyId);
  }

  @Test
  public void getTextHistoryByIdWithUnmatchedHistoryItemId() throws Exception {
    PatientHistoryTextHistory protossResult = getFixture(PatientHistoryTextHistory.class);
    int patientId = protossResult.getPatientId();
    int historyItemId = TestUtilities.nextId();
    int historyId = protossResult.getId();
    String historyType = "FREE_TEXT";

    when(textHistoryManager.getById(historyId)).thenReturn(protossResult);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager).getById(historyId);
  }

  @Test
  public void getByHistoryIdWithNullHistoryType() throws Exception {
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    int historyId = TestUtilities.nextId();

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getByHistoryIdWithInvalidHistoryType() throws Exception {
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    int historyId = TestUtilities.nextId();
    String historyType = TestUtilities.nextString(20);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(400);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getByHistoryIdWithUnmatchedPatientId() throws Exception {
    PatientHistoryRegularHistory protossResult = getFixture(PatientHistoryRegularHistory.class);
    int patientId = TestUtilities.nextId();
    int historyItemId = protossResult.getPatientHistoryRegularId();
    int historyId = protossResult.getId();
    String historyType = "REGULAR";

    when(regularHistoryManager.getById(historyId)).thenReturn(protossResult);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .pathParam("historyId", historyId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories/{historyId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(regularHistoryManager).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getForPatientHistoryRegular() throws Exception {
    List<PatientHistoryRegularHistory> protossResult =
        getFixtures(PatientHistoryRegularHistory.class, ArrayList::new, 10);
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    String historyType = "REGULAR";

    List<AbstractPatientHistoryItemHistoryDto> expected = new ArrayList<>();
    protossResult.forEach(p -> {
      p.setPatientId(patientId);
      p.setPatientHistoryRegularId(historyItemId);
      expected.add(mapDto(p, PatientHistoryRegularHistoryDto.class));
    });

    when(regularHistoryManager.getByHistoryItemId(historyItemId)).thenReturn(protossResult);

    List<AbstractPatientHistoryItemHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(regularHistoryManager).getByHistoryItemId(historyItemId);
    verify(trackingHistoryManager, never()).getHistoryByPatientIdAndTrackingItemId(patientId,
        historyItemId);
    verify(urlHistoryManager, never()).getForPatientHistoryUrl(historyItemId);
    verify(textHistoryManager, never()).getForPatientHistoryText(historyItemId);
  }

  @Test
  public void getForPatientHistoryTracking() throws Exception {
    List<PatientHistoryTracking> protossResult =
        getFixtures(PatientHistoryTracking.class, ArrayList::new, 10);
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    String historyType = "TRACKING";

    List<AbstractPatientHistoryItemHistoryDto> expected = new ArrayList<>();
    protossResult.forEach(p -> {
      p.setPatientId(patientId);
      p.getHistoryTrackingItem().setId(historyItemId);
      expected.add(mapDto(p, PatientHistoryTrackingHistoryDto.class));
    });

    when(trackingHistoryManager.getHistoryByPatientIdAndTrackingItemId(patientId, historyItemId))
        .thenReturn(protossResult);

    List<AbstractPatientHistoryItemHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getByHistoryItemId(historyItemId);
    verify(trackingHistoryManager).getHistoryByPatientIdAndTrackingItemId(patientId, historyItemId);
    verify(urlHistoryManager, never()).getForPatientHistoryUrl(historyItemId);
    verify(textHistoryManager, never()).getForPatientHistoryText(historyItemId);
  }

  @Test
  public void getForPatientHistoryUrl() throws Exception {
    List<PatientHistoryUrlHistory> protossResult =
        getFixtures(PatientHistoryUrlHistory.class, ArrayList::new, 10);
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    String historyType = "URL";

    List<AbstractPatientHistoryItemHistoryDto> expected = new ArrayList<>();
    protossResult.forEach(p -> {
      p.setPatientId(patientId);
      p.setPatientHistoryUrlId(historyItemId);
      expected.add(mapDto(p, PatientHistoryUrlHistoryDto.class));
    });

    when(urlHistoryManager.getForPatientHistoryUrl(historyItemId)).thenReturn(protossResult);

    List<AbstractPatientHistoryItemHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getByHistoryItemId(historyItemId);
    verify(trackingHistoryManager, never()).getHistoryByPatientIdAndTrackingItemId(patientId,
        historyItemId);
    verify(urlHistoryManager).getForPatientHistoryUrl(historyItemId);
    verify(textHistoryManager, never()).getForPatientHistoryText(historyItemId);
  }

  @Test
  public void getForPatientHistoryText() throws Exception {
    List<PatientHistoryTextHistory> protossResult =
        getFixtures(PatientHistoryTextHistory.class, ArrayList::new, 10);
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    String historyType = "FREE_TEXT";

    List<AbstractPatientHistoryItemHistoryDto> expected = new ArrayList<>();
    protossResult.forEach(p -> {
      p.setPatientId(patientId);
      p.setPatientHistoryTextId(historyItemId);
      expected.add(mapDto(p, PatientHistoryTextHistoryDto.class));
    });

    when(textHistoryManager.getForPatientHistoryText(historyItemId)).thenReturn(protossResult);

    List<AbstractPatientHistoryItemHistoryDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("historyItemId", historyItemId)
            .queryParam("historyType", historyType)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/"
                + "{patientId}/history-items/{historyItemId}/histories")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AbstractPatientHistoryItemHistoryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(regularHistoryManager, never()).getByHistoryItemId(historyItemId);
    verify(trackingHistoryManager, never()).getHistoryByPatientIdAndTrackingItemId(patientId,
        historyItemId);
    verify(urlHistoryManager, never()).getForPatientHistoryUrl(historyItemId);
    verify(textHistoryManager).getForPatientHistoryText(historyItemId);
  }

  @Test
  public void getForPatientHistoryItemWithNullHistoryType() throws Exception {
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    int historyId = TestUtilities.nextId();

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories")
        .then()
        .assertThat()
        .statusCode(400);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }

  @Test
  public void getForPatientHistoryItemWithInvalidHistoryType() throws Exception {
    int patientId = TestUtilities.nextId();
    int historyItemId = TestUtilities.nextId();
    int historyId = TestUtilities.nextId();
    String historyType = TestUtilities.nextString(20);

    given()
        .pathParam("patientId", patientId)
        .pathParam("historyItemId", historyItemId)
        .queryParam("historyType", historyType)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/"
            + "{patientId}/history-items/{historyItemId}/histories")
        .then()
        .assertThat()
        .statusCode(400);

    verify(regularHistoryManager, never()).getById(historyId);
    verify(trackingHistoryManager, never()).getHistoryById(historyId);
    verify(urlHistoryManager, never()).getById(historyId);
    verify(textHistoryManager, never()).getById(historyId);
  }
}
