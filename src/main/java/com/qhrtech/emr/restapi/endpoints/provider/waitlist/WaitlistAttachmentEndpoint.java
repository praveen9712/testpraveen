
package com.qhrtech.emr.restapi.endpoints.provider.waitlist;

import static com.qhrtech.emr.accuro.model.attachments.DiagnosisLinkSourceType.SWL_REQUEST;
import com.qhrtech.emr.accuro.api.attachments.DiagnosisLinkManager;
import com.qhrtech.emr.accuro.model.attachments.DiagnosisLink;
import com.qhrtech.emr.accuro.model.attachments.ItemCategory;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.attachment.ItemCategoryDto;
import com.qhrtech.emr.restapi.models.dto.attachment.WaitlistAttachmentDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code WaitlistAttachmentEndpoint} is designed to create information relating to the
 * waitlist attachments.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 400 The module of the waitlist is not enabled
 * @HTTP 400 The given input is invalid
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/waitlists")
@Facet("provider-portal")
@Tag(name = "WaitlistAttachment Endpoints", description = "Exposes waitlist attachment endpoints")
public class WaitlistAttachmentEndpoint extends AbstractEndpoint {

  private static DiagnosisLink convertToDiagnosisLink(WaitlistAttachmentDto attachment,
      int waitlistId) {
    DiagnosisLink link = new DiagnosisLink();
    link.setDiagnosisId(waitlistId);
    link.setItemId(attachment.getItemId());
    if (attachment.getItemCategoryDto() != null) {
      link.setItemCategory(ItemCategory.lookup(attachment.getItemCategoryDto().getCategoryId()));
    }
    // hard code as this endpoint caters to only this source type as of now
    link.setSourceType(SWL_REQUEST);
    return link;
  }

  private static WaitlistAttachmentDto convertToAttachment(DiagnosisLink link) {
    if (link == null || SWL_REQUEST != link.getSourceType()) {
      return null;
    }
    WaitlistAttachmentDto attachment = new WaitlistAttachmentDto();
    attachment.setId(link.getId());
    attachment.setWaitlistId(link.getDiagnosisId());
    attachment.setItemId(link.getItemId());
    if (link.getItemCategory() != null) {
      attachment.setItemCategoryDto(ItemCategoryDto.lookup(link.getItemCategory().getCategoryId()));
    }
    return attachment;
  }

  /**
   * Saves all attachments for the given waitlist id.
   *
   * @param listId The waitlist id
   * @param attachments The set of attachments
   * @throws ProtossException If there has been a database error
   * @throws SaveException If item id is invalid
   * @HTTP 204 Success but no content
   */
  @POST
  @Path("/{waitlistId}/attachments")
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_WRITE' ) ")
  @Operation(
      summary = "Saves all the attachments for the given waitlist id.",
      description = "Saves all the attachments for the given waitlist id. Refer to the attachments"
          + " object documentation to see what goes in each field. ",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success but no content"),
          @ApiResponse(responseCode = "400",
              description = "The module of the waitlist is not enabled"),
          @ApiResponse(responseCode = "400", description = "The given input is invalid"),

      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @RequestBody(description = "The list of attachments objects which needs to be linked to the "
      + "waitlist request",
      content = @Content(
          array = @ArraySchema(
              schema = @Schema(implementation = WaitlistAttachmentDto.class))))
  public void create(
      @Parameter(description = "The waitlist id") @PathParam("waitlistId") int listId,
      @Parameter(description = "The set of attachement(s)"
          + "") @RequestBody Set<WaitlistAttachmentDto> attachments)
      throws ProtossException {
    Set<DiagnosisLink> links = new HashSet<>();
    for (WaitlistAttachmentDto attachment : attachments) {

      if (attachment.getWaitlistId() != null) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "The waitlistId is read only field.");
      }
      if (attachment.getItemCategoryDto() == null) {
        throw Error.webApplicationException(Status.BAD_REQUEST, "Unsupported or "
            + "null item category.");
      }
      links.add(convertToDiagnosisLink(attachment, listId));
    }

