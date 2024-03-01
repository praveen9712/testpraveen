
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.masking.MaskDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Address data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.demographics.Address
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Address data transfer object model. In any of these fields "
    + "- note, type, start or end - are null, they will not be shown in the response object."
    + "Example shown here is the ideal scenario where all the fields are not null.",
    implementation = AddressDto.class, name = "AddressDto")
public class AddressDto {

  @JsonProperty("street")
  @Schema(
      description = "Street of the address",
      example = "102 Maple Street", type = "string")
  private String street;

  @JsonProperty("city")
  @Schema(
      description = "City name of the address",
      example = "Vancouver", type = "string")
  private String city;

  @JsonProperty("postalZip")
  @Schema(
      description = "Postal or zip code of the Address",
      example = "A1A2B2", type = "string")
  private String postalZip;

  @JsonProperty("locationId")
  @Schema(
      description = "Unique location identifier of the Address",
      example = "10", type = "integer")
  private Integer locationId;

  @JsonProperty("note")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description = "Note of the Address",
      example = "A quick note", type = "string")
  private String note;

  @JsonProperty("type")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description = "The address type",
      example = "Civil", type = "string")
  private String type;

  @JsonProperty("start")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description = "Effective start date of the address",
      type = "string",
      example = "2019-03-06")
  private Calendar start;

  @JsonProperty("end")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description = "Effective end date of the address",
      type = "string",
      example = "2020-03-31")
  private Calendar end;

  @JsonProperty("masks")
  @Schema(description = "The collection of masks on address")
  private List<MaskDto> masks;

  /**
   * Street for the Address
   *
   * @return a street
   * @documentationExample 102 Maple Street
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
   * @return a city name
   * @documentationExample Vancouver
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
   * @return a postal or zip code
   * @documentationExample A1A2B2
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
   * @return Location ID
   * @documentationExample 1
   */
  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  /**
   * Note for the Address
   *
   * @return A note
   * @documentationExample A quick note.
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * Type for the address.
   *
   * @return The type for the address
   * @documentationExample type
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * The effective date for the address
   *
   * @return A date
   * @documentationExample 2017
   */
  public Calendar getStart() {
    return start;
  }

  public void setStart(Calendar start) {
    this.start = start;
  }

  /**
   * The end date for the address.
   *
   * @return A date
   * @documentationExample 2017
   */
  public Calendar getEnd() {
    return end;
  }

  public void setEnd(Calendar end) {
    this.end = end;
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
    this.masks = masks == null ? Collections.emptyList() : masks;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.street);
    hash = 53 * hash + Objects.hashCode(this.city);
    hash = 53 * hash + Objects.hashCode(this.postalZip);
    hash = 53 * hash + Objects.hashCode(this.locationId);
    hash = 53 * hash + Objects.hashCode(this.note);
    hash = 53 * hash + Objects.hashCode(this.type);
    hash = 53 * hash + Objects.hashCode(this.start);
    hash = 53 * hash + Objects.hashCode(this.end);
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
    final AddressDto other = (AddressDto) obj;
    if (!Objects.equals(this.street, other.street)) {
      return false;
    }
    if (!Objects.equals(this.city, other.city)) {
      return false;
    }
    if (!Objects.equals(this.postalZip, other.postalZip)) {
      return false;
    }
    if (!Objects.equals(this.note, other.note)) {
      return false;
    }
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.locationId, other.locationId)) {
      return false;
    }
    if (!Objects.equals(this.start, other.start)) {
      return false;
    }
    if (!Objects.equals(this.end, other.end)) {
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
    sb.append(", note='").append(note).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", start=").append(start);
    sb.append(", end=").append(end);
    sb.append(", masks=").append(masks);
    sb.append('}');
    return sb.toString();
  }
}
