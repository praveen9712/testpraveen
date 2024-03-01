
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelRequest;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.dataaccess.exception.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ErxCancelRequestsDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This <code>ErxCancelRequestEndpoint</code> collection is designed to expose the Eprescribe cancel
 * requests endpoints. Requires provider level authorization or client credentials grant with QHR
 * first party scope.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials grant with
 *                QHR first party scope
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 */
@Component
@Path("/v1/provider-portal/erx-cancel-requests/")
@Facet("provider-portal")
@Tag(name = "Eprescribe cancel requests endpoints",
    description = "Eprescribe cancel requests endpoints")
public class ErxCancelRequestEndpoint extends AbstractEndpoint {

  /**
   * Retrieves Eprescribe cancel requests associated with the id.
   *
   * @param localId local id of erx cancel request
   * @return Erx cancel request {@link ErxCancelRequestsDto} data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{localId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.read' ) ")
  @Operation(
      summary = "Retrieves Eprescribe cancel requests",
      description = "Retrieves Eprescribe cancel requests associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ErxCancelRequestsDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public ErxCancelRequestsDto getByLocalId(@Parameter(
      description = "Erx cancel request local id") @PathParam("localId") int localId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException {

    EprescribeCancelRequestManager manager = getImpl(EprescribeCancelRequestManager.class);

    return mapDto(manager.getEprescribeCancelRequestById(localId), ErxCancelRequestsDto.class);

  }

  /**
   * Retrieves Eprescribe cancel request associated with the given external id or prescription id.
   *
   * If both query parameters are provided, prescription id will be ignored.
   *
   * @param externalId External id of erx cancel request
   * @param prescriptionId Prescription id of erx cancel request
   * @return Erx cancel request {@link ErxCancelRequestsDto} data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.read' ) ")
  @Operation(
      summary = "Retrieves Erx cancel request by external id or by prescription id",
      description = "Retrieves Erx cancel request by external id or by prescription id."
          + "If both are provided, prescription id will be ignored.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ErxCancelRequestsDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public ErxCancelRequestsDto getByExternalIdOrPrescriptionId(
      @QueryParam("externalId") String externalId,
      @QueryParam("prescriptionId") Integer prescriptionId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    if (StringUtils.isBlank(externalId) && prescriptionId == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "External id or prescription id is required");
    }

    EprescribeCancelRequestManager manager = getImpl(EprescribeCancelRequestManager.class);

    if (StringUtils.isNotBlank(externalId)) {
      return mapDto(manager.getEprescribeCancelRequestByExternalId(UUID.fromString(externalId)),
          ErxCancelRequestsDto.class);
    } else {
      return mapDto(manager.getEprescribeCancelRequestByRxId(prescriptionId),
          ErxCancelRequestsDto.class);
    }
  }

  /**
   * Creates Eprescribe cancel request.
   *
   * @param erxCancelRequestsDto {@link ErxCancelRequestsDto} Erx cancel request dto
   * @return Newly created Erx cancel request ID
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.create' ) ")
  @Operation(
      summary = "Creates Eprescribe cancel request",
      description = "Creates Eprescribe cancel request.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created. Returns erx cancel request id.",
              content = @Content(
                  schema = @Schema(type = "Integer", example = "1")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(@RequestBody @Valid ErxCancelRequestsDto erxCancelRequestsDto)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException,
      TimeZoneNotFoundException {

    if (erxCancelRequestsDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Erx cancel request is required.");
    }

    EprescribeCancelRequestManager manager = getImpl(EprescribeCancelRequestManager.class);

    int id = manager
        .createEprescribeCancelRequest(mapDto(erxCancelRequestsDto, EprescribeCancelRequest.class));

    return Response.status(Status.CREATED).entity(id).build();

  }

  /**
   * Deletes Eprescribe cancel request by localId.
   *
   * @param id Erx calcel request id
   * @throws ProtossException If there has been a database error.
   */
  @DELETE
  @Path("/{localId}")
  @Hidden
  @Facet("internal")
  @PreAuthorize("#oauth2.hasScope( 'API_INTERNAL' ) ")
  @Operation(
      summary = "Deletes Eprescribe cancel request by localId.",
      description = "Deletes Eprescribe cancel request by localId.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Internal Use Only",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(
      @Parameter(description = "Erx cancel request local id") @PathParam("localId") int id)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException,
      TimeZoneNotFoundException {

    EprescribeCancelRequestManager manager = getImpl(EprescribeCancelRequestManager.class);

    manager.deleteEprescribeCancelRequest(id);

    return Response.status(Status.NO_CONTENT).build();

  }

  /**
   * Updates Eprescribe cancel request.
   *
   * @param erxCancelRequestsDto {@link ErxCancelRequestsDto} Erx cancel request dto
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{localId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.update' ) ")
  @Operation(
      summary = "Updates Eprescribe cancel request",
      description = "Updates Eprescribe cancel request.",
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
  public Response update(@RequestBody @Valid ErxCancelRequestsDto erxCancelRequestsDto,
      @Parameter(description = "Erx cancel request local id") @PathParam("localId") int localId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException, NoDataFoundException,
      TimeZoneNotFoundException {

    if (erxCancelRequestsDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Erx cancel request is required.");
    }

    if (localId != erxCancelRequestsDto.getLocalId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Local id in path does not match with the body.");
    }

    EprescribeCancelRequestManager manager = getImpl(EprescribeCancelRequestManager.class);

    manager
        .updateEprescribeCancelRequest(mapDto(erxCancelRequestsDto, EprescribeCancelRequest.class));

    return Response.status(Status.NO_CONTENT).build();

  }

}
