
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosis;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code PatientDiagnosisEndpoint} is designed to retrieve diagnosis information for patients.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientDiagnosis Endpoints", description = "Exposes patient diagnosis endpoints")
public class PatientDiagnosisEndpoint extends AbstractEndpoint {

  /**
   * Gets a single patient diagnosis by diagnosis id associated to the patient id.
   *
   * @param patientId Patient id
   * @param diagnosisId Diagnosis id
   * @return A {@link PatientDiagnosisDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the diagnosisId does not exist or is not associated to the patient id.
   */
  @GET
  @Path("/{patientId}/diagnoses/{diagnosisId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves the patient diagnosis by the given ids",
      description = "Retrieves the single patient diagnosis by the given diagnosis id "
          + "associated to the given patient id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "The diagnosis doesn't exist or is not associated to the patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientDiagnosisDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientDiagnosisDto getByDiagnosisId(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Diagnosis id") @PathParam("diagnosisId") int diagnosisId)
      throws ProtossException {

    PatientDiagnosisManager patientDiagnosisManager = getImpl(PatientDiagnosisManager.class);
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);

    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();

    PatientDiagnosis patientDiagnosis = patientDiagnosisManager.getById(diagnosisId);

    if (patientDiagnosis == null || patientDiagnosis.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    DiagnosisStatus diagnosisStatus = diagnosisStatuses.stream()
        .filter(f -> f.getStatusId() == patientDiagnosis.getDiagnosisStatusId()).findFirst()
        .orElse(null);
    PatientDiagnosisDto patientDiagnosisDto = mapDto(patientDiagnosis, PatientDiagnosisDto.class);
    if (diagnosisStatus != null) {
      patientDiagnosisDto.setDiagnosisStatus(mapDto(diagnosisStatus, DiagnosisStatusDto.class));
    }
    return patientDiagnosisDto;
  }

  /**
   * Gets all active patient diagnoses for a patient.
   *
   * @param patientId Patient id
   * @return A {@link Set} of {@link PatientDiagnosisDto}
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/diagnoses/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Operation(
      summary = "Retrieves patient diagnoses for the patient",
      description = "Retrieves all active patient diagnoses for the given patient id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PatientDiagnosisDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientDiagnosisDto> getForPatient(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {

    PatientDiagnosisManager patientDiagnosisManager = getImpl(PatientDiagnosisManager.class);
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);

    // mapping diagnosis status
    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();
    Map<Integer, DiagnosisStatus> mappedStatus = new HashMap<>();
    diagnosisStatuses.forEach(status -> mappedStatus.put(status.getStatusId(), status));

    Set<PatientDiagnosis> diagnoses = patientDiagnosisManager.getForPatient(patientId);
    Set<PatientDiagnosisDto> diagnosisDtos = new HashSet<>();
    for (PatientDiagnosis patientDiagnosis : diagnoses) {
      PatientDiagnosisDto patientDiagnosisDto = mapDto(patientDiagnosis, PatientDiagnosisDto.class);

      DiagnosisStatus diagnosisStatus = mappedStatus.get(patientDiagnosis.getDiagnosisStatusId());
      if (diagnosisStatus != null) {
        patientDiagnosisDto.setDiagnosisStatus(mapDto(diagnosisStatus, DiagnosisStatusDto.class));
      }
      diagnosisDtos.add(patientDiagnosisDto);
    }
    return diagnosisDtos;
  }
}
