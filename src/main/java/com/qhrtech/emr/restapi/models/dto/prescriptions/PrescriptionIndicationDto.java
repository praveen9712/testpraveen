
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "PrescriptionIndication data transfer object model")
public class PrescriptionIndicationDto {

  @JsonProperty("indicationId")
  @Schema(description = "The indication id of an indication for the prescription", example = "1")
  private String indicationId;

  @JsonProperty("indicationName")
  @Schema(description = "The indication name", example = "Crushing injury of external genitalia")
  private String indicationName;

  @JsonProperty("indicationNote")
  @Schema(description = "The indication note", example = "SAMPLE NOTE")
  private String indicationNote;

  @JsonProperty("type")
  @Schema(description = "The indication type", example = "Legacy")
  private String type;

  /**
   * An indication id of an indication for a prescription.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getIndicationId() {
    return indicationId;
  }

  public void setIndicationId(String indicationId) {
    this.indicationId = indicationId;
  }

  /**
   * An indication name.
   *
   * @documentationExample Crushing injury of external genitalia
   *
   * @return
   */
  public String getIndicationName() {
    return indicationName;
  }

  public void setIndicationName(String indicationName) {
    this.indicationName = indicationName;
  }

  /**
   * An indication note.
   *
   * @documentationExample SAMPLE NOTE
   *
   * @return
   */
  public String getIndicationNote() {
    return indicationNote;
  }

  public void setIndicationNote(String indicationNote) {
    this.indicationNote = indicationNote;
  }

  /**
   * A indication type.
   *
   * @documentationExample Legacy
   *
   * @return
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PrescriptionIndicationDto)) {
      return false;
    }

    PrescriptionIndicationDto that = (PrescriptionIndicationDto) o;

    if (getIndicationId() != null ? !getIndicationId().equals(that.getIndicationId())
        : that.getIndicationId() != null) {
      return false;
    }
    if (getIndicationName() != null ? !getIndicationName().equals(that.getIndicationName())
        : that.getIndicationName() != null) {
      return false;
    }
    if (getIndicationNote() != null ? !getIndicationNote().equals(that.getIndicationNote())
        : that.getIndicationNote() != null) {
      return false;
    }
    return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
  }

  @Override
  public int hashCode() {
    int result = getIndicationId() != null ? getIndicationId().hashCode() : 0;
    result = 31 * result + (getIndicationName() != null ? getIndicationName().hashCode() : 0);
    result = 31 * result + (getIndicationNote() != null ? getIndicationNote().hashCode() : 0);
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PrescriptionIndicationDto{"
        + "indicationId='" + indicationId + '\''
        + ", indicationName='" + indicationName + '\''
        + ", indicationNote='" + indicationNote + '\''
        + ", type='" + type + '\''
        + '}';
  }
}
