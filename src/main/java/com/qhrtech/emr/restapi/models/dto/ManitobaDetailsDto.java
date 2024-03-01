
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Manitoba specific patient details.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Manitoba specific patient details.",
    implementation = ManitobaDetailsDto.class, name = "ManitobaDetailsDto")
public class ManitobaDetailsDto {

  @JsonProperty("healthRegistrationNumber")
  @Schema(description = "The health registration number", example = "123", type = "string",
      maxLength = 3)
  private String healthRegistrationNumber;

  /**
   * Patients health registration number
   *
   * @return Health registration number
   * @documentationExample 123
   */
  public String getHealthRegistrationNumber() {
    return healthRegistrationNumber;
  }

  public void setHealthRegistrationNumber(String healthRegistrationNumber) {
    this.healthRegistrationNumber = healthRegistrationNumber;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.healthRegistrationNumber);
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
    final ManitobaDetailsDto other = (ManitobaDetailsDto) obj;
    if (!Objects.equals(this.healthRegistrationNumber, other.healthRegistrationNumber)) {
      return false;
    }
    return true;
  }

}
