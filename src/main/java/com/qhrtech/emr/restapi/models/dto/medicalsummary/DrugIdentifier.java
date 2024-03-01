
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public class DrugIdentifier {

  @JsonProperty("codeSystem")
  @Schema(description = "Drug type", example = "AIG/DIN/GCN")
  private String codeSystem;

  @JsonProperty("value")
  @Schema(description = "The unique drug identification number", example = "00000001")
  private String value;

  public String getCodeSystem() {
    return codeSystem;
  }

  public void setCodeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("DrugIdentifier{");
    sb.append("codeSystem='").append(codeSystem).append('\'');
    sb.append(", value='").append(value).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DrugIdentifier that = (DrugIdentifier) o;

    if (!Objects.equals(codeSystem, that.codeSystem)) {
      return false;
    }
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    int result = codeSystem != null ? codeSystem.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
