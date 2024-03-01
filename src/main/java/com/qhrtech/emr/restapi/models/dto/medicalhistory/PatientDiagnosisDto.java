
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient diagnosis data transfer object model")
public class PatientDiagnosisDto {

  @JsonProperty("id")
  @Schema(description = "ID of the diagnosis", example = "1")
  private int id;

  @JsonProperty("patientId")
  @Schema(description = "ID of the patient", example = "1")
  private int patientId;

  @JsonProperty("codeTable")
  @Schema(description = "Code table name of the diagnosis", example = "ICD9")
  private String codeTable;

  @JsonProperty("code")
  @Schema(description = "Diagnosis code", example = "7641")
  private String code;

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
      example = "2018-07-13")
  private AccuroCalendar onsetDate;

  @JsonProperty("recoveryDate")
  @Schema(description = "The diagnosis recovery date", example = "2018-07-13", type = "string")
  private AccuroCalendar recoveryDate;

  @JsonProperty("negative")
  @Schema(description = "Indication if the diagnosis is negative", example = "true")
  private boolean negative;

  @JsonProperty("diagnosisStatus")
  @Schema(description = "Status of the diagnosis")
  private DiagnosisStatusDto diagnosisStatus;

  @JsonProperty("symptomCodeTable")
  @Schema(description = "Symptom code table name", example = "ICD9")
  private String symptomCodeTable;

  @JsonProperty("symptomCode")
  @Schema(description = "Symptom code", example = "E91")
  private String symptomCode;

  @JsonProperty("symptomDescription")
  @Schema(description = "Description of the diagnosis symptom",
      example = "'NO' INDICATOR PRESENT")
  private String symptomDescription;

  @JsonProperty("lifeStage")
  @Schema(description = "Life stage of the patient diagnosis",
      example = "Newborn, Newborn: Birth - 28 days")
  private LifeStageType lifeStage;

  @JsonProperty("problemDescription")
  @Schema(description = "Diagnosis problem description", example = "ATTENTION")
  private String problemDescription;

  @JsonProperty("creatorUserId")
  @Schema(description = "ID of the user who created the diagnosis", example = "1")
  private Integer creatorUserId;

  @JsonProperty("maskId")
  @Schema(description = "Mask id", example = "2")
  private Integer maskId;

  @JsonProperty("pinned")
  @Schema(description = "Indication if the diagnosis is pinned", example = "true")
  private boolean pinned;

  /**
   * A unique patient diagnosis id.
   *
   * @documentationExample 1
   *
   * @return The Diagnosis id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The patient id of the patient who has the diagnosis.
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
   * A code table name of a diagnosis.
   *
   * @see com.qhrtech.emr.accuro.model.codes.CodeTable
   *
   * @documentationExample ICD9
   *
   * @return The code table name.
   */
  public String getCodeTable() {
    return codeTable;
  }

  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * A diagnosis code.
   *
   * @documentationExample 7641
   *
   * @return The diagnosis code
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * A description of a diagnosis. This is also known as Diagnosis Name in Accuro.
   *
   * @documentationExample "LIGHT-FOR-DATES" WITH SIGNS OF FETAL MALNUTRITION
   *
   * @return
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Notes of a patient diagnosis.
   *
   * @documentationExample ATTENTION!!!
   *
   * @return The notes
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The date when diagnosis is created.
   *
   * @return The created date.
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Date of the diagnosis.
   *
   * @return The diagnosis date. The result is based on the date precision.
   */
  @DocumentationExample("2018-07-13")
  @TypeHint(String.class)
  public AccuroCalendar getDateOfDiagnosis() {
    return dateOfDiagnosis;
  }

  public void setDateOfDiagnosis(AccuroCalendar dateOfDiagnosis) {
    this.dateOfDiagnosis = dateOfDiagnosis;
  }

  /**
   * The onset date of diagnosis when the symptom first appeared.
   *
   * @return The onset date of diagnosis. The result is based on the date precision.
   */
  @DocumentationExample("2018-07-13")
  @TypeHint(String.class)
  public AccuroCalendar getOnsetDate() {
    return onsetDate;
  }

  public void setOnsetDate(AccuroCalendar onsetDate) {
    this.onsetDate = onsetDate;
  }

  /**
   * The diagnosis recovery date.
   *
   * @return The recovery date. The result is based on the date precision.
   */
  @DocumentationExample("2018-07-13")
  @TypeHint(String.class)
  public AccuroCalendar getRecoveryDate() {
    return recoveryDate;
  }

  public void setRecoveryDate(AccuroCalendar recoveryDate) {
    this.recoveryDate = recoveryDate;
  }

  /**
   * A flag if a diagnosis is negative.
   *
   * @documentationExample false
   *
   * @return The negative flag.
   */
  public boolean isNegative() {
    return negative;
  }

  public void setNegative(boolean negative) {
    this.negative = negative;
  }

  /**
   * Diagnosis status of a patient diagnosis.
   *
   * @return A {@link DiagnosisStatusDto}.
   */
  public DiagnosisStatusDto getDiagnosisStatus() {
    return diagnosisStatus;
  }

  public void setDiagnosisStatus(
      DiagnosisStatusDto diagnosisStatus) {
    this.diagnosisStatus = diagnosisStatus;
  }

  /**
   * A symptom code table's name.
   *
   * @see com.qhrtech.emr.accuro.model.codes.CodeTable
   *
   * @documentationExample ICD9
   *
   * @return Code table name.
   */
  public String getSymptomCodeTable() {
    return symptomCodeTable;
  }

  public void setSymptomCodeTable(String symptomCodeTable) {
    this.symptomCodeTable = symptomCodeTable;
  }

  /**
   * A code to represent the diagnosis sympton.
   *
   * @documentationExample E91
   *
   * @return Sympton code.
   */
  public String getSymptomCode() {
    return symptomCode;
  }

  public void setSymptomCode(String symptomCode) {
    this.symptomCode = symptomCode;
  }

  /**
   * A diagnosis symptom description.
   *
   * @documentationExample *'NO' INDICATOR PRESENT
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
   * Life Stage of the Patient Diagnosis.
   * <p>
   * Note, these are serialized as objects, with 2 fields: name, abbreviation.
   * <p>
   * Our documentation example is unable to display this.
   *
   * @documentationExample Newborn, Newborn: Birth - 28 days
   *
   * @return The life stage
   */
  public LifeStageType getLifeStage() {
    return lifeStage;
  }

  public void setLifeStage(LifeStageType lifeStage) {
    this.lifeStage = lifeStage;
  }

  /**
   * Diagnosis problem description.
   *
   * @documentationExample ATTENTION!!!
   *
   * @return Problem description
   */
  public String getProblemDescription() {
    return problemDescription;
  }

  public void setProblemDescription(String problemDescription) {
    this.problemDescription = problemDescription;
  }

  /**
   * User id who created the diagnosis.
   *
   * @documentationExample 1
   *
   * @return User id.
   */
  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * Mask id of a patient diagnosis.
   *
   * @documentationExample 1
   *
   * @return Mask id if it is masked otherwise {@code null}.
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * A flag if a diagnosis is pinned.
   *
   * @documentationExample false
   *
   * @return {@code true} if pinned otherwise {@code false}.
   */
  public boolean isPinned() {
    return pinned;
  }

  public void setPinned(boolean pinned) {
    this.pinned = pinned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientDiagnosisDto)) {
      return false;
    }

    PatientDiagnosisDto that = (PatientDiagnosisDto) o;

    if (id != that.id) {
      return false;
    }
    if (patientId != that.patientId) {
      return false;
    }
    if (negative != that.negative) {
      return false;
    }
    if (pinned != that.pinned) {
      return false;
    }
    if (codeTable != null ? !codeTable.equals(that.codeTable) : that.codeTable != null) {
      return false;
    }
    if (code != null ? !code.equals(that.code) : that.code != null) {
      return false;
    }
    if (description != null ? !description.equals(that.description) : that.description != null) {
      return false;
    }
    if (notes != null ? !notes.equals(that.notes) : that.notes != null) {
      return false;
    }
    if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
      return false;
    }
    if (dateOfDiagnosis != null ? !dateOfDiagnosis.equals(that.dateOfDiagnosis)
        : that.dateOfDiagnosis != null) {
      return false;
    }
    if (onsetDate != null ? !onsetDate.equals(that.onsetDate) : that.onsetDate != null) {
      return false;
    }
    if (recoveryDate != null ? !recoveryDate.equals(that.recoveryDate)
        : that.recoveryDate != null) {
      return false;
    }
    if (diagnosisStatus != null ? !diagnosisStatus.equals(that.diagnosisStatus)
        : that.diagnosisStatus != null) {
      return false;
    }
    if (symptomCodeTable != null ? !symptomCodeTable.equals(that.symptomCodeTable)
        : that.symptomCodeTable != null) {
      return false;
    }
    if (symptomCode != null ? !symptomCode.equals(that.symptomCode) : that.symptomCode != null) {
      return false;
    }
    if (symptomDescription != null ? !symptomDescription.equals(that.symptomDescription)
        : that.symptomDescription != null) {
      return false;
    }
    if (lifeStage != that.lifeStage) {
      return false;
    }
    if (problemDescription != null ? !problemDescription.equals(that.problemDescription)
        : that.problemDescription != null) {
      return false;
    }
    if (creatorUserId != null ? !creatorUserId.equals(that.creatorUserId)
        : that.creatorUserId != null) {
      return false;
    }
    return maskId != null ? maskId.equals(that.maskId) : that.maskId == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + patientId;
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
    result = 31 * result + (creatorUserId != null ? creatorUserId.hashCode() : 0);
    result = 31 * result + (maskId != null ? maskId.hashCode() : 0);
    result = 31 * result + (pinned ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PatientDiagnosisDto{"
        + "id=" + id
        + ", patientId=" + patientId
        + ", codeTable='" + codeTable + '\''
        + ", code='" + code + '\''
        + ", description='" + description + '\''
        + ", notes='" + notes + '\''
        + ", createdDate=" + createdDate
        + ", dateOfDiagnosis=" + dateOfDiagnosis
        + ", onsetDate=" + onsetDate
        + ", recoveryDate=" + recoveryDate
        + ", negative=" + negative
        + ", symptomCodeTable='" + symptomCodeTable + '\''
        + ", symptomCode='" + symptomCode + '\''
        + ", diagnosisStatus='" + diagnosisStatus + '\''
        + ", symptomDescription='" + symptomDescription + '\''
        + ", lifeStage=" + lifeStage
        + ", problemDescription='" + problemDescription + '\''
        + ", creatorUserId=" + creatorUserId
        + ", maskId=" + maskId
        + ", pinned=" + pinned
        + '}';
  }
}
