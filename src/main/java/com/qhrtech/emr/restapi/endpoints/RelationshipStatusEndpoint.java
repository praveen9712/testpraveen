
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.person.RelationshipStatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.person.RelationshipStatus;
import com.qhrtech.emr.restapi.models.dto.RelationshipStatusDto;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

/**
 * This <code>RelationshipStatusEndpoint</code> collection is designed to expose the Relationship
 * Status. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If authorized
 * @HTTP 401 If unauthorized
 */
@Component
@Path("/v1/enumerations/relationship-statuses")
@Tag(name = "Relationship status Endpoints",
    description = "Exposes Relationship status endpoints.")
public class RelationshipStatusEndpoint extends AbstractEndpoint {

  /**
   * Retrieve a list of all available Relationship statuses.
   *
   * @return A <code>List<></code> of RelationshipStatusDto DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves Relationship statuses",
      description = "Gets a set of all available Relationship statuses.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = RelationshipStatusDto.class))))
      })
  @GET
  public List<RelationshipStatusDto> getRelationshipStatuses() throws ProtossException {
    RelationshipStatusManager manager = getImpl(RelationshipStatusManager.class);

    List<RelationshipStatus> protossResults = manager.getAll();

    return mapDto(protossResults, RelationshipStatusDto.class, ArrayList::new);
  }

}
