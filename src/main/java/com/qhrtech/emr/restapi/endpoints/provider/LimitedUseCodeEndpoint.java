
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LimitedUseCodeDto;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * End point for exposing limited use codes by DIN. Requires Provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 400 If an invalid DIN is provided
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/medications")
@Facet("provider-portal")
@Tag(name = "LimitedUseCode Endpoints", description = "Exposes limited use code endpoints")
public class LimitedUseCodeEndpoint extends AbstractEndpoint {

  /**
   * Retrieves limited use code by DIN.
   *
   * @param din Drug Identification Number
   * @return List of limited use code.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If an invalid DIN is passed.
   */
  @GET
  @Path("/limited-use-codes")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves limited use code",
      description = "Retrieves all limited use codes for the drug id (DIN).",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid DIN or access beyond Ontario Province"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = LimitedUseCodeDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<LimitedUseCodeDto> getLmitedUseCodeByDrugId(
      @Parameter(description = "Drug id") @QueryParam("din") String din)
      throws ProtossException {

    SystemInformationManager systemInfoManager = getImpl(SystemInformationManager.class);
    if (systemInfoManager.getProvince() != AccuroProvince.ON) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "This endpoint is only accessible for Ontario.");
    }

    int drugId;
    try {
      drugId = Integer.parseInt(din);
    } catch (NumberFormatException ex) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "Invalid DIN.", ex);
    }

    LimitedUseCodeManager limitedUseCodeManager = getImpl(LimitedUseCodeManager.class);
    return mapDto(limitedUseCodeManager.getLimitedUseCodeByDrugId(drugId),
        LimitedUseCodeDto.class, ArrayList::new);
  }
}
