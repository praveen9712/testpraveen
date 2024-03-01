
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EprescribeJobTypeDto {

  @JsonProperty("ePrescribeJobTypeId")
  @Schema(description = "The eprescribe job type id", example = "1")
  private int eprescribeJobTypeId;

  @NotNull
  @Size(max = 100)
  @JsonProperty("jobTypeSystem")
  @Schema(description = "The eprescribe job type system", example = "system")
  private String jobTypeSystem;

  @JsonProperty("description")
  @Schema(description = "The description", example = "details")
  private String description;

  @NotNull
  @Size(max = 100)
  @JsonProperty("jobSubtype")
  @Schema(description = "The sub job type", example = "subtype1")
  private String jobSubtype;

  /**
   * The e-prescribe job type id
   * 
   * @documentationExample 1
   * @return The e-prescribe job type id
   */
  public int getEprescribeJobTypeId() {
    return eprescribeJobTypeId;
  }

  public void setEprescribeJobTypeId(int eprescribeJobTypeId) {
    this.eprescribeJobTypeId = eprescribeJobTypeId;
  }

  /**
   * The job type system
   * 
   * @documentationExample system
   * @return THe job type system
   */
  public String getJobTypeSystem() {
    return jobTypeSystem;
  }

  public void setJobTypeSystem(String jobTypeSystem) {
    this.jobTypeSystem = jobTypeSystem;
  }

  /**
   * The description
   * 
   * @documentationExample details
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The job subtype
   * 
   * @documentationExample subtype1
   * @return The job subtype
   */
  public String getJobSubtype() {
    return jobSubtype;
  }

  public void setJobSubtype(String jobSubtype) {
    this.jobSubtype = jobSubtype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EprescribeJobTypeDto that = (EprescribeJobTypeDto) o;
    return eprescribeJobTypeId == that.eprescribeJobTypeId
        && Objects.equals(jobTypeSystem, that.jobTypeSystem)
        && Objects.equals(description, that.description)
        && Objects.equals(jobSubtype, that.jobSubtype);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eprescribeJobTypeId, jobTypeSystem, description, jobSubtype);
  }

  @Override
  public String toString() {
    return "EprescribeJobTypeDto{"
        + "ePrescribeJobTypeId=" + eprescribeJobTypeId
        + ", jobTypeSystem='" + jobTypeSystem + '\''
        + ", description='" + description + '\''
        + ", jobSubtype='" + jobSubtype + '\''
        + '}';
  }
}
