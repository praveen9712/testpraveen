
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@Schema(description = "StatusHistory data transfer object model")
public class StatusHistoryDto {

  @JsonProperty("changeId")
  @Schema(description = "Change id", example = "12")
  private int changeId;

  @JsonProperty("prescriptionId")
  @Schema(description = "The prescription id.", example = "12")
  private int prescriptionId;

  @JsonProperty("orderStatus")
  @Schema(description = "The order status", example = "Order is on hold")
  @Size(max = 100)
  private String orderStatus;

  @JsonProperty("effectiveDate")
  @Schema(description = "The effective date", type = "string", example = "2018-07-13T00:00:00.000")
  private LocalDateTime effectiveDate;

  @JsonProperty("endDate")
  @Schema(description = "The end date", type = "string", example = "2018-07-13T00:00:00.000")
  private LocalDateTime endDate;

  @JsonProperty("authorizedBy")
  @Schema(description = "The name and the provider id of the provider who authorized the status",
      example = "Doctor, David [12340]")
  @Size(max = 100)
  private String authorizedBy;

  @JsonProperty("reason")
  @Schema(description = "The reason of the status", example = "HOLD Reasons no longer apply")
  @Size(max = 255)
  private String reason;

  @JsonProperty("comments")
  @Schema(description = "The comments of the status", example = "SAMPLE COMMENTS")
  @Size(max = 255)
  private String comments;

  /**
   * Change id.
   *
   * @documentationExample 12
   *
   * @return Integer
   */
  public int getChangeId() {
    return changeId;
  }

  public void setChangeId(int changeId) {
    this.changeId = changeId;
  }

  /**
   * Prescription id.
   *
   * @documentationExample 12
   *
   * @return Integer
   */
  public int getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(int prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * An order status.
   *
   * @documentationExample Order is on hold
   *
   * @return
   */
  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  /**
   * An effective date.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDateTime effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  /**
   * An end date.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  /**
   * A name and a provider id of a provider who authorized a status.
   *
   * @documentationExample Doctor, David [12340]
   *
   * @return
   */
  public String getAuthorizedBy() {
    return authorizedBy;
  }

  public void setAuthorizedBy(String authorizedBy) {
    this.authorizedBy = authorizedBy;
  }

  /**
   * A reson of a status.
   *
   * @documentationExample HOLD Reasons no longer apply
   *
   * @return
   */
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  /**
   * A comments of a status.
   *
   * @documentationExample SAMPLE COMMENTS
   *
   * @return
   */
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StatusHistoryDto that = (StatusHistoryDto) o;

    if (changeId != that.changeId) {
      return false;
    }
    if (prescriptionId != that.prescriptionId) {
      return false;
    }
    if (!Objects.equals(orderStatus, that.orderStatus)) {
      return false;
    }
    if (!Objects.equals(effectiveDate, that.effectiveDate)) {
      return false;
    }
    if (!Objects.equals(endDate, that.endDate)) {
      return false;
    }
    if (!Objects.equals(authorizedBy, that.authorizedBy)) {
      return false;
    }
    if (!Objects.equals(reason, that.reason)) {
      return false;
    }
    return Objects.equals(comments, that.comments);
  }

  @Override
  public int hashCode() {
    int result = changeId;
    result = 31 * result + prescriptionId;
    result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
    result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
    result = 31 * result + (authorizedBy != null ? authorizedBy.hashCode() : 0);
    result = 31 * result + (reason != null ? reason.hashCode() : 0);
    result = 31 * result + (comments != null ? comments.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("StatusHistoryDto{");
    sb.append("changeId=").append(changeId);
    sb.append(", prescriptionId=").append(prescriptionId);
    sb.append(", orderStatus='").append(orderStatus).append('\'');
    sb.append(", effectiveDate=").append(effectiveDate);
    sb.append(", endDate=").append(endDate);
    sb.append(", authorizedBy='").append(authorizedBy).append('\'');
    sb.append(", reason='").append(reason).append('\'');
    sb.append(", comments='").append(comments).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
