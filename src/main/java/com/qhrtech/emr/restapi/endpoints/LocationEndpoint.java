
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.demographics.LocationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.LocationDto;
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
 * This <code>LocationEndpoint</code> collection is designed to expose the Location DTO and related
 * public endpoints. Provides methods for returning location information. Requires client, provider,
 * or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If the request was successful
 * @HTTP 401 If the consumer is unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.LocationDto
 * @see com.qhrtech.emr.accuro.api.demographics.LocationManager
 */
@Component
@Path("/v1/locations")
@Tag(name = "Location Endpoints", description = "Exposes location endpoints.")
public class LocationEndpoint extends AbstractEndpoint {

  /**
   * Retrieves a <code>List</code> of all locations.
   *
   * @return A <code>List</code> of all Location DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves locations",
      description = "Retrieves all locations.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LocationDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<LocationDto> getLocations() throws ProtossException {
    LocationManager locationInterface = getImpl(LocationManager.class);
    return mapDto(locationInterface.getLocations(), LocationDto.class, ArrayList::new);
  }
}
