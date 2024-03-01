
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class OfficialLanguageDto {

  @Schema(description = "The code ", example = "eng")
  @JsonProperty("code")
  private String code;

  @Schema(description = "The code description", example = "English")
  @JsonProperty("description")
  private String description;

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
    if (!(o instanceof OfficialLanguageDto)) {
      return false;
    }
    OfficialLanguageDto that = (OfficialLanguageDto) o;
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
    result = 31 * result + (isDeleted() ? 1 : 0);
    return result;
  }
}
