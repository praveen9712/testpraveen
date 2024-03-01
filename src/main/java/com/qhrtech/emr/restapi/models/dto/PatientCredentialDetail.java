
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class PatientCredentialDetail {

  @JsonProperty("name")
  @Schema(description = "FirstName + LastName", example = "David Doctor")
  private String name;

  @JsonProperty("insurer")
  @Schema(description = "The associated insurer")
  private String insurer;

  @JsonProperty("referringProvider")
  @Schema(description = "The referring provider")
  private String referringProvider;

  @JsonProperty("familyProvider")
  @Schema(description = "The family provider")
  private String familyProvider;

  @JsonProperty("enrolledProvider")
  @Schema(description = "The enrolled provider")
  private String enrolledProvider;

  @JsonProperty("officeProvider")
  @Schema(description = "The office provider")
  private String officeProvider;

  @JsonProperty("occupation")
  @Schema(description = "The occupation")
  private String occupation;

  @JsonProperty("phones")
  @Schema(description = "The person's listed phone numbers")
  private List<Phone> phones;

  @JsonProperty("addresses")
  @Schema(
      description = "The person's listed addresses. Addresses are ordered as follows:\n\n"
          + "1.Primary Address.\n\n"
          + "2.Secondary Address\n\n"
          + "3.All subsequent addresses.\n\n"
          + "For the primary and secondary addresses, the location id of the address can be null. "
          + "However, for all subsequent addresses the location id must be set and valid.")
  private List<UserInfoAddressDto> addresses;

  @JsonProperty("email")
  @Schema(description = "Email address", example = "david@me.com")
  private String email;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInsurer() {
    return insurer;
  }

  public void setInsurer(String insurer) {
    this.insurer = insurer;
  }

  public String getReferringProvider() {
    return referringProvider;
  }

  public void setReferringProvider(String referringProvider) {
    this.referringProvider = referringProvider;
  }

  public String getFamilyProvider() {
    return familyProvider;
  }

  public void setFamilyProvider(String familyProvider) {
    this.familyProvider = familyProvider;
  }

  public String getEnrolledProvider() {
    return enrolledProvider;
  }

  public void setEnrolledProvider(String enrolledProvider) {
    this.enrolledProvider = enrolledProvider;
  }

  public String getOfficeProvider() {
    return officeProvider;
  }

  public void setOfficeProvider(String officeProvider) {
    this.officeProvider = officeProvider;
  }

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public List<Phone> getPhones() {
    return phones;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }

  public List<UserInfoAddressDto> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<UserInfoAddressDto> addresses) {
    this.addresses = addresses;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientCredentialDetail that = (PatientCredentialDetail) o;

    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (insurer != null ? !insurer.equals(that.insurer) : that.insurer != null) {
      return false;
    }
    if (referringProvider != null ? !referringProvider.equals(that.referringProvider)
        : that.referringProvider != null) {
      return false;
    }
    if (familyProvider != null ? !familyProvider.equals(that.familyProvider)
        : that.familyProvider != null) {
      return false;
    }
    if (enrolledProvider != null ? !enrolledProvider.equals(that.enrolledProvider)
        : that.enrolledProvider != null) {
      return false;
    }
    if (officeProvider != null ? !officeProvider.equals(that.officeProvider)
        : that.officeProvider != null) {
      return false;
    }
    if (occupation != null ? !occupation.equals(that.occupation) : that.occupation != null) {
      return false;
    }
    if (phones != null ? !phones.equals(that.phones) : that.phones != null) {
      return false;
    }
    if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null) {
      return false;
    }
    return email != null ? email.equals(that.email) : that.email == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (insurer != null ? insurer.hashCode() : 0);
    result = 31 * result + (referringProvider != null ? referringProvider.hashCode() : 0);
    result = 31 * result + (familyProvider != null ? familyProvider.hashCode() : 0);
    result = 31 * result + (enrolledProvider != null ? enrolledProvider.hashCode() : 0);
    result = 31 * result + (officeProvider != null ? officeProvider.hashCode() : 0);
    result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
    result = 31 * result + (phones != null ? phones.hashCode() : 0);
    result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PatientCredentialDetail{"
        + "name='" + name + '\''
        + ", insurer='" + insurer + '\''
        + ", referringProvider='" + referringProvider + '\''
        + ", familyProvider='" + familyProvider + '\''
        + ", enrolledProvider='" + enrolledProvider + '\''
        + ", officeProvider='" + officeProvider + '\''
        + ", occupation='" + occupation + '\''
        + ", phones=" + phones
        + ", addresses=" + addresses
        + ", email='" + email + '\''
        + '}';
  }
}
