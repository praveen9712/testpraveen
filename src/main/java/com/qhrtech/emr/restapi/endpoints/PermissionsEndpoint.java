
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.restapi.models.dto.PermissionsType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PermissionsEndpoint</code> collection is designed to expose provider permissions
 * endpoints.
 *
 * @RequestHeader Authorization Provider Credentials bearer token.
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component
@Path("/v1/")
@Tag(name = "Provider permissions Endpoints",
    description = "Exposes provider permissions endpoints")
public class PermissionsEndpoint extends AbstractEndpoint {

  /**
   * Get permissible provider ids for a provided permissions type. If the access token is without
   * user (e.g client credentials grant), empty list is returned.
   *
   * @param permissionsType {@link PermissionsType}
   * @return A list of all provider ids.
   * @responseExample application/json [1223, 1224, 1225]
   */
  @Operation(
      summary = "Get permissible provider ids for a provided permissions type.",
      description = "Get all providers for the logged in user to access a feature/resource "
          + "for a provided type.The type determines feature/resource."
          + "If the access token is without user (e.g client credentials grant),"
          + " empty list is returned.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = Integer.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              required = true,
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER),
          @Parameter(
              name = "permissionsType",
              description = "Permissions access type for.",
              in = ParameterIn.QUERY,
              schema = @Schema(
                  allowableValues = {"READ_APPOINTMENTS", "WRITE_APPOINTMENTS"}))
      })
  @GET
  @Path("/accessible-providers")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ProviderPermissions.read')")
  public List<Integer> getAccessibleProviders(
      @Parameter(hidden = true) @QueryParam("permissionsType") PermissionsType permissionsType) {
    if (permissionsType == null) {
      throw new IllegalArgumentException("permissionsType is required.");
    }
    ProviderPermissionManager providerPermissionManager = getImpl(ProviderPermissionManager.class);
    Set<Integer> result = providerPermissionManager.getAccessibleProvidersForUser(
        com.qhrtech.emr.accuro.permissions.PermissionsType.valueOf(permissionsType.name()));

    if (result == null) {
      return Collections.emptyList();
    }

    return result.stream().sorted().collect(Collectors.toList());
  }

}
