
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Appointment site model object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The appointment site transfer model object.")
public class SiteDto {

  @JsonProperty("siteId")
  @Schema(description = "The site id", example = "1")
  private Integer siteId;

  @JsonProperty("name")
  @Schema(description = "The appointment site name", example = "Examination Room 1")
  private String name;

  @JsonProperty("abbreviation")
  @Schema(description = "The appointment site abbreviation", example = "EX1")
  private String abbreviation;

  @JsonProperty("shortName")
  @Schema(description = "The appointment site short name", example = "Exam 1")
  private String shortName;

  @JsonProperty("siteOfficeId")
  @Schema(description = "The office id for the appointment site", example = "1")
  private Integer siteOfficeId;

  @JsonProperty("active")
  @Schema(description = "Indication of the appointment site is active or not", example = "true")
  private Boolean active;

  /**
   * The Site ID
   *
   * @documentationExample 1
   *
   * @return Site ID
   */
  public Integer getSiteId() {
    return siteId;
  }

  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }

  /**
   * The appointment site name
   *
   * @documentationExample Examination Room 1
   *
   * @return Site name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The appointment sites abbreviation.
   *
   * @documentationExample EX1
   *
   * @return Abbreviation
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * The appointment sites short name
   *
   * @documentationExample Exam 1
   *
   * @return Short name
   */
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * The office ID for the appointment site
   *
   * @documentationExample 1
   *
   * @return Office ID
   */
  public Integer getSiteOfficeId() {
    return siteOfficeId;
  }

  public void setSiteOfficeId(Integer siteOfficeId) {
    this.siteOfficeId = siteOfficeId;
  }

  /**
   * Indication of the appointment site is active or not.
   *
   * @documentationExample true
   *
   * @return true if active, or else false
   */
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.siteId);
    hash = 17 * hash + Objects.hashCode(this.name);
    hash = 17 * hash + Objects.hashCode(this.abbreviation);
    hash = 17 * hash + Objects.hashCode(this.shortName);
    hash = 17 * hash + Objects.hashCode(this.siteOfficeId);
    hash = 17 * hash + Objects.hashCode(this.active);
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
    final SiteDto other = (SiteDto) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.abbreviation, other.abbreviation)) {
      return false;
    }
    if (!Objects.equals(this.shortName, other.shortName)) {
      return false;
    }
    if (!Objects.equals(this.siteId, other.siteId)) {
      return false;
    }
    if (!Objects.equals(this.siteOfficeId, other.siteOfficeId)) {
      return false;
    }
    if (!Objects.equals(this.active, other.active)) {
      return false;
    }
    return true;
  }

}
