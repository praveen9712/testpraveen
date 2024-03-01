
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Patient chart lock data transfer object.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient chart lock Data transfer object")
public class PatientChartLockDto {

  @JsonProperty("patientChartLockId")
  @Schema(description = "The patient chart lock id", example = "1")
  private Integer patientChartLockId;

  @JsonProperty("patientId")
  @Schema(description = "The patient id", example = "1")
  @NotNull
  private Integer patientId;

  @JsonProperty("physicianId")
  @Schema(description = "The physician id. Read-only field and its value would be set as the "
      + "physician Id associated to the Accuro user.", example = "1")
  private Integer physicianId;

  @JsonProperty("allProviders")
  @Schema(description = "If the chart lock is for all providers or not. Default value is false. "
      + "Note: allProviders and hasExceptions cannot be true at the same time.",
      example = "true")
  private boolean allProviders;

  @JsonProperty("hasExceptions")
  @Schema(description = "If the patient chart lock has exception users for access. "
      + "Read-only field and it would be set to true or false by the system depending upon the "
      + "user IDs passed in the patient chart lock exception object. ",
      example = "true")
  private boolean hasExceptions;

  @JsonProperty("exceptions")
  @Schema(description = "List of patient chart lock exception DTO")
  @Valid
  private List<PatientChartLockExceptionDto> exceptions;

  /**
   * Patient chart lock id
   *
   * @return {@link Integer} Patient chart lock id
   */
  public Integer getPatientChartLockId() {
    return patientChartLockId;
  }

  public void setPatientChartLockId(Integer patientChartLockId) {
    this.patientChartLockId = patientChartLockId;
  }

  /**
   * Patient id
   *
   * @return {@link Integer} Patient id
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Physician id
   *
   * @return {@link Integer} Physician id
   */
  public Integer getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(Integer physicianId) {
    this.physicianId = physicianId;
  }

  /**
   * If the chart lock is for all providers or not. Note: allProviders and hasExceptions cannot be
   * true at the same time.
   *
   * @return true/false
   */
  public boolean isAllProviders() {
    return allProviders;
  }

  public void setAllProviders(boolean allProviders) {
    this.allProviders = allProviders;
  }

  /**
   * If the patient chart lock has exception users for access. Read-only field and it would be set
   * to true or false by the system depending upon the user IDs passed in the patient chart lock
   * exception object.
   *
   * @return true/false
   */
  public boolean hasExceptions() {
    return hasExceptions;
  }

  public void setHasExceptions(boolean hasExceptions) {
    this.hasExceptions = hasExceptions;
  }

  /**
   * List of patient chart lock exception DTO {@link PatientChartLockExceptionDto}.
   *
   * @return List of {@link PatientChartLockExceptionDto}
   */
  public List<PatientChartLockExceptionDto> getExceptions() {
    return exceptions;
  }

  public void setExceptions(
      List<PatientChartLockExceptionDto> exceptions) {
    this.exceptions = exceptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientChartLockDto that = (PatientChartLockDto) o;

    if (!Objects.equals(patientId, that.patientId)) {
      return false;
    }
    if (allProviders != that.allProviders) {
      return false;
    }
    if (hasExceptions != that.hasExceptions) {
      return false;
    }
    if (!Objects.equals(patientChartLockId, that.patientChartLockId)) {
      return false;
    }
    if (!Objects.equals(physicianId, that.physicianId)) {
      return false;
    }
    return Objects.equals(exceptions, that.exceptions);
  }

  @Override
  public int hashCode() {
    int result = patientChartLockId != null ? patientChartLockId.hashCode() : 0;
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (physicianId != null ? physicianId.hashCode() : 0);
    result = 31 * result + (allProviders ? 1 : 0);
    result = 31 * result + (hasExceptions ? 1 : 0);
    result = 31 * result + (exceptions != null ? exceptions.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PatientChartLockDto{");
    sb.append("patientChartLockId=").append(patientChartLockId);
    sb.append(", patientId=").append(patientId);
    sb.append(", physicianId=").append(physicianId);
    sb.append(", allProviders=").append(allProviders);
    sb.append(", hasExceptions=").append(hasExceptions);
    sb.append(", exceptions=").append(exceptions);
    sb.append('}');
    return sb.toString();
  }
}
