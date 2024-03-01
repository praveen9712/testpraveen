
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LabGroupDto;
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
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * End points for retrieving patient lab data that is not scoped to a particular patient.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 400 If an invalid provider id is provided
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/")
@Facet("provider-portal")
@PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
    + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
@Tag(name = "ProviderLabs Endpoints",
    description = "Exposes provider labs endpoints")
public class ProviderLabsEndpoint extends AbstractEndpoint {

  /**
   * Get all active labs for the current provider. This will return all unreviewed or unmatched labs
   * for a particular provider. This request is similar to the home section in Accuro.
   *
   * @param providerId Provider ID
   *
   * @return Active labs for the requested provider.
   *
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("provider/{providerId}/lab-groups")
  @PreAuthorize("#oauth2.hasAnyScope( 'LABS_READ', 'LABS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_LABS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Labs, level = AccessLevel.ReadOnly, description = "Allows "
      + "access to the requested providers lab groups when this user has this permission for this "
      + "provider")
  @Operation(
      summary = "Retrieves active lab groups",
      description = "Gets all active labs for the given provider id. "
          + "This will return all unreviewed or unmatched labs for the particular provider. "
          + "This request is similar to the home section in Accuro.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid provider id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LabGroupDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<LabGroupDto> getActiveLabGroups(
      @Parameter(description = "Provider id") @PathParam("providerId") int providerId)
      throws ProtossException {

    if (providerId < 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid Provider Id.");
    }
    LabManager labManager = getImpl(LabManager.class);
    return mapDto(labManager.getActiveLabsForProvider(providerId), LabGroupDto.class, HashSet::new);
  }

}
