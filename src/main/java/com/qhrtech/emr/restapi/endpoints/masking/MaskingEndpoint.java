
package com.qhrtech.emr.restapi.endpoints.masking;

import com.qhrtech.emr.accuro.api.masking.MaskingManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.masking.Mask;
import com.qhrtech.emr.accuro.model.masking.MaskAuthorization;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.masking.MaskAuthorizationDto;
import com.qhrtech.emr.restapi.models.dto.masking.MaskDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>MaskingEndpoint</code> collection is designed to expose the Patient masks and masks
 * authorization endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 * @see com.qhrtech.emr.accuro.api.masking.MaskingManager
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "Masking Endpoints",
    description = "Exposes patient masking endpoints")
public class MaskingEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Retrieves patient mask associated with given patient ID and mask ID.
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @return Mask associated with this patient and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient mask record does not exist in the database.
   */
  @GET
  @Path("/{patientId}/masks/{maskId}")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.read')")
  @Operation(
      summary = "Retrieves patient mask corresponding to given mask id.",
      description = "Retrieves patient mask corresponding to given mask id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Mask Not Found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MaskDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public MaskDto getPatientMask(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId)
      throws ProtossException {

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask maskById = manager.getMaskById(maskId);

    if (maskById != null && maskById.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask with id " + maskId + " not found for patient " + patientId);
    }

    return mapDto(maskById, MaskDto.class);
  }

  /**
   * Retrieves all masks of the given patient which meet the specified filters.
   *
   * @param patientId Patient ID
   * @param fieldName Name of the field. For example: STREET1 - Will retrieve all the masks with
   *        STREET1 as field combined with other supplied filters.
   * @return Mask associated with this patient and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient mask record does not exist in the database.
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @GET
  @Path("/{patientId}/masks")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.read')")
  @Operation(
      summary = "Retrieves all masks of the given patient which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is same as "
          + " **EnvelopeDto.lastId** of the previous page."
          + " Last id is the **maskId** of the last record of the page, "
          + "and results will be ordered by this field i.e **maskId**. ",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(ref = "EnvelopeDtoMaskDto")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "fieldName",
              description = "The name of the field on which mask has been applied.",
              example = "STREET1",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "The size of the pages with default value 25 and maximum value 50."
                  + " If the page size is not provided or less than 1, "
                  + " the page size will be set to default value 25."
                  + " If page size provided is more than maximum value, "
                  + " the page size will be set to the default maximum value 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "This id is **EnvelopeDto.lastId** of the previous page(request)"
                  + "It is same as the **maskId** of the last records of the previous results.",
              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<MaskDto> searchMasks(
      @Parameter(hidden = true) @PathParam("patientId") Integer patientId,
      @QueryParam("userId") Integer userId,
      @Parameter(hidden = true) @QueryParam("fieldName") String fieldName,
      @Parameter(hidden = true) @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @Parameter(hidden = true) @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws ProtossException {

    validatePatientId(patientId);
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

    MaskingManager manager = getImpl(MaskingManager.class);

    Envelope<Mask> maskEnvelope =
        manager.getMasks(patientId, userId, fieldName, actualStartingId, actualPageSize);

    EnvelopeDto<MaskDto> envelopeDto = new EnvelopeDto<>();
    envelopeDto.setContents(mapDto(maskEnvelope.getContents(), MaskDto.class, ArrayList::new));
    envelopeDto.setCount(maskEnvelope.getCount());
    envelopeDto.setTotal(maskEnvelope.getTotal());
    envelopeDto.setLastId(maskEnvelope.getLastId());

    return envelopeDto;
  }


  /**
   * Creates a new mask corresponding to the given patient.
   *
   * @param patientId Unique Patient ID
   * @param maskDto JSON representation of the maskDto fields
   * @return Response with the mask ID of the newly created resource.
   * @throws ProtossException If there is any exception occurred.
   */
  @POST
  @Path("/{patientId}/masks")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.create')")
  @Operation(
      summary = "Creates a new mask corresponding to the given patient.",
      description = "Creates a new mask corresponding to the given patient.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "201",
              description = "created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns mask id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createMask(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask") @RequestBody(
          description = "The new mask") @Valid MaskDto maskDto)
      throws ProtossException {

    if (maskDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask information is missing.");
    }
    if (patientId != maskDto.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient id in path does not match with the body.");
    }

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    String fieldName = maskDto.getFieldName();

    // If mask belong to demographic field for patient, we check if there is already present to
    // avoid duplication
    if (StringUtils.isNotBlank(fieldName)) {
      Envelope<Mask> existingMasks = manager.getMasks(patientId, null, fieldName, null, null);

      if (existingMasks.getContents().size() > 0) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Mask already exists for the patient demographics for field: " + fieldName);
      }
    }

    int maskId = manager.createMask(mapDto(maskDto, Mask.class));

    return Response.status(Status.CREATED).entity(maskId).build();

  }

  /**
   * Update a mask. Note: Reason is required only to non-owners of the mask.
   * <p>
   * Only notes and masked flag can be updated.
   * </p>
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @param reason Reason for updating the mask (Required only to non-owners of mask)
   * @param mask JSON representation of the mask fields
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 mask information missing
   * @HTTP 400 mask does not exit with the ID provided
   * @HTTP 400 Reason not supplied for non-owned mask update
   */
  @Operation(
      summary = "Updates mask",
      description = "Updates the given mask. "
          + "Note: Reason is required only to non-owners of the mask. "
          + "Only notes and masked flag can be updated. ",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "mask information missing"),
          @ApiResponse(
              responseCode = "400",
              description = "mask does not exit with the ID provided"),
          @ApiResponse(
              responseCode = "400",
              description = "Reason not supplied for non-owned mask update"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{patientId}/masks/{maskId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.update')")
  public Response updateMask(
      @Parameter(description = "Patient id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask id") @PathParam("maskId") Integer maskId,
      @Parameter(description = "Reason") @QueryParam("reason") String reason,
      @RequestBody(description = "Updated mask") @Valid MaskDto mask)
      throws ProtossException {

    if (mask == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask information is missing.");
    }
    if (mask.getMaskId() != maskId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "MaskId in path does not match with the body.");
    }
    if (patientId != mask.getPatientId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient id in path does not match with the body.");
    }

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask maskProtoss = mapDto(mask, Mask.class);

    manager.updateMask(maskProtoss, reason);

    return Response.status(Status.NO_CONTENT).build();
  }


  /**
   * Return the patient mask authorization associated to given patient ID, mask ID and authorization
   * ID.
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @param authId Authorization ID
   * @return {@link MaskAuthorizationDto} associated with this patient ID and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @GET
  @Path("/{patientId}/masks/{maskId}/authorizations/{authorizationId}")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.read')")
  @Operation(
      summary = "Retrieves patient mask authorizations corresponding to given "
          + "mask id and patient id.",
      description = "Retrieves patient mask authorizations corresponding to given "
          + "mask id and patient id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Mask authorization not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MaskAuthorizationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public MaskAuthorizationDto getMaskAuthorizationById(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId,
      @Parameter(description = "authorization Id") @PathParam("authorizationId") Integer authId)
      throws ProtossException {

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask existingMask = checkExistingMask(manager, maskId, patientId);

    List<MaskAuthorization> existingAuths = existingMask.getMaskAuthorizations();
    MaskAuthorization maskAuthorization =
        existingAuths.stream()
            .filter(t -> Optional.ofNullable(t.getId()).equals(Optional.ofNullable(authId)))
            .findAny().orElse(null);

    if (maskAuthorization == null || maskAuthorization.getMaskId() != maskId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask authorization not found.");
    }

    return mapDto(maskAuthorization, MaskAuthorizationDto.class);

  }


  /**
   * Return the patient mask authorizations associated to given patient ID and given mask ID.
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @return {@link List} of Mask authorizations associated with this patient ID and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @GET
  @Path("/{patientId}/masks/{maskId}/authorizations")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.read')")
  @Operation(
      summary = "Retrieves patient mask authorizations corresponding to given "
          + "mask id and patient id.",
      description = "Retrieves patient mask authorizations corresponding to "
          + "given mask id and patient id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MaskAuthorizationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<MaskAuthorizationDto> getMaskAuthorizations(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId)
      throws ProtossException {

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask existingMask = checkExistingMask(manager, maskId, patientId);

    List<MaskAuthorization> maskAuthorizations = existingMask.getMaskAuthorizations();

    return mapDto(maskAuthorizations, MaskAuthorizationDto.class, ArrayList::new);

  }

  /**
   * Creates mask authorization. Reason is required if the user is not the creator of the mask.
   * Cannot create authorization if the mask has already existing authorization with same user or
   * role id
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @param reason Reason (The reason for breaking a glass if user is not the creator of mask.)
   * @return {@link List} of Mask authorizations associated with this patient ID and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @POST
  @Path("/{patientId}/masks/{maskId}/authorizations")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.create')")
  @Operation(
      summary = "Creates mask authorization.",
      description = "Creates mask authorization. Reason is required if the user is not the creator "
          + "of the mask. Cannot create authorization if the mask has already existing "
          + "authorization with same user or role id",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = Integer.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createAuthorization(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId,
      @Parameter(description = "Reason") @QueryParam("reason") String reason,
      @RequestBody(
          description = "Mask authorization") @Valid MaskAuthorizationDto authorizationDto)
      throws ProtossException {

    if (authorizationDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask authorization information is missing.");
    }

    if (maskId != authorizationDto.getMaskId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask ID in path does not match with body.");
    }

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask existingMask = manager.getMaskById(maskId);

    if (existingMask == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask not found with ID " + maskId + " for patient " + patientId);
    }

    MaskAuthorization authorization = mapDto(authorizationDto, MaskAuthorization.class);

    if (existingMask.getPatientId() != patientId.intValue()) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask not found with ID " + maskId + " for patient " + patientId);
    }

    List<MaskAuthorization> maskAuthorizations = existingMask.getMaskAuthorizations();

    for (MaskAuthorization auth : maskAuthorizations) {

      if ((Optional.ofNullable(auth.getRoleId())
          .equals(Optional.ofNullable(authorizationDto.getRoleId())))
          && Optional.ofNullable(auth.getUserId())
              .equals(Optional.ofNullable(authorizationDto.getUserId()))) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Mask authorization already exists.");
      }
    }

    Integer authId =
        manager.createMaskAuthorization(authorization, reason);

    return Response.status(Status.CREATED).entity(authId).build();
  }

  /**
   * Updates the patient mask authorization associated to given patient ID and given mask ID. The
   * reason is required for breaking a glass if user is not the creator of mask.
   * <p>
   * Note:Only Until date can be updated.
   * </p>
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @param reason Reason for update (Required only for non-owners)
   * @param authorizationDto Authorization dto
   * @return {@link List} of Mask authorizations associated with this patient ID and mask ID
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Reason not supplied for non-owned mask update
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @PUT
  @Path("/{patientId}/masks/{maskId}/authorizations/{authorizationId}")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.update')")
  @Operation(
      summary = "Updates mask authorization.",
      description = "Update mask authorization. Note: Only Until date can be updated. "
          + "The reason is required for breaking a glass if user is not the creator of mask.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "Reason not supplied for non-owned mask update")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response updateAuthorization(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId,
      @Parameter(
          description = "Authorization Id") @PathParam("authorizationId") Integer authorizationId,
      @Parameter(description = "Reason") @QueryParam("reason") String reason,
      @RequestBody(
          description = "Mask authorization") @Valid MaskAuthorizationDto authorizationDto)
      throws ProtossException {

    if (authorizationDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask authorization information is missing.");
    }

    if (maskId != authorizationDto.getMaskId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Mask id in the path does not match the id in the body.");
    }

    if (!Optional.ofNullable(authorizationId)
        .equals(Optional.ofNullable(authorizationDto.getId()))) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Authorization id in the path does not match the id in the body.");
    }

    if (authorizationDto.getUserId() != null && authorizationDto.getRoleId() != null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Either user ID, or role ID should be provided, not both.");
    }

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    checkExistingMask(manager, maskId, patientId);

    manager.updateMaskAuthorization(mapDto(authorizationDto, MaskAuthorization.class), reason);

    return Response.status(Status.NO_CONTENT).build();
  }


  /**
   * Deletes the patient mask authorization associated to given patient ID and given mask ID. The
   * reason is required for breaking a glass if user is not the creator of mask.
   * <p>
   * Note:Only Until date can be updated.
   * </p>
   *
   * @param patientId Patient ID
   * @param maskId Mask ID
   * @param reason Reason for update (Required only for non-owners)
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the masking record does not exist in the database.
   */
  @DELETE
  @Path("/{patientId}/masks/{maskId}/authorizations/{authorizationId}")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.delete')")
  @Operation(
      summary = "Creates mask authorization.",
      description = "Creates mask authorization",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "404",
              description = "Mask/Mask Authorization not found.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response deleteAuthorization(
      @Parameter(description = "Patient Id") @PathParam("patientId") Integer patientId,
      @Parameter(description = "Mask Id") @PathParam("maskId") Integer maskId,
      @Parameter(
          description = "Authorization Id") @PathParam("authorizationId") Integer authorizationId,
      @Parameter(description = "Reason") @QueryParam("reason") String reason)
      throws ProtossException {

    validatePatientId(patientId);
    MaskingManager manager = getImpl(MaskingManager.class);

    Mask existingMask = checkExistingMask(manager, maskId, patientId);

    List<MaskAuthorization> existingAuths = existingMask.getMaskAuthorizations();

    if ((existingAuths != null)
        && existingAuths.stream().anyMatch(t -> t.getId() == authorizationId.intValue())) {
      manager.deleteMaskAuthorization(authorizationId, reason);
    } else {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Authorization " + authorizationId + " not found for mask with id " + maskId);
    }

    return Response.status(Status.NO_CONTENT).build();
  }

  private Mask checkExistingMask(MaskingManager manager, Integer maskId, Integer patientId)
      throws DatabaseInteractionException, ForbiddenException, UnsupportedSchemaVersionException {

    Mask existingMask = manager.getMaskById(maskId);

    if (existingMask == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask not found with id: " + maskId);
    }

    if (existingMask.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Mask not found for patient " + patientId + " with mask id: " + maskId);
    }
    return existingMask;
  }

  private void validatePatientId(Integer patientId) throws InsufficientRolesException,
      UnsupportedSchemaVersionException, DatabaseInteractionException,
      InsufficientPermissionsException,
      InsufficientFeatureAccessException {
    PatientManager patientManager = getImpl(PatientManager.class);
    Patient patientById = patientManager.getPatientById(patientId == null ? -1 : patientId);
    if (patientById == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Patient not found with Id: " + patientId);
    }
  }
}
