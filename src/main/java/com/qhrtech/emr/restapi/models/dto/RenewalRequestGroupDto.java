
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTime2Range;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The renewal request group transfer model object.")
public class RenewalRequestGroupDto {

  private int id;
  private String additionalInstructions;

  @Schema(description = "The original prescriber name", example = "The prescriber name")
  @Size(max = 255)
  private String originalPrescriberName;

  @Schema(description = "The pharmacy patient Id", example = "112")
  @NotNull
  private Integer pharmacyPatientId;

  @Schema(description = "The matched PatientId", example = "10")
  private Integer matchedPatientId;

  @Schema(description = "The accuro Physician Id", example = "10")
  private Integer accuroPhysicianId;

  @Schema(description = "The matched Pharmacy Contact Id", example = "10")
  private Integer matchedPharmacyContactId;

  @Size(max = 255)
  @Schema(description = "The pharmacy Name", example = "Shoppers Drug Mart")
  private String pharmacyName;

  @Size(max = 255)
  @Schema(description = "The pharmacy Address Line", example = "233 Doug Rd")
  private String pharmacyAddressLine;

  @Size(max = 255)
  @Schema(description = "The pharmacy Address Line 2", example = "233 Doug Rd")
  private String pharmacyAddressLine2;

  @Size(max = 255)
  @Schema(description = "pharmacy City ", example = "Vernon")
  private String pharmacyCity;

  @Size(max = 255)
  @Schema(description = "pharmacy State ", example = "BC")
  private String pharmacyState;

  @Size(max = 255)
  @Schema(description = "pharmacy Postal Code ", example = "V1P 5X4")
  private String pharmacyPostalCode;

  @Size(max = 255)
  @Schema(description = "pharmacy Phone Number ", example = "123-223-4569")
  private String pharmacyPhoneNumber;

  @Size(max = 255)
  @Schema(description = "pharmacyPostalCode ", example = "232-565-445")
  private String pharmacyFaxNumber;

  @Schema(description = "The conversation message Id", example = "112")
  @NotNull
  private Integer conversationMessageId;

  @JsonProperty("createdDateUtc")
  @CheckLocalDateTime2Range
  @Schema(description = "The created datetime in UTC. ", example = "2021-07-01T07:45:59.000")
  private LocalDateTime createdDateUtc;

  @Size(max = 255)
  @Schema(description = "original Prescriber Clinic Address", example = "112")

  private String originalPrescriberClinicAddress;
  @Size(max = 255)
  @Schema(description = "original Prescriber Phone", example = "232-565-445")
  private String originalPrescriberPhone;

  @Size(max = 255)
  @Schema(description = "original Prescriber Fax", example = "232-565-445")
  private String originalPrescriberFax;

  @Size(max = 255)
  @Schema(description = "original Prescriber LicenseNumber", example = "1212111")
  private String originalPrescriberLicenseNumber;

  @Size(max = 255)
  @Schema(description = "original Prescriber Specialty", example = "GP")
  private String originalPrescriberSpecialty;

  @JsonProperty("processedDateUtc")
  @CheckLocalDateTime2Range
  @Schema(description = "The processed datetime in UTC. ", example = "2021-07-01T07:45:59.000")
  private LocalDateTime processedDateUtc;

  @JsonProperty("messageHeaderUuid")
  @Schema(description = "Message header UUID", example = "123e4567-e89b-12d3-a456-426614174000")
  @Size(max = 255)
  private String messageHeaderUuid;


  /**
   * The unique ID for the renewal request group
   *
   * @return {@link Integer} renewal request group ID
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Additional instructions for the renewal request group
   *
   * @return {@link String} renewal request group additional instructions
   * @documentationExample Additional instructions
   */
  public String getAdditionalInstructions() {
    return additionalInstructions;
  }

  public void setAdditionalInstructions(String additionalInstructions) {
    this.additionalInstructions = additionalInstructions;
  }

  /**
   * Original prescriber name for the renewal request group
   *
   * @return {@link String} renewal request group original prescriber name
   * @documentationExample Prescriber name
   */
  public String getOriginalPrescriberName() {
    return originalPrescriberName;
  }

  public void setOriginalPrescriberName(String originalPrescriberName) {
    this.originalPrescriberName = originalPrescriberName;
  }

