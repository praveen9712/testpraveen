
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.messaging.eprescription.DispenseNotificationManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.messaging.eprescription.prescribeIt.DispenseNotification;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.dataaccess.exception.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import javassist.NotFoundException;
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
 * This {@code DispenseNotificationEndpoint} is designed to expose {@link DispenseNotificationDto}
 * endpoints.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/dispense-notifications")
@Facet("provider-portal")
@Tag(name = "DispenseNotification Endpoints",
    description = "Exposes DispenseNotification endpoints")
public class DispenseNotificationEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Retrieves Dispense Notification with the id.
   *
   * @param id dispense notification id
   * @return {@link DispenseNotificationDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxDispenseNotification.read' ) ")
  @Operation(
      summary = "Retrieves Dispense Notification",
      description = "Retrieves Dispense Notification associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = DispenseNotificationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public DispenseNotificationDto getById(@Parameter(
      description = "Dispense Notification id") @PathParam("id") int id)
      throws InsufficientRolesException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, NotFoundException, NoDataFoundException {

    DispenseNotificationManager manager = getImpl(DispenseNotificationManager.class);
    DispenseNotification dispenseNotification = manager.getDispenseNotification(id);
    DispenseNotificationDto dispenseNotificationDto =
        mapDto(dispenseNotification, DispenseNotificationDto.class);

    if (dispenseNotification.isCancelled()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource not found.");
    }
    return dispenseNotificationDto;
  }

  /**
   * Creates dispense notification.
   * <p>
   * Note: Either of the authorizingRequestUuid or prescribeItAuthorizingRequestUuid is required and
   * both of them are mutually exclusive.
   * </p>
   * <p>
   * Field created date will be set to current UTC time.
   * </p>
   *
   * @param dispenseNotificationDto {@link DispenseNotificationDto} data transfer object
   * @return Id for the created dispense notification.
   * @throws ProtossException If there has been a database error.
   * @HTTP 400 Bad request if violate field validations.
   */
  @POST
  @PreAuthorize("#oauth2.hasScope( 'user/provider.ErxDispenseNotification.create' )")
  @Operation(
      summary = "Creates dispense notification.",
      description = "Creates dispense notification corresponding to given renewal request."
          + " Field created date will be set to current UTC time, "
          + "Either of the authorizingRequestUuid or prescribeItAuthorizingRequestUuid is required "
          + "and both of them are mutually exclusive.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "integer"),
                  examples = @ExampleObject(
                      name = "Response Example",
                      value = "1",
                      summary = "The ID of the newly created Dispense Notification"))),
          @ApiResponse(
              responseCode = "400",
              description = "If violate field validations.")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + " with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true)})
  public Response create(
      @Parameter(description = "Dispense Notification") @Valid @RequestBody(
          description = "Dispense Notification") DispenseNotificationDto dispenseNotificationDto)
      throws DatabaseInteractionException, InsufficientPermissionsException,
      InsufficientRolesException, InsufficientFeatureAccessException, TimeZoneNotFoundException,
      SupportingResourceNotFoundException, UnsupportedSchemaVersionException {

    DispenseNotificationManager manager = getImpl(DispenseNotificationManager.class);
    DispenseNotification notification = mapDto(dispenseNotificationDto, DispenseNotification.class);
    int notificationId = manager.createDispenseNotification(notification);
    return Response.status(Status.CREATED).entity(notificationId).build();
  }

  /**
   * Search {@link DispenseNotificationDto dispense notifications}. The identifier is exact search.
   *
   * @param prescriptionUuid The prescription authorizing request UUID as String. Optional. It is
   *        exact search.
   * @param patientUuidString The patient UUID. Optional. It is exact search.
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
   * @return The list of the {@link DispenseNotificationDto dispense notifications}
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 200 Success
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxDispenseNotification.read' ) ")
  @Operation(
      summary = "Retrieves all dispense notifications which meet the specified filters",
      description = "The results will be provided in a paginated form. "
          + " To request the next page, specify the startingId which is same as "
          + " **Envelope.lastId** of the previous page."
          + " Last id is the **dispenseId** of the last record of the page, "
          + "and results will be ordered by this field i.e **dispenseId**. "
          + "Identifier searches are exact search.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = DispenseNotificationDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + " with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "prescriptionUuid",
              description = "The prescription authorizing request"
                  + " UUID as String. Optional. It is exact search and search is on"
                  + "either ePrescribe or prescribeit authorizing request_uuid.",
              example = "7dfbc81e-5e18-4d24-9750-f884869b8855",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "patientUuid",
              description = "The patient UUID. Optional. It is exact search.",
              example = "7dfbc81e-5e18-4d24-9750-f884869b8855",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "identifierSystem",
              description = "The dispense notification identifier system. Optional. "
                  + "It is exact search.",
              example = "identifierSystemString",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "identifierValue",
              description = "The dispense notification identifier value. Optional."
                  + " It is exact search.",
              example = "identifierValueString",
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
              description = "This id is **Envelope.lastId** of the previous page(request)"
                  + "It is same as the **dispenseId** of the last records of the previous results.",
              in = ParameterIn.QUERY)
      })
  public EnvelopeDto<DispenseNotificationDto> getDispenseNotifications(
      @Parameter(hidden = true) @QueryParam("prescriptionUuid") String prescriptionUuid,
      @Parameter(hidden = true) @QueryParam("patientUuid") String patientUuidString,
      @Parameter(hidden = true) @QueryParam("identifierValue") String identifierValue,
      @Parameter(hidden = true) @QueryParam("identifierSystem") String identifierSystem,
      @Parameter(hidden = true) @QueryParam("pageSize") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "pageSize field can only have numbers") String pageSize,
      @Parameter(hidden = true) @QueryParam("startingId") @Pattern(
          regexp = "(^-?\\d+$|^$)",
          message = "startingId field can only have numbers") String startingId)
      throws com.qhrtech.emr.accuro.model.exceptions.ProtossException {
    Integer actualPageSize =
        StringUtils.isBlank(pageSize) ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSize);

    if (actualPageSize <= 0) {
      actualPageSize = DEFAULT_PAGE_SIZE;
    } else if (actualPageSize > MAX_PAGE_SIZE) {
      actualPageSize = MAX_PAGE_SIZE;
    }
    UUID rxUuid = null;
    if (prescriptionUuid != null) {
      rxUuid = UUID.fromString(prescriptionUuid);
    }
    UUID patientUuid = null;
    if (patientUuidString != null) {
      patientUuid = UUID.fromString(patientUuidString);
    }

    Integer actualStartingId =
        StringUtils.isBlank(startingId) ? null : Integer.parseInt(startingId);

    DispenseNotificationManager manager = getImpl(DispenseNotificationManager.class);
    // we are not searching by prescription Id as there is no such requirement for this endpoint.
    // search by prescription Id is requirement in Accuro that is why it is added in Accuro.
    Envelope<DispenseNotification> dispenseNotification =
        manager.getDispenseNotifications(rxUuid != null ? rxUuid.toString() : null, patientUuid,
            null, actualStartingId, actualPageSize, false, identifierValue, identifierSystem);

    EnvelopeDto<DispenseNotificationDto> result = new EnvelopeDto<>();
    result.setContents(
        mapDto(dispenseNotification.getContents(), DispenseNotificationDto.class, ArrayList::new));
    result.setCount(dispenseNotification.getCount());
    result.setTotal(dispenseNotification.getTotal());
    result.setLastId(dispenseNotification.getLastId());
    return result;
  }

  /**
   * Deletes the Dispense Notification associated with the given ID.
   *
   * @param id Id of the Dispense Notification
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @DELETE
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxDispenseNotification.delete' ) ")
  @Operation(
      summary = "Deletes the dispense notification associated with the given ID.",
      description = "Deletes the dispense notification associated with the given ID.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public void delete(
      @Parameter(description = "Dispense Notification id") @PathParam("id") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException, TimeZoneNotFoundException, InsufficientRolesException,
      NotFoundException {

    DispenseNotificationManager manager = getImpl(DispenseNotificationManager.class);
    getById(id);

    manager.cancelDispenseNotification(id);

  }

  /**
   * Updates Dispense Notification.
   *
   * @param id Dispense Notification id
   * @param dispenseNotificationDto JSON representation of the Dispense Notification
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 201 OK after update
   * @HTTP 400 invalid request entity
   */
  @PUT
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.ErxDispenseNotification.update' ) ")
  @Operation(
      summary = "Updates Dispense Notification",
      description = "Updates Dispense Notification.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid request.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public void update(@PathParam("id") int id,
      @RequestBody(
          description = "Notification") @Valid DispenseNotificationDto dispenseNotificationDto)
      throws UnsupportedSchemaVersionException,
      DatabaseInteractionException, NoDataFoundException,
      TimeZoneNotFoundException, InsufficientPermissionsException, InsufficientRolesException,
      SupportingResourceNotFoundException, NotFoundException {
    if (dispenseNotificationDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Dispense Notification information is missing.");
    }
    if (dispenseNotificationDto.getId() != id) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "id from URL path should match with the id in the request");
    }

    if (dispenseNotificationDto.getNotes() != null) {
      dispenseNotificationDto.getNotes().removeAll(Collections.singletonList(null));
    }

    DispenseNotificationManager manager = getImpl(DispenseNotificationManager.class);
    DispenseNotificationDto dispenseNotification = getById(id);
    dispenseNotificationDto.setCancelled(dispenseNotification.isCancelled());

    manager.updateDispenseNotification(
        mapDto(dispenseNotificationDto, DispenseNotification.class));

  }


}
