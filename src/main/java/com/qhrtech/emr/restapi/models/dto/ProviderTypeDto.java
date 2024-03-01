
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Provider Type model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Provider Type data transfer object model")
public class ProviderTypeDto {

  @JsonProperty("typeId")
  @Schema(description = "The unique provider type id", example = "1")
  private int typeId;

  @JsonProperty("parentTypeId")
  @Schema(description = "The unique parent type id", example = "1")
  private Integer parentTypeId;

  @JsonProperty("name")
  @Schema(description = "The provider name", example = "General Surgery")
  private String name;

  @JsonProperty("cambianName")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The cambian name", example = "GENERAL_SURGERY")
  private String cambianName;

  @JsonProperty("selectable")
  @Schema(description = "Indication if the provider type is selectable or not", example = "true")
  private boolean selectable;

  @JsonProperty("physician")
  @Schema(description = "Indication if the provider type is associated with the physician",
      example = "true")
  private boolean physician;

  /**
   * The unique Provider Type ID
   *
   * @documentationExample 1
   *
   * @return A ProviderType ID
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * A unique Parent Type ID
   *
   * @documentationExample 1
   *
   * @return A ParentType ID
   */
  public Integer getParentTypeId() {
    return parentTypeId;
  }

  public void setParentTypeId(Integer parentTypeId) {
    this.parentTypeId = parentTypeId;
  }

  /**
   * The providers name
   *
   * @documentationExample General Surgery
   *
   * @return The providers name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Cambian name
   *
   * @documentationExample GENERAL_SURGERY
   *
   * @return Cambian name
   */
  public String getCambianName() {
    return cambianName;
  }

  public void setCambianName(String cambianName) {
    this.cambianName = cambianName;
  }

  /**
   * Indication if the Provider Type is selectable or not.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if selectable, or else <code>false</code>
   */
  public boolean isSelectable() {
    return selectable;
  }

  public void setSelectable(boolean selectable) {
    this.selectable = selectable;
  }

  /**
   * Indication if the Provider Type is associated with a physician.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if physician, or else <code>false</code>
   */
  public boolean isPhysician() {
    return physician;
  }

  public void setPhysician(boolean physician) {
    this.physician = physician;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + this.typeId;
    hash = 67 * hash + Objects.hashCode(this.parentTypeId);
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.cambianName);
    hash = 67 * hash + (this.selectable ? 1 : 0);
    hash = 67 * hash + (this.physician ? 1 : 0);
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
    final ProviderTypeDto other = (ProviderTypeDto) obj;
    if (this.typeId != other.typeId) {
      return false;
    }
    if (this.selectable != other.selectable) {
      return false;
    }
    if (this.physician != other.physician) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.cambianName, other.cambianName)) {
      return false;
    }
    if (!Objects.equals(this.parentTypeId, other.parentTypeId)) {
      return false;
    }
    return true;
  }

}
