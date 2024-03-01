
package com.qhrtech.emr.restapi.endpoints.patient;

import com.qhrtech.emr.accuro.api.allergy.AllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.NoKnownAllergyManager;
import com.qhrtech.emr.accuro.api.allergy.PatientAllergyManager;
import com.qhrtech.emr.accuro.api.codes.CodeSystemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisManager;
import com.qhrtech.emr.accuro.api.medications.AlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.FormulationManager;
import com.qhrtech.emr.accuro.api.medications.GenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.ManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.NaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.IngredientManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.UserSecurity;
import com.qhrtech.emr.accuro.model.allergy.AllergyReaction;
import com.qhrtech.emr.accuro.model.allergy.NoKnownAllergy;
import com.qhrtech.emr.accuro.model.allergy.PatientAllergy;
import com.qhrtech.emr.accuro.model.codes.CodeSystem;
import com.qhrtech.emr.accuro.model.codes.CodeValue;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosis;
import com.qhrtech.emr.accuro.model.medications.AlternativeHealthProduct;
import com.qhrtech.emr.accuro.model.medications.Formulation;
import com.qhrtech.emr.accuro.model.medications.GenericDrug;
import com.qhrtech.emr.accuro.model.medications.ManufacturedDrug;
import com.qhrtech.emr.accuro.model.medications.MedicationType;
import com.qhrtech.emr.accuro.model.medications.NaturalHealthProduct;
import com.qhrtech.emr.accuro.model.prescription.Dosage;
import com.qhrtech.emr.accuro.model.prescription.Ingredient;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.AllergiesDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.AlternativeHealthProductSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.CustomCompoundSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.DiagnosisDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.DrugIdentifier;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.FormulationSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.IngredientSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.ManufacturedDrugSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.MedicationDetails;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.MedicationTypeApi;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.NaturalHealthProductSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.NoKnownAllergySummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PatientAllergySummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PatientMedicalSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PrescriptionDto;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.DosageDto;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.security.exceptions.PrescriptionConversionException;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import com.qhrtech.emr.restapi.util.EmrUnitsMapping;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientMedicalSummaryEndpoint</code> is designed to expose
 * {@link PatientMedicalSummaryDto} endpoints. Requires Provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@PreAuthorize("#oauth2.hasAnyScope('PATIENT_SUMMARY_READ') "
    + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
@Tag(name = "Medical summary Endpoints",
    description = "Exposes patient medical summary endpoints")
public class PatientMedicalSummaryEndpoint extends AbstractEndpoint {

  @Autowired
  private PrescriptionDetailsService service;

  private static String formatFloat(float num) {
    if (num == (long) num) {
      return String.format("%d", (long) num);
    } else {
      return String.format("%s", num);
    }
  }

  /**
   * Retrieves patient medical summary related to given patient id. Only prescriptions with provider
   * permissions and active status are retrieved. Dosages are sorted by start date.
   *
   * @param patientId Patient ID
   * @return {@link PatientMedicalSummaryDto} data object belonging to given patient id.
   * @throws ProtossException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/medical-summary")
  @Operation(
      summary = "Retrieves patient medical summary by patient id.",
      description = "Retrieves the patient medical summary by the given patient id. "
          + "Only prescriptions with provider permissions and active status are retrieved."
          + "Dosages are sorted by start date.",
      responses = {
          @ApiResponse(
              responseCode = "401",
              description = "The user is not authorized."),
          @ApiResponse(
              responseCode = "404",
              description = "Invalid patient id."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientMedicalSummaryDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "patientId",
              description = "The unique patient id.",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for")
  public PatientMedicalSummaryDto getPatientMedicalSummary(
      @Parameter(hidden = true) @PathParam("patientId") int patientId)
      throws ProtossException {

    PatientMedicalSummaryDto medicalSummaryDto = new PatientMedicalSummaryDto();

    populatePrescription(medicalSummaryDto, filterPrescriptions(patientId));

    populateDiagnosis(medicalSummaryDto, patientId);

    populateAllergies(medicalSummaryDto, patientId);

    return medicalSummaryDto;
  }

  /**
   * This function filters the prescription medication to only providers which user has permission
   * to access and prescriptions which are active.
   */
  private List<PrescriptionMedication> filterPrescriptions(int patientId)
      throws ProtossException {

    PrescriptionMedicationManager medicationManager = getImpl(PrescriptionMedicationManager.class);
    List<PrescriptionMedication> prescriptionMedicationList =
        medicationManager.getPrescriptionsForPatient(patientId);

    if (getUser().getUserId() == null) {
      return prescriptionMedicationList;
    }

    UserSecurity userSecurity = service.getPermissionsForMedicalSummary();

    AccuroApiContextManager apiContextManager = getImpl(AccuroApiContextManager.class);
    Set<Integer> permissibleProviders;
    try {
      permissibleProviders =
          apiContextManager.getPermissibleProviders(getUser().getUserId(), userSecurity);
    } catch (ForbiddenException ex) {
      permissibleProviders = new HashSet<>();
    }
    /*
     * Prescriptions are only visible if they are of permissible providers or external medication
     * which can have providers null or empty.
     */
    Set<Integer> finalPermissibleProviders = permissibleProviders;
    List<PrescriptionMedication> filteredList = prescriptionMedicationList.stream()
        .filter(pres -> pres.getMedStatus().equals("Active")
            && (pres.getPrescribingPhysician() == null || pres.getPrescribingPhysician() < 1
                || finalPermissibleProviders.contains(pres.getPrescribingPhysician())))
        .collect(Collectors.toList());

    return filteredList;
  }

  private void populateAllergies(PatientMedicalSummaryDto medicalSummaryDto,
      int patientId) throws ProtossException {
    AllergiesDto allergiesDto = new AllergiesDto();
    populateNoKnownAllergies(allergiesDto, patientId);
    populatePatientAllergies(allergiesDto, patientId);
    medicalSummaryDto.setAllergies(allergiesDto);
  }

  private void populateNoKnownAllergies(AllergiesDto allergiesDto, int patientId)
      throws ProtossException {
    NoKnownAllergyManager noKnownAllergyManager = getImpl(NoKnownAllergyManager.class);
    List<NoKnownAllergy> allergies = noKnownAllergyManager.getForPatient(patientId);
    List<NoKnownAllergySummaryDto> noKnownAllergySummaryDtos =
        mapDto(allergies, NoKnownAllergySummaryDto.class, ArrayList::new);

    allergiesDto.setNoKnownAllergies(noKnownAllergySummaryDtos);
  }

  private void populatePatientAllergies(AllergiesDto allergiesDto, int patientId)
      throws ProtossException {
    PatientAllergyManager patientAllergyManager = getImpl(PatientAllergyManager.class);
    List<PatientAllergy> patientAllergies = patientAllergyManager.getForPatient(patientId);
    List<PatientAllergySummaryDto> allergySummaryDtos = new ArrayList<>();
    for (PatientAllergy patientAllergy : patientAllergies) {
      /**
       * The providerId in patientAllergy may have value. Check ModifyAllergy.java at line 1158
       * onwards in accuro-core
       */
      PatientAllergySummaryDto allergySummaryDto =
          mapDto(patientAllergy, PatientAllergySummaryDto.class);
      AllergyReactionManager allergyReactionManager = getImpl(AllergyReactionManager.class);

      StringBuilder displayText = new StringBuilder();
      displayText.append("Group: ").append(patientAllergy.getAllergyGroupName()).append(" - ");
      displayText.append("Name: ").append(patientAllergy.getAllergyName()).append(" - ");
      displayText.append("Severity: ").append(patientAllergy.getSeverityCode());

      Set<AllergyReaction> allergyReactions =
          allergyReactionManager.getByPatientAllergyId(patientAllergy.getId());

      if (!allergyReactions.isEmpty()) {
        displayText.append(" - ");
        displayText.append("Reactions: ");
        Set<AllergyReactionDto> reactionDtos =
            mapDto(allergyReactions, AllergyReactionDto.class, HashSet::new);
        Iterator<AllergyReactionDto> reactionIterator = reactionDtos.iterator();
        while (reactionIterator.hasNext()) {
          AllergyReactionDto reactionDto = reactionIterator.next();
          displayText.append(reactionDto.getReactionCode()).append(" - ");
          displayText.append(reactionDto.getDescription());
          if (reactionIterator.hasNext()) {
            displayText.append(", ");
          }
        }
        allergySummaryDto
            .setReactions(reactionDtos);
      }
      allergySummaryDto.setAllergyDisplayText(displayText.toString());
      allergySummaryDto.setDrugIdentifier(getDrugIdentifier(
          patientAllergy.getMedicationType() == null ? null
              : patientAllergy.getMedicationType().toString(),
          patientAllergy.getDrugCode()));
      allergySummaryDtos.add(allergySummaryDto);
    }
    allergiesDto.setPatientAllergies(allergySummaryDtos);
  }

  private void populateDiagnosis(PatientMedicalSummaryDto medicalSummaryDto, int patientId)
      throws ProtossException {

    PatientDiagnosisManager patientDiagnosisManager = getImpl(PatientDiagnosisManager.class);
    DiagnosisStatusManager diagnosisStatusManager = getImpl(DiagnosisStatusManager.class);

    // mapping diagnosis status
    Set<DiagnosisStatus> diagnosisStatuses = diagnosisStatusManager.getAll();
    Map<Integer, DiagnosisStatus> mappedStatus = new HashMap<>();
    diagnosisStatuses.forEach(status -> mappedStatus.put(status.getStatusId(), status));

    Set<PatientDiagnosis> protossResult = patientDiagnosisManager.getForPatient(patientId);

    Set<DiagnosisDto> diagnosisDtoSet = new HashSet<>();
    for (PatientDiagnosis patientDiagnosis : protossResult) {
      StringBuilder diagnosisDisplayText = new StringBuilder();
      if (patientDiagnosis.isNegative()) {
        diagnosisDisplayText.append("Negative - ");
      }
      diagnosisDisplayText.append("Status: ");
      DiagnosisDto diagnosisDto = mapDto(patientDiagnosis, DiagnosisDto.class);
      DiagnosisStatus diagnosisStatus = mappedStatus.get(patientDiagnosis.getDiagnosisStatusId());
      if (diagnosisStatuses != null) {
        DiagnosisStatusDto diagnosisStatusDto = mapDto(diagnosisStatus, DiagnosisStatusDto.class);
        diagnosisDto.setDiagnosisStatus(diagnosisStatusDto);
        diagnosisDisplayText.append(diagnosisStatus.getStatusName()).append(" - ");
      }

      diagnosisDisplayText.append(patientDiagnosis.getDescription());
      diagnosisDto.setDiagnosisDisplayText(diagnosisDisplayText.toString());
      diagnosisDtoSet.add(diagnosisDto);

    }
    medicalSummaryDto.setDiagnosis(diagnosisDtoSet);
  }

  private void populatePrescription(
      PatientMedicalSummaryDto medicalSummaryDto,
      List<PrescriptionMedication> prescriptionMedicationList) throws ProtossException {

    List<PrescriptionDto> prescriptionDtos = new ArrayList<>();

    for (PrescriptionMedication prescriptionMed : prescriptionMedicationList) {
      PrescriptionDto prescriptionDto = new PrescriptionDto();
      prescriptionDto.setPrescriptionId(prescriptionMed.getPrescriptionId());
      prescriptionDto.setPrescribingProvider(prescriptionMed.getPrescribingPhysician());
      prescriptionDto.setExternalRx(prescriptionMed.isExternalRx());
      prescriptionDto.setAuthorizingProvider(prescriptionMed.getAuthorizingPhysician());
      prescriptionDto.setDosageForm(prescriptionMed.getDosageForm());
      prescriptionDto.setSigInstructions(prescriptionMed.getSigInstructions());
      prescriptionDto.setRoute(prescriptionMed.getRoute());
      prescriptionDto.setMedStatus(prescriptionMed.getMedStatus());
      prescriptionDto.setDrugUse(prescriptionMed.getDrugType());
      prescriptionDto.setNote(prescriptionMed.getNote());
      AccuroCalendar accuroCalendar = new AccuroCalendar();

      prescriptionDto.setWrittenDate(
          prescriptionMed.getWrittenDate() == null ? null
              : LocalDateTime
                  .fromCalendarFields(accuroCalendar.unwrap(prescriptionMed.getWrittenDate())));
      prescriptionDto.setExpiryDate(prescriptionMed.getExpiryDate());
      prescriptionDto.setEffectiveDate(prescriptionMed.getEffectiveDate());
      prescriptionDto.setCreatedDate(prescriptionMed.getCreatedDate());
      prescriptionDto.setLastModified((prescriptionMed.getLastModified()));

      MedicationType medicationType = prescriptionMed.getDinSystem();
      int medicationId = prescriptionMed.getMedicationId();

      switch (medicationType) {
        case DIN:
          populateManufacturedDrugDto(prescriptionDto, prescriptionMed.getDin());
          break;
        case GD:
          populateGenericDrugDetails(prescriptionDto, medicationId);
          break;
        case NPN:
          populateNaturalHealthProductDto(prescriptionDto, prescriptionMed.getDin());
          break;
        case AHP:
          populateAlternativeHealthProductDetails(prescriptionDto, medicationId);
          break;
        case GF:
          populateFormulationDto(prescriptionDto, prescriptionMed.getWellnetFormulationId());
          break;
        case COMPOUND:
          populateCustomCompoundDto(prescriptionDto, prescriptionMed.getCompoundName(),
              prescriptionMed.getCompoundDetails(), prescriptionMed.getPrescriptionId());
          break;
        default:
          throw new PrescriptionConversionException("No valid medication type: " + medicationType);
      }
      getDosages(prescriptionDto, prescriptionMed.getPrescriptionId());
      StringBuilder instructions = new StringBuilder();
      if (StringUtils.isBlank(prescriptionDto.getInstructionsDisplayText())
          && !prescriptionDto.getSigInstructions().isEmpty()) {
        instructions.append("SigInstructions: " + prescriptionDto.getSigInstructions());
      } else {
        instructions.append(prescriptionDto.getInstructionsDisplayText());
        if (!prescriptionDto.getSigInstructions().isEmpty()) {
          instructions.append(" - SigInstructions: " + prescriptionDto.getSigInstructions());
        }
      }
      prescriptionDto.setInstructionsDisplayText(instructions.toString());

      prescriptionDtos.add(prescriptionDto);
    }
    medicalSummaryDto.setPrescriptions(prescriptionDtos);
  }

  private void populateManufacturedDrugDto(PrescriptionDto prescriptionDto,
      String din) throws ProtossException {
    ManufacturedDrugManager drugManager = getImpl(ManufacturedDrugManager.class);
    ManufacturedDrug manufacturedDrug = drugManager.getManufacturedDrug(din);
    if (manufacturedDrug != null) {
      ManufacturedDrugSummaryDto drugSummaryDto = new ManufacturedDrugSummaryDto();
      drugSummaryDto.setName(manufacturedDrug.getName());
      drugSummaryDto.setManufacturer(manufacturedDrug.getManufacturer());
      // DIN
      drugSummaryDto
          .setIdentifier(getDrugIdentifier(MedicationTypeApi.DIN.toString(),
              manufacturedDrug.getDrugIdentificationNumber()));

      // AIG
      drugSummaryDto.setActiveIngredientIdentifier(
          getDrugIdentifier(MedicationTypeApi.AIG.toString(),
              manufacturedDrug.getActiveIngredientNumber()));

      prescriptionDto.setMedicationDisplayText(manufacturedDrug.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setManufacturedDrug(drugSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }
  }

  private void populateFormulationDto(PrescriptionDto prescriptionDto, Integer formulationId)
      throws ProtossException {

    FormulationManager formulationManager = getImpl(FormulationManager.class);
    Formulation formulation = formulationManager.getFormulation(formulationId);
    if (formulation != null) {
      FormulationSummaryDto formulationSummaryDto = new FormulationSummaryDto();

      formulationSummaryDto.setIdentifier(
          getDrugIdentifier(MedicationTypeApi.GCN.toString(),
              Integer.toString(formulation.getFormulationId())));
      formulationSummaryDto.setName(formulation.getName());
      formulationSummaryDto.setStrengthDescription(formulation.getStrengthDescription());

      prescriptionDto.setMedicationDisplayText(formulation.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setFormulation(formulationSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }
  }

  private void populateNaturalHealthProductDto(
      PrescriptionDto prescriptionDto, String drugId)
      throws ProtossException {
    NaturalHealthProductManager naturalHealthProductManager =
        getImpl(NaturalHealthProductManager.class);
    NaturalHealthProduct product = naturalHealthProductManager.getNaturalHealthProduct(drugId);
    if (product != null) {
      NaturalHealthProductSummaryDto productSummaryDto = new NaturalHealthProductSummaryDto();
      productSummaryDto.setName(product.getName());
      productSummaryDto.setIdentifier(
          getDrugIdentifier(MedicationTypeApi.NPN.toString(), product.getLicenceNumber()));

      prescriptionDto.setMedicationDisplayText(product.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setNaturalHealthProduct(productSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }
  }

  private void populateCustomCompoundDto(PrescriptionDto prescriptionDto,
      String compoundName,
      String compoundDetails, int rxId) throws ProtossException {
    // set ingredients on a custom compound
    IngredientManager ingredientManager = getImpl(IngredientManager.class);
    Set<Ingredient> rxIngredients = ingredientManager.get(rxId);

    if (!rxIngredients.isEmpty()) {
      // rx stores ingredients in two categories, included and excluded
      Map<Boolean, Set<Ingredient>> ingredients = rxIngredients.stream()
          .collect(Collectors.groupingBy(Ingredient::isDrugIncluded, Collectors.toSet()));

      Set<IngredientSummaryDto> included =
          mapDto(ingredients.getOrDefault(true, new HashSet<>()), IngredientSummaryDto.class,
              HashSet::new);
      Set<IngredientSummaryDto> excluded =
          mapDto(ingredients.getOrDefault(false, new HashSet<>()), IngredientSummaryDto.class,
              HashSet::new);

      included.stream().forEach(t -> t.setCodeSystem(MedicationTypeApi.HIC.toString()));
      excluded.stream().forEach(t -> t.setCodeSystem(MedicationTypeApi.HIC.toString()));

      CustomCompoundSummaryDto compoundDto = new CustomCompoundSummaryDto();
      compoundDto.setName(compoundName);
      compoundDto.setDescription(compoundDetails);
      compoundDto.setIncludedIngredients(included);
      compoundDto.setExcludedIngredients(excluded);

      prescriptionDto.setMedicationDisplayText(compoundName + " " + compoundDetails);
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setCustomCompound(compoundDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }
  }

  private void populateAlternativeHealthProductDetails(PrescriptionDto rx, int medicationId)
      throws ProtossException {
    AlternativeHealthProductManager ahpManager = getImpl(AlternativeHealthProductManager.class);
    AlternativeHealthProduct ahp = ahpManager.getAlternativeHealthProduct(medicationId);
    if (ahp != null) {
      AlternativeHealthProductSummaryDto ahpDto =
          mapDto(ahp, AlternativeHealthProductSummaryDto.class);
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setAlternativeHealthProduct(ahpDto);
      rx.setMedicationDetails(medDetails);
      rx.setMedicationDisplayText(ahp.getName() + " - " + ahp.getDescription());
    }
  }

  private void getDosages(PrescriptionDto prescriptionDto, int prescriptionId)
      throws ProtossException {
    DosageManager dosageManager = getImpl(DosageManager.class);
    List<Dosage> protossDosage = dosageManager.getForPrescription(prescriptionId);

    if (!protossDosage.isEmpty()) {
      // Sort the dosage set by start date to build the dosage instructions text in order/sequence
      List<DosageDto> dosageDtos =
          mapDto(protossDosage, DosageDto.class, ArrayList::new);

      prescriptionDto.setDosages(dosageDtos);
      prescriptionDto
          .setInstructionsDisplayText(generateDosageInstructionDisplayText(dosageDtos));
    }
  }

  private String generateDosageInstructionDisplayText(List<DosageDto> dosages)
      throws DatabaseInteractionException {
    StringBuilder instruction = new StringBuilder();

    // retrieve measure units and dosage frequency types
    Map<String, String> descriptionByDosageFrequencyType = getDescriptionByMnemonic("99P0108");
    Map<String, String> descriptionByMeasureUnit = getDescriptionByMnemonic("99P0100");

    instruction.append("Dosages: ");
    boolean notFirstDosage = false;
    LocalDate prevDosageStartDate = null;
    DosageDto prevDosage = null;
    for (DosageDto dosage : dosages) {
      LocalDate curDosageStartDate =
          dosage.getStartDate() == null ? null : dosage.getStartDate().toLocalDate();

      if (notFirstDosage) {
        if (dosage.isConcurrent()) {
          instruction.append("AND ");
        } else {
          instruction.append("THEN ");

          if (curDosageStartDate != null && prevDosageStartDate != null) {
            // calculate end date of previous dosage
            LocalDate prevDosageEndDate = prevDosageStartDate.plusDays(
                getDurationInDays(prevDosage.getDurationAmount(), prevDosage.getDurationUnit()));

            if (curDosageStartDate.isAfter(prevDosageEndDate)) {
              int daysBreak = Days.daysBetween(prevDosageEndDate, curDosageStartDate).getDays();
              instruction.append("NOTHING for ");
              instruction.append(daysBreak).append(" ");
              instruction.append("day(s) ");
              instruction.append("THEN ");
            }
          }
        }
      }

      instruction.append(formatFloat(dosage.getMin()));
      if (dosage.getMax() > 0) {
        instruction.append("-");
        instruction.append(formatFloat(dosage.getMax()));
      }
      instruction.append(" ");

      String measureUnit = descriptionByMeasureUnit.get(dosage.getUnit());
      instruction.append(StringUtils.isBlank(measureUnit) ? dosage.getUnit() : measureUnit);
      instruction.append(" ");

      if (dosage.getEyeCode() != null && !EyeCode.NO_EYES.equals(dosage.getEyeCode())) {
        instruction.append(dosage.getEyeCode().toString());
        instruction.append(" ");
      }

      String dosageFrequencyType = descriptionByDosageFrequencyType.get(dosage.getIntervalTime());
      instruction
          .append(StringUtils.isBlank(dosageFrequencyType) ? dosage.getIntervalTime()
              : dosageFrequencyType);
      instruction.append(" ");

      if (dosage.isProReNata()) {
        instruction.append("As Needed ");
      }

      if (!"STAT".equals(dosage.getIntervalTime())) {
        instruction.append("for ");
        instruction.append(formatFloat(dosage.getDurationAmount())).append(" ");
        EmrUnitsMapping unitsMapping = new EmrUnitsMapping();
        TreeMap<String, String> allDurations = unitsMapping.getAllDurUnits();
        String duration = allDurations.get(dosage.getDurationUnit());
        instruction.append(StringUtils.isBlank(duration) ? dosage.getDurationUnit() : duration);
        instruction.append(" ");
      }

      boolean includeDate = false;
      if (curDosageStartDate != null) {
        boolean notSameDay = !curDosageStartDate.equals(prevDosageStartDate);
        if (!notFirstDosage || (dosage.isConcurrent() && notSameDay)) {
          includeDate = true;
        }
      }

      if (includeDate && curDosageStartDate != null) {
        instruction.append("starting on ");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MMM-dd");
        instruction.append(curDosageStartDate.toString(fmt));
        instruction.append(" ");
      }

      notFirstDosage = true;
      prevDosage = dosage;
      prevDosageStartDate = curDosageStartDate;
    }
    return instruction.toString().trim();
  }

  private void populateGenericDrugDetails(PrescriptionDto rx, int medicationId)
      throws ProtossException {
    GenericDrugManager genericDrugManager = getImpl(GenericDrugManager.class);
    GenericDrug genericDrug = genericDrugManager.getGenericDrug(medicationId);
    if (genericDrug != null) {
      GenericDrugDto genericDrugDto = mapDto(genericDrug, GenericDrugDto.class);
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setGenericDrug(genericDrugDto);
      rx.setMedicationDetails(medDetails);
    }
  }

  private DrugIdentifier getDrugIdentifier(String code, String value) {
    DrugIdentifier drugIdentifier = new DrugIdentifier();
    drugIdentifier.setCodeSystem(code);
    drugIdentifier.setValue(value);
    return drugIdentifier;
  }

  private Map<String, String> getDescriptionByMnemonic(String tableId)
      throws DatabaseInteractionException {
    CodeSystemManager codeSystemManager = getImpl(CodeSystemManager.class);
    CodeSystem codeSystem = codeSystemManager.getCodeSystem(tableId);
    List<CodeValue> codeValues = Stream
        .concat(codeSystem.getBuiltInValues().stream(),
            codeSystem.getCustomValues().stream())
        .distinct()
        .collect(Collectors.toList());
    return codeValues
        .stream()
        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getDescription));
  }

  // copied from Accuro#MedicationManager#getDurationInDays
  private int getDurationInDays(float duration, String units) {

    int unitDuration = 0;
    if (units.equals("Day")) {
      unitDuration = 1;
    } else if (units.equals("Week")) {
      unitDuration = 7;
    } else if (units.equals("MO28")) {
      unitDuration = 28;
    } else if (units.equals("MO30")) {
      unitDuration = 30;
    } else if (units.equals("Year")) {
      unitDuration = 365;
    }

    return (int) ((double) duration * (double) unitDuration);
  }
}
