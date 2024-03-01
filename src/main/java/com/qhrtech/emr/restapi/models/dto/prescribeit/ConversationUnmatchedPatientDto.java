
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDate;

/**
 * The Conversation unmatched patient data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.synapse.ConversationUnmatchedPatient
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationUnmatchedPatientDto {

  @JsonProperty("id")
  @Schema(description = "Id of the unmatched patient", example = "1")
  private int id;

  @JsonProperty("conversationId")
  @Schema(description = "Conversation ID",
      type = "integer",
      example = "19")
  @NotNull
  private Integer conversationId;

  @JsonProperty("firstName")
  @Schema(description = "First name of the patient.",
      type = "string",
      example = "John")
  @Size(max = 100, message = "First name should not be more than 100 characters.")
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "Last name of the patient.",
      type = "string",
      example = "Shaw")
  @Size(max = 100, message = "Last name should not be more than 100 characters.")
  private String lastName;

  @JsonProperty("dateOfBirth")
  @Schema(description = "The patient's date of birth. Format should be: YYYY-MM-DD.",
      type = "string",
      example = "1959-02-16")
  @CheckLocalDateRange
  private LocalDate dateOfBirth;

  @JsonProperty("sex")
  @Schema(description = "The patient sex.",
      type = "string",
      example = "Male")
  @Size(max = 10, message = "Maximum allowed length is 10.")
  private String sex;

  @JsonProperty("phn")
  @Schema(description = "Patient health number.",
      type = "string",
      example = "1001010")
  @Size(max = 30, message = "Maximum allowed length is 30.")
  private String phn;

  @JsonProperty("patientId")
  @Schema(description = "Patient ID",
      type = "integer",
      example = "19")
  private Integer patientId;

  @JsonProperty("externalIdentifier")
  @Schema(description = "External identifier.",
      type = "string",
      example = "123e4567-e89b-12d3-a456-426614174000")
  @Size(max = 64, message = "Maximum allowed length is 64.")
  private String externalIdentifier;

  @JsonProperty("patientHeightValue")
  @Schema(description = "Patient height value",
      type = "integer",
      example = "10")
  private Integer patientHeightValue;

  @JsonProperty("patientHeightUnit")
  @Schema(description = "Unit in which height is measured.",
      type = "string",
      example = "inches")
  @Size(max = 100, message = "Maximum allowed length is 100.")
  private String patientHeightUnit;

  @JsonProperty("patientHeightSystem")
  @Schema(description = "Patient height system.",
      type = "string",
      example = "Metric")
  @Size(max = 100, message = "Maximum allowed length is 100.")
  private String patientHeightSystem;

  @JsonProperty("patientWeightValue")
  @Schema(description = "Patient weight system.",
      type = "integer",
      example = "10")
  private Integer patientWeightValue;

  @JsonProperty("patientWeightUnit")
  @Schema(description = "Patient weight unit.",
      type = "string",
      example = "Kilogram")
  @Size(max = 100, message = "Maximum allowed length is 100.")
  private String patientWeightUnit;

  @JsonProperty("patientWeightSystem")
  @Schema(description = "Patient weight system.",
      type = "string",
      example = "Metric")
  @Size(max = 100, message = "Maximum allowed length is 100.")
  private String patientWeightSystem;

  @JsonProperty("patientAddress")
  @Schema(description = "Patient address.",
      type = "string",
      example = "1212 Lakeshore dr, Kelowna, BC, Canada")
  @Size(max = 500, message = "Maximum allowed length is 500.")
  private String patientAddress;

  @JsonProperty("patientPhone")
  @Schema(description = "Patient phone.",
      type = "string",
      example = "121-288-8212")
  @Size(max = 50, message = "Maximum allowed length is 50.")
  private String patientPhone;

  @JsonProperty("allergyList")
  @Schema(description = "Patient's allergy list.",
      type = "string",
      example = "List of allergies")
  @Size(max = 2000, message = "Maximum allowed length is 2000.")
  private String allergyList;


  /**
   * Unique ID for conversation unmatched patient
   *
   * @documentationExample 12
   *
   * @return Unique ID of unmatched patient
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Unique ID for conversation this unmatched patient is part of.
   *
   * @documentationExample 12
   *
   * @return Unique ID of conversation
   */
  public Integer getConversationId() {
    return conversationId;
  }

  public void setConversationId(Integer conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * First name of unmatched patient
   *
   * @documentationExample John
   *
   * @return First name
   */
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last name of unmatched patient
   *
   * @documentationExample Smith
   *
   * @return Last name
   */
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Date of birth of unmatched patient
   *
   * @documentationExample 1993-12-31
   *
   * @return Date of birth
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Gender of unmatched patient
   *
   * @documentationExample Male
   *
   * @return Gender of patient
   */
  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  /**
   * Patient health number of unmatched patient
   *
   * @documentationExample 123456789
   *
   * @return Patient Health Number
   */
  public String getPhn() {
    return phn;
  }

  public void setPhn(String phn) {
    this.phn = phn;
  }

  /**
   * Accuro Patient ID of unmatched patient
   *
   * @documentationExample 10
   *
   * @return Patient ID
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * External identifier of unmatched patient
   *
   * @documentationExample identifier
   *
   * @return External identifier
   */
  public String getExternalIdentifier() {
    return externalIdentifier;
  }

  public void setExternalIdentifier(String externalIdentifier) {
    this.externalIdentifier = externalIdentifier;
  }

  /**
   * Height of unmatched patient
   *
   * @documentationExample 170
   *
   * @return Height
   */
  public Integer getPatientHeightValue() {
    return patientHeightValue;
  }

  public void setPatientHeightValue(Integer patientHeightValue) {
    this.patientHeightValue = patientHeightValue;
  }

  /**
   * Height unit of unmatched patient
   *
   * @documentationExample cm (centimeter)
   *
   * @return Unit of the height
   */
  public String getPatientHeightUnit() {
    return patientHeightUnit;
  }

  public void setPatientHeightUnit(String patientHeightUnit) {
    this.patientHeightUnit = patientHeightUnit;
  }

  /**
   * Height system of unmatched patient
   *
   * @documentationExample height system
   *
   * @return Height system
   */
  public String getPatientHeightSystem() {
    return patientHeightSystem;
  }

  public void setPatientHeightSystem(String patientHeightSystem) {
    this.patientHeightSystem = patientHeightSystem;
  }

  /**
   * Weight of unmatched patient
   *
   * @documentationExample 160
   *
   * @return Weight
   */
  public Integer getPatientWeightValue() {
    return patientWeightValue;
  }

  public void setPatientWeightValue(Integer patientWeightValue) {
    this.patientWeightValue = patientWeightValue;
  }

  /**
   * Weight unit of unmatched patient
   *
   * @documentationExample pounds
   *
   * @return Weight unit
   */
  public String getPatientWeightUnit() {
    return patientWeightUnit;
  }

  public void setPatientWeightUnit(String patientWeightUnit) {
    this.patientWeightUnit = patientWeightUnit;
  }

  /**
   * Weight system of unmatched patient
   *
   * @documentationExample weight system
   *
   * @return weight system
   */
  public String getPatientWeightSystem() {
    return patientWeightSystem;
  }

  public void setPatientWeightSystem(String patientWeightSystem) {
    this.patientWeightSystem = patientWeightSystem;
  }

  /**
   * Address of unmatched patient
   *
   * @documentationExample 120 Main Street
   *
   * @return Address
   */
  public String getPatientAddress() {
    return patientAddress;
  }

  public void setPatientAddress(String patientAddress) {
    this.patientAddress = patientAddress;
  }

  /**
   * Phone of unmatched patient
   *
   * @documentationExample 206-120-3232
   *
   * @return Phone
   */
  public String getPatientPhone() {
    return patientPhone;
  }

  public void setPatientPhone(String patientPhone) {
    this.patientPhone = patientPhone;
  }

  /**
   * List of allergies of unmatched patient
   *
   * @documentationExample Allergies list
   *
   * @return Allergies list
   */
  public String getAllergyList() {
    return allergyList;
  }

  public void setAllergyList(String allergyList) {
    this.allergyList = allergyList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConversationUnmatchedPatientDto that = (ConversationUnmatchedPatientDto) o;

    if (id != that.id) {
      return false;
    }
    if (!Objects.equals(conversationId, that.conversationId)) {
      return false;
    }
    if (!Objects.equals(firstName, that.firstName)) {
      return false;
    }
    if (!Objects.equals(lastName, that.lastName)) {
      return false;
    }
    if (!Objects.equals(dateOfBirth, that.dateOfBirth)) {
      return false;
    }
    if (!Objects.equals(sex, that.sex)) {
      return false;
    }
    if (!Objects.equals(phn, that.phn)) {
      return false;
    }
    if (!Objects.equals(patientId, that.patientId)) {
      return false;
    }
    if (!Objects.equals(externalIdentifier, that.externalIdentifier)) {
      return false;
    }
    if (!Objects.equals(patientHeightValue, that.patientHeightValue)) {
      return false;
    }
    if (!Objects.equals(patientHeightUnit, that.patientHeightUnit)) {
      return false;
    }
    if (!Objects.equals(patientHeightSystem, that.patientHeightSystem)) {
      return false;
    }
    if (!Objects.equals(patientWeightValue, that.patientWeightValue)) {
      return false;
    }
    if (!Objects.equals(patientWeightUnit, that.patientWeightUnit)) {
      return false;
    }
    if (!Objects.equals(patientWeightSystem, that.patientWeightSystem)) {
      return false;
    }
    if (!Objects.equals(patientAddress, that.patientAddress)) {
      return false;
    }
    if (!Objects.equals(patientPhone, that.patientPhone)) {
      return false;
    }
    return Objects.equals(allergyList, that.allergyList);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (conversationId != null ? conversationId.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
    result = 31 * result + (sex != null ? sex.hashCode() : 0);
    result = 31 * result + (phn != null ? phn.hashCode() : 0);
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (externalIdentifier != null ? externalIdentifier.hashCode() : 0);
    result = 31 * result + (patientHeightValue != null ? patientHeightValue.hashCode() : 0);
    result = 31 * result + (patientHeightUnit != null ? patientHeightUnit.hashCode() : 0);
    result = 31 * result + (patientHeightSystem != null ? patientHeightSystem.hashCode() : 0);
    result = 31 * result + (patientWeightValue != null ? patientWeightValue.hashCode() : 0);
    result = 31 * result + (patientWeightUnit != null ? patientWeightUnit.hashCode() : 0);
    result = 31 * result + (patientWeightSystem != null ? patientWeightSystem.hashCode() : 0);
    result = 31 * result + (patientAddress != null ? patientAddress.hashCode() : 0);
    result = 31 * result + (patientPhone != null ? patientPhone.hashCode() : 0);
    result = 31 * result + (allergyList != null ? allergyList.hashCode() : 0);
    return result;
  }
}
