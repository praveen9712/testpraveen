
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Represents an allergy which has been applied to a specific patient.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient allergy data transfer object model representing an allergy "
    + "which has been applied to a specific patient")
public class PatientAllergyDto {

  @JsonProperty("id")
  @Schema(description = "The unique patient allergy id", example = "1")
  private int id;

  @JsonProperty("patientId")
  @Schema(description = "The id of the patient", example = "1200")
  private int patientId;

  @JsonProperty("allergyId")
  @Schema(description = "The id of the allergy", example = "1")
  private int allergyId;

  @JsonProperty("allergyName")
  @Schema(description = "The name of the allergy", example = "Tylenol")
  private String allergyName;

  @JsonProperty("allergyGroupId")
  @Schema(description = "The id of the allergen group", example = "2")
  private String allergyGroupId;

  @JsonProperty("allergyGroupName")
  @Schema(description = "The name of the allergen group", example = "Clebopride")
  private String allergyGroupName;

  @JsonProperty("allergyType")
  @Schema(description = "The type of the allergy", example = "DRUG_ALLERGY")
  private AllergyType allergyType;

  @JsonProperty("clinicalStatus")
  @Schema(description = "The clinical status of the allergy", example = "DoubtRaised")
  private ClinicalStatus clinicalStatus;

  @JsonProperty("reactionDate")
  @Schema(description = "The patient reaction date", example = "2017-11-29")
  private String reactionDate;

  @JsonProperty("diagnosedAge")
  @Schema(description = "The age that the allergy was diagnosed", example = "59 Yr")
  private String diagnosedAge;

  @JsonProperty("lifeStageType")
  @Schema(description = "The life stage type of the patient allergy", example = "Child")
  private LifeStageType lifeStageType;

  @JsonProperty("severityCode")
  @Schema(description = "The severity code of the allergy", example = "Moderate")
  private SeverityCode severityCode;

  @JsonProperty("reactionCode")
  @Schema(description = "The allergic reaction after exposure to the allergen",
      example = "RashHives")
  private ReactionCode reactionCode;

  @JsonProperty("maskId")
  @Schema(description = "The mask id", example = "3")
  private Integer maskId;

  @JsonProperty("masked")
  @Schema(description = "The indication if the patient allergy is masked",
      example = "false")
  private Boolean masked;

  @JsonProperty("reportedDate")
  @Schema(description = "The date when the allergy was reported", example = "2017-11-29",
      type = "string")
  private LocalDate reportedDate;

  @JsonProperty("modifiedDate")
  @Schema(description = "The date when this entry was last modified",
      example = "2017-11-29T00:00:00.000", type = "string")
  private LocalDateTime modifiedDate;

  @JsonProperty("authorizingProvider")
  @Schema(description = "The id of the authorizing provider",
      example = "25862")
  private String authorizingProvider;

  @JsonProperty("providerInformation")
  @Schema(description = "The information about the authorizing provider",
      example = "Doctor, David [12340]")
  private String providerInformation;

  @JsonProperty("drugCode")
  @Schema(description = "The drug code", example = "11577")
  private String drugCode;

  @JsonProperty("genericDrugId")
  @Schema(description = "The generic drug id", example = "18")
  private Integer genericDrugId;

  @JsonProperty("medicationType")
  @Schema(description = "The medication type", example = "DIN")
  private MedicationType medicationType;

  @JsonProperty("patientReaction")
  @Schema(description = "The allergic reaction caused by the allergen", example = "Hives")
  private String patientReaction;

  @JsonProperty("reactionDescription")
  @Schema(description = "The description of the effects caused by the allergy",
      example = "An itchy sensation")
  private String reactionDescription;

  @JsonProperty("providerId")
  @Schema(description = "The provider id", example = "18230")
  private String providerId;

  @JsonProperty("legacy")
  @Schema(
      description = "The indication if this allergy was imported from another EMR",
      example = "true")
  private boolean legacy;

  @JsonProperty("ehrId")
  @Schema(description = "The id of EHR", example = "3")
  private String ehrId;

