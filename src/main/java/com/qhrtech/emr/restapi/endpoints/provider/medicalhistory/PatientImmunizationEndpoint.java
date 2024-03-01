
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.immunization.PatientImmunizationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.immunization.PatientImmunization;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientImmunizationDto;
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
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientImmunizationEndpoint</code> is designed to retrieve immunization information
 * for patients.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 404 Not found
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientImmunization Endpoints", description = "Exposes patient immunization endpoints")
public class PatientImmunizationEndpoint extends AbstractEndpoint {

  /**
   * <p>
   * Get all patient Immunizations for a given patient.
   * </p>
   *
   * @param patientId Patient ID
   * @return A List of {@link PatientImmunizationDto}s.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/immunizations/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all the patient immunizations",
      description = "Retrieves all the patient immunizations for the given patient.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PatientImmunizationDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PatientImmunizationDto> getAllImmunizations(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {
    PatientImmunizationManager patientImmunizationManager =
        getImpl(PatientImmunizationManager.class);
    Set<PatientImmunization> patientImmunizations = patientImmunizationManager.getAll(patientId);

    return mapDto(patientImmunizations, PatientImmunizationDto.class, ArrayList::new);
  }

  /**
   * <p>
   * Get a {@link PatientImmunizationDto} for a given patient and ImmunizationId.
   * </p>
   *
   * @param patientId Patient ID
   * @param immunizationId Immunization ID
   * @return A List of {@link PatientImmunizationDto}s ordered by patient id.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/immunizations/{immunizationId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves immunization for the patient",
      description = "Retrieves the patient immunization for the given patient and immunization id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If immunization is not found for the patient"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientImmunizationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientImmunizationDto getImmunization(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Immunization id") @PathParam("immunizationId") int immunizationId)
      throws ProtossException {
    PatientImmunizationManager patientImmunizationManager =
        getImpl(PatientImmunizationManager.class);
    PatientImmunization patientImmunization = patientImmunizationManager.get(immunizationId);

    if (patientImmunization == null || patientImmunization.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(patientImmunization, PatientImmunizationDto.class);
  }
}
