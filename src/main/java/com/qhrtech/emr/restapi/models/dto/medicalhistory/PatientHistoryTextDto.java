
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * The PatientHistoryText data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryText
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientHistoryText data transfer object model")
public class PatientHistoryTextDto extends AbstractPatientHistoryItemDto
    implements Serializable {

  @JsonProperty("historyTypeId")
  @Schema(description = "The history type id", example = "10")
  private int historyTypeId;

  @JsonProperty("historyDate")
  @Schema(type = "string", description = "The history date", example = "2018-09-11")
  private AccuroCalendar historyDate;

  @JsonProperty("value")
  @Schema(description = "The value", example = "HepB screen")
  private String value;

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  /**
   * The history type id.
   *
   * @documentationExample 10
   *
   * @return History type id
   */
  public int getHistoryTypeId() {
    return historyTypeId;
  }

  public void setHistoryTypeId(int historyTypeId) {
    this.historyTypeId = historyTypeId;
  }

  /**
   * The history date.
   *
   * @return History date
   */
  @DocumentationExample("2018-09-11")
  @TypeHint(String.class)
  public AccuroCalendar getHistoryDate() {
    return historyDate;
  }

  public void setHistoryDate(AccuroCalendar historyDate) {
    this.historyDate = historyDate;
  }

  /**
   * The value.
   *
   * @documentationExample sample free text
   *
   * @return Value
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + this.historyTypeId;
    hash = 97 * hash + Objects.hashCode(this.historyDate);
    hash = 97 * hash + Objects.hashCode(this.value);
    hash = 97 * hash + (this.deleted ? 1 : 0);
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
    final PatientHistoryTextDto other = (PatientHistoryTextDto) obj;
    if (this.historyTypeId != other.historyTypeId) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    if (!Objects.equals(this.historyDate, other.historyDate)) {
      return false;
    }
    if (this.deleted != other.deleted) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PatientHistoryTextDto{" + "historyTypeId=" + historyTypeId + ", historyDate="
        + historyDate + ", value=" + value + '}';
  }

}
