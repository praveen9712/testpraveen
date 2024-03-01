
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Immunization Schedules store meta data associated with groups of scheduled vaccines.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "ImmunizationSchedule stores meta data associated with groups "
    + "of scheduled vaccines.")
public class ImmunizationScheduleDto {

  @JsonProperty("id")
  @Schema(description = "ID of the immunization schedule", example = "1")
  private int id;

  @JsonProperty("name")
  @Schema(description = "Name of the immunization schedule",
      example = "Routine Schedule for Infants and Children")
  private String name;

  @JsonProperty("forAllPatients")
  @Schema(description = "Flag which indicates if the immunization schedule is for all patients",
      example = "true")
  private boolean forAll;

  /**
   * The Id of this immunization schedule.
   *
   * @documentationExample 3
   *
   * @return Immunization Schedule ID
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The name of this immunization schedule.
   *
   * @documentationExample Routine Schedule for Infants and Children
   *
   * @return Immunization Schedule Name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Indicates if the Immunization Schedule is for all patients.
   * <p>
   * Immunization Schedules with this flag set to true will be applied to all patients by default.
   *
   * @documentationExample false
   *
   * @return <code>true</code> the Immunization Schedule is for all patients otherwise
   *         <code>false</code>.
   */
  public boolean isForAll() {
    return forAll;
  }

  public void setForAll(boolean forAll) {
    this.forAll = forAll;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.id;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + (this.forAll ? 1 : 0);
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
    final ImmunizationScheduleDto other = (ImmunizationScheduleDto) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.forAll != other.forAll) {
      return false;
    }
    return Objects.equals(this.name, other.name);
  }

}
