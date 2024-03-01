
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Alias model object. A Patient Alias is an alternative name that a patient may be known by.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(
    description = "Alias model object. "
        + "The Patient Alias is an alternative name that the patient may be known by.")
public class AliasDto {

  @JsonProperty("aliasId")
  @Schema(description = "Patient alias ID", example = "1")
  private int aliasId;

  @JsonProperty("firstName")
  @Schema(description = "First name", example = "Jonathan")
  private String firstName;

  @JsonProperty("lastName")
  @Schema(description = "Last name", example = "Doe")
  private String lastName;

  @JsonProperty("aliasType")
  @Schema(
      description = "The type of alias. This is a dynamic variable defined in Accuro.",
      example = "AliasType")
  private String aliasType;

  /**
   * Patient Alias ID
   *
   * @documentationExample 1
   *
   * @return Alias ID
   */
  public int getAliasId() {
    return aliasId;
  }

  public void setAliasId(int aliasId) {
    this.aliasId = aliasId;
  }

  /**
   * First name
   *
   * @documentationExample Jonathan
   *
   * @return First name
   */
  @NotNull
  @Size(max = 50)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last name
   *
   * @documentationExample Doe
   *
   * @return Last Name
   */
  @NotNull
  @Size(max = 50)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The type of alias. This is a dynamic variable defined in Accuro.
   *
   * @documentationExample AliasType
   *
   * @return Alias type
   */
  @NotNull
  @Size(max = 100)
  public String getAliasType() {
    return aliasType;
  }

  public void setAliasType(String aliasType) {
    this.aliasType = aliasType;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + this.aliasId;
    hash = 97 * hash + Objects.hashCode(this.firstName);
    hash = 97 * hash + Objects.hashCode(this.lastName);
    hash = 97 * hash + Objects.hashCode(this.aliasType);
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
    final AliasDto other = (AliasDto) obj;
    if (this.aliasId != other.aliasId) {
      return false;
    }
    if (!Objects.equals(this.firstName, other.firstName)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.aliasType, other.aliasType)) {
      return false;
    }
    return true;
  }

}
