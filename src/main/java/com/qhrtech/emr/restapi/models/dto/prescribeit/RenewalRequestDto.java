
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class RenewalRequestDto {

  @JsonProperty("id")
  @Schema(description = "The unique identity", example = "10")
  private int id;

  @JsonProperty("externalTaskId")
  @Schema(description = "The external task identity", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String externalTaskId;

  @JsonProperty("externalTaskGroupId")
  @Schema(description = "The task group identity", example = "10")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String externalTaskGroupId;

  @JsonProperty("createdDateUtc")
  @Schema(description = "Created date time(read-only)", example = "2020-02-10T00:00:00.000")
  private LocalDateTime createdDateUtc;

  @JsonProperty("system")
  @Schema(description = "System description", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String system;

  @JsonProperty("systemVersion")
  @Schema(description = "System version", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String systemVersion;

  @JsonProperty("priority")
  @Schema(description = "Priority", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String priority;

  @JsonProperty("medicationRequestId")
  @Schema(description = "Medication request id", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String medicationRequestId;

  @JsonProperty("medicationCodeValue")
  @Schema(description = "Medication code value", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String medicationCodeValue;

  @JsonProperty("medicationCodeSystem")
  @Schema(description = "Medication code system", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String medicationCodeSystem;

  @JsonProperty("medicationCodeDescription")
  @Schema(description = "Medication code description", example = "String")
  @Size(max = 2500, message = "Maximum length allowed is 2500 characters.")
  private String medicationCodeDescription;

  @JsonProperty("medicationIsRepresentative")
  @Schema(description = "Medication representative flag", example = "True")
  private Boolean medicationIsRepresentative;

  @JsonProperty("medicationCategory")
  @Schema(description = "Medication category", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String medicationCategory;

  @JsonProperty("medicationRenderedDosage")
  @Schema(description = "Medication rendered dosage", example = "String")
  private String medicationRenderedDosage;

  @JsonProperty("medicationProtocol")
  @Schema(description = "Medication protocol", example = "String")
  @Size(max = 1000, message = "Maximum length allowed is 1000 characters.")
  private String medicationProtocol;

  @JsonProperty("medicationTreatmentType")
  @Schema(description = "Medication treatment type", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String medicationTreatmentType;

  @JsonProperty("medicationPharmacyInstructions")
  @Schema(description = "Medication pharmacy instructions", example = "Instruction")
  private String medicationPharmacyInstructions;

  @JsonProperty("medicationIsCompoundMonitored")
  @Schema(description = "The flag if medication is compound monitored", example = "False")
  private Boolean medicationIsCompoundMonitored;

  @JsonProperty("medicationAllowRenewals")
  @Schema(description = "Whether medication allow renewals", example = "True")
  private Boolean medicationAllowRenewals;

  @JsonProperty("medicationEmrId")
  @Schema(description = "Medication emr id", example = "String")
  @Size(max = 64, message = "Maximum length allowed is 64 characters.")
  private String medicationEmrId;

  @JsonProperty("medicationDisId")
  @Schema(description = "Medication dis id", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String medicationDisId;

  @JsonProperty("medicationDateWritten")
  @Schema(description = "Medication written date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime medicationDateWritten;

  @JsonProperty("medicationNote")
  @Schema(description = "Medication note", example = "String")
  @Size(max = 2500, message = "Maximum length allowed is 2500 characters.")
  private String medicationNote;

  @JsonProperty("priorPrescriptionId")
  @Schema(description = "Prior prescription Id", example = "String")
  @Size(max = 255, message = "Maximum length allowed is 255 characters.")
  private String priorPrescriptionId;

  @JsonProperty("renewalRequestGroupId")
  @Schema(description = "Renewal request group id", example = "10")
  private int renewalRequestGroupId;

  @Valid
  @NotNull
  @JsonProperty("dispenseRequest")
  @Schema(description = "Dispense request object")
  private DispenseRequestDto dispenseRequest;

  @JsonProperty("lastDispenseDate")
  @Schema(description = "last dispense date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime lastDispenseDate;

  @Valid
  @JsonProperty("renewalRequestDosages")
  @Schema(description = "Collection of renewal request dosage objects")
  private List<RenewalRequestDosageDto> renewalRequestDosages;

  @JsonProperty("taskInstructions")
  @Schema(description = "Task instructions")
  @Size(max = 2000)
  private String taskInstructions;

  @JsonProperty("communication")
  @Schema(description = "Communication")
  private String communication;


  /**
   * The unique identity
   *
   * @return Id
   * @documentationExample 10
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The external task identity
   *
   * @return external task Id
   * @documentationExample 10
   */
  public String getExternalTaskId() {
    return externalTaskId;
  }

  public void setExternalTaskId(String externalTaskId) {
    this.externalTaskId = externalTaskId;
  }

  /**
   * The task group identity
   *
   * @return externalTaskGroupId
   * @documentationExample 10
   */
  public String getExternalTaskGroupId() {
    return externalTaskGroupId;
  }

  public void setExternalTaskGroupId(String externalTaskGroupId) {
    this.externalTaskGroupId = externalTaskGroupId;
  }

  /**
   * Created date time(read-only)
   *
   * @return createdDateUtc
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDateUtc() {
    return createdDateUtc;
  }

  public void setCreatedDateUtc(LocalDateTime createdDateUtc) {
    this.createdDateUtc = createdDateUtc;
  }

  /**
   * System description
   *
   * @return system
   * @documentationExample String
   */
  public String getSystem() {
    return system;
  }

  public void setSystem(String system) {
    this.system = system;
  }

  /**
   * System version
   *
   * @return systemVersion
   * @documentationExample String
   */
  public String getSystemVersion() {
    return systemVersion;
  }

  public void setSystemVersion(String systemVersion) {
    this.systemVersion = systemVersion;
  }

  /**
   * Priority
   *
   * @return Priority
   * @documentationExample String
   */
  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * Medication request id
   *
   * @return Medication request id
   * @documentationExample String
   */
  public String getMedicationRequestId() {
    return medicationRequestId;
  }

  public void setMedicationRequestId(String medicationRequestId) {
    this.medicationRequestId = medicationRequestId;
  }

  /**
   * Medication code value
   *
   * @return Medication code value
   * @documentationExample String
   */
  public String getMedicationCodeValue() {
    return medicationCodeValue;
  }

  public void setMedicationCodeValue(String medicationCodeValue) {
    this.medicationCodeValue = medicationCodeValue;
  }

  /**
   * Medication code system
   *
   * @return Medication code system
   * @documentationExample String
   */
  public String getMedicationCodeSystem() {
    return medicationCodeSystem;
  }

  public void setMedicationCodeSystem(String medicationCodeSystem) {
    this.medicationCodeSystem = medicationCodeSystem;
  }

  /**
   * Medication code description
   *
   * @return Medication code description
   * @documentationExample String
   */
  public String getMedicationCodeDescription() {
    return medicationCodeDescription;
  }

  public void setMedicationCodeDescription(String medicationCodeDescription) {
    this.medicationCodeDescription = medicationCodeDescription;
  }

  /**
   * Medication representative flag
   *
   * @return If Medication is representative.
   * @documentationExample True
   */
  public Boolean getMedicationIsRepresentative() {
    return medicationIsRepresentative;
  }

  public void setMedicationIsRepresentative(Boolean medicationIsRepresentative) {
    this.medicationIsRepresentative = medicationIsRepresentative;
  }

  /**
   * Medication category
   *
   * @return Medication category
   * @documentationExample String
   */
  public String getMedicationCategory() {
    return medicationCategory;
  }

  public void setMedicationCategory(String medicationCategory) {
    this.medicationCategory = medicationCategory;
  }

  /**
   * Medication rendered dosage
   *
   * @return Medication rendered dosage
   * @documentationExample String
   */
  public String getMedicationRenderedDosage() {
    return medicationRenderedDosage;
  }

  public void setMedicationRenderedDosage(String medicationRenderedDosage) {
    this.medicationRenderedDosage = medicationRenderedDosage;
  }

  /**
   * Medication protocol
   *
   * @return Medication protocol
   * @documentationExample String
   */
  public String getMedicationProtocol() {
    return medicationProtocol;
  }

  public void setMedicationProtocol(String medicationProtocol) {
    this.medicationProtocol = medicationProtocol;
  }

  /**
   * Medication treatment type
   *
   * @return Medication treatment type
   * @documentationExample String
   */
  public String getMedicationTreatmentType() {
    return medicationTreatmentType;
  }

  public void setMedicationTreatmentType(String medicationTreatmentType) {
    this.medicationTreatmentType = medicationTreatmentType;
  }

  /**
   * Medication pharmacy instructions
   *
   * @return Medication pharmacy instructions
   * @documentationExample String
   */
  public String getMedicationPharmacyInstructions() {
    return medicationPharmacyInstructions;
  }

  public void setMedicationPharmacyInstructions(String medicationPharmacyInstructions) {
    this.medicationPharmacyInstructions = medicationPharmacyInstructions;
  }

  /**
   * The flag if medication is compound monitored
   *
   * @return Boolean if medication is compound monitored
   * @documentationExample True
   */
  public Boolean getMedicationIsCompoundMonitored() {
    return medicationIsCompoundMonitored;
  }

  public void setMedicationIsCompoundMonitored(Boolean medicationIsCompoundMonitored) {
    this.medicationIsCompoundMonitored = medicationIsCompoundMonitored;
  }

  /**
   * Whether medication allow renewals
   *
   * @return Whether medication allow renewals
   * @documentationExample False
   */
  public Boolean getMedicationAllowRenewals() {
    return medicationAllowRenewals;
  }

  public void setMedicationAllowRenewals(Boolean medicationAllowRenewals) {
    this.medicationAllowRenewals = medicationAllowRenewals;
  }

  /**
   * Medication emr id
   *
   * @return Medication emr id
   * @documentationExample 10(or string)
   */
  public String getMedicationEmrId() {
    return medicationEmrId;
  }

  public void setMedicationEmrId(String medicationEmrId) {
    this.medicationEmrId = medicationEmrId;
  }

  /**
   * Medication dis id
   *
   * @return Medication dis id
   * @documentationExample 10(or string)
   */
  public String getMedicationDisId() {
    return medicationDisId;
  }

  public void setMedicationDisId(String medicationDisId) {
    this.medicationDisId = medicationDisId;
  }

  /**
   * Medication written date
   *
   * @return Medication written date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getMedicationDateWritten() {
    return medicationDateWritten;
  }

  public void setMedicationDateWritten(LocalDateTime medicationDateWritten) {
    this.medicationDateWritten = medicationDateWritten;
  }

  /**
   * Medication note
   *
   * @return Medication note
   * @documentationExample String
   */
  public String getMedicationNote() {
    return medicationNote;
  }

  public void setMedicationNote(String medicationNote) {
    this.medicationNote = medicationNote;
  }

  /**
   * Prior prescription Id
   *
   * @return Prior prescription Id
   * @documentationExample 10(or string)
   */
  public String getPriorPrescriptionId() {
    return priorPrescriptionId;
  }

  public void setPriorPrescriptionId(String priorPrescriptionId) {
    this.priorPrescriptionId = priorPrescriptionId;
  }

  /**
   * Renewal request group id
   *
   * @return Renewal request group id
   * @documentationExample 10
   */
  public int getRenewalRequestGroupId() {
    return renewalRequestGroupId;
  }

  public void setRenewalRequestGroupId(int renewalRequestGroupId) {
    this.renewalRequestGroupId = renewalRequestGroupId;
  }

  /**
   * Dispense request object
   *
   * @return Dispense request object
   * @documentationExample {@link DispenseRequestDto}
   */
  public DispenseRequestDto getDispenseRequest() {
    return dispenseRequest;
  }

  public void setDispenseRequest(DispenseRequestDto dispenseRequest) {
    this.dispenseRequest = dispenseRequest;
  }

  /**
   * last dispense date
   *
   * @return last dispense date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastDispenseDate() {
    return lastDispenseDate;
  }

  public void setLastDispenseDate(LocalDateTime lastDispenseDate) {
    this.lastDispenseDate = lastDispenseDate;
  }

  /**
   * Collection of renewal request dosage objects
   *
   * @return Collection of {@link RenewalRequestDosageDto} objects
   * @documentationExample {@link RenewalRequestDosageDto}
   */
  public List<RenewalRequestDosageDto> getRenewalRequestDosages() {
    return renewalRequestDosages;
  }

  public void setRenewalRequestDosages(List<RenewalRequestDosageDto> renewalRequestDosages) {
    this.renewalRequestDosages = renewalRequestDosages;
  }

  /**
   * Task instructions
   *
   * @return Instructions
   * @documentationExample String
   */
  public String getTaskInstructions() {
    return taskInstructions;
  }

  public void setTaskInstructions(String taskInstructions) {
    this.taskInstructions = taskInstructions;
  }

  /**
   * Communication type
   *
   * @return String
   * @documentationExample communication A
   */
  public String getCommunication() {
    return communication;
  }

  public void setCommunication(String communication) {
    this.communication = communication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RenewalRequestDto that = (RenewalRequestDto) o;

    if (id != that.id) {
      return false;
    }
    if (renewalRequestGroupId != that.renewalRequestGroupId) {
      return false;
    }
    if (externalTaskId != null ? !externalTaskId.equals(that.externalTaskId)
        : that.externalTaskId != null) {
      return false;
    }
    if (externalTaskGroupId != null ? !externalTaskGroupId.equals(that.externalTaskGroupId)
        : that.externalTaskGroupId != null) {
      return false;
    }
    if (createdDateUtc != null ? !createdDateUtc.equals(that.createdDateUtc)
        : that.createdDateUtc != null) {
      return false;
    }
    if (system != null ? !system.equals(that.system) : that.system != null) {
      return false;
    }
    if (systemVersion != null ? !systemVersion.equals(that.systemVersion)
        : that.systemVersion != null) {
      return false;
    }
    if (priority != null ? !priority.equals(that.priority) : that.priority != null) {
      return false;
    }
    if (medicationRequestId != null ? !medicationRequestId.equals(that.medicationRequestId)
        : that.medicationRequestId != null) {
      return false;
    }
    if (medicationCodeValue != null ? !medicationCodeValue.equals(that.medicationCodeValue)
        : that.medicationCodeValue != null) {
      return false;
    }
    if (medicationCodeSystem != null ? !medicationCodeSystem.equals(that.medicationCodeSystem)
        : that.medicationCodeSystem != null) {
      return false;
    }
    if (medicationCodeDescription != null ? !medicationCodeDescription.equals(
        that.medicationCodeDescription) : that.medicationCodeDescription != null) {
      return false;
    }
    if (medicationIsRepresentative != null ? !medicationIsRepresentative.equals(
        that.medicationIsRepresentative) : that.medicationIsRepresentative != null) {
      return false;
    }
    if (medicationCategory != null ? !medicationCategory.equals(that.medicationCategory)
        : that.medicationCategory != null) {
      return false;
    }
    if (medicationRenderedDosage != null ? !medicationRenderedDosage.equals(
        that.medicationRenderedDosage) : that.medicationRenderedDosage != null) {
      return false;
    }
    if (medicationProtocol != null ? !medicationProtocol.equals(that.medicationProtocol)
        : that.medicationProtocol != null) {
      return false;
    }
    if (medicationTreatmentType != null ? !medicationTreatmentType.equals(
        that.medicationTreatmentType) : that.medicationTreatmentType != null) {
      return false;
    }
    if (medicationPharmacyInstructions != null ? !medicationPharmacyInstructions.equals(
        that.medicationPharmacyInstructions) : that.medicationPharmacyInstructions != null) {
      return false;
    }
    if (medicationIsCompoundMonitored != null ? !medicationIsCompoundMonitored.equals(
        that.medicationIsCompoundMonitored) : that.medicationIsCompoundMonitored != null) {
      return false;
    }
    if (medicationAllowRenewals != null ? !medicationAllowRenewals.equals(
        that.medicationAllowRenewals) : that.medicationAllowRenewals != null) {
      return false;
    }
    if (medicationEmrId != null ? !medicationEmrId.equals(that.medicationEmrId)
        : that.medicationEmrId != null) {
      return false;
    }
    if (medicationDisId != null ? !medicationDisId.equals(that.medicationDisId)
        : that.medicationDisId != null) {
      return false;
    }
    if (medicationDateWritten != null ? !medicationDateWritten.equals(that.medicationDateWritten)
        : that.medicationDateWritten != null) {
      return false;
    }
    if (medicationNote != null ? !medicationNote.equals(that.medicationNote)
        : that.medicationNote != null) {
      return false;
    }
    if (priorPrescriptionId != null ? !priorPrescriptionId.equals(that.priorPrescriptionId)
        : that.priorPrescriptionId != null) {
      return false;
    }
    if (dispenseRequest != null ? !dispenseRequest.equals(that.dispenseRequest)
        : that.dispenseRequest != null) {
      return false;
    }
    if (lastDispenseDate != null ? !lastDispenseDate.equals(that.lastDispenseDate)
        : that.lastDispenseDate != null) {
      return false;
    }
    if (renewalRequestDosages != null ? !renewalRequestDosages.equals(that.renewalRequestDosages)
        : that.renewalRequestDosages != null) {
      return false;
    }
    if (taskInstructions != null ? !taskInstructions.equals(that.taskInstructions)
        : that.taskInstructions != null) {
      return false;
    }
    if (communication != null ? !communication.equals(that.communication)
        : that.communication != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (externalTaskId != null ? externalTaskId.hashCode() : 0);
    result = 31 * result + (externalTaskGroupId != null ? externalTaskGroupId.hashCode() : 0);
    result = 31 * result + (createdDateUtc != null ? createdDateUtc.hashCode() : 0);
    result = 31 * result + (system != null ? system.hashCode() : 0);
    result = 31 * result + (systemVersion != null ? systemVersion.hashCode() : 0);
    result = 31 * result + (priority != null ? priority.hashCode() : 0);
    result = 31 * result + (medicationRequestId != null ? medicationRequestId.hashCode() : 0);
    result = 31 * result + (medicationCodeValue != null ? medicationCodeValue.hashCode() : 0);
    result = 31 * result + (medicationCodeSystem != null ? medicationCodeSystem.hashCode() : 0);
    result =
        31 * result + (medicationCodeDescription != null ? medicationCodeDescription.hashCode()
            : 0);
    result =
        31 * result + (medicationIsRepresentative != null ? medicationIsRepresentative.hashCode()
            : 0);
    result = 31 * result + (medicationCategory != null ? medicationCategory.hashCode() : 0);
    result =
        31 * result + (medicationRenderedDosage != null ? medicationRenderedDosage.hashCode() : 0);
    result = 31 * result + (medicationProtocol != null ? medicationProtocol.hashCode() : 0);
    result =
        31 * result + (medicationTreatmentType != null ? medicationTreatmentType.hashCode() : 0);
    result = 31 * result + (medicationPharmacyInstructions != null
        ? medicationPharmacyInstructions.hashCode()
        : 0);
    result = 31 * result + (medicationIsCompoundMonitored != null
        ? medicationIsCompoundMonitored.hashCode()
        : 0);
    result =
        31 * result + (medicationAllowRenewals != null ? medicationAllowRenewals.hashCode() : 0);
    result = 31 * result + (medicationEmrId != null ? medicationEmrId.hashCode() : 0);
    result = 31 * result + (medicationDisId != null ? medicationDisId.hashCode() : 0);
    result = 31 * result + (medicationDateWritten != null ? medicationDateWritten.hashCode() : 0);
    result = 31 * result + (medicationNote != null ? medicationNote.hashCode() : 0);
    result = 31 * result + (priorPrescriptionId != null ? priorPrescriptionId.hashCode() : 0);
    result = 31 * result + renewalRequestGroupId;
    result = 31 * result + (dispenseRequest != null ? dispenseRequest.hashCode() : 0);
    result = 31 * result + (lastDispenseDate != null ? lastDispenseDate.hashCode() : 0);
    result = 31 * result + (renewalRequestDosages != null ? renewalRequestDosages.hashCode() : 0);
    result = 31 * result + (taskInstructions != null ? taskInstructions.hashCode() : 0);
    result = 31 * result + (communication != null ? communication.hashCode() : 0);
    return result;
  }
}
