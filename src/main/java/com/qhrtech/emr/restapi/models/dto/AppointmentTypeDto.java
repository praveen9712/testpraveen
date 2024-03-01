
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.awt.Color;
import java.util.Objects;

/**
 * The Appointment Type model
 *
 * @see com.qhrtech.emr.accuro.model.scheduling.AppointmentType
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The appointment type model")
public class AppointmentTypeDto {

  @JsonProperty("typeId")
  @Schema(description = "The appointment type id", example = "1")
  private int typeId;

  @JsonProperty("name")
  @Schema(description = "The appointment type name", example = "Walk-In")
  private String name;

  @JsonProperty("color")
  @Schema(description = "Indicated color of the appointment type",
      implementation = Color.class)
  private Color color;


  @Schema(
      description = "Indicates if the appointment type object is shared between multiple offices",
      example = "true")
  @JsonProperty("shared")
  private boolean shared;

  @Schema(description = "The office id", example = "1")
  @JsonProperty("officeId")
  private int officeId;

  /**
   * A unique appointment type ID.
   *
   * @documentationExample 1
   *
   * @return A Type ID
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * A name for the appointment Type object
   *
   * @documentationExample E-Booking
   *
   * @return An appointment type name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The color value for the appointment type.
   *
   * @documentationExample #33CCFF
   *
   * @return Color value
   */
  @DocumentationExample("#33CCFF")
  @TypeHint(String.class)
  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Indicates if the appointment type object is shared between multiple offices.
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
   * Unique office ID associated with the Type DTO
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
    int hash = 3;
    hash = 59 * hash + this.typeId;
    hash = 59 * hash + Objects.hashCode(this.name);
    hash = 59 * hash + Objects.hashCode(this.color);
    hash = 59 * hash + (this.shared ? 1 : 0);
    hash = 59 * hash + this.officeId;
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
    final AppointmentTypeDto other = (AppointmentTypeDto) obj;
    if (this.typeId != other.typeId) {
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
    if (!Objects.equals(this.color, other.color)) {
      return false;
    }
    return true;
  }

}
