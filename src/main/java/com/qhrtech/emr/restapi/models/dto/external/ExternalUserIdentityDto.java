
package com.qhrtech.emr.restapi.models.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

@Schema(description = "External User Identity data transfer object")
public class ExternalUserIdentityDto {

  @JsonProperty("accuroUserId")
  @Schema(name = "accuroUserId", description = "Accuro user id", type = "int", example = "1")
  private int userId;

  @JsonProperty("externalIdSystemId")
  @Schema(name = "externalIdSystemId", description = "External identity system id", type = "long",
      example = "1")
  private long extIdSystemId;

  @JsonProperty("identifier")
  @NotBlank(message = "The identifier can not be null or empty.")
  @Schema(name = "identifier", description = "Unique External user identity id.", type = "String",
      example = "3ae75011-1d20-4333-9691-xxxxxxxxxx")
  private String identity;

  @JsonProperty("createdDateUtc")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(name = "createdDateUtc", description = "Created date on UTC(read-only)", type = "string",
      example = "2020-06-09T23:39:59.500")
  private LocalDateTime createdOnInUtc;

  @JsonProperty("version")
  @Schema(description = "Version", type = "String", example = "1")
  private String version;

  @JsonProperty("validFromDateUtc")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(name = "validFromDateUtc", description = "Valid from the date on UTC", type = "string",
      example = "2020-06-09T23:39:59.500")
  private LocalDateTime validFromInUtc;

  @JsonProperty("validToDateUtc")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(name = "validToDateUtc", description = "Valid to the date on UTC", type = "string",
      example = "2020-06-10T23:39:59.500")
  private LocalDateTime validToInUtc;

  /**
   * The Accuro User id
   * 
   * @documentationExample 1
   *
   * @return userId
   */
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * External ID for System Identification
   *
   * @documentationExample 1
   *
   * @return external ID System
   */
  public long getExtIdSystemId() {
    return extIdSystemId;
  }

  public void setExtIdSystemId(long extIdSystemId) {
    this.extIdSystemId = extIdSystemId;
  }

  /**
   * System Identity
   *
   * @documentationExample 3ae75011-1d20-4333-9691-xxxxxxxxxx
   *
   * @return identity
   */
  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  /**
   * The created date time in UTC for External Identity System
   *
   * @return A Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedOnInUtc() {
    return createdOnInUtc;
  }

  public void setCreatedOnInUtc(LocalDateTime createdOnInUtc) {
    this.createdOnInUtc = createdOnInUtc;
  }

  /**
   * Version for external User Identity
   *
   * @documentationExample 1
   *
   * @return version
   */
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * The valid from date time in UTC.
   *
   * @return A Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getValidFromInUtc() {
    return validFromInUtc;
  }

  public void setValidFromInUtc(LocalDateTime validFromInUtc) {
    this.validFromInUtc = validFromInUtc;
  }

  /**
   * The valid to date time in UTC.
   *
   * @return A Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getValidToInUtc() {
    return validToInUtc;
  }

  public void setValidToInUtc(LocalDateTime validToInUtc) {
    this.validToInUtc = validToInUtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExternalUserIdentityDto)) {
      return false;
    }

    ExternalUserIdentityDto that = (ExternalUserIdentityDto) o;

    if (getUserId() != that.getUserId()) {
      return false;
    }
    if (getExtIdSystemId() != that.getExtIdSystemId()) {
      return false;
    }
    if (!Objects.equals(getIdentity(), that.getIdentity())) {
      return false;
    }
    if (!Objects.equals(getCreatedOnInUtc(), that.getCreatedOnInUtc())) {
      return false;
    }
    if (!Objects.equals(getVersion(), that.getVersion())) {
      return false;
    }
    if (!Objects.equals(getValidFromInUtc(), that.getValidFromInUtc())) {
      return false;
    }
    return Objects.equals(getValidToInUtc(), that.getValidToInUtc());
  }

  @Override
  public int hashCode() {
    int result = getUserId();
    result = 31 * result + (int) (getExtIdSystemId() ^ (getExtIdSystemId() >>> 32));
    result = 31 * result + (getIdentity() != null ? getIdentity().hashCode() : 0);
    result = 31 * result + (getCreatedOnInUtc() != null ? getCreatedOnInUtc().hashCode() : 0);
    result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
    result = 31 * result + (getValidFromInUtc() != null ? getValidFromInUtc().hashCode() : 0);
    result = 31 * result + (getValidToInUtc() != null ? getValidToInUtc().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ExternalUserIdentityDto{");
    sb.append("userId=").append(userId);
    sb.append(", extIdSystemId=").append(extIdSystemId);
    sb.append(", identity='").append(identity).append('\'');
    sb.append(", createdOnInUtc=").append(createdOnInUtc);
    sb.append(", version='").append(version).append('\'');
    sb.append(", validFromInUtc=").append(validFromInUtc);
    sb.append(", validToInUtc=").append(validToInUtc);
    sb.append('}');
    return sb.toString();
  }
}
