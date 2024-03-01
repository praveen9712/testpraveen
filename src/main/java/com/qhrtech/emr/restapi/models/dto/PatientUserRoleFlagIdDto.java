
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(
    description = "This object represents PatientId and flagId, "
        + "flagId can be userId when userFlags are created or roleId when roleFlags are created")
public class PatientUserRoleFlagIdDto {

  @JsonProperty("patientId")
  @Schema(description = "Patient Id", example = "1")
  private int patientId;

  @JsonProperty("flagId")
  @Schema(description = "RoleId or UserId", example = "1")
  private int flagId;

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public int getFlagId() {
    return flagId;
  }

  public void setFlagId(int flagId) {
    this.flagId = flagId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientUserRoleFlagIdDto that = (PatientUserRoleFlagIdDto) o;

    if (patientId != that.patientId) {
      return false;
    }
    return flagId == that.flagId;
  }

  @Override
  public int hashCode() {
    int result = patientId;
    result = 31 * result + flagId;
    return result;
  }
}
