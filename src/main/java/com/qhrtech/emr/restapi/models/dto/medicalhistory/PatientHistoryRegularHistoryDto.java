
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.joda.time.LocalDate;

/**
 * Patient history regular history data transfer object extending
 * {@link AbstractPatientHistoryItemHistoryDto}.
 *
 * @See {@link com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegularHistory}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient history regular history data transfer object")
public class PatientHistoryRegularHistoryDto extends AbstractPatientHistoryItemHistoryDto implements
    Serializable {

  @JsonProperty("historyRegularId")
  @Schema(
      description = "The id of the history regular item this patient history regular record "
          + "refers to.",
      example = "1")
  private int historyRegularId;

  @JsonProperty("patientHistoryRegularId")
  @Schema(description = "The id of the patient history regular record", example = "1")
  private int patientHistoryRegularId;

  @JsonProperty("creatorUserId")
  @Schema(description = "The creator user id", example = "1")
  private Integer creatorUserId;

  @JsonProperty("historyUserId")
  @Schema(description = "The id of the Accuro user who updated the patient history regular record",
      example = "1")
  private Integer historyUserId;

  @JsonProperty("date")
  @Schema(
      type = "string",
      description = "The date of this patient history regular history record",
      example = "2018-09-11")
  private AccuroCalendar date;

  @JsonProperty("resolvedDate")
  @Schema(
      type = "string",
      description = "The date when resolved",
      example = "2018-09-11")
  private AccuroCalendar resolvedDate;

  @JsonProperty("createdDate")
  @Schema(description = "The date this patient history regular was created at the very first time",
      example = "2018-07-13T00:00:00.000")
  private LocalDate createdDate;

  @JsonProperty("relation")
  @Schema(description = "The relation", example = "Mother")
  private String relation;

  @JsonProperty("treatment")
  @Schema(description = "The treatment", example = "ETC")
  private String treatment;

  @JsonProperty("note")
  @Schema(description = "The note", example = "high blood pressure")
  private String note;

  @JsonProperty("details")
  @Schema(description = "The details of the item", example = "Non-Smoker")
  private String details;

  @JsonProperty("historyClass")
  @Schema(description = "The history classification", example = "Surgical/Medical")
  private String historyClass;

  @JsonProperty("ageOfOnset")
  @Schema(description = "The age of onset", example = "Diagnosed in her 40s")
  private String ageOfOnset;

  @JsonProperty("location")
  @Schema(description = "The eye location")
  private String location;

  @JsonProperty("lifeStage")
  @Schema(description = "The life stage", example = "Adult: 18 years or older")
  private LifeStageType lifeStage;

  @JsonProperty("active")
  @Schema(description = "Flag indicating if this record is active", example = "true")
  private boolean active;

  @JsonProperty("negative")
  @Schema(description = "Flag indicating if it is negative", example = "true")
  private boolean negative;

  /**
   * The id of the history regular item this patient history regular records are associated to.
   *
   * @documentationExample 1
   *
   * @return The history regular item id.
   */
  public int getHistoryRegularId() {
    return historyRegularId;
  }

  public void setHistoryRegularId(int historyRegularId) {
    this.historyRegularId = historyRegularId;
  }

  /**
   * The unique id of the patient history regular record.
   *
   * @documentationExample 1
   *
   * @return The patient history regular id.
   */
  public int getPatientHistoryRegularId() {
    return patientHistoryRegularId;
  }

  public void setPatientHistoryRegularId(int patientHistoryRegularId) {
    this.patientHistoryRegularId = patientHistoryRegularId;
  }

  /**
   * The id of the Accuro user who created the patient history regular record at the very first
   * time.
   *
   * @documentationExample 1
   *
   * @return The user id of the creator.
   */
  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * The id of the Accuro user who updated the patient history regular record.
   *
   * @documentationExample 1
   *
   * @return The user id of the updater.
   */
  public Integer getHistoryUserId() {
    return historyUserId;
  }

  public void setHistoryUserId(Integer historyUserId) {
    this.historyUserId = historyUserId;
  }

  /**
   * The recorded date for this patient history regular history record.
   *
   * @return The date of this patient history regular history record.
   */
  @DocumentationExample("2018-09-06")
  @TypeHint(String.class)
  public AccuroCalendar getDate() {
    return date;
  }

  public void setDate(AccuroCalendar date) {
    this.date = date;
  }

  /**
   * The date the patient history regular was resolved.
   *
   * @return The date the patient history regular was resolved.
   */
  @DocumentationExample("2018-09-06")
  @TypeHint(String.class)
  public AccuroCalendar getResolvedDate() {
    return resolvedDate;
  }

  public void setResolvedDate(AccuroCalendar resolvedDate) {
    this.resolvedDate = resolvedDate;
  }

  /**
   * The date this patient history regular was created at the very first time.
   *
   * @return The date this patient history regular was created.
   */
  @DocumentationExample("2018-07-13")
  @TypeHint(String.class)
  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * The relation associated with the patient history regular.
   *
   * @documentationExample Brother
   *
   * @return The relation associated with this patient history regular.
   */
  public String getRelation() {
    return relation;
  }

  public void setRelation(String relation) {
    this.relation = relation;
  }

  /**
   * The treatment contains free text from Accuro.
   *
   * @documentationExample Currently not undergoing treatment.
   *
   * @return The treatment field text.
   */
  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  /**
   * The free text note on the patient history regular record.
   *
   * @documentationExample Patient was a little unsure of the date on this event.
   *
   * @return The note on the patient history regular.
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * The details contains manageable text from Accuro.
   *
   * @documentationExample Stopped Smoking
   *
   * @return The selected details text.
   */
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  /**
   * The history classification.
   *
   * @documentationExample Surgical
   *
   * @return History classification.
   */
  public String getHistoryClass() {
    return historyClass;
  }

  public void setHistoryClass(String historyClass) {
    this.historyClass = historyClass;
  }

  /**
   * The free text field in Accuro for describing age of onset.
   *
   * @documentationExample Started in early 30s
   *
   * @return The age of onset.
   */
  public String getAgeOfOnset() {
    return ageOfOnset;
  }

  public void setAgeOfOnset(String ageOfOnset) {
    this.ageOfOnset = ageOfOnset;
  }

  /**
   * The eye location for regular history items.
   * <p>
   * Note, these are serialized as objects, with 2 string fields: name, abbreviation
   *
   * @documentationExample LEFT_EYE, OS
   *
   * @return Location of occular history item records.
   */
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * The life stage of the patient history item
   * <p>
   * Note, these are serialized as objects, with 2 fields: name, abbreviation.
   * <p>
   * Our documentation example is unable to display this.
   *
   * @documentationExample Newborn, Newborn: Birth - 28 days
   *
   * @return The life stage of the patient history regular
   */
  public LifeStageType getLifeStage() {
    return lifeStage;
  }

  public void setLifeStage(LifeStageType lifeStage) {
    this.lifeStage = lifeStage;
  }

  /**
   * Whether this record is active as marked by Accuro.
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
   * The patient history regular in Accuro can be marked as negative, this field stores that value.
   *
   * @documentationExample false
   *
   * @return If this record has been marked as negative.
   */
  public boolean isNegative() {
    return negative;
  }

  public void setNegative(boolean negative) {
    this.negative = negative;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientHistoryRegularHistoryDto)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PatientHistoryRegularHistoryDto that = (PatientHistoryRegularHistoryDto) o;

    if (getHistoryRegularId() != that.getHistoryRegularId()) {
      return false;
    }
    if (getPatientHistoryRegularId() != that.getPatientHistoryRegularId()) {
      return false;
    }
    if (isActive() != that.isActive()) {
      return false;
    }
    if (isNegative() != that.isNegative()) {
      return false;
    }
    if (getCreatorUserId() != null ? !getCreatorUserId().equals(that.getCreatorUserId())
        : that.getCreatorUserId() != null) {
      return false;
    }
    if (getHistoryUserId() != null ? !getHistoryUserId().equals(that.getHistoryUserId())
        : that.getHistoryUserId() != null) {
      return false;
    }
    if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) {
      return false;
    }
    if (getResolvedDate() != null ? !getResolvedDate().equals(that.getResolvedDate())
        : that.getResolvedDate() != null) {
      return false;
    }
    if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate())
        : that.getCreatedDate() != null) {
      return false;
    }
    if (getRelation() != null ? !getRelation().equals(that.getRelation())
        : that.getRelation() != null) {
      return false;
    }
    if (getTreatment() != null ? !getTreatment().equals(that.getTreatment())
        : that.getTreatment() != null) {
      return false;
    }
    if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null) {
      return false;
    }
    if (getDetails() != null ? !getDetails().equals(that.getDetails())
        : that.getDetails() != null) {
      return false;
    }
    if (getHistoryClass() != null ? !getHistoryClass().equals(that.getHistoryClass())
        : that.getHistoryClass() != null) {
      return false;
    }
    if (getAgeOfOnset() != null ? !getAgeOfOnset().equals(that.getAgeOfOnset())
        : that.getAgeOfOnset() != null) {
      return false;
    }
    if (getLocation() != null ? !getLocation().equals(that.getLocation())
        : that.getLocation() != null) {
      return false;
    }
    return getLifeStage() == that.getLifeStage();
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getHistoryRegularId();
    result = 31 * result + getPatientHistoryRegularId();
    result = 31 * result + (getCreatorUserId() != null ? getCreatorUserId().hashCode() : 0);
    result = 31 * result + (getHistoryUserId() != null ? getHistoryUserId().hashCode() : 0);
    result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
    result = 31 * result + (getResolvedDate() != null ? getResolvedDate().hashCode() : 0);
    result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
    result = 31 * result + (getRelation() != null ? getRelation().hashCode() : 0);
    result = 31 * result + (getTreatment() != null ? getTreatment().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (getDetails() != null ? getDetails().hashCode() : 0);
    result = 31 * result + (getHistoryClass() != null ? getHistoryClass().hashCode() : 0);
    result = 31 * result + (getAgeOfOnset() != null ? getAgeOfOnset().hashCode() : 0);
    result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
    result = 31 * result + (getLifeStage() != null ? getLifeStage().hashCode() : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result = 31 * result + (isNegative() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb =
        new StringBuilder("PatientHistoryRegularHistoryDto{");
    sb.append(super.toString());
    sb.append(", historyRegularId=").append(historyRegularId);
    sb.append(", patientHistoryRegularId=").append(patientHistoryRegularId);
    sb.append(", creatorUserId=").append(creatorUserId);
    sb.append(", historyUserId=").append(historyUserId);
    sb.append(", date=").append(date);
    sb.append(", resolvedDate=").append(resolvedDate);
    sb.append(", createdDate=").append(createdDate);
    sb.append(", relation='").append(relation).append('\'');
    sb.append(", treatment='").append(treatment).append('\'');
    sb.append(", note='").append(note).append('\'');
    sb.append(", details='").append(details).append('\'');
    sb.append(", historyClass='").append(historyClass).append('\'');
    sb.append(", ageOfOnset='").append(ageOfOnset).append('\'');
    sb.append(", location='").append(location).append('\'');
    sb.append(", lifeStage=").append(lifeStage);
    sb.append(", active=").append(active);
    sb.append(", negative=").append(negative);
    sb.append('}');
    return sb.toString();
  }
}
