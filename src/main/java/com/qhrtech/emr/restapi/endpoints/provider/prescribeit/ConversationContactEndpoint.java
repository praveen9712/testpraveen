
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.synapse.ConversationContact;
import com.qhrtech.emr.accuro.model.synapse.UserConversationContact;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationContactDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.UserConversationContactDto;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to create/retrieve conversation contact.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/conversation-contacts")
@Facet("provider-portal")
@Tag(name = "Conversation Contact Endpoints",
    description = "Exposes conversation contact endpoints")
public class ConversationContactEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Retrieves {@link ConversationContactDto conversation contact} associated with the given
   * conversation contact id.
   *
   * @param contactId The id of the conversation contact
   *
   * @return The {@link ConversationContactDto conversation contact}
   *
   * @HTTP 200 Success
   * @HTTP 404 Conversation Contact not found
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{contactId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves Conversation Contact",
      description = "Retrieves Conversation Contact associated with "
          + "the given conversation contact id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationContactDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Conversation Contact not found")})
  @Parameter(
      name = "authorization",
      description = "Provider authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ConversationContactDto getConversationContactById(
      @Parameter(description = "Conversation contact id",
          example = "1") @PathParam("contactId") int contactId)
      throws ProtossException {
    ConversationContactManager manager = getImpl(ConversationContactManager.class);
    ConversationContact contact = manager.getContactById(contactId);

    return mapDto(contact, ConversationContactDto.class);
  }

  /**
   * Search {@link ConversationContactDto conversation contacts}. The identifier is exact search.
   *
   * @param identifier The identifier of the contact. Optional. It is exact search.
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId The starting {@code contactId} (exclusive) of the next page of data.
   *        Typically this is the {@code EnvelopeDto.lastId} from the last page.
   *
   * @return The list of the {@link ConversationContactDto conversation contacts}
   *
   * @HTTP 200 Success
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves all conversation contact which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is same as "
          + " **Envelope.lastId** of the previous page."
          + " Last id is the **contactId** of the last record of the page, "
          + "and results will be ordered by this field i.e **contactId**. "
          + "Identifier searches are exact search.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ConversationContactDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "identifier",
              description = "The identifier of the conversation contact",
              example = "7dfbc81e-5e18-4d24-9750-f884869b8855",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is **Envelope.lastId** of the previous page(request)"
                  + "It is same as the **contactId** of the last records of the previous results.",
              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<ConversationContactDto> getConversationContacts(
      @QueryParam("identifier") String identifier,
      @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws ProtossException {
    Integer actualPageSize =
        StringUtils.isBlank(pageSize) ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSize);

    if (actualPageSize <= 0) {
      actualPageSize = DEFAULT_PAGE_SIZE;
    } else if (actualPageSize > MAX_PAGE_SIZE) {
      actualPageSize = MAX_PAGE_SIZE;
    }

    Integer actualStartingId =
        StringUtils.isBlank(startingId) ? null : Integer.parseInt(startingId);

    ConversationContactManager manager = getImpl(ConversationContactManager.class);
    Envelope<ConversationContact> contactEnvelope =
        manager.searchConversationContacts(identifier, actualStartingId, actualPageSize);

    EnvelopeDto<ConversationContactDto> result = new EnvelopeDto<>();
    result.setContents(
        mapDto(contactEnvelope.getContents(), ConversationContactDto.class, ArrayList::new));
    result.setCount(contactEnvelope.getCount());
    result.setTotal(contactEnvelope.getTotal());
    result.setLastId(contactEnvelope.getLastId());
    return result;
  }

  /**
   * Creates {@link ConversationContactDto conversation contact}
   *
   * @param conversationContactDto The conversation contact
   *
   * @return The unique id of {@link ConversationContactDto conversation contact}
   *
   * @HTTP 201 Created
   * @HTTP 400 If the conversation contact not provided
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.create' ) ")
  @Operation(
      summary = "Creates Conversation contact",
      description = "Creates Conversation contact",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1",
                      description = "Returns the unique id of the conversation contact"))),
          @ApiResponse(
              responseCode = "400",
              description = "If the conversation contact not provided")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)
      })
  public Response createConversationContact(@Valid ConversationContactDto conversationContactDto)
      throws ProtossException {
    if (conversationContactDto == null) {
      throw new IllegalArgumentException("The ConversationContactDto is required");
    }

    ConversationContactManager manager = getImpl(ConversationContactManager.class);

    if (manager.getContactByIdentifierAndService(conversationContactDto.getIdentifier(),
        conversationContactDto.getService()) != null) {
      throw new ResourceConflictException(
          "A contact already exists with the given identifier and service");
    }

    int id = manager.createContact(mapDto(conversationContactDto, ConversationContact.class));
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * link user to contacts
   *
   * @param contactId conversation contact Id.
   * @param userId accuro user Id.
   *
   *
   * @HTTP 204 response if success.
   * @HTTP 400 If one of the param is not a valid id.
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{contactId}/users/{userId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.create' ) ")
  @Operation(
      summary = "Create accuro user to conversation contact link",
      description = "Link an accuro user to conversation contact",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Linked"),
          @ApiResponse(
              responseCode = "400",
              description = "If the conversation contact not provided")})
  @Parameters(
      value = {
          @Parameter(
              name = "contactId",
              description = "Conversation contact Id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "userId",
              description = "Accuro user Id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)
      })
  public Response linkConversationUserContact(
      @PathParam("contactId") int contactId,
      @PathParam("userId") int userId) throws ProtossException {

    ConversationContactManager manager = getImpl(ConversationContactManager.class);

    List<UserConversationContact> existinglinks = manager.getUserContacts(contactId);

    if (existinglinks.stream().anyMatch(l -> l.getUserId() == userId)) {
      return Response.status(Status.NO_CONTENT).build();
    }

    UserConversationContact userConversationContact = new UserConversationContact();

    userConversationContact.setContactId(contactId);
    userConversationContact.setUserId(userId);
    userConversationContact.setLastSyncTime(DateTime.now(DateTimeZone.UTC));
    manager.createUserContact(userConversationContact);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Retrieve links of user to conversation contact
   *
   * @return a list of UserConversationContactDto
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{contactId}/users")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves links of user to conversation contact",
      description = "Retrieves links of user to conversation contact associated with "
          + "the given conversation contact id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = UserConversationContactDto.class)))),
          @ApiResponse(
              responseCode = "404",
              description = "Conversation Contact not found")})
  @Parameter(
      name = "authorization",
      description = "Provider authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<UserConversationContactDto> getUserConversationContact(
      @Parameter(description = "Conversation contact id",
          example = "1") @PathParam("contactId") int contactId)
      throws ProtossException {

    ConversationContactManager manager = getImpl(ConversationContactManager.class);
    List<UserConversationContact> links = manager.getUserContacts(contactId);

    return mapDto(links, UserConversationContactDto.class, ArrayList::new);
  }


}
