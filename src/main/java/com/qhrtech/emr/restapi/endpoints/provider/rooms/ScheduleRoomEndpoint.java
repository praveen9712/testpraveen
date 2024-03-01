
package com.qhrtech.emr.restapi.endpoints.provider.rooms;

import com.qhrtech.emr.accuro.api.scheduling.rooms.ScheduleRoomManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.scheduling.rooms.ScheduleRoom;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.rooms.ScheduleRoomDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermission;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/provider-portal/scheduler")
@Facet("provider-portal")
@Tag(name = "ScheduleRoom Endpoint", description = "Exposes schedule room endpoint")
public class ScheduleRoomEndpoint extends AbstractEndpoint {

  /**
   * Retrieves the active schedule room by given room id.
   *
   * @param id The id of the schedule room
   * @return The schedule room
   * @throws ProtossException If there has been a database error
   * @HTTP 200 Success
   * @HTTP 404 Not found
   */
  @Operation(
      summary = "Retrieves the active schedule room",
      description = "Gets the active schedule room by the give room id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ScheduleRoomDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/schedule-rooms/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ' ) "
      + "and (#accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) "
      + "or  #accuro.hasAccess( 'TRAFFIC_MANAGER', 'READ_ONLY' )) ")
  @RolePermissions(
      operation = LogicalOperation.OR,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "TRAFFIC_MANAGER")})
  public ScheduleRoomDto getById(
      @Parameter(description = "The schedule room id") @PathParam("id") int id)
      throws DataAccessException, SupportingResourceNotFoundException {
    try {
      ScheduleRoomManager scheduleRoomManager = getImpl(ScheduleRoomManager.class);
      ScheduleRoom room = scheduleRoomManager.getById(id, getUser().getUserId());

      if (room == null) {
        throw Error.webApplicationException(Status.NOT_FOUND, "Schedule room not found.");
      }

      return mapDto(room, ScheduleRoomDto.class);
    } catch (ForbiddenException e) {
      throw Error.webApplicationException(Status.FORBIDDEN, e.getMessage());
    }

  }

  /**
   * Retrieves the active schedule rooms by the given appointment id.
   *
   * @param appointmentId The id of the appointment
   * @return The list of the schedule rooms
   * @throws ProtossException If there has been a database error
   * @HTTP 200 Ok
   * @HTTP 404 Not found
   */
  @Operation(
      summary = "Retrieves the schedule rooms of the appointment",
      description = "Gets the active schedule room by the give appointment id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = ScheduleRoomDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/appointments/{appointmentId}/schedule-rooms")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ' ) "
      + "and (#accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) "
      + "or  #accuro.hasAccess( 'TRAFFIC_MANAGER', 'READ_ONLY' )) ")
  @RolePermissions(
      operation = LogicalOperation.OR,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "TRAFFIC_MANAGER")})
  public List<ScheduleRoomDto> getByAppointmentId(
      @Parameter(
          description = "The appointment room id") @PathParam("appointmentId") int appointmentId)
      throws ProtossException {
    ScheduleRoomManager scheduleRoomManager = getImpl(ScheduleRoomManager.class);
    List<ScheduleRoom> rooms =
        scheduleRoomManager.getByAppointmentId(appointmentId, getUser().getUserId());
    return mapDto(rooms, ScheduleRoomDto.class, ArrayList::new);
  }

  /**
   * Saves the given schedule room.
   *
   * The given order will be ignored and set it as the last order.
   *
   * @param scheduleRoomDto The schedule room
   * @return The id of the schedule room
   * @throws ProtossException If there has been a database error
   * @HTTP 201 Created
   * @HTTP 400 If the schedule room is not given
   */
  @Operation(
      summary = "Saves the given schedule room",
      description = "Creates the given schedule room. "
          + "The given order will be ignored and set it as the last order.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If the schedule room is not given"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(schema = @Schema(type = "integer")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @Path("/schedule-rooms")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) "
      + "and #accuro.hasFeature( 'SCHEDULING_MANAGE_ROOMS' ) ")
  @FeaturePermission(type = FeatureType.SCHEDULING_MANAGE_ROOMS)
  public Response create(@NotNull @Valid ScheduleRoomDto scheduleRoomDto)
      throws ProtossException {

    // set default values
    ScheduleRoom scheduleRoom = mapDto(scheduleRoomDto, ScheduleRoom.class);
    scheduleRoom.setTmSimple(false);
    scheduleRoom.setTmAvatar("");
    scheduleRoom.setTmRow(0);
    scheduleRoom.setIconColor(-16777216);
    if (scheduleRoom.getTmNote() == null) {
      scheduleRoom.setTmNote("");
    }
    if (scheduleRoom.getKioskMessage() == null) {
      scheduleRoom.setKioskMessage("");
    }

    try {
      ScheduleRoomManager scheduleRoomManager = getImpl(ScheduleRoomManager.class);
      int id = scheduleRoomManager.create(scheduleRoom, getUser());
      return Response.status(Status.CREATED).entity(id).build();
    } catch (ForbiddenException e) {
      throw Error.webApplicationException(Status.FORBIDDEN, e.getMessage());
    }

  }

  /**
   * Updates the given schedule room.
   *
   * @param scheduleRoomDto The schedule room
   * @throws ProtossException If there has been a database error
   * @HTTP 204 Successful but no content
   * @HTTP 400 If the schedule room is not given
   * @HTTP 400 If the given path id and room id are different
   */
  @Operation(
      summary = "Updates the schedule room",
      description = "Updates the given schedule room.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If the schedule room is not given"),
          @ApiResponse(
              responseCode = "400",
              description = "If the given path id and room id are different"),
          @ApiResponse(
              responseCode = "404",
              description = "If the room not found"),
          @ApiResponse(
              responseCode = "204",
              description = "Successful but no content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/schedule-rooms/{id}")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) "
      + "and #accuro.hasFeature( 'SCHEDULING_MANAGE_ROOMS' ) ")
  @FeaturePermission(type = FeatureType.SCHEDULING_MANAGE_ROOMS)
  public void update(@PathParam("id") int id,
      @NotNull @Valid ScheduleRoomDto scheduleRoomDto)
      throws ProtossException {

    if (id != scheduleRoomDto.getId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The room id of the path param should be the same as the id of the given room.");
    }
    try {
      ScheduleRoomManager scheduleRoomManager = getImpl(ScheduleRoomManager.class);
      ScheduleRoom oldScheduleRoom = scheduleRoomManager.getById(id, getUser().getUserId());

      if (oldScheduleRoom == null) {
        throw Error.webApplicationException(Status.NOT_FOUND, "The room not found.");
      }

      // set the values from old schedule room
      ScheduleRoom newScheduleRoom = mapDto(scheduleRoomDto, ScheduleRoom.class);
      newScheduleRoom.setSvgIcon(oldScheduleRoom.getSvgIcon());
      newScheduleRoom.setIconColor(oldScheduleRoom.getIconColor());
      newScheduleRoom.setTmAvatar(oldScheduleRoom.getTmAvatar());
      newScheduleRoom.setTmRow(oldScheduleRoom.getTmRow());
      newScheduleRoom.setTmSimple(oldScheduleRoom.isTmSimple());
      if (newScheduleRoom.getTmNote() == null) {
        newScheduleRoom.setTmNote("");
      }
      if (newScheduleRoom.getKioskMessage() == null) {
        newScheduleRoom.setKioskMessage("");
      }

      scheduleRoomManager.update(newScheduleRoom, getUser());
    } catch (ForbiddenException e) {
      throw Error.webApplicationException(Status.FORBIDDEN, e.getMessage());
    }

  }

  /**
   * Deletes the schedule room by the given id.
   *
   * @param id The id of The schedule room
   * @throws ProtossException If there has been a database error
   * @HTTP 204 Successful but no content
   */
  @Operation(
      summary = "Deletes the given schedule room",
      description = "Deletes the given schedule room.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If the schedule room is not given"),
          @ApiResponse(
              responseCode = "204",
              description = "Successful but no content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @DELETE
  @Path("/schedule-rooms/{id}")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) "
      + "and #accuro.hasFeature( 'SCHEDULING_MANAGE_ROOMS' ) ")
  @FeaturePermission(type = FeatureType.SCHEDULING_MANAGE_ROOMS)
  public void delete(@PathParam("id") int id)
      throws DataAccessException, SupportingResourceNotFoundException {
    try {
      ScheduleRoomManager scheduleRoomManager = getImpl(ScheduleRoomManager.class);
      scheduleRoomManager.delete(id, getUser());
    } catch (ForbiddenException e) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          e.getMessage());
    }
  }
}
