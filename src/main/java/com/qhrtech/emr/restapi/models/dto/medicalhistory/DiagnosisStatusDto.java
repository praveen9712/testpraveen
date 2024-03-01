
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The DiagnosisStatus data transfer object model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DiagnosisStatus data transfer object model")
public class DiagnosisStatusDto {

  @JsonProperty("statusId")
  @Schema(description = "ID of the diagnosis status", example = "1")
  private int statusId;

  @JsonProperty("statusName")
  @Schema(description = "Name of the diagnosis status", example = "Active")
  private String statusName;

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DiagnosisStatusDto{");
    sb.append("statusId=").append(statusId);
    sb.append(", statusName='").append(statusName).append('\'');
    sb.append('}');
    return sb.toString();
  }

  /**
   * The Status ID of diagnosis status.
   *
   * @documentationExample 3
   *
   * @return Status id
   */
  public int getStatusId() {
    return statusId;
  }

  public void setStatusId(int statusId) {
    this.statusId = statusId;
  }

  /**
   * The name of diagnosis status.
   *
   * @documentationExample Suspected
   *
   * @return Status name
   */
  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.statusId;
    hash = 97 * hash + Objects.hashCode(this.statusName);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DiagnosisStatusDto other = (DiagnosisStatusDto) obj;
    if (this.statusId != other.statusId) {
      return false;
    }
    return Objects.equals(this.statusName, other.statusName);
  }

}
