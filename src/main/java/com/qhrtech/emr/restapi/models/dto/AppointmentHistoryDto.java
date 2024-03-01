
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * <p>
 * Represents a state change for an appointment.
 * </p>
 * <p>
 * The values in this entity represent the state of the appointment after the state change.
 * </p>
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "AppointmentHistory data transfer object model.")
public class AppointmentHistoryDto {

  @JsonProperty("history_id")
  @Schema(
      description = "Unique appointment history id",
      example = "1")
  private int historyId;
  @JsonProperty("appointment_id")
  @Schema(
      description = "ID of the appointment",
      example = "1")
  private int appointmentId;
  @JsonProperty("patient_id")
  @Schema(
      description = "ID of the patient associated with the appointment this point in the history",
      example = "1")
  private Integer patientId;
  @JsonProperty("appointment_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "Date of the appointment at this point in the history", type = "string",
      example = "2017-11-29")
  private LocalDate appointmentDate;
  @JsonProperty("start_time")
  @Schema(
      description = "Start time, in 12 hour time, of the appointment at this point in the history",
      example = "8:00am")
  private String startTime;
  @JsonProperty("provider_name")
  @Schema(description = "Scheduling Provider of the appointment at this point in the history",
      example = "Doe, Jane")
  private String providerName;
  @JsonProperty("billing_provider_name")
  @Schema(description = "Billing Provider of the appointment at this point in the history",
      example = "Smith, John")
  private String billingProviderName;
  @JsonProperty("username")
  @Schema(description = "User who initiated the state change of the appointment",
      example = "daviddoctor")
  private String username;
  @JsonProperty("timestamp")
  @Schema(description = "Date time of the appointment at this point in the history",
      type = "string", example = "2012-02-15 07:44:59.000")
  private LocalDateTime timestamp;
  @JsonProperty("action_type")
  @Schema(description = "The type of state change that occurred for the appointment",
      example = "MedeoBookingRequestCreated")
  private AppointmentActionType actionType;
  @JsonProperty("action_reason")
  @Schema(description = "Description of the state change that occurred",
      example = "Request created")
  private String actionReason;
  @JsonProperty("appointment_type")
  @Schema(description = "Appointment type at this point in the history", example = "Checkup")
  private String type;
  @JsonProperty("appointment_reason")
  @Schema(description = "Appointment reason at this point in the history",
      example = "Health concerns")
  private String reason;

  /**
   * Unique appointment history ID
   *
   * @documentationExample 1
   *
   * @return History ID
   */
  public int getHistoryId() {
    return historyId;
  }

  public void setHistoryId(int historyId) {
    this.historyId = historyId;
  }

  /**
   * Appointment this state change is associated with.
   *
   * @documentationExample 1
   *
   * @return Appointment ID
   */
  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * Patient the appointment is associated with at this point in the history.
   *
   * @documentationExample 1
   *
   * @return Patient ID
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Date of the appointment at this point in the history.
   *
   * @return Appointment Date
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getAppointmentDate() {
    return appointmentDate;
  }

  public void setAppointmentDate(LocalDate appointmentDate) {
    this.appointmentDate = appointmentDate;
  }

  /**
   * Start time, in 12 hour time, of the appointment at this point in the history.
   *
   * @documentationExample 8:00am
   *
   * @return Start time
   */
  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  /**
   * Scheduling Provider of the appointment at this point in the history.
   *
   * @documentationExample Doe, Jane
   *
   * @return Provider name
   */
  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  /**
   * Billing Provider of the appointment at this point in the history.
   *
   * @documentationExample Smith, John
   *
   * @return Billing provider name
   */
  public String getBillingProviderName() {
    return billingProviderName;
  }

  public void setBillingProviderName(String billingProviderName) {
    this.billingProviderName = billingProviderName;
  }

  /**
   * User who initiated the state change of the appointment.
   *
   * @documentationExample daviddoctor
   *
   * @return Username
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Date time of the appointment at this point in the history.
   *
   * @return Datetime
   */
  @DocumentationExample("2012-02-15 07:44:59.000")
  @TypeHint(String.class)
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * The type of state change that occured for the appointment.
   *
   * @documentationExample MedeoBookingRequestCreated
   *
   * @return Action type
   */
  public AppointmentActionType getActionType() {
    return actionType;
  }

  public void setActionType(AppointmentActionType actionType) {
    this.actionType = actionType;
  }

