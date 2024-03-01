
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.patient.PatientChartLockManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.patient.PatientChartLock;
import com.qhrtech.emr.accuro.model.patient.PatientChartLockException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.PatientChartLockDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code PatientChartLockEndpoint} is designed to expose {@link PatientChartLockDto}
 * endpoints.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "Patient Chart Lock Endpoints", description = "Exposes patient chart lock endpoints")
public class PatientChartLockEndpoint extends AbstractEndpoint {

  /**
   * Get patient chart lock for given patient
   *
   * @param patientId Patient ID
   * @return {@link PatientChartLockDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/patient-chart-lock")
  @PreAuthorize("#oauth2.hasScope('user/provider.PatientChartLock.read')")
  @Operation(
      summary = "Retrieves Patient chart lock",
      description = "Retrieves patient chart lock",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientChartLockDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientChartLockDto getByPatientId(
      @Parameter(description = "PatientId") @PathParam("patientId") int patientId)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    PatientChartLockManager manager = getImpl(PatientChartLockManager.class);

    PatientChartLock byPatientId = manager.getByPatientId(patientId);
    PatientChartLockDto chartLockDto = mapDto(byPatientId, PatientChartLockDto.class);

    return chartLockDto;
  }

  /**
   * Create patient chart lock for given patient
   *
   * hasExceptions is read-only field and it would be set to true or false by the system depending
   * upon the user IDs passed in the patient chart lock object.
   *
   * Patient ID field in the {@link PatientChartLockException} object is read only field and not
   * considered while creating patient chart lock.
   *
   * The user will face an exception based on the below scenarios:
   *
   * 1. allProviders is true in the presence of exception user IDs.
   *
   * 2. Patient id is invalid
   *
   * 3. User id of any exceptions is invalid
   *
   * @param patientId Patient ID
   * @param chartLockDto {@link PatientChartLockDto} Patient chart lock DTO object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/patient-chart-lock")
  @PreAuthorize("#oauth2.hasScope('user/provider.PatientChartLock.create')")
  @Operation(
      summary = "Create patient chart lock for given patient ",
      description = "Create patient chart lock for given patient. "
          + "hasExceptions is read-only field and it would be set to true or false by the system "
          + "depending upon the user IDs passed in the patient chart lock object. "
          + "Patient ID field in the PatientChartLockException object is read only field "
          + "and not considered while creating patient chart lock. "
          + "The user will face an exception based on the below scenarios:\n"
          + "1. allProviders is true in the presence of exception user IDs.\n"
          + "2. Patient id is invalid\n"
          + "3. User id of any exceptions is invalid",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createPatientChartLock(
      @Parameter(description = "PatientId") @PathParam("patientId") int patientId,
      @Valid PatientChartLockDto chartLockDto)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      ResourceConflictException, ForbiddenException, TimeZoneNotFoundException,
      SupportingResourceNotFoundException {

    if (chartLockDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Request body cannot be null.");
    }
    if (patientId != chartLockDto.getPatientId()) {

      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient id in path does not match the id in the body.");
    }

    if (chartLockDto.getExceptions() != null) {
      chartLockDto.getExceptions().removeAll(Collections.singletonList(null));
    }

    PatientChartLockManager manager = getImpl(PatientChartLockManager.class);

    // Check existing lock
    PatientChartLock lock = manager.getByPatientId(patientId);
    if (lock != null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient chart lock already exists - patient id: " + patientId);
    }

    PatientChartLock chartLock = mapDto(chartLockDto, PatientChartLock.class);

    manager.create(chartLock);

    return Response.status(Status.CREATED).build();
  }


  /**
   * Unlocks chart lock for given patient
   *
   * Note: Patient Id is not validated for its activeness during this operation
   *
   * @param patientId Patient ID
   * @param unlockReason Unlock reason. Required
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @POST
  @Path("/{patientId}/patient-chart-unlock")
  @PreAuthorize("#oauth2.hasScope('user/provider.PatientChartLock.delete')")
  @Operation(
      summary = "Unlocks chart lock for given patient",
      description = "Unlocks chart lock for given patient. Unlock reason is required for this "
          + "operation Note: Patient Id is not validated for its activeness during this operation.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientChartLockDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response patientChartUnlock(
      @Parameter(description = "PatientId") @PathParam("patientId") int patientId,
      @Parameter(description = "Unlock reason",
          required = true) @QueryParam("unlockReason") String unlockReason)
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException, SupportingResourceNotFoundException, NoDataFoundException {

    if (StringUtils.isBlank(unlockReason)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Unlock reason is required for this operation.");
    }

    PatientChartLockManager manager = getImpl(PatientChartLockManager.class);

    manager.deleteByPatientId(patientId, unlockReason);

    return Response.status(Status.NO_CONTENT).build();
  }

}
