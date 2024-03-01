
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The Gender data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.demographics.Gender
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Gender data transfer object model")
public class GenderDto {

  @JsonProperty("id")
  @Schema(description = "Unique identifier for the gender", example = "11")
  private int id;

  @JsonProperty("name")
  @Schema(description = "The name for the gender", example = "Female")
  private String name;

  @JsonProperty("builtIn")
  @Schema(description = "Boolean indication if the gender is built-in or custom",
      example = "true")
  private boolean builtIn;

  @JsonProperty("shortName")
  @Schema(description = "The abbreviation for the gender", example = "F")
  private String shortName;

  @JsonProperty("simplifiesToMale")
  @Schema(description = "Indication if the gender can be simplified to male", example = "true")
  private boolean simplifiesToMale;

  /**
   * Unique identifier for the Gender object
   *
   * @documentationExample 11
   *
   * @return A unique ID
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * A name for the Gender object
   *
   * @documentationExample Female
   *
   * @return A name String
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Boolean indication if the Gender object is built in or custom.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if built in or <code>false</code> if not built in.
   */
  public boolean isBuiltIn() {
    return builtIn;
  }

  public void setBuiltIn(boolean builtIn) {
    this.builtIn = builtIn;
  }

  /**
   * The abbreviation for the Gender object.
   *
   * @documentationExample F
   *
   * @return A short name String
   */
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Indication if the gender can be simplified to Male.
   *
   * @documentationExample true
   *
   * @return true if the gender simplifies to male
   */
  public boolean isSimplifiesToMale() {
    return simplifiesToMale;
  }

  public void setSimplifiesToMale(boolean simplifiesToMale) {
    this.simplifiesToMale = simplifiesToMale;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GenderDto genderDto = (GenderDto) o;

    if (id != genderDto.id) {
      return false;
    }
    if (builtIn != genderDto.builtIn) {
      return false;
    }
    if (simplifiesToMale != genderDto.simplifiesToMale) {
      return false;
    }
    if (name != null ? !name.equals(genderDto.name) : genderDto.name != null) {
      return false;
    }
    return shortName != null ? shortName.equals(genderDto.shortName) : genderDto.shortName == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (builtIn ? 1 : 0);
    result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
    result = 31 * result + (simplifiesToMale ? 1 : 0);
    return result;
  }
}
