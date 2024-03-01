
package com.qhrtech.emr.restapi.models.dto.patientv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qhrtech.emr.restapi.config.serialization.JodaLocalDateDeserializer;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.joda.time.LocalDate;

/**
 * The Personal Health Card model object.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The personal health card data object.",
    implementation = PersonalHealthCardDto.class, name = "PersonalHealthCardV2Dto")
public class PersonalHealthCardDto {

  @JsonProperty("phn")
  @Schema(description = "A personal health card number", example = "123133")
  private String phn;

  @JsonProperty("locationId")
  @Schema(description = "A unique location id for the personal health card", example = "1")
  private Integer locationId;

  @JsonProperty("expiry")
  @Schema(description = "The expiry date for the personal health card number",
      example = "2016-02-16")
  @CheckLocalDateRange
  @JsonDeserialize(using = JodaLocalDateDeserializer.class)
  private LocalDate expiry;

  /**
   * The Personal Health Card number
   *
   * @documentationExample 1254
   *
   * @return A personal health card number
   */
  public String getPhn() {
    return phn;
  }

  public void setPhn(String phn) {
    this.phn = phn;
  }

  /**
   * A unique location ID for the Personal Health Card.
   *
   * @documentationExample 1
   *
   * @return A location ID
   */
  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  /**
   * An expiry date for the Personal Health Card.
   *
   * @documentationExample 2017-11-29 19:43:40.013
   *
   * @return An expiry date.
   */
  @DocumentationExample("2016-02-16")
  @TypeHint(String.class)
  public LocalDate getExpiry() {
    return expiry;
  }

  public void setExpiry(LocalDate expiry) {
    this.expiry = expiry;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 73 * hash + Objects.hashCode(this.phn);
    hash = 73 * hash + Objects.hashCode(this.locationId);
    hash = 73 * hash + Objects.hashCode(this.expiry);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PersonalHealthCardDto other = (PersonalHealthCardDto) obj;
    if (!Objects.equals(this.phn, other.phn)) {
      return false;
    }
    if (!Objects.equals(this.locationId, other.locationId)) {
      return false;
    }
    if (!Objects.equals(this.expiry, other.expiry)) {
      return false;
    }
    return true;
  }

}
