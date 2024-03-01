
package com.qhrtech.emr.restapi.models.dto.medications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The generic drug transfer model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The generic drug transfer model")
public class GenericDrugDto {

  @JsonProperty("genericId")
  @Schema(description = "The unique generic drug id", example = "1")
  private Integer genericId;

  @JsonProperty("name")
  @Schema(description = "The name of a generic drug", example = "caffeine/dextrose")
  private String name;

  @JsonProperty("ingredientCount")
  @Schema(description = "The count of ingredients in the generic drug", example = "1")
  private String ingredientCount;

  @JsonProperty("activeIngredientCode")
  @Schema(description = "The number of the active ingredient as known as AIG",
      example = "0000000001")
  private String activeIngredientCode;

  @JsonProperty("triplicate")
  @Schema(description = "The flag if a generic drug is triplicate", example = "true")
  private boolean triplicate;

  @JsonProperty("asDirected")
  @Schema(description = "The flag if a generic drug is as directed", example = "true")
  private boolean asDirected;

  /**
   * A unique generic drug id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getGenericId() {
    return genericId;
  }

  public void setGenericId(Integer genericId) {
    this.genericId = genericId;
  }

  /**
   * A name of a generic drug.
   *
   * @documentationExample caffeine/dextrose
   *
   * @return
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * A count of ingredients in a generic drug.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getIngredientCount() {
    return ingredientCount;
  }

  public void setIngredientCount(String ingredientCount) {
    this.ingredientCount = ingredientCount;
  }

  /**
   * The number of an active ingredient as known as AIG.
   *
   * @documentationExample 0000000001
   *
   * @return
   */
  public String getActiveIngredientCode() {
    return activeIngredientCode;
  }

  public void setActiveIngredientCode(String activeIngredientCode) {
    this.activeIngredientCode = activeIngredientCode;
  }

  /**
   * A flag if a generic drug is triplicate.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isTriplicate() {
    return triplicate;
  }

  public void setTriplicate(boolean triplicate) {
    this.triplicate = triplicate;
  }

  /**
   * A flag if a generic drug is as directed.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isAsDirected() {
    return asDirected;
  }

  public void setAsDirected(boolean asDirected) {
    this.asDirected = asDirected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GenericDrugDto)) {
      return false;
    }

    GenericDrugDto that = (GenericDrugDto) o;

    if (isTriplicate() != that.isTriplicate()) {
      return false;
    }
    if (isAsDirected() != that.isAsDirected()) {
      return false;
    }
    if (getGenericId() != null ? !getGenericId().equals(that.getGenericId())
        : that.getGenericId() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getIngredientCount() != null ? !getIngredientCount().equals(that.getIngredientCount())
        : that.getIngredientCount() != null) {
      return false;
    }
    return getActiveIngredientCode() != null ? getActiveIngredientCode()
        .equals(that.getActiveIngredientCode()) : that.getActiveIngredientCode() == null;
  }

  @Override
  public int hashCode() {
    int result = getGenericId() != null ? getGenericId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getIngredientCount() != null ? getIngredientCount().hashCode() : 0);
    result =
        31 * result + (getActiveIngredientCode() != null ? getActiveIngredientCode().hashCode()
            : 0);
    result = 31 * result + (isTriplicate() ? 1 : 0);
    result = 31 * result + (isAsDirected() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "GenericDrugDto{"
        + "genericId=" + genericId
        + ", name='" + name + '\''
        + ", ingredientCount='" + ingredientCount + '\''
        + ", activeIngredientCode='" + activeIngredientCode + '\''
        + ", triplicate=" + triplicate
        + ", asDirected=" + asDirected
        + '}';
  }
}
