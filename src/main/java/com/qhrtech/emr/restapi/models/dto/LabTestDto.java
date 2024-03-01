
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * <p>
 * The lab test model.
 * </p>
 * <p>
 * Lab tests represent a template from which individual lab groups can be created.
 * </p>
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab test data transfer object model")
public class LabTestDto {

  @JsonProperty("testId")
  @Schema(description = "Unique identifier for the lab test", example = "42")
  private int testId;

  @JsonProperty("testName")
  @Schema(description = "Name of the lab test", example = "CHOL Panel")
  private String testName;

  @JsonProperty("sourceId")
  @Schema(description = "Id of the lab source for this lab test", example = "5")
  private Integer sourceId;

  @JsonProperty("active")
  @Schema(description = "The active state of this lab test", example = "true")
  private boolean active;

  /**
   * Unique Identifier for the Lab Test.
   *
   * @documentationExample 42
   *
   * @return Lab test ID
   */
  public int getTestId() {
    return testId;
  }

  public void setTestId(int testId) {
    this.testId = testId;
  }

  /**
   * Name of the Lab Test.
   *
   * @documentationExample CHOL Panel
   *
   * @return Lab test name
   */
  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  /**
   * Id of the Lab Source for this Lab Test.
   *
   * @documentationExample 5
   *
   * @return Lab test source ID
   */
  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * The Active state of this Lab Test.
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
    hash = 53 * hash + this.testId;
    hash = 53 * hash + Objects.hashCode(this.testName);
    hash = 53 * hash + Objects.hashCode(this.sourceId);
    hash = 53 * hash + (this.active ? 1 : 0);
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
    final LabTestDto other = (LabTestDto) obj;
    if (this.testId != other.testId) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    if (!Objects.equals(this.testName, other.testName)) {
      return false;
    }
    if (!Objects.equals(this.sourceId, other.sourceId)) {
      return false;
    }
    return true;
  }

}
