
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.logging.LogActivityType;
import com.qhrtech.emr.accuro.model.logging.LoggableObject;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

/**
 * Data Transfer Object from {@link com.qhrtech.emr.accuro.model.medications.MedicationLogEntry}.
 */
@Schema(description = "The prescription history transfer model")
public class PrescriptionHistoryDto {

  @JsonProperty("id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription history id.")
  private int id;

  /**
   * The date and time (Accuro application time) the activity was logged.
   */
  @JsonProperty("loggedTime")
  @Schema(description = "The date and time (Accuro application time) the activity was logged.")
  private LocalDateTime loggedTime;

  /**
   * An xml representation of the changed prescription.
   *
   * @see LoggableObject#getLoggableView()
   */
  @JsonProperty("loggedObject")
  @Schema(description = "An xml representation of the changed prescription.")
  private String loggedObject;

  /**
   * The action performed on the Prescription.
   *
   * @see LogActivityType#ADD_PRESCRIPTION
   * @see LogActivityType#UPDATE_PRESCRIPTION
   * @see LogActivityType#REMOVE_PRESCRIPTION
   * @see LogActivityType#RECOVER_PRESCRIPTION
   */
  @JsonProperty("activity")
  @Schema(description = "The action performed on the Prescription.")
  private LogActivityType activity;

  /**
   * The unique database id of the prescription that has been altered or created.
   */
  @JsonProperty("prescriptionId")
  @Schema(
      description = "The unique database id of the prescription that has been altered or created.")
  private int prescriptionId;

  /**
   * The username of the Accuro User who performed the activity.
   */
  @JsonProperty("username")
  @Schema(description = "The username of the Accuro User who performed the activity.")
  private String username;

  /**
   * The patient the prescription is for.
   */
  @JsonProperty("patientId")
  @Schema(description = "A patient where a prescription is for.")
  private int patientId;

  /**
   * The Prescription History id.
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
   * The date and time (Accuro application time) the activity was logged.
   *
   * @return The Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLoggedTime() {
    return loggedTime;
  }

  public void setLoggedTime(LocalDateTime loggedTime) {
    this.loggedTime = loggedTime;
  }

  /**
   * The XML representation of the changed Prescription.
   *
   * @return The loggedObject
   * @documentationExample string
   */
  public String getLoggedObject() {
    return loggedObject;
  }

  public void setLoggedObject(String loggedObject) {
    this.loggedObject = loggedObject;
  }

  /**
   * The Log Activity type of the Prescription
   *
   * @return The logActivityType
   * @documentationExample ADD_PRESCRIPTION
   */
  public LogActivityType getActivity() {
    return activity;
  }

  public void setActivity(LogActivityType activity) {
    this.activity = activity;
  }

  /**
   * The Id of the Prescription that has been altered or created.
   *
   * @return The prescriptionId
   * @documentationExample 1
   */
  public int getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(int prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * The accuro username who performed the activity
   *
   * @return userName
   * @documentationExample jonathan
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * The id of the patient to whom the prescription is for
   *
   * @return The patientId
   * @documentationExample 1
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PrescriptionHistoryDto)) {
      return false;
    }

    PrescriptionHistoryDto that = (PrescriptionHistoryDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPrescriptionId() != that.getPrescriptionId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getLoggedTime() != null ? !getLoggedTime().equals(that.getLoggedTime())
        : that.getLoggedTime() != null) {
      return false;
    }
    if (getLoggedObject() != null ? !getLoggedObject().equals(that.getLoggedObject())
        : that.getLoggedObject() != null) {
      return false;
    }
    if (getActivity() != that.getActivity()) {
      return false;
    }
    return getUsername() != null ? getUsername().equals(that.getUsername())
        : that.getUsername() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getLoggedTime() != null ? getLoggedTime().hashCode() : 0);
    result = 31 * result + (getLoggedObject() != null ? getLoggedObject().hashCode() : 0);
    result = 31 * result + (getActivity() != null ? getActivity().hashCode() : 0);
    result = 31 * result + getPrescriptionId();
    result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
    result = 31 * result + getPatientId();
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PrescriptionHistoryDto{");
    sb.append("id=").append(id);
    sb.append(", loggedTime=").append(loggedTime);
    sb.append(", loggedObject='").append(loggedObject).append('\'');
    sb.append(", activity=").append(activity);
    sb.append(", prescriptionId=").append(prescriptionId);
    sb.append(", username='").append(username).append('\'');
    sb.append(", patientId=").append(patientId);
    sb.append('}');
    return sb.toString();
  }
}
