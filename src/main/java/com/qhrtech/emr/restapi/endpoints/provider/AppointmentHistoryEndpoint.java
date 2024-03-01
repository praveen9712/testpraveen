
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.scheduling.AppointmentHistoryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AppointmentHistoryDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermissions;
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
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>AppointmentHistoryEndpoint</code> collection is designed to expose the Appointment
 * History DTO and related public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/scheduler")
@Facet("provider-portal")
@PreAuthorize("#oauth2.hasAnyScope('SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
    + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
@Tag(name = "AppointmentHistory Endpoints",
    description = "Exposes appointment history endpoints")
public class AppointmentHistoryEndpoint extends AbstractEndpoint {

  @GET
  @Path("/appointments/{appointmentId}/history")
  @Operation(
      summary = "Retrieves appointment history",
      description = "Retrieves the history of the appointment represented "
          + "by the series of state changes.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = AppointmentHistoryDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "appointmentId",
      description = "Appointment id",
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  @ProviderPermissions(
      operation = LogicalOperation.OR,
      providerPermissions = {
          @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
              description = "Allows access to appointment history for appointments with a provider "
                  + "the user has this permission for"),
          @ProviderPermission(type = AccessType.Billing, level = AccessLevel.ReadOnly,
              description = "Allows access to appointment history for appointments with a billing "
                  + "provider the user has this permission for")})
  public List<AppointmentHistoryDto> getAppointmentHistory(
      @Parameter(hidden = true) @PathParam("appointmentId") int appointmentId)
      throws ProtossException {

    AppointmentHistoryManager historyManager = getImpl(AppointmentHistoryManager.class);
    List<AppointmentHistoryDto> histories = mapDto(
        historyManager.getHistoryForAppointment(appointmentId),
        AppointmentHistoryDto.class,
        ArrayList::new);

    if (histories.isEmpty()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Appointment not found");
    }
    return histories;
  }

  /**
   * Return all appointment state changes for a date range.
   *
   * @param startDate Start date
   * @param endDate End date
   * @return A list of Appointment History DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/appointments/history")
  @Operation(
      summary = "Retrieves appointment history for the date range",
      description = "Retrieves all the appointment state changes for a date range.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Start date is invalid"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AppointmentHistoryDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "startDate",
      description = "Start date",
      in = ParameterIn.QUERY,
      required = true)
  @Parameter(
      name = "endDate",
      description = "End date",
      in = ParameterIn.QUERY,
      required = true)
  @ProviderPermissions(
      operation = LogicalOperation.OR,
      providerPermissions = {
          @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
              description = "Allows access to appointment history for appointments with a provider "
                  + "the user has this permission for"),
          @ProviderPermission(type = AccessType.Billing, level = AccessLevel.ReadOnly,
              description = "Allows access to appointment history for appointments with a billing "
                  + "provider the user has this permission for")})
  public List<AppointmentHistoryDto> getAppointmentHistory(
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate)
      throws ProtossException {

    if (startDate == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Missing Required Parameter: startDate");
    }

    LocalDate localStartDate = LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    if (localEndDate != null && localStartDate.isAfter(localEndDate)) {
      LocalDate temp = localStartDate;
      localStartDate = localEndDate;
      localEndDate = temp;
    }

    AppointmentHistoryManager manager = getImpl(AppointmentHistoryManager.class);

    return mapDto(manager.getHistoryByDate(localStartDate, localEndDate),
        AppointmentHistoryDto.class,
        ArrayList::new);
  }

}
