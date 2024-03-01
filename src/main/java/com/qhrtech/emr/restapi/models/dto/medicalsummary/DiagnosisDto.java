
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.LifeStageType;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.joda.time.LocalDateTime;

/**
 * The patient diagnosis summary data transfer object model. This dto has specific fields from
 * PatientDiagnosisHistoryDto which are required by the story.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The diagnosis summary transfer model")
public class DiagnosisDto {

  @JsonProperty("id")
  @Schema(description = "The unique patient diagnosis summary id", example = "1")
  private int id;

  @JsonProperty("diagnosisDisplayText")
  @Schema(description = "Displays top-level summary for the diagnosis.",
      example = "BENZEL SPOT-ON ACNE 2.5% GEL")
  private String diagnosisDisplayText;

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

  @JsonProperty("createdDate")
  @Schema(description = "Date when diagnosis is created", example = "2018-07-13T00:00:00.000",
      type = "string")
  private LocalDateTime createdDate;

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

  @JsonProperty("negative")
  @Schema(description = "Flag indicating if the diagnosis is negative", example = "true")
  private boolean negative;

  @JsonProperty("diagnosisStatus")
  @Schema(description = "Status of the diagnosis")
  private DiagnosisStatusDto diagnosisStatus; // status

  @JsonProperty("symptomCodeTable")
  @Schema(description = "Symptom code table name", example = "ICD9")
  private String symptomCodeTable;

  @JsonProperty("symptomCode")
  @Schema(description = "Symptom code", example = "E91")
  private String symptomCode; // symptom

  @JsonProperty("symptomDescription")
  @Schema(description = "Diagnosis symptom description", example = "INJURY TO INTERNAL ORGAN")
  private String symptomDescription;

  @JsonProperty("lifeStage")
  @Schema(description = "Life stage of the patient diagnosis",
      example = "Newborn, Newborn: Birth - 28 days")
  private LifeStageType lifeStage;

  @JsonProperty("problemDescription")
  @Schema(description = "Diagnosis problem description", example = "ATTENTION")
  private String problemDescription;

  /**
   * Diagnosis display text
   *
   * @return diagnosisDisplayText
   * @documentationExample String
   */
  public String getDiagnosisDisplayText() {
    return diagnosisDisplayText;
  }

  public void setDiagnosisDisplayText(String diagnosisDisplayText) {
    this.diagnosisDisplayText = diagnosisDisplayText;
  }

  /**
   * Symptom description
   *
   * @return symptomDescription
   * @documentationExample String
   */
  public String getSymptomDescription() {
    return symptomDescription;
  }

  public void setSymptomDescription(String symptomDescription) {
    this.symptomDescription = symptomDescription;
  }

  /**
   * The unique Patient Diagnosis summary id
   * 
   * @return ID
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Diagnosis code table
   *
   * @return codeTable
   * @documentationExample ICD19
   */
  public String getCodeTable() {
    return codeTable;
  }

  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * Diagnosis code
   *
   * @return code
   * @documentationExample 11
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Diagnosis description
   *
   * @return description
   * @documentationExample LIGHT-FOR-DATES' WITH SIGNS OF FETAL MALNUTRITION
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Diagnosis notes
   *
   * @return notes
   * @documentationExample the notes
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Date of diagnosis
   *
   * @return dateOfDiagnosis
   * @documentationExample "2018-07-13"
   */
  public AccuroCalendar getDateOfDiagnosis() {
    return dateOfDiagnosis;
  }

  public void setDateOfDiagnosis(AccuroCalendar dateOfDiagnosis) {
    this.dateOfDiagnosis = dateOfDiagnosis;
  }

  /**
   * Diagnosis On Set Date
   *
   * @return onsetDate
   * @documentationExample "2018-07-13"
   */
  public AccuroCalendar getOnsetDate() {
    return onsetDate;
  }

  public void setOnsetDate(AccuroCalendar onsetDate) {
    this.onsetDate = onsetDate;
  }

  /**
   * Diagnosis Recovery Date.
   *
   * @return recoveryDate
   * @documentationExample "2018-07-13"
   */
  public AccuroCalendar getRecoveryDate() {
    return recoveryDate;
  }

  public void setRecoveryDate(AccuroCalendar recoveryDate) {
    this.recoveryDate = recoveryDate;
  }


  /**
   * Indication of is active or not.
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isNegative() {
    return negative;
  }

  public void setNegative(boolean negative) {
    this.negative = negative;
  }

  /**
   * Diagnosis status
   *
   * @return diagnosisStatus
   */
  public DiagnosisStatusDto getDiagnosisStatus() {
    return diagnosisStatus;
  }

  public void setDiagnosisStatus(
      DiagnosisStatusDto diagnosisStatus) {
    this.diagnosisStatus = diagnosisStatus;
  }

  /**
   * Symptom Code Table
   *
   * @return symptomCodeTable
   * @documentationExample ICD12
   */
  public String getSymptomCodeTable() {
    return symptomCodeTable;
  }

  public void setSymptomCodeTable(String symptomCodeTable) {
    this.symptomCodeTable = symptomCodeTable;
  }

  /**
   * Diagnosis Symptom Code
   *
   * @return symptomCode
   * @documentationExample 0713
   */
  public String getSymptomCode() {
    return symptomCode;
  }

  public void setSymptomCode(String symptomCode) {
    this.symptomCode = symptomCode;
  }

  /**
   * Problem Description
   *
   * @return problemDescription
   * @documentationExample String
   */
  public String getProblemDescription() {
    return problemDescription;
  }

  public void setProblemDescription(String problemDescription) {
    this.problemDescription = problemDescription;
  }

  /**
   * Diagnosis Life Stage
   *
   * @return lifeStage
   * @documentationExample Newborn, Newborn: Birth - 28 days
   */
  public LifeStageType getLifeStage() {
    return lifeStage;
  }

  public void setLifeStage(LifeStageType lifeStage) {
    this.lifeStage = lifeStage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DiagnosisDto that = (DiagnosisDto) o;

    if (id != that.id) {
      return false;
    }
    if (negative != that.negative) {
      return false;
    }
    if (!Objects.equals(diagnosisDisplayText, that.diagnosisDisplayText)) {
      return false;
    }
    if (!Objects.equals(codeTable, that.codeTable)) {
      return false;
    }
    if (!Objects.equals(code, that.code)) {
      return false;
    }
    if (!Objects.equals(description, that.description)) {
      return false;
    }
    if (!Objects.equals(notes, that.notes)) {
      return false;
    }
    if (!Objects.equals(createdDate, that.createdDate)) {
      return false;
    }
    if (!Objects.equals(dateOfDiagnosis, that.dateOfDiagnosis)) {
      return false;
    }
    if (!Objects.equals(onsetDate, that.onsetDate)) {
      return false;
    }
    if (!Objects.equals(recoveryDate, that.recoveryDate)) {
      return false;
    }
    if (!Objects.equals(diagnosisStatus, that.diagnosisStatus)) {
      return false;
    }
    if (!Objects.equals(symptomCodeTable, that.symptomCodeTable)) {
      return false;
    }
    if (!Objects.equals(symptomCode, that.symptomCode)) {
      return false;
    }
    if (!Objects.equals(symptomDescription, that.symptomDescription)) {
      return false;
    }
    if (lifeStage != that.lifeStage) {
      return false;
    }
    return Objects.equals(problemDescription, that.problemDescription);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (diagnosisDisplayText != null ? diagnosisDisplayText.hashCode() : 0);
    result = 31 * result + (codeTable != null ? codeTable.hashCode() : 0);
    result = 31 * result + (code != null ? code.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (dateOfDiagnosis != null ? dateOfDiagnosis.hashCode() : 0);
    result = 31 * result + (onsetDate != null ? onsetDate.hashCode() : 0);
    result = 31 * result + (recoveryDate != null ? recoveryDate.hashCode() : 0);
    result = 31 * result + (negative ? 1 : 0);
    result = 31 * result + (diagnosisStatus != null ? diagnosisStatus.hashCode() : 0);
    result = 31 * result + (symptomCodeTable != null ? symptomCodeTable.hashCode() : 0);
    result = 31 * result + (symptomCode != null ? symptomCode.hashCode() : 0);
    result = 31 * result + (symptomDescription != null ? symptomDescription.hashCode() : 0);
    result = 31 * result + (lifeStage != null ? lifeStage.hashCode() : 0);
    result = 31 * result + (problemDescription != null ? problemDescription.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DiagnosisDto{");
    sb.append("id=").append(id);
    sb.append(", diagnosisDisplayText='").append(diagnosisDisplayText).append('\'');
    sb.append(", codeTable='").append(codeTable).append('\'');
    sb.append(", code='").append(code).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", notes='").append(notes).append('\'');
    sb.append(", createdDate=").append(createdDate);
    sb.append(", dateOfDiagnosis=").append(dateOfDiagnosis);
    sb.append(", onsetDate=").append(onsetDate);
    sb.append(", recoveryDate=").append(recoveryDate);
    sb.append(", negative=").append(negative);
    sb.append(", diagnosisStatus=").append(diagnosisStatus);
    sb.append(", symptomCodeTable='").append(symptomCodeTable).append('\'');
    sb.append(", symptomCode='").append(symptomCode).append('\'');
    sb.append(", symptomDescription='").append(symptomDescription).append('\'');
    sb.append(", lifeStage=").append(lifeStage);
    sb.append(", problemDescription='").append(problemDescription).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
