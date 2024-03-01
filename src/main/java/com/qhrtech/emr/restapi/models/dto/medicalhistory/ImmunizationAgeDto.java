
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents an age at which an immunization is scheduled to be given.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "ImmunizationAge data transfer object model representing the age "
    + "at which the immunization is scheduled to be given")
public class ImmunizationAgeDto {

  @JsonProperty("id")
  @Schema(description = "ID of this Immunization Age", example = "+00106")
  private String id;

  @JsonProperty("years")
  @Schema(description = "The number of years the age represents", example = "6")
  private int years;

  @JsonProperty("months")
  @Schema(description = "The number of months the age represents", example = "10")
  private int months;

  @JsonProperty("customName")
  @Schema(description = "Custom display name for use in Accuro", example = "One Year")
  private String customName;

  @JsonProperty("fromScheduledDate")
  @Schema(description = "Whether the age represents the time from the date of birth, "
      + "or from the scheduled vaccine date",
      example = "true")
  private boolean fromScheduledDate;

  /**
   * The internal id for the Immunization Age
   *
   * @documentationExample +00106
   *
   * @return
   */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * The number of years the age represents.
   *
   * @documentationExample 1
   *
   * @return The number of years.
   */
  public int getYears() {
    return years;
  }

  public void setYears(int years) {
    this.years = years;
  }

  /**
   * The number of months the age represents.
   *
   * @documentationExample 6
   *
   * @return The number of months.
   */
  public int getMonths() {
    return months;
  }

  public void setMonths(int months) {
    this.months = months;
  }

  /**
   * A custom display name for use in Accuro.
   *
   * @documentationExample One Year, Six Months
   *
   * @return The custom display name.
   */
  public String getCustomName() {
    return customName;
  }

  public void setCustomName(String customName) {
    this.customName = customName;
  }

  /**
   * Whether the immunization age represents the time from date of birth, or scheduled vaccine date.
   *
   * @documentationExample true
   *
   * @return If the age represents time from birth, true, false otherwise
   */
  public boolean isFromScheduledDate() {
    return fromScheduledDate;
  }

  public void setFromScheduledDate(boolean fromScheduledDate) {
    this.fromScheduledDate = fromScheduledDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ImmunizationAgeDto that = (ImmunizationAgeDto) o;

    if (years != that.years) {
      return false;
    }
    if (months != that.months) {
      return false;
    }
    if (fromScheduledDate != that.fromScheduledDate) {
      return false;
    }
    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    return customName != null ? customName.equals(that.customName) : that.customName == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + years;
    result = 31 * result + months;
    result = 31 * result + (customName != null ? customName.hashCode() : 0);
    result = 31 * result + (fromScheduledDate ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ImmunizationAgeDto{");
    sb.append("id='").append(id).append('\'');
    sb.append(", years='").append(years).append('\'');
    sb.append(", months='").append(months).append('\'');
    sb.append(", customName='").append(customName).append('\'');
    sb.append(", fromScheduledDate=").append(fromScheduledDate);
    sb.append('}');
    return sb.toString();
  }
}
