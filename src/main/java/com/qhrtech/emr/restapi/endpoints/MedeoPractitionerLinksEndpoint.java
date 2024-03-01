
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.medeo.MedeoProviderLinkManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.medeo.MedeoProviderLink;
import com.qhrtech.emr.restapi.models.dto.MedeoProviderLinkDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/")
@Tag(name = "Medeo Practitioner links Endpoints",
    description = "Exposes Medeo Practitioner links endpoints")
public class MedeoPractitionerLinksEndpoint extends AbstractEndpoint {


  @GET
  @Path("/medeo-practitioner-links")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.MedeoPractitionerLinks.read' )")
  @Operation(
      summary = "Retrieves medeo practitioner links for provider",
      description = "Gets medeo practitioner links for the given Accuro provider id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = MedeoProviderLinkDto.class,
                      subTypes = ArrayList.class)))})
  @Parameter(
      name = "authorization",
      description = "Client or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<MedeoProviderLinkDto> getMedeoPractitionerLinks(
      @Parameter(
          description = "Accuro provider "
              + "id") @QueryParam("accuroProviderId") @NotNull(
                  message = "accuroProviderId should not be null.") Integer accuroProviderId)
      throws ProtossException {

    MedeoProviderLinkManager medeoProviderLinkManager = getImpl(MedeoProviderLinkManager.class);
    List<MedeoProviderLink> medeoProviderLinks =
        medeoProviderLinkManager.getPractitionersByProviderId(accuroProviderId);
    return mapDto(medeoProviderLinks, MedeoProviderLinkDto.class, ArrayList::new);

  }

  @DELETE
  @Path("/medeo-practitioner-links")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.MedeoPractitionerLinks.delete' )")
  @Operation(
      summary = "Delete a medeo practitioner link ",
      description = "Remove a medeo practitioner link for the provided parameters",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found")})
  @Parameter(
      name = "authorization",
      description = "Client or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response deleteMedeoPractitionerLink(
      @Parameter(
          description = "Accuro provider "
              + "id") @QueryParam("accuroProviderId") @NotNull(
                  message = "accuroProviderId should not be null.") Integer accuroProviderId,
      @Parameter(description = "Organization id") @QueryParam("orgId") @NotNull(
          message = "orgId should not be null.") Long organizationId)
      throws ProtossException {
    MedeoProviderLinkManager medeoProviderLinkManager = getImpl(MedeoProviderLinkManager.class);

    MedeoProviderLink medeoProviderLink =
        medeoProviderLinkManager.getPractitioner(accuroProviderId, organizationId);
    if (medeoProviderLink == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    medeoProviderLinkManager.deleteProviderLink(accuroProviderId, organizationId);
    return Response.status(Status.NO_CONTENT).build();
  }


}
