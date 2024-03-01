
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Location data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.demographics.Location
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Location data transfer object model")
public class LocationDto {

  @JsonProperty("locationId")
  @Schema(description = "Unique identifier for the location", example = "2")
  private int locationId;

  @JsonProperty("province")
  @Schema(description = "Province or state abbreviation for the location", example = "BC")
  private String province;

  @JsonProperty("country")
  @Schema(description = "Country name for the location", example = "Canada")
  private String country;

  @JsonProperty("description")
  @Schema(description = "Description of locations province or state", example = "British Columbia")
  private String description;

  /**
   * Unique Identifier for the Location.
   *
   * @documentationExample 2
   *
   * @return the location ID
   */
  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  /**
   * Province or state abbreviation for the Location.
   *
   * @documentationExample BC
   *
   * @return Province or state abbreviation
   */
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  /**
   * Country name for the Location.
   *
   * @documentationExample Canada
   *
   * @return Country name String
   */
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Description of Locations province or state.
   *
   * @documentationExample British Columbia
   *
   * @return Description String
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.locationId;
    hash = 97 * hash + Objects.hashCode(this.province);
    hash = 97 * hash + Objects.hashCode(this.country);
    hash = 97 * hash + Objects.hashCode(this.description);
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
    final LocationDto other = (LocationDto) obj;
    if (this.locationId != other.locationId) {
      return false;
    }
    if (!Objects.equals(this.province, other.province)) {
      return false;
    }
    if (!Objects.equals(this.country, other.country)) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    return true;
  }

}
