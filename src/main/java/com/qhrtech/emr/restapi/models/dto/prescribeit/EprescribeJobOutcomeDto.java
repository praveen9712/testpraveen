
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTime2Range;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class EprescribeJobOutcomeDto {

  @JsonProperty("id")
  @Schema(description = "Eprescribe Job Outcome Id", example = "12")
  private int id;

  @NotNull
  @JsonProperty("outcomeCodeId")
  @Schema(description = "Eprescribe Job Outcome Code Id. Valid outcome code ID required.",
      example = "12")
  private Integer outcomeCodeId;

  @JsonProperty("previousMessageHeaderId")
  @Schema(description = "Previous Message Header Id", example = "12")
  @Size(max = 36, message = "Maximum size allowed is 36 characters.")
  private String previousMessageHeaderId;

  @JsonProperty("responseCode")
  @Schema(description = "Response Code", example = "12")
  @Size(max = 255, message = "Maximum size allowed is 255 characters.")
  private String responseCode;

  @JsonProperty("errorSeverity")
  @Schema(description = "Error Severity", example = "FATAL")
  @Size(max = 255, message = "Maximum size allowed is 255 characters.")
  private String errorSeverity;

  @JsonProperty("code")
  @Schema(description = "Code", example = "123")
  @Size(max = 255, message = "Maximum size allowed is 255 characters.")
  private String code;

  @JsonProperty("title")
  @Schema(description = "Title", example = "Outcome Title")
  private String title;

  @JsonProperty("detail")
  @Schema(description = "Details", example = "This is test outcome")
  private String detail;

  @NotNull
  @CheckLocalDateTime2Range
  @JsonProperty("timestamp")
  @Schema(description = "The updated local datetime", example = "2012-02-15T07:44:59.000")
  private LocalDateTime timestamp;

  @JsonProperty("createdAt")
  @CheckLocalDateTime2Range
  @Schema(description = "The created datetime(read-only)", example = "2012-02-15T07:44:59.000")
  private LocalDateTime createdAt;

  /**
   * Eprescribe Job Outcome Id. Valid outcome code ID required.
   *
   * @documentationExample 12
   * 
   * @return Eprescribe Job Outcome Id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Eprescribe Job Outcome Code Id
   * 
   * @documentationExample 12
   * 
   * @return Eprescribe Job Outcome Code Id
   */
  public Integer getOutcomeCodeId() {
    return outcomeCodeId;
  }

  public void setOutcomeCodeId(Integer outcomeCodeId) {
    this.outcomeCodeId = outcomeCodeId;
  }

  /**
   * Previous Message Header Id
   * 
   * @documentationExample 12
   * 
   * @return Previous Message Header Id
   */
  public String getPreviousMessageHeaderId() {
    return previousMessageHeaderId;
  }

  public void setPreviousMessageHeaderId(String previousMessageHeaderId) {
    this.previousMessageHeaderId = previousMessageHeaderId;
  }

  /**
   * Response Code
   * 
   * @documentationExample 12
   * 
   * @return Response Code
   */
  public String getResponseCode() {
    return responseCode;
  }


  public void setResponseCode(String responseCode) {
    this.responseCode = responseCode;
  }

  /**
   * Error Severity
   * 
   * @documentationExample FATAL
   * 
   * @return Error Severity
   */
  public String getErrorSeverity() {
    return errorSeverity;
  }

  public void setErrorSeverity(String errorSeverity) {
    this.errorSeverity = errorSeverity;
  }

  /**
   * Code
   * 
   * @documentationExample 12
   * 
   * @return Code
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Title
   * 
   * @documentationExample My Outcome
   * 
   * @return Title
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Detail
   * 
   * @documentationExample This is a test outcome
   * 
   * @return Detail
   */
  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Updated local datetime
   * 
   * @documentationExample 2012-02-15T07:44:59.000
   * 
   * @return Updated local datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Created local datetime
   * 
   * @documentationExample 2012-02-15T07:44:59.000
   * 
   * @return Created local datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EprescribeJobOutcomeDto that = (EprescribeJobOutcomeDto) o;
    return id == that.id
        && Objects.equals(outcomeCodeId, that.outcomeCodeId)
        && Objects.equals(previousMessageHeaderId, that.previousMessageHeaderId)
        && Objects.equals(responseCode, that.responseCode)
        && Objects.equals(errorSeverity, that.errorSeverity)
        && Objects.equals(code, that.code)
        && Objects.equals(title, that.title)
        && Objects.equals(detail, that.detail)
        && Objects.equals(timestamp, that.timestamp)
        && Objects.equals(createdAt, that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, outcomeCodeId, previousMessageHeaderId, responseCode, errorSeverity, code, title,
            detail, timestamp, createdAt);
  }

  @Override
  public String toString() {
    return "EprescribeJobOutcomeDto{"
        + "id=" + id
        + ", outcomeCodeId=" + outcomeCodeId
        + ", previousMessageHeaderId='" + previousMessageHeaderId + '\''
        + ", responseCode='" + responseCode + '\''
        + ", errorSeverity='" + errorSeverity + '\''
        + ", code='" + code + '\''
        + ", title='" + title + '\''
        + ", detail='" + detail + '\''
        + ", timestamp=" + timestamp
        + ", createdAt=" + createdAt
        + '}';
  }
}
