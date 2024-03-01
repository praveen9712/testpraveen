
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.InsurerDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>InsurerEndpoint</code> collection is designed to expose the Insurer DTO and related
 * public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/insurers")
@Facet("provider-portal")
@Tag(name = "Insurer Endpoints",
    description = "Exposes insurer endpoints")
public class InsurerEndpoint extends AbstractEndpoint {

  /**
   * Retrieves Insurers.
   *
   * @return Set of Insurer DTO
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all insurers",
      description = "Retrieves all insurers.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = InsurerDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public Set<InsurerDto> getInsurers() throws ProtossException {
    InsurerManager insurerManager = getImpl(InsurerManager.class);
    return mapDto(insurerManager.getAllInsurers(), InsurerDto.class, HashSet::new);
  }

  /**
   * Retrieves Insurers by IDs.
   *
   * @param insurerIds Insurer IDs
   * @return Set of Insurer DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves insurers by IDs",
      description = "Retrieves insurers by the given list of ids.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = InsurerDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "No insurer ids have been supplied")})
  @RequestBody(
      description = "Collection of insurer id",
      content = @Content(
          array = @ArraySchema(schema = @Schema(type = "integer", example = "1"))))
  public Set<InsurerDto> getInsurers(
      Collection<Integer> insurerIds)
      throws ProtossException {

    if (insurerIds == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "No insurer ids have been supplied.");
    }
    InsurerManager insurerManager = getImpl(InsurerManager.class);
    return mapDto(insurerManager.getInsurersByIds(insurerIds), InsurerDto.class, HashSet::new);
  }

  /**
   * Retrieves Insurer by ID.
   *
   * @param insurerId Insurer ID
   * @return Insurer DTO
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{insurerId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves insurer by id",
      description = "Retrieves insurer by the given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = InsurerDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "insurerId",
              description = "The insurer id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public InsurerDto getInsurerById(
      @Parameter(hidden = true) @PathParam("insurerId") int insurerId)
      throws ProtossException {

    InsurerManager insurerManager = getImpl(InsurerManager.class);
    return mapDto(insurerManager.getInsurerById(insurerId), InsurerDto.class);
  }
}
