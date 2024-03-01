
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.locks.ProtectionLockManager;
import com.qhrtech.emr.accuro.api.locks.ScheduleLockManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.api.provider.ProviderScheduleCalenderManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentManager;
import com.qhrtech.emr.accuro.api.scheduling.AvailabilityManager;
import com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.SiteManager;
import com.qhrtech.emr.accuro.api.scheduling.StatusManager;
import com.qhrtech.emr.accuro.api.scheduling.SuggestionManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.security.MaskException;
import com.qhrtech.emr.accuro.model.locks.ProtectionLock;
import com.qhrtech.emr.accuro.model.locks.ProtectionLockRequest;
import com.qhrtech.emr.accuro.model.locks.ProtectionType;
import com.qhrtech.emr.accuro.model.locks.ScheduleLock;
import com.qhrtech.emr.accuro.model.locks.ScheduleLockRequest;
import com.qhrtech.emr.accuro.model.provider.ProviderScheduleCalendar;
import com.qhrtech.emr.accuro.model.scheduling.Appointment;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentReason;
import com.qhrtech.emr.accuro.model.scheduling.BillingDetails;
import com.qhrtech.emr.accuro.model.scheduling.ScheduleSlot;
import com.qhrtech.emr.accuro.model.scheduling.Site;
import com.qhrtech.emr.accuro.model.scheduling.Status;
import com.qhrtech.emr.accuro.model.scheduling.availability.AppliedAvailability;
import com.qhrtech.emr.accuro.model.scheduling.suggestions.AppliedSuggestion;
import com.qhrtech.emr.accuro.model.scheduling.suggestions.ScheduleSuggestion;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AppliedAvailabilityDto;
import com.qhrtech.emr.restapi.models.dto.AppliedSuggestionDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentDto;
import com.qhrtech.emr.restapi.models.dto.AppointmentReasonDto;
import com.qhrtech.emr.restapi.models.dto.AvailabilityDto;
import com.qhrtech.emr.restapi.models.dto.AvailabilityTemplateDto;
import com.qhrtech.emr.restapi.models.dto.BillingDetailsDto;
import com.qhrtech.emr.restapi.models.dto.ProviderScheduleCalendarDto;
import com.qhrtech.emr.restapi.models.dto.ScheduleSlotDto;
import com.qhrtech.emr.restapi.models.dto.ScheduleSuggestionDto;
import com.qhrtech.emr.restapi.models.dto.SiteDto;
import com.qhrtech.emr.restapi.models.dto.StatusDto;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This <code>ScheduleEndpoint</code> collection is designed to expose the Appointment DTO and
 * related public endpoints. Requires patient level authorization.
 * </p>
 * <p>
 * Scheduler for provider portal applications.
 * </p>
 *
 * <p>
 * When create/update/cancel/delete the appointment wait room entry will be updated according to the
 * appointment as follow:
 * <ol>
 * <li>The wait room entry will be created if the appointment is created and the patient
 * arrives</li>
 * <li>The wait room entry will be created if the appointment is updated and the patient
 * arrives</li>
 * <li>The wait room entry will be deleted if the appointment is updated and the patient leave or
 * does not arrive</li>
 * <li>The wait room entry will be deleted if the appointment is cancelled or deleted</li>
 * </ol>
 * The appointment date must be today. If it is not the wait room entry it won't be created.
 * Currently, API doesn't support to create wait room entry for the future appointment as Accuro
 * does.
 * </p>
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager
 */
@Component("provider-portal-scheduler")
@Path("/v1/provider-portal/scheduler")
@Facet("provider-portal")
@Tag(name = "Schedule Endpoints - Provider",
    description = "Exposes schedule appointments. "
        + "Scheduler for provider portal applications.\n\n"
        + "When create/update/cancel/delete the appointment, "
        + "wait room entry will be updated according to the appointment as follow:\n"
        + "* The wait room entry will be created if the appointment is created "
        + "and the patient arrives.\n\n"
        + "* The wait room entry will be created if the appointment is updated "
        + "and the patient arrives.\n\n"
        + "* The wait room entry will be deleted if the appointment is updated "
        + "and the patient leave or does not arrive.\n\n"
        + "* The wait room entry will be deleted if the appointment is cancelled or deleted.\n\n"
        + "The appointment date must be today. "
        + "If it is not the wait room entry it won't be created.\n\n"
        + "Currently, API doesn't support to create wait room entry for the future appointment "
        + "as Accuro does.\n")
public class ScheduleEndpoint extends AbstractEndpoint {

  private static final long DEFAULT_LOCK_EXPIRY = TimeUnit.MINUTES.toSeconds(5);

