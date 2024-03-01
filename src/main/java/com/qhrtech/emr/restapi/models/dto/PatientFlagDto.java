
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Objects;

/**
 * Patient Flag.
 *
 * A representation of a message, either global, personal or based on a role that is shown when a
 * patient is viewed. This message has an associated time
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A representation of a message, either global, personal or based on a role "
    + "that is shown when a patient is viewed. This message has an associated time.",
    implementation = PatientFlagDto.class, name = "PatientFlagDto")
public class PatientFlagDto {

  @JsonProperty("message")
  @Schema(description = "A message", example = "A message")
  private String message;

  @JsonProperty("lastUpdated")
  @Schema(description = "Date of last update", example = "2016-02-16T00:00:00.000-0800")
  private Calendar lastUpdated;

  @JsonProperty("flagUser")
  @Schema(
      description = "Flag user represents who created/updated the global message.",
      example = "12")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer flagUser;

  public PatientFlagDto() {
    message = "";
  }

  /**
   * A message associated with the patient flag.
   *
   * @return A message
   * @documentationExample A message
   */
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Date of last update
   *
   * @return Last updated date
   */
  @DocumentationExample("2016-02-16T00:00:00.000-0800")
  @TypeHint(String.class)
  public Calendar getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Calendar lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Integer getFlagUser() {
    return flagUser;
  }

  public void setFlagUser(Integer flagUser) {
    this.flagUser = flagUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientFlagDto that = (PatientFlagDto) o;

    if (!Objects.equals(message, that.message)) {
      return false;
    }
    if (!Objects.equals(lastUpdated, that.lastUpdated)) {
      return false;
    }
    return Objects.equals(flagUser, that.flagUser);
  }

  @Override
  public int hashCode() {
    int result = message != null ? message.hashCode() : 0;
    result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
    result = 31 * result + (flagUser != null ? flagUser.hashCode() : 0);
    return result;
  }
}
