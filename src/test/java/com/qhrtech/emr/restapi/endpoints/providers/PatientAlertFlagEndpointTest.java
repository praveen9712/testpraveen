
package com.qhrtech.emr.restapi.endpoints.providers;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.patient.PatientFlagManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.PatientFlag;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.provider.PatientAlertFlagEndpoint;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class PatientAlertFlagEndpointTest extends AbstractEndpointTest<PatientAlertFlagEndpoint> {

  private static final String ENDPOINT_URL = "/v1/provider-portal/patients";
  private final PatientFlagManager patientFlagManager;
  private final ApiSecurityContext context;

  public PatientAlertFlagEndpointTest() {
    super(new PatientAlertFlagEndpoint(), PatientAlertFlagEndpoint.class);
    patientFlagManager = mock(PatientFlagManager.class);
    context = new ApiSecurityContext();
    AuditLogUser user = new AuditLogUser(
        TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(PatientFlagManager.class, patientFlagManager);
  }

  @Test
  public void getFlagById() throws ProtossException {
    PatientFlag expected = getFixture(PatientFlag.class);

    when(patientFlagManager.getFlags()).thenReturn(Collections.singleton(expected));

    given()
        .pathParam("flagId", expected.getId())
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/flags/{flagId}")
        .then()
        .assertThat().statusCode(200)
        .and().body("id", is(expected.getId()))
        .and().body("name", is(expected.getName()));

    verify(patientFlagManager).getFlags();
  }

  @Test
  public void getFlagByIdNotFound() throws ProtossException {
    int flagId = TestUtilities.nextId();
    Set<PatientFlag> emptySet = Collections.emptySet();
    when(patientFlagManager.getFlags()).thenReturn(emptySet);

    given()
        .pathParam("flagId", flagId)
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/flags/{flagId}")
        .then()
        .assertThat().statusCode(404);

    verify(patientFlagManager).getFlags();
  }

  @Test
  public void getFlags() throws ProtossException {
    PatientFlag expected = getFixture(PatientFlag.class);

    when(patientFlagManager.getFlags()).thenReturn(Collections.singleton(expected));


    when()
        .get(getBaseUrl() + ENDPOINT_URL + "/flags")
        .then()
        .assertThat().statusCode(200)
        .and().body("id", hasItems(expected.getId()))
        .and().body("name", hasItems(expected.getName()));

    verify(patientFlagManager).getFlags();
  }

  @Test
  public void getFlagForPatient() throws ProtossException {
    Set<PatientFlag> appliedFlags = getFixtures(PatientFlag.class, HashSet::new, 2);
    Set<PatientFlag> unAppliedFlags = getFixtures(PatientFlag.class, HashSet::new, 2);
    Set<PatientFlag> intersectionFlags = new HashSet<>(appliedFlags);
    intersectionFlags.retainAll(unAppliedFlags);
    // sanity check to verify unique ids
    assertTrue(intersectionFlags.isEmpty());

    final PatientFlag flagToRequest = TestUtilities.nextElement(appliedFlags);
    Set<Integer> appliedFlagIds = appliedFlags.stream()
        .map(PatientFlag::getId).collect(Collectors.toSet());

    Set<PatientFlag> allFlags = new HashSet<>(appliedFlags);
    allFlags.addAll(unAppliedFlags);

    int patientId = TestUtilities.nextId();

    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(appliedFlagIds);
    when(patientFlagManager.getFlags()).thenReturn(allFlags);

    given()
        .pathParams("patientId", patientId, "flagId", flagToRequest.getId())
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then()
        .assertThat().statusCode(200)
        .and().body("id", is(flagToRequest.getId()))
        .and().body("name", is(flagToRequest.getName()));

    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).getFlags();
  }

  @Test
  public void getFlagForPatientNotFound() throws ProtossException {
    Set<PatientFlag> appliedFlags = getFixtures(PatientFlag.class, HashSet::new, 2);
    Set<PatientFlag> unAppliedFlags = getFixtures(PatientFlag.class, HashSet::new, 2);

    // sanity check to verify unique ids
    Set<PatientFlag> intersectionFlags = new HashSet<>(appliedFlags);
    intersectionFlags.retainAll(unAppliedFlags);
    assertTrue(intersectionFlags.isEmpty());

    final PatientFlag flagToRequest = TestUtilities.nextElement(unAppliedFlags);
    Set<Integer> appliedFlagIds = appliedFlags.stream()
        .map(PatientFlag::getId).collect(Collectors.toSet());
    Set<PatientFlag> allFlags = new HashSet<>(appliedFlags);
    allFlags.addAll(unAppliedFlags);
    int patientId = TestUtilities.nextId();

    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(appliedFlagIds);
    when(patientFlagManager.getFlags()).thenReturn(allFlags);

    given()
        .pathParams("patientId", patientId, "flagId", flagToRequest.getId())
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then()
        .assertThat().statusCode(404);

    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).getFlags();
  }

  @Test
  public void getFlagsForPatient() throws ProtossException {
    HashSet<Integer> patientFlag = new HashSet<>();
    int patientId = TestUtilities.nextId();

    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlag);

    given()
        .pathParam("patientId", patientId)
        .when()
        .get(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then()
        .assertThat().statusCode(200);

    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).getFlags();
  }

  @Test
  public void removeFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = Collections.singleton(flagId);

    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlagIds);

    given()
        .pathParams("patientId", patientId, "flagId", flagId)
        .when()
        .delete(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).removeFlag(patientId, flagId);
  }

  @Test
  public void removeFlagWithNonAppliedFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = new HashSet<>();

    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlagIds);

    given()
        .pathParams("patientId", patientId, "flagId", flagId)
        .when()
        .delete(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then().assertThat().statusCode(404);

    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager, never()).removeFlag(anyInt(), anyInt());
  }

  @Test
  public void applyFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = new HashSet<>();
    Set<PatientFlag> patientFlags = Collections.singleton(patientFlag);

    when(patientFlagManager.getFlags()).thenReturn(patientFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlagIds);

    given()
        .pathParams("patientId", patientId, "flagId", flagId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).applyFlag(patientId, flagId);
  }

  @Test
  public void applyFlagWithNonExistedFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = new HashSet<>();
    Set<PatientFlag> patientFlags = new HashSet<>();

    when(patientFlagManager.getFlags()).thenReturn(patientFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlagIds);

    given()
        .pathParams("patientId", patientId, "flagId", flagId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then().assertThat().statusCode(404);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager, never()).getFlagsForPatient(patientId);
    verify(patientFlagManager, never()).applyFlag(anyInt(), anyInt());
  }

  @Test
  public void applyFlagWithDuplicatedFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = Collections.singleton(flagId);
    Set<PatientFlag> patientFlags = Collections.singleton(patientFlag);

    when(patientFlagManager.getFlags()).thenReturn(patientFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(patientFlagIds);

    given()
        .pathParams("patientId", patientId, "flagId", flagId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags/{flagId}")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager, never()).applyFlag(anyInt(), anyInt());
  }

  @Test
  public void applyFlags() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = Collections.singleton(flagId);
    Set<PatientFlag> patientFlags = Collections.singleton(patientFlag);

    when(patientFlagManager.getFlags()).thenReturn(patientFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(new HashSet<>());
    given()
        .pathParam("patientId", patientId)
        .body(patientFlagIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).applyFlags(patientId, patientFlagIds);
  }

  @Test
  public void applyFlagsWithNullFlagIds() throws ProtossException {
    int patientId = TestUtilities.nextId();

    given()
        .pathParam("patientId", patientId)
        .body("null")
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then().assertThat().statusCode(400);

    verify(patientFlagManager, never()).getFlags();
    verify(patientFlagManager, never()).getFlagsForPatient(anyInt());
    verify(patientFlagManager, never()).applyFlags(anyInt(), anySetOf(Integer.class));
  }

  @Test
  public void applyFlagsWithNonExistedFlag() throws ProtossException {
    PatientFlag patientFlag = getFixture(PatientFlag.class);
    int patientId = TestUtilities.nextId();
    int flagId = patientFlag.getId();
    Set<Integer> patientFlagIds = Collections.singleton(flagId);

    when(patientFlagManager.getFlags()).thenReturn(Collections.emptySet());
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(Collections.emptySet());
    given()
        .pathParam("patientId", patientId)
        .body(patientFlagIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then().assertThat().statusCode(400);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager, never()).getFlagsForPatient(anyInt());
    verify(patientFlagManager, never()).applyFlags(anyInt(), anySetOf(Integer.class));
  }

  @Test
  public void applyFlagsWithDuplicatedFlag() throws ProtossException {
    int patientId = TestUtilities.nextId();
    Set<PatientFlag> patientFlags = getFixtures(PatientFlag.class, HashSet::new, 2);
    Set<Integer> flagIds =
        patientFlags.stream().map(PatientFlag::getId).collect(Collectors.toSet());

    when(patientFlagManager.getFlags()).thenReturn(patientFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(flagIds);
    given()
        .pathParam("patientId", patientId)
        .body(flagIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager, never()).applyFlags(anyInt(), anySetOf(Integer.class));
  }

  @Test
  public void applyFlagsWithSomeDuplicateFlags() throws ProtossException {
    // generate existing data
    Set<PatientFlag> appliedFlags = getFixtures(PatientFlag.class, HashSet::new, 4);
    Set<PatientFlag> unAppliedFlags = getFixtures(PatientFlag.class, HashSet::new, 4);

    // Just a sanity check in the event the fixtures implementation changes
    Set<PatientFlag> intersectionFlags = new HashSet<>(appliedFlags);
    intersectionFlags.retainAll(unAppliedFlags);
    assertTrue(intersectionFlags.isEmpty());

    // Cherry pick one pre applied and one non applied flag to submit
    Set<Integer> flagsToAdd = new HashSet<>(Arrays.asList(
        TestUtilities.nextElement(appliedFlags).getId(),
        TestUtilities.nextElement(unAppliedFlags).getId()));

    final int patientId = TestUtilities.nextId();
    Set<Integer> appliedFlagIds =
        appliedFlags.stream().map(PatientFlag::getId).collect(Collectors.toSet());

    // we expect that only the new flags will be added to the patient
    Set<Integer> expectedFlags = new HashSet<>(flagsToAdd);
    expectedFlags.removeAll(appliedFlagIds);

    // all the possible flags are a union of the two sets
    Set<PatientFlag> existingFlags = new HashSet<>(appliedFlags);
    existingFlags.addAll(unAppliedFlags);

    // mocks
    when(patientFlagManager.getFlags()).thenReturn(existingFlags);
    when(patientFlagManager.getFlagsForPatient(patientId)).thenReturn(appliedFlagIds);

    given()
        .pathParam("patientId", patientId)
        .body(flagsToAdd)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + ENDPOINT_URL + "/{patientId}/flags")
        .then().assertThat().statusCode(204);

    verify(patientFlagManager).getFlags();
    verify(patientFlagManager).getFlagsForPatient(patientId);
    verify(patientFlagManager).applyFlags(patientId, expectedFlags);
  }

}
