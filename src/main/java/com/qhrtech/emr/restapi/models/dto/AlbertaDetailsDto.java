
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Alberta specific patient details.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Alberta specific patient details.",
    implementation = AlbertaDetailsDto.class, name = "AlbertaDetailsDto")
public class AlbertaDetailsDto {

  @JsonProperty("type")
  @Schema(
      description = "The person type. Person type codes are as follows:"
          + "<ul>\n"
          + "<li>PYST - Payee</li>\n"
          + "<li>RECP - Service Recipient</li>\n"
          + "<li>RFRC - Out of Province Referring Service Provider</li>\n"
          + "</ul>",
      example = "PYST", allowableValues = {"PYST", "RECP", "RFRC"}, type = "string", maxLength = 4)
  private String type;

  @JsonProperty("newBornCode")
  @Schema(description = "New born code. Now born codes are as follows:\n"
      + "<ul>\n"
      + "<li>ADOP - Adoption</li>\n"
      + "<li>LVBR - Live Birth</li>\n"
      + "<li>MULT - Multiple Birth</li>\n"
      + "<li>STBN - Still Born</li>\n"
      + "</ul>",
      example = "ADOP",
      allowableValues = {"ADOP", "LVBR", "MULT", "STBN"}, type = "string", maxLength = 4)
  private String newBornCode;

  @JsonProperty("guardianUli")
  @Schema(description = "Guardian ULI (PHN)", example = "234-123-564", type = "string",
      maxLength = 9)
  private String guardianUli;

  @JsonProperty("guardianRegistration")
  @Schema(description = "Guardian Registration", example = "123", type = "string",
      maxLength = 12)
  private String guardianRegistration;

  @JsonProperty("uli2")
  @Schema(description = "Secondary ULI (PHN)")
  private PersonalHealthCardDto uli2;

  /**
   * The person type. Person type codes are as follows:
   *
   * <ul>
   * <li>PYST - Payee</li>
   * <li>RECP - Service Recipient</li>
   * <li>RFRC - Out of Province Referring Service Provider</li>
   * </ul>
   *
   * @return Person type
   * @documentationExample RECP
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * New born code. Now born codes are as follows:
   *
   * <ul>
   * <li>ADOP - Adoption</li>
   * <li>LVBR - Live Birth</li>
   * <li>MULT - Multiple Birth</li>
   * <li>STBN - Still Born</li>
   * </ul>
   *
   * @return A new born code
   * @documentationExample ADOP
   */
  public String getNewBornCode() {
    return newBornCode;
  }

  public void setNewBornCode(String newBornCode) {
    this.newBornCode = newBornCode;
  }

  /**
   * Guardian ULI (PHN)
   *
   * @return Personal Health Card
   * @documentationExample 234-123-564
   */
  public String getGuardianUli() {
    return guardianUli;
  }

  public void setGuardianUli(String guardianUli) {
    this.guardianUli = guardianUli;
  }

  /**
   * Guardian Registration
   *
   * @return Guardian registration
   * @documentationExample 123
   */
  public String getGuardianRegistration() {
    return guardianRegistration;
  }

  public void setGuardianRegistration(String guardianRegistration) {
    this.guardianRegistration = guardianRegistration;
  }

  /**
   * Secondary ULI (PHN)
   *
   * @return Personal Health Card
   * @documentationExample 234-123-564
   */
  public PersonalHealthCardDto getUli2() {
    return uli2;
  }

  public void setUli2(PersonalHealthCardDto uli2) {
    this.uli2 = uli2;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.type);
    hash = 13 * hash + Objects.hashCode(this.newBornCode);
    hash = 13 * hash + Objects.hashCode(this.guardianUli);
    hash = 13 * hash + Objects.hashCode(this.guardianRegistration);
    hash = 13 * hash + Objects.hashCode(this.uli2);
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
    final AlbertaDetailsDto other = (AlbertaDetailsDto) obj;
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.newBornCode, other.newBornCode)) {
      return false;
    }
    if (!Objects.equals(this.guardianUli, other.guardianUli)) {
      return false;
    }
    if (!Objects.equals(this.guardianRegistration, other.guardianRegistration)) {
      return false;
    }
    if (!Objects.equals(this.uli2, other.uli2)) {
      return false;
    }
    return true;
  }

}
