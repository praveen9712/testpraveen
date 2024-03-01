
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Medeo Organization details")
public class MedeoOrganizationDto {

  @JsonProperty("medeoId")
  @Schema(description = "medeoId", example = "6253")
  private long medeoId;

  @JsonProperty("accuroOfficeId")
  @Schema(description = "accuro office id", example = "1280")
  private int accuroOfficeId;

  @JsonProperty("uuid")
  @Schema(description = "uuid", example = "dca20596-e35e-325f-56a4-3190281a020f")
  private String uuid;

  @JsonProperty("medeoOrgUuid")
  @Schema(description = "medeo organization uuid", example = "dca20596-e35e-325f-56a4-3190281a020f")
  private String medeoOrgUuid;

  public long getMedeoId() {
    return medeoId;
  }

  public void setMedeoId(long medeoId) {
    this.medeoId = medeoId;
  }

  public int getAccuroOfficeId() {
    return accuroOfficeId;
  }

  public void setAccuroOfficeId(int accuroOfficeId) {
    this.accuroOfficeId = accuroOfficeId;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  public String getMedeoOrgUuid() {
    return medeoOrgUuid;
  }

  public void setMedeoOrgUuid(String medeoOrgUuid) {
    this.medeoOrgUuid = medeoOrgUuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MedeoOrganizationDto that = (MedeoOrganizationDto) o;

    if (getMedeoId() != that.getMedeoId()) {
      return false;
    }
    if (getAccuroOfficeId() != that.getAccuroOfficeId()) {
      return false;
    }
    if (getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null) {
      return false;
    }
    return getMedeoOrgUuid() != null ? getMedeoOrgUuid().equals(that.getMedeoOrgUuid())
        : that.getMedeoOrgUuid() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getMedeoId() ^ (getMedeoId() >>> 32));
    result = 31 * result + getAccuroOfficeId();
    result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
    result = 31 * result + (getMedeoOrgUuid() != null ? getMedeoOrgUuid().hashCode() : 0);
    return result;
  }
}
