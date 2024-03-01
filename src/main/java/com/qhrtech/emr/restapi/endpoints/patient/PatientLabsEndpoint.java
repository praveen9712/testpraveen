
package com.qhrtech.emr.restapi.endpoints.patient;

import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.labs.LabGroup;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabObservationDto;
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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientLabsEndpoint</code> collection is designed to expose the Patient Labs DTO and
 * related public endpoints. Requires patient level authorization.
 *
 * @RequestHeader Authorization Patient level authorization grant
 * @HTTP 200 Request was successful
 * @HTTP 401 Consumer is unauthorized
 */
@Component("patientLabEndpoint")
@Path("/v1/patient-portal/lab-groups")
@Facet("patient-portal")
@Tag(name = "PatientLabs Endpoints - Patient",
    description = "Exposes patient labs endpoints(patient)")
public class PatientLabsEndpoint extends AbstractEndpoint {

  /**
   * Retrieve all Lab Groups for the current patient.
   *
   * @param testIds The test Ids for which results needs to be retrieved.
   * @param resultIds The result Ids for which results needs to be retrieved.
   * @param startDate If both start and end date is provided, the observations between these are
   *        retrieved. If only start date is provided, the observation on that particular date are
   *        retrieved.
   * @param endDate If both start and end date is provided, the observations between these two dates
   *        are retrieved.
   * @return List of Lab Groups of the Patient.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves the lab groups",
      description = "Gets all the lab groups for the particular patient.",
      responses = {

          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = LabGroupDto.class))))})
  @Parameters({
      @Parameter(
          name = "authorization",
          description = "Patient authorization grant",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "testIds",
          description = "Multiple test ids can be provided, "
              + "i.e. in a request url, testIds=12&testIds=39. \n\n "
              + "If this parameter is provided, all results will be filtered "
              + "by the test ids they are a part of.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "resultIds",
          description = "Multiple result ids can be provided, "
              + "i.e. in a request url, resultIds=12&resultIds=39. \n\n "
              + "If this parameter is provided, all results will be filtered "
              + "by the result ids they are a part of.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "startDate",
          description = "If both start and end date is provided, the observations between "
              + "these are retrieved. If only start date is provided, the observation on that "
              + "particular date are retrieved.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "endDate",
          description = "If both start and end date is provided, the observations between these "
              + "two dates are retrieved",
          in = ParameterIn.QUERY)
  })
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public List<LabGroupDto> getLabGroups(
      @Parameter(hidden = true) @QueryParam("testIds") Set<Integer> testIds,
      @Parameter(hidden = true) @QueryParam("resultIds") Set<Integer> resultIds,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate)
      throws ProtossException {
    LocalDate start = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate end = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    LabManager labManager = getImpl(LabManager.class);
    return mapDto(
        labManager.getLabsForPatient(getPatientId(), testIds, resultIds, start, end),
        LabGroupDto.class, ArrayList::new);
  }

  /**
   * Retrieve Lab Group by Group Id.
   *
   * @param groupId Group Id
   * @return Lab Group of the Patient.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Group id is invalid.
   * @HTTP 403 Resource does not belong to the authorized user.
   * @HTTP 404 Lab Group not found.
   */
  @Operation(
      summary = "Retrieves the lab group",
      description = "Gets the lab group by the given group id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Group id is invalid"),
          @ApiResponse(
              responseCode = "403",
              description = "Resource does not belong to the authorized user"),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = LabGroupDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public LabGroupDto getLabGroup(
      @Parameter(description = "Group id") @PathParam("groupId") int groupId)
      throws ProtossException {

    if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    LabGroup group = labManager.getLab(groupId);
    if (group == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    } else if (!Objects.equals(getPatientId(), group.getPatientId())) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "Not authorized to view this resource.");
    }
    LabGroupDto groupDto = mapDto(group, LabGroupDto.class);

    return groupDto;
  }

  /**
   * Retrieve Observations for a Lab Group.
   *
   * @param groupId Group Id
   * @return List of Lab Observations for a specific group.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Group id is invalid.
   * @HTTP 403 Resource does not belong to the authorized user.
   * @HTTP 404 Lab Group not found.
   */

  @Operation(
      summary = "Retrieves the lab group observations",
      description = "Gets the observations for the given lab group id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Group id is invalid"),
          @ApiResponse(
              responseCode = "403",
              description = "Resource does not belong to the authorized user"),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = LabObservationDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}/observations")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public List<LabObservationDto> getLabGroupObservations(
      @Parameter(description = "Group id") @PathParam("groupId") int groupId)
      throws ProtossException {

    if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    LabGroup group = labManager.getLab(groupId);
    if (group == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    } else if (!Objects.equals(getPatientId(), group.getPatientId())) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "Not authorized to view this resource.");
    }
    LabGroupDto groupDto = mapDto(group, LabGroupDto.class);

    return groupDto.getObservations();
  }

  /**
   * List the complete history of a Patient's Lab Group.
   *
   * @param baseGroupId Base Group ID of the Lab (ID of the first Lab Group)
   * @return Collection of all Lab Group states in the Base Group.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Lab Group ID does not match the request resource.
   * @HTTP 404 Requested resource is not found
   */
  @Operation(
      summary = "Retrieves the lab group history",
      description = "Gets the complete history of the patient's base lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Lab group id does not match with the requested resource"),
          @ApiResponse(
              responseCode = "404",
              description = "Requested resource is not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = LabGroupDto.class))))
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Patient authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "baseGroupId",
              description = "Base group id of the lab (id of the first lab group)",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @GET
  @Path("/{baseGroupId}/history")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public List<LabGroupDto> getLabGroupHistory(
      @Parameter(hidden = true) @PathParam("baseGroupId") int baseGroupId)
      throws ProtossException {

    if (baseGroupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    List<LabGroupDto> baseGroup =
        mapDto(labManager.getLabsForBaseGroup(baseGroupId), LabGroupDto.class, ArrayList::new);

    if (baseGroup.isEmpty()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }

    baseGroup.stream().filter(g -> !Objects.equals(g.getPatientId(), getPatientId())).findAny()
        .ifPresent(g -> {
          throw Error.webApplicationException(Status.BAD_REQUEST,
              "The provided Lab Group Id does not match the specified resource.");
        });
    return baseGroup;
  }
}
