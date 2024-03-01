
package com.qhrtech.emr.restapi.models.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.AddressDto;
import com.qhrtech.emr.restapi.models.dto.EmailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Portal Access Token
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Portal access token.")
public class PortalToken {

  @JsonProperty("patientId")
  @Schema(description = "Accuro patient id", example = "1")
  private int patientId;

  @JsonProperty("firstName")
  @Schema(description = "First name", example = "Jane")
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "Last name", example = "Doe")
  private String lastName;

  @JsonProperty("middleName")
  @Schema(description = "Middle name", example = "Lee")
  private String middleName;

  @JsonProperty("email")
  @Schema(description = "Email")
  private EmailDto email;

  @JsonProperty("address")
  @Schema(description = "Address")
  private AddressDto address;

  @JsonProperty("token")
  @Schema(description = "Access token for Rest API")
  private OAuth2AccessToken token;

  /**
   * Accuro Patient ID
   *
   * @documentationExample 1
   *
   * @return Accuro Patient ID
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * First Name
   *
   * @documentationExample Jane
   *
   * @return Patient first name
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last Name
   *
   * @documentationExample Doe
   *
   * @return Patient last name
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Middle Name
   *
   * @documentationExample Lee
   *
   * @return Patient middle name
   */
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * Email DTO
   *
   * @return Email DTO
   */
  public EmailDto getEmail() {
    return email;
  }

  public void setEmail(EmailDto email) {
    this.email = email;
  }

  /**
   * Address DTO
   *
   * @return Address DTO
   */
  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(AddressDto address) {
    this.address = address;
  }

  /**
   * Access token for Rest API
   *
   * @return Access Token
   */
  public OAuth2AccessToken getToken() {
    return token;
  }

  public void setToken(OAuth2AccessToken token) {
    this.token = token;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + this.patientId;
    hash = 53 * hash + Objects.hashCode(this.firstName);
    hash = 53 * hash + Objects.hashCode(this.lastName);
    hash = 53 * hash + Objects.hashCode(this.middleName);
    hash = 53 * hash + Objects.hashCode(this.email);
    hash = 53 * hash + Objects.hashCode(this.address);
    hash = 53 * hash + Objects.hashCode(this.token);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PortalToken other = (PortalToken) obj;
    if (this.patientId != other.patientId) {
      return false;
    }
    if (!Objects.equals(this.firstName, other.firstName)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.middleName, other.middleName)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    return Objects.equals(this.token, other.token);
  }

}
