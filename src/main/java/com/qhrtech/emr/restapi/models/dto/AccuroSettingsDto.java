
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Accuro Settings data transfer object model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Data Transfer Object for Accuro Settings")
public class AccuroSettingsDto {

  @JsonProperty("mode")
  @Schema(description = "The Mode Accuro is running in.", example = "Full")
  private String mode;

  @JsonProperty("timeZone")
  @Schema(description = "Time zone settings for Accuro", example = "Canada/Eastern")
  private String timeZone;

  @JsonProperty("province")
  @Schema(description = "The province the Accuro instance is configured for.", example = "ON")
  private String province;

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccuroSettingsDto that = (AccuroSettingsDto) o;
    return Objects.equals(mode, that.mode) && Objects.equals(timeZone,
        that.timeZone) && Objects.equals(province, that.province);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, timeZone, province);
  }

  @Override
  public String toString() {
    return "AccuroSettingsDto{"
        + "mode='" + mode + '\''
        + ", timeZone='" + timeZone + '\''
        + ", province='" + province + '\''
        + '}';
  }
}
