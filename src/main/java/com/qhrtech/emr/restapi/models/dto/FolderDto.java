
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Folder model object.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Folder data transfer object model")
public class FolderDto {

  @JsonProperty("id")
  @Schema(description = "ID of the folder", example = "2")
  private Integer id;

  @JsonProperty("name")
  @NotBlank(message = "Folder name must not be blank or null")
  @Schema(description = "Name of the folder", example = "Document")
  private String name;

  @JsonProperty("subFolders")
  @Valid
  @Schema(
      description = "A set of sub folders",
      type = "array",
      example = "[{\"id\": 1, \"name\": \"Document\"},"
          + "{\"id\": 2, \"name\": \"Family History\"}]")
  private Set<SubFolderDto> subFolders;


  /**
   * ID of the folder.
   *
   * @documentationExample 2
   *
   * @return Folder Id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Name of the folder.
   *
   * @documentationExample Labs
   *
   * @return Folder name
   */
  public String getName() {
    return name;
  }

  /**
   * List of all sub-folder objects of type {@link SubFolderDto} which are linked to this folder.
   *
   * @documentationExample Xrays
   *
   * @return Set of {@link SubFolderDto}
   */
  public Set<SubFolderDto> getSubFolders() {
    return subFolders;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSubFolders(Set<SubFolderDto> subFolders) {
    this.subFolders = subFolders;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.id);
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.subFolders);
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
    final FolderDto other = (FolderDto) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.subFolders, other.subFolders);
  }

}
