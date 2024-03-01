
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.RegexValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.Pattern;

/**
 * The Phone data transfer object model.
 *
 * @author jesse.pasos
 * @see com.qhrtech.emr.accuro.model.demographics.Phone
 */

@Schema(description = "Phone object model. Except **number** field, if any other field is null, "
    + "it will not be shown in the response object. The example shown here is the ideal scenario"
    + "where all the fields are not null.",
    implementation = PhoneDto.class, name = "PhoneDto")
public class PhoneDto {

  @JsonProperty("phoneId")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The id of the phone", type = "integer", example = "1")
  private Integer phoneId;

  @JsonProperty("number")
  @Schema(description = "The phone number For e.g (123) 456-7890 or 1234567890",
      example = "(123) 456-7890")
  @Pattern(regexp = RegexValidator.PHONE_PATTERN, message = RegexValidator.PHONE_MESSAGE)
  private String number;

  @JsonProperty("ext")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The phone extension number", type = "string", example = "112")
  private String ext;

  @JsonProperty("equipType")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The equipment type of the phone", type = "string", example = "Cell")
  private String equipType;

  @JsonProperty("usage")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The phones usage", type = "string")
  private String usage;

  @JsonProperty("notes")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Notes for the phone", type = "string",
      example = "Only available between 8am and noon.")
  private String notes;

  @JsonProperty("order")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The order of the phone", type = "integer", example = "1")
  private Integer order;

  @JsonProperty("contactType")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Phone contact type", example = "WorkPhone")
  private ContactType contactType;

  /**
   * Unique phone identifier
   *
   * @return Phone ID
   * @documentationExample 1
   */
  public Integer getPhoneId() {
    return phoneId;
  }

  public void setPhoneId(Integer phoneId) {
    this.phoneId = phoneId;
  }

  /**
   * 10 digit valid phone number i.e. (123) 456-7890 or 1234567890
   *
   * @return Phone number
   * @documentationExample (123) 456-7890
   */
  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Extension for the Phone
   *
   * @return Phone extension
   * @documentationExample 112
   */
  public String getExt() {
    return ext;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  /**
   * The equipment type for the phone.
   *
   * @return The phones equipment type
   * @documentationExample Cell
   */
  public String getEquipType() {
    return equipType;
  }

  public void setEquipType(String equipType) {
    this.equipType = equipType;
  }

  /**
   * The phones usage.
   *
   * @return the phones usage.
   * @documentationExample Active
   */
  public String getUsage() {
    return usage;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }

  /**
   * Any notes about the phone.
   *
   * @return A note
   * @documentationExample Only available between 8am and noon.
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The order of the phone number
   *
   * @return An int value
   * @documentationExample 2
   */
  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  /**
   * Phone contact type.
   *
   * @return A <code>ContactType</code> value
   * @documentationExample Business
   */
  public ContactType getContactType() {
    return contactType;
  }

  public void setContactType(ContactType contactType) {
    this.contactType = contactType;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.phoneId);
    hash = 97 * hash + Objects.hashCode(this.number);
    hash = 97 * hash + Objects.hashCode(this.ext);
    hash = 97 * hash + Objects.hashCode(this.equipType);
    hash = 97 * hash + Objects.hashCode(this.usage);
    hash = 97 * hash + Objects.hashCode(this.notes);
    hash = 97 * hash + Objects.hashCode(this.order);
    hash = 97 * hash + Objects.hashCode(this.contactType);
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
    final PhoneDto other = (PhoneDto) obj;
    if (!Objects.equals(this.number, other.number)) {
      return false;
    }
    if (!Objects.equals(this.ext, other.ext)) {
      return false;
    }
    if (!Objects.equals(this.equipType, other.equipType)) {
      return false;
    }
    if (!Objects.equals(this.usage, other.usage)) {
      return false;
    }
    if (!Objects.equals(this.notes, other.notes)) {
      return false;
    }
    if (!Objects.equals(this.phoneId, other.phoneId)) {
      return false;
    }
    if (!Objects.equals(this.order, other.order)) {
      return false;
    }
    if (this.contactType != other.contactType) {
      return false;
    }
    return true;
  }

}
