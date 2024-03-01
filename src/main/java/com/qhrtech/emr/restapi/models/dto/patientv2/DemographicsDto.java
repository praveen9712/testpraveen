
package com.qhrtech.emr.restapi.models.dto.patientv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qhrtech.emr.restapi.config.serialization.JodaLocalDateDeserializer;
import com.qhrtech.emr.restapi.models.dto.ContactType;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.joda.time.LocalDate;

/**
 * Demographics of a person associated with Accuro, for example a Patient.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Demographics of a person associated with Accurodecd, e.g. patient.",
    implementation = DemographicsDto.class, name = "DemographicsV2Dto")
public class DemographicsDto {

  @JsonProperty("firstName")
  @Schema(description = "The person's first name", example = "John", type = "string",
      maxLength = 100, nullable = true)
  @Size(max = 100, message = "firstName size should not be greater than 100 characters")
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "The person's last name", example = "Doe", type = "string",
      maxLength = 100, nullable = true)
  @Size(max = 100, message = "lastName size should not be greater than 100 characters")
  private String lastName;

  @JsonProperty("middleName")
  @Schema(description = "The person's middle name", example = "Smith", type = "string",
      maxLength = 100, nullable = true)
  @Size(max = 100, message = "middleName size should not be greater than 100 characters")
  private String middleName;

  @JsonProperty("title")
  @Schema(description = "The person's honorific (Mrs., Dr., etc.)", example = "Mr.",
      type = "string",
      maxLength = 50, nullable = true)
  // title used to have size 50 and latest schema has 20. We are doing business validation
  // for this.
  @Size(max = 50, message = "'title' size should not be greater than 50 characters")
  private String title;

  @JsonProperty("suffix")
  @Schema(description = "The person's name suffix (Jr., Sr., etc.)", example = "Sr.",
      type = "string",
      maxLength = 10, nullable = true)
  @Size(max = 10, message = "suffix size should not be greater than 10 characters")
  private String suffix;

  @JsonProperty("birthday")
  @Schema(description = "The person's date of birth",
      type = "string",
      example = "1959-02-16")
  @CheckLocalDateRange
  @JsonDeserialize(using = JodaLocalDateDeserializer.class)
  private LocalDate birthday;

  @JsonProperty("genderId")
  @Schema(description = "The person's gender", example = "1", required = true)
  private int genderId;

  @JsonProperty("email")
  @Schema(description = "The person's primary email")
  @Valid
  private EmailDto email;

  @JsonProperty("phones")
  @Schema(description = "The person's listed phone numbers", nullable = true)
  @Valid
  private List<PhoneDto> phones;

  @JsonProperty("addresses")
  @Schema(
      description = "The person's listed addresses. Addresses are ordered as follows:\n\n"
          + "1.Primary Address.\n\n"
          + "2.Secondary Address\n\n"
          + "3.All subsequent addresses.\n\n"
          + "For the primary and secondary addresses, the location id of the address can be null. "
          + "However, for all subsequent addresses the location id must be set and valid.",
      nullable = true)
  @Valid
  private List<AddressDto> addresses;

  @JsonProperty("healthCard")
  @Schema(description = "The person's personal health card information", nullable = true)
  @Valid
  private PersonalHealthCardDto healthCard;

  @JsonProperty("preferredContactType")
  @Schema(description = "The person's preferred method of contact", example = "WorkPhone",
      nullable = true)
  private ContactType preferredContactType;

  @JsonProperty("nextKinName")
  @Schema(description = "The persons's listed next of kin", example = "Jane Doe", type = "string",
      maxLength = 50, nullable = true)
  @Size(max = 50, message = "The nextKinName size should not be greater than 50 characters")
  private String nextKinName;

  @JsonProperty("nextKinPhone")
  @Schema(description = "Contact information for the person's next of kin", nullable = true)
  private PhoneDto nextKinPhone;

  @JsonProperty("officialLanguageCode")
  @Schema(description = "ISO_639-2 Language Code representing the official language of the person",
      example = "eng", type = "string",
      maxLength = 3, nullable = true)
  @Size(max = 3, message = "The officialLanguageCode size should not be greater than 3 characters")
  private String officialLanguageCode;

  @JsonProperty("spokenLanguageCode")
  @Schema(
      description = "ISO_639-2 language Code representing the person's primary spoken language",
      example = "bul", type = "string",
      maxLength = 3, nullable = true)
  @Size(max = 3, message = "The spokenLanguageCode size should not be greater than 3 characters")
  private String spokenLanguageCode;

  @JsonProperty("relationshipStatusId")
  @Schema(
      description = "The person's relationship status. Built in statuses are as follows:\n\n"
          + "1.Single\n\n"
          + "2.Married\n\n"
          + "3.Widow\n\n"
          + "4.Widower\n\n"
          + "5.Separated\n\n"
          + "6.Unknown\n\n",
      example = "2", nullable = true, type = "integer")
  private Integer relationshipStatusId;

  /**
   * The person's first name.
   *
   * @return A name String
   * @documentationExample John
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * The person's last name.
   *
   * @return A name String
   * @documentationExample Doe
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The person's middle name.
   *
   * @return A name String
   * @documentationExample Smith
   */
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * The person's Honorific (Mrs., Dr., etc.).
   *
   * @return A title String
   * @documentationExample Mr.
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * The person's name suffix (Jr., Sr., etc.).
   *
   * @return A suffix String
   * @documentationExample Sr.
   */
  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * The person's date of birth.
   *
   * @return A date of birth Calender object
   */
  @DocumentationExample("1959-02-16")
  @TypeHint(String.class)
  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  /**
   * The person's gender.
   *
   * @return A numeric gender ID
   * @documentationExample 1
   */
  public int getGenderId() {
    return genderId;
  }

  public void setGenderId(int genderId) {
    this.genderId = genderId;
  }

  /**
   * The person's primary email.
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
   * The person's listed phone numbers.
   *
   * @return A Phone DTO
   */
  public List<PhoneDto> getPhones() {
    return phones;
  }

  public void setPhones(List<PhoneDto> phones) {
    this.phones = phones;
  }

  /**
   * The person's listed addresses.
   * <p>
   * Addresses are ordered as follows:
   * <p>
   * <ol>
   * <li>Primary Address.
   * <li>Secondary Address.
   * <li>All subsequent addresses.
   * </ol>
   * <p>
   * For the Primary and Secondary Addresses, the Location Id of the address can be null. However,
   * for all subsequent addresses the location id must be set and valid.
   *
   * @return A list of Address DTOs
   */
  public List<AddressDto> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<AddressDto> addresses) {
    this.addresses = addresses;
  }

  /**
   * The person's personal health card information.
   *
   * @return A PersonalHealthCard DTO
   */
  public PersonalHealthCardDto getHealthCard() {
    return healthCard;
  }

  public void setHealthCard(PersonalHealthCardDto healthCard) {
    this.healthCard = healthCard;
  }

  /**
   * The person's preferred method of contact.
   *
   * @return A Contact Type
   * @documentationExample WorkPhone
   */
  public ContactType getPreferredContactType() {
    return preferredContactType;
  }

  public void setPreferredContactType(ContactType preferredContactType) {
    this.preferredContactType = preferredContactType;
  }

  /**
   * The persons's listed next of kin.
   *
   * @return A name String
   * @documentationExample Jane Doe
   */
  public String getNextKinName() {
    return nextKinName;
  }

  public void setNextKinName(String nextKinName) {
    this.nextKinName = nextKinName;
  }

  /**
   * Contact information for the person's next of kin.
   *
   * @return A Phone DTO
   */
  public PhoneDto getNextKinPhone() {
    return nextKinPhone;
  }

  public void setNextKinPhone(PhoneDto nextKinPhone) {
    this.nextKinPhone = nextKinPhone;
  }

  /**
   * ISO_639-2 Language Code representing the official language of the person.
   *
   * @return 3 character ISO_639-2 language code.
   * @documentationExample eng
   */
  public String getOfficialLanguageCode() {
    return officialLanguageCode;
  }

  public void setOfficialLanguageCode(String officialLanguageCode) {
    this.officialLanguageCode = officialLanguageCode;
  }

  /**
   * ISO_639-2 language Code representing the person's primary spoken language
   *
   * @return 3 character ISO_639-2 language code.
   * @documentationExample bul
   */
  public String getSpokenLanguageCode() {
    return spokenLanguageCode;
  }

  public void setSpokenLanguageCode(String spokenLanguageCode) {
    this.spokenLanguageCode = spokenLanguageCode;
  }

  /**
   * <p>
   * The person's relationship status. Built in statuses are as follows:
   * </p>
   * <ul>
   * <li>1 -> Single</li>
   * <li>2 -> Married</li>
   * <li>3 -> Widow</li>
   * <li>4 -> Widower</li>
   * <li>5 -> Separated</li>
   * <li>6 -> Unknown</li>
   * </ul>
   *
   * @return Relationship status ID
   * @documentationExample 2
   */
  public Integer getRelationshipStatusId() {
    return relationshipStatusId;
  }

  public void setRelationshipStatusId(Integer relationshipStatusId) {
    this.relationshipStatusId = relationshipStatusId;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + Objects.hashCode(this.firstName);
    hash = 17 * hash + Objects.hashCode(this.lastName);
    hash = 17 * hash + Objects.hashCode(this.middleName);
    hash = 17 * hash + Objects.hashCode(this.title);
    hash = 17 * hash + Objects.hashCode(this.suffix);
    hash = 17 * hash + Objects.hashCode(this.birthday);
    hash = 17 * hash + this.genderId;
    hash = 17 * hash + Objects.hashCode(this.email);
    hash = 17 * hash + Objects.hashCode(this.phones);
    hash = 17 * hash + Objects.hashCode(this.addresses);
    hash = 17 * hash + Objects.hashCode(this.healthCard);
    hash = 17 * hash + Objects.hashCode(this.preferredContactType);
    hash = 17 * hash + Objects.hashCode(this.nextKinName);
    hash = 17 * hash + Objects.hashCode(this.nextKinPhone);
    hash = 17 * hash + Objects.hashCode(this.officialLanguageCode);
    hash = 17 * hash + Objects.hashCode(this.spokenLanguageCode);
    hash = 17 * hash + Objects.hashCode(this.relationshipStatusId);
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
    final DemographicsDto other = (DemographicsDto) obj;
    if (this.genderId != other.genderId) {
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
    if (!Objects.equals(this.title, other.title)) {
      return false;
    }
    if (!Objects.equals(this.suffix, other.suffix)) {
      return false;
    }
    if (!Objects.equals(this.nextKinName, other.nextKinName)) {
      return false;
    }
    if (!Objects.equals(this.officialLanguageCode, other.officialLanguageCode)) {
      return false;
    }
    if (!Objects.equals(this.spokenLanguageCode, other.spokenLanguageCode)) {
      return false;
    }
    if (!Objects.equals(this.birthday, other.birthday)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.phones, other.phones)) {
      return false;
    }
    if (!Objects.equals(this.addresses, other.addresses)) {
      return false;
    }
    if (!Objects.equals(this.healthCard, other.healthCard)) {
      return false;
    }
    if (this.preferredContactType != other.preferredContactType) {
      return false;
    }
    if (!Objects.equals(this.nextKinPhone, other.nextKinPhone)) {
      return false;
    }
    if (!Objects.equals(this.relationshipStatusId, other.relationshipStatusId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DemographicsDto{");
    sb.append("firstName='").append(firstName).append('\'');
    sb.append(", lastName='").append(lastName).append('\'');
    sb.append(", middleName='").append(middleName).append('\'');
    sb.append(", title='").append(title).append('\'');
    sb.append(", suffix='").append(suffix).append('\'');
    sb.append(", birthday=").append(birthday);
    sb.append(", genderId=").append(genderId);
    sb.append(", email=").append(email);
    sb.append(", phones=").append(phones);
    sb.append(", addresses=").append(addresses);
    sb.append(", healthCard=").append(healthCard);
    sb.append(", preferredContactType=").append(preferredContactType);
    sb.append(", nextKinName='").append(nextKinName).append('\'');
    sb.append(", nextKinPhone=").append(nextKinPhone);
    sb.append(", officialLanguageCode='").append(officialLanguageCode).append('\'');
    sb.append(", spokenLanguageCode='").append(spokenLanguageCode).append('\'');
    sb.append(", relationshipStatusId=").append(relationshipStatusId);
    sb.append('}');
    return sb.toString();
  }
}
