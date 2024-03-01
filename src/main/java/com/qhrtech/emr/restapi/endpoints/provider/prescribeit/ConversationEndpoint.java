
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
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
import com.qhrtech.emr.accuro.model.synapse.ConversationExternalPatient;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessage;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachment;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageAttachmentContent;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessageStatus;
import com.qhrtech.emr.accuro.model.synapse.ConversationParticipant;
import com.qhrtech.emr.accuro.model.synapse.ConversationPatient;
import com.qhrtech.emr.dataaccess.exception.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageAttachmentDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationMessageStatusDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationParticipantDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.validators.CheckUuid;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>ConversationsEndpoint</code> collection is designed to expose the conversation
 * endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 */
@Component
@Path("/v1/provider-portal/conversations")
@Facet("provider-portal")
@Tag(name = "Conversation Endpoints",
    description = "Conversation Endpoints")
public class ConversationEndpoint extends AbstractEndpoint {

  /**
   * Update the link between a conversation and a task group.
   *
   * <p>
   * Note: If the link does not exist it will create new link.
   *
   * If the link exists but with a different task group id it will update the link with the given
   * task group uuid.
   *
   * </p>
   *
   * @param conversationId Conversation ID
   * @param taskGroupUuid Task Group Uuid
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{conversationId}/task-groups/{taskGroupUuid}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Update the link between conversation and task group.",
      description = "Update the link between conversation and task group. If the link does not "
          + "exist it will create new link. If the link exists but different task group id it "
          + "will update the link with the given task group id.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "taskGroupUuid",
              description = "Task group UUID",
              in = ParameterIn.PATH)})
  public Response updateConversationTaskGroupLink(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("taskGroupUuid") @CheckUuid String taskGroupUuid)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationManager manager = getImpl(ConversationManager.class);

    if (manager.getConversationById(conversationId) == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Conversation does not exist.");
    }

    manager.addTaskGroupToConversation(conversationId, UUID.fromString(taskGroupUuid));

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Update the link between a conversation and a patient.
   *
   * <p>
   * Note: If the link does not exist it will create new link.
   *
   * If the link exists but with a different patient it will update the link with the given patient
   * id.
   *
   * </p>
   *
   * @param conversationId Conversation ID
   * @param patientId Patient ID
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{conversationId}/patients/{patientId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Update the link between conversation and patient.",
      description = "Update the link between conversation and patient. If the link does not "
          + "exist it will create new link. If the link exists but different patient it "
          + "will update the link with the patient id.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "patientId",
              description = "Patient ID",
              in = ParameterIn.PATH)})
  public Response updateConversationPatientLink(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("patientId") int patientId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationManager manager = getImpl(ConversationManager.class);

    if (manager.getConversationById(conversationId) == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Conversation does not exist.");
    }

    ConversationPatientManager patientManager = getImpl(ConversationPatientManager.class);

    List<ConversationPatient> existingLinks =
        patientManager.getPatientsForConversation(conversationId);

    if (existingLinks.stream().anyMatch(l -> l.getPatientId() == patientId)) {
      return Response.status(Status.NO_CONTENT).build();
    }

    ConversationPatient conversationPatient = new ConversationPatient();
    conversationPatient.setPatientId(patientId);
    conversationPatient.setConversationId(conversationId);
    patientManager.createPatient(conversationPatient);
    return Response.status(Status.NO_CONTENT).build();
  }


  /**
   * Retrieves Conversation associated with the id.
   *
   * @param id conversation id
   * @return Conversation data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves conversation",
      description = "Retrieves Conversation associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public ConversationDto getConversationById(@Parameter(
      description = "Conversation id") @PathParam("id") int id)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationManager manager =
        getImpl(ConversationManager.class);

    Conversation conversation = manager.getConversationById(id);

    ConversationDto conversationDto = mapDto(conversation,
        ConversationDto.class);

    return conversationDto;

  }

  /**
   * Retrieves Conversations associated with the externalId.
   *
   * @param externalId conversation id
   * @return {@link List}Conversation data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieve conversations",
      description = "Retrieve Conversations associated "
          + "with the external id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<ConversationDto> getConversations(
      @Parameter(description = "External Id") @QueryParam("externalId") String externalId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationManager manager =
        getImpl(ConversationManager.class);
    List<Conversation> conversations = manager.getByExternalIdentifier(externalId);
    return mapDto(conversations, ConversationDto.class, ArrayList::new);
  }

  /**
   * Create Conversation.
   *
   * @param conversationDto data transfer object
   * @return Id of Conversation created.
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Communication.create' ) ")
  @Operation(
      summary = "Creates conversation",
      description = "Creates Conversation",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer", example = "1"))),
          @ApiResponse(
              responseCode = "409",
              description = "Resource already exists.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response createConversation(
      @RequestBody(description = "ConversationDto") @Valid ConversationDto conversationDto)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {

    ConversationManager manager =
        getImpl(ConversationManager.class);

    ConversationContactManager contactManager =
        getImpl(ConversationContactManager.class);

    // remove this check once protoss have the validation
    if (contactManager.getContactById(conversationDto.getOwner()) == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Owner must be an existing conversation contact");
    }

    Conversation conversation = mapDto(conversationDto, Conversation.class);

    int conversationId = manager.createConversation(conversation, getUser());

    return Response.status(Status.CREATED).entity(conversationId).build();

  }

  /**
   * Retrieves Conversation Message associated with the id.
   *
   * @param conversationId conversation id
   * @param messageId conversation message id
   * @return Conversation message data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{conversationId}/messages/{messageId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves conversation message",
      description = "Retrieves Conversation message associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationMessageDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public ConversationMessageDto getConversationMessageById(@Parameter(
      description = "Conversation id") @PathParam("conversationId") int conversationId,
      @Parameter(description = "Conversation message id") @PathParam("messageId") int messageId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationMessageManager manager =
        getImpl(ConversationMessageManager.class);

    ConversationMessage message = manager.getMessageById(messageId);

    ConversationMessageDto dto = mapDto(message,
        ConversationMessageDto.class);

    if (dto.getConversationId() != conversationId) {

      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Requested message do not belong to the provided conversation id ");
    }
    return dto;
  }

  /**
   * Create Conversation message.
   *
   * @param conversationMessageDto data transfer object
   * @return Id of Conversation message created.
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @Path("/{conversationId}/messages")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Communication.create' ) ")
  @Operation(
      summary = "Creates conversation message",
      description = "Creates Conversation message",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer", example = "1"))),
          @ApiResponse(
              responseCode = "409",
              description = "Resource already exists."),
          @ApiResponse(
              responseCode = "400",
              description = "If Conversation id in the path param "
                  + "do not match with the one in the body."),
          @ApiResponse(
              responseCode = "404",
              description = "If conversation id provided is not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response createConversationMessage(@Parameter(
      description = "Conversation id") @PathParam("conversationId") int conversationId,
      @RequestBody(
          description = "ConversationMessage") @Valid ConversationMessageDto conversationMessageDto)
      throws DatabaseInteractionException,
      UnsupportedSchemaVersionException,
      SupportingResourceNotFoundException, TimeZoneNotFoundException {

    if (conversationMessageDto.getConversationId() != conversationId) {

      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Conversation Id provided in the path must match with the one provided in the body");
    }

    ConversationMessageManager manager =
        getImpl(ConversationMessageManager.class);

    ConversationMessage message = mapDto(conversationMessageDto, ConversationMessage.class);

    int messageId = manager.createMessage(message, getUser());

    return Response.status(Status.CREATED).entity(messageId).build();

  }

  /**
   * Retrieves conversation message statuses belonging to given conversation message Id.
   *
   * <p>
   * The results are descending order by the updated time.
   * </p>
   *
   * @param conversationId Conversation ID
   * @param messageId Conversation message ID
   * @return List of {@link ConversationMessageStatusDto} data transfer object
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{conversationId}/messages/{messageId}/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' )")
  @Operation(
      summary = "Retrieves conversation message statuses belonging to given "
          + "conversation message id.",
      description = "Retrieves conversation message statuses belonging to given "
          + "conversation id. The results will be sorted in descending order of updated time.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(array = @ArraySchema(schema = @Schema(
                  implementation = ConversationMessageStatusDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Conversation message id",
              in = ParameterIn.PATH)})
  public List<ConversationMessageStatusDto> getConversationMessageStatuses(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationMessageManager messageManager = getImpl(ConversationMessageManager.class);
    ConversationMessage message = messageManager.getMessageById(messageId);

    if (message == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "No Conversation Message found for the provided message id");
    }
    if (message.getConversationId() != conversationId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Conversation id provided in the path doesn't match the "
              + "id of the conversation message");
    }

    ConversationMessageStatusManager manager = getImpl(ConversationMessageStatusManager.class);

    List<ConversationMessageStatus> statuses = manager.getMessageStatus(messageId);

    return mapDto(statuses, ConversationMessageStatusDto.class, ArrayList::new);

  }

  /**
   * Creates or updates conversation message status.
   *
   * <p>
   * Note: This will create new if not already exists otherwise replace the existing conversation
   * message status. The updated field will be set to default UTC time.
   * </p>
   *
   * @param conversationId Conversation ID
   * @param messageId Conversation message ID
   * @param messageStatusDto {@link ConversationMessageStatusDto} data transfer object
   * @return 201 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{conversationId}/messages/{messageId}/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Creates or updates conversation message status.",
      description = "Creates or updates conversation message status. A new status will be created "
          + "if it does not exist, otherwise it will be replaced with the provided status.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Conversation message id",
              in = ParameterIn.PATH)})
  public Response saveMessageStatus(
      @RequestBody @Valid ConversationMessageStatusDto messageStatusDto,
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException,
      TimeZoneNotFoundException {

    if (messageStatusDto.getMessageId() != messageId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Message ids from body and path must match.");
    }
    ConversationMessageManager messageManager = getImpl(ConversationMessageManager.class);
    ConversationMessage message = messageManager.getMessageById(messageId);

    if (message == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "No Conversation Message found for the provided message id");
    }

    if (message.getConversationId() != conversationId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Conversation id provided in the path doesn't match the "
              + "id of the conversation message resource.");
    }

    ConversationMessageStatusManager manager = getImpl(ConversationMessageStatusManager.class);
    List<ConversationMessageStatus> statuses = manager.getMessageStatus(messageId);

    ConversationMessageStatus messageStatus =
        mapDto(messageStatusDto, ConversationMessageStatus.class);

    if (statuses.isEmpty()
        || !statuses.stream().anyMatch(s -> s.getContactId() == messageStatusDto.getContactId())) {
      manager.createMessageStatus(messageStatus, getUser());
    } else {
      manager.updateMessageStatus(messageStatus, getUser());
    }

    return Response.status(Status.NO_CONTENT).build();

  }

  /**
   * Create conversation participants list. Add the given participant to the existing conversation
   * participants.
   *
   * @param conversationId Conversation ID
   * @param participantDto {@link ConversationParticipantDto} data transfer object
   * @return 201 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @POST
  @Path("/{id}/participants")
  @PreAuthorize("#oauth2.hasScope('user/provider.Communication.create')")
  @Operation(
      summary = "Creates a single conversation participant",
      description = "Creates a single conversation participant. Adds the given participant to the "
          + "existing conversation participants.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH)})
  public Response createConversationParticipant(
      @Parameter(description = "Conversation contact") @Valid @RequestBody(
          description = "Conversation participant") ConversationParticipantDto participantDto,
      @Parameter(hidden = true) @PathParam("id") int conversationId)
      throws DatabaseInteractionException, SupportingResourceNotFoundException,
      TimeZoneNotFoundException, UnsupportedSchemaVersionException {

    if (conversationId != participantDto.getConversationId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Conversation Id provided in the path must match the one provided in the body");
    }

    ConversationManager manager = getImpl(ConversationManager.class);
    manager.createParticipant(mapDto(participantDto, ConversationParticipant.class), getUser());

    return Response.status(Status.CREATED).build();

  }

  /**
   * Retrieves conversation participants belonging to given conversation ID.
   *
   * @param conversationId Conversation ID
   * @return List of {@link ConversationParticipantDto} data transfer object. Results are ordered by
   *         contactId.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{id}/participants")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' )")
  @Operation(
      summary = "Retrieves list of conversation participants belonging to given "
          + "conversation ID.",
      description = "Retrieves list of conversation participants belonging to given "
          + "conversation ID. Results are ordered by contactId.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(array = @ArraySchema(
                  schema = @Schema(
                      implementation = ConversationParticipantDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH)})
  public List<ConversationParticipantDto> getConversationParticipants(
      @Parameter(hidden = true) @PathParam("id") int conversationId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    ConversationManager manager = getImpl(ConversationManager.class);

    List<ConversationParticipant> participants =
        manager.getConversationParticipants(conversationId);

    return mapDto(participants, ConversationParticipantDto.class, ArrayList::new);

  }

  /**
   * Updates conversation participants.
   *
   * <p>
   * Note: This will replace the existing conversation participants if any with the given
   * conversation participants.
   * </p>
   *
   * @param conversationId Conversation ID
   * @param participantDto {@link ConversationParticipantDto} data transfer object
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{id}/participants")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  @Operation(
      summary = "Updates conversation participants",
      description = "Updates conversation participants. This will replace the existing "
          + "conversation participants.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH)})
  public Response updateConversationParticipant(
      @Parameter(description = "Conversation participants") @Valid @RequestBody(
          description = "Conversation participants") Set<ConversationParticipantDto> participantDto,
      @Parameter(hidden = true) @PathParam("id") int conversationId)
      throws DatabaseInteractionException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException {

    ConversationManager manager = getImpl(ConversationManager.class);

    HashSet<ConversationParticipant> conversationParticipants =
        mapDto(participantDto, ConversationParticipant.class, HashSet::new);

    manager.updateConversationParticipants(conversationId, conversationParticipants, getUser());

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Retrieves the metadata of conversation message attachment.
   *
   * @param conversationId The conversation Id
   * @param messageId The message Id
   * @param attachmentId The attachment Id
   * @return The metadata of conversation message attachment
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If db scheme is unsupported.
   * @throws NoDataFoundException If the resource not found.
   */
  @GET
  @Path("/{conversationId}/messages/{messageId}/attachments/{attachmentId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Attachment.read' )")
  @Operation(
      summary = "Retrieves the metadata of conversation message attachment",
      description = "Retrieves the metadata of conversation message attachment",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationMessageAttachmentDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Supporting Resource Not Found"),
          @ApiResponse(
              responseCode = "404",
              description = "Not Found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Message id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "attachmentId",
              description = "Attachment id",
              in = ParameterIn.PATH)})
  public ConversationMessageAttachmentDto getConversationMessageAttachment(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId,
      @Parameter(hidden = true) @PathParam("attachmentId") int attachmentId)
      throws DatabaseInteractionException,
      UnsupportedSchemaVersionException,
      NoDataFoundException, SupportingResourceNotFoundException {

    ConversationMessageAttachment attachmentById =
        validateConversationIds(messageId, conversationId, attachmentId);
    return mapDto(attachmentById, ConversationMessageAttachmentDto.class);
  }

  /**
   * Saves the metadata of conversation message attachment.
   *
   * @param conversationId The conversation Id
   * @param messageId The message Id
   * @return The id of metadata of conversation message attachment
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If db scheme is unsupported.
   * @throws NoDataFoundException If the resource not found.
   */
  @POST
  @Path("/{conversationId}/messages/{messageId}/attachments")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Attachment.create' )")
  @Operation(
      summary = "Saves the metadata of conversation message attachment",
      description = "Saves the metadata of conversation message attachment",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(schema = @Schema(type = "Integer", example = "1"))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input"),
          @ApiResponse(
              responseCode = "404",
              description = "The resource not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Message id",
              in = ParameterIn.PATH)})
  public Response createConversationMessageAttachment(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId,
      @RequestBody(
          description = "Attachement") @Valid ConversationMessageAttachmentDto attachment)
      throws DatabaseInteractionException,
      UnsupportedSchemaVersionException,
      NoDataFoundException, SupportingResourceNotFoundException {

    if (messageId != attachment.getConversationMessageId()) {
      throw new IllegalArgumentException(
          "The conversation message id from the body do not match with the message id in the path");
    }

    ConversationMessageManager messageManager = getImpl(ConversationMessageManager.class);
    ConversationMessage message = messageManager.getMessageById(messageId);

    if (message == null || conversationId != message.getConversationId()) {
      throw new SupportingResourceNotFoundException("The supporting resource doesn't exist.");
    }

    ConversationMessageAttachmentManager manager =
        getImpl(ConversationMessageAttachmentManager.class);
    ConversationMessageAttachment protossAttachment =
        mapDto(attachment, ConversationMessageAttachment.class);
    int id = manager.createAttachment(protossAttachment);

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Retrieves the content of conversation message attachment.
   *
   * @param conversationId The conversation Id
   * @param messageId The message Id
   * @param attachmentId The attachment Id
   * @return The content of conversation message attachment. Multipart response which has binary
   *         contents of content-type application/octet-stream.
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If db scheme is unsupported.
   * @throws NoDataFoundException If the resource not found.
   */
  @GET
  @Path("/{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Attachment.read' )")
  @Operation(
      summary = "Retrieves the content of conversation message attachment",
      description = "Retrieves the content of conversation message attachment",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Multipart response which has binary "
                  + "contents of content-type application/octet-stream"),
          @ApiResponse(
              responseCode = "400",
              description = "Supporting Resource Not Found"),
          @ApiResponse(
              responseCode = "404",
              description = "Not Found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Message id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "attachmentId",
              description = "Attachment id",
              in = ParameterIn.PATH)})
  public Response getConversationMessageAttachmentContent(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId,
      @Parameter(hidden = true) @PathParam("attachmentId") int attachmentId)
      throws DatabaseInteractionException,
      UnsupportedSchemaVersionException,
      NoDataFoundException, SupportingResourceNotFoundException {

    validateConversationIds(messageId, conversationId, attachmentId);

    ConversationManager conversationManager = getImpl(ConversationManager.class);
    Conversation conversation = conversationManager.getConversationById(conversationId);

    ConversationMessageAttachmentContentManager manager =
        getImpl(ConversationMessageAttachmentContentManager.class);
    ConversationMessageAttachmentContent content =
        manager.getContentsByAttachmentId(attachmentId, conversation.getConversationType());

    if (content == null) {
      throw new NoDataFoundException("The resource doesn't exist.");
    }
    return Response.ok(content.getContent(), APPLICATION_OCTET_STREAM_VALUE).build();
  }

  /**
   * Saves the content of conversation message attachment.
   *
   * @param conversationId The conversation Id
   * @param messageId The message Id
   * @param attachmentId The attachment Id
   * @param file Multipart request body containing attachment contents(binary data)
   * @return 201 in response
   **/
  @POST
  @Consumes("multipart/form-data")
  @Path("/{conversationId}/messages/{messageId}/attachments/{attachmentId}/contents")
  @PreAuthorize("#oauth2.hasScope('user/provider.Attachment.create')")
  @Operation(
      summary = "Saves the metadata of conversation message attachment",
      description = "Saves the metadata of conversation message attachment",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created"),
          @ApiResponse(
              responseCode = "404",
              description = "The supporting resource not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Message id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "attachmentId",
              description = "Attachment id",
              in = ParameterIn.PATH)})
  @RequestBody(
      description = "Multipart request body containing attachment contents(binary data)",
      content = {
          @Content(mediaType = MediaType.MULTIPART_FORM_DATA,
              schema = @Schema(implementation = ConversationAttachmentMultipartDefinition.class))})
  public Response createConversationMessageAttachmentContent(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("messageId") int messageId,
      @Parameter(hidden = true) @PathParam("attachmentId") int attachmentId,
      @Parameter(hidden = true) @Multipart(value = "file") Attachment file)
      throws com.qhrtech.emr.accuro.model.exceptions.ProtossException {

    validateConversationIds(messageId, conversationId, attachmentId);

    byte[] fileBytes = file.getObject(byte[].class);

    if (fileBytes.length == 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "File content is required.");
    }

    ConversationManager conversationManager = getImpl(ConversationManager.class);
    Conversation conversation = conversationManager.getConversationById(conversationId);

    ConversationMessageAttachmentContent content = new ConversationMessageAttachmentContent();
    content.setContent(fileBytes);
    content.setAttachmentId(attachmentId);

    ConversationMessageAttachmentContentManager manager =
        getImpl(ConversationMessageAttachmentContentManager.class);
    manager.createContent(content, conversation.getConversationType());

    return Response.status(Status.CREATED).build();
  }


  /**
   * Update the link between conversation and prescription.
   *
   * <p>
   * Note: If the link does not exist it will create new link.
   *
   * If the link exists but different prescription id it will update the link with the given
   * prescription id.
   *
   * If the request is identical with the existing link it won't do anything.
   * </p>
   *
   * @param conversationId Conversation ID
   * @param prescriptionMedicationId Prescription medication ID
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{conversationId}/medications/{prescriptionMedicationId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Updates the link between conversation and prescription.",
      description = "Updates the link between conversation and prescription. If the link does not "
          + "exist, it will create a new link. If the link exists but with a different prescription"
          + " id, it will update the link with the given prescription id. If the request is "
          + "identical with the existing link, it won't do anything.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "404",
              description = "Conversation not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionMedicationId",
              description = "Prescription Medication id",
              in = ParameterIn.PATH)})
  public Response updateConversationPrescriptionLink(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("prescriptionMedicationId") int prescriptionMedicationId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException {

    ConversationManager manager = getImpl(ConversationManager.class);

    if (manager.getConversationById(conversationId) == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "The conversation not found.");
    }

    manager.addPrescriptionToConversation(conversationId, prescriptionMedicationId);

    return Response.status(Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{conversationId}/external-patients/{externalPatientId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Updates the link between conversation and external patient.",
      description = "Updates the link between conversation and and external patient. "
          + "If the link does not exist, it will create a new link.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "404",
              description = "Conversation not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "messageId",
              description = "Conversation message id",
              in = ParameterIn.PATH)})
  public Response updateConversationExternalPatientLink(
      @Parameter(hidden = true) @PathParam("conversationId") int conversationId,
      @Parameter(hidden = true) @PathParam("externalPatientId") int externalPatientId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    ConversationExternalPatient conversationExternalPatient = new ConversationExternalPatient();
    conversationExternalPatient.setConversationId(conversationId);
    conversationExternalPatient.setExternalPatientId(externalPatientId);

    ConversationExternalPatientManager manager = getImpl(ConversationExternalPatientManager.class);
    try {
      manager.create(conversationExternalPatient);
    } catch (ResourceConflictException ex) {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Retrieves List of {@link ExternalPatientDto external patients} associated with conversation id.
   *
   * @param conversationId The id of the external patient
   * @return The List of {@link ExternalPatientDto conversation external patients}
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 404 External Patient not found
   */
  @GET
  @Path("/{conversationId}/external-patients")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves external patient object(s) by conversation Id",
      description = "Retrieves external patient object(s) associated with "
          + "the conversation id. ",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ExternalPatientDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "External Patient not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<ExternalPatientDto> getExternalPatientsByConversationId(
      @Parameter(description = "Conversation id") @PathParam("conversationId") int conversationId)
      throws com.qhrtech.emr.accuro.model.exceptions.ProtossException {

    ExternalPatientManager extPatientManager = getImpl(ExternalPatientManager.class);
    List<ExternalPatient> externalPatientList =
        extPatientManager.getByConversationId(conversationId);

    return mapDto(externalPatientList, ExternalPatientDto.class, ArrayList::new);
  }

  /**
   * Archives the conversation.
   *
   * @param conversationId Conversation ID
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */

  @PUT
  @Path("/{id}/archive")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Archives the conversation",
      description = "Archives the conversation.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "404",
              description = "If conversation does not exist.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "id",
              description = "Conversation id",
              in = ParameterIn.PATH)})
  public Response archiveConversation(
      @Parameter(hidden = true) @PathParam("id") int conversationId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException {

    ConversationManager manager = getImpl(ConversationManager.class);

    manager.archiveConversation(conversationId);

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Unarchives the conversation.
   *
   * @param conversationId Conversation ID
   * @return 204 response if success.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{id}/unarchive")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.update' )")
  @Operation(
      summary = "Unarchives the conversation",
      description = "Unarchives the conversation.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "404",
              description = "If conversation does not exist.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "conversationId",
              description = "Conversation id",
              in = ParameterIn.PATH)})
  public Response unarchiveConversation(
      @Parameter(hidden = true) @PathParam("id") int conversationId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException {

    ConversationManager manager = getImpl(ConversationManager.class);

    manager.unarchiveConversation(conversationId);

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Deletes Conversation and all its linked resources.
   *
   * @param id conversation id
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 204 no content after delete
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.delete' ) ")
  @Operation(
      summary = "Deletes a conversation and all its linked resources.",
      description = "Deletes a conversation and all its linked resources.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "no content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(@Parameter(
      description = "Conversation id") @PathParam("id") int id)
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    ConversationManager manager = getImpl(ConversationManager.class);
    manager.deleteAllConversationLinkedResources(id, getUser());
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Deletes ConversationMessage and all its linked resources.
   *
   * @param messageId ConversationMessage id
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 204 no content after delete
   */
  @DELETE
  @Path("/{conversationId}/messages/{messageId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.delete' ) ")
  @Operation(
      summary = "Deletes conversation message and all its linked resources.",
      description = "Deletes conversation message and all its linked resources.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "no content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response deleteConversationMessageById(@Parameter(
      description = "Conversation id") @PathParam("conversationId") int conversationId,
      @Parameter(description = "Conversation message id") @PathParam("messageId") int messageId)
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    ConversationManager manager = getImpl(ConversationManager.class);
    ConversationMessageManager messageManager = getImpl(ConversationMessageManager.class);

    ConversationMessage message = messageManager.getMessageById(messageId);

    if (Objects.isNull(message)) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Conversation message is not found");
    }

    if (message.getConversationId() != conversationId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Requested message do not belong to the provided conversation id ");
    }

    manager.deleteConversationMessageAndLinks(messageId, getUser());
    return Response.status(Status.NO_CONTENT).build();
  }

  private class ConversationAttachmentMultipartDefinition {

    @Schema(description = "File binary", type = "Attachment", format = "binary")
    Attachment file;

    public Attachment getFile() {
      return file;
    }

    public void setFile(Attachment file) {
      this.file = file;
    }
  }

  private ConversationMessageAttachment validateConversationIds(int messageId, int conversationId,
      int attachmentId)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException {
    ConversationMessageManager messageManager = getImpl(ConversationMessageManager.class);
    ConversationMessage message = messageManager.getMessageById(messageId);

    if (message == null
        || message.getMessageId() != messageId
        || message.getConversationId() != conversationId) {
      throw new SupportingResourceNotFoundException("The supporting resource doesn't exist.");
    }

    ConversationMessageAttachmentManager managerAttachment =
        getImpl(ConversationMessageAttachmentManager.class);
    ConversationMessageAttachment attachmentById =
        managerAttachment.getAttachmentById(attachmentId);

    if (attachmentById == null || attachmentById.getConversationMessageId() != messageId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Conversation message attachment not found with attachment id: " + attachmentId
              + " and message id: " + messageId);
    }

    return attachmentById;
  }
}
