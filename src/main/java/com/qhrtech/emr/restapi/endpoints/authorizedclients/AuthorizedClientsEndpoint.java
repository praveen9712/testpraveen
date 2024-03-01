
package com.qhrtech.emr.restapi.endpoints.authorizedclients;

import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.authorizedclients.AuthorizedClientDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.tasks.UserTaskDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>AuthorizedClientsEndpoint</code> collection is designed to expose api authorized
 * clients endpoints.
 *
 * @RequestHeader Authorization Provider Credentials bearer token.
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component
@Path("/v1/authorized-clients")
@Facet("provider-portal")
@Tag(name = "Authorized Clients Endpoints", description = "Exposes api authorized clients.")
public class AuthorizedClientsEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Get all authorized clients which meet the specified filters. The results will be provided in a
   * paginated form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to
   * request the next page. Last id is the {@code id} of the last record of the page, and results
   * will be ordered by this field.
   *
   * <p>
   * All of the parameters are optional. All existing authorized clients will be returned if none of
   * the params are provided. If multiple filters are provided, they will be combined with AND
   * operator.
   * </p>
   * <p>
   * Client name can be searched with wild card starting with parameter characters. For example:
   * "APIClient" can be searched by passing "api" and not "client".
   * </p>
   *
   * @param clientId The authorized client id.
   * @param clientName The authorized client name.
   * @param clientUuid UUID for authorized client.
   * @param serviceUserId Service user Id.
   * @param pageSize The size of the pages with default value 25 and maximum value 50.
   *        <p>
   *        If the page size is not provided or less than 1, the page size will be set to default
   *        value 25.
   *        </p>
   *        <p>
   *        If page size provided is more than maximum value, the page size will be set to the
   *        default maximum value 50.
   *        </p>
   * @param startingId The starting {@code id} (exclusive) of the next page of data. Typically this
   *        is the {@code EnvelopeDto.lastId} from the last page.
   * @return A list of all tasks descending ordered by id.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "Gets all authorized clients which meet the specified filters.",
      description = "Gets all authorized clients which meet the specified filters. The results "
          + "will be provided in a paginated form. Set the startingId to the "
          + "**Envelope.lastId** of the previous page to request the next page. "
          + "Last id is the **id** of the last record of the page, and results will "
          + "be ordered by this field. Client name can be searched with wild card starting "
          + "with parameter characters. For example: 'APIClient' can be searched by "
          + "passing 'api' and not 'client'.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AuthorizedClientDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              required = true,
              description = "Provider credentials bearer token",
              in = ParameterIn.HEADER),
          @Parameter(
              name = "clientName",
              description = "Authorized client name",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "clientId",
              description = "The authorization client Id.",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "clientUuid",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "serviceUserId",
              description = "Service user id.",
              in = ParameterIn.QUERY,
              example = "1200"),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50. "
                  + "<p>If the page size is not provided or less than 1, the page size will be "
                  + "set to default value 25.</p><p>If page size provided is more than maximum "
                  + "value, the page size will be set to the default maximum value 50.</p>",
              in = ParameterIn.QUERY,
              example = "25"),
          @Parameter(
              name = "startingId",
              description = "The starting id (exclusive) of the next page of data."
                  + "Typically this is the Envelope.lastId from the last page.",
              in = ParameterIn.QUERY,
              example = "10")
      })
  @GET
  @PreAuthorize("#oauth2.hasScope( 'user/provider.AuthorizedClient.read')")
  public EnvelopeDto<AuthorizedClientDto> getAuthorizedClient(
      @QueryParam("clientName") String clientName,
      @QueryParam("clientId") String clientId,
      @QueryParam("clientUuid") String clientUuid,
      @QueryParam("serviceUserId") Integer serviceUserId,
      @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Integer actualPageSize =
        StringUtils.isBlank(pageSize)
            ? DEFAULT_PAGE_SIZE
            : Integer.parseInt(pageSize);

    if (actualPageSize <= 0) {
      actualPageSize = DEFAULT_PAGE_SIZE;
    } else if (actualPageSize > MAX_PAGE_SIZE) {
      actualPageSize = MAX_PAGE_SIZE;
    }
    Integer actualStartingId = StringUtils.isBlank(startingId)
        ? null
        : Integer.parseInt(startingId);

    AuthorizedClientManager manager = getImpl(AuthorizedClientManager.class);
    UUID protossUuid = null;
    if (StringUtils.isNotBlank(clientUuid)) {
      protossUuid = UUID.fromString(clientUuid);
    }

    Envelope<AuthorizedClient> result =
        manager.search(clientId, protossUuid, clientName, serviceUserId, actualStartingId,
            actualPageSize);

    EnvelopeDto<AuthorizedClientDto> envelopeDto = new EnvelopeDto<>();
    envelopeDto
        .setContents(mapDto(result.getContents(), AuthorizedClientDto.class, ArrayList::new));
    envelopeDto.setCount(result.getCount());
    envelopeDto.setTotal(result.getTotal());
    envelopeDto.setLastId(result.getLastId());

    return envelopeDto;
  }


  /**
   * Return the authorized client associated with this ID
   *
   * @param id AuthorizedClient ID
   * @return Authorized client associated with this ID
   * @throws ProtossException If there has been a database error.
   * @HTTP 404 If the record does not exist in the database.
   * @HTTP 200 Success
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.AuthorizedClient.read' )")
  @Operation(
      summary = "Retrieves authorized client",
      description = "Retrieves authorized client associated with the id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Authorized client not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = AuthorizedClientDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public AuthorizedClientDto getById(
      @Parameter(description = "AuthorizedClient id") @PathParam("id") Integer id)
      throws ProtossException {

    AuthorizedClientManager manager = getImpl(AuthorizedClientManager.class);
    return mapDto(manager.getAuthorizedClientById(id), AuthorizedClientDto.class);
  }

  /**
   * Creates the authorized client
   *
   * <p>
   * Note: Only expected fields are clientId and clientName, rest of the fields will be ignored if
   * passed and are handled internally.
   * </p>
   *
   * @param authorizedClientDto Authorized client DTO
   * @return Authorized client object
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 200 If success
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.AuthorizedClient.create' )")
  @Operation(
      summary = "Creates authorized client",
      description = "Creates authorized client. Note: Only expected fields are clientId and "
          + "clientName, rest of the fields will be ignored if passed and are handled internally.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = AuthorizedClientDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public AuthorizedClientDto create(
      @RequestBody(
          description = "The new authorized client") @Valid AuthorizedClientDto authorizedClientDto)
      throws ProtossException {
    if (authorizedClientDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Authorized client information is missing.");
    }
    AuthorizedClientManager manager = getImpl(AuthorizedClientManager.class);
    int id =
        manager.createAuthorizedClient(mapDto(authorizedClientDto, AuthorizedClient.class));
    AuthorizedClient authorizedClient = manager.getAuthorizedClientById(id);
    return mapDto(authorizedClient, AuthorizedClientDto.class);
  }

  /**
   * Updates the authorized client
   *
   * <p>
   * Note: Only fields allowed to be updated are: clientName, clientId, serviceUserId
   * </p>
   *
   * @param authorizedClientDto Authorized client DTO
   * @return Authorized client object
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 204 No content
   */
  @PUT
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Operation(
      summary = "Updates authorized client",
      description = "Updates authorized client. Note: Only fields allowed to be updated are: "
          + "clientName, clientId, serviceUserId.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Data"),
          @ApiResponse(
              responseCode = "204",
              description = "No content",
              content = @Content(
                  schema = @Schema(implementation = AuthorizedClientDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response update(
      @PathParam("id") Integer id,
      @RequestBody(
          description = "The new authorized client") @Valid AuthorizedClientDto authorizedClientDto)
      throws ProtossException {
    if (authorizedClientDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Authorized client information is missing.");
    }
    if (id != authorizedClientDto.getId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Id in path does not match with the body.");
    }
    AuthorizedClientManager manager = getImpl(AuthorizedClientManager.class);

    manager.updateAuthorizedClient(mapDto(authorizedClientDto, AuthorizedClient.class));
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Deletes the authorized client
   *
   * @param id Authorized client ID
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 204 No content
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.AuthorizedClient.delete' )")
  @Operation(
      summary = "Deletes authorized client related to given ID",
      description = "Deletes authorized client related to given ID.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(
      @PathParam("id") Integer id)
      throws ProtossException {
    AuthorizedClientManager manager = getImpl(AuthorizedClientManager.class);

    manager.deleteAuthorizedClient(id);
    return Response.status(Status.NO_CONTENT).build();
  }


}
