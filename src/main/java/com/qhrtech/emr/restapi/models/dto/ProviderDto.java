
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.patientv2.EmailDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The Provider model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The provider data transfer object.")
public class ProviderDto {

  @JsonProperty("providerId")
  @Schema(description = "Unique id of the provider", example = "1")
  private int providerId;

  @JsonProperty("statusId")
  @Schema(description = "Status id of the provider", example = "1")
  private Integer statusId;

  @JsonProperty("typeId")
  @Schema(description = "Type id of the provider", example = "1")
  @NotNull
  private Integer typeId;

  @JsonProperty("title")
  @Schema(description = "The provider's title", example = "Sir")
  @Size(max = 50)
  private String title;

  @JsonProperty("firstName")
  @Schema(description = "The provider's first name", example = "Arthur")
  @NotNull
  @Size(max = 100)
  private String firstName;

  @JsonProperty("middleName")
  @Schema(description = "The provider's middle name", example = "Conan")
  @Size(max = 100)
  private String middleName;

  @JsonProperty("lastName")
  @Schema(description = "The provider's last name", example = "Doyle")
  @NotNull
  @Size(max = 100)
  private String lastName;

  @JsonProperty("suffix")
  @Schema(description = "The provider's suffix", example = "Sr.")
  private String suffix;

  @JsonProperty("address")
  @Schema(description = "The provider's address", example = "221B Baker St")
  @Valid
  private ProviderAddressDto address;

  @JsonProperty("phones")
  @Schema(description = "A list of provider phone numbers")
  @Valid
  private List<PhoneDto> phones;

  @JsonProperty("email")
  @Schema(description = "The provider's email addresses", example = "contact@provider.com")
  @Valid
  private EmailDto email;

  @JsonProperty("specialty")
  @Schema(description = "The provider's specialty", example = "Family Practice")
  @Size(max = 100)
  private String specialty;

  @JsonProperty("defaultOffice")
  @Schema(description = "The default office id", example = "17766")
  private Integer defaultOffice;

  @JsonProperty("cpsoNumber")
  @Schema(description = "The physician's cpso number", example = "1442456")
  @Size(max = 20)
  private String cpsoNumber;

  @JsonProperty("practitionerNumber")
  @Schema(description = "The provider's practitioner number", example = "12345")
  @Size(max = 20)
  private String practitionerNumber;

  /**
   * Unique provider ID for the Provider DTO
   *
   * @return A provider ID
   * @documentationExample 1
   */
  public int getProviderId() {
    return providerId;
  }

  public void setProviderId(int providerId) {
    this.providerId = providerId;
  }

  /**
   * Unique status ID for the Provider DTO
   *
   * @return A status ID
   * @documentationExample 1
   */
  public Integer getStatusId() {
    return statusId;
  }

  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
  }

  /**
   * Unique type ID for the Provider DTO
   *
   * @return A type ID
   * @documentationExample 1
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * The providers title.
   *
   * @return The providers title.
   * @documentationExample Sir
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * The providers first name.
   *
   * @return The providers first name
   * @documentationExample Arthur
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * The providers middle name
   *
   * @return The providers middle name
   * @documentationExample Conan
   */
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * The providers last name
   *
   * @return The providers last name
   * @documentationExample Doyle
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The providers suffix
   *
   * @return The providers suffix
   * @documentationExample Sr.
   */
  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * The providers address
   *
   * @return The providers address
   * @documentationExample 221B Baker Street
   */
  public ProviderAddressDto getAddress() {
    return address;
  }

  public void setAddress(ProviderAddressDto address) {
    this.address = address;
  }

  /**
   * A list of provider phone numbers
   *
   * @return A list of phone number objects
   * @documentationExample 1-240-989-5454
   */
  public List<PhoneDto> getPhones() {
    return phones;
  }

  public void setPhones(List<PhoneDto> phones) {
    this.phones = phones;
  }

  /**
   * A list of provider email addresses
   *
   * @return A list of email objects
   * @documentationExample contact@provider.com
   */
  public EmailDto getEmail() {
    return email;
  }

  public void setEmail(EmailDto email) {
    this.email = email;
  }

  /**
   * The providers specialty.
   *
   * @return A provider specialty
   * @documentationExample Family Practice
   */
  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  /**
   * The providers default office ID
   *
   * @return An office ID
   * @documentationExample 1
   */
  public Integer getDefaultOffice() {
    return defaultOffice;
  }

  public void setDefaultOffice(Integer defaultOffice) {
    this.defaultOffice = defaultOffice;
  }

  public String getCpsoNumber() {
    return cpsoNumber;
  }

  public void setCpsoNumber(String cpsoNumber) {
    this.cpsoNumber = cpsoNumber;
  }

  public String getPractitionerNumber() {
    return practitionerNumber;
  }

  public void setPractitionerNumber(String practitionerNumber) {
    this.practitionerNumber = practitionerNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProviderDto that = (ProviderDto) o;

    if (providerId != that.providerId) {
      return false;
    }
    if (!Objects.equals(typeId, that.typeId)) {
      return false;
    }
    if (!Objects.equals(statusId, that.statusId)) {
      return false;
    }
    if (!Objects.equals(title, that.title)) {
      return false;
    }
    if (!Objects.equals(firstName, that.firstName)) {
      return false;
    }
    if (!Objects.equals(middleName, that.middleName)) {
      return false;
    }
    if (!Objects.equals(lastName, that.lastName)) {
      return false;
    }
    if (!Objects.equals(suffix, that.suffix)) {
      return false;
    }
    if (!Objects.equals(address, that.address)) {
      return false;
    }
    if (!Objects.equals(phones, that.phones)) {
      return false;
    }
    if (!Objects.equals(email, that.email)) {
      return false;
    }
    if (!Objects.equals(specialty, that.specialty)) {
      return false;
    }
    if (!Objects.equals(defaultOffice, that.defaultOffice)) {
      return false;
    }
    if (!Objects.equals(practitionerNumber, that.practitionerNumber)) {
      return false;
    }
    return Objects.equals(cpsoNumber, that.cpsoNumber);
  }

  @Override
  public int hashCode() {
    int result = providerId;
    result = 31 * result + (statusId != null ? statusId.hashCode() : 0);
    result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (phones != null ? phones.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (defaultOffice != null ? defaultOffice.hashCode() : 0);
    result = 31 * result + (cpsoNumber != null ? cpsoNumber.hashCode() : 0);
    result = 31 * result + (practitionerNumber != null ? practitionerNumber.hashCode() : 0);
    return result;
  }
}
