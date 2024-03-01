
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.security.UserPreferencesManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/users")
@Tag(name = "Copy User Preferences",
    description = "Exposes copy user preferences endpoint")
public class CopyUserPreferenceEndpoint extends AbstractEndpoint {

  /**
   * Copy the user preferences from source user to destination user.
   *
   * @param userId the id of the destination user
   * @param sourceUser the id of the source user
   * @return 204 response if success
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{userId}/copy-user-preferences")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.UserPreference.update' )")
  @Operation(
      summary = "Copies user preferences from source user to destination user",
      description = "Copies user preferences from source user to destination user based on id.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the source/destination user id is missing/not valid."),
          @ApiResponse(
              responseCode = "401",
              description = "Access forbidden. Permission is required.")})
  @Parameter(
      name = "authorization",
      description = "Okta authorization only",
      in = ParameterIn.HEADER,
      required = true)
  public Response copyUserPreferences(
      @Parameter(description = "Destination User id") @PathParam("userId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "Destination user id is not correct") String userId,
      @QueryParam("sourceUser") @Pattern(
          regexp = "(^-?\\d+$)",
          message = "Source user id should be numeric and is required") @NotNull(
              message = "Source user id must be provided") String sourceUser)
      throws ProtossException {

    UserPreferencesManager manager = getImpl(UserPreferencesManager.class);
    manager.copyUserPreferences(Integer.parseInt(sourceUser), Integer.parseInt(userId));
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
