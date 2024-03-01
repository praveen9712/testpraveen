
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.provider.Provider;
import com.qhrtech.emr.accuro.model.provider.ProviderIdentifier;
import com.qhrtech.emr.restapi.models.dto.ProviderDto;
import com.qhrtech.emr.restapi.models.dto.ProviderIdentifierDto;
import com.qhrtech.emr.restapi.models.dto.ProviderTypeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PaginationConstant;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
 * This <code>ProviderEndpoint</code> collection is designed to expose the Provider DTO and related
 * public endpoints. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.ProviderDto
 * @see com.qhrtech.emr.restapi.models.dto.ProviderTypeDto
 * @see com.qhrtech.emr.accuro.api.provider.ProviderManager
 */
@Component
@Path("/v1/providers")
@Tag(name = "Provider Endpoints",
    description = "Exposes provider endpoints")
public class ProviderEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Get a set of all providers.
   *
   * @return A set of Provider DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves providers",
      description = "Gets a set of all providers.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ProviderDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ProviderDto> getProviders() throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return mapDto(providerManager.getProviders(), ProviderDto.class, HashSet::new);
  }

  /**
   * Gets providers for all default offices.
   *
   * @return A set of {@link ProviderDto}'s
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/offices")
  @Operation(
      summary = "Retrieves all providers in offices",
      description = "Gets all providers in offices.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ProviderDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<ProviderDto> getAllOfficeProviders() throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return mapDto(providerManager.getProvidersForClient(), ProviderDto.class, ArrayList::new);
  }

  /**
   * Get's providers for a given office.
   *
   * @param officeId Office ID
   * @return A set of {@link ProviderDto}'s
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/offices/{officeId}")
  @Operation(
      summary = "Retrieves providers for the office",
      description = "Gets providers for the give office id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ProviderDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ProviderDto> getProvidersForOffice(
      @Parameter(description = "Office id") @PathParam("officeId") int officeId)
      throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return mapDto(providerManager.getProvidersForOffice(officeId), ProviderDto.class, HashSet::new);
  }

  /**
   * Get a set of provider types.
   *
   * @return A set of ProviderType DTOs.
   * @throws DataAccessException If there has been a database error.
   */

  @GET
  @Path("/types")
  @Operation(
      summary = "Retrieves provider types",
      description = "Gets a set of provider types.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ProviderTypeDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<ProviderTypeDto> getProviderTypes() throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return mapDto(providerManager.getProviderTypes(), ProviderTypeDto.class, HashSet::new);
  }

  /**
   * Get provider statuses.
   *
   * @return A map of provider statuses
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/statuses")
  @Operation(
      summary = "Retrieves provider statuses",
      description = "Gets provider statuses.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "object",
                      example = "{ { 1 : \"status1\" }, { 2 : \"status2\" } }",
                      description = "Returns a map of status id and description")))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<Integer, String> getProviderStatuses() throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return providerManager.getProviderStatuses();
  }

  /**
   * Get provider specialties. Provider specialties are not supported in British Columbia.
   *
   * @return A map of provider specialties
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/specialties")
  @Operation(
      summary = "Retrieves Specialities",
      description = "Gets provider specialties. "
          + "Provider specialties are not supported in British Columbia.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "object",
                      example = "{ { \"code1\" : \"specialty1\" }, "
                          + "{ \"code2\" : \"specialty2\" } }",
                      description = "Returns a Map of speciality id and description")))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<String, String> getSpecialties() throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return providerManager.getSpecialtyCodes();
  }

  /**
   * Get provider by id.
   *
   * @param providerId Provider Id
   * @return A Provider DTO
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{providerId}")
  @Operation(
      summary = "Retrieves the provider",
      description = "Gets the provider for the given provider id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ProviderDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ProviderDto getProviderById(
      @Parameter(description = "Provider id") @PathParam("providerId") int providerId)
      throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    return mapDto(providerManager.getProviderById(providerId), ProviderDto.class);
  }

  /**
   * Create Provider
   *
   * <p>
   * This endpoint creates a new provider, requiring first name and last name. Please ensure Title
   * and Suffix to be valid if provided as this endpoint will not do the validation. Also please
   * ensure the office id is valid and active if default Office is provided.
   * </p>
   *
   * @param providerDto Provider DTO
   * @return A provider id
   * @throws DataAccessException If there has been a database error.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.create' )")
  @Operation(
      summary = "Creates a new provider",
      description = "This endpoint creates a new provider, requiring first name and last name."
          + "Please ensure Title and Suffix to be valid if provided as this endpoint will not "
          + "do the validation. Also please ensure the office id is valid and active "
          + "if default Office is provided.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns provider id"))),
          @ApiResponse(
              responseCode = "400",
              description = "If provider details are missing."),
          @ApiResponse(
              responseCode = "400",
              description = "If provider first or last name is missing."),
          @ApiResponse(
              responseCode = "400",
              description = "If provider practitionerNumber field exceeds the max size."
                  + " Each province has different size limit on this field: BC & AB=5, "
                  + "MB=12 NS & ON=6 SK=4 "),
          @ApiResponse(
              responseCode = "201",
              description = "Created")})
  @Parameter(
      name = "authorization", description = "Okta authorization",
      in = ParameterIn.HEADER,
      required = true)
  public Response createProvider(
      @RequestBody(
          description = "New provider") @Valid ProviderDto providerDto)
      throws ProtossException {
    if (providerDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "provider details are missing.");
    }

    if (StringUtils.isBlank(providerDto.getFirstName())
        || StringUtils.isBlank(providerDto.getLastName())) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "First name and last name must be provided.");
    }

    ProviderManager providerManager = getImpl(ProviderManager.class);
    int id = providerManager.createProvider(mapDto(providerDto, Provider.class));
    return Response.status(Status.CREATED).entity(id).build();
  }


  /**
   * Link provider to multiple offices
   *
   * <p>
   * This endpoint adds the provider to the offices. Please ensure provider and offices ids are
   * valid and active as this endpoint will not do the validation.
   * </p>
   *
   * @param providerId provider id
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "link offices to provider",
      description = "This endpoint adds the provider to the offices. Please ensure provider and "
          + "offices ids are valid and active as this endpoint will not do the validation.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid provider id or office id."),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization", description = "Okta authorization",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{providerId}/offices")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.update' )")
  public Response addProviderToOffices(
      @Parameter(description = "provider id") @PathParam("providerId") int providerId,
      @RequestBody(description = "A list of office ids") List<Integer> officeList)
      throws ProtossException {

    List<Integer> officeIds = officeList == null ? null
        : officeList.stream().filter(Objects::nonNull).collect(Collectors.toList());

    if (officeIds == null || officeIds.size() == 0) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Office ids must be provided.");
    }

    ProviderManager providerManager = getImpl(ProviderManager.class);
    providerManager.addProviderToMultipleOffices(providerId, officeIds);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Get specialties for a specific provider. Provider specialties are not supported in British
   * Columbia.
   *
   * @param providerId Provider Id
   * @return A map of specialties for a specific provider.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{providerId}/specialties")
  @Operation(
      summary = "Retrieves specialities for provider",
      description = "Gets specialities for the given provider id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(type = "object",
                      example = "{ { \"code1\" : \"specialty1\" }, "
                          + "{ \"code2\" : \"specialty2\" } }",
                      description = "Returns a map of specialty id and description")))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Map<String, String> getProviderSpecialties(
      @Parameter(description = "Provider id") @PathParam("providerId") int providerId)
      throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    Map<String, String> specialtyMap = providerManager.getSpecialtyCodes();
    List<String> specialties = providerManager.getSpecialtyCodesForProvider(providerId);
    specialtyMap.keySet().retainAll(specialties);
    return specialtyMap;
  }

  @GET
  @Path("/{providerId}/identifiers")
  @Operation(
      summary = "Retrieves identifiers for provider",
      description = "Gets identifiers for the given provider id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ProviderIdentifierDto.class,
                      subTypes = ArrayList.class)))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<ProviderIdentifierDto> getProviderIdentifiers(
      @Parameter(description = "Provider id") @PathParam("providerId") int providerId)
      throws ProtossException {

    ProviderManager providerManager = getImpl(ProviderManager.class);
    List<ProviderIdentifier> listProviderIdentifier =
        providerManager.getProviderIdentifiers(providerId);
    return mapDto(listProviderIdentifier, ProviderIdentifierDto.class, ArrayList::new);

  }

  /**
   * Get all providers which meet the specified filters. The results will be provided in a paginated
   * form. Set the startingId to the {@code EnvelopeDto.lastId} of the previous page to request the
   * next page. Last id is the {@code id} of the last record of the page, and results will be
   * ordered by this field.
   *
   * <p>
   * Atleast first or last name should be provided with minimum of 2 characters. If multiple filters
   * are provided, they will be combined with AND operator.
   * </p>
   *
   * @param firstName The providers first name.
   * @param lastName The providers last name.
   * @param globalSearch min. 2 characters. Will search for firstNames or LastNames containing
   *        searched string. All other fields would be ignored if this field is passed.Optional.
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
   * @return A list of all providers ordered by id.
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "Gets all providers which meet the specified filters.",
      description = "Gets all providers which meet the specified filters. The results "
          + "will be provided in a paginated form. Set the startingId to the "
          + "{@code EnvelopeDto.lastId} of the previous page to request the next page. "
          + "Last id is the {@code id} of the last record of the page, and results will "
          + "be ordered by this field. first name or last name must pass at least two "
          + "characters. For example: 'APIClient' can be searched by "
          + "passing 'ap' or 'client'. Either First name or last name or globalSearch must be "
          + "provided.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = ProviderDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              required = true,
              description = "Client, patient, or provider level authorization grant",
              in = ParameterIn.HEADER),
          @Parameter(
              name = "firstName",
              description = "The providers firstName",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "lastName",
              description = "The providers lastName .",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "String")),
          @Parameter(
              name = "globalSearch",
              description = "Search for firstName or lastName which contains the"
                  + " searched string. When this field is provided, all other fields will be"
                  + " ignored. Minimum 2 characters are required.",
              in = ParameterIn.QUERY),
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
              description = "The starting {@code id} (exclusive) of the next page of data. "
                  + "Typically this is the {@code EnvelopeDto.lastId} from the last page.",
              in = ParameterIn.QUERY,
              example = "10")
      })
  @GET
  @Path("/search")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.read' )")
  public EnvelopeDto<ProviderDto> searchProviders(
      @Parameter(hidden = true) @QueryParam("firstName") @Size(min = 2,
          message = "firstName should have at-least 2 characters.") String firstName,
      @Parameter(hidden = true) @QueryParam("lastName") @Size(min = 2,
          message = "lastName should have at-least 2 characters.") String lastName,
      @Parameter(hidden = true) @QueryParam("globalSearch") @Size(min = 2,
          message = "globalSearch should have at-least 2 characters.") String globalSearch,
      @Parameter(hidden = true) @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @Parameter(hidden = true) @QueryParam("startingId") @Pattern(
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

    ProviderManager providerManager = getImpl(ProviderManager.class);
    Envelope<Provider> result =
        providerManager.searchProviders(firstName, lastName, globalSearch, actualStartingId,
            actualPageSize);

    EnvelopeDto<ProviderDto> envelopeDto = new EnvelopeDto<>();
    envelopeDto
        .setContents(mapDto(result.getContents(), ProviderDto.class, ArrayList::new));
    envelopeDto.setCount(result.getCount());
    envelopeDto.setTotal(result.getTotal());
    envelopeDto.setLastId(result.getLastId());

    return envelopeDto;
  }

  /**
   * Disables the provider visibility for the given physicianId.
   *
   * @param providerId The path for which provider should be updated.
   * @return A 204 response if the operation is successful.
   */
  @PUT
  @Path("/{providerId}/disable")
  @PreAuthorize("#oauth2.hasScope('user/provider.Provider.update')")
  @Operation(
      summary = "Disables Provider Visibility",
      description = "Disables Provider Visibility for the providerId provided",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "401",
              description = "Access forbidden. Permission is required.")})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)

  public Response disableProviderVisibility(
      @PathParam("providerId") int providerId)
      throws DatabaseInteractionException, TimeZoneNotFoundException {
    if (providerId < 0) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "invalid providerId");
    }

    ProviderPermissionManager manager = getImpl(ProviderPermissionManager.class);
    manager.disableVisibilityForPhysician(providerId);
    return Response.status(Status.NO_CONTENT).build();
  }

}
