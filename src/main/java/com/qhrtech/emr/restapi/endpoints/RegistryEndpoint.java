
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.registry.RegistryEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.registry.RegistryEntry.Type;
import com.qhrtech.emr.restapi.models.dto.RegistryEntryDto;
import com.qhrtech.emr.restapi.models.dto.RegistryType;
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
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


@Component
@Path("/v1/registry-entries")
@Tag(name = "Registry Endpoints", description = "Exposes Registry endpoints.")
@Hidden
public class RegistryEndpoint extends AbstractEndpoint {


  @GET
  @Operation(
      summary = "Retrieves registry records",
      description = "Retrieves all registry records.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = RegistryEntryDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Client, or provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  public List<RegistryEntryDto> getRegistries(
      @Parameter(hidden = true) @QueryParam("id") Long id,
      @Parameter(hidden = true) @QueryParam("registryType") RegistryType registryType)
      throws DatabaseInteractionException {
    Type type = null;
    if (registryType != null) {
      type = Type.lookup(registryType.name());
    }

    RegistryEntryManager registryEntryManager = getImpl(RegistryEntryManager.class);
    return mapDto(registryEntryManager.getEntries(type, id), RegistryEntryDto.class,
        ArrayList::new);
  }


}
