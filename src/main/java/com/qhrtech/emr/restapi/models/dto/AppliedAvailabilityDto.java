
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.util.Calendar;
import java.util.Objects;

/**
 * Applied Availabilities represent sections of the Accuro scheduler where a provider has indicated
 * they are available in a particular office. Applied Availabilities can influence the office an
 * appointment is created in when the Appointment is created in the Accuro EMR.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppliedAvailabilityDto {

  @JsonProperty("officeId")
  private int officeId;

  @JsonProperty("providerId")
  private int providerId;

  @JsonProperty("date")
  private Calendar date;

  @JsonProperty("startTime")
  private int startTime;

  @JsonProperty("endTime")
  private int endTime;

  /**
   * The office the provider is available in for the duration of the applied availability.
   *
   * @documentationExample 1
   *
   * @return Office ID
   */
  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  /**
   * The Id of the provider whose schedule this availability is applied to.
   *
   * @documentationExample 1
   *
   * @return Provider ID
   */
  public int getProviderId() {
    return providerId;
  }

  public void setProviderId(int providerId) {
    this.providerId = providerId;
  }

  /**
   * The date of the applied availability.
   *
   * @return A Calendar object
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
   * The start time for the applied availability.
   *
   * @documentationExample 1400
   *
   * @return A time number
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * The end time for the applied availability.
   *
   * @documentationExample 1415
   *
   * @return A time number
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + this.officeId;
    hash = 37 * hash + this.providerId;
    hash = 37 * hash + Objects.hashCode(this.date);
    hash = 37 * hash + this.startTime;
    hash = 37 * hash + this.endTime;
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
    final AppliedAvailabilityDto other = (AppliedAvailabilityDto) obj;
    if (this.officeId != other.officeId) {
      return false;
    }
    if (this.providerId != other.providerId) {
      return false;
    }
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    return true;
  }

}
