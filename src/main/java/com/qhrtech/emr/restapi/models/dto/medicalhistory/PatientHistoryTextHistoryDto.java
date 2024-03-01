
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.restapi.endpoints.provider.medicalhistory.HistoryTypeEndpoint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * Patient history text history data transfer object and this model extends
 * {@link AbstractPatientHistoryItemHistoryDto}.
 *
 * @See {@link com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryTextHistory}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient history text history data transfer object")
public class PatientHistoryTextHistoryDto extends AbstractPatientHistoryItemHistoryDto implements
    Serializable {

  @JsonProperty("patientHistoryTextId")
  @Schema(description = "Id of the patient history text record", example = "1")
  private int patientHistoryTextId;

  @JsonProperty("historyTypeId")
  @Schema(description = "The history type id", example = "10")
  private int historyTypeId;

  @JsonProperty("historyDate")
  @Schema(description = "The history date", example = "2018-09-11", type = "string")
  private AccuroCalendar historyDate;

  @JsonProperty("value")
  @Schema(description = "The value", example = "HepB screen")
  private String value;

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  @JsonProperty("historyUserId")
  @Schema(description = "Id of the Accuro user who created/updated the patient history text record",
      example = "2")
  private Integer historyUserId;

  /**
   * The unique id of the patient history text record.
   *
   * @documentationExample 1
   *
   * @return The patient history text id.
   */
  public int getPatientHistoryTextId() {
    return patientHistoryTextId;
  }

  public void setPatientHistoryTextId(int patientHistoryTextId) {
    this.patientHistoryTextId = patientHistoryTextId;
  }

  /**
   * The unique id of the history type.
   *
   * @see HistoryTypeEndpoint
   *
   * @documentationExample 1
   *
   * @return The history type id.
   */
  public int getHistoryTypeId() {
    return historyTypeId;
  }

  public void setHistoryTypeId(int historyTypeId) {
    this.historyTypeId = historyTypeId;
  }

  /**
   * The created date of this patient history url at the very beginning.
   *
   * @return The created date of this patient history url at the very beginning.
   */
  public AccuroCalendar getHistoryDate() {
    return historyDate;
  }

  public void setHistoryDate(AccuroCalendar historyDate) {
    this.historyDate = historyDate;
  }

  /**
   * The free text on the patient history text record.
   *
   * @documentationExample This patient may need to come back this January.
   *
   * @return The free text.
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
   * The id of the Accuro user who created/updated the patient history text record.
   *
   * @documentationExample 1
   *
   * @return The user id of the creator/updater.
   */
  public Integer getHistoryUserId() {
    return historyUserId;
  }

  public void setHistoryUserId(Integer historyUserId) {
    this.historyUserId = historyUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientHistoryTextHistoryDto)) {
      return false;
    }

    PatientHistoryTextHistoryDto that = (PatientHistoryTextHistoryDto) o;

    if (getPatientHistoryTextId() != that.getPatientHistoryTextId()) {
      return false;
    }
    if (getHistoryTypeId() != that.getHistoryTypeId()) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    if (getHistoryDate() != null ? !getHistoryDate().equals(that.getHistoryDate())
        : that.getHistoryDate() != null) {
      return false;
    }
    if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) {
      return false;
    }
    return getHistoryUserId() != null ? getHistoryUserId().equals(that.getHistoryUserId())
        : that.getHistoryUserId() == null;
  }

  @Override
  public int hashCode() {
    int result = getPatientHistoryTextId();
    result = 31 * result + getHistoryTypeId();
    result = 31 * result + (getHistoryDate() != null ? getHistoryDate().hashCode() : 0);
    result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    result = 31 * result + (getHistoryUserId() != null ? getHistoryUserId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PatientHistoryTextHistoryDto{");
    sb.append(super.toString());
    sb.append(", patientHistoryTextId=").append(patientHistoryTextId);
    sb.append(", historyTypeId=").append(historyTypeId);
    sb.append(", historyDate=").append(historyDate);
    sb.append(", value='").append(value).append('\'');
    sb.append(", deleted=").append(deleted);
    sb.append(", historyUserId=").append(historyUserId);
    sb.append('}');
    return sb.toString();
  }
}
