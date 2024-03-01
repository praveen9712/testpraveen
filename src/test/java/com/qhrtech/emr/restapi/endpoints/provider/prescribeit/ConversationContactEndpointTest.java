
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.synapse.ConversationContact;
import com.qhrtech.emr.accuro.model.synapse.UserConversationContact;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationContactDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.UserConversationContactDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Assert;
import org.junit.Test;

public class ConversationContactEndpointTest extends
    AbstractEndpointTest<ConversationContactEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  ConversationContactManager conversationContactManager;
  String baseUrl = getBaseUrl() + "/v1/provider-portal/conversation-contacts";

  public ConversationContactEndpointTest() {
    super(new ConversationContactEndpoint(), ConversationContactEndpoint.class);

    conversationContactManager = mock(ConversationContactManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(ConversationContactManager.class, conversationContactManager);
    return map;
  }

  @Test
  public void getConversationContactById() throws ProtossException {
    ConversationContact protossResult = getFixture(ConversationContact.class);
    int contactId = protossResult.getContactId();
    when(conversationContactManager.getContactById(contactId)).thenReturn(protossResult);

    ConversationContactDto expected = mapDto(protossResult, ConversationContactDto.class);

    // test
    ConversationContactDto actual =
        given().pathParams("contactId", contactId)
            .when()
            .get(baseUrl + "/{contactId}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ConversationContactDto.class);

    assertEquals(actual, expected);
    verify(conversationContactManager).getContactById(contactId);
  }

  @Test
  public void getConversationContactByInvalidId() throws ProtossException {
    int id = TestUtilities.nextId();
    given().pathParams("contactId", id)
        .when()
        .get(baseUrl + "/{contactId}")
        .then()
        .assertThat().statusCode(404);

    verify(conversationContactManager).getContactById(id);
  }

  @Test
  public void getConversationContactsWithoutIdentifier() throws ProtossException {
    Envelope<ConversationContact> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = TestUtilities.nextId();

    when(conversationContactManager.searchConversationContacts(null, startingId, pageSize))
        .thenReturn(protossResult);

    EnvelopeDto<ConversationContactDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(conversationContactManager).searchConversationContacts(null, startingId, pageSize);
  }

  @Test
  public void getConversationContactsWithIdentifier() throws ProtossException {
    Envelope<ConversationContact> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = TestUtilities.nextId();
    String identifier = TestUtilities.nextString(10);

    when(conversationContactManager.searchConversationContacts(identifier, startingId, pageSize))
        .thenReturn(protossResult);

    EnvelopeDto<ConversationContactDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("identifier", identifier)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(conversationContactManager).searchConversationContacts(identifier, startingId, pageSize);
  }

  @Test
  public void getConversationContactsWithNegativePageSize() throws ProtossException {
    Envelope<ConversationContact> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1000) * (-1);
    Integer startingId = TestUtilities.nextInt();
    String identifier = TestUtilities.nextString(10);

    when(conversationContactManager
        .searchConversationContacts(identifier, startingId, DEFAULT_PAGE_SIZE))
            .thenReturn(protossResult);

    EnvelopeDto<ConversationContactDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("identifier", identifier)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(conversationContactManager)
        .searchConversationContacts(identifier, startingId, DEFAULT_PAGE_SIZE);
    verify(conversationContactManager, never())
        .searchConversationContacts(identifier, startingId, pageSize);
  }

  @Test
  public void getConversationContactsWithOverMaxPageSize() throws ProtossException {
    Envelope<ConversationContact> protossResult = getEnvelopeForProtoss();
    Integer pageSize = TestUtilities.nextInt(1000) + 51;
    Integer startingId = TestUtilities.nextInt();
    String identifier = TestUtilities.nextString(10);

    when(conversationContactManager
        .searchConversationContacts(identifier, startingId, MAX_PAGE_SIZE))
            .thenReturn(protossResult);

    EnvelopeDto<ConversationContactDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("identifier", identifier)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(conversationContactManager)
        .searchConversationContacts(identifier, startingId, MAX_PAGE_SIZE);
    verify(conversationContactManager, never())
        .searchConversationContacts(identifier, startingId, pageSize);
  }

  @Test
  public void getConversationContactsWithOverNullPageSize() throws ProtossException {
    Envelope<ConversationContact> protossResult = getEnvelopeForProtoss();
    Integer pageSize = null;
    Integer startingId = TestUtilities.nextInt();
    String identifier = TestUtilities.nextString(10);

    when(conversationContactManager
        .searchConversationContacts(identifier, startingId, DEFAULT_PAGE_SIZE))
            .thenReturn(protossResult);

    EnvelopeDto<ConversationContactDto> expected = transformForApi(protossResult);

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("identifier", identifier)
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    verify(conversationContactManager)
        .searchConversationContacts(identifier, startingId, DEFAULT_PAGE_SIZE);
    verify(conversationContactManager, never())
        .searchConversationContacts(identifier, startingId, pageSize);
  }

  @Test
  public void createConversationContact() throws ProtossException {
    ConversationContact contact = getFixture(ConversationContact.class);
    int expected = contact.getContactId();
    when(conversationContactManager.createContact(contact)).thenReturn(expected);
    ConversationContactDto contactDto = mapDto(contact, ConversationContactDto.class);

    // test
    int actual =
        given()
            .body(contactDto)
            .when()
            .contentType(ContentType.JSON)
            .post(baseUrl)
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .as(Integer.class);

    assertEquals(actual, expected);
    verify(conversationContactManager).getContactByIdentifierAndService(contact.getIdentifier(),
        contactDto.getService());
    verify(conversationContactManager).createContact(contact);
  }

  @Test
  public void createConversationContactWithExistingIdentifier() throws ProtossException {
    ConversationContact contact = getFixture(ConversationContact.class);
    String identifier = contact.getIdentifier();
    String service = contact.getService();
    when(conversationContactManager.getContactByIdentifierAndService(identifier, service))
        .thenReturn(contact);
    ConversationContactDto contactDto = mapDto(contact, ConversationContactDto.class);

    given()
        .body(contactDto)
        .when()
        .contentType(ContentType.JSON)
        .post(baseUrl)
        .then()
        .assertThat()
        .statusCode(Status.CONFLICT.getStatusCode());

    verify(conversationContactManager).getContactByIdentifierAndService(identifier, service);
    verify(conversationContactManager, never()).createContact(any());
  }

  @Test
  public void createConversationContactWithNull() throws ProtossException {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(baseUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationContactManager, never()).createContact(any());
  }

  @Test
  public void linkConversationUserContact() throws ProtossException {
    UserConversationContact userContact = getFixture(UserConversationContact.class);

    when(conversationContactManager.getUserContacts(userContact.getContactId()))
        .thenReturn(Collections.emptyList());

    given()
        .when()
        .pathParam("contactId", userContact.getContactId())
        .pathParam("userId", userContact.getUserId())
        .put(baseUrl + "/{contactId}/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationContactManager).createUserContact(any());
  }

  @Test
  public void linkConversationUserContactExisting() throws ProtossException {
    UserConversationContact userContact = getFixture(UserConversationContact.class);

    when(conversationContactManager.getUserContacts(userContact.getContactId()))
        .thenReturn(Arrays.asList(userContact));

    given()
        .when()
        .pathParam("contactId", userContact.getContactId())
        .pathParam("userId", userContact.getUserId())
        .put(baseUrl + "/{contactId}/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationContactManager, never()).createUserContact(any());
  }


  private Envelope<ConversationContact> getEnvelopeForProtoss() {
    Envelope<ConversationContact> envelope = new Envelope<>();
    List<ConversationContact> contacts = getFixtures(ConversationContact.class, ArrayList::new, 5);
    envelope.setContents(contacts);
    envelope.setCount(contacts.size());
    envelope.setLastId(TestUtilities.nextLong());
    envelope.setTotal(TestUtilities.nextId());
    return envelope;
  }

  private EnvelopeDto<ConversationContactDto> transformForApi(
      Envelope<ConversationContact> protossEnvelop) {
    EnvelopeDto<ConversationContactDto> envelope = new EnvelopeDto<>();
    envelope.setContents(
        mapDto(protossEnvelop.getContents(), ConversationContactDto.class, ArrayList::new));
    envelope.setCount(protossEnvelop.getCount());
    envelope.setLastId(protossEnvelop.getLastId());
    envelope.setTotal(protossEnvelop.getTotal());
    return envelope;
  }

  private EnvelopeDto<ConversationContactDto> refType(EnvelopeDto rawEnvelope) {
    List<ConversationContactDto> contents = getObjectMapper()
        .convertValue(rawEnvelope.getContents(),
            new TypeReference<List<ConversationContactDto>>() {});
    EnvelopeDto<ConversationContactDto> envelope =
        (EnvelopeDto<ConversationContactDto>) rawEnvelope;
    envelope.setContents(contents);
    return envelope;
  }

  @Test
  public void getUserConversationContact() throws ProtossException {

    UserConversationContact link = getFixture(UserConversationContact.class);
    int contactId = link.getContactId();
    List<UserConversationContact> protossResult = Arrays.asList(link);
    when(conversationContactManager.getUserContacts(contactId))
        .thenReturn(protossResult);

    List<UserConversationContactDto> expected =
        mapDto(protossResult, UserConversationContactDto.class, ArrayList::new);

    // test
    List<UserConversationContactDto> actual = toCollection(
        given()
            .when()
            .pathParam("contactId", contactId)
            .get(baseUrl + "/{contactId}/users")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(UserConversationContactDto[].class),
        ArrayList::new);

    // assertions
    actual.get(0).setLastSyncTime(expected.get(0).getLastSyncTime());
    Assert.assertEquals(expected, actual);
    verify(conversationContactManager).getUserContacts(contactId);
  }



}
