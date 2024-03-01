
package com.qhrtech.emr.restapi.models.dto.patientv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qhrtech.emr.restapi.config.serialization.JodaLocalDateDeserializer;
import com.qhrtech.emr.restapi.config.serialization.JodaLocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

/**
 * Patient Flag.
 *
 * A representation of a message, either global, personal or based on a role that is shown when a
 * patient is viewed. This message has an associated time
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A representation of a message, either global, personal or based on a role "
    + "that is shown when a patient is viewed. This message has an associated time.",
    implementation = PatientFlagDto.class, name = "PatientFlagV2Dto")
public class PatientFlagDto {

  @JsonProperty("message")
  @Schema(description = "A message", example = "A message")
  @Size(max = 400, message = "The message size should not be greater than 400 characters")
  private String message;

  @JsonProperty("lastUpdated")
  @Schema(description = "Date of last update", example = "2016-02-16T00:00:00.000")
  @JsonDeserialize(using = JodaLocalDateTimeDeserializer.class)
  @CheckLocalDateTimeRange
  private LocalDateTime lastUpdated;

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
   * @documentationExample A message
   *
   * @return A message
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
  @DocumentationExample("2016-02-16T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
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
