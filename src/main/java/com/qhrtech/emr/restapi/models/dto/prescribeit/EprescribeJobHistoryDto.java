
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Eprescribe job history data transfer object model")
public class EprescribeJobHistoryDto {

  @JsonProperty("id")
  @Schema(description = "Eprescribe Job History ID", example = "1")
  private int eprescribeJobHistoryId;

  @JsonProperty("eprescribeJobId")
  @Schema(description = "Eprescribe Job ID", example = "1")
  private int eprescribeJobId;

  @NotNull
  @JsonProperty("status")
  @Schema(description = "Eprescribe Job History status")
  private String status;

  @NotNull
  @CheckLocalDateTimeRange
  @JsonProperty("timestamp")
  @Schema(description = "Eprescribe Job History date time")
  private LocalDateTime timestamp;

  @JsonProperty("httpStatusCode")
  @Schema(description = "Eprescribe Job History HTTP status code")
  private Integer httpStatusCode;

  @Size(max = 50)
  @JsonProperty("appErrorCode")
  @Schema(description = "Eprescribe Job History app error code")
  private String appErrorCode;

  @JsonProperty("description")
  @Schema(description = "Eprescribe Job History description")
  private String description;

  @JsonProperty("details")
  @Schema(description = "Eprescribe Job History details")
  private String details;

  /**
   * Eprescribe Job History ID
   *
   * @return Erx Job History ID
   * @documentationExample 12
   */
  public int getEprescribeJobHistoryId() {
    return eprescribeJobHistoryId;
  }

  public void setEprescribeJobHistoryId(int eprescribeJobHistoryId) {
    this.eprescribeJobHistoryId = eprescribeJobHistoryId;
  }

  /**
   * Eprescribe Job ID
   *
   * @return Erx Job ID
   * @documentationExample 12
   */
  public int getEprescribeJobId() {
    return eprescribeJobId;
  }

  public void setEprescribeJobId(int eprescribeJobId) {
    this.eprescribeJobId = eprescribeJobId;
  }

  /**
   * Eprescribe Job History status
   *
   * @return Erx Job History status
   * @documentationExample String
   */
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Date time for Erx Job History
   *
   * @return Date time
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Eprescribe Job History HTTP status code
   *
   * @return Erx Job History HTTP status code
   * @documentationExample 12
   */
  public Integer getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(Integer httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  /**
   * Eprescribe Job History App error code
   *
   * @return Erx Job History app error code
   * @documentationExample String
   */
  public String getAppErrorCode() {
    return appErrorCode;
  }

  public void setAppErrorCode(String appErrorCode) {
    this.appErrorCode = appErrorCode;
  }

  /**
   * Eprescribe Job History description
   *
   * @return Erx Job History description
   * @documentationExample String
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Eprescribe Job History details
   *
   * @return Erx Job History details
   * @documentationExample String
   */
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EprescribeJobHistoryDto that = (EprescribeJobHistoryDto) o;

    if (eprescribeJobHistoryId != that.eprescribeJobHistoryId) {
      return false;
    }
    if (eprescribeJobId != that.eprescribeJobId) {
      return false;
    }
    if (!Objects.equals(status, that.status)) {
      return false;
    }
    if (!Objects.equals(timestamp, that.timestamp)) {
      return false;
    }
    if (!Objects.equals(httpStatusCode, that.httpStatusCode)) {
      return false;
    }
    if (!Objects.equals(appErrorCode, that.appErrorCode)) {
      return false;
    }
    if (!Objects.equals(description, that.description)) {
      return false;
    }
    return Objects.equals(details, that.details);
  }

  @Override
  public int hashCode() {
    int result = eprescribeJobHistoryId;
    result = 31 * result + eprescribeJobId;
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (httpStatusCode != null ? httpStatusCode.hashCode() : 0);
    result = 31 * result + (appErrorCode != null ? appErrorCode.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (details != null ? details.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("EprescribeJobHistoryDto{");
    sb.append("ePrescribeJobHistoryId=").append(eprescribeJobHistoryId);
    sb.append(", ePrescribeJobId=").append(eprescribeJobId);
    sb.append(", status='").append(status).append('\'');
    sb.append(", timestamp=").append(timestamp);
    sb.append(", httpStatusCode=").append(httpStatusCode);
    sb.append(", appErrorCode='").append(appErrorCode).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", details='").append(details).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
