
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.demographics.GenderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.GenderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

/**
 * This <code>GenderEndpoint</code> collection is designed to expose the Gender DTO and related
 * public endpoints. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If authorized
 * @HTTP 401 If unauthorized
 * @see com.qhrtech.emr.accuro.api.demographics.GenderManager
 * @see com.qhrtech.emr.restapi.models.dto.GenderDto
 */
@Component
@Path("/v1/genders")
@Tag(name = "Gender Endpoints",
    description = "Exposes gender endpoints.")
public class GenderEndpoint extends AbstractEndpoint {

  /**
   * Retrieve a list of all available Genders. This will include the built in Genders for male,
   * female and unknown, as well as any user defined Genders.
   *
   * @return A <code>Set<></code> of Gender DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves genders",
      description = "Gets a set of all available genders."
          + " This will include the built-in genders for male,"
          + "female and unknown, as well as any user defined genders.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "OK",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = GenderDto.class))))
      })
  public Set<GenderDto> getGenders() throws ProtossException {

    GenderManager genderManager = getImpl(GenderManager.class);
    return mapDto(genderManager.getGenders(), GenderDto.class, HashSet::new);
  }
}