  /**
   * The renewal request group pharmacy patient ID
   *
   * @return {@link Integer} renewal request group pharmacy patient ID
   * @documentationExample 12
   */
  public Integer getPharmacyPatientId() {
    return pharmacyPatientId;
  }

  public void setPharmacyPatientId(Integer pharmacyPatientId) {
    this.pharmacyPatientId = pharmacyPatientId;
  }

  /**
   * The renewal request group matched patient ID
   *
   * @return {@link Integer} renewal request group matched patient ID
   * @documentationExample 12
   */
  public Integer getMatchedPatientId() {
    return matchedPatientId;
  }

  public void setMatchedPatientId(Integer matchedPatientId) {
    this.matchedPatientId = matchedPatientId;
  }

  /**
   * The accuro physician ID
   *
   * @return {@link Integer} Accuro physician ID
   * @documentationExample 12
   */
  public Integer getAccuroPhysicianId() {
    return accuroPhysicianId;
  }

  public void setAccuroPhysicianId(Integer accuroPhysicianId) {
    this.accuroPhysicianId = accuroPhysicianId;
  }

  /**
   * Matched pharmacy contact ID
   *
   * @return {@link Integer} Matched pharmacy contact ID
   * @documentationExample 12
   */
  public Integer getMatchedPharmacyContactId() {
    return matchedPharmacyContactId;
  }

  public void setMatchedPharmacyContactId(Integer matchedPharmacyContactId) {
    this.matchedPharmacyContactId = matchedPharmacyContactId;
  }

  /**
   * Pharmacy name
   *
   * @return {@link String} Pharmacy name
   * @documentationExample Pharmacy name
   */
  public String getPharmacyName() {
    return pharmacyName;
  }

  public void setPharmacyName(String pharmacyName) {
    this.pharmacyName = pharmacyName;
  }

  /**
   * Pharmacy address line
   *
   * @return {@link String} Pharmacy address line
   * @documentationExample 812 main street
   */
  public String getPharmacyAddressLine() {
    return pharmacyAddressLine;
  }

  public void setPharmacyAddressLine(String pharmacyAddressLine) {
    this.pharmacyAddressLine = pharmacyAddressLine;
  }

  /**
   * Pharmacy address line 2
   *
   * @return {@link String} Pharmacy address line 2
   * @documentationExample 67th Avenue
   */
  public String getPharmacyAddressLine2() {
    return pharmacyAddressLine2;
  }

  public void setPharmacyAddressLine2(String pharmacyAddressLine2) {
    this.pharmacyAddressLine2 = pharmacyAddressLine2;
  }

  /**
   * Pharmacy city
   *
   * @return {@link String} Pharmacy city
   * @documentationExample Vancouver
   */
  public String getPharmacyCity() {
    return pharmacyCity;
  }

  public void setPharmacyCity(String pharmacyCity) {
    this.pharmacyCity = pharmacyCity;
  }

  /**
   * Pharmacy state
   *
   * @return {@link String} Pharmacy state
   * @documentationExample British Columbia
   */
  public String getPharmacyState() {
    return pharmacyState;
  }

  public void setPharmacyState(String pharmacyState) {
    this.pharmacyState = pharmacyState;
  }

  /**
   * Pharmacy postal code
   *
   * @return {@link String} Pharmacy postal code
   * @documentationExample V1Y 2W2
   */
  public String getPharmacyPostalCode() {
    return pharmacyPostalCode;
  }

  public void setPharmacyPostalCode(String pharmacyPostalCode) {
    this.pharmacyPostalCode = pharmacyPostalCode;
  }

  /**
   * Pharmacy phone number
   *
   * @return {@link String} Pharmacy phone number
   * @documentationExample 624 121 3030
   */
  public String getPharmacyPhoneNumber() {
    return pharmacyPhoneNumber;
  }

  public void setPharmacyPhoneNumber(String pharmacyPhoneNumber) {
    this.pharmacyPhoneNumber = pharmacyPhoneNumber;
  }

  /**
   * Pharmacy fax number
   *
   * @return {@link String} Pharmacy fax number
   * @documentationExample 624 121 3030
   */
  public String getPharmacyFaxNumber() {
    return pharmacyFaxNumber;
  }

