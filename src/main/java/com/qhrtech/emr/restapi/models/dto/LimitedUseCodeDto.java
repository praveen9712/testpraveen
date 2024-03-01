
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The LimitedUseCode data transfer model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "LimitedUseCode data transfer model")
public class LimitedUseCodeDto {

  @JsonProperty("code")
  @Schema(description = "Limited use code", example = "236")
  private int code;

  @JsonProperty("description")
  @Schema(description = "Description of the limited use code",
      example = "For the treatment of Paget's disease. LU Authorization Period: Indefinite")
  private String description;

  /**
   * Limited Use Code
   *
   * @documentationExample 236
   *
   * @return Code
   */
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  /**
   * Description of Limited Use Code
   *
   * @documentationExample For the treatment of Paget's disease. LU Authorization Period:
   *                       Indefinite.
   *
   * @return Description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + this.code;
    hash = 41 * hash + Objects.hashCode(this.description);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final LimitedUseCodeDto other = (LimitedUseCodeDto) obj;
    if (this.code != other.code) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    return true;
  }

}
