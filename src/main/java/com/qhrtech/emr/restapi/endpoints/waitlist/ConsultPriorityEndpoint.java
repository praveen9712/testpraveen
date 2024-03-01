
package com.qhrtech.emr.restapi.endpoints.waitlist;

import com.qhrtech.emr.accuro.api.waitlist.ConsultPriorityManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.waitlist.ConsultPriority;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.waitlist.ConsultPriorityDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve consult priorities information.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/waitlist-consult-priorities")
@Facet("provider-portal")
@Tag(name = "Waitlist Endpoints", description = "Exposes waitlist endpoints")
public class ConsultPriorityEndpoint extends AbstractEndpoint {

  /**
   * Get all consult priorities.
   *
   * @return A List of {@link ConsultPriorityDto}s ordered by priority value(wait time).
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/")
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_READ', 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves consult priorities",
      description = "Gets all waitlist consult priorities.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ConsultPriorityDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)

  public List<ConsultPriorityDto> getAllPriorities()
      throws ProtossException {
    ConsultPriorityManager consultPriorityManager = getImpl(ConsultPriorityManager.class);
    List<ConsultPriority> consultPriorities = consultPriorityManager.getAll();
    return mapDto(consultPriorities, ConsultPriorityDto.class, ArrayList::new);
  }

  /**
   * Get a single consult priority by priority id.
   *
   * @param priorityId The priority id.
   * @return A {@link ConsultPriorityDto} associated to the id.
   * @throws ProtossException If there has been a database error.
   * @HTTP 404 Not found
   */
  @GET
  @Path("/{priorityId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_READ', 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the single consult priority by id",
      description = "Gets the single priority by the given priority id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = ConsultPriorityDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Consult priority not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ConsultPriorityDto getPriorityById(
      @Parameter(description = "Priority id") @PathParam("priorityId") int priorityId)
      throws ProtossException {
    ConsultPriorityManager priorityManager = getImpl(ConsultPriorityManager.class);
    ConsultPriority consultPriority = priorityManager.getById(priorityId);

    if (consultPriority == null) {
      throw Error.webApplicationException(Response.Status.NOT_FOUND, "PriorityList Not Found");
    }

    return mapDto(consultPriority, ConsultPriorityDto.class);
  }


}
