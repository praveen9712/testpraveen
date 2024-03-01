
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.immunization.ImmunizationScheduleManager;
import com.qhrtech.emr.accuro.api.immunization.ScheduleVaccineManager;
import com.qhrtech.emr.accuro.api.medicalhistory.VaccineManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.immunization.ScheduleVaccine;
import com.qhrtech.emr.accuro.model.medicalhistory.Vaccine;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ScheduleVaccineDto;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve scheduled vaccine information for a patient.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientScheduledVaccines Endpoints",
    description = "Retrieves patient scheduled vaccine endpoints")
public class PatientScheduledVaccinesEndpoint extends AbstractEndpoint {

  /**
   * Retrieves scheduled vaccines for a given schedule id.
   *
   * @param scheduleId Schedule id
   * @param patientId Patient id
   *
   * @return A Set of {@link ScheduleVaccineDto scheduled vaccines}
   *
   * @HTTP 404 Not found
   *
   * @throws DataAccessException If there has been a database error.
   */

  @Operation(
      summary = "Retrieves the scheduled vaccines",
      description = "Gets the scheduled vaccines for the given schedule id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Schedule cannot be found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ScheduleVaccineDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{patientId}/immunization-schedules/{scheduleId}/scheduled-vaccines")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  public Set<ScheduleVaccineDto> getByScheduleId(
      @Parameter(description = "Schedule id") @PathParam("scheduleId") int scheduleId,
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {

    // Ensure the request schedule id actually exists, and is associated to the requested patient.
    ImmunizationScheduleManager scheduleManager = getImpl(ImmunizationScheduleManager.class);
    scheduleManager.getByPatientId(patientId).stream()
        .filter(i -> i.getId() == scheduleId).findFirst().orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "No Resource Found"));

    // now that we have verified the patient has this schedule associated to their chart,
    // we can pull up the vaccines for it.
    ScheduleVaccineManager scheduleVaccineManager = getImpl(ScheduleVaccineManager.class);
    VaccineManager vaccineManager = getImpl(VaccineManager.class);
    Set<ScheduleVaccine> scheduleVaccines = scheduleVaccineManager.getByScheduleId(scheduleId);


    // pull up all vaccines and map them by id.
    List<Vaccine> vaccines = vaccineManager.getAllVaccines();
    Map<Integer, Vaccine> vaccinesById =
        vaccines.stream().collect(Collectors.toMap(Vaccine::getVaccineId, Function.identity()));

    Set<ScheduleVaccineDto> results = new HashSet<>();
    for (ScheduleVaccine scheduleVaccine : scheduleVaccines) {
      Vaccine vaccine = vaccinesById.get(scheduleVaccine.getVaccineId());
      ScheduleVaccineDto dto = mapDto(scheduleVaccine, ScheduleVaccineDto.class);
      if (vaccine != null) {
        dto.setVaccine(mapDto(vaccine, VaccineDto.class));
      }
      results.add(dto);
    }
    return results;
  }

  /**
   * Retrieves scheduled vaccines for a given patient id.
   *
   * @param patientId Patient id
   *
   * @return A Set of {@link ScheduleVaccineDto scheduled vaccines}
   *
   * @HTTP 404 Not found
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/scheduled-vaccines")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the scheduled vaccines for the patient",
      description = "Gets the scheduled vaccines for the given patient id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Patient not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ScheduleVaccineDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ScheduleVaccineDto> getByPatientId(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {
    PatientManager patientManager = getImpl(PatientManager.class);
    ScheduleVaccineManager scheduleVaccineManager = getImpl(ScheduleVaccineManager.class);
    VaccineManager vaccineManager = getImpl(VaccineManager.class);

    if (patientManager.getPatientById(patientId) == null) {
      // no patient record, so we will 404
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    Set<ScheduleVaccine> scheduleVaccines = scheduleVaccineManager.getByPatientId(patientId);

    // pull up all vaccines and map them by id.
    List<Vaccine> vaccines = vaccineManager.getAllVaccines();
    Map<Integer, Vaccine> vaccinesById =
        vaccines.stream().collect(Collectors.toMap(Vaccine::getVaccineId, Function.identity()));

    Set<ScheduleVaccineDto> results = new HashSet<>();
    for (ScheduleVaccine scheduleVaccine : scheduleVaccines) {
      Vaccine vaccine = vaccinesById.get(scheduleVaccine.getVaccineId());
      ScheduleVaccineDto dto = mapDto(scheduleVaccine, ScheduleVaccineDto.class);
      if (vaccine != null) {
        dto.setVaccine(mapDto(vaccine, VaccineDto.class));
      }
      results.add(dto);
    }
    return results;
  }
}
