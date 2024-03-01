
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.json.JsonSeeAlso;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.joda.time.LocalDateTime;

/**
 * This is an abstract model of patient history item history and, currently, sub models extending it
 * are as follows:
 * <ul>
 * <li>{@link PatientHistoryRegularHistoryDto}</li>
 * <li>{@link PatientHistoryTrackingHistoryDto}</li>
 * <li>{@link PatientHistoryUrlHistoryDto}</li>
 * <li>{@link PatientHistoryTextHistoryDto}</li>
 * </ul>
 * This model will set the name depending on the sub models automatically and the names are as
 * follows:
 * <ul>
 * <li>REGULAR - {@link PatientHistoryRegularHistoryDto}</li>
 * <li>TRACKING - {@link PatientHistoryTrackingHistoryDto}</li>
 * <li>URL - {@link PatientHistoryUrlHistoryDto}</li>
 * <li>FREE_TEXT - {@link PatientHistoryTextHistoryDto}</li>
 * </ul>
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "historyType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PatientHistoryRegularHistoryDto.class, name = "REGULAR"),
    @JsonSubTypes.Type(value = PatientHistoryTrackingHistoryDto.class, name = "TRACKING"),
    @JsonSubTypes.Type(value = PatientHistoryTextHistoryDto.class, name = "FREE_TEXT"),
    @JsonSubTypes.Type(value = PatientHistoryUrlHistoryDto.class, name = "URL")
})
@JsonSeeAlso({
    PatientHistoryRegularHistoryDto.class,
    PatientHistoryTrackingHistoryDto.class,
    PatientHistoryTextHistoryDto.class,
    PatientHistoryUrlHistoryDto.class
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Abstract model class of patient history item history")
public abstract class AbstractPatientHistoryItemHistoryDto implements Serializable {

  @JsonProperty("id")
  @Schema(
      description = "Id of this record",
      example = "102")
  private int id;

  @JsonProperty("patientId")
  @Schema(
      description = "Id of the patient who owns the history record",
      example = "1500")
  private int patientId;

  @JsonProperty("maskId")
  @Schema(
      description = "If masked in Accuro, this value will be the mask id, null otherwise",
      example = "2")
  private Integer maskId;

  @JsonProperty("modifiedDate")
  @Schema(
      description = "The date when this patient history item was modified",
      type = "string",
      example = "2018-07-13")
  private LocalDateTime modifiedDate;

  /**
   * The unique id of the patient history history record.
   *
   * @documentationExample 1
   *
   * @return The id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  /**
   * The unique id of the patient who owns the history record.
   *
   * @documentationExample 1
   *
   * @return The patient id.
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * If the record is masked in Accuro, this value will hold the mask id, {@code null} otherwise.
   *
   * @documentationExample 1
   *
   * @return The mask id of this record if the record is masked.
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * The date this patient history item was modified.
   *
   * @return The date this patient history item was modified.
   */
  @DocumentationExample("2018-07-13")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractPatientHistoryItemHistoryDto)) {
      return false;
    }

    AbstractPatientHistoryItemHistoryDto that = (AbstractPatientHistoryItemHistoryDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getMaskId() != null ? !getMaskId().equals(that.getMaskId()) : that.getMaskId() != null) {
      return false;
    }
    return getModifiedDate() != null ? getModifiedDate().equals(that.getModifiedDate())
        : that.getModifiedDate() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPatientId();
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    result = 31 * result + (getModifiedDate() != null ? getModifiedDate().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("id=").append(id);
    sb.append(", patientId=").append(patientId);
    sb.append(", maskId=").append(maskId);
    sb.append(", modifiedDate=").append(modifiedDate);
    return sb.toString();
  }
}
