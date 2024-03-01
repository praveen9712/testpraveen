
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * Schedule availability model object.
 * </p>
 * <p>
 * This entity represents availabilities templates that have been applied along side suggestion
 * templates.
 * </p>
 *
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Schedule availability data transfer object model")
public class AvailabilityDto {

  @JsonProperty("officeId")
  @Schema(description = "The ID of the office associated with this Availability DTO", example = "1")
  private int officeId;

  @JsonProperty("startTime")
  @Schema(description = "Availability start time as 24-hour number", example = "1400")
  private int startTime;

  @JsonProperty("endTime")
  @Schema(description = "Availability end time as 24-hour number", example = "1415")
  private int endTime;

  /**
   * The ID of the office associated with this Availability DTO
   *
   * @documentationExample 1
   *
   * @return Office ID
   */
  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  /**
   * Availability start time as 24-hour number
   *
   * @documentationExample 1400
   *
   * @return Time stamp
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * Availability end time as 24-hour number
   *
   * @documentationExample 1415
   *
   * @return Time stamp
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + this.officeId;
    hash = 37 * hash + this.startTime;
    hash = 37 * hash + this.endTime;
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
    final AvailabilityDto other = (AvailabilityDto) obj;
    if (this.officeId != other.officeId) {
      return false;
    }
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    return true;
  }

}
