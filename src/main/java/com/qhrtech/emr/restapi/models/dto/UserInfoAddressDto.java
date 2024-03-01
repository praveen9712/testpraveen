
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserInfoAddressDto {

  @JsonProperty("address")
  @Schema(description = "Address", example = "1212 Main Street")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String address;

  @JsonProperty("city")
  @Schema(description = "The city name", example = "Vancouver")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String city;

  @JsonProperty("postalCode")
  @Schema(description = "The postal code", example = "V5X4B3")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String postalCode;

  @JsonProperty("province")
  @Schema(description = "Province", example = "BC")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String province;

  @JsonProperty("country")
  @Schema(description = "Country", example = "Canada")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String country;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserInfoAddressDto that = (UserInfoAddressDto) o;

    if (address != null ? !address.equals(that.address) : that.address != null) {
      return false;
    }
    if (city != null ? !city.equals(that.city) : that.city != null) {
      return false;
    }
    if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) {
      return false;
    }
    if (province != null ? !province.equals(that.province) : that.province != null) {
      return false;
    }
    return country != null ? country.equals(that.country) : that.country == null;
  }

  @Override
  public int hashCode() {
    int result = address != null ? address.hashCode() : 0;
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
    result = 31 * result + (province != null ? province.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    return result;
  }
}
