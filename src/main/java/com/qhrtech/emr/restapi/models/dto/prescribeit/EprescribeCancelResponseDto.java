
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Eprescribe Cancel Response Data transfer object")
public class EprescribeCancelResponseDto {

  @JsonProperty("id")
  @Schema(description = "The local id of the erx cancel request", example = "1")
  private int id;

  @NotNull
  @JsonProperty("externalId")
  @Schema(description = "The external id", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID externalId;

  @JsonProperty("eprescribeCancelRequestId")
  @Schema(description = "Eprescribe Cancel Request Id ", example = "1")
  private int eprescribeCancelRequestId;

  @NotNull
  @JsonProperty("patientId")
  @Schema(description = "Patient id", example = "1")
  private Integer patientId;

  @Size(max = 25, message = "Maximum length allowed is 25 characters.")
  @JsonProperty("responseStatus")
  private String responseStatus;

  @Size(max = 50)
  @JsonProperty("deliveryMethod")
  @Schema(description = "The delivery method for cancel request")
  private String deliveryMethod;

  @JsonProperty("createdDate")
  @Schema(description = "Created date time", example = "2021-02-21T00:00:00.000")
  private LocalDateTime createdDate;

  @JsonProperty("lastUpdated")
  @Schema(description = "The date time of when record was last updated ",
      example = "2021-05-210T00:00:00.000")
  private LocalDateTime lastUpdated;

  @Size(max = 50)
  @JsonProperty("responseEvent")
  @Schema(description = "The response event id", example = "String")
  private String responseEvent;

  @NotNull
  @JsonProperty("bundleId")
  @Schema(description = "The bundle id", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID bundleId;

  @NotNull
  @JsonProperty("taskId")
  @Schema(description = "The task id", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID taskId;

  @Size(max = 50)
  @NotNull
  @JsonProperty("pharmacyId")
  @Schema(description = "The pharmacy id", example = "String")
  private String pharmacyId;

  @JsonProperty("prescriptionMedicationId")
  @Schema(description = "Prescription medication id", example = "1")
  private Integer prescriptionMedicationId;

  /**
   * Eprescribe cancel response id
   *
   * @documentationExample 12
   *
   * @return {@link Integer} Id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Eprescribe cancel response external Id
   *
   * @documentationExample E87DDD0A-D768-45C2-9B90-49E96DDF221C
   *
   * @return {@link UUID} external Id
   */
  public UUID getExternalId() {
    return externalId;
  }

  public void setExternalId(UUID externalId) {
    this.externalId = externalId;
  }

  /**
   * Eprescribe cancel request Id
   *
   * @documentationExample 12
   *
   * @return {@link Integer} Eprescribe request Id
   */
  public int getEprescribeCancelRequestId() {
    return eprescribeCancelRequestId;
  }

  public void setEprescribeCancelRequestId(int eprescribeCancelRequestId) {
    this.eprescribeCancelRequestId = eprescribeCancelRequestId;
  }

  /**
   * Patient Id
   *
   * @documentationExample 122
   *
   * @return {@link Integer} Patient Id
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Eprescribe cancel response status
   *
   * @documentationExample Response status
   *
   * @return {@link String} response status
   */
  public String getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
  }

  /**
   * Eprescribe cancel response delivery method.
   *
   * @documentationExample Delivery method
   *
   * @return {@link String} Delivery method
   */
  public String getDeliveryMethod() {
    return deliveryMethod;
  }

  public void setDeliveryMethod(String deliveryMethod) {
    this.deliveryMethod = deliveryMethod;
  }

  /**
   * Eprescribe cancel response created date
   *
   * @documentationExample 2021-02-21T00:00:00.000
   *
   * @return {@link LocalDateTime} created date time
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Eprescribe cancel response last updated date time.
   *
   * @documentationExample 2021-02-21T00:00:00.000
   *
   * @return {@link LocalDateTime} last updated date time
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
   * Eprescribe cancel response response event
   *
   * @documentationExample Response event
   *
   * @return {@link String} response event
   */
  public String getResponseEvent() {
    return responseEvent;
  }

  public void setResponseEvent(String responseEvent) {
    this.responseEvent = responseEvent;
  }

  /**
   * Eprescribe cancel response bundle id
   *
   * @documentationExample E87DDD0A-D768-45C2-9B90-49E96DDF221C
   *
   * @return {@link UUID} bundle id
   */
  public UUID getBundleId() {
    return bundleId;
  }

  public void setBundleId(UUID bundleId) {
    this.bundleId = bundleId;
  }

  /**
   * Eprescribe cancel response task id
   *
   * @documentationExample E87DDD0A-D768-45C2-9B90-49E96DDF221C
   *
   * @return {@link UUID} task id
   */
  public UUID getTaskId() {
    return taskId;
  }

  public void setTaskId(UUID taskId) {
    this.taskId = taskId;
  }

  /**
   * Eprescribe cancel response pharmacy id
   *
   * @documentationExample Pharmacy id
   *
   * @return {@link String} pharmacy id
   */
  public String getPharmacyId() {
    return pharmacyId;
  }

  public void setPharmacyId(String pharmacyId) {
    this.pharmacyId = pharmacyId;
  }

  /**
   * Prescription medication id.
   *
   * @documentationExample 123
   *
   * @return {@link Integer} Prescription medication id
   */
  public Integer getPrescriptionMedicationId() {
    return prescriptionMedicationId;
  }

  public void setPrescriptionMedicationId(Integer prescriptionMedicationId) {
    this.prescriptionMedicationId = prescriptionMedicationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EprescribeCancelResponseDto that = (EprescribeCancelResponseDto) o;
    if (id != that.id) {
      return false;
    }
    if (eprescribeCancelRequestId != that.eprescribeCancelRequestId) {
      return false;
    }
    if (!Objects.equals(patientId, that.patientId)) {
      return false;
    }
    if (!Objects.equals(externalId, that.externalId)) {
      return false;
    }
    if (!Objects.equals(responseStatus, that.responseStatus)) {
      return false;
    }
    if (!Objects.equals(deliveryMethod, that.deliveryMethod)) {
      return false;
    }
    if (!Objects.equals(createdDate, that.createdDate)) {
      return false;
    }
    if (!Objects.equals(lastUpdated, that.lastUpdated)) {
      return false;
    }
    if (!Objects.equals(responseEvent, that.responseEvent)) {
      return false;
    }
    if (!Objects.equals(bundleId, that.bundleId)) {
      return false;
    }
    if (!Objects.equals(taskId, that.taskId)) {
      return false;
    }
    if (!Objects.equals(pharmacyId, that.pharmacyId)) {
      return false;
    }
    return Objects.equals(prescriptionMedicationId, that.prescriptionMedicationId);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
    result = 31 * result + eprescribeCancelRequestId;
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (responseStatus != null ? responseStatus.hashCode() : 0);
    result = 31 * result + (deliveryMethod != null ? deliveryMethod.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
    result = 31 * result + (responseEvent != null ? responseEvent.hashCode() : 0);
    result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
    result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
    result = 31 * result + (pharmacyId != null ? pharmacyId.hashCode() : 0);
    result =
        31 * result + (prescriptionMedicationId != null ? prescriptionMedicationId.hashCode() : 0);
    return result;
  }
}
