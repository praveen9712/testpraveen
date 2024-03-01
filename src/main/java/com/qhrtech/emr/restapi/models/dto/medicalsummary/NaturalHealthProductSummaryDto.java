
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public class NaturalHealthProductSummaryDto {

  @JsonProperty("name")
  @Schema(description = "The name of the natural health product", example = "Melatonin")
  private String name;

  @JsonProperty("identifier")
  @Schema(description = "The licence number of the natural health product", example = "80000370")
  private DrugIdentifier identifier;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DrugIdentifier getIdentifier() {
    return identifier;
  }

  public void setIdentifier(DrugIdentifier identifier) {
    this.identifier = identifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NaturalHealthProductSummaryDto that = (NaturalHealthProductSummaryDto) o;

    if (!Objects.equals(name, that.name)) {
      return false;
    }
    return Objects.equals(identifier, that.identifier);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("NaturalHealthProductSummaryDto{");
    sb.append("name='").append(name).append('\'');
    sb.append(", identifier=").append(identifier);
    sb.append('}');
    return sb.toString();
  }

}
