
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.joda.time.LocalDate;

/**
 * Patient history tracking history data transfer object and this model extends
 * {@link AbstractPatientHistoryItemHistoryDto}.
 *
 * @See {@link com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTracking}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient history tracking history data transfer object")
public class PatientHistoryTrackingHistoryDto extends AbstractPatientHistoryItemHistoryDto
    implements Serializable {

  @JsonProperty("modifiedUserId")
  @Schema(description = "The modified user id", example = "1")
  private int modifiedUserId;

  @JsonProperty("createdUserId")
  @Schema(description = "The creator user id", example = "2")
  private Integer createdUserId;

  @JsonProperty("stateDate")
  @Schema(description = "The state date", example = "2018-09-11", type = "string")
  private LocalDate stateDate;

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

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  private HistoryTrackingItemDto historyTrackingItem;

  /**
   * The id of the Accuro user who updated the patient history tracking record.
   *
   * @documentationExample 1
   *
   * @return The user id of the updater.
   */
  public int getModifiedUserId() {
    return modifiedUserId;
  }

  public void setModifiedUserId(int modifiedUserId) {
    this.modifiedUserId = modifiedUserId;
  }

  /**
   * The id of the Accuro user who created the patient history tracking history record at the very
   * first time.
   *
   * @documentationExample 1
   *
   * @return The user id of the creator.
   */
  public Integer getCreatedUserId() {
    return createdUserId;
  }

  public void setCreatedUserId(Integer createdUserId) {
    this.createdUserId = createdUserId;
  }

  /**
   * The recorded date for this patient history tracking history record.
   *
   * @return The recorded date of this patient history tracking history record.
   */
  @DocumentationExample("2018-09-06")
  @TypeHint(String.class)
  public LocalDate getStateDate() {
    return stateDate;
  }

  public void setStateDate(LocalDate stateDate) {
    this.stateDate = stateDate;
  }

  /**
   * The free text note on the patient history tracking record.
   *
   * @documentationExample Patient was a little unsure of the date on this event.
   *
   * @return The note on the patient tracking regular.
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * Whether this record is a current record. It means if this is true and not deleted the history
   * show up on Accuro tracking history band screen.
   *
   * @documentationExample true
   *
   * @return If this is a current record or not.
   */
  public boolean isCurrentState() {
    return currentState;
  }

  public void setCurrentState(boolean currentState) {
    this.currentState = currentState;
  }

  /**
   * Whether this record is major change.
   *
   * @documentationExample true
   *
   * @return If this is a major change record or not.
   */
  public boolean isMajorChange() {
    return majorChange;
  }

  public void setMajorChange(boolean majorChange) {
    this.majorChange = majorChange;
  }

  /**
   * Whether this is active as marked by Accuro.
   *
   * @documentationExample true
   *
   * @return If this is an active record or not.
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Whether this is deleted as marked by Accuro.
   *
   * @documentationExample true
   *
   * @return If this is an active record or not.
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * History tracking item this patient history tracking history records are associated to.
   * <p>
   * Our documentation example is unable to display this.
   *
   * @return The history tracking item
   */
  public HistoryTrackingItemDto getHistoryTrackingItem() {
    return historyTrackingItem;
  }

  public void setHistoryTrackingItem(
      HistoryTrackingItemDto historyTrackingItem) {
    this.historyTrackingItem = historyTrackingItem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientHistoryTrackingHistoryDto)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PatientHistoryTrackingHistoryDto that = (PatientHistoryTrackingHistoryDto) o;

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
    if (getCreatedUserId() != null ? !getCreatedUserId().equals(that.getCreatedUserId())
        : that.getCreatedUserId() != null) {
      return false;
    }
    if (getStateDate() != null ? !getStateDate().equals(that.getStateDate())
        : that.getStateDate() != null) {
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
    result = 31 * result + (getCreatedUserId() != null ? getCreatedUserId().hashCode() : 0);
    result = 31 * result + (getStateDate() != null ? getStateDate().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (isCurrentState() ? 1 : 0);
    result = 31 * result + (isMajorChange() ? 1 : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    result =
        31 * result + (getHistoryTrackingItem() != null ? getHistoryTrackingItem().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb =
        new StringBuilder("PatientHistoryTrackingHistoryDto{");
    sb.append(super.toString());
    sb.append(", modifiedUserId=").append(modifiedUserId);
    sb.append(", createdUserId=").append(createdUserId);
    sb.append(", stateDate=").append(stateDate);
    sb.append(", note='").append(note).append('\'');
    sb.append(", currentState=").append(currentState);
    sb.append(", majorChange=").append(majorChange);
    sb.append(", active=").append(active);
    sb.append(", deleted=").append(deleted);
    sb.append(", historyTrackingItem=").append(historyTrackingItem);
    sb.append('}');
    return sb.toString();
  }
}
