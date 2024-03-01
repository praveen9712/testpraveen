
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;

/**
 * The custom compound transfer model. This model uses IngredientSummaryDto which has less fields as
 * compared to IngredientDto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The custom compound transfer model")
public class CustomCompoundSummaryDto {

  @JsonProperty("name")
  @Schema(description = "The custom compound name", example = "CUSTOM COMPOUND")
  private String name;

  @JsonProperty("description")
  @Schema(description = "The description of the compound", example = "THIS IS A CUSTOM COMPOUND")
  private String description;

  @JsonProperty("includedIngredients")
  @Schema(description = "A Set of ingredients included in the compound")
  private Set<IngredientSummaryDto> includedIngredients;

  @JsonProperty("excludedIngredients")
  @Schema(description = "A Set of ingredients excluded in the compound")
  private Set<IngredientSummaryDto> excludedIngredients;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<IngredientSummaryDto> getIncludedIngredients() {
    return includedIngredients;
  }

  public void setIncludedIngredients(
      Set<IngredientSummaryDto> includedIngredients) {
    this.includedIngredients = includedIngredients;
  }

  public Set<IngredientSummaryDto> getExcludedIngredients() {
    return excludedIngredients;
  }

  public void setExcludedIngredients(
      Set<IngredientSummaryDto> excludedIngredients) {
    this.excludedIngredients = excludedIngredients;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomCompoundSummaryDto that = (CustomCompoundSummaryDto) o;

    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(description, that.description)) {
      return false;
    }
    if (!Objects.equals(includedIngredients, that.includedIngredients)) {
      return false;
    }
    return Objects.equals(excludedIngredients, that.excludedIngredients);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (includedIngredients != null ? includedIngredients.hashCode() : 0);
    result = 31 * result + (excludedIngredients != null ? excludedIngredients.hashCode() : 0);
    return result;
  }
}
