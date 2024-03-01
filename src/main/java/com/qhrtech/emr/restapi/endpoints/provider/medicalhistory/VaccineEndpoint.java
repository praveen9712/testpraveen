
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.VaccineManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.Vaccine;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.VaccineDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve vaccine information.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 *
 */
@Component
@Path("/v1/provider-portal/vaccines")
@Facet("provider-portal")
@Tag(name = "Vaccine Endpoints", description = "Exposes vaccine endpoints")
public class VaccineEndpoint extends AbstractEndpoint {

  /**
   * Get all active vaccines.
   *
   * @return A List of {@link VaccineDto}s ordered by immunization Abbreviation.
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves vaccines",
      description = "Gets all active vaccines.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = VaccineDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<VaccineDto> getVaccines()
      throws ProtossException {
    VaccineManager vaccineManager = getImpl(VaccineManager.class);
    List<Vaccine> vaccines = vaccineManager.getActiveVaccines();
    return mapDto(vaccines, VaccineDto.class, ArrayList::new);
  }

  /**
   * Get a single vaccine by vaccine id.
   *
   * @HTTP 404 Not found
   *
   * @param vaccineId The vaccine id.
   *
   * @return A {@link VaccineDto} associated to the vaccine id.
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{vaccineId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the single vaccine by vaccine id",
      description = "Gets the single vaccine by the given vaccine id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success.",
              content = @Content(schema = @Schema(implementation = VaccineDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Vaccine not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public VaccineDto getVaccineById(
      @Parameter(description = "Vaccine id") @PathParam("vaccineId") int vaccineId)
      throws ProtossException {
    VaccineManager vaccineManager = getImpl(VaccineManager.class);
    Vaccine vaccine = vaccineManager.getVaccineById(vaccineId);

    if (vaccine == null) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND, "Vaccine Not Found");
    }

    return mapDto(vaccine, VaccineDto.class);
  }

}
