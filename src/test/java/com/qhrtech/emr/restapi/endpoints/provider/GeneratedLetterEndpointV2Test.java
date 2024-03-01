
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.accuro.api.letters.GeneratedLetterManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.letters.GeneratedLetter;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.GeneratedLetterDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class GeneratedLetterEndpointV2Test extends AbstractEndpointTest<GeneratedLetterEndpointV2> {

  private static int targetId = TestUtilities.nextInt();
  private GeneratedLetterManager manager;

  public GeneratedLetterEndpointV2Test() {
    super(new GeneratedLetterEndpointV2(), GeneratedLetterEndpointV2.class);
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

    // setup random data
    List<GeneratedLetter> protossLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    Envelope<GeneratedLetter> protossEnvelope = new Envelope<>();
    long lastId = protossLetters.stream().max(Comparator.comparing(GeneratedLetter::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossLetters);

    EnvelopeDto<GeneratedLetterDto> letterDtoEnvelopeDto = new EnvelopeDto<>();
    List<GeneratedLetterDto> expected =
        mapDto(protossLetters, GeneratedLetterDto.class, ArrayList::new);
    letterDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    letterDtoEnvelopeDto.setTotal(expected.size());
    letterDtoEnvelopeDto.setCount(expected.size());
    letterDtoEnvelopeDto.setLastId(lastId);

    Integer patientId = TestUtilities.nextInt();
    Integer userId = TestUtilities.nextId();

    List<GeneratedLetter> generatedLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    for (GeneratedLetter generatedLetter : generatedLetters) {
      generatedLetter.setPatientId(patientId);
      generatedLetter.setUserId(userId);
      generatedLetter.setTargetId(0);
      generatedLetter.setLetterContent(null);
    }

    LocalDate startDate = new LocalDate();
    LocalDate endDate = startDate.plusDays(2);
    Integer appointmentId = TestUtilities.nextId();

    Integer startingId = null;
    // random number between 1-49
    int pageSize = 60;

    when(manager.getAllWithNoContent(userId, patientId, startDate, endDate,
        appointmentId, 50, startingId))
            .thenReturn(protossEnvelope);

    String startAndEndDateString = new LocalDate().toString();

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("userId", userId)
            .queryParam("patientId", patientId)
            .queryParam("createdStartDate", startDate.toString())
            .queryParam("createdEndDate", endDate.toString())
            .queryParam("appointmentId", appointmentId)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/generated-letters")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<GeneratedLetterDto> actual = reType(rawActual);
    assertEquals(letterDtoEnvelopeDto, actual);
    verify(manager).getAllWithNoContent(userId, patientId, startDate, endDate,
        appointmentId, 50, startingId);

  }


  @Test
  public void testGetGeneratedLettersWithNullPageSize() throws ProtossException {

    // setup random data
    List<GeneratedLetter> protossLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    Envelope<GeneratedLetter> protossEnvelope = new Envelope<>();
    long lastId = protossLetters.stream().max(Comparator.comparing(GeneratedLetter::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossLetters);

    EnvelopeDto<GeneratedLetterDto> letterDtoEnvelopeDto = new EnvelopeDto<>();
    List<GeneratedLetterDto> expected =
        mapDto(protossLetters, GeneratedLetterDto.class, ArrayList::new);
    letterDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    letterDtoEnvelopeDto.setTotal(expected.size());
    letterDtoEnvelopeDto.setCount(expected.size());
    letterDtoEnvelopeDto.setLastId(lastId);

    Integer patientId = TestUtilities.nextInt();
    Integer userId = TestUtilities.nextId();

    List<GeneratedLetter> generatedLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    for (GeneratedLetter generatedLetter : generatedLetters) {
      generatedLetter.setPatientId(patientId);
      generatedLetter.setUserId(userId);
      generatedLetter.setTargetId(0);
      generatedLetter.setLetterContent(null);
    }

    LocalDate startDate = new LocalDate();
    LocalDate endDate = startDate.plusDays(2);
    Integer appointmentId = TestUtilities.nextId();

    Integer startingId = null;
    Integer pageSize = null;

    when(manager.getAllWithNoContent(userId, patientId, startDate, endDate,
        appointmentId, 25, startingId))
            .thenReturn(protossEnvelope);

    // test
    EnvelopeDto rawActual =
        given()
            .queryParam("userId", userId)
            .queryParam("patientId", patientId)
            .queryParam("createdStartDate", startDate.toString())
            .queryParam("createdEndDate", endDate.toString())
            .queryParam("appointmentId", appointmentId)
            .queryParam("startingId", startingId)
            .queryParam("pageSize", pageSize)
            .when()
            .get(getBaseUrl() + "/v2/provider-portal/generated-letters")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(EnvelopeDto.class);

    EnvelopeDto<GeneratedLetterDto> actual = reType(rawActual);
    assertEquals(letterDtoEnvelopeDto, actual);
    verify(manager).getAllWithNoContent(userId, patientId, startDate, endDate,
        appointmentId, 25, startingId);

  }

  @Test
  public void testGetGeneratedLettersWithWrongDates() throws ProtossException {

    // setup random data
    List<GeneratedLetter> protossLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    Envelope<GeneratedLetter> protossEnvelope = new Envelope<>();
    long lastId = protossLetters.stream().max(Comparator.comparing(GeneratedLetter::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossLetters);

    EnvelopeDto<GeneratedLetterDto> letterDtoEnvelopeDto = new EnvelopeDto<>();
    List<GeneratedLetterDto> expected =
        mapDto(protossLetters, GeneratedLetterDto.class, ArrayList::new);
    letterDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    letterDtoEnvelopeDto.setTotal(expected.size());
    letterDtoEnvelopeDto.setCount(expected.size());
    letterDtoEnvelopeDto.setLastId(lastId);

    Integer patientId = TestUtilities.nextInt();
    Integer userId = TestUtilities.nextId();

    List<GeneratedLetter> generatedLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    for (GeneratedLetter generatedLetter : generatedLetters) {
      generatedLetter.setPatientId(patientId);
      generatedLetter.setUserId(userId);
      generatedLetter.setTargetId(0);
      generatedLetter.setLetterContent(null);
    }

    LocalDate endDate = new LocalDate();
    LocalDate startDate = endDate.plusDays(2);
    Integer appointmentId = TestUtilities.nextId();

    Integer startingId = null;
    // random number between 1-49
    int pageSize = TestUtilities.nextInt((49 - 1) + 1) + 1;

    given()
        .queryParam("userId", userId)
        .queryParam("patientId", patientId)
        .queryParam("createdStartDate", startDate.toString())
        .queryParam("createdEndDate", endDate.toString())
        .queryParam("appointmentId", appointmentId)
        .queryParam("startingId", startingId)
        .queryParam("pageSize", pageSize)
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters")
        .then()
        .assertThat()
        .statusCode(400);

    verify(manager, never()).getAllWithNoContent(userId, patientId, startDate, endDate,
        appointmentId, pageSize, startingId);

  }

  @Test
  public void testGetGeneratedLettersWithWrongDateValues() throws ProtossException {

    // setup random data
    List<GeneratedLetter> protossLetters = getFixtures(GeneratedLetter.class, ArrayList::new, 5);
    Envelope<GeneratedLetter> protossEnvelope = new Envelope<>();
    long lastId = protossLetters.stream().max(Comparator.comparing(GeneratedLetter::getId))
        .orElseThrow(RuntimeException::new).getId();
    protossEnvelope.setCount(5);
    protossEnvelope.setTotal(5);
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setContents(protossLetters);

    EnvelopeDto<GeneratedLetterDto> letterDtoEnvelopeDto = new EnvelopeDto<>();
    List<GeneratedLetterDto> expected =
        mapDto(protossLetters, GeneratedLetterDto.class, ArrayList::new);
    letterDtoEnvelopeDto.setContents(new ArrayList<>(expected));
    letterDtoEnvelopeDto.setTotal(expected.size());
    letterDtoEnvelopeDto.setCount(expected.size());
    letterDtoEnvelopeDto.setLastId(lastId);

    Integer patientId = TestUtilities.nextInt();
    Integer userId = TestUtilities.nextId();

    LocalDate endDate = new LocalDate();

    given()
        .queryParam("userId", userId)
        .queryParam("patientId", patientId)
        .queryParam("createdStartDate", "TestValue")
        .queryParam("createdEndDate", endDate.toString())
        .queryParam("appointmentId", TestUtilities.nextInt())
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters")
        .then()
        .assertThat()
        .statusCode(400);

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
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters/{id}")
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
  public void testGetGeneratedLettersByIdMulipartWithTiff() throws Exception {
    GeneratedLetter letter = getFixture(GeneratedLetter.class);
    letter.setLetterContent(TestUtilities.nextString(10).getBytes());
    letter.setExtension("tiff");
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
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters/{id}")
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
      if (part.contains("image/tiff")) {

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

  @Test
  public void testGetGeneratedLettersByIdWitNoData() throws ProtossException {
    int id = TestUtilities.nextId();
    doReturn(null).when(manager).get(id);

    String actual = given()
        .pathParam("id", id)
        .when()
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters/{id}")
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
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters/{id}")
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
        .get(getBaseUrl() + "/v2/provider-portal/generated-letters/{id}")
        .then()
        .assertThat()
        .statusCode(404).extract().asString();

    verify(manager).get(id);
  }

  private EnvelopeDto<GeneratedLetterDto> reType(EnvelopeDto rawEnvelope) {
    List<GeneratedLetterDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<GeneratedLetterDto>>() {});
    EnvelopeDto<GeneratedLetterDto> e = (EnvelopeDto<GeneratedLetterDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }


}
