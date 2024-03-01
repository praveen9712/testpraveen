
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.physician.MasterNumberManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedProvinceException;
import com.qhrtech.emr.restapi.models.dto.MasterNumberDto;
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
 * This <code>PhysicianMasterNumbersEndpoint</code> retrieves list of all assigned physician master
 * numbers. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @see MasterNumberDto
 * @see MasterNumberManager
 */
@Component
@Path("/v1/enumerations/physician-master-numbers")
@Tag(name = "Person Titles Endpoint",
    description = "Exposes person titles endpoints")
public class PhysicianMasterNumbersEndpoint extends AbstractEndpoint {

  /**
   * Get a list of all assigned physician master numbers. This feature is only available for Ontario
   * Province.
   *
   * @return A list of all assigned physician master numbers (String)
   * @throws DataAccessException If there has been a database error.
   * @throws UnsupportedProvinceException If the province is other than ON.
   */
  @GET
  @Operation(
      summary = "Retrieves assigned physician master numbers",
      description = "Retrieves assigned physician master numbers. This feature is only available "
          + "for Ontario Province.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = MasterNumberDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<MasterNumberDto> getPhysicianMasterNumbers() throws ProtossException {

    MasterNumberManager masterNumberManager = getImpl(MasterNumberManager.class);
    return mapDto(masterNumberManager.getAssignedMasterNumbers(), MasterNumberDto.class,
        ArrayList::new);
  }

}
