
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckFutureDate;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Data Transfer Object for waitroom entry information")
public class WaitRoomEntryDto {

  @JsonProperty("waitroomId")
  @Schema(description = "Unique waitroom id.", example = "1")
  private int id;

  @JsonProperty("patientId")
  @Schema(description = "Unique id of a patient", example = "125")
  private int patientId;

  @JsonProperty("insurerId")
  @Schema(description = "The id for the patients insurer", example = "2")
  private int insurerId;

  @JsonProperty("physicianId")
  @Schema(description = "The id for the patients physician", example = "8")
  private int physicianId;

  @JsonProperty("appointmentId")
  @Schema(description = "The unique id of the appointment", example = "19")
  private int appointmentId;

  @JsonProperty("roomId")
  @Schema(description = "The id for the room in which patient is allocated. If the room id is -1 "
      + "then the patient is in waiting room.", example = "2")
  private int roomId;

  @JsonProperty("roomPhysicianId")
  @Schema(description = "Id of the physician allocated to the room.", example = "2")
  private Integer roomPhysicianId;

  @JsonProperty("officeId")
  @Schema(description = "The id of the office associated with this appointment", example = "54")
  private int officeId;

  @CheckFutureDate
  @JsonProperty("arrivedTime")
  @Schema(description = "The arrival date time of the patient.",
      example = "2020-01-23T10:15:00.000", type = "string")
  @NotNull
  private LocalDateTime arrivedTime;

  @CheckFutureDate
  @JsonProperty("enteredRoomTime")
  @Schema(description = "Room entered time.")
  private LocalDateTime enteredRoomTime;

  @CheckFutureDate
  @JsonProperty("completeDate")
  @Schema(description = "The completed date time of the wait room entry to the related patient."
      + "If the value is null then the patient is still in traffic manager.",
      example = "2020-01-23T11:15:00.000", type = "string")
  private LocalDateTime completedDate;

  @JsonProperty("lastModifiedDate")
  @Schema(description = "This field is updated by the system.")
  private LocalDateTime lastModifiedDate;

  @Size(max = 1000, message = "Avatars cannot be more than 1000 characters.")
  @JsonProperty("avatars")
  @Schema(
      description = "This field represents the avatars for the rooms which the patient "
          + "has been through. This field is read only.",
      example = "/icons/pill16.png,/icons/Document 2 16.png")
  private String avatars;

  @JsonProperty("cashCollected")
  @Schema(description = "")
  private boolean cashCollected;

  /**
   * The Waitroom id
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
   * The Patient id
   *
   * @return The patientId
   * @documentationExample 123
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The Patient insurer id
   *
   * @return The insurerId
   * @documentationExample 2
   */
  public int getInsurerId() {
    return insurerId;
  }

  public void setInsurerId(int insurerId) {
    this.insurerId = insurerId;
  }

  /**
   * The Patient's physician id
   *
   * @return The physicianId
   * @documentationExample 34
   */
  public int getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(int physicianId) {
    this.physicianId = physicianId;
  }

  /**
   * The Appointment id
   *
   * @return The appointmentId
   * @documentationExample 1
   */
  public int getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * The Room id allocated to patient. If the room id is -1 then the patient is in waiting room.
   *
   * @return The roomId
   * @documentationExample 3
   */
  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  /**
   * The Id of the physician allocated to the room
   *
   * @return The roomPhysicianId
   * @documentationExample 2
   */
  public Integer getRoomPhysicianId() {
    return roomPhysicianId;
  }

  public void setRoomPhysicianId(Integer roomPhysicianId) {
    this.roomPhysicianId = roomPhysicianId;
  }

