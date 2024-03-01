
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Address data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.allergy.AllergyReaction
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Allergy reaction data transfer object model")
public class AllergyReactionDto {

  @JsonProperty(value = "reactionId")
  @Schema(description = "The id of the allergic reaction", example = "1")
  private int reactionId;

  @JsonProperty(value = "patientAllergyId")
  @Schema(description = "The id of the patient allergy", example = "2")
  private int patientAllergyId;

  @JsonProperty(value = "reactionCode")
  @Schema(description = "The code of the allergic reaction")
  private ReactionCode reactionCode;

  @JsonProperty(value = "severityCode")
  @Schema(description = "The severity code")
  private SeverityCode severityCode;

  @JsonProperty(value = "description")
  @Schema(description = "The description of the allergic reaction", example = "some description")
  private String description;

  /**
   * A unique reaction id of an allergy reaction.
   *
   * @documentationExample 1
   * 
   * @return The id.
   */
  public int getReactionId() {
    return reactionId;
  }

  public void setReactionId(int reactionId) {
    this.reactionId = reactionId;
  }

  /**
   * The severity of an allergic reaction.{@link SeverityCode}
   *
   * @documentationExample 'MO' for moderate status.
   * 
   * @return the severity.
   */
  public SeverityCode getSeverityCode() {
    return severityCode;
  }

  public void setSeverityCode(SeverityCode severityCode) {
    this.severityCode = severityCode;
  }

  /**
   * The type of reaction for the allergic reaction.{@link ReactionCode}
   * 
   * @documentationExample RashHives
   * 
   * @return The reaction code.
   */
  public ReactionCode getReactionCode() {
    return reactionCode;
  }

  public void setReactionCode(ReactionCode reactionCode) {
    this.reactionCode = reactionCode;
  }

  /**
   * The id of the patient allergy associated with this allergy reaction.
   * 
   * @documentationExample 1
   * 
   * @return The patient allergy id.
   */
  public int getPatientAllergyId() {
    return patientAllergyId;
  }

  public void setPatientAllergyId(int patientAllergyId) {
    this.patientAllergyId = patientAllergyId;
  }

  /**
   * The descriptive text for the AllergyReaction.
   * 
   * @documentationExample some description
   * 
   * @return A string of descriptive text.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AllergyReactionDto{");
    sb.append("reactionId=").append(reactionId);
    sb.append(", patientAllergyId=").append(patientAllergyId);
    sb.append(", reactionCode=").append(reactionCode);
    sb.append(", severityCode=").append(severityCode);
    sb.append(", description='").append(description).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AllergyReactionDto)) {
      return false;
    }

    AllergyReactionDto that = (AllergyReactionDto) o;

    if (reactionId != that.reactionId) {
      return false;
    }
    if (patientAllergyId != that.patientAllergyId) {
      return false;
    }
    if (reactionCode != that.reactionCode) {
      return false;
    }
    if (severityCode != that.severityCode) {
      return false;
    }
    return Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    int result = reactionId;
    result = 31 * result + patientAllergyId;
    result = 31 * result + (reactionCode != null ? reactionCode.hashCode() : 0);
    result = 31 * result + (severityCode != null ? severityCode.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
