
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.awt.Color;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The Appointment status object.
 *
 * @see com.qhrtech.emr.accuro.model.scheduling.Status
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The appointment status data transfer object")
public class StatusDto {

  @JsonProperty("statusId")
  @Schema(description = "The unique status id", example = "1")
  private int statusId;

  @NotNull(message = "The status name can not be null.")
  @Size(max = 50, message = "The status name can not exceed 50 characters.")
  @JsonProperty("statusName")
  @Schema(description = "The name for the status", example = "No Show")
  private String statusName;

  @NotNull(message = "The status abbreviation can not be null.")
  @Size(max = 2, message = "The status abbreviation can not exceed 2 characters.")
  @JsonProperty("statusAbbreviation")
  @Schema(description = "The one or 2 character abbreviation for the status name", example = "NS")
  private String statusAbbreviation;

  @JsonProperty("shape")
  @Schema(description = "The shape value for the appointment status")
  private Shape shape;

  @JsonProperty("color")
  @Schema(description = "The color for the appointment status", example = "#33CCFF")
  private Color color;

  /**
   * A unique Status ID
   *
   * @documentationExample 1
   *
   * @return The Status ID
   */
  public int getStatusId() {
    return statusId;
  }

  public void setStatusId(int statusId) {
    this.statusId = statusId;
  }

  /**
   * A name for the Status object
   *
   * @documentationExample No Show
   *
   * @return A Status name
   */
  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  /**
   * A one or 2 character abbreviation for the Status name
   *
   * @documentationExample NS
   *
   * @return An abbreviation
   */
  public String getStatusAbbreviation() {
    return statusAbbreviation;
  }

  public void setStatusAbbreviation(String statusAbbreviation) {
    this.statusAbbreviation = statusAbbreviation;
  }

  /**
   * A shape value for the appointment status.
   *
   * @return A shape value
   */
  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
  }

  /**
   * 6 character hexadecimal color value for the appointment status.
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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + this.statusId;
    hash = 53 * hash + Objects.hashCode(this.statusName);
    hash = 53 * hash + Objects.hashCode(this.statusAbbreviation);
    hash = 53 * hash + Objects.hashCode(this.shape);
    hash = 53 * hash + Objects.hashCode(this.color);
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
    final StatusDto other = (StatusDto) obj;
    if (this.statusId != other.statusId) {
      return false;
    }
    if (!Objects.equals(this.statusName, other.statusName)) {
      return false;
    }
    if (!Objects.equals(this.statusAbbreviation, other.statusAbbreviation)) {
      return false;
    }
    if (this.shape != other.shape) {
      return false;
    }
    if (!Objects.equals(this.color, other.color)) {
      return false;
    }
    return true;
  }

}
