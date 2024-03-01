
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.masking.MaskAuthorizationDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ClinicalStatus;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.LifeStageType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.SeverityCode;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Represents allery summary which has been applied to a specific patient. Only consists fields from
 * Patient allergy dto which are required.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient allergy data transfer object model representing an allergy "
    + "which has been applied to a specific patient")
public class PatientAllergySummaryDto {

  @JsonProperty("id")
  @Schema(description = "The unique patient allergy id", example = "1")
  private int id;

  @JsonProperty("allergyDisplayText")
  @Schema(description = "The top-level summary for allergy",
      example = "Status: Suspect - Group: Salicylates - Name: ADVIL COLD & FLU 200-38 MG TAB - "
          + "Severity: Moderate - Reactions: Rash - local contact, Rash - other")
  private String allergyDisplayText;

  @JsonProperty("allergyName")
  @Schema(description = "The name of the allergy", example = "Tylenol")
  private String allergyName;

  @JsonProperty("allergyGroupName")
  @Schema(description = "The name of the allergen group", example = "Clebopride")
  private String allergyGroupName;

  @JsonProperty("clinicalStatus")
  @Schema(description = "The clinical status of the allergy", example = "DoubtRaised")
  private ClinicalStatus clinicalStatus;

  @JsonProperty("diagnosedAge")
  @Schema(description = "The age that the allergy was diagnosed", example = "59 Yr")
  private String diagnosedAge;

  @JsonProperty("lifeStageType")
  @Schema(description = "The life stage type of the patient allergy", example = "Child")
  private LifeStageType lifeStageType;

  @JsonProperty("severityCode")
  @Schema(description = "The severity code of the allergy", example = "Moderate")
  private SeverityCode severityCode;

  @JsonProperty("reactions")
  @Schema(description = "The allergic reactions after exposure to the allergen",
      example = "RashHives")
  private Set<AllergyReactionDto> reactions;

  @JsonProperty("allergyType")
  @Schema(description = "The type of the allergy", example = "DRUG_INTOLERANCE")
  private AllergyType allergyType;

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

  @JsonProperty("drugIdentifier")
  @Schema(description = "The drug identifier")
  private DrugIdentifier drugIdentifier;

  @JsonProperty("providerId")
  @Schema(description = "The provider id", example = "18230")
  private String providerId;

  /**
   * Patient Allergy Display Text
   *
   * @return allergyDisplayText
   * @documentationExample Status: Suspect - Group: Salicylates - Name: ADVIL COLD & FLU 200-38 MG
   *                       TAB - " + "Severity: Moderate - Reactions: Rash - local contact, Rash -
   *                       other
   */
  public String getAllergyDisplayText() {

    return allergyDisplayText;
  }

  public void setAllergyDisplayText(String allergyDisplayText) {
    this.allergyDisplayText = allergyDisplayText;
  }

  /**
   * The unique patient allergy id
   *
   * @return id
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The name of allergy
   *
   * @return allergyName
   * @documentationExample tylenol
   */
  public String getAllergyName() {
    return allergyName;
  }

  public void setAllergyName(String allergyName) {
    this.allergyName = allergyName;
  }

  /**
   * The name of the allergen group
   *
   * @return allergyGroupName
   * @documentationExample Clebopride
   */
  public String getAllergyGroupName() {
    return allergyGroupName;
  }

  public void setAllergyGroupName(String allergyGroupName) {
    this.allergyGroupName = allergyGroupName;
  }

  /**
   * The Clinical Status of the allergy
   *
   * @return clinicalStatus
   * @documentationExample doubtraised
   */
  public ClinicalStatus getClinicalStatus() {
    return clinicalStatus;
  }

  public void setClinicalStatus(
      ClinicalStatus clinicalStatus) {
    this.clinicalStatus = clinicalStatus;
  }

  /**
   * The age of the patient when allergy diagnosed
   *
   * @return diagnosedAge
   * @documentationExample 59 yr
   */
  public String getDiagnosedAge() {
    return diagnosedAge;
  }

  public void setDiagnosedAge(String diagnosedAge) {
    this.diagnosedAge = diagnosedAge;
  }

  /**
   * The life stage of the patient allergy
   *
   * @return lifeStageType
   * @documentationExample child
   */
  public LifeStageType getLifeStageType() {
    return lifeStageType;
  }

  public void setLifeStageType(
      LifeStageType lifeStageType) {
    this.lifeStageType = lifeStageType;
  }

  /**
   * The severity code of the allergy
   *
   * @return {@link SeverityCode}
   * @documentationExample moderate
   */
  public SeverityCode getSeverityCode() {
    return severityCode;
  }

  public void setSeverityCode(SeverityCode severityCode) {
    this.severityCode = severityCode;
  }

  /**
   * A set of all the reactions.
   *
   * @return set of {@link AllergyReactionDto}
   * @documentationExample Set {@link AllergyReactionDto}
   */
  public Set<AllergyReactionDto> getReactions() {
    return reactions;
  }

  public void setReactions(Set<AllergyReactionDto> reactions) {
    this.reactions = reactions;
  }

