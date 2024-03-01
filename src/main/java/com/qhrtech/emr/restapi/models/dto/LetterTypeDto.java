
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Letter type model.
 *
 * @see com.qhrtech.emr.accuro.model.letters.LetterType
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Letter type data transfer object model")
public class LetterTypeDto {

  @JsonProperty("typeId")
  @Schema(description = "Unique letter type id", example = "1")
  private int typeId;

  @JsonProperty("typeName")
  @Schema(description = "Letter type name", example = "Clinical Note")
  private String typeName;

  @JsonProperty("active")
  @Schema(description = "Indication if the letter type is currently active", example = "true")
  private boolean active;

  @JsonProperty("abbreviation")
  @Schema(description = "Letter type abbreviation", example = "CN")
  private String abbreviation;

  /**
   * Unique letter type ID
   *
   * @documentationExample 1
   *
   * @return Letter Type ID
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * Letter type name
   *
   * @documentationExample Clinical Note
   *
   * @return Name string
   */
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * indication if the letter type is currently active
   *
   * @documentationExample true
   *
   * @return true if active, or else false
   */
  public boolean iActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Letter type abbreviation
   *
   * @documentationExample CN
   *
   * @return Type abbreviation
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + this.typeId;
    hash = 89 * hash + Objects.hashCode(this.typeName);
    hash = 89 * hash + (this.active ? 1 : 0);
    hash = 89 * hash + Objects.hashCode(this.abbreviation);
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
    final LetterTypeDto other = (LetterTypeDto) obj;
    if (this.typeId != other.typeId) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    if (!Objects.equals(this.typeName, other.typeName)) {
      return false;
    }
    if (!Objects.equals(this.abbreviation, other.abbreviation)) {
      return false;
    }
    return true;
  }

}
