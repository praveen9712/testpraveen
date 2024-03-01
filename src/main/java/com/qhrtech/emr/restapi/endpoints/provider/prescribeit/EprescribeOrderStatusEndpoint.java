
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatus;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOrderStatusDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeOrderStatusSystem;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Endpoints for Eprescribe order status.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/eprescribe-order-statuses")
@Facet("provider-portal")
@Tag(name = "Eprescribe order status Endpoints",
    description = "Exposes eprescribe order status endpoints")
public class EprescribeOrderStatusEndpoint extends AbstractEndpoint {

  /**
   * Creates a new e-prescribe order status.
   *
   * @param statusDto Eprescribe order status data transfer object {@link EprescribeOrderStatusDto}.
   *
   * @return the e-prescribe order status id
   *
   * @HTTP 201 created
   * @HTTP 400 invalid request entity
   *
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws SupportingResourceNotFoundException if the job type does not exist or is provided as
   *         null
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Prescription.create' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full)
  @Operation(
      summary = "Creates a new e-prescribe order status",
      description = "Creates a new e-prescribe order status.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "invalid request entity"),
          @ApiResponse(
              responseCode = "201",
              description = "created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns a new e-prescribe order status id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(@RequestBody @Valid EprescribeOrderStatusDto statusDto)
      throws ForbiddenException, UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException {
    if (statusDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe order status information is missing.");
    }
    EprescribeOrderStatusManager manager = getImpl(EprescribeOrderStatusManager.class);

    int id = manager.createEprescribeOrderStatus(mapDto(statusDto, EprescribeOrderStatus.class));
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Retrieves e-prescribe order status associated with the given prescription and system.
   * Prescription Id is mandatory. System is optional and it should be included in the query
   * parameter if it is not required. System does not accept null or blank value.
   *
   *
   *
   * @param rxId Prescription ID. Required
   * @param system {@link EprescribeOrderStatusSystem} Optional.
   *
   * @return The e-prescribe job
   *
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If any required resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Prescription.read' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly)
  @Operation(
      summary = "Retrieves list of e-prescribe order status related to given prescription id "
          + "and eprescribe order status system.",
      description = "Retrieves list of e-prescribe order status related to given prescription id "
          + "and eprescribe order status system. Prescription Id is mandatory. System is optional "
          + "and it should be included in the query parameter if it is not required."
          + " System does not accept null or blank value.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "success",
              content = @Content(array = @ArraySchema(schema = @Schema(
                  implementation = EprescribeOrderStatusDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeOrderStatusDto> getById(
      @Parameter(description = "Prescription id") @QueryParam("rxId") Integer rxId,
      @QueryParam("system") EprescribeOrderStatusSystem system)
      throws InsufficientRolesException, UnsupportedSchemaVersionException,
      DatabaseInteractionException {

    if (rxId == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Prescription id is required.");
    }
    com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatusSystem protossSystem = null;
    if (system != null) {
      protossSystem =
          com.qhrtech.emr.accuro.model.eprescribe.EprescribeOrderStatusSystem.lookup(
              system.getOrderStatusSystem());
    }


    EprescribeOrderStatusManager manager = getImpl(EprescribeOrderStatusManager.class);
    return mapDto(manager.getEprescribeOrderStatusHistory(rxId, protossSystem),
        EprescribeOrderStatusDto.class, ArrayList::new);
  }

}