  /**
   * The Type of the allergy
   *
   * @return {@link AllergyType}
   * @documentationExample tylenol
   */
  public AllergyType getAllergyType() {
    return allergyType;
  }

  public void setAllergyType(AllergyType allergyType) {
    this.allergyType = allergyType;
  }

  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDate getReportedDate() {
    return reportedDate;
  }

  public void setReportedDate(LocalDate reportedDate) {
    this.reportedDate = reportedDate;
  }

  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * @return authorizingProvider
   * @documentationExample 25862
   */
  public String getAuthorizingProvider() {
    return authorizingProvider;
  }

  public void setAuthorizingProvider(String authorizingProvider) {
    this.authorizingProvider = authorizingProvider;
  }

  /**
   * @return {@link DrugIdentifier}
   * @documentationExample {@link DrugIdentifier}
   */
  public DrugIdentifier getDrugIdentifier() {
    return drugIdentifier;
  }

  public void setDrugIdentifier(
      DrugIdentifier drugIdentifier) {
    this.drugIdentifier = drugIdentifier;
  }

  /**
   * A provider id
   *
   * @return providerId
   * @documentationExample 18230
   */
  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientAllergySummaryDto that = (PatientAllergySummaryDto) o;

    if (id != that.id) {
      return false;
    }
    if (allergyDisplayText != null ? !allergyDisplayText.equals(that.allergyDisplayText)
        : that.allergyDisplayText != null) {
      return false;
    }
    if (allergyName != null ? !allergyName.equals(that.allergyName) : that.allergyName != null) {
      return false;
    }
    if (allergyGroupName != null ? !allergyGroupName.equals(that.allergyGroupName)
        : that.allergyGroupName != null) {
      return false;
    }
    if (clinicalStatus != that.clinicalStatus) {
      return false;
    }
    if (diagnosedAge != null ? !diagnosedAge.equals(that.diagnosedAge)
        : that.diagnosedAge != null) {
      return false;
    }
    if (lifeStageType != that.lifeStageType) {
      return false;
    }
    if (severityCode != that.severityCode) {
      return false;
    }
    if (reactions != null ? !reactions.equals(that.reactions) : that.reactions != null) {
      return false;
    }
    if (allergyType != that.allergyType) {
      return false;
    }
    if (reportedDate != null ? !reportedDate.equals(that.reportedDate)
        : that.reportedDate != null) {
      return false;
    }
    if (modifiedDate != null ? !modifiedDate.equals(that.modifiedDate)
        : that.modifiedDate != null) {
      return false;
    }
    if (authorizingProvider != null ? !authorizingProvider.equals(that.authorizingProvider)
        : that.authorizingProvider != null) {
      return false;
    }
    if (drugIdentifier != null ? !drugIdentifier.equals(that.drugIdentifier)
        : that.drugIdentifier != null) {
      return false;
    }
    return providerId != null ? providerId.equals(that.providerId) : that.providerId == null;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PatientAllergySummaryDto{");
    sb.append("id=").append(id);
    sb.append(", allergyDisplayText='").append(allergyDisplayText).append('\'');
    sb.append(", allergyName='").append(allergyName).append('\'');
    sb.append(", allergyGroupName='").append(allergyGroupName).append('\'');
    sb.append(", clinicalStatus=").append(clinicalStatus);
    sb.append(", diagnosedAge='").append(diagnosedAge).append('\'');
    sb.append(", lifeStageType=").append(lifeStageType);
    sb.append(", severityCode=").append(severityCode);
    sb.append(", reactions=").append(reactions);
    sb.append(", allergyType=").append(allergyType);
    sb.append(", reportedDate=").append(reportedDate);
    sb.append(", modifiedDate=").append(modifiedDate);
    sb.append(", authorizingProvider='").append(authorizingProvider).append('\'');
    sb.append(", drugIdentifier=").append(drugIdentifier);
    sb.append(", providerId='").append(providerId).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (allergyDisplayText != null ? allergyDisplayText.hashCode() : 0);
    result = 31 * result + (allergyName != null ? allergyName.hashCode() : 0);
    result = 31 * result + (allergyGroupName != null ? allergyGroupName.hashCode() : 0);
    result = 31 * result + (clinicalStatus != null ? clinicalStatus.hashCode() : 0);
    result = 31 * result + (diagnosedAge != null ? diagnosedAge.hashCode() : 0);
    result = 31 * result + (lifeStageType != null ? lifeStageType.hashCode() : 0);
    result = 31 * result + (severityCode != null ? severityCode.hashCode() : 0);
    result = 31 * result + (reactions != null ? reactions.hashCode() : 0);
    result = 31 * result + (allergyType != null ? allergyType.hashCode() : 0);
    result = 31 * result + (reportedDate != null ? reportedDate.hashCode() : 0);
    result = 31 * result + (modifiedDate != null ? modifiedDate.hashCode() : 0);
    result = 31 * result + (authorizingProvider != null ? authorizingProvider.hashCode() : 0);
    result = 31 * result + (drugIdentifier != null ? drugIdentifier.hashCode() : 0);
    result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
    return result;
  }
}
