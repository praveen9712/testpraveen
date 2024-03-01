
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Insurer object model
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Insurer data transfer object model")
public class InsurerDto {

  @JsonProperty("insurerId")
  @Schema(description = "Unique insurer id", example = "1")
  private Integer insurerId;

  @JsonProperty("name")
  @Schema(description = "Name of insurer", example = "ICBC")
  private String name;

  @JsonProperty("officePhone")
  @Schema(description = "Insurer's office phone number")
  @Valid
  private PhoneDto officePhone;

  @JsonProperty("faxPhone")
  @Schema(description = "Office fax number")
  @Valid
  private PhoneDto faxPhone;

  @JsonProperty("email")
  @Schema(description = "Office email address")
  private EmailDto email;

  @JsonProperty("address")
  @Schema(description = "Office address")
  private AddressDto address;

  @JsonProperty("private")
  @Schema(description = "Indication if the insurance provider if private or not",
      example = "false")
  private Boolean privateInsurer;

  @JsonProperty("taxExempt")
  @Schema(description = "Indication if the insurer is tax exempt", example = "true")
  private Boolean taxExempt;

  @JsonProperty("externalId")
  @Schema(description = "The insurance provider's external id", example = "1")
  private String externalId;

  @JsonProperty("zeroBillAmount")
  @Schema(description = "Indication if zero bill amount applies to the insurance provider",
      example = "true")
  private Boolean zeroBillAmount;

  @JsonProperty("forAllPatients")
  @Schema(description = "Indication if the insurer can be applied to all patients",
      example = "false")
  private Boolean forAllPatients;

  @JsonProperty("isIcbc")
  @Schema(description = "Indication if the insurance provider belongs to ICBC or not",
      example = "true")
  private Boolean icbc;

  @JsonProperty("isWcb")
  @Schema(description = "Indication if the insurance provider belongs to WCB or not",
      example = "false")
  private Boolean wcb;

  @JsonProperty("isMsp")
  @Schema(description = "Indication if the insurance provider belongs to MSP or not",
      example = "false")
  private Boolean msp;

  @JsonProperty("active")
  @Schema(description = "Indication if the insurance provider is active or not", example = "true")
  private Boolean active;

  @JsonProperty("displayName")
  @Schema(description = "The insurance provider's display name", example = "ICBC")
  private String displayName;

  @JsonProperty("groupCode")
  @Schema(description = "The insurer's group code", example = "1")
  private String groupCode;

  @JsonProperty("priceListId")
  @Schema(description = "Price list id", example = "1")
  private Integer priceListId;

  @JsonProperty("immediatePaymentRequired")
  @Schema(description = "Indication if immediate payment is required for this insurer",
      example = "true")
  private Boolean immediatePaymentRequired;

  @JsonProperty("sortPriority")
  @Schema(description = "Insurer's sort priority", example = "2")
  private Integer sortPriority;

  @JsonProperty("deleted")
  @Schema(description = "Indication if the insurance provider has been deleted or not",
      example = "false")
  private Boolean deleted;

  @JsonProperty("regionalMedicalAssociates")
  @Schema(description = "Indication if the insurer is a regional medical associate",
      example = "false")
  private Boolean regionalMedicalAssociates;

  /**
   * Unique insurer ID
   *
   * @documentationExample 1
   *
   * @return Insurer ID
   */
  public Integer getInsurerId() {
    return insurerId;
  }

  public void setInsurerId(Integer insurerId) {
    this.insurerId = insurerId;
  }

  /**
   * Name of insurer
   *
   * @documentationExample ICBC
   *
   * @return Insurer name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Insurer's office phone number
   *
   * @return Phone DTO
   */
  public PhoneDto getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(PhoneDto officePhone) {
    this.officePhone = officePhone;
  }

  /**
   * Office fax number
   *
   * @return Phone DTO
   */
  public PhoneDto getFaxPhone() {
    return faxPhone;
  }

  public void setFaxPhone(PhoneDto faxPhone) {
    this.faxPhone = faxPhone;
  }

  /**
   * Office email address
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
   * Office address
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
   * Indication if the insurance provider if private or not.
   *
   * @documentationExample false
   *
   * @return <code>true</code> if private, or else <code>false</code>
   */
  public Boolean getPrivateInsurer() {
    return privateInsurer;
  }

  public void setPrivateInsurer(Boolean privateInsurer) {
    this.privateInsurer = privateInsurer;
  }

  /**
   * Indication if the insurer is tax exempt.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if tax exempt, or else <code>false</code>
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  public void setTaxExempt(Boolean taxExempt) {
    this.taxExempt = taxExempt;
  }

  /**
   * The insurance provider's external ID
   *
   * @documentationExample 1
   *
   * @return ID String
   */
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  /**
   * Indication if zero bill amount applies to the insurance provider.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if zero bill amount applies, or else <code>false</code>
   */
  public Boolean getZeroBillAmount() {
    return zeroBillAmount;
  }

  public void setZeroBillAmount(Boolean zeroBillAmount) {
    this.zeroBillAmount = zeroBillAmount;
  }

  /**
   * Indication if the insurer can be applied to all patients.
   *
   * @documentationExample false
   *
   * @return <code>true</code> or <code>false</code>
   */
  public Boolean getForAllPatients() {
    return forAllPatients;
  }

  public void setForAllPatients(Boolean forAllPatients) {
    this.forAllPatients = forAllPatients;
  }

  /**
   * Indication if the insurance provider belongs to ICBC or not.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if insurance provider belongs to ICBC, or else <code>false</code>
   */
  public Boolean getIcbc() {
    return icbc;
  }

  public void setIcbc(Boolean icbc) {
    this.icbc = icbc;
  }

