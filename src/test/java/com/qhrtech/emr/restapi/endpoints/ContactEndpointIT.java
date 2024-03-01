
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.medicalhistory.ContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.db.medicalhistory.ContactFixture;
import com.qhrtech.emr.accuro.db.medicalhistory.ExternalContactIdentifierFixture;
import com.qhrtech.emr.accuro.db.medicalhistory.ExternalContactSystemFixture;
import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.medicalhistory.Contact;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ContactDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ExternalContactIdentifierDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ContactEndpointIT extends AbstractEndpointIntegrationTest<ContactEndpoint> {

  private ContactManager contactManager;
  private ExternalContactIdentifierManager externalContactIdentifierManager;

  public ContactEndpointIT() throws IOException {
    super(new ContactEndpoint(), ContactEndpoint.class);
    contactManager = new DefaultContactManager(ds);
    externalContactIdentifierManager = new DefaultExternalContactIdentifierManager(ds);
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
    managerMap.put(ContactManager.class, contactManager);
    managerMap.put(ExternalContactIdentifierManager.class, externalContactIdentifierManager);
    return managerMap;
  }

  @Test
  public void getContactById() throws ProtossException, SQLException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());
    ContactDto expected = mapDto(contactFixture.get(), ContactDto.class);

    try {
      ContactDto actual = given()
          .pathParam("contactId", expected.getContactId())
          .when()
          .get(getBaseUrl() + "/v1/contacts/{contactId}")
          .then()
          .assertThat()
          .statusCode(200)
          .extract().as(ContactDto.class);

      assertEquals(expected, actual);
    } finally {
      deleteContact(expected);
    }
  }

  @Test
  public void returnNullForGetContactById() {
    int contactId = RandomUtils.nextInt();
    given().pathParams("contactId", contactId)
        .when()
        .get(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testSearchByExternalIdAndSystem()
      throws UnsupportedSchemaVersionException, SQLException {
    ExternalContactIdentifierFixture externalContactIdentifier =
        new ExternalContactIdentifierFixture();

    Connection conn = getConnection();
    externalContactIdentifier.setUp(conn);

    ContactDto contactDto = mapDto(externalContactIdentifier.getContact(), ContactDto.class);

    EnvelopeDto<ContactDto> expected = new EnvelopeDto<>();
    expected.setTotal(1);
    expected.setCount(1);
    expected.setLastId((long) contactDto.getContactId());
    expected.setContents(Arrays.asList(contactDto));

    EnvelopeDto rawActual = given()
        .queryParam("externalContactId", externalContactIdentifier.get().getValue())
        .queryParam("externalContactSystemId",
            externalContactIdentifier.getExternalContactSystem().getSystemIdentifier())
        .queryParam("pageSize", -1)
        .when()
        .get(getBaseUrl() + "/v1/contacts/search")
        .then()
        .assertThat().statusCode(Status.OK.getStatusCode())
        .extract().as(EnvelopeDto.class);

    EnvelopeDto<ContactDto> actual = refType(rawActual);
    assertEquals(expected, actual);

  }

  @Test
  public void testSearch()
      throws UnsupportedSchemaVersionException, SQLException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());
    ContactDto contactDto = mapDto(contactFixture.get(), ContactDto.class);
    EnvelopeDto<ContactDto> expected = new EnvelopeDto<>();
    expected.setTotal(1);
    expected.setCount(1);
    expected.setLastId((long) contactDto.getContactId());
    expected.setContents(Arrays.asList(contactDto));

    try {
      EnvelopeDto rawActual = given()
          .queryParam("contactName", contactDto.getContactName())
          .queryParam("pageSize", -1)
          .when()
          .get(getBaseUrl() + "/v1/contacts/search")
          .then()
          .assertThat().statusCode(Status.OK.getStatusCode())
          .extract().as(EnvelopeDto.class);

      EnvelopeDto<ContactDto> actual = refType(rawActual);
      assertEquals(expected, actual);
    } finally {
      deleteContact(contactDto);
    }

  }

  private EnvelopeDto<ContactDto> refType(EnvelopeDto rawEnvelope) {
    List<ContactDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<ContactDto>>() {});
    EnvelopeDto<ContactDto> e = (EnvelopeDto<ContactDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  @Test
  public void getExternalContactIdentifier() throws SQLException,
      UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    ExternalContactIdentifierFixture fixture = new ExternalContactIdentifierFixture();
    fixture.setUp(getConnection());
    if (fixture.get().getIsDeleted()) {
      updateExternalContact(fixture.get().getContactId());
    }

    ExternalContactIdentifierDto expected =
        mapDto(fixture.get(), ExternalContactIdentifierDto.class);

    try {
      ExternalContactIdentifierDto actual = given()
          .pathParams("contactId", expected.getContactId())
          .pathParams("systemIdentifier", expected.getExternalSystemIdentifier())
          .when()
          .get(
              getBaseUrl()
                  + "/v1/contacts/{contactId}/external-contact-identifiers/{systemIdentifier}")
          .then()
          .assertThat().statusCode(Status.OK.getStatusCode())
          .extract()
          .as(ExternalContactIdentifierDto.class);
      assertEquals(expected, actual);
    } finally {
      deleteExternalContact(expected);
    }
  }

  @Test
  public void getExternalContactIdentifiersByContactId()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {
    ExternalContactIdentifierFixture fixture = new ExternalContactIdentifierFixture();
    fixture.setUp(getConnection());
    if (fixture.get().getIsDeleted()) {
      updateExternalContact(fixture.get().getContactId());
    }

    ExternalContactIdentifierDto identifierDto =
        mapDto(fixture.get(), ExternalContactIdentifierDto.class);
    Set<ExternalContactIdentifierDto> expected = Collections.singleton(identifierDto);

    try {
      Set<ExternalContactIdentifierDto> actual = toCollection(
          given()
              .pathParams("contactId", identifierDto.getContactId())
              .when()
              .get(
                  getBaseUrl() + "/v1/contacts/{contactId}/external-contact-identifiers")
              .then()
              .assertThat()
              .statusCode(Status.OK.getStatusCode())
              .extract()
              .as(ExternalContactIdentifierDto[].class),
          HashSet::new);
      assertEquals(expected, actual);
    } finally {
      deleteExternalContact(identifierDto);
    }
  }

  @Test
  public void returnNullForGetExternalContactByContactId() throws ProtossException {
    int contactId = RandomUtils.nextInt();
    String externalSystemIdentifier = RandomStringUtils.randomAlphanumeric(9);

    // test
    given().pathParams("contactId", contactId)
        .pathParams("systemIdentifier", externalSystemIdentifier)
        .when()
        .get(
            getBaseUrl()
                + "/v1/contacts/{contactId}/external-contact-identifiers/{systemIdentifier}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testCreateExternalContactIdentifier()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, SQLException,
      NoDataFoundException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());

    ExternalContactSystemFixture systemFixture = new ExternalContactSystemFixture();
    systemFixture.setUp(getConnection());

    ExternalContactIdentifierDto dto = getFixture(ExternalContactIdentifierDto.class);
    dto.setContactId(contactFixture.get().getContactId());
    dto.setExternalSystemIdentifier(systemFixture.get().getSystemIdentifier());

    try {
      given()
          .pathParam("id", dto.getContactId())
          .body(dto)
          .when()
          .contentType(JSON)
          .post(getBaseUrl() + "/v1/contacts/{id}/external-contact-identifiers")
          .then()
          .assertThat()
          .statusCode(Status.CREATED.getStatusCode());
    } finally {
      deleteExternalContact(dto);
    }
  }

  @Test
  public void testCreateContact()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());
    Contact protossContact = contactFixture.get();
    protossContact.setAliases(getAliases());
    protossContact.setCity(TestUtilities.nextString(50));
    ContactDto contactDto = mapDto(protossContact, ContactDto.class);

    try {
      int actual = given()
          .body(contactDto)
          .when()
          .contentType(JSON)
          .post(getBaseUrl() + "/v1/contacts")
          .then()
          .assertThat()
          .statusCode(Status.CREATED.getStatusCode())
          .extract().as(Integer.class);

      Contact existing = contactManager.getById(actual);

      // set newly generated contact id
      contactDto.setContactId(actual);
      ContactDto actualDto = mapDto(existing, ContactDto.class);

      assertEquals(actualDto, contactDto);

    } finally {
      deleteContact(contactDto);
    }
  }


  @Test
  public void testUpdateContact()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());
    Contact protossContact = contactFixture.get();
    protossContact.setAliases(getAliases());
    ContactDto contactDto = mapDto(protossContact, ContactDto.class);
    contactDto.setContactName(TestUtilities.nextString(50));
    contactDto.setCity(TestUtilities.nextString(50));

    int id = contactDto.getContactId();
    try {
      given()
          .body(contactDto)
          .pathParam("contactId", id)
          .when()
          .contentType(JSON)
          .put(getBaseUrl() + "/v1/contacts/{contactId}")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode());

      Contact existing = contactManager.getById(id);
      ContactDto actualDto = mapDto(existing, ContactDto.class);

      assertEquals(actualDto, contactDto);

    } finally {
      deleteContact(contactDto);
    }
  }


  @Test
  public void testDeleteContact()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {
    ContactFixture contactFixture = new ContactFixture();
    contactFixture.setUp(getConnection());
    ContactDto contactDto = mapDto(contactFixture.get(), ContactDto.class);

    int id = contactDto.getContactId();
    try {
      given()
          .pathParam("contactId", id)
          .when()
          .delete(getBaseUrl() + "/v1/contacts/{contactId}")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode());

      contactManager.getById(id);

    } catch (NoDataFoundException e) {
      // Nothing to do
      // Protoss throws no data found for deleted contact
    } finally {
      deleteContact(contactDto);
    }
  }

  private void updateExternalContact(int id) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("UPDATE external_contact SET deleted = 0 WHERE contact_id = ? ", id);
    execute(query);
  }

  private void deleteExternalContact(ExternalContactIdentifierDto identifier)
      throws SQLException, DatabaseInteractionException, NoDataFoundException,
      UnsupportedSchemaVersionException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM external_contact WHERE contact_id = ? ", identifier.getContactId());
    execute(query);

    query = new PreparedStatementBuffer();
    query.append("DELETE FROM external_contact_system WHERE system_identifier = ? ",
        identifier.getExternalSystemIdentifier());
    execute(query);

    Contact contact = contactManager.getById(identifier.getContactId());
    deleteContact(mapDto(contact, ContactDto.class));
  }

  private void deleteContact(ContactDto contact) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM contacts WHERE contact_id = ? ", contact.getContactId());
    execute(query);

    query = new PreparedStatementBuffer();
    query.append("DELETE FROM contact_alias WHERE contact_id = ? ", contact.getContactId());
    execute(query);

    query = new PreparedStatementBuffer();
    query.append("DELETE FROM location WHERE location_id = ? ",
        contact.getContactLocation().getLocationId());
    execute(query);
  }

  private void execute(PreparedStatementBuffer query) throws SQLException {
    try (PreparedStatement s = query.generateStatement(getConnection())) {
      s.executeUpdate();
    }
  }

  private List<String> getAliases() {
    List<String> alias = new ArrayList<>();
    alias.add(TestUtilities.nextString(10));
    alias.add(TestUtilities.nextString(10));
    alias.add(TestUtilities.nextString(10));
    return alias;
  }
}
