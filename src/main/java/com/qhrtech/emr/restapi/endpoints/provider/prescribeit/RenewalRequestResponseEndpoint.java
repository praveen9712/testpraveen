
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestResponseManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestResponse;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.RenewalRequestResponseDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
 * This <code>RenewalRequestEndpoint</code> is designed to expose {@link RenewalRequestResponseDto}
 * endpoints. Requires Provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/renewal-request-responses")
@Facet("provider-portal")
@Tag(name = "Renewal requests response Endpoints",
    description = "Exposes renewal requests response endpoints")
public class RenewalRequestResponseEndpoint extends AbstractEndpoint {


  /**
   * Retrieves renewal request response related to given id.
   *
   * @param responseId Renewal response ID
   * @return {@link RenewalRequestResponseDto} data object belonging to given renewal request
   *         response.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{responseId}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxRenewalRequest.read' )")
  @Operation(
      summary = "Retrieves Renewal request response corresponding renewal request response id.",
      description = "Retrieves Renewal request response corresponding renewal request response id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Invalid renewal request response id."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = RenewalRequestResponseDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "responseId",
              description = "The renewal request response id.",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public RenewalRequestResponseDto getRenewalResponseById(
      @Parameter(hidden = true) @PathParam("responseId") int responseId)
      throws ProtossException {

    RenewalRequestResponseManager manager = getImpl(RenewalRequestResponseManager.class);

    RenewalRequestResponse renewalResponse = manager.getById(responseId);

    if (renewalResponse == null) {
      throw new NoDataFoundException(
          "Renewal request response not found with id: " + responseId);
    }

    return mapDto(renewalResponse, RenewalRequestResponseDto.class);
  }

  /**
   * Creates renewal request response.
   *
   * <p>
   * Expects a valid renewal request id to create a response. Created By Id would be set as client
   * user Id. If it is null, then client has to pass the created by Id in the request object.
   * </p>
   *
   * @param renewalResponseDto {@link RenewalRequestResponseDto} data transfer object
   * @return {@link RenewalRequestResponseDto} Id
   * @throws ProtossException If there has been a database error.
   * @HTTP 201 Request successful
   * @HTTP 404 If renewal request Id is not valid(or found).
   */
  @POST
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Operation(
      summary = "Creates renewal request response.",
      description = "Creates renewal request response corresponding to given renewal request."
          + "Created By Id would be set as client user Id. If it is null, then client has to "
          + "pass the created by Id in the request object.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Invalid renewal request id."),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = RenewalRequestResponseDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public Response create(
      @Parameter(description = "Renewal Response") @Valid @RequestBody(
          description = "Renewal response") RenewalRequestResponseDto renewalResponseDto)
      throws ProtossException {

    RenewalRequestResponseManager manager = getImpl(RenewalRequestResponseManager.class);
    // client userId would override the id passed in the object
    Integer createById = getUser().getUserId();
    if (createById == null) {

      createById = renewalResponseDto.getCreatedById();

      if (createById == null) {
        throw Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Created By id is required for this operation.");
      }

    }
    renewalResponseDto.setCreatedById(createById);
    RenewalRequestResponse response = mapDto(renewalResponseDto, RenewalRequestResponse.class);

    int responseId = manager.create(response);
    return Response.status(Status.CREATED).entity(responseId).build();
  }

}
