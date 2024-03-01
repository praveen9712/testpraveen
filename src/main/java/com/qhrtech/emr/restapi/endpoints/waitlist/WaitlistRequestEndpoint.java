
package com.qhrtech.emr.restapi.endpoints.waitlist;

import com.qhrtech.emr.accuro.api.waitlist.WaitlistRequestManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.waitlist.WaitlistRequest;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.waitlist.WaitlistRequestDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
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
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This <code>WaitlistRequestEndpoint</code> collection is designed to expose the Waitlist Request
 * DTO. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/waitlists")
@Facet("provider-portal")
@Tag(name = "Waitlist Endpoints",
    description = "Exposes waitlist endpoints to create, read and update a request.")
public class WaitlistRequestEndpoint extends AbstractEndpoint {


  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Get all the waitlist requests which meet the specified filters. The result will be provided in
   * a paginated form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to
   * request the next page. Last id is the {@code id} of the last record of the page, and the
   * results will be ordered by this field.
   *
   * <p>
   * All of the parameters are optional. All the waitlist requests will be returned if none of the
   * params are provided. If multiple filters are provided, they will be combined with AND operator.
   * </p>
   *
   * @param waitlistProviderId Waitlist provider id.
   * @param patientId Patient id.
   * @param consultStatus Waitlist consult status.
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId The starting {@code id} (exclusive) of the next page of data. Typically this
   *        is the {@code EnvelopeDto.lastId} from the last page.
   * @return A page of {@link WaitlistRequestDto}'s
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope('WAITLIST_READ', 'WAITLIST_WRITE')"
      + "and #accuro.hasAccess('WAITLIST', 'READ_ONLY') ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to waitlist requests"
          + " with a provider the user has this permission for.")
  @Operation(
      summary = "Retrieves all the waitlist requests based on the specified filters.",
      description = "Gets a list of waitlist requests. All parameters are optional."
          + "The result will be given in a paginated form.  "
          + "To request the next page, specify the startingId the same as "
          + " **EnvelopeDto.lastId** of the previous page."
          + "Last id is the **id** of the last record in the page."
          + "All existing requests will be returned if none of the "
          + "params are provided. If multiple filters are provided, "
          + "they will be combined with AND operator.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = WaitlistRequestDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "waitlistProviderId",
              description = "Waitlist provider id, can only be numbers",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "patientId",
              description = "Patient id, can only be numbers",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "consultStatus",
              description = "Waitlist consult status",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request). "
                  + "It is the same as the **id** (waitlist request id) of the last record of the "
                  + "previous results.",
              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<WaitlistRequestDto> getWaitlistRequests(
      @Parameter(hidden = true) @QueryParam("waitlistProviderId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "Waitlist providerId field can only have numbers") String waitlistProviderId,
      @Parameter(hidden = true) @QueryParam("patientId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "patientId field can only have numbers") String patientId,
      @Parameter(hidden = true) @QueryParam("consultStatus") String consultStatus,
      @Parameter(hidden = true) @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @Parameter(hidden = true) @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws ProtossException {
    int actualPageSize =
        StringUtils.isBlank(pageSize)
            ? DEFAULT_PAGE_SIZE
            : Integer.parseInt(pageSize);

    if (actualPageSize <= 0) {
      actualPageSize = DEFAULT_PAGE_SIZE;
    } else if (actualPageSize > MAX_PAGE_SIZE) {
      actualPageSize = MAX_PAGE_SIZE;
    }
    Integer actualStartingId = StringUtils.isBlank(startingId)
        ? null
        : Integer.parseInt(startingId);

    Integer newWaitlistProviderId =
        StringUtils.isBlank(waitlistProviderId) ? null : Integer.parseInt(waitlistProviderId);
    Integer waitlistPatientId =
        StringUtils.isBlank(patientId) ? null : Integer.parseInt(patientId);
    WaitlistRequestManager requestManager = getImpl(WaitlistRequestManager.class);

    Envelope<WaitlistRequest> waitlistEnvelope = requestManager.search(
        newWaitlistProviderId,
        waitlistPatientId,
        StringUtils.isBlank(consultStatus) ? null : consultStatus,
        actualStartingId, actualPageSize);

    EnvelopeDto<WaitlistRequestDto> envelopeDto = new EnvelopeDto<>();

    envelopeDto.setContents(
        mapDto(waitlistEnvelope.getContents(), WaitlistRequestDto.class, ArrayList::new));
    envelopeDto.setCount(waitlistEnvelope.getCount());
    envelopeDto.setTotal(waitlistEnvelope.getTotal());
    envelopeDto.setLastId(waitlistEnvelope.getLastId());

    return envelopeDto;
  }


  /**
   * Gets waitlist request {@link WaitlistRequestDto} based on the given id.
   *
   * @param requestId Unique waitlist request id.
   * @return {@link WaitlistRequestDto} object
   * @HTTP 200 Request successful
   * @HTTP 401 Consumer unauthorized
   * @HTTP 404 Resource Not Found
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope('WAITLIST_READ', 'WAITLIST_WRITE')"
      + "and #accuro.hasAccess('WAITLIST', 'READ_ONLY') ")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.ReadOnly,
      description = "Allows access to waitlist "
          + "requests with a provider the user has this permission for.")
  @Operation(
      summary = "Retrieves the waitlist request based on the given id.",
      description = "Gets the waitlist request based on given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = WaitlistRequestDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public WaitlistRequestDto getById(
      @Parameter(description = "Waitlist request id.") @PathParam("id") int requestId)
      throws ProtossException {

    WaitlistRequestManager requestManager = getImpl(WaitlistRequestManager.class);
    WaitlistRequest request = requestManager.getById(requestId);

    return mapDto(request, WaitlistRequestDto.class);
  }

  /**
   * Saves the waitlist.
   *
   * @param waitlistRequest The request of the waitlist
   * @return The unique id of the waitlist.
   * @throws ProtossException If there has been a database error
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess('WAITLIST', 'READ_WRITE')")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for any provider.")
  @Operation(
      summary = "Saves the waitlist.",
      description = "Saves the waitlist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = Integer.class))),
          @ApiResponse(
              responseCode = "400",
              description = "If the given input is invalid")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public int create(@Valid @RequestBody WaitlistRequestDto waitlistRequest)
      throws ProtossException {
    if (waitlistRequest == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Waitlist object must be provided.");
    }
    validate(waitlistRequest);
    WaitlistRequestManager waitlistRequestManager = getImpl(WaitlistRequestManager.class);
    return waitlistRequestManager.create(mapDto(waitlistRequest, WaitlistRequest.class), getUser());
  }

  /**
   * Update the waitlist.
   *
   * @param waitlistId The id of the waitlist
   * @param waitlistRequest The request of the waitlist
   * @throws ProtossException If there has been a database error
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   */
  @PUT
  @Path("/{waitlistId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess('WAITLIST', 'READ_WRITE')")
  @ProviderPermission(type = AccessType.Scheduling, level = AccessLevel.Full,
      description = "Allows access to waitlist "
          + "entries with a provider the user has this permission for.")
  @Operation(
      summary = "Updates the waitlist.",
      description = "Updates the waitlist.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "If the given input is invalid")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "waitlistId",
              description = "Waitlist id",
              in = ParameterIn.PATH)})
  public void update(@Parameter(hidden = true) @PathParam("waitlistId") int waitlistId,
      @Valid @RequestBody WaitlistRequestDto waitlistRequest)
      throws ProtossException {
    if (waitlistRequest == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Waitlist object must be provided.");
    }

    if (waitlistId != waitlistRequest.getRequestId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The id in the path doesn't match the id provided in the waitlist object.");
    }

    WaitlistRequestManager managerWithoutPermission = getImpl(WaitlistRequestManager.class, true);
    WaitlistRequest oldWaitlistRequest = managerWithoutPermission.getById(waitlistId);
    if (oldWaitlistRequest == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Waitlist not found.");
    }

    validate(waitlistRequest);

    // set the values from old request
    WaitlistRequest newWaitlistRequest = mapDto(waitlistRequest, WaitlistRequest.class);
    newWaitlistRequest.setInsurerId(oldWaitlistRequest.getInsurerId());
    newWaitlistRequest.setWaitTypeId(oldWaitlistRequest.getWaitTypeId());
    newWaitlistRequest.setSurgicalPriority(oldWaitlistRequest.getSurgicalPriority());
    newWaitlistRequest.setSurgicalSiteId(oldWaitlistRequest.getSurgicalSiteId());
    newWaitlistRequest.setSurgicalStatus(oldWaitlistRequest.getSurgicalStatus());
    newWaitlistRequest.setSurgicalType(oldWaitlistRequest.getSurgicalType());
    newWaitlistRequest.setSurgicalRefusedReason(oldWaitlistRequest.getSurgicalRefusedReason());
    newWaitlistRequest.setAvailableForCancellation(oldWaitlistRequest.isAvailableForCancellation());
    newWaitlistRequest.setSurgicalBookedBy(oldWaitlistRequest.getSurgicalBookedBy());
    newWaitlistRequest.setDiagnosisCode(oldWaitlistRequest.getDiagnosisCode());
    newWaitlistRequest.setDiagnosisSource(oldWaitlistRequest.getDiagnosisSource());
    newWaitlistRequest.setTargetWeeks(oldWaitlistRequest.getTargetWeeks());
    newWaitlistRequest.setAssistant(oldWaitlistRequest.getAssistant());
    newWaitlistRequest.setAssistantRequired(oldWaitlistRequest.isAssistantRequired());
    newWaitlistRequest.setAssistantLength(oldWaitlistRequest.getAssistantLength());
    newWaitlistRequest.setMigrationState(oldWaitlistRequest.getMigrationState());
    newWaitlistRequest.setWait(oldWaitlistRequest.getWait());
    newWaitlistRequest.setPackageId(oldWaitlistRequest.getPackageId());
    newWaitlistRequest.setAcats(oldWaitlistRequest.getAcats());
    newWaitlistRequest.setHeldDate(oldWaitlistRequest.getHeldDate());
    newWaitlistRequest.setSpecialEquip(oldWaitlistRequest.getSpecialEquip());
    newWaitlistRequest.setConsultBookedBy(oldWaitlistRequest.getConsultBookedBy());
    newWaitlistRequest.setDeleteFlag(oldWaitlistRequest.isDeleteFlag());

    WaitlistRequestManager managerWithPermission = getImpl(WaitlistRequestManager.class);
    managerWithPermission.update(newWaitlistRequest, getUser());
  }

  private void validate(WaitlistRequestDto request) {

    if (request.getComplaint() == null) {
      request.setComplaint("");
    }

    if (StringUtils.isBlank(request.getConsultPriority())) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Consult priority cannot be empty.");
    }

    if (StringUtils.isBlank(request.getConsultStatus())) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Consult status cannot be empty.");
    }

    if (StringUtils.isBlank(request.getConsultType())) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Consult type cannot be empty.");
    }

    if (request.getBookedDate() == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Request date cannot be null.");
    }
  }
}