  /**
   * Get all appointments for the specific date or date range. Filtered for the specific provider,
   * resource, patient or office.
   *
   * @param startDate Start date.
   * @param endDate End date.
   * @param providerId Provider id.
   * @param resourceId Resource id.
   * @param patientId Patient id.
   * @param officeId Office id.
   * @param accessionNumber The appointment accessionNumber. This is an identifier for the
   *        appointment resource. Null and blank accession numbers are not valid.
   * @return List of Appointment DTOs.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Bad request parameters
   * @HTTP 403 Forbidden with current authentication level
   */
  @GET
  @Path("/appointments")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointments",
      description = "Gets all appointments for the specific date or date range. "
          + "Filtered for the specific provider, resource, patient or office or accession number",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "Start date or patient id must be supplied"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "startDate",
              description = "Start Date - required if no patient id",
              required = true,
              in = ParameterIn.QUERY,
              example = "2018-10-01"),
          @Parameter(
              name = "endDate",
              description = "End Date",
              in = ParameterIn.QUERY,
              example = "2018-10-11"),
          @Parameter(
              name = "provider",
              description = "Provider id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "resource",
              description = "Resource id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "patient",
              description = "Patient id - required if no start date",
              required = true,
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "officeId",
              description = "Office id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              schema = @Schema(type = "String"),
              name = "accessionNumber",
              required = true,
              description = "The appointment accessionNumber. This is another "
                  + "identifier for the appointment resource. Null and blank accession numbers "
                  + "are not valid.",
              in = ParameterIn.QUERY)})
  public List<AppointmentDto> getAppointments(
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate,
      @Parameter(hidden = true) @QueryParam("provider") Integer providerId,
      @Parameter(hidden = true) @QueryParam("resource") Integer resourceId,
      @Parameter(hidden = true) @QueryParam("patient") Integer patientId,
      @Parameter(hidden = true) @QueryParam("officeId") Integer officeId,
      @Parameter(hidden = true) @QueryParam("accessionNumber") String accessionNumber)
      throws ProtossException {

    if (startDate == null && patientId == null && StringUtils.isBlank(accessionNumber)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Start date, patient id, or accession number must be supplied.");
    }

    LocalDate localStartDate = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    if (localStartDate != null && localEndDate != null && localStartDate.isAfter(localEndDate)) {
      LocalDate temp = localStartDate;
      localStartDate = localEndDate;
      localEndDate = temp;
    }

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    List<Appointment> appointmentList = appointmentManager.getAppointments(
        localStartDate,
        localEndDate,
        patientId,
        providerId,
        resourceId,
        StringUtils.isBlank(accessionNumber) ? null : accessionNumber);

    List<AppointmentDto> appointments =
        mapDto(appointmentList, AppointmentDto.class, ArrayList::new);

    // Filter by officeId if provided
    if (officeId != null) {
      appointments = appointments
          .stream()
          .filter(a -> officeId.equals(a.getOfficeId()))
          .collect(Collectors.toList());
    }

    return appointments;
  }

  /**
   * Get Appointment by ID.
   *
   * @param appointmentId Appointment ID
   * @return An Appointment DTO
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 Appointment not found
   */
  @GET
  @Path("/appointments/{appointmentId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves appointment",
      description = "Gets appointment by the given appointment id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = AppointmentDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public AppointmentDto getAppointment(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId)
      throws ProtossException {

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    Appointment appt = appointmentManager.getAppointmentById(appointmentId);

    if (appt == null) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND,
          "Appointment not found.");
    }
    return mapDto(appt, AppointmentDto.class);
  }

  /**
   * Create new appointment.
   *
   * @param appointment Appointment being created.
   * @param uriInfo Provides access to application and request URI info
   *
   *        The otherProviderIds field in Appointment details object is read-only and cannot be
   *        added.
   * @return Response with the appointment id and location of the newly created resource.
   * @throws SaveException An exception occurred during a save action
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @throws ResourceConflictException If there has been a schedule lock conflict error.
   * @HTTP 400 Unable to process json for this request
   * @HTTP 400 The provided start time must not come on or after the appointment's end time.
   * @HTTP 400 The start and end times must be a valid 24 hour time between 0000-2400 hours
   * @HTTP 401 The user is not authorized for the office
   * @HTTP 403 Schedule lock is required to create appointment
   * @HTTP 403 Appointment already exists in the selected time frame
   * @HTTP 409 If another client is editing the schedule in an overlapping location.
   */
  @POST
  @Path("/appointments")
  @PreAuthorize("#oauth2.hasScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @Operation(
      summary = "Saves appointment",
      description = "Creates new appointment.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns the appointment id"))),
          @ApiResponse(
              responseCode = "400",
              description = "The start and end times must be within 0000-2400 hours.\n\n"
                  + "The provided start time must not come on "
                  + "or after the appointment's end time.\n\n"
                  + "Start Time must be in valid 24 hour time.\n\n"
                  + "End Time must be in valid 24 hour time."),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Attempting to create an appointment in another office"),
          @ApiResponse(
              responseCode = "409",
              description = "If another client is editing the schedule in the location"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Consumes(MediaType.APPLICATION_JSON)
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
          description = "Allows access if the user has this permission for the appointment "
              + "provider")
  })
  public Response createAppointment(
      @RequestBody(description = "New Appointment") @Valid AppointmentDto appointment,
      @Context UriInfo uriInfo) throws ProtossException {

    validateUserId();

    UserAuthenticationManager userAuthenticationManager = getImpl(UserAuthenticationManager.class);
    Set<Integer> officeIds = userAuthenticationManager.getOfficeIds(getUser().getUserId());

    if (!officeIds.contains(appointment.getOfficeId())) {
      throw Error.webApplicationException(Response.Status.UNAUTHORIZED,
          "The user is not authorized for the office: " + appointment.getOfficeId());
    }

    // Create the appointment and get back the generated id
    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    Appointment protossAppointment = mapDto(appointment, Appointment.class);
    UUID createdScheduleLock = null;

    try {
      ScheduleSlot scheduleSlot = getScheduleSlot(protossAppointment);
      validateScheduleSlotTime(scheduleSlot);
      UUID existingScheduleLock = getUsableScheduleLock(scheduleSlot, getClientUuid());
      if (existingScheduleLock == null) {
        createdScheduleLock = createScheduleLockImpl(scheduleSlot);
      }

      int apptId =
          appointmentManager.createAppointment(protossAppointment, getClientUuid());

      // Build a uri representing the location of the new resource
      UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
      uriBuilder.path(Integer.toString(apptId));

      // Return a created response with the location and id as the body
      return Response.created(uriBuilder.build()).entity(apptId).build();
    } finally {
      if (createdScheduleLock != null) {
        releaseScheduleLock(createdScheduleLock);
      }
    }
  }

  /**
   * Update existing Appointment DTO.
   *
   * @param appointmentId Appointment ID
   * @param appointment New appointment information
   * @param httpRequest HttpServletRequest
   *
   *        The otherProviderIds field in Appointment details object is read-only and cannot be
   *        updated.
   * @return No content response if update is successful.
   * @throws SaveException An exception occurred during a save action
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @throws ResourceConflictException If there has been a protection lock conflict error.
   * @throws ResourceConflictException If there has been a schedule lock conflict error.
   * @HTTP 400 Unable to process json for this request
   * @HTTP 401 The user is not authorized for the office
   * @HTTP 403 Access forbidden without Schedule Lock
   * @HTTP 409 If another client is editing this appointment or its location on the schedule.
   */
  @PUT
  @Path("/appointments/{appointmentId}")
  @PreAuthorize(" #oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermissions(operation = LogicalOperation.AND, providerPermissions = {
      @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
          description = "Allows access if the user has this permission for the appointment "
              + "provider")
  })
  @Operation(
      summary = "Updates appointment",
      description = "Updates existing Appointment.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Updated successfully"),
          @ApiResponse(
              responseCode = "400",
              description = "The start and end times must be within 0000-2400 hours.\n\n"
                  + "The provided start time must not come on "
                  + "or after the appointment's end time.\n\n"
                  + "The provided appointment does not match the specified resource.\n\n"),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden without Schedule Lock.\n\n"
                  + "Access the masked information without the right permission."),
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found"),
          @ApiResponse(
              responseCode = "409",
              description = "If another client is editing this appointment "
                  + "or its location on the schedule"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateAppointment(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId,
      @RequestBody(description = "Updated appointment") @Valid AppointmentDto appointment,
      @Context HttpServletRequest httpRequest) throws ProtossException {

    UserAuthenticationManager userAuthenticationManager = getImpl(UserAuthenticationManager.class);

    validateUserId();

    Set<Integer> officeIds = userAuthenticationManager.getOfficeIds(getUser().getUserId());

    if (!officeIds.contains(appointment.getOfficeId())) {
      throw Error.webApplicationException(Response.Status.UNAUTHORIZED,
          "The user is not authorized for the office: " + appointment.getOfficeId());
    }

    AppointmentManager managerRead = getImpl(AppointmentManager.class, true);
    Appointment existingAppointment =
        managerRead.getAppointmentById(appointmentId);

    if (existingAppointment == null) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND,
          "Appointment not found.");
    }

    // Check if the office of existing appt is allowed
    if (!officeIds.contains(existingAppointment.getOfficeId())) {
      throw Error.webApplicationException(Response.Status.UNAUTHORIZED,
          "The user is not authorized for the office " + existingAppointment.getOfficeId()
              + " of the existing appointment.");
    } else if (appointmentId != appointment.getAppointmentId()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "The provided appointment does not match the specified resource.");
    }

    Appointment updatedAppointment = mapDto(appointment, Appointment.class);

    // v1 api model does not contain site id or group appointment id at this time
    updatedAppointment.setSiteId(existingAppointment.getSiteId());
    updatedAppointment.setGroupAppointmentId(existingAppointment.getGroupAppointmentId());

    UUID createdScheduleLock = null;
    UUID createdProtectionLock = null;
    ScheduleSlot scheduleSlot = getScheduleSlot(updatedAppointment);
    validateScheduleSlotTime(scheduleSlot);
    try {
      // see if there are existing locks we can use
      UUID existingScheduleLock = getUsableScheduleLock(scheduleSlot, getClientUuid());
      UUID existingProtectionLock = getUsableProtectionLock(appointmentId);

      // if one or both of the locks are not already present, we will created them for the client
      if (existingScheduleLock == null) {
        createdScheduleLock = createScheduleLockImpl(scheduleSlot);
      }
      if (existingProtectionLock == null) {
        createdProtectionLock =
            createProtectionLockImpl(appointmentId, httpRequest.getRemoteHost());
      }

      AppointmentManager appointmentManager = getImpl(AppointmentManager.class);

      appointmentManager.updateAppointment(updatedAppointment, getClientUuid());
      return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON_TYPE)
          .build();
    } finally {
      // We dont want to delete locks created by the client, so we only release what we created
      if (createdScheduleLock != null) {
        releaseScheduleLock(createdScheduleLock);
      }
      if (createdProtectionLock != null) {
        releaseProtectionLock(appointmentId);
      }
    }
  }

  /**
   * Delete or cancel an appointment.
   *
   * @param appointmentId Appointment id
   * @param cancel If this request is to cancel or delete the appointment
   * @param cancellationReason Reason for canceling the appointment, ignored for delete
   * @param httpRequest HttpServletRequest
   * @return 204 No Content
   * @throws SaveException An exception occurred during a save action
   * @throws MaskException unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @throws ResourceConflictException If there has been a protection lock conflict error.
   * @HTTP 401 The user is not authorized for the office
   * @HTTP 403 Access forbidden without Protection Lock
   * @HTTP 404 Appointment not found
   * @HTTP 409 If another client is editing this appointment.
   */
  @DELETE
  @Path("/appointments/{appointmentId}")
  @PreAuthorize(" #oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the appointment provider.")
  @FeaturePermission(type = FeatureType.SCHEDULING_DELETE_APPOINTMENT,
      description = " - Is required to delete an appointment")
  @Operation(
      summary = "Deletes appointment",
      description = "Deletes or cancels the appointment.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Deleted successfully"),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden without Protection Lock.\n\n"
                  + "Access the masked information without the right permission."),
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found"),
          @ApiResponse(
              responseCode = "409",
              description = "If another client is editing this appointment"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "appointmentId",
              description = "Appointment id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "cancel",
              description = "Cancel appointment and do not delete",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "boolean")),
          @Parameter(
              name = "cancellationReason",
              description = "Reason for cancellation",
              in = ParameterIn.QUERY)})
  public Response deleteAppointment(
      @Parameter(hidden = true) @PathParam("appointmentId") Integer appointmentId,
      @Parameter(hidden = true) @QueryParam("cancel") Boolean cancel,
      @Parameter(hidden = true) @QueryParam("cancellationReason") String cancellationReason,
      @Context HttpServletRequest httpRequest) throws ProtossException {

    validateUserId();

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);

    UUID createdProtectionLock = null;
    try {

      // see if there is an existing lock we can use
      UUID existingProtectionLock = getUsableProtectionLock(appointmentId);

      // if the lock is not already present, we will created it for the client
      if (existingProtectionLock == null) {
        createdProtectionLock =
            createProtectionLockImpl(appointmentId, httpRequest.getRemoteHost());
      }

      if (Boolean.TRUE.equals(cancel)) {
        AccuroPreferenceManager preferenceManager = getImpl(AccuroPreferenceManager.class);
        String timezone = preferenceManager.getSystemPreference("TimeZone");
        if (timezone == null) {
          throw new TimeZoneNotFoundException("Timezone not set in Accuro.");
        }
        DateTimeZone dateTimeZone = DateTimeZone.forID(timezone);
        DateTime cancelDate = DateTime.now(dateTimeZone);
        appointmentManager.cancelAppointment(
            appointmentId,
            getClientUuid(),
            cancellationReason,
            cancelDate);
      } else {
        appointmentManager.deleteAppointment(appointmentId, getClientUuid());
      }
      return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON_TYPE)
          .build();
    } finally {
      // We dont want to delete locks created by the client, so we only release what we created
      if (createdProtectionLock != null) {
        releaseProtectionLock(appointmentId);
      }
    }
  }

  /**
   * Retrieve billing details for multiple appointments.
   *
   * @param appointmentIds List of appointment IDs
   * @return A map of appointment billing details.
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 No appointment ids have been supplied
   */
  @POST
  @Path("/appointments/billing_details")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Billing, level = AccessLevel.ReadOnly,
      description = "Allows access to billing details if the user has this permission for the "
          + "appointment provider.")
  @Operation(
      summary = "Retrieves billing details for appointments",
      description = "Gets billing details for multiple appointments.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(type = "object",
                      example = "{ { 1 : { \"noCharge\" : false, \"noShow\" : true,"
                          + " \"insurerId\" : 14 } } }",
                      description = "Map of appointment id and BillingDetailsDto"))),
          @ApiResponse(
              responseCode = "400",
              description = "No appointment ids have been supplied"),
          @ApiResponse(
              responseCode = "403",
              description = "Access the masked information without the right permission"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<Integer, BillingDetailsDto> getBillingDetails(
      @RequestBody(description = "Appointment ids",
          required = true) Collection<Integer> appointmentIds)
      throws ProtossException {

    if (appointmentIds == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No appointment ids have been supplied.");
    }

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    Map<Integer, BillingDetails> appointmentBillingDetails =
        appointmentManager.getBillingDetails(appointmentIds);
    return mapDto(appointmentBillingDetails, BillingDetailsDto.class, HashMap::new);
  }

  /**
   * Get billing details for a specific appointment.
   *
   * @param appointmentId Appointment ID
   * @return Billing information for a specific appointment.
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/appointments/{appointmentId}/billing_details")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Billing, level = AccessLevel.ReadOnly,
      description = "Allows access to billing details if the user has this permission for the "
          + "appointment provider.")
  @Operation(
      summary = "Retrieves billing details for appointment",
      description = "Gets billing details for the specific appointment.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = BillingDetailsDto.class))),
          @ApiResponse(
              responseCode = "403",
              description = "Accesses the masked information without the right permission"),
          @ApiResponse(
              responseCode = "404",
              description = "Appointment not found"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public BillingDetailsDto getBillingDetails(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId)
      throws ProtossException {

    AppointmentManager appointmentManager = getImpl(AppointmentManager.class);
    BillingDetails billingDetails = appointmentManager.getBillingDetails(appointmentId);
    if (billingDetails == null) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND,
          "Billing details not found.");
    }
    return mapDto(billingDetails, BillingDetailsDto.class);
  }

  /**
   * Attempt to obtain a protection lock for a specific appointment.
   *
   * @param appointmentId Appointment ID
   * @param httpRequest Request information for the HTTP servlet
   * @return 204 No Content if successful, or else returns a 403 Forbidden status
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @throws ResourceConflictException If there has been a protection lock conflict
   * @HTTP 404 Appointment not found
   * @HTTP 403 Attempting to lock an appointment in another office
   * @HTTP 409 If another client is editing this appointment.
   */
  @POST
  @Path("/appointments/{appointmentId}/lock")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @Operation(
      summary = "Saves protection lock",
      description = "Creates the protection lock for the specific appointment.",
      deprecated = true,
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "403",
              description = "Attempting to lock an appointment in another office.\n\n"
                  + "Access the masked information without the right permission."),
          @ApiResponse(
              responseCode = "409",
              description = "If another client is editing this appointment"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createProtectionLock(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId,
      @Context HttpServletRequest httpRequest) throws ProtossException {

    validateUserId();

    // Request a protection lock
    UUID lockUuid = createProtectionLockImpl(appointmentId, httpRequest.getRemoteHost());

    // If request fails check for an existing protection lock for this appointment
    if (lockUuid == null) {
      ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
      ProtectionLock lock = lockManager.getLockByItemId(ProtectionType.Appointment, appointmentId);

      /*
       * It is possible for protection lock to be null due to another user releasing the lock
       * between attempting to create and retrieve or due to an internal server error.
       *
       * We don't currently have a way to know so return a forbidden with.
       */
      if (lock == null) {
        throw Error.webApplicationException(Response.Status.FORBIDDEN,
            "Unable to obtain protection lock.");
      }

      // Return lock preventing the creation of this protection lock
      return Response.status(Response.Status.FORBIDDEN)
          .entity(lock)
          .type(MediaType.APPLICATION_JSON_TYPE)
          .build();
    }
    return Response.noContent().build();
  }

  /**
   * Release a protection lock for a specific appointment.
   *
   * @param appointmentId Appointment ID
   * @return 204 No Content if successful, or else returns a 403 Forbidden status
   * @throws DataAccessException If there has been a database error.
   * @HTTP 403 Unable to delete protection lock.
   */
  @DELETE
  @Path("/appointments/{appointmentId}/lock")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @Operation(
      summary = "Deletes protection lock",
      description = "Releases the protection lock for the specific appointment.",
      deprecated = true,
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response deleteProtectionLock(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId)
      throws ProtossException {
    validateUserId();
    releaseProtectionLock(appointmentId);
    return Response.noContent().build();
  }

  /**
   * Get statuses for an appointment.
   *
   * @param appointmentId Appointment ID
   * @return A set of status IDs
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/appointments/{appointmentId}/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments if the"
          + " user has this permission for appointment provider.")
  @Operation(
      summary = "Retrieves appointment statuses",
      description = "Gets statuses for the appointment.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          type = "integer",
                          example = "1",
                          description = "Returns Status(s)")))),
          @ApiResponse(
              responseCode = "403",
              description = "Access the masked information without the right permission"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<Integer> getAppointmentStatuses(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId)
      throws ProtossException {

    StatusManager statusManager = getImpl(StatusManager.class);
    return statusManager.getStatusesByAppointmentId(appointmentId, getUser());
  }

  /**
   * Adds a Status to an Appointment.
   *
   * @param appointmentId Appointment ID
   * @param statusId Status to add to the Appointment
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Status does not exist
   */
  @PUT
  @Path("/appointments/{appointmentId}/statuses/{statusId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Updates appointment status",
      description = "Adds the status to the Appointment.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if operation is successful"))),
          @ApiResponse(
              responseCode = "400",
              description = "Unable to add status"),
          @ApiResponse(
              responseCode = "403",
              description = "Access the masked information without the right permission"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public boolean addAppointmentStatus(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId,
      @Parameter(description = "Status id") @PathParam("statusId") Integer statusId)
      throws ProtossException {

    StatusManager statusManager = getImpl(StatusManager.class);
    statusManager.addStatus(appointmentId, statusId, getUser());

    return true;
  }

  /**
   * Removes a Status from an Appointment.
   *
   * @param appointmentId Appointment ID
   * @param statusId Status to delete from the Appointment
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/appointments/{appointmentId}/statuses/{statusId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Deletes appointment status",
      description = "Removes the status from the appointment.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if operation is successful"))),
          @ApiResponse(
              responseCode = "403",
              description = "Access the masked information without the right permission"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public boolean removeAppointmentStatus(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") Integer appointmentId,
      @Parameter(description = "Status id") @PathParam("statusId") Integer statusId)
      throws ProtossException {

    StatusManager statusManager = getImpl(StatusManager.class);
    statusManager.removeStatus(appointmentId, statusId, getUser());
    return true;
  }

  /**
   * Retrieve Schedule Suggestions that have been applied in the scheduler. For startDate and
   * endDate, only date part is considered. If time and time zone passed, they will be ignored.
   *
   * @param startDate Start date to filter
   * @param endDate End date to filter
   * @param providerId Provider ID to filter
   * @param resourceId Resource ID to filter
   * @param subColumn SubColumn to filter
   * @return A Map of Calendar to AppliedSuggestionTemplate.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Valid start date and end date are required
   */
  @GET
  @Path("/applied-suggestions")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves applied suggestions",
      description = "Gets schedule suggestions that have been applied in the scheduler."
          + "For startDate and endDate, only date part is considered. If time and time zone"
          + " passed, they will be ignored.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AppliedSuggestionDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "startDate and endDate are required")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "startDate",
              description = "Start date. Only date part is considered",
              required = true,
              in = ParameterIn.QUERY),
          @Parameter(
              name = "endDate",
              description = "End date. Only date part is considered",
              required = true,
              in = ParameterIn.QUERY),
          @Parameter(
              name = "provider",
              description = "Provider id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "resource",
              description = "Resource id",
              deprecated = true,
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "subColumn",
              description = "Sub column",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer"))})
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to applied suggestions for providers the user has this "
          + "permission for.")
  public Set<AppliedSuggestionDto> getAppliedSuggestions(
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate,
      @Parameter(hidden = true) @QueryParam("provider") Integer providerId,
      @Parameter(hidden = true) @QueryParam("resource") Integer resourceId,
      @Parameter(hidden = true) @QueryParam("subColumn") Integer subColumn)
      throws ProtossException {

    // Start and end date are required.
    if (startDate == null || endDate == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "startDate and endDate are required.");
    }
    if (startDate.after(endDate)) {
      Calendar temp = startDate;
      startDate = endDate;
      endDate = temp;
    }

    TimeUtil.startOf(startDate, Calendar.DAY_OF_MONTH);
    TimeUtil.endOf(endDate, Calendar.DAY_OF_MONTH);

    Collection<Integer> providerIds =
        providerId == null ? null : Collections.singletonList(providerId);
    Collection<Integer> resourceids =
        resourceId == null ? null : Collections.singletonList(resourceId);

    LocalDate localStartDate = LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = LocalDate.fromCalendarFields(endDate);
    SuggestionManager suggestionManager = getImpl(SuggestionManager.class);
    Set<AppliedSuggestion> appliedSuggestions = suggestionManager.getAppliedSuggestions(
        localStartDate,
        localEndDate,
        providerIds,
        resourceids,
        subColumn);

    return mapDto(appliedSuggestions, AppliedSuggestionDto.class, HashSet::new);
  }

  /**
   * Retrieve all Schedule Suggestions.
   *
   * @return Collection of schedule suggestions
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/suggestions")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all suggestions",
      description = "Gets all schedule suggestions.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ScheduleSuggestionDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ScheduleSuggestionDto> getAllSuggestions() throws ProtossException {
    SuggestionManager suggestionManager = getImpl(SuggestionManager.class);
    Set<ScheduleSuggestion> suggestions = suggestionManager.getSuggestions();
    return mapDto(suggestions, ScheduleSuggestionDto.class, HashSet::new);
  }

  /**
   * Update appointment reason.
   *
   * @param reasonId Reason ID
   * @param reason New Representation
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/reasons/{reasonId}")
  @PreAuthorize("#oauth2.hasScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the provider associated "
          + "with the appointment reason.")
  @Operation(
      summary = "Updates appointment reason",
      description = "Updates appointment reason.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(type = "boolean", example = "true",
                      description = "Returns true if operation is successful"))),
          @ApiResponse(
              responseCode = "400",
              description = "Reason id does not match path")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public boolean updateReason(
      @Parameter(description = "Reason id") @PathParam("reasonId") int reasonId,
      @RequestBody(description = "Updated appointment reason") AppointmentReasonDto reason)
      throws ProtossException {

    if (reasonId != reason.getReasonId()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Reason id does not match path.");
    }

    ScheduleSettingsManager settings = getImpl(ScheduleSettingsManager.class);
    AppointmentReason apptReason = mapDto(reason, AppointmentReason.class);
    settings.updateReason(apptReason);
    return true;
  }

  /**
   * Creates an Appointment Status.
   *
   * @param status Status to create
   * @return Status ID if successful
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @Operation(
      summary = "Creates appointment status",
      description = "Creates the appointment status.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(type = "integer", example = "1001",
                      description = "Returns id"))),
          @ApiResponse(
              responseCode = "400",
              description = "Status name can not be null.\n\n"
                  + "Status abbreviation can not be null.\n\n"
                  + "Name must be at most 50 characters.\n\n"
                  + "Abbreviation must be at most 2 characters.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createStatus(
      @RequestBody(description = "New appointment status") @Valid StatusDto status)
      throws ProtossException {

    StatusManager statusManager = getImpl(StatusManager.class);
    Status apptStatus = mapDto(status, Status.class);
    return statusManager.createStatus(apptStatus);
  }

  /**
   * Updates an Appointment Status.
   *
   * @param statusId Status being updated
   * @param status New contents of the status
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Name must be at most 50 characters
   * @HTTP 400 Abbreviation must be at most 2 characters
   * @HTTP 400 The provided Status does not match the specified resource.
   */
  @PUT
  @Path("/statuses/{statusId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @Operation(
      summary = "Updates appointment status",
      description = "Updates the appointment status.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "boolean",
                      example = "true",
                      description = "Returns true if update is successful"))),
          @ApiResponse(
              responseCode = "400",
              description = "Status name can not be null.\n\n"
                  + "Status abbreviation can not be null.\n\n"
                  + "Name must be at most 50 characters.\n\n"
                  + "Abbreviation must be at most 2 characters.\n\n"
                  + "The provided Status does not match the specified resource.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public boolean updateStatus(
      @Parameter(description = "Status id") @PathParam("statusId") Integer statusId,
      @RequestBody(description = "Updated status") @Valid StatusDto status)
      throws ProtossException {

    if (statusId != status.getStatusId()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "The provided Status does not match the specified resource.");
    }
    StatusManager statusManager = getImpl(StatusManager.class);
    Status apptStatus = mapDto(status, Status.class);
    statusManager.updateStatus(apptStatus);
    return true;
  }

  /**
   * Retrieve statuses applied to multiple appointments.
   *
   * @param appointmentIds List of appointment IDs
   * @return A map of arrays of status IDs to appointment IDs.
   * @throws MaskException An unauthorized user attempted to mutate a masked appointment
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to the appointment statuses the "
          + "user has this permission for the providers of the "
          +
          "appointments.")
  @Operation(
      summary = "Retrieves statuses by appointment ids",
      description = "Gets statuses applied to multiple appointments.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(type = "object",
                      example = "{ 1 : { 1, 3, 4, 5 }, 2 : { 2, 3, 4 } }",
                      description = "The map of appointment ids to array of status(s)"))),
          @ApiResponse(
              responseCode = "400",
              description = "No appointment ids have been supplied"),
          @ApiResponse(
              responseCode = "403",
              description = "Access the masked information without the right permission"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public Map<Integer, Set<Integer>> getStatusesByAppointmentIds(
      @RequestBody(description = "Appointment ids") Collection<Integer> appointmentIds)
      throws ProtossException {

    if (appointmentIds == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No appointment ids have been supplied.");
    }
    StatusManager statusManager = getImpl(StatusManager.class);
    return statusManager.getStatusesByAppointmentIds(appointmentIds, getUser());
  }

  /**
   * Retrieve all Sites.
   *
   * @return A list of all sites
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/sites")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all sites",
      description = "Gets all sites.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = SiteDto.class))))})

  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access to sites "
          + "associated with an office the user has this permission for.")
  public List<SiteDto> getSites() throws ProtossException {

    SiteManager siteManager = getImpl(SiteManager.class);
    List<Site> sites = siteManager.getSites();
    return mapDto(sites, SiteDto.class, ArrayList::new);
  }

  /**
   * Retrieve all availability templates.
   *
   * @return A set of all Availability Template objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/availability-templates")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves availability templates",
      description = "Gets all availability templates.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = AvailabilityTemplateDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<AvailabilityTemplateDto> getAvailabilityTemplates() throws ProtossException {

    AvailabilityManager availabilityManager = getImpl(AvailabilityManager.class);
    return mapDto(availabilityManager.getTemplates(), AvailabilityTemplateDto.class, HashSet::new);
  }

  /**
   * Retrieve a specific availability template by ID.
   *
   * @param templateId Template ID
   * @return a set of all Availability objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/availability-templates/{templateId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves template availability",
      description = "Gets the specific availability template by the given template id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = AvailabilityDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "No Template id specified")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<AvailabilityDto> getTemplateAvailabilities(
      @Parameter(description = "Template id") @PathParam("templateId") Integer templateId)
      throws ProtossException {

    if (templateId == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No Template ID specified.");
    }
    AvailabilityManager availabilityManager = getImpl(AvailabilityManager.class);
    return mapDto(
        availabilityManager.getAvailabilities(templateId), AvailabilityDto.class, HashSet::new);
  }

  /**
   * Retrieve Availabilities applied to the scheduler.
   *
   * @param providerId Provider Id
   * @param startDate Start Date
   * @param endDate End Date
   * @return A set of all Applied Availability objects
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 No Provider ID specified
   * @HTTP 400 No Start Date specified
   * @HTTP 404 Invalid request parameters
   */
  @GET
  @Path("/applied-availabilities")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves applied availabilities",
      description = "Gets availabilities applied to the scheduler.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AppliedAvailabilityDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "No provider id or start date specified")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "providerId",
              description = "Provider id",
              required = true,
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "startDate",
              description = "Start date",
              required = true,
              in = ParameterIn.QUERY),
          @Parameter(
              name = "endDate",
              description = "End date",
              in = ParameterIn.QUERY)})
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to "
          + "availabilities for providers the user has this permission for.")
  public Set<AppliedAvailabilityDto> getAppliedAvailabilities(
      @Parameter(hidden = true) @QueryParam("providerId") Integer providerId,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate) throws ProtossException {

    if (providerId == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No Provider ID specified.");
    } else if (startDate == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No Start Date specified.");
    }
    TimeUtil.startOf(startDate, Calendar.DAY_OF_MONTH);
    if (endDate != null) {
      TimeUtil.endOf(endDate, Calendar.DAY_OF_MONTH);
    }
    AvailabilityManager availabilityManager = getImpl(AvailabilityManager.class);
    Set<AppliedAvailability> availability =
        availabilityManager.getAppliedAvailabilities(providerId,
            LocalDate.fromCalendarFields(startDate), LocalDate.fromCalendarFields(endDate));
    return mapDto(availability, AppliedAvailabilityDto.class, HashSet::new);
  }

  /**
   * Request Schedule Protection Lock.
   *
   * @param slot Time slot of the schedule the lock is to cover
   * @param httpRequest Raw Http request from the client.
   * @return 200 OK if successful, or else returns a 403 Forbidden status
   * @throws DataAccessException If there has been a database error.
   * @throws ResourceConflictException If there has been a schedule lock conflict error.
   * @HTTP 400 Unable to process JSON for this request.
   * @HTTP 400 A Provider ID or Resource ID must beset.
   * @HTTP 409 If another client is editing an overlapping location on the schedule.
   */
  @POST
  @Path("/lock")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Saves schedule lock",
      description = "Requests schedule protection lock.",
      deprecated = true,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "object",
                      description = "The object of UUID.",
                      example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"))),
          @ApiResponse(
              responseCode = "400",
              description = "A provider or resource must be specified to lock"),
          @ApiResponse(
              responseCode = "409",
              description = "If another client is editing the location on the schedule")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createScheduleLock(
      @RequestBody(description = "Schedule slot for lock") ScheduleSlotDto slot,
      @Context HttpServletRequest httpRequest) throws ProtossException {

    validateUserId();

    if (slot.getProviderId() == null && slot.getResourceId() == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "A Provider or Resource must be specified to lock");
    }
    ScheduleSlot scheduleSlot = mapDto(slot, ScheduleSlot.class);
    UUID uuid = createScheduleLockImpl(scheduleSlot);

    // If request fails check for an existing schedule lock for this slot
    if (uuid == null) {
      ScheduleLockManager scheduleLockManager = getImpl(ScheduleLockManager.class);
      Set<ScheduleLock> locks = scheduleLockManager.getLockConflicts(scheduleSlot);

      /*
       * It is possible for the schedule lock to be null due to another user releasing the lock
       * between attempting to create and retrieve or due to an internal server error.
       *
       * We don't currently have a way to know so return a forbidden with.
       */
      if (locks.isEmpty()) {
        throw Error.webApplicationException(Response.Status.FORBIDDEN,
            "Unable to obtain schedule lock.");
      }

      // Return lock preventing the creation of this schedule lock
      return Response.status(Response.Status.FORBIDDEN)
          .entity(locks)
          .type(MediaType.APPLICATION_JSON_TYPE)
          .build();
    }
    return Response.ok(uuid).build();
  }

  /**
   * Release Schedule Protection Lock.
   *
   * @param lockUuid Lock UUID
   * @return 204 No Content if successful, or else returns a 403 Forbidden status
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Lock ID must be specified
   * @HTTP 403 Unable to delete schedule lock
   */
  @DELETE
  @Path("/lock")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Deletes schedule lock",
      description = "Releases schedule protection lock.",
      deprecated = true,
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "Lock id must be specified.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response deleteScheduleLock(@RequestBody(description = "Lock UUID") UUID lockUuid)
      throws ProtossException {
    if (lockUuid == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Lock Id must be specified.");
    }
    validateUserId();
    releaseScheduleLock(lockUuid);
    return Response.noContent().build();
  }

  /**
   * Create a schedule slot object from an appointment object.
   *
   * @param appointment Appointment object
   * @return Schedule slot
   */
  private ScheduleSlot getScheduleSlot(Appointment appointment) {
    return new ScheduleSlot(
        appointment.getDate().toDateTimeAtStartOfDay().toCalendar(Locale.CANADA),
        appointment.getSubColumn(),
        appointment.getStartTime(),
        appointment.getEndTime(),
        appointment.getProviderId(),
        appointment.getResourceId());
  }

  /**
   * Retrives a Set of provider schedule calendar notes based on given providerId and date range.
   * Note: The maximum date range allowed is 1 year.
   *
   * @param providerId Unique provider id
   * @param startDate start date
   * @param endDate end date
   * @return Set of provider schedule calendar notes {@link ProviderScheduleCalendarDto}
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/providers/{providerId}/calendar-notes")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has this permission for the provider.")
  @Operation(
      summary = "Retrieves provider calendar notes for the specified date range.",
      description = "Gets all the calendar notes for the specified provider within the given date "
          + "range. Start date is required. If there is no end date, record for only start date "
          + "will be returned. Note: The maximum date range allowed is 1 year.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ProviderScheduleCalendarDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "Start date not specified")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              required = true,
              in = ParameterIn.HEADER),
          @Parameter(
              name = "providerId",
              description = "Provider id",
              required = true,
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "startDate",
              description = "Start date",
              required = true,
              in = ParameterIn.QUERY),
          @Parameter(
              name = "endDate",
              description = "End date",
              in = ParameterIn.QUERY)})
  public List<ProviderScheduleCalendarDto> getProviderCalendarNote(
      @Parameter(hidden = true) @PathParam("providerId") int providerId,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate) throws ProtossException {

    LocalDate start = null;
    LocalDate end = null;
    if (startDate != null) {
      start = LocalDate.fromCalendarFields(startDate);
    }

    if (endDate != null) {
      end = LocalDate.fromCalendarFields(endDate);
    }

    if (start != null && end != null) {
      int months = Months.monthsBetween(start, end).getMonths();
      if (months > 12 || months < -12) {
        throw Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Maximum up to 1 year allowed.");
      }
    }

    ProviderScheduleCalenderManager scheduleCalenderManager =
        getImpl(ProviderScheduleCalenderManager.class);

    List<ProviderScheduleCalendar> scheduleCalendarList =
        scheduleCalenderManager.getForProvider(providerId, start, end);

    // Filter the result set if both note and title are empty.
    List<ProviderScheduleCalendar> filteredResult = scheduleCalendarList.stream()
        .filter(t -> (StringUtils.isNotBlank(t.getNote()) || StringUtils.isNotBlank(t.getTitle())))
        .collect(Collectors.toList());

    return mapDto(filteredResult, ProviderScheduleCalendarDto.class, ArrayList::new);

  }

  /**
   * Deletes a provider calendar note belonging to given provider id and date.
   *
   * @param providerId Unique provider id
   * @throws DataAccessException If there has been a database error.
   */
  @DELETE
  @Path("/providers/{providerId}/calendar-notes")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the provider.")
  @Operation(
      summary = "Deletes provider calendar note for the specified date.",
      description = "Deletes provider calendar note for the specified date.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid provider or date not specified.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              required = true,
              in = ParameterIn.HEADER),
          @Parameter(
              name = "providerId",
              description = "Provider id",
              required = true,
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "date",
              description = "Date for which calendar note is to be deleted.",
              required = true,
              in = ParameterIn.QUERY)})
  public Response deleteProviderCalendarNote(
      @Parameter(hidden = true) @PathParam("providerId") int providerId,
      @Parameter(hidden = true) @QueryParam("date") Calendar date) throws ProtossException {

    if (date == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "No date specified.");
    }
    ProviderScheduleCalenderManager managerRead =
        getImpl(ProviderScheduleCalenderManager.class, true);

    List<ProviderScheduleCalendar> calendarNotes = managerRead
        .getForProvider(providerId, LocalDate.fromCalendarFields(date), null);

    if (calendarNotes.isEmpty()) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND,
          "Record not found for deletion..");
    } else {
      ProviderScheduleCalenderManager scheduleCalenderManager =
          getImpl(ProviderScheduleCalenderManager.class);

      ProviderScheduleCalendar providerScheduleCalendar = calendarNotes.get(0);
      // Delete only if there is calendar note present with actual note and title.
      if (StringUtils.isNotBlank(providerScheduleCalendar.getTitle())
          || StringUtils.isNotBlank(providerScheduleCalendar.getNote())) {
        scheduleCalenderManager.delete(providerId, LocalDate.fromCalendarFields(date));
      } else {
        throw Error.webApplicationException(Response.Status.NOT_FOUND,
            "Record not found for deletion.");
      }
    }
    return Response.ok().build();
  }

  /**
   * Creates a provider calendar note {@link ProviderScheduleCalendarDto} belonging to given
   * provider id and date.
   *
   * @param providerId Unique provider id
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @Path("/providers/{providerId}/calendar-notes")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the provider.")
  @Operation(
      summary = "Creates a provider calendar note",
      description = "Creates a provider calendar note. "
          + "If the given date already has calendar note, it will be updated.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Missing or invalid required fields or calendar note already exists"),
          @ApiResponse(
              responseCode = "200",
              description = "Success")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @RequestBody(
      description = "Provider schedule calendar",
      content = @Content(schema = @Schema(implementation = ProviderScheduleCalendarDto.class)))
  public Response createProviderCalendarNote(
      @RequestBody @Valid @NotNull ProviderScheduleCalendarDto scheduleCalDto,
      @Parameter(description = "Provider Id") @PathParam("providerId") int providerId)
      throws ProtossException {

    if (providerId != scheduleCalDto.getProviderId()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Provider id in path does not match with the body.");
    }

    validateCalendarNote(scheduleCalDto);

    ProviderScheduleCalenderManager managerRead =
        getImpl(ProviderScheduleCalenderManager.class, true);

    // Find if record already exist with blank note and title
    List<ProviderScheduleCalendar> providerCal = managerRead
        .getForProvider(scheduleCalDto.getProviderId(), scheduleCalDto.getDate(),
            null);

    ProviderScheduleCalenderManager scheduleCalenderManager =
        getImpl(ProviderScheduleCalenderManager.class);
    if (providerCal.isEmpty()) {
      scheduleCalenderManager.create(mapDto(scheduleCalDto, ProviderScheduleCalendar.class));
    } else {
      // update
      if (StringUtils.isBlank(providerCal.get(0).getNote())
          && StringUtils.isBlank(providerCal.get(0).getTitle())) {
        scheduleCalenderManager.update(mapDto(scheduleCalDto, ProviderScheduleCalendar.class));
      } else {
        throw Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Provider calendar note for the given date already exists.");
      }

    }

    return Response.ok().build();
  }

  /**
   * Updates a provider calendar note {@link ProviderScheduleCalendarDto} belonging to given
   * provider id and date.
   *
   * @param providerId Unique provider id
   * @throws DataAccessException If there has been a database error.
   */
  @PUT
  @Path("/providers/{providerId}/calendar-notes")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE') "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for the provider.")
  @Operation(
      summary = "Updates a provider calendar note",
      description = "Updates a provider calendar note.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Missing or invalid required fields"),
          @ApiResponse(
              responseCode = "200",
              description = "Success"),
          @ApiResponse(
              responseCode = "404",
              description = "Calendar note not found for the given date.")
      })
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @RequestBody(
      description = "Provider schedule calendar",
      content = @Content(schema = @Schema(implementation = ProviderScheduleCalendarDto.class)))
  public Response updateProviderCalendarNote(
      @RequestBody @Valid @NotNull ProviderScheduleCalendarDto scheduleCalDto,
      @Parameter(description = "Provider Id") @PathParam("providerId") int providerId)
      throws ProtossException {

    if (providerId != scheduleCalDto.getProviderId()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Provider id in path does not match with the body.");
    }

    validateCalendarNote(scheduleCalDto);

    ProviderScheduleCalenderManager scheduleCalenderManager =
        getImpl(ProviderScheduleCalenderManager.class);

    scheduleCalenderManager.update(mapDto(scheduleCalDto, ProviderScheduleCalendar.class));

    return Response.ok().build();
  }

  private void validateCalendarNote(ProviderScheduleCalendarDto scheduleCalDto) {
    if (StringUtils.isBlank(scheduleCalDto.getNote())
        && StringUtils.isBlank(scheduleCalDto.getTitle())) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Either of the note or title should be a valid string");
    }
  }


  private UUID createProtectionLockImpl(int appointmentId, String machine)
      throws ProtossException {
    ProtectionLockRequest.Builder builder = new ProtectionLockRequest.Builder();
    builder.setUser(getUser());
    builder.setMachine(machine);
    builder.setClientUUID(getClientUuid());
    builder.setType(ProtectionType.Appointment);
    builder.setLockId(appointmentId);
    builder.setDuration((int) DEFAULT_LOCK_EXPIRY);
    ProtectionLockRequest request = builder.build();

    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    return lockManager.createLock(request);
  }

  private UUID getUsableProtectionLock(int appointmentId) throws ProtossException {
    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    ProtectionLock lock = lockManager.getLockByItemId(ProtectionType.Appointment, appointmentId);
    if (lock != null && Objects.equals(lock.getClientUUID(), getClientUuid())) {
      return lock.getUuid();
    }
    return null;
  }

  /**
   * Delete appointment protect lock.
   *
   * @param appointmentId Appointment id
   */
  private void releaseProtectionLock(Integer appointmentId) throws ProtossException {
    ProtectionLockManager lockManager = getImpl(ProtectionLockManager.class);
    lockManager.releaseLock(ProtectionType.Appointment, appointmentId, getClientUuid());
  }

  /**
   * Attempts to create lock for a particular section of the scheduler.
   *
   * @param scheduleSlot Represents the section of the schedule to lock.
   * @return The uuid of the lock if created.
   * @throws DataAccessException If there was an error connecting to the database.
   */
  private UUID createScheduleLockImpl(ScheduleSlot scheduleSlot)
      throws ProtossException {
    ScheduleLockRequest.Builder builder = new ScheduleLockRequest.Builder();

    validateScheduleSlotTime(scheduleSlot);
    builder.setSlot(scheduleSlot);
    builder.setUser(getUser());
    builder.setClientUUID(getClientUuid());
    builder.setDuration((int) DEFAULT_LOCK_EXPIRY);
    ScheduleLockRequest request = builder.build();

    ScheduleLockManager lockManager = getImpl(ScheduleLockManager.class);
    // Request a schedule lock
    return lockManager.createLock(request);
  }

  /**
   * Returns a collection of usable schedule locks. Usable locks are have been created by the same
   * client, and the provided schedule slot will fit within the section of the scheduler locked by
   * the found locks.
   *
   * @param slot The Schedule slot to find appropriate locks for
   * @param clientUuid The client to find appropriate locks for
   * @return The uuid of a usable lock if one is present, null otherwise.
   * @throws DataAccessException If there is an error access the database.
   */
  private UUID getUsableScheduleLock(ScheduleSlot slot, UUID clientUuid)
      throws ProtossException {
    ScheduleLockManager lockManager = getImpl(ScheduleLockManager.class, true);
    Set<ScheduleLock> locks = lockManager.getLockConflicts(slot);
    return locks.stream()
        // getLockConficts can return expired locks (Protoss 4.1.0), so first we filter those out
        // Note: This could have some drift if the api server time is off of the database server
        .filter(l -> l.getExpiration().after(Calendar.getInstance(l.getExpiration().getTimeZone())))
        // Next, we only care about locks created by the client
        .filter(l -> Objects.equals(l.getClientUUID(), clientUuid))
        // Usable locks need to be in the same column
        .filter(l -> l.getSubColumn() == slot.getSubColumn())
        // Usable locks need to be for the same resource OR provider
        .filter(l -> (slot.getProviderId() != null
            && Objects.equals(l.getProviderId(), slot.getProviderId()))
            || (slot.getResourceId() != null
                && Objects.equals(l.getResourceId(), slot.getResourceId())))
        // Edge case, we will filter out any improperly initialized locks to avoid NPEs below
        .filter(l -> l.getDate() != null)
        // Appointment cant span across midnight, so we check if the slot is the same day
        .filter(l -> l.getDate().get(Calendar.YEAR) == slot.getDate().get(Calendar.YEAR))
        .filter(l -> l.getDate().get(Calendar.MONTH) == slot.getDate().get(Calendar.MONTH))
        .filter(l -> l.getDate().get(Calendar.DAY_OF_MONTH) == slot.getDate()
            .get(Calendar.DAY_OF_MONTH))
        // To be usable, the lock needs to contain the slot time
        .filter(l -> l.getStartTime() <= slot.getStartTime())
        .filter(l -> l.getEndTime() >= slot.getEndTime())
        // There should only ever be 0 or 1 locks present after these filters
        .findFirst().map(ScheduleLock::getUuid).orElse(null);
  }

  /**
   * Delete schedule lock.
   *
   * @param lockUuid Schedule lock UUID
   */
  private void releaseScheduleLock(UUID lockUuid) throws ProtossException {
    ScheduleLockManager lockManager = getImpl(ScheduleLockManager.class);
    lockManager.releaseLockForClient(lockUuid, getClientUuid());
  }

  /**
   * Validate the schedule slot times.
   *
   * @param scheduleSlot The schedule slot for which times are to be validated.
   */
  private void validateScheduleSlotTime(ScheduleSlot scheduleSlot) {
    if (scheduleSlot.getStartTime() >= scheduleSlot.getEndTime()) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "The provided start time must not come on or after the provided end time.");
    } else if (!TimeUtil.isValidTime(String.valueOf(scheduleSlot.getStartTime()))) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Start Time must be in valid 24 hour time");
    } else if (!TimeUtil.isValidTime(String.valueOf(scheduleSlot.getEndTime()))) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "End Time must be in valid 24 hour time");
    }
  }

}
