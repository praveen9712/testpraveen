
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The patient medical summary model object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient medical summary model object")
public class PatientMedicalSummaryDto {

  @JsonProperty("prescriptions")
  @Schema(description = "Prescriptions for a patient")
  private List<PrescriptionDto> prescriptions;

  @JsonProperty("diagnosis")
  @Schema(description = "Diagnosis for a patient")
  private Set<DiagnosisDto> diagnosis;

  @JsonProperty("allergies")
  @Schema(description = "Allergies for a patient")
  private AllergiesDto allergies;

  /**
   * A set of all the reactions.
   *
   * @return list of {@link PrescriptionDto}
   * @documentationExample list {@link PrescriptionDto}
   */
  public List<PrescriptionDto> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(
      List<PrescriptionDto> prescriptions) {
    this.prescriptions = prescriptions;
  }

  /**
   * A set of all the reactions.
   *
   * @return set of {@link DiagnosisDto}
   * @documentationExample Set {@link DiagnosisDto}
   */
  public Set<DiagnosisDto> getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(
      Set<DiagnosisDto> diagnosis) {
    this.diagnosis = diagnosis;
  }

  /**
   * A set of all the reactions.
   *
   * @return {@link AllergyReactionDto}
   * @documentationExample {@link AllergyReactionDto}
   */
  public AllergiesDto getAllergies() {
    return allergies;
  }

  public void setAllergies(AllergiesDto allergies) {
    this.allergies = allergies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientMedicalSummaryDto that = (PatientMedicalSummaryDto) o;

    if (!Objects.equals(prescriptions, that.prescriptions)) {
      return false;
    }
    if (!Objects.equals(diagnosis, that.diagnosis)) {
      return false;
    }
    return Objects.equals(allergies, that.allergies);
  }

  @Override
  public int hashCode() {
    int result = prescriptions != null ? prescriptions.hashCode() : 0;
    result = 31 * result + (diagnosis != null ? diagnosis.hashCode() : 0);
    result = 31 * result + (allergies != null ? allergies.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PatientMedicalSummaryDto{");
    sb.append("prescriptions=").append(prescriptions);
    sb.append(", diagnosis=").append(diagnosis);
    sb.append(", allergies=").append(allergies);
    sb.append('}');
    return sb.toString();
  }
}
