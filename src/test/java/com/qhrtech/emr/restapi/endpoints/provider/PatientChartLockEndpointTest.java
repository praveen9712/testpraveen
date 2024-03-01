
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.patient.PatientChartLockManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.patient.PatientChartLock;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PatientChartLockDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PatientChartLockEndpointTest extends AbstractEndpointTest<PatientChartLockEndpoint> {

  private final PatientChartLockManager chartLockManager;
  private final String endpointUrl = "/v1/provider-portal/patients/";

  public PatientChartLockEndpointTest() {
    super(new PatientChartLockEndpoint(), PatientChartLockEndpoint.class);
    chartLockManager = mock(PatientChartLockManager.class);
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
    servicesMap.put(PatientChartLockManager.class, chartLockManager);
    return servicesMap;
  }

  @Test
  public void testGetByPatientId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    PatientChartLockDto expected = getFixture(PatientChartLockDto.class);

    PatientChartLock protossResult = mapDto(expected, PatientChartLock.class);
    int patientId = expected.getPatientId();
    when(chartLockManager.getByPatientId(patientId)).thenReturn(protossResult);

    PatientChartLockDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.OK.getStatusCode())
        .extract().as(PatientChartLockDto.class);

    verify(chartLockManager).getByPatientId(patientId);
    assertEquals(expected, actual);

  }


  @Test
  public void testCreateLock()
      throws ResourceConflictException, ForbiddenException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, TimeZoneNotFoundException, SupportingResourceNotFoundException {
    PatientChartLockDto chartLockDto = getFixture(PatientChartLockDto.class);

    int patientId = chartLockDto.getPatientId();

    given()
        .when()
        .pathParam("patientId", patientId)
        .body(chartLockDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.CREATED.getStatusCode());

    PatientChartLock chartLock = mapDto(chartLockDto, PatientChartLock.class);

    verify(chartLockManager).create(chartLock);

  }

  @Test
  public void testCreateLockWithNullException() {
    PatientChartLockDto chartLockDto = getFixture(PatientChartLockDto.class);

    int patientId = chartLockDto.getPatientId();
    chartLockDto.getExceptions().add(null);

    given()
        .when()
        .pathParam("patientId", patientId)
        .body(chartLockDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.CREATED.getStatusCode());

  }

  @Test
  public void testCreateLockPatientIdDiff() {
    PatientChartLockDto chartLockDto = getFixture(PatientChartLockDto.class);

    int patientId = TestUtilities.nextInt();

    given()
        .when()
        .pathParam("patientId", patientId)
        .body(chartLockDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(chartLockManager);

  }

  @Test
  public void testCreateLockNoBody() {
    PatientChartLockDto chartLockDto = getFixture(PatientChartLockDto.class);

    int patientId = chartLockDto.getPatientId();

    given()
        .when()
        .pathParam("patientId", patientId)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(chartLockManager);

  }

  @Test
  public void testPatientChartUnlock()
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, SupportingResourceNotFoundException, NoDataFoundException {

    int patientId = TestUtilities.nextInt();
    String reason = TestUtilities.nextString(10);

    given()
        .when()
        .pathParam("patientId", patientId)
        .queryParam("unlockReason", reason)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-unlock")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(chartLockManager).deleteByPatientId(patientId, reason);


  }

  @Test
  public void testPatientChartUnlockNoReason() {

    int patientId = TestUtilities.nextInt();
    String reason = getNullOrBlank();

    given()
        .when()
        .pathParam("patientId", patientId)
        .queryParam("unlockReason", reason)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-unlock")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(chartLockManager);


  }

  @Test
  public void testCreateLockExistingError() throws DatabaseInteractionException,
      UnsupportedSchemaVersionException {
    PatientChartLockDto chartLockDto = getFixture(PatientChartLockDto.class);

    int patientId = chartLockDto.getPatientId();

    PatientChartLock protossResult = mapDto(chartLockDto, PatientChartLock.class);
    when(chartLockManager.getByPatientId(patientId)).thenReturn(protossResult);

    given()
        .when()
        .pathParam("patientId", patientId)
        .body(chartLockDto)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  private String getNullOrBlank() {
    String[] arr = {null, ""};
    Random r = new Random();
    int randomIndex = r.nextInt(arr.length);
    return arr[randomIndex];
  }


}
