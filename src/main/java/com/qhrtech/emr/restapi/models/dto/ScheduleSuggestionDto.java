
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.awt.Color;
import java.util.Objects;

/**
 * Model object for the schedule suggestions
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Schedule suggestion data transfer object model")
public class ScheduleSuggestionDto {

  @JsonProperty("name")
  @Schema(description = "Suggestion Name", example = "General Checkups")
  private String name;

  @JsonProperty("color")
  @Schema(description = "The hex color code for the suggestions label", example = "#FFFFFF")
  private Color color;

  @JsonProperty("abbreviation")
  @Schema(description = "Suggestion Abbreviation", example = "CH")
  private String abbreviation;

  @JsonProperty("typeId")
  @Schema(description = "Appointment Type ID", example = "1")
  private Integer typeId;

  @JsonProperty("reasonId")
  @Schema(description = "Appointment Reason ID", example = "2")
  private Integer reasonId;

  @JsonProperty("allowProviderCreation")
  @Schema(description = "Allow Provider Creation", example = "true")
  private boolean allowProviderCreation;

  @JsonProperty("allowMedeoEBookingRequests")
  @Schema(description = "Allow Medeo E-Booking Request Creation", example = "true")
  private boolean allowMedeoEBookingRequests;

  @JsonProperty("barDisplay")
  @Schema(description = "Is there a Bar Display", example = "true")
  private boolean barDisplay;

  @JsonProperty("active")
  @Schema(description = "Indication if the Schedule Suggestion is Active", example = "true")
  private boolean active;

  /**
   * Suggestion Name
   *
   * @documentationExample General Checkups
   *
   * @return Suggestion name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The hex color code for the suggestions label
   *
   * @documentationExample #FFFFFF
   *
   * @return Color
   */
  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Suggestion Abbreviation
   *
   * @documentationExample CH
   *
   * @return Abbreviation
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * Appointment Type ID
   *
   * @documentationExample 1
   *
   * @return Appointment Type ID
   */
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  /**
   * Appointment Reason ID
   *
   * @documentationExample 2
   *
   * @return Appointment Reason ID
   */
  public Integer getReasonId() {
    return reasonId;
  }

  public void setReasonId(Integer reasonId) {
    this.reasonId = reasonId;
  }

  /**
   * Allow Provider Creation
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isAllowProviderCreation() {
    return allowProviderCreation;
  }

  public void setAllowProviderCreation(boolean allowProviderCreation) {
    this.allowProviderCreation = allowProviderCreation;
  }

  /**
   * Allow Medeo E-Booking Request Creation
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isAllowMedeoEBookingRequests() {
    return allowMedeoEBookingRequests;
  }

  public void setAllowMedeoEBookingRequests(boolean allowMedeoEBookingRequests) {
    this.allowMedeoEBookingRequests = allowMedeoEBookingRequests;
  }

  /**
   * Is there a Bar Display
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isBarDisplay() {
    return barDisplay;
  }

  public void setBarDisplay(boolean barDisplay) {
    this.barDisplay = barDisplay;
  }

  /**
   * Indication if the Schedule Suggestion is Active
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + Objects.hashCode(this.name);
    hash = 17 * hash + Objects.hashCode(this.color);
    hash = 17 * hash + Objects.hashCode(this.abbreviation);
    hash = 17 * hash + Objects.hashCode(this.typeId);
    hash = 17 * hash + Objects.hashCode(this.reasonId);
    hash = 17 * hash + (this.allowProviderCreation ? 1 : 0);
    hash = 17 * hash + (this.allowMedeoEBookingRequests ? 1 : 0);
    hash = 17 * hash + (this.barDisplay ? 1 : 0);
    hash = 17 * hash + (this.active ? 1 : 0);
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
    final ScheduleSuggestionDto other = (ScheduleSuggestionDto) obj;
    if (this.allowProviderCreation != other.allowProviderCreation) {
      return false;
    }
    if (this.allowMedeoEBookingRequests != other.allowMedeoEBookingRequests) {
      return false;
    }
    if (this.barDisplay != other.barDisplay) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.abbreviation, other.abbreviation)) {
      return false;
    }
    if (!Objects.equals(this.color, other.color)) {
      return false;
    }
    if (!Objects.equals(this.typeId, other.typeId)) {
      return false;
    }
    if (!Objects.equals(this.reasonId, other.reasonId)) {
      return false;
    }
    return true;
  }

}
