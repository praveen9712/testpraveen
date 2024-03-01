
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * <p>
 * A Lab Result model.
 * </p>
 * <p>
 * A Lab Result represents a template from which a lab Observation can be created.
 * </p>
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab result data transfer object model")
public class LabResultDto {

  @JsonProperty("resultId")
  @Schema(description = "Unique identifier for this lab result", example = "42")
  private int resultId;

  @JsonProperty("resultName")
  @Schema(description = "User defined name for this lab result", example = "BP - Diastolic")
  private String resultName;

  @JsonProperty("dataType")
  @Schema(
      description = "Data type for this lab result",
      example = "Numeric")
  private LabDataType dataType;

  @JsonProperty("units")
  @Schema(description = "Unit used for the lab result", example = "mmHG")
  private String units;

  @JsonProperty("sourceId")
  @Schema(description = "Identifier of the external lab source for this lab result", example = "5")
  private Integer sourceId;

  @JsonProperty("imperial")
  @Schema(description = "Identifies whether this lab result uses imperial measurements",
      example = "false")
  private boolean imperial;

  @JsonProperty("description")
  @Schema(description = "User defined description for the lab result",
      example = "Arterial pressure between heart beats")
  private String description;

  @JsonProperty("referenceRange")
  @Schema(description = "Reference range for the lab result", example = "< 80")
  private String referenceRange;

  @JsonProperty("active")
  @Schema(description = "Active state for this lab result", example = "true")
  private boolean active;

  /**
   * Unique identifier for this Lab Result.
   *
   * @documentationExample 42
   *
   * @return Lab result ID
   */
  public int getResultId() {
    return resultId;
  }

  public void setResultId(int resultId) {
    this.resultId = resultId;
  }

  /**
   * User defined name for this Lab Result.
   *
   * @documentationExample BP - Diastolic
   *
   * @return Lab result name
   */
  public String getResultName() {
    return resultName;
  }

  public void setResultName(String resultName) {
    this.resultName = resultName;
  }

  /**
   * Data Type for this Lab Result. Valid types are: Numeric, Text, Unidentified
   *
   * @documentationExample Numeric
   *
   * @return Lab result data type
   */
  public LabDataType getDataType() {
    return dataType;
  }

  public void setDataType(LabDataType dataType) {
    this.dataType = dataType;
  }

  /**
   * Unit used for the Lab Result.
   *
   * @documentationExample mmHG
   *
   * @return Lab result unit
   */
  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  /**
   * Identifier of the external Lab Source for this Lab Result.
   *
   * @documentationExample 5
   *
   * @return Source ID for the lab result
   */
  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Identifies whether this Lab Result uses Imperial measurements.
   *
   * @documentationExample false
   *
   * @return <code>true</code> if imperial, or <code>false</code> if metric.
   */
  public boolean isImperial() {
    return imperial;
  }

  public void setImperial(boolean imperial) {
    this.imperial = imperial;
  }

  /**
   * User defined description for the Lab Result.
   *
   * @documentationExample Arterial pressure between heart beats.
   *
   * @return Lab result description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Reference range for the Lab Result.
   *
   * @documentationExample < 80
   *
   * @return Lab result reference range
   */
  public String getReferenceRange() {
    return referenceRange;
  }

  public void setReferenceRange(String referenceRange) {
    this.referenceRange = referenceRange;
  }

  /**
   * Active state for this Lab Result.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if active, or <code>false</code> if inactive.
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + this.resultId;
    hash = 13 * hash + Objects.hashCode(this.resultName);
    hash = 13 * hash + Objects.hashCode(this.dataType);
    hash = 13 * hash + Objects.hashCode(this.units);
    hash = 13 * hash + Objects.hashCode(this.sourceId);
    hash = 13 * hash + (this.imperial ? 1 : 0);
    hash = 13 * hash + Objects.hashCode(this.description);
    hash = 13 * hash + Objects.hashCode(this.referenceRange);
    hash = 13 * hash + (this.active ? 1 : 0);
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
    final LabResultDto other = (LabResultDto) obj;
    if (this.resultId != other.resultId) {
      return false;
    }
    if (this.imperial != other.imperial) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    if (!Objects.equals(this.resultName, other.resultName)) {
      return false;
    }
    if (!Objects.equals(this.units, other.units)) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    if (!Objects.equals(this.referenceRange, other.referenceRange)) {
      return false;
    }
    if (this.dataType != other.dataType) {
      return false;
    }
    if (!Objects.equals(this.sourceId, other.sourceId)) {
      return false;
    }
    return true;
  }

}
