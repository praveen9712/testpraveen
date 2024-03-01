
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.billing.BillProcedureManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
 * This <code>BillProcedureEndpoint</code> collection is designed to retrieve the authorized
 * providers billing procedure information. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 *
 * @author David.Huang
 */
@Component
@Path("/v1/provider-portal/appointments")
@Facet("provider-portal")
@Tag(name = "BillProcedure Endpoints",
    description = "Exposes bill procedure endpoints")
public class BillProcedureEndpoint extends AbstractEndpoint {

  /**
   * <p>
   * Get Procedure Codes by an appointment ID.
   * </p>
   *
   * @param appointmentId Appointment ID
   *
   * @return A set of Procedure Codes
   *
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{appointmentId}/billprocedures/")
  @PreAuthorize("#oauth2.hasAnyScope( 'SCHEDULING_READ', 'SCHEDULING_WRITE' ) "
      + "and #accuro.hasAccess( 'SCHEDULING', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.Billing, level = AccessLevel.ReadOnly,
      description = "Allows access to bill procedures for claims that are assigned a provider the "
          + "user has this permission for.")
  @Operation(
      summary = "Retrieves procedure codes",
      description = "Retrieves medical procedure codes for an appointment. ",
      responses = @ApiResponse(
          responseCode = "200",
          description = "Success",
          content = @Content(
              schema = @Schema(
                  type = "string",
                  example = "[\"A001\",\"A002\"]",
                  description = "Set of procedure code"))))
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<String> getProcedureCodes(
      @Parameter(description = "Appointment id") @PathParam("appointmentId") int appointmentId)
      throws ProtossException {
    BillProcedureManager billProcedureManager = getImpl(BillProcedureManager.class);
    return new HashSet<>(billProcedureManager.getProcedureCodesByAppointmentId(appointmentId));
  }

}
