
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.webcohesion.enunciate.metadata.json.JsonSeeAlso;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The abstract class of patient history items.
 *
 * @see PatientHistoryRegularDto
 * @see PatientHistoryTextDto
 * @see PatientHistoryUrlDto
 * @see PatientHistoryTrackingDto
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "historyType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PatientHistoryRegularDto.class, name = "REGULAR"),
    @JsonSubTypes.Type(value = PatientHistoryTextDto.class, name = "FREE_TEXT"),
    @JsonSubTypes.Type(value = PatientHistoryUrlDto.class, name = "URL"),
    @JsonSubTypes.Type(value = PatientHistoryTrackingDto.class, name = "TRACKING"),
})

@JsonSeeAlso({
    PatientHistoryRegularDto.class,
    PatientHistoryTrackingDto.class,
    PatientHistoryTextDto.class,
    PatientHistoryUrlDto.class
})

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Abstract model class of patient history item")
public abstract class AbstractPatientHistoryItemDto implements java.io.Serializable {

  @JsonProperty("id")
  @Schema(description = "Id of this history item", example = "2")
  private int id;

  @JsonProperty("patientId")
  @Schema(description = "Patient id of this history item", example = "23680")
  private int patientId;

  @JsonProperty("maskId")
  @Schema(description = "Id of the mask", example = "5")
  private Integer maskId;

  /**
   * The unique id of this patient history item.
   *
   * @documentationExample 2
   *
   * @return Unique id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The patient id of this patient history item.
   *
   * @documentationExample 23680
   *
   * @return Patient id
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The ID of the mask associated with patient history item.
   *
   * @documentationExample 11
   *
   * @return Mask ID of the patient history item.
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbstractPatientHistoryItemDto that = (AbstractPatientHistoryItemDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    return getMaskId() != null ? getMaskId().equals(that.getMaskId()) : that.getMaskId() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPatientId();
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    return result;
  }
}
