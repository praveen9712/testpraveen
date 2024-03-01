
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The schedule vaccine data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.immunization.ScheduleVaccine
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Schedule vaccine data transfer object model")
public class ScheduleVaccineDto {

  @JsonProperty("immunizationScheduleId")
  @Schema(description = "ID of this immunization schedule", example = "1")
  private int immunizationScheduleId;

  @JsonProperty("immunizationAge")
  @Schema(description = "Immunization age scheduled.", example = "5")
  private ImmunizationAgeDto immunizationAge;

  @JsonProperty("vaccine")
  @Schema(description = "The vaccine")
  private VaccineDto vaccine;

  @JsonProperty("optional")
  @Schema(description = "Flag indicating if the scheduled vaccine is optional", example = "false")
  private boolean optional;

  /**
   * A unique id of an immunization schedule.
   *
   * @documentationExample 8
   *
   * @return The immunization schedule id.
   */
  public int getImmunizationScheduleId() {
    return immunizationScheduleId;
  }

  public void setImmunizationScheduleId(int immunizationScheduleId) {
    this.immunizationScheduleId = immunizationScheduleId;
  }

  /**
   * A vaccine.
   *
   * @return The vaccine.
   */
  public VaccineDto getVaccine() {
    return vaccine;
  }

  public void setVaccine(VaccineDto vaccine) {
    this.vaccine = vaccine;
  }

  /**
   * An immunization age scheduled.
   *
   * @return The immunization age
   */
  public ImmunizationAgeDto getImmunizationAge() {
    return immunizationAge;
  }

  public void setImmunizationAge(ImmunizationAgeDto immunizationAge) {
    this.immunizationAge = immunizationAge;
  }

  /**
   * Indicates if the scheduled vaccine is optional.
   *
   * @documentationExample true
   *
   * @return {@code true} if the scheduled vaccine is optional, otherwise {@code false}.
   */
  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
    this.optional = optional;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScheduleVaccineDto)) {
      return false;
    }

    ScheduleVaccineDto that = (ScheduleVaccineDto) o;

    if (getImmunizationScheduleId() != that.getImmunizationScheduleId()) {
      return false;
    }
    if (isOptional() != that.isOptional()) {
      return false;
    }
    if (getImmunizationAge() != null ? !getImmunizationAge().equals(that.getImmunizationAge())
        : that.getImmunizationAge() != null) {
      return false;
    }
    return getVaccine() != null ? getVaccine().equals(that.getVaccine())
        : that.getVaccine() == null;
  }

  @Override
  public int hashCode() {
    int result = getImmunizationScheduleId();
    result = 31 * result + (getImmunizationAge() != null ? getImmunizationAge().hashCode() : 0);
    result = 31 * result + (getVaccine() != null ? getVaccine().hashCode() : 0);
    result = 31 * result + (isOptional() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ScheduleVaccineDto{");
    sb.append("immunizationScheduleId=").append(immunizationScheduleId);
    sb.append(", immunizationAge=").append(immunizationAge);
    sb.append(", vaccine=").append(vaccine);
    sb.append(", optional=").append(optional);
    sb.append('}');
    return sb.toString();
  }
}
