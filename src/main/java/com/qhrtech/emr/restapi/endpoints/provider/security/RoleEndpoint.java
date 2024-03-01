
package com.qhrtech.emr.restapi.endpoints.provider.security;

import com.qhrtech.emr.accuro.api.security.roles.RoleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.roles.Role;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.security.RoleDto;
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
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve role information.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/roles")
@Facet("provider-portal")
@Tag(name = "Role Endpoints",
    description = "Exposes role endpoints")
public class RoleEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all existing active roles.
   *
   * @return The set of roles
   *
   * @throws ProtossException If there was a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves all existing active roles",
      description = "Gets a set of all existing active roles.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = RoleDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<RoleDto> getAll() throws ProtossException {
    RoleManager roleManager = getImpl(RoleManager.class);
    return mapDto(
        roleManager.getAllRoles().stream().filter(Role::isActive).collect(Collectors.toSet()),
        RoleDto.class, HashSet::new);
  }

  /**
   * "Gets the active role by the given role id.
   *
   * @param id The role id
   *
   * @return The role
   *
   * @throws ProtossException If there was a database error.
   *
   * @HTTP 404 Not found
   */
  @GET
  @Path("/{id}")
  @Operation(
      summary = "Retrieves a single role",
      description = "Gets the role by the given role id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = RoleDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Not found")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public RoleDto getById(
      @Parameter(description = "The role id") @PathParam("id") int id)
      throws ProtossException {
    RoleManager roleManager = getImpl(RoleManager.class);
    return mapDto(roleManager.getRoleById(id), RoleDto.class);
  }
}
