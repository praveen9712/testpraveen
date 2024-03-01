
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;


/**
 * Patient RoleFlag.
 *
 * A representation of a message, based on a role that is shown when a patient is viewed. This
 * message has an associated time
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A representation of a message, based on a role that is shown when a "
    + "patient is viewed. This message has an associated time")
public class PatientRoleFlagDto {

  @JsonProperty("roleId")
  @Schema(description = "Role Id", example = "1")
  private int roleId;

  @JsonProperty("message")
  @Schema(description = "A patient role flag", example = "A message")
  private String flag;

  @JsonProperty("lastUpdated")
  @Schema(description = "Date of last update", example = "2020-01-03 15:27:27.310")
  private LocalDateTime lastUpdatedDate;

  /**
   * The Role id
   *
   * @return The roleId
   * @documentationExample 1
   */
  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  /**
   * The Patient role flag
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

    PatientRoleFlagDto that = (PatientRoleFlagDto) o;

    if (roleId != that.roleId) {
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
    int result = roleId;
    result = 31 * result + (flag != null ? flag.hashCode() : 0);
    result = 31 * result + (lastUpdatedDate != null ? lastUpdatedDate.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PatientRoleFlagDto{"
        + "roleId=" + roleId
        + ", flag='" + flag + '\''
        + ", lastUpdatedDate=" + lastUpdatedDate
        + '}';
  }
}
