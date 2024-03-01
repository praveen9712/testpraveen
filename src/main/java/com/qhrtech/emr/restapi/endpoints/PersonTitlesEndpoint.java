
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.demographics.PersonTitleManager;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.ProviderDto;
import com.qhrtech.emr.restapi.models.dto.ProviderTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

/**
 * This <code>PersonTitlesEndpoint</code> collection is designed to expose the person titles
 * endpoints. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see ProviderDto
 * @see ProviderTypeDto
 * @see ProviderManager
 */
@Component
@Path("/v1/enumerations/person-titles")
@Tag(name = "Person Titles Endpoint",
    description = "Exposes person titles endpoints")
public class PersonTitlesEndpoint extends AbstractEndpoint {

  /**
   * Get a list of all available person titles (both built-in and custom).
   *
   * @return A list of all available person titles (String)
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves available person titles",
      description = "Retrieves a list person titles(both built-in and custom).",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<String> getPersonTitles() throws ProtossException {

    PersonTitleManager personTitleManager = getImpl(PersonTitleManager.class);
    return personTitleManager.search(null);
  }

}
