
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobTask;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobTaskDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code EprescribeTaskEndpoint} is designed to expose {@link EprescribeJobTaskDto} endpoints
 * that are not already covered by {@code EprescribeJobTaskEndpoint}. More specifically, these are
 * endpoints that operate directly on the tasks themselves without relation to the job that the task
 * is associated with.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-job-tasks")
@Facet("provider-portal")
@Tag(name = "Eprescribe Task Endpoints",
    description = "Exposes Eprescribe Task endpoints")
public class EprescribeTaskEndpoint extends AbstractEndpoint {

  /**
   * Get an Eprescribe Job Task with the id.
   *
   * @param id Eprescribe Job Task ID
   * @return {@link EprescribeJobTaskDto} object.
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Path("/{id}")
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
      @Parameter(description = "Eprescribe job task id") @PathParam("id") int id)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJobTaskManager manager = getImpl(EprescribeJobTaskManager.class);
    EprescribeJobTask eprescribeJobTaskById = manager.getEprescribeJobTaskById(id);

    if (eprescribeJobTaskById == null) {
      throw new NoDataFoundException("Resource not found.");
    }

    return mapDto(eprescribeJobTaskById, EprescribeJobTaskDto.class);
  }
}
