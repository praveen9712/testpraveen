
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultSelectionListManager;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientChartLockManager;
import com.qhrtech.emr.accuro.api.patient.PatientChartLockManager;
import com.qhrtech.emr.accuro.db.patient.PatientChartLockFixture;
import com.qhrtech.emr.accuro.db.patient.PatientFixture;
import com.qhrtech.emr.accuro.db.security.AccuroUserFixture;
import com.qhrtech.emr.accuro.db.security.roles.RoleFixture;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.patient.PatientChartLock;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PatientChartLockDto;
import com.qhrtech.emr.restapi.models.dto.PatientChartLockExceptionDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PatientChartLockEndpointIT
    extends AbstractEndpointIntegrationTest<PatientChartLockEndpoint> {

  private final PatientChartLockManager chartLockManager;
  private final String endpointUrl = "/v1/provider-portal/patients/";
  private Integer userId;
  private Integer providerId;
  private String selectionListName = "ChartUnlockReason";

  public PatientChartLockEndpointIT()
      throws IOException, SQLException, UnsupportedSchemaVersionException {
    super(new PatientChartLockEndpoint(), PatientChartLockEndpoint.class);
    setUpAndGetAccuroUserDetails();
    this.chartLockManager = new DefaultPatientChartLockManager(ds, null, defaultUser(userId));
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
      throws UnsupportedSchemaVersionException, SQLException {

    PatientChartLockFixture fixture = new PatientChartLockFixture();
    fixture.setUp(getConnection());
    PatientChartLock protossResult = fixture.get();

    int patientId = protossResult.getPatientId();

    PatientChartLockDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-lock")
        .then()
        .assertThat().statusCode(Status.OK.getStatusCode())
        .extract().as(PatientChartLockDto.class);

    assertTrue(protossResult.getExceptions().size() == actual.getExceptions().size());

    PatientChartLockDto chartLockDto = setUpDto(protossResult, actual);
    assertEquals(actual, chartLockDto);

  }


  @Test
  public void testCreateLock()
      throws UnsupportedSchemaVersionException,
      DatabaseInteractionException,
      SQLException {
    PatientChartLockDto chartLockDto = setUpDataForCreate(true);

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

    PatientChartLock protossLock = chartLockManager.getByPatientId(patientId);

    assertEquals(protossLock.getExceptions().size(), chartLockDto.getExceptions().size());

  }

  @Test
  public void testCreateLockNoExceptions() throws UnsupportedSchemaVersionException,
      DatabaseInteractionException, SQLException {
    PatientChartLockDto chartLockDto = setUpDataForCreate(false);

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

    PatientChartLock protossLock = chartLockManager.getByPatientId(patientId);

    assertTrue(protossLock != null);

  }


  @Test
  public void testPatientChartUnlock()
      throws UnsupportedSchemaVersionException, SQLException, DatabaseInteractionException,
      ResourceConflictException, ForbiddenException, TimeZoneNotFoundException,
      SupportingResourceNotFoundException {

    PatientChartLockDto chartLockDto = setUpDataForCreate(false);

    int patientId = chartLockDto.getPatientId();
    PatientChartLock chartLock = mapDto(chartLockDto, PatientChartLock.class);
    chartLockManager.create(chartLock);

    // ChartUnlockReason
    SelectionListManager manager = new DefaultSelectionListManager(ds);
    String reason = TestUtilities.nextString(10);
    manager.setSelectionList(selectionListName, Collections.singletonList(reason));


    given()
        .when()
        .pathParam("patientId", patientId)
        .queryParam("unlockReason", reason)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl
            + "{patientId}/patient-chart-unlock")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    PatientChartLock protossLock = chartLockManager.getByPatientId(patientId);

    assertTrue(protossLock == null);


  }

  private PatientChartLockDto setUpDataForCreate(boolean hasExceptions)
      throws SQLException, UnsupportedSchemaVersionException {
    PatientChartLockDto lock = new PatientChartLockDto();
    if (hasExceptions) {
      lock.setAllProviders(false);
      PatientChartLockExceptionDto exceptionDto = new PatientChartLockExceptionDto();
      exceptionDto.setUserId(userId);
      lock.setExceptions(Collections.singletonList(exceptionDto));
    } else {
      lock.setAllProviders(true);
    }

    PatientFixture patientFixture = new PatientFixture();
    patientFixture.setUp(getConnection());

    lock.setPatientId(patientFixture.get().getPatientId());
    lock.setPhysicianId(providerId);

    return lock;

  }


  // setting ids which fixture not set
  private PatientChartLockDto setUpDto(PatientChartLock protossLock,
      PatientChartLockDto chartLockDto) {

    PatientChartLockDto patientChartLockDto = mapDto(protossLock, PatientChartLockDto.class);
    patientChartLockDto.setPatientChartLockId(chartLockDto.getPatientChartLockId());

    for (int i = 0; i < protossLock.getExceptions().size(); i++) {

      patientChartLockDto.getExceptions().get(i)
          .setExceptionId(chartLockDto.getExceptions().get(i).getExceptionId());
    }
    return patientChartLockDto;

  }

  private void setUpAndGetAccuroUserDetails()
      throws SQLException, UnsupportedSchemaVersionException {
    AccuroUserFixture accuroUserFixture = new AccuroUserFixture();

    accuroUserFixture.setUp(getConnection());

    AccuroUser user = accuroUserFixture.get();
    userId = user.getUserId();
    providerId = user.getProviderId();

    // Set the role for the newly created user
    RoleFixture roleFixture = new RoleFixture();
    roleFixture.setUserId(userId);
    roleFixture.setUp(getConnection());
  }


}
