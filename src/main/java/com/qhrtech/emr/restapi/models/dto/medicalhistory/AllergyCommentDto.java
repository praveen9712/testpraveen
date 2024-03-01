
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;

/**
 * Allergy comments represent the data stored for each comment on a
 * {@link com.qhrtech.emr.accuro.model.allergy.PatientAllergy}. This contains information such as
 * the source and time of the comment.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Allergy comment data transfer object model")
public class AllergyCommentDto {

  @JsonProperty("commentId")
  @Schema(description = "The id of the allergy comment", example = "2")
  private int commentId;

  @JsonProperty("accuroUserId")
  @Schema(description = "User id of the person who commented", example = "10")
  private int accuroUserId;

  @JsonProperty("patientAllergyId")
  @Schema(description = "The patient allergy id", example = "200")
  private int patientAllergyId;

  @JsonProperty("creationDate")
  @Schema(description = "The comment date", example = "2018-01-01T00:00:00.000-0800")
  private Calendar creationDate;

  @JsonProperty("comment")
  @Schema(description = "The content of the comment related to allergy",
      example = "This content is limited to 2000 characters")
  private String comment;

  /**
   * The comment id of a patient allergy comment.
   *
   * @documentationExample 1
   *
   * @return The comment id.
   */
  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  /**
   * The user id of the person who comments.
   *
   * @documentationExample 11
   *
   * @return The user id.
   */
  public int getAccuroUserId() {
    return accuroUserId;
  }

  public void setAccuroUserId(int accuroUserId) {
    this.accuroUserId = accuroUserId;
  }

  /**
   * The patient allergy id.
   *
   * @documentationExample 11
   * @return The patient allergy id.
   *
   */
  public int getPatientAllergyId() {
    return patientAllergyId;
  }

  public void setPatientAllergyId(int patientAllergyId) {
    this.patientAllergyId = patientAllergyId;
  }

  /**
   * The comment date.
   *
   * @return The date the comment was created.
   */
  @DocumentationExample("2018-01-01T00:00:00.000-0800")
  @TypeHint(String.class)
  public Calendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * The comment content of an allergy.
   *
   * @documentationExample suffering from.
   *
   * @return The content of the comment. This string is limited to 2000 characters long.
   */
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AllergyCommentDto)) {
      return false;
    }

    AllergyCommentDto that = (AllergyCommentDto) o;

    if (commentId != that.commentId) {
      return false;
    }
    if (accuroUserId != that.accuroUserId) {
      return false;
    }
    if (patientAllergyId != that.patientAllergyId) {
      return false;
    }
    if (creationDate != null ? !creationDate.equals(that.creationDate)
        : that.creationDate != null) {
      return false;
    }
    return comment != null ? comment.equals(that.comment) : that.comment == null;
  }

  @Override
  public int hashCode() {
    int result = commentId;
    result = 31 * result + accuroUserId;
    result = 31 * result + patientAllergyId;
    result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
    result = 31 * result + (comment != null ? comment.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AllergyCommentDto{"
        + "commentId=" + commentId
        + ", accuroUserId=" + accuroUserId
        + ", patientAllergyId=" + patientAllergyId
        + ", creationDate=" + creationDate
        + ", comment='" + comment + '\''
        + '}';
  }
}
