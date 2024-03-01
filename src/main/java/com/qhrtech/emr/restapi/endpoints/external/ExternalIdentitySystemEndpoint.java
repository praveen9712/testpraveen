
package com.qhrtech.emr.restapi.endpoints.external;

import com.qhrtech.emr.accuro.api.identity.ExternalUserIdentityManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.identity.ExternalIdentitySystem;
import com.qhrtech.emr.accuro.model.identity.ExternalUserIdentity;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.external.ExternalIdentitySystemDto;
import com.qhrtech.emr.restapi.models.dto.external.Vendor;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code ExternalIdentitySystemEndpoint} is designed to create/retrieve external identity
 * system records.
 */
@Component
@Path("/v1/")
@Tag(name = "ExternalIdentitySystem Endpoints",
    description = "Exposes external identity system endpoints")
public class ExternalIdentitySystemEndpoint extends AbstractEndpoint {

  /**
   * Create the external identity system.
   *
   * @param externalIdentitySystemDto the external identity system
   * @return The unique id of the external identity system if success.
   * @throws ProtossException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ExternalIdentitySystem.create' ) "
      + "and #accuroapi.isOktaClient()")
  @Path("/external-id-systems")
  @Operation(
      summary = "Saves External Identity System",
      description = "Creates new external identity system.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns the external identity system id"))),
          @ApiResponse(
              responseCode = "400",
              description = "If the external identity system is not given."),
          @ApiResponse(
              responseCode = "400",
              description = "If the vendor name is not supported."),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required.")})
  @Parameter(
      name = "authorization",
      description = "Okta authorization only",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(@Valid ExternalIdentitySystemDto externalIdentitySystemDto)
      throws ProtossException {

    if (externalIdentitySystemDto == null) {
      throw new IllegalArgumentException("The external identity system must be given.");
    }

    if (Vendor.lookup(externalIdentitySystemDto.getVendorName()) == null) {
      throw new IllegalArgumentException("The vendor name is not supported");
    }

    externalIdentitySystemDto.setCreatedOnInUtc(LocalDateTime.now(DateTimeZone.UTC));
    ExternalIdentitySystem system = mapDto(externalIdentitySystemDto, ExternalIdentitySystem.class);
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    long id = manager.createIdentitySystem(system);
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Update external id system. Only name and vendor can be updated.
   *
   * @param id The unique id of the external id system
   * @param externalIdentitySystemDto The external id system
   * @return No content if success.
   * @throws ProtossException If there has been a database error.
   */
  @PUT
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ExternalIdentitySystem.update' ) "
      + "and #accuroapi.isOktaClient()")
  @Path("/external-id-systems/{systemId}")
  @Operation(
      summary = "Updates External Identity System",
      description = "Update external identity system. Only name and vendor can be updated.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content."),
          @ApiResponse(
              responseCode = "400",
              description = "If the external identity system is not provided."),
          @ApiResponse(
              responseCode = "400",
              description = "If the external identity system id is different from "
                  + "the id in the path."),
          @ApiResponse(
              responseCode = "400",
              description = "If the vendor name is not supported"),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "External identity system not found")})
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
  public Response update(@Parameter(hidden = true) @PathParam("systemId") long id,
      @Valid ExternalIdentitySystemDto externalIdentitySystemDto)
      throws ProtossException {

    if (externalIdentitySystemDto == null) {
      throw new IllegalArgumentException("The external identity system must be provided.");
    }

    if (id != externalIdentitySystemDto.getSystemId()) {
      throw new IllegalArgumentException("The given system id doesn't match the path id.");
    }

    if (Vendor.lookup(externalIdentitySystemDto.getVendorName()) == null) {
      throw new IllegalArgumentException("The vendor name is not supported");
    }

    ExternalIdentitySystem system = mapDto(externalIdentitySystemDto, ExternalIdentitySystem.class);
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    manager.updateIdentitySystem(system);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Delete the external identity system with the given id.
   *
   * @param id The unique id of the external id system
   * @return No content if success.
   * @throws ProtossException If there has been a database error.
   */
  @DELETE
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ExternalIdentitySystem.delete' ) "
      + "and #accuroapi.isOktaClient()")
  @Path("/external-id-systems/{systemId}")
  @Operation(
      summary = "Deletes External Identity System",
      description = "Delete external identity system.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content."),
          @ApiResponse(
              responseCode = "400",
              description = "Referred by external user and cannot be deleted."),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "External identity system not found")})
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
  public Response delete(@Parameter(hidden = true) @PathParam("systemId") long id)
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);

    // Check external_user_identity
    Set<ExternalUserIdentity> userIds = manager.getUserIdentityBySystemId(id);
    if (!userIds.isEmpty()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The external identity system is referred by external users and cannot be deleted.");
    }

    manager.deleteExtIdentitySystem(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Get the external identity system with the given id.
   *
   * @param id The unique id of the external id system
   * @return The the external id system
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/external-id-systems/{systemId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ExternalIdentitySystem.read' ) "
      + "and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Retrieves External Identity System",
      description = "Retrieve external identity system.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success.",
              content = @Content(
                  schema = @Schema(implementation = ExternalIdentitySystemDto.class))),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "External identity system not found")})
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
  public ExternalIdentitySystemDto getById(@Parameter(hidden = true) @PathParam("systemId") long id)
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    ExternalIdentitySystem externalIdentitySystem = manager.getIdentitySystemById(id);

    if (externalIdentitySystem == null) {
      throw new NoDataFoundException("Resource not found");
    }

    return mapDto(externalIdentitySystem, ExternalIdentitySystemDto.class);
  }

  /**
   * Get the all external identity systems.
   *
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/external-id-systems")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ExternalIdentitySystem.read' ) "
      + "and #accuroapi.isOktaClient()")
  @Operation(
      summary = "Retrieves all External Identity Systems",
      description = "Retrieve all external identity systems.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success.",
              content = @Content(array = @ArraySchema(schema = @Schema(
                  implementation = ExternalIdentitySystemDto.class)))),
          @ApiResponse(
              responseCode = "403",
              description = "Access forbidden. Permission is required."),
          @ApiResponse(
              responseCode = "404",
              description = "External identity system not found")})
  @Parameter(
      name = "authorization",
      description = "Okta authorization only",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ExternalIdentitySystemDto> getAll()
      throws ProtossException {
    ExternalUserIdentityManager manager = getImpl(ExternalUserIdentityManager.class);
    Set<ExternalIdentitySystem> systems = manager.getIdentitySystems();
    return mapDto(systems, ExternalIdentitySystemDto.class, HashSet::new);
  }
}
