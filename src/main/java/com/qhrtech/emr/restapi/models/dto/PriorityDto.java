
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Appointment Priority object.
 *
 * @see com.qhrtech.emr.accuro.model.scheduling.Priority
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Appointment Priority data transfer object model")
public class PriorityDto {

  @JsonProperty("priorityId")
  @Schema(description = "The unique id for the priority", example = "1")
  private int priorityId;

  @JsonProperty("priorityName")
  @Schema(description = "The name for the priority", example = "Urgent")
  private String priorityName;

  /**
   * The unique ID for the priority
   *
   * @documentationExample 1
   *
   * @return The Priority ID
   */
  public int getPriorityId() {
    return priorityId;
  }

  public void setPriorityId(int priorityId) {
    this.priorityId = priorityId;
  }

  /**
   * A name for the Priority object
   *
   * @documentationExample Urgent
   *
   * @return A priority name
   */
  public String getPriorityName() {
    return priorityName;
  }

  public void setPriorityName(String priorityName) {
    this.priorityName = priorityName;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + this.priorityId;
    hash = 89 * hash + Objects.hashCode(this.priorityName);
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
    final PriorityDto other = (PriorityDto) obj;
    if (this.priorityId != other.priorityId) {
      return false;
    }
    if (!Objects.equals(this.priorityName, other.priorityName)) {
      return false;
    }
    return true;
  }

}
