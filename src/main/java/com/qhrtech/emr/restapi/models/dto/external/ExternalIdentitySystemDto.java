
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

public class ExternalIdentitySystemDto {

  @JsonProperty("id")
  @Schema(name = "id", description = "External identity System id", type = "long", example = "1")
  private long systemId;

  @JsonProperty("name")
  @NotBlank(message = "External identity System name must not be blank or null")
  @Schema(description = "External identity System name", type = "string", example = "SQUID")
  private String name;

  @JsonProperty("vendorName")
  @NotBlank(message = "Vendor name must not be blank or null")
  @Schema(description = "Vendor name and please see Vendor object", type = "string",
      example = "QHR")
  private String vendorName;

  @JsonProperty("createdDateUtc")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(name = "createdDateUtc", description = "Created date on UTC(read-only)", type = "string",
      example = "2020-06-09T23:39:59.500")
  private LocalDateTime createdOnInUtc;

  /**
   * The System ID for External Identity System
   *
   * @documentationExample 1
   *
   * @return {@link Long} systemId
   */
  public long getSystemId() {
    return systemId;
  }

  public void setSystemId(long systemId) {
    this.systemId = systemId;
  }

  /**
   * The name for External Identity System
   *
   * @documentationExample SQUID
   *
   * @return {@link String} name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Vendor Name for External Identity System
   *
   * @documentationExample QHR
   *
   * @return {@link String} vendorName
   */
  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExternalIdentitySystemDto)) {
      return false;
    }

    ExternalIdentitySystemDto that = (ExternalIdentitySystemDto) o;

    if (getSystemId() != that.getSystemId()) {
      return false;
    }
    if (!Objects.equals(getName(), that.getName())) {
      return false;
    }
    if (!Objects.equals(getVendorName(), that.getVendorName())) {
      return false;
    }
    return Objects.equals(getCreatedOnInUtc(), that.getCreatedOnInUtc());
  }

  @Override
  public int hashCode() {
    int result = (int) (getSystemId() ^ (getSystemId() >>> 32));
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getVendorName() != null ? getVendorName().hashCode() : 0);
    result = 31 * result + (getCreatedOnInUtc() != null ? getCreatedOnInUtc().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ExternalIdentitySystemDto{");
    sb.append("systemId=").append(systemId);
    sb.append(", name='").append(name).append('\'');
    sb.append(", vendorName='").append(vendorName).append('\'');
    sb.append(", createdOnInUtc=").append(createdOnInUtc);
    sb.append('}');
    return sb.toString();
  }
}
