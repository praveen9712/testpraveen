
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

/**
 * The patient diagnosis history data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosisHistory
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient diagnosis history data transfer object model")
public class PatientDiagnosisHistoryDto {

  @JsonProperty("id")
  @Schema(description = "The unique patient diagnosis history id", example = "1")
  private int id;

  @JsonProperty("patientId")
  @Schema(description = "ID of the patient", example = "1")
  private int patientId;

  @JsonProperty("patientDiagnosisId")
  @Schema(description = "ID of the patient diagnosis", example = "1")
  private int patientDiagnosisId; // only for history

  @JsonProperty("diagnosisStatus")
  @Schema(description = "Status of the diagnosis")
  private DiagnosisStatusDto diagnosisStatus; // status

  @JsonProperty("creatorUserId")
  @Schema(description = "ID of the user who created the diagnosis", example = "1")
  private Integer creatorUserId;

  @JsonProperty("maskId")
  @Schema(description = "Mask id", example = "2")
  private Integer maskId;

  @JsonProperty("codeTable")
  @Schema(description = "Code table name of the diagnosis", example = "ICD9")
  private String codeTable;

  @JsonProperty("code")
  @Schema(description = "Diagnosis code", example = "7641")
  private String code; // diagnosis

  @JsonProperty("description")
  @Schema(description = "Description of the diagnosis",
      example = "'LIGHT-FOR-DATES' WITH SIGNS OF FETAL MALNUTRITION")
  private String description;

  @JsonProperty("notes")
  @Schema(description = "Notes of the patient diagnosis", example = "Extra attention is needed")
  private String notes;

  @JsonProperty("problemDescription")
  @Schema(description = "Diagnosis problem description", example = "ATTENTION")
  private String problemDescription;

  @JsonProperty("symptomCodeTable")
  @Schema(description = "Symptom code table name", example = "ICD9")
  private String symptomCodeTable;

  @JsonProperty("symptomCode")
  @Schema(description = "Symptom code", example = "E91")
  private String symptomCode; // symptom

  @JsonProperty("symptomDescription")
  @Schema(description = "Description of the diagnosis symptom",
      example = "'NO' INDICATOR PRESENT")
  private String symptomDescription;

  @JsonProperty("outcomeCode")
  @Schema(description = "Outcome code", example = "E91")
  private String outcomeCode;

  @JsonProperty("alternativeDescription")
  @Schema(description = "Alternative description", example = "Essential hypertension")
  private String alternativeDescription;

  @JsonProperty("negative")
  @Schema(description = "Flag indicating if the diagnosis is negative", example = "true")
  private boolean negative;

  @JsonProperty("lifeStage")
  @Schema(description = "Life stage of the patient diagnosis",
      example = "Newborn, Newborn: Birth - 28 days")
  private LifeStageType lifeStage;

  @JsonProperty("createdDate")
  @Schema(description = "Date when diagnosis is created", example = "2018-07-13T00:00:00.000",
      type = "string")
  private LocalDateTime createdDate;

  @JsonProperty("modifiedDate")
  @Schema(description = "Date when diagnosis is last modified", example = "2018-07-13T00:00:00.000",
      type = "string")
  private LocalDateTime modifiedDate; // only for history

  @JsonProperty("dateOfDiagnosis")
  @Schema(description = "Date of the diagnosis", example = "2018-07-13", type = "string")
  private AccuroCalendar dateOfDiagnosis;

  @JsonProperty("onsetDate")
  @Schema(description = "Date of diagnosis when the symptom first appeared",
      example = "2018-07-13", type = "string")
  private AccuroCalendar onsetDate;

  @JsonProperty("recoveryDate")
  @Schema(description = "The diagnosis recovery date", example = "2018-07-13", type = "string")
  private AccuroCalendar recoveryDate;

  /**
   * The unique patient diagnosis history id.
   *
   * @documentationExample 1
   *
   * @return The patient diagnosis history id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The unique id of the patient diagnosed.
   *
   * @documentationExample 1
   *
   * @return The patient id.
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The original id of patient diagnosis. So the user can track the history entries by this id.
   *
   * @documentationExample 1
   *
   * @return The patient diagnosis id.
   */
  public int getPatientDiagnosisId() {
    return patientDiagnosisId;
  }

  public void setPatientDiagnosisId(int patientDiagnosisId) {
    this.patientDiagnosisId = patientDiagnosisId;
  }

  /**
   * The diagnosis status of the patient diagnosis.
   *
   * @return The diagnosis status.
   */
  public DiagnosisStatusDto getDiagnosisStatus() {
    return diagnosisStatus;
  }

  public void setDiagnosisStatus(DiagnosisStatusDto diagnosisStatus) {
    this.diagnosisStatus = diagnosisStatus;
  }

  /**
   * The user id of the user who created the original patient diagnosis.
   *
   * @documentationExample 1
   *
   * @return The user id.
   */
  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * The mask id. This id can be null if the record is not masked.
   *
   * @documentationExample 1
   *
   * @return The mask id.
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * The code type(table) of the diagnosis.
   *
   * @documentationExample ICD9
   *
   * @return The code type(table).
   */
  public String getCodeTable() {
    return codeTable;
  }

  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * The code of the diagnosis.
   *
   * @documentationExample 1
   *
   * @return The code.
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * The description of the diagnosis. This diagnosis is pre-defined under the code system.
   *
   * @documentationExample ABRASION
   *
   * @return The description.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The free text notes on the patient diagnosis from the provider.
   *
   * @documentationExample THIS PATIENT NEEDS TO COME BACK IN A MONTH
   *
   * @return The notes.
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The free text description on the patient diagnosis from the provider. This entity represents
   * "Description" section on Accuro's panel.
   *
   * @documentationExample THERE WERE CUTS AND ABRASIONS TO THE LIPS AND JAW
   *
   * @return The description.
   */
  public String getProblemDescription() {
    return problemDescription;
  }

  public void setProblemDescription(String problemDescription) {
    this.problemDescription = problemDescription;
  }

  /**
   * The code type(table) of the diagnostic symptom.
   *
   * @documentationExample ICD9
   *
   * @return The symptom code type(table).
   */
  public String getSymptomCodeTable() {
    return symptomCodeTable;
  }

  public void setSymptomCodeTable(String symptomCodeTable) {
    this.symptomCodeTable = symptomCodeTable;
  }

  /**
   * The code of the diagnostic symptom.
   *
   * @documentationExample E01
   *
   * @return The symptom code.
   */
  public String getSymptomCode() {
    return symptomCode;
  }

  public void setSymptomCode(String symptomCode) {
    this.symptomCode = symptomCode;
  }

  /**
   * The description of the diagnostic symptom.
   *
   * @documentationExample TULARAEMIA
   *
   * @return The symptom description.
   */
  public String getSymptomDescription() {
    return symptomDescription;
  }

  public void setSymptomDescription(String symptomDescription) {
    this.symptomDescription = symptomDescription;
  }

  /**
   * The outcome code of the diagnosis.
   *
   * @documentationExample CODE1
   *
   * @return The outcome code.
   */
  public String getOutcomeCode() {
    return outcomeCode;
  }

  public void setOutcomeCode(String outcomeCode) {
    this.outcomeCode = outcomeCode;
  }

  /**
   * The alternative description.
   *
   * @documentationExample THIS PATIENT HAS TO COME BACK AGAIN
   *
   * @return The alternative description.
   */
  public String getAlternativeDescription() {
    return alternativeDescription;
  }

  public void setAlternativeDescription(String alternativeDescription) {
    this.alternativeDescription = alternativeDescription;
  }

  /**
   * The flag if the diagnosis is negative.
   *
   * @documentationExample true
   *
   * @return {@code true} if it is negative, {@code false} otherwise.
   */
  public boolean isNegative() {
    return negative;
  }

  public void setNegative(boolean negative) {
    this.negative = negative;
  }

  /**
   * The life stage type of this patient diagnosis.
   *
   * @documentationExample Child
   *
   * @return The life stage type.
   *
   * @see LifeStageType
   */
  public LifeStageType getLifeStage() {
    return lifeStage;
  }

  public void setLifeStage(LifeStageType lifeStage) {
    this.lifeStage = lifeStage;
  }

  /**
   * The created date of the patient diagnosis.
   *
   * @return The created date.
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
   * The modified date of the patient diagnosis.
   *
   * @return The created date.
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
   * The date of the diagnosis.
   *
   * @return The date of the diagnosis.
   */
  @DocumentationExample("2017/11/29")
  @TypeHint(String.class)
  public AccuroCalendar getDateOfDiagnosis() {
    return dateOfDiagnosis;
  }

  public void setDateOfDiagnosis(AccuroCalendar dateOfDiagnosis) {
    this.dateOfDiagnosis = dateOfDiagnosis;
  }

  /**
   * The onset date of the diagnosis.
   *
   * @return The onset date of the diagnosis.
   */
  @DocumentationExample("2017/11/29")
  @TypeHint(String.class)
  public AccuroCalendar getOnsetDate() {
    return onsetDate;
  }

  public void setOnsetDate(AccuroCalendar onsetDate) {
    this.onsetDate = onsetDate;
  }

  /**
   * The recovery date of the diagnosis. This date is placed in "Status" section in Accuro's panel.
   *
   * @return The recovery date of the diagnosis.
   */
  @DocumentationExample("2017/11/29")
  @TypeHint(String.class)
  public AccuroCalendar getRecoveryDate() {
    return recoveryDate;
  }

  public void setRecoveryDate(AccuroCalendar recoveryDate) {
    this.recoveryDate = recoveryDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientDiagnosisHistoryDto)) {
      return false;
    }

    PatientDiagnosisHistoryDto that = (PatientDiagnosisHistoryDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getPatientDiagnosisId() != that.getPatientDiagnosisId()) {
      return false;
    }
    if (isNegative() != that.isNegative()) {
      return false;
    }
    if (getDiagnosisStatus() != null ? !getDiagnosisStatus().equals(that.getDiagnosisStatus())
        : that.getDiagnosisStatus() != null) {
      return false;
    }
    if (getCreatorUserId() != null ? !getCreatorUserId().equals(that.getCreatorUserId())
        : that.getCreatorUserId() != null) {
      return false;
    }
    if (getMaskId() != null ? !getMaskId().equals(that.getMaskId()) : that.getMaskId() != null) {
      return false;
    }
    if (getCodeTable() != null ? !getCodeTable().equals(that.getCodeTable())
        : that.getCodeTable() != null) {
      return false;
    }
    if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
      return false;
    }
    if (getDescription() != null ? !getDescription().equals(that.getDescription())
        : that.getDescription() != null) {
      return false;
    }
    if (getNotes() != null ? !getNotes().equals(that.getNotes()) : that.getNotes() != null) {
      return false;
    }
    if (getProblemDescription() != null ? !getProblemDescription()
        .equals(that.getProblemDescription()) : that.getProblemDescription() != null) {
      return false;
    }
    if (getSymptomCodeTable() != null ? !getSymptomCodeTable().equals(that.getSymptomCodeTable())
        : that.getSymptomCodeTable() != null) {
      return false;
    }
    if (getSymptomCode() != null ? !getSymptomCode().equals(that.getSymptomCode())
        : that.getSymptomCode() != null) {
      return false;
    }
    if (getSymptomDescription() != null ? !getSymptomDescription()
        .equals(that.getSymptomDescription()) : that.getSymptomDescription() != null) {
      return false;
    }
    if (getOutcomeCode() != null ? !getOutcomeCode().equals(that.getOutcomeCode())
        : that.getOutcomeCode() != null) {
      return false;
    }
    if (getAlternativeDescription() != null ? !getAlternativeDescription()
        .equals(that.getAlternativeDescription()) : that.getAlternativeDescription() != null) {
      return false;
    }
    if (getLifeStage() != that.getLifeStage()) {
      return false;
    }
    if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate())
        : that.getCreatedDate() != null) {
      return false;
    }
    if (getModifiedDate() != null ? !getModifiedDate().equals(that.getModifiedDate())
        : that.getModifiedDate() != null) {
      return false;
    }
    if (getDateOfDiagnosis() != null ? !getDateOfDiagnosis().equals(that.getDateOfDiagnosis())
        : that.getDateOfDiagnosis() != null) {
      return false;
    }
    if (getOnsetDate() != null ? !getOnsetDate().equals(that.getOnsetDate())
        : that.getOnsetDate() != null) {
      return false;
    }
    return getRecoveryDate() != null ? getRecoveryDate().equals(that.getRecoveryDate())
        : that.getRecoveryDate() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPatientId();
    result = 31 * result + getPatientDiagnosisId();
    result = 31 * result + (getDiagnosisStatus() != null ? getDiagnosisStatus().hashCode() : 0);
    result = 31 * result + (getCreatorUserId() != null ? getCreatorUserId().hashCode() : 0);
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    result = 31 * result + (getCodeTable() != null ? getCodeTable().hashCode() : 0);
    result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
    result =
        31 * result + (getProblemDescription() != null ? getProblemDescription().hashCode() : 0);
    result = 31 * result + (getSymptomCodeTable() != null ? getSymptomCodeTable().hashCode() : 0);
    result = 31 * result + (getSymptomCode() != null ? getSymptomCode().hashCode() : 0);
    result =
        31 * result + (getSymptomDescription() != null ? getSymptomDescription().hashCode() : 0);
    result = 31 * result + (getOutcomeCode() != null ? getOutcomeCode().hashCode() : 0);
    result =
        31 * result + (getAlternativeDescription() != null ? getAlternativeDescription().hashCode()
            : 0);
    result = 31 * result + (isNegative() ? 1 : 0);
    result = 31 * result + (getLifeStage() != null ? getLifeStage().hashCode() : 0);
    result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
    result = 31 * result + (getModifiedDate() != null ? getModifiedDate().hashCode() : 0);
    result = 31 * result + (getDateOfDiagnosis() != null ? getDateOfDiagnosis().hashCode() : 0);
    result = 31 * result + (getOnsetDate() != null ? getOnsetDate().hashCode() : 0);
    result = 31 * result + (getRecoveryDate() != null ? getRecoveryDate().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PatientDiagnosisHistoryDto{");
    sb.append("id=").append(id);
    sb.append(", patientId=").append(patientId);
    sb.append(", patientDiagnosisId=").append(patientDiagnosisId);
    sb.append(", diagnosisStatus=").append(diagnosisStatus);
    sb.append(", creatorUserId=").append(creatorUserId);
    sb.append(", maskId=").append(maskId);
    sb.append(", codeTable='").append(codeTable).append('\'');
    sb.append(", code='").append(code).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", notes='").append(notes).append('\'');
    sb.append(", problemDescription='").append(problemDescription).append('\'');
    sb.append(", symptomCodeTable='").append(symptomCodeTable).append('\'');
    sb.append(", symptomCode='").append(symptomCode).append('\'');
    sb.append(", symptomDescription='").append(symptomDescription).append('\'');
    sb.append(", outcomeCode='").append(outcomeCode).append('\'');
    sb.append(", alternativeDescription='").append(alternativeDescription).append('\'');
    sb.append(", negative=").append(negative);
    sb.append(", lifeStage=").append(lifeStage);
    sb.append(", createdDate=").append(createdDate);
    sb.append(", modifiedDate=").append(modifiedDate);
    sb.append(", dateOfDiagnosis=").append(dateOfDiagnosis);
    sb.append(", onsetDate=").append(onsetDate);
    sb.append(", recoveryDate=").append(recoveryDate);
    sb.append('}');
    return sb.toString();
  }
}
