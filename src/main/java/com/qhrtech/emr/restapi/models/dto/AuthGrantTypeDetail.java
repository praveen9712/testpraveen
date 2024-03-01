
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.demographics.Phone;
import com.qhrtech.emr.restapi.models.dto.security.OfficeRoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;

public class AuthGrantTypeDetail {

  @JsonProperty("id")
  @Schema(description = "The user name", example = "DavidDoctor")
  private String id;

  @JsonProperty("userId")
  @Schema(description = "The user ID", example = "1")
  private int userId;

  @JsonProperty("providerId")
  @Schema(description = "Physician ID", example = "25863")
  private Integer providerId;

  @JsonProperty("name")
  @Schema(description = "FirstName + LastName", example = "David Doctor")
  private String name;

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

  @JsonProperty("officeRoles")
  @Schema(description = "Office Roles")
  private Set<OfficeRoleDto> officeRoles;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Set<OfficeRoleDto> getOfficeRoles() {
    return officeRoles;
  }

  public void setOfficeRoles(
      Set<OfficeRoleDto> officeRoles) {
    this.officeRoles = officeRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AuthGrantTypeDetail)) {
      return false;
    }

    AuthGrantTypeDetail that = (AuthGrantTypeDetail) o;

    if (getUserId() != that.getUserId()) {
      return false;
    }
    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    }
    if (getProviderId() != null ? !getProviderId().equals(that.getProviderId())
        : that.getProviderId() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getPhones() != null ? !getPhones().equals(that.getPhones()) : that.getPhones() != null) {
      return false;
    }
    if (getAddresses() != null ? !getAddresses().equals(that.getAddresses())
        : that.getAddresses() != null) {
      return false;
    }
    if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) {
      return false;
    }
    return getOfficeRoles() != null ? getOfficeRoles().equals(that.getOfficeRoles())
        : that.getOfficeRoles() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + getUserId();
    result = 31 * result + (getProviderId() != null ? getProviderId().hashCode() : 0);
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getPhones() != null ? getPhones().hashCode() : 0);
    result = 31 * result + (getAddresses() != null ? getAddresses().hashCode() : 0);
    result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
    result = 31 * result + (getOfficeRoles() != null ? getOfficeRoles().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AuthGrantTypeDetail{");
    sb.append("id='").append(id).append('\'');
    sb.append(", userId=").append(userId);
    sb.append(", providerId=").append(providerId);
    sb.append(", name='").append(name).append('\'');
    sb.append(", phones=").append(phones);
    sb.append(", addresses=").append(addresses);
    sb.append(", email='").append(email).append('\'');
    sb.append(", officeRoles=").append(officeRoles);
    sb.append('}');
    return sb.toString();
  }
}
