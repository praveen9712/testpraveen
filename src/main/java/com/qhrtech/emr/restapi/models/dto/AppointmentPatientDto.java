
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.Objects;

/**
 * The AppointmentPatient data transfer object. This is used to store aggregated patient data
 * relating to appointment materials.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentPatientDto {

  @JsonProperty("patient")
  @Schema(description = "The patient")
  private PatientDto patient;

  @JsonProperty("arrived")
  @Schema(description = "Arrived status for this patient", example = "true")
  private boolean arrived;

  @JsonProperty("confirmed")
  @Schema(description = "Confirmed status for this patient", example = "true")
  private boolean confirmed;

  @JsonProperty("noShow")
  @Schema(description = "No show status for this patient", example = "true")
  private Boolean noShow;

  @JsonProperty("customProperties")
  @Schema(description = "Custom properties for this patient")
  private Map<String, String> customProperties;

  /**
   * Underlying patient record.
   *
   * @return The underlying patient record.
   */
  public PatientDto getPatient() {
    return patient;
  }

  public void setPatient(PatientDto patient) {
    this.patient = patient;
  }

  /**
   * Arrived Status for this Patient
   *
   * @return If the patient has arrived for the appointment or not.
   *
   * @documentationExample true
   */
  public boolean isArrived() {
    return arrived;
  }

  public void setArrived(boolean arrived) {
    this.arrived = arrived;
  }

  /**
   * Confirmed Status for this Patient
   *
   * @return If the appointment has been confirmed by the clinic
   *
   * @documentationExample false
   */
  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  /**
   * No Show Status for this Patient
   *
   * @return If the appointment has been as a No Show by the clinic
   *
   * @documentationExample false
   */
  public Boolean getNoShow() {
    return noShow;
  }

  public void setNoShow(Boolean noShow) {
    this.noShow = noShow;
  }

  /**
   * Custom Properties for this Patient
   *
   * @return Custom Properties
   *
   * @documentationExample managing_organization western
   */
  public Map<String, String> getCustomProperties() {
    return customProperties;
  }

  public void setCustomProperties(Map<String, String> customProperties) {
    this.customProperties = customProperties;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + Objects.hashCode(this.patient);
    hash = 59 * hash + (this.arrived ? 1 : 0);
    hash = 59 * hash + (this.confirmed ? 1 : 0);
    hash = 59 * hash + Objects.hashCode(this.noShow);
    hash = 59 * hash + Objects.hashCode(this.customProperties);
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
    final AppointmentPatientDto other = (AppointmentPatientDto) obj;
    if (this.arrived != other.arrived) {
      return false;
    }
    if (this.confirmed != other.confirmed) {
      return false;
    }
    if (!Objects.equals(this.patient, other.patient)) {
      return false;
    }
    if (!Objects.equals(this.noShow, other.noShow)) {
      return false;
    }
    return Objects.equals(this.customProperties, other.customProperties);
  }


  @Override
  public String toString() {
    return "AppointmentPatientDto{"
        + "patient=" + patient
        + ", arrived=" + arrived
        + ", confirmed=" + confirmed
        + ", noShow=" + noShow
        + ", customProperties=" + (customProperties == null ? "" : customProperties.toString())
        + '}';
  }
}