    if (links.isEmpty()) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "The attachments must be given.");
    }

    DiagnosisLinkManager diagnosisLinkManager = getImpl(DiagnosisLinkManager.class);
    diagnosisLinkManager.createLink(links, listId, getUser());
  }

  /**
   * Retrieves waitlist attachment by the waitlist id and the attachment ids.
   *
   * @param waitlistId The waitlist id.
   * @param attachmentId The attachment id.
   * @return The waitlist attachment
   * @throws ProtossException If there has been a database error
   * @HTTP 200 Success
   * @HTTP 404 The waitlist with the given id does not exist
   */
  @GET
  @Path("/{waitlistId}/attachments/{attachmentId}")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves waitlist attachment by the waitlist id and the attachment ids.",
      description = "Retrieves waitlist attachment by the waitlist id and the attachment ids.",
      responses = {
          @ApiResponse(responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = WaitlistAttachmentDto.class))

          ),
          @ApiResponse(responseCode = "400",
              description = "The module of the waitlist is not enabled"),
          @ApiResponse(responseCode = "400", description = "The given input is invalid"),
          @ApiResponse(responseCode = "404",
              description = "The waitlist with the given id does not exist")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public WaitlistAttachmentDto getById(
      @Parameter(description = "The waitlist id") @PathParam("waitlistId") int waitlistId,
      @Parameter(description = "The attachment id") @PathParam("attachmentId") int attachmentId)
      throws ProtossException {
    DiagnosisLinkManager diagnosisLinkManager = getImpl(DiagnosisLinkManager.class);
    DiagnosisLink link = diagnosisLinkManager.getLinkById(attachmentId);

    WaitlistAttachmentDto attachment = convertToAttachment(link);
    if (attachment == null || (attachment.getWaitlistId() != waitlistId)) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "The waitlist with the given id does not exist.");
    }

    return attachment;
  }

  /**
   * Retrieves waitlist's attachments by the waitlist id.
   *
   * @param waitlistId The waitlist id
   * @return The set of the waitlist attachment
   * @throws ProtossException If there has been a database error
   * @HTTP 200 Success
   */
  @GET
  @Path("/{waitlistId}/attachments")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves waitlist attachments by the waitlist id.",
      description = "Retrieves waitlist attachments by the waitlist id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = WaitlistAttachmentDto.class)))),
          @ApiResponse(responseCode = "400",
              description = "The module of the waitlist is not enabled"),
          @ApiResponse(responseCode = "400", description = "The given input is invalid")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public Set<WaitlistAttachmentDto> getByWaitlistId(
      @Parameter(description = "The waitlist id") @PathParam("waitlistId") int waitlistId)
      throws ProtossException {
    DiagnosisLinkManager diagnosisLinkManager = getImpl(DiagnosisLinkManager.class);
    Set<DiagnosisLink> links = diagnosisLinkManager.getLinksByDiagnosisId(waitlistId);

    Set<WaitlistAttachmentDto> attachments = new HashSet<>();
    for (DiagnosisLink link : links) {
      WaitlistAttachmentDto attachment = convertToAttachment(link);
      if (attachment != null) {
        attachments.add(attachment);
      }
    }
    return attachments;
  }

  /**
   * Deletes the waitlist attachment by the given waitlist id and attachment ids.
   *
   * @param waitlistId The waitlist id
   * @param attachmentId The waitlist attachment id
   * @throws ProtossException If there has been a database error
   * @HTTP 204 Success but no content
   * @HTTP 404 The waitlist with the given id does not exist
   */
  @DELETE
  @Path("/{waitlistId}/attachments/{attachmentId}")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes the waitlist attachment by the given waitlist id and attachment ids.",
      description = "Deletes the waitlist attachment by the given waitlist id and attachment ids.",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success but no content"),
          @ApiResponse(responseCode = "400",
              description = "The module of the waitlist is not enabled"),
          @ApiResponse(responseCode = "400", description = "The given input is invalid"),
          @ApiResponse(responseCode = "404",
              description = "The waitlist with the given id does not exist")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public void deleteById(
      @Parameter(description = "The waitlist id") @PathParam("waitlistId") int waitlistId,
      @Parameter(description = "The attachment id") @PathParam("attachmentId") int attachmentId)
      throws ProtossException {
    DiagnosisLinkManager diagnosisLinkManager = getImpl(DiagnosisLinkManager.class);
    DiagnosisLink link = diagnosisLinkManager.getLinkById(attachmentId);

    WaitlistAttachmentDto attachment = convertToAttachment(link);
    if (attachment == null || (attachment.getWaitlistId() != waitlistId)) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "The waitlist with the given id does not exist.");
    }

    diagnosisLinkManager.deleteLinkById(attachmentId, getUser());
  }

  /**
   * Deletes the waitlist attachment by the given waitlist id.
   *
   * @param listId The waitlist id
   * @throws ProtossException If there has been a database error
   * @HTTP 204 Success but no content
   * @HTTP 404 The waitlist with the given id does not exist
   */
  @DELETE
  @Path("/{waitlistId}/attachments")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes the waitlist attachment by the given waitlist id.",
      description = "Deletes the waitlist attachment by the given waitlist id.",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success but no content"),
          @ApiResponse(responseCode = "400",
              description = "The module of the waitlist is not enabled"),
          @ApiResponse(responseCode = "400", description = "The given input is invalid"),
          @ApiResponse(responseCode = "404",
              description = "The waitlist with the given id does not exist")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public void deleteByWaitlistId(
      @Parameter(description = "The waitlist id") @PathParam("waitlistId") int listId)
      throws ProtossException {
    DiagnosisLinkManager diagnosisLinkManager = getImpl(DiagnosisLinkManager.class);
    Set<DiagnosisLink> links = diagnosisLinkManager.getLinksByDiagnosisId(listId);
    if (links.isEmpty()) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "The waitlist(s) do not exists for the given id");
    }

    if (links.stream().anyMatch(l -> SWL_REQUEST != l.getSourceType())) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "This endpoint currently supports to delete only waitlist attachment(s)");
    }

    diagnosisLinkManager.deleteLinksByDiagnosisId(listId, getUser());
  }
}
