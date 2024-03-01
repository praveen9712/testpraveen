
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
import com.qhrtech.emr.restapi.util.PaginationConstant;
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
import javax.ws.rs.QueryParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("v1/provider-portal/prescriptions")
@Facet("provider-portal")
@Tag(name = "Prescription Endpoints",
    description = "Exposes prescription endpoints")
public class PrescriptionEndpoint extends AbstractEndpoint {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();

  /**
   * Gets all prescriptions for a given filters.
   *
   * <p>
   * The {@link LimitedUseCodeDto} variable is ON province specific which is not included for other
   * province.
   * </p>
   * <p>
   * All the parameters are optional.
   * </p>
   *
   * @param startingId The unique row number(exclusive) for the list of records of the next page. In
   *        other words, this id specifies, the next set of records returned by API, would have row
   *        number greater than this startingId. For the very first request, this field can be left
   *        null or 0. For the subsequent requests, the value for this field should be obtained from
   *        the lastId {@code EnvelopeDto.lastId} field of the data received in the previous
   *        request. The last id is the row number of the last record in the list. The records are
   *        arranged in the descending order of their created date.
   * @param pageSize Default page size is 25. The size must be 0 < pageSize <= 50. If the size is
   *        not provided or less than 1 it will be set the default size 25. If it is over 50 it will
   *        be set the maximum size which is 50.
   * @param external Boolean which specifies if prescription records should include the external
   *        records or internal. To include both internal and external records, keep the parameter
   *        null.
   * @param patientId Patient Id
   * @param rxUuid Prescription uuid
   * @param includeCcddMappings Boolean value to include ccdd mappings
   *
   * @return A List of {@link PrescriptionMedicationDto}s or {@code empty}{@link Envelope} if not
   *         found.
   *
   * @throws ProtossException If there has been a database error.
   */
  @Operation(
      summary = " Retrieves all prescriptions for a patient",
      description = "Retrieves all prescriptions for a given patient. "
          + "The **LimitedUseCodeDto** variable is **ON** province specific "
          + "and it is not included for the other provinces. "
          + "All the parameters are optional. All prescriptions follow Accuro "
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
              in = ParameterIn.QUERY),
          @Parameter(
              name = "rxUuid",
              description = "Prescription uuid",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "external",
              description = "Include only external medications. "
                  + "To include both internal and external records, keep the parameter null.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "startingId",
              description = "The unique row number(exclusive) for the list of records of the "
                  + "next page. In other words, this id specifies, the next set of records returned"
                  + " by API, would have row number greater than this startingId."
                  + " For the very first request, this field can be left null or 0."
                  + " For the subsequent requests, the value for this  field should be obtained"
                  + " from the lastId {@code EnvelopeDto.lastId} field of the data received "
                  + "in the previous request. The last id is the row number of the"
                  + " last record in the list.  The records are arranged in the "
                  + "descending order of their created date.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "pageSize",
              description = "Default page size is 25. The size must be 0 < pageSize <= 50. "
                  + "If the size is not provided or less than 1 "
                  + "it will be set the default size i.e 25. "
                  + "If it is over 50 it will set to the maximum size which is 50.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "includeCcddMappings",
              description = "The flag if ccdd mappings are included in return",
              in = ParameterIn.QUERY)})
  @GET
  @Path("")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  public EnvelopeDto<PrescriptionMedicationDto> getPrescriptions(
      @Parameter(hidden = true) @QueryParam("patientId") Integer patientId,
      @Parameter(hidden = true) @QueryParam("rxUuid") String rxUuid,
      @Parameter(hidden = true) @QueryParam("startingId") Integer startingId,
      @Parameter(hidden = true) @QueryParam("pageSize") Integer pageSize,
      @Parameter(hidden = true) @QueryParam("external") Boolean external,
      @Parameter(hidden = true) @QueryParam("includeCcddMappings") Boolean includeCcddMappings)
      throws ProtossException {

    if (pageSize == null || pageSize <= 0) {
      pageSize = DEFAULT_PAGE_SIZE;
    }

    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    Envelope<PrescriptionMedication> data =
        prescriptionMedicationManager.searchPrescriptions(
            rxUuid,
            patientId,
            pageSize,
            startingId,
            external,
            includeCcddMappings);

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
