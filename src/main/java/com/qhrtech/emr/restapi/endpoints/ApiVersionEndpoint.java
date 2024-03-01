
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.demographics.GenderManager;
import com.qhrtech.emr.restapi.models.dto.GenderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.stereotype.Component;

/**
 * This endpoint is designed to retrieve and display the current version of the Accuro API build.
 * Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If authorized
 * @HTTP 401 If unauthorized
 * @see GenderManager
 * @see GenderDto
 */
@Component
@Path("/v1/api-version")
@Tag(name = "Accuro API version Endpoints",
    description = "Exposes Accuro API version Endpoints.")
public class ApiVersionEndpoint extends AbstractEndpoint {

  /**
   * Returns the current version of Accuro API build.
   *
   * @return A string which contains the current version of Accuro API.
   */
  @GET
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant.",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves the version of Accuro API build",
      description = "Retrieves the version of Accuro API build.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = String.class),
                  examples = {
                      @ExampleObject(name = "Get Accuro API version",
                          summary = "Retrieves Accuro API version.",
                          description = "Retrieves Accuro API version.",
                          value = "10.2.0")
                  }

              )

          )

      })
  public String getApiVersion() {
    String version = getClass().getPackage().getImplementationVersion();
    return version;
  }
}
