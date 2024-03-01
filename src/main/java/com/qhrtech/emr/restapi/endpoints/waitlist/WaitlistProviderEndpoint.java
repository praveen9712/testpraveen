
package com.qhrtech.emr.restapi.endpoints.waitlist;

import com.qhrtech.emr.accuro.api.waitlist.WaitlistProviderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.waitlist.WaitlistProvider;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.waitlist.WaitlistProviderDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>{@link WaitlistProviderEndpoint}</code> is designed to expose the waitlist providers.
 * Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see com.qhrtech.emr.accuro.api.waitlist.WaitlistProviderManager
 */
@Component
@Path("/v1/provider-portal/waitlist-providers")
@Facet("provider-portal")
@Tag(name = "Waitlist Endpoints",
    description = "Exposes waitlist endpoints.")
public class WaitlistProviderEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all the providers registered for waitlist.
   *
   * @return {@link Set} of {@link WaitlistProviderDto}'s
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_READ', 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all the waitlist providers",
      description = "Gets all the providers registered for waitlist.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = WaitlistProviderDto.class)))),
          @ApiResponse(
              responseCode = "401",
              description = "User unauthorized")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<WaitlistProviderDto> getAll() throws ProtossException {
    WaitlistProviderManager waitlistProviderManager = getImpl(WaitlistProviderManager.class);

    return mapDto(waitlistProviderManager.getAll(), WaitlistProviderDto.class, HashSet::new);
  }

  /**
   * Retrieves the waitlist provider registered with the given id.
   *
   * @return {@link WaitlistProviderDto} Waitlist provider data transfer object.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_READ', 'WAITLIST_WRITE' ) "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves waitlist provider with the given id",
      description = "Gets the waitlist provider belonging to the given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = WaitlistProviderDto.class)))),
          @ApiResponse(
              responseCode = "401",
              description = "User unauthorized"),
          @ApiResponse(
              responseCode = "404",
              description = "Waitlist provider not found.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "id",
              description = "Waitlist provider id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public WaitlistProviderDto getById(
      @Parameter(hidden = true) @PathParam("id") int waitlistProviderId)
      throws ProtossException {

    WaitlistProviderManager waitlistProviderManager = getImpl(WaitlistProviderManager.class);
    WaitlistProvider waitlistProvider = waitlistProviderManager.getById(waitlistProviderId);
    if (waitlistProvider == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Waitlist provider not found with id: " + waitlistProviderId);
    }
    return mapDto(waitlistProvider, WaitlistProviderDto.class);
  }

}
