
package com.qhrtech.emr.restapi.endpoints.materials;

import com.qhrtech.emr.accuro.api.scheduling.AppointmentReminderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.scheduling.ReminderData;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.AppointmentMaterialsDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>AppointmentMaterialsEndpoint</code> collection is designed to expose materials related
 * to appointments.
 *
 * @RequestHeader Authorization Client credential bearer token
 *
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @deprecated Use /v2/materials/appointments/{appointmentId} to retrieve the same data.
 */
@Component
@Path("/v1/materials")
@Deprecated
@PreAuthorize("#oauth2.hasAnyScope('REMINDER_API') "
    + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS' , 'READ_ONLY') "
    + "and #accuro.hasAccess('SCHEDULING', 'READ_ONLY') ")
@Tag(name = "AppointmentMaterials Endpoints",
    description = "Exposes appointment material endpoints "
        + "(Deprecated: Refer to the version v2)")
public class AppointmentMaterialsEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Get all appointments which meet the specified filters. This method returns aggregate data for
   * each appointment found. The results are provided in paginated form, to request the next page
   * specify the startingId which should be set to the {@link EnvelopeDto#getLastId()} + 1 of the
   * previous page. All of the parameters are optional except for startDate. Results are ordered by
   * record id.
   *
   * @param providerId If this field is provided, the appointments will be filtered by provider ID.
   * @param startingId Specifies where to start the next page of data. Typically this is the
   *        {@link EnvelopeDto#getLastId()} from the last page of data.
   * @param pageSize The size of the pages requested. Page size has default value as 25 and maximum
   *        value as 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value i.e 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be reset to the
   *        maximum value i.e 50.
   *        </p>
   * @param startDate If this date is provided without an endDate, endpoint will return all the
   *        appointments scheduled on this date only. If endDate is also provided then endpoint will
   *        return all the appointments scheduled within start and end date( both dates inclusive ).
   *        Will throw @HTTP 400 if not provided.
   * @param endDate If this date is provided then endpoint will return all the appointments
   *        scheduled within start and this date( both dates inclusive ).
   * @param officeId If this field is provided, all appointments will be filtered by the office they
   *        are a part of.
   * @param includeBillOnly If this field is true, results will include both regular and billOnly
   *        appointments. If this field is false, the results will not include billOnly
   *        appointments. If the field is not provided or null, the results will not include
   *        billOnly appointments.
   * @HTTP 400 If startDate is not provided.
   * @return A page of aggregated appointment materials.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/appointments")
  @Facet("internal")
  @PreAuthorize("#oauth2.hasAnyScope('API_INTERNAL') "
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS' , 'READ_ONLY') "
      + "and #accuro.hasAccess('SCHEDULING', 'READ_ONLY') ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments with a provider that the user has this "
          + "permission for.")
  @Operation(
      deprecated = true,
      summary = "Retrieves appointments",
      description = " Retrieves all the appointments which meet the specified filters. "
          + "This method returns aggregate data for each appointment found. "
          + " The results are provided in paginated form, "
          + "to request the next page specify the startingId which should be set to the "
          + "**EnvelopeDto#getLastId()** + 1 of the previous page. "
          + "All of the parameters are optional except for startDate. "
          + "Results are ordered by record id. "
          + "This endpoint is deprecated. Refer to the version v2",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      ref = "EnvelopeDtoAppointmentDto")))
      })
  @Parameters(value = {
      @Parameter(
          name = "authorization",
          description = "Client credential bearer token",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "providerId",
          description = "The provider Id. If this field is provided, the appointments  "
              + "will be filtered by provider ID.",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer")),
      @Parameter(
          name = "startingId",
          description = "Specifies where to start the next page of data. Typically this is the "
              + "**EnvelopeDto#getLastId()** from the last page of data.",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer")),
      @Parameter(
          name = "pageSize",
          description = "The size of the pages requested. Default page size is 50. "
              + "Must be 0 < pageSize < 50, otherwise the default page size, 25, will be set.",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer")),
      @Parameter(
          name = "startDate",
          description = "Beginning cutoff for the appointment date.If this is provided without an "
              + "endDate, this method will only return appointments which were scheduled on this "
              + "specific date. If endDate is also provided "
              + "then appointments within the date range will be returned.",
          required = true,
          in = ParameterIn.QUERY),
      @Parameter(
          name = "endDate",
          description = "Ending cutoff for the appointment date. If this field is provided, "
              + "the appointments will be filtered by the date range startDate - endDate.",
          in = ParameterIn.QUERY),
      @Parameter(
          name = "officeId",
          description = "The office Id. If provided, all appointments will be filtered by the "
              + "office they are a part of.",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer")),
      @Parameter(
          schema = @Schema(type = "boolean"),
          name = "includeBillOnly",
          description = "If this field is true, results will include both regular and billOnly"
              + " appointments. If this field is false, the results will not include billOnly"
              + " appointments. If the field is not provided or null, "
              + "the results will not include  billOnly appointments",
          in = ParameterIn.QUERY)
  })
  public EnvelopeDto<AppointmentMaterialsDto> getAppointments(
      @Parameter(hidden = true) @QueryParam("providerId") Integer providerId,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate,
      @Parameter(hidden = true) @QueryParam("officeId") Integer officeId,
      @Parameter(hidden = true) @QueryParam("includeBillOnly") Boolean includeBillOnly)
      throws ProtossException {

    if (startDate == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "startDate must be provided");
    }

    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    if (includeBillOnly == null) {
      includeBillOnly = false;
    }
    LocalDate localStartDate = LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    if (localEndDate != null && localStartDate.isAfter(localEndDate)) {
      LocalDate temp = localStartDate;
      localStartDate = localEndDate;
      localEndDate = temp;
    }

    AppointmentReminderManager reminderManager = getImpl(AppointmentReminderManager.class);
    Envelope<ReminderData> data = reminderManager.getReminderData(localStartDate, localEndDate,
        providerId, officeId, includeBillOnly, startingId, pageSize);
    EnvelopeDto<AppointmentMaterialsDto> envelope = new EnvelopeDto<>();
    envelope.setContents(mapDto(data.getContents(), AppointmentMaterialsDto.class, ArrayList::new));
    envelope.setCount(data.getCount());
    envelope.setTotal(data.getTotal());
    envelope.setLastId(data.getLastId());
    return envelope;
  }

  /**
   * Gets aggregated appointment data for the provided appointmentId.
   *
   * @param appointmentId the id for which to get the aggregated appointment data.
   * @return The aggregated appointment materials.
   * @throws DataAccessException if there has been a database error.
   * @HTTP 404 If the specified Appointment is not found.
   */
  @GET
  @Path("/appointments/{appointmentId}")
  @PreAuthorize("#oauth2.hasAnyScope('REMINDER_API') "
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS' , 'READ_ONLY') "
      + "and #accuro.hasAccess('SCHEDULING', 'READ_ONLY') ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments with a provider that the user has this "
          + "permission for.")
  @Operation(
      deprecated = true,
      summary = "Retrieves an appointment",
      description = "Retrieves an aggregated appointment data for the provided appointmentId.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Specified appointment is not found."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = AppointmentMaterialsDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client credential bearer token",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "appointmentId",
      description = "The id for which aggregated appointment data has to be retrieved",
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  public AppointmentMaterialsDto getAppointment(
      @Parameter(hidden = true) @PathParam("appointmentId") int appointmentId)
      throws ProtossException {

    AppointmentReminderManager reminderManager = getImpl(AppointmentReminderManager.class);
    ReminderData data = reminderManager.getReminderData(appointmentId);
    if (data == null) {
      throw Error
          .returnNotFoundResult("Appointment not found.");
    }
    return mapDto(data, AppointmentMaterialsDto.class);
  }

}
