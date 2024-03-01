
package com.qhrtech.emr.restapi.endpoints.patient;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.labs.LabGroup;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabObservationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

public class PatientLabsEndpointTest extends AbstractEndpointTest<PatientLabsEndpoint> {
  private final LabManager labManager;
  ApiSecurityContext context;
  private AuditLogUser user;

  public PatientLabsEndpointTest() {
    super(new PatientLabsEndpoint(), PatientLabsEndpoint.class);
    labManager = mock(LabManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    context.setPatientId(TestUtilities.nextInt());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(LabManager.class, labManager);
    return servicesMap;
  }

  @Test
  public void testGetLabGroups() throws Exception {
    // setup random data
    List<LabGroup> protossResults =
        getFixtures(LabGroup.class, ArrayList::new, 2);

    int testId = TestUtilities.nextId();
    int resultId = TestUtilities.nextId();

    // mock dependencies
    org.joda.time.LocalDate start = TestUtilities.nextLocalDate();
    org.joda.time.LocalDate end = TestUtilities.nextLocalDate();

    String startDate = start.toString();
    String endDate = end.toString();

    start = LocalDate.parse(startDate);
    end = LocalDate.parse(endDate);

    // mock dependencies
    when(labManager.getLabsForPatient(context.getPatientId(), Collections.singleton(testId),
        Collections.singleton(resultId),
        start, end)).thenReturn(protossResults);

    // test
    List<LabGroupDto> expected = mapDto(protossResults, LabGroupDto.class, ArrayList::new);
    List<LabGroupDto> actual = toCollection(
        given()
            .queryParam("testIds", testId)
            .queryParam("resultIds", resultId)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when()
            .get(getBaseUrl() + "/v1/patient-portal/lab-groups/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLabsForPatient(context.getPatientId(), Collections.singleton(testId),
        Collections.singleton(resultId),
        start, end);
  }

  @Test
  public void testGetLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int groupId = RandomUtils.nextInt();

    // mock dependencies
    labGroup.setPatientId(context.getPatientId());
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    LabGroupDto expected = mapDto(labGroup, LabGroupDto.class);
    LabGroupDto actual =
        given()
            .pathParam("groupId", groupId)
            .when()
            .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupWithInvalidGroupId() throws Exception {
    // setup random data
    int groupId = RandomUtils.nextInt();

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupNegativePatientId() throws Exception {
    // setup random data
    int groupId = RandomUtils.nextInt() * -1;

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(labManager, never()).getLab(groupId);
  }

  @Test
  public void testGetLabGroupNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int groupId = RandomUtils.nextInt();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(403);
  }

  @Test
  public void testGetLabGroupObservations() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int groupId = RandomUtils.nextInt();

    // mock dependencies
    labGroup.setPatientId(context.getPatientId());
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    List<LabObservationDto> expected = labGroupDto.getObservations();
    List<LabObservationDto> actual = toCollection(
        given()
            .pathParam("groupId", groupId)
            .when()
            .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}/observations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabObservationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationsWithInvalidGroupId() throws Exception {
    // setup random data
    int groupId = RandomUtils.nextInt();

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationsNegativeGroupId() throws Exception {
    // setup random data
    int groupId = RandomUtils.nextInt() * -1;

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(labManager, never()).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationsNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int groupId = RandomUtils.nextInt();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(403);

    // assertions
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupHistory() throws Exception {
    // setup random data
    List<LabGroup> protossResults =
        getFixtures(LabGroup.class, ArrayList::new, 2);
    int baseGroupId = RandomUtils.nextInt();

    // mock dependencies
    protossResults.get(0).setPatientId(context.getPatientId());
    protossResults.get(1).setPatientId(context.getPatientId());
    when(labManager.getLabsForBaseGroup(baseGroupId)).thenReturn(protossResults);

    // test
    List<LabGroupDto> expected = mapDto(protossResults, LabGroupDto.class, ArrayList::new);
    List<LabGroupDto> actual = toCollection(
        given()
            .pathParam("baseGroupId", baseGroupId)
            .when()
            .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{baseGroupId}/history")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLabsForBaseGroup(baseGroupId);
  }

  @Test
  public void testGetLabGroupHistoryNegativeBashGroupId() throws Exception {
    // setup random data
    int baseGroupId = RandomUtils.nextInt() * -1;
    given()
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(labManager, never()).getLabsForBaseGroup(baseGroupId);
  }

  @Test
  public void testGetLabGroupHistoryEmptyBaseGroup() throws Exception {
    // setup random data
    List<LabGroup> protossResults = new ArrayList<>();
    int baseGroupId = RandomUtils.nextInt();

    // mock dependencies
    when(labManager.getLabsForBaseGroup(baseGroupId)).thenReturn(protossResults);

    // test
    given()
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(labManager).getLabsForBaseGroup(baseGroupId);
  }

  @Test
  public void testGetLabGroupHistoryNotEqualPatientId() throws Exception {
    // setup random data
    List<LabGroup> protossResults =
        getFixtures(LabGroup.class, ArrayList::new, 2);
    int baseGroupId = RandomUtils.nextInt();

    // mock dependencies
    when(labManager.getLabsForBaseGroup(baseGroupId)).thenReturn(protossResults);

    // test
    given()
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl() + "/v1/patient-portal/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(labManager).getLabsForBaseGroup(baseGroupId);
  }
}
