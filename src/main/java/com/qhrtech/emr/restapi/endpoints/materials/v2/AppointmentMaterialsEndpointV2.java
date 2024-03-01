
package com.qhrtech.emr.restapi.endpoints.materials.v2;

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
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
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
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This is the 2nd version of the collection designed to expose materials related to appointments.
 *
 * @RequestHeader Authorization Client credential bearer token
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@SuppressWarnings("DuplicatedCode")
@Component
@Path("/v2/materials")
@PreAuthorize("#oauth2.hasAnyScope('REMINDER_API') "
    + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS' , 'READ_ONLY') "
    + "and #accuro.hasAccess('SCHEDULING', 'READ_ONLY') ")
@Tag(name = "AppointmentMaterialsV2 Endpoints",
    description = "Exposes the appointment materials V2 endpoints")
public class AppointmentMaterialsEndpointV2 extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Get all appointments which meet the specified filters. The results are provided in paginated
   * form, to request the next page specify the startingId which should be set to the
   * {@code EnvelopeDto.lastId} of the previous page. All of the parameters are optional except for
   * {@code startDate}.
   * <p>
   * {@code rowversion}, the update timestamp of an appointment, is introduced in this endpoint.
   * Last id is the {@code rowversion} of the last record of the page, and results are ordered by
   * this field.
   * </p>
   * <p>
   * In addition to pagination, the {@code rowversion} can be used to retrieve all changed
   * appointments since a previous call. Repeating a call with the same filters, but updating the
   * {@code startingId} to the {@code lastId} of the previous request will provide only appointments
   * that have changed since the last request, preventing the need to perform repeat expensive full
   * queries to detect changes.
   * </p>
   * <p>
   * {@code total} is the count of remaining records which includes the current page. The count will
   * be reset to 0 on the last page.
   * </p>
   *
   * @param providerId If this field is provided, the appointments will be filtered by provider ID.
   * @param startingId Specifies the starting {@code rowversion} (exclusive) of the next page of
   *        data. Typically this is the {@code EnvelopeDto.lastId} from the last page of data.
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
   * @param officeIds Multiple office ids can be provided, i.e. in a request url,
   *        officeId=1280&officeId=3996. If this field is provided, all appointments will be
   *        filtered by the offices they are a part of.
   * @param includeNullOffice Include null office, if only Null offices are wanted pass -1 for
   *        officeIds and set this flag to be True. By default it is true and appointments with no
   *        office will be included, so this parameter is only required if filtering no office
   *        appointments out, or requesting <b>only</b> no office appointments. If false then null
   *        office appointments are excluded.
   * @param includeBillOnly If this field is true, results will include both regular and billOnly
   *        appointments. If this field is false, the results will not include billOnly
   *        appointments. If the field is not provided or null, the results will not include
   *        billOnly appointments.
   * @param includeCancelled If this field is true, results will include cancelled appointments
   *        along with active appointments. If the field is null, not provided or false, the results
   *        will not include the cancelled appointments.
   * @param accessionNumber The appointment accessionNumber. This is an identifier for the
   *        appointment resource. Null and blank accession numbers are not valid.
   *
   * @HTTP 400 If startDate is not provided.
   * @return A page of aggregated appointment materials.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/appointments")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to appointments with a provider that the user has this "
          + "permission for.")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "PATIENT_DEMOGRAPHICS")})

  @Operation(
      summary = "Retrieves appointments",
      description = "Retrieves all the appointments which meet the specified filters. "
          + "The results are provided in paginated form. "
          + "To request the next page, specify the startingId which "
          + "should be set to the **EnvelopeDto.lastId** of the previous page. "
          + "All of the parameters are optional except for **startDate**.<br><br>"
          + "Field **rowversion**, the update timestamp of an appointment is introduced in "
          + "this endpoint. Last id is the **rowversion** of the last record of the page, "
          + "and results are ordered by this field.<br><br>"
          + "In addition to pagination, the **rowversion** can be used to retrieve all "
          + "changed appointments since a previous call. Repeating a call with the same filters, "
          + "but updating the **startingId** to the **lastId** of the previous request will "
          + "provide only appointments that have changed since last request, preventing the need "
          + "to perform repeat expensive full queries to detect changes.<br><br>"
          + "The count will be reset to 0 on the last page.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      ref = "EnvelopeDtoAppointmentDto")))
      })

  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client credential bearer token",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "providerId",
              description = "The provider Id. If this field is provided, "
                  + "the appointments will be filtered by provider Id.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "startingId",
              description = "The starting rowversion(exclusive) of the next page of data."
                  + " Typically this is the EnvelopeDto.lastId from the last page of data.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages requested. Default page size is 25. "
                  + "Must be 0 < pageSize < 50, otherwise the default page size will be set.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "startDate",
              description = "If provided without an endDate parameter, "
                  + "only appointments which were scheduled "
                  + "on the date specified in this parameter will be returned. "
                  + "If endDate parameter is also provided "
                  + "then appointments within the date range will be returned.",
              in = ParameterIn.QUERY,
              required = true),
          @Parameter(
              name = "endDate",
              description = "If provided, "
                  + "the appointments will be filtered by the date range startDate - endDate. ",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "includeNullOffice",
              description = "Indication to include appointments with no-office "
                  + "in the results or not. "
                  + "If only Null offices are required in the results, "
                  + "pass -1 as the **officeIds** parameter and set this parameter to **True**. "
                  + "By default this parameter is **True** and results will include appointments "
                  + "with no office as well. "
                  + "So this parameter is only required for filtering out no-office appointments, "
                  + "or requesting **only** no-office appointments. "
                  + "If this parameter is **False** then no-office appointments will be excluded "
                  + "from the results.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "boolean")),
          @Parameter(
              schema = @Schema(type = "boolean"),
              name = "includeBillOnly",
              description = "If this field is true, results will include both regular and billOnly"
                  + " appointments. If this field is false, the results will not include billOnly"
                  + " appointments. If the field is not provided or null, "
                  + "the results will not include  billOnly appointments",
              in = ParameterIn.QUERY),
          @Parameter(
              schema = @Schema(type = "boolean"),
              name = "includeCancelled",
              description = "If this field is true, results will include cancelled appointments. "
                  + "If the field is null, not provided or false, the results will not include the "
                  + "cancelled appointments. ",
              in = ParameterIn.QUERY),
          @Parameter(
              schema = @Schema(type = "String"),
              name = "accessionNumber",
              description = " The appointment accessionNumber. This is another "
                  + "identifier for the appointment resource. Null and blank accession numbers "
                  + "are not valid.",

              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<AppointmentMaterialsDto> getAppointments(
      @Parameter(hidden = true) @QueryParam("providerId") Integer providerId,
      @Parameter(hidden = true) @QueryParam("startingId") Long startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize,
      @Parameter(hidden = true) @QueryParam("startDate") Calendar startDate,
      @Parameter(hidden = true) @QueryParam("endDate") Calendar endDate,
      @Parameter(
          name = "officeId",
          description = "Multiple office ids can be provided, "
              + "i.e. in a request url, officeId=1280&officeId=3996. \n\n "
              + "If this parameter is provided, all appointments will be filtered "
              + "by the offices they are a part of.",
          in = ParameterIn.QUERY) @QueryParam("officeId") List<Integer> officeIds,
      @Parameter(hidden = true) @QueryParam("includeNullOffice") Boolean includeNullOffice,
      @Parameter(hidden = true) @QueryParam("includeBillOnly") Boolean includeBillOnly,
      @QueryParam("includeCancelled") Boolean includeCancelled,
      @QueryParam("accessionNumber") String accessionNumber)
      throws ProtossException {

    if (includeCancelled == null) {
      includeCancelled = false;
    }
    if (startDate == null && StringUtils.isBlank(accessionNumber)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "startDate or accessionNumber must be provided");
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

    LocalDate localStartDate = startDate == null ? null : LocalDate.fromCalendarFields(startDate);
    LocalDate localEndDate = endDate == null ? null : LocalDate.fromCalendarFields(endDate);

    if (localEndDate != null && localStartDate != null && localStartDate.isAfter(localEndDate)) {
      LocalDate temp = localStartDate;
      localStartDate = localEndDate;
      localEndDate = temp;
    }

    if (includeNullOffice == null) {
      includeNullOffice = true;
    }

    AppointmentReminderManager reminderManager = getImpl(AppointmentReminderManager.class);
    Envelope<ReminderData> data =
        reminderManager.getAppointmentMaterials(localStartDate, localEndDate,
            providerId, officeIds, includeNullOffice, includeBillOnly, includeCancelled,
            StringUtils.isBlank(accessionNumber) ? null : accessionNumber,
            startingId,
            pageSize);

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
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to the appointment with a provider the user has this "
          + "permission for.")
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "SCHEDULING"),
          @RolePermission(type = "PATIENT_DEMOGRAPHICS")})
  @Operation(
      summary = "Retrieves an appointment",
      description = "Retrieves an appointment data for the provided appointmentId.",
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
      description = "Id of the appointment",
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  public AppointmentMaterialsDto getAppointment(
      @Parameter(hidden = true) @PathParam("appointmentId") int appointmentId)
      throws ProtossException {
    AppointmentReminderManager reminderManager = getImpl(AppointmentReminderManager.class);
    ReminderData data =
        reminderManager.getReminderData(appointmentId);
    if (data == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Appointment not found.");
    }
    return mapDto(data, AppointmentMaterialsDto.class);
  }

}
