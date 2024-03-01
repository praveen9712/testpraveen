
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Patient chart lock exceptions data transfer object.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient chart lock exception Data transfer object")
public class PatientChartLockExceptionDto {

  @JsonProperty("exceptionId")
  @Schema(description = "The patient chart lock exception id", example = "1")
  private Integer exceptionId;

  @JsonProperty("patientId")
  @Schema(description = "Patient id. Read-only field.", example = "1")
  private int patientId;

  @JsonProperty("userId")
  @Schema(description = "User id", example = "1")
  @NotNull
  private Integer userId;

  /**
   * Patient chart lock exception id
   *
   * @return {@link Integer} Exception id
   */
  public Integer getExceptionId() {
    return exceptionId;
  }

  public void setExceptionId(Integer exceptionId) {
    this.exceptionId = exceptionId;
  }

  /**
   * Patient id. Read-only field
   *
   * @return {@link Integer} Patient id
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * User id
   *
   * @return {@link Integer} User id
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientChartLockExceptionDto that = (PatientChartLockExceptionDto) o;

    if (patientId != that.patientId) {
      return false;
    }
    if (!Objects.equals(userId, that.userId)) {
      return false;
    }
    return Objects.equals(exceptionId, that.exceptionId);
  }

  @Override
  public int hashCode() {
    int result = exceptionId != null ? exceptionId.hashCode() : 0;
    result = 31 * result + patientId;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PatientChartLockExceptionDto{");
    sb.append("exceptionId=").append(exceptionId);
    sb.append(", patientId=").append(patientId);
    sb.append(", userId=").append(userId);
    sb.append('}');
    return sb.toString();
  }
}
