
package com.qhrtech.emr.restapi.models.dto.medications.compounds;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * The custom compound transfer model.
 */
@Schema(description = "The custom compound transfer model")
public class CustomCompoundDto {

  @JsonProperty("name")
  @Schema(description = "The custom compound name", example = "CUSTOM COMPOUND")
  private String name;

  @JsonProperty("description")
  @Schema(description = "The description of the compound", example = "THIS IS A CUSTOM COMPOUND")
  private String description;

  @JsonProperty("includedIngredients")
  @Schema(description = "A Set of ingredients included in the compound")
  private Set<IngredientDto> includedIngredients;

  @JsonProperty("excludedIngredients")
  @Schema(description = "A Set of ingredients excluded in the compound")
  private Set<IngredientDto> excludedIngredients;

  /**
   * A custom compound name.
   *
   * @documentationExample CUSTOM COMPOUND
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
   * A description of a compound.
   *
   * @documentationExample THIS IS A CUSTOM COMPOUND
   *
   * @return
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * A Set of ingredients included in a compound.
   *
   * @return
   */
  public Set<IngredientDto> getIncludedIngredients() {
    return includedIngredients;
  }

  public void setIncludedIngredients(
      Set<IngredientDto> includedIngredients) {
    this.includedIngredients = includedIngredients;
  }

  /**
   * A Set of ingredients excluded in a compound.
   * 
   * @return
   */
  public Set<IngredientDto> getExcludedIngredients() {
    return excludedIngredients;
  }

  public void setExcludedIngredients(
      Set<IngredientDto> excludedIngredients) {
    this.excludedIngredients = excludedIngredients;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomCompoundDto)) {
      return false;
    }

    CustomCompoundDto that = (CustomCompoundDto) o;

    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getDescription() != null ? !getDescription().equals(that.getDescription())
        : that.getDescription() != null) {
      return false;
    }
    if (getIncludedIngredients() != null ? !getIncludedIngredients()
        .equals(that.getIncludedIngredients()) : that.getIncludedIngredients() != null) {
      return false;
    }
    return getExcludedIngredients() != null ? getExcludedIngredients()
        .equals(that.getExcludedIngredients()) : that.getExcludedIngredients() == null;
  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result =
        31 * result + (getIncludedIngredients() != null ? getIncludedIngredients().hashCode() : 0);
    result =
        31 * result + (getExcludedIngredients() != null ? getExcludedIngredients().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CustomCompoundDto{"
        + "name='" + name + '\''
        + ", description='" + description + '\''
        + ", includedIngredients=" + includedIngredients
        + ", excludedIngredients=" + excludedIngredients
        + '}';
  }
}
