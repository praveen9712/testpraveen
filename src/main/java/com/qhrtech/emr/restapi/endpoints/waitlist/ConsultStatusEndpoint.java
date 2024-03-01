
package com.qhrtech.emr.restapi.endpoints.waitlist;

import com.qhrtech.emr.accuro.api.waitlist.ConsultStatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.waitlist.ConsultStatus;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.waitlist.ConsultStatusDto;
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
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>ConsultStatusEndpoint</code> collection is designed to expose the waitlist consult
 * statuses. Requires provider level authorization.
 *
 * @RequestHeader Authorization: provider level authorization grant
 * @HTTP 200 If the request was successful
 * @HTTP 401 If the consumer is unauthorized
 * @see ConsultStatusDto
 */
@Component
@Path("/v1/provider-portal/waitlist-consult-statuses")
@Facet("provider-portal")
@Tag(name = "Waitlist Endpoints",
    description = "Exposes waitlist endpoints.")
public class ConsultStatusEndpoint extends AbstractEndpoint {


  /**
   * Retreives list of all the waitlist consult statuses.
   *
   * @return {@link List} of {@link ConsultStatusDto}
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope('WAITLIST_READ', 'WAITLIST_WRITE') "
      + "and #accuro.hasAccess('WAITLIST', 'READ_ONLY')")
  @Operation(
      summary = "Retrieves all the waitlist consult statuses.",
      description = "Retrieves all the available waitlist consult statuses.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ConsultStatusDto.class)))),
          @ApiResponse(
              responseCode = "401",
              description = "User unauthorized.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<ConsultStatusDto> getAll() throws ProtossException {

    ConsultStatusManager waitListStatusManager = getImpl(ConsultStatusManager.class);
    List<ConsultStatus> waitlistStatuses = waitListStatusManager.getAll();

    return mapDto(waitlistStatuses, ConsultStatusDto.class, ArrayList::new);
  }

  @GET
  @Path("/{statusId}")
  @PreAuthorize("#oauth2.hasAnyScope('WAITLIST_READ', 'WAITLIST_WRITE') "
      + "and #accuro.hasAccess('WAITLIST', 'READ_ONLY')")
  @Operation(
      summary = "Retrieves waitlist consult status belonging to given id.",
      description = "Retrieves waitlist consult status belonging to given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ConsultStatusDto.class)))),
          @ApiResponse(
              responseCode = "401",
              description = "User unauthorized."),
          @ApiResponse(
              responseCode = "404",
              description = "Waitlist consult status not found.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ConsultStatusDto getById(
      @Parameter(description = "status id") @PathParam("statusId") int statusId)
      throws ProtossException {
    ConsultStatusManager waitListStatusManager = getImpl(ConsultStatusManager.class);
    ConsultStatus result = waitListStatusManager.getById(statusId);
    if (result == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Waitlist consult status not found with id: " + statusId);
    }
    return mapDto(result, ConsultStatusDto.class);
  }

}
