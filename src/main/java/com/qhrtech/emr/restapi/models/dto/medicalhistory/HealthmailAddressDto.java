
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "HealthmailAddress data transfer object model")
public class HealthmailAddressDto {

  @JsonProperty(value = "id")
  @Schema(description = "Identity of the healthmail address", example = "12")
  private int id;

  @JsonProperty(value = "address")
  @Schema(description = "Healthmail address. Should not be more than 64 characters "
      + "and should have pattern as shown in the example.", example = "Name1#Name2#Name3")
  private String healthmailAddress;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHealthmailAddress() {
    return healthmailAddress;
  }

  public void setHealthmailAddress(String healthmailAddress) {
    this.healthmailAddress = healthmailAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HealthmailAddressDto that = (HealthmailAddressDto) o;

    if (id != that.id) {
      return false;
    }
    return healthmailAddress != null ? healthmailAddress.equals(that.healthmailAddress)
        : that.healthmailAddress == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (healthmailAddress != null ? healthmailAddress.hashCode() : 0);
    return result;
  }
}
