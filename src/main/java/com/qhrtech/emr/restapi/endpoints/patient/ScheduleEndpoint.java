
package com.qhrtech.emr.restapi.endpoints.patient;

import com.qhrtech.emr.accuro.api.scheduling.AppointmentManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.scheduling.Appointment;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AppointmentDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
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
import org.springframework.stereotype.Component;

/**
 * This <code>ScheduleEndpoint</code> collection is designed to expose the Appointment DTO and
 * related patient-level endpoints. Requires patient level authorization.
 *
 * @RequestHeader Authorization Patient level authorization grant
 *
 * @HTTP 200 Successful request
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 * @HTTP 404 Invalid query parameters
 *
 * @author kevin.kendall
 */
@Component("patient-portal-scheduler")
@Path("/v1/patient-portal/scheduler")
@Facet("patient-portal")
@Tag(name = "Schedule Endpoints - Patient",
    description = "Exposes patient portal schedule endpoints")
public class ScheduleEndpoint extends AbstractEndpoint {

  /**
   * Get all appointments for a specific date or date range. Filtered for a specific provider or
   * resource. This endpoint accepts both a start and end date. Results will be returned for
   * appointments that start or end within the specified range. If no end date is supplied then
   * results will only include appointments that started or ended on the start date.
   *
   * @param startDate Start date.
   * @param endDate End date.
   * @param providerId Provider id.
   * @param resourceId Resource id.
   *
   * @return A List of AppointmentDtos.
   *
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/appointments")
  @Operation(
      summary = "Retrieves appointments",
      description = "Gets all appointments for the specific date or date range. Filtered for the "
          + "specific provider or resource. This endpoint accepts both a start and end "
          + "date. Results will be returned for appointments that start or end within "
          + "the specified range. If no end date is supplied then results will only include "
          + "appointments that started or ended on the start date.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client credential bearer token",
      in = ParameterIn.HEADER,
      required = true)
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments with a provider that the user has this "
          + "permission for.")
  public List<AppointmentDto> getAppointments(
      @Parameter(description = "Start date",
          example = "2018-10-01") @QueryParam("startDate") Calendar startDate,
      @Parameter(description = "End date",
          example = "2018-10-15") @QueryParam("endDate") Calendar endDate,
      @Parameter(description = "Provider id") @QueryParam("provider") Integer providerId,
      @Parameter(description = "Resource id") @QueryParam("resource") Integer resourceId)
      throws ProtossException {

    LocalDate localStartDate = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    int patientId = getPatientId();
    if (localStartDate != null && localEndDate != null && localStartDate.isAfter(localEndDate)) {
      LocalDate temp = localStartDate;
      localStartDate = localEndDate;
      localEndDate = temp;
    }
    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);

    List<Appointment> appointments = appointmentManager.getAppointments(
        localStartDate,
        localEndDate,
        patientId,
        providerId,
        resourceId, null);
    return mapDto(appointments, AppointmentDto.class, ArrayList::new);
  }

  /**
   * Get an Appointment DTO by ID.
   *
   * @param appointmentId Appointment ID.
   *
   * @return An Appointment DTO
   *
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/appointments/{appointmentId}")
  @Operation(
      summary = "Retrieves patient's appointment",
      description = "Gets the patient's appointment by the given appointment id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = AppointmentDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found")})
  @Parameter(
      name = "authorization",
      description = "Client credential bearer token",
      in = ParameterIn.HEADER,
      required = true)
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments with a provider that the user has this "
          + "permission for.")
  public AppointmentDto getAppointment(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId)
      throws ProtossException {

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    Appointment appointment = appointmentManager.getVisibleAppointmentById(appointmentId);

    if (appointment == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Appointment not found.");
    }
    int patientId = getPatientId();
    if (patientId != appointment.getPatientId()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Appointment not found.");
    }
    return mapDto(appointment, AppointmentDto.class);
  }

}
