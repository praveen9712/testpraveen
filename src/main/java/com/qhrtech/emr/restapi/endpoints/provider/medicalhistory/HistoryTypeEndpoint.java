
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.HistoryTypeManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryTypeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code HistoryTypeEndpoint} collection is designed to retrieve history types and items for
 * patient medical history.
 *
 * @RequestHeader Authorization Provider Level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/history-types")
@Facet("provider-portal")
@PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
    + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) ")
@Tag(name = "HistoryType Endpoints",
    description = "Exposes history type endpoints")
public class HistoryTypeEndpoint extends AbstractEndpoint {

  /**
   * <P>
   * Retrieves all medical history types.
   * </P>
   * <p>
   * Note: It is possible to have multiple history type's with the same name if they were created by
   * two instances of Accuro at the same time. Some actions that use history types are performed by
   * name and may have inconsistent results.
   * </p>
   *
   * @return A list of {@link HistoryTypeDto}s ordered by name of the type.
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves all history types",
      description = "Retrieves all medical history types.\n\n"
          + "Note: It is possible to have multiple history types with the same name if they were "
          + "created by two instances of Accuro at the same time. Some actions that use history "
          + "types are performed by name and may have inconsistent results.\n",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = HistoryTypeDto.class))))})
  public List<HistoryTypeDto> getAll() throws ProtossException {
    HistoryTypeManager historyTypeManager = getImpl(HistoryTypeManager.class);
    List<HistoryType> historyTypes = historyTypeManager.getAllHistoryTypes();
    return mapDto(historyTypes, HistoryTypeDto.class, ArrayList::new);
  }

  /**
   * Retrieves a {@link HistoryTypeDto} for a given history type id.
   *
   * @param id History type ID
   * @return The {@link HistoryTypeDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the specified history type is not found.
   */
  @GET
  @Path("/{typeId}")
  @Operation(
      summary = "Retrieves history type by id",
      description = "Retrieves the history type by the given history type id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "The specified history type doesn't exist"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = HistoryTypeDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "typeId",
              description = "The history type id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public HistoryTypeDto getById(
      @Parameter(hidden = true) @PathParam("typeId") int id)
      throws ProtossException {
    HistoryTypeManager historyTypeManager = getImpl(HistoryTypeManager.class);
    HistoryType historyType = historyTypeManager.getHistoryTypeByTypeId(id);
    if (historyType == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(historyType, HistoryTypeDto.class);
  }

}
