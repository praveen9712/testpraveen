
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.allergy.AllergyCommentManager;
import com.qhrtech.emr.accuro.api.allergy.AllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.PatientAllergyManager;
import com.qhrtech.emr.accuro.model.allergy.AllergyComment;
import com.qhrtech.emr.accuro.model.allergy.AllergyReaction;
import com.qhrtech.emr.accuro.model.allergy.AllergyType;
import com.qhrtech.emr.accuro.model.allergy.PatientAllergy;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyCommentDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientAllergyDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ReactionCode;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class PatientAllergyEndpointTest extends AbstractEndpointTest<PatientAllergyEndpoint> {

  private final PatientAllergyManager patientAllergyManager;
  private final AllergyReactionManager allergyReactionManager;
  private final AllergyCommentManager allergyCommentManager;

  public PatientAllergyEndpointTest() {
    super(new PatientAllergyEndpoint(), PatientAllergyEndpoint.class);
    allergyCommentManager = mock(AllergyCommentManager.class);
    allergyReactionManager = mock(AllergyReactionManager.class);
    patientAllergyManager = mock(PatientAllergyManager.class);
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
    servicesMap.put(PatientAllergyManager.class, patientAllergyManager);
    servicesMap.put(AllergyReactionManager.class, allergyReactionManager);
    servicesMap.put(AllergyCommentManager.class, allergyCommentManager);
    return servicesMap;
  }

  @Test
  public void testGetAllergyComments() throws Exception {

    PatientAllergy patientAllergy = getFixture(PatientAllergy.class);
    patientAllergy.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());
    int patientAllergyId = patientAllergy.getId();
    int patientId = patientAllergy.getPatientId();

    // setup random data
    List<AllergyComment> protossResults = getFixtures(AllergyComment.class, ArrayList::new, 5);

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(patientAllergy);
    when(allergyCommentManager.getAllergyCommentsByPatientAllergyId(patientAllergyId))
        .thenReturn(protossResults);

    List<AllergyCommentDto> expected =
        mapDto(protossResults, AllergyCommentDto.class, ArrayList::new);

    // test
    List<AllergyCommentDto> actual = toCollection(
        given().pathParam("patientId", patientId).pathParam("patientAllergyId", patientAllergyId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/comments")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AllergyCommentDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(patientAllergyManager).getById(patientAllergyId);
    verify(allergyCommentManager).getAllergyCommentsByPatientAllergyId(patientAllergyId);
  }

  @Test
  public void testGetAllergyCommentsForPatientAllergyNotFound() throws Exception {

    int patientAllergyId = TestUtilities.nextId();
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(null);

    // test
    given().pathParam("patientId", patientId).pathParam("patientAllergyId", patientAllergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/comments")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(patientAllergyManager).getById(patientAllergyId);
  }

  @Test
  public void testGetAllergyCommentsForWrongPatient() throws Exception {

    PatientAllergy patientAllergy = getFixture(PatientAllergy.class);
    patientAllergy.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());
    int patientAllergyId = patientAllergy.getId();
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(patientAllergy);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("patientAllergyId", patientAllergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/comments")
        .then()
        .assertThat()
        .statusCode(404);

    verify(patientAllergyManager).getById(patientAllergyId);
  }

  @Test
  public void testGetReactionsForPatientAllergy() throws Exception {

    PatientAllergy patientAllergy = getFixture(PatientAllergy.class);
    patientAllergy.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());
    int patientAllergyId = patientAllergy.getId();
    int patientId = patientAllergy.getPatientId();

    // setup random data
    Set<AllergyReaction> protossResults =
        getFixtures(AllergyReaction.class, HashSet::new, 5);

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(patientAllergy);
    when(allergyReactionManager.getByPatientAllergyId(patientAllergyId)).thenReturn(protossResults);

    Set<AllergyReactionDto> expected =
        mapDto(protossResults, AllergyReactionDto.class, HashSet::new);

    // test
    Set<AllergyReactionDto> actual = toCollection(
        given()
            .pathParam("patientId", patientId)
            .pathParam("patientAllergyId", patientAllergyId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/reactions")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(AllergyReactionDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(patientAllergyManager).getById(patientAllergyId);
    verify(allergyReactionManager).getByPatientAllergyId(patientAllergyId);
  }

  @Test
  public void testGetReactionsForPatientAllergyNotFound() throws Exception {

    int patientAllergyId = TestUtilities.nextId();
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(null);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("patientAllergyId", patientAllergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/reactions")
        .then()
        .assertThat()
        .statusCode(404);

    verify(patientAllergyManager).getById(patientAllergyId);
  }

  @Test
  public void testGetReactionsForWrongPatient() throws Exception {

    PatientAllergy patientAllergy = getFixture(PatientAllergy.class);
    patientAllergy.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());
    int patientAllergyId = patientAllergy.getId();
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getById(patientAllergyId)).thenReturn(patientAllergy);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("patientAllergyId", patientAllergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}/reactions")
        .then()
        .assertThat()
        .statusCode(404);

    verify(patientAllergyManager).getById(patientAllergyId);
  }

  @Test
  public void testGetAllForPatient() throws Exception {
    // setup random data
    List<PatientAllergy> protossResults =
        getFixtures(PatientAllergy.class, ArrayList::new, 5);
    for (PatientAllergy pa : protossResults) {
      pa.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());
    }
    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getForPatient(patientId)).thenReturn(protossResults);

    List<PatientAllergyDto> expected =
        mapDto(protossResults, PatientAllergyDto.class, ArrayList::new);

    // test
    List<PatientAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/allergies")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientAllergyDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
  }

  @Test
  public void testGetAllForPatientByType() throws Exception {
    // set up random data
    List<PatientAllergy> results = getFixtures(PatientAllergy.class, ArrayList::new, 25);
    results.forEach(p -> p.setAllergyType(TestUtilities.nextValue(AllergyType.class)));
    results.forEach(p -> p.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode()));

    AllergyType selectedType = TestUtilities.nextValue(AllergyType.class);

    List<PatientAllergyDto> expected =
        mapDto(results.stream().filter(a -> a.getAllergyType() == selectedType)
            .collect(Collectors.toList()), PatientAllergyDto.class, ArrayList::new);

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getForPatient(patientId)).thenReturn(results);

    // test
    List<PatientAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("allergyType", selectedType)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/allergies")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientAllergyDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetAllForPatientByTypeNoneFound() throws Exception {
    // set up random data
    List<PatientAllergy> results = getFixtures(PatientAllergy.class, ArrayList::new, 25);
    results.forEach(p -> p.setAllergyType(TestUtilities.nextValue(AllergyType.class)));
    results.forEach(p -> p.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode()));

    AllergyType selectedType = TestUtilities.nextValue(AllergyType.class);

    List<PatientAllergyDto> expected = Collections.emptyList();

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(patientAllergyManager.getForPatient(patientId))
        .thenReturn(results.stream().filter(a -> a.getAllergyType() != selectedType)
            .collect(Collectors.toList()));

    // test
    List<PatientAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("allergyType", selectedType)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/allergies")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientAllergyDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetOne() throws Exception {
    // setup random data
    PatientAllergy protossResults =
        getFixture(PatientAllergy.class);
    protossResults.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());

    // mock dependencies
    when(patientAllergyManager.getById(protossResults.getId())).thenReturn(protossResults);

    PatientAllergyDto expected =
        mapDto(protossResults, PatientAllergyDto.class);

    // test
    PatientAllergyDto actual =
        given().pathParam("patientId", protossResults.getPatientId())
            .pathParam("patientAllergyId", protossResults.getId())
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientAllergyDto.class);

    // assertions
    assertEquals(expected, actual);
  }

  @Test
  public void testGetOnePatientIdDoesntMatch() throws Exception {
    // setup random data
    PatientAllergy protossResults =
        getFixture(PatientAllergy.class);
    protossResults.setReactionCode(TestUtilities.nextValue(ReactionCode.class).getCode());

    // mock dependencies
    when(patientAllergyManager.getById(protossResults.getId())).thenReturn(protossResults);

    // test
    given().pathParam("patientId", protossResults.getPatientId() + 1)
        .pathParam("patientAllergyId", protossResults.getId())
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/allergies/{patientAllergyId}")
        .then()
        .assertThat()
        .statusCode(404);
  }
}
