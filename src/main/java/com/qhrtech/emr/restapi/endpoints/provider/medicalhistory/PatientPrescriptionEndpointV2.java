
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LimitedUseCodeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve prescription information for patients.
 *
 * @RequestHeader Authorization Provider level authorization grant
 *
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 404 Not found
 */
@Component
@Path("v2/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "Patient PrescriptionV2 Endpoints",
    description = "Exposes prescription V2 endpoints")
public class PatientPrescriptionEndpointV2 extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = 25;
  private static final int MAX_PAGE_SIZE = 50;

  /**
   * Gets all prescriptions for a given patient.
   *
   * <p>
   * The {@link LimitedUseCodeDto} variable is ON province specific which is not included for other
   * province.
   * </p>
   * <p>
   * Except Patient ID, all the parameters are optional.
   * </p>
   *
   * @param patientId Patient ID
   * @param startingId Specifies the unique order id of the next page of data (exclusive). Typically
   *        this is the {@code EnvelopeDto.lastId} from the last page of data.
   * @param pageSize Default page size is 25. The size must be 0 < pageSize <= 50. If the size is
   *        not provided or less than 1 it will be set the default size 25. If it is over 50 it will
   *        be set the maximum size which is 50.
   * @param external Boolean which specifies if prescription records should include the external
   *        records or internal. To include both internal and external records, keep the parameter
   *        null.
   *
   * @return A List of {@link PrescriptionMedicationDto}s or {@code empty}{@link List} if not found.
   *
   * @throws ProtossException If there has been a database error.
   */
  @Operation(
      summary = " Retrieves all prescriptions for a patient",
      description = "Gets all prescriptions for a given patient. "
          + "The **LimitedUseCodeDto** variable is **ON** province specific "
          + "and it is not included for the other provinces. "
          + "Except Patient ID, all the parameters are optional. All prescriptions follow Accuro "
          + " permissions with the exception of external prescriptions.  If the checkbox 'Exclude"
          + " Prescriptions from EMR Provider Permissions' under global security settings is"
          + " checked, permissions will have no effect on prescriptions.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionMedicationDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "external",
              description = "Include only external medications. "
                  + "To include both internal and external records, keep the parameter null.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "Specifies the unique order id of the next page "
                  + "of data (exclusive). Typically this is the "
                  + "{@code EnvelopeDto.lastId} from the last page of data.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "Default page size is 25. The size must be 0 < pageSize <= 50. "
                  + "If the size is not provided or less than 1 "
                  + "it will be set the default size i.e 25. "
                  + "If it is over 50 it will set to the maximum size which is 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "includeCcddMapping",
              description = "Boolean value. Results should included ccdd mapping or not.",
              in = ParameterIn.QUERY)})
  @GET
  @Path("/{patientId}/prescriptions/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  public EnvelopeDto<PrescriptionMedicationDto> getForPatient(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize,
      @Parameter(hidden = true) @QueryParam("external") Boolean external,
      @Parameter(hidden = true) @QueryParam("includeCcddMapping") Boolean includeCcddMapping)
      throws ProtossException {

    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    includeCcddMapping = includeCcddMapping != null && includeCcddMapping;

    Envelope<PrescriptionMedication> data =
        prescriptionMedicationManager.getPrescriptionsByPatientId(patientId, external, pageSize,
            startingId, includeCcddMapping);

    List<PrescriptionMedication> contents = data.getContents();
    ArrayList<PrescriptionMedicationDto> prescriptionMedication =
        mapDto(contents, PrescriptionMedicationDto.class, ArrayList::new);

    EnvelopeDto<PrescriptionMedicationDto> envelope = new EnvelopeDto<>();

    if (contents.isEmpty()) {
      envelope.setContents(Collections.emptyList());
      envelope.setCount(0);
      envelope.setTotal(0);
      envelope.setLastId(0L);
    } else {
      envelope.setContents(prescriptionMedication);
      envelope.setCount(data.getCount());
      envelope.setTotal(data.getTotal());
      envelope.setLastId(data.getLastId());
    }

    return envelope;
  }
}
