
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationContactManager;
import com.qhrtech.emr.accuro.db.security.AccuroUserFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationContactFixture;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.synapse.ConversationContact;
import com.qhrtech.emr.accuro.model.synapse.UserConversationContact;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationContactDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.UserConversationContactDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Assert;
import org.junit.Test;

public class ConversationContactEndpointIT
    extends AbstractEndpointIntegrationTest<ConversationContactEndpoint> {


  private final ConversationContactManager conversationContactManager;
  private AuditLogUser user;
  final String baseUrl = getBaseUrl() + "/v1/provider-portal/conversation-contacts";

  public ConversationContactEndpointIT() throws IOException {
    super(new ConversationContactEndpoint(), ConversationContactEndpoint.class);
    conversationContactManager = new DefaultConversationContactManager(ds, null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(ConversationContactManager.class, conversationContactManager);
    return servicesMap;
  }

  @Test
  public void getConversationContactById() throws ProtossException, SQLException {
    ConversationContactFixture contactFixture = new ConversationContactFixture();
    contactFixture.setUp(getConnection());

    ConversationContact contact = contactFixture.get();
    int contactId = contact.getContactId();

    ConversationContactDto expected = mapDto(contact, ConversationContactDto.class);
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
    conversationContactManager.deleteContact(contactId);
  }

  @Test
  public void getConversationContacts() throws ProtossException, SQLException {
    ConversationContactFixture contactFixture = new ConversationContactFixture();
    contactFixture.setUp(getConnection());

    ConversationContact contact = contactFixture.get();

    Integer pageSize = TestUtilities.nextInt(1, 50);
    Integer startingId = contact.getContactId() - 1;


    EnvelopeDto<ConversationContactDto> expected =
        transformForApi(getEnvelopeForProtoss(Arrays.asList(contact)));

    // test
    EnvelopeDto rawEnvelope =
        given()
            .when()
            .queryParam("pageSize", pageSize)
            .queryParam("startingId", startingId)
            .queryParam("identifier", contact.getIdentifier())
            .get(baseUrl)
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(EnvelopeDto.class);

    EnvelopeDto<ConversationContactDto> actual = refType(rawEnvelope);

    assertEquals(actual, expected);
    conversationContactManager.deleteContact(contact.getContactId());
  }

  @Test
  public void createConversationContact() throws ProtossException {
    ConversationContact contact = getFixture(ConversationContact.class);
    ConversationContactDto contactDto = mapDto(contact, ConversationContactDto.class);
    ConversationContactDto expected = mapDto(contact, ConversationContactDto.class);
    // test
    int id =
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

    expected.setContactId(id);

    ConversationContactDto actual =
        mapDto(conversationContactManager.getContactById(id), ConversationContactDto.class);
    assertEquals(actual, expected);
    conversationContactManager.deleteContact(id);
  }

  @Test
  public void linkConversationUserContact() throws ProtossException, SQLException {
    AccuroUserFixture accuroUserFixture = new AccuroUserFixture();
    ConversationContactFixture contactFixture = new ConversationContactFixture();
    contactFixture.setUp(getConnection());
    accuroUserFixture.setUp(getConnection());

    int contactId = contactFixture.get().getContactId();
    int userId = accuroUserFixture.get().getUserId();

    given()
        .when()
        .pathParam("contactId", contactId)
        .pathParam("userId", userId)
        .put(baseUrl + "/{contactId}/users/{userId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    List<UserConversationContact> existinglinks =
        conversationContactManager.getUserContacts(contactId);

    assertTrue(existinglinks.stream().anyMatch(l -> l.getUserId() == userId));
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

  private Envelope<ConversationContact> getEnvelopeForProtoss(List<ConversationContact> contacts) {
    Envelope<ConversationContact> envelope = new Envelope<>();
    envelope.setContents(contacts);
    envelope.setCount(contacts.size());
    long lastId = contacts.get(contacts.size() - 1).getContactId();
    envelope.setLastId(lastId);
    envelope.setTotal(contacts.size());
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
  public void getUserConversationContact() throws ProtossException, SQLException {

    AccuroUserFixture accuroUserFixture = new AccuroUserFixture();
    ConversationContactFixture contactFixture = new ConversationContactFixture();
    contactFixture.setUp(getConnection());
    accuroUserFixture.setUp(getConnection());

    int contactId = contactFixture.get().getContactId();

    UserConversationContact link = new UserConversationContact();
    link.setUserId(accuroUserFixture.get().getUserId());
    link.setContactId(contactId);
    conversationContactManager.createUserContact(link);

    link = conversationContactManager.getUserContacts(contactId).get(0);

    List<UserConversationContactDto> expected =
        mapDto(Arrays.asList(link), UserConversationContactDto.class, ArrayList::new);

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
  }


}
