
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The ingredient summary transfer model")
public class IngredientSummaryDto {

  @JsonProperty("codeSystem")
  @Schema(description = "Drug type", example = "AIG/DIN/HIC")
  private String codeSystem;

  @JsonProperty("drugId")
  @Schema(description = "The ingredient id", example = "1")
  private String drugId;

  @JsonProperty("componentAmount")
  @Schema(description = "The component amount", example = "1")
  private int componentAmount;

  @JsonProperty("componentUnit")
  @Schema(description = "The component unit", example = "mg")
  private String componentUnit;

  @JsonProperty("componentNote")
  @Schema(description = "The component note", example = "THIS IS THE SAMPLE COMPONENT")
  private String componentNote;

  public String getCodeSystem() {
    return codeSystem;
  }

  public void setCodeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
  }

  public String getDrugId() {
    return drugId;
  }

  public void setDrugId(String drugId) {
    this.drugId = drugId;
  }

  public int getComponentAmount() {
    return componentAmount;
  }

  public void setComponentAmount(int componentAmount) {
    this.componentAmount = componentAmount;
  }

  public String getComponentUnit() {
    return componentUnit;
  }

  public void setComponentUnit(String componentUnit) {
    this.componentUnit = componentUnit;
  }

  public String getComponentNote() {
    return componentNote;
  }

  public void setComponentNote(String componentNote) {
    this.componentNote = componentNote;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IngredientSummaryDto that = (IngredientSummaryDto) o;

    if (componentAmount != that.componentAmount) {
      return false;
    }
    if (!Objects.equals(codeSystem, that.codeSystem)) {
      return false;
    }
    if (!Objects.equals(drugId, that.drugId)) {
      return false;
    }
    if (!Objects.equals(componentUnit, that.componentUnit)) {
      return false;
    }
    return Objects.equals(componentNote, that.componentNote);
  }

  @Override
  public int hashCode() {
    int result = codeSystem != null ? codeSystem.hashCode() : 0;
    result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
    result = 31 * result + componentAmount;
    result = 31 * result + (componentUnit != null ? componentUnit.hashCode() : 0);
    result = 31 * result + (componentNote != null ? componentNote.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("IngredientSummaryDto{");
    sb.append("codeSystem='").append(codeSystem).append('\'');
    sb.append(", drugId='").append(drugId).append('\'');
    sb.append(", componentAmount=").append(componentAmount);
    sb.append(", componentUnit='").append(componentUnit).append('\'');
    sb.append(", componentNote='").append(componentNote).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
