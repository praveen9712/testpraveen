
package com.qhrtech.emr.restapi.models.dto.waitlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Model object for the waitlist provider
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Waitlist providers data transfer object model.")
public class WaitlistProviderDto {

  @JsonProperty("id")
  @Schema(description = "Waitlist provider id", example = "2")
  private int id;

  @JsonProperty("practitionerNumber")
  @Schema(description = "Practitioner number", example = "223456")
  @Size(max = 12)
  private String practitionerNumber;

  @JsonProperty("payeeNumber")
  @Schema(description = "Payee number", example = "12")
  @Size(max = 12)
  private String payeeNumber;

  @JsonProperty("firstName")
  @Schema(description = "First name of the provider.", example = "John")
  @NotNull
  @Size(max = 50)
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "Last name of the provider.", example = "Doe")
  @NotNull
  @Size(max = 50)
  private String lastName;

  @JsonProperty("phoneNumber")
  @Schema(description = "Phone number of the provider.", example = "(201) 987-6543")
  @Size(max = 20)
  private String phoneNumber;

  @JsonProperty("phoneExt")
  @Schema(description = "Phone extension.", example = "213")
  @Size(max = 5)
  private String phoneExt;

  @JsonProperty("providerId")
  @Schema(description = "Provider id.", example = "2600")
  private int providerId;

  @JsonProperty("active")
  @Schema(description = "Provider is active or not.", example = "true")
  private boolean active;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPractitionerNumber() {
    return practitionerNumber;
  }

  public void setPractitionerNumber(String practitionerNumber) {
    this.practitionerNumber = practitionerNumber;
  }

  public String getPayeeNumber() {
    return payeeNumber;
  }

  public void setPayeeNumber(String payeeNumber) {
    this.payeeNumber = payeeNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneExt() {
    return phoneExt;
  }

  public void setPhoneExt(String phoneExt) {
    this.phoneExt = phoneExt;
  }

  public int getProviderId() {
    return providerId;
  }

  public void setProviderId(int providerId) {
    this.providerId = providerId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WaitlistProviderDto that = (WaitlistProviderDto) o;

    if (id != that.id) {
      return false;
    }
    if (providerId != that.providerId) {
      return false;
    }
    if (active != that.active) {
      return false;
    }
    if (!Objects.equals(practitionerNumber, that.practitionerNumber)) {
      return false;
    }
    if (!Objects.equals(payeeNumber, that.payeeNumber)) {
      return false;
    }
    if (!Objects.equals(firstName, that.firstName)) {
      return false;
    }
    if (!Objects.equals(lastName, that.lastName)) {
      return false;
    }
    if (!Objects.equals(phoneNumber, that.phoneNumber)) {
      return false;
    }
    return Objects.equals(phoneExt, that.phoneExt);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (practitionerNumber != null ? practitionerNumber.hashCode() : 0);
    result = 31 * result + (payeeNumber != null ? payeeNumber.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
    result = 31 * result + (phoneExt != null ? phoneExt.hashCode() : 0);
    result = 31 * result + providerId;
    result = 31 * result + (active ? 1 : 0);
    return result;
  }
}
