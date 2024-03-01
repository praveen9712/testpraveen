
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.Size;

/**
 * The Eprescribe Job Task data transfer object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Eprescribe Job Task object.")
public class EprescribeJobTaskDto implements Serializable {

  @JsonProperty("id")
  @Schema(description = "Eprescribe Job Task Id", example = "12")
  private int erxJobTaskId;

  @JsonProperty("erxJobId")
  @Schema(description = "Eprescribe job Id", example = "12")
  private int erxJobId;

  @JsonProperty("taskId")
  @Schema(description = "Task Id", example = "12")
  @Size(max = 50)
  private String taskId;

  @JsonProperty("taskType")
  @Schema(description = "Task Type")
  @Size(max = 50)
  private String taskType;

  @JsonProperty("taskUuid")
  @Schema(description = "Task UUID")
  private UUID taskUuid;

  /**
   * Eprescribe Job Task ID
   *
   * @return Eprescribe Job Task ID
   * @documentationExample 12
   */
  public int getErxJobTaskId() {
    return erxJobTaskId;
  }

  public void setErxJobTaskId(int erxJobTaskId) {
    this.erxJobTaskId = erxJobTaskId;
  }

  /**
   * Eprescribe Job ID
   *
   * @return Eprescribe Job ID
   * @documentationExample 12
   */
  public int getErxJobId() {
    return erxJobId;
  }

  public void setErxJobId(int erxJobId) {
    this.erxJobId = erxJobId;
  }

  /**
   * Task ID of the Eprescribe Job Task
   *
   * @return Task Id of the Eprescribe Job Task
   * @documentationExample 12
   */
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  /**
   * Task type of the Eprescribe Job Task
   *
   * @return Task Type of the Eprescribe Job Task
   * @documentationExample 12
   */
  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  /**
   * Task uuid of the Eprescribe Job Task
   *
   * @return Task uuid of the Eprescribe Job Task
   */
  public UUID getTaskUuid() {
    return taskUuid;
  }

  public void setTaskUuid(UUID taskUuid) {
    this.taskUuid = taskUuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EprescribeJobTaskDto that = (EprescribeJobTaskDto) o;

    if (erxJobTaskId != that.erxJobTaskId) {
      return false;
    }
    if (erxJobId != that.erxJobId) {
      return false;
    }
    if (!Objects.equals(taskId, that.taskId)) {
      return false;
    }
    if (!Objects.equals(taskType, that.taskType)) {
      return false;
    }
    return Objects.equals(taskUuid, that.taskUuid);
  }

  @Override
  public int hashCode() {
    int result = erxJobTaskId;
    result = 31 * result + erxJobId;
    result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
    result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
    result = 31 * result + (taskUuid != null ? taskUuid.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("EprescribeJobTaskDto{");
    sb.append("erxJobTaskId=").append(erxJobTaskId);
    sb.append(", erxJobId=").append(erxJobId);
    sb.append(", taskId='").append(taskId).append('\'');
    sb.append(", taskType='").append(taskType).append('\'');
    sb.append(", taskUuid=").append(taskUuid);
    sb.append('}');
    return sb.toString();
  }
}
