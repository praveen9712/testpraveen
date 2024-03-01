
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestGroupManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequest;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestGroup;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.RenewalRequestGroupDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * End points for retrieving and creating renewal requests groups.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/renewal-request-groups")
@Tag(name = "Renewal request groups Endpoints",
    description = "Exposes Renewal request groups endpoints.")
public class RenewalRequestGroupsEndpoint extends AbstractEndpoint {

  /**
   * Gets renewal requests group by ID.
   *
   * @param id renewal request group id
   * @return {@link RenewalRequestGroupDto} object
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxRenewalRequest.read' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to renewal request groups with a provider the user has this"
          + " permission for (if Accuro is configured to enforce provider permissions for"
          + " prescriptions).")
  @Operation(
      summary = "Retrieves renewal request group by id",
      description = "Retrieves renewal request group by id",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Renewal request group not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = RenewalRequestGroupDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public RenewalRequestGroupDto getRenewalRequestGroup(
      @PathParam("id") int id)
      throws ProtossException {
    RenewalRequestGroupManager manager = getImpl(RenewalRequestGroupManager.class);
    return mapDto(manager.getRenewalRequestGroupById(id), RenewalRequestGroupDto.class);
  }

  /**
   * Creates new renewal request group.
   *
   * @param renewalRequestGroup JSON representation of the renewal request group fields
   * @return renewal request group id newly created resource.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 invalid pharmacyPatientId, matchedPatient Id, accuro physician Id, pharmacy contact
   *       Id or conversation message Id
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxRenewalRequest.create' ) ")
  @ProviderPermissions(providerPermissions = {
      @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
          description = "Allows access if the user "
              + "has this permission for the Accuro provider."),
      @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full,
          description = "Allows access "
              + "if the user has this permission for the Accuro provider.")})
  @Operation(
      summary = "Creates a new renewal request group",
      description = "Creates a new renewal request group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Conversation Unmatched Patient id (Pharmacy Patient id), "
                  + "Matched Patient id, Accuro physician id, Matched Pharmacy Contact id, "
                  + "or Conversation message id"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns a new renewal request group id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createRenewalRequestGroup(@Valid RenewalRequestGroupDto renewalRequestGroup)
      throws ProtossException {
    if (renewalRequestGroup == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Renewal request group cannot be null.");
    }
    RenewalRequestGroupManager manager = getImpl(RenewalRequestGroupManager.class);
    RenewalRequestGroup requestGroup = mapDto(renewalRequestGroup, RenewalRequestGroup.class);
    return manager.create(requestGroup);

  }

  /**
   * Updates renewal request group.
   *
   * @param renewalRequestGroup JSON representation of the renewal request group fields
   * @return renewal request group id newly created resource.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 invalid pharmacyPatientId, matchedPatient Id, accuro physician Id, pharmacy contact
   *       Id or conversation message Id
   */
  @PUT
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxRenewalRequest.update' ) ")
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
              description = "Allows access if the "
                  + "user has this permission for the Accuro provider."),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full,
              description = "Allows access "
                  + "if the user has this permission for the Accuro provider.")})
  @Operation(
      summary = "Updates a renewal request group",
      description = "Updates a new renewal request group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Conversation Unmatched Patient id (Pharmacy Patient id), "
                  + "Matched Patient id, Accuro physician id, Matched Pharmacy Contact id, "
                  + "or Conversation message id"),
          @ApiResponse(
              responseCode = "400",
              description = "renewal request group is null"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "204",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public void updateRenewalRequestGroup(@PathParam("id") int id,
      @Valid RenewalRequestGroupDto renewalRequestGroup)
      throws ProtossException {
    if (renewalRequestGroup == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Renewal request group cannot be null.");
    }

    if (id != renewalRequestGroup.getId()) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The resource id and group id in the object body should be same.");
    }
    RenewalRequestGroupManager manager = getImpl(RenewalRequestGroupManager.class);
    RenewalRequestGroup requestGroup = mapDto(renewalRequestGroup, RenewalRequestGroup.class);
    manager.update(requestGroup);
  }

  /**
   * Deletes renewal request group.
   *
   * @param id Id of the group which needs to be deleted
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If group to be deleted is not found
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
              description = "Allows access if the"
                  + "user has this permission for the Accuro provider."),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full,
              description = "Allows access "
                  + "if the user has this permission for the Accuro provider.")})
  @Operation(
      summary = "Deletes a renewal request group",
      description = "Delete a new renewal request group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "if the renewal request group is still in use"),
          @ApiResponse(
              responseCode = "404",
              description = "renewal request group to be deleted is not found"),
          @ApiResponse(
              responseCode = "204",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with API_INTERNAL scope",
      in = ParameterIn.HEADER,
      required = true)
  public void deleteRenewalRequestGroup(@PathParam("id") int id)
      throws ProtossException {

    // Check in use
    RenewalRequestManager requestManager = getImpl(RenewalRequestManager.class);
    List<RenewalRequest> requests = requestManager.getRenewalRequestsByRenewalRequestGroupId(id);
    if (requests.size() > 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Renewal request group cannot be deleted. It's still in use.");
    }

    RenewalRequestGroupManager manager = getImpl(RenewalRequestGroupManager.class);
    manager.delete(id);
  }

}
