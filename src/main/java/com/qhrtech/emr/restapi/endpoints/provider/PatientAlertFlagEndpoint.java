
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.patient.PatientFlagManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.patient.PatientFlag;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.PatientAlertFlagDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientAlertFlagEndpoint</code> collection is designed to expose the Patient Alert
 * Flag Endpoints. Requires provider level authentication.
 *
 * @RequestHeader Authorization Provider Level authorization grant
 * @HTTP 200 Request successful
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientAlertFlag Endpoints", description = "Exposes patient flag endpoints")
public class PatientAlertFlagEndpoint extends AbstractEndpoint {


  /**
   * Retrieves an available Patient Flag associated with a provided flag id.
   *
   * @return A Patient Flag
   * @throws DataAccessException f there has been a database error.
   * @HTTP 404 If not found a flag
   */
  @GET
  @Path("/flags/{flagId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient flag",
      description = "Retrieves an available patient flag associated with the provided flag id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Flag not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = PatientAlertFlagDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientAlertFlagDto getFlagById(
      @Parameter(description = "The flag id", required = true) @PathParam("flagId") int flagId)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientFlag> patientFlags = patientFlagManager.getFlags();
    PatientFlag flag = patientFlags.stream()
        .filter(f -> f.getId() == flagId)
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found"));

    return mapDto(flag, PatientAlertFlagDto.class);
  }

  /**
   * Retrieves all available Patient Flags.
   *
   * @return A set of Patient Flags
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient flags",
      description = "Retrieves all available patient flags.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = PatientAlertFlagDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientAlertFlagDto> getFlags() throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientFlag> patientFlags = patientFlagManager.getFlags();
    return mapDto(patientFlags, PatientAlertFlagDto.class, HashSet::new);
  }

  /**
   * Retrieves a Patient Flag that have been applied to a Patient.
   *
   * @param patientId Patient id
   * @param flagId Patient Flag id
   * @return A Patient Flag
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If not found a flag for a patient
   */
  @GET
  @Path("/{patientId}/flags/{flagId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient flag by flag Id",
      description = "Retrieves a patient flag that has been applied to the patient.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = PatientAlertFlagDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientAlertFlagDto getFlagForPatient(
      @Parameter(description = "Patient id", required = true) @PathParam("patientId") int patientId,
      @Parameter(description = "Flag id", required = true) @PathParam("flagId") int flagId)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientFlag> flags = patientFlagManager.getFlags();
    Set<Integer> patientFlagIds = patientFlagManager.getFlagsForPatient(patientId);
    PatientFlag flag = flags.stream()
        .filter(f -> patientFlagIds.contains(f.getId()))
        .filter(f -> f.getId() == flagId)
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found."));
    return mapDto(flag, PatientAlertFlagDto.class);

  }

  /**
   * Retrieves all Patient Flags that have been applied to a Patient.
   *
   * @param patientId Patient id
   * @return A Set of Patient Flags.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient flags",
      description = "Retrieves all patient flags that have been applied to the patient.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = PatientAlertFlagDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientAlertFlagDto> getFlagsForPatient(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientFlag> flags = patientFlagManager.getFlags();
    Set<Integer> patientFlagIds = patientFlagManager.getFlagsForPatient(patientId);
    Set<PatientFlag> patientFlags =
        flags.stream()
            .filter(f -> patientFlagIds.contains(f.getId()))
            .collect(Collectors.toSet());
    return mapDto(patientFlags, PatientAlertFlagDto.class, HashSet::new);
  }

  /**
   * Removes the Patient Flag from the Patient.
   *
   * @param patientId Patient id
   * @param flagId Patient Flag id
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If a flag doesn't exist or has not been applied for a patient
   */
  @DELETE
  @Path("/{patientId}/flags/{flagId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Removes patient flag",
      description = "Removes a patient flag from the patient.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public void removeFlag(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Flag id") @PathParam("flagId") int flagId)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);

    Set<Integer> patientFlagIds = patientFlagManager.getFlagsForPatient(patientId);
    if (!patientFlagIds.contains(flagId)) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }
    patientFlagManager.removeFlag(patientId, flagId);
  }

  /**
   * Applies a PatientFlag to a Patient
   *
   * @param patientId Patient id
   * @param flagId Patient Flag id
   * @HTTP 404 If the requested flag does not exist
   */
  @PUT
  @Path("/{patientId}/flags/{flagId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Applies patient flag",
      description = "Applies the patient flag to the patient",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public void applyFlag(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Flag id") @PathParam("flagId") int flagId)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);

    Set<PatientFlag> flags = patientFlagManager.getFlags();
    PatientFlag flag = flags.stream()
        .filter(f -> f.getId() == flagId)
        .findFirst()
        .orElseThrow(
            () -> Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found."));

    Set<Integer> patientFlagIds = patientFlagManager.getFlagsForPatient(patientId);
    if (patientFlagIds.contains(flag.getId())) {
      // this call should be idempotent.
      // we will only apply the flag if it is not already applied.d
      return;
    }
    patientFlagManager.applyFlag(patientId, flagId);
  }

  /**
   * Applies multiple Patient Flags to a single Patient.
   *
   * @param patientId Patient id
   * @param flagIds Patient Flag ids
   * @HTTP 400 If a flag doesn't exist for an alert or has already been applied for a patient
   */
  @PUT
  @Path("/{patientId}/flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Applies multiple patient flags",
      description = "Applies multiple patient flags to the single patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Flag ids must be provided"),
          @ApiResponse(
              responseCode = "400",
              description = "Flag ids could not be found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public void applyFlags(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @RequestBody(description = "Patient flag ids") Set<Integer> flagIds)
      throws ProtossException {
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);

    if (flagIds == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Flag ids must be supplied.");
    }

    Set<PatientFlag> allFlags = patientFlagManager.getFlags();
    Set<Integer> allFlagIds = allFlags.stream().map(PatientFlag::getId).collect(Collectors.toSet());
    Set<Integer> notFoundIds = flagIds.stream()
        .filter(i -> !allFlagIds.contains(i))
        .collect(Collectors.toSet());

    if (!notFoundIds.isEmpty()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Attempting to add non-existing patient flags: " + notFoundIds.toString());
    }

    Set<Integer> patientFlagIds = patientFlagManager.getFlagsForPatient(patientId);
    Set<Integer> newFlagIds = flagIds.stream()
        .filter(i -> !patientFlagIds.contains(i))
        .collect(Collectors.toSet());

    if (newFlagIds.isEmpty()) {
      return;
    }

    patientFlagManager.applyFlags(patientId, newFlagIds);
  }
}
