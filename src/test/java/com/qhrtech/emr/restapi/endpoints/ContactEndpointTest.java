
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.medicalhistory.ContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ExternalContactSystemManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.medicalhistory.Contact;
import com.qhrtech.emr.accuro.model.medicalhistory.ExternalContactIdentifier;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.AddressContactType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ContactDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ExternalContactIdentifierDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ContactEndpointTest extends AbstractEndpointTest<ContactEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;
  private ContactManager contactManager;
  private ExternalContactIdentifierManager externalContactIdentifierManager;
  private ExternalContactSystemManager externalContactSystemManager;


  public ContactEndpointTest() {
    super(new ContactEndpoint(), ContactEndpoint.class);
    contactManager = mock(ContactManager.class);
    externalContactIdentifierManager = mock(ExternalContactIdentifierManager.class);
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
    managerMap.put(ContactManager.class, contactManager);
    managerMap.put(ExternalContactIdentifierManager.class, externalContactIdentifierManager);
    return managerMap;
  }

  @Test
  public void getContactById() throws ProtossException {
    Contact protossResult = getFixture(Contact.class);
    int contactId = protossResult.getContactId();
    when(contactManager.getById(contactId)).thenReturn(protossResult);

    ContactDto expected =
        mapDto(protossResult, ContactDto.class);

    // test
    ContactDto actual =
        given().pathParams("contactId", contactId)
            .when()
            .get(getBaseUrl() + "/v1/contacts/{contactId}")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .extract()
            .as(ContactDto.class);

    assertEquals(actual, expected);
    verify(contactManager).getById(contactId);
  }

  @Test
  public void returnNullForGetContactById() throws ProtossException {
    int contactId = RandomUtils.nextInt();

    // test
    given().pathParams("contactId", contactId)
        .when()
        .get(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());

    verify(contactManager).getById(contactId);
  }

  @Test
  public void testSearchAllFields()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    AddressContactType contactType =
        TestUtilities.nextValue(AddressContactType.class);
    String contactName = TestUtilities.nextString(200);
    String externalContactId = TestUtilities.nextString(10);
    String externalContactSystemId = TestUtilities.nextString(10);

    com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType protossAddressType =
        com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType
            .lookupType(contactType.getTypeId());

    int startingId =
        getProtossEnvelope().getContents().stream().min(Comparator.comparing(Contact::getContactId))
            .orElseThrow(RuntimeException::new).getContactId();

    Envelope<Contact> protossEnvelope = getProtossEnvelope();

    when(contactManager.search(protossAddressType, contactName,
        externalContactId, externalContactSystemId, startingId - 1, DEFAULT_PAGE_SIZE))
            .thenReturn(protossEnvelope);

    // test
    EnvelopeDto rawActual = given()
        .queryParam("contactType", contactType)
        .queryParam("contactName", contactName)
        .queryParam("externalContactId", externalContactId)
        .queryParam("externalContactSystemId", externalContactSystemId)
        .queryParam("startingId", startingId - 1)
        .queryParam("pageSize", -1)
        .when()
        .get(getBaseUrl() + "/v1/contacts/search")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ContactDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(contactManager).search(protossAddressType,
        contactName, externalContactId, externalContactSystemId, startingId - 1, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchAllFieldsStartingIdPageSizeBlank()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    AddressContactType contactType =
        TestUtilities.nextValue(AddressContactType.class);
    String contactName = TestUtilities.nextString(200);

    com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType protossAddressType =
        com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType
            .lookupType(contactType.getTypeId());

    int startingId =
        getProtossEnvelope().getContents().stream().min(Comparator.comparing(Contact::getContactId))
            .orElseThrow(RuntimeException::new).getContactId();

    Envelope<Contact> protossEnvelope = getProtossEnvelope();

    when(contactManager.search(protossAddressType, contactName,
        null, null, null, DEFAULT_PAGE_SIZE)).thenReturn(protossEnvelope);

    // test
    EnvelopeDto rawActual = given()
        .queryParam("contactType", contactType)
        .queryParam("contactName", contactName)
        .when()
        .get(getBaseUrl() + "/v1/contacts/search")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ContactDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(contactManager).search(protossAddressType,
        contactName, null, null, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testSearchInvalidEnum() {
    // test
    given()
        .queryParam("contactType", TestUtilities.nextString(50))
        .queryParam("startingId", TestUtilities.nextInt())
        .queryParam("pageSize", 1000)
        .when()
        .get(getBaseUrl() + "/v1/contacts/search")
        .then()
        .assertThat().statusCode(400);

  }

  @Test
  public void testSearchMaxPageSize()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    AddressContactType contactType =
        TestUtilities.nextValue(AddressContactType.class);
    String contactName = TestUtilities.nextString(200);

    com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType protossAddressType =
        com.qhrtech.emr.accuro.model.medicalhistory.AddressContactType
            .lookupType(contactType.getTypeId());

    int startingId =
        getProtossEnvelope().getContents().stream().min(Comparator.comparing(Contact::getContactId))
            .orElseThrow(RuntimeException::new).getContactId();

    Envelope<Contact> protossEnvelope = getProtossEnvelope();

    when(contactManager.search(protossAddressType, contactName,
        null, null, startingId - 1, MAX_PAGE_SIZE)).thenReturn(protossEnvelope);

    // test
    EnvelopeDto rawActual = given()
        .queryParam("contactType", contactType)
        .queryParam("contactName", contactName)
        .queryParam("startingId", startingId - 1)
        .queryParam("pageSize", 1000)
        .when()
        .get(getBaseUrl() + "/v1/contacts/search")
        .then()
        .assertThat().statusCode(200).extract().as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ContactDto> actual = refType(rawActual);
    assertEquals(getExpected(protossEnvelope), actual);
    verify(contactManager).search(protossAddressType,
        contactName, null, null, startingId - 1, MAX_PAGE_SIZE);

  }


  private Envelope<Contact> getProtossEnvelope() {
    List<Contact> protossResults = getFixtures(Contact.class, ArrayList::new, 10);
    Envelope<Contact> protossEnvelope = new Envelope<>();
    long lastId = protossResults.stream().max(Comparator.comparing(Contact::getContactId))
        .orElseThrow(RuntimeException::new).getContactId();
    protossEnvelope.setContents(protossResults);
    protossEnvelope.setCount(protossResults.size());
    protossEnvelope.setLastId(lastId);
    protossEnvelope.setTotal(protossResults.size());
    return protossEnvelope;
  }

  private EnvelopeDto<ContactDto> getExpected(Envelope<Contact> protossEnvelope) {
    List<ContactDto> contacts =
        mapDto(protossEnvelope.getContents(), ContactDto.class, ArrayList::new);

    EnvelopeDto<ContactDto> expected = new EnvelopeDto<>();

    expected.setContents(contacts);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  private EnvelopeDto<ContactDto> refType(EnvelopeDto rawEnvelope) {
    List<ContactDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<ContactDto>>() {});
    EnvelopeDto<ContactDto> e = (EnvelopeDto<ContactDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }


  /* ExternalContactIdentifier tests */

  @Test
  public void getExternalContactIdentifier()
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException {
    ExternalContactIdentifier protossExternalContactIdentifierResult =
        getFixture(ExternalContactIdentifier.class);

    String externalSystemIdentifier = RandomStringUtils.randomAlphanumeric(9);

    when(externalContactIdentifierManager
        .getExternalContactIdentifier(protossExternalContactIdentifierResult.getContactId(),
            externalSystemIdentifier))
                .thenReturn(protossExternalContactIdentifierResult);

    ExternalContactIdentifierDto expectedDto =
        mapDto(protossExternalContactIdentifierResult, ExternalContactIdentifierDto.class);

    int contactId = protossExternalContactIdentifierResult.getContactId();

    // Test
    ExternalContactIdentifierDto actual = given()
        .pathParams("contactId", contactId)
        .pathParams("systemIdentifier", externalSystemIdentifier)
        .when()
        .get(
            getBaseUrl()
                + "/v1/contacts/{contactId}/external-contact-identifiers/{systemIdentifier}")
        .then()
        .assertThat().statusCode(Status.OK.getStatusCode()).extract()
        .as(ExternalContactIdentifierDto.class);

    assertEquals(expectedDto, actual);
  }

  @Test
  public void getExternalContactIdentifiersByContactId()
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException {
    Set<ExternalContactIdentifier> protossResults =
        getFixtures(ExternalContactIdentifier.class, HashSet::new, 2);

    // Mapping done here as the standard mapDto() does not map the ExternalContactSystem information
    Set<ExternalContactIdentifierDto> expected = new HashSet<>();
    for (Iterator<ExternalContactIdentifier> iterator = protossResults.iterator(); iterator
        .hasNext();) {

      ExternalContactIdentifierDto currentIdentifier =
          mapDto(iterator.next(), ExternalContactIdentifierDto.class);

      expected.add(currentIdentifier);
    }

    int contactId = RandomUtils.nextInt();

    // Have the Manager return a set of <ExternalContactIdentifiers>
    when(externalContactIdentifierManager
        .getExternalContactIdentifiersByContactId(contactId))
            .thenReturn(protossResults);

    // Test
    Set<ExternalContactIdentifierDto> actual = toCollection(
        given()
            .pathParams("contactId", contactId)
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
    verify(externalContactIdentifierManager).getExternalContactIdentifiersByContactId(contactId);
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

    verify(externalContactIdentifierManager)
        .getExternalContactIdentifier(contactId, externalSystemIdentifier);
  }

  @Test
  public void testCreateExternalContactIdentifier()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ExternalContactIdentifierDto dto = getFixture(ExternalContactIdentifierDto.class);

    given()
        .pathParam("id", dto.getContactId())
        .body(dto)
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/contacts/{id}/external-contact-identifiers")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode());

    verify(externalContactIdentifierManager)
        .createExternalContactIdentifier(mapDto(dto, ExternalContactIdentifier.class));
  }

  @Test
  public void testCreateExternalContactIdentifierIdNotMatch() {
    ExternalContactIdentifierDto dto = getFixture(ExternalContactIdentifierDto.class);

    given()
        .pathParam("id", TestUtilities.nextInt())
        .body(dto)
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/contacts/{id}/external-contact-identifiers")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(externalContactIdentifierManager);
  }

  @Test
  public void testCreateContact()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    ContactDto contactDto = getFixture(ContactDto.class);
    int contactId = contactDto.getContactId();
    AuditLogUser user = testObject.getUser();
    Contact contact = mapDto(contactDto, Contact.class);
    when(contactManager.create(contact, user)).thenReturn(contactId);



    int id = given()
        .body(contactDto)
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/contacts")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode()).extract().as(Integer.class);

    assertTrue(id == contactId);
    verify(contactManager).create(contact, user);

  }

  @Test
  public void testCreateContactMaxAliasSize() {
    ContactDto contactDto = getFixture(ContactDto.class);
    contactDto.setAliases(getAliases());
    given()
        .body(contactDto)
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/contacts")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(contactManager);

  }

  @Test
  public void testCreateContactNullBody()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    Contact protossResult = getFixture(Contact.class);
    when(contactManager.create(protossResult, testObject.getUser()))
        .thenReturn(protossResult.getContactId());

    given()
        .when()
        .contentType(JSON)
        .post(getBaseUrl() + "/v1/contacts")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(contactManager);

  }

  @Test
  public void testUpdateContact()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    ContactDto contactDto = getFixture(ContactDto.class);

    int contactId = contactDto.getContactId();

    given()
        .pathParam("contactId", contactId)
        .body(contactDto)
        .when()
        .contentType(JSON)
        .put(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());
    Contact contact = mapDto(contactDto, Contact.class);
    verify(contactManager).update(contact, testObject.getUser());

  }

  @Test
  public void testUpdateContactIdNotMatched() {
    ContactDto contactDto = getFixture(ContactDto.class);

    given()
        .pathParam("contactId", TestUtilities.nextInt())
        .body(contactDto)
        .when()
        .contentType(JSON)
        .put(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(contactManager);

  }

  @Test
  public void testUpdateContactAliasMaxSize() {
    ContactDto contactDto = getFixture(ContactDto.class);
    contactDto.setAliases(getAliases());
    int contactId = contactDto.getContactId();

    given()
        .pathParam("contactId", contactId)
        .body(contactDto)
        .when()
        .contentType(JSON)
        .put(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
    verifyNoInteractions(contactManager);
  }

  @Test
  public void testUpdateContactNullBody()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    Contact protossResult = getFixture(Contact.class);
    when(contactManager.create(protossResult, testObject.getUser()))
        .thenReturn(protossResult.getContactId());

    given()
        .pathParam("contactId", protossResult.getContactId())
        .when()
        .contentType(JSON)
        .put(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(contactManager);

  }

  @Test
  public void testUpdateContactIdNotMatch()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    Contact protossResult = getFixture(Contact.class);
    when(contactManager.create(protossResult, testObject.getUser()))
        .thenReturn(protossResult.getContactId());

    ContactDto contactDto = mapDto(protossResult, ContactDto.class);

    given()
        .pathParam("contactId", TestUtilities.nextInt())
        .body(contactDto)
        .when()
        .contentType(JSON)
        .put(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(contactManager);

  }

  @Test
  public void testDeleteContact()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException {
    int contactId = TestUtilities.nextInt();

    given()
        .pathParam("contactId", contactId)
        .when()
        .contentType(JSON)
        .delete(getBaseUrl() + "/v1/contacts/{contactId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());

    verify(contactManager).delete(contactId, testObject.getUser());
  }


  private List<String> getAliases() {
    List<String> alias = new ArrayList<>();
    alias.add(TestUtilities.nextString(10));
    alias.add(TestUtilities.nextString(10));
    alias.add(TestUtilities.nextString(10));
    alias.add(null);
    alias.add(" ");
    alias.add(TestUtilities.nextString(101));
    return alias;
  }

}
