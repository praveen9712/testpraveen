
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The History Tracking Item data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.HistoryTrackingItem
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The History Tracking Item data transfer object model")
public class HistoryTrackingItemDto {

  @JsonProperty("id")
  @Schema(description = "The unique history item id", example = "1")
  private int id;

  @JsonProperty("historyTypeId")
  @Schema(description = "The unique history type id that the item belongs to", example = "1")
  private int historyTypeId;

  @JsonProperty("name")
  @Schema(description = "The tracking name", example = "Flu Vaccination")
  private String name;

  @JsonProperty("active")
  @Schema(description = "The flag indicating if the item is active", example = "true")
  private boolean active;

  /**
   * The unique history item id.
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
  public int getHistoryTypeId() {
    return historyTypeId;
  }

  public void setHistoryTypeId(int historyTypeId) {
    this.historyTypeId = historyTypeId;
  }

  /**
   * The tracking name.
   *
   * @documentationExample Flu Vaccination
   *
   * @return The tracking name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + this.id;
    hash = 23 * hash + this.historyTypeId;
    hash = 23 * hash + Objects.hashCode(this.name);
    hash = 23 * hash + (this.active ? 1 : 0);
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
    final HistoryTrackingItemDto other = (HistoryTrackingItemDto) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.historyTypeId != other.historyTypeId) {
      return false;
    }
    if (this.active != other.active) {
      return false;
    }
    return Objects.equals(this.name, other.name);
  }

  @Override
  public String toString() {
    return "HistoryTrackingItemDto{" + "id=" + id + ", historyTypeId=" + historyTypeId + ", name="
        + name + ", active=" + active + '}';
  }

}
