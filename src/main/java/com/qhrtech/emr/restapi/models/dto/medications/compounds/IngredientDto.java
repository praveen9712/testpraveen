
package com.qhrtech.emr.restapi.models.dto.medications.compounds;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The ingredient transfer model.
 */
@Schema(description = "The ingredient transfer model")
public class IngredientDto {

  @JsonProperty("drugId")
  @Schema(description = "The ingredient id", example = "1")
  private String drugId;

  @JsonProperty("drugType")
  @Schema(description = "The ingredient type", example = "SAMPLE")
  private String drugType;

  @JsonProperty("drugName")
  @Schema(description = "The ingredient name", example = "GRAPE")
  private String drugName;

  @JsonProperty("componentAmount")
  @Schema(description = "The component amount", example = "1")
  private int componentAmount;

  @JsonProperty("componentUnit")
  @Schema(description = "The component unit", example = "mg")
  private String componentUnit;

  @JsonProperty("componentNote")
  @Schema(description = "The component note", example = "THIS IS THE SAMPLE COMPONENT")
  private String componentNote;

  @JsonProperty("drugIncluded")
  @Schema(description = "The flag if the ingredient is included", example = "true")
  private boolean drugIncluded;

  /**
   * A ingredient id.
   *
   * @documentationExample U5IY
   *
   * @return
   */
  public String getDrugId() {
    return drugId;
  }

  public void setDrugId(String drugId) {
    this.drugId = drugId;
  }

  // This field is always null currently.
  /**
   * A ingredient type.
   *
   * @documentationExample SAMPLE
   *
   * @return
   */
  public String getDrugType() {
    return drugType;
  }

  public void setDrugType(String drugType) {
    this.drugType = drugType;
  }

  /**
   * A ingredient name.
   *
   * @documentationExample GRAPE
   *
   * @return
   */
  public String getDrugName() {
    return drugName;
  }

  public void setDrugName(String drugName) {
    this.drugName = drugName;
  }

  /**
   * A component amount.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getComponentAmount() {
    return componentAmount;
  }

  public void setComponentAmount(int componentAmount) {
    this.componentAmount = componentAmount;
  }

  /**
   * A component unit.
   *
   * @documentationExample mg
   *
   * @return
   */
  public String getComponentUnit() {
    return componentUnit;
  }

  public void setComponentUnit(String componentUnit) {
    this.componentUnit = componentUnit;
  }

  /**
   * A component note.
   *
   * @documentationExample THIS IS A SAMPLE COMPONENT
   *
   * @return
   */
  public String getComponentNote() {
    return componentNote;
  }

  public void setComponentNote(String componentNote) {
    this.componentNote = componentNote;
  }

  /**
   * A flag if an ingredient is included.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isDrugIncluded() {
    return drugIncluded;
  }

  public void setDrugIncluded(boolean drugIncluded) {
    this.drugIncluded = drugIncluded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IngredientDto)) {
      return false;
    }

    IngredientDto that = (IngredientDto) o;

    if (getComponentAmount() != that.getComponentAmount()) {
      return false;
    }
    if (isDrugIncluded() != that.isDrugIncluded()) {
      return false;
    }
    if (getDrugId() != null ? !getDrugId().equals(that.getDrugId()) : that.getDrugId() != null) {
      return false;
    }
    if (getDrugType() != null ? !getDrugType().equals(that.getDrugType())
        : that.getDrugType() != null) {
      return false;
    }
    if (getDrugName() != null ? !getDrugName().equals(that.getDrugName())
        : that.getDrugName() != null) {
      return false;
    }
    if (getComponentUnit() != null ? !getComponentUnit().equals(that.getComponentUnit())
        : that.getComponentUnit() != null) {
      return false;
    }
    return getComponentNote() != null ? getComponentNote().equals(that.getComponentNote())
        : that.getComponentNote() == null;
  }

  @Override
  public int hashCode() {
    int result = getDrugId() != null ? getDrugId().hashCode() : 0;
    result = 31 * result + (getDrugType() != null ? getDrugType().hashCode() : 0);
    result = 31 * result + (getDrugName() != null ? getDrugName().hashCode() : 0);
    result = 31 * result + getComponentAmount();
    result = 31 * result + (getComponentUnit() != null ? getComponentUnit().hashCode() : 0);
    result = 31 * result + (getComponentNote() != null ? getComponentNote().hashCode() : 0);
    result = 31 * result + (isDrugIncluded() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "IngredientDto{"
        + "drugId='" + drugId + '\''
        + ", drugType='" + drugType + '\''
        + ", drugName='" + drugName + '\''
        + ", componentAmount=" + componentAmount
        + ", componentUnit='" + componentUnit + '\''
        + ", componentNote='" + componentNote + '\''
        + ", drugIncluded=" + drugIncluded
        + '}';
  }
}
