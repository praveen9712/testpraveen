
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.AddressContactType;
import com.qhrtech.emr.restapi.models.dto.LocationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Contact data transfer object model")
public class ContactDto {

  @JsonProperty(value = "contactId")
  @Schema(description = "Identity of the contact", example = "12")
  private int contactId;

  @NotNull
  @Size(max = 200, message = "Can not exceed 200 characters.")
  @JsonProperty(value = "contactName")
  @Schema(description = "Name of the contact", example = "Water Front Pharmacy")
  private String contactName;

  @NotNull
  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "faxNumber")
  @Schema(description = "Fax number of the contact", example = "(250)321-9876")
  private String faxNumber;

  @NotNull
  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "phoneNumber")
  @Schema(description = "Phone number of the contact", example = "(250)321-9877")
  private String phoneNumber;

  @NotNull
  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "altPhoneNumber")
  @Schema(description = "Alternate phone number of the contact", example = "(250)321-9878")
  private String altPhoneNumber;

  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "phoneExt")
  @Schema(description = "Extension number of the phone", example = "1001")
  private String phoneExt;

  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "altPhoneExt")
  @Schema(description = "Extension number of the alternate phone", example = "1002")
  private String altPhoneExt;

  @NotNull
  @Size(max = 100, message = "Can not exceed 100 characters.")
  @JsonProperty(value = "email")
  @Schema(description = "Email of the contact", example = "abc@mycontact.com")
  private String email;

  @NotNull
  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "mainContact")
  @Schema(description = "Main contact", example = "John")
  private String mainContact;

  @Size(max = 50, message = "Can not exceed 50 characters.")
  @JsonProperty(value = "city")
  @Schema(description = "City name of the contact", example = "Toronto")
  private String city;

  @NotNull
  @Size(max = 100, message = "Can not exceed 100 characters.")
  @JsonProperty(value = "street")
  @Schema(description = "Street of the contact", example = "1500 Queen St")
  private String street;

  @NotNull
  @Size(max = 12, message = "Can not exceed 12 characters.")
  @JsonProperty(value = "postalCode")
  @Schema(description = "Post code of the contact", example = "A1A2B2")
  private String postalCode;

  @NotNull
  @Size(max = 500, message = "Can not exceed 500 characters.")
  @JsonProperty(value = "note")
  @Schema(description = "Note about the contact", example = "Available till 5PM")
  private String note;

  @JsonProperty(value = "contactLocation")
  @Schema(description = "Location of the contact. "
      + "For write/update, only LocationDto#locationId is only considered.")
  private LocationDto contactLocation; // contact location id

  @JsonProperty(value = "locationId")
  @Size(max = 100, message = "Can not exceed 100 characters.")
  @Schema(description = "The contact identifier", example = "100")
  private String locationId; // Identifier

  @NotNull
  @JsonProperty(value = "longDistance")
  @Schema(description = "Indicates if the contact is long distance", example = "true")
  private Boolean longDistance;

  @JsonProperty(value = "eRx")
  @Size(max = 50, message = "Can not exceed 50 characters.")
  @Schema(description = "The e-Prescribing of the contact",
      example = "Tolazamide 100MG for injection")
  private String erx;

  @NotNull
  @JsonProperty(value = "AddressContactType")
  @Schema(description = "The address contact type", example = "Pharmacy")
  private AddressContactType type;

  @NotNull
  @JsonProperty(value = "aliases")
  @Schema(
      description = "Aliases of the contact. All NULL and EMPTY values will be removed from list.",
      type = "array",
      example = "[\"Doctora\",\"Experta\",\"Central Pharm\"]")
  private List<String> aliases;

  @JsonProperty(value = "healthmailAddress")
  @Schema(description = "Healthmail address of the contact")
  private HealthmailAddressDto healthmailAddress;

  /**
   * Healthmail address object.
   *
   * @documentationExample {@link HealthmailAddressDto}
   *
   * @return {@link HealthmailAddressDto} healthmail address
   */
  public HealthmailAddressDto getHealthmailAddress() {
    return healthmailAddress;
  }

  public void setHealthmailAddress(
      HealthmailAddressDto healthmailAddress) {
    this.healthmailAddress = healthmailAddress;
  }

  /**
   * The identity of a contact.
   *
   * @documentationExample 12
   *
   * @return contact id
   */
  public int getContactId() {
    return contactId;
  }

  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * The name of a contact.
   *
   * @documentationExample shoppers drug mart
   *
   * @return contact name
   */
  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * The fax number of a contact.
   *
   * @documentationExample 87367831
   *
   * @return fax number
   */
  public String getFaxNumber() {
    return faxNumber;
  }

  public void setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
  }

  /**
   * The phone number of a contact.
   *
   * @documentationExample 7778888876
   *
   * @return phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * The alternate phone number of a contact.
   *
   * @documentationExample 9998888876
   *
   * @return alternate phone number
   */
  public String getAltPhoneNumber() {
    return altPhoneNumber;
  }

  public void setAltPhoneNumber(String altPhoneNumber) {
    this.altPhoneNumber = altPhoneNumber;
  }

  /**
   * The extension phone number of a contact.
   *
   * @documentationExample 7786
   *
   * @return extension number
   */
  public String getPhoneExt() {
    return phoneExt;
  }

  public void setPhoneExt(String phoneExt) {
    this.phoneExt = phoneExt;
  }

  /**
   * The extension number of alternate phone.
   *
   * @documentationExample 7787
   *
   * @return extension number of alternate phone
   */
  public String getAltPhoneExt() {
    return altPhoneExt;
  }

  public void setAltPhoneExt(String altPhoneExt) {
    this.altPhoneExt = altPhoneExt;
  }

  /**
   * The email of a contact.
   *
   * @documentationExample example@example.com
   *
   * @return contact email
   */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * The main contact of a contact.
   *
   * @documentationExample John McDonald
   *
   * @return main contact
   */
  public String getMainContact() {
    return mainContact;
  }

  public void setMainContact(String mainContact) {
    this.mainContact = mainContact;
  }

  /**
   * The city of a contact.
   *
   * @documentationExample Toronto
   *
   * @return city
   */
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * The street of a contact address.
   *
   * @documentationExample 120 Lawrence Ave
   *
   * @return street address.
   */
  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * The postal code of a contact.
   *
   * @documentationExample M6A 2T9
   *
   * @return postal code.
   */
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * The note of a contact.
   *
   * @documentationExample some note
   *
   * @return note
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * The location details of a contact. For write/update, only LocationDto#locationId is only
   * considered.
   *
   * @documentationExample {@link LocationDto}
   *
   * @return {@link LocationDto} of a contact.
   */
  public LocationDto getContactLocation() {
    return contactLocation;
  }

  public void setContactLocation(LocationDto contactLocation) {
    this.contactLocation = contactLocation;
  }

  /**
   * The location identity of a contact.
   *
   * @documentationExample 12
   *
   * @return location id
   */
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  /**
   * Determines contact is long distance or not.
   *
   * @documentationExample true
   *
   * @return true if contact is long distance otherwise false.
   */
  public Boolean isLongDistance() {
    return longDistance;
  }

  public void setLongDistance(Boolean longDistance) {
    this.longDistance = longDistance;
  }

  /**
   * The eRx of a contact.
   *
   * @documentationExample some electronic prescription.
   *
   * @return string eRx.
   */
  public String getERx() {
    return erx;
  }

  public void setERx(String erx) {
    this.erx = erx;
  }

  /**
   * The address contact type
   *
   * @documentationExample Pharmacy
   *
   * @return {@link AddressContactType}
   */
  public AddressContactType getType() {
    return type;
  }

  public void setType(AddressContactType type) {
    this.type = type;
  }

  /**
   * Alias list of a contact. All NULL and EMPTY values will be removed from list.
   *
   * @documentationExample Central Pharm
   *
   * @return A {@link List} of contact aliases.
   */
  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ContactDto that = (ContactDto) o;

    if (contactId != that.contactId) {
      return false;
    }
    if (!Objects.equals(longDistance, that.longDistance)) {
      return false;
    }
    if (!Objects.equals(contactName, that.contactName)) {
      return false;
    }
    if (!Objects.equals(faxNumber, that.faxNumber)) {
      return false;
    }
    if (!Objects.equals(phoneNumber, that.phoneNumber)) {
      return false;
    }
    if (!Objects.equals(altPhoneNumber, that.altPhoneNumber)) {
      return false;
    }
    if (!Objects.equals(phoneExt, that.phoneExt)) {
      return false;
    }
    if (!Objects.equals(altPhoneExt, that.altPhoneExt)) {
      return false;
    }
    if (!Objects.equals(email, that.email)) {
      return false;
    }
    if (!Objects.equals(mainContact, that.mainContact)) {
      return false;
    }
    if (!Objects.equals(city, that.city)) {
      return false;
    }
    if (!Objects.equals(street, that.street)) {
      return false;
    }
    if (!Objects.equals(postalCode, that.postalCode)) {
      return false;
    }
    if (!Objects.equals(note, that.note)) {
      return false;
    }
    if (!Objects.equals(contactLocation, that.contactLocation)) {
      return false;
    }
    if (!Objects.equals(locationId, that.locationId)) {
      return false;
    }
    if (!Objects.equals(erx, that.erx)) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    if (!Objects.equals(aliases, that.aliases)) {
      return false;
    }
    return Objects.equals(healthmailAddress, that.healthmailAddress);
  }

  @Override
  public int hashCode() {
    int result = contactId;
    result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
    result = 31 * result + (faxNumber != null ? faxNumber.hashCode() : 0);
    result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
    result = 31 * result + (altPhoneNumber != null ? altPhoneNumber.hashCode() : 0);
    result = 31 * result + (phoneExt != null ? phoneExt.hashCode() : 0);
    result = 31 * result + (altPhoneExt != null ? altPhoneExt.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (mainContact != null ? mainContact.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (street != null ? street.hashCode() : 0);
    result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
    result = 31 * result + (note != null ? note.hashCode() : 0);
    result = 31 * result + (contactLocation != null ? contactLocation.hashCode() : 0);
    result = 31 * result + (locationId != null ? locationId.hashCode() : 0);
    result = 31 * result + (longDistance != null ? longDistance.hashCode() : 0);
    result = 31 * result + (erx != null ? erx.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (aliases != null ? aliases.hashCode() : 0);
    result = 31 * result + (healthmailAddress != null ? healthmailAddress.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ContactDto{");
    sb.append("contactId=").append(contactId);
    sb.append(", contactName='").append(contactName).append('\'');
    sb.append(", faxNumber='").append(faxNumber).append('\'');
    sb.append(", phoneNumber='").append(phoneNumber).append('\'');
    sb.append(", altPhoneNumber='").append(altPhoneNumber).append('\'');
    sb.append(", phoneExt='").append(phoneExt).append('\'');
    sb.append(", altPhoneExt='").append(altPhoneExt).append('\'');
    sb.append(", email='").append(email).append('\'');
    sb.append(", mainContact='").append(mainContact).append('\'');
    sb.append(", city='").append(city).append('\'');
    sb.append(", street='").append(street).append('\'');
    sb.append(", postalCode='").append(postalCode).append('\'');
    sb.append(", note='").append(note).append('\'');
    sb.append(", contactLocation=").append(contactLocation);
    sb.append(", locationId='").append(locationId).append('\'');
    sb.append(", longDistance=").append(longDistance);
    sb.append(", erx='").append(erx).append('\'');
    sb.append(", type=").append(type);
    sb.append(", aliases=").append(aliases);
    sb.append(", healthmailAddress=").append(healthmailAddress);
    sb.append('}');
    return sb.toString();
  }
}
