
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.models.dto.MedeoOrganizationDto;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
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
import javax.ws.rs.PathParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>OfficeEndpoint</code> collection is designed to expose the Office DTO and related
 * public endpoints. Requires client, provider, or patient level authorization.
 *
 * @RequestHeader Authorization Client, patient, or provider level authorization grant
 * @HTTP 200 If the request was successful
 * @HTTP 401 If the consumer is unauthorized
 * @see com.qhrtech.emr.restapi.models.dto.OfficeDto
 */
@Component
@Path("/v1/offices")
@Tag(name = "Office Endpoints", description = "Exposes office endpoints.")
public class OfficeEndpoint extends AbstractEndpoint {

  /**
   * Retrieves a list of all Accuro Offices.
   *
   * @return A list of all Accuro Offices DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves Offices",
      description = "Retrieves all Accuro offices.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = OfficeDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, patient, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<OfficeDto> getOfficeList() throws ProtossException {

    OfficeManager officeInterface = getImpl(OfficeManager.class);
    return mapDto(officeInterface.getAllOffices(), OfficeDto.class, HashSet::new);
  }

  /**
   * Retrieves a list of all Accuro Offices.
   *
   * @return A list of all Accuro Offices DTOs.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Operation(
      summary = "Retrieves Medeo organizaton for an office. ",
      description = "Retrieves Medeo organizaton for an office.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MedeoOrganizationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Client, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  @Path("/{officeId}/medeo-organizations")
  public MedeoOrganizationDto getOfficeMedeoOrganization(
      @Parameter(description = "Office id") @PathParam("officeId") int officeId)
      throws ProtossException {

    MedeoOrganizationManager medeoOrganizationManager = getImpl(MedeoOrganizationManager.class);
    return mapDto(medeoOrganizationManager.getMedeoOrganizationByOffice(officeId),
        MedeoOrganizationDto.class);
  }
}
