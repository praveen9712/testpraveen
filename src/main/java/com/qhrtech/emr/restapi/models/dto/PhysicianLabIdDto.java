
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * The PhysicianLabId model
 * </p>
 *
 * @author praveen.ktheegala
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "physician lab id's transfer object model")
public class PhysicianLabIdDto {

  @JsonProperty("physicianId")
  @Schema(description = "The provider id", example = "42")
  private int physicianId;

  @JsonProperty("sourceId")
  @Schema(description = "Id of the lab source", example = "5")
  private Integer sourceId;

  @JsonProperty("exactMatch")
  @Schema(
      description = "Indication of exact match.",
      example = "true")
  private boolean exactMatch;

  @JsonProperty("labId")
  @Schema(description = "The lab id",
      example = "labId")
  private String labId;

  public int getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(int physicianId) {
    this.physicianId = physicianId;
  }

  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  public boolean isExactMatch() {
    return exactMatch;
  }

  public void setExactMatch(boolean exactMatch) {
    this.exactMatch = exactMatch;
  }

  public String getLabId() {
    return labId;
  }

  public void setLabId(String labId) {
    this.labId = labId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PhysicianLabIdDto that = (PhysicianLabIdDto) o;

    if (physicianId != that.physicianId) {
      return false;
    }
    if (exactMatch != that.exactMatch) {
      return false;
    }
    if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) {
      return false;
    }
    return labId != null ? labId.equals(that.labId) : that.labId == null;
  }

  @Override
  public int hashCode() {
    int result = physicianId;
    result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
    result = 31 * result + (exactMatch ? 1 : 0);
    result = 31 * result + (labId != null ? labId.hashCode() : 0);
    return result;
  }
}
