
package com.qhrtech.emr.restapi.endpoints.provider.referral;

import com.qhrtech.emr.accuro.api.referral.ReferralOrderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.referral.ReferralOrder;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.referrals.ReferralOrderDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.util.DateFormatter;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
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
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code ReferralOrderEndpoint} collection is designed to expose the {@link ReferralOrderDto}.
 *
 * <p>
 * Referral Order Status(statusOrder) and Type(type) can be retrieved at:
 * <ul>
 * <li>Referral Order Status: /v1/provider-portal/selection-list/referral-statuses</li>
 * <li>Referral Order Type: /v1/provider-portal/selection-list/referral-types</li>
 * </ul>
 * </p>
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/referral-orders")
@Facet("provider-portal")
@Tag(name = "Referral Order Endpoints", description = "Exposes referral order endpoints")
public class ReferralOrderEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MAXIMUM_PAGE_SIZE = 50;

  private static boolean equals(Object obj1, Object obj2) {
    return (obj1 == null ? obj2 == null : obj1.equals(obj2));
  }

  /**
   * Get referral orders by the specified filters. The results will be provided in paginated form.
   * To request the next page, the startingId should be set to the {@code EnvelopeDto.lastId} of the
   * previous page. The order with the startingId will be excluded.
   *
   * <p>
   * The last id is the unique id of the referral order and the last record of the page. The results
   * will be sorted by this id.
   * </p>
   *
   * <p>
   * {@code total} is the number of all the records including the current page. The {@code count} is
   * the number of the records in the current page and will be reset to 0 in the last page.
   * </p>
   * <p>
   * All the parameters are optional.
   * </p>
   * <p>
   * lastModifiedStart and lastModifiedEnd if provided, should be provided together. The difference
   * between lastModifiedStart and lastModifiedEnd should not be more than 30 days.
   * lastModifiedStart and lastModifiedEnd will be compared against last modified of referral or
   * repliforms (whichever is existing and greater). If lastModifiedStart is greater than
   * lastModifiedEnd, their values will be swapped. If values of lastModifiedStart and
   * lastModifiedEnd are passed as date only (which we do not recommend), then the time part will be
   * rounded to zero, e.g., 2022-11-25 will be rounded to 2022-11-25T00:00:00.000.
   * </p>
   *
   * @param fromDate The specific date when the referral orders occurred. API will return the orders
   *        on or after this date.
   * @param lastModifiedStart The last updated or modified date of orders or repliforms (which ever
   *        is greater). API will return the orders matching the results >= the date, e.g.,
   *        2022-11-25T15:44:28.000
   * @param lastModifiedEnd The last updated or modified date of orders or repliforms (which ever is
   *        greater). API will return the orders matching the results <= the date, e.g.,
   *        2022-11-25T15:44:28.000
   * @param startingId Specifies the unique order id of the next page of data (exclusive). Typically
   *        this is the {@code EnvelopeDto.lastId} from the last page of data.
   * @param pageSize Default page size is 10. The size must be 0 < pageSize <= 50. If the size is
   *        not provided or less than 1 it will be set the default size. If it is over 50 it will be
   *        set the maximum size which is 50.
   * @param reconciled The flag indicates if the referral orders are reconciled or not. If true it
   *        will return all reconciled referral orders. Otherwise it will return all non-reconciled
   *        referral orders. If the flag is not provided it will return both
   *        reconciled/non-reconciled referral orders.
   * @return A page of aggregated referral orders
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Successful.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to referral orders "
          + "for providers the user has this permission for.")
  @Operation(
      summary = "Retrieves Referral orders",
      description = "Gets all Referral orders for the specific dates and the reconciled flag."
          + "The results will be provided in paginated form."
          + " To request the next page, the startingId should"
          + " be set to the {@code EnvelopeDto.lastId} of the"
          + " previous page. The order with the startingId "
          + "will be excluded. "
          + "The last id is the unique id of the referral order"
          + " and the last record of the page. The results "
          + "will be sorted by this id."
          + " Total is the number of all the records including"
          + " the current page. The {@code count} is "
          + " the number of the records in the current page"
          + " and will be reset to 0 in the last page. \n"
          + " lastModifiedStart and lastModifiedEnd if provided, should be provided together.\n"
          + " The difference between lastModifiedStart and lastModifiedEnd should not "
          + "be more than 30 days.\n"
          + " lastModifiedStart and lastModifiedEnd will be compared against last "
          + "modified of referral or repliforms (whichever is existing and greater).\n"
          + "if lastModifiedStart is greater than lastModifiedEnd, their values will be"
          + " swapped.\n"
          + "If values of lastModifiedStart and lastModifiedEnd are passed as date only,"
          + " the time part will be rounded to zero, \n"
          + " e.g., 2022-11-25 will be rounded to 2022-11-25T00:00:00.000 \n",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ReferralOrderDto.class,
                      subTypes = EnvelopeDto.class)))),
          @ApiResponse(
              responseCode = "400",
              description = "If schema version is not supported")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "fromDate",
              description = "The specific date when the referral"
                  + " orders occurred. API will return the orders "
                  + "on or after this date.",
              in = ParameterIn.QUERY,
              example = "2018-10-01"),
          @Parameter(
              name = "lastModifiedStart",
              description = "The last updated or modified date"
                  + " of orders. API will return the orders "
                  + "matching the results on or after this date.",
              in = ParameterIn.QUERY,
              example = "2022-11-25T15:44:28.000"),
          @Parameter(
              name = "lastModifiedEnd",
              description = "The last updated or modified date"
                  + " of orders. API will return the orders "
                  + "matching the results on or before this date.",
              in = ParameterIn.QUERY,
              example = "2022-11-25T15:44:28.000"),
          @Parameter(
              name = "startingId",
              description = "Specifies where to start the "
                  + "next page of data. Typically this is the "
                  + "**EnvelopeDto#getLastId()** from the last page of data.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages requested."
                  + " Default page size is 50. "
                  + "Must be 0 < pageSize < 50, otherwise "
                  + "the default page size, 10, will be set.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer")),
          @Parameter(
              name = "reconciled",
              description = "The flag indicates if the referral"
                  + " orders are reconciled or not. If true it "
                  + "will return all reconciled referral orders."
                  + " Otherwise it will return all non-reconciled "
                  + "referral orders. If the flag is not "
                  + "provided, it will return both "
                  + "reconciled/non-reconciled referral orders.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "boolean"))})

  public EnvelopeDto<ReferralOrderDto> getFromDate(
      @Parameter(hidden = true) @QueryParam("fromDate") String fromDate,
      @Parameter(hidden = true) @QueryParam("lastModifiedStart") String lastModifiedStart,
      @Parameter(hidden = true) @QueryParam("lastModifiedEnd") String lastModifiedEnd,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize,
      @Parameter(hidden = true) @QueryParam("reconciled") Boolean reconciled)
      throws ProtossException {
    if ((lastModifiedStart == null && lastModifiedEnd != null)
        || (lastModifiedStart != null && lastModifiedEnd == null)) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "lastModifiedStart and lastModifiedEnd fields should be used together.");
    }
    LocalDateTime startDate = DateFormatter.toLocalDatetimeOptionalTime(lastModifiedStart);
    LocalDateTime endDate = DateFormatter.toLocalDatetimeOptionalTime(lastModifiedEnd);

    if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
      LocalDateTime temp = startDate;
      startDate = endDate;
      endDate = temp;
    }
    if (startDate != null && endDate != null) {
      int diff = Days.daysBetween(startDate, endDate).getDays();
      if (diff > 30) {
        throw Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Difference "
                + "between lastModifiedStart and lastModifiedEnd should not be more than 30 days");
      }
    }

    if (pageSize == null || pageSize < 1) {
      pageSize = DEFAULT_PAGE_SIZE;
    }
    if (pageSize > MAXIMUM_PAGE_SIZE) {
      pageSize = MAXIMUM_PAGE_SIZE;
    }
    LocalDateTime localSince =
        fromDate == null ? null : DateFormatter.toLocalDatetimeOptionalTime(fromDate);
    EnvelopeDto<ReferralOrderDto> envelopeDto = new EnvelopeDto<>();

    ReferralOrderManager referralOrderManager = getImpl(ReferralOrderManager.class);
    Envelope<ReferralOrder> orders =
        referralOrderManager.getReferralOrders(localSince, startDate, endDate, startingId, pageSize,
            reconciled);

    envelopeDto.setContents(mapDto(orders.getContents(), ReferralOrderDto.class, ArrayList::new));
    envelopeDto.setCount(orders.getCount());
    envelopeDto.setTotal(orders.getTotal());
    envelopeDto.setLastId(orders.getLastId());

    return envelopeDto;
  }

  /**
   * Get the referral order by the specific order id.
   *
   * @param id The unique id of the referral order.
   * @return The referral order.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Successful.
   * @HTTP 404 If the specified referral order is not found.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has this permission"
          + " for the provider of the referral order.")
  @Operation(
      summary = "Retrieves Referral order",
      description = "Gets Referral order by the given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ReferralOrderDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ReferralOrderDto getById(@PathParam("id") int id) throws ProtossException {
    ReferralOrderManager referralOrderManager = getImpl(ReferralOrderManager.class);
    ReferralOrder order = referralOrderManager.getById(id);

    if (order == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Referral order not found.");
    }

    return mapDto(order, ReferralOrderDto.class);
  }

  /**
   * Update referral order.
   * <p>
   * Only specified fields below can be updated, otherwise Bad Request (400 error) will be returned.
   * <ul>
   * <li>status</li>
   * <li>type</li>
   * <li>description</li>
   * <li>specific</li>
   * <li>location</li>
   * <li>bookedDate</li>
   * <li>appointmentDatetime</li>
   * <li>appointmentTime</li>
   * </ul>
   * </p>
   * <p>
   * The modified by user(modifiedByUser) will be set accordingly to the current user logging in API
   * so the requested value will be ignored.
   * </p>
   * <p>
   * The last modified date(lastModified) will be set accordingly to Accuro current time so the
   * requested value will be ignored.
   * </p>
   * <p>
   * Any fields which are not listed above must be set the the previous values which are set for
   * referral order.
   * </p>
   *
   * @param id The unique order id.
   * @param referralOrderDto The referral order.
   * @return 204 No Content if successful.
   * @throws DataAccessException If there has been a database error.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @HTTP 204 Successful but no content.
   * @HTTP 400 If the referral order is not given.
   * @HTTP 400 If the current user and the given order's modifier doesn't match.
   * @HTTP 400 If the referral order doesn't exist
   * @HTTP 400 If request invalid fields to be updated.
   * @HTTP 400 If the referral order status or type is not valid.
   */
  @PUT
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission "
          + "for the provider of the referral order.")
  @Operation(
      summary = "Updates Referral Order",
      description = "Update Referral order.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "If the referral order is not given."
                  + " And if the given order is for outbound e-referral."),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden without the right permission."),
          @ApiResponse(
              responseCode = "404",
              description = "Referral order not found"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response update(@PathParam("id") int id, @Valid ReferralOrderDto referralOrderDto)
      throws ProtossException {

    if (referralOrderDto == null) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The referral order must be given.");
    }

    if (id != referralOrderDto.getId()) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The path id and the given order's id doesn't match.");
    }

    ReferralOrderManager referralOrderManager = getImpl(ReferralOrderManager.class, true);
    ReferralOrder previous = referralOrderManager.getById(id);

    if (previous == null) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The referral order doesn't exist.");
    }

    // check if the previous/current values match.
    if (previous.getPhysicianId() != referralOrderDto.getPhysicianId()
        || previous.getPatientId() != referralOrderDto.getPatientId()
        || previous.isReconciled() != referralOrderDto.isReconciled()
        || !equals(previous.getOrder(), referralOrderDto.getOrder())
        || !equals(previous.getPrimaryRecipient(), referralOrderDto.getPrimaryRecipient())
        || !equals(previous.getCcRecipients(), referralOrderDto.getCcRecipients())
        || !equals(previous.getChartItemId(), referralOrderDto.getChartItemId())
        || !equals(previous.getChartItemType(), referralOrderDto.getChartItemType())
        || !equals(previous.getDate(), referralOrderDto.getDate())
        || !equals(previous.getContactType(), referralOrderDto.getContactType())
        || !equals(previous.getContactId(), referralOrderDto.getContactId())
        || !equals(previous.getErefOutboundId(), referralOrderDto.getErefOutboundId())
        || !equals(previous.getFaxLogId(), referralOrderDto.getFaxLogId())
        || !equals(previous.getErefApptStatus(), referralOrderDto.getErefApptStatus())) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST,
          "Please check the documentation for a list of fields which are allowed to be updated. "
              + " An disallowed field has been modified.");
    }
    ReferralOrderManager referralOrderManagerUpdate = getImpl(ReferralOrderManager.class);

    ReferralOrder referralOrder = mapDto(referralOrderDto, ReferralOrder.class);
    try {
      referralOrderManagerUpdate.update(referralOrder, getUser());
      return Response.noContent().build();
    } catch (IllegalArgumentException iae) {
      throw Error.webApplicationException(Status.BAD_REQUEST, iae.getMessage());
    }
  }

  /**
   * Create the referral order.
   * <p>
   * The last modified date(lastModified) will be set accordingly to Accuro current time.
   * </p>
   *
   * @param referralOrderDto The referral order.
   * @return The unique id of the referral order.
   * @throws DataAccessException If there has been a database error.
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @HTTP 201 Created.
   * @HTTP 400 If the referral order is not given.
   * @HTTP 400 If the given order is for outbound e-referral.
   */
  @POST
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Saves Referral Order",
      description = "Creates new Referral order.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns the referral order id"))),
          @ApiResponse(
              responseCode = "400",
              description = "If the referral order is not given."
                  + " And if the given order is for outbound e-referral."),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden without the right permission."),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)

  public Response create(@Valid ReferralOrderDto referralOrderDto)
      throws ProtossException {
    if (referralOrderDto == null) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The referral order must be given.");
    }

    // those two fields are for outbound e-referral which API/Protoss doesn't support yet.
    if (referralOrderDto.getErefOutboundId() != null
        || !StringUtils.isBlank(referralOrderDto.getErefApptStatus())) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "Unsupported feature - create the order of outbound e-referral.");
    }

    ReferralOrder referralOrder = mapDto(referralOrderDto, ReferralOrder.class);
    ReferralOrderManager referralOrderManager = getImpl(ReferralOrderManager.class);
    try {
      int id = referralOrderManager.create(referralOrder, getUser());
      return Response.status(Status.CREATED).entity(id).build();
    } catch (IllegalArgumentException iae) {
      throw Error.webApplicationException(Status.BAD_REQUEST, iae.getMessage());
    }
  }

  /**
   * Delete(hard) the referral order by the given order id.
   *
   * @param id The unique id of the referral order.
   * @return 204 response if successful.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 204 Successful but no content.
   */
  @DELETE
  @Facet("internal")
  @Hidden
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Deletes Referral Order",
      description = "Remove the Referral order.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Deleted successfully"),
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized for the office"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden without the right permission."),
          @ApiResponse(
              responseCode = "404",
              description = "Referral order not found"),
          @ApiResponse(
              responseCode = "500",
              description = "Timezone not set in Accuro.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(@PathParam("id") int id) throws ProtossException {
    ReferralOrderManager referralOrderManager = getImpl(ReferralOrderManager.class);
    referralOrderManager.delete(id);
    return Response.noContent().build();
  }
}
