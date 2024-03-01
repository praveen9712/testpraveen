
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.security.UserPermissionManager;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/users")
@Tag(name = "Copy User Permissions",
    description = "Exposes copy user permissions endpoint")
public class CopyUserPermissionsEndpoint extends AbstractEndpoint {

  private final Logger log = LoggerFactory.getLogger(getClass());

  /**
   * Copy the user permission from source user to destination user.
   *
   * If includedOfficeId query param is not passed, the permissions would be copied from source to
   * destination user for all the source user offices. If at-least one includedOfficeId query param
   * is passed, the permissions would be copied from source to destination user only for that
   * office(s). Copy permissions would be done only for the valid office Id(s) and the offices(s) to
   * which source user belong. Other office ID(s) would be ignored.
   *
   * If all the officeIds passed are invalid or does not belong to the source user, no action would
   * be taken and response would still be 204.
   *
   * includedOfficeId: The value should be numeric. Blank value is considered invalid.
   *
   * @param userId the id of the destination user
   * @param sourceUser the id of the source user
   * @return 204 response if success
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{userId}/copy-user-permissions")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.UserPermissions.update' )")
  @Operation(
      summary = "Copies user permission from source user to destination user",
      description = "Copies user permission from source user to destination user based on id."
          + "If includedOfficeId query param is not passed, the permissions would be copied from\n"
          + "source to destination user for all the source user offices.\n"
          + "If at-least one includedOfficeId query param is passed, the permissions would be "
          + "copied\n"
          + "from source to destination user only for that office(s).\n"
          + "Copy permissions would be done only for the valid office Id(s) and the offices(s) "
          + "to which\n"
          + "source user belong. Other office ID(s) would be ignored.\n"
          + "If all the officeIds passed are invalid or does not belong to the source user,"
          + " no action would\n"
          + "be taken and response would still be 204. ",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the id's are not given."),
          @ApiResponse(
              responseCode = "400",
              description = "If the source user id is not valid."),
          @ApiResponse(
              responseCode = "400",
              description = "If one or more office ID(s) are not numeric."),
          @ApiResponse(
              responseCode = "400",
              description = "The destination user id is not valid."),
          @ApiResponse(
              responseCode = "401",
              description = "Access forbidden. Permission is required.")})
  @Parameters({
      @Parameter(
          name = "authorization",
          description = "Okta authorization only",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "includedOfficeId",
          description = "The value should be numeric. Blank value is considered invalid.",
          in = ParameterIn.QUERY)
  })

  public Response copyUserPermissions(
      @Parameter(description = "Destination User id") @PathParam("userId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "Destination user id is not correct") String userId,
      @QueryParam("sourceUser") @Pattern(
          regexp = "(^-?\\d+$)",
          message = "Source user id should be numeric and is required") @NotNull(
              message = "Source user id must be provided") String sourceUser,
      @QueryParam("includedOfficeId") Set<String> includedOfficeIds)
      throws ProtossException {
    Set<Integer> officeIds = includedOfficeIds.stream()
        .filter(Objects::nonNull)
        .map(x -> {
          try {
            return Integer.parseInt(x);
          } catch (NumberFormatException e) {
            log.error("Office Id(s) should be a valid integer and not blank.");
            throw Error.webApplicationException(Status.BAD_REQUEST,
                "Office Id(s) should be a valid integer and not blank.");
          }
        })
        .collect(Collectors.toSet());
    Integer sourceUserId;
    Integer destinationUserId;
    try {
      sourceUserId = Integer.parseInt(sourceUser);
      destinationUserId = Integer.parseInt(userId);
    } catch (NumberFormatException exception) {
      log.error("Source and destination user Ids must be valid integer.");
      throw new IllegalArgumentException("Source and destination user Id must be a valid integer.");

    }

    // Check office ids
    if (officeIds.size() > 0) {
      OfficeManager officeManager = getImpl(OfficeManager.class);
      List<Integer> allOfficeIds = officeManager.getAllOffices().stream().map(
          Office::getOfficeId).collect(Collectors.toList());
      for (Integer officeId : officeIds) {
        if (!allOfficeIds.contains(officeId)) {
          log.error("Office id is invalid: " + officeId);
          throw Error.webApplicationException(Status.BAD_REQUEST,
              "Office id is invalid: " + officeId);
        }
      }
    }

    UserPermissionManager manager = getImpl(UserPermissionManager.class);
    AuditLogUser auditLogUser = getUser();
    manager.copyUserPermissions(sourceUserId, destinationUserId,
        auditLogUser, officeIds);
    return Response.status(Status.NO_CONTENT).build();
  }

}
