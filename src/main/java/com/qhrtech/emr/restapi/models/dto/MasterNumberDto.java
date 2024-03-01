
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Master number details. Valid in Ontario province only.")
public class MasterNumberDto {

  @JsonProperty("masterNumber")
  @Schema(description = "Master number", example = "1")
  private int masterNumber;

  @JsonProperty("name")
  @Schema(description = "Master number name", example = "NURSING HOME - ONTARIO - GENERIC #")
  private String name;

  @JsonProperty("type")
  @Schema(description = "Master number type", example = "IN")
  private String type;

  @JsonProperty("location")
  @Schema(description = "Master number location", example = "TORONTO")
  private String location;

  @JsonProperty("lhinCode")
  @Schema(description = "Master number lhinCode", example = "7")
  private Integer lhinCode;

  @JsonProperty("facilityNumber")
  @Schema(description = "Master number facilityNumber", example = "1")
  private Integer facilityNumber;

  public int getMasterNumber() {
    return masterNumber;
  }

  public void setMasterNumber(int masterNumber) {
    this.masterNumber = masterNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getLhinCode() {
    return lhinCode;
  }

  public void setLhinCode(Integer lhinCode) {
    this.lhinCode = lhinCode;
  }

  public Integer getFacilityNumber() {
    return facilityNumber;
  }

  public void setFacilityNumber(Integer facilityNumber) {
    this.facilityNumber = facilityNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MasterNumberDto)) {
      return false;
    }
    MasterNumberDto that = (MasterNumberDto) o;
    return getMasterNumber() == that.getMasterNumber()
        && Objects.equals(getName(), that.getName())
        && Objects.equals(getType(), that.getType())
        && Objects.equals(getLocation(), that.getLocation())
        && Objects.equals(getLhinCode(), that.getLhinCode())
        && Objects.equals(getFacilityNumber(), that.getFacilityNumber());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMasterNumber(), getName(), getType(), getLocation(), getLhinCode(),
        getFacilityNumber());
  }
}
