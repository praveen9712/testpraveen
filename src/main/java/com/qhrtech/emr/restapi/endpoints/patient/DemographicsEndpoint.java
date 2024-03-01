
package com.qhrtech.emr.restapi.endpoints.patient;

import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.patient.Demographics;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AddressDto;
import com.qhrtech.emr.restapi.models.dto.DemographicsDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This <code>DemographicsEndpoint</code> collection is designed to expose the Demographics DTO and
 * related patient-level endpoints. Requires patient level authorization.
 *
 * @author kevin.kendall
 * @RequestHeader Authorization Patient authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 * @see com.qhrtech.emr.accuro.api.patient.PatientManager
 */
@Component
@Path("/v1/patient-portal/demographics")
@Facet("patient-portal")
@Tag(name = "Demographics Endpoints - Patients",
    description = "Exposes patient portal demographics endpoints")
public class DemographicsEndpoint extends AbstractEndpoint {

  /**
   * Get patient demographics for the current authenticated patient.
   *
   * @return Patient demographics
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves patient demographics",
      description = "Retrieves patient demographics for the current authenticated patient.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = DemographicsDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Patient not found")})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public DemographicsDto getPatient() throws ProtossException {
    int patientId = getPatientId();
    PatientManager patientManager = getImpl(PatientManager.class);
    Demographics demographics = patientManager.getPatientDemographics(patientId);
    if (demographics == null) {
      throw Error.returnNotFoundResult("Patient not found");
    }
    return mapDto(demographics, DemographicsDto.class);
  }

  /**
   * Accepts a new Demographics object. Replaces the current Demographics with this new Demographics
   * in the current patient.
   *
   * @param demographics Patient demographics
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 204 Update successful, no content returned
   * @HTTP 400 Unable to process json
   */
  @PUT
  @Operation(
      summary = "Updates patient demographics",
      description = "Accepts a new Demographics object. Replaces the current Demographics with "
          + "this new Demographics in the current patient.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "Unable to process json"),
          @ApiResponse(
              responseCode = "404",
              description = "Patient not found")})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public void updatePatient(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Patient Demographics") @RequestBody @Valid DemographicsDto demographics)
      throws ProtossException {

    int patientId = getPatientId();
    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patient = patientManager.getPatientById(patientId);

    if (patient == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Patient not found");
    }

    List<AddressDto> addresses = demographics.getAddresses();
    if (addresses != null && addresses.size() > 2) {
      addresses = addresses.subList(2, addresses.size());
      if (addresses.stream().anyMatch(a -> Objects.isNull(a.getLocationId()))) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Location id on non primary or secondary address must be supplied.");
      }
    }

    Demographics updatedDemographics = mapDto(demographics, Demographics.class);
    patient.setDemographics(updatedDemographics);
    // keeping the termination Reason as null as we only update patient demographics through
    // this endpoint.
    patientManager.updatePatient(patient, getUser(), null);
  }
}
