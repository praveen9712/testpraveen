
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTime2Range;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Erx cancel request Data transfer object")
public class ErxCancelRequestsDto {

  @JsonProperty("localId")
  @Schema(description = "The local id of the erx cancel request", example = "1")
  private int localId;

  @NotNull
  @JsonProperty("externalId")
  @Schema(description = "The external id of the erx cancel request",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID externalId;

  @JsonProperty("prescriptionMedicationId")
  @Schema(description = "Prescription medication id", example = "1")
  private Integer prescriptionMedicationId;

  @JsonProperty("contactId")
  @Schema(description = "Contact id", example = "1")
  private Integer contactId;

  @JsonProperty("eprescribeTaskId")
  @Schema(description = "Eprescribe task id", example = "1")
  private Integer eprescribeTaskId;

  @JsonProperty("created")
  @Schema(description = "Created date time", example = "2021-02-21T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime created;

  @JsonProperty("lastUpdated")
  @Schema(description = "The date time of when record was last updated. Read only field. ",
      example = "2021-05-210T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime lastUpdated;

  @NotNull
  @JsonProperty("providerId")
  @Schema(description = "Provider Id", example = "1")
  private Integer providerId;

  @NotNull
  @JsonProperty("patientId")
  @Schema(description = "Patient id", example = "1")
  private Integer patientId;

  @Size(max = 50)
  @JsonProperty("deliveryMethod")
  @Schema(description = "The delivery method for cancel request")
  private String deliveryMethod;

  @NotNull
  @JsonProperty("userId")
  @Schema(description = "User Id", example = "1")
  private Integer userId;

  @Size(max = 50)
  @JsonProperty("requestEvent")
  @Schema(description = "Request event for cancel request")
  private String requestEvent;

  @Size(max = 100)
  @NotNull
  @JsonProperty("requestReasonCode")
  @Schema(description = "Request reason code for cancel request")
  private String requestReasonCode;

  @Size(max = 2000)
  @JsonProperty("requestReasonText")
  @Schema(description = "Request reason for cancel request")
  private String requestReasonText;

  @JsonProperty("dateRead")
  @Schema(description = "Cancel request date read")
  @CheckLocalDateTime2Range
  private LocalDateTime dateRead;

  /**
   * Date time when request was read.
   *
   * @documentationExample 2021-05-210T00:00:00.000
   *
   * @return {@link LocalDateTime} Date time
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDateRead() {
    return dateRead;
  }

  public void setDateRead(LocalDateTime dateRead) {
    this.dateRead = dateRead;
  }

  /**
   * Local id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} local id
   */
  public int getLocalId() {
    return localId;
  }

  public void setLocalId(int localId) {
    this.localId = localId;
  }

  /**
   * Local id of the eprescription request
   *
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   *
   * @return {@link UUID} uuid
   */
  public UUID getExternalId() {
    return externalId;
  }

  public void setExternalId(UUID externalId) {
    this.externalId = externalId;
  }

  /**
   * Prescription medication id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} prescriptionMedicationId
   */
  public Integer getPrescriptionMedicationId() {
    return prescriptionMedicationId;
  }

  public void setPrescriptionMedicationId(Integer prescriptionMedicationId) {
    this.prescriptionMedicationId = prescriptionMedicationId;
  }

  /**
   * Contact id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} contact id
   */
  public Integer getContactId() {
    return contactId;
  }

  public void setContactId(Integer contactId) {
    this.contactId = contactId;
  }

  /**
   * Eprescribe task id
   *
   * @documentationExample 12
   *
   * @return {@link Integer} task id
   */
  public Integer getEprescribeTaskId() {
    return eprescribeTaskId;
  }

  public void setEprescribeTaskId(Integer eprescribeTaskId) {
    this.eprescribeTaskId = eprescribeTaskId;
  }

  /**
   * Created date of the eprescription request
   *
   * @documentationExample 2021-05-210T00:00:00.000
   *
   * @return {@link LocalDateTime} created date
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  /**
   * Last updated date of the eprescription request. Read only field.
   *
   * @documentationExample 2021-05-210T00:00:00.000
   *
   * @return {@link LocalDateTime} last updated date
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /**
   * Provider id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} provider id
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * Patient id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} provider id
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Delivery method of the eprescription request
   *
   * @documentationExample Delivery method
   *
   * @return {@link String} delivery method
   */
  public String getDeliveryMethod() {
    return deliveryMethod;
  }

  public void setDeliveryMethod(String deliveryMethod) {
    this.deliveryMethod = deliveryMethod;
  }

  /**
   * User id of the eprescription request
   *
   * @documentationExample 12
   *
   * @return {@link Integer} user id
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * Request event of the eprescription request
   *
   * @documentationExample request event
   *
   * @return {@link String} request event
   */
  public String getRequestEvent() {
    return requestEvent;
  }

  public void setRequestEvent(String requestEvent) {
    this.requestEvent = requestEvent;
  }

  /**
   * Request reason code of the eprescription request
   *
   * @documentationExample request reason code
   *
   * @return {@link String} request reason code
   */
  public String getRequestReasonCode() {
    return requestReasonCode;
  }

  public void setRequestReasonCode(String requestReasonCode) {
    this.requestReasonCode = requestReasonCode;
  }

  /**
   * Request reason text of the eprescription request
   *
   * @documentationExample request reason text
   *
   * @return {@link String} request reason text
   */
  public String getRequestReasonText() {
    return requestReasonText;
  }

  public void setRequestReasonText(String requestReasonText) {
    this.requestReasonText = requestReasonText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ErxCancelRequestsDto that = (ErxCancelRequestsDto) o;

    if (localId != that.localId) {
      return false;
    }
    if (!Objects.equals(patientId, that.patientId)) {
      return false;
    }
    if (!Objects.equals(userId, that.userId)) {
      return false;
    }
    if (!Objects.equals(externalId, that.externalId)) {
      return false;
    }
    if (!Objects.equals(prescriptionMedicationId, that.prescriptionMedicationId)) {
      return false;
    }
    if (!Objects.equals(contactId, that.contactId)) {
      return false;
    }
    if (!Objects.equals(eprescribeTaskId, that.eprescribeTaskId)) {
      return false;
    }
    if (!Objects.equals(created, that.created)) {
      return false;
    }
    if (!Objects.equals(lastUpdated, that.lastUpdated)) {
      return false;
    }
    if (!Objects.equals(providerId, that.providerId)) {
      return false;
    }
    if (!Objects.equals(deliveryMethod, that.deliveryMethod)) {
      return false;
    }
    if (!Objects.equals(requestEvent, that.requestEvent)) {
      return false;
    }
    if (!Objects.equals(requestReasonCode, that.requestReasonCode)) {
      return false;
    }
    if (!Objects.equals(dateRead, that.dateRead)) {
      return false;
    }
    return Objects.equals(requestReasonText, that.requestReasonText);
  }

  @Override
  public int hashCode() {
    int result = localId;
    result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
    result =
        31 * result + (prescriptionMedicationId != null ? prescriptionMedicationId.hashCode() : 0);
    result = 31 * result + (contactId != null ? contactId.hashCode() : 0);
    result = 31 * result + (eprescribeTaskId != null ? eprescribeTaskId.hashCode() : 0);
    result = 31 * result + (created != null ? created.hashCode() : 0);
    result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
    result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (deliveryMethod != null ? deliveryMethod.hashCode() : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (requestEvent != null ? requestEvent.hashCode() : 0);
    result = 31 * result + (requestReasonCode != null ? requestReasonCode.hashCode() : 0);
    result = 31 * result + (requestReasonText != null ? requestReasonText.hashCode() : 0);
    result = 31 * result + (dateRead != null ? dateRead.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ErxCancelRequestsDto{");
    sb.append("localId=").append(localId);
    sb.append(", externalId=").append(externalId);
    sb.append(", prescriptionMedicationId=").append(prescriptionMedicationId);
    sb.append(", contactId=").append(contactId);
    sb.append(", eprescribeTaskId=").append(eprescribeTaskId);
    sb.append(", created=").append(created);
    sb.append(", lastUpdated=").append(lastUpdated);
    sb.append(", providerId='").append(providerId).append('\'');
    sb.append(", patientId=").append(patientId);
    sb.append(", deliveryMethod='").append(deliveryMethod).append('\'');
    sb.append(", userId=").append(userId);
    sb.append(", requestEvent='").append(requestEvent).append('\'');
    sb.append(", requestReasonCode='").append(requestReasonCode).append('\'');
    sb.append(", requestReasonText='").append(requestReasonText).append('\'');
    sb.append(", dateRead='").append(dateRead).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