  public void setPharmacyFaxNumber(String pharmacyFaxNumber) {
    this.pharmacyFaxNumber = pharmacyFaxNumber;
  }

  /**
   * Conversation message ID
   *
   * @return {@link Integer} Conversation message ID
   * @documentationExample 624
   */
  public Integer getConversationMessageId() {
    return conversationMessageId;
  }

  public void setConversationMessageId(Integer conversationMessageId) {
    this.conversationMessageId = conversationMessageId;
  }

  /**
   * Renewal request group created date time in UTC
   *
   * @return {@link LocalDateTime} Renewal request group created date time
   * @documentationExample 2012-02-15T07:44:59.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDateUtc() {
    return createdDateUtc;
  }

  public void setCreatedDateUtc(LocalDateTime createdDateUtc) {
    this.createdDateUtc = createdDateUtc;
  }

  /**
   * Renewal request group original prescriber clinic address
   *
   * @return {@link String} Original prescriber clinic address
   * @documentationExample Clinic address
   */
  public String getOriginalPrescriberClinicAddress() {
    return originalPrescriberClinicAddress;
  }

  public void setOriginalPrescriberClinicAddress(String originalPrescriberClinicAddress) {
    this.originalPrescriberClinicAddress = originalPrescriberClinicAddress;
  }

  /**
   * Renewal request group original prescriber clinic address
   *
   * @return {@link String} Original prescriber clinic address
   * @documentationExample Clinic address
   */
  public String getOriginalPrescriberPhone() {
    return originalPrescriberPhone;
  }

  public void setOriginalPrescriberPhone(String originalPrescriberPhone) {
    this.originalPrescriberPhone = originalPrescriberPhone;
  }

  /**
   * Renewal request group original prescriber fax
   *
   * @return {@link String} Original prescriber fax
   * @documentationExample Clinic address
   */
  public String getOriginalPrescriberFax() {
    return originalPrescriberFax;
  }

  public void setOriginalPrescriberFax(String originalPrescriberFax) {
    this.originalPrescriberFax = originalPrescriberFax;
  }

  /**
   * Renewal request group original prescriber license number
   *
   * @return {@link String} Original prescriber license number
   * @documentationExample prescriber license number
   */
  public String getOriginalPrescriberLicenseNumber() {
    return originalPrescriberLicenseNumber;
  }

  public void setOriginalPrescriberLicenseNumber(String originalPrescriberLicenseNumber) {
    this.originalPrescriberLicenseNumber = originalPrescriberLicenseNumber;
  }

  /**
   * Renewal request group original prescriber specialty
   *
   * @return {@link String} Original prescriber specialty
   * @documentationExample prescriber specialty
   */
  public String getOriginalPrescriberSpecialty() {
    return originalPrescriberSpecialty;
  }

  public void setOriginalPrescriberSpecialty(String originalPrescriberSpecialty) {
    this.originalPrescriberSpecialty = originalPrescriberSpecialty;
  }

