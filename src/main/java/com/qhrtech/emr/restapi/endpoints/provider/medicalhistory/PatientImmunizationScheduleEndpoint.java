
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.immunization.ImmunizationScheduleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.immunization.ImmunizationSchedule;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ImmunizationScheduleDto;
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
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve immunization schedule information for patients.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 404 Not found
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientImmunizationSchedule Endpoints",
    description = "Exposes patient immunization schedule endpoints")
public class PatientImmunizationScheduleEndpoint extends AbstractEndpoint {

  /**
   * <p>
   * Retrieves all immunization schedules for a given patient.
   * </p>
   *
   * @param patientId Patient ID
   * @return A List of {@link ImmunizationScheduleDto}s.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/immunization-schedules/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves immunization schedules for the patient",
      description = "Retrieves all the immunization schedules for the given patient.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ImmunizationScheduleDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<ImmunizationScheduleDto> getImmunizationSchedules(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {
    ImmunizationScheduleManager immunizationScheduleManager =
        getImpl(ImmunizationScheduleManager.class);
    Set<ImmunizationSchedule> immunizationSchedules =
        immunizationScheduleManager.getByPatientId(patientId);

    return mapDto(immunizationSchedules, ImmunizationScheduleDto.class, ArrayList::new);
  }

  /**
   * Retrieves one immunization schedule of a given patient with the specified immunization schedule
   * id.
   *
   * @param patientId Patient ID
   * @param id Immunization Schedule ID
   * @return A {@link ImmunizationScheduleDto}.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/immunization-schedules/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves immunization schedule by id",
      description = "Retrieves one immunization schedule of the given patient with the specified "
          + "immunization schedule id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "The immunization is not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ImmunizationScheduleDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ImmunizationScheduleDto getImmunizationScheduleById(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Immunization schedule id") @PathParam("id") int id)
      throws ProtossException {
    ImmunizationScheduleManager immunizationScheduleManager =
        getImpl(ImmunizationScheduleManager.class);
    Set<ImmunizationSchedule> immunizationSchedules =
        immunizationScheduleManager.getByPatientId(patientId);

    ImmunizationSchedule immunizationSchedule = immunizationSchedules.stream()
        .filter(f -> f.getId() == id)
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Response.Status.NOT_FOUND,
                "Immunization schedule Not Found"));

    return mapDto(immunizationSchedule, ImmunizationScheduleDto.class);
  }

}
