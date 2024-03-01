
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDate;

/**
 * The appointment data transfer object.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Appointment data transfer object")
public class AppointmentDto {

  @JsonProperty("appointmentId")
  @Schema(description = "The unique id of the appointment", type = "integer")
  private int appointmentId = -1;

  @JsonProperty("date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "Appointment date", type = "string",
      example = "2017-11-29", required = true)
  private LocalDate date;

  @JsonProperty("startTime")
  @Schema(description = "The appointment start time in 24 hour time as an integer",
      example = "1300", type = "integer", required = true)
  private int startTime;

  @JsonProperty("endTime")
  @Schema(description = "The appointment end time in 24 hour time as an integer", example = "1315",
      type = "integer", required = true)
  private int endTime;

  @JsonProperty("subColumn")
  @Schema(
      description = "Sub column of the appointment. Appointments with overlapping times cannot "
          + "have the same sub column, otherwise they conflict. Sub columns start at zero.",
      example = "1", type = "integer", required = true)
  private int subColumn;

  @JsonProperty("patientId")
  @Schema(description = "The id of the patient associated with this appointment", example = "12",
      type = "integer", nullable = true)
  private Integer patientId;

  @JsonProperty("providerId")
  @Schema(
      description = "The id of the provider associated with this appointment. Should be null "
          + "if resource scheduling is used.",
      example = "21", type = "integer", nullable = true)
  private Integer providerId;

  @JsonProperty("resourceId")
  @Schema(
      description = "The id of the resource associated with this appointment. Should be null if "
          + "provider scheduling is used.",
      example = "34", type = "integer", nullable = true)
  private Integer resourceId;

  @JsonProperty("officeId")
  @Schema(description = "The id of the office associated with this appointment", example = "54",
      required = true, type = "integer")
  @NotNull
  private Integer officeId;

  @JsonProperty("maskId")
  @Schema(
      description = "The id of the mask associated with this appointment. Should be null unless "
          + "the appointment has been masked.",
      example = "11", type = "integer", nullable = true)
  private Integer maskId;

  @JsonProperty("billOnly")
  private Boolean billOnly;

  @JsonProperty("accessionNumber")
  @Schema(description = "Accession number associated with the appointment."
      + " This is read-only field.",
      example = "2020-00-022244", accessMode = AccessMode.READ_ONLY,
      type = "string", maxLength = 50)
  private String accessionNumber;

  @Valid
  @JsonProperty("appointmentDetails")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Non-Public details about this appointment.")
  private AppointmentDetailsDto appointmentDetails;

  /**
   * Boolean which identifies if appointment is 'BillOnly' or not. 'BillOnly' appointments are
   * created for creating claims and are not shown on the Accuro scheduler. Claims can only be
   * created through the appointments. Therefore, for only claims creation, 'BillOnly' appointments
   * are created.
   *
   * @return Boolean
   * @documentationExample true
   */
  public Boolean getBillOnly() {
    return billOnly;
  }

  public void setBillOnly(Boolean billOnly) {
    this.billOnly = billOnly;
  }


  /**
   * Unique identifier for this Appointment.
   *
   * @return Id of the appointment
   * @documentationExample 18911
   */
  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * Appointment date.
   *
   * @return An appointment date
   */
  @DocumentationExample("2017-11-29T00:00:00.000-0800")
  @TypeHint(String.class)
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * The appointment start time in 24 hour time as an integer.
   *
   * @return Start time of the appointment in 24 hour time as an integer.
   * @documentationExample 1300
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * The appointment end time in 24 hour time as an integer.
   *
   * @return End time of the Appointment in 24 hour time as an integer.
   * @documentationExample 1315
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  /**
   * Sub Column of the Appointment. Appointments with overlapping times cannot have the same
   * sub-column, otherwise they conflict. Sub columns start at zero.
   *
   * @return Sub Column of the Appointment.
   * @documentationExample 1
   */
  public int getSubColumn() {
    return subColumn;
  }

  public void setSubColumn(int subColumn) {
    this.subColumn = subColumn;
  }

  /**
   * The ID of the patient associated with this appointment.
   *
   * @return ID of the Patient for associated with this appointment
   * @documentationExample 12
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * The ID of the provider associated with this appointment. Should be null if Resource Scheduling
   * is used.
   *
   * @return Scheduling Provider ID for this Appointment. Should be null if Resource Scheduling is
   *         used.
   * @documentationExample 21
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * The ID of the resource associated with this appointment. Should be null if Provider Scheduling
   * is used.
   *
   * @return Scheduled Resource ID for this Appointment. Should be null if Provider Scheduling is
   *         used.
   * @documentationExample 34
   */
  public Integer getResourceId() {
    return resourceId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  /**
   * The ID of the office associated with this appointment.
   *
   * @return Accuro Office ID where the Appointment is located.
   * @documentationExample 54
   */
  public Integer getOfficeId() {
    return officeId;
  }

  public void setOfficeId(Integer officeId) {
    this.officeId = officeId;
  }

  /**
   * The ID of the mask associated with this appointment. Should be null unless the Appointment has
   * been masked.
   *
   * @return Mask ID of the appointment. Should be null unless the Appointment has been masked.
   * @documentationExample 11
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  public String getAccessionNumber() {
    return accessionNumber;
  }

  public void setAccessionNumber(String accessionNumber) {
    this.accessionNumber = accessionNumber;
  }

  /**
   * Non-Public details about this Appointment.
   *
   * @return Non-Public details about this Appointment
   */
  @DocumentationExample(type = @TypeHint(AppointmentDetailsDto.class))
  public AppointmentDetailsDto getAppointmentDetails() {
    return appointmentDetails;
  }

  public void setAppointmentDetails(AppointmentDetailsDto appointmentDetails) {
    this.appointmentDetails = appointmentDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AppointmentDto that = (AppointmentDto) o;

    if (appointmentId != that.appointmentId) {
      return false;
    }
    if (startTime != that.startTime) {
      return false;
    }
    if (endTime != that.endTime) {
      return false;
    }
    if (subColumn != that.subColumn) {
      return false;
    }
    if (!Objects.equals(date, that.date)) {
      return false;
    }
    if (!Objects.equals(patientId, that.patientId)) {
      return false;
    }
    if (!Objects.equals(providerId, that.providerId)) {
      return false;
    }
    if (!Objects.equals(resourceId, that.resourceId)) {
      return false;
    }
    if (!Objects.equals(officeId, that.officeId)) {
      return false;
    }
    if (!Objects.equals(maskId, that.maskId)) {
      return false;
    }
    if (!Objects.equals(billOnly, that.billOnly)) {
      return false;
    }
    if (!Objects.equals(accessionNumber, that.accessionNumber)) {
      return false;
    }
    return Objects.equals(appointmentDetails, that.appointmentDetails);
  }

  @Override
  public int hashCode() {
    int result = appointmentId;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + startTime;
    result = 31 * result + endTime;
    result = 31 * result + subColumn;
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
    result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
    result = 31 * result + (officeId != null ? officeId.hashCode() : 0);
    result = 31 * result + (maskId != null ? maskId.hashCode() : 0);
    result = 31 * result + (billOnly != null ? billOnly.hashCode() : 0);
    result = 31 * result + (accessionNumber != null ? accessionNumber.hashCode() : 0);
    result = 31 * result + (appointmentDetails != null ? appointmentDetails.hashCode() : 0);
    return result;
  }
}
