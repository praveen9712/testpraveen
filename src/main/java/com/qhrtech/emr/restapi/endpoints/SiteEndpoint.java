
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.scheduling.SiteManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.scheduling.Site;
import com.qhrtech.emr.restapi.models.dto.SiteDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
 * This <code>SiteEndpoint</code> collection is designed to expose Appointment Sites.
 *
 * @RequestHeader Authorization Client Credentials bearer token.
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component
@Path("/v1/")
@PreAuthorize("#oauth2.hasAnyScope('REMINDER_API')")
@Tag(name = "Site Endpoints", description = "Exposes site endpoints")
public class SiteEndpoint extends AbstractEndpoint {

  /**
   * Retrieve an Appointment Site by the site id.
   *
   * @param siteId Appointment Site id.
   * @return A site
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the Appointment Site id does not exist on the server.
   */
  @GET
  @Path("/sites/{siteId}")
  @Operation(
      summary = "Retrieves the appointment site by the site id",
      description = "Gets the appointment site by the site id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the appointment site id does not exist on the server"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = SiteDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Client credential bearer token",
      in = ParameterIn.HEADER,
      required = true)
  public SiteDto getSite(@Parameter(description = "Site id") @PathParam("siteId") int siteId)
      throws ProtossException {

    SiteManager siteManager = getImpl(SiteManager.class);
    Site site = siteManager.getSiteById(siteId);
    if (site == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Site not found.");
    }
    return mapDto(site, SiteDto.class);
  }

  /**
   * Retrieve all Appointment Sites.
   *
   * @return A list of all sites ordered by the site's name.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/sites")
  @Operation(
      summary = "Retrieves all appointment sites",
      description = "Gets all appointment sites.",
      parameters = @Parameter(
          name = "authorization",
          required = true,
          description = "Client credentials bearer token",
          in = ParameterIn.HEADER),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = SiteDto.class))))})
  public List<SiteDto> getSites() throws ProtossException {

    SiteManager siteManager = getImpl(SiteManager.class);
    List<Site> sites = siteManager.getSites();
    return mapDto(sites, SiteDto.class, ArrayList::new);
  }

}
