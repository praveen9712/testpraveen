
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Billing details model object
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Billing details data transfer object model")
public class BillingDetailsDto {

  @JsonProperty("noCharge")
  @Schema(description = "Indication if the appointment is to be charged or not.", example = "false")
  private boolean noCharge;

  @JsonProperty("noShow")
  @Schema(description = "Indication if the patient failed to show.", example = "true")
  private boolean noShow;

  @JsonProperty("insurerId")
  @Schema(description = "The ID of the Insurer associated with this Billing Details DTO",
      example = "14")
  private int insurerId;

  /**
   * Indication if the appointment is to be charged or not.
   *
   * @documentationExample false
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isNoCharge() {
    return noCharge;
  }

  public void setNoCharge(boolean noCharge) {
    this.noCharge = noCharge;
  }

  /**
   * Indication if the patient failed to show.
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isNoShow() {
    return noShow;
  }

  public void setNoShow(boolean noShow) {
    this.noShow = noShow;
  }

  /**
   * The ID of the Insurer associated with this Billing Details DTO
   *
   * @documentationExample 14
   *
   * @return Insurer ID
   */
  public int getInsurerId() {
    return insurerId;
  }

  public void setInsurerId(int insurerId) {
    this.insurerId = insurerId;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 37 * hash + (this.noCharge ? 1 : 0);
    hash = 37 * hash + (this.noShow ? 1 : 0);
    hash = 37 * hash + this.insurerId;
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
    final BillingDetailsDto other = (BillingDetailsDto) obj;
    if (this.noCharge != other.noCharge) {
      return false;
    }
    if (this.noShow != other.noShow) {
      return false;
    }
    return this.insurerId == other.insurerId;
  }

}
