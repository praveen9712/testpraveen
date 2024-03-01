
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medications.MedicationLogEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medications.MedicationLogEntry;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionHistoryDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
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
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve prescription history entry for patients.
 *
 * <p>
 * Steps to retrieve this data from Accuro:
 * <ol>
 * <li>Log into Accuro</li>
 * <li>Click "EMR" on the left panel</li>
 * <li>Click "Encounter Note" tab</li>
 * <li>Create a record in "Active Medication"</li>
 * <li>Click "Medication" tab on the top</li>
 * <li>Right click over the prescription you may want to change</li>
 * <li>Do "Edit" > "SIG" or "Change Status"</li>
 * <li>Double click the prescription</li>
 * <li>Click "Other" tab</li>
 * <li>Find your edit on "Change History" tab</li>
 * </ol>
 * </p>
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto
 * @see com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionHistoryDto
 */
@Component
@Path("v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PrescriptionHistory Endpoints",
    description = "Exposes prescription history endpoints")
public class PrescriptionHistoryEndpoint extends AbstractEndpoint {

  /**
   * Gets prescription history entries for a given patient id.
   *
   * @param patientId Patient id
   * @return A List of {@link PrescriptionHistoryDto}s or {@code empty}{@link List} if not found.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = " Retrieves prescriptions history for a patient",
      description = "This Endpoint Gets all prescriptions history for the given patient. "
          + "If the checkbox 'Exclude Prescriptions from EMR Provider Permissions' "
          + "under global security settings is checked, "
          + "permissions will have no effect on prescriptions.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionHistoryDto.class,
                          subTypes = ArrayList.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{patientId}/prescriptions/histories")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescription histories for prescriptions prescribed by a "
          + "provider the user has this permission for (if Accuro is configured to enforce "
          + "provider permissions for prescriptions).")
  public List<PrescriptionHistoryDto> getByPatientId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId) throws ProtossException {

    MedicationLogEntryManager medicationLogEntryManager = getImpl(MedicationLogEntryManager.class);
    List<MedicationLogEntry> entryFromProtoss =
        medicationLogEntryManager.getAll()
            .stream()
            .filter(l -> l.getPatientId() == patientId)
            .collect(Collectors.toList());

    return mapDto(entryFromProtoss, PrescriptionHistoryDto.class, ArrayList::new);
  }

  /**
   * Gets prescription history entries for a given patient id and prescription id.
   *
   * @param patientId Patient id
   * @param prescriptionId Rx id
   * @return A List of {@link PrescriptionHistoryDto}s or {@code empty}{@link List} if not found.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = " Retrieves prescription history by prescription id",
      description = "Gets prescription history for a given prescription id. If the checkbox "
          + "'Exclude Prescriptions from EMR Provider Permissions' under global security settings "
          + "is checked, permissions will have no effect which prescriptions are retrieved.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionHistoryDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionId",
              description = "Prescription id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{patientId}/prescriptions/{prescriptionId}/histories")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescription histories for prescriptions prescribed by a "
          + "provider the user has this permission for (if Accuro is configured to enforce "
          + "provider permissions for prescriptions).")
  public List<PrescriptionHistoryDto> getByPrescriptionId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("prescriptionId") int prescriptionId)
      throws ProtossException {

    MedicationLogEntryManager medicationLogEntryManager = getImpl(MedicationLogEntryManager.class);
    List<MedicationLogEntry> entryFromProtoss =
        medicationLogEntryManager.getByPrescriptionId(prescriptionId)
            .stream()
            .filter(l -> l.getPatientId() == patientId)
            .collect(Collectors.toList());

    return mapDto(entryFromProtoss, PrescriptionHistoryDto.class, ArrayList::new);
  }

  /**
   * Gets one prescription history entry for a given patient id, prescription id and history id.
   *
   * <p>
   * All prescriptions follow Accuro permissions with the exception of external prescriptions.
   * </p>
   *
   * @param patientId Patient id
   * @param prescriptionId prescription id
   * @param historyId history entry id
   * @return The {@link PrescriptionHistoryDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Not found
   */
  @Operation(
      summary = " Retrieves a prescription history entry by id",
      description = " This endpoint collection is designed to retrieve prescription history"
          + " entry for patients.\n"
          + " <p>\n"
          + " Steps to retrieve this data from Accuro:\n"
          + " <ol>\n"
          + " <li>Log into Accuro</li>\n"
          + " <li>Click \"EMR\" on the left panel</li>\n"
          + " <li>Click \"Encounter Note\" tab</li>\n"
          + " <li>Create a record in \"Active Medication\"</li>\n"
          + " <li>Click \"Medication\" tab on the top</li>\n"
          + " <li>Right click over the prescription you may want to change</li>\n"
          + " <li>Do \"Edit\" > \"SIG\" or \"Change Status\"</li>\n"
          + " <li>Double click the prescription</li>\n"
          + " <li>Click \"Other\" tab</li>\n"
          + " <li>Find your edit on \"Change History\" tab</li>\n"
          + " </ol>\n"
          + " </p>This Endpoint Gets a prescription history entry for a given history id."
          + " All results follow "
          + "Accuro permissions with the exception of external prescriptions. If the checkbox "
          + "'Exclude Prescriptions from EMR Provider Permissions' under global security settings "
          + "is checked, permissions will have no effect which prescriptions are retrieved.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionHistoryDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionId",
              description = "Prescription id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "historyId",
              description = "History id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{patientId}/prescriptions/{prescriptionId}/histories/{historyId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescription histories for prescriptions prescribed by a "
          + "provider the user has this permission for (if Accuro is configured to enforce "
          + "provider permissions for prescriptions).")
  public PrescriptionHistoryDto getByHistoryId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("prescriptionId") int prescriptionId,
      @Parameter(hidden = true) @PathParam("historyId") int historyId)
      throws ProtossException {

    MedicationLogEntryManager medicationLogEntryManager = getImpl(MedicationLogEntryManager.class);
    MedicationLogEntry entryFromProtoss =
        medicationLogEntryManager.get(historyId);

    if (entryFromProtoss == null
        || entryFromProtoss.getPatientId() != patientId
        || entryFromProtoss.getPrescriptionId() != prescriptionId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    return mapDto(entryFromProtoss, PrescriptionHistoryDto.class);
  }

  /**
   * Gets summary of the prescription history entry for a given patient id and prescription id.
   *
   * @param patientId Patient id
   * @param prescriptionId prescription id
   * @return A summary of {@link PrescriptionHistoryDto}s.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = " Retrieves prescription history summary by prescription id",
      description = "Gets prescription history summary for a prescription. "
          + "If the checkbox 'Exclude Prescriptions from EMR Provider Permissions' "
          + "under global security settings is checked, "
          + "permissions will have no effect which prescriptions are retrieved.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = String.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionId",
              description = "Prescription id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{patientId}/prescriptions/{prescriptionId}/histories/summaries")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescription histories for prescriptions prescribed by a "
          + "provider the user has this permission for (if Accuro is configured to enforce "
          + "provider permissions for prescriptions).")
  public String getSummary(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("prescriptionId") int prescriptionId)
      throws ProtossException {
    MedicationLogEntryManager medicationLogEntryManager = getImpl(MedicationLogEntryManager.class);
    return medicationLogEntryManager.getChangesByPrescriptionId(prescriptionId);
  }
}
