
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultExternalPatientManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentContentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageStatusManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationPatientManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageAttachmentContentManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageAttachmentManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageStatusManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationPatientManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobFixture;
import com.qhrtech.emr.accuro.db.eprescribe.ExternalPatientFixture;
import com.qhrtech.emr.accuro.db.prescription.PrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationContactFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationExternalPatientFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationMessageFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationMessageStatusFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationParticipantFixture;
import com.qhrtech.emr.accuro.db.synapse.ConversationPatientFixture;
import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJob;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.model.synapse.Conversation;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessage;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachment;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachmentContent;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageStatus;
import com.qhrtech.emr.accuro.model.synapse.ConversationParticipant;
import com.qhrtech.emr.accuro.model.synapse.ConversationPatient;
import com.qhrtech.emr.accuro.model.synapse.MessageStatusCode;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.CleanPrescriptionRecords;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationContactDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageAttachmentContentDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageAttachmentDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageStatusDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationParticipantDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConversationEndpointIT extends AbstractEndpointIntegrationTest<ConversationEndpoint> {

  private final ConversationManager conversationManager;
  private final ConversationMessageManager conversationMessageManager;
  private final ConversationContactManager conversationContactManager;
  private final ConversationMessageAttachmentManager attachmentManager;
  private final ConversationMessageAttachmentContentManager contentManager;
  private final ConversationMessageStatusManager messageStatusManager;
  private final ConversationPatientManager patientManager;
  private final EprescribeJobManager jobManager;
  private final EprescribeJobTypeManager jobTypeManager;
  private final ConversationExternalPatientManager conversationExternalPatientManager;
  private final ExternalPatientManager externalPatientManager;
  private final String endpointUrl = "/v1/provider-portal/conversations/";
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private AuditLogUser user;

  public ConversationEndpointIT() throws IOException {
    super(new ConversationEndpoint(), ConversationEndpoint.class);
    conversationManager = new DefaultConversationManager(ds);
    conversationMessageManager = new DefaultConversationMessageManager(ds);
    conversationContactManager = new DefaultConversationContactManager(ds, null, defaultUser());
    attachmentManager = new DefaultConversationMessageAttachmentManager(ds);
    contentManager = new DefaultConversationMessageAttachmentContentManager(ds);
    messageStatusManager = new DefaultConversationMessageStatusManager(ds);
    jobManager = new DefaultEprescribeJobManager(ds);
    jobTypeManager = new DefaultEprescribeJobTypeManager(ds);
    patientManager = new DefaultConversationPatientManager(ds);
    externalPatientManager = new DefaultExternalPatientManager(ds, null, defaultUser());
    conversationExternalPatientManager =
        new DefaultConversationExternalPatientManager(ds, null, defaultUser());
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
    servicesMap.put(ExternalPatientManager.class, externalPatientManager);
    servicesMap.put(ConversationExternalPatientManager.class, conversationExternalPatientManager);
    return servicesMap;
  }

  @Test
  public void testUpdateConversationPatientLink()
      throws SQLException, UnsupportedSchemaVersionException {
    ConversationPatientFixture fixture = new ConversationPatientFixture();
    fixture.setUp(getConnection());
    ConversationPatient conversationPatient = fixture.get();
    given()
        .when()
        .pathParam("conversationId", conversationPatient.getConversationId())
        .pathParam("patientId", conversationPatient.getPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/patients/{patientId}")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

  }

  @Test
  public void testUpdateConversationPatientLinkConversationNotFound() {
    ConversationPatient conversationPatient = getFixture(ConversationPatient.class);

    given()
        .when()
        .pathParam("conversationId", conversationPatient.getConversationId())
        .pathParam("patientId", conversationPatient.getPatientId())
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/patients/{patientId}")
        .then()
        .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testUpdateConversationTaskGroupNoConversation()
      throws UnsupportedSchemaVersionException, SQLException, DatabaseInteractionException,
      NoDataFoundException, BusinessLogicException {

    EprescribeJobFixture eprescribeJobFixture = new EprescribeJobFixture();
    eprescribeJobFixture.setUp(getConnection());

    EprescribeJob eprescribeJob = eprescribeJobFixture.get();
    String taskGroupUuid = eprescribeJob.getJobUuid();

    try {
      given()
          .when()
          .pathParam("conversationId", Integer.MAX_VALUE)
          .pathParam("taskGroupUuid", taskGroupUuid)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/task-groups/{taskGroupUuid}")
          .then()
          .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
    } finally {
      jobManager.deleteEprescribeJob(eprescribeJob.getePrescribeJobId());
      jobTypeManager.deleteEprescribeJobType(eprescribeJob.getJobType().getePrescribeJobTypeId());
    }
  }

  @Test
  public void testUpdateConversationTaskGroupNew()
      throws UnsupportedSchemaVersionException, SQLException, DatabaseInteractionException,
      NoDataFoundException, BusinessLogicException {

    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    EprescribeJobFixture eprescribeJobFixture = new EprescribeJobFixture();
    eprescribeJobFixture.setUp(getConnection());
    EprescribeJob eprescribeJob = eprescribeJobFixture.get();

    Conversation conversation = conversationFixture.get();
    ConversationDto conversationDto = mapDto(conversation, ConversationDto.class);
    int conversationId = conversation.getConversationId();
    String taskGroupUuid = eprescribeJob.getJobUuid();

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("taskGroupUuid", taskGroupUuid)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/task-groups/{taskGroupUuid}")
          .then()
          .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    } finally {
      deleteTaskGroupLink(conversationId, taskGroupUuid);
      deleteConversation(conversationDto);
      jobManager.deleteEprescribeJob(eprescribeJob.getePrescribeJobId());
      jobTypeManager.deleteEprescribeJobType(eprescribeJob.getJobType().getePrescribeJobTypeId());
    }
  }

  private void deleteTaskGroupLink(int converstaionId, String uuid)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM conversation_task_group_identifier ");
    query.append("WHERE conversation_id = ?", converstaionId);
    query.append(" AND task_group_identifier = ?", uuid);
    executeLocalQuery(query);
  }

  @Test
  public void testGetConversationById() throws SQLException, UnsupportedSchemaVersionException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationDto expected =
        mapDto(fixture.get(), ConversationDto.class);

    try {
      ConversationDto actual = given()
          .when()
          .pathParam("id", expected.getConversationId())
          .get(getBaseUrl() + endpointUrl + "{id}")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode())
          .extract().as(ConversationDto.class);

      assertEquals(expected, actual);
    } finally {
      deleteConversation(expected);
    }
  }

  @Test
  public void testGetConversationByInvalidId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    int invalidId = TestUtilities.nextId();
    while (conversationManager.getConversationById(invalidId) != null) {
      invalidId = TestUtilities.nextId();
    }
    given()
        .when()
        .pathParam("id", invalidId)
        .get(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testCreateConversation() throws Exception {
    ConversationContactFixture fixture = new ConversationContactFixture();
    fixture.setUp(getConnection());

    ConversationDto conversationDto = getFixture(ConversationDto.class);
    conversationDto.setOwner(fixture.get().getContactId());

    try {
      int id = given()
          .when()
          .contentType(ContentType.JSON)
          .body(conversationDto)
          .post(getBaseUrl() + endpointUrl)
          .then()
          .assertThat()
          .statusCode(Status.CREATED.getStatusCode())
          .extract().as(Integer.class);
      conversationDto.setConversationId(id);
    } finally {
      deleteConversation(conversationDto);
    }
  }

  @Test
  public void testCreateConversationWithInvalidContact() {
    ConversationDto conversationDto = getFixture(ConversationDto.class);

    given()
        .when()
        .contentType(ContentType.JSON)
        .body(conversationDto)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }


  @Test
  public void testDeleteConversationAndMessageLinksById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {

    ConversationMessageFixture messageFixture = new ConversationMessageFixture();
    messageFixture.setUp(getConnection());

    ConversationFixture fixture = (ConversationFixture) messageFixture.getConversationFixture();

    int conversationId = fixture.get().getConversationId();

    assertNotNull(conversationManager.getConversationById(conversationId));
    assertNotNull(conversationMessageManager.getMessageById(messageFixture.get().getMessageId()));

    given()
        .when()
        .pathParam("id", conversationId)
        .delete(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    assertNull(conversationManager.getConversationById(conversationId));
    assertNull(conversationMessageManager.getMessageById(messageFixture.get().getMessageId()));
  }

  @Test
  public void testDeleteConversationAndPatientLinksById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {

    ConversationPatientFixture patientFixture = new ConversationPatientFixture();
    patientFixture.setUp(getConnection());

    int conversationId = patientFixture.get().getConversationId();

    assertNotNull(conversationManager.getConversationById(conversationId));
    assertNotNull(patientManager.getPatientById(patientFixture.get().getId()));

    given()
        .when()
        .pathParam("id", conversationId)
        .delete(getBaseUrl() + endpointUrl + "{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    assertNull(conversationManager.getConversationById(conversationId));
    assertNull(patientManager.getPatientById(patientFixture.get().getId()));
  }

  @Test
  public void testDeleteConversationMessageAndLinksByMessageId()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException {

    ConversationMessageFixture messageFixture = new ConversationMessageFixture();
    messageFixture.setUp(getConnection());

    ConversationFixture fixture = (ConversationFixture) messageFixture.getConversationFixture();

    int conversationId = fixture.get().getConversationId();

    assertNotNull(conversationMessageManager.getMessageById(messageFixture.get().getMessageId()));

    given()
        .when()
        .pathParam("id", conversationId)
        .pathParam("messageId", messageFixture.get().getMessageId())
        .delete(getBaseUrl() + endpointUrl + "{id}/messages/{messageId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    assertNull(conversationMessageManager.getMessageById(messageFixture.get().getMessageId()));

  }

  @Test
  public void testGetConversationMessageById()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto expected = mapDto(message, ConversationMessageDto.class);

    try {
      ConversationMessageDto actual = given()
          .when()
          .pathParam("id", expected.getConversationId())
          .pathParam("messageId", expected.getMessageId())
          .get(getBaseUrl() + endpointUrl + "{id}/messages/{messageId}")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode())
          .extract().as(ConversationMessageDto.class);

      expected.setTimestamp(actual.getTimestamp());
      assertEquals(expected, actual);
    } finally {
      deleteConversationMessage(expected);
    }
  }

  @Test
  public void testGetConversationMessageByIdWithInvalidConversationId()
      throws SQLException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    try {
      given()
          .when()
          .pathParam("id", TestUtilities.nextId())
          .pathParam("messageId", messageDto.getMessageId())
          .get(getBaseUrl() + endpointUrl + "{id}/messages/{messageId}")
          .then()
          .assertThat()
          .statusCode(Status.BAD_REQUEST.getStatusCode());

    } finally {
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testCreateConversationMessage() throws SQLException,
      UnsupportedSchemaVersionException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    try {
      Integer createdId = given()
          .body(messageDto)
          .when()
          .pathParam("conversationId", messageDto.getConversationId())
          .contentType(ContentType.JSON)
          .post(getBaseUrl() + endpointUrl + "{conversationId}/messages")
          .then()
          .assertThat()
          .statusCode(Status.CREATED.getStatusCode())
          .extract().as(Integer.class);
      messageDto.setMessageId(createdId);
      assertNotNull(createdId);
    } finally {
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testCreateMessageWithUnmatchedConversation()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessageDto messageDto = getFixture(ConversationMessageDto.class);

    while (conversationManager.getConversationById(messageDto.getConversationId()) != null) {
      messageDto.setConversationId(TestUtilities.nextId());
    }
    given()
        .body(messageDto)
        .when()
        .pathParam("conversationId", messageDto.getConversationId())
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "{conversationId}/messages")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testUpdateParticipant() throws SQLException, UnsupportedSchemaVersionException {
    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    ConversationParticipant participant = new ConversationParticipant();
    participant.setConversationId(conversationFixture.get().getConversationId());
    participant.setContactId(conversationFixture.get().getOwner());
    // Roles from accuro conversation roles table 1: Provider, 2: Patient, 3: Caregiver
    participant.setRole(TestUtilities.nextInt(1, 3));
    ConversationParticipantDto participantDto =
        mapDto(participant, ConversationParticipantDto.class);
    Set<ConversationParticipantDto> participantDtos = Collections.singleton(participantDto);

    try {
      given().body(participantDtos)
          .pathParam("id", participantDto.getConversationId())
          .when()
          .contentType(ContentType.JSON)
          .put(getBaseUrl() + endpointUrl + "{id}/participants")
          .then()
          .assertThat()
          .statusCode(Status.NO_CONTENT.getStatusCode());
    } finally {
      deleteConversationParticipant(participantDto);
    }
  }

  @Test
  public void testUpdateParticipantWithDifferentConversationId()
      throws SQLException, UnsupportedSchemaVersionException {
    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    ConversationParticipant participant = getFixture(ConversationParticipant.class);
    participant.setConversationId(conversationFixture.get().getConversationId());
    participant.setContactId(conversationFixture.get().getOwner());
    ConversationParticipantDto participantDto =
        mapDto(participant, ConversationParticipantDto.class);
    Set<ConversationParticipantDto> participantDtos = Collections.singleton(participantDto);

    try {
      given().body(participantDtos)
          .pathParam("id", TestUtilities.nextId())
          .when()
          .contentType(ContentType.JSON)
          .put(getBaseUrl() + endpointUrl + "{id}/participants")
          .then()
          .assertThat()
          .statusCode(Status.BAD_REQUEST.getStatusCode());
    } finally {
      deleteConversationParticipant(participantDto);
    }
  }

  @Test
  public void testCreateParticipant() throws SQLException, UnsupportedSchemaVersionException {
    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    ConversationParticipant participant = new ConversationParticipant();
    participant.setConversationId(conversationFixture.get().getConversationId());
    participant.setContactId(conversationFixture.get().getOwner());
    // Roles from accuro conversation roles table 1: Provider, 2: Patient, 3: Caregiver
    participant.setRole(TestUtilities.nextInt(1, 3));
    ConversationParticipantDto participantDto =
        mapDto(participant, ConversationParticipantDto.class);

    try {
      given().body(participantDto)
          .pathParam("id", participantDto.getConversationId())
          .when()
          .contentType(ContentType.JSON)
          .post(getBaseUrl() + endpointUrl + "{id}/participants")
          .then()
          .assertThat()
          .statusCode(Status.CREATED.getStatusCode());
    } finally {
      deleteConversationParticipant(participantDto);
    }
  }

  @Test
  public void testGetParticipants()
      throws SQLException, UnsupportedSchemaVersionException {
    ConversationParticipantFixture participantFixture = new ConversationParticipantFixture();
    participantFixture.setUp(getConnection());
    int conversationId = participantFixture.get().getConversationId();

    ConversationParticipantDto conversationParticipant =
        mapDto(participantFixture.get(), ConversationParticipantDto.class);
    List<ConversationParticipantDto> expected = Collections.singletonList(conversationParticipant);

    List<ConversationParticipantDto> actual = toCollection(given()
        .when()
        .pathParam("id", conversationId)
        .get(getBaseUrl() + endpointUrl + "{id}/participants")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(ConversationParticipantDto[].class),
        ArrayList::new);
    assertEquals(expected, actual);

  }

  @Test
  public void testGetConversationMessageContentAttachment()
      throws SQLException, DatabaseInteractionException, UnsupportedSchemaVersionException,
      SupportingResourceNotFoundException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    ConversationMessageAttachmentDto expected = getFixture(ConversationMessageAttachmentDto.class);
    expected.setContentLengthBytes(null); // read only field
    expected.setConversationMessageId(messageDto.getMessageId());
    int attachmentId =
        attachmentManager.createAttachment(mapDto(expected, ConversationMessageAttachment.class));
    expected.setId(attachmentId);

    try {
      ConversationMessageAttachmentDto actual =
          given()
              .when()
              .pathParam("conversationId", message.getConversationId())
              .pathParam("messageId", message.getMessageId())
              .pathParam("attachmentId", expected.getId())
              .get(getBaseUrl() + endpointUrl
                  + "{conversationId}/messages/{messageId}/attachments/{attachmentId}")
              .then()
              .assertThat()
              .statusCode(Status.OK.getStatusCode())
              .extract()
              .as(ConversationMessageAttachmentDto.class);
      assertEquals(expected, actual);
    } finally {
      deleteConversationMessageAttachment(expected);
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testGetConversationMessageAttachmentWhenSupportingResourceNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    int attachmentId = TestUtilities.nextInt();
    while (attachmentManager.getAttachmentById(attachmentId) != null) {
      attachmentId = TestUtilities.nextInt();
    }
    given()
        .when()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testCreateConversationMessageAttachment()
      throws SQLException, DatabaseInteractionException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    ConversationMessageAttachmentDto attachmentDto =
        getFixture(ConversationMessageAttachmentDto.class);
    attachmentDto.setConversationMessageId(messageDto.getMessageId());

    try {
      Integer attachmentId = given()
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
      assertNotNull(attachmentId);

    } finally {
      deleteConversationMessage(messageDto);
      deleteConversationMessageAttachment(attachmentDto);
    }
  }

  @Test
  public void testCreateConversationMessageAttachmentWithInvalidMessageId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    ConversationMessageAttachmentDto attachmentDto =
        getFixture(ConversationMessageAttachmentDto.class);
    while (conversationMessageManager
        .getMessageById(attachmentDto.getConversationMessageId()) != null) {
      attachmentDto.setConversationMessageId(TestUtilities.nextId());
    }

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
  }

  @Test
  public void testCreateConversationMessageAttachmentWhenMessageNotFound() {
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
  }

  @Test
  public void testGetConversationMessageAttachmentContent()
      throws SQLException, DatabaseInteractionException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException, IOException, TimeZoneNotFoundException,
      ResourceConflictException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    ConversationMessageAttachmentDto attachmentDto =
        getFixture(ConversationMessageAttachmentDto.class);
    attachmentDto.setConversationMessageId(messageDto.getMessageId());
    int attachmentId =
        attachmentManager
            .createAttachment(mapDto(attachmentDto, ConversationMessageAttachment.class));
    attachmentDto.setId(attachmentId);

    File testFile = temporaryFolder.newFile("test.pdf");
    byte[] bytes = Files.readAllBytes(testFile.toPath());

    ConversationMessageAttachmentContent content = new ConversationMessageAttachmentContent();
    content.setAttachmentId(attachmentId);
    content.setContent(bytes);
    contentManager.createContent(content, fixture.get().getConversationType());
    ConversationMessageAttachmentContentDto contentDto =
        mapDto(content, ConversationMessageAttachmentContentDto.class);

    try {
      given()
          .when()
          .pathParam("conversationId", message.getConversationId())
          .pathParam("messageId", message.getMessageId())
          .pathParam("attachmentId", attachmentId)
          .get(getBaseUrl() + endpointUrl
              + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode());
    } finally {
      deleteConversationMessageAttachmentContent(contentDto);
      deleteConversationMessageAttachment(attachmentDto);
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testGetConversationMessageAttachmentContentWhenSupportingResourceNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    int attachmentId = TestUtilities.nextInt();
    while (attachmentManager.getAttachmentById(attachmentId) != null) {
      attachmentId = TestUtilities.nextInt();
    }

    given()
        .when()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testGetConversationMessageAttachmentContentWhenContentNotFound()
      throws DatabaseInteractionException, SQLException, UnsupportedSchemaVersionException,
      SupportingResourceNotFoundException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    int attachmentId = TestUtilities.nextInt();
    while (attachmentManager.getAttachmentById(attachmentId) != null) {
      attachmentId = TestUtilities.nextInt();
    }

    try {
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
    } finally {
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testCreateConversationMessageAttachmentContent()
      throws SQLException, DatabaseInteractionException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException, IOException, TimeZoneNotFoundException {
    ConversationFixture fixture = new ConversationFixture();
    fixture.setUp(getConnection());

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(fixture.get().getConversationId());
    message.setSender(fixture.get().getOwner());
    message.setDelegator(null);
    message.setPriority(null);
    int id = conversationMessageManager.createMessage(message, user);
    message.setMessageId(id);
    ConversationMessageDto messageDto = mapDto(message, ConversationMessageDto.class);

    ConversationMessageAttachmentDto attachmentDto =
        getFixture(ConversationMessageAttachmentDto.class);
    attachmentDto.setConversationMessageId(messageDto.getMessageId());
    int attachmentId =
        attachmentManager
            .createAttachment(mapDto(attachmentDto, ConversationMessageAttachment.class));
    attachmentDto.setId(attachmentId);

    File testFile = temporaryFolder.newFile("test.pdf");
    FileWriter fileWriter = new FileWriter(testFile);
    fileWriter.write("Test File");
    fileWriter.flush();
    fileWriter.close();
    byte[] bytes = Files.readAllBytes(testFile.toPath());

    ConversationMessageAttachmentContent content = new ConversationMessageAttachmentContent();
    content.setAttachmentId(attachmentId);
    content.setContent(bytes);
    ConversationMessageAttachmentContentDto contentDto =
        mapDto(content, ConversationMessageAttachmentContentDto.class);

    try {
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
    } finally {
      deleteConversationMessageAttachmentContent(contentDto);
      deleteConversationMessageAttachment(attachmentDto);
      deleteConversationMessage(messageDto);
    }
  }

  @Test
  public void testCreateConversationMessageAttachmentContentWhenMessageNotFound()
      throws IOException, DatabaseInteractionException, UnsupportedSchemaVersionException {
    File testFile = temporaryFolder.newFile("test.pdf");
    int attachmentId = TestUtilities.nextInt();
    while (attachmentManager.getAttachmentById(attachmentId) != null) {
      attachmentId = TestUtilities.nextInt();
    }

    given()
        .pathParam("conversationId", TestUtilities.nextId())
        .pathParam("messageId", TestUtilities.nextId())
        .pathParam("attachmentId", attachmentId)
        .multiPart("file", testFile)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + endpointUrl
            + "{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  public void testSaveConversationMessageStatusAsUpdate() throws Exception {
    ConversationMessageStatusFixture fixture = new ConversationMessageStatusFixture();
    fixture.setUp(getConnection());

    ConversationMessage message =
        conversationMessageManager.getMessageById(fixture.get().getMessageId());
    int conversationId = message.getConversationId();
    ConversationMessageStatus status = fixture.get();
    ConversationMessageStatusDto statusDto = mapDto(status, ConversationMessageStatusDto.class);

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("messageId", status.getMessageId())
          .header("Content-Type", "application/json")
          .body(statusDto)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/messages/{messageId}/statuses")
          .then()
          .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    } finally {
      deleteConversationMessageStatus(statusDto);
      deleteConversationMessage(mapDto(status, ConversationMessageDto.class));
    }
  }

  @Test
  public void testSaveConversationMessageStatusAsCreate() throws Exception {

    ConversationMessageFixture conversationMessagefixture = new ConversationMessageFixture();
    conversationMessagefixture.setUp(getConnection());

    ConversationMessage message = conversationMessagefixture.get();
    int conversationId = message.getConversationId();
    ConversationMessageStatus status = new ConversationMessageStatus();
    status.setMessageId(message.getMessageId());
    status.setContactId(message.getDelegator());
    status.setStatusCode(MessageStatusCode.PROCESSED);
    ConversationMessageStatusDto statusDto = mapDto(status, ConversationMessageStatusDto.class);

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("messageId", status.getMessageId())
          .header("Content-Type", "application/json")
          .body(statusDto)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/messages/{messageId}/statuses")
          .then()
          .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    } finally {
      deleteConversationMessageStatus(statusDto);
      deleteConversationMessage(mapDto(status, ConversationMessageDto.class));
    }
  }

  @Test
  public void testGetConversationMessageStatuses() throws Exception {
    ConversationMessageStatusFixture fixture = new ConversationMessageStatusFixture();
    fixture.setUp(getConnection());

    ConversationMessage message =
        conversationMessageManager.getMessageById(fixture.get().getMessageId());
    int conversationId = message.getConversationId();
    ConversationMessageStatus status = fixture.get();
    ConversationMessageStatusDto statusDto = mapDto(status, ConversationMessageStatusDto.class);

    try {
      List<ConversationMessageStatusDto> actual = toCollection(given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("messageId", message.getMessageId())
          .get(getBaseUrl() + endpointUrl + "{conversationId}/messages/{messageId}/statuses")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode())
          .extract().as(ConversationMessageStatusDto[].class),
          ArrayList::new);

      assertTrue(actual.contains(statusDto));
    } finally {
      deleteConversationMessageStatus(statusDto);
      deleteConversationMessage(mapDto(status, ConversationMessageDto.class));
    }
  }

  @Test
  public void testUpdateConversationPrescriptionLinkNoPrescription()
      throws UnsupportedSchemaVersionException, SQLException {

    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    Conversation conversation = conversationFixture.get();
    ConversationDto conversationDto = mapDto(conversation, ConversationDto.class);
    int conversationId = conversation.getConversationId();
    int rxId = TestUtilities.nextId();

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("prescriptionMedicationId", rxId)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/medications/{prescriptionMedicationId}")
          .then()
          .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());
    } finally {
      deleteConversation(conversationDto);
    }

  }


  @Test
  public void testUpdateConversationPrescriptionLinkNoConversation()
      throws UnsupportedSchemaVersionException, SQLException, IOException {

    PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();
    medicationFixture.setUp(getConnection());

    PrescriptionMedication medication = medicationFixture.get();
    int conversationId = TestUtilities.nextInt();
    int rxId = medication.getPrescriptionId();

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("prescriptionMedicationId", rxId)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/medications/{prescriptionMedicationId}")
          .then()
          .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
    } finally {
      CleanPrescriptionRecords cleanPrescriptionRecords =
          new CleanPrescriptionRecords(new ConversationEndpoint(), ConversationEndpoint.class);
      cleanPrescriptionRecords.cleanMedicationRecord(medication);
    }

  }


  @Test
  public void testUpdateConversationPrescriptionLink()
      throws UnsupportedSchemaVersionException, SQLException {

    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());

    PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();
    medicationFixture.setUp(getConnection());

    Conversation conversation = conversationFixture.get();
    ConversationDto conversationDto = mapDto(conversation, ConversationDto.class);
    int conversationId = conversation.getConversationId();
    int rxId = medicationFixture.get().getPrescriptionId();

    try {
      given()
          .when()
          .pathParam("conversationId", conversationId)
          .pathParam("prescriptionMedicationId", rxId)
          .put(getBaseUrl() + endpointUrl
              + "{conversationId}/medications/{prescriptionMedicationId}")
          .then()
          .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());
    } finally {
      deleteConversationPrescriptionLink(conversationId, rxId);
      deleteConversation(conversationDto);
    }

  }

  @Test
  public void testArchiveConversation()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      SQLException {
    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());
    Conversation conversation = conversationFixture.get();
    int conversationId = conversation.getConversationId();

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/archive")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    Conversation conversationById = conversationManager.getConversationById(conversationId);

    assertTrue(conversationById.getArchived() != null);

  }

  @Test
  public void testUnarchiveConversation()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      SQLException {
    ConversationFixture conversationFixture = new ConversationFixture();
    conversationFixture.setUp(getConnection());
    Conversation conversation = conversationFixture.get();
    int conversationId = conversation.getConversationId();

    given()
        .when()
        .pathParam("conversationId", conversationId)
        .put(getBaseUrl() + endpointUrl
            + "{conversationId}/unarchive")
        .then()
        .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());

    Conversation conversationById = conversationManager.getConversationById(conversationId);

    assertTrue(conversationById.getArchived() == null);

  }

  @Test
  public void testGetExternalPatientsByConversationId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, NoDataFoundException, SQLException {

    ConversationExternalPatientFixture conversationExternalFixture =
        new ConversationExternalPatientFixture();
    conversationExternalFixture.setUp(getConnection());

    ExternalPatientDto externalPatientDto =
        mapDto(externalPatientManager.getExternalPatientById(
            conversationExternalFixture.get().getExternalPatientId()), ExternalPatientDto.class);

    List<ExternalPatientDto> expectedList = Collections.singletonList(externalPatientDto);

    List<ExternalPatientDto> actual = toCollection(
        given()
            .when()
            .pathParam("conversationId", conversationExternalFixture.get().getConversationId())
            .get(getBaseUrl() + endpointUrl + "{conversationId}/external-patients")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract().as(ExternalPatientDto[].class),
        ArrayList::new);

    TestCase.assertEquals(expectedList, actual);
    conversationExternalPatientManager.deleteByConversationId(
        conversationExternalFixture.get().getConversationId());
  }

  @Test
  public void testConversationAndExternalPatientLink()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      TimeZoneNotFoundException, SQLException {

    ConversationFixture conversationFixture =
        new ConversationFixture();
    ExternalPatientFixture externalPatientFixture = new ExternalPatientFixture();

    conversationFixture.setUp(getConnection());
    externalPatientFixture.setUp(getConnection());

    given()
        .contentType(ContentType.JSON)
        .pathParam("conversationId", conversationFixture.get().getConversationId())
        .pathParam("externalPatientId", externalPatientFixture.get().getId())
        .put(getBaseUrl() + endpointUrl + "{conversationId}/external-patients/{externalPatientId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode())
        .extract();

    conversationExternalPatientManager.deleteByConversationId(
        conversationFixture.get().getConversationId());
  }

  private void deleteConversationPrescriptionLink(int converstaionId, int prescriptionId)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM conversation_prescription ");
    query.append("WHERE conversation_id = ?", converstaionId);
    query.append(" AND prescription_medication_id = ?", prescriptionId);
    executeLocalQuery(query);
  }

  private void deleteConversationMessageAttachmentContent(
      ConversationMessageAttachmentContentDto content)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append(
        "DELETE FROM accuro_conversation_message_attachment_content WHERE attachment_id = ? ",
        content.getAttachmentId());
    executeLocalQuery(query);
  }

  private void deleteConversationMessageAttachment(ConversationMessageAttachmentDto attachment)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation_message_attachment WHERE id = ? ",
        attachment.getId());
    executeLocalQuery(query);
  }

  private void deleteConversationParticipant(ConversationParticipantDto participant)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation_participant WHERE conversation_id = ? ",
        participant.getConversationId());
    executeLocalQuery(query);

    ConversationDto conversationDto = new ConversationDto();
    conversationDto.setConversationId(participant.getConversationId());
    conversationDto.setOwner(participant.getConversationContactId());
    deleteConversation(conversationDto);
  }

  private void deleteConversationMessage(ConversationMessageDto message) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation_message WHERE message_id = ? ",
        message.getMessageId());
    executeLocalQuery(query);

    ConversationDto conversationDto = new ConversationDto();
    conversationDto.setConversationId(message.getConversationId());
    conversationDto.setOwner(message.getSender());
    deleteConversation(conversationDto);
  }

  private void deleteConversation(ConversationDto conversation) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation WHERE conversation_id = ? ",
        conversation.getConversationId());
    executeLocalQuery(query);

    ConversationContactDto contactDto = new ConversationContactDto();
    contactDto.setContactId(conversation.getOwner());
    deleteConversationContact(contactDto);
  }

  private void deleteConversationContact(ConversationContactDto contact) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation_contact WHERE contact_id = ? ",
        contact.getContactId());
    executeLocalQuery(query);
  }

  private void deleteConversationMessageStatus(ConversationMessageStatusDto statusDto)
      throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM accuro_conversation_message_status WHERE message_id = ? ",
        statusDto.getMessageId());
    executeLocalQuery(query);
  }

}