  @JsonProperty("pipId")
  @Schema(description = "The id of the Pharmaceutical Information Program(PIP) in Saskatchewan",
      example = "2")
  private String pipId;

  /**
   * The id of this patient allergy.
   *
   * @return The id of this patient allergy.
   *
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The id of the patient for which this allergy belongs.
   *
   * @return The id of the patient for which this allergy belongs.
   *
   * @documentationExample 1
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The id of the allergy that this {@link PatientAllergyDto} refers to.
   *
   * @return The id of the allergy that this {@link PatientAllergyDto} refers to.
   *
   * @documentationExample 1
   */
  public int getAllergyId() {
    return allergyId;
  }

  public void setAllergyId(int allergyId) {
    this.allergyId = allergyId;
  }

  /**
   * The name of the allergy.
   *
   * @return The name of the allergy.
   *
   * @documentationExample Tylenol
   */
  public String getAllergyName() {
    return allergyName;
  }

  public void setAllergyName(String allergyName) {
    this.allergyName = allergyName;
  }

  /**
   * The groupId of the allergy. The group refers to types of allergens, for example Rhenium
   * Containing Compounds.
   *
   * @return The groupId of the allergy.
   *
   * @documentationExample 15
   */
  public String getAllergyGroupId() {
    return allergyGroupId;
  }

  public void setAllergyGroupId(String allergyGroupId) {
    this.allergyGroupId = allergyGroupId;
  }

  /**
   * The name of the allergen group.
   * <P>
   * The allergy group name is same as allergy name for some constraints.
   * </P>
   *
   * @return The name of the allergen group.
   *
   * @documentationExample Pegaptanib
   */
  public String getAllergyGroupName() {
    return allergyGroupName;
  }

  public void setAllergyGroupName(String allergyGroupName) {
    this.allergyGroupName = allergyGroupName;
  }

  /**
   * The clinical status of this patientAllergy. See {@link ClinicalStatus} for the different
   * options.
   *
   * @return The clinical status of this patientAllergy.
   *
   * @documentationExample ConfirmedOrVerified
   */
  public ClinicalStatus getClinicalStatus() {
    return clinicalStatus;
  }

  public void setClinicalStatus(ClinicalStatus clinicalStatus) {
    this.clinicalStatus = clinicalStatus;
  }

  /**
   * The patientReaction date of this patientAllergy.
   *
   * @return The patientReaction date of this patientAllergy.
   *
   * @documentationExample 2017-11-29
   */
  public String getReactionDate() {
    return reactionDate;
  }

  public void setReactionDate(String reactionDate) {
    this.reactionDate = reactionDate;
  }

  /**
   * The age this allergy was diagnosed.
   *
   * @return The age this allergy was diagnosed.
   *
   * @documentationExample 59 Yr
   */
  public String getDiagnosedAge() {
    return diagnosedAge;
  }

  public void setDiagnosedAge(String diagnosedAge) {
    this.diagnosedAge = diagnosedAge;
  }

  /**
   * The life stage type of this patientAllergy. See {@link LifeStageType} for the different
   * options.
   *
   * @return The life stage type of this patientAllergy.
   *
   * @documentationExample Child
   */
  public LifeStageType getLifeStageType() {
    return lifeStageType;
  }

  public void setLifeStageType(LifeStageType lifeStageType) {
    this.lifeStageType = lifeStageType;
  }

  /**
   * The clinical status of this patientAllergy. See {@link LifeStageType} for the different
   * options.
   *
   * @return The clinical status of this patientAllergy.
   *
   * @documentationExample Moderate
   */
  public SeverityCode getSeverityCode() {
    return severityCode;
  }

  public void setSeverityCode(SeverityCode severityCode) {
    this.severityCode = severityCode;
  }

  /**
   * The id used for security masking.
   *
   * @return The id used for security masking.
   *
   * @documentationExample -1
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * Indicates whether or not this patientAllergy uses masking.
   *
   * @return true if masked or false if not.
   * 
   * @documentationExample true
   */
  public Boolean getMasked() {
    return masked;
  }

