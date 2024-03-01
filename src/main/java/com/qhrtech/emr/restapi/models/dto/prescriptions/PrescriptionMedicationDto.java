
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.restapi.models.dto.LimitedUseCodeDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.MedicationType;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * The prescription medication transfer model.
 */
@Schema(description = "Prescription medication transfer model")
public class PrescriptionMedicationDto {

  @JsonProperty("prescriptionId")
  @Schema(description = "The unique prescription id", example = "1")
  private int prescriptionId;
  @JsonProperty("uuid")
  @Schema(description = "The mapped uuid for the prescription id",
      example = "cda20596-d35e-425b-87a5-d190281a020c")
  private String uuid;
  @JsonProperty("medicationId")
  @Schema(description = "The medication id of the medication prescribed", example = "1")
  private int medicationId; // The drug
  @JsonProperty("maxDispenseQuantity")
  @Schema(description = "The maximum dispense quantity", example = "0")
  private int maxDispenseQuantity;
  @JsonProperty("dispenseInterval")
  @Schema(description = "The dispense interval", example = "0")
  private int dispenseInterval;
  @JsonProperty("patientId")
  @Schema(description = "The patient id of the patient having this prescription", example = "1")
  private Integer patientId;
  @JsonProperty("refills")
  @Schema(description = "The number of refill", example = "0")
  private Integer refills;
  @JsonProperty("prescribingPhysician")
  @Schema(description = "The physician id of the physician prescribing", example = "1")
  private Integer prescribingPhysician;
  @JsonProperty("authorizingPhysician")
  @Schema(description = "The physician id of the physician authorized")
  private Integer authorizingPhysician;
  @JsonProperty("maskId")
  @Schema(description = "The mask id", example = "1")
  private Integer maskId;
  @JsonProperty("dispenseAmount")
  @Schema(description = "The dispense amount", example = "1")
  private Integer dispenseAmount;
  @JsonProperty("daysRemaining")
  @Schema(description = "Days remaining", example = "0")
  private Integer daysRemaining;
  @JsonProperty("creatorUserId")
  @Schema(description = "The user id of the user created the prescription", example = "1")
  private Integer creatorUserId;
  @JsonProperty("previousPrescriptionId")
  @Schema(
      description = "The previous prescription id. "
          + "Null if the prescription is the first prescription",
      nullable = true,
      example = "1")
  private Integer previousPrescriptionId;
  @JsonProperty("refillAmount")
  @Schema(description = "The refill amount", example = "1")
  private Integer refillAmount;
  @JsonProperty("userId")
  @Schema(description = "The user id", example = "1")
  private Integer userId;
  @JsonProperty("wellnetFormulationId")
  @Schema(description = "The Wellnet formulation id", example = "1")
  private Integer wellnetFormulationId;
  @JsonProperty("pharmacyId")
  @Schema(description = "The pharmacy id", example = "1")
  private Integer pharmacyId;
  @JsonProperty("active")
  @Schema(description = "The indication if the prescription is active", example = "true")
  private boolean active;
  @JsonProperty("maskDisplay")
  @Schema(description = "The indication if the prescription is masked", example = "true")
  private boolean maskDisplay; // toString is masked
  @JsonProperty("externalRx")
  @Schema(description = "The indication if the prescription is an external prescription",
      example = "true")
  private boolean externalRx; // A Rx from another provider
  @JsonProperty("nondrug")
  @Schema(description = "The indication if the prescribed medication is non-drug",
      example = "true")
  private boolean nondrug; // States if this is a drug or a non drug prescription
  @JsonProperty("legacy")
  @Schema(description = "The indication if the prescription is legacy", example = "true")
  private boolean legacy; // Is this a prescription from Pre VCUR?
  @JsonProperty("autoExpire")
  @Schema(description = "The indication if the prescription is auto-expired",
      example = "true")
  private boolean autoExpire;
  @JsonProperty("renewed")
  @Schema(description = "The indication if the prescription is re-newed", example = "true")
  private boolean renewed;
  @JsonProperty("quantityModified")
  @Schema(description = "The indication if the quantity is modified", example = "true")
  private boolean quantityModified;
  @JsonProperty("quantityUnitModified")
  @Schema(description = "The indication if the quantity unit is modified", example = "true")
  private boolean quantityUnitModified;
  @JsonProperty("nonAuthoritativeIndicator")
  @Schema(description = "The indication if the prescribed indicator is non-authoritative",
      example = "true")
  private boolean nonAuthoritativeIndicator;
  @JsonProperty("allowSubstitutions")
  @Schema(
      description = "The indication if the prescription allows substitutions.",
      nullable = true,
      example = "true")
  private Boolean allowSubstitutions;
  @JsonProperty("allowTrialDispenses")
  @Schema(
      description = "The indication if the prescription allows trial dispenses",
      nullable = true,
      example = "true")
  private Boolean allowTrialDispenses;
  @JsonProperty("compliancePackageRequired")
  @Schema(
      description = "The indication if the prescription requires compliance package",
      nullable = true,
      example = "true")
  private Boolean compliancePackageRequired;
  @JsonProperty("patientCompliance")
  @Schema(
      description = "The indication if a prescribed patient is compliance",
      nullable = true,
      example = "true")
  private Boolean patientCompliance;
  @JsonProperty("localStartDate")
  @Schema(description = "The prescription local start date", type = "string",
      example = "2017-11-29")
  private LocalDate localStartDate;
  @JsonProperty("dosage")
  @Schema(description = "The dosage", example = "TAB")
  private String dosage;
  @JsonProperty("dosageForm")
  @Schema(description = "The dosage form", example = "TAB")
  private String dosageForm; // Dosage Form (Pill, tablet, etc...)
  @JsonProperty("medType")
  @Schema(description = "The medication type", example = "MEDICATION TYPE")
  private String medType;
  @JsonProperty("pharmacyInstructions")
  @Schema(description = "The pharmacy instructions", example = "DO NOT TAKE 2 PILLS AT ONCE")
  private String pharmacyInstructions; // Pharmacy Instructions
  @JsonProperty("sigInstructions")
  @Schema(description = "The sig instructions which is labeled in prescription",
      example = "TAKE 1 PILL, BY MOUTH, AT BEDTIME")
  private String sigInstructions; // Patient Instructions
  @JsonProperty("route")
  @Schema(description = "The route to take the medication", example = "Oral")
  private String route;
  @JsonProperty("interval")
  @Schema(description = "The interval quantity to take the medication", example = "1")
  private String interval;
  @JsonProperty("amount")
  @Schema(description = "Unit to take the medication", example = "TAB")
  private String amount;
  @JsonProperty("medStatus")
  @Schema(
      description = "Status of the prescription",
      example = "Recently Active",
      allowableValues = {"Active", "Recently Active", "Inactive"})
  private String medStatus; // Active, Recently Active, Inactive
  @JsonProperty("dispenseUnit")
  @Schema(description = "The dispense unit", example = "TAB")
  private String dispenseUnit;
  @JsonProperty("drugUse")
  @Schema(description = "The drug use on Accuro", example = "One Time")
  private String drugUse;
  @JsonProperty("openIndication")
  @Schema(description = "The open indication", example = "TAKE 1PILL EVERYDAY")
  private String openIndication;
  @JsonProperty("din")
  @Schema(description = "The drug identification number as DIN", example = "1")
  private String din; // drug id
  @JsonProperty("compoundName")
  @Schema(
      description = "The compound name. An empty string If the prescription is not a compound.",
      example = "SAMPLE COMPOUND")
  private String compoundName;
  @JsonProperty("wellnetId")
  @Schema(description = "The Wellnet id", example = "1")
  private String wellnetId;
  @JsonProperty("orderStatus")
  @Schema(description = "The order status", example = "Unknown")
  private String orderStatus;
  @JsonProperty("compoundDetails")
  @Schema(
      description = "The compound details. Empty if the prescription is not a compound.",
      example = "THIS IS A SAMPLE COMPOUND")
  private String compoundDetails;
  @JsonProperty("providerId")
  @Schema(description = "The provider id of a provider prescribed", example = "1")
  private String providerId;
  @JsonProperty("providerName")
  @Schema(description = "The name of a provider prescribed", example = "David Doctor")
  private String providerName;
  @JsonProperty("orderCode")
  @Schema(description = "The order code", example = "1")
  private String orderCode; // Code for Description of status
  @JsonProperty("source")
  @Schema(description = "The source of the drug", example = "PIN")
  private String source;
  @JsonProperty("maxDispenseUnit")
  @Schema(description = "The maximum dispense unit", example = "APPLN")
  private String maxDispenseUnit;
  @JsonProperty("dispenseIntervalUnit")
  @Schema(description = "The dispense interval unit", example = "AN")
  private String dispenseIntervalUnit;
  @JsonProperty("dispensedNote")
  @Schema(description = "The dispense note", example = "SAMPLE NOTE")
  private String dispensedNote;
  @JsonProperty("externalMedicationState")
  @Schema(description = "The external medication state")
  private String externalMedicationState; // state of the external medications
  @JsonProperty("protocolIdentifier")
  @Schema(description = "The protocol identifier")
  private String protocolIdentifier;
  @JsonProperty("eprescribeStatus")
  @Schema(description = "Electronic prescription status", example = "NEW")
  private String eprescribeStatus;
  @JsonProperty("note")
  @Schema(description = "The note", example = "TAKE 2 PILLS AT ONCE")
  private String note; // comments
  @JsonProperty("dinSystem")
  @Schema(description = "The medication type", example = "DIN")
  private MedicationType dinSystem; // DIN system/Medication category
  @JsonProperty("writtenDate")
  @Schema(description = "The written date of the prescription", example = "2017/11/29T00:00:00",
      type = "string")
  private AccuroCalendar writtenDate;
  @JsonProperty("expiryDate")
  @Schema(description = "The expired date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate expiryDate;
  @JsonProperty("effectiveDate")
  @Schema(description = "The effective date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate effectiveDate;
  @JsonProperty("pickUpDate")
  @Schema(description = "The pick-up date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate pickUpDate;
  @JsonProperty("dispensedDate")
  @Schema(description = "The dispensed date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate dispensedDate;
  @JsonProperty("createdDate")
  @Schema(description = "The created date of the prescription", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime createdDate;
  @JsonProperty("lastModified")
  @Schema(description = "The last modified date of the prescription", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime lastModified;

  @JsonProperty("doNotAutofill")
  @Schema(description = "Do not auto fill flag for prescription", nullable = true,
      example = "true")
  private Boolean doNotAutofill;

  @JsonProperty("allowRenewalRequests")
  @Schema(description = "Allow renewal requests flag for prescription", nullable = true,
      example = "false")
  private Boolean allowRenewalRequests;

  @JsonProperty("prescriptionDetails")
  @Schema(description = "The prescription details")
  private PrescriptionDetailsDto prescriptionDetails;
  @JsonProperty("dosages")
  @Schema(description = "A set of dosages")
  private Set<DosageDto> dosages;
  @JsonProperty("indications")
  @Schema(description = "A set of indications")
  private Set<PrescriptionIndicationDto> indications;
  @JsonProperty("annotations")
  @Schema(description = "A list of annotations")
  private List<AnnotationDto> annotations;
  @JsonProperty("statusHistories")
  @Schema(description = "A set of status histories")
  private Set<StatusHistoryDto> statusHistories;
  @JsonProperty("interactions")
  @Schema(description = "A set of interactions")
  private Set<InteractionDto> interactions;
  @JsonProperty("wellnetPrescriptionLinks")
  @Schema(description = "A set of Wellnet prescription links")
  private Set<WellnetPrescriptionLinkDto> wellnetPrescriptionLinks;
  @JsonProperty("limitedUseCodes")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "A list of limited use code. This entity will return on Ontario and "
      + "when the medication type is DIN")
  private List<LimitedUseCodeDto> limitedUseCodes;

  /**
   * A unique prescription id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(int prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * A medication id of a medication prescribed.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getMedicationId() {
    return medicationId;
  }

  public void setMedicationId(int medicationId) {
    this.medicationId = medicationId;
  }

  /**
   * A maximum dispense quantity.
   *
   * @documentationExample 0
   *
   * @return
   */
  public int getMaxDispenseQuantity() {
    return maxDispenseQuantity;
  }

  public void setMaxDispenseQuantity(int maxDispenseQuantity) {
    this.maxDispenseQuantity = maxDispenseQuantity;
  }

  /**
   * A dispense interval.
   *
   * @documentationExample 0
   *
   * @return
   */
  public int getDispenseInterval() {
    return dispenseInterval;
  }

  public void setDispenseInterval(int dispenseInterval) {
    this.dispenseInterval = dispenseInterval;
  }

  /**
   * A patient id of a patient having this prescription.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * The number of refill.
   *
   * @documentationExample 0
   *
   * @return
   */
  public Integer getRefills() {
    return refills;
  }

  public void setRefills(Integer refills) {
    this.refills = refills;
  }

  /**
   * A physician id of a physician prescribing.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getPrescribingPhysician() {
    return prescribingPhysician;
  }

  public void setPrescribingPhysician(Integer prescribingPhysician) {
    this.prescribingPhysician = prescribingPhysician;
  }

  /**
   * A physician id of a physician authorized.
   *
   * @return
   */
  public Integer getAuthorizingPhysician() {
    return authorizingPhysician;
  }

  public void setAuthorizingPhysician(Integer authorizingPhysician) {
    this.authorizingPhysician = authorizingPhysician;
  }

  /**
   * A mask id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * A dispense amount.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getDispenseAmount() {
    return dispenseAmount;
  }

  public void setDispenseAmount(Integer dispenseAmount) {
    this.dispenseAmount = dispenseAmount;
  }

  /**
   * Days remaining.
   *
   * @documentationExample 0
   *
   * @return
   */
  public Integer getDaysRemaining() {
    return daysRemaining;
  }

  public void setDaysRemaining(Integer daysRemaining) {
    this.daysRemaining = daysRemaining;
  }

  /**
   * A user id of a user created the prescription.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * A previous prescription id.
   *
   * <p>
   * {@code null} If the prescription is the first prescription.
   * </p>
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getPreviousPrescriptionId() {
    return previousPrescriptionId;
  }

  public void setPreviousPrescriptionId(Integer previousPrescriptionId) {
    this.previousPrescriptionId = previousPrescriptionId;
  }

  /**
   * A refill amount.
   *
   * @documentationExample 1
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getRefillAmount() {
    return refillAmount;
  }

  public void setRefillAmount(Integer refillAmount) {
    this.refillAmount = refillAmount;
  }

  /**
   * A user id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * A Wellnet formulation id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getWellnetFormulationId() {
    return wellnetFormulationId;
  }

  public void setWellnetFormulationId(Integer wellnetFormulationId) {
    this.wellnetFormulationId = wellnetFormulationId;
  }

  /**
   * A pharmacy id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getPharmacyId() {
    return pharmacyId;
  }

  public void setPharmacyId(Integer pharmacyId) {
    this.pharmacyId = pharmacyId;
  }

  /**
   * A flag if the prescription is active.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * A flag if the prescription is masked.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isMaskDisplay() {
    return maskDisplay;
  }

  public void setMaskDisplay(boolean maskDisplay) {
    this.maskDisplay = maskDisplay;
  }

  /**
   * A flag if the prescription is an external prescription.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isExternalRx() {
    return externalRx;
  }

  public void setExternalRx(boolean externalRx) {
    this.externalRx = externalRx;
  }

  /**
   * A flag if the prescribed medication is non-drug.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isNondrug() {
    return nondrug;
  }

  public void setNondrug(boolean nondrug) {
    this.nondrug = nondrug;
  }

  /**
   * A flag if the prescription is legacy.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isLegacy() {
    return legacy;
  }

  public void setLegacy(boolean legacy) {
    this.legacy = legacy;
  }

  /**
   * A flag if the prescription is auto-expired.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isAutoExpire() {
    return autoExpire;
  }

  public void setAutoExpire(boolean autoExpire) {
    this.autoExpire = autoExpire;
  }

  /**
   * A flag if the prescirption is re-newed.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isRenewed() {
    return renewed;
  }

  public void setRenewed(boolean renewed) {
    this.renewed = renewed;
  }

  /**
   * A flag if the quantity is modified.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isQuantityModified() {
    return quantityModified;
  }

  public void setQuantityModified(boolean quantityModified) {
    this.quantityModified = quantityModified;
  }

  /**
   * A flag if the quantity unit is modified.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isQuantityUnitModified() {
    return quantityUnitModified;
  }

  public void setQuantityUnitModified(boolean quantityUnitModified) {
    this.quantityUnitModified = quantityUnitModified;
  }

  /**
   * A flag if the prescribed indicator is non-authoritative.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isNonAuthoritativeIndicator() {
    return nonAuthoritativeIndicator;
  }

  public void setNonAuthoritativeIndicator(boolean nonAuthoritativeIndicator) {
    this.nonAuthoritativeIndicator = nonAuthoritativeIndicator;
  }

  /**
   * A flag if the prescription allows substitutions.
   *
   * <p>
   * This entity can be {@code null}.
   * </p>
   *
   * @documentationExample true
   *
   * @return
   */
  public Boolean getAllowSubstitutions() {
    return allowSubstitutions;
  }

  public void setAllowSubstitutions(Boolean allowSubstitutions) {
    this.allowSubstitutions = allowSubstitutions;
  }

  /**
   * A flag if the prescription allows trial dispenses.
   *
   * <p>
   * This entity can be {@code null}.
   * </p>
   *
   * @documentationExample true
   *
   * @return
   */
  public Boolean getAllowTrialDispenses() {
    return allowTrialDispenses;
  }

  public void setAllowTrialDispenses(Boolean allowTrialDispenses) {
    this.allowTrialDispenses = allowTrialDispenses;
  }

  /**
   * A flag if the prescription requires compliance package.
   *
   * <p>
   * This entity can be {@code null}.
   * </p>
   *
   * @documentationExample true
   *
   * @return
   */
  public Boolean getCompliancePackageRequired() {
    return compliancePackageRequired;
  }

  public void setCompliancePackageRequired(Boolean compliancePackageRequired) {
    this.compliancePackageRequired = compliancePackageRequired;
  }

  /**
   * A flag if a prescribed patient is compliance.
   *
   * <p>
   * This entity can be {@code null}.
   * </p>
   *
   * @documentationExample true
   *
   * @return
   */
  public Boolean getPatientCompliance() {
    return patientCompliance;
  }

  public void setPatientCompliance(Boolean patientCompliance) {
    this.patientCompliance = patientCompliance;
  }

  /**
   * A prescription local start date.
   *
   * @return start date in {@link LocalDate} format.
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getLocalStartDate() {
    return localStartDate;
  }

  public void setLocalStartDate(LocalDate localStartDate) {
    this.localStartDate = localStartDate;
  }

  /**
   * A dosage.
   *
   * @documentationExample TAB
   *
   * @return
   */
  public String getDosage() {
    return dosage;
  }

  public void setDosage(String dosage) {
    this.dosage = dosage;
  }

  /**
   * A dosage form.
   *
   * @documentationExample TAB
   *
   * @return
   */
  public String getDosageForm() {
    return dosageForm;
  }

  public void setDosageForm(String dosageForm) {
    this.dosageForm = dosageForm;
  }

  /**
   * A medication type.
   *
   * @documentationExample MEDICATION TYPE
   *
   * @return
   */
  public String getMedType() {
    return medType;
  }

  public void setMedType(String medType) {
    this.medType = medType;
  }

  /**
   * A pharmacy instructions.
   *
   * @documentationExample DO NOT TAKE 2 PILLS AT ONCE
   *
   * @return
   */
  public String getPharmacyInstructions() {
    return pharmacyInstructions;
  }

  public void setPharmacyInstructions(String pharmacyInstructions) {
    this.pharmacyInstructions = pharmacyInstructions;
  }

  /**
   * A sig instructions which is labeled in prescription.
   *
   * @documentationExample TAKE 1 PILL, BY MOUTH, AT BEDTIME
   *
   * @return
   */
  public String getSigInstructions() {
    return sigInstructions;
  }

  public void setSigInstructions(String sigInstructions) {
    this.sigInstructions = sigInstructions;
  }

  /**
   * A route to take the medication.
   *
   * @documentationExample Oral
   *
   * @return
   */
  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  /**
   * An interval quantity to take the medication.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getInterval() {
    return interval;
  }

  public void setInterval(String interval) {
    this.interval = interval;
  }

  /**
   *
   * Unit to take the medication.
   *
   * @documentationExample TAB
   *
   * @return
   */
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  /**
   * Status of the prescription.
   *
   * @documentationExample Recently Active
   *
   * @return
   */
  public String getMedStatus() {
    return medStatus;
  }

  public void setMedStatus(String medStatus) {
    this.medStatus = medStatus;
  }

  /**
   * A dispense unit.
   *
   * @documentationExample TAB
   *
   * @return
   */
  public String getDispenseUnit() {
    return dispenseUnit;
  }

  public void setDispenseUnit(String dispenseUnit) {
    this.dispenseUnit = dispenseUnit;
  }

  /**
   * A drug use on Accuro.
   *
   * @documentationExample One Time
   *
   * @return
   */
  public String getDrugUse() {
    return drugUse;
  }

  public void setDrugUse(String drugUse) {
    this.drugUse = drugUse;
  }

  /**
   * A open indication.
   *
   * @documentationExample TAKE 1PILL EVERYDAY
   *
   * @return
   */
  public String getOpenIndication() {
    return openIndication;
  }

  public void setOpenIndication(String openIndication) {
    this.openIndication = openIndication;
  }

  /**
   * A drug identification number as DIN.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getDin() {
    return din;
  }

  public void setDin(String din) {
    this.din = din;
  }

  /**
   * A compound name.
   *
   * <p>
   * An empty string If the prescription is not a compound.
   * </p>
   *
   * @documentationExample SAMPLE COMPOUND
   *
   * @return
   */
  public String getCompoundName() {
    return compoundName;
  }

  public void setCompoundName(String compoundName) {
    this.compoundName = compoundName;
  }

  /**
   * A Wellnet id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getWellnetId() {
    return wellnetId;
  }

  public void setWellnetId(String wellnetId) {
    this.wellnetId = wellnetId;
  }

  /**
   * An order status.
   *
   * @documentationExample Unknown
   *
   * @return
   */
  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  /**
   * A compound details.
   *
   * <p>
   * An empty string If the prescription is not a compound.
   * </p>
   *
   * @documentationExample THIS IS A SAMPLE COMPOUND
   *
   * @return
   */
  public String getCompoundDetails() {
    return compoundDetails;
  }

  public void setCompoundDetails(String compoundDetails) {
    this.compoundDetails = compoundDetails;
  }

  /**
   * A provider id of a provider prescribed.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  /**
   * A name of a provider prescribed.
   *
   * @documentationExample QHR PROVIDER
   *
   * @return
   */
  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  /**
   * An order code.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  /**
   * A source of the drug.
   *
   * @documentationExample PIN
   *
   * @return
   */
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  /**
   * A maximum dispense unit.
   *
   * @documentationExample APPLN
   *
   * @return
   */
  public String getMaxDispenseUnit() {
    return maxDispenseUnit;
  }

  public void setMaxDispenseUnit(String maxDispenseUnit) {
    this.maxDispenseUnit = maxDispenseUnit;
  }

  /**
   * A dispense interval unit.
   *
   * @documentationExample AN
   *
   * @return
   */
  public String getDispenseIntervalUnit() {
    return dispenseIntervalUnit;
  }

  public void setDispenseIntervalUnit(String dispenseIntervalUnit) {
    this.dispenseIntervalUnit = dispenseIntervalUnit;
  }

  /**
   * A dispense note.
   *
   * @documentationExample SAMPLE NOTE
   *
   * @return
   */
  public String getDispensedNote() {
    return dispensedNote;
  }

  public void setDispensedNote(String dispensedNote) {
    this.dispensedNote = dispensedNote;
  }

  /**
   * An external medication state.
   *
   * @documentationExample
   *
   * @return
   */
  public String getExternalMedicationState() {
    return externalMedicationState;
  }

  public void setExternalMedicationState(String externalMedicationState) {
    this.externalMedicationState = externalMedicationState;
  }

  /**
   * A protocol identifier.
   *
   * @documentationExample
   *
   * @return
   */
  public String getProtocolIdentifier() {
    return protocolIdentifier;
  }

  public void setProtocolIdentifier(String protocolIdentifier) {
    this.protocolIdentifier = protocolIdentifier;
  }

  /**
   * Electronic prescription status.
   *
   * @documentationExample NEW
   *
   * @return
   */
  public String getEprescribeStatus() {
    return eprescribeStatus;
  }

  public void setEprescribeStatus(String eprescribeStatus) {
    this.eprescribeStatus = eprescribeStatus;
  }

  /**
   * A note.
   *
   * @documentationExample TAKE 2 PILLS AT ONCE
   *
   * @return
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * A medication type.
   *
   * @documentationExample DIN
   *
   * @return
   */
  public MedicationType getDinSystem() {
    return dinSystem;
  }

  public void setDinSystem(MedicationType dinSystem) {
    this.dinSystem = dinSystem;
  }

  /**
   * A written date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017/11/29T00:00:00")
  @TypeHint(String.class)
  public AccuroCalendar getWrittenDate() {
    return writtenDate;
  }

  public void setWrittenDate(AccuroCalendar writtenDate) {
    this.writtenDate = writtenDate;
  }

  /**
   * An expired date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * An effective date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDate effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  /**
   * A pick-up date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getPickUpDate() {
    return pickUpDate;
  }

  public void setPickUpDate(LocalDate pickUpDate) {
    this.pickUpDate = pickUpDate;
  }

  /**
   * A dispensed date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getDispensedDate() {
    return dispensedDate;
  }

  public void setDispensedDate(LocalDate dispensedDate) {
    this.dispensedDate = dispensedDate;
  }

  /**
   * A created date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * A last modified date of the prescription.
   *
   * @return
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * A prescription details.
   *
   * @return
   */
  public PrescriptionDetailsDto getPrescriptionDetails() {
    return prescriptionDetails;
  }

  public void setPrescriptionDetails(
      PrescriptionDetailsDto prescriptionDetails) {
    this.prescriptionDetails = prescriptionDetails;
  }

  /**
   * A set of dosages.
   *
   * @return
   */
  public Set<DosageDto> getDosages() {
    return dosages;
  }

  public void setDosages(Set<DosageDto> dosages) {
    this.dosages = dosages;
  }

  /**
   * A set of indications.
   *
   * @return
   */
  public Set<PrescriptionIndicationDto> getIndications() {
    return indications;
  }

  public void setIndications(
      Set<PrescriptionIndicationDto> indications) {
    this.indications = indications;
  }

  /**
   * A list of annotations.
   *
   * @return
   */
  public List<AnnotationDto> getAnnotations() {
    return annotations;
  }

  public void setAnnotations(
      List<AnnotationDto> annotations) {
    this.annotations = annotations;
  }

  /**
   * A set of status histories.
   *
   * @return
   */
  public Set<StatusHistoryDto> getStatusHistories() {
    return statusHistories;
  }

  public void setStatusHistories(
      Set<StatusHistoryDto> statusHistories) {
    this.statusHistories = statusHistories;
  }

  /**
   * A set of interactions.
   *
   * @return
   */
  public Set<InteractionDto> getInteractions() {
    return interactions;
  }

  public void setInteractions(
      Set<InteractionDto> interactions) {
    this.interactions = interactions;
  }

  /**
   * A set of Wellnet prescription links
   *
   * @return
   */
  public Set<WellnetPrescriptionLinkDto> getWellnetPrescriptionLinks() {
    return wellnetPrescriptionLinks;
  }

  public void setWellnetPrescriptionLinks(
      Set<WellnetPrescriptionLinkDto> wellnetPrescriptionLinks) {
    this.wellnetPrescriptionLinks = wellnetPrescriptionLinks;
  }

  /**
   * A list of limited use code. This entity will return on Ontario and when the medication type is
   * DIN.
   *
   * @return
   */
  public List<LimitedUseCodeDto> getLimitedUseCodes() {
    return limitedUseCodes;
  }

  public void setLimitedUseCodes(
      List<LimitedUseCodeDto> limitedUseCodes) {
    this.limitedUseCodes = limitedUseCodes;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Boolean getDoNotAutofill() {
    return doNotAutofill;
  }

  public void setDoNotAutofill(Boolean doNotAutofill) {
    this.doNotAutofill = doNotAutofill;
  }

  public Boolean getAllowRenewalRequests() {
    return allowRenewalRequests;
  }

  public void setAllowRenewalRequests(Boolean allowRenewalRequests) {
    this.allowRenewalRequests = allowRenewalRequests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PrescriptionMedicationDto)) {
      return false;
    }

    PrescriptionMedicationDto that = (PrescriptionMedicationDto) o;

    if (getPrescriptionId() != that.getPrescriptionId()) {
      return false;
    }
    if (getMedicationId() != that.getMedicationId()) {
      return false;
    }
    if (getMaxDispenseQuantity() != that.getMaxDispenseQuantity()) {
      return false;
    }
    if (getDispenseInterval() != that.getDispenseInterval()) {
      return false;
    }
    if (isActive() != that.isActive()) {
      return false;
    }
    if (isMaskDisplay() != that.isMaskDisplay()) {
      return false;
    }
    if (isExternalRx() != that.isExternalRx()) {
      return false;
    }
    if (isNondrug() != that.isNondrug()) {
      return false;
    }
    if (isLegacy() != that.isLegacy()) {
      return false;
    }
    if (isAutoExpire() != that.isAutoExpire()) {
      return false;
    }
    if (isRenewed() != that.isRenewed()) {
      return false;
    }
    if (isQuantityModified() != that.isQuantityModified()) {
      return false;
    }
    if (isQuantityUnitModified() != that.isQuantityUnitModified()) {
      return false;
    }
    if (isNonAuthoritativeIndicator() != that.isNonAuthoritativeIndicator()) {
      return false;
    }
    if (getPatientId() != null ? !getPatientId().equals(that.getPatientId())
        : that.getPatientId() != null) {
      return false;
    }
    if (getRefills() != null ? !getRefills().equals(that.getRefills())
        : that.getRefills() != null) {
      return false;
    }
    if (getPrescribingPhysician() != null ? !getPrescribingPhysician()
        .equals(that.getPrescribingPhysician()) : that.getPrescribingPhysician() != null) {
      return false;
    }
    if (getAuthorizingPhysician() != null ? !getAuthorizingPhysician()
        .equals(that.getAuthorizingPhysician()) : that.getAuthorizingPhysician() != null) {
      return false;
    }
    if (getMaskId() != null ? !getMaskId().equals(that.getMaskId()) : that.getMaskId() != null) {
      return false;
    }
    if (getDispenseAmount() != null ? !getDispenseAmount().equals(that.getDispenseAmount())
        : that.getDispenseAmount() != null) {
      return false;
    }
    if (getDaysRemaining() != null ? !getDaysRemaining().equals(that.getDaysRemaining())
        : that.getDaysRemaining() != null) {
      return false;
    }
    if (getCreatorUserId() != null ? !getCreatorUserId().equals(that.getCreatorUserId())
        : that.getCreatorUserId() != null) {
      return false;
    }
    if (getPreviousPrescriptionId() != null ? !getPreviousPrescriptionId()
        .equals(that.getPreviousPrescriptionId()) : that.getPreviousPrescriptionId() != null) {
      return false;
    }
    if (getRefillAmount() != null ? !getRefillAmount().equals(that.getRefillAmount())
        : that.getRefillAmount() != null) {
      return false;
    }
    if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
      return false;
    }
    if (getWellnetFormulationId() != null ? !getWellnetFormulationId()
        .equals(that.getWellnetFormulationId()) : that.getWellnetFormulationId() != null) {
      return false;
    }
    if (getPharmacyId() != null ? !getPharmacyId().equals(that.getPharmacyId())
        : that.getPharmacyId() != null) {
      return false;
    }
    if (getAllowSubstitutions() != null ? !getAllowSubstitutions()
        .equals(that.getAllowSubstitutions()) : that.getAllowSubstitutions() != null) {
      return false;
    }
    if (getAllowTrialDispenses() != null ? !getAllowTrialDispenses()
        .equals(that.getAllowTrialDispenses()) : that.getAllowTrialDispenses() != null) {
      return false;
    }
    if (getCompliancePackageRequired() != null ? !getCompliancePackageRequired()
        .equals(that.getCompliancePackageRequired())
        : that.getCompliancePackageRequired() != null) {
      return false;
    }
    if (getPatientCompliance() != null ? !getPatientCompliance().equals(that.getPatientCompliance())
        : that.getPatientCompliance() != null) {
      return false;
    }
    if (getLocalStartDate() != null ? !getLocalStartDate().equals(that.getLocalStartDate())
        : that.getLocalStartDate() != null) {
      return false;
    }
    if (getDosage() != null ? !getDosage().equals(that.getDosage()) : that.getDosage() != null) {
      return false;
    }
    if (getDosageForm() != null ? !getDosageForm().equals(that.getDosageForm())
        : that.getDosageForm() != null) {
      return false;
    }
    if (getMedType() != null ? !getMedType().equals(that.getMedType())
        : that.getMedType() != null) {
      return false;
    }
    if (getPharmacyInstructions() != null ? !getPharmacyInstructions()
        .equals(that.getPharmacyInstructions()) : that.getPharmacyInstructions() != null) {
      return false;
    }
    if (getSigInstructions() != null ? !getSigInstructions().equals(that.getSigInstructions())
        : that.getSigInstructions() != null) {
      return false;
    }
    if (getRoute() != null ? !getRoute().equals(that.getRoute()) : that.getRoute() != null) {
      return false;
    }
    if (getInterval() != null ? !getInterval().equals(that.getInterval())
        : that.getInterval() != null) {
      return false;
    }
    if (getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null) {
      return false;
    }
    if (getMedStatus() != null ? !getMedStatus().equals(that.getMedStatus())
        : that.getMedStatus() != null) {
      return false;
    }
    if (getDispenseUnit() != null ? !getDispenseUnit().equals(that.getDispenseUnit())
        : that.getDispenseUnit() != null) {
      return false;
    }
    if (getDrugUse() != null ? !getDrugUse().equals(that.getDrugUse())
        : that.getDrugUse() != null) {
      return false;
    }
    if (getOpenIndication() != null ? !getOpenIndication().equals(that.getOpenIndication())
        : that.getOpenIndication() != null) {
      return false;
    }
    if (getDin() != null ? !getDin().equals(that.getDin()) : that.getDin() != null) {
      return false;
    }
    if (getCompoundName() != null ? !getCompoundName().equals(that.getCompoundName())
        : that.getCompoundName() != null) {
      return false;
    }
    if (getWellnetId() != null ? !getWellnetId().equals(that.getWellnetId())
        : that.getWellnetId() != null) {
      return false;
    }
    if (getOrderStatus() != null ? !getOrderStatus().equals(that.getOrderStatus())
        : that.getOrderStatus() != null) {
      return false;
    }
    if (getCompoundDetails() != null ? !getCompoundDetails().equals(that.getCompoundDetails())
        : that.getCompoundDetails() != null) {
      return false;
    }
    if (getProviderId() != null ? !getProviderId().equals(that.getProviderId())
        : that.getProviderId() != null) {
      return false;
    }
    if (getProviderName() != null ? !getProviderName().equals(that.getProviderName())
        : that.getProviderName() != null) {
      return false;
    }
    if (getOrderCode() != null ? !getOrderCode().equals(that.getOrderCode())
        : that.getOrderCode() != null) {
      return false;
    }
    if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) {
      return false;
    }
    if (getMaxDispenseUnit() != null ? !getMaxDispenseUnit().equals(that.getMaxDispenseUnit())
        : that.getMaxDispenseUnit() != null) {
      return false;
    }
    if (getDispenseIntervalUnit() != null ? !getDispenseIntervalUnit()
        .equals(that.getDispenseIntervalUnit()) : that.getDispenseIntervalUnit() != null) {
      return false;
    }
    if (getDispensedNote() != null ? !getDispensedNote().equals(that.getDispensedNote())
        : that.getDispensedNote() != null) {
      return false;
    }
    if (getExternalMedicationState() != null ? !getExternalMedicationState()
        .equals(that.getExternalMedicationState()) : that.getExternalMedicationState() != null) {
      return false;
    }
    if (getProtocolIdentifier() != null ? !getProtocolIdentifier()
        .equals(that.getProtocolIdentifier()) : that.getProtocolIdentifier() != null) {
      return false;
    }
    if (getEprescribeStatus() != null ? !getEprescribeStatus().equals(that.getEprescribeStatus())
        : that.getEprescribeStatus() != null) {
      return false;
    }
    if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null) {
      return false;
    }
    if (getDinSystem() != that.getDinSystem()) {
      return false;
    }
    if (getWrittenDate() != null ? !getWrittenDate().equals(that.getWrittenDate())
        : that.getWrittenDate() != null) {
      return false;
    }
    if (getExpiryDate() != null ? !getExpiryDate().equals(that.getExpiryDate())
        : that.getExpiryDate() != null) {
      return false;
    }
    if (getEffectiveDate() != null ? !getEffectiveDate().equals(that.getEffectiveDate())
        : that.getEffectiveDate() != null) {
      return false;
    }
    if (getPickUpDate() != null ? !getPickUpDate().equals(that.getPickUpDate())
        : that.getPickUpDate() != null) {
      return false;
    }
    if (getDispensedDate() != null ? !getDispensedDate().equals(that.getDispensedDate())
        : that.getDispensedDate() != null) {
      return false;
    }
    if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate())
        : that.getCreatedDate() != null) {
      return false;
    }
    if (getLastModified() != null ? !getLastModified().equals(that.getLastModified())
        : that.getLastModified() != null) {
      return false;
    }
    if (getPrescriptionDetails() != null ? !getPrescriptionDetails()
        .equals(that.getPrescriptionDetails()) : that.getPrescriptionDetails() != null) {
      return false;
    }
    if (getDosages() != null ? !getDosages().equals(that.getDosages())
        : that.getDosages() != null) {
      return false;
    }
    if (getIndications() != null ? !getIndications().equals(that.getIndications())
        : that.getIndications() != null) {
      return false;
    }
    if (getAnnotations() != null ? !getAnnotations().equals(that.getAnnotations())
        : that.getAnnotations() != null) {
      return false;
    }
    if (getStatusHistories() != null ? !getStatusHistories().equals(that.getStatusHistories())
        : that.getStatusHistories() != null) {
      return false;
    }
    if (getInteractions() != null ? !getInteractions().equals(that.getInteractions())
        : that.getInteractions() != null) {
      return false;
    }
    if (getWellnetPrescriptionLinks() != null ? !getWellnetPrescriptionLinks()
        .equals(that.getWellnetPrescriptionLinks()) : that.getWellnetPrescriptionLinks() != null) {
      return false;
    }
    if (getUuid() != null ? !getUuid()
        .equals(that.getUuid()) : that.getUuid() != null) {
      return false;
    }
    if (getAllowRenewalRequests() != null ? !getAllowRenewalRequests()
        .equals(that.getAllowRenewalRequests()) : that.getAllowRenewalRequests() != null) {
      return false;
    }
    if (getDoNotAutofill() != null ? !getDoNotAutofill()
        .equals(that.getDoNotAutofill()) : that.getDoNotAutofill() != null) {
      return false;
    }
    return getLimitedUseCodes() != null ? getLimitedUseCodes().equals(that.getLimitedUseCodes())
        : that.getLimitedUseCodes() == null;
  }

  @Override
  public int hashCode() {
    int result = getPrescriptionId();
    result = 31 * result + getMedicationId();
    result = 31 * result + getMaxDispenseQuantity();
    result = 31 * result + getDispenseInterval();
    result = 31 * result + (getPatientId() != null ? getPatientId().hashCode() : 0);
    result = 31 * result + (getRefills() != null ? getRefills().hashCode() : 0);
    result =
        31 * result + (getPrescribingPhysician() != null ? getPrescribingPhysician().hashCode()
            : 0);
    result =
        31 * result + (getAuthorizingPhysician() != null ? getAuthorizingPhysician().hashCode()
            : 0);
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    result = 31 * result + (getDispenseAmount() != null ? getDispenseAmount().hashCode() : 0);
    result = 31 * result + (getDaysRemaining() != null ? getDaysRemaining().hashCode() : 0);
    result = 31 * result + (getCreatorUserId() != null ? getCreatorUserId().hashCode() : 0);
    result =
        31 * result + (getPreviousPrescriptionId() != null ? getPreviousPrescriptionId().hashCode()
            : 0);
    result = 31 * result + (getRefillAmount() != null ? getRefillAmount().hashCode() : 0);
    result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
    result =
        31 * result + (getWellnetFormulationId() != null ? getWellnetFormulationId().hashCode()
            : 0);
    result = 31 * result + (getPharmacyId() != null ? getPharmacyId().hashCode() : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result = 31 * result + (isMaskDisplay() ? 1 : 0);
    result = 31 * result + (isExternalRx() ? 1 : 0);
    result = 31 * result + (isNondrug() ? 1 : 0);
    result = 31 * result + (isLegacy() ? 1 : 0);
    result = 31 * result + (isAutoExpire() ? 1 : 0);
    result = 31 * result + (isRenewed() ? 1 : 0);
    result = 31 * result + (isQuantityModified() ? 1 : 0);
    result = 31 * result + (isQuantityUnitModified() ? 1 : 0);
    result = 31 * result + (isNonAuthoritativeIndicator() ? 1 : 0);
    result =
        31 * result + (getAllowSubstitutions() != null ? getAllowSubstitutions().hashCode() : 0);
    result =
        31 * result + (getAllowTrialDispenses() != null ? getAllowTrialDispenses().hashCode() : 0);
    result = 31 * result + (getCompliancePackageRequired() != null ? getCompliancePackageRequired()
        .hashCode() : 0);
    result = 31 * result + (getPatientCompliance() != null ? getPatientCompliance().hashCode() : 0);
    result = 31 * result + (getLocalStartDate() != null ? getLocalStartDate().hashCode() : 0);
    result = 31 * result + (getDosage() != null ? getDosage().hashCode() : 0);
    result = 31 * result + (getDosageForm() != null ? getDosageForm().hashCode() : 0);
    result = 31 * result + (getMedType() != null ? getMedType().hashCode() : 0);
    result =
        31 * result + (getPharmacyInstructions() != null ? getPharmacyInstructions().hashCode()
            : 0);
    result = 31 * result + (getSigInstructions() != null ? getSigInstructions().hashCode() : 0);
    result = 31 * result + (getRoute() != null ? getRoute().hashCode() : 0);
    result = 31 * result + (getInterval() != null ? getInterval().hashCode() : 0);
    result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
    result = 31 * result + (getMedStatus() != null ? getMedStatus().hashCode() : 0);
    result = 31 * result + (getDispenseUnit() != null ? getDispenseUnit().hashCode() : 0);
    result = 31 * result + (getDrugUse() != null ? getDrugUse().hashCode() : 0);
    result = 31 * result + (getOpenIndication() != null ? getOpenIndication().hashCode() : 0);
    result = 31 * result + (getDin() != null ? getDin().hashCode() : 0);
    result = 31 * result + (getCompoundName() != null ? getCompoundName().hashCode() : 0);
    result = 31 * result + (getWellnetId() != null ? getWellnetId().hashCode() : 0);
    result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
    result = 31 * result + (getCompoundDetails() != null ? getCompoundDetails().hashCode() : 0);
    result = 31 * result + (getProviderId() != null ? getProviderId().hashCode() : 0);
    result = 31 * result + (getProviderName() != null ? getProviderName().hashCode() : 0);
    result = 31 * result + (getOrderCode() != null ? getOrderCode().hashCode() : 0);
    result = 31 * result + (getSource() != null ? getSource().hashCode() : 0);
    result = 31 * result + (getMaxDispenseUnit() != null ? getMaxDispenseUnit().hashCode() : 0);
    result =
        31 * result + (getDispenseIntervalUnit() != null ? getDispenseIntervalUnit().hashCode()
            : 0);
    result = 31 * result + (getDispensedNote() != null ? getDispensedNote().hashCode() : 0);
    result =
        31 * result + (getExternalMedicationState() != null ? getExternalMedicationState()
            .hashCode()
            : 0);
    result =
        31 * result + (getProtocolIdentifier() != null ? getProtocolIdentifier().hashCode() : 0);
    result = 31 * result + (getEprescribeStatus() != null ? getEprescribeStatus().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (getDinSystem() != null ? getDinSystem().hashCode() : 0);
    result = 31 * result + (getWrittenDate() != null ? getWrittenDate().hashCode() : 0);
    result = 31 * result + (getExpiryDate() != null ? getExpiryDate().hashCode() : 0);
    result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
    result = 31 * result + (getPickUpDate() != null ? getPickUpDate().hashCode() : 0);
    result = 31 * result + (getDispensedDate() != null ? getDispensedDate().hashCode() : 0);
    result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
    result = 31 * result + (getLastModified() != null ? getLastModified().hashCode() : 0);
    result =
        31 * result + (getPrescriptionDetails() != null ? getPrescriptionDetails().hashCode() : 0);
    result = 31 * result + (getDosages() != null ? getDosages().hashCode() : 0);
    result = 31 * result + (getIndications() != null ? getIndications().hashCode() : 0);
    result = 31 * result + (getAnnotations() != null ? getAnnotations().hashCode() : 0);
    result = 31 * result + (getStatusHistories() != null ? getStatusHistories().hashCode() : 0);
    result = 31 * result + (getInteractions() != null ? getInteractions().hashCode() : 0);
    result = 31 * result + (getWellnetPrescriptionLinks() != null ? getWellnetPrescriptionLinks()
        .hashCode() : 0);
    result = 31 * result + (getLimitedUseCodes() != null ? getLimitedUseCodes().hashCode() : 0);
    result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
    result = 31 * result + (getDoNotAutofill() != null ? getDoNotAutofill().hashCode() : 0);
    result = 31 * result
        + (getAllowRenewalRequests() != null ? getAllowRenewalRequests().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PrescriptionMedicationDto{"
        + "prescriptionId=" + prescriptionId
        + ", medicationId=" + medicationId
        + ", maxDispenseQuantity=" + maxDispenseQuantity
        + ", dispenseInterval=" + dispenseInterval
        + ", patientId=" + patientId
        + ", refills=" + refills
        + ", prescribingPhysician=" + prescribingPhysician
        + ", authorizingPhysician=" + authorizingPhysician
        + ", maskId=" + maskId
        + ", dispenseAmount=" + dispenseAmount
        + ", daysRemaining=" + daysRemaining
        + ", creatorUserId=" + creatorUserId
        + ", previousPrescriptionId=" + previousPrescriptionId
        + ", refillAmount=" + refillAmount
        + ", userId=" + userId
        + ", wellnetFormulationId=" + wellnetFormulationId
        + ", pharmacyId=" + pharmacyId
        + ", active=" + active
        + ", maskDisplay=" + maskDisplay
        + ", externalRx=" + externalRx
        + ", nondrug=" + nondrug
        + ", legacy=" + legacy
        + ", autoExpire=" + autoExpire
        + ", renewed=" + renewed
        + ", quantityModified=" + quantityModified
        + ", quantityUnitModified=" + quantityUnitModified
        + ", nonAuthoritativeIndicator=" + nonAuthoritativeIndicator
        + ", allowSubstitutions=" + allowSubstitutions
        + ", allowTrialDispenses=" + allowTrialDispenses
        + ", compliancePackageRequired=" + compliancePackageRequired
        + ", patientCompliance=" + patientCompliance
        + ", localStartDate='" + localStartDate + '\''
        + ", dosage='" + dosage + '\''
        + ", dosageForm='" + dosageForm + '\''
        + ", medType='" + medType + '\''
        + ", pharmacyInstructions='" + pharmacyInstructions + '\''
        + ", sigInstructions='" + sigInstructions + '\''
        + ", route='" + route + '\''
        + ", interval='" + interval + '\''
        + ", amount='" + amount + '\''
        + ", medStatus='" + medStatus + '\''
        + ", dispenseUnit='" + dispenseUnit + '\''
        + ", drugUse='" + drugUse + '\''
        + ", openIndication='" + openIndication + '\''
        + ", din='" + din + '\''
        + ", compoundName='" + compoundName + '\''
        + ", wellnetId='" + wellnetId + '\''
        + ", orderStatus='" + orderStatus + '\''
        + ", compoundDetails='" + compoundDetails + '\''
        + ", providerId='" + providerId + '\''
        + ", providerName='" + providerName + '\''
        + ", orderCode='" + orderCode + '\''
        + ", source='" + source + '\''
        + ", maxDispenseUnit='" + maxDispenseUnit + '\''
        + ", dispenseIntervalUnit='" + dispenseIntervalUnit + '\''
        + ", dispensedNote='" + dispensedNote + '\''
        + ", externalMedicationState='" + externalMedicationState + '\''
        + ", protocolIdentifier='" + protocolIdentifier + '\''
        + ", eprescribeStatus='" + eprescribeStatus + '\''
        + ", note='" + note + '\''
        + ", dinSystem=" + dinSystem
        + ", writtenDate=" + writtenDate
        + ", expiryDate=" + expiryDate
        + ", effectiveDate=" + effectiveDate
        + ", pickUpDate=" + pickUpDate
        + ", dispensedDate=" + dispensedDate
        + ", createdDate=" + createdDate
        + ", lastModified=" + lastModified
        + ", prescriptionDetails=" + prescriptionDetails
        + ", dosages=" + dosages
        + ", indications=" + indications
        + ", annotations=" + annotations
        + ", statusHistories=" + statusHistories
        + ", interactions=" + interactions
        + ", wellnetPrescriptionLinks=" + wellnetPrescriptionLinks
        + ", limitedUseCodes=" + limitedUseCodes
        + ", uuid=" + uuid
        + ", doNotAutofill=" + doNotAutofill
        + ", allowRenewalRequests=" + allowRenewalRequests
        + '}';
  }
}
