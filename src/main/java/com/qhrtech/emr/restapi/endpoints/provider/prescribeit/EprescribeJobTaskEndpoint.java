
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobTask;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code EprescribeJobTaskEndpoint} is designed to expose {@link EprescribeJobTaskDto}
 * endpoints.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-jobs")
@Facet("provider-portal")
@Tag(name = "Eprescribe Job Task Endpoints",
    description = "Exposes Eprescribe Job Task endpoints")
public class EprescribeJobTaskEndpoint extends AbstractEndpoint {

  /**
   * Get an Eprescribe Job Task with the id.
   *
   * @param jobId Eprescribe Job ID
   * @param id Eprescribe Job Task ID
   * @return {@link EprescribeJobTaskDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{jobId}/tasks/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves Eprescribe Job Task",
      description = "Retrieves an Eprescribe Job Task associated "
          + "with the id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = EprescribeJobTaskDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public EprescribeJobTaskDto getById(
      @Parameter(description = "Eprescribe Job id") @PathParam("jobId") int jobId,
      @Parameter(description = "Eprescribe Job Task id") @PathParam("id") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {

    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);

    EprescribeJobTask eprescribeJobTaskById = manager.getEprescribeJobTaskById(id);

    if (eprescribeJobTaskById == null || eprescribeJobTaskById.getePrescribeJobId() != jobId) {
      throw new NoDataFoundException("Resource not found.");
    }

    return mapDto(eprescribeJobTaskById, EprescribeJobTaskDto.class);
  }

  /**
   * Retrive all the Eprescribe Job Task with the job id.
   *
   * @param jobId Eprescribe Job ID
   * @return List of {@link EprescribeJobTaskDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{jobId}/tasks")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves all Eprescribe Job Tasks by job id",
      description = "Retrieves Eprescribe Job Tasks associated "
          + "with the job id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = EprescribeJobTaskDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeJobTaskDto> getByJobId(
      @Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);

    List<EprescribeJobTask> eprescribeJobTaskById = manager.getAllJobTasksByJobId(jobId);

    return mapDto(eprescribeJobTaskById, EprescribeJobTaskDto.class, ArrayList::new);
  }

  /**
   * Creates the Eprescribe Job Task. The combination of Job id, Task id and Task type should not
   * already exist.
   *
   * @param jobId Eprescribe Job ID
   * @param eprescribeJobTaskDto {@link EprescribeJobTaskDto} Data transfer object
   * @return ID of the newly created record.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @POST
  @Path("/{jobId}/tasks")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.RxJob.create' ) ")
  @Operation(
      summary = "Creates new Eprescribe Job Task",
      description = "Create new Eprescribe Job Task. The combination of Job id, Task id "
          + "and Task type should not exist before this operation.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "invalid request"),
          @ApiResponse(
              responseCode = "409",
              description = "If combination of Job id, Task id "
                  + "and Task type already exists"),
          @ApiResponse(
              responseCode = "201",
              description = "created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns a new e-prescribe job task id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @Parameter(description = "Eprescribe Job id") @PathParam("jobId") int jobId,
      @Valid EprescribeJobTaskDto eprescribeJobTaskDto)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      ResourceConflictException, SupportingResourceNotFoundException {

    checkRequestBodyDto(eprescribeJobTaskDto, jobId, null);
    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);

    EprescribeJobTask eprescribeJobTask = mapDto(eprescribeJobTaskDto, EprescribeJobTask.class);

    int id = manager.createEprescribeJobTask(eprescribeJobTask);

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Updates Eprescribe Job Task.
   *
   * @param jobId Eprescribe Job ID
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @PUT
  @Path("/{jobId}/tasks/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.RxJob.update' ) ")
  @Operation(
      summary = "Updates Eprescribe Job Task",
      description = "Updates Eprescribe Job Task.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content"),
          @ApiResponse(
              responseCode = "400",
              description = "invalid request"),
          @ApiResponse(
              responseCode = "409",
              description = "If combination of Job id, Task id "
                  + "and Task type of a different job task id already exists")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response update(
      @Parameter(description = "Eprescribe Job id") @PathParam("jobId") int jobId,
      @Parameter(description = "Eprescribe Job Task id") @PathParam("id") int id,
      @Valid EprescribeJobTaskDto eprescribeJobTaskDto)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      ResourceConflictException, SupportingResourceNotFoundException {

    checkRequestBodyDto(eprescribeJobTaskDto, jobId, id);
    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);

    EprescribeJobTask eprescribeJobTask = mapDto(eprescribeJobTaskDto, EprescribeJobTask.class);

    manager.updateEprescribeJobTask(eprescribeJobTask);

    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Deletes the Eprescribe Job Task by id.
   *
   * @param jobId Eprescribe Job ID
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @DELETE
  @Path("/{jobId}/tasks/{id}")
  @PreAuthorize("#oauth2.hasScope( 'user/provider.RxJob.delete' ) ")
  @Operation(
      summary = "Deletes the Eprescribe Job Task",
      description = "Deletes the Eprescribe Job Task associated with given id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Eprescribe job task not found with given Job "
                  + "Id or ePrescribe Taks Id"),
          @ApiResponse(
              responseCode = "204",
              description = "Success")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response delete(
      @Parameter(description = "Eprescribe Job id") @PathParam("jobId") int jobId,
      @Parameter(description = "Eprescribe Job Task id") @PathParam("id") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      NoDataFoundException {

    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);

    EprescribeJobTask eprescribeJobTaskById = manager.getEprescribeJobTaskById(id);
    if (eprescribeJobTaskById == null || eprescribeJobTaskById.getePrescribeJobId() != jobId) {
      throw new NoDataFoundException("Eprescribe job task not found with given Job ID");
    }

    manager.deleteEprescribeJobTask(id);

    return Response.status(Status.NO_CONTENT).build();
  }

  public void checkRequestBodyDto(EprescribeJobTaskDto eprescribeJobTaskDto, int jobId,
      Integer taskId) {
    if (eprescribeJobTaskDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Request body cannot be null.");
    }
    if (eprescribeJobTaskDto.getErxJobId() != jobId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Job Id in path does not match the id in the body.");
    }

    if (taskId != null) {
      if (eprescribeJobTaskDto.getErxJobTaskId() != taskId) {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Job task Id in path does not match the id in the body.");
      }
    }
  }


}
