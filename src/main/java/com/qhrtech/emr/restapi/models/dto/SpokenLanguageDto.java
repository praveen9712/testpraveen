
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class SpokenLanguageDto {

  @Schema(description = "The code ", example = "ita")
  @JsonProperty("code")
  private String code;

  @Schema(description = "The code description", example = "Italian")
  @JsonProperty("description")
  private String description;

  @Schema(description = "The code order of same category", example = "1")
  @JsonProperty("codeOrder")
  private int codeOrder;

  @Schema(description = "Indication if the language was deleted or not", example = "false")
  @JsonProperty("deleted")
  private boolean deleted;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCodeOrder() {
    return codeOrder;
  }

  public void setCodeOrder(int codeOrder) {
    this.codeOrder = codeOrder;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SpokenLanguageDto)) {
      return false;
    }
    SpokenLanguageDto that = (SpokenLanguageDto) o;
    if (getCodeOrder() != that.getCodeOrder()) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
      return false;
    }
    return getDescription() != null ? getDescription().equals(that.getDescription())
        : that.getDescription() == null;
  }

  @Override
  public int hashCode() {
    int result = getCode() != null ? getCode().hashCode() : 0;
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + getCodeOrder();
    result = 31 * result + (isDeleted() ? 1 : 0);
    return result;
  }
}
