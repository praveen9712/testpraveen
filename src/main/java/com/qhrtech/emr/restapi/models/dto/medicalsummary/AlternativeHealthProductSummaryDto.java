
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * An alternative health product transfer model. Only selective fields are included in this as per
 * requirement.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The alternative health product transfer model")
public class AlternativeHealthProductSummaryDto {

  @JsonProperty("name")
  @Schema(description = "The unique name of the alternative health product",
      example = "QHR SAMPLE PRODUCT 1MG")
  private String name;

  @JsonProperty("description")
  @Schema(description = "The description about the alternative health product",
      example = "THIS IS A SAMPLE PRODUCT")
  private String description;

  @JsonProperty("category")
  @Schema(description = "The category of the alternative health product",
      example = "TEMPORARY")
  private String category;

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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AlternativeHealthProductSummaryDto that = (AlternativeHealthProductSummaryDto) o;

    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(description, that.description)) {
      return false;
    }
    return Objects.equals(category, that.category);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    return result;
  }
}
