
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.EprescribeOutcomeCode;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOutcomeCodeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Endpoints for Eprescribe Outcome Code.
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-outcome-codes")
@Facet("provider-portal")
@Tag(name = "Eprescribe Outcome Code Endpoints",
    description = "Exposes eprescribe outcome code endpoints")
public class EprescribeOutcomeCodeEndpoint extends AbstractEndpoint {

  /**
   * Get all Eprescribe Outcome Code.
   *
   * @return A list of {@link EprescribeOutcomeCodeDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   * @throws UnsupportedSchemaVersionException If the current scheme version is not supported
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves all Eprescribe Outcome Code",
      description = "Retrieves all Eprescribe Outcome Code.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = EprescribeOutcomeCodeDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeOutcomeCodeDto> getAllErxOutcomeCode()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeOutcomeCodeManager manager = getImpl(EprescribeOutcomeCodeManager.class);
    List<EprescribeOutcomeCode> outcomeCodes = manager.getAllErxOutcomeCodes();
    return mapDto(outcomeCodes, EprescribeOutcomeCodeDto.class, ArrayList::new);
  }

  /**
   * Create Eprescribe Outcome Code.
   *
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @return ID of the newly created record.
   */
  @POST
  @Path("/")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Hidden
  @Operation(
      summary = "Create a record of Eprescribe Outcome Code",
      description = "Create a record of Eprescribe Outcome Code.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = Integer.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Scope API_INTERNAL required.",
              in = ParameterIn.HEADER,
              required = true)})
  public Response createErxOutcomeCode(
      @Valid EprescribeOutcomeCodeDto erxOutcomeCodeDto)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    if (erxOutcomeCodeDto == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "The given object should not be null.");
    }

    EprescribeOutcomeCodeManager manager = getImpl(EprescribeOutcomeCodeManager.class);
    int id = manager.createErxOutcomeCode(mapDto(erxOutcomeCodeDto, EprescribeOutcomeCode.class));

    return Response.status(Response.Status.CREATED).entity(id).build();
  }

  /**
   * Delete an Eprescribe Outcome Code.
   *
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 204 no content
   */
  @DELETE
  @Path("/{id}")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Delete a record of Eprescribe Outcome Code by id",
      description = "Delete a record of Eprescribe Outcome Code by id.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "no content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Scope API_INTERNAL required.",
              in = ParameterIn.HEADER,
              required = true)})
  public void deleteErxOutcomeCode(@PathParam("id") int id)
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException {
    EprescribeOutcomeCodeManager manager = getImpl(EprescribeOutcomeCodeManager.class);
    manager.deleteErxOutcomeCode(id);
  }

}
