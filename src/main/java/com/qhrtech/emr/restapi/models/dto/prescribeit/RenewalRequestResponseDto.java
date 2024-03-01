
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestResponseType;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.joda.time.LocalDateTime;

/**
 * Renewal request response model object
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Renewal Request Response model object")
public class RenewalRequestResponseDto {

  @JsonProperty("id")
  @Schema(description = "Unique id of renewal request response", example = "221")
  private int id;

  @JsonProperty("renewalRequestId")
  @Schema(description = "Unique id of renewal request", example = "221")
  private int renewalRequestId;

  @JsonProperty("prescriptionMedicationId")
  @Schema(description = "Prescription medication id", example = "221")
  private Integer prescriptionMedicationId;

  @JsonProperty("responseType")
  @Schema(description = "Response Type",
      example = "UNDER_REVIEW, ACCEPTED_WITH_CHANGES, ACCEPTED, DENIED")
  private RenewalRequestResponseType responseType;

  @JsonProperty("createdById")
  @Schema(description = "Created by user id", example = "221")
  private Integer createdById;

  @JsonProperty("denyReason")
  @Schema(description = "Reason for denial of renewal request", example = "Denial reason")
  private String denyReason;

  @JsonProperty("additionalComments")
  @Schema(description = "Additional comments on renewal request",
      example = "Some additional comments on renewal requests")
  private String additionalComments;

  @JsonProperty("createdDateUtc")
  @Schema(description = "Created date time in UTC",
      example = "2021-03-18T22:49:00.782Z")
  private LocalDateTime createdDateUtc;


  /**
   * A unique renewal request response ID.
   *
   * @return Renewal Request Response ID
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * A unique Renewal Request ID.
   *
   * @return Renewal request ID
   * @documentationExample 1
   */
  public int getRenewalRequestId() {
    return renewalRequestId;
  }

  public void setRenewalRequestId(int renewalRequestId) {
    this.renewalRequestId = renewalRequestId;
  }

  /**
   * A prescription medication ID.
   *
   * @return Prescription medication ID
   * @documentationExample 1
   */
  public Integer getPrescriptionMedicationId() {
    return prescriptionMedicationId;
  }

  public void setPrescriptionMedicationId(Integer prescriptionMedicationId) {
    this.prescriptionMedicationId = prescriptionMedicationId;
  }

  /**
   * Response type for renewal request response.
   *
   * @return Renewal request response Type
   * @documentationExample UNDER_REVIEW, ACCEPTED_WITH_CHANGES, ACCEPTED, DENIED
   */
  public RenewalRequestResponseType getResponseType() {
    return responseType;
  }

  public void setResponseType(RenewalRequestResponseType responseType) {
    this.responseType = responseType;
  }

  /**
   * Created by User Id
   *
   * @return User ID
   * @documentationExample 1
   */
  public Integer getCreatedById() {
    return createdById;
  }

  public void setCreatedById(Integer createdById) {
    this.createdById = createdById;
  }

  /**
   * Deny Reason for renewal request response
   *
   * @return Deny Reason
   * @documentationExample Deny Reason
   */
  public String getDenyReason() {
    return denyReason;
  }

  public void setDenyReason(String denyReason) {
    this.denyReason = denyReason;
  }

  /**
   * Any additional comments about the renewal request
   *
   * @return Additional Comments
   * @documentationExample Additional comments
   */
  public String getAdditionalComments() {
    return additionalComments;
  }

  public void setAdditionalComments(String additionalComments) {
    this.additionalComments = additionalComments;
  }

  /**
   * Created date time in UTC
   *
   * @return DateTime in UTC
   * @documentationExample "2017-11-29T00:00:00.000"
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDateUtc() {
    return createdDateUtc;
  }

  public void setCreatedDateUtc(LocalDateTime createdDateUtc) {
    this.createdDateUtc = createdDateUtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RenewalRequestResponseDto that = (RenewalRequestResponseDto) o;

    if (id != that.id) {
      return false;
    }
    if (renewalRequestId != that.renewalRequestId) {
      return false;
    }
    if (!Objects.equals(createdById, that.createdById)) {
      return false;
    }
    if (!Objects.equals(prescriptionMedicationId, that.prescriptionMedicationId)) {
      return false;
    }
    if (responseType != that.responseType) {
      return false;
    }
    if (!Objects.equals(denyReason, that.denyReason)) {
      return false;
    }
    if (!Objects.equals(additionalComments, that.additionalComments)) {
      return false;
    }
    return Objects.equals(createdDateUtc, that.createdDateUtc);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + renewalRequestId;
    result =
        31 * result + (prescriptionMedicationId != null ? prescriptionMedicationId.hashCode() : 0);
    result = 31 * result + (responseType != null ? responseType.hashCode() : 0);
    result = 31 * result + (createdById != null ? createdById.hashCode() : 0);
    result = 31 * result + (denyReason != null ? denyReason.hashCode() : 0);
    result = 31 * result + (additionalComments != null ? additionalComments.hashCode() : 0);
    result = 31 * result + (createdDateUtc != null ? createdDateUtc.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("RenewalRequestResponseDto{");
    sb.append("id=").append(id);
    sb.append(", renewalRequestId=").append(renewalRequestId);
    sb.append(", prescriptionMedicationId=").append(prescriptionMedicationId);
    sb.append(", responseType=").append(responseType);
    sb.append(", createdById=").append(createdById);
    sb.append(", denyReason='").append(denyReason).append('\'');
    sb.append(", additionalComments='").append(additionalComments).append('\'');
    sb.append(", createdDateUtc=").append(createdDateUtc);
    sb.append('}');
    return sb.toString();
  }
}
