
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "AppointmentDetails data transfer object model")
public class AppointmentDetailsDto {

  @JsonProperty("billingProviderId")
  @Schema(
      description = "ID of the billing provider on this appointment"
          + "This will be the provider associated with the appointment's claim",
      example = "18770", type = "integer", nullable = true)
  private Integer billingProviderId;

  @JsonProperty("referringProviderId")
  @Schema(description = "ID of the referring provider on this appointment", example = "18770",
      type = "integer", nullable = true)
  private Integer referringProviderId;

  @JsonProperty("notes")
  @Schema(description = "Note that will be displayed in the appointment view in Accuro",
      example = "Patient would like to discuss flu vaccines", type = "string", maxLength = 1000,
      nullable = true)
  private String notes;

  @JsonProperty("alerts")
  @Schema(
      description = "Alerts that will be displayed when the appointment is clicked upon in Accuro",
      example = "Patient is always late, booked an extra 10 minutes", type = "string",
      maxLength = 255, nullable = true)
  private String alerts;

  @JsonProperty("reasonId")
  @Schema(description = "ID of the appointment reason", example = "42", type = "integer",
      nullable = true)
  private Integer reasonId;

  @JsonProperty("typeId")
  @Schema(description = "ID of the appointment type", example = "17", type = "integer",
      nullable = true)
  private Integer typeId;

  @JsonProperty("priorityId")
  @Schema(description = "ID of the priority level for this appointment", example = "1",
      type = "integer", nullable = true)
  private Integer priorityId;

  @JsonProperty("arrived")
  @Schema(description = "Arrived status", example = "true", type = "boolean")
  private boolean arrived;

  @JsonProperty("confirmed")
  @Schema(description = "Confirmed status for this appointment", example = "false",
      type = "boolean")
  private boolean confirmed;

  @JsonProperty("serviceLocation")
  @Schema(description = "Location code for this appointment", example = "O", type = "string",
      maxLength = 1)
  private String serviceLocation;

  @Schema(description = "ID of the room(s) in which appointment is scheduled",
      example = "[100, 102]")
  private Set<Integer> roomIds;

  @Schema(description = "IDs of the other providers in which appointment is scheduled. "
      + "These IDs are read-only. ",
      example = "[100, 102]")
  private List<Integer> otherProviderIds;

  /**
   * IDs of the other providers in which appointment is scheduled. These IDs are read-only.
   *
   * @return The provider IDs
   * @documentationExample 100
   */
  public List<Integer> getOtherProviderIds() {
    return otherProviderIds;
  }

  public void setOtherProviderIds(List<Integer> otherProviderIds) {
    this.otherProviderIds = otherProviderIds;
  }

  /**
   * ID of the room(s) in which appointment is scheduled
   *
   * @return Room ID(s)
   * @documentationExample 100
   */
  public Set<Integer> getRoomIds() {
    return this.roomIds;
  }

  public void setRoomIds(Set<Integer> roomIds) {
    this.roomIds = roomIds;
  }

  /**
   * Id of the billing provider on this appointment
   *
   * This will be the provider associated with the appointment's claim
   *
   * @return Billing provider id
   * @documentationExample 18770
   */
  public Integer getBillingProviderId() {
    return billingProviderId;
  }

  public void setBillingProviderId(Integer billingProviderId) {
    this.billingProviderId = billingProviderId;
  }

  /**
   * Id of the referring provider on this appointment
   *
   * @return Referring provider id
   * @documentationExample 18770
   */
  public Integer getReferringProviderId() {
    return referringProviderId;
  }

  public void setReferringProviderId(Integer referringProviderId) {
    this.referringProviderId = referringProviderId;
  }

  /**
   * Note that will be displayed in the appointment view in Accuro
   *
   * @return Appointment notes
   * @documentationExample Patient would like to discuss flu vaccines
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Alerts that will be displayed when the appointment is clicked upon in Accuro
   *
   * @return Appointment alerts
   * @documentationExample Patient is always late, booked an extra 10 minutes
   */
  public String getAlerts() {
    return alerts;
  }

  public void setAlerts(String alerts) {
    this.alerts = alerts;
  }

  /**
   * Id of the appointment reason
   *
   * @return Appointment reason id
   * @documentationExample 42
   */
  public Integer getReasonId() {
    return reasonId;
  }

  public void setReasonId(Integer reasonId) {
    this.reasonId = reasonId;
  }

  /**
   * Id of the appointment type
   *
   * @return Appointment Type Id
   * @documentationExample 17
   */
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  /**
   * Id of the priority level for this appointment
   *
   * @return Appointment priority id
   * @documentationExample 1
   */
  public Integer getPriorityId() {
    return priorityId;
  }

  public void setPriorityId(Integer priorityId) {
    this.priorityId = priorityId;
  }

  /**
   * Arrived status
   *
   * @return true if the patient has arrived, otherwise false
   * @documentationExample true
   */
  public boolean isArrived() {
    return arrived;
  }

  public void setArrived(boolean arrived) {
    this.arrived = arrived;
  }

  /**
   * Confirmed Status for this Appointment
   *
   * @return true if the appointment has been confirmed, otherwise false
   * @documentationExample false
   */
  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  /**
   * Location code for this appointment
   *
   * @return The appointment's location code
   * @documentationExample O
   */
  @Valid
  @NotNull
  @Size(max = 1, min = 1)
  @Pattern(regexp = "^[0-9A-Z]$")
  public String getServiceLocation() {
    return serviceLocation;
  }

  public void setServiceLocation(String serviceLocation) {
    this.serviceLocation = serviceLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AppointmentDetailsDto that = (AppointmentDetailsDto) o;

    if (arrived != that.arrived) {
      return false;
    }
    if (confirmed != that.confirmed) {
      return false;
    }
    if (billingProviderId != null ? !billingProviderId.equals(that.billingProviderId)
        : that.billingProviderId != null) {
      return false;
    }
    if (referringProviderId != null ? !referringProviderId.equals(that.referringProviderId)
        : that.referringProviderId != null) {
      return false;
    }
    if (notes != null ? !notes.equals(that.notes) : that.notes != null) {
      return false;
    }
    if (alerts != null ? !alerts.equals(that.alerts) : that.alerts != null) {
      return false;
    }
    if (reasonId != null ? !reasonId.equals(that.reasonId) : that.reasonId != null) {
      return false;
    }
    if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) {
      return false;
    }
    if (priorityId != null ? !priorityId.equals(that.priorityId) : that.priorityId != null) {
      return false;
    }
    if (!Objects.equals(getRoomIds(), that.getRoomIds())) {
      return false;
    }
    if (!Objects.equals(getOtherProviderIds(), that.getOtherProviderIds())) {
      return false;
    }
    return serviceLocation != null ? serviceLocation.equals(that.serviceLocation)
        : that.serviceLocation == null;
  }

  @Override
  public int hashCode() {
    int result = billingProviderId != null ? billingProviderId.hashCode() : 0;
    result = 31 * result + (referringProviderId != null ? referringProviderId.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + (alerts != null ? alerts.hashCode() : 0);
    result = 31 * result + (reasonId != null ? reasonId.hashCode() : 0);
    result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
    result = 31 * result + (priorityId != null ? priorityId.hashCode() : 0);
    result = 31 * result + (arrived ? 1 : 0);
    result = 31 * result + (confirmed ? 1 : 0);
    result = 31 * result + (serviceLocation != null ? serviceLocation.hashCode() : 0);
    result = 31 * result + Objects.hashCode(this.roomIds);
    result = 31 * result + Objects.hashCode(this.otherProviderIds);
    return result;
  }
}

