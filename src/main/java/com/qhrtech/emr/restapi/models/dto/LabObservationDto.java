
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckBigDecimal;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Objects;
import org.joda.time.LocalDateTime;

/**
 * <p>
 * The Lab Observation model
 * </p>
 * <p>
 * A Lab Observation represents an instance of a {@code LabResult}
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab observation data transfer object model")
public class LabObservationDto {

  @JsonProperty("observationId")
  @Schema(description = "Unique lab observation ID", example = "1")
  private int observationId;

  @JsonProperty("observationDate")
  @Schema(description = "Lab observation date", example = "1999-12-11T18:11:25.340")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime observationDate;

  @JsonProperty("observationNote")
  @Schema(description = "Lab observation note", example = "Example note")
  private String observationNote;

  @JsonProperty("observationFlag")
  @Schema(
      description = "Lab observation flag. See the lab_abnormal_flags "
          + "table for more information on lab observation flags.",
      example = "L")
  private String observationFlag;

  @JsonProperty("observationValue")
  @Schema(
      description = "String value of the lab observation.\n\nThis value is always set, regardless "
          + "of the data type of the observation.",
      example = "13.71")
  private String observationValue;

  @JsonProperty("observationNumber")
  @Schema(
      description = "Floating point value of the lab observation\n\nThis value is only set "
          + "when the data type of the observation is numeric.",
      example = "13.70999")
  @CheckBigDecimal
  private BigDecimal observationNumber;

  @JsonProperty("observationUnits")
  @Schema(description = "Unit of measurement for the lab observation", example = "mg/L")
  private String observationUnits;

  @JsonProperty("label")
  @Schema(
      description = "The lab observation label.\n\nTypically the same as the name of the result "
          + "the observation is associated with.",
      example = "ACETAMINOPHEN (serum)")
  private String label;

  @JsonProperty("resultId")
  @Schema(description = "Unique ID for the lab result associated with the lab observation.",
      example = "1")
  private String resultId;

  @JsonProperty("observationRange")
  @Schema(description = "The reference range for the lab observation.", example = "10 - 25")
  private String observationRange;

  /**
   * Unique Lab Observation ID
   *
   * @documentationExample 1
   *
   * @return Observation ID
   */
  public int getObservationId() {
    return observationId;
  }

  public void setObservationId(int observationId) {
    this.observationId = observationId;
  }

  /**
   * Lab observation date
   *
   * @return Datetime
   */
  @DocumentationExample("1999-12-11T18:11:25.343-0800")
  @TypeHint(String.class)
  public LocalDateTime getObservationDate() {
    return observationDate;
  }

  public void setObservationDate(LocalDateTime observationDate) {
    this.observationDate = observationDate;
  }

  /**
   * Lab observation note
   *
   * @documentationExample Example note
   *
   * @return A note String
   */
  public String getObservationNote() {
    return observationNote;
  }

  public void setObservationNote(String observationNote) {
    this.observationNote = observationNote;
  }

  /**
   * Lab observation flag. See the <code>lab_abnormal_flags</code> table for more information on lab
   * observation flags.
   *
   * @documentationExample L
   *
   * @return Lab observation flag
   */
  public String getObservationFlag() {
    return observationFlag;
  }

  public void setObservationFlag(String observationFlag) {
    this.observationFlag = observationFlag;
  }

  /**
   * <p>
   * String Value of the Lab Observation.
   * </p>
   * <p>
   * This value is always set, regardless of the data type of the observation.
   * </p>
   *
   * @documentationExample 13.71
   *
   * @return A value String
   */
  public String getObservationValue() {
    return observationValue;
  }

  public void setObservationValue(String observationValue) {
    this.observationValue = observationValue;
  }

  /**
   * <p>
   * Floating Point Value of the Lab Observation
   * </p>
   * <p>
   * This value is only set when the data type of the observation is numeric
   * </p>
   *
   * @documentationExample 13.70999
   *
   * @return Observation number
   */
  public BigDecimal getObservationNumber() {
    return observationNumber;
  }

  public void setObservationNumber(BigDecimal observationNumber) {
    this.observationNumber = observationNumber;
  }

  /**
   * Unit of measurement for the lab observation
   *
   * @documentationExample mg/L
   *
   * @return Unit of measurement
   */
  public String getObservationUnits() {
    return observationUnits;
  }

  public void setObservationUnits(String observationUnits) {
    this.observationUnits = observationUnits;
  }

  /**
   * <p>
   * A lab observation label.
   * </p>
   * <p>
   * Typically the same as the name of the result the observation is associated with.
   * </p>
   *
   * @documentationExample ACETAMINOPHEN (serum)
   *
   * @return A label String
   */
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Unique ID for the Lab Result associated with the lab observation.
   *
   * @documentationExample 1
   *
   * @return Lab Result ID
   */
  public String getResultId() {
    return resultId;
  }

  public void setResultId(String resultId) {
    this.resultId = resultId;
  }

  /**
   * The Reference Range for the Lab Observation.
   *
   * @documentationExample 10 - 25
   *
   * @return A range String
   */
  public String getObservationRange() {
    return observationRange;
  }

  public void setObservationRange(String observationRange) {
    this.observationRange = observationRange;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + this.observationId;
    hash = 89 * hash + Objects.hashCode(this.observationDate);
    hash = 89 * hash + Objects.hashCode(this.observationNote);
    hash = 89 * hash + Objects.hashCode(this.observationFlag);
    hash = 89 * hash + Objects.hashCode(this.observationValue);
    hash = 89 * hash + Objects.hashCode(this.observationNumber);
    hash = 89 * hash + Objects.hashCode(this.observationUnits);
    hash = 89 * hash + Objects.hashCode(this.label);
    hash = 89 * hash + Objects.hashCode(this.resultId);
    hash = 89 * hash + Objects.hashCode(this.observationRange);
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
    final LabObservationDto other = (LabObservationDto) obj;
    if (this.observationId != other.observationId) {
      return false;
    }
    if (!Objects.equals(this.observationNote, other.observationNote)) {
      return false;
    }
    if (!Objects.equals(this.observationFlag, other.observationFlag)) {
      return false;
    }
    if (!Objects.equals(this.observationValue, other.observationValue)) {
      return false;
    }
    if (!Objects.equals(this.observationNumber, other.observationNumber)) {
      return false;
    }
    if (!Objects.equals(this.observationUnits, other.observationUnits)) {
      return false;
    }
    if (!Objects.equals(this.label, other.label)) {
      return false;
    }
    if (!Objects.equals(this.resultId, other.resultId)) {
      return false;
    }
    if (!Objects.equals(this.observationRange, other.observationRange)) {
      return false;
    }
    if (!Objects.equals(this.observationDate, other.observationDate)) {
      return false;
    }
    return true;
  }

}
