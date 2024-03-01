
package com.qhrtech.emr.restapi.endpoints.provider.scheduler;

import com.qhrtech.emr.accuro.api.scheduling.PriorityManager;
import com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.StatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AppointmentReasonDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentTypeDto;
import com.qhrtech.emr.restapi.models.dto.PriorityDto;
import com.qhrtech.emr.restapi.models.dto.StatusDto;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>ScheduleEndpoint</code> collection is designed to expose Appointment Types, Reasons,
 * Statuses, and Priorities.
 *
 * @RequestHeader Provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see com.qhrtech.emr.accuro.api.scheduling.PriorityManager
 * @see com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager
 * @see com.qhrtech.emr.accuro.api.scheduling.StatusManager
 */
@Component
@Path("/v1/provider-portal/scheduler")
@Tag(name = "Schedule Endpoints- Public",
    description = "Exposes schedule endpoints")
public class ScheduleEndpoint extends AbstractEndpoint {

  /**
   * Get a list of appointment types.
   *
   * @return List of types
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/types")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointment types",
      description = "Gets a list of appointment types.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AppointmentTypeDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<AppointmentTypeDto> getAppointmentTypes() throws ProtossException {

    ScheduleSettingsManager settings = getImpl(ScheduleSettingsManager.class);
    return mapDto(settings.getTypes(), AppointmentTypeDto.class, ArrayList::new);
  }

  /**
   * Get a list of appointment reasons.
   *
   * @return List of reasons
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/reasons")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointment reasons",
      description = "Gets a list of appointment reasons.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AppointmentReasonDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointment reasons in offices that contain a provider "
          + "the user has this permission for.")
  public List<AppointmentReasonDto> getAppointmentReasons() throws ProtossException {

    ScheduleSettingsManager settings = getImpl(ScheduleSettingsManager.class);
    return mapDto(settings.getReasons(), AppointmentReasonDto.class, ArrayList::new);
  }

  /**
   * Get a list of appointment statuses.
   *
   * @return List of statuses
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointment statuses",
      description = "Gets a list of appointment statuses.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = StatusDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<StatusDto> getAppointmentStatuses() throws ProtossException {

    StatusManager statusInterface = getImpl(StatusManager.class);
    return mapDto(statusInterface.getStatusList(), StatusDto.class, ArrayList::new);
  }

  /**
   * Get a list of appointment priorities.
   *
   * @return List of priorities
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/priorities")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointment priorities",
      description = "Gets a list of appointment priorities.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = PriorityDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<PriorityDto> getPriorities() throws ProtossException {

    PriorityManager priorityApi = getImpl(PriorityManager.class);
    return mapDto(priorityApi.getPriorities(), PriorityDto.class, ArrayList::new);
  }
}
