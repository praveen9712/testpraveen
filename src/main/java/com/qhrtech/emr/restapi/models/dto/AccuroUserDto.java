
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

/**
 * The Accuro User data transfer object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccuroUserDto {

  @JsonProperty("userId")
  @Schema(description = "The unique id of an accuro user.", example = "1")
  private int userId;

  @JsonProperty("providerId")
  @Schema(description = "The provider id if the user is a provider.", example = "25892")
  private Integer providerId;

  @JsonProperty("supervisorUserId")
  @Schema(description = "The supervisor user id.", example = "12114")
  private Integer supervisorUserId;

  @JsonProperty("username")
  @Schema(description = "The name of the user.", example = "Shaunwright22")
  @Size(max = 50, message = "Can not exceed 50 characters.")
  private String username;

  @JsonProperty("displayName")
  @Schema(description = "The display name of the user.", example = "Shaun")
  @Size(max = 100, message = "Can not exceed 100 characters.")
  private String displayName;

  @JsonProperty("activeDirectoryUser")
  @Schema(description = "The value of the active directory user.", example = "UAQ01")
  @Size(max = 200, message = "Can not exceed 200 characters.")
  private String activeDirectoryUser;

  @JsonProperty("systemAdmin")
  @Schema(description = "Indicate if the user is a system admin.")
  private boolean systemAdmin;

  @JsonProperty("note")
  @Schema(description = "A note.", example = "This is a service user.")
  @Size(max = 200, message = "Can not exceed 200 characters.")
  private String note;

  @JsonProperty("lockedDate")
  @Schema(description = "The locked date.")
  @CheckLocalDateTimeRange
  private LocalDateTime locked;

  @JsonProperty("active")
  @Schema(description = "Indicate if the user is active or de active.")
  private boolean active;

  @JsonProperty("firstName")
  @Schema(description = "The first name of the user.", example = "Shaun")
  @Size(max = 100, message = "Can not exceed 100 characters.")
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "The last name of the user.", example = "Wright")
  @Size(max = 100, message = "Can not exceed 100 characters.")
  private String lastName;

  @JsonProperty("homePhone")
  @Schema(description = "Home phone details of the user.")
  @Valid
  private PhoneDto homePhone;

  @JsonProperty("cellPhone")
  @Schema(description = "Cellular details of the user.")
  @Valid
  private PhoneDto cellPhone;

  @JsonProperty("workPhone")
  @Schema(description = "Work phone details of the user.")
  @Valid
  private PhoneDto workPhone;

  @JsonProperty("email")
  @Schema(description = "Email details of the user.")
  private EmailDto email;

  @JsonProperty("address")
  @Schema(description = "Address details of the user.")
  private AddressDto address;


  /**
   * The UserId for the AccuroUser
   *
   * @return UserId
   * @documentationExample 20
   */
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * The ProviderId for the AccuroUser
   *
   * @return ProviderId
   * @documentationExample 20
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * The SupervisorUserId for the AccuroUser
   *
   * @return Supervisor UserId
   * @documentationExample 20
   */
  public Integer getSupervisorUserId() {
    return supervisorUserId;
  }

  public void setSupervisorUserId(Integer supervisorUserId) {
    this.supervisorUserId = supervisorUserId;
  }

  /**
   * The Username for the AccuroUser
   *
   * @return Username
   * @documentationExample Test User
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * The displayname for the AccuroUser
   *
   * @return Display name
   * @documentationExample Test User
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * The ActiveDirectoryUser for the AccuroUser
   *
   * @return a activeDirectoryUser
   * @documentationExample Test User
   */
  public String getActiveDirectoryUser() {
    return activeDirectoryUser;
  }

  public void setActiveDirectoryUser(String activeDirectoryUser) {
    this.activeDirectoryUser = activeDirectoryUser;
  }

  /**
   * Indication if the AccuroUser is SystemAdmin or not.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isSystemAdmin() {
    return systemAdmin;
  }

  public void setSystemAdmin(boolean systemAdmin) {
    this.systemAdmin = systemAdmin;
  }

  /**
   * Note for the AccuroUser
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
   * Locked date for the AccuroUser
   *
   * @return A Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLocked() {
    return locked;
  }

  public void setLocked(LocalDateTime locked) {
    this.locked = locked;
  }

  /**
   * Indication if the AccuroUser is active or not.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * The FirstName for the AccuroUser
   *
   * @return firstName
   * @documentationExample Jonathan
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * The effective LastName for the AccuroUser
   *
   * @return lastName
   * @documentationExample Test User
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The HomePhone for the AccuroUser
   *
   * @return homePhone
   */
  public PhoneDto getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(PhoneDto homePhone) {
    this.homePhone = homePhone;
  }

  /**
   * The cellphone for the AccuroUser
   *
   * @return cellPhone
   */
  public PhoneDto getCellPhone() {
    return cellPhone;
  }

  public void setCellPhone(PhoneDto cellPhone) {
    this.cellPhone = cellPhone;
  }

  /**
   * The workPhone for the AccuroUser
   *
   * @return workPhone
   */
  public PhoneDto getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(PhoneDto workPhone) {
    this.workPhone = workPhone;
  }

  /**
   * The email for the AccuroUser
   *
   * @return email
   */
  public EmailDto getEmail() {
    return email;
  }

  public void setEmail(EmailDto email) {
    this.email = email;
  }

  /**
   * The address for the AccuroUser
   *
   * @return Address
   */
  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(AddressDto address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AccuroUserDto)) {
      return false;
    }
    AccuroUserDto that = (AccuroUserDto) o;
    if (userId != that.userId) {
      return false;
    }
    if (systemAdmin != that.systemAdmin) {
      return false;
    }
    if (active != that.active) {
      return false;
    }
    if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) {
      return false;
    }
    if (supervisorUserId != null ? !supervisorUserId.equals(that.supervisorUserId)
        : that.supervisorUserId != null) {
      return false;
    }
    if (username != null ? !username.equals(that.username) : that.username != null) {
      return false;
    }
    if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) {
      return false;
    }
    if (activeDirectoryUser != null ? !activeDirectoryUser.equals(that.activeDirectoryUser)
        : that.activeDirectoryUser != null) {
      return false;
    }
    if (note != null ? !note.equals(that.note) : that.note != null) {
      return false;
    }
    if (locked != null ? !locked.equals(that.locked) : that.locked != null) {
      return false;
    }
    if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
      return false;
    }
    if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) {
      return false;
    }
    if (homePhone != null ? !homePhone.equals(that.homePhone) : that.homePhone != null) {
      return false;
    }
    if (cellPhone != null ? !cellPhone.equals(that.cellPhone) : that.cellPhone != null) {
      return false;
    }
    if (workPhone != null ? !workPhone.equals(that.workPhone) : that.workPhone != null) {
      return false;
    }
    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    return address != null ? address.equals(that.address) : that.address == null;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
    result = 31 * result + (supervisorUserId != null ? supervisorUserId.hashCode() : 0);
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
    result = 31 * result + (activeDirectoryUser != null ? activeDirectoryUser.hashCode() : 0);
    result = 31 * result + (systemAdmin ? 1 : 0);
    result = 31 * result + (note != null ? note.hashCode() : 0);
    result = 31 * result + (locked != null ? locked.hashCode() : 0);
    result = 31 * result + (active ? 1 : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (homePhone != null ? homePhone.hashCode() : 0);
    result = 31 * result + (cellPhone != null ? cellPhone.hashCode() : 0);
    result = 31 * result + (workPhone != null ? workPhone.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    return result;
  }


}
