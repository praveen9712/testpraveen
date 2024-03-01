
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientLetter data transfer object model")
public class NewPatientLetterDto {

  // Type ID
  @JsonProperty("typeId")
  @Schema(description = "Type id", example = "1")
  private Integer typeId;

  // Patient ID
  @JsonProperty("patientId")
  @Schema(description = "Patient id", example = "1")
  private Integer patientId;

  // Provider ID
  @JsonProperty("providerId")
  @Schema(description = "Provider id", example = "1")
  private int providerId;

  // Appointment ID
  @JsonProperty("appointmentId")
  @Schema(description = "Appointment id", example = "1")
  private Integer appointmentId;

  // Letter Title
  @JsonProperty("title")
  @Schema(description = "Title of the letter. The title must be set when creating a letter.",
      example = "CAGE - Alcohol Screening Questionnaire")
  private String title;

  // Letter Styled Content
  @JsonProperty("content")
  @Schema(
      name = "content",
      description = "Content of the letter of any plain or RTF text. The Content must not be null.")
  private String contentText;

  /**
   * Type ID
   *
   * @documentationExample 1
   *
   * @return Type ID
   */
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  /**
   * Patient ID
   *
   * @documentationExample 1
   *
   * @return Patient ID
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Provider ID
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
   * Appointment ID
   *
   * @documentationExample 1
   *
   * @return Appointment ID
   */
  public Integer getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Integer appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * Title of the letter. The title must be set when creating a letter.
   *
   * @documentationExample CAGE - Alcohol Screening Questionnaire
   *
   * @return Letter tittle
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Content of the letter of any plain or RTF text. The Content must not be null.
   *
   * @documentationExample {\rtf1\ansi\n{\fonttbl\f0\fnil
   *                       Monospaced;}\n{\colortbl\red0\green0\blue0; \red166\green169\blue0;
   *                       \red0\green0\blue170;}\n\b Your content here --\~ \n}\n\u0000
   *
   * @return Letter content
   */
  public String getContentText() {
    return contentText;
  }

  public void setContentText(String contentText) {
    this.contentText = contentText;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.typeId);
    hash = 37 * hash + Objects.hashCode(this.patientId);
    hash = 37 * hash + this.providerId;
    hash = 37 * hash + Objects.hashCode(this.appointmentId);
    hash = 37 * hash + Objects.hashCode(this.title);
    hash = 37 * hash + Objects.hashCode(this.contentText);
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
    final NewPatientLetterDto other = (NewPatientLetterDto) obj;
    if (this.providerId != other.providerId) {
      return false;
    }
    if (!Objects.equals(this.title, other.title)) {
      return false;
    }
    if (!Objects.equals(this.contentText, other.contentText)) {
      return false;
    }
    if (!Objects.equals(this.typeId, other.typeId)) {
      return false;
    }
    if (!Objects.equals(this.patientId, other.patientId)) {
      return false;
    }

    return Objects.equals(this.appointmentId, other.appointmentId);
  }

  @Override
  public String toString() {
    return "NewPatientLetterDto{"
        + "typeId=" + typeId
        + ", patientId=" + patientId
        + ", providerId=" + providerId
        + ", appointmentId=" + appointmentId
        + ", title=" + title
        + ", content=" + contentText + '}';
  }

}
