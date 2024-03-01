
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.models.dto.MedeoOrganizationDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/medeo-organizations")
@Tag(name = "Medeo Organization Endpoints",
    description = "Exposes Medeo Organization endpoints")
@Hidden
public class MedeoOrganizationEndpoint extends AbstractEndpoint {


  @GET
  @Operation(
      summary = "Retrieves all medeo organizations. If medeo id provided it will"
          + " return particular medeo organization",
      description = "Gets medeo organization",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MedeoOrganizationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Client or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  public List<MedeoOrganizationDto> getMedeoOrganization(
      @Parameter(
          description = "medeo org id "
              + "medeoId") @QueryParam("medeoId") Long medeoId)
      throws ProtossException {

    MedeoOrganizationManager medeoOrganizationManager = getImpl(MedeoOrganizationManager.class);

    return mapDto(
        medeoOrganizationManager.getMedeoOrganization(medeoId, null),
        MedeoOrganizationDto.class, ArrayList::new);

  }
}
