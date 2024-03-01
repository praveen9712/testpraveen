
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Appointment Reason object.
 *
 * @see com.qhrtech.emr.accuro.model.scheduling.AppointmentReason
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The appointment reason data transfer object")
public class AppointmentReasonDto {

  @JsonProperty("reasonId")
  @Schema(description = "The unique appointment reason id", example = "1")
  private int reasonId;

  @JsonProperty("name")
  @Schema(description = "The name of the appointment reason", example = "Checkup")
  private String name;

  @JsonProperty("physicianId")
  @Schema(description = "The unique id for the physician associated with this reason. "
      + "Non-provider specific reasons will have this field set to null.", example = "10025")
  private Integer physicianId;

  @JsonProperty("shared")
  @Schema(description = "Indicates if the reason object is shared between all offices",
      example = "false")
  private boolean shared;

  @JsonProperty("officeId")
  @Schema(description = "The unique id of the office associated with the reason", example = "1")
  private int officeId;

  /**
   * A unique reason ID
   *
   * @documentationExample 1
   *
   * @return The Reason ID
   */
  public int getReasonId() {
    return reasonId;
  }

  public void setReasonId(int reasonId) {
    this.reasonId = reasonId;
  }

  /**
   * A name for the Reason object
   *
   * @documentationExample Checkup
   *
   * @return A reason name String
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * <p>
   * The unique ID for the physician associated with this reason.
   * </p>
   * <p>
   * Non-provider specific reasons will have this field set to null.
   * </p>
   *
   * @documentationExample 1
   *
   * @return A Physician ID
   */
  public Integer getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(Integer physicianId) {
    this.physicianId = physicianId;
  }

  /**
   * Indicates if the Reason object is shared between all offices.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if the reason is shared between offices, or <code>false</code> if the
   *         reason is not shared.
   */
  public boolean isShared() {
    return shared;
  }

  public void setShared(boolean shared) {
    this.shared = shared;
  }

  /**
   * Unique office ID associated with the Reason DTO
   *
   * @documentationExample 1
   *
   * @return An Office ID.
   */
  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + this.reasonId;
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Objects.hashCode(this.physicianId);
    hash = 79 * hash + (this.shared ? 1 : 0);
    hash = 79 * hash + this.officeId;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AppointmentReasonDto other = (AppointmentReasonDto) obj;
    if (this.reasonId != other.reasonId) {
      return false;
    }
    if (this.shared != other.shared) {
      return false;
    }
    if (this.officeId != other.officeId) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.physicianId, other.physicianId)) {
      return false;
    }
    return true;
  }

}
