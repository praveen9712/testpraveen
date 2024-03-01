
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.security.AccuroUserValidationException;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.models.dto.AccuroUserDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PhoneNumberFormatterUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>AccuroUserEndpoint</code> collection is designed to expose Accuro user endpoints.
 *
 * @RequestHeader Authorization Provider, Client Credentials bearer token.
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component
@Path("/v1/users")
@Tag(name = "AccuroUser Endpoints",
    description = "Exposes Accuro user endpoints")
public class AccuroUserEndpoint extends AbstractEndpoint {

  /**
   * Creates the Accuro user
   *
   * @param accuroUserDto Accuro user DTO
   * @return Accuro user ID
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 200 If success
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.User.create' )")
  @Operation(
      summary = "Creates an Accuro user",
      description = "Creates an Accuro user.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Data"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = AccuroUserDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Client, provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @RequestBody(
          description = "Accuro user - userId is not required") @Valid AccuroUserDto accuroUserDto)
      throws ProtossException {
    if (accuroUserDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Accuro user information is missing.");
    }
    // set the user ID zero :
    accuroUserDto.setUserId(0);
    formatPhoneNumbers(accuroUserDto);
    AccuroUserManager manager = getImpl(AccuroUserManager.class);
    AuditLogUser auditLogUser = getUser();
    try {
      int id = manager.createAccuroUser(mapDto(accuroUserDto, AccuroUser.class), auditLogUser);
      return Response.status(Status.CREATED).entity(id).build();
    } catch (AccuroUserValidationException exp) {
      throw Error.webApplicationException(Status.BAD_REQUEST, exp.getMessage());
    }
  }

  /**
   * Updates Accuro user.
   * <p>
   * Setting the field AccuroUserDto.active to false is deactivating the user.
   * </p>
   *
   * @param userId the id of the destination user
   * @param accuroUserDto Accuro user DTO
   * @return 204 response if success
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/{userId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.update' )")
  @Operation(
      summary = "Updates an existing Accuro user",
      description = "Updates an Accuro user based on id."
          + " Setting the field AccuroUserDto.active to false is deactivating the user.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the id is not given."),
          @ApiResponse(
              responseCode = "401",
              description = "Access forbidden. Permission is required.")})
  @Parameter(
      name = "authorization",
      description = "Client, provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  public Response updateAccuroUser(@PathParam("userId") Integer userId,
      @RequestBody(
          description = "Accuro user") @Valid AccuroUserDto accuroUserDto)
      throws ProtossException {

    if (accuroUserDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Accuro user information is missing.");
    }
    if (userId != accuroUserDto.getUserId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Id in path does not match with the body.");
    }
    formatPhoneNumbers(accuroUserDto);
    AccuroUserManager manager = getImpl(AccuroUserManager.class);
    AuditLogUser auditLogUser = getUser();
    try {
      manager.updateAccuroUser(mapDto(accuroUserDto, AccuroUser.class), auditLogUser);
    } catch (AccuroUserValidationException exp) {
      throw Error.webApplicationException(Status.BAD_REQUEST, exp.getMessage());
    }
    return Response.status(Status.NO_CONTENT).build();
  }

  private void formatPhoneNumbers(AccuroUserDto accuroUserDto) {

    if (accuroUserDto.getCellPhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getCellPhone().getNumber())) {
        accuroUserDto.getCellPhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getCellPhone().getNumber()));
      }
    }
    if (accuroUserDto.getHomePhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getHomePhone().getNumber())) {
        accuroUserDto.getHomePhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getHomePhone().getNumber()));
      }
    }
    if (accuroUserDto.getWorkPhone() != null) {
      if (StringUtils.isNotBlank(accuroUserDto.getWorkPhone().getNumber())) {
        accuroUserDto.getWorkPhone().setNumber(
            PhoneNumberFormatterUtils.formatPhoneNumber(accuroUserDto.getWorkPhone().getNumber()));
      }
    }
  }

}
