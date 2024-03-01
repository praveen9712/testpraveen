
package com.qhrtech.emr.restapi.endpoints.external;

import com.qhrtech.emr.accuro.api.identity.ExternalUserIdentityManager;
import com.qhrtech.emr.accuro.api.security.UserInfoManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.identity.ExternalUserIdentity;
import com.qhrtech.emr.accuro.model.security.UserInfo;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.external.ExternalUserIdentityDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/")
@Tag(name = "ExternalUserIdentity Endpoints",
    description = "Exposes external user identity endpoints")
public class ExternalUserIdentityEndpoint extends AbstractEndpoint {

  /**
   * Create an external user identity.
   *
   * @param externalSystemId the id of the external identity system
   * @param externalUserIdentityDto the external user identity
   * @return 201 response if success.
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @Path("/external-id-systems/{systemId}/external-id-users")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.create' ) and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Creates External User Identity",
      description = "Creates a new external user identity.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the external user identity is not given."),
          @ApiResponse(
              responseCode = "400",
              description = "If the external identity system id is different from "
                  + "the id in the path."),
          @ApiResponse(
              responseCode = "400",
              description = "The external identity system id is not valid."),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required.")})
  @Parameters({
      @Parameter(
          name = "authorization",
          description = "Okta authorization only",
          in = ParameterIn.HEADER,
          required = true),
      @Parameter(
          name = "systemId",
          description = "External system Id - External"
              + " identity system.",
          in = ParameterIn.PATH)
  })

  public Response create(@Parameter(hidden = true) @PathParam("systemId") long externalSystemId,
      @Valid ExternalUserIdentityDto externalUserIdentityDto)
      throws ProtossException {

    if (externalUserIdentityDto == null) {
      throw new IllegalArgumentException("The external user identity must be provided.");
    }

    if (externalSystemId != externalUserIdentityDto.getExtIdSystemId()) {
      throw new IllegalArgumentException("The given system id doesn't match the path id.");
    }

    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    if (manager.getIdentitySystemById(externalSystemId) == null) {
      throw new SupportingResourceNotFoundException(
          "The external identity system id is not valid.");
    }

    if (manager.getUserIdentity(externalSystemId, externalUserIdentityDto.getIdentity()) != null) {
      throw new IllegalArgumentException(
          "The given identifier exists in the given external system.");
    }

    if (notExistAccuroUserById(externalUserIdentityDto.getUserId())) {
      throw new SupportingResourceNotFoundException("The given accuro user id is not valid.");
    }

    externalUserIdentityDto.setCreatedOnInUtc(LocalDateTime.now(DateTimeZone.UTC));

    if (externalUserIdentityDto.getValidFromInUtc() == null) {
      externalUserIdentityDto.setValidFromInUtc(LocalDateTime.now(DateTimeZone.UTC));
    }

    ExternalUserIdentity identity = mapDto(externalUserIdentityDto, ExternalUserIdentity.class);
    manager.createUserIdentity(identity);
    return Response.status(Status.CREATED).build();
  }

  public boolean notExistAccuroUserById(int userId) throws DatabaseInteractionException {
    UserInfoManager userInfoManager = getImpl(UserInfoManager.class);
    UserInfo userInfo = userInfoManager.getById(userId);
    return userInfo == null ? true : false;
  }

  /**
   * Update the external user identity.
   *
   * @param externalSystemId the id of the external identity system
   * @param externalUserId the id of the external user identity
   * @param externalUserIdentityDto the external user identity
   * @return 204 response if success
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @Path("/external-id-systems/{systemId}/external-id-users/{externalUserId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.update' ) and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Updates External User Identity",
      description = "Update external user identity.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the external user identity is not given."),
          @ApiResponse(
              responseCode = "400",
              description = "If the external identity system id is different from "
                  + "the id in the path."),
          @ApiResponse(
              responseCode = "400",
              description = "If the user identifier is different from the id in the path."),
          @ApiResponse(
              responseCode = "400",
              description = "The external identity system id is not valid."),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "The external user identity not found.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Okta authorization only",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "systemId",
              description = "External system Id - External"
                  + " identity system.",
              in = ParameterIn.PATH),
          @Parameter(
              name = "externalUserId",
              description = "External User Id - External User Identifier.",
              in = ParameterIn.PATH)
      })

  public Response update(@Parameter(hidden = true) @PathParam("systemId") long externalSystemId,
      @Parameter(hidden = true) @PathParam("externalUserId") String externalUserId,
      @Valid ExternalUserIdentityDto externalUserIdentityDto)
      throws ProtossException {

    if (externalUserIdentityDto == null) {
      throw new IllegalArgumentException("The external identity system must be given.");
    }

    if (externalSystemId != externalUserIdentityDto.getExtIdSystemId()) {
      throw new IllegalArgumentException("The path id doesn't match the given system's id.");
    } else if (!Optional.ofNullable(externalUserId).orElse("")
        .equals(externalUserIdentityDto.getIdentity())) {
      throw new IllegalArgumentException("The path id doesn't match the given user identifier.");
    }

    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    if (manager.getIdentitySystemById(externalSystemId) == null) {
      throw new SupportingResourceNotFoundException(
          "The external identity system id is not valid.");
    }

    if (notExistAccuroUserById(externalUserIdentityDto.getUserId())) {
      throw new SupportingResourceNotFoundException("The given accuro user id is not valid.");
    }

    if (externalUserIdentityDto.getValidFromInUtc() == null) {
      externalUserIdentityDto.setValidFromInUtc(LocalDateTime.now(DateTimeZone.UTC));
    }

    externalUserIdentityDto.setCreatedOnInUtc(LocalDateTime.now(DateTimeZone.UTC));
    ExternalUserIdentity identity = mapDto(externalUserIdentityDto, ExternalUserIdentity.class);
    manager.updateUserIdentity(identity);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Delete the external user identity.
   *
   * @param externalSystemId the id of the external identity system
   * @param externalUserId the id of the external user identity
   * @return 204 if success.
   * @throws ProtossException If there has been a database error.
   */
  @DELETE
  @Path("/external-id-systems/{systemId}/external-id-users/{externalUserId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.delete' ) and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Deletes External User Identity",
      description = "Delete external user identity.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "The external user identity not found.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Okta authorization only",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "systemId",
              description = "External system Id - External"
                  + " identity system.",
              in = ParameterIn.PATH),
          @Parameter(
              name = "externalUserId",
              description = "External User Id - External User Identifier.",
              in = ParameterIn.PATH)
      })

  public Response delete(@Parameter(hidden = true) @PathParam("systemId") long externalSystemId,
      @Parameter(hidden = true) @PathParam("externalUserId") String externalUserId)
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    ExternalUserIdentity identity = manager.getUserIdentity(externalSystemId, externalUserId);

    if (identity == null) {
      throw new NoDataFoundException("Resource not found.");
    }

    manager.deleteUserIdentity(externalSystemId, externalUserId);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Get the external user identity.
   *
   * @param externalSystemId the id of the external identity system
   * @param externalUserId the id of the external user identity
   * @return the external user identity
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/external-id-systems/{systemId}/external-id-users/{externalUserId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.read' ) and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Retrieves External User Identity",
      description = "Retrieve external user identity.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = ExternalUserIdentityDto.class))),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "The external user identity not found.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Okta authorization only",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "systemId",
              description = "External system Id - External"
                  + " identity system.",
              in = ParameterIn.PATH),
          @Parameter(
              name = "externalUserId",
              description = "External User Id - External User Identifier.",
              in = ParameterIn.PATH)
      })

  public ExternalUserIdentityDto getByIdentifier(
      @Parameter(hidden = true) @PathParam("systemId") long externalSystemId,
      @Parameter(hidden = true) @PathParam("externalUserId") String externalUserId)
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    ExternalUserIdentity identity = manager.getUserIdentity(externalSystemId, externalUserId);

    if (identity == null) {
      throw new NoDataFoundException("Resource not found.");
    }

    return mapDto(identity, ExternalUserIdentityDto.class);
  }

  /**
   * Get a List of external user identities. Ordered by Accuro user id.
   *
   * @param accuroUserId the user id of Accuro
   * @param externalUserId the id of the external user identity
   * @param externalSystemId the id of the external identity system
   * @return a list of the external user identity.
   * @throws ProtossException If there has been a database error.
   */
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Okta authorization only",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "accuroUserId",
              description = "The user id of Accuro",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "externalUserId",
              description = "External User Id - External User Identifier.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "systemId",
              description = "External system Id - External"
                  + " identity system.",
              in = ParameterIn.QUERY)
      })
  @GET
  @Path("/external-id-users")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.User.read' ) and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Retrieves External User Identities",
      description = "Retrieve external user identities. If no filtering parameters provided, "
          + "all External User Identities records will be returned.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = ExternalUserIdentityDto.class)))),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required.")})
  public List<ExternalUserIdentityDto> getExternalUserIdentities(
      @Parameter(hidden = true) @QueryParam("accuroUserId") Integer accuroUserId,
      @Parameter(hidden = true) @QueryParam("externalUserId") String externalUserId,
      @Parameter(hidden = true) @QueryParam("systemId") Long externalSystemId)
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    List<ExternalUserIdentity> users;
    if (accuroUserId == null && StringUtils.isBlank(externalUserId) && externalSystemId == null) {
      users = manager.getUserIdentities();
    } else {
      users = manager.getUserIdentity(externalSystemId, externalUserId, accuroUserId);
    }
    return mapDto(users, ExternalUserIdentityDto.class, ArrayList::new);
  }
}
