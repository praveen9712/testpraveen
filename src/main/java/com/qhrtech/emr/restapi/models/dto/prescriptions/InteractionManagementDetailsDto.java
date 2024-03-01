
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.CodeValueDto;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@Schema(description = "The interaction management details data transfer object")
public class InteractionManagementDetailsDto {

  @JsonProperty("interactionId")
  @Schema(description = "The id of the contraindication (interaction)", example = "1")
  private int interactionId;

  @JsonProperty("userName")
  @Schema(description = "The user name managed this contraindication (interaction). </p>"
      + "This is not a provider's name.",
      example = "JohnDoe")
  private String userName;

  @JsonProperty("managedTime")
  @Schema(description = "The date and time the contraindication was managed",
      example = "2018-07-13T00:00:00.000")
  private LocalDateTime managedTime;

  @JsonProperty("managementAction")
  @Schema(description = "The management action", example = "Continued - See Comments")
  private CodeValueDto managementAction;

  @JsonProperty("applied")
  @Schema(description = "The flag if this is applied", example = "true")
  private boolean applied;

  @JsonProperty("comments")
  @Schema(description = "The comments the user left",
      example = "THIS PATIENT SHOULD TRY TO TAKE THIS PILL.")
  private String comments;

  @JsonProperty("printedOnRx")
  @Schema(description = "The flag if the indication was printed on the prescription",
      example = "true")
  private boolean printedOnRx;

  @JsonProperty("userId")
  @Schema(description = "The user id managed this contraindication (interaction)",
      example = "1",
      nullable = true)
  private Integer userId;

  @JsonProperty("prescriptionId")
  @Schema(description = "The id of a managed prescription",
      example = "1",
      nullable = true)
  private Integer prescriptionId;

  /**
   * The id of the contraindication (interaction).
   *
   * @documentationExample 1
   *
   * @return
   *
   * @see InteractionDto
   */
  public int getInteractionId() {
    return interactionId;
  }

  public void setInteractionId(int interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * The user name managed this contraindication (interaction).
   * <p>
   * This is not a provider's name.
   *
   * @documentationExample JohnDoe
   *
   * @return
   */
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * The date and time the contraindication was managed.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getManagedTime() {
    return managedTime;
  }

  public void setManagedTime(LocalDateTime managedTime) {
    this.managedTime = managedTime;
  }

  /**
   * The management action.
   *
   * @documentationExample Continued - See Comments
   *
   * @return
   */
  public CodeValueDto getManagementAction() {
    return managementAction;
  }

  public void setManagementAction(CodeValueDto managementAction) {
    this.managementAction = managementAction;
  }

  /**
   * The flag if this is applied.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isApplied() {
    return applied;
  }

  public void setApplied(boolean applied) {
    this.applied = applied;
  }

  /**
   * The comments the user left.
   *
   * @documentationExample THIS PATIENT SHOULD TRY TO TAKE THIS PILL.
   *
   * @return
   */
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * The flag if the indication was printed on the prescription.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isPrintedOnRx() {
    return printedOnRx;
  }

  public void setPrintedOnRx(boolean printedOnRx) {
    this.printedOnRx = printedOnRx;
  }

  /**
   * The user id managed this contraindication (interaction). This id can be null.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * The id of a managed prescription. This id can be null.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(Integer prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InteractionManagementDetailsDto)) {
      return false;
    }

    InteractionManagementDetailsDto that = (InteractionManagementDetailsDto) o;

    if (getInteractionId() != that.getInteractionId()) {
      return false;
    }
    if (isApplied() != that.isApplied()) {
      return false;
    }
    if (isPrintedOnRx() != that.isPrintedOnRx()) {
      return false;
    }
    if (getUserName() != null ? !getUserName().equals(that.getUserName())
        : that.getUserName() != null) {
      return false;
    }
    if (getManagedTime() != null ? !getManagedTime().equals(that.getManagedTime())
        : that.getManagedTime() != null) {
      return false;
    }
    if (getManagementAction() != null ? !getManagementAction().equals(that.getManagementAction())
        : that.getManagementAction() != null) {
      return false;
    }
    if (getComments() != null ? !getComments().equals(that.getComments())
        : that.getComments() != null) {
      return false;
    }
    if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
      return false;
    }
    return getPrescriptionId() != null ? getPrescriptionId().equals(that.getPrescriptionId())
        : that.getPrescriptionId() == null;
  }

  @Override
  public int hashCode() {
    int result = getInteractionId();
    result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
    result = 31 * result + (getManagedTime() != null ? getManagedTime().hashCode() : 0);
    result = 31 * result + (getManagementAction() != null ? getManagementAction().hashCode() : 0);
    result = 31 * result + (isApplied() ? 1 : 0);
    result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
    result = 31 * result + (isPrintedOnRx() ? 1 : 0);
    result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
    result = 31 * result + (getPrescriptionId() != null ? getPrescriptionId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "InteractionManagementDetailsDto{"
        + "interactionId=" + interactionId
        + ", userName='" + userName + '\''
        + ", managedTime=" + managedTime
        + ", managementAction=" + managementAction
        + ", applied=" + applied
        + ", comments='" + comments + '\''
        + ", printedOnRx=" + printedOnRx
        + ", userId=" + userId
        + ", prescriptionId=" + prescriptionId
        + '}';
  }
}
