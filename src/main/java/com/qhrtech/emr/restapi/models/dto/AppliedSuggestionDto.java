
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Objects;

/**
 * Applied suggestions model object.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Applied suggestion data transfer object model")
public class AppliedSuggestionDto {

  @JsonProperty("name")
  @Schema(description = "The name of the applied suggestion", example = "Checkup")
  private String name;

  @JsonProperty("providerId")
  @Schema(description = "The provider id", example = "1")
  private Integer providerId;

  @JsonProperty("resourceId")
  @Schema(description = "The resource id", example = "1")
  private Integer resourceId;

  @JsonProperty("subColumn")
  @Schema(description = "Suggestion sub-column id", example = "0")
  private int subColumn;

  @JsonProperty("date")
  @Schema(description = "The date the suggestion applies for.",
      example = "2018-08-07T00:00:00.000-0700")
  private Calendar date;

  @JsonProperty("startTime")
  @Schema(description = "The start time for the applied suggestion", example = "800")
  private int startTime;

  @JsonProperty("endTime")
  @Schema(description = "The end time for the applied suggestion", example = "815")
  private int endTime;

  @JsonProperty("grouped")
  @Schema(description = "Indication if the Applied Suggestion is grouped together or not.",
      example = "true")
  private boolean grouped;

  @JsonProperty("allowMedeoEBookingRequests")
  @Schema(description = "Is Medeo E-Booking Request Creation Allowed", example = "true")
  private boolean allowMedeoEBookingRequests;

  @JsonProperty("allowProviderCreation")
  @Schema(description = "Is Provider Appointment Creation Allowed", example = "true")
  private boolean allowProviderCreation;

  /**
   * The name of the applied suggestion
   *
   * @documentationExample Checkup
   *
   * @return The suggestion name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The provider id
   *
   * @documentationExample 1
   *
   * @return Provider id
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * The resource id
   *
   * @documentationExample 1
   *
   * @return Resource id
   */
  public Integer getResourceId() {
    return resourceId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  /**
   * Suggestion sub-column id
   *
   * @documentationExample 0
   *
   * @return Sub-column
   */
  public int getSubColumn() {
    return subColumn;
  }

  public void setSubColumn(int subColumn) {
    this.subColumn = subColumn;
  }

  /**
   * The date the suggestion applies for.
   *
   * @return A datetime stamp
   */
  @DocumentationExample("2018-08-07T00:00:00.000-0700")
  @TypeHint(String.class)
  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    this.date = date;
  }

  /**
   * The start time for the applied suggestion
   *
   * @documentationExample 800
   *
   * @return A time stamp
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * The end time for the applied suggestion
   *
   * @documentationExample 815
   *
   * @return A time stamp
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  /**
   * Indication if the Applied Suggestion is grouped together or not.
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public boolean isGrouped() {
    return grouped;
  }

  public void setGrouped(boolean grouped) {
    this.grouped = grouped;
  }

  /**
   * Is Medeo E-Booking Request Creation Allowed
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
   * Is Provider Appointment Creation Allowed
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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + Objects.hashCode(this.providerId);
    hash = 71 * hash + Objects.hashCode(this.resourceId);
    hash = 71 * hash + this.subColumn;
    hash = 71 * hash + Objects.hashCode(this.date);
    hash = 71 * hash + this.startTime;
    hash = 71 * hash + this.endTime;
    hash = 71 * hash + (this.grouped ? 1 : 0);
    hash = 71 * hash + (this.allowMedeoEBookingRequests ? 1 : 0);
    hash = 71 * hash + (this.allowProviderCreation ? 1 : 0);
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
    final AppliedSuggestionDto other = (AppliedSuggestionDto) obj;
    if (this.subColumn != other.subColumn) {
      return false;
    }
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    if (this.grouped != other.grouped) {
      return false;
    }
    if (this.allowMedeoEBookingRequests != other.allowMedeoEBookingRequests) {
      return false;
    }
    if (this.allowProviderCreation != other.allowProviderCreation) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.providerId, other.providerId)) {
      return false;
    }
    if (!Objects.equals(this.resourceId, other.resourceId)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    return true;
  }

}