  /**
   * Description of the state change that occurred.
   *
   * @documentationExample Request created
   *
   * @return Action reason
   */
  public String getActionReason() {
    return actionReason;
  }

  public void setActionReason(String actionReason) {
    this.actionReason = actionReason;
  }

  /**
   * Appointment type at this point in the history.
   *
   * @documentationExample Checkup
   *
   * @return Appointment type
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Appointment reason at this point in the history.
   *
   * @documentationExample Health concerns
   *
   * @return Appointment reason
   */
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 13 * hash + this.historyId;
    hash = 13 * hash + this.appointmentId;
    hash = 13 * hash + Objects.hashCode(this.patientId);
    hash = 13 * hash + Objects.hashCode(this.appointmentDate);
    hash = 13 * hash + Objects.hashCode(this.startTime);
    hash = 13 * hash + Objects.hashCode(this.providerName);
    hash = 13 * hash + Objects.hashCode(this.billingProviderName);
    hash = 13 * hash + Objects.hashCode(this.username);
    hash = 13 * hash + Objects.hashCode(this.timestamp);
    hash = 13 * hash + Objects.hashCode(this.actionType);
    hash = 13 * hash + Objects.hashCode(this.actionReason);
    hash = 13 * hash + Objects.hashCode(this.type);
    hash = 13 * hash + Objects.hashCode(this.reason);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AppointmentHistoryDto other = (AppointmentHistoryDto) obj;
    if (this.historyId != other.historyId) {
      return false;
    }
    if (this.appointmentId != other.appointmentId) {
      return false;
    }
    if (!Objects.equals(this.patientId, other.patientId)) {
      return false;
    }
    if (!Objects.equals(this.appointmentDate, other.appointmentDate)) {
      return false;
    }
    if (!Objects.equals(this.startTime, other.startTime)) {
      return false;
    }
    if (!Objects.equals(this.providerName, other.providerName)) {
      return false;
    }
    if (!Objects.equals(this.billingProviderName, other.billingProviderName)) {
      return false;
    }
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.timestamp, other.timestamp)) {
      return false;
    }
    if (!Objects.equals(this.actionType, other.actionType)) {
      return false;
    }
    if (!Objects.equals(this.actionReason, other.actionReason)) {
      return false;
    }
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.reason, other.reason)) {
      return false;
    }
    return true;
  }

  /**
   * Represents the possible state change action types.
   */
  @Schema(description = "Appointment Action Type enum")
  public enum AppointmentActionType {

    /**
     * Used when the history action type is not set or unknown.
     */
    NotSet,

    /**
     * Used when the appointment was created by an Accuro user.
     */
    Created,

    /**
     * Used when the appointment was updated by an Accuro user, and does not fit the below states.
     */
    Edited,

    /**
     * Used when the appointment has been copied and pasted on the scheduler.
     */
    Copied,

    /**
     * Used when the appointment has been moved to another time by an Accuro user.
     */
    Moved,

    /**
     * Used when the appointment has been cancelled by an Accuro user.
     */
    Cancelled,

    /**
     * Used when the appointment has been deleted by an Accuro user.
     */
    Deleted,

    /**
     * Used when appointment has been confirmed by an Accuro user.
     */
    Confirmed,

    /**
     * Used when the appointment has been unconfirmed by an Accuro user.
     */
    Unconfirmed,

    /**
     * Used when the appointment has been arrived by an Accuro user.
     */
    Arrived,

    /**
     * Used when the appointment has been arrived by the patient (e.g. through the kiosk).
     */
    PatientArrived,

    /**
     * Used when the patient has unarrived the appointment (e.g. through the kiosk).
     */
    PatientLeft,

    /**
     * Used when a custom appointment status has been removed from the appointment.
     */
    CustomStatusRemoved,

    /**
     * Used when a custom appointment status has been added to the appointment.
     */
    CustomStatusAdded,

    /**
     * Used when the cancellation or deletion of the appointment was reversed by an Accuro admin.
     */
    Restored,

    /**
     * Used when the appointment has been cancelled by the patient (e.g. via medeo ebooking).
     */
    PatientCancelled,

    /**
     * Used when the appointment was created by a Medeo E-Booking request.
     */
    MedeoBookingRequestCreated,

    /**
     * Used when the appointment had its Medeo E-Booking request accepted.
     */
    MedeoBookingRequestAccepted,

    /**
     * Used when the appointment was deleted by a Medeo E-Booking rejection.
     */
    MedeoBookingRequestRejected
  }

}
