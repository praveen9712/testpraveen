
package com.qhrtech.emr.restapi.models.dto.waitlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Priority list data transfer object model")
public class ConsultPriorityDto {

  @JsonProperty("id")
  @Schema(description = "Unique identifier of the Priority list", example = "1")
  private int id;

  @JsonProperty("priorityName")
  @Schema(description = "The name of the consult priority ", example = "urgent")
  private String priorityName;

  @JsonProperty("priorityValue")
  @Schema(description = "Maximum wait for the priority in days ", example = "365")
  private int priorityValue; // In days

  @JsonProperty("active")
  @Schema(description = "Is the priority active", example = "true")
  private boolean active;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPriorityName() {
    return priorityName;
  }

  public void setPriorityName(String priorityName) {
    this.priorityName = priorityName;
  }

  public int getPriorityValue() {
    return priorityValue;
  }

  public void setPriorityValue(int priorityValue) {
    this.priorityValue = priorityValue;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConsultPriorityDto)) {
      return false;
    }
    ConsultPriorityDto that = (ConsultPriorityDto) o;
    if (id != that.id) {
      return false;
    }
    if (priorityValue != that.priorityValue) {
      return false;
    }
    if (active != that.active) {
      return false;
    }
    return priorityName != null ? priorityName.equals(that.priorityName)
        : that.priorityName == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (priorityName != null ? priorityName.hashCode() : 0);
    result = 31 * result + priorityValue;
    result = 31 * result + (active ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ConsultPriorityDto{"
        + "id=" + id
        + ", priorityName='" + priorityName
        + '\'' + ", priorityValue=" + priorityValue
        + ", active=" + active
        + '}';
  }
}
