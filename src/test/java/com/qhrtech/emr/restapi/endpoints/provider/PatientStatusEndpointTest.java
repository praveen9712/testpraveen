
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.patient.PatientStatusManager;
import com.qhrtech.emr.accuro.model.patient.PatientStatus;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.PatientStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class PatientStatusEndpointTest extends AbstractEndpointTest<PatientStatusEndpoint> {
  private final PatientStatusManager patientStatusManager;
  ApiSecurityContext context;

  public PatientStatusEndpointTest() {
    super(new PatientStatusEndpoint(), PatientStatusEndpoint.class);
    patientStatusManager = mock(PatientStatusManager.class);

    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(PatientStatusManager.class, patientStatusManager);
    servicesMap.put(PatientStatusManager.class, patientStatusManager);
    return servicesMap;
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Test
  public void testGetById() throws Exception {
    // setup random data
    PatientStatus expected = getFixture(PatientStatus.class);
    int id = RandomUtils.nextInt();
    expected.setStatusId(id);
    // mock dependencies
    when(patientStatusManager.getByStatusId(id)).thenReturn(expected);

    PatientStatusDto expectedDto = mapDto(expected, PatientStatusDto.class);

    // test
    PatientStatusDto actual =
        given()
            .pathParam("id", id)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patient-statuses/{id}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientStatusDto.class);
    // assertions
    assertEquals(expectedDto, actual);
    verify(patientStatusManager).getByStatusId(id);
  }

  @Test
  public void testGetAll() throws Exception {
    // setup random data
    List<PatientStatus> protossStatuses = getFixtures(PatientStatus.class, ArrayList::new, 5);

    List<PatientStatusDto> expected =
        mapDto(protossStatuses, PatientStatusDto.class, ArrayList::new);


    // mock dependencies
    when(patientStatusManager.getAll())
        .thenReturn(protossStatuses);
    // test
    List<PatientStatusDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patient-statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientStatusDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(patientStatusManager).getAll();
  }

  @Test
  public void testCreate() throws Exception {
    // setup random data
    PatientStatusDto patientStatusDto = getFixture(PatientStatusDto.class);

    Integer expectedId = RandomUtils.nextInt();
    // mock dependencies
    PatientStatus patientStatus = mapDto(patientStatusDto, PatientStatus.class);
    patientStatus.setStatusColor(0);
    patientStatus.setCoreStatus(true);
    when(patientStatusManager.create(patientStatus))
        .thenReturn(expectedId);
    // test
    Integer actualId =
        given()
            .body(patientStatusDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patient-statuses")
            .then()
            .assertThat()
            .statusCode(201)
            .extract().as(Integer.class);
    // assertions
    assertEquals(actualId, expectedId);
    verify(patientStatusManager).create(patientStatus);
  }

  @Test
  public void testCreateNullDto() throws Exception {
    given()
        .body("")
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patient-statuses")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientStatusManager, times(0)).create(null);
  }

  @Test
  public void testUpdate() throws Exception {
    // setup random data
    PatientStatusDto patientStatusDto = getFixture(PatientStatusDto.class);
    int id = patientStatusDto.getStatusId();

    PatientStatus patientStatus = mapDto(patientStatusDto, PatientStatus.class);

    given()
        .pathParam("id", id)
        .body(patientStatusDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patient-statuses/{id}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(patientStatusManager).update(patientStatus);
  }

  @Test
  public void testUpdateMatchingId() throws Exception {
    // setup random data
    PatientStatusDto patientStatusDto = getFixture(PatientStatusDto.class);
    int id = patientStatusDto.getStatusId();

    PatientStatus patientStatus = mapDto(patientStatusDto, PatientStatus.class);

    patientStatus.setStatusColor(0);
    patientStatus.setCoreStatus(true);

    given()
        .pathParam("id", id + 1)
        .body(patientStatusDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patient-statuses/{id}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientStatusManager, times(0)).update(patientStatus);
  }

  @Test
  public void testUpdateNullDto() throws Exception {

    given()
        .pathParam("id", 1)
        .body("")
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patient-statuses/{id}")
        .then()
        .assertThat()
        .statusCode(400);
    // assertions
    verify(patientStatusManager, times(0)).update(null);
  }


  @Test
  public void testDelete() throws Exception {
    given()
        .pathParam("id", 1)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patient-statuses/{id}")
        .then()
        .assertThat()
        .statusCode(204);
    // assertions
    verify(patientStatusManager).delete(1);
  }

}
