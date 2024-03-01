
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTypeManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTypeDto;
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
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * End point for retrieving all the Eprescribe Job types.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-job-types")
@Tag(name = "Eprescribe Job Type Endpoint",
    description = "Exposes endpoint to retrieve Eprescribe Job Types.")
public class EprescribeJobTypeEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all the Eprescribe Job Types available.
   *
   * @return List of {@link EprescribeJobTypeDto} object
   * @throws ProtossException If there is any exception occured.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly)
  @Operation(
      summary = "Retrieves all the Eprescribe Job Types available.",
      description = "Retrieves all the Eprescribe Job Types available.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = EprescribeJobTypeDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeJobTypeDto> getAllJobTypes()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    EprescribeJobTypeManager manager = getImpl(EprescribeJobTypeManager.class);

    return mapDto(manager.getAllJobTypes(), EprescribeJobTypeDto.class, ArrayList::new);

  }

  /**
   * Creates new Eprescribe Job Type.
   *
   * @return ID of the newly created job type
   * @throws ProtossException If there is any exception occured.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Hidden
  @Facet("internal")
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full)})
  @Operation(
      summary = "Creates new Eprescribe Job Type",
      description = "Creates new Eprescribe Job Type.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @RequestBody @Valid EprescribeJobTypeDto jobTypeDto)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {

    EprescribeJobTypeManager manager = getImpl(EprescribeJobTypeManager.class);

    EprescribeJobType eprescribeJobType = mapDto(jobTypeDto, EprescribeJobType.class);

    int id = manager.createEprescribeJobType(eprescribeJobType);

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Deletes the Eprescribe Job Type associated with the given ID.
   *
   * @param id Id of the Eprescribe job type
   * @throws ProtossException If there is any exception occured.
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Hidden
  @Facet("internal")
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full)})
  @Operation(
      summary = "Deletes the Eprescribe Job Type associated with the given ID.",
      description = "Deletes the Eprescribe Job Type associated with the given ID.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(
      @Parameter(description = "Eprescribe job type id") @PathParam("id") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {

    EprescribeJobTypeManager manager = getImpl(EprescribeJobTypeManager.class);

    manager.deleteEprescribeJobType(id);

    return Response.status(Status.NO_CONTENT).build();
  }


}