  public void setMasked(Boolean masked) {
    this.masked = masked;
  }

  /**
   * The date this allergy was reported.
   *
   * @return The date this allergy was reported.
   * 
   * @documentationExample 2017-11-29
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getReportedDate() {
    return reportedDate;
  }

  public void setReportedDate(LocalDate reportedDate) {
    this.reportedDate = reportedDate;
  }

  /**
   * The date this entry was last modified.
   *
   * @return The date this entry was last modified.
   * 
   * @documentationExample 2017-11-29T00:00:00.000
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * A unique for the EHR this is associated to. Empty if there is none.
   *
   * @return A unique for the EHR this is associated to.
   * 
   * @documentationExample
   */
  public String getEhrId() {
    return ehrId;
  }

  public void setEhrId(String ehrId) {
    this.ehrId = ehrId;
  }

  /**
   * The id associated with the Pharmaceutical Information Program in Saskatchewan. Empty if none.
   *
   * @return The id associated with the Pharmaceutical Information Program in Saskatchewan.
   * 
   * @documentationExample 153
   */
  public String getPipId() {
    return pipId;
  }

  public void setPipId(String pipId) {
    this.pipId = pipId;
  }

  /**
   * The id of the provider which authorized this PatientAllergy.
   *
   * @return The id of the provider which authorized this PatientAllergy.
   * 
   * @documentationExample 25862
   */
  public String getAuthorizingProvider() {
    return authorizingProvider;
  }

  public void setAuthorizingProvider(String authorizingProvider) {
    this.authorizingProvider = authorizingProvider;
  }

  /**
   * Denotes whether or not this patientAllergy has been imported from another EMR.
   *
   * @return true if legacy or false if not.
   * 
   * @documentationExample True
   */
  public boolean isLegacy() {
    return legacy;
  }

  public void setLegacy(boolean legacy) {
    this.legacy = legacy;
  }

  /**
   * The drug code. Used in combination with {@link #getMedicationType()} to get drug information.
   *
   * @return The drug code.
   * 
   * @documentationExample 11577
   */
  public String getDrugCode() {
    return drugCode;
  }

  public void setDrugCode(String drugCode) {
    this.drugCode = drugCode;
  }

  /**
   * The generic drug id. Used in combination with {@link #getMedicationType()} to get drug
   * information.
   *
   * @return The generic drug id.
   * 
   * @documentationExample 18
   */
  public Integer getGenericDrugId() {
    return genericDrugId;
  }

  public void setGenericDrugId(Integer genericDrugId) {
    this.genericDrugId = genericDrugId;
  }

  /**
   * The patientReaction caused by this allergy.
   *
   * @return The patientReaction caused by this allergy.
   * 
   * @documentationExample Hives
   */
  public String getPatientReaction() {
    return patientReaction;
  }

  public void setPatientReaction(String patientReaction) {
    this.patientReaction = patientReaction;
  }

  /**
   * A description of the effects caused by this allergy.
   *
   * @return A description of the effects caused by this allergy.
   * 
   * @documentationExample An itchy sensation.
   */
  public String getReactionDescription() {
    return reactionDescription;
  }

  public void setReactionDescription(String reactionDescription) {
    this.reactionDescription = reactionDescription;
  }

  /**
   * The patientReaction when this allergy is encountered.
   *
   * @return The patientReaction when this allergy is encountered.
   * 
   * @documentationExample RashHives
   */
  public ReactionCode getReactionCode() {
    return reactionCode;
  }

  public void setReactionCode(ReactionCode reactionCode) {
    this.reactionCode = reactionCode;
  }

  /**
   * The provider which created or entered this PatientAllergy.
   *
   * @return The provider which created or entered this PatientAllergy.
   * 
   * @documentationExample 143
   */
  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  /**
   * The MedicationType to be used with {@link #getDrugCode()} and {@link #getGenericDrugId()} to
   * get the drug information.
   *
   * @return The MedicationType
   * 
   * @documentationExample DIN
   */
  public MedicationType getMedicationType() {
    return medicationType;
  }

