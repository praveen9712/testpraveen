
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeCancelResponse;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeCancelResponseDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>EprescribeCancelResponseEndpoint</code> collection is designed to expose the
 * Eprescribe cancel response endpoints. Requires provider level authorization or client credentials
 * grant with QHR first party scope.
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
    description = "Eprescribe cancel response endpoints")
public class EprescribeCancelResponseEndpoint extends AbstractEndpoint {

  /**
   * Retrieves Eprescribe cancel responses associated with the cancel request id.
   *
   * @param cancelRequestId local id of erx cancel request
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @return Erx cancel request {@link EprescribeCancelResponseDto} data transfer object.
   */
  @GET
  @Path("/{cancelRequestId}/responses")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.read' ) ")
  @Operation(
      summary = "Retrieves Eprescribe cancel responses",
      description = "Retrieves Eprescribe cancel responses associated "
          + "with the cancel request id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = EprescribeCancelResponseDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with QHR first party scope",
      in = ParameterIn.HEADER,
      required = true)
  public EprescribeCancelResponseDto getByRequestLocalId(@Parameter(
      description = "Erx cancel request id") @PathParam("cancelRequestId") int cancelRequestId)
      throws ProtossException {

    EprescribeCancelResponseManager manager = getImpl(EprescribeCancelResponseManager.class);

    return mapDto(manager.getEprescribeCancelResponseByCancelRequestlId(cancelRequestId),
        EprescribeCancelResponseDto.class);

  }

  /**
   * Creates Eprescribe cancel response for a cancel request.
   *
   * @param erxCancelResponseDto {@link EprescribeCancelResponseDto} Erx calcel request dto
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @return Newly created Erx cancel response ID
   */
  @POST
  @Path("/{cancelRequestId}/responses")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxCancelRequest.create' ) ")
  @Operation(
      summary = "Creates Eprescribe cancel response",
      description = "Creates Eprescribe cancel response.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer", example = "1"))),
          @ApiResponse(
              responseCode = "400",
              description = "If violate field validations.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with QHR first party scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(@Parameter(
      description = "Erx cancel request id") @PathParam("cancelRequestId") int cancelRequestId,
      @RequestBody(
          description = "CancelResponse") @Valid EprescribeCancelResponseDto erxCancelResponseDto)
      throws ProtossException {

    if (erxCancelResponseDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Erx cancel response is required.");
    }

    if (cancelRequestId != erxCancelResponseDto.getEprescribeCancelRequestId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Erx cancel request id from the path should match the id provided in the body.");
    }

    EprescribeCancelResponseManager manager = getImpl(EprescribeCancelResponseManager.class);

    int id = manager
        .createEprescribeCancelResponse(
            mapDto(erxCancelResponseDto, EprescribeCancelResponse.class));

    return Response.status(Status.CREATED).entity(id).build();

  }

}
