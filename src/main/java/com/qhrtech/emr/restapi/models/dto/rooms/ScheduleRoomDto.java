
package com.qhrtech.emr.restapi.models.dto.rooms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleRoomDto implements Serializable {

  @JsonProperty("id")
  @Schema(description = "The id of the schedule room", example = "1")
  private int id;

  @NotBlank(message = "The schedule room's name cannot be null or blank")
  @Size(max = 100, message = "The length of the name cannot be over 100")
  @Schema(description = "The name of the schedule room", example = "Lab")
  @JsonProperty("name")
  private String name;

  @JsonProperty("order")
  @Schema(description = "The order of the schedule room", example = "1")
  private int order;

  @Size(max = 3, message = "The length of the abbreviation cannot be over 3")
  @JsonProperty("abbreviation")
  @Schema(description = "The abbreviation of the schedule room", example = "Big")
  private String abbreviation;

  @JsonProperty("officeId")
  @Schema(description = "The office id of the schedule room", example = "123")
  private int officeId;

  @JsonProperty("showAll")
  @Schema(description = "The flag if shows patients for all arrived dates", example = "true")
  private boolean showAll;

  @Size(max = 1000, message = "The length of the kiosk message cannot be over 1000")
  @JsonProperty("kioskMessage")
  @Schema(description = "The kiosk message", example = "Test message")
  private String kioskMessage;

  @Size(max = 100, message = "The length of the note cannot be over 100")
  @JsonProperty("tmNote")
  @Schema(description = "The note of the schedule room in the traffic manager",
      example = "Lab is on 2nd floor")
  private String tmNote;

  @JsonProperty("tmPhysician")
  @Schema(
      description = "The physician id of the schedule room. If it is null it will be saved as 0.")
  private int tmPhysician;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  public boolean isShowAll() {
    return showAll;
  }

  public void setShowAll(boolean showAll) {
    this.showAll = showAll;
  }

  public String getKioskMessage() {
    return kioskMessage;
  }

  public void setKioskMessage(String kioskMessage) {
    this.kioskMessage = kioskMessage;
  }

  public String getTmNote() {
    return tmNote;
  }

  public void setTmNote(String tmNote) {
    this.tmNote = tmNote;
  }

  public int getTmPhysician() {
    return tmPhysician;
  }

  public void setTmPhysician(int tmPhysician) {
    this.tmPhysician = tmPhysician;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScheduleRoomDto)) {
      return false;
    }
    ScheduleRoomDto that = (ScheduleRoomDto) o;
    if (getId() != that.getId()) {
      return false;
    }
    if (getOrder() != that.getOrder()) {
      return false;
    }
    if (getOfficeId() != that.getOfficeId()) {
      return false;
    }
    if (isShowAll() != that.isShowAll()) {
      return false;
    }
    if (getTmPhysician() != that.getTmPhysician()) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getAbbreviation() != null ? !getAbbreviation().equals(that.getAbbreviation())
        : that.getAbbreviation() != null) {
      return false;
    }
    if (getKioskMessage() != null ? !getKioskMessage().equals(that.getKioskMessage())
        : that.getKioskMessage() != null) {
      return false;
    }
    return getTmNote() != null ? getTmNote().equals(that.getTmNote()) : that.getTmNote() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + getOrder();
    result = 31 * result + (getAbbreviation() != null ? getAbbreviation().hashCode() : 0);
    result = 31 * result + getOfficeId();
    result = 31 * result + (isShowAll() ? 1 : 0);
    result = 31 * result + (getKioskMessage() != null ? getKioskMessage().hashCode() : 0);
    result = 31 * result + (getTmNote() != null ? getTmNote().hashCode() : 0);
    result = 31 * result + getTmPhysician();
    return result;
  }
}