  public void setMedicationType(MedicationType medicationType) {
    this.medicationType = medicationType;
  }

  /**
   * The type of drug allergy.
   *
   * @return The type of drug allergy.
   * 
   * @documentationExample DRUG_INTOLERANCE
   */
  public AllergyType getAllergyType() {
    return allergyType;
  }

  public void setAllergyType(AllergyType allergyType) {
    this.allergyType = allergyType;
  }

  /**
   * Denotes who prescribed the allergy for the patient.
   *
   * @return Denotes who prescribed the allergy for the patient.
   * 
   * @documentationExample Doctor, David [12340]
   */
  public String getProviderInformation() {
    return providerInformation;
  }

  public void setProviderInformation(String providerInformation) {
    this.providerInformation = providerInformation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientAllergyDto that = (PatientAllergyDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getAllergyId() != that.getAllergyId()) {
      return false;
    }
    if (isLegacy() != that.isLegacy()) {
      return false;
    }
    if (getAllergyName() != null ? !getAllergyName().equals(that.getAllergyName())
        : that.getAllergyName() != null) {
      return false;
    }
    if (getAllergyGroupId() != null ? !getAllergyGroupId().equals(that.getAllergyGroupId())
        : that.getAllergyGroupId() != null) {
      return false;
    }
    if (getAllergyGroupName() != null ? !getAllergyGroupName().equals(that.getAllergyGroupName())
        : that.getAllergyGroupName() != null) {
      return false;
    }
    if (getAllergyType() != that.getAllergyType()) {
      return false;
    }
    if (getClinicalStatus() != that.getClinicalStatus()) {
      return false;
    }
    if (getReactionDate() != null ? !getReactionDate().equals(that.getReactionDate())
        : that.getReactionDate() != null) {
      return false;
    }
    if (getDiagnosedAge() != null ? !getDiagnosedAge().equals(that.getDiagnosedAge())
        : that.getDiagnosedAge() != null) {
      return false;
    }
    if (getLifeStageType() != that.getLifeStageType()) {
      return false;
    }
    if (getSeverityCode() != that.getSeverityCode()) {
      return false;
    }
    if (getReactionCode() != that.getReactionCode()) {
      return false;
    }
    if (getMaskId() != null ? !getMaskId().equals(that.getMaskId()) : that.getMaskId() != null) {
      return false;
    }
    if (getMasked() != null ? !getMasked().equals(that.getMasked()) : that.getMasked() != null) {
      return false;
    }
    if (getReportedDate() != null ? !getReportedDate().equals(that.getReportedDate())
        : that.getReportedDate() != null) {
      return false;
    }
    if (getModifiedDate() != null ? !getModifiedDate().equals(that.getModifiedDate())
        : that.getModifiedDate() != null) {
      return false;
    }
    if (getAuthorizingProvider() != null ? !getAuthorizingProvider()
        .equals(that.getAuthorizingProvider()) : that.getAuthorizingProvider() != null) {
      return false;
    }
    if (getProviderInformation() != null ? !getProviderInformation()
        .equals(that.getProviderInformation()) : that.getProviderInformation() != null) {
      return false;
    }
    if (getDrugCode() != null ? !getDrugCode().equals(that.getDrugCode())
        : that.getDrugCode() != null) {
      return false;
    }
    if (getGenericDrugId() != null ? !getGenericDrugId().equals(that.getGenericDrugId())
        : that.getGenericDrugId() != null) {
      return false;
    }
    if (getMedicationType() != that.getMedicationType()) {
      return false;
    }
    if (getPatientReaction() != null ? !getPatientReaction().equals(that.getPatientReaction())
        : that.getPatientReaction() != null) {
      return false;
    }
    if (getReactionDescription() != null ? !getReactionDescription()
        .equals(that.getReactionDescription()) : that.getReactionDescription() != null) {
      return false;
    }
    if (getProviderId() != null ? !getProviderId().equals(that.getProviderId())
        : that.getProviderId() != null) {
      return false;
    }
    if (getEhrId() != null ? !getEhrId().equals(that.getEhrId()) : that.getEhrId() != null) {
      return false;
    }
    return getPipId() != null ? getPipId().equals(that.getPipId()) : that.getPipId() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPatientId();
    result = 31 * result + getAllergyId();
    result = 31 * result + (getAllergyName() != null ? getAllergyName().hashCode() : 0);
    result = 31 * result + (getAllergyGroupId() != null ? getAllergyGroupId().hashCode() : 0);
    result = 31 * result + (getAllergyGroupName() != null ? getAllergyGroupName().hashCode() : 0);
    result = 31 * result + (getAllergyType() != null ? getAllergyType().hashCode() : 0);
    result = 31 * result + (getClinicalStatus() != null ? getClinicalStatus().hashCode() : 0);
    result = 31 * result + (getReactionDate() != null ? getReactionDate().hashCode() : 0);
    result = 31 * result + (getDiagnosedAge() != null ? getDiagnosedAge().hashCode() : 0);
    result = 31 * result + (getLifeStageType() != null ? getLifeStageType().hashCode() : 0);
    result = 31 * result + (getSeverityCode() != null ? getSeverityCode().hashCode() : 0);
    result = 31 * result + (getReactionCode() != null ? getReactionCode().hashCode() : 0);
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    result = 31 * result + (getMasked() != null ? getMasked().hashCode() : 0);
    result = 31 * result + (getReportedDate() != null ? getReportedDate().hashCode() : 0);
    result = 31 * result + (getModifiedDate() != null ? getModifiedDate().hashCode() : 0);
    result =
        31 * result + (getAuthorizingProvider() != null ? getAuthorizingProvider().hashCode() : 0);
    result =
        31 * result + (getProviderInformation() != null ? getProviderInformation().hashCode() : 0);
    result = 31 * result + (getDrugCode() != null ? getDrugCode().hashCode() : 0);
    result = 31 * result + (getGenericDrugId() != null ? getGenericDrugId().hashCode() : 0);
    result = 31 * result + (getMedicationType() != null ? getMedicationType().hashCode() : 0);
    result = 31 * result + (getPatientReaction() != null ? getPatientReaction().hashCode() : 0);
    result =
        31 * result + (getReactionDescription() != null ? getReactionDescription().hashCode() : 0);
    result = 31 * result + (getProviderId() != null ? getProviderId().hashCode() : 0);
    result = 31 * result + (isLegacy() ? 1 : 0);
    result = 31 * result + (getEhrId() != null ? getEhrId().hashCode() : 0);
    result = 31 * result + (getPipId() != null ? getPipId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PatientAllergyDto{"
        + "id=" + id
        + ", patientId=" + patientId
        + ", allergyId=" + allergyId
        + ", allergyName='" + allergyName + '\''
        + ", allergyGroupId='" + allergyGroupId + '\''
        + ", allergyGroupName='" + allergyGroupName + '\''
        + ", allergyType=" + allergyType
        + ", clinicalStatus=" + clinicalStatus
        + ", reactionDate='" + reactionDate + '\''
        + ", diagnosedAge='" + diagnosedAge + '\''
        + ", lifeStageType=" + lifeStageType
        + ", severityCode=" + severityCode
        + ", reactionCode=" + reactionCode
        + ", maskId=" + maskId
        + ", masked=" + masked
        + ", reportedDate=" + reportedDate
        + ", modifiedDate=" + modifiedDate
        + ", authorizingProvider='" + authorizingProvider + '\''
        + ", providerInformation='" + providerInformation + '\''
        + ", drugCode='" + drugCode + '\''
        + ", genericDrugId=" + genericDrugId
        + ", medicationType=" + medicationType
        + ", patientReaction='" + patientReaction + '\''
        + ", reactionDescription='" + reactionDescription + '\''
        + ", providerId='" + providerId + '\''
        + ", legacy=" + legacy
        + ", ehrId='" + ehrId + '\''
        + ", pipId='" + pipId + '\''
        + '}';
  }
}
