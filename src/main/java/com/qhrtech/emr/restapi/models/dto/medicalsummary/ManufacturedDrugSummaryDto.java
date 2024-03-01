
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public class ManufacturedDrugSummaryDto {

  @JsonProperty("identifier")
  @Schema(description = "This object will have DIN and its unique value")
  private DrugIdentifier identifier;

  @JsonProperty("name")
  @Schema(description = "The name of the manufactured drug", example = "QHR SAMPLE 1MG TABLET")
  private String name;

  @JsonProperty("manufacturer")
  @Schema(description = "The manufacturer of the manufactured drug", example = "QHR PHARMA")
  private String manufacturer;

  @JsonProperty("activeIngredientIdentifier")
  @Schema(description = "This object will have AIG and its unique value")
  private DrugIdentifier activeIngredientIdentifier;

  public DrugIdentifier getIdentifier() {
    return identifier;
  }

  public void setIdentifier(DrugIdentifier identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public DrugIdentifier getActiveIngredientIdentifier() {
    return activeIngredientIdentifier;
  }

  public void setActiveIngredientIdentifier(
      DrugIdentifier activeIngredientIdentifier) {
    this.activeIngredientIdentifier = activeIngredientIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ManufacturedDrugSummaryDto that = (ManufacturedDrugSummaryDto) o;

    if (!Objects.equals(identifier, that.identifier)) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(manufacturer, that.manufacturer)) {
      return false;
    }
    return Objects.equals(activeIngredientIdentifier, that.activeIngredientIdentifier);
  }

  @Override
  public int hashCode() {
    int result = identifier != null ? identifier.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
    result =
        31 * result + (activeIngredientIdentifier != null ? activeIngredientIdentifier.hashCode()
            : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ManufacturedDrugSummaryDto{");
    sb.append("identifier=").append(identifier);
    sb.append(", name='").append(name).append('\'');
    sb.append(", manufacturer='").append(manufacturer).append('\'');
    sb.append(", activeIngredientIdentifier=").append(activeIngredientIdentifier);
    sb.append('}');
    return sb.toString();
  }

}