  /**
   * Renewal request group processed date time in UTC
   *
   * @return {@link LocalDateTime} Processed date time
   * @documentationExample 2012-02-15T07:44:59.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getProcessedDateUtc() {
    return processedDateUtc;
  }

  public void setProcessedDateUtc(LocalDateTime processedDateUtc) {
    this.processedDateUtc = processedDateUtc;
  }

  /**
   * Message header uuid
   *
   * @return String uuid
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public String getMessageHeaderUuid() {
    return messageHeaderUuid;
  }

  public void setMessageHeaderUuid(String messageHeaderUuid) {
    this.messageHeaderUuid = messageHeaderUuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RenewalRequestGroupDto that = (RenewalRequestGroupDto) o;

    if (id != that.id) {
      return false;
    }
    if (!Objects.equals(additionalInstructions, that.additionalInstructions)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberName, that.originalPrescriberName)) {
      return false;
    }
    if (!Objects.equals(pharmacyPatientId, that.pharmacyPatientId)) {
      return false;
    }
    if (!Objects.equals(matchedPatientId, that.matchedPatientId)) {
      return false;
    }
    if (!Objects.equals(accuroPhysicianId, that.accuroPhysicianId)) {
      return false;
    }
    if (!Objects.equals(matchedPharmacyContactId, that.matchedPharmacyContactId)) {
      return false;
    }
    if (!Objects.equals(pharmacyName, that.pharmacyName)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressLine, that.pharmacyAddressLine)) {
      return false;
    }
    if (!Objects.equals(pharmacyAddressLine2, that.pharmacyAddressLine2)) {
      return false;
    }
    if (!Objects.equals(pharmacyCity, that.pharmacyCity)) {
      return false;
    }
    if (!Objects.equals(pharmacyState, that.pharmacyState)) {
      return false;
    }
    if (!Objects.equals(pharmacyPostalCode, that.pharmacyPostalCode)) {
      return false;
    }
    if (!Objects.equals(pharmacyPhoneNumber, that.pharmacyPhoneNumber)) {
      return false;
    }
    if (!Objects.equals(pharmacyFaxNumber, that.pharmacyFaxNumber)) {
      return false;
    }
    if (!Objects.equals(conversationMessageId, that.conversationMessageId)) {
      return false;
    }
    if (!Objects.equals(createdDateUtc, that.createdDateUtc)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberClinicAddress, that.originalPrescriberClinicAddress)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberPhone, that.originalPrescriberPhone)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberFax, that.originalPrescriberFax)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberLicenseNumber, that.originalPrescriberLicenseNumber)) {
      return false;
    }
    if (!Objects.equals(originalPrescriberSpecialty, that.originalPrescriberSpecialty)) {
      return false;
    }
    if (!Objects.equals(messageHeaderUuid, that.messageHeaderUuid)) {
      return false;
    }
    return Objects.equals(processedDateUtc, that.processedDateUtc);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (additionalInstructions != null ? additionalInstructions.hashCode() : 0);
    result = 31 * result + (originalPrescriberName != null ? originalPrescriberName.hashCode() : 0);
    result = 31 * result + (pharmacyPatientId != null ? pharmacyPatientId.hashCode() : 0);
    result = 31 * result + (matchedPatientId != null ? matchedPatientId.hashCode() : 0);
    result = 31 * result + (accuroPhysicianId != null ? accuroPhysicianId.hashCode() : 0);
    result =
        31 * result + (matchedPharmacyContactId != null ? matchedPharmacyContactId.hashCode() : 0);
    result = 31 * result + (pharmacyName != null ? pharmacyName.hashCode() : 0);
    result = 31 * result + (pharmacyAddressLine != null ? pharmacyAddressLine.hashCode() : 0);
    result = 31 * result + (pharmacyAddressLine2 != null ? pharmacyAddressLine2.hashCode() : 0);
    result = 31 * result + (pharmacyCity != null ? pharmacyCity.hashCode() : 0);
    result = 31 * result + (pharmacyState != null ? pharmacyState.hashCode() : 0);
    result = 31 * result + (pharmacyPostalCode != null ? pharmacyPostalCode.hashCode() : 0);
    result = 31 * result + (pharmacyPhoneNumber != null ? pharmacyPhoneNumber.hashCode() : 0);
    result = 31 * result + (pharmacyFaxNumber != null ? pharmacyFaxNumber.hashCode() : 0);
    result = 31 * result + (conversationMessageId != null ? conversationMessageId.hashCode() : 0);
    result = 31 * result + (createdDateUtc != null ? createdDateUtc.hashCode() : 0);
    result =
        31 * result + (originalPrescriberClinicAddress != null ? originalPrescriberClinicAddress
            .hashCode() : 0);
    result =
        31 * result + (originalPrescriberPhone != null ? originalPrescriberPhone.hashCode() : 0);
    result = 31 * result + (originalPrescriberFax != null ? originalPrescriberFax.hashCode() : 0);
    result =
        31 * result + (originalPrescriberLicenseNumber != null ? originalPrescriberLicenseNumber
            .hashCode() : 0);
    result =
        31 * result + (originalPrescriberSpecialty != null ? originalPrescriberSpecialty.hashCode()
            : 0);
    result = 31 * result + (processedDateUtc != null ? processedDateUtc.hashCode() : 0);
    result = 31 * result + (messageHeaderUuid != null ? messageHeaderUuid.hashCode() : 0);
    return result;
  }
}
