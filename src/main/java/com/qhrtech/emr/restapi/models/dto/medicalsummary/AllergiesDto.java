
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationAnnotationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

/**
 * The patient allergy history data transfer object model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient allergy history transfer model")
public class AllergiesDto {

  @JsonProperty("noKnownAllergies")
  @Schema(description = "Patient allergies not known.")
  private List<NoKnownAllergySummaryDto> noKnownAllergies;

  @JsonProperty("patientAllergies")
  @Schema(description = "Patient allergies.")
  private List<PatientAllergySummaryDto> patientAllergies;

  /**
   * Patients no known allergies summary
   *
   * @return list of {@link NoKnownAllergySummaryDto}
   *
   */
  public List<NoKnownAllergySummaryDto> getNoKnownAllergies() {
    return noKnownAllergies;
  }

  public void setNoKnownAllergies(
      List<NoKnownAllergySummaryDto> noKnownAllergies) {
    this.noKnownAllergies = noKnownAllergies;
  }

  /**
   * Patient allergy summary details
   *
   * @return List of {@link PatientAllergySummaryDto}
   *
   */
  public List<PatientAllergySummaryDto> getPatientAllergies() {
    return patientAllergies;
  }

  public void setPatientAllergies(
      List<PatientAllergySummaryDto> patientAllergies) {
    this.patientAllergies = patientAllergies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AllergiesDto that = (AllergiesDto) o;

    if (!Objects.equals(noKnownAllergies, that.noKnownAllergies)) {
      return false;
    }
    return Objects.equals(patientAllergies, that.patientAllergies);
  }

  @Override
  public int hashCode() {
    int result = noKnownAllergies != null ? noKnownAllergies.hashCode() : 0;
    result = 31 * result + (patientAllergies != null ? patientAllergies.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AllergiesDto{");
    sb.append("noKnownAllergies=").append(noKnownAllergies);
    sb.append(", patientAllergies=").append(patientAllergies);
    sb.append('}');
    return sb.toString();
  }
}
