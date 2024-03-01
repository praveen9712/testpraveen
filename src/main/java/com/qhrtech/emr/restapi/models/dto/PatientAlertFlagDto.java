
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Patient Alert Flag model object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient Flag data transfer object model")
public class PatientAlertFlagDto {

  @JsonProperty("id")
  @Schema(description = "Patient flag id", example = "1")
  private int id;

  @JsonProperty("name")
  @Schema(description = "The name of the patient flag", example = "Critical Attn Needed")
  private String name;

  /**
   * Patient flag ID
   *
   * @return int flag ID
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The name of the patient alert flag
   *
   * @return A string name for the patient alert flag
   * @documentationExample Critical Attn Needed
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 61 * hash + this.id;
    hash = 61 * hash + Objects.hashCode(this.name);
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
    final PatientAlertFlagDto other = (PatientAlertFlagDto) obj;
    if (this.id != other.id) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }
}
