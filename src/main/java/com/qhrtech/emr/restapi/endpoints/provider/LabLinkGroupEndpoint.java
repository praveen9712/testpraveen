
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.labs.LabLinkGroupManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.labs.LabLinkGroup;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LabLinkGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabLinkGroupReadOnlyDto;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * Endpoints to access Lab link groups.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 *
 */
@Component
@Path("/v1/provider-portal/lab-link-groups")
@Facet("provider-portal")
@Tag(name = "Lab Link Group Endpoints", description = "Exposes lab link group endpoints")
public class LabLinkGroupEndpoint extends AbstractEndpoint {

  /**
   * Get all the lab link groups available.
   *
   * @return Set of {@link LabLinkGroupReadOnlyDto}
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all the lab link groups available.",
      description = "Gets all the lab link groups available.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = LabLinkGroupReadOnlyDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<LabLinkGroupReadOnlyDto> getAll() throws ProtossException {
    LabLinkGroupManager linkGroupManager = getImpl(LabLinkGroupManager.class);

    Set<LabLinkGroup> labLinkGroups = linkGroupManager.getAll();

    return mapDto(labLinkGroups, LabLinkGroupReadOnlyDto.class, HashSet::new);
  }

  /**
   * Get the {@link LabLinkGroupReadOnlyDto} for the given lab group id.
   *
   * @return Set of {@link LabLinkGroupReadOnlyDto}
   *
   * @throws DatabaseInteractionException If there has been a database error.
   *
   * @HTTP 404 Lab Group not found.
   */
  @GET
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the lab link group by group id.",
      description = "Gets the lab link group which belong to the given group id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = LabLinkGroupReadOnlyDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public LabLinkGroupReadOnlyDto getByGroupId(
      @Parameter(description = "Lab link group id.") @PathParam("groupId") int groupId)
      throws ProtossException {
    LabLinkGroupManager linkGroupManager = getImpl(LabLinkGroupManager.class);

    LabLinkGroup labLinkGroups = linkGroupManager.getByGroupId(groupId);

    return mapDto(labLinkGroups, LabLinkGroupReadOnlyDto.class);
  }

  /**
   * Creates a link between the given lab results.
   * <p>
   * Note: Cannot create a link if any of the result IDs are already linked. Atleast two result ids
   * are required to create a link group.
   * </p>
   *
   * @param labLinkGroupDto {@link LabLinkGroupDto} request body
   * @return The generated link group id.
   *
   * @throws DatabaseInteractionException If there has been a database error.
   *
   * @HTTP 404 One or more provided lab results does not exist
   */
  @POST
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Creates lab link group.",
      description = "Creates a link between the given lab results. \n"
          + "Note: Cannot link result IDs which are already linked to "
          + "different group and non primary.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "One or more provided lab results does not exist."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "45",
                      description = "Returns id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public Integer createLabLinkGroup(
      @RequestBody(description = "Lab link group") LabLinkGroupDto labLinkGroupDto)
      throws ProtossException {

    if (labLinkGroupDto == null || labLinkGroupDto.getLinkedLabResultIds().isEmpty()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Lab results ids are required to create a link.");
    }

    LabLinkGroupManager linkGroupManager = getImpl(LabLinkGroupManager.class);

    return linkGroupManager.createResultLinks(labLinkGroupDto.getPrimaryResultId(),
        labLinkGroupDto.getLinkedLabResultIds());

  }

  /**
   * Updates lab results group. Primary result id cannot be updated. If new primary result id needs
   * to be updated then delete the existing link group and create new lab link group with new
   * primary result id.
   * <p>
   * Note: If any of the result IDs are already linked and is primary, it becomes the non-primary
   * and all its existing links get linked to the new primary link. Cannot link result IDs which are
   * already linked to different group and non primary.
   * </p>
   *
   * @param labLinkGroupDto {@link LabLinkGroupDto} request body
   * @param groupId Lab link group id.
   *
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Updates lab link group.",
      description = "Updates lab results group. Primary result id cannot be updated. "
          + " \n If new primary result id needs to be updated then delete the existing "
          + "link group and create new lab link group with new primary result id. \n"
          + "Note: If any of the result IDs are already linked and is primary, "
          + "it becomes the non-primary and all its existing links get linked "
          + "to the new primary link.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public Response updateLabLinkGroup(
      @Parameter(description = "Lab link group id.") @PathParam("groupId") int groupId,
      @RequestBody(description = "Lab link group") LabLinkGroupDto labLinkGroupDto)
      throws ProtossException {

    if (groupId != labLinkGroupDto.getLinkGroupId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Group Id in path does not match with resource: " + labLinkGroupDto.getLinkGroupId());
    }
    if (labLinkGroupDto == null || labLinkGroupDto.getLinkedLabResultIds().isEmpty()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Lab results ids are required to create a link.");
    }

    LabLinkGroupManager linkGroupManager = getImpl(LabLinkGroupManager.class);

    linkGroupManager.updateResultLinks(labLinkGroupDto.getLinkGroupId(),
        labLinkGroupDto.getPrimaryResultId(),
        labLinkGroupDto.getLinkedLabResultIds());

    return Response.ok().build();

  }

  /**
   * Deletes a link between the lab results {@link LabLinkGroup} associated with the given link
   * group id.
   *
   * @param groupId The link group id.
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 404 Lab Group does not exists.
   */
  @DELETE
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Updates link between the lab results.",
      description = "Deletes the link between the lab results.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Lab link group does not exist"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public Response deleteLabLinkGroup(
      @Parameter(description = "Lab link group id.") @PathParam("groupId") int groupId)
      throws ProtossException {

    LabLinkGroupManager linkGroupManager = getImpl(LabLinkGroupManager.class);

    linkGroupManager.deleteResultLinks(groupId);

    return Response.ok().build();
  }

}
