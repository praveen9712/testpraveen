
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Objects;

/**
 * The Personal Health Card model object.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The personal health card data object.",
    implementation = PersonalHealthCardDto.class, name = "PersonalHealthCardDto")
public class PersonalHealthCardDto {

  @JsonProperty("phn")
  @Schema(description = "A personal health card number", example = "123133")
  private String phn;

  @JsonProperty("locationId")
  @Schema(description = "A unique location id for the personal health card", type = "integer",
      example = "1")
  private Integer locationId;

  @JsonProperty("expiry")
  @Schema(description = "The expiry date for the personal health card number",
      example = "2017-11-29 19:43:40.013")
  private Calendar expiry;

  /**
   * The Personal Health Card number
   *
   * @return A personal health card number
   * @documentationExample 1254
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
   * @return A location ID
   * @documentationExample 1
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
   * @return An expiry date.
   * @documentationExample 2017-11-29 19:43:40.013
   */
  @DocumentationExample("2016-02-16T00:00:00.000-0800")
  @TypeHint(String.class)
  public Calendar getExpiry() {
    return expiry;
  }

  public void setExpiry(Calendar expiry) {
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
