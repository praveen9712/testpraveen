
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisHistoryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosisHistory;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisHistoryDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code PatientDiagnosisHistoryEndpoint} is designed to retrieve patient diagnosis history
 * records.
 *
 * <p>
 * Also, this endpoint represents item history of "History Of Problems" in Accuro.
 * </p>
 *
 * <p>
 * Steps to retrieve this data from Accuro:
 * <ol>
 * <li>Log into Accuro</li>
 * <li>Click "EMR" on the left panel</li>
 * <li>Click "Encounter Note" tab</li>
 * <li>Go to "History Of Problems"</li>
 * <li>Click Add or Edit button</li>
 * <li>Select any diagnoses. If there is none then create one and edit it to generate a history
 * record.</li>
 * <li>Click History button</li>
 * </ol>
 * </p>
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisDto
 * @see com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientDiagnosisHistoryDto
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientDiagnosisHistory Endpoints",
    description = "Exposes patient diagnosis history endpoints")
public class PatientDiagnosisHistoryEndpoint extends AbstractEndpoint {

  /**
   * Gets one patient diagnosis history entry for the given patient diagnosis history id.
   *
   * @param patientId The patient id
   * @param diagnosisId The patient diagnosis id
   * @param historyId The patient diagnosis history id
   * @return The patient diagnosis history entry.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If there is no data matched with the supplied patient id, diagnosis id or history id
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Path("/{patientId}/diagnoses/{diagnosisId}/histories/{historyId}")
  @Operation(
      summary = "Retrieves the patient diagnosis history by each id",
      description = "This PatientDiagnosisHistoryEndpoint  is designed to retrieve patient"
          + " diagnosis history\n"
          + "  records.\n"
          + " \n"
          + "  <p>\n"
          + "  Also, this endpoint represents item history of \"History Of Problems\" in Accuro.\n"
          + "  </p>\n"
          + " \n"
          + "  <p>\n"
          + "  Steps to retrieve this data from Accuro:\n"
          + "  <ol>\n"
          + "  <li>Log into Accuro</li>\n"
          + "  <li>Click \"EMR\" on the left panel</li>\n"
          + "  <li>Click \"Encounter Note\" tab</li>\n"
          + "  <li>Go to \"History Of Problems\"</li>\n"
          + "  <li>Click Add or Edit button</li>\n"
          + "  <li>Select any diagnoses. If there is none then create one and edit it to"
          + " generate a history\n"
          + "  record.</li>\n"
          + "  <li>Click History button</li>\n"
          + "  </ol>\n"
          + "  </p>",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientDiagnosisHistoryDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientDiagnosisHistoryDto getById(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Diagnosis id") @PathParam("diagnosisId") int diagnosisId,
      @Parameter(description = "History id") @PathParam("historyId") int historyId)
      throws ProtossException {
    // retrieve the patient diagnosis through api
    PatientDiagnosisHistoryManager patientDiagnosisHistoryManager =
        getImpl(PatientDiagnosisHistoryManager.class);
    PatientDiagnosisHistory historyEntry = patientDiagnosisHistoryManager.getById(historyId);

    // validation check
    if (historyEntry == null
        || historyEntry.getPatientId() != patientId
        || historyEntry.getPatientDiagnosisId() != diagnosisId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    // data transfer
    PatientDiagnosisHistoryDto historyEntryDto =
        mapDto(historyEntry, PatientDiagnosisHistoryDto.class);

    // set the diagnosis status on the dto
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);
    DiagnosisStatus diagnosisStatus = diagnosisStatusManager.getAll().stream()
        .filter(f -> f.getStatusId() == historyEntry.getDiagnosisStatusId())
        .findFirst().orElse(null);

    if (diagnosisStatus != null) {
      DiagnosisStatusDto diagnosisStatusDto = mapDto(diagnosisStatus, DiagnosisStatusDto.class);
      historyEntryDto.setDiagnosisStatus(diagnosisStatusDto);
    }

    return historyEntryDto;
  }

  /**
   * Gets the patient diagnosis history records for the given patient diagnosis id.
   *
   * <p>
   * The results are order by id in descending order.
   * </p>
   *
   * @param patientId The patient id
   * @param diagnosisId The diagnosis id
   * @return The List of the {@link PatientDiagnosisHistoryDto} or {@code empty} if not found.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS", accessLevel = "READ_ONLY")})
  @Path("/{patientId}/diagnoses/{diagnosisId}/histories")
  @Operation(
      summary = "Retrieves the patient diagnosis history by each id",
      description = "Retrieves the patient diagnosis history records "
          + "for the given patient id and the patient diagnosis id.\n"
          + "The results are order by the id in the descending order.\n",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PatientDiagnosisHistoryDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PatientDiagnosisHistoryDto> getForPatientDiagnosis(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Diagnosis id") @PathParam("diagnosisId") int diagnosisId)
      throws ProtossException {
    // retrieve the patient diagnosis through api
    PatientDiagnosisHistoryManager patientDiagnosisHistoryManager =
        getImpl(PatientDiagnosisHistoryManager.class);
    List<PatientDiagnosisHistory> historyEntries =
        patientDiagnosisHistoryManager.getForPatientDiagnosis(diagnosisId);

    // mapping diagnosis status
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);
    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();
    Map<Integer, DiagnosisStatus> mappedStatus = new HashMap<>();
    diagnosisStatuses.forEach(status -> mappedStatus.put(status.getStatusId(), status));

    // data transfer and set diagnosis status on each diagnosis
    List<PatientDiagnosisHistoryDto> historyEntryDtos = new ArrayList<>();
    for (PatientDiagnosisHistory historyEntry : historyEntries) {
      // check if the retrieving patient id is matched with the supplied one.
      if (historyEntry.getPatientId() == patientId) {
        PatientDiagnosisHistoryDto historyEntryDto =
            mapDto(historyEntry, PatientDiagnosisHistoryDto.class);
        int statusId = historyEntry.getDiagnosisStatusId();
        if (mappedStatus.containsKey(statusId)) {
          DiagnosisStatusDto diagnosisStatus =
              mapDto(mappedStatus.get(statusId), DiagnosisStatusDto.class);
          historyEntryDto.setDiagnosisStatus(diagnosisStatus);
        }
        historyEntryDtos.add(historyEntryDto);
      }
    }
    return historyEntryDtos;
  }

}
