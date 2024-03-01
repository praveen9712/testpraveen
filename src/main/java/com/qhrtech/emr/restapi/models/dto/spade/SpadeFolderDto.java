
package com.qhrtech.emr.restapi.models.dto.spade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpadeFolderDto {

  private Integer id;

  @NotBlank
  private String name;

  private boolean unrestricted;

  /**
   * Boolean indicating if the folder is unrestricted.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isUnrestricted() {
    return unrestricted;
  }

  public void setUnrestricted(boolean unrestricted) {
    this.unrestricted = unrestricted;
  }

  /**
   * ID of the spade folder.
   *
   * @documentationExample 2
   *
   * @return Folder Id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Name of the spade folder.
   *
   * @documentationExample James' Folder
   *
   * @return Folder name
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpadeFolderDto that = (SpadeFolderDto) o;

    if (isUnrestricted() != that.isUnrestricted()) {
      return false;
    }
    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    }
    return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (isUnrestricted() ? 1 : 0);
    return result;
  }
}
