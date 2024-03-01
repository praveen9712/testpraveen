
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.prescription.AnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.StatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.WellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.model.prescription.StatusHistory;
import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LimitedUseCodeDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.AnnotationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.DosageDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.InteractionDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.InteractionManagementDetailsDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionDetailsDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionIndicationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.StatusHistoryDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.WellnetPrescriptionLinkDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve prescription information for patients.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 404 Not found
 */
@Component
@Path("v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "Prescription Endpoints",
    description = "Exposes prescription endpoints")
public class PatientPrescriptionEndpoint extends AbstractEndpoint {

  @Autowired
  private PrescriptionDetailsService service;

  /**
   * Gets all prescriptions for a given patient.
   *
   * @param patientId Patient ID
   * @return A List of {@link PrescriptionMedicationDto} or {@code empty}{@link List} if not found.
   * @throws DataAccessException If there has been a database error.
   * @deprecated
   *             <p>
   *             The {@link LimitedUseCodeDto} variable is ON province specific which is not
   *             included for other province.
   *             </p>
   *             <p>
   *             Note: This endpoint is deprecated. Check the version-2 to access the similar
   *             resources.
   *             </p>
   */
  @Operation(
      summary = " Retrieves the prescriptions for the patient",
      description = "Gets all prescriptions for the given patient.\n\n"
          + "The **LimitedUseCodeDto** variable is **ON** province specific "
          + "and it is not included for the other provinces. "
          + "Note: This endpoint is deprecated. Check the version-2"
          + " to access the similar resources.   If the checkbox 'Exclude Prescriptions from EMR"
          + " Provider Permissions' under global security settings is checked, permissions will"
          + " have no effect on prescriptions.",
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
              description = "Include only external medications",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "includeCcddMapping",
              description = "Include includeCcddMapping or not",
              in = ParameterIn.QUERY)})
  @GET
  @Path("/{patientId}/prescriptions/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  @Deprecated
  public List<PrescriptionMedicationDto> getForPatient(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @QueryParam("external") Boolean external,
      @Parameter(hidden = true) @QueryParam("includeCcddMapping") Boolean includeCcddMapping)
      throws ProtossException {
    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    includeCcddMapping = includeCcddMapping != null && includeCcddMapping;

    List<PrescriptionMedication> prescriptionsFromProtoss = prescriptionMedicationManager
        .getForPatient(patientId, includeCcddMapping)
        .stream()
        .filter(p -> external == null || external == p.isExternalRx())
        .collect(Collectors.toList());

    if (prescriptionsFromProtoss.isEmpty()) {
      return Collections.emptyList();
    }
    ArrayList<PrescriptionMedicationDto> prescriptions =
        mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);
    return prescriptions;
  }

  /**
   * Retrieves one prescription of a given patient with the specified prescription id.
   *
   * <p>
   * The {@link LimitedUseCodeDto} variable is ON province specific which is not included for other
   * province.
   * </p>
   *
   * @param patientId Patient ID
   * @param prescriptionId Prescription ID
   * @return A {@link PrescriptionMedicationDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the prescription id does not exist or not associated to the patient id.
   */

  @Operation(
      summary = "Retrieves a prescription for the patient",
      description = "Gets the prescription for the given patient "
          + "with the specified prescription id.\n\n"
          + "The **LimitedUseCodeDto** variable is specific to **ON** province, "
          + "which is not included for the other provinces. "
          + "If the checkbox 'Exclude Prescriptions from EMR Provider Permissions' "
          + "under global security settings is checked, permissions will have "
          + "no effect on prescriptions. ",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Prescription id does not exist "
                  + "or not associated to the patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PrescriptionMedicationDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @GET
  @Path("/{patientId}/prescriptions/{prescriptionId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              required = true,
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER),
          @Parameter(
              name = "patientId",
              description = "Patient id.",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionId",
              description = "Prescription id.",
              in = ParameterIn.PATH),
          @Parameter(
              name = "includeCcddMapping",
              description = "includeCcddMapping.",
              in = ParameterIn.QUERY)
      })
  public PrescriptionMedicationDto getByPrescriptionId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("prescriptionId") int prescriptionId,
      @Parameter(hidden = true) @QueryParam("includeCcddMapping") Boolean includeCcddMapping)
      throws ProtossException {

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    PrescriptionMedication prescriptionFromProtoss =
        prescriptionMedicationManager.getById(prescriptionId);

    // Check if the prescription is for the right patient
    if (prescriptionFromProtoss == null || prescriptionFromProtoss.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    PrescriptionMedicationDto prescription =
        mapDto(prescriptionFromProtoss, PrescriptionMedicationDto.class);

    includeCcddMapping = includeCcddMapping != null && includeCcddMapping;

    // set prescription details
    PrescriptionDetailsDto prescriptionDetailsDto =
        service.getPrescriptionDetails(prescriptionFromProtoss, includeCcddMapping);
    prescription.setPrescriptionDetails(prescriptionDetailsDto);

    // set dosages
    prescription.setDosages(getDosages(prescriptionId));

    // set indications
    prescription.setIndications(getIndications(prescriptionId));

    // set annotations
    prescription.setAnnotations(getAnnotations(prescriptionId));

    // set status histories
    prescription.setStatusHistories(getStatusHistories(prescriptionId));

    // set interactions and interaction management details on each interaction
    prescription.setInteractions(getInteractions(prescriptionId));

    // set Wellnet prescription links
    prescription.setWellnetPrescriptionLinks(getWellnetPrescriptionLinks(prescriptionId));

    // set limited use code
    SystemInformationManager systemInfoManager = getImpl(SystemInformationManager.class);
    if (AccuroProvince.ON.equals(systemInfoManager.getProvince())) {
      prescription.setLimitedUseCodes(getLimitedUseCodes(prescription));
    }

    return prescription;
  }


  /**
   * Updates order status for given prescription of a given patient.
   *
   * <P>
   * And prescription status history will be created only if there is an Eprescribe cancel request
   * response with 'Approve' status linked to the prescription.
   * </P>
   *
   * @param patientId Patient ID
   * @param prescriptionId Prescription ID
   * @return changeId in 201(created) response
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the prescription id does not exist or not associated to the patient id.
   */

  @Operation(
      summary = "Updates order status for the given prescription of the given patient.",
      description = "Updates order status for the given prescription of the given patient."
          + " Prescription status history will be created only "
          + "if there is an Eprescribe cancel request response "
          + "with 'Approve' status linked to the prescription.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Prescription id does not exist "
                  + "or not associated to the patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Ok")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + "with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  @POST
  @Path("/{patientId}/prescriptions/{prescriptionId}/order-statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.Prescription.update' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access to update prescription order status "
          + "that are assigned a provider the user has this permission for.")
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              hidden = true,
              description = "Provider level authorization grant or client credentials "
                  + "with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "Patient id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "prescriptionId",
              description = "Prescription id",
              in = ParameterIn.PATH)})
  @RequestBody(
      description = "StatusHistory data transfer object",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = StatusHistoryDto.class))})
  public Response updatePrescriptionOrderStatus(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("prescriptionId") int prescriptionId,
      @RequestBody @Valid StatusHistoryDto statusHistory)
      throws ForbiddenException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, SupportingResourceNotFoundException {

    if (prescriptionId != statusHistory.getPrescriptionId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Prescription id in the path does not match the id in the body.");
    }

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class, true);

    PrescriptionMedication prescriptionFromProtoss =
        prescriptionMedicationManager.getById(prescriptionId);

    // Check if the prescription is for the right patient
    if (prescriptionFromProtoss == null || prescriptionFromProtoss.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Prescription not found for the patient");
    }

    prescriptionMedicationManager
        .updatePrescriptionStatus(mapDto(statusHistory, StatusHistory.class));

    return Response.status(Status.OK).build();

  }


  private Set<DosageDto> getDosages(int prescriptionId) throws ProtossException {
    DosageManager dosageManager = getImpl(DosageManager.class);
    return mapDto(dosageManager.getForPrescription(prescriptionId), DosageDto.class, HashSet::new);
  }

  private Set<PrescriptionIndicationDto> getIndications(int prescriptionId)
      throws ProtossException {
    PrescriptionIndicationManager indicationManager =
        getImpl(PrescriptionIndicationManager.class);
    return mapDto(indicationManager.get(prescriptionId), PrescriptionIndicationDto.class,
        HashSet::new);
  }

  private List<AnnotationDto> getAnnotations(int prescriptionId) throws ProtossException {
    AnnotationManager annotationManager = getImpl(AnnotationManager.class);
    return mapDto(annotationManager.getForPrescription(prescriptionId), AnnotationDto.class,
        ArrayList::new);
  }

  private Set<StatusHistoryDto> getStatusHistories(int prescriptionId) throws ProtossException {
    StatusHistoryManager statusHistoryManager = getImpl(StatusHistoryManager.class);
    return mapDto(statusHistoryManager.getByPrescriptionId(prescriptionId), StatusHistoryDto.class,
        HashSet::new);
  }

  private Set<InteractionDto> getInteractions(int prescriptionId) throws ProtossException {
    InteractionManager interactionManager = getImpl(InteractionManager.class);
    Set<InteractionDto> interactions = mapDto(interactionManager.getForPrescription(prescriptionId),
        InteractionDto.class, HashSet::new);

    InteractionManagementDetailsManager interactionManagementDetailsManager =
        getImpl(InteractionManagementDetailsManager.class);
    for (InteractionDto interaction : interactions) {
      int interactionId = interaction.getId();
      Set<InteractionManagementDetailsDto> interactionManagementDetails =
          mapDto(interactionManagementDetailsManager.getByInteractionId(interactionId),
              InteractionManagementDetailsDto.class, HashSet::new);
      interaction.setInteractionManagementDetails(interactionManagementDetails);
    }
    return interactions;
  }

  private Set<WellnetPrescriptionLinkDto> getWellnetPrescriptionLinks(int prescriptionId)
      throws ProtossException {
    WellnetPrescriptionLinkManager wellnetPrescriptionLinkManager =
        getImpl(WellnetPrescriptionLinkManager.class);
    return mapDto(wellnetPrescriptionLinkManager.getByPrescriptionId(prescriptionId),
        WellnetPrescriptionLinkDto.class, HashSet::new);
  }

  private List<LimitedUseCodeDto> getLimitedUseCodes(PrescriptionMedicationDto prescription)
      throws ProtossException {
    LimitedUseCodeManager limitedUseCodeManager = getImpl(LimitedUseCodeManager.class);
    int drugId;
    try {
      drugId = Integer.parseInt(prescription.getDin());
    } catch (NumberFormatException ex) {
      LoggerFactory.getLogger(getClass()).error("Invalid DIN: " + prescription.getDin(), ex);
      return Collections.emptyList();
    }
    return mapDto(limitedUseCodeManager.getLimitedUseCodeByDrugId(drugId), LimitedUseCodeDto.class,
        ArrayList::new);
  }
}
