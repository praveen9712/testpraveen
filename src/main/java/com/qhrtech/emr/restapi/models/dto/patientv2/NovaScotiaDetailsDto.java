
package com.qhrtech.emr.restapi.models.dto.patientv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.qhrtech.emr.restapi.validators.RegexValidator;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

/**
 * Nova Scotia specific patient details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Nova Scotia specific patient details.",
    implementation = NovaScotiaDetailsDto.class, name = "NovaScotiaDetailsV2Dto")
public class NovaScotiaDetailsDto {

  @JsonProperty("guardianHcn")
  @Schema(description = "The patients guardian health card number", example = "123-234-123")
  // We are unable to find the usage of this field in Accuro though the field exists in DB
  @Size(max = 9, message = "The guardian Hcn size should not be greater than 9 characters")
  private String guardianHcn;

  @JsonProperty("secondaryHealthCard")
  @Schema(description = "The secondary health card")
  @Valid
  private PersonalHealthCardDto secondaryHealthCard;

  @JsonProperty("lastUpdatedDatetime")
  @Schema(description = "The last updated date time", example = "2000-05-31T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime lastUpdatedDatetime;

  /**
   * The patients guardian Health Card number
   *
   * @documentationExample 123-234-123
   *
   * @return Health care number
   */
  public String getGuardianHcn() {
    return guardianHcn;
  }

  public void setGuardianHcn(String guardianHcn) {
    this.guardianHcn = guardianHcn;
  }

  /**
   * The patients secondary Health Card number.
   *
   * @documentationExample 123-234-123
   *
   * @return Health card number
   */
  public PersonalHealthCardDto getSecondaryHealthCard() {
    return secondaryHealthCard;
  }

  public void setSecondaryHealthCard(PersonalHealthCardDto secondaryHealthCard) {
    this.secondaryHealthCard = secondaryHealthCard;
  }

  /**
   * Last updated patient
   *
   * @return Date of last update
   */
  @DocumentationExample("2000-05-31T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastUpdatedDatetime() {
    return lastUpdatedDatetime;
  }

  public void setLastUpdatedDatetime(LocalDateTime lastUpdatedDatetime) {
    this.lastUpdatedDatetime = lastUpdatedDatetime;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.guardianHcn);
    hash = 97 * hash + Objects.hashCode(this.secondaryHealthCard);
    hash = 97 * hash + Objects.hashCode(this.lastUpdatedDatetime);
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
    final NovaScotiaDetailsDto other = (NovaScotiaDetailsDto) obj;
    if (!Objects.equals(this.guardianHcn, other.guardianHcn)) {
      return false;
    }
    if (!Objects.equals(this.secondaryHealthCard, other.secondaryHealthCard)) {
      return false;
    }
    if (!Objects.equals(this.lastUpdatedDatetime, other.lastUpdatedDatetime)) {
      return false;
    }
    return true;
  }

}
