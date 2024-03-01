
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The formulation summary transfer model. This model is build for the specific story which does not
 * require all the fields from FormulationDto to be exposed.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The formulation transfer model")
public class FormulationSummaryDto {

  @JsonProperty("identifier")
  @Schema(description = "GCN and its unique identifier")
  private DrugIdentifier identifier;

  @JsonProperty("name")
  @Schema(description = "The formulation name", example = "250 mg Oral Capsule")
  private String name;

  @JsonProperty("strengthDescription")
  @Schema(description = "The strength description", example = "2 mg/mL")
  private String strengthDescription;

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

  public String getStrengthDescription() {
    return strengthDescription;
  }

  public void setStrengthDescription(String strengthDescription) {
    this.strengthDescription = strengthDescription;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FormulationSummaryDto that = (FormulationSummaryDto) o;

    if (!Objects.equals(identifier, that.identifier)) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    return Objects.equals(strengthDescription, that.strengthDescription);
  }

  @Override
  public int hashCode() {
    int result = identifier != null ? identifier.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (strengthDescription != null ? strengthDescription.hashCode() : 0);
    return result;
  }
}
