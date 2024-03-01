
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.model.eprescribe.ExternalPatient;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationContactDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ExternalPatientDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * This endpoint collection is designed to create/retrieve External Patient.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/external-patients")
@Facet("provider-portal")
@Tag(name = "External patient Endpoints",
    description = "Exposes external patient endpoints")
public class ExternalPatientEndpoint extends AbstractEndpoint {


  /**
   * Creates {@link ExternalPatientDto external patient}
   * <p>
   * External patient identifier and external system identifier fields combined must be unique.
   * </p>
   *
   * @param externalPatientDto The external patient dto
   * @return The unique id of {@link ExternalPatientDto}
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 201 Created
   * @HTTP 400 If external patient dto not provided
   * @HTTP If external patient identifier and external system identifier combined are not unique
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.create' ) ")
  @Operation(
      summary = "Create External Patient endpoint",
      description = "Creates a new external patient. External patient identifier and "
          + "external system identifier fields combined must be unique.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1",
                      description = "Returns the unique id of the external patient"))),
          @ApiResponse(
              responseCode = "400",
              description = "If external patient dto is not provided"),
          @ApiResponse(
              responseCode = "400",
              description = "If external patient identifier and external system identifier"
                  + " combined are not unique")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)
      })
  public Response createExternalPatient(@Valid ExternalPatientDto externalPatientDto)
      throws ProtossException {
    if (externalPatientDto == null) {
      throw new IllegalArgumentException("ExternalPatientDto is required");
    }
    ExternalPatientManager manager = getImpl(ExternalPatientManager.class);
    int id = manager.createExternalPatient(mapDto(externalPatientDto, ExternalPatient.class));
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Retrieves {@link ExternalPatientDto external patient} associated with the given external
   * patient id.
   *
   * @param id Id of the external patient
   * @return {@link ExternalPatientDto external patient}
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 404 External Patient not found
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves ExternalPatient",
      description = "Retrieves ExternalPatient associated with "
          + "the given id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ExternalPatientDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "External Patient not found")})
  @Parameter(
      name = "authorization",
      description = "Provider authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ExternalPatientDto getById(
      @Parameter(description = "External patient id",
          example = "1") @PathParam("id") int id)
      throws ProtossException {
    ExternalPatientManager manager = getImpl(ExternalPatientManager.class);
    ExternalPatient externalPatient = manager.getExternalPatientById(id);
    return mapDto(externalPatient, ExternalPatientDto.class);
  }


  /**
   * Retrieves List of {@link ExternalPatientDto external patient} associated with the given
   * identifiers.
   * <p>
   * Both query params are mandatory.
   * </p>
   *
   * @param externalSystemIdentifier External system identifier
   * @param externalPatientIdentifier External patient identifier
   * @return The List of {@link ExternalPatientDto external patient}
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 200 Success
   * @HTTP 404 External Patients not found
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Communication.read' ) ")
  @Operation(
      summary = "Retrieves ExternalPatient",
      description = "Retrieves ExternalPatient associated with "
          + "the given identifiers. Both query params are mandatory. "
          + "The results are filtered with exact match to the params.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ExternalPatientDto.class)))),
          @ApiResponse(
              responseCode = "404",
              description = "Externals Patient not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "extSystemId",
              description = "External system identifier",
              in = ParameterIn.QUERY,
              required = true,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "extPatientId",
              description = "External patient identifier",
              in = ParameterIn.QUERY,
              required = true,
              schema = @Schema(type = "String"))
      })
  public List<ExternalPatientDto> getExternalPatients(
      @Parameter(hidden = true) @QueryParam("extSystemId") @NotNull(
          message = "extSystemId param is required") String externalSystemIdentifier,
      @Parameter(hidden = true) @QueryParam("extPatientId") @NotNull(
          message = "extPatientId param is required") String externalPatientIdentifier)
      throws ProtossException {
    ExternalPatientManager manager = getImpl(ExternalPatientManager.class);
    ExternalPatient externalPatient =
        manager.getByIdentifiers(externalSystemIdentifier, externalPatientIdentifier);
    if (externalPatient == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(mapDto(externalPatient, ExternalPatientDto.class));
  }

}
