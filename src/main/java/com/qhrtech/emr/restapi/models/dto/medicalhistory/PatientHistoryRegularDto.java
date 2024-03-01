
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import org.joda.time.LocalDateTime;

/**
 * The PatientHistoryRegular data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryRegular
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientHistoryRegular data transfer object model")
public class PatientHistoryRegularDto extends AbstractPatientHistoryItemDto
    implements Serializable {

  @JsonProperty("historyRegularItem")
  @Schema(description = "THe history regular item")
  private HistoryRegularItemDto historyRegularItem;

  @JsonProperty("date")
  @Schema(description = "The history date", example = "2018-09-11", type = "string")
  private AccuroCalendar date;

  @JsonProperty("active")
  @Schema(description = "Flag indicating if this record is active", example = "true")
  private boolean active;

  @JsonProperty("note")
  @Schema(description = "The note", example = "high blood pressure")
  private String note;

  @JsonProperty("negative")
  @Schema(description = "Flag indicating if it is negative", example = "true")
  private boolean negative;

  @JsonProperty("location")
  @Schema(description = "The eye location")
  private EyeCode location;

  @JsonProperty("relation")
  @Schema(description = "The relation", example = "Mother")
  private String relation;

  @JsonProperty("createdDate")
  @Schema(description = "The recorded date of this patient history item",
      example = "2018-07-13T00:00:00.000", type = "string")
  private LocalDateTime createdDate;

  @JsonProperty("details")
  @Schema(description = "The details of the item", example = "Non-Smoker")
  private String details;

  @JsonProperty("treatment")
  @Schema(description = "The treatment", example = "ETC")
  private String treatment;

  @JsonProperty("creatorUserId")
  @Schema(description = "The creator user id", example = "1")
  private int creatorUserId;

  @JsonProperty("resolvedDate")
  @Schema(description = "The date when resolved", example = "2018-09-11", type = "string")
  private AccuroCalendar resolvedDate;

  @JsonProperty("ageOfOnset")
  @Schema(description = "The age of onset", example = "Diagnosed in her 40s")
  private String ageOfOnset;

  @JsonProperty("lifeStage")
  @Schema(description = "The life stage", example = "Adult: 18 years or older")
  private LifeStageType lifeStage;

  @JsonProperty("historyClass")
  @Schema(description = "The history classification", example = "Surgical/Medical")
  private String historyClass;

  /**
   * History Regular Item data transfer object.
   * <p>
   * See {@link HistoryRegularItemDto}
   *
   * @return History Regular Item data transfer object.
   */
  public HistoryRegularItemDto getHistoryRegularItem() {
    return historyRegularItem;
  }

  public void setHistoryRegularItem(HistoryRegularItemDto historyRegularItemDto) {
    this.historyRegularItem = historyRegularItemDto;
  }

  /**
   * The history date.
   *
   * @return History date
   */
  @DocumentationExample("2018-09-11")
  @TypeHint(String.class)
  public AccuroCalendar getDate() {
    return date;
  }

  public void setDate(AccuroCalendar date) {
    this.date = date;
  }

  /**
   * Indicates whether or not this record is active.
   *
   * @documentationExample true
   *
   * @return Current state
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * The note.
   *
   * @documentationExample high blood pressure
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
   * A flag if it is negative.
   *
   * @documentationExample true
   *
   * @return {@code true} if negative or {@code false} if not.
   */
  public boolean isNegative() {
    return negative;
  }

  public void setNegative(boolean negative) {
    this.negative = negative;
  }

  /**
   * The eye location.
   *
   * See {@link EyeCode}
   *
   * @return Eye location.
   */
  public EyeCode getLocation() {
    return location;
  }

  public void setLocation(EyeCode location) {
    this.location = location;
  }

  /**
   * The relation.
   *
   * @documentationExample Brother
   *
   * @return The relation.
   */
  public String getRelation() {
    return relation;
  }

  public void setRelation(String relation) {
    this.relation = relation;
  }

  /**
   * The recorded date for this patient history item record.
   *
   * @return Created date time.
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * The details of the item.
   *
   * @documentationExample father and brother polyps
   *
   * @return Details.
   */
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  /**
   * The treatment.
   *
   * @documentationExample ECT
   *
   * @return The treatment.
   */
  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  /**
   * The creator user id.
   *
   * @documentationExample 30
   *
   * @return User id.
   */
  public int getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(int creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  /**
   * The date resolved.
   *
   * @return Date.
   */
  @DocumentationExample("2018-09-11")
  @TypeHint(String.class)
  public AccuroCalendar getResolvedDate() {
    return resolvedDate;
  }

  public void setResolvedDate(AccuroCalendar resolvedDate) {
    this.resolvedDate = resolvedDate;
  }

  /**
   * The age of onset.
   *
   * @documentationExample 25 years
   *
   * @return Age.
   */
  public String getAgeOfOnset() {
    return ageOfOnset;
  }

  public void setAgeOfOnset(String ageOfOnset) {
    this.ageOfOnset = ageOfOnset;
  }

  /**
   * The life stage.
   *
   * @documentationExample Adult: 18 years or older
   *
   * @return Life stage.
   */
  public LifeStageType getLifeStage() {
    return lifeStage;
  }

  public void setLifeStage(LifeStageType lifeStage) {
    this.lifeStage = lifeStage;
  }

  /**
   * The history classification.
   *
   * @documentationExample Surgical/Medical
   *
   * @return History classification.
   */
  public String getHistoryClass() {
    return historyClass;
  }

  public void setHistoryClass(String historyClass) {
    this.historyClass = historyClass;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.historyRegularItem);
    hash = 29 * hash + Objects.hashCode(this.date);
    hash = 29 * hash + (this.active ? 1 : 0);
    hash = 29 * hash + Objects.hashCode(this.note);
    hash = 29 * hash + (this.negative ? 1 : 0);
    hash = 29 * hash + Objects.hashCode(this.location);
    hash = 29 * hash + Objects.hashCode(this.relation);
    hash = 29 * hash + Objects.hashCode(this.createdDate);
    hash = 29 * hash + Objects.hashCode(this.details);
    hash = 29 * hash + Objects.hashCode(this.treatment);
    hash = 29 * hash + this.creatorUserId;
    hash = 29 * hash + Objects.hashCode(this.resolvedDate);
    hash = 29 * hash + Objects.hashCode(this.ageOfOnset);
    hash = 29 * hash + Objects.hashCode(this.lifeStage);
    hash = 29 * hash + Objects.hashCode(this.historyClass);
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
    final PatientHistoryRegularDto other = (PatientHistoryRegularDto) obj;
    if (this.active != other.active) {
      return false;
    }
    if (this.negative != other.negative) {
      return false;
    }
    if (this.creatorUserId != other.creatorUserId) {
      return false;
    }
    if (!Objects.equals(this.note, other.note)) {
      return false;
    }
    if (!Objects.equals(this.relation, other.relation)) {
      return false;
    }
    if (!Objects.equals(this.details, other.details)) {
      return false;
    }
    if (!Objects.equals(this.treatment, other.treatment)) {
      return false;
    }
    if (!Objects.equals(this.ageOfOnset, other.ageOfOnset)) {
      return false;
    }
    if (!Objects.equals(this.lifeStage, other.lifeStage)) {
      return false;
    }
    if (!Objects.equals(this.historyClass, other.historyClass)) {
      return false;
    }
    if (!Objects.equals(this.historyRegularItem, other.historyRegularItem)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (this.location != other.location) {
      return false;
    }
    if (!Objects.equals(this.createdDate, other.createdDate)) {
      return false;
    }
    return Objects.equals(this.resolvedDate, other.resolvedDate);
  }

}
