
package com.qhrtech.emr.restapi.endpoints.provider;


import com.qhrtech.emr.accuro.api.repliform.PatientRepliformReportableManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.repliform.PatientRepliformReportable;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.referrals.ReferralOrderDto;
import com.qhrtech.emr.restapi.models.dto.repliform.PatientRepliformReportableDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>RepliformReportableEndpoint</code> collection is designed to expose the
 * PatientRepliformReportable DTO and related public endpoints. Requires provider level
 * authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patient-repliforms-reportables")
@Facet("provider-portal")
@Tag(name = "Patient Repliforms Reportable Endpoints",
    description = "Exposes patient repliforms reportable data endpoints")
public class RepliformReportableEndpoint extends AbstractEndpoint {


  /**
   * Return the patient repliforms reportable data associated with this chartItemId
   *
   * @param chartItemId ChartItemId this field can be obtained from the {@link ReferralOrderDto}
   * @return Patient repliforms reportable data associated with this ChartItemId
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the repliforms reportable data does not exist in the database.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.PatientRepliform.read')")
  @Operation(
      summary = "Retrieves patient repliforms reportable data",
      description = "Retrieves patient repliforms reportable data associated with the chartItemId."
          + " The chartItemId can be obtained from the referral orders endpoint.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Patient repliforms reportable data not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientRepliformReportableDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientRepliformReportableDto getPatient(
      @Parameter(description = "chartItemId",
          required = true) @QueryParam("chartItemId") Integer chartItemId)
      throws ProtossException {

    if (chartItemId == null) {
      throw new IllegalArgumentException(
          "ChartItemId should not be null.");
    }

    PatientRepliformReportableManager patientManager =
        getImpl(PatientRepliformReportableManager.class);
    PatientRepliformReportable reportableData = patientManager.getByPatientFormId(chartItemId);
    if (reportableData == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Patient repliforms reportable data not found with chart item id: " + chartItemId);
    }
    return mapDto(reportableData, PatientRepliformReportableDto.class);
  }

}
