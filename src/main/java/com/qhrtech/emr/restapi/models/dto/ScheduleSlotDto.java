
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Objects;

/**
 * Schedule Timeslot representing a range of time in the scheduler. Immutable implementation that
 * should follow the fields expected of a class that would implement ScheduleTime.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Schedule Timeslot data transfer object model representing a range of "
    + "time in the scheduler.")
public class ScheduleSlotDto {

  @JsonProperty("date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "Schedule slot date.", example = "2017-11-29", type = "string")
  private Calendar date;

  @JsonProperty("subColumn")
  @Schema(description = "Schedule slot sub column.", example = "0")
  private int subColumn;

  @JsonProperty("startTime")
  @Schema(description = "Schedule slot start time in 24 hours.", example = "1300")
  private int startTime;

  @JsonProperty("endTime")
  @Schema(description = "Schedule slot end time in 24 hours.", example = "1315")
  private int endTime;

  @JsonProperty("providerId")
  @Schema(description = "Provider ID associated with the schedule slot", example = "1")
  private Integer providerId;

  @JsonProperty("resourceId")
  @Schema(description = "Resource ID associated with the schedule slot.", example = "1")
  private Integer resourceId;

  /**
   * Schedule slot date.
   *
   * @return Date
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    this.date = date;
  }

  /**
   * Schedule slot sub column.
   *
   * @documentationExample 0
   *
   * @return Sub column
   */
  public int getSubColumn() {
    return subColumn;
  }

  public void setSubColumn(int subColumn) {
    this.subColumn = subColumn;
  }

  /**
   * Schedule slot start time in 24 hours.
   *
   * @documentationExample 1300
   *
   * @return Start time
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * Schedule slot end time in 24 hours.
   *
   * @documentationExample 1315
   *
   * @return End time
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  /**
   * Provider ID associated with the schedule slot
   *
   * @documentationExample 1
   *
   * @return Provider ID
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * Resource ID associated with the schedule slot.
   *
   * @documentationExample 1
   *
   * @return Resource ID
   */
  public Integer getResourceId() {
    return resourceId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + Objects.hashCode(this.date);
    hash = 83 * hash + this.subColumn;
    hash = 83 * hash + this.startTime;
    hash = 83 * hash + this.endTime;
    hash = 83 * hash + Objects.hashCode(this.providerId);
    hash = 83 * hash + Objects.hashCode(this.resourceId);
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
    final ScheduleSlotDto other = (ScheduleSlotDto) obj;
    if (this.subColumn != other.subColumn) {
      return false;
    }
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.providerId, other.providerId)) {
      return false;
    }
    if (!Objects.equals(this.resourceId, other.resourceId)) {
      return false;
    }
    return true;
  }

}
