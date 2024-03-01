
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.labs.LabResult;
import com.qhrtech.emr.accuro.model.labs.LabTest;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LabResultDto;
import com.qhrtech.emr.restapi.models.dto.LabTestDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class LabTemplateEndpointTest extends AbstractEndpointTest<LabTemplateEndpoint> {
  private final LabManager labManager;

  public LabTemplateEndpointTest() {
    super(new LabTemplateEndpoint(), LabTemplateEndpoint.class);
    labManager = mock(LabManager.class);
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
    servicesMap.put(LabManager.class, labManager);
    return servicesMap;
  }

  @Test
  public void testGetLabTests() throws Exception {
    // setup random data
    Set<LabTest> protossResults =
        getFixtures(LabTest.class, HashSet::new, 5);
    // mock dependencies
    when(labManager.getLabTests()).thenReturn(protossResults);

    Set<LabTestDto> expected =
        mapDto(protossResults, LabTestDto.class, HashSet::new);

    // test
    Set<LabTestDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/tests")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabTestDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLabTests();
  }

  @Test
  public void testGetLabTest() throws Exception {
    // setup random data
    LabTest expected = getFixture(LabTest.class);
    int testId = expected.getTestId();
    // mock dependencies
    when(labManager.getLabTestsById(Collections.singleton(testId)))
        .thenReturn(Collections.singleton(expected));
    LabTestDto expectedDto = mapDto(expected, LabTestDto.class);

    // test
    LabTestDto actual =
        given()
            .pathParam("testId", testId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabTestDto.class);
    // assertions
    assertEquals(expectedDto, actual);
    verify(labManager).getLabTestsById(Collections.singleton(testId));
  }

  @Test
  public void testGetLabResultsForTest() throws Exception {
    // setup random data
    int testId = TestUtilities.nextId();
    Set<LabResult> protossResults =
        getFixtures(LabResult.class, HashSet::new, 5);

    // mock dependencies
    when(labManager.getResultsForTest(testId)).thenReturn(protossResults);

    Set<LabResultDto> expected =
        mapDto(protossResults, LabResultDto.class, HashSet::new);

    // test
    Set<LabResultDto> actual = toCollection(
        given()
            .pathParam("testId", testId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}/results")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabResultDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getResultsForTest(testId);
  }

  @Test
  public void testGetLabResults() throws Exception {
    // setup random data
    Set<LabResult> protossResults =
        getFixtures(LabResult.class, HashSet::new, 5);

    // mock dependencies
    when(labManager.getLabResults()).thenReturn(protossResults);

    Set<LabResultDto> expected =
        mapDto(protossResults, LabResultDto.class, HashSet::new);

    // test
    Set<LabResultDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/results")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabResultDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(labManager).getLabResults();
  }

  @Test
  public void testGetLabResult() throws Exception {
    // setup random data
    LabResult expected = getFixture(LabResult.class);
    int resultId = expected.getResultId();

    // mock dependencies
    when(labManager.getLabResultsById(Collections.singleton(resultId)))
        .thenReturn(Collections.singleton(expected));
    LabResultDto expectedDto =
        mapDto(expected, LabResultDto.class);

    // test
    LabResultDto actual =
        given()
            .pathParam("resultId", resultId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/results/{resultId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabResultDto.class);

    // assertions
    assertEquals(expectedDto, actual);
    verify(labManager).getLabResultsById(Collections.singleton(resultId));
  }


  @Test
  public void testGetLabResultWithNoResult() throws Exception {
    // setup random data
    Set<LabResult> labResults = new HashSet();

    Integer resultId = TestUtilities.nextInt();
    // mock dependencies
    when(labManager.getLabResultsById(Collections.singleton(resultId)))
        .thenReturn(labResults);

    given()
        .pathParam("resultId", resultId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/labs/results/{resultId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    // assertEquals(expectedDto, actual);
    verify(labManager).getLabResultsById(Collections.singleton(resultId));
  }

  @Test
  public void testGetLabSources() throws Exception {
    // setup random data
    Map<Integer, String> expectedLabSources = new TreeMap<>();
    expectedLabSources.put(TestUtilities.nextInt(9), TestUtilities.nextString(10));
    expectedLabSources.put(TestUtilities.nextInt(9), TestUtilities.nextString(10));
    expectedLabSources.put(TestUtilities.nextInt(9), TestUtilities.nextString(10));
    // mock dependencies
    when(labManager.getLabSources()).thenReturn(expectedLabSources);

    // test
    Map<Integer, String> actualLabSources =
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/labs/sources")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(expectedLabSources.getClass());
    // assertions
    assertEquals(expectedLabSources.toString(), actualLabSources.toString());
    verify(labManager).getLabSources();
  }

  @Test
  public void testCreateLabTest() throws Exception {
    // setup random data
    Integer expectedId = RandomUtils.nextInt();
    LabTest labTest = new LabTest();
    // mock dependencies
    when(labManager.createLabTest(labTest)).thenReturn(expectedId);
    LabTestDto expectedDto =
        mapDto(labTest, LabTestDto.class);

    // test
    Integer actual =
        given()
            .body(expectedDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/labs/tests")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(labManager).createLabTest(labTest);
  }

  @Test
  public void testUpdateLabTest() throws Exception {
    LabTest labTest = getFixture(LabTest.class);
    Integer testId = labTest.getTestId();
    LabTestDto expectedDto =
        mapDto(labTest, LabTestDto.class);


    given()
        .pathParam("testId", testId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}")
        .then().assertThat()
        .statusCode(204);
    verify(labManager).updateLabTest(labTest);
  }

  @Test
  public void testUpdateLabTestNullTestId() throws Exception {
    LabTest labTest = getFixture(LabTest.class);
    LabTestDto expectedDto =
        mapDto(labTest, LabTestDto.class);


    given()
        .pathParam("testId", RandomUtils.nextInt())
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}")
        .then().assertThat()
        .statusCode(400);
    verify(labManager, never()).updateLabTest(labTest);

  }

  @Test
  public void testAddResultsToTest() throws Exception {
    Integer testId = RandomUtils.nextInt();
    Set<Integer> resultIds = getFixtures(Integer.class, HashSet::new, 2);

    given()
        .pathParam("testId", testId)
        .body(resultIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}/results")
        .then().assertThat()
        .statusCode(204);
    verify(labManager).addResultsToTest(testId, resultIds);
  }

  @Test
  public void testDeleteLabResultsForTest() throws Exception {
    int testId = RandomUtils.nextInt();
    Set<Integer> resultIds = getFixtures(Integer.class, HashSet::new, 2);

    given()
        .pathParams("testId", testId)
        .body(resultIds)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/labs/tests/{testId}/results")
        .then().assertThat()
        .statusCode(204);
    verify(labManager).removeResultsFromTest(testId, resultIds);
  }

  @Test
  public void testCreateLabResult() throws Exception {
    // setup random data
    Integer expectedId = RandomUtils.nextInt();
    LabResult labResult = getFixture(LabResult.class);
    // setting below two fields false and null as they dont exists in DTO
    labResult.setLinkPrimary(false);
    labResult.setLinkGroup(null);
    // mock dependencies
    LabResultDto expectedDto =
        mapDto(labResult, LabResultDto.class);
    when(labManager.createLabResult(labResult)).thenReturn(expectedId);

    // test
    Integer actual =
        given()
            .body(expectedDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/labs/results")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(Integer.class);
    // assertions
    assertEquals(expectedId, actual);
    verify(labManager).createLabResult(labResult);
  }

  @Test
  public void testUpdateLabResult() throws Exception {
    LabResult labResult = getFixture(LabResult.class);
    // set link group to null and link primary false as there is no such field in DTO
    labResult.setLinkGroup(null);
    labResult.setLinkPrimary(false);
    Integer resultId = labResult.getResultId();
    LabResultDto expectedDto =
        mapDto(labResult, LabResultDto.class);

    given()
        .pathParam("resultId", resultId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/labs/results/{resultId}")
        .then().assertThat()
        .statusCode(204);
    verify(labManager).updateLabResult(labResult);
  }

  @Test
  public void testUpdateLabResultWithWrongId() throws Exception {
    LabResult labResult = getFixture(LabResult.class);
    Integer resultId = RandomUtils.nextInt();
    LabResultDto expectedDto =
        mapDto(labResult, LabResultDto.class);

    given()
        .pathParam("resultId", resultId)
        .body(expectedDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/labs/results/{resultId}")
        .then().assertThat()
        .statusCode(400);
    verify(labManager, never()).updateLabResult(labResult);
  }
}
