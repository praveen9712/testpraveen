
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.Valid;

/**
 * The Accuro Office data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.demographics.Office
 * @see com.qhrtech.emr.restapi.models.dto.AddressDto
 * @see com.qhrtech.emr.restapi.models.dto.PhoneDto
 * @see com.qhrtech.emr.restapi.models.dto.EmailDto
 *
 * @author jesse.pasos
 */
@Schema(description = "Accuro Office model object")
public class OfficeDto {

  @JsonProperty("officeId")
  @Schema(description = "Unique Accuro office identifier", example = "12")
  private Integer officeId;

  @JsonProperty("name")
  @Schema(description = "Name of the Accuro office", example = "Test Office")
  private String name;

  @JsonProperty("address")
  @Schema(description = "Address model object for the Accuro office")
  private AddressDto address;

  @JsonProperty("officePhone")
  @Schema(description = "Phone model object for the Accuro office. Represents a telephone number.")
  @Valid
  private PhoneDto officePhone;

  @JsonProperty("fax")
  @Schema(description = "Phone model object for the Accuro office. Represents a fax number.")
  @Valid
  private PhoneDto fax;

  @JsonProperty("email")
  @Schema(description = "Email model object for the Accuro office")
  private EmailDto email;

  @JsonProperty("website")
  @Schema(description = "Website URL for the Accuro office", example = "https://google.com/")
  private String website;

  @JsonProperty("facilityNumber")
  @Schema(description = "Facility number for the Accuro office", example = "132")
  private String facilityNumber;

  @JsonProperty("abbreviation")
  @Schema(description = "Abbreviation for the Accuro office", example = "ABBRV")
  private String abbreviation;

  @JsonProperty("active")
  @Schema(description = "Indication of whether the Accuro office is active or not",
      example = "True")
  private Boolean active;

  /**
   * Unique Accuro Office identifier.
   *
   * @documentationExample 12
   *
   * @return Office ID
   */
  public Integer getOfficeId() {
    return officeId;
  }

  public void setOfficeId(Integer officeId) {
    this.officeId = officeId;
  }

  /**
   * Name for the Accuro Office
   *
   * @documentationExample Test Office
   *
   * @return Office name String
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Address DTO for the Accuro Office.
   *
   * @return An Address DTO
   */
  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(AddressDto address) {
    this.address = address;
  }

  /**
   * Phone DTO for the Accuro Office. Represents a telephone number.
   *
   * @return A Phone DTO
   */
  public PhoneDto getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(PhoneDto officePhone) {
    this.officePhone = officePhone;
  }

  /**
   * Phone DTO for the Accuro Office. Represents a fax number.
   *
   * @return A Phone DTO
   */
  public PhoneDto getFax() {
    return fax;
  }

  public void setFax(PhoneDto fax) {
    this.fax = fax;
  }

  /**
   * Email DTO for the Accuro Office.
   *
   * @return An Email DTO
   */
  public EmailDto getEmail() {
    return email;
  }

  public void setEmail(EmailDto email) {
    this.email = email;
  }

  /**
   * Website URL for the Accuro Office.
   *
   * @documentationExample https://google.com/
   *
   * @return An email String
   */
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  /**
   * Facility number for the Accuro Office.
   *
   * @documentationExample 132
   *
   * @return Facility number String
   */
  public String getFacilityNumber() {
    return facilityNumber;
  }

  public void setFacilityNumber(String facilityNumber) {
    this.facilityNumber = facilityNumber;
  }

  /**
   * Abbreviation for the Accuro Office.
   *
   * @documentationExample ABBRV
   *
   * @return Office abbreviation String
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * Boolean indication of whether the Accuro Office is active or not.
   *
   * @documentationExample True
   *
   * @return <code>true</code> if active, <code>false</code> if inactive.
   */
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.officeId);
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Objects.hashCode(this.address);
    hash = 79 * hash + Objects.hashCode(this.officePhone);
    hash = 79 * hash + Objects.hashCode(this.fax);
    hash = 79 * hash + Objects.hashCode(this.email);
    hash = 79 * hash + Objects.hashCode(this.website);
    hash = 79 * hash + Objects.hashCode(this.facilityNumber);
    hash = 79 * hash + Objects.hashCode(this.abbreviation);
    hash = 79 * hash + Objects.hashCode(this.active);
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
    final OfficeDto other = (OfficeDto) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.website, other.website)) {
      return false;
    }
    if (!Objects.equals(this.facilityNumber, other.facilityNumber)) {
      return false;
    }
    if (!Objects.equals(this.abbreviation, other.abbreviation)) {
      return false;
    }
    if (!Objects.equals(this.officeId, other.officeId)) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.officePhone, other.officePhone)) {
      return false;
    }
    if (!Objects.equals(this.fax, other.fax)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.active, other.active)) {
      return false;
    }
    return true;
  }

}
