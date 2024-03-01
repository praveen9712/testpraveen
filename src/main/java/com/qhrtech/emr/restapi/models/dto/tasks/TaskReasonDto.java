
package com.qhrtech.emr.restapi.models.dto.tasks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Data Transfer Object for task reason")
public class TaskReasonDto {

  @JsonProperty("id")
  private int id;

  @NotNull(message = "The task reason can not be null.")
  @Size(max = 100, message = "The task reason can not exceed 100 characters.")
  @JsonProperty("taskReason")
  @Schema(description = "The task reason", example = "daily task")
  private String taskReason;

  @JsonProperty("officeId")
  private Integer officeId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTaskReason() {
    return taskReason;
  }

  public void setTaskReason(String taskReason) {
    this.taskReason = taskReason;
  }

  public Integer getOfficeId() {
    return officeId;
  }

  public void setOfficeId(Integer officeId) {
    this.officeId = officeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TaskReasonDto)) {
      return false;
    }
    TaskReasonDto that = (TaskReasonDto) o;
    if (getId() != that.getId()) {
      return false;
    }
    if (getTaskReason() != null ? !getTaskReason().equals(that.getTaskReason())
        : that.getTaskReason() != null) {
      return false;
    }
    return getOfficeId() != null ? getOfficeId().equals(that.getOfficeId())
        : that.getOfficeId() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getTaskReason() != null ? getTaskReason().hashCode() : 0);
    result = 31 * result + (getOfficeId() != null ? getOfficeId().hashCode() : 0);
    return result;
  }

}
