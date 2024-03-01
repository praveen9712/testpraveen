
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.masking.MaskDto;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

/**
 * The Address data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.demographics.Address
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Address data transfer object model. In any of these fields "
    + "- note, type, start or end - are null, they will not be shown in the response object."
    + "Example shown here is the ideal scenario where all the fields are not null.")
public class ProviderAddressDto {

  @JsonProperty("street")
  @Schema(
      description = "Street of the address",
      example = "102 Maple Street")
  @Size(max = 60)
  private String street;

  @JsonProperty("city")
  @Schema(
      description = "City name of the address",
      example = "Vancouver")
  @Size(max = 50)
  private String city;

  @JsonProperty("postalZip")
  @Schema(
      description = "Postal or zip code of the Address",
      example = "A1A2B2")
  @Size(max = 50)
  private String postalZip;

  @JsonProperty("locationId")
  @Schema(
      description = "Unique location identifier of the Address",
      example = "10")
  private Integer locationId;

  @JsonProperty("masks")
  @Schema(description = "The collection of masks on address")
  private List<MaskDto> masks;

  /**
   * Street for the Address
   *
   * @documentationExample 102 Maple Street
   *
   * @return a street
   */
  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * City name for the Address
   *
   * @documentationExample Vancouver
   *
   * @return a city name
   */
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Postal or zip code for the Address.
   *
   * @documentationExample A1A2B2
   *
   * @return a postal or zip code
   */
  public String getPostalZip() {

    return postalZip;
  }

  public void setPostalZip(String postalZip) {
    this.postalZip = postalZip;
  }

  /**
   * Unique Location identifier for the Address
   *
   * @documentationExample 1
   *
   * @return Location ID
   */
  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  /**
   * The masks for the address
   *
   * @return The collection of masks
   */
  public List<MaskDto> getMasks() {
    return masks;
  }

  public void setMasks(List<MaskDto> masks) {
    this.masks = masks;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.street);
    hash = 53 * hash + Objects.hashCode(this.city);
    hash = 53 * hash + Objects.hashCode(this.postalZip);
    hash = 53 * hash + Objects.hashCode(this.locationId);
    hash = 53 * hash + Objects.hashCode(this.masks);
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
    final ProviderAddressDto other = (ProviderAddressDto) obj;
    if (!Objects.equals(this.street, other.street)) {
      return false;
    }
    if (!Objects.equals(this.city, other.city)) {
      return false;
    }
    if (!Objects.equals(this.postalZip, other.postalZip)) {
      return false;
    }
    if (!Objects.equals(this.masks, other.masks)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AddressDto{");
    sb.append("street='").append(street).append('\'');
    sb.append(", city='").append(city).append('\'');
    sb.append(", postalZip='").append(postalZip).append('\'');
    sb.append(", locationId=").append(locationId);
    sb.append(", masks=").append(masks);
    sb.append('}');
    return sb.toString();
  }
}
