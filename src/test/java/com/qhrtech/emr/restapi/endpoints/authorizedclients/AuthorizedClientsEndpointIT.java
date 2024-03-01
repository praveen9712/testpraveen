
package com.qhrtech.emr.restapi.endpoints.authorizedclients;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.api.security.DefaultAuthorizedClientManager;
import com.qhrtech.emr.accuro.db.security.AuthorizedClientFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.authorizedclients.AuthorizedClientDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class AuthorizedClientsEndpointIT extends
    AbstractEndpointIntegrationTest<AuthorizedClientsEndpoint> {

  private AuthorizedClientManager manager;

  public AuthorizedClientsEndpointIT() throws IOException {
    super(new AuthorizedClientsEndpoint(), AuthorizedClientsEndpoint.class);
    manager = new DefaultAuthorizedClientManager(getDs(), null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(defaultUser());
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(AuthorizedClientManager.class, manager);
    return managerMap;
  }


  @Test
  public void testGetById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {
    AuthorizedClientFixture fixture = new AuthorizedClientFixture();
    fixture.setUp(getConnection());
    AuthorizedClientDto authorizedClientDto = mapDto(fixture.get(), AuthorizedClientDto.class);

    AuthorizedClientDto actual = given()
        .pathParam("id", authorizedClientDto.getId())
        .when()
        .get(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(AuthorizedClientDto.class);

    assertEquals(authorizedClientDto, actual);
  }

  @Test
  public void testSearch() throws SQLException, UnsupportedSchemaVersionException {
    AuthorizedClientFixture fixture = new AuthorizedClientFixture();
    fixture.setUp(getConnection());
    AuthorizedClientDto authorizedClientDto = mapDto(fixture.get(), AuthorizedClientDto.class);
    EnvelopeDto<AuthorizedClientDto> expected = new EnvelopeDto<>();
    expected.setTotal(1);
    expected.setCount(1);
    expected.setLastId((long) authorizedClientDto.getId());
    expected.setContents(Arrays.asList(authorizedClientDto));

    // test
    EnvelopeDto rawActual = given()
        .queryParam("clientId", authorizedClientDto.getClientId())
        .queryParam("clientName", authorizedClientDto.getClientName())
        .when()
        .get(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<AuthorizedClientDto> actual = refType(rawActual);
    assertEquals(expected, actual);

  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    AuthorizedClientDto authorizedClientDto = new AuthorizedClientDto();
    authorizedClientDto.setClientId(TestUtilities.nextString(256));
    authorizedClientDto.setClientName(TestUtilities.nextString(256));
    AuthorizedClientDto actual = given()
        .body(authorizedClientDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/authorized-clients")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(AuthorizedClientDto.class);

    AuthorizedClient authorizedClientById = manager.getAuthorizedClientById(actual.getId());

    AuthorizedClientDto getById = mapDto(authorizedClientById, AuthorizedClientDto.class);

    assertEquals(getById, actual);
    assertTrue(authorizedClientDto.getClientName().equals(actual.getClientName()));
    assertTrue(authorizedClientDto.getClientId().equals(actual.getClientId()));
  }

  @Test
  public void testUpdate() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException, SQLException {
    AuthorizedClientFixture fixture = new AuthorizedClientFixture();
    fixture.setUp(getConnection());
    AuthorizedClientDto updatedDto = mapDto(fixture.get(), AuthorizedClientDto.class);
    updatedDto.setClientName(TestUtilities.nextString(256));

    given()
        .body(updatedDto)
        .pathParam("id", updatedDto.getId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());
    AuthorizedClient authorizedClientById = manager.getAuthorizedClientById(updatedDto.getId());
    AuthorizedClientDto getById = mapDto(authorizedClientById, AuthorizedClientDto.class);
    assertEquals(updatedDto, getById);
  }

  @Test
  public void testDelete() throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException, SQLException {
    AuthorizedClientFixture fixture = new AuthorizedClientFixture();
    fixture.setUp(getConnection());
    AuthorizedClientDto authorizedClientDto = mapDto(fixture.get(), AuthorizedClientDto.class);
    int id = authorizedClientDto.getId();
    given()
        .pathParam("id", id)
        .when()
        .delete(getBaseUrl() + "/v1/authorized-clients/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    AuthorizedClient authorizedClientById =
        manager.getAuthorizedClientById(authorizedClientDto.getId());
    assertTrue(authorizedClientById == null);
  }

  private EnvelopeDto<AuthorizedClientDto> refType(EnvelopeDto rawEnvelope) {
    List<AuthorizedClientDto> contents = getObjectMapper().convertValue(rawEnvelope.getContents(),
        new TypeReference<>() {});
    EnvelopeDto<AuthorizedClientDto> e = (EnvelopeDto<AuthorizedClientDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

}
