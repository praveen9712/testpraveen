
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The Relationship status transfer model object.")
public class RelationshipStatusDto {

  @JsonProperty("id")
  @Schema(description = "The status identity", example = "10")
  private int id;

  @Size(max = 50, message = "The name can not exceed 50 characters.")
  @Schema(description = "The status name", example = "Married")
  @JsonProperty("name")
  private String name;

  @Schema(description = "Indication if the relationship status was deleted or not",
      example = "false")
  @JsonProperty("deleted")
  private boolean deleted;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    if (!(o instanceof RelationshipStatusDto)) {
      return false;
    }
    RelationshipStatusDto that = (RelationshipStatusDto) o;
    if (getId() != that.getId()) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    return result;
  }
}
