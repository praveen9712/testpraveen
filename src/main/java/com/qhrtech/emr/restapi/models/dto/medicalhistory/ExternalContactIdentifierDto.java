
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Contact data transfer object model")
public class ExternalContactIdentifierDto {

  @JsonProperty(value = "contactId")
  @Schema(description = "Identity of the contact", example = "12")
  private int contactId;

  @JsonProperty(value = "externalSystemIdentifier")
  @Schema(description = "System identifier of the external system",
      example = "healthcare_directory")
  private String externalSystemIdentifier;

  @Size(max = 64, message = "The external identifier value can not exceed 64 characters.")
  @JsonProperty(value = "value")
  @Schema(description = "external identifier value", example = "200127586")
  private String value;

  @JsonProperty(value = "createdDate")
  @Schema(
      description = "Date external identifier was added to database. "
          + "Date time in UTC",
      example = "2012-02-15 07:44:59.000")
  private LocalDateTime createdDate;

  @JsonProperty(value = "lastUpdatedDate")
  @Schema(description = "Date last updated. Date time in UTC",
      example = "2012-02-15 07:44:59.000")
  private LocalDateTime lastUpdatedDate;


  /**
   * The identity of a contact.
   *
   * @return contact id
   * @documentationExample 12
   */
  public int getContactId() {
    return contactId;
  }

  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * External identifier value
   *
   * @return value
   * @documentationExample 200127586
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /**
   * External identifier created date. Date time in UTC
   *
   * @return date time
   * @documentationExample 2012-02-15 07:44:59.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * External identifier lat updated date. Date time in UTC
   *
   * @return date time
   * @documentationExample 2012-02-15 07:44:59.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  /**
   * System identifier of the external system.
   *
   * @return External system identifier
   * @documentationExample healthcare-directory
   */
  public String getExternalSystemIdentifier() {
    return externalSystemIdentifier;
  }

  public void setExternalSystemIdentifier(String externalSystemIdentifier) {
    this.externalSystemIdentifier = externalSystemIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (this == o) {
      return true;
    }
    if (!(this.getClass() == o.getClass())) {
      return false;
    }
    ExternalContactIdentifierDto that = (ExternalContactIdentifierDto) o;
    return contactId == that.contactId
        && Objects.equals(externalSystemIdentifier, that.externalSystemIdentifier)
        && Objects.equals(value, that.value)
        && Objects.equals(createdDate, that.createdDate)
        && Objects.equals(lastUpdatedDate, that.lastUpdatedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactId, externalSystemIdentifier, value, createdDate,
        lastUpdatedDate);
  }
}
