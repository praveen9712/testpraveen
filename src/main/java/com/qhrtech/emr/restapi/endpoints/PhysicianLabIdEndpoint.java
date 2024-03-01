
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.provider.PhysicianLabIdsManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.provider.PhysicianLab;
import com.qhrtech.emr.restapi.models.dto.PhysicianLabIdDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/providers")
@Tag(name = "Physician Lab Ids Endpoint",
    description = "Exposes physician lab Ids endpoint")
public class PhysicianLabIdEndpoint extends AbstractEndpoint {

  private static final Logger log = LoggerFactory.getLogger(PhysicianLabIdEndpoint.class);

  @GET
  @Path("/{physicianId}/lab-ids")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.read' )")
  @Operation(
      summary = "Retrieves all physician lab ids for the provided physician id",
      description = "Get all physician lab id records for the physician id provided",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PhysicianLabIdDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Okta authorization",
      in = ParameterIn.HEADER,
      required = true)
  public List<PhysicianLabIdDto> getPhysicianLabs(
      @Parameter(
          description = "The provider id") @PathParam("physicianId") Integer physicianId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    PhysicianLabIdsManager physicianLabIdsManager = getImpl(PhysicianLabIdsManager.class);

    return mapDto(
        physicianLabIdsManager.getPhysicianLabIdsByPhysicianId(physicianId),
        PhysicianLabIdDto.class, ArrayList::new);
  }

  /**
   * Creates the physician lab id record.
   *
   * @param physicianLabIdDto Physician Lab DTO
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 200 If success
   */
  @POST
  @Path("/{physicianId}/lab-ids")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.update' )")
  @Operation(
      summary = "Creates physician lab ids",
      description = "Creates physicianLabId object.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid Data"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PhysicianLabIdDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Okta authorization",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @Parameter(
          description = "The provider id") @PathParam("physicianId") Integer physicianId,
      @RequestBody(
          description = "The new physician lab id's object") PhysicianLabIdDto physicianLabIdDto)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException {
    if (physicianLabIdDto == null) {
      log.error("PhysicianLabId object is required.");
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "PhysicianLabId object is required.");
    }
    if (physicianLabIdDto.getPhysicianId() != physicianId) {
      log.error("physicianId in physicianLab does not match with the physicianId in parameter.");
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "physicianId in physicianLab does not match with the physicianId in parameter.");
    }

    PhysicianLabIdsManager physicianLabIdsManager = getImpl(PhysicianLabIdsManager.class);
    physicianLabIdsManager.createPhysicianLabIds(
        mapDto(physicianLabIdDto, PhysicianLab.class));
    return Response.status(Status.OK).build();
  }

  /**
   * Deletes the physician lab ids records
   *
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 400 Invalid data.
   * @HTTP 204 No content
   */
  @DELETE
  @Path("/{physicianId}/lab-ids")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.Provider.update' )")
  @Operation(
      summary = "Deletes physician lab records related to given ID",
      description = "Deletes physician lab records related to given ID.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content")})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(@Parameter(
      description = "The provider id") @PathParam("physicianId") Integer physicianId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      TimeZoneNotFoundException, NoDataFoundException {
    PhysicianLabIdsManager physicianLabIdsManager = getImpl(PhysicianLabIdsManager.class);
    physicianLabIdsManager.deletePhysicianLabIds(physicianId);

    return Response.status(Status.NO_CONTENT).build();
  }

}
