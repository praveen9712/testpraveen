
package com.qhrtech.emr.restapi.endpoints.authorizedclients;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.authorizedclients.AuthorizedClientDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class AuthorizedClientsEndpointTest extends AbstractEndpointTest<AuthorizedClientsEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  private AuthorizedClientManager manager;


  public AuthorizedClientsEndpointTest() {
    super(new AuthorizedClientsEndpoint(), AuthorizedClientsEndpoint.class);
    manager = mock(AuthorizedClientManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(getFixture(AuditLogUser.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(AuthorizedClientManager.class, manager);
    return managerMap;
  }

  @Test
  public void testGetById() throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    AuthorizedClient protossResult = getFixture(AuthorizedClient.class);
    int id = protossResult.getId();
    when(manager.getAuthorizedClientById(id)).thenReturn(protossResult);

    AuthorizedClientDto expected = mapDto(protossResult, AuthorizedClientDto.class);

    AuthorizedClientDto actual = given().pathParams("id", id)
        .when()
        .get(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(AuthorizedClientDto.class);

    assertEquals(actual, expected);
    verify(manager).getAuthorizedClientById(id);

  }

  @Test
  public void testGetClientsByAllParams()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<AuthorizedClient> protossEnvelope = getProtossEnvelope();
    UUID uuid = UUID.randomUUID();
    String clientName = TestUtilities.nextString(256);
    String clientId = TestUtilities.nextString(256);
    Integer userId = TestUtilities.nextInt();
    int startingId =
        getProtossEnvelope().getContents().stream()
            .min(Comparator.comparing(AuthorizedClient::getId))
            .orElseThrow(RuntimeException::new).getId();
    doReturn(protossEnvelope).when(manager)
        .search(clientId, uuid, clientName, userId, startingId - 1, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("clientUuid", uuid.toString())
        .queryParam("clientName", clientName)
        .queryParam("clientId", clientId)
        .queryParam("serviceUserId", userId)
        .queryParam("startingId", startingId - 1)
        .queryParam("pageSize", DEFAULT_PAGE_SIZE)
        .get(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<AuthorizedClientDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(manager).search(clientId, uuid, clientName, userId, startingId - 1, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetClientsByNoParams()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<AuthorizedClient> protossEnvelope = getProtossEnvelope();
    doReturn(protossEnvelope).when(manager)
        .search(null, null, null, null, null, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .get(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<AuthorizedClientDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(manager).search(null, null, null, null, null, DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetClientsByMaxPageSize()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<AuthorizedClient> protossEnvelope = getProtossEnvelope();
    int pageSize = TestUtilities.nextInt(200, 1000);
    doReturn(protossEnvelope).when(manager)
        .search(null, null, null, null, null, MAX_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<AuthorizedClientDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(manager).search(null, null, null, null, null, MAX_PAGE_SIZE);
  }


  private EnvelopeDto<AuthorizedClientDto> refType(EnvelopeDto rawEnvelope) {
    List<AuthorizedClientDto> contents = getObjectMapper().convertValue(rawEnvelope.getContents(),
        new TypeReference<>() {});
    EnvelopeDto<AuthorizedClientDto> e = (EnvelopeDto<AuthorizedClientDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  @Test
  public void testGetClientById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    AuthorizedClient protossResult = getFixture(AuthorizedClient.class);
    doReturn(protossResult).when(manager).getAuthorizedClientById(protossResult.getId());

    // test
    AuthorizedClientDto actual = given()
        .when()
        .pathParam("id", protossResult.getId())
        .get(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(AuthorizedClientDto.class);

    // assertions
    assertEquals(mapDto(protossResult, AuthorizedClientDto.class), actual);
    verify(manager).getAuthorizedClientById(protossResult.getId());
  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    AuthorizedClient authorizedClient = getFixture(AuthorizedClient.class);
    AuthorizedClientDto expected = mapDto(authorizedClient, AuthorizedClientDto.class);
    doReturn(authorizedClient.getId()).when(manager).createAuthorizedClient(authorizedClient);
    doReturn(authorizedClient).when(manager).getAuthorizedClientById(authorizedClient.getId());
    AuthorizedClientDto actual = given()
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(AuthorizedClientDto.class);

    assertEquals(expected, actual);
    verify(manager).createAuthorizedClient(authorizedClient);
  }

  @Test
  public void testCreateNoBody() {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(manager);
  }

  @Test
  public void testUpdate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    AuthorizedClient authorizedClient = getFixture(AuthorizedClient.class);
    AuthorizedClientDto expected = mapDto(authorizedClient, AuthorizedClientDto.class);
    doNothing().when(manager).updateAuthorizedClient(authorizedClient);
    given()
        .body(expected)
        .pathParam("id", expected.getId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).updateAuthorizedClient(authorizedClient);
  }

  @Test
  public void testUpdateIdNotMatch() {
    AuthorizedClient authorizedClient = getFixture(AuthorizedClient.class);
    AuthorizedClientDto expected = mapDto(authorizedClient, AuthorizedClientDto.class);

    given()
        .body(expected)
        .pathParam("id", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testDelete() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    int id = TestUtilities.nextInt();
    doNothing().when(manager).deleteAuthorizedClient(id);
    given()
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteAuthorizedClient(id);
  }


  private Envelope<AuthorizedClient> getProtossEnvelope() {
    List<AuthorizedClient> protossResult = getFixtures(AuthorizedClient.class, ArrayList::new, 10);
    Envelope<AuthorizedClient> envelope = new Envelope<>();
    long lastId = protossResult.stream().max(Comparator.comparing(AuthorizedClient::getId))
        .orElseThrow(RuntimeException::new).getId();
    envelope.setContents(protossResult);
    envelope.setCount(protossResult.size());
    envelope.setLastId(lastId);
    envelope.setTotal(protossResult.size());
    return envelope;
  }

  private EnvelopeDto<AuthorizedClientDto> getExpectedEnvelope(
      Envelope<AuthorizedClient> protossEnvelope) {
    List<AuthorizedClientDto> authorizedClientDtos =
        mapDto(protossEnvelope.getContents(), AuthorizedClientDto.class,
            ArrayList::new);

    EnvelopeDto<AuthorizedClientDto> expected = new EnvelopeDto<>();

    expected.setContents(authorizedClientDtos);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

}
