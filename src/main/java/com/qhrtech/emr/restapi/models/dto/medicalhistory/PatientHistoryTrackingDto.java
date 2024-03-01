
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * The PatientHistoryTracking data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientHistoryTracking data transfer object model")
public class PatientHistoryTrackingDto extends AbstractPatientHistoryItemDto
    implements Serializable {

  @JsonProperty("modifiedUserId")
  @Schema(description = "The modified user id", example = "1")
  private int modifiedUserId;

  @JsonProperty("creatorUserId")
  @Schema(description = "The creator user id", example = "2")
  private Integer creatorUserId;

  @JsonProperty("stateDate")
  @Schema(description = "The state date", example = "2018-09-11")
  private LocalDate stateDate;

  @JsonProperty("modifiedDate")
  @Schema(type = "string", description = "The modified date", example = "2018-07-13T00:00:00.000")
  private LocalDateTime modifiedDate;

  @JsonProperty("note")
  @Schema(description = "The note")
  private String note;

  @JsonProperty("currentState")
  @Schema(description = "Flag indicating if this is current state", example = "true")
  private boolean currentState;

  @JsonProperty("majorChange")
  @Schema(description = "Flag indicating if this is the major change", example = "true")
  private boolean majorChange;

  @JsonProperty("active")
  @Schema(description = "Flag indicating if this item is active", example = "true")
  private boolean active;

  @JsonProperty("historyTrackingItem")
  @Schema(description = "The history tracking item")
  private HistoryTrackingItemDto historyTrackingItem;

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  /**
   * The modified user id.
   *
   * @documentationExample 15
   *
   * @return Modified user id
   */
  public int getModifiedUserId() {
    return modifiedUserId;
  }

  public void setModifiedUserId(int modifiedUserId) {
    this.modifiedUserId = modifiedUserId;
  }

  /**
   * The creator user id.
   *
   * @documentationExample 16
   *
   * @return Creator user id
   */
  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * The state date.
   *
   * @return State Date
   */
  @DocumentationExample("2018-09-11")
  @TypeHint(String.class)
  public LocalDate getStateDate() {
    return stateDate;
  }

  public void setStateDate(LocalDate stateDate) {
    this.stateDate = stateDate;
  }

  /**
   * The modified date.
   *
   * @return Modified Date
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * The note.
   *
   * @documentationExample Note of something
   *
   * @return Note
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * Indicates whether or not this is the current state.
   *
   * @documentationExample true
   *
   * @return Current state
   */
  public boolean isCurrentState() {
    return currentState;
  }

  public void setCurrentState(boolean currentState) {
    this.currentState = currentState;
  }

  /**
   * Indicates this is whether or not the major change.
   *
   * @documentationExample true
   *
   * @return Major change
   */
  public boolean isMajorChange() {
    return majorChange;
  }

  public void setMajorChange(boolean majorChange) {
    this.majorChange = majorChange;
  }

  /**
   * A flag if the item is active.
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
   * History Tracking Item data transfer object.
   * <p>
   * See {@link HistoryTrackingItemDto}
   *
   * @return History Tracking Item data transfer object.
   */
  public HistoryTrackingItemDto getHistoryTrackingItem() {
    return historyTrackingItem;
  }

  public void setHistoryTrackingItem(HistoryTrackingItemDto historyTrackingItemDto) {
    this.historyTrackingItem = historyTrackingItemDto;
  }

  /**
   * A flag to indicate the delete state of this patient history item.
   *
   * @documentationExample true
   *
   * @return The delete state
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "PatientHistoryTrackingDto{" + "modifiedUserId=" + modifiedUserId + ", creatorUserId="
        + creatorUserId + ", stateDate=" + stateDate + ", modifiedDate=" + modifiedDate + ", note="
        + note + ", currentState=" + currentState + ", majorChange=" + majorChange + ", active="
        + active + ", historyTrackingItemDto=" + historyTrackingItem + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PatientHistoryTrackingDto that = (PatientHistoryTrackingDto) o;

    if (getModifiedUserId() != that.getModifiedUserId()) {
      return false;
    }
    if (isCurrentState() != that.isCurrentState()) {
      return false;
    }
    if (isMajorChange() != that.isMajorChange()) {
      return false;
    }
    if (isActive() != that.isActive()) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    if (getCreatorUserId() != null ? !getCreatorUserId().equals(that.getCreatorUserId())
        : that.getCreatorUserId() != null) {
      return false;
    }
    if (getStateDate() != null ? !getStateDate().equals(that.getStateDate())
        : that.getStateDate() != null) {
      return false;
    }
    if (getModifiedDate() != null ? !getModifiedDate().equals(that.getModifiedDate())
        : that.getModifiedDate() != null) {
      return false;
    }
    if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null) {
      return false;
    }
    return getHistoryTrackingItem() != null ? getHistoryTrackingItem()
        .equals(that.getHistoryTrackingItem()) : that.getHistoryTrackingItem() == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getModifiedUserId();
    result = 31 * result + (getCreatorUserId() != null ? getCreatorUserId().hashCode() : 0);
    result = 31 * result + (getStateDate() != null ? getStateDate().hashCode() : 0);
    result = 31 * result + (getModifiedDate() != null ? getModifiedDate().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (isCurrentState() ? 1 : 0);
    result = 31 * result + (isMajorChange() ? 1 : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result =
        31 * result + (getHistoryTrackingItem() != null ? getHistoryTrackingItem().hashCode() : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    return result;
  }
}
