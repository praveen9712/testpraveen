
package com.qhrtech.emr.restapi.models.dto.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import com.qhrtech.emr.restapi.validators.CheckNull;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Data Transfer Object for user task")
public class UserTaskDto {

  @JsonProperty("id")
  @Schema(description = "The task identity", example = "10")
  private int id;

  @JsonProperty("assignedToUserIds")
  @Schema(description = "All users assigned to  task", example = "[1,2,3]")
  @Valid
  private Set<@CheckNull Integer> assignedToUserIds;

  @JsonProperty("assignedToRoleIds")
  @Schema(description = "All roles assigned to  task", example = "[1,2,3]")
  @Valid
  private Set<@CheckNull Integer> assignedToRoleIds;

  @JsonProperty("patientId")
  @Schema(description = "Patient assigned to  task", example = "[1,2,3]")
  private Integer patientId;

  @NotNull(message = "The task reason can not be null.")
  @Size(max = 100, message = "The task reason can not exceed 100 characters.")
  @JsonProperty("reason")
  @Schema(description = "The task reason", example = "daily task")
  private String reason;

  @Size(max = 2048, message = "The task notes can not exceed 2048 characters.")
  @JsonProperty("notes")
  @Schema(description = "Task notes", example = "Finish up prior to patient next visit")
  private String notes;

  @JsonProperty("priority")
  @Schema(description = "Task priority level", example = "Urgent")
  private Priority priority;

  @JsonProperty("completed")
  @Schema(description = "Whether task completed", example = "True")
  private boolean completed;

  @JsonProperty("completedDate")
  @Schema(description = "Task completed date", example = "2020-10-10")
  private LocalDateTime completedDate;

  @Size(max = 100, message = "Can not exceed 100 characters.")
  @JsonProperty("completedBy")
  @Schema(description = "Task completed by username", example = "Super user")
  private String completedBy;

  @Size(max = 100, message = "Can not exceed 100 characters.")
  @JsonProperty("createdBy")
  @Schema(description = "Task created by username", example = "Super user")
  private String createdBy;

  @JsonProperty("createdById")
  @Schema(description = "Task created by identity", example = "101")
  private Integer createdById;

  @JsonProperty("createdOn")
  @Schema(description = "Task created date", example = "2020-10-10")
  private LocalDateTime createdOn;

  @NotNull(message = "The office id assigned to can not be null.")
  @JsonProperty("officeIdAssignedTo")
  @Schema(description = "Task assigned to users or roles under which office", example = "Eastern")
  private int officeIdAssignedTo;

  @NotNull(message = "The office id assigned to can not be null.")
  @JsonProperty("officeIdAssignedFrom")
  @Schema(description = "Task assigned from which office", example = "Western")
  private int officeIdAssignedFrom;

  @JsonProperty("taskDueDate")
  @Schema(description = "Task due date", example = "2020-10-10")
  @NotNull(message = "taskDueDate cannot be null.")
  @CheckLocalDateRange
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate taskDueDate;

  @JsonProperty("deleted")
  @Schema(description = "Whether task deleted", example = "True")
  private boolean deleted;

  /**
   * The Task id
   *
   * @return The id
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The Set of users assigned to the task
   *
   * @return The Set of {@link Integer}
   * @documentationExample 123
   */
  public Set<Integer> getAssignedToUserIds() {
    return assignedToUserIds;
  }

  public void setAssignedToUserIds(Set<Integer> assignedToUserIds) {
    this.assignedToUserIds = assignedToUserIds;
  }

  /**
   * The Set of roles assigned to the task
   *
   * @return The Set of {@link Integer}
   * @documentationExample 123
   */
  public Set<Integer> getAssignedToRoleIds() {
    return assignedToRoleIds;
  }

  public void setAssignedToRoleIds(Set<Integer> assignedToRoleIds) {
    this.assignedToRoleIds = assignedToRoleIds;
  }

  /**
   * The Id of Patient assigned to the Task
   *
   * @return The patientId
   * @documentationExample 1
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * The Task reason
   *
   * @return The reason
   * @documentationExample Daily Task
   */
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  /**
   * The Task notes
   *
   * @return The notes
   * @documentationExample Finishup prior to patient next visit.
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The Task priority level
   *
   * @return The {@link Priority}
   * @documentationExample Urgent
   */
  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  /**
   * The Indication whether task completed or not
   *
   * @return {@code true} if completed or {@code false} if not completed
   * @documentationExample True
   */
  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * The Task completed date
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(LocalDateTime completedDate) {
    this.completedDate = completedDate;
  }

  /**
   * The Username of the task completed
   *
   * @return The {@link String} completedBy
   * @documentationExample Super user
   */
  public String getCompletedBy() {
    return completedBy;
  }

  public void setCompletedBy(String completedBy) {
    this.completedBy = completedBy;
  }

  /**
   * The Username of the task created
   *
   * @return The {@link String} createdBy
   * @documentationExample Super user
   */
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * The Task created by id
   *
   * @return {@link Integer} createdBYId
   * @documentationExample 121
   */
  public Integer getCreatedById() {
    return createdById;
  }

  public void setCreatedById(Integer createdById) {
    this.createdById = createdById;
  }

