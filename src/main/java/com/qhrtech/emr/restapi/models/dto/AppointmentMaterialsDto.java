
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import org.joda.time.LocalDate;

/**
 * The AppointmentMaterials data transfer object. Holds various models relating to appointments.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "AppointmentMaterials object including various models related to"
    + "the  appointments")
public class AppointmentMaterialsDto {

  @JsonProperty("appointmentId")
  @Schema(description = "Unique identifier for this appointment", example = "18911")
  private int appointmentId;

  @JsonProperty("date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "Appointment date",
      type = "string",
      example = "2017-11-29T00:00:00.000-0800")
  private LocalDate date;

  @JsonProperty("startTime")
  @Schema(description = "The appointment start time in 24 hour time as an integer",
      example = "1300")
  private int startTime;

  @JsonProperty("endTime")
  @Schema(description = "The appointment end time in 24 hour time as an integer", example = "1315")
  private int endTime;

  @JsonProperty("subColumn")
  @Schema(
      description = "Sub column of the appointment. Appointments with overlapping times cannot "
          + "have the same sub-column, otherwise they conflict. Sub columns start at zero.",
      example = "1")
  private int subColumn;

  @JsonProperty("appointmentPatients")
  @Schema(description = "A set of patients who are scheduled on the appointment")
  private Set<AppointmentPatientDto> appointmentPatients;

  @JsonProperty("appointmentHistory")
  @Schema(
      description = "The state history of this appointment (created, updated, cancelled, "
          + "arrived, etc)")
  private List<AppointmentHistoryDto> appointmentHistory;

  @JsonProperty("resourceId")
  @Schema(
      description = "The id of the resource associated with the appointment."
          + "Should be null if provider scheduling is used.",
      example = "34")
  private Integer resourceId;

  @JsonProperty("provider")
  @Schema(description = "The provider associated with this appointment")
  private ProviderDto provider;

  @JsonProperty("office")
  @Schema(description = "The office associated with the appointment")
  @Valid
  private OfficeDto office;

  @JsonProperty("site")
  @Schema(description = "The site associated with the appointment")
  private SiteDto site;

  @JsonProperty("groupAppointmentId")
  @Schema(
      description = "The id of the associated group appointment record. "
          + "Will be null if the appointment is not a group appointment.",
      example = "34")
  private Integer groupAppointmentId;

  @JsonProperty("groupAppointment")
  @Schema(
      description = "Indicates if appointment is associated to group appointments for not",
      example = "true")
  private boolean groupAppointment;

  @JsonProperty("notes")
  @Schema(description = "Note that will display in the appointment view in Accuro",
      example = "Patient would like to discuss flu vaccines")
  private String notes;

  @JsonProperty("appointmentReason")
  @Schema(description = "The appointment reason")
  private AppointmentReasonDto appointmentReason;

  @JsonProperty("appointmentType")
  @Schema(description = "The appointment type")
  private AppointmentTypeDto appointmentType;

  @JsonProperty("location")
  @Schema(description = "The geographic location of the appointment")
  private LocationDto location;

  @JsonProperty("statuses")
  @Schema(description = "A set of custom statuses set to this appointment")
  private Set<StatusDto> statuses;

  @JsonProperty("alerts")
  @Schema(description = "Pop-up note, It will be displayed when clicked on the appointment",
      example = "Ask for updated phone number on arrival")
  private String alerts;


  @JsonProperty("rowVersion")
  @Schema(
      description = "The current version of the appointment record. The value of the number only "
          + "has meaning when compared to other rowversions.",
      example = "218147251623")
  private Long rowVersion;

  @JsonProperty("billOnly")
  private Boolean billOnly;

  @JsonProperty("cancelled")
  private boolean cancelled;

  @Schema(description = "ID of the room(s) in which appointment is scheduled",
      example = "[100, 102]")
  private Set<Integer> roomIds;

  @JsonProperty("accessionNumber")
  @Schema(description = "Accession number associated with the appointment.",
      example = "2020-00-022244", readOnly = true)
  private String accessionNumber;

  /**
   * ID of the room(s) in which appointment is scheduled
   *
   * @return Room ID(s)
   *
   * @documentationExample 100
   */
  public Set<Integer> getRoomIds() {
    return this.roomIds;
  }

  public void setRoomIds(Set<Integer> roomIds) {
    this.roomIds = roomIds;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  /**
   * Boolean which identifies if appointment is billOnly or not.
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
   * Alerts or pop-up notes that will display when clicked on an Appointment in Accuro
   *
   * @return Appointment pop-up Notes
   * @documentationExample Remind patient to update the address on file
   */
  public String getAlerts() {
    return alerts;
  }

  public void setAlerts(String alerts) {
    this.alerts = alerts;
  }

  /**
   * Unique identifier for this Appointment.
   *
   * @return ID of the Appointment
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
   * @return Start Time of the Appointment in 24 hour time as an integer.
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
   * @return End Time of the Appointment in 24 hour time as an integer.
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
   * The Set of Patients who are scheduled on this appointment.
   *
   * @return The set of patients who are scheduled on this appointment.
   */
  public Set<AppointmentPatientDto> getAppointmentPatients() {
    return appointmentPatients;
  }

  public void setAppointmentPatients(
      Set<AppointmentPatientDto> appointmentPatients) {
    this.appointmentPatients = appointmentPatients;
  }

  /**
   * The state history of this appointment (created, updated, cancelled, arrived, etc).
   *
   * @return The state history of this appointment.
   */
  public List<AppointmentHistoryDto> getAppointmentHistory() {
    return appointmentHistory;
  }

  public void setAppointmentHistory(
      List<AppointmentHistoryDto> appointmentHistory) {
    this.appointmentHistory = appointmentHistory;
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
   * The provider associated with this appointment.
   *
   * @return Scheduling Provider for this Appointment.
   */
  public ProviderDto getProvider() {
    return provider;
  }

  public void setProvider(ProviderDto provider) {
    this.provider = provider;
  }

  /**
   * The office associated with this appointment.
   *
   * @return Accuro Office where the Appointment is located.
   */
  public OfficeDto getOffice() {
    return office;
  }

  public void setOffice(OfficeDto office) {
    this.office = office;
  }

  /**
   * The site associated with this appointment.
   *
   * @return Accuro site where the Appointment is located.
   */
  public SiteDto getSite() {
    return site;
  }

  public void setSite(SiteDto site) {
    this.site = site;
  }

  /**
   * The ID of the associated group appointment record. Will be null if the appointment is not a
   * group appointment.
   *
   * @return The ID of the associated group appointment record. Will be null if the appointment is
   *         not a group appointment.
   * @documentationExample 34
   */
  public Integer getGroupAppointmentId() {
    return groupAppointmentId;
  }

  public void setGroupAppointmentId(Integer groupAppointmentId) {
    this.groupAppointmentId = groupAppointmentId;
  }

  /**
   * Appointments may be associated to group appointments. This value will indicate this state.
   *
   * @return If this appointment is associated to a group appointment.
   * @documentationExample true
   */
  public boolean isGroupAppointment() {
    return groupAppointment;
  }

  public void setGroupAppointment(boolean groupAppointment) {
    this.groupAppointment = groupAppointment;
  }

  /**
   * Note that will display in the Appointment view in Accuro
   *
   * @return Appointment Notes
   * @documentationExample Patient Would like to discuss flu vaccines
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The Appointment Reason for this Appointment
   *
   * @return Appointment Reason
   */
  public AppointmentReasonDto getAppointmentReason() {
    return appointmentReason;
  }

  public void setAppointmentReason(
      AppointmentReasonDto appointmentReason) {
    this.appointmentReason = appointmentReason;
  }

  /**
   * The Appointment Type for this Appointment
   *
   * @return Appointment Type
   */
  public AppointmentTypeDto getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(AppointmentTypeDto appointmentType) {
    this.appointmentType = appointmentType;
  }

  /**
   * The geographic location of the appointment.
   *
   * @return An entity representing the geographic location of the appointment.
   */
  public LocationDto getLocation() {
    return location;
  }

  public void setLocation(LocationDto location) {
    this.location = location;
  }

  /**
   * The set of custom statuses set to this appointment.
   *
   * @return Custom statuses on this appointment.
   */
  public Set<StatusDto> getStatuses() {
    return statuses;
  }

  public void setStatuses(Set<StatusDto> statuses) {
    this.statuses = statuses;
  }

  /**
   * The current version of the appointment record.
   * <p>
   * The value of the number only has meaning when compared to other rowversions in that higher
   * rownumbers are later versions
   *
   * @return A number for the timestamp
   * @documentationExample 218147251623
   */
  public Long getRowVersion() {
    return rowVersion;
  }

  public void setRowVersion(Long rowVersion) {
    this.rowVersion = rowVersion;
  }

  public String getAccessionNumber() {
    return accessionNumber;
  }

  public void setAccessionNumber(String accessionNumber) {
    this.accessionNumber = accessionNumber;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AppointmentMaterialsDto{");
    sb.append("appointmentId=").append(appointmentId);
    sb.append(", date=").append(date);
    sb.append(", startTime=").append(startTime);
    sb.append(", endTime=").append(endTime);
    sb.append(", subColumn=").append(subColumn);
    sb.append(", appointmentPatients=").append(appointmentPatients);
    sb.append(", appointmentHistory=").append(appointmentHistory);
    sb.append(", resourceId=").append(resourceId);
    sb.append(", provider=").append(provider);
    sb.append(", office=").append(office);
    sb.append(", site=").append(site);
    sb.append(", groupAppointmentId=").append(groupAppointmentId);
    sb.append(", groupAppointment=").append(groupAppointment);
    sb.append(", notes='").append(notes).append('\'');
    sb.append(", appointmentReason=").append(appointmentReason);
    sb.append(", appointmentType=").append(appointmentType);
    sb.append(", location=").append(location);
    sb.append(", statuses=").append(statuses);
    sb.append(", alerts='").append(alerts).append('\'');
    sb.append(", rowVersion=").append(rowVersion);
    sb.append(", billOnly=").append(billOnly);
    sb.append(", cancelled=").append(cancelled);
    sb.append(", roomIds=").append(roomIds);
    sb.append(", accessionNumber='").append(accessionNumber).append('\'');
    sb.append('}');
    return sb.toString();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AppointmentMaterialsDto that = (AppointmentMaterialsDto) o;

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
    if (groupAppointment != that.groupAppointment) {
      return false;
    }
    if (cancelled != that.cancelled) {
      return false;
    }
    if (!Objects.equals(date, that.date)) {
      return false;
    }
    if (!Objects.equals(appointmentPatients, that.appointmentPatients)) {
      return false;
    }
    if (!Objects.equals(appointmentHistory, that.appointmentHistory)) {
      return false;
    }
    if (!Objects.equals(resourceId, that.resourceId)) {
      return false;
    }
    if (!Objects.equals(provider, that.provider)) {
      return false;
    }
    if (!Objects.equals(office, that.office)) {
      return false;
    }
    if (!Objects.equals(site, that.site)) {
      return false;
    }
    if (!Objects.equals(groupAppointmentId, that.groupAppointmentId)) {
      return false;
    }
    if (!Objects.equals(notes, that.notes)) {
      return false;
    }
    if (!Objects.equals(appointmentReason, that.appointmentReason)) {
      return false;
    }
    if (!Objects.equals(appointmentType, that.appointmentType)) {
      return false;
    }
    if (!Objects.equals(location, that.location)) {
      return false;
    }
    if (!Objects.equals(statuses, that.statuses)) {
      return false;
    }
    if (!Objects.equals(alerts, that.alerts)) {
      return false;
    }
    if (!Objects.equals(rowVersion, that.rowVersion)) {
      return false;
    }
    if (!Objects.equals(billOnly, that.billOnly)) {
      return false;
    }
    if (!Objects.equals(roomIds, that.roomIds)) {
      return false;
    }
    return Objects.equals(accessionNumber, that.accessionNumber);
  }

  @Override
  public int hashCode() {
    int result = appointmentId;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + startTime;
    result = 31 * result + endTime;
    result = 31 * result + subColumn;
    result = 31 * result + (appointmentPatients != null ? appointmentPatients.hashCode() : 0);
    result = 31 * result + (appointmentHistory != null ? appointmentHistory.hashCode() : 0);
    result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
    result = 31 * result + (provider != null ? provider.hashCode() : 0);
    result = 31 * result + (office != null ? office.hashCode() : 0);
    result = 31 * result + (site != null ? site.hashCode() : 0);
    result = 31 * result + (groupAppointmentId != null ? groupAppointmentId.hashCode() : 0);
    result = 31 * result + (groupAppointment ? 1 : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + (appointmentReason != null ? appointmentReason.hashCode() : 0);
    result = 31 * result + (appointmentType != null ? appointmentType.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    result = 31 * result + (statuses != null ? statuses.hashCode() : 0);
    result = 31 * result + (alerts != null ? alerts.hashCode() : 0);
    result = 31 * result + (rowVersion != null ? rowVersion.hashCode() : 0);
    result = 31 * result + (billOnly != null ? billOnly.hashCode() : 0);
    result = 31 * result + (cancelled ? 1 : 0);
    result = 31 * result + (roomIds != null ? roomIds.hashCode() : 0);
    result = 31 * result + (accessionNumber != null ? accessionNumber.hashCode() : 0);
    return result;
  }
}
