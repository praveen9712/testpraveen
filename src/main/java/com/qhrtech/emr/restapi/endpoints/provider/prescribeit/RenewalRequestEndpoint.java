
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequest;
import com.qhrtech.emr.dataaccess.exception.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.RenewalRequestDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
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

/**
 * This {@code RenewalRequestsEndpoint} is designed to expose {@link RenewalRequestDto} endpoints.
 * Requires Provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/renewal-requests")
@Facet("provider-portal")
@Tag(name = "RenewalRequests Endpoints",
    description = "Exposes RenewalRequests endpoints")
public class RenewalRequestEndpoint extends AbstractEndpoint {


  /**
   * Retrieves Renewal Request with the id.
   *
   * @param id renewal request id
   * @return {@link RenewalRequestDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxRenewalRequest.read' ) ")
  @Operation(
      summary = "Retrieves Renewal Request",
      description = "Retrieves  Renewal Request associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = RenewalRequestDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public RenewalRequestDto get(@Parameter(
      description = "Renewal Request id") @PathParam("id") int id)
      throws InsufficientRolesException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, NotFoundException, NoDataFoundException {

    RenewalRequestManager manager = getImpl(RenewalRequestManager.class);
    RenewalRequest renewalRequest = manager.getRenewalRequestById(id);
    RenewalRequestDto renewalRequestDto = mapDto(renewalRequest, RenewalRequestDto.class);

    return renewalRequestDto;
  }

  /**
   * Creates renewal request.
   * <p>
   * Fields like created date and last updated will be set to current UTC time.
   * </p>
   *
   * @param renewalRequestDto {@link RenewalRequestDto} data transfer object
   * @return Id for the created request.
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Bad request if violate field validations.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxRenewalRequest.create' )")
  @Operation(
      summary = "Creates renewal request.",
      description = "Creates renewal request corresponding to given renewal request."
          + " Fields like created date and last updated will be set to current UTC time",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = Integer.class))),
          @ApiResponse(
              responseCode = "400",
              description = "If violate field validations.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + " with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true)})
  public Response create(
      @Parameter(description = "Renewal Request") @Valid @RequestBody(
          description = "Renewal Request") RenewalRequestDto renewalRequestDto)
      throws DatabaseInteractionException, InsufficientPermissionsException,
      InsufficientRolesException, InsufficientFeatureAccessException, TimeZoneNotFoundException,
      SupportingResourceNotFoundException, UnsupportedSchemaVersionException {

    RenewalRequestManager manager = getImpl(RenewalRequestManager.class);
    RenewalRequest request = mapDto(renewalRequestDto, RenewalRequest.class);
    int responseId = manager.create(request);
    return Response.status(Status.CREATED).entity(responseId).build();
  }

  /**
   * Deletes renewal request.
   *
   * @param id renewal request id.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxRenewalRequest.delete' )")
  @Operation(
      summary = "Deletes renewal request.",
      description = "Remove renewal request corresponding to given renewal request.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "404",
              description = "If resource not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + " with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true)})
  public Response delete(@Parameter(description = "Request id") @PathParam("id") int id)
      throws DatabaseInteractionException, NoDataFoundException,
      InsufficientRolesException, TimeZoneNotFoundException, InsufficientPermissionsException,
      InsufficientFeatureAccessException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {
    RenewalRequestManager manager = getImpl(RenewalRequestManager.class);
    manager.delete(id);
    return Response.status(Status.NO_CONTENT).build();
  }

}
