
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Medeo provider links for medeo practitioners")
public class MedeoProviderLinkDto {

  @JsonProperty("medeoId")
  @Schema(description = "medeoId", example = "1")
  private long medeoId;

  @JsonProperty("medeoOrganizationId")
  @Schema(description = "medeo organization id", example = "1")
  private long medeoOrganizationId;

  @JsonProperty("accuroProviderId")
  @Schema(description = "accuro provider id", example = "1")
  private int accuroProviderId;

  @JsonProperty("medeoUserId")
  @Schema(description = "medeo user id", example = "1")
  private Long medeoUserId;


  public long getMedeoId() {
    return medeoId;
  }

  public void setMedeoId(long medeoId) {
    this.medeoId = medeoId;
  }

  public long getMedeoOrganizationId() {
    return medeoOrganizationId;
  }

  public void setMedeoOrganizationId(long medeoOrganizationId) {
    this.medeoOrganizationId = medeoOrganizationId;
  }

  public int getAccuroProviderId() {
    return accuroProviderId;
  }

  public void setAccuroProviderId(int accuroProviderId) {
    this.accuroProviderId = accuroProviderId;
  }

  public Long getMedeoUserId() {
    return medeoUserId;
  }

  public void setMedeoUserId(Long medeoUserId) {
    this.medeoUserId = medeoUserId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MedeoProviderLinkDto that = (MedeoProviderLinkDto) o;

    if (getMedeoId() != that.getMedeoId()) {
      return false;
    }
    if (getMedeoOrganizationId() != that.getMedeoOrganizationId()) {
      return false;
    }
    if (getAccuroProviderId() != that.getAccuroProviderId()) {
      return false;
    }
    return getMedeoUserId().equals(that.getMedeoUserId());
  }

  @Override
  public int hashCode() {
    int result = (int) (getMedeoId() ^ (getMedeoId() >>> 32));
    result = 31 * result + (int) (getMedeoOrganizationId() ^ (getMedeoOrganizationId() >>> 32));
    result = 31 * result + getAccuroProviderId();
    result = 31 * result + getMedeoUserId().hashCode();
    return result;
  }
}
