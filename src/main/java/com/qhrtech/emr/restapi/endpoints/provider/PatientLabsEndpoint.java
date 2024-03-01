
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.LabException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.labs.LabGroup;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabObservationDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermission;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientLabsEndpoint</code> collection is designed to expose the Patient Labs DTO and
 * related public endpoints. Requires provider level authorization.
 *
 * This controller is sub-resourced from {@link PatientEndpoint}. All endpoints in this controller
 * are prefaced by: /patients/{patientId}/lab-groups
 *
 * @RequestHeader Authorization Patient authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Path("/v1/provider-portal/patients/{patientId}/lab-groups")
@Component("providerLabEndpoint")
@Facet("provider-portal")
@Tag(name = "PatientLabs Endpoints - Provider",
    description = "Exposes patient labs endpoints(provider)")
public class PatientLabsEndpoint extends AbstractEndpoint {

  /**
   * Get Lab Groups for a specific patient.
   *
   * @param patientId Patient Id
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
   * @HTTP 400 If the Patient Id is invalid
   */
  @Operation(
      summary = "Retrieves the lab groups",
      description = "Gets the lab groups for the specific patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabGroupDto.class))))})
  @Parameters({
      @Parameter(
          name = "authorization",
          description = "Provider level authorization grant",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "testIds",
          description = "This parameter will filter the results by the test ids to which they "
              + "belong. \n\n Multiple test ids can be provided, "
              + "i.e. in the request url, testIds=12&testIds=39.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "resultIds",
          description = "This parameter will filter the results by the result ids to which they "
              + "belong. \n\n Multiple result ids can be provided, "
              + "i.e. in the request url, resultIds=12&resultIds=39.",
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
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public List<LabGroupDto> getLabGroups(
      @Parameter(description = "Patient id", hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @QueryParam("testIds") Set<Integer> testIds,
      @Parameter(hidden = true) @QueryParam("resultIds") Set<Integer> resultIds,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate)
      throws ProtossException {

    LocalDate start = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate end = endDate == null ? null : LocalDate.fromCalendarFields(endDate);
    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getLabsForPatient(patientId, testIds, resultIds, start, end),
        LabGroupDto.class, ArrayList::new);
  }

  /**
   * Retrieve a patients Lab Groups by group id.
   *
   * @param patientId Patient ID
   * @param groupId Group ID
   * @return Lab Group of the Patient.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Invalid Patient or Group ID is passed.
   */
  @Operation(
      summary = "Retrieves the lab group for the patient",
      description = "Gets the patient's lab group by group id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided lab group does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = LabGroupDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to lab groups with a recipient provider the user has this "
          + "permission for.")
  public LabGroupDto getLabGroup(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class);
    LabGroup labGroupFromProtoss = labManager.getLab(groupId);
    if (labGroupFromProtoss == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (!Objects.equals(patientId, labGroupFromProtoss.getPatientId())) {
      throw Error
          .webApplicationException(Status.BAD_REQUEST,
              "The provided Lab Group does not match the specified resource.");
    }
    return mapDto(labGroupFromProtoss, LabGroupDto.class);
  }

  /**
   * Creates a Lab Group for a particular Patient
   *
   * @param patientId Id of the patient on the lab.
   * @param labGroup Lab Group to create.
   * @return The database generated id, or {@code null} if an exception was thrown.
   * @throws LabException If there is invalid data in the updated Lab
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the patient id on the Lab Group does not match the specified resource.
   */

  @Operation(
      summary = "Saves the lab group",
      description = "Creates the lab group for the particular patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id.\n\n"
                  + "Transaction date is required for the lab groups.\n\n"
                  + "Version date is required for the lab groups.\n\n"
                  + "The provided lab group does not match with the specified resource.\n\n"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when the ordering provider is set to a provider the user has "
          + "this permission for.")
  public Integer createLabGroup(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @RequestBody(description = "New lab group") @Valid LabGroupDto labGroup)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (labGroup == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Lab Group.");
    } else if (labGroup.getTransactionDate() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Transaction Date is Required For Lab Groups.");
    } else if (labGroup.getVersionDate() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Version Date is Required For Lab Groups.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group does not match the specified resource.");
    }
    LabManager labManager = getImpl(LabManager.class);
    return labManager.createLab(mapDto(labGroup, LabGroup.class), getUser());
  }

  /**
   * Updates the Lab Group for a particular Patient. Lab Group updates create a new Lab Group, so
   * this call returns the new ID for the lab group.
   *
   * @param patientId ID of the patient on the lab.
   * @param labGroupId ID of the resource to be updated.
   * @param labGroup Lab Group to be updated.
   * @return The id of the new active {@link LabGroup}.
   * @throws LabException If there is invalid data in the updated Lab
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the patient id does not match the specified resource.
   * @HTTP 400 If the lab group id does not match the specified resource.
   */

  @Operation(
      summary = "Updates the lab group for the patient",
      description = "Updates the lab group for the particular patient. This endpoint creates "
          + "the new lab group, therefore it returns the new id for the lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id.\n\n"
                  + "Transaction date is required for the lab groups.\n\n"
                  + "Version date is required for the lab groups.\n\n"
                  + "The provided lab group does not match with the specified resource.\n\n"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns new group id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
          description = "Allows access when a recipient provider is a provider the user has this "
              + "permission for."),
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
          description = "Allows access when the ordering provider are providers the user has this "
              + "permission for.")
  })
  public Integer updateLabGroup(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int labGroupId,
      @RequestBody(description = "Updated lab group") @Valid LabGroupDto labGroup)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (labGroupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    } else if (labGroup == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Lab Group.");
    } else if (labGroup.getTransactionDate() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Transaction Date is Required For Lab Groups.");
    } else if (labGroup.getVersionDate() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Version Date is Required For Lab Groups.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error
          .webApplicationException(Status.BAD_REQUEST,
              "The provided Lab Group does not match the specified resource.");
    } else if (labGroupId != labGroup.getGroupId()) {
      throw Error
          .webApplicationException(Status.BAD_REQUEST,
              "The provided Lab Group does not match the specified resource.");
    }
    LabManager labManager = getImpl(LabManager.class);
    LabGroup mappedLabGroup = mapDto(labGroup, LabGroup.class);
    return labManager.updateLab(mappedLabGroup, getUser());
  }

  /**
   * Removes a Lab Group from a patient. Note: This end point will be implemented in the future.
   *
   * @param patientId If of the patient on the lab.
   * @param labGroupId Id of the resource to delete.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Lab Group Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   * @HTTP 500 Until lab deletion is fully implemented.
   */
  @Operation(
      summary = "Deletes the lab group",
      description = "Deletes the lab group for the particular patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided lab group id does not match with the specified resource.\n\n"),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @DELETE
  @Path("/{groupId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full, description = "Allows "
      + "access when a recipient provider is a provider the user has this permission for.")
  @FeaturePermission(type = FeatureType.EMR_DELETE_LABS,
      description = " - Is Required to delete a lab")
  public void deleteLabGroup(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int labGroupId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (labGroupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class, true);

    LabGroup labGroupFromProtoss = labManager.getLab(labGroupId);
    if (labGroupFromProtoss == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Lab Group Not Found.");
    } else if (patientId != labGroupFromProtoss.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group Id does not match the specified resource.");
    }
    LabManager labManagerDelete = getImpl(LabManager.class);
    labManagerDelete.deleteLabs(new HashSet<>(Collections.singletonList(labGroupId)), getUser());
  }

  /**
   * List the complete history of a Lab Group.
   *
   * @param patientId Id of the patient on the lab.
   * @param baseGroupId Base Group Id of the Lab (Id of the first Lab Group).
   * @return Collection of all Lab Group states in the Base Group.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Lab Group Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */

  @Operation(
      summary = "Retrieves the lab group history",
      description = "Retrieves the complete history of the lab group by group id "
          + "for the particular patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided lab group Id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabGroupDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{baseGroupId}/history")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access to history when a recipient provider is a provider the user has "
          + "this permission for.")
  public List<LabGroupDto> getLabGroupHistory(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Base group id") @PathParam("baseGroupId") int baseGroupId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (baseGroupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    List<LabGroupDto> baseGroup =
        mapDto(labManager.getLabsForBaseGroup(baseGroupId), LabGroupDto.class, ArrayList::new);

    if (baseGroup.isEmpty()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }

    if (baseGroup.stream().anyMatch(g -> !Objects.equals(g.getPatientId(), patientId))) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group Id does not match the specified resource.");
    }

    return baseGroup;
  }

  /**
   * Retrieves the Provider Ids who are recipients of the Lab Group
   *
   * @param patientId Id of the patient on the lab.
   * @param labGroupId Id of the resource requested.
   * @return Accuro Provider ids who have received this Lab Group.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Lab Group Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */
  @Operation(
      summary = "Retrieves the lab group recipient ids",
      description = "Retrieves the provider ids who are the recipients of the lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided lab group id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "integer",
                          example = "[ 1001 ]",
                          description = "Returns a set of ids"))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}/recipients")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when a recipient provider is a provider the user has this "
          + "permission for.")
  public Set<Integer> getLabGroupRecipientIds(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int labGroupId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (labGroupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    LabGroup labGroup = labManager.getLab(labGroupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group Id does not match the specified resource.");
    }

    return labManager.getRecipientsForLab(labGroupId);
  }

  /**
   * Adds the passed provider ids as Lab Group Recipients
   *
   * @param patientId Patient id of the patient on the Lab
   * @param groupId Id of the resource accessed.
   * @param recipients Accuro Provider ids to send the associated lab to.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the requested resource is not found
   * @HTTP 400 If the Lab Group Id does not match the request resource.
   */
  @Operation(
      summary = "Updates the lab group recipients",
      description = "Updates the passed provider ids as the lab group recipients.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided lab group id does not match with the specified resource.\n\n"),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "ID of the patient on the lab",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "groupId",
              description = "ID of the resource accessed",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(description = "Add recipient's ids to lab group",
      content = @Content(
          array = @ArraySchema(
              schema = @Schema(
                  type = "integer",
                  example = "1001",
                  description = "Recipient's ids"))))
  @PUT
  @Path("/{groupId}/recipients")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
      description = "Allows access when an existing recipient provider and the added recipient "
          + "providers are providers the user has this permission for.")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
          description = "Allows access when an existing recipient providers is a providers the "
              + "user has this permission for."),
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
          description = "Allows access when the added recipient providers are providers the user "
              + "has this permission for.")
  })
  public void updateLabGroupRecipients(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("groupId") int groupId,
      Set<Integer> recipients)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group Id does not match the specified resource.");
    }
    LabManager updateLabManager = getImpl(LabManager.class);
    updateLabManager.addRecipientsToLab(groupId, recipients, getUser());
  }

  /**
   * Removes the passed provider ids as recipients from the Lab Group
   *
   * @param patientId Id of the Patient on the Lab to.
   * @param groupId Id of the resource to delete.
   * @param recipients Provider ids to remove as recipients of the Lab.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the requested resource is not found
   * @HTTP 400 If the Lab Group Id does not match the request resource.
   */

  @Operation(
      summary = "Removes the lab group recipients",
      description = "Removes the passed provider ids as the recipients from the lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient ID or group ID.\n\n"
                  + "The provided lab group Id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id of the patient on the lab",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "groupId",
              description = "ID of the resource accessed",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @RequestBody(description = "Remove recipients from lab group",
      content = @Content(
          array = @ArraySchema(
              schema = @Schema(
                  type = "integer",
                  example = "[ 1001 ]",
                  description = "Recipient's ids"))))
  @DELETE
  @Path("/{groupId}/recipients")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
          description = "Allows access when the removed recipient providers are providers the "
              + "user has this permission for.")
  })
  public void removeLabGroupRecipients(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("groupId") int groupId,
      Set<Integer> recipients)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);

    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Lab Group Id does not match the specified resource.");
    }
    LabManager labManagerRemove = getImpl(LabManager.class);
    labManagerRemove.removeRecipientsFromLab(groupId, recipients, getUser());
  }

  /**
   * Retrieve Observations for a Lab Group.
   *
   * @param patientId Patient Id
   * @param groupId Group Id
   * @return List of Lab Observations for a specific group.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If an invalid Patient Id is passed.
   * @HTTP 400 If an invalid Group Id is passed.
   */
  @Operation(
      summary = "Retrieves the lab group observations",
      description = "Gets the observations for the particular lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = LabObservationDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}/observations")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when a recipient provider is a provider the user has this "
          + "permission for.")
  public List<LabObservationDto> getLabGroupObservations(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }
    LabManager labManager = getImpl(LabManager.class);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }
    LabGroupDto lab = mapDto(labGroup, LabGroupDto.class);
    return lab.getObservations();
  }

  /**
   * Retrieve a specific lab observation
   *
   * @param patientId Patient on the lab resource requested.
   * @param groupId Id of the resource requested.
   * @param observationId Id of the observation resource requested.
   * @return The Lab Observation requested.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Patient Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found (lab group or observation)
   */
  @Operation(
      summary = "Retrieves the lab group observation",
      description = "Gets an observation by observation id for the particular lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided patient id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = LabObservationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{groupId}/observations/{observationId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly,
      description = "Allows access when a recipient provider is a provider the user has this "
          + "permission for.")
  public LabObservationDto getLabGroupObservation(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId,
      @Parameter(description = "Observation id") @PathParam("observationId") int observationId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Patient Id does not match the specified resource.");
    }
    LabGroupDto lab = mapDto(labGroup, LabGroupDto.class);

    return lab.getObservations()
        .stream()
        .filter(o -> Objects.equals(observationId, o.getObservationId()))
        .findFirst()
        .orElseThrow(() -> Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found"));
  }

  /**
   * Updates an observation for a particular Lab Group.
   *
   * The Returned integer for this call will be the id of the new {@link LabGroup} as Lab Group
   * updates will always generate a new Lab Group.
   *
   * @param patientId Patient id of the requested resource.
   * @param groupId Lab Group id of the requested resource.
   * @param observationId Observation id of the requested resource.
   * @param labObservation The new state to update the resource to.
   * @return The id of the new active {@link LabGroup}.
   * @throws LabException If invalid data is passed.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Patient Id does not match the request resource.
   * @HTTP 400 If the Observation Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */

  @Operation(
      summary = "Updates the lab group observation",
      description = "Updates the observation for the particular lab group.\n\n"
          + "The returned integer for this endpoint call will be the id of the new lab group "
          + "as UPDATE operation always generates the new lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided patient id does not match with the specified resource.\n\n"
                  + "The provided observation id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{groupId}/observations/{observationId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full,
          description = "Allows access when the ordering provider is a provider the user has this "
              + "permission for.")
  })
  public Integer updateLabGroupObservation(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId,
      @Parameter(description = "Observation id") @PathParam("observationId") int observationId,
      @RequestBody(description = "Updated lab observation") @Valid LabObservationDto labObservation)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Patient Id does not match the specified resource.");
    } else if (labObservation.getObservationId() != observationId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Observation Id does not match the specified resource.");
    }
    LabGroupDto lab = mapDto(labGroup, LabGroupDto.class);

    List<LabObservationDto> observations = lab.getObservations()
        .stream()
        .filter(o -> !Objects.equals(observationId, o.getObservationId()))
        .collect(Collectors.toList());
    if (observations.size() == lab.getObservations().size()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    observations.add(labObservation);

    lab.setObservations(observations);
    LabManager labManagerUpdate = getImpl(LabManager.class);
    return labManagerUpdate.updateLab(mapDto(lab, LabGroup.class), getUser());
  }

  /**
   * Removes a Lab Observation from a Lab Group
   *
   * The Returned integer for this call will be the id of the new {@link LabGroup} as Lab Group
   * updates will always generate a new Lab Group.
   *
   * @param patientId Patient Id of the resource to remove.
   * @param groupId Lab Group Id of the resource to remove.
   * @param observationId Observation Id of the resource to remove.
   * @return The id of the new active {@link LabGroup}.
   * @throws LabException If an error deleting the observation occurs.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Patient Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */
  @Operation(
      summary = "Removes the lab group observation",
      description = "Removes the lab observation from a lab group.\n\n"
          + "The returned integer for this call will be the id of the new lab group "
          + "as lab group updates will always generate the new lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided patient id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns new active group id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @DELETE
  @Path("/{groupId}/observations/{observationId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full, description = "Allows "
          + "access when the ordering provider is a provider the user has this permission for.")
  })
  public Integer deleteLabGroupObservation(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId,
      @Parameter(description = "Observation id") @PathParam("observationId") int observationId)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Patient Id does not match the specified resource.");
    }
    LabGroupDto lab = mapDto(labGroup, LabGroupDto.class);

    List<LabObservationDto> observations = lab.getObservations()
        .stream()
        .filter(o -> !Objects.equals(observationId, o.getObservationId()))
        .collect(Collectors.toList());
    if (observations.size() == lab.getObservations().size()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    lab.setObservations(observations);
    LabManager labManagerUpdate = getImpl(LabManager.class);
    return labManagerUpdate.updateLab(mapDto(lab, LabGroup.class), getUser());
  }

  /**
   * Adds a new Lab Observation to a Lab Group
   *
   * The Returned integer for this call will be the id of the new {@link LabGroup} as Lab Group
   * updates will always generate a new Lab Group.
   *
   * @param patientId Patient Id of the resource modified.
   * @param groupId Lab Group Id of the resource modified.
   * @param labObservation Observation being added to the Lab Group.
   * @return The id of the new Lab Group generated.
   * @throws LabException If invalid data is passed.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Patient Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */
  @POST
  @Path("/{groupId}/observations")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full, description = "Allows "
          + "access when the ordering provider is a provider the user has this permission for.")
  })
  @Operation(
      summary = "Saves observation to lab group",
      description = "Adds the new lab observation to the lab group.\n\n"
          + "The returned integer for this call will be the id of the new lab group "
          + "as lab group updates will always generate the new lab group.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided patient id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer addLabGroupObservation(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId,
      @RequestBody(description = "Lab observation") @Valid LabObservationDto labObservation)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    } else if (patientId != labGroup.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided Patient Id does not match the specified resource.");
    }
    LabGroupDto lab = mapDto(labGroup, LabGroupDto.class);
    lab.getObservations().add(labObservation);
    LabManager labManagerUpdate = getImpl(LabManager.class);
    return labManagerUpdate.updateLab(mapDto(lab, LabGroup.class), getUser());
  }

  /**
   * Marks a Lab Group as review for a particular provider.
   *
   * @param patientId Patient Id of the resource modified.
   * @param groupId Lab Id of the resource modified.
   * @param providerIds Accuro Provider IDs to set as reviewed for this Lab
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the Patient Id does not match the request resource.
   * @HTTP 404 If the requested resource is not found
   */

  @Operation(
      summary = "Update to mark the lab group as reviewed",
      description = "Marks the lab group as reviewed for the particular provider.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid patient id or group id.\n\n"
                  + "The provided patient id does not match with the specified resource."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{groupId}/reviews")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Labs, level = AccessLevel.Full, description = "Allows "
          + "access when the reviewing providers are providers the user has this permission for.")
  })
  public void reviewLabGroupForProviders(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Group id") @PathParam("groupId") int groupId,
      @RequestBody(description = "A set of provider ids") Set<Integer> providerIds)
      throws ProtossException {

    if (patientId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Patient ID.");
    } else if (groupId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Group ID.");
    }

    LabManager labManager = getImpl(LabManager.class, true);
    LabGroup labGroup = labManager.getLab(groupId);
    if (labGroup == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found.");
    }
    if (patientId != labGroup.getPatientId()) {
      throw Error
          .webApplicationException(Status.BAD_REQUEST,
              "The provided Patient Id does not match the specified resource.");
    }
    LabManager labManagerReview = getImpl(LabManager.class);
    labManagerReview.reviewLabForProviders(groupId, providerIds, getUser());
  }
}
