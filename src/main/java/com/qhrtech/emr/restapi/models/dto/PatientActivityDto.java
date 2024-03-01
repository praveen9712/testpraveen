
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.util.Calendar;

/**
 * Patient activity event.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientActivityDto {

  @JsonProperty("patientId")
  private int patientId;

  @JsonProperty("recordId")
  private String recordId;

  @Override
  public boolean equals(Object o) {
    PatientActivityDto that = (PatientActivityDto) o;
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (patientId != that.patientId) {
      return false;
    }
    if (recordId != null ? !recordId.equals(that.recordId) : that.recordId != null) {
      return false;
    }
    if (timeUtc != null ? !timeUtc.getTime().equals(that.timeUtc.getTime())
        : that.timeUtc != null) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    return description != null ? description.equals(that.description) : that.description == null;
  }

  @Override
  public int hashCode() {
    int result = patientId;
    result = 31 * result + (recordId != null ? recordId.hashCode() : 0);
    result = 31 * result + (timeUtc != null ? timeUtc.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

  @JsonProperty("timeUtc")
  private Calendar timeUtc;

  @JsonProperty("type")
  private PatientActivityType type;

  @JsonProperty("description")
  private String description;

  /**
   * Patient ID
   *
   * @return Patient ID
   * @documentationExample 1
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * Record ID
   *
   * @return Record ID
   * @documentationExample 1
   */
  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  /**
   * Activity time as UTC time
   *
   * @return UTC time
   */
  @DocumentationExample("2016-02-16T00:00:00.000-0800")
  @TypeHint(String.class)
  public Calendar getTimeUtc() {
    return timeUtc;
  }

  public void setTimeUtc(Calendar timeUtc) {
    this.timeUtc = timeUtc;
  }

  /**
   * Patient activity type
   *
   * @return Activity type
   */
  public PatientActivityType getType() {
    return type;
  }

  public void setType(PatientActivityType type) {
    this.type = type;
  }

  /**
   * Activity description
   *
   * @return Description
   * @documentationExample A description of the patient activity.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