  /**
   * Indication if the insurance provider belongs to WCB or not.
   *
   * @documentationExample false
   *
   * @return <code>true</code> if insurance provider belongs to WCB, or else <code>false</code>
   */
  public Boolean getWcb() {
    return wcb;
  }

  public void setWcb(Boolean wcb) {
    this.wcb = wcb;
  }

  /**
   * Indication if the insurance provider belongs to MSP or not.
   *
   * @documentationExample false
   *
   * @return <code>true</code> if insurance provider belongs to MSP, or else <code>false</code>
   */
  public Boolean getMsp() {
    return msp;
  }

  public void setMsp(Boolean msp) {
    this.msp = msp;
  }

  /**
   * Indication if the insurance provider is active or not.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if insurance provider is active, or else <code>false</code>
   */
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  /**
   * The insurance provider's display name
   *
   * @documentationExample ICBC
   *
   * @return Display name
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * The insurer's group code.
   *
   * @documentationExample 1
   *
   * @return Group Code
   */
  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  /**
   * Price list ID
   *
   * @documentationExample 1
   *
   * @return Price List ID
   */
  public Integer getPriceListId() {
    return priceListId;
  }

  public void setPriceListId(Integer priceListId) {
    this.priceListId = priceListId;
  }

  /**
   * Indication if immediate payment is required for this insurer.
   *
   * @documentationExample true
   *
   * @return <code>true</code> or <code>false</code>
   */
  public Boolean getImmediatePaymentRequired() {
    return immediatePaymentRequired;
  }

  public void setImmediatePaymentRequired(Boolean immediatePaymentRequired) {
    this.immediatePaymentRequired = immediatePaymentRequired;
  }

  /**
   * Insurer's sort priority
   *
   * @documentationExample 2
   *
   * @return Sort priority
   */
  public Integer getSortPriority() {
    return sortPriority;
  }

  public void setSortPriority(Integer sortPriority) {
    this.sortPriority = sortPriority;
  }

  /**
   * Indication if the insurance provider has been deleted or not.
   *
   * @documentationExample false
   *
   * @return <code>true</code> or <code>false</code>
   */
  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * Indication if the insurer is a regional medical associate.
   *
   * @documentationExample false
   *
   * @return <code>true</code> or <code>false</code>
   */
  public Boolean getRegionalMedicalAssociates() {
    return regionalMedicalAssociates;
  }

  public void setRegionalMedicalAssociates(Boolean isRegionalMedicalAssociates) {
    this.regionalMedicalAssociates = isRegionalMedicalAssociates;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + Objects.hashCode(this.insurerId);
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + Objects.hashCode(this.officePhone);
    hash = 71 * hash + Objects.hashCode(this.faxPhone);
    hash = 71 * hash + Objects.hashCode(this.email);
    hash = 71 * hash + Objects.hashCode(this.address);
    hash = 71 * hash + Objects.hashCode(this.privateInsurer);
    hash = 71 * hash + Objects.hashCode(this.taxExempt);
    hash = 71 * hash + Objects.hashCode(this.externalId);
    hash = 71 * hash + Objects.hashCode(this.zeroBillAmount);
    hash = 71 * hash + Objects.hashCode(this.forAllPatients);
    hash = 71 * hash + Objects.hashCode(this.icbc);
    hash = 71 * hash + Objects.hashCode(this.wcb);
    hash = 71 * hash + Objects.hashCode(this.msp);
    hash = 71 * hash + Objects.hashCode(this.active);
    hash = 71 * hash + Objects.hashCode(this.displayName);
    hash = 71 * hash + Objects.hashCode(this.groupCode);
    hash = 71 * hash + Objects.hashCode(this.priceListId);
    hash = 71 * hash + Objects.hashCode(this.immediatePaymentRequired);
    hash = 71 * hash + Objects.hashCode(this.sortPriority);
    hash = 71 * hash + Objects.hashCode(this.deleted);
    hash = 71 * hash + Objects.hashCode(this.regionalMedicalAssociates);
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
    final InsurerDto other = (InsurerDto) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.externalId, other.externalId)) {
      return false;
    }
    if (!Objects.equals(this.displayName, other.displayName)) {
      return false;
    }
    if (!Objects.equals(this.groupCode, other.groupCode)) {
      return false;
    }
    if (!Objects.equals(this.insurerId, other.insurerId)) {
      return false;
    }
    if (!Objects.equals(this.officePhone, other.officePhone)) {
      return false;
    }
    if (!Objects.equals(this.faxPhone, other.faxPhone)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.privateInsurer, other.privateInsurer)) {
      return false;
    }
    if (!Objects.equals(this.taxExempt, other.taxExempt)) {
      return false;
    }
    if (!Objects.equals(this.zeroBillAmount, other.zeroBillAmount)) {
      return false;
    }
    if (!Objects.equals(this.forAllPatients, other.forAllPatients)) {
      return false;
    }
    if (!Objects.equals(this.icbc, other.icbc)) {
      return false;
    }
    if (!Objects.equals(this.wcb, other.wcb)) {
      return false;
    }
    if (!Objects.equals(this.msp, other.msp)) {
      return false;
    }
    if (!Objects.equals(this.active, other.active)) {
      return false;
    }
    if (!Objects.equals(this.priceListId, other.priceListId)) {
      return false;
    }
    if (!Objects.equals(this.immediatePaymentRequired, other.immediatePaymentRequired)) {
      return false;
    }
    if (!Objects.equals(this.sortPriority, other.sortPriority)) {
      return false;
    }
    if (!Objects.equals(this.deleted, other.deleted)) {
      return false;
    }
    if (!Objects.equals(this.regionalMedicalAssociates, other.regionalMedicalAssociates)) {
      return false;
    }
    return true;
  }

}
