
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The History Item data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.HistoryRegularItem
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "History item data transfer object model")
public class HistoryRegularItemDto {

  @JsonProperty("id")
  @Schema(description = "Id of this item", example = "1")
  private int id;

  @JsonProperty("typeId")
  @Schema(description = "Unique history type id", example = "1")
  private int typeId;

  @JsonProperty("description")
  @Schema(description = "Description of the item", example = "Chronic Pain")
  private String description;

  @JsonProperty("location")
  @Schema(description = "The eye location abbreviation",
      example = "LEFT_EYE")
  private EyeCode location;

  @JsonProperty("active")
  @Schema(description = "Flag which indicates if the item is active", example = "true")
  private boolean active;

  @JsonProperty("code")
  @Schema(description = "The drug code", example = "1230")
  private String code;

  @JsonProperty("codeSystem")
  @Schema(description = "The code system", example = "CUSTOM")
  private String codeSystem;

  @JsonProperty("codeSubSystem")
  @Schema(description = "The drug code subsystem", example = "Diagnosis")
  private String codeSubSystem;

  /**
   * The unique id.
   *
   * @documentationExample 1
   *
   * @return The history item id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The unique history type id that the item belongs to.
   *
   * @documentationExample 1
   *
   * @return The history type id
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * The description of the history item.
   *
   * @documentationExample Custom Item
   *
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The eye location abbreviation.
   *
   * @documentationExample OS
   *
   * @return The eye location.
   */
  public EyeCode getLocation() {
    return location;
  }

  public void setLocation(EyeCode location) {
    this.location = location;
  }

  /**
   * The flag indicating if the item is active.
   *
   * @documentationExample true
   *
   * @return {@code true} if active or {@code false} if not.
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * The drug code
   *
   * @documentationExample V9199
   *
   * @return The drug code
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * The drug code system.
   *
   * @documentationExample ICD9
   *
   * @return The drug code system
   */
  public String getCodeSystem() {
    return codeSystem;
  }

  public void setCodeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
  }

  /**
   * The drug code subsystem.
   *
   * @documentationExample Diagnosis
   *
   * @return THe drug code subsystem
   */
  public String getCodeSubSystem() {
    return codeSubSystem;
  }

  public void setCodeSubSystem(String codeSubSystem) {
    this.codeSubSystem = codeSubSystem;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + this.id;
    hash = 79 * hash + this.typeId;
    hash = 79 * hash + Objects.hashCode(this.description);
    hash = 79 * hash + Objects.hashCode(this.location);
    hash = 79 * hash + (this.active ? 1 : 0);
    hash = 79 * hash + Objects.hashCode(this.code);
    hash = 79 * hash + Objects.hashCode(this.codeSystem);
    hash = 79 * hash + Objects.hashCode(this.codeSubSystem);
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
    final HistoryRegularItemDto other = (HistoryRegularItemDto) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.typeId != other.typeId) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    if (!Objects.equals(this.code, other.code)) {
      return false;
    }
    if (!Objects.equals(this.codeSystem, other.codeSystem)) {
      return false;
    }
    if (!Objects.equals(this.codeSubSystem, other.codeSubSystem)) {
      return false;
    }
    return this.location == other.location;
  }
}
