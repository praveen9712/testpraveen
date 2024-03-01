
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.restapi.models.dto.AccuroSettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/accuro-settings")
@Tag(name = "Accuro Settings Endpoint",
    description = "Exposes accuro settings endpoint")
public class AccuroSettingsEndpoint extends AbstractEndpoint {


  /**
   * Retrieves information about Accuro Settings.
   *
   * @return The Accuro Setting model object
   * @throws DatabaseInteractionException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves Accuro Settings",
      description = "Retrieves Accuro Settings.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = AccuroSettingsDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Client, patient, password level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PreAuthorize("#oauth2.hasScope( 'user/provider.AccuroSettings.read' )")
  public AccuroSettingsDto getAccuroSettings() throws DatabaseInteractionException {

    AccuroSettingsDto accuroSettings = new AccuroSettingsDto();
    // Time zone settings needs to be obtained from Accuro preferences tables
    AccuroPreferenceManager preferenceManager = getImpl(AccuroPreferenceManager.class);
    accuroSettings.setTimeZone(preferenceManager.getSystemPreference("TimeZone"));

    // Province and Accuro Mode would be obtained from the API context.
    accuroSettings.setProvince(getAccuroApiContext().getAccuroProvince().name());
    accuroSettings.setMode(getAccuroApiContext().getAccuroMode().toString());

    return accuroSettings;
  }


}
