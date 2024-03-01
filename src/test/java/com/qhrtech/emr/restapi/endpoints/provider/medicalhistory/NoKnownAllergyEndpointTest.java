
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.allergy.NoKnownAllergyManager;
import com.qhrtech.emr.accuro.model.allergy.AllergyType;
import com.qhrtech.emr.accuro.model.allergy.NoKnownAllergy;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.NoKnownAllergyDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class NoKnownAllergyEndpointTest extends AbstractEndpointTest<NoKnownAllergyEndpoint> {

  private final NoKnownAllergyManager noKnownAllergyManager;

  public NoKnownAllergyEndpointTest() {
    super(new NoKnownAllergyEndpoint(), NoKnownAllergyEndpoint.class);
    noKnownAllergyManager = mock(NoKnownAllergyManager.class);
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
    servicesMap.put(NoKnownAllergyManager.class, noKnownAllergyManager);
    return servicesMap;
  }

  @Test
  public void getForPatient() throws Exception {

    int patientId = TestUtilities.nextId();

    // setup random data
    List<NoKnownAllergy> protossResults = getFixtures(NoKnownAllergy.class, ArrayList::new, 5);

    // mock dependencies
    when(noKnownAllergyManager.getForPatient(patientId)).thenReturn(protossResults);

    List<NoKnownAllergyDto> expected =
        mapDto(protossResults, NoKnownAllergyDto.class, ArrayList::new);

    // test
    List<NoKnownAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/no-known-allergies/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(NoKnownAllergyDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(noKnownAllergyManager).getForPatient(patientId);
  }

  @Test
  public void getForPatientWithAllergyType() throws Exception {

    List<NoKnownAllergy> allergies = getFixtures(NoKnownAllergy.class, ArrayList::new, 4);
    for (int i = 0; i < 4; i++) {
      allergies.get(i).setAllergyType(AllergyType.values()[i]);
    }

    // Set random data for all allergy type. Cannot expect more than 1 no known allergy with same
    // type.
    List<NoKnownAllergy> protossResults = new ArrayList<>(allergies);
    String allergyType = TestUtilities.nextValue(AllergyType.class).name();

    int patientId = TestUtilities.nextId();

    // mock dependencies
    when(noKnownAllergyManager.getForPatient(patientId)).thenReturn(protossResults);

    List<NoKnownAllergyDto> expected =
        mapDto(
            Collections.singleton(protossResults.stream()
                .filter(p -> p.getAllergyType().toString().equals(allergyType)).findFirst()
                .orElseThrow(RuntimeException::new)),
            NoKnownAllergyDto.class, ArrayList::new);

    // test
    List<NoKnownAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("allergyType", allergyType)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/no-known-allergies/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(NoKnownAllergyDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(noKnownAllergyManager).getForPatient(patientId);
  }

  @Test
  public void getForPatientWithInvalidAllergyType() throws Exception {
    // set up test data
    List<NoKnownAllergy> allergies = getFixtures(NoKnownAllergy.class, ArrayList::new, 4);
    for (int i = 0; i < 4; i++) {
      allergies.get(i).setAllergyType(AllergyType.values()[i]);
    }

    int patientId = TestUtilities.nextId();
    List<NoKnownAllergy> protossResults = new ArrayList<>(allergies);

    // mock dependencies
    when(noKnownAllergyManager.getForPatient(patientId)).thenReturn(protossResults);

    // test
    given().pathParam("patientId", patientId)
        .queryParam("allergyType", TestUtilities.nextString(15))
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/no-known-allergies/")
        .then()
        .assertThat()
        .statusCode(400);

    // assertions
    verify(noKnownAllergyManager, never()).getForPatient(patientId);
  }

  @Test
  public void getForPatientWithAllergyTypeNotFound() throws Exception {
    // set up data
    List<NoKnownAllergy> allergies = getFixtures(NoKnownAllergy.class, ArrayList::new, 3);
    for (int i = 0; i < 3; i++) {
      allergies.get(i).setAllergyType(AllergyType.values()[i]);
    }

    AllergyType typeParameter = AllergyType.values()[3];
    int patientId = TestUtilities.nextId();
    List<NoKnownAllergy> protossResults = new ArrayList<>(allergies);

    // mock dependencies
    when(noKnownAllergyManager.getForPatient(patientId)).thenReturn(protossResults);

    List<NoKnownAllergyDto> actual = toCollection(
        given().pathParam("patientId", patientId)
            .queryParam("allergyType", typeParameter.name())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/no-known-allergies/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(NoKnownAllergyDto[].class),
        ArrayList::new);

    assertEquals(Collections.emptyList(), actual);
    verify(noKnownAllergyManager).getForPatient(patientId);
  }

  @Test
  public void getById() throws Exception {

    // setup random data
    NoKnownAllergy protossResults = getFixture(NoKnownAllergy.class);

    int patientId = protossResults.getPatientId();
    int allergyId = protossResults.getNoKnownAllergyId();

    // mock dependencies
    when(noKnownAllergyManager.getById(allergyId)).thenReturn(protossResults);

    NoKnownAllergyDto expected =
        mapDto(protossResults, NoKnownAllergyDto.class);

    // test
    NoKnownAllergyDto actual =
        given()
            .pathParam("patientId", patientId)
            .pathParam("allergyId", allergyId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/patients/{patientId}/no-known-allergies/{allergyId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(NoKnownAllergyDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(noKnownAllergyManager).getById(allergyId);
    verify(noKnownAllergyManager, never()).getForPatient(patientId);
  }

  @Test
  public void getByIdNotFound() throws Exception {

    // setup random data
    int patientId = TestUtilities.nextId();
    int allergyId = TestUtilities.nextId();

    // mock dependencies
    when(noKnownAllergyManager.getById(allergyId)).thenReturn(null);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("allergyId", allergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/no-known-allergies/{allergyId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(noKnownAllergyManager).getById(allergyId);
    verify(noKnownAllergyManager, never()).getForPatient(patientId);
  }

  @Test
  public void getByIdWithWrongPatientId() throws Exception {

    // setup random data
    NoKnownAllergy protossResults = getFixture(NoKnownAllergy.class);

    int patientId = TestUtilities.nextId();
    int allergyId = protossResults.getNoKnownAllergyId();

    // mock dependencies
    when(noKnownAllergyManager.getById(allergyId)).thenReturn(protossResults);

    // test
    given()
        .pathParam("patientId", patientId)
        .pathParam("allergyId", allergyId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/patients/{patientId}/no-known-allergies/{allergyId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(noKnownAllergyManager).getById(allergyId);
    verify(noKnownAllergyManager, never()).getForPatient(patientId);
  }
}
