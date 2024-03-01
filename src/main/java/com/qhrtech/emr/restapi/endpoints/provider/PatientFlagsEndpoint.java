
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.patient.PatientFlagManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.PatientRoleFlag;
import com.qhrtech.emr.accuro.model.patient.PatientUserFlag;
import com.qhrtech.emr.accuro.model.patient.PatientUserRoleFlagId;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.PatientRoleFlagDto;
import com.qhrtech.emr.restapi.models.dto.PatientUserFlagDto;
import com.qhrtech.emr.restapi.models.dto.PatientUserRoleFlagIdDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientFlagsEndpoint</code> collection is designed to expose the Patient UserFlag and
 * RoleFlags endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 * @see com.qhrtech.emr.accuro.api.patient.PatientFlagManager
 */
@Component
@Path("/v2/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientFlags Endpoints",
    description = "Exposes patient user flags and role flags endpoints")
public class PatientFlagsEndpoint extends AbstractEndpoint {

  /**
   * Retrieves patient user flag associated with the patient id and belongs to the logged in user.
   *
   * @param patientId Patient ID
   * @return Set of patient user flags.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/user-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient user flag belongs to logged-in user",
      description = "Retrieves patient user flag associated with the patient id "
          + "and belongs to the logged in user",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientUserFlagDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or Client level authorization grant "
          + "with first party scope.",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientUserFlagDto> getPatientUserFlag(
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId)
      throws ProtossException, SQLException {

    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientUserFlag> patientUserFlagSet =
        patientFlagManager.getPatientUserFlags(patientId);
    PatientUserFlag userFlag = getPatientUserFlagFromSet(patientUserFlagSet);
    if (userFlag == null) {
      return Collections.emptySet();
    }
    return mapDto(Collections.singleton(userFlag), PatientUserFlagDto.class, HashSet::new);
  }

  /**
   * Retrieves patient role flags associated with the patient id.
   *
   * @param patientId Patient ID
   * @return Set of patient role flags.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/role-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient role flags",
      description = "Retrieves patient role flags associated with the patient id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON,
                  examples = @ExampleObject(
                      value = "["
                          + "{\"roleId\": 1,"
                          + "\"flag\": \"A message\","
                          + "\"lastUpdatedDate\": \"2017-11-08T00:00:00.000\""
                          + "}"
                          + "]")))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientRoleFlagDto> getPatientRoleFlags(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId)
      throws ProtossException {

    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientRoleFlag> patientRoleFlagSet =
        patientFlagManager.getPatientRoleFlags(patientId);

    return mapDto(patientRoleFlagSet, PatientRoleFlagDto.class, HashSet::new);
  }

  /**
   * Deletes an user flag associated to a patient.
   *
   * @param patientId Patient ID
   * @param userId User ID
   * @throws ProtossException If there has been a database error.
   */
  @DELETE
  @Path("/{patientId}/user-flags/{userId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes a patient user flag",
      description = "Deletes patient user flag associated with patient id and logged in user. "
          + "An user flag can be accessed/modified only by the logged in user",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "User id should match the logged in user"),
          @ApiResponse(
              responseCode = "404",
              description = "User Flag Not Found for Deletion"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or Client level authorization grant "
          + "with first party scope.",
      in = ParameterIn.HEADER,
      required = true)
  public Response deletePatientUserFlag(
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId,
      @Parameter(description = "User Id") @PathParam("userId") int userId)
      throws ProtossException, SQLException {

    if (getUser().getUserId() != null && userId != getUser().getUserId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "User id should match the logged in user");
    }
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientUserFlag> patientUserFlagSet =
        patientFlagManager.getPatientUserFlags(patientId);
    if (getUser().getUserId() == null) {
      patientFlagManager.deletePatientUserFlag(patientId, userId, getUser());
    } else if (patientUserFlagSet.stream().filter(f -> f.getUserId() == getUser().getUserId())
        .findAny().isPresent()) {
      patientFlagManager.deletePatientUserFlag(patientId, userId, getUser());
    } else {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Patient User Flag Not Found for Deletion");
    }
    return Response.ok().build();
  }


  /**
   * Deletes a role flag associated to a patient.
   *
   * @param patientId Patient ID
   * @throws ProtossException If there has been a database error.
   */
  @DELETE
  @Path("/{patientId}/role-flags/{roleId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes patient role flag",
      description = "Deletes patient role flag associated with the patient id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Role Flag Not Found for Deletion"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response deletePatientRoleFlag(
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId,
      @Parameter(description = "Role Id") @PathParam("roleId") int roleId)
      throws ProtossException {

    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientRoleFlag> roleFlagSet = patientFlagManager.getPatientRoleFlags(patientId);

    if (roleFlagSet.stream().filter(f -> f.getRoleId() == roleId).findAny().isPresent()) {
      patientFlagManager.deletePatientRoleFlag(patientId, roleId, getUser());
    } else {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Patient Role Flag Not Found for Deletion");
    }
    return Response.ok().build();
  }


  /**
   * Creates an user flag for a patient.
   *
   * @param patientId Patient ID
   * @param userFlag {@link PatientUserFlag}
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/user-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Creates a patient user flag",
      description = "Creates a patient user flag",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Patient user flags missing or Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientUserRoleFlagIdDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or Client level authorization grant "
          + "with first party scope.",
      in = ParameterIn.HEADER,
      required = true)
  public PatientUserRoleFlagIdDto createPatientUserFlags(
      @RequestBody(description = "Patient User Flag") PatientUserFlagDto userFlag,
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId)
      throws ProtossException, SQLException {

    if (userFlag == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient user flags missing");
    }
    if (getUser().getUserId() != null && userFlag.getUserId() != getUser().getUserId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "User id should match the logged in user");
    }

    PatientUserRoleFlagIdDto patientUserRoleFlagIdDto = null;
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);

    Set<PatientUserFlag> patientUserFlagSet =
        patientFlagManager.getPatientUserFlags(patientId);
    PatientUserFlag patientUserFlagFromSet = getPatientUserFlagFromSet(patientUserFlagSet);

    if (patientUserFlagFromSet != null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "userFlag already exists for patient: " + patientId);
    }
    Set<PatientUserRoleFlagId> userRoleFlagIdSet = patientFlagManager.createPatientUserFlags(
        patientId,
        mapDto(Collections.singleton(userFlag), PatientUserFlag.class, HashSet::new),
        getUser());
    for (PatientUserRoleFlagId userFlagId : userRoleFlagIdSet) {
      patientUserRoleFlagIdDto = mapDto(userFlagId, PatientUserRoleFlagIdDto.class);
    }
    return patientUserRoleFlagIdDto;
  }

  /**
   * Creates role flags for a patient.
   *
   * @param patientId Patient ID
   * @param patientFlagsSet Set of {@link PatientRoleFlag}'s
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/role-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Creates patient role flags",
      description = "Creates patient role flags",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Patient role flags missing or invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON,
                  examples = @ExampleObject(
                      value = "["
                          + "{\"patientId\": 1,"
                          + "\"flagId\": 2}]")))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<PatientUserRoleFlagIdDto> createPatientRoleFlags(
      @RequestBody(description = "Patient role flags") Set<PatientRoleFlagDto> patientFlagsSet,
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId)
      throws ProtossException {

    if (patientFlagsSet == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient role flags missing.");
    }
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    Set<PatientRoleFlag> patientRoleFlagSet = patientFlagManager.getPatientRoleFlags(patientId);
    boolean flagExistanceCheck = false;
    List<Integer> errRoleIdList = new ArrayList<>();
    for (PatientRoleFlagDto roleFlag : patientFlagsSet) {
      for (PatientRoleFlag allRoleFlags : patientRoleFlagSet) {
        if (roleFlag.getRoleId() == allRoleFlags.getRoleId()) {
          flagExistanceCheck = true;
          errRoleIdList.add(roleFlag.getRoleId());
        }
      }
    }
    if (flagExistanceCheck) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Role flags already exists for " + errRoleIdList.toString());
    }
    Set<PatientUserRoleFlagId> userRoleFlagIdSet = patientFlagManager.createPatientRoleFlags(
        patientId,
        mapDto(patientFlagsSet, PatientRoleFlag.class, HashSet::new),
        getUser());
    return mapDto(userRoleFlagIdSet, PatientUserRoleFlagIdDto.class, HashSet::new);
  }

  /**
   * Updates an user flag for a patient.
   *
   * @param patientId Patient ID
   * @param userFlag {@link PatientUserFlag}
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/user-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient user flag",
      description = "Updates patient user flag",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Patient user flags missing or Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or "
          + "Client level authorization grant with first party scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response updatePatientUserFlags(
      @RequestBody(description = "Patient User Flag") PatientUserFlagDto userFlag,
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId)
      throws ProtossException {

    if (userFlag == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient user flag missing.");
    }
    if (getUser().getUserId() != null && userFlag.getUserId() != getUser().getUserId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "User id should match the logged in user.");
    }

    PatientUserFlag patientUserFlag = mapDto(userFlag, PatientUserFlag.class);
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    patientFlagManager.updatePatientUserFlags(patientId,
        Collections.singleton(patientUserFlag),
        getUser());
    return Response.ok().build();
  }

  /**
   * Updates role flags for a patient.
   *
   * @param patientId Patient ID
   * @param roleFlags {@link PatientRoleFlag}'s
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{patientId}/role-flags")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates patient role flags",
      description = "Updates patient role flags",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Patient role flags missing or invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response updatePatientRoleFlags(
      @RequestBody(description = "Patient role flags") Set<PatientRoleFlagDto> roleFlags,
      @Parameter(description = "Patient Id") @PathParam("patientId") int patientId)
      throws ProtossException {

    if (roleFlags == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient role flags missing.");
    }
    PatientFlagManager patientFlagManager = getImpl(PatientFlagManager.class);
    patientFlagManager.updatePatientRoleFlags(patientId,
        mapDto(roleFlags, PatientRoleFlag.class, HashSet::new),
        getUser());
    return Response.ok().build();
  }

  private PatientUserFlag getPatientUserFlagFromSet(Set<PatientUserFlag> patientUserFlagSet) {
    for (PatientUserFlag userFlag : patientUserFlagSet) {
      if (getUser().getUserId() == null || userFlag.getUserId() == getUser().getUserId()) {
        return userFlag;
      }
    }
    return null;
  }
}
