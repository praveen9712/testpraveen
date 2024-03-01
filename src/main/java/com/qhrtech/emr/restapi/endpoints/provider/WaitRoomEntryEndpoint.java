
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ModuleAccessException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.scheduling.rooms.WaitRoomEntry;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.WaitRoomEntryDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
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
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>WaitRoomEntryEndpoint</code> collection is designed to expose the WaitRoom DTO and
 * related public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */

@Component
@Path("/v1/provider-portal/wait-room-entries/")
@Facet("provider-portal")
@Tag(name = "Waitroom Endpoints", description = "Exposes waitroom endpoints")
public class WaitRoomEntryEndpoint extends AbstractEndpoint {

  /**
   * Return the {@link WaitRoomEntryDto} waitroom entry related to given id.
   *
   * @param id WaitRoom Id
   * @return WaitRoom entry {@link WaitRoomEntryDto} associated with the given id.
   * @throws DatabaseInteractionException If a database error occurs.
   * @throws ModuleAccessException If required module is not enabled for the operation.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 404 If the waitroom entry is not found in database.
   * @HTTP 404 If the record does not exist in the database.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ' ) "
      + "and (#accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) "
      + "or  #accuro.hasAccess( 'TRAFFIC_MANAGER', 'READ_ONLY' )) ")

  @RolePermissions(
      operation = LogicalOperation.OR,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "TRAFFIC_MANAGER")})
  @Operation(
      summary = "Retrieves waitroom entry.",
      description = "Retrieves wait room entry by id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Wait room entry not found."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = WaitRoomEntryDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public WaitRoomEntryDto getWaitRoomEntryById(
      @Parameter(description = "WaitRoom Id") @PathParam("id") int id)
      throws ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {

    WaitRoomEntryManager waitRoomEntryManager = getImpl(WaitRoomEntryManager.class);
    WaitRoomEntry entry = waitRoomEntryManager.getById(id);
    return mapDto(entry, WaitRoomEntryDto.class);
  }

  /**
   * Return the @List{@link WaitRoomEntryDto} waitroom entries satisfying the given criteria.
   *
   * <p>
   * All of the parameters are optional. All existing waitroom entries for the current day will be
   * returned if none of the params are provided. If multiple filters are provided, they will be
   * combined with AND operator. If start and end date are provided the the results between dates
   * are displayed.
   * </p>
   *
   * @param isCompleted Waitroom entry if completed or in progress.
   * @param providerId The provider id.
   * @param patientId The patient id.
   * @param officeId The unique office id.
   * @param roomId Unique id for the wait room.
   * @param appointmentId The unique id for an appointment.
   * @param startDate Start date for the filter.
   * @param endDate End date for the filter.
   * @return List of WaitRoom entries associated with the parameters passed.
   * @throws DatabaseInteractionException If a database error occurs.
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database
   * @throws ModuleAccessException If required module is not enabled for the operation.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @GET
  @Path("/")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ' ) "
      + "and (#accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) "
      + "or  #accuro.hasAccess( 'TRAFFIC_MANAGER', 'READ_ONLY' )) ")
  @RolePermissions(
      operation = LogicalOperation.OR,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "TRAFFIC_MANAGER")})
  @Operation(
      summary = "Retrieves waitroom entries.",
      description = "Retrieves the waitroom entries satisfying the given criteria.\n"
          + "All of the parameters are optional. \n All existing waitroom entries for the current "
          + "day will be returned if none of the params are provided. If multiple filters are "
          + "provided, they will be combined with AND operator. If start and end date are "
          + "provided the the results between dates are displayed.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = WaitRoomEntryDto.class))))})
  @Parameters(value = {
      @Parameter(
          name = "authorization",
          description = "Provider level authorization grant",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "isCompleted",
          description = "Wait room entries completed or in progress.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "providerId",
          description = "Unique id of the provider.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "patientId",
          description = "Unique id of the patient",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "officeId",
          description = "Office Id for which waitroom entries are requested.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "roomId",
          description = "Unique id for the wait room.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "appointmentId",
          description = "Unique id for the appointment.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "startDate",
          description = "Starting date.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "endDate",
          description = "End date.",
          in = ParameterIn.QUERY)})
  public List<WaitRoomEntryDto> getWaitRoomEntries(
      @Parameter(hidden = true) @QueryParam("isCompleted") Boolean isCompleted,
      @Parameter(hidden = true) @QueryParam("providerId") Integer providerId,
      @Parameter(hidden = true) @QueryParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("officeId") Integer officeId,
      @Parameter(hidden = true) @QueryParam("roomId") Integer roomId,
      @Parameter(hidden = true) @QueryParam("appointmentId") Integer appointmentId,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate)
      throws TimeZoneNotFoundException, ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException {

    LocalDateTime localStart =
        startDate == null ? null : LocalDateTime.fromCalendarFields(startDate);
    LocalDateTime localEnd = endDate == null ? null : LocalDateTime.fromCalendarFields(endDate);

    if (localStart == null && localEnd != null) {
      localStart = localEnd;
      localEnd = null;
    }

    if (localEnd != null && localEnd.isEqual(localStart)) {
      localEnd = null;
    }

    if (localEnd != null && localStart.isAfter(localEnd)) {
      LocalDateTime temp = localStart;
      localStart = localEnd;
      localEnd = temp;
    }

    WaitRoomEntryManager waitRoomEntryManager = getImpl(WaitRoomEntryManager.class);
    List<WaitRoomEntry> result = waitRoomEntryManager.getWaitRoomEntries(isCompleted, providerId,
        patientId, officeId, roomId, appointmentId, localStart, localEnd);
    return mapDto(result, WaitRoomEntryDto.class, ArrayList::new);
  }

  /**
   * Creates a wait room entry.
   *
   * @param entry {@link WaitRoomEntryDto} Wait room entry
   * @throws DatabaseInteractionException If a database error occurs.
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database
   * @throws ModuleAccessException If required module is not enabled for the operation.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws NoDataFoundException If any required resource for the operation doesn't exist.
   * @throws InsufficientPermissionsException If the user doesn't have a permission on the office of
   *         the retrieving schedule room
   * @throws SupportingResourceNotFoundException If any of the required resource for the operation
   *         is not found
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  @Facet("internal")
  @Operation(
      summary = "Creates a wait room entry. \n"
          + "Note: PatientId, insurerId, providerId, appointmentId, arrived time and officeId "
          + "are required."
          + "If patient is assigned to specific room then entered room time is required.",
      description = "Creates wait room entry",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Bad Waitroom data."),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public int createWaitRoomEntry(
      @RequestBody(description = "Wait room entry") WaitRoomEntryDto entry)
      throws ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException, SupportingResourceNotFoundException,
      TimeZoneNotFoundException, NoDataFoundException, InsufficientPermissionsException {

    if (entry == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Wait room entry missing.");
    }

    WaitRoomEntryManager waitRoomEntryManager = getImpl(WaitRoomEntryManager.class);

    List<WaitRoomEntry> existingEntry =
        waitRoomEntryManager.getForAppointment(entry.getAppointmentId());

    if (existingEntry.stream().anyMatch(t -> t.getCompletedDate() == null)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Waitroom entry already exists for appointment: " + entry.getAppointmentId());
    }

    WaitRoomEntry waitRoomEntry = mapDto(entry, WaitRoomEntry.class);

    int waitRoomId = waitRoomEntryManager.create(waitRoomEntry, getUser());

    return waitRoomId;

  }

  /**
   * Updates wait room entry, the patient can be moved from waitroom to a new room, from one room to
   * another, back to waitroom and to completed Note: The room Id should be -1 to send the patient
   * to waiting room. To complete, set completed date time.
   *
   * @param entry {@link WaitRoomEntryDto} Wait room entry
   * @throws DatabaseInteractionException If a database error occurs.
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database
   * @throws ModuleAccessException If required module is not enabled for the operation.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws NoDataFoundException If any required resource for the operation doesn't exist.
   * @throws InsufficientPermissionsException If the user doesn't have a permission on the office of
   *         the retrieving schedule room
   * @throws SupportingResourceNotFoundException If any of the required resource for the operation
   *         is not found
   */
  @PUT
  @Path("/{waitroomId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  @Facet("internal")
  @Operation(
      summary = "Updates a wait room entry",
      description = "Updates wait room entry, the patient can be moved from waitroom to a new room,"
          + " from one room to another, back to waitroom, to completed."
          + " The room Id should be null or -1 to send the patient to waiting room."
          + " To complete, set completed date time.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Bad Waitroom data."),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  public Response updateWaitRoomEntry(
      @Parameter(description = "Waitroom id") @PathParam("waitroomId") int id,
      @RequestBody(description = "Wait room entry") WaitRoomEntryDto entry)
      throws DatabaseInteractionException, NoDataFoundException, TimeZoneNotFoundException,
      ModuleAccessException, InsufficientPermissionsException, SupportingResourceNotFoundException,
      UnsupportedSchemaVersionException {

    if (entry == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Wait room entry missing.");
    }

    if (id != entry.getId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Wait room id in path does not match with the body: ");
    }

    if (entry.getArrivedTime() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Arrived time cannot be null.");
    }

    WaitRoomEntryManager waitRoomEntryManager = getImpl(WaitRoomEntryManager.class);
    WaitRoomEntry waitRoomEntry = mapDto(entry, WaitRoomEntry.class);
    waitRoomEntryManager.update(waitRoomEntry, getUser());
    return Response.ok().build();
  }


  /**
   * Deletes wait room entry, Patient left -> this boolean option will add 'Patient Left' in
   * appointment History if set to true or just clear the history if set to false.\n" Note:'Patient
   * Left' will automatically remove the patient from traffic manager. Default value of patientLeft
   * is false.
   *
   * @param waitRoomId Wait room entry Id
   * @param patientLeft patientLeft flag (true/false)
   * @throws DatabaseInteractionException If a database error occurs.
   * @throws TimeZoneNotFoundException If the time zone preference is not set in the database
   * @throws ModuleAccessException If required module is not enabled for the operation.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws NoDataFoundException If any required resource for the operation doesn't exist.
   */
  @DELETE
  @Path("/{waitRoomId}/{patientLeft}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Operation(
      summary = "Deletes wait room entry",
      description = "Deletes wait room entry",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Wait room entry not found."),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "waitRoomId",
              description = "Wait Room Id to be deleted.",
              example = "12",
              in = ParameterIn.PATH),
          @Parameter(
              name = "patientLeft",
              description = "This option will add 'Patient Left' in appointment History if set to "
                  + "true or just clear the history if set to false.\n"
                  + "Note:'Patient Left' will automatically remove the patient from "
                  + "traffic manager. Default is false.",
              example = "true",
              in = ParameterIn.PATH)
      })
  @Hidden
  public Response deleteWaitRoom(
      @PathParam("waitRoomId") int waitRoomId,
      @PathParam("patientLeft") Boolean patientLeft)
      throws TimeZoneNotFoundException, ModuleAccessException, DatabaseInteractionException,
      UnsupportedSchemaVersionException, NoDataFoundException {

    WaitRoomEntryManager waitRoomEntryManager = getImpl(WaitRoomEntryManager.class);
    waitRoomEntryManager.delete(waitRoomId, (patientLeft == null ? false : patientLeft),
        getUser());

    return Response.ok().build();
  }
}
