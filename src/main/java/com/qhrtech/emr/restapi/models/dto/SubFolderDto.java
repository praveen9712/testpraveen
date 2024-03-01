
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Sub-Folder (or SubType) model object.
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Sub folder data transfer object model")
public class SubFolderDto {

  @JsonProperty("id")
  @Schema(description = "Sub folder id", example = "2")
  private Integer id;

  @JsonProperty("name")
  @NotBlank(message = "Sub folder name must not be blank or null")
  @Schema(description = "Sub folder name", example = "Family History")
  private String name;

  /**
   * Sub folder ID.
   *
   * @documentationExample 2
   *
   * @return Folder Id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sub Folder Name.
   *
   * @documentationExample Reports
   *
   * @return Sub folder name
   */
  public String getName() {
    return name;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.id);
    hash = 89 * hash + Objects.hashCode(this.name);
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
    final SubFolderDto other = (SubFolderDto) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return Objects.equals(this.id, other.id);
  }

}
