
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcome;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobOutcomeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
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
 * Endpoints for Eprescribe job outcome.
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-job-outcomes")
@Facet("provider-portal")
@Tag(name = "Eprescribe Job Outcome Endpoints",
    description = "Exposes eprescribe job outcome endpoints")
public class EprescribeJobOutcomeEndpoint extends AbstractEndpoint {

  /**
   * Creates a new e-prescribe job outcome.
   *
   * @param outcomeDto JSON representation of the e-prescribe job outcome
   * @return the e-prescribe job outcome id
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the current scheme version is not supported
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.create' ) ")
  @Operation(
      summary = "Creates new Eprescribe Job Outcome",
      description = "Create new Eprescribe Job Outcome.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "invalid request"),
          @ApiResponse(
              responseCode = "201",
              description = "created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns a new e-prescribe job outcome id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @RequestBody(
          description = "EprescribeJobOutcome") @Valid EprescribeJobOutcomeDto outcomeDto)
      throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException {
    if (outcomeDto == null) {
      throw new IllegalArgumentException("Eprescribe job outcome information is missing.");
    }
    EprescribeOutcome outcome = mapDto(outcomeDto, EprescribeOutcome.class);
    EprescribeOutcomeManager manager = getImpl(EprescribeOutcomeManager.class);
    int id = manager.createErxOutcome(outcome);
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Get an Eprescribe Job Outcome with the id.
   *
   * @param id Eprescribe Outcome ID
   * @return {@link EprescribeJobOutcomeDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the current scheme version is not supported
   */
  @GET
  @Path("/{outcomeId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves Eprescribe Job Outcome",
      description = "Retrieves an Eprescribe Job Outcome associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = EprescribeJobOutcomeDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public EprescribeJobOutcomeDto getById(
      @Parameter(description = "Eprescribe Outcome Id") @PathParam("outcomeId") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeOutcomeManager manager = getImpl(EprescribeOutcomeManager.class);
    EprescribeOutcome outcome = manager.getErxOutcome(id);
    return mapDto(outcome, EprescribeJobOutcomeDto.class);
  }
}
