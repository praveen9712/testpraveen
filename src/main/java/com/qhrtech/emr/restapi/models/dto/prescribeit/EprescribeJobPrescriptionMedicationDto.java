
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "EprescribeJobPrescriptionMedication Data transfer object")
public class EprescribeJobPrescriptionMedicationDto {

  @JsonProperty("prescriptionMedicationId")
  @Schema(description = "The e-prescribe job medication id", example = "1")
  private int prescriptionMedicationId;

  @JsonProperty("ePrescribeJobId")
  @Schema(description = "The e-prescribe job id", example = "1")
  private int eprescribeJobId;

  /**
   * The e-prescribe job medication id
   *
   * @return The e-prescribe job medication id
   */
  public int getPrescriptionMedicationId() {
    return prescriptionMedicationId;
  }

  public void setPrescriptionMedicationId(int prescriptionMedicationId) {
    this.prescriptionMedicationId = prescriptionMedicationId;
  }

  /**
   * The e-prescribe job id
   *
   * @return The e-prescribe job id
   */
  public int getEprescribeJobId() {
    return eprescribeJobId;
  }

  public void setEprescribeJobId(int eprescribeJobId) {
    this.eprescribeJobId = eprescribeJobId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EprescribeJobPrescriptionMedicationDto that = (EprescribeJobPrescriptionMedicationDto) o;

    if (prescriptionMedicationId != that.prescriptionMedicationId) {
      return false;
    }
    return eprescribeJobId == that.eprescribeJobId;
  }

  @Override
  public int hashCode() {
    int result = prescriptionMedicationId;
    result = 31 * result + eprescribeJobId;
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb =
        new StringBuffer("EprescribeJobPrescriptionMedicationDto{");
    sb.append("prescriptionMedicationId=").append(prescriptionMedicationId);
    sb.append(", ePrescribeJobId=").append(eprescribeJobId);
    sb.append('}');
    return sb.toString();
  }
}