  /**
   * The Id of the office associated with this appointment
   *
   * @return The officeId
   * @documentationExample 54
   */
  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  /**
   * The Arrival date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getArrivedTime() {
    return arrivedTime;
  }

  public void setArrivedTime(LocalDateTime arrivedTime) {
    this.arrivedTime = arrivedTime;
  }

  /**
   * The Room entered time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getEnteredRoomTime() {
    return enteredRoomTime;
  }

  public void setEnteredRoomTime(LocalDateTime enteredRoomTime) {
    this.enteredRoomTime = enteredRoomTime;
  }

  /**
   * The Completed date time of the wait room entry to the related patient. If the value is null
   * then the patient is still in the traffic manager.
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(LocalDateTime completedDate) {
    this.completedDate = completedDate;
  }

  /**
   * The Last modified date time updated by the system
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /**
   * This field represents the avatars for the rooms which the patient has been through. This field
   * is read only
   *
   * @documentationExample /icons/pilll6.png/,/icons/Document 2 14.png
   * @return The avatars
   */
  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  /**
   * The Cash collected
   *
   * @return {@code true} if collected or {@code false} if not collected
   * @documentationExample True
   */
  public boolean isCashCollected() {
    return cashCollected;
  }

  public void setCashCollected(boolean cashCollected) {
    this.cashCollected = cashCollected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WaitRoomEntryDto entryDto = (WaitRoomEntryDto) o;

    if (id != entryDto.id) {
      return false;
    }
    if (patientId != entryDto.patientId) {
      return false;
    }
    if (insurerId != entryDto.insurerId) {
      return false;
    }
    if (physicianId != entryDto.physicianId) {
      return false;
    }
    if (appointmentId != entryDto.appointmentId) {
      return false;
    }
    if (roomId != entryDto.roomId) {
      return false;
    }
    if (officeId != entryDto.officeId) {
      return false;
    }
    if (cashCollected != entryDto.cashCollected) {
      return false;
    }
    if (!Objects.equals(roomPhysicianId, entryDto.roomPhysicianId)) {
      return false;
    }
    if (!Objects.equals(arrivedTime, entryDto.arrivedTime)) {
      return false;
    }
    if (!Objects.equals(enteredRoomTime, entryDto.enteredRoomTime)) {
      return false;
    }
    if (!Objects.equals(completedDate, entryDto.completedDate)) {
      return false;
    }
    if (!Objects.equals(lastModifiedDate, entryDto.lastModifiedDate)) {
      return false;
    }
    return Objects.equals(avatars, entryDto.avatars);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + patientId;
    result = 31 * result + insurerId;
    result = 31 * result + physicianId;
    result = 31 * result + appointmentId;
    result = 31 * result + roomId;
    result = 31 * result + (roomPhysicianId != null ? roomPhysicianId.hashCode() : 0);
    result = 31 * result + officeId;
    result = 31 * result + (arrivedTime != null ? arrivedTime.hashCode() : 0);
    result = 31 * result + (enteredRoomTime != null ? enteredRoomTime.hashCode() : 0);
    result = 31 * result + (completedDate != null ? completedDate.hashCode() : 0);
    result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
    result = 31 * result + (avatars != null ? avatars.hashCode() : 0);
    result = 31 * result + (cashCollected ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("WaitRoomEntryDto{");
    sb.append("id=").append(id);
    sb.append(", patientId=").append(patientId);
    sb.append(", insurerId=").append(insurerId);
    sb.append(", physicianId=").append(physicianId);
    sb.append(", appointmentId=").append(appointmentId);
    sb.append(", roomId=").append(roomId);
    sb.append(", roomPhysicianId=").append(roomPhysicianId);
    sb.append(", officeId=").append(officeId);
    sb.append(", arrivedTime=").append(arrivedTime);
    sb.append(", enteredRoomTime=").append(enteredRoomTime);
    sb.append(", completedDate=").append(completedDate);
    sb.append(", lastModifiedDate=").append(lastModifiedDate);
    sb.append(", avatars='").append(avatars).append('\'');
    sb.append(", cashCollected=").append(cashCollected);
    sb.append('}');
    return sb.toString();
  }
}
