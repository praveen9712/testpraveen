
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient status Data transfer object")
public class PatientStatusDto {

  @JsonProperty("statusId")
  @Schema(description = "Unique id of a patient status", example = "18911")
  private int statusId;

  @NotNull(message = "The status name can not be null.")
  @Size(max = 50, message = "The status name can not exceed 50 characters.")
  @JsonProperty("statusName")
  @Schema(description = "Status name", example = "Active")
  private String statusName;

  @JsonProperty("statusOrder")
  @Schema(description = "Order of a status", example = "2")
  private int statusOrder;

  @JsonProperty("statusColor")
  @Schema(description = "Status colour code", example = "-65536")
  private int statusColor;

  @JsonProperty("coreStatus")
  @Schema(description = "Indicate if its a core status", example = "True")
  private boolean coreStatus;

  public int getStatusId() {
    return statusId;
  }

  public void setStatusId(int statusId) {
    this.statusId = statusId;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  public int getStatusOrder() {
    return statusOrder;
  }

  public void setStatusOrder(int statusOrder) {
    this.statusOrder = statusOrder;
  }

  public int getStatusColor() {
    return statusColor;
  }

  public void setStatusColor(int statusColor) {
    this.statusColor = statusColor;
  }

  public boolean isCoreStatus() {
    return coreStatus;
  }

  public void setCoreStatus(boolean coreStatus) {
    this.coreStatus = coreStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientStatusDto)) {
      return false;
    }
    PatientStatusDto that = (PatientStatusDto) o;
    if (getStatusId() != that.getStatusId()) {
      return false;
    }
    if (getStatusOrder() != that.getStatusOrder()) {
      return false;
    }
    if (getStatusColor() != that.getStatusColor()) {
      return false;
    }
    if (isCoreStatus() != that.isCoreStatus()) {
      return false;
    }
    return getStatusName() != null ? getStatusName().equals(that.getStatusName())
        : that.getStatusName() == null;
  }

  @Override
  public int hashCode() {
    int result = getStatusId();
    result = 31 * result + (getStatusName() != null ? getStatusName().hashCode() : 0);
    result = 31 * result + getStatusOrder();
    result = 31 * result + getStatusColor();
    result = 31 * result + (isCoreStatus() ? 1 : 0);
    return result;
  }
}
