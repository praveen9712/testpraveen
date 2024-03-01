
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckBigDecimal;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class DispenseNotificationDto {

  @JsonProperty("id")
  @Schema(description = "The identity", example = "10")
  private int id;

  @JsonProperty("uuid")
  @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  @NotNull
  private UUID uuid;

  @JsonProperty("conversationMessageId")
  @Schema(description = "Conversation Message Id", example = "10")
  private Integer conversationMessageId;

  @JsonProperty("createdDate")
  @Schema(description = "Created date time (UTC). The field is read-only.",
      example = "2021-02-21T00:00:00.000")
  private LocalDateTime createdDate;

  @JsonProperty("cancellationReason")
  @Schema(description = "Cancellation reason", example = "String")
  private String cancellationReason;

  @JsonProperty("patient")
  @Schema(description = "Patient UUID", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID patient;

  @JsonProperty("renderedDosageInstruction")
  @Schema(description = "Rendered dosage instruction", example = "String")
  private String renderedDosageInstruction;

  @JsonProperty("quantityRemainingValue")
  @Schema(description = "Quantity remaining value", example = "10.10")
  @CheckBigDecimal(precision = 18, scale = 2)
  private BigDecimal quantityRemainingValue;

  @JsonProperty("quantityRemainingUnit")
  @Schema(description = "Quantity remaining unit", example = "String")
  @Size(max = 100)
  private String quantityRemainingUnit;

  @JsonProperty("quantityRemainingSystem")
  @Schema(description = "Quantity remaining system", example = "String")
  @Size(max = 100, message = "Maximum size allowed is 100 characters.")
  private String quantityRemainingSystem;

  @JsonProperty("pharmacyName")
  @Schema(description = "Pharmacy name", example = "String")
  @Size(max = 255)
  private String pharmacyName;

  @JsonProperty("pharmacyFax")
  @Schema(description = "The pharmacy fax", example = "String")
  @Size(max = 255)
  private String pharmacyFax;

  @JsonProperty("pharmacyPhone")
  @Schema(description = "The pharmacy phone", example = "String")
  @Size(max = 255)
  private String pharmacyPhone;

  @JsonProperty("pharmacyAddressLine")
  @Schema(description = "The pharmacy address line", example = "String")
  @Size(max = 255)
  private String pharmacyAddressLine;

  @JsonProperty("pharmacyAddressCity")
  @Schema(description = "The pharmacy address city", example = "String")
  @Size(max = 255)
  private String pharmacyAddressCity;

  @JsonProperty("pharmacyAddressState")
  @Schema(description = "The pharmacy address state", example = "String")
  @Size(max = 255)
  private String pharmacyAddressState;

  @JsonProperty("pharmacyAddressPostalCode")
  @Schema(description = "The pharmacy address postal code", example = "String")
  @Size(max = 255)
  private String pharmacyAddressPostalCode;

  @JsonProperty("daysSupply")
  @Schema(description = "Days supply", example = "10")
  private Integer daysSupply;

  @JsonProperty("status")
  @Schema(description = "Dispense notification status")
  private DispenseNotificationStatus status;

  @JsonProperty("category")
  @Schema(description = "Category", example = "String")
  @Size(max = 10)
  private String category;

  @JsonProperty("authorizingRequestUuid")
  @Schema(description = "Authorizing request uuid",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID authorizingRequestUuid;

  @JsonProperty("prescribeItAuthorizingRequestUuid")
  @Schema(description = "PrescribeIT authorizing request uuid",
      example = "123e4567-e89b-12d3-a456-426614174000")
  @Size(max = 64)
  private String prescribeItAuthorizingRequestUuid;

  @JsonProperty("dispenseType")
  @Schema(description = "Dispense type", example = "String")
  @Size(max = 6)
  private String dispenseType;

  @JsonProperty("substitutionReason")
  @Schema(description = "Substitution reason", example = "String")
  private String substitutionReason;

  @JsonProperty("notDoneReason")
  @Schema(description = "Not done reason", example = "String")
  private String notDoneReason;

  @JsonProperty("din")
  @Schema(description = "Din", example = "String")
  @Size(max = 18)
  private String din;

  @JsonProperty("medicationCodeSystem")
  @Schema(description = "Medication code system", example = "String")
  @Size(max = 100)
  private String medicationCodeSystem;

  @JsonProperty("medicationName")
  @Schema(description = "Medication Name", example = "String")
  @NotNull
  @Size(max = 100)
  private String medicationName;

  @JsonProperty("medicationDetails")
  @Schema(description = "Medication Details", example = "String")
  @Size(max = 1000)
  private String medicationDetails;

  @JsonProperty("dispenseAmount")
  @Schema(description = "Dispense Amount", example = "10")
  private Integer dispenseAmount;

  @JsonProperty("dispenseUnit")
  @Schema(description = "Dispense Unit", example = "String")
  @Size(max = 100)
  private String dispenseUnit;

  @JsonProperty("dispenseTime")
  @Schema(description = "Dispense time", example = "2021-02-21T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime dispenseTime;

  @JsonProperty("notes")
  @Schema(description = "Notes", example = "10")
  @Valid
  private List<DispenseNotificationAnnotationDto> notes;

  @JsonProperty("cancelled")
  @Schema(description = "Is cancelled", example = "False")
  private boolean cancelled;

  @JsonProperty("identifierSystem")
  @Schema(description = "Identifier System")
  @Size(max = 255)
  private String identifierSystem;

  @JsonProperty("identifierValue")
  @Schema(description = "Identifier Value")
  @Size(max = 255)
  private String identifierValue;

  /**
   * The identity
   *
   * @return DispenseNotification ID
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Unique identifier
   *
   * @return UUID
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * The identity
   *
   * @return DispenseNotification ID
   * @documentationExample 1
   */
  public Integer getConversationMessageId() {
    return conversationMessageId;
  }

  public void setConversationMessageId(Integer conversationMessageId) {
    this.conversationMessageId = conversationMessageId;
  }

  /**
   * The identity
   *
   * @return DispenseNotification ID
   * @documentationExample 1
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
   * The identity
   *
   * @return DispenseNotification ID
   * @documentationExample 1
   */
  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  /**
   * The identity
   *
   * @return DispenseNotification ID
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public UUID getPatient() {
    return patient;
  }

  public void setPatient(UUID patient) {
    this.patient = patient;
  }

  /**
   * Rendered dosage instructions
   *
   * @return String - instructions
   * @documentationExample Instructions
   */
  public String getRenderedDosageInstruction() {
    return renderedDosageInstruction;
  }

  public void setRenderedDosageInstruction(String renderedDosageInstruction) {
    this.renderedDosageInstruction = renderedDosageInstruction;
  }

  /**
   * Quantity remaining value
   *
   * @return Double - value
   * @documentationExample 10.110
   */
  public BigDecimal getQuantityRemainingValue() {
    return quantityRemainingValue;
  }

  public void setQuantityRemainingValue(BigDecimal quantityRemainingValue) {
    this.quantityRemainingValue = quantityRemainingValue;
  }

  /**
   * Quantity remaining unit
   *
   * @return String - unit name
   * @documentationExample String
   */
  public String getQuantityRemainingUnit() {
    return quantityRemainingUnit;
  }

  public void setQuantityRemainingUnit(String quantityRemainingUnit) {
    this.quantityRemainingUnit = quantityRemainingUnit;
  }

  /**
   * Quantity remaining in the system
   *
   * @return String - system remaining
   * @documentationExample String
   */
  public String getQuantityRemainingSystem() {
    return quantityRemainingSystem;
  }

  public void setQuantityRemainingSystem(String quantityRemainingSystem) {
    this.quantityRemainingSystem = quantityRemainingSystem;
  }

  /**
   * Pharmacy name
   *
   * @return String - name
   * @documentationExample pharmacy name
   */
  public String getPharmacyName() {
    return pharmacyName;
  }

  public void setPharmacyName(String pharmacyName) {
    this.pharmacyName = pharmacyName;
  }

  /**
   * Pharmacy fax number
   *
   * @return String - fax details
   * @documentationExample 1234567890
   */
  public String getPharmacyFax() {
    return pharmacyFax;
  }

  public void setPharmacyFax(String pharmacyFax) {
    this.pharmacyFax = pharmacyFax;
  }

  /**
   * Pharmacy phone
   *
   * @return String - phone number
   * @documentationExample 123449544
   */
  public String getPharmacyPhone() {
    return pharmacyPhone;
  }

  public void setPharmacyPhone(String pharmacyPhone) {
    this.pharmacyPhone = pharmacyPhone;
  }

  /**
   * Pharmacy address line
   *
   * @return String - street
   * @documentationExample street line
   */
  public String getPharmacyAddressLine() {
    return pharmacyAddressLine;
  }

  public void setPharmacyAddressLine(String pharmacyAddressLine) {
    this.pharmacyAddressLine = pharmacyAddressLine;
  }

  /**
   * Pharmacy address city
   *
   * @return String - city
   * @documentationExample kelowna
   */
  public String getPharmacyAddressCity() {
    return pharmacyAddressCity;
  }

  public void setPharmacyAddressCity(String pharmacyAddressCity) {
    this.pharmacyAddressCity = pharmacyAddressCity;
  }

  /**
   * Pharmacy address state
   *
   * @return String - province
   * @documentationExample BC
   */
  public String getPharmacyAddressState() {
    return pharmacyAddressState;
  }

  public void setPharmacyAddressState(String pharmacyAddressState) {
    this.pharmacyAddressState = pharmacyAddressState;
  }

  /**
   * Pharmacy address postal code
   *
   * @return String - postal code
   * @documentationExample 1a31f4
   */
  public String getPharmacyAddressPostalCode() {
    return pharmacyAddressPostalCode;
  }

  public void setPharmacyAddressPostalCode(String pharmacyAddressPostalCode) {
    this.pharmacyAddressPostalCode = pharmacyAddressPostalCode;
  }

  /**
   * Days supply
   *
   * @return Integer - number of days supply
   * @documentationExample 10
   */
  public Integer getDaysSupply() {
    return daysSupply;
  }

  public void setDaysSupply(Integer daysSupply) {
    this.daysSupply = daysSupply;
  }

  /**
   * Dispense Notification Status
   *
   * @return {@link DispenseNotificationStatus} details
   * @documentationExample PREPARATION
   */
  public DispenseNotificationStatus getStatus() {
    return status;
  }

  public void setStatus(DispenseNotificationStatus status) {
    this.status = status;
  }

  /**
   * Category
   *
   * @return String - category name
   * @documentationExample abc
   */
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Authorizing Request UUID
   *
   * @return UUID Unique identifier
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public UUID getAuthorizingRequestUuid() {
    return authorizingRequestUuid;
  }

  public void setAuthorizingRequestUuid(UUID authorizingRequestUuid) {
    this.authorizingRequestUuid = authorizingRequestUuid;
  }

  /**
   * PrescribeIt Authorizing Request UUID
   *
   * @return String - Unique identifier
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public String getPrescribeItAuthorizingRequestUuid() {
    return prescribeItAuthorizingRequestUuid;
  }

  public void setPrescribeItAuthorizingRequestUuid(String prescribeItAuthorizingRequestUuid) {
    this.prescribeItAuthorizingRequestUuid = prescribeItAuthorizingRequestUuid;
  }

  /**
   * Medication dispense type
   *
   * @return String - dispense type
   * @documentationExample String
   */
  public String getDispenseType() {
    return dispenseType;
  }

  public void setDispenseType(String dispenseType) {
    this.dispenseType = dispenseType;
  }

  /**
   * Substitution reason
   *
   * @return String - reason for substitution
   * @documentationExample String
   */
  public String getSubstitutionReason() {
    return substitutionReason;
  }

  public void setSubstitutionReason(String substitutionReason) {
    this.substitutionReason = substitutionReason;
  }

  /**
   * Not done reason
   *
   * @return String - reason for not done
   * @documentationExample String
   */
  public String getNotDoneReason() {
    return notDoneReason;
  }

  public void setNotDoneReason(String notDoneReason) {
    this.notDoneReason = notDoneReason;
  }

  /**
   * Medication DIN
   *
   * @return String - DIN value
   * @documentationExample 1df3
   */
  public String getDin() {
    return din;
  }

  public void setDin(String din) {
    this.din = din;
  }

  /**
   * Dispense medication code system
   *
   * @return String - code system
   * @documentationExample XYZ
   */
  public String getMedicationCodeSystem() {
    return medicationCodeSystem;
  }

  public void setMedicationCodeSystem(String medicationCodeSystem) {
    this.medicationCodeSystem = medicationCodeSystem;
  }

  /**
   * Dispense medication name
   *
   * @return String - medication name
   * @documentationExample abc
   */
  public String getMedicationName() {
    return medicationName;
  }

  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  /**
   * Dispense medication details
   *
   * @return Details of the dispense
   * @documentationExample String
   */
  public String getMedicationDetails() {
    return medicationDetails;
  }

  public void setMedicationDetails(String medicationDetails) {
    this.medicationDetails = medicationDetails;
  }

  /**
   * Dispense amount
   *
   * @return Amount number
   * @documentationExample 10
   */
  public Integer getDispenseAmount() {
    return dispenseAmount;
  }

  public void setDispenseAmount(Integer dispenseAmount) {
    this.dispenseAmount = dispenseAmount;
  }

  /**
   * The dispense unit
   *
   * @return Unit name
   * @documentationExample String
   */
  public String getDispenseUnit() {
    return dispenseUnit;
  }

  public void setDispenseUnit(String dispenseUnit) {
    this.dispenseUnit = dispenseUnit;
  }

  /**
   * The Dispense notification time
   *
   * @return Date time
   * @documentationExample 2021-02-21T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDispenseTime() {
    return dispenseTime;
  }

  public void setDispenseTime(LocalDateTime dispenseTime) {
    this.dispenseTime = dispenseTime;
  }

  /**
   * Dispense Notification notes details
   *
   * @return {@link DispenseNotificationAnnotationDto}
   */
  public List<DispenseNotificationAnnotationDto> getNotes() {
    return notes;
  }

  public void setNotes(List<DispenseNotificationAnnotationDto> notes) {
    this.notes = notes;
  }

  /**
   * Notification status as cancelled
   *
   * @return true if cancelled
   * @documentationExample true
   */
  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  /**
   * Identifier System of dispense notification
   *
   * @return identifierSystemString
   * @documentationExample String
   */
  public String getIdentifierSystem() {
    return identifierSystem;
  }

  public void setIdentifierSystem(String identifierSystem) {
    this.identifierSystem = identifierSystem;
  }

  /**
   * Identifier Value of dispense notification
   *
   * @return identifierValueString
   * @documentationExample String
   */
  public String getIdentifierValue() {
    return identifierValue;
  }

  public void setIdentifierValue(String identifierValue) {
    this.identifierValue = identifierValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DispenseNotificationDto that = (DispenseNotificationDto) o;

    if (id != that.id) {
      return false;
    }
    if (cancelled != that.cancelled) {
      return false;
    }
    if (!Objects.equals(uuid, that.uuid)) {
      return false;
    }
    if (!Objects.equals(conversationMessageId, that.conversationMessageId)) {
      return false;
    }
    if (!Objects.equals(createdDate, that.createdDate)) {
      return false;
    }
    if (!Objects.equals(cancellationReason, that.cancellationReason)) {
      return false;
    }
    if (!Objects.equals(patient, that.patient)) {
      return false;
    }
    if (!Objects.equals(renderedDosageInstruction, that.renderedDosageInstruction)) {
      return false;
    }
    if (!Objects.equals(quantityRemainingValue, that.quantityRemainingValue)) {
      return false;
    }
    if (!Objects.equals(quantityRemainingUnit, that.quantityRemainingUnit)) {
      return false;
    }
    if (!Objects.equals(quantityRemainingSystem, that.quantityRemainingSystem)) {
      return false;
    }
    if (!Objects.equals(pharmacyName, that.pharmacyName)) {
      return false;
    }
    if (!Objects.equals(pharmacyFax, that.pharmacyFax)) {
      return false;
    }
    if (!Objects.equals(pharmacyPhone, that.pharmacyPhone)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressLine, that.pharmacyAddressLine)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressCity, that.pharmacyAddressCity)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressState, that.pharmacyAddressState)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressPostalCode, that.pharmacyAddressPostalCode)) {
      return false;
    }
    if (!Objects.equals(daysSupply, that.daysSupply)) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (!Objects.equals(category, that.category)) {
      return false;
    }
    if (!Objects.equals(authorizingRequestUuid, that.authorizingRequestUuid)) {
      return false;
    }
    if (!Objects.equals(prescribeItAuthorizingRequestUuid,
        that.prescribeItAuthorizingRequestUuid)) {
      return false;
    }
    if (!Objects.equals(dispenseType, that.dispenseType)) {
      return false;
    }
    if (!Objects.equals(substitutionReason, that.substitutionReason)) {
      return false;
    }
    if (!Objects.equals(notDoneReason, that.notDoneReason)) {
      return false;
    }
    if (!Objects.equals(din, that.din)) {
      return false;
    }
    if (!Objects.equals(medicationCodeSystem, that.medicationCodeSystem)) {
      return false;
    }
    if (!Objects.equals(medicationName, that.medicationName)) {
      return false;
    }
    if (!Objects.equals(medicationDetails, that.medicationDetails)) {
      return false;
    }
    if (!Objects.equals(dispenseAmount, that.dispenseAmount)) {
      return false;
    }
    if (!Objects.equals(dispenseUnit, that.dispenseUnit)) {
      return false;
    }
    if (!Objects.equals(dispenseTime, that.dispenseTime)) {
      return false;
    }
    if (!Objects.equals(notes, that.notes)) {
      return false;
    }
    if (!Objects.equals(identifierSystem, that.identifierSystem)) {
      return false;
    }
    return Objects.equals(identifierValue, that.identifierValue);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (conversationMessageId != null ? conversationMessageId.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (cancellationReason != null ? cancellationReason.hashCode() : 0);
    result = 31 * result + (patient != null ? patient.hashCode() : 0);
    result =
        31 * result + (renderedDosageInstruction != null ? renderedDosageInstruction.hashCode()
            : 0);
    result = 31 * result + (quantityRemainingValue != null ? quantityRemainingValue.hashCode() : 0);
    result = 31 * result + (quantityRemainingUnit != null ? quantityRemainingUnit.hashCode() : 0);
    result =
        31 * result + (quantityRemainingSystem != null ? quantityRemainingSystem.hashCode() : 0);
    result = 31 * result + (pharmacyName != null ? pharmacyName.hashCode() : 0);
    result = 31 * result + (pharmacyFax != null ? pharmacyFax.hashCode() : 0);
    result = 31 * result + (pharmacyPhone != null ? pharmacyPhone.hashCode() : 0);
    result = 31 * result + (pharmacyAddressLine != null ? pharmacyAddressLine.hashCode() : 0);
    result = 31 * result + (pharmacyAddressCity != null ? pharmacyAddressCity.hashCode() : 0);
    result = 31 * result + (pharmacyAddressState != null ? pharmacyAddressState.hashCode() : 0);
    result =
        31 * result + (pharmacyAddressPostalCode != null ? pharmacyAddressPostalCode.hashCode()
            : 0);
    result = 31 * result + (daysSupply != null ? daysSupply.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    result = 31 * result + (authorizingRequestUuid != null ? authorizingRequestUuid.hashCode() : 0);
    result = 31 * result + (prescribeItAuthorizingRequestUuid != null
        ? prescribeItAuthorizingRequestUuid.hashCode()
        : 0);
    result = 31 * result + (dispenseType != null ? dispenseType.hashCode() : 0);
    result = 31 * result + (substitutionReason != null ? substitutionReason.hashCode() : 0);
    result = 31 * result + (notDoneReason != null ? notDoneReason.hashCode() : 0);
    result = 31 * result + (din != null ? din.hashCode() : 0);
    result = 31 * result + (medicationCodeSystem != null ? medicationCodeSystem.hashCode() : 0);
    result = 31 * result + (medicationName != null ? medicationName.hashCode() : 0);
    result = 31 * result + (medicationDetails != null ? medicationDetails.hashCode() : 0);
    result = 31 * result + (dispenseAmount != null ? dispenseAmount.hashCode() : 0);
    result = 31 * result + (dispenseUnit != null ? dispenseUnit.hashCode() : 0);
    result = 31 * result + (dispenseTime != null ? dispenseTime.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + (cancelled ? 1 : 0);
    result = 31 * result + (identifierSystem != null ? identifierSystem.hashCode() : 0);
    result = 31 * result + (identifierValue != null ? identifierValue.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DispenseNotificationDto{");
    sb.append("id=").append(id);
    sb.append(", uuid=").append(uuid);
    sb.append(", conversationMessageId=").append(conversationMessageId);
    sb.append(", createdDate=").append(createdDate);
    sb.append(", cancellationReason='").append(cancellationReason).append('\'');
    sb.append(", patient=").append(patient);
    sb.append(", renderedDosageInstruction='").append(renderedDosageInstruction).append('\'');
    sb.append(", quantityRemainingValue=").append(quantityRemainingValue);
    sb.append(", quantityRemainingUnit='").append(quantityRemainingUnit).append('\'');
    sb.append(", quantityRemainingSystem='").append(quantityRemainingSystem).append('\'');
    sb.append(", pharmacyName='").append(pharmacyName).append('\'');
    sb.append(", pharmacyFax='").append(pharmacyFax).append('\'');
    sb.append(", pharmacyPhone='").append(pharmacyPhone).append('\'');
    sb.append(", pharmacyAddressLine='").append(pharmacyAddressLine).append('\'');
    sb.append(", pharmacyAddressCity='").append(pharmacyAddressCity).append('\'');
    sb.append(", pharmacyAddressState='").append(pharmacyAddressState).append('\'');
    sb.append(", pharmacyAddressPostalCode='").append(pharmacyAddressPostalCode).append('\'');
    sb.append(", daysSupply=").append(daysSupply);
    sb.append(", status=").append(status);
    sb.append(", category='").append(category).append('\'');
    sb.append(", authorizingRequestUuid=").append(authorizingRequestUuid);
    sb.append(", prescribeItAuthorizingRequestUuid='").append(prescribeItAuthorizingRequestUuid)
        .append('\'');
    sb.append(", dispenseType='").append(dispenseType).append('\'');
    sb.append(", substitutionReason='").append(substitutionReason).append('\'');
    sb.append(", notDoneReason='").append(notDoneReason).append('\'');
    sb.append(", din='").append(din).append('\'');
    sb.append(", medicationCodeSystem='").append(medicationCodeSystem).append('\'');
    sb.append(", medicationName='").append(medicationName).append('\'');
    sb.append(", medicationDetails='").append(medicationDetails).append('\'');
    sb.append(", dispenseAmount=").append(dispenseAmount);
    sb.append(", dispenseUnit='").append(dispenseUnit).append('\'');
    sb.append(", dispenseTime=").append(dispenseTime);
    sb.append(", notes=").append(notes);
    sb.append(", cancelled=").append(cancelled);
    sb.append(", identifierSystem='").append(identifierSystem).append('\'');
    sb.append(", identifierValue='").append(identifierValue).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
