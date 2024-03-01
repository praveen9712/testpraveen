
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@Schema(description = "Dosage data transfer object model")
public class DosageDto {

  @JsonProperty("id")
  @Schema(description = "The dosage unique id", example = "1")
  private int id;

  @JsonProperty("min")
  @Schema(description = "The minimum dosage", example = "1")
  private Float min;

  @JsonProperty("max")
  @Schema(description = "The maximum dosage", example = "1")
  private Float max;

  @JsonProperty("unit")
  @Schema(description = "The dosage unit", example = "TAB")
  private String unit;

  @JsonProperty("durationAmount")
  @Schema(description = "The duration amount", example = "1")
  private Float durationAmount;

  @JsonProperty("durationUnit")
  @Schema(description = "The duration unit", example = "Day")
  private String durationUnit;

  @JsonProperty("intervalTime")
  @Schema(description = "The interval time", example = "QD")
  private String intervalTime;

  @JsonProperty("startDate")
  @Schema(description = "The start date of the dosage", type = "string",
      example = "2018-07-13T00:00:00.000")
  private LocalDateTime startDate;

  @JsonProperty("proReNata")
  @Schema(description = "The flag indicating if the dosage is Pro re nata", example = "true")
  private boolean proReNata;

  @JsonProperty("concurrent")
  @Schema(description = "The flag indicating if the dosage is concurrent", example = "true")
  private boolean concurrent;

  @JsonProperty("eyeCode")
  @Schema(description = "The eye code of the dosage", example = "OS")
  private EyeCode eyeCode;

  /**
   * The dosage id
   *
   * @documentationExample 1
   *
   * @return id of the dosage
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * A minimum dosage.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Float getMin() {
    return min;
  }

  public void setMin(Float min) {
    this.min = min;
  }

  /**
   * A maximum dosage.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Float getMax() {
    return max;
  }

  public void setMax(Float max) {
    this.max = max;
  }

  /**
   * A dosage unit.
   *
   * @documentationExample EA,PKG
   *
   * @return
   */
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * A duration amount.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Float getDurationAmount() {
    return durationAmount;
  }

  public void setDurationAmount(Float durationAmount) {
    this.durationAmount = durationAmount;
  }

  /**
   * A duration unit.
   *
   * @documentationExample Day
   *
   * @return
   */
  public String getDurationUnit() {
    return durationUnit;
  }

  public void setDurationUnit(String durationUnit) {
    this.durationUnit = durationUnit;
  }

  /**
   * An interval time.
   *
   * @documentationExample QD,BID,TID
   *
   * @return
   */
  public String getIntervalTime() {
    return intervalTime;
  }

  public void setIntervalTime(String intervalTime) {
    this.intervalTime = intervalTime;
  }

  /**
   * A start date of a dosage.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  /**
   * A flag if a dosage is Pro re nata.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isProReNata() {
    return proReNata;
  }

  public void setProReNata(boolean proReNata) {
    this.proReNata = proReNata;
  }

  /**
   * A flag if a dosage is concurrent.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isConcurrent() {
    return concurrent;
  }

  public void setConcurrent(boolean concurrent) {
    this.concurrent = concurrent;
  }

  /**
   * An eye code of a dosage.
   *
   * @documentationExample OS
   *
   * @return
   */
  public EyeCode getEyeCode() {
    return eyeCode;
  }

  public void setEyeCode(EyeCode eyeCode) {
    this.eyeCode = eyeCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DosageDto dosageDto = (DosageDto) o;

    if (getId() != dosageDto.getId()) {
      return false;
    }
    if (isProReNata() != dosageDto.isProReNata()) {
      return false;
    }
    if (isConcurrent() != dosageDto.isConcurrent()) {
      return false;
    }
    if (getMin() != null ? !getMin().equals(dosageDto.getMin()) : dosageDto.getMin() != null) {
      return false;
    }
    if (getMax() != null ? !getMax().equals(dosageDto.getMax()) : dosageDto.getMax() != null) {
      return false;
    }
    if (getUnit() != null ? !getUnit().equals(dosageDto.getUnit()) : dosageDto.getUnit() != null) {
      return false;
    }
    if (getDurationAmount() != null ? !getDurationAmount().equals(dosageDto.getDurationAmount())
        : dosageDto.getDurationAmount() != null) {
      return false;
    }
    if (getDurationUnit() != null ? !getDurationUnit().equals(dosageDto.getDurationUnit())
        : dosageDto.getDurationUnit() != null) {
      return false;
    }
    if (getIntervalTime() != null ? !getIntervalTime().equals(dosageDto.getIntervalTime())
        : dosageDto.getIntervalTime() != null) {
      return false;
    }
    if (getStartDate() != null ? !getStartDate().equals(dosageDto.getStartDate())
        : dosageDto.getStartDate() != null) {
      return false;
    }
    return getEyeCode() == dosageDto.getEyeCode();
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getMin() != null ? getMin().hashCode() : 0);
    result = 31 * result + (getMax() != null ? getMax().hashCode() : 0);
    result = 31 * result + (getUnit() != null ? getUnit().hashCode() : 0);
    result = 31 * result + (getDurationAmount() != null ? getDurationAmount().hashCode() : 0);
    result = 31 * result + (getDurationUnit() != null ? getDurationUnit().hashCode() : 0);
    result = 31 * result + (getIntervalTime() != null ? getIntervalTime().hashCode() : 0);
    result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
    result = 31 * result + (isProReNata() ? 1 : 0);
    result = 31 * result + (isConcurrent() ? 1 : 0);
    result = 31 * result + (getEyeCode() != null ? getEyeCode().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DosageDto{"
        + "id=" + id
        + ", min=" + min
        + ", max=" + max
        + ", unit='" + unit + '\''
        + ", durationAmount=" + durationAmount
        + ", durationUnit='" + durationUnit + '\''
        + ", intervalTime='" + intervalTime + '\''
        + ", startDate=" + startDate
        + ", proReNata=" + proReNata
        + ", concurrent=" + concurrent
        + ", eyeCode=" + eyeCode
        + '}';
  }
}
