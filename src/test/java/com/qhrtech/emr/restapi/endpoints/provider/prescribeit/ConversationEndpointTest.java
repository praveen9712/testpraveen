
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentContentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageStatusManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationPatientManager;
import com.qhrtech.emr.accuro.model.eprescribe.ExternalPatient;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.synapse.Conversation;
import com.qhrtech.emr.accuro.model.synapse.ConversationContact;
import com.qhrtech.emr.accuro.model.synapse.ConversationExternalPatient;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessage;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachment;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachmentContent;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageStatus;
import com.qhrtech.emr.accuro.model.synapse.ConversationParticipant;
import com.qhrtech.emr.accuro.model.synapse.ConversationPatient;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageAttachmentDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageStatusDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationParticipantDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConversationEndpointTest extends AbstractEndpointTest<ConversationEndpoint> {

  private final ConversationManager conversationManager;
  private final ConversationMessageManager conversationMessageManager;
  private final ConversationContactManager conversationContactManager;
  private final ConversationMessageAttachmentManager attachmentManager;
  private final ConversationMessageAttachmentContentManager contentManager;
  private final ConversationMessageStatusManager messageStatusManager;
  private final ConversationExternalPatientManager conversationExternalPatientManager;
  private final ExternalPatientManager externalPatientManager;

  private final String endpointUrl = "/v1/provider-portal/conversations/";
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private AuditLogUser user;
  private final ConversationPatientManager patientManager;

  public ConversationEndpointTest() {
    super(new ConversationEndpoint(), ConversationEndpoint.class);
    conversationManager = mock(ConversationManager.class);
    conversationMessageManager = mock(ConversationMessageManager.class);
    conversationContactManager = mock(ConversationContactManager.class);
    attachmentManager = mock(ConversationMessageAttachmentManager.class);
    contentManager = mock(ConversationMessageAttachmentContentManager.class);
    messageStatusManager = mock(ConversationMessageStatusManager.class);
    patientManager = mock(ConversationPatientManager.class);
    conversationExternalPatientManager = mock(ConversationExternalPatientManager.class);
    externalPatientManager = mock(ExternalPatientManager.class);
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
    servicesMap.put(ConversationManager.class, conversationManager);
    servicesMap.put(ConversationMessageManager.class, conversationMessageManager);
    servicesMap.put(ConversationContactManager.class, conversationContactManager);
    servicesMap.put(ConversationMessageAttachmentManager.class, attachmentManager);
    servicesMap.put(ConversationMessageAttachmentContentManager.class, contentManager);
    servicesMap.put(ConversationMessageStatusManager.class, messageStatusManager);
    servicesMap.put(ConversationPatientManager.class, patientManager);
    servicesMap.put(ConversationExternalPatientManager.class, conversationExternalPatientManager);
    servicesMap.put(ExternalPatientManager.class, externalPatientManager);
    return servicesMap;
  }

  @Test
  public void testUpdateConversationPatientLink()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ConversationPatient conversationPatient = getFixture(ConversationPatient.class);

    doReturn(getFixture(Conversation.class)).when(conversationManager)
        .getConversationById(conversationPatient.getConversationId());

    given()
        .when()
        .pathParam("conversationId", conversationPatient.getConversationId())
        .pathParam("patientId", conversationPatient.getPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/patients/{patientId}")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    conversationPatient.setId(null);
    verify(patientManager).createPatient(conversationPatient);
    verify(patientManager).getPatientsForConversation(conversationPatient.getConversationId());
    verify(conversationManager).getConversationById(conversationPatient.getConversationId());
  }

  @Test
  public void testUpdateConversationPatientLinkAlreadyPresent()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ConversationPatient conversationPatient = getFixture(ConversationPatient.class);

    List<ConversationPatient> existingLinks = Collections.singletonList(conversationPatient);
    when(patientManager.getPatientsForConversation(conversationPatient.getConversationId()))
        .thenReturn(existingLinks);

    doReturn(getFixture(Conversation.class)).when(conversationManager)
        .getConversationById(conversationPatient.getConversationId());

    given()
        .when()
        .pathParam("conversationId", conversationPatient.getConversationId())
        .pathParam("patientId", conversationPatient.getPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/patients/{patientId}")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    conversationPatient.setId(null);
    verify(patientManager, times(0)).createPatient(conversationPatient);
    verify(patientManager).getPatientsForConversation(conversationPatient.getConversationId());
    verify(conversationManager).getConversationById(conversationPatient.getConversationId());
  }

  @Test
  public void testUpdateConversationPatientLinkConversationNotFound()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    ConversationPatient conversationPatient = getFixture(ConversationPatient.class);

    doReturn(null).when(conversationManager)
        .getConversationById(conversationPatient.getConversationId());

    given()
        .when()
        .pathParam("conversationId", conversationPatient.getConversationId())
        .pathParam("patientId", conversationPatient.getPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/patients/{patientId}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());

    verifyNoInteractions(patientManager);
    verify(conversationManager).getConversationById(conversationPatient.getConversationId());
  }

  @Test
  public void testUpdateConversationTaskGroupLink()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    int conversationId = TestUtilities.nextInt();
    UUID uuid = UUID.randomUUID();

    doReturn(getFixture(Conversation.class)).when(conversationManager)
        .getConversationById(conversationId);
    doNothing().when(conversationManager).addTaskGroupToConversation(conversationId, uuid);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("taskGroupUuid", uuid)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/task-groups/{taskGroupUuid}")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).addTaskGroupToConversation(conversationId, uuid);
  }

  @Test
  public void testUpdateConversationTaskGroupLinkInvalidUuid() {
    int conversationId = TestUtilities.nextInt();
    String uuid = TestUtilities.nextString(36);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("taskGroupUuid", uuid)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/task-groups/{taskGroupUuid}")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(conversationManager);
  }

  @Test
  public void testUpdateConversationTaskGroupLinkWithInvalidConversationId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    int conversationId = TestUtilities.nextInt();
    UUID uuid = UUID.randomUUID();

    doNothing().when(conversationManager).addTaskGroupToConversation(conversationId, uuid);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("taskGroupUuid", uuid)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/task-groups/{taskGroupUuid}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());


  }

  @Test
  public void testGetConversationById() throws Exception {
    Conversation protossResult = getFixture(Conversation.class);
    int id = protossResult.getConversationId();
    when(conversationManager.getConversationById(id)).thenReturn(protossResult);

    ConversationDto expected =
        mapDto(protossResult, ConversationDto.class);
    ConversationDto actual = given()
        .when()
        .pathParam("id", id)
        .get(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ConversationDto.class);

    assertEquals(expected, actual);
    verify(conversationManager).getConversationById(id);
  }

  @Test
  public void testGetConversationByIdWithNullMessageType()
      throws Exception {
    Conversation protossResult = getFixture(Conversation.class);
    int id = protossResult.getConversationId();
    when(conversationManager.getConversationById(id)).thenReturn(protossResult);

    ConversationDto expected =
        mapDto(protossResult, ConversationDto.class);
    ConversationDto actual = given()
        .when()
        .pathParam("id", id)
        .get(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ConversationDto.class);

    assertEquals(expected, actual);
    verify(conversationManager).getConversationById(id);

  }

  @Test
  public void testGetByIdNotFound()
      throws Exception {
    Conversation protossResult = getFixture(Conversation.class);
    int id = protossResult.getConversationId();
    when(conversationManager.getConversationById(id)).thenReturn(null);

    given()
        .when()
        .pathParam("id", id)
        .get(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationManager).getConversationById(id);

  }

  @Test
  public void testGetConversations() throws Exception {
    List<Conversation> protossResults = getFixtures(Conversation.class, ArrayList::new, 4);
    String externalIdentifier = protossResults.get(0).getExternalIdentifier();
    when(conversationManager.getByExternalIdentifier(externalIdentifier))
        .thenReturn(protossResults);

    List<ConversationDto> expected =
        mapDto(protossResults, ConversationDto.class, ArrayList::new);

    List<ConversationDto> actual = toCollection(
        given()
            .when()
            .queryParam("externalId", externalIdentifier)
            .get(getBaseUrl() + endpointUrl)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(ConversationDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(conversationManager).getByExternalIdentifier(externalIdentifier);

  }

  @Test
  public void testCreateConversation() throws Exception {
    ConversationDto conversationDto = getFixture(ConversationDto.class);

    Conversation protossModel =
        mapDto(conversationDto, Conversation.class);

    when(conversationManager.createConversation(protossModel, user))
        .thenReturn(protossModel.getConversationId());

    when(conversationContactManager.getContactById(protossModel.getOwner()))
        .thenReturn(new ConversationContact());

    int createdId = given()
        .body(conversationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .extract().as(Integer.class);

    assertTrue(conversationDto.getConversationId() == createdId);
    verify(conversationManager).createConversation(protossModel, user);

  }

  @Test
  public void testCreateConversationWithInvalidOwner()
      throws Exception {
    ConversationDto conversationDto = getFixture(ConversationDto.class);

    Conversation protossModel =
        mapDto(conversationDto, Conversation.class);

    when(conversationManager.createConversation(protossModel, user))
        .thenReturn(protossModel.getConversationId());

    when(conversationContactManager.getContactById(protossModel.getOwner()))
        .thenReturn(null);

    given()
        .body(conversationDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationManager, never()).createConversation(protossModel, user);
  }

  @Test
  public void testGetConversationMessageById() throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    Integer messageId = protossResult.getMessageId();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(protossResult);

    ConversationMessageDto expected =
        mapDto(protossResult, ConversationMessageDto.class);

    ConversationMessageDto actual = given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId())
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ConversationMessageDto.class);

    assertEquals(expected, actual);
    verify(conversationMessageManager).getMessageById(messageId);

  }

  @Test
  public void testGetConversationMessageByIdWithNullSubType()
      throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    protossResult.setMessageSubtype(null);

    Integer messageId = protossResult.getMessageId();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(protossResult);

    ConversationMessageDto expected =
        mapDto(protossResult, ConversationMessageDto.class);
    ConversationMessageDto actual = given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId())
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(ConversationMessageDto.class);

    assertEquals(expected, actual);
    verify(conversationMessageManager).getMessageById(messageId);

  }

  @Test
  public void testGetConversationMessageByIdNotFound()
      throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    int messageId = protossResult.getMessageId();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(null);

    given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId())
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationMessageManager).getMessageById(messageId);

  }

  @Test
  public void testGetMessageWithUnmatchedConversationId() throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    Integer messageId = protossResult.getMessageId();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(protossResult);

    given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId() + 1)
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(messageId);

  }

  @Test
  public void testCreateConversationMessage() throws Exception {
    ConversationMessageDto conversationMessageDto = getFixture(ConversationMessageDto.class);

    ConversationMessage protossModel =
        mapDto(conversationMessageDto, ConversationMessage.class);

    when(conversationMessageManager.createMessage(protossModel, user))
        .thenReturn(protossModel.getMessageId());

    Integer createdId = given()
        .body(conversationMessageDto)
        .when()
        .pathParam("conversationId", protossModel.getConversationId())
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "{conversationId}/messages")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(conversationMessageDto.getMessageId() == createdId);
    verify(conversationMessageManager).createMessage(protossModel, user);

  }

  @Test
  public void testCreateMessageWithUnmatchedConversation()
      throws Exception {
    ConversationMessageDto conversationMessageDto = getFixture(ConversationMessageDto.class);

    ConversationMessage protossModel =
        mapDto(conversationMessageDto, ConversationMessage.class);

    given()
        .body(conversationMessageDto)
        .when()
        .pathParam("conversationId", protossModel.getConversationId() + 1)
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "{conversationId}/messages")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager, never()).createMessage(protossModel, user);

  }

  @Test
  public void testCreateMessageWithNullSubtype() throws Exception {
    ConversationMessageDto conversationMessageDto = getFixture(ConversationMessageDto.class);

    conversationMessageDto.setMessageSubtype(null);

    ConversationMessage protossModel =
        mapDto(conversationMessageDto, ConversationMessage.class);

    when(conversationMessageManager.createMessage(protossModel, user))
        .thenReturn(protossModel.getMessageId());

    Integer createdId = given()
        .body(conversationMessageDto)
        .when()
        .pathParam("conversationId", protossModel.getConversationId())
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "{conversationId}/messages")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(conversationMessageDto.getMessageId() == createdId);
    verify(conversationMessageManager).createMessage(protossModel, user);

  }

  @Test
  public void testUpdateParticipant()
      throws DatabaseInteractionException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException {
    Set<ConversationParticipantDto> contactDtos =
        getFixtures(ConversationParticipantDto.class, HashSet::new, 10);
    int conversationId = TestUtilities.nextInt();

    given().body(contactDtos)
        .pathParam("id", conversationId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "{id}/participants")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    Set<ConversationParticipant> participants =
        mapDto(contactDtos, ConversationParticipant.class, HashSet::new);

    verify(conversationManager).updateConversationParticipants(conversationId, participants, user);

  }

  @Test
  public void testCreateParticipant()
      throws DatabaseInteractionException, SupportingResourceNotFoundException,
      TimeZoneNotFoundException, UnsupportedSchemaVersionException {
    ConversationParticipantDto contactDto = getFixture(ConversationParticipantDto.class);
    int conversationId = contactDto.getConversationId();

    given().body(contactDto)
        .pathParam("id", conversationId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "{id}/participants")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode());
    ConversationParticipant participant = mapDto(contactDto, ConversationParticipant.class);

    verify(conversationManager).createParticipant(participant, user);

  }

  @Test
  public void testGetParticipants()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    List<ConversationParticipantDto> expected =
        getFixtures(ConversationParticipantDto.class, ArrayList::new, 10);
    int conversationId = TestUtilities.nextInt();
    List<ConversationParticipant> protossResult =
        mapDto(expected, ConversationParticipant.class, ArrayList::new);

    when(conversationManager.getConversationParticipants(conversationId)).thenReturn(protossResult);

    List<ConversationParticipantDto> actual = toCollection(given()
        .when()
        .pathParam("id", conversationId)
        .get(getBaseUrl() + endpointUrl + "{id}/participants")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract().as(ConversationParticipantDto[].class),
        ArrayList::new);

    verify(conversationManager).getConversationParticipants(conversationId);
    assertEquals(expected, actual);

  }

  @Test
  public void testGetConversationMessageAttachment()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    ConversationMessageAttachmentDto expected =
        getFixture(ConversationMessageAttachmentDto.class);
    expected.setConversationMessageId(message.getMessageId());
    ConversationMessageAttachment attachment =
        mapDto(expected, ConversationMessageAttachment.class);

    when(attachmentManager.getAttachmentById(expected.getId())).thenReturn(attachment);

    ConversationMessageAttachmentDto actual =
        given()
            .when()
            .pathParam("conversationId", message.getConversationId())
            .pathParam("messageId", message.getMessageId())
            .pathParam("attachmentId", attachment.getId())
            .get(getBaseUrl() + endpointUrl
                + "{conversationId}/messages/{messageId}/attachments/{attachmentId}")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(ConversationMessageAttachmentDto.class);

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(attachmentManager).getAttachmentById(attachment.getId());
    assertEquals(expected, actual);
  }

  @Test
  public void testGetConversationMessageAttachmentWhenSupportingResourceNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    given()
        .when()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", TestUtilities.nextId())
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(anyInt());
    verify(attachmentManager, never()).getAttachmentById(anyInt());
  }

  @Test
  public void testCreateConversationMessageAttachment()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    ConversationMessageAttachment attachment =
        getFixture(ConversationMessageAttachment.class);
    attachment.setConversationMessageId(message.getMessageId());
    ConversationMessageAttachmentDto attachmentDto =
        mapDto(attachment, ConversationMessageAttachmentDto.class);
    int expected = attachmentDto.getId();

    when(attachmentManager.createAttachment(attachment)).thenReturn(expected);

    int actual = given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(attachmentDto)
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments")
        .then()
        .assertThat().statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertEquals(expected, actual);
    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(attachmentManager).createAttachment(attachment);
  }

  @Test
  public void testCreateConversationMessageAttachmentWithInvalidMessageId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessageAttachmentDto attachmentDto =
        getFixture(ConversationMessageAttachmentDto.class);

    given()
        .when()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .header("Content-Type", "application/json")
        .body(attachmentDto)
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager, never()).getMessageById(anyInt());
    verify(attachmentManager, never()).createAttachment(any());
  }

  @Test
  public void testCreateConversationMessageAttachmentWhenMessageNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageAttachment attachment =
        getFixture(ConversationMessageAttachment.class);
    attachment.setConversationMessageId(message.getMessageId());
    ConversationMessageAttachmentDto attachmentDto =
        mapDto(attachment, ConversationMessageAttachmentDto.class);

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(attachmentDto)
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(attachmentManager, never()).createAttachment(attachment);
  }

  @Test
  public void testGetConversationMessageAttachmentContent()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    int attachmentId = TestUtilities.nextInt();
    ConversationMessageAttachment attachment = getFixture(ConversationMessageAttachment.class);
    attachment.setId(attachmentId);
    attachment.setConversationMessageId(message.getMessageId());
    when(attachmentManager.getAttachmentById(attachmentId)).thenReturn(attachment);
    ConversationMessageAttachmentContent content =
        getFixture(ConversationMessageAttachmentContent.class);
    Conversation conversation = getFixture(Conversation.class);
    when(conversationManager.getConversationById(message.getConversationId())).thenReturn(
        conversation);

    when(contentManager.getContentsByAttachmentId(attachmentId,
        conversation.getConversationType())).thenReturn(
            content);

    byte[] actual = given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract().asByteArray();

    assertTrue(Arrays.equals(content.getContent(), actual));
    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(contentManager).getContentsByAttachmentId(attachmentId,
        conversation.getConversationType());
  }

  @Test
  public void testGetConversationMessageAttachmentContentWhenSupportingResourceNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    given()
        .when()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", TestUtilities.nextId())
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(anyInt());
    verify(contentManager, never()).getContentsByAttachmentId(anyInt(), any());
  }

  @Test
  public void testGetConversationMessageAttachmentContentWhenContentNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);
    Conversation conversation = getFixture(Conversation.class);
    when(conversationManager.getConversationById(message.getConversationId())).thenReturn(
        conversation);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    int attachmentId = TestUtilities.nextInt();

    ConversationMessageAttachment attachment = getFixture(ConversationMessageAttachment.class);
    attachment.setId(attachmentId);
    attachment.setConversationMessageId(message.getMessageId());

    when(attachmentManager.getAttachmentById(attachmentId)).thenReturn(attachment);

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(contentManager).getContentsByAttachmentId(anyInt(), any());
    verify(attachmentManager).getAttachmentById(attachmentId);
  }

  @Test
  public void testCreateConversationMessageAttachmentContent() throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);
    Conversation conversation = getFixture(Conversation.class);

    when(conversationManager.getConversationById(message.getConversationId())).thenReturn(
        conversation);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    File testFile = temporaryFolder.newFile("test.pdf");
    ConversationMessageAttachmentContent content =
        getFixture(ConversationMessageAttachmentContent.class);
    byte[] bytes = content.getContent();
    FileUtils.writeByteArrayToFile(testFile, bytes);
    int attachmentId = content.getAttachmentId();

    ConversationMessageAttachment attachment = getFixture(ConversationMessageAttachment.class);
    attachment.setId(attachmentId);
    attachment.setConversationMessageId(message.getMessageId());
    when(attachmentManager.getAttachmentById(attachmentId)).thenReturn(attachment);

    given()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .pathParam("attachmentId", attachmentId)
        .multiPart("file", testFile)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode());

    verify(contentManager).createContent(content, conversation.getConversationType());
    verify(attachmentManager).getAttachmentById(attachmentId);
    verify(conversationManager).getConversationById(message.getConversationId());
    verify(conversationMessageManager).getMessageById(message.getMessageId());
  }

  @Test
  public void testCreateConversationMessageAttachmentContentAttachmentDiff() throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);
    Conversation conversation = getFixture(Conversation.class);

    when(conversationManager.getConversationById(message.getConversationId())).thenReturn(
        conversation);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    File testFile = temporaryFolder.newFile("test.pdf");
    ConversationMessageAttachmentContent content =
        getFixture(ConversationMessageAttachmentContent.class);
    byte[] bytes = content.getContent();
    FileUtils.writeByteArrayToFile(testFile, bytes);
    int attachmentId = content.getAttachmentId();

    ConversationMessageAttachment attachment = getFixture(ConversationMessageAttachment.class);
    attachment.setId(attachmentId);
    when(attachmentManager.getAttachmentById(attachmentId)).thenReturn(attachment);

    given()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .pathParam("attachmentId", attachmentId)
        .multiPart("file", testFile)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verifyNoInteractions(contentManager);
    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(attachmentManager).getAttachmentById(attachmentId);
  }

  @Test
  public void testGetConversationMessageAttachmentContentAttachmentDifferent()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessage message = getFixture(ConversationMessage.class);
    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);

    int attachmentId = TestUtilities.nextInt();
    ConversationMessageAttachmentContent content =
        getFixture(ConversationMessageAttachmentContent.class);

    ConversationMessageAttachment attachment = getFixture(ConversationMessageAttachment.class);
    attachment.setId(attachmentId);
    when(attachmentManager.getAttachmentById(attachmentId)).thenReturn(attachment);
    Conversation conversation = getFixture(Conversation.class);
    when(conversationManager.getConversationById(message.getConversationId())).thenReturn(
        conversation);

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verifyNoInteractions(contentManager);
    verify(attachmentManager).getAttachmentById(attachmentId);
  }


  @Test
  public void testCreateConversationMessageAttachmentContentWhenMessageNotFound()
      throws Exception {
    File testFile = temporaryFolder.newFile("test.pdf");

    given()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", TestUtilities.nextId())
        .multiPart("file", testFile)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(contentManager, never()).createContent(any(), any());
  }

  @Test
  public void testGetMessageStatuses() throws Exception {
    List<ConversationMessageStatusDto> expected =
        getFixtures(ConversationMessageStatusDto.class, ArrayList::new, 3);
    ConversationMessage message = getFixture(ConversationMessage.class);
    int conversationId = message.getConversationId();
    int messageId = message.getMessageId();
    List<ConversationMessageStatus> protossResult =
        mapDto(expected, ConversationMessageStatus.class, ArrayList::new);
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(message);
    when(messageStatusManager.getMessageStatus(messageId)).thenReturn(protossResult);

    List<ConversationMessageStatusDto> actual = toCollection(given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(ConversationMessageStatusDto[].class),
        ArrayList::new);

    verify(messageStatusManager).getMessageStatus(messageId);
    assertEquals(expected, actual);

  }

  @Test
  public void testGetMessageStatusesWithMessageNotFound() throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    int conversationId = 1;
    int messageId = message.getMessageId();

    when(conversationMessageManager.getMessageById(messageId)).thenReturn(message);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(messageId);
  }

  @Test
  public void testGetMessageStatusesWithInvalidConversationId() throws Exception {

    int conversationId = 1;
    int messageId = 1;
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(null);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("messageId", messageId)
        .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationMessageManager).getMessageById(messageId);
  }

  @Test
  public void testSaveConversationMessageStatusAsCreate()
      throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageStatus status =
        getFixture(ConversationMessageStatus.class);
    status.setMessageId(message.getMessageId());
    ConversationMessageStatusDto statusDto =
        mapDto(status, ConversationMessageStatusDto.class);

    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);
    when(messageStatusManager.getMessageStatus(message.getMessageId()))
        .thenReturn(Collections.emptyList());

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(statusDto)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(messageStatusManager).createMessageStatus(status, user);
  }

  @Test
  public void testSaveConversationMessageStatusAsUpdate()
      throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageStatus status =
        getFixture(ConversationMessageStatus.class);
    status.setMessageId(message.getMessageId());
    ConversationMessageStatusDto statusDto =
        mapDto(status, ConversationMessageStatusDto.class);

    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);
    when(messageStatusManager.getMessageStatus(message.getMessageId()))
        .thenReturn(Collections.singletonList(status));

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(statusDto)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(messageStatusManager).updateMessageStatus(status, user);
  }

  @Test
  public void testSaveConversationMessageStatusMessageNotFound()
      throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageStatus status =
        getFixture(ConversationMessageStatus.class);
    status.setMessageId(message.getMessageId());
    ConversationMessageStatusDto statusDto =
        mapDto(status, ConversationMessageStatusDto.class);

    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(null);
    when(messageStatusManager.getMessageStatus(message.getMessageId()))
        .thenReturn(Collections.emptyList());

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(statusDto)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(messageStatusManager, never()).createMessageStatus(status, user);
    verify(messageStatusManager, never()).updateMessageStatus(status, user);
  }

  @Test
  public void testSaveConversationMessageStatusUnmatchMessageId()
      throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageStatus status =
        getFixture(ConversationMessageStatus.class);
    status.setMessageId(message.getMessageId());
    ConversationMessageStatusDto statusDto =
        mapDto(status, ConversationMessageStatusDto.class);

    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);
    when(messageStatusManager.getMessageStatus(message.getMessageId()))
        .thenReturn(Collections.singletonList(status));

    given()
        .when()
        .pathParam("conversationId", message.getConversationId())
        .pathParam("messageId", 1)
        .header("Content-Type", "application/json")
        .body(statusDto)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager, never()).getMessageById(message.getMessageId());
    verify(messageStatusManager, never()).updateMessageStatus(status, user);
    verify(messageStatusManager, never()).createMessageStatus(status, user);
  }

  @Test
  public void testSaveConversationMessageStatusInvalidConversationId()
      throws Exception {
    ConversationMessage message = getFixture(ConversationMessage.class);

    ConversationMessageStatus status =
        getFixture(ConversationMessageStatus.class);
    status.setMessageId(message.getMessageId());
    ConversationMessageStatusDto statusDto =
        mapDto(status, ConversationMessageStatusDto.class);

    when(conversationMessageManager.getMessageById(message.getMessageId())).thenReturn(message);
    when(messageStatusManager.getMessageStatus(message.getMessageId()))
        .thenReturn(Collections.singletonList(status));

    given()
        .when()
        .pathParam("conversationId", 1)
        .pathParam("messageId", message.getMessageId())
        .header("Content-Type", "application/json")
        .body(statusDto)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/statuses")
        .then()
        .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationMessageManager).getMessageById(message.getMessageId());
    verify(messageStatusManager, never()).updateMessageStatus(status, user);
    verify(messageStatusManager, never()).createMessageStatus(status, user);
  }

  @Test
  public void testUpdateConversationPrescriptionLink()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    int conversationId = TestUtilities.nextInt();
    int rxId = TestUtilities.nextId();

    doReturn(getFixture(Conversation.class)).when(conversationManager)
        .getConversationById(conversationId);
    doNothing().when(conversationManager).addPrescriptionToConversation(conversationId, rxId);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("prescriptionMedicationId", rxId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/medications/{prescriptionMedicationId}")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).addPrescriptionToConversation(conversationId, rxId);
  }

  @Test
  public void testUpdateConversationPrescriptionLinkWithInvalidConversaionId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    int conversationId = TestUtilities.nextInt();
    int rxId = TestUtilities.nextId();

    doNothing().when(conversationManager).addPrescriptionToConversation(conversationId, rxId);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .pathParam("prescriptionMedicationId", rxId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/medications/{prescriptionMedicationId}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationManager, never()).addPrescriptionToConversation(conversationId, rxId);
  }


  @Test
  public void testArchiveConversation()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    int conversationId = TestUtilities.nextInt();

    doNothing().when(conversationManager).archiveConversation(conversationId);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/archive")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).archiveConversation(conversationId);
  }

  @Test
  public void testUnarchiveConversation()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    int conversationId = TestUtilities.nextInt();

    doNothing().when(conversationManager).unarchiveConversation(conversationId);

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/unarchive")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).unarchiveConversation(conversationId);
  }

  @Test
  public void testDeleteConversationById() throws Exception {
    Conversation protossResult = getFixture(Conversation.class);
    int id = protossResult.getConversationId();

    given()
        .when()
        .pathParam("id", id)
        .delete(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).deleteAllConversationLinkedResources(id, user);
  }

  @Test
  public void testDeleteConversationMessageById() throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    Integer messageId = protossResult.getMessageId();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(protossResult);

    given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId())
        .pathParam("messageId", messageId)
        .delete(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(conversationManager).deleteConversationMessageAndLinks(messageId, user);

  }

  @Test
  public void testDeleteConversationMessageByInvalidId() throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    int messageId = TestUtilities.nextInt();
    when(conversationMessageManager.getMessageById(messageId)).thenReturn(null);

    given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId())
        .pathParam("messageId", messageId)
        .delete(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(conversationManager, never()).deleteConversationMessageAndLinks(messageId, user);

  }

  @Test
  public void testDeleteConversationMessageByIdInvalidConversationId() throws Exception {
    ConversationMessage protossResult = getFixture(ConversationMessage.class);
    Integer messageId = protossResult.getMessageId();

    when(conversationMessageManager.getMessageById(messageId)).thenReturn(protossResult);

    given()
        .when()
        .pathParam("conversationId", protossResult.getConversationId() + 1)
        .pathParam("messageId", messageId)
        .delete(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(conversationManager, never()).deleteConversationMessageAndLinks(messageId, user);

  }


  @Test
  public void testUpdateConversationExternalPatient()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      ResourceConflictException, TimeZoneNotFoundException {

    ConversationExternalPatient protossModel =
        getFixture(ConversationExternalPatient.class);

    given()
        .when()
        .pathParam("conversationId", protossModel.getConversationId())
        .pathParam("externalPatientId", protossModel.getExternalPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/external-patients/{externalPatientId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode())
        .extract();

    verify(conversationExternalPatientManager).create(protossModel);

  }

  @Test
  public void testGetExternalPatientByConversationIdEmpty()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    int conversationId = RandomUtils.nextInt(10);

    when(externalPatientManager.getByConversationId(conversationId)).thenReturn(
        Collections.emptyList());
    List<ExternalPatientDto> actual = toCollection(
        given()
            .when()
            .pathParam("conversationId", conversationId)
            .get(getBaseUrl() + endpointUrl + "{conversationId}/external-patients")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    TestCase.assertEquals(Collections.emptyList(), actual);
    verify(externalPatientManager).getByConversationId(conversationId);

  }

  @Test
  public void testGetExternalPatientByConversationId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    List<ExternalPatientDto> expected =
        getFixtures(ExternalPatientDto.class, ArrayList::new, 1);
    ExternalPatient externalPatient = mapDto(expected.get(0), ExternalPatient.class);
    int conversationId = RandomUtils.nextInt(10);

    when(externalPatientManager.getByConversationId(conversationId)).thenReturn(
        Collections.singletonList(externalPatient));

    List<ExternalPatientDto> actual = toCollection(
        given()
            .when()
            .pathParam("conversationId", conversationId)
            .get(getBaseUrl() + endpointUrl + "{conversationId}/external-patients")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    TestCase.assertEquals(expected, actual);
    verify(externalPatientManager).getByConversationId(conversationId);

  }


}
