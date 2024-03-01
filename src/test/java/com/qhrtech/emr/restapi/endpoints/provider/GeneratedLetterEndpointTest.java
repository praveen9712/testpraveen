
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.accuro.api.letters.GeneratedLetterManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.letters.GeneratedLetter;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.GeneratedLetterDto;
import com.qhrtech.emr.restapi.models.dto.ReferralStatus;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class GeneratedLetterEndpointTest extends AbstractEndpointTest<GeneratedLetterEndpoint> {

  private static int targetId = TestUtilities.nextInt();
  private GeneratedLetterManager manager;

  public GeneratedLetterEndpointTest() {
    super(new GeneratedLetterEndpoint(), GeneratedLetterEndpoint.class);
    manager = mock(GeneratedLetterManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    AuditLogUser auditLogUser = new AuditLogUser(targetId, -1, null, null, null);
    context.setUser(auditLogUser);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(GeneratedLetterManager.class, manager);
    return map;
  }

  @Test
  public void testGetGeneratedLetters() throws ProtossException {
    Integer patientId = TestUtilities.nextInt();
    Integer userId = TestUtilities.nextId();

    List<GeneratedLetter> generatedLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    for (GeneratedLetter generatedLetter : generatedLetters) {
      generatedLetter.setPatientId(patientId);
      generatedLetter.setUserId(userId);
      generatedLetter.setTargetId(0);
      generatedLetter.setLetterContent(null);
    }

    LocalDate startAndEndDate = new LocalDate();
    Integer appointmentId = TestUtilities.nextId();

    when(manager.getAllWithNoContent(userId, patientId, startAndEndDate, startAndEndDate,
        appointmentId))
            .thenReturn(generatedLetters);

    String startAndEndDateString = new LocalDate().toString();
    // test
    List<GeneratedLetterDto> actual = toCollection(
        given()
            .queryParam("userId", userId)
            .queryParam("patientId", patientId)
            .queryParam("startDate", startAndEndDateString)
            .queryParam("endDate", startAndEndDateString)
            .queryParam("appointmentId", appointmentId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/generated-letters")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(GeneratedLetterDto[].class),
        ArrayList::new);

    List<GeneratedLetterDto> expected =
        mapDto(generatedLetters, GeneratedLetterDto.class, ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetGeneratedLettersWithNoPatientId() throws ProtossException {
    // test
    given()
        .queryParam("userId", 1)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters")
        .then()
        .assertThat()
        .statusCode(400);

    verify(manager, never()).getAllWithNoContent(any(Integer.class), any(Integer.class),
        any(LocalDate.class), any(LocalDate.class), any(Integer.class));
  }

  @Test
  public void testGetGeneratedLettersWithNoUserId() throws ProtossException {
    // test
    given()
        .queryParam("patientId", 1)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters")
        .then()
        .assertThat()
        .statusCode(400);

    verify(manager, never()).getAllWithNoContent(any(Integer.class), any(Integer.class),
        any(LocalDate.class), any(LocalDate.class), any(Integer.class));
  }

  @Test
  public void testGetGeneratedLettersByIdMultipart() throws Exception {
    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    letter.setLetterContent(TestUtilities.nextString(10).getBytes());
    letter.setExtension("pdf");
    int id = TestUtilities.nextId();
    doReturn(letter).when(manager).get(id);

    int targetId = getSecurityContext().getUser().getUserId();
    letter.setTargetId(targetId);
    GeneratedLetterDto expected = mapDto(
        letter,
        GeneratedLetterDto.class);

    String result = given()
        .pathParam("id", id)
        .accept("multipart/form-data")
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(200).extract().asString();

    // splitting the results on the line basis so as to parse the results for json string
    // and byte data.
    // Rest assured doesnt support parsing of Multipart response, So we have to manually
    // parse the results.
    String[] results = result.split("\n");

    int position = 0;
    String jsonString = "";
    String byteString = "";

    for (String part : results) {
      if (part.contains("application/json")) {

        jsonString = results[position + 4].trim();

      }
      if (part.contains("application/pdf")) {

        byteString = results[position + 4].trim();
      }
      position++;
    }

    ObjectMapper mapper = new ObjectMapper();
    GeneratedLetterDto actual = mapper.readValue(jsonString, GeneratedLetterDto.class);

    actual.setLetterContent(byteString.getBytes(StandardCharsets.UTF_8));
    assertEquals(expected, actual);

    verify(manager).get(id);
  }

  @Test
  public void testGetGeneratedLettersByIdMulipartWithOtherExtension() throws Exception {
    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    letter.setLetterContent(TestUtilities.nextString(10).getBytes());
    int id = TestUtilities.nextId();
    doReturn(letter).when(manager).get(id);

    int targetId = getSecurityContext().getUser().getUserId();
    letter.setTargetId(targetId);
    GeneratedLetterDto expected = mapDto(
        letter,
        GeneratedLetterDto.class);

    String result = given()
        .pathParam("id", id)
        .accept("multipart/form-data")
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(200).extract().asString();

    // splitting the results on the line basis so as to parse the results for json string
    // and byte data.
    // Rest assured doesnt support parsing of Multipart response, So we have to manually
    // parse the results.
    String[] results = result.split("\n");

    int position = 0;
    String jsonString = "";
    String byteString = "";

    for (String part : results) {
      if (part.contains("application/json")) {

        jsonString = results[position + 4].trim();

      }
      if (part.contains("application/octet-stream")) {

        byteString = results[position + 4].trim();
      }
      position++;
    }

    ObjectMapper mapper = new ObjectMapper();
    GeneratedLetterDto actual = mapper.readValue(jsonString, GeneratedLetterDto.class);

    actual.setLetterContent(byteString.getBytes(StandardCharsets.UTF_8));
    Assert.assertEquals(expected, actual);

    verify(manager).get(id);
  }

  public void testGetGeneratedLettersContentsById() throws Exception {

    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    int id = TestUtilities.nextId();

    doReturn(letter).when(manager).get(id);
    int targetId = getSecurityContext().getUser().getUserId();
    letter.setTargetId(targetId);
    letter.setLetterId(id);

    byte[] actual = given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}/contents")
        .then()
        .assertThat()
        .statusCode(200).extract().asByteArray();

    byte[] expected = letter.getLetterContent();
    assertTrue(Arrays.equals(expected, actual));

    verify(manager).get(id);
  }


  public void testGetGeneratedLettersContentsWithNoData() throws Exception {

    int id = TestUtilities.nextId();

    doReturn(null).when(manager).get(id);

    given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}/contents")
        .then()
        .assertThat()
        .statusCode(404).extract().asByteArray();

    verify(manager).get(id);
  }


  @Test
  public void testGetGeneratedLettersByIdWitNoData() throws ProtossException {
    int id = TestUtilities.nextId();
    doReturn(null).when(manager).get(id);

    String actual = given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(404).extract().asString();

    Assert.assertTrue(actual.isEmpty());
    verify(manager).get(id);
  }


  @Test
  public void testGetGeneratedLettersByIdWithoutAccess() throws ProtossException {
    // test
    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    int id = TestUtilities.nextId();
    doReturn(letter).when(manager).get(id);

    given()
        .pathParam("id", id)
        .accept("multipart/form-data")
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(200).extract().asString();



    verify(manager).get(id);
  }

  @Test
  public void testGetGeneratedLettersByIdWitNoDataFound() throws ProtossException {
    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    int id = TestUtilities.nextId();
    doReturn(null).when(manager).get(id);
    int targetId = getSecurityContext().getUser().getUserId();
    letter.setTargetId(targetId);

    given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(404).extract().asString();

    verify(manager).get(id);
  }


  @Test
  public void testUpdateLetterStatusWithInvalidStatus() throws ProtossException {

    GeneratedLetterDto generatedLetterDto = new GeneratedLetterDto();
    generatedLetterDto.setId(TestUtilities.nextId());
    String randomStatus = TestUtilities.nextString(10);
    given()
        .pathParam("generatedLetterId", generatedLetterDto.getId())
        .body(randomStatus)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/generated-letters/{generatedLetterId}/status")
        .then()
        .assertThat()
        .statusCode(400);

    verify(manager, never()).updateStatus(any(GeneratedLetter.class), any(AuditLogUser.class));
  }


  @Test
  public void testUpdateLetterStatus() throws ProtossException {

    ReferralStatus status = TestUtilities.nextValue(ReferralStatus.class);
    GeneratedLetterDto generatedLetterDto = new GeneratedLetterDto();
    generatedLetterDto.setStatus(status);
    generatedLetterDto.setId(TestUtilities.nextId());
    generatedLetterDto.setTargetId(targetId);
    GeneratedLetter letter = mapDto(generatedLetterDto, GeneratedLetter.class);

    doReturn(letter).when(manager).getWithNoContent(letter.getId());
    given()
        .pathParam("generatedLetterId", generatedLetterDto.getId())
        .body(status)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/generated-letters/{generatedLetterId}/status")
        .then()
        .assertThat()
        .statusCode(200);

    verify(manager).updateStatus(any(GeneratedLetter.class), any(AuditLogUser.class));
  }


}
