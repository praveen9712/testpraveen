
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
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
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve Diagnosis Status information.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/diagnosis-statuses")
@Facet("provider-portal")
@Tag(name = "DiagnosisStatus Endpoints",
    description = "Exposes diagnosis status endpoints")
public class DiagnosisStatusEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all diagnosis status.
   *
   * @return A list of diagnosis status.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all diagnosis statuses",
      description = "Gets a set of diagnosis statuses.",
      responses = {

          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = DiagnosisStatusDto.class))))})
  @Parameters(
      value = {
          @Parameter(name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)
      })
  public Set<DiagnosisStatusDto> getAllDiagnosisStatuses()
      throws ProtossException {
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);

    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();

    return mapDto(diagnosisStatuses, DiagnosisStatusDto.class, HashSet::new);
  }

  /**
   * Retrieves a diagnosis status via the specified status id.
   *
   * @param statusId Status id
   * @return The diagnosis status
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Not found
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{statusId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the diagnosis status",
      description = "Gets the diagnosis status via the specified status id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Not Found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = DiagnosisStatusDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public DiagnosisStatusDto getDiagnosisStatus(
      @Parameter(description = "DiagnosisStatus id") @PathParam("statusId") int statusId)
      throws ProtossException {
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);

    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();

    DiagnosisStatus diagnosisStatus = diagnosisStatuses.stream()
        .filter(f -> f.getStatusId() == statusId)
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Response.Status.NOT_FOUND,
                "Diagnosis Status Not Found"));

    return mapDto(diagnosisStatus, DiagnosisStatusDto.class);
  }

}