  /**
   * The Task created date
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }

  /**
   * The Task assigned to user or user roles of which office
   *
   * @return The officeIdAssignedTo
   * @documentationExample 1211
   */
  public int getOfficeIdAssignedTo() {
    return officeIdAssignedTo;
  }

  public void setOfficeIdAssignedTo(int officeIdAssignedTo) {
    this.officeIdAssignedTo = officeIdAssignedTo;
  }

  /**
   * The Task Assigned from which office
   *
   * @return The officeIdAssignedFrom
   * @documentationExample 1212
   */
  public int getOfficeIdAssignedFrom() {
    return officeIdAssignedFrom;
  }

  public void setOfficeIdAssignedFrom(int officeIdAssignedFrom) {
    this.officeIdAssignedFrom = officeIdAssignedFrom;
  }

  /**
   * The Task due date
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDate getTaskDueDate() {
    return taskDueDate;
  }

  public void setTaskDueDate(LocalDate taskDueDate) {
    this.taskDueDate = taskDueDate;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserTaskDto)) {
      return false;
    }
    UserTaskDto that = (UserTaskDto) o;
    if (getId() != that.getId()) {
      return false;
    }
    if (isCompleted() != that.isCompleted()) {
      return false;
    }
    if (getOfficeIdAssignedTo() != that.getOfficeIdAssignedTo()) {
      return false;
    }
    if (getOfficeIdAssignedFrom() != that.getOfficeIdAssignedFrom()) {
      return false;
    }
    if (getAssignedToUserIds() != null ? !getAssignedToUserIds().equals(that.getAssignedToUserIds())
        : that.getAssignedToUserIds() != null) {
      return false;
    }
    if (getAssignedToRoleIds() != null ? !getAssignedToRoleIds().equals(that.getAssignedToRoleIds())
        : that.getAssignedToRoleIds() != null) {
      return false;
    }
    if (getPatientId() != null ? !getPatientId().equals(that.getPatientId())
        : that.getPatientId() != null) {
      return false;
    }
    if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null) {
      return false;
    }
    if (getNotes() != null ? !getNotes().equals(that.getNotes()) : that.getNotes() != null) {
      return false;
    }
    if (getPriority() != that.getPriority()) {
      return false;
    }
    if (getCompletedDate() != null ? !getCompletedDate().equals(that.getCompletedDate())
        : that.getCompletedDate() != null) {
      return false;
    }
    if (getCompletedBy() != null ? !getCompletedBy().equals(that.getCompletedBy())
        : that.getCompletedBy() != null) {
      return false;
    }
    if (getCreatedBy() != null ? !getCreatedBy().equals(that.getCreatedBy())
        : that.getCreatedBy() != null) {
      return false;
    }
    if (getCreatedById() != null ? !getCreatedById().equals(that.getCreatedById())
        : that.getCreatedById() != null) {
      return false;
    }
    if (getCreatedOn() != null ? !getCreatedOn().equals(that.getCreatedOn())
        : that.getCreatedOn() != null) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    return getTaskDueDate() != null ? getTaskDueDate().equals(that.getTaskDueDate())
        : that.getTaskDueDate() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getAssignedToUserIds() != null ? getAssignedToUserIds().hashCode() : 0);
    result = 31 * result + (getAssignedToRoleIds() != null ? getAssignedToRoleIds().hashCode() : 0);
    result = 31 * result + (getPatientId() != null ? getPatientId().hashCode() : 0);
    result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
    result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
    result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
    result = 31 * result + (isCompleted() ? 1 : 0);
    result = 31 * result + (getCompletedDate() != null ? getCompletedDate().hashCode() : 0);
    result = 31 * result + (getCompletedBy() != null ? getCompletedBy().hashCode() : 0);
    result = 31 * result + (getCreatedBy() != null ? getCreatedBy().hashCode() : 0);
    result = 31 * result + (getCreatedById() != null ? getCreatedById().hashCode() : 0);
    result = 31 * result + (getCreatedOn() != null ? getCreatedOn().hashCode() : 0);
    result = 31 * result + getOfficeIdAssignedTo();
    result = 31 * result + getOfficeIdAssignedFrom();
    result = 31 * result + (getTaskDueDate() != null ? getTaskDueDate().hashCode() : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UserTaskDto{");
    sb.append("id=").append(id);
    sb.append(", assignedToUserIds=").append(assignedToUserIds);
    sb.append(", assignedToRoleIds=").append(assignedToRoleIds);
    sb.append(", patientId=").append(patientId);
    sb.append(", reason='").append(reason).append('\'');
    sb.append(", notes='").append(notes).append('\'');
    sb.append(", priority=").append(priority);
    sb.append(", completed=").append(completed);
    sb.append(", completedDate=").append(completedDate);
    sb.append(", completedBy='").append(completedBy).append('\'');
    sb.append(", createdBy='").append(createdBy).append('\'');
    sb.append(", createdById=").append(createdById);
    sb.append(", createdOn=").append(createdOn);
    sb.append(", officeIdAssignedTo=").append(officeIdAssignedTo);
    sb.append(", officeIdAssignedFrom=").append(officeIdAssignedFrom);
    sb.append(", taskDueDate=").append(taskDueDate);
    sb.append(", deleted=").append(deleted);
    sb.append('}');
    return sb.toString();
  }


}
