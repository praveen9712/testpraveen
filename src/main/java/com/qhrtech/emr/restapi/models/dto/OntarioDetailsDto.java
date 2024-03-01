
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Objects;

/**
 * Ontario specific patient details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Ontario specific patient details.",
    implementation = OntarioDetailsDto.class, name = "OntarioDetailsDto")
public class OntarioDetailsDto {

  @JsonProperty("validationStatus")
  @Schema(description = "The valid flag", example = "true")
  private boolean validationStatus;

  @JsonProperty("validationMessage")
  @Schema(description = "The validation message", example = "valid", type = "string",
      maxLength = 150)
  private String validationMessage;

  @JsonProperty("admissionDate")
  @Schema(description = "The admission date", example = "2000-05-31 00:00:00.000")
  private Calendar admissionDate;

  @JsonProperty("dischargeDate")
  @Schema(description = "The discharge date", example = "2000-05-31 00:00:00.000")
  private Calendar dischargeDate;

  @JsonProperty("masterNumber")
  @Schema(description = "The master number", example = "123", type = "integer",
      maximum = "999999999")
  private Integer masterNumber;

  /**
   * Valid Flag
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isValidationStatus() {
    return validationStatus;
  }

  public void setValidationStatus(boolean validationStatus) {
    this.validationStatus = validationStatus;
  }

  /**
   * Validation Message
   *
   * @return A message
   * @documentationExample A message
   */
  public String getValidationMessage() {
    return validationMessage;
  }

  public void setValidationMessage(String validationMessage) {
    this.validationMessage = validationMessage;
  }

  /**
   * Admission Date
   *
   * @return Admission date
   */
  @DocumentationExample("2000-05-31 00:00:00.000")
  @TypeHint(String.class)
  public Calendar getAdmissionDate() {
    return admissionDate;
  }

  public void setAdmissionDate(Calendar admissionDate) {
    this.admissionDate = admissionDate;
  }

  /**
   * Discharge Date
   *
   * @return discharge date
   */
  @DocumentationExample("2000-05-31 00:00:00.000")
  @TypeHint(String.class)
  public Calendar getDischargeDate() {
    return dischargeDate;
  }

  public void setDischargeDate(Calendar dischargeDate) {
    this.dischargeDate = dischargeDate;
  }

  /**
   * Master Number
   *
   * @return Master number
   * @documentationExample 123
   */
  public Integer getMasterNumber() {
    return masterNumber;
  }

  public void setMasterNumber(Integer masterNumber) {
    this.masterNumber = masterNumber;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + (this.validationStatus ? 1 : 0);
    hash = 89 * hash + Objects.hashCode(this.validationMessage);
    hash = 89 * hash + Objects.hashCode(this.admissionDate);
    hash = 89 * hash + Objects.hashCode(this.dischargeDate);
    hash = 89 * hash + Objects.hashCode(this.masterNumber);
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
    final OntarioDetailsDto other = (OntarioDetailsDto) obj;
    if (this.validationStatus != other.validationStatus) {
      return false;
    }
    if (!Objects.equals(this.validationMessage, other.validationMessage)) {
      return false;
    }
    if (!Objects.equals(this.admissionDate, other.admissionDate)) {
      return false;
    }
    if (!Objects.equals(this.dischargeDate, other.dischargeDate)) {
      return false;
    }
    if (!Objects.equals(this.masterNumber, other.masterNumber)) {
      return false;
    }
    return true;
  }

}
