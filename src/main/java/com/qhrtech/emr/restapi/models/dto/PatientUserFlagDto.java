
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;


/**
 * Patient UserFlag.
 *
 * A representation of a personal message that is shown when a patient is viewed. This message has
 * an associated time
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A representation of a personal message that is shown when a "
    + "patient is viewed. This message has an associated time")
public class PatientUserFlagDto {

  @JsonProperty("userId")
  @Schema(description = "User Id", example = "1")
  private int userId;

  @JsonProperty("message")
  @Schema(description = "A patient user flag", example = "A message")
  private String flag;

  @JsonProperty("lastUpdated")
  @Schema(description = "Date of last update", example = "2020-01-03 15:27:27.310")
  private LocalDateTime lastUpdatedDate;

  /**
   * The User id
   *
   * @return The userId
   * @documentationExample 1
   */
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * A Patient user flag
   *
   * @return The flag
   * @documentationExample A message
   */
  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  /**
   * The Last Updated date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientUserFlagDto that = (PatientUserFlagDto) o;

    if (userId != that.userId) {
      return false;
    }
    if (flag != null ? !flag.equals(that.flag) : that.flag != null) {
      return false;
    }
    return lastUpdatedDate != null ? lastUpdatedDate.equals(that.lastUpdatedDate)
        : that.lastUpdatedDate == null;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (flag != null ? flag.hashCode() : 0);
    result = 31 * result + (lastUpdatedDate != null ? lastUpdatedDate.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PatientUserFlagDto{"
        + "userId=" + userId
        + ", flag='" + flag + '\''
        + ", lastUpdatedDate=" + lastUpdatedDate
        + '}';
  }
}
