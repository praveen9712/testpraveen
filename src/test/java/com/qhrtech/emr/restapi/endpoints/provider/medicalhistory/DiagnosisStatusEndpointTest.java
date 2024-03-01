
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class DiagnosisStatusEndpointTest extends AbstractEndpointTest<DiagnosisStatusEndpoint> {

  private final DiagnosisStatusManager diagnosisStatusManager;

  public DiagnosisStatusEndpointTest() {
    super(new DiagnosisStatusEndpoint(), DiagnosisStatusEndpoint.class);
    diagnosisStatusManager = mock(DiagnosisStatusManager.class);
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
    servicesMap.put(DiagnosisStatusManager.class, diagnosisStatusManager);
    return servicesMap;
  }

  @Test
  public void testGetAllDiagnosisStatuses() throws Exception {

    // setup random data
    Set<DiagnosisStatus> protossResults =
        getFixtures(DiagnosisStatus.class, HashSet::new, 5);

    // mock dependencies
    when(diagnosisStatusManager.getAll()).thenReturn(protossResults);

    Set<DiagnosisStatusDto> expected =
        mapDto(protossResults, DiagnosisStatusDto.class, HashSet::new);

    // test
    Set<DiagnosisStatusDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/diagnosis-statuses")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DiagnosisStatusDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(diagnosisStatusManager).getAll();
  }

  @Test
  public void testGetDiagnosisStatus() throws Exception {

    // setup random data
    Set<DiagnosisStatus> protossResults =
        getFixtures(DiagnosisStatus.class, HashSet::new, 5);

    Set<DiagnosisStatusDto> expected =
        mapDto(protossResults, DiagnosisStatusDto.class, HashSet::new);

    // mock dependencies
    when(diagnosisStatusManager.getAll()).thenReturn(protossResults);

    int statusId = expected.iterator().next().getStatusId();

    // test
    DiagnosisStatusDto actual =
        given().pathParam("statusId", statusId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/diagnosis-statuses/{statusId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DiagnosisStatusDto.class);

    // assertions
    assertTrue(expected.contains(actual));
    assertEquals(statusId, actual.getStatusId());
    verify(diagnosisStatusManager).getAll();
  }

  @Test
  public void testGetDiagnosisStatusNotFound() throws Exception {

    // setup random data
    Set<DiagnosisStatus> protossResults =
        getFixtures(DiagnosisStatus.class, HashSet::new, 1);

    Set<DiagnosisStatusDto> expected =
        mapDto(protossResults, DiagnosisStatusDto.class, HashSet::new);

    // mock dependencies
    when(diagnosisStatusManager.getAll()).thenReturn(protossResults);

    int statusId = expected.iterator().next().getStatusId() + 1;

    // test
    given().pathParam("statusId", statusId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/diagnosis-statuses/{statusId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(diagnosisStatusManager).getAll();
  }
}
