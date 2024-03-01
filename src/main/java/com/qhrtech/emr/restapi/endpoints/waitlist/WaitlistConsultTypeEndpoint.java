
package com.qhrtech.emr.restapi.endpoints.waitlist;

import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.webcohesion.enunciate.metadata.Facet;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code WaitlistSelectionListEndpoint} is designed to expose the waitlist selection list.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/waitlist-consult-types")
@Facet("provider-portal")
@Tag(name = "Waitlist Endpoints",
    description = "Exposes waitlist endpoints.")
public class WaitlistConsultTypeEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all the consult types for waitlist under selection list 'SWLConsultType'.
   *
   * @return A list of all consult types available under SWLConsultType
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'WAITLIST_READ', 'WAITLIST_WRITE') "
      + "and #accuro.hasAccess( 'WAITLIST', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves all the consult type available for waitlist.",
      description = "Gets all the waitlist consult types available.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class)))),
          @ApiResponse(
              responseCode = "401",
              description = "Consumer unauthorized")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<String> getConsultTypes() throws ProtossException {

    SelectionListManager selectionListManager = getImpl(SelectionListManager.class);
    return selectionListManager.getSelectionList(SelectionListName.SWL_CONSULT_TYPE.getListName());
  }
}
