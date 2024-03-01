
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@Schema(description = "Annotation data transfer object model")
public class AnnotationDto {

  @JsonProperty("date")
  @Schema(description = "The date, the annotation has been created", type = "string",
      example = "2018-07-13T00:00:00.000")
  private LocalDateTime date;

  @JsonProperty("type")
  @Schema(description = "The note type", example = "Record dosage for an as-directed Rx")
  private String type;

  @JsonProperty("authorizedBy")
  @Schema(description = "Provider name who authorized the note", example = "Doctor, David")
  private String authorizedBy;

  @JsonProperty("enteredBy")
  @Schema(description = "Provider name who entered the note", example = "Doctor, David")
  private String enteredBy;

  @JsonProperty("comments")
  @Schema(description = "Comments by provider to the note type",
      example = "THIS PATIENT TAKES 2 PILLS AT ONCE")
  private String comments;

  /**
   * A date an annotation has been created.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * A note type.
   *
   * @documentationExample Record Dosage for an As-Directed Rx
   *
   * @return
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Provider name who authorized the note.
   *
   * @documentationExample Doctor, David
   *
   * @return
   */
  public String getAuthorizedBy() {
    return authorizedBy;
  }

  public void setAuthorizedBy(String authorizedBy) {
    this.authorizedBy = authorizedBy;
  }

  /**
   * Provider name who entered the note.
   *
   * @documentationExample Doctor, David
   *
   * @return
   */
  public String getEnteredBy() {
    return enteredBy;
  }

  public void setEnteredBy(String enteredBy) {
    this.enteredBy = enteredBy;
  }

  /**
   * Comments by provider to a note type.
   *
   * @documentationExample THIS PATIENT TAKES 2 PILLS AT ONCE
   *
   * @return
   */
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AnnotationDto)) {
      return false;
    }

    AnnotationDto that = (AnnotationDto) o;

    if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) {
      return false;
    }
    if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
      return false;
    }
    if (getAuthorizedBy() != null ? !getAuthorizedBy().equals(that.getAuthorizedBy())
        : that.getAuthorizedBy() != null) {
      return false;
    }
    if (getEnteredBy() != null ? !getEnteredBy().equals(that.getEnteredBy())
        : that.getEnteredBy() != null) {
      return false;
    }
    return getComments() != null ? getComments().equals(that.getComments())
        : that.getComments() == null;
  }

  @Override
  public int hashCode() {
    int result = getDate() != null ? getDate().hashCode() : 0;
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    result = 31 * result + (getAuthorizedBy() != null ? getAuthorizedBy().hashCode() : 0);
    result = 31 * result + (getEnteredBy() != null ? getEnteredBy().hashCode() : 0);
    result = 31 * result + (getComments() != null ? getComments().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AnnotationDto{"
        + "date=" + date
        + ", type='" + type + '\''
        + ", authorizedBy='" + authorizedBy + '\''
        + ", enteredBy='" + enteredBy + '\''
        + ", comments='" + comments + '\''
        + '}';
  }
}
