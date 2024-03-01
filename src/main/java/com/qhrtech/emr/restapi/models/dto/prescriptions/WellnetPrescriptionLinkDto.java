
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WellnetPrescriptionLink data transfer object model")
public class WellnetPrescriptionLinkDto {

  @JsonProperty("wellnetId")
  @Schema(description = "The Wellnet id related to the prescription", example = "1")
  private String wellnetId;

  /**
   * A Wellnet id related to a prescription.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getWellnetId() {
    return wellnetId;
  }

  public void setWellnetId(String wellnetId) {
    this.wellnetId = wellnetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WellnetPrescriptionLinkDto)) {
      return false;
    }

    WellnetPrescriptionLinkDto that = (WellnetPrescriptionLinkDto) o;

    return getWellnetId() != null ? getWellnetId().equals(that.getWellnetId())
        : that.getWellnetId() == null;
  }

  @Override
  public int hashCode() {
    return getWellnetId() != null ? getWellnetId().hashCode() : 0;
  }

  @Override
  public String toString() {
    return "WellnetPrescriptionLinkDto{"
        + "wellnetId='" + wellnetId + '\''
        + '}';
  }
}
