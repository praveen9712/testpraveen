
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.labs.LabGroup;
import com.qhrtech.emr.accuro.model.labs.LabObservation;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabObservationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.LocalDate;
import org.junit.Assert;
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
    int patientId = Math.abs(TestUtilities.nextInt());
    int testId = TestUtilities.nextId();
    int resultId = TestUtilities.nextId();

    // mock dependencies
    org.joda.time.LocalDate start = TestUtilities.nextLocalDate();
    org.joda.time.LocalDate end = TestUtilities.nextLocalDate();

    String startDate = start.toString();
    String endDate = end.toString();

    start = LocalDate.parse(startDate);
    end = LocalDate.parse(endDate);

    when(labManager.getLabsForPatient(patientId, Collections.singleton(testId),
        Collections.singleton(resultId),
        start, end)).thenReturn(protossResults);
    // test
    List<LabGroupDto> expected = mapDto(protossResults, LabGroupDto.class, ArrayList::new);
    List<LabGroupDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .queryParam("testIds", testId)
            .queryParam("resultIds", resultId)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLabsForPatient(patientId, Collections.singleton(testId),
        Collections.singleton(resultId),
        start, end);
  }


  @Test
  public void testGetLabGroupsNegativePatientId() throws Exception {
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);

    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLabsForPatient(anyInt());
  }

  @Test
  public void testGetLabGroup() throws Exception {
    // setup random data
    LabGroup expected = getFixture(LabGroup.class);
    // mock dependencies
    when(labManager.getLab(expected.getGroupId())).thenReturn(expected);
    LabGroupDto expectedDto = mapDto(expected, LabGroupDto.class);
    // test
    LabGroupDto actual =
        given()
            .pathParam("patientId", expected.getPatientId())
            .pathParam("groupId", expected.getGroupId())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabGroupDto.class);
    // assertions
    Assert.assertEquals(expectedDto, actual);
    verify(labManager).getLab(expected.getGroupId());
  }

  @Test
  public void testGetLabGroupWithNullResult() throws Exception {
    // setup random data
    LabGroup expected = getFixture(LabGroup.class);
    // mock dependencies
    when(labManager.getLab(expected.getGroupId())).thenReturn(null);
    // test
    Object response = given()
        .pathParam("patientId", expected.getPatientId())
        .pathParam("groupId", expected.getGroupId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(404)
        .extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found."));
    verify(labManager).getLab(expected.getGroupId());
  }

  @Test
  public void testGetLabGroupNegativePatientId() throws Exception {
    // setup random data
    // LabGroup expected = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int groupId = Math.abs(TestUtilities.nextInt());
    // expected.setPatientId(patientId);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupNegativeGroupId() throws Exception {
    // setup random data
    LabGroup expected = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;
    expected.setPatientId(patientId);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupGroupNull() throws Exception {
    // setup random data
    LabGroup expected = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(expected);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group does not match the specified resource"));
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testCreateLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    labGroup.setObservations(null);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    Integer groupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.createLab(labGroup, user)).thenReturn(groupId);
    // test
    Integer actualId =
        given()
            .pathParam("patientId", patientId)
            .body(expectedDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    Assert.assertEquals(groupId, actualId);
    verify(labManager).createLab(labGroup, user);
  }


  @Test
  public void testCreateLabGroupWithNullLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();

    Object response = given()
        .pathParam("patientId", patientId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Lab Group"));
    verify(labManager, never()).createLab(labGroup, user);
  }

  @Test
  public void testCreateLabGroupNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).createLab(any(), any());
  }

  @Test
  public void testCreateLabGroupTransactionDateNull() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    expectedDto.setTransactionDate(null);
    int patientId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Transaction Date is Required For Lab Groups"));
    verify(labManager, never()).createLab(any(), any());
  }

  @Test
  public void testCreateLabGroupVersionDateNull() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    expectedDto.setVersionDate(null);
    int patientId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Version Date is Required For Lab Groups"));
    verify(labManager, never()).createLab(any(), any());
  }

  @Test
  public void testCreateLabGroupUnequalPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group does not match the specified resource"));
    verify(labManager, never()).createLab(any(), any());
  }

  @Test
  public void testUpdateLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = expectedDto.getGroupId();
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Integer actualId =
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", labGroupId)
            .body(expectedDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    Assert.assertEquals(expectedId, actualId);
    verify(labManager).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = expectedDto.getGroupId();
    Integer expectedId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Lab Group"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId() * -1;
    int labGroupId = expectedDto.getGroupId();
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupNegativeGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = expectedDto.getGroupId() * -1;
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupNullTransactionDate() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = expectedDto.getGroupId();
    expectedDto.setTransactionDate(null);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Transaction Date is Required For Lab Groups"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupNullVersionDate() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = expectedDto.getGroupId();
    expectedDto.setVersionDate(null);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Version Date is Required For Lab Groups"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupUnequalPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = expectedDto.getGroupId();

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group does not match the specified resource"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupUnequalGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group does not match the specified resource"));
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(204);
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testDeleteLabGroupWithNullResult() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(null);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Lab Group Not Found"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testDeleteLabGroupNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId() * -1;
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testDeleteLabGroupNegativeGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto expectedDto = mapDto(labGroup, LabGroupDto.class);
    int patientId = expectedDto.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testDeleteLabGroupUnequalPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .delete(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group Id does not match the specified resource"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testGetLabGroupHistory() throws Exception {
    // setup random data
    List<LabGroup> protossResults =
        getFixtures(LabGroup.class, ArrayList::new, 2);
    int patientId = Math.abs(TestUtilities.nextInt());
    int baseGroupId = Math.abs(TestUtilities.nextInt());
    protossResults.get(0).setPatientId(patientId);
    protossResults.get(1).setPatientId(patientId);
    // mock dependencies
    when(labManager.getLabsForBaseGroup(baseGroupId)).thenReturn(protossResults);
    List<LabGroupDto> expected =
        mapDto(protossResults, LabGroupDto.class, ArrayList::new);

    // test
    List<LabGroupDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("baseGroupId", baseGroupId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups/{baseGroupId}/history")
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
  public void testGetLabGroupHistoryNegativePatientId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int baseGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLabsForBaseGroup(anyInt());
  }

  @Test
  public void testGetLabGroupHistoryNegativeBaseGroupId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt());
    int baseGroupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLabsForBaseGroup(anyInt());
  }

  @Test
  public void testGetLabGroupHistoryNullBaseGroup() throws Exception {
    // setup random data
    List<LabGroup> protossResults = new ArrayList<>();
    int patientId = Math.abs(TestUtilities.nextInt());
    int baseGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLabsForBaseGroup(baseGroupId)).thenReturn(protossResults);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLabsForBaseGroup(baseGroupId);
  }

  @Test
  public void testGetLabGroupHistoryPatientIdNotMatch() throws Exception {
    List<LabGroup> protossResults =
        getFixtures(LabGroup.class, ArrayList::new, 2);
    int patientId = Math.abs(TestUtilities.nextInt());
    int baseGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLabsForBaseGroup(anyInt())).thenReturn(protossResults);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("baseGroupId", baseGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{baseGroupId}/history")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group Id does not match the specified resource"));
    verify(labManager).getLabsForBaseGroup(anyInt());
  }

  @Test
  public void testGetLabGroupRecipientIds() throws Exception {
    // setup random data
    Set<Integer> protossResults = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    when(labManager.getRecipientsForLab(labGroupId)).thenReturn(protossResults);
    // test
    Set<Integer> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", labGroupId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer[].class),
        HashSet::new);
    // assertions
    Assert.assertEquals(protossResults, actual);
    verify(labManager).getLab(labGroupId);
    verify(labManager).getRecipientsForLab(labGroupId);
  }

  @Test
  public void testGetLabGroupRecipientIdsWithNullResults() throws Exception {
    // setup random data
    Set<Integer> protossResults = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(null);
    when(labManager.getRecipientsForLab(labGroupId)).thenReturn(protossResults);
    // test

    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(labGroupId);
    verify(labManager, never()).getRecipientsForLab(labGroupId);
  }

  @Test
  public void testGetLabGroupRecipientIdsNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId() * -1;
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).getRecipientsForLab(anyInt());
  }

  @Test
  public void testGetLabGroupRecipientIdsNegativeLabGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).getRecipientsForLab(anyInt());
  }

  @Test
  public void testGetLabGroupRecipientIdsNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group Id does not match the specified resource"));
    verify(labManager).getLab(labGroupId);
    verify(labManager, never()).getRecipientsForLab(anyInt());
  }

  @Test
  public void testUpdateLabGroupRecipients() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(204);
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testUpdateLabGroupRecipientsWithNullLabGroup() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(null);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testUpdateLabGroupRecipientsNegativePatientId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId() * -1;
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testupdateLabGroupRecipientsNegativeGroupId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testupdateLabGroupRecipientsNotEqualPatientId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group Id does not match the specified resource"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testRemoveLabGroupRecipients() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(204);
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testRemoveLabGroupRecipientsWithNullResult() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(null);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testRemoveLabGroupRecipientsNegativePatientId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId() * -1;
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testRemoveLabGroupRecipientsNegativeGroupId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testRemoveLabGroupRecipientsNotEqualPatientId() throws Exception {
    // setup random data
    Set<Integer> recipients = getFixtures(Integer.class, HashSet::new, 3);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int labGroupId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .body(recipients)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/recipients")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Lab Group Id does not match the specified resource"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testGetLabGroupObservations() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    List<LabObservationDto> expected = labGroupDto.getObservations();
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(labGroup);
    // test
    List<LabObservationDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", labGroupId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabObservationDto[].class),
        ArrayList::new);
    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testGetLabGroupObservationsWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.getLab(labGroupId)).thenReturn(null);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(labGroupId);
  }

  @Test
  public void testGetLabGroupObservationsNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId() * -1;
    int labGroupId = Math.abs(TestUtilities.nextInt());
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupObservationsNegativeGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int labGroupId = Math.abs(TestUtilities.nextInt()) * -1;
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", labGroupId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupObservation() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto expectedDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = expectedDto.getObservationId();
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    LabObservationDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", groupId)
            .pathParam("observationId", observationId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/"
                + "lab-groups/{groupId}/observations/{observationId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabObservationDto.class);
    // assertions
    Assert.assertEquals(expectedDto, actual);
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto expectedDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = expectedDto.getObservationId();
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(null);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/"
            + "lab-groups/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    // assertions
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationNegativePatientId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupObservationNegativeGroupId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;
    int observationId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testGetLabGroupObservationNotFound() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testGetLabGroupObservationNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Patient Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testUpdateLabGroupObservation() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    List<LabObservation> observations = labGroup.getObservations();
    List<LabObservation> finalObservations = new ArrayList<>();
    for (LabObservation observation : observations) {
      observation.setObservationNumber(new BigDecimal("1.21"));
      finalObservations.add(observation);
    }
    labGroup.setObservations(finalObservations);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", groupId)
            .pathParam("observationId", observationId)
            .body(labObservationDto)
            .when()
            .contentType(ContentType.JSON)
            .put(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups"
                + "/{groupId}/observations/{observationId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(labManager).getLab(groupId);
    verify(labManager).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(null);

    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationNegativePatientId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}"
            + "/lab-groups/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationNegativeGroupId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;
    int observationId = Math.abs(TestUtilities.nextInt());
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Patient Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationNotEqualobservationId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Observation Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testUpdateLabGroupObservationObservationNotFound() throws Exception {
    // setup random data
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroupObservation() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", groupId)
            .pathParam("observationId", observationId)
            .when()
            .delete(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups"
                + "/{groupId}/observations/{observationId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(labManager).getLab(groupId);
    verify(labManager).updateLab(any(), any());
  }


  @Test
  public void testDeleteLabGroupObservationWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(null);
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups"
            + "/{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroupObservationNegativePatientId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroupObservationNegativeGroupId() throws Exception {
    // setup random data
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;
    int observationId = Math.abs(TestUtilities.nextInt());
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroupObservationNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Patient Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testDeleteLabGroupObservationObservationNotFound() throws Exception {
    // setup random data
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    int observationId = labObservationDto.getObservationId();
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .pathParam("observationId", observationId)
        .when()
        .delete(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations/{observationId}")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testAddLabGroupObservationWithNullGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    List<LabObservation> observations = labGroup.getObservations();
    List<LabObservation> finalObservations = new ArrayList<>();
    for (LabObservation observation : observations) {
      observation.setObservationNumber(new BigDecimal("1.21"));
      finalObservations.add(observation);
    }
    labGroup.setObservations(finalObservations);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(null);
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test

    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/"
            + "{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    // assertions
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }


  @Test
  public void testAddLabGroupObservation() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    List<LabObservation> observations = labGroup.getObservations();
    List<LabObservation> finalObservations = new ArrayList<>();
    for (LabObservation observation : observations) {
      observation.setObservationNumber(new BigDecimal("1.21"));
      finalObservations.add(observation);
    }
    labGroup.setObservations(finalObservations);
    LabGroupDto labGroupDto = mapDto(labGroup, LabGroupDto.class);
    LabObservationDto labObservationDto = labGroupDto.getObservations().get(0);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    Integer expectedId = Math.abs(TestUtilities.nextInt());

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    when(labManager.updateLab(any(LabGroup.class), any(AuditLogUser.class))).thenReturn(expectedId);
    // test
    Integer actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("groupId", groupId)
            .body(labObservationDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/lab-groups/"
                + "{groupId}/observations")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(labManager).getLab(groupId);
    verify(labManager).updateLab(any(), any());
  }

  @Test
  public void testAddLabGroupObservationNegativePatientId() throws Exception {
    // setup random data
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    int patientId = Math.abs(TestUtilities.nextInt()) * -1;
    int groupId = Math.abs(TestUtilities.nextInt());

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testAddLabGroupObservationNegativeGroupId() throws Exception {
    // setup random data
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testAddLabGroupObservationNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    LabObservationDto labObservationDto = getFixture(LabObservationDto.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);

    // test
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(labObservationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/observations")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Patient Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
    verify(labManager, never()).updateLab(any(), any());
  }

  @Test
  public void testReviewLabGroupForProviders() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    Set<Integer> providerIds = new HashSet<>();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(providerIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/reviews")
        .then()
        .assertThat()
        .statusCode(204);
    verify(labManager).getLab(groupId);
  }


  @Test
  public void testReviewLabGroupForProvidersWithNullLabGroup() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt());
    Set<Integer> providerIds = new HashSet<>();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(null);
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(providerIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/reviews")
        .then()
        .assertThat()
        .statusCode(404).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Resource Not Found"));
    verify(labManager).getLab(groupId);
  }

  @Test
  public void testReviewLabGroupForProvidersNegativePatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId() * -1;
    int groupId = Math.abs(TestUtilities.nextInt());
    Set<Integer> providerIds = new HashSet<>();

    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(providerIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/reviews")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Patient ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testReviewLabGroupForProvidersNegativeGroupId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = labGroup.getPatientId();
    int groupId = Math.abs(TestUtilities.nextInt()) * -1;
    Set<Integer> providerIds = new HashSet<>();

    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(providerIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/reviews")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString().contains("Invalid Group ID"));
    verify(labManager, never()).getLab(anyInt());
  }

  @Test
  public void testReviewLabGroupForProvidersNotEqualPatientId() throws Exception {
    // setup random data
    LabGroup labGroup = getFixture(LabGroup.class);
    int patientId = Math.abs(TestUtilities.nextInt());
    int groupId = Math.abs(TestUtilities.nextInt());
    Set<Integer> providerIds = new HashSet<>();

    // mock dependencies
    when(labManager.getLab(groupId)).thenReturn(labGroup);
    Object response = given()
        .pathParam("patientId", patientId)
        .pathParam("groupId", groupId)
        .body(providerIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/lab-groups/{groupId}/reviews")
        .then()
        .assertThat()
        .statusCode(400).extract().as(Object.class);
    Assert.assertTrue(response.toString()
        .contains("The provided Patient Id does not match the specified resource"));
    verify(labManager).getLab(groupId);
  }
}
