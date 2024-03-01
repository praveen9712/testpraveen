
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class RenewalRequestDosageDto {

  @JsonProperty("id")
  @Schema(description = "The identity", example = "10")
  private int id;

  @JsonProperty("renewalRequestId")
  @Schema(description = "Renewal request id", example = "10")
  private int renewalRequestId;

  @JsonProperty("sequence")
  @Schema(description = "Sequence", example = "10")
  private Integer sequence;

  @JsonProperty("instructionRelationship")
  @Schema(description = "Instruction relationship", example = "Direct")
  @Size(max = 5, message = "Maximum length allowed is 5 characters.")
  private String instructionRelationship;

  @JsonProperty("instructions")
  @Schema(description = "Instructions on the dosage", example = "Daily 2 times")
  @Size(max = 1000, message = "Maximum length allowed is 1000 characters.")
  private String instructions;

  @JsonProperty("additionalInstructions")
  @Schema(description = "Additional instructions", example = "Follow up")
  @Size(max = 1000, message = "Maximum length allowed is 1000 characters.")
  private String additionalInstructions;

  @JsonProperty("timing")
  @Schema(description = "Timing", example = "2020-02-10T00:00:00.000")
  private LocalDateTime timing;

  @JsonProperty("timingRepeatBoundsQuantityValue")
  @Schema(description = "Timing repeat bounds quantity value", example = "1")
  private Integer timingRepeatBoundsQuantityValue;

  @JsonProperty("timingRepeatBoundsQuantityUnit")
  @Schema(description = "Timing repeat bounds quantity unit", example = "Unit name")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatBoundsQuantityUnit;

  @JsonProperty("timingRepeatBoundsQuantitySystem")
  @Schema(description = "Timing repeat bounds quantity system", example = "System name")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatBoundsQuantitySystem;

  @JsonProperty("timingRepeatBoundsRangeHigh")
  @Schema(description = "Timing repeat bounds range high ", example = "100")
  private Integer timingRepeatBoundsRangeHigh;

  @JsonProperty("timingRepeatBoundsRangeLow")
  @Schema(description = "Timing repeat bounds range low ", example = "10")
  private Integer timingRepeatBoundsRangeLow;

  @JsonProperty("timingRepeatBoundsRangeUnit")
  @Schema(description = "Timing repeat bounds range unit", example = "Unit Name")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatBoundsRangeUnit;

  @JsonProperty("timingRepeatBoundsRangeSystem")
  @Schema(description = "Timing repeat bounds range system", example = "System name")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatBoundsRangeSystem;

  @JsonProperty("timingRepeatBoundsPeriodStart")
  @Schema(description = "Timing repeat bounds period start", example = "2020-02-10T00:00:00.000")
  private LocalDateTime timingRepeatBoundsPeriodStart;

  @JsonProperty("timingRepeatBoundsPeriodEnd")
  @Schema(description = "Timing repeat bounds period end", example = "2020-02-10T00:00:00.000")
  private LocalDateTime timingRepeatBoundsPeriodEnd;

  @JsonProperty("timingRepeatDoseCount")
  @Schema(description = "Timing repeat dose count", example = "10")
  private Integer timingRepeatDoseCount;

  @JsonProperty("timingRepeatDoseCountMax")
  @Schema(description = "Timing repeat dose count max", example = "10")
  private Integer timingRepeatDoseCountMax;

  @JsonProperty("timingRepeatDuration")
  @Schema(description = "Timing repeat duration", example = "10")
  private Integer timingRepeatDuration;

  @JsonProperty("timingRepeatDurationMax")
  @Schema(description = "Timing repeat duration max", example = "10")
  private Integer timingRepeatDurationMax;

  @JsonProperty("timingRepeatDurationUnit")
  @Schema(description = "Timing repeat duration unit", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatDurationUnit;

  @JsonProperty("timingRepeatFrequency")
  @Schema(description = "Timing repeat frequency", example = "10")
  private Integer timingRepeatFrequency;

  @JsonProperty("timingRepeatFrequencyMax")
  @Schema(description = "Timing repeat frequency max", example = "10")
  private Integer timingRepeatFrequencyMax;

  @JsonProperty("timingRepeatPeriod")
  @Schema(description = "Timing repeat period", example = "10")
  private Integer timingRepeatPeriod;

  @JsonProperty("timingRepeatPeriodMax")
  @Schema(description = "Timing repeat period max", example = "10")
  private Integer timingRepeatPeriodMax;

  @JsonProperty("timingRepeatPeriodUnit")
  @Schema(description = "Timing repeat period unit", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String timingRepeatPeriodUnit;

  @JsonProperty("asNeeded")
  @Schema(description = "Is needed", example = "True")
  private Boolean asNeeded;

  @JsonProperty("route")
  @Schema(description = "Route", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String route;

  @JsonProperty("doseQuantityValue")
  @Schema(description = "Dose quantity value", example = "10")
  private Integer doseQuantityValue;

  @JsonProperty("doseQuantityUnit")
  @Schema(description = "Dose quantity unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String doseQuantityUnit;

  @JsonProperty("doseQuantitySystem")
  @Schema(description = "Dose range quantity system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String doseQuantitySystem;

  @JsonProperty("doseRangeLowValue")
  @Schema(description = "Dose range low value", example = "10")
  private Integer doseRangeLowValue;

  @JsonProperty("doseRangeHighValue")
  @Schema(description = "Dose range high value", example = "10")
  private Integer doseRangeHighValue;

  @JsonProperty("doseRangeUnit")
  @Schema(description = "Dose range unit", example = "10")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String doseRangeUnit;

  @JsonProperty("doseRangeSystem")
  @Schema(description = "Dose range system", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String doseRangeSystem;

  @JsonProperty("rateNumeratorValue")
  @Schema(description = "Rate numerator value", example = "10")
  private Integer rateNumeratorValue;

  @JsonProperty("rateDenominatorValue")
  @Schema(description = "Rate denominator value", example = "10")
  private Integer rateDenominatorValue;

  @JsonProperty("rateUnit")
  @Schema(description = "Rate Unit", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String rateUnit;

  @JsonProperty("rateSystem")
  @Schema(description = "Rate system", example = "String")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String rateSystem;

  @JsonProperty("maxDosePerPeriodNumeratorValue")
  @Schema(description = "Max dose per period Numerator value", example = "10")
  private Integer maxDosePerPeriodNumeratorValue;

  @JsonProperty("maxDosePerPeriodDenominatorValue")
  @Schema(description = "Max dose per period denomination value", example = "10")
  private Integer maxDosePerPeriodDenominatorValue;

  @JsonProperty("maxDosePerPeriodUnit")
  @Schema(description = "Max dose per period unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String maxDosePerPeriodUnit;

  @JsonProperty("maxDosePerPeriodSystem")
  @Schema(description = "Max dose per period system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String maxDosePerPeriodSystem;

  @JsonProperty("createdDateUtc")
  @Schema(description = "Created date time", example = "2020-02-10T00:00:00.000")
  private LocalDateTime createdDateUtc;

  /**
   * The unique identity
   *
   * @return Id
   * @documentationExample 10
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Renewal request Id
   *
   * @return Renewal request id
   * @documentationExample 10
   */
  public int getRenewalRequestId() {
    return renewalRequestId;
  }

  public void setRenewalRequestId(int renewalRequestId) {
    this.renewalRequestId = renewalRequestId;
  }

  /**
   * Sequence
   *
   * @return sequence value
   * @documentationExample 10
   */
  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  /**
   * Instruction relationship
   *
   * @return Instruction relationship
   * @documentationExample String
   */
  public String getInstructionRelationship() {
    return instructionRelationship;
  }

  public void setInstructionRelationship(String instructionRelationship) {
    this.instructionRelationship = instructionRelationship;
  }

  /**
   * Instructions
   *
   * @return Instructions
   * @documentationExample String
   */
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  /**
   * Additional Instructions
   *
   * @return Additional Instructions
   * @documentationExample String
   */
  public String getAdditionalInstructions() {
    return additionalInstructions;
  }

  public void setAdditionalInstructions(String additionalInstructions) {
    this.additionalInstructions = additionalInstructions;
  }

  /**
   * Timing
   *
   * @return Timing
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTiming() {
    return timing;
  }

  public void setTiming(LocalDateTime timing) {
    this.timing = timing;
  }

  /**
   * Timing repeat bounds quantity value
   *
   * @return Timing repeat bounds quantity value
   * @documentationExample 10
   */
  public Integer getTimingRepeatBoundsQuantityValue() {
    return timingRepeatBoundsQuantityValue;
  }

  public void setTimingRepeatBoundsQuantityValue(Integer timingRepeatBoundsQuantityValue) {
    this.timingRepeatBoundsQuantityValue = timingRepeatBoundsQuantityValue;
  }

  /**
   * Timing repeat bounds quantity unit
   *
   * @return Timing repeat bounds quantity unit
   * @documentationExample Unit name
   */
  public String getTimingRepeatBoundsQuantityUnit() {
    return timingRepeatBoundsQuantityUnit;
  }

  public void setTimingRepeatBoundsQuantityUnit(String timingRepeatBoundsQuantityUnit) {
    this.timingRepeatBoundsQuantityUnit = timingRepeatBoundsQuantityUnit;
  }

  /**
   * Timing repeat bounds quantity system
   *
   * @return Timing repeat bounds quantity system
   * @documentationExample System name
   */
  public String getTimingRepeatBoundsQuantitySystem() {
    return timingRepeatBoundsQuantitySystem;
  }

  public void setTimingRepeatBoundsQuantitySystem(String timingRepeatBoundsQuantitySystem) {
    this.timingRepeatBoundsQuantitySystem = timingRepeatBoundsQuantitySystem;
  }

  /**
   * Timing repeat bounds range high
   *
   * @return Timing repeat bounds range high
   * @documentationExample 10
   */
  public Integer getTimingRepeatBoundsRangeHigh() {
    return timingRepeatBoundsRangeHigh;
  }

  public void setTimingRepeatBoundsRangeHigh(Integer timingRepeatBoundsRangeHigh) {
    this.timingRepeatBoundsRangeHigh = timingRepeatBoundsRangeHigh;
  }

  /**
   * Timing repeat bounds range low
   *
   * @return Timing repeat bounds range low
   * @documentationExample 10
   */
  public Integer getTimingRepeatBoundsRangeLow() {
    return timingRepeatBoundsRangeLow;
  }

  public void setTimingRepeatBoundsRangeLow(Integer timingRepeatBoundsRangeLow) {
    this.timingRepeatBoundsRangeLow = timingRepeatBoundsRangeLow;
  }

  /**
   * Timing repeat bounds range unit
   *
   * @return Timing repeat bounds range unit
   * @documentationExample Unit name
   */
  public String getTimingRepeatBoundsRangeUnit() {
    return timingRepeatBoundsRangeUnit;
  }

  public void setTimingRepeatBoundsRangeUnit(String timingRepeatBoundsRangeUnit) {
    this.timingRepeatBoundsRangeUnit = timingRepeatBoundsRangeUnit;
  }

  /**
   * Timing repeat bounds range system
   *
   * @return Timing repeat bounds range system
   * @documentationExample System name
   */
  public String getTimingRepeatBoundsRangeSystem() {
    return timingRepeatBoundsRangeSystem;
  }

  public void setTimingRepeatBoundsRangeSystem(String timingRepeatBoundsRangeSystem) {
    this.timingRepeatBoundsRangeSystem = timingRepeatBoundsRangeSystem;
  }

  /**
   * Timing repeat bounds range period start date
   *
   * @return Timing repeat bounds range period start date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimingRepeatBoundsPeriodStart() {
    return timingRepeatBoundsPeriodStart;
  }

  public void setTimingRepeatBoundsPeriodStart(LocalDateTime timingRepeatBoundsPeriodStart) {
    this.timingRepeatBoundsPeriodStart = timingRepeatBoundsPeriodStart;
  }

  /**
   * Timing repeat bounds range period end date
   *
   * @return Timing repeat bounds range period end date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimingRepeatBoundsPeriodEnd() {
    return timingRepeatBoundsPeriodEnd;
  }

  public void setTimingRepeatBoundsPeriodEnd(LocalDateTime timingRepeatBoundsPeriodEnd) {
    this.timingRepeatBoundsPeriodEnd = timingRepeatBoundsPeriodEnd;
  }

  /**
   * Timing repeat dose count
   *
   * @return Timing repeat dose count
   * @documentationExample 10
   */
  public Integer getTimingRepeatDoseCount() {
    return timingRepeatDoseCount;
  }

  public void setTimingRepeatDoseCount(Integer timingRepeatDoseCount) {
    this.timingRepeatDoseCount = timingRepeatDoseCount;
  }

  /**
   * Timing repeat dose count max
   *
   * @return Timing repeat dose count max
   * @documentationExample 10
   */
  public Integer getTimingRepeatDoseCountMax() {
    return timingRepeatDoseCountMax;
  }

  public void setTimingRepeatDoseCountMax(Integer timingRepeatDoseCountMax) {
    this.timingRepeatDoseCountMax = timingRepeatDoseCountMax;
  }

  /**
   * Timing repeat duration
   *
   * @return Timing repeat duration
   * @documentationExample 10
   */
  public Integer getTimingRepeatDuration() {
    return timingRepeatDuration;
  }

  public void setTimingRepeatDuration(Integer timingRepeatDuration) {
    this.timingRepeatDuration = timingRepeatDuration;
  }

  /**
   * Timing repeat duration max
   *
   * @return Timing repeat duration max
   * @documentationExample 10
   */
  public Integer getTimingRepeatDurationMax() {
    return timingRepeatDurationMax;
  }

  public void setTimingRepeatDurationMax(Integer timingRepeatDurationMax) {
    this.timingRepeatDurationMax = timingRepeatDurationMax;
  }

  /**
   * Timing repeat duration unit
   *
   * @return Timing repeat duration unit
   * @documentationExample Unit name
   */
  public String getTimingRepeatDurationUnit() {
    return timingRepeatDurationUnit;
  }

  public void setTimingRepeatDurationUnit(String timingRepeatDurationUnit) {
    this.timingRepeatDurationUnit = timingRepeatDurationUnit;
  }

  /**
   * Timing repeat frequency
   *
   * @return Timing repeat frequency
   * @documentationExample 10
   */
  public Integer getTimingRepeatFrequency() {
    return timingRepeatFrequency;
  }

  public void setTimingRepeatFrequency(Integer timingRepeatFrequency) {
    this.timingRepeatFrequency = timingRepeatFrequency;
  }

  /**
   * Timing repeat frequency max
   *
   * @return Timing repeat frequency max
   * @documentationExample 10
   */
  public Integer getTimingRepeatFrequencyMax() {
    return timingRepeatFrequencyMax;
  }

  public void setTimingRepeatFrequencyMax(Integer timingRepeatFrequencyMax) {
    this.timingRepeatFrequencyMax = timingRepeatFrequencyMax;
  }

  /**
   * Timing repeat period
   *
   * @return Timing repeat period
   * @documentationExample 10
   */
  public Integer getTimingRepeatPeriod() {
    return timingRepeatPeriod;
  }

  public void setTimingRepeatPeriod(Integer timingRepeatPeriod) {
    this.timingRepeatPeriod = timingRepeatPeriod;
  }

  /**
   * Timing repeat period max
   *
   * @return Timing repeat period max value
   * @documentationExample 10
   */
  public Integer getTimingRepeatPeriodMax() {
    return timingRepeatPeriodMax;
  }

  public void setTimingRepeatPeriodMax(Integer timingRepeatPeriodMax) {
    this.timingRepeatPeriodMax = timingRepeatPeriodMax;
  }

  /**
   * Timing repeat period unit
   *
   * @return Timing repeat period unit
   * @documentationExample Unit name
   */
  public String getTimingRepeatPeriodUnit() {
    return timingRepeatPeriodUnit;
  }

  public void setTimingRepeatPeriodUnit(String timingRepeatPeriodUnit) {
    this.timingRepeatPeriodUnit = timingRepeatPeriodUnit;
  }

  /**
   * Is needed
   *
   * @return Is needed
   * @documentationExample True
   */
  public Boolean getAsNeeded() {
    return asNeeded;
  }

  public void setAsNeeded(Boolean asNeeded) {
    this.asNeeded = asNeeded;
  }

  /**
   * Route
   *
   * @return Route
   * @documentationExample String
   */
  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  /**
   * Dose quantity value
   *
   * @return Dose quantity value
   * @documentationExample 10
   */
  public Integer getDoseQuantityValue() {
    return doseQuantityValue;
  }

  public void setDoseQuantityValue(Integer doseQuantityValue) {
    this.doseQuantityValue = doseQuantityValue;
  }

  /**
   * Dose quantity unit
   *
   * @return Dose quantity unit
   * @documentationExample Unit name
   */
  public String getDoseQuantityUnit() {
    return doseQuantityUnit;
  }

  public void setDoseQuantityUnit(String doseQuantityUnit) {
    this.doseQuantityUnit = doseQuantityUnit;
  }

  /**
   * Dose quantity system name
   *
   * @return Dose quantity system name
   * @documentationExample System name
   */
  public String getDoseQuantitySystem() {
    return doseQuantitySystem;
  }

  public void setDoseQuantitySystem(String doseQuantitySystem) {
    this.doseQuantitySystem = doseQuantitySystem;
  }

  /**
   * Dose range low value
   *
   * @return Dose range low value
   * @documentationExample 10
   */
  public Integer getDoseRangeLowValue() {
    return doseRangeLowValue;
  }

  public void setDoseRangeLowValue(Integer doseRangeLowValue) {
    this.doseRangeLowValue = doseRangeLowValue;
  }

  /**
   * Dose range high value
   *
   * @return Dose range high value
   * @documentationExample 10
   */
  public Integer getDoseRangeHighValue() {
    return doseRangeHighValue;
  }

  public void setDoseRangeHighValue(Integer doseRangeHighValue) {
    this.doseRangeHighValue = doseRangeHighValue;
  }

  /**
   * Dose range unit
   *
   * @return Dose range unit
   * @documentationExample Unit name
   */
  public String getDoseRangeUnit() {
    return doseRangeUnit;
  }

  public void setDoseRangeUnit(String doseRangeUnit) {
    this.doseRangeUnit = doseRangeUnit;
  }

  /**
   * Dose range system
   *
   * @return Dose range system
   * @documentationExample System name
   */
  public String getDoseRangeSystem() {
    return doseRangeSystem;
  }

  public void setDoseRangeSystem(String doseRangeSystem) {
    this.doseRangeSystem = doseRangeSystem;
  }

  /**
   * Rate numerator value
   *
   * @return Rate numerator value
   * @documentationExample 10
   */
  public Integer getRateNumeratorValue() {
    return rateNumeratorValue;
  }

  public void setRateNumeratorValue(Integer rateNumeratorValue) {
    this.rateNumeratorValue = rateNumeratorValue;
  }

  /**
   * Rate denominator value
   *
   * @return Rate denominator value
   * @documentationExample 10
   */
  public Integer getRateDenominatorValue() {
    return rateDenominatorValue;
  }

  public void setRateDenominatorValue(Integer rateDenominatorValue) {
    this.rateDenominatorValue = rateDenominatorValue;
  }

  /**
   * Rate unit
   *
   * @return Rate unit
   * @documentationExample Unit name
   */
  public String getRateUnit() {
    return rateUnit;
  }

  public void setRateUnit(String rateUnit) {
    this.rateUnit = rateUnit;
  }

  /**
   * Rate system
   *
   * @return Rate system
   * @documentationExample System name
   */
  public String getRateSystem() {
    return rateSystem;
  }

  public void setRateSystem(String rateSystem) {
    this.rateSystem = rateSystem;
  }

  /**
   * Max dose per period Numerator value
   *
   * @return Max dose per period Numerator value
   * @documentationExample 10
   */
  public Integer getMaxDosePerPeriodNumeratorValue() {
    return maxDosePerPeriodNumeratorValue;
  }

  public void setMaxDosePerPeriodNumeratorValue(Integer maxDosePerPeriodNumeratorValue) {
    this.maxDosePerPeriodNumeratorValue = maxDosePerPeriodNumeratorValue;
  }

  /**
   * Max dose per period denominator value
   *
   * @return Max dose per period denominator value
   * @documentationExample 10
   */
  public Integer getMaxDosePerPeriodDenominatorValue() {
    return maxDosePerPeriodDenominatorValue;
  }

  public void setMaxDosePerPeriodDenominatorValue(Integer maxDosePerPeriodDenominatorValue) {
    this.maxDosePerPeriodDenominatorValue = maxDosePerPeriodDenominatorValue;
  }

  /**
   * Max dose per period unit
   *
   * @return Max dose per period unit
   * @documentationExample Unit name
   */
  public String getMaxDosePerPeriodUnit() {
    return maxDosePerPeriodUnit;
  }

  public void setMaxDosePerPeriodUnit(String maxDosePerPeriodUnit) {
    this.maxDosePerPeriodUnit = maxDosePerPeriodUnit;
  }

  /**
   * Max dose per period system
   *
   * @return Max dose per period system
   * @documentationExample System name
   */
  public String getMaxDosePerPeriodSystem() {
    return maxDosePerPeriodSystem;
  }

  public void setMaxDosePerPeriodSystem(String maxDosePerPeriodSystem) {
    this.maxDosePerPeriodSystem = maxDosePerPeriodSystem;
  }

  /**
   * Created date time - read only
   *
   * @return Created date time
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDateUtc() {
    return createdDateUtc;
  }

  public void setCreatedDateUtc(LocalDateTime createdDateUtc) {
    this.createdDateUtc = createdDateUtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RenewalRequestDosageDto that = (RenewalRequestDosageDto) o;
    if (id != that.id) {
      return false;
    }
    if (renewalRequestId != that.renewalRequestId) {
      return false;
    }
    if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) {
      return false;
    }
    if (instructionRelationship != null
        ? !instructionRelationship.equals(that.instructionRelationship)
        : that.instructionRelationship != null) {
      return false;
    }
    if (instructions != null ? !instructions.equals(that.instructions)
        : that.instructions != null) {
      return false;
    }
    if (additionalInstructions != null ? !additionalInstructions.equals(that.additionalInstructions)
        : that.additionalInstructions != null) {
      return false;
    }
    if (timing != null ? !timing.equals(that.timing) : that.timing != null) {
      return false;
    }
    if (timingRepeatBoundsQuantityValue != null
        ? !timingRepeatBoundsQuantityValue.equals(that.timingRepeatBoundsQuantityValue)
        : that.timingRepeatBoundsQuantityValue != null) {
      return false;
    }
    if (timingRepeatBoundsQuantityUnit != null
        ? !timingRepeatBoundsQuantityUnit.equals(that.timingRepeatBoundsQuantityUnit)
        : that.timingRepeatBoundsQuantityUnit != null) {
      return false;
    }
    if (timingRepeatBoundsQuantitySystem != null
        ? !timingRepeatBoundsQuantitySystem.equals(that.timingRepeatBoundsQuantitySystem)
        : that.timingRepeatBoundsQuantitySystem != null) {
      return false;
    }
    if (timingRepeatBoundsRangeHigh != null
        ? !timingRepeatBoundsRangeHigh.equals(that.timingRepeatBoundsRangeHigh)
        : that.timingRepeatBoundsRangeHigh != null) {
      return false;
    }
    if (timingRepeatBoundsRangeLow != null
        ? !timingRepeatBoundsRangeLow.equals(that.timingRepeatBoundsRangeLow)
        : that.timingRepeatBoundsRangeLow != null) {
      return false;
    }
    if (timingRepeatBoundsRangeUnit != null
        ? !timingRepeatBoundsRangeUnit.equals(that.timingRepeatBoundsRangeUnit)
        : that.timingRepeatBoundsRangeUnit != null) {
      return false;
    }
    if (timingRepeatBoundsRangeSystem != null
        ? !timingRepeatBoundsRangeSystem.equals(that.timingRepeatBoundsRangeSystem)
        : that.timingRepeatBoundsRangeSystem != null) {
      return false;
    }
    if (timingRepeatBoundsPeriodStart != null
        ? !timingRepeatBoundsPeriodStart.equals(that.timingRepeatBoundsPeriodStart)
        : that.timingRepeatBoundsPeriodStart != null) {
      return false;
    }
    if (timingRepeatBoundsPeriodEnd != null
        ? !timingRepeatBoundsPeriodEnd.equals(that.timingRepeatBoundsPeriodEnd)
        : that.timingRepeatBoundsPeriodEnd != null) {
      return false;
    }
    if (timingRepeatDoseCount != null ? !timingRepeatDoseCount.equals(that.timingRepeatDoseCount)
        : that.timingRepeatDoseCount != null) {
      return false;
    }
    if (timingRepeatDoseCountMax != null
        ? !timingRepeatDoseCountMax.equals(that.timingRepeatDoseCountMax)
        : that.timingRepeatDoseCountMax != null) {
      return false;
    }
    if (timingRepeatDuration != null ? !timingRepeatDuration.equals(that.timingRepeatDuration)
        : that.timingRepeatDuration != null) {
      return false;
    }
    if (timingRepeatDurationMax != null
        ? !timingRepeatDurationMax.equals(that.timingRepeatDurationMax)
        : that.timingRepeatDurationMax != null) {
      return false;
    }
    if (timingRepeatDurationUnit != null
        ? !timingRepeatDurationUnit.equals(that.timingRepeatDurationUnit)
        : that.timingRepeatDurationUnit != null) {
      return false;
    }
    if (timingRepeatFrequency != null ? !timingRepeatFrequency.equals(that.timingRepeatFrequency)
        : that.timingRepeatFrequency != null) {
      return false;
    }
    if (timingRepeatFrequencyMax != null
        ? !timingRepeatFrequencyMax.equals(that.timingRepeatFrequencyMax)
        : that.timingRepeatFrequencyMax != null) {
      return false;
    }
    if (timingRepeatPeriod != null ? !timingRepeatPeriod.equals(that.timingRepeatPeriod)
        : that.timingRepeatPeriod != null) {
      return false;
    }
    if (timingRepeatPeriodMax != null ? !timingRepeatPeriodMax.equals(that.timingRepeatPeriodMax)
        : that.timingRepeatPeriodMax != null) {
      return false;
    }
    if (timingRepeatPeriodUnit != null ? !timingRepeatPeriodUnit.equals(that.timingRepeatPeriodUnit)
        : that.timingRepeatPeriodUnit != null) {
      return false;
    }
    if (asNeeded != null ? !asNeeded.equals(that.asNeeded) : that.asNeeded != null) {
      return false;
    }
    if (route != null ? !route.equals(that.route) : that.route != null) {
      return false;
    }
    if (doseQuantityValue != null ? !doseQuantityValue.equals(that.doseQuantityValue)
        : that.doseQuantityValue != null) {
      return false;
    }
    if (doseQuantityUnit != null ? !doseQuantityUnit.equals(that.doseQuantityUnit)
        : that.doseQuantityUnit != null) {
      return false;
    }
    if (doseQuantitySystem != null ? !doseQuantitySystem.equals(that.doseQuantitySystem)
        : that.doseQuantitySystem != null) {
      return false;
    }
    if (doseRangeLowValue != null ? !doseRangeLowValue.equals(that.doseRangeLowValue)
        : that.doseRangeLowValue != null) {
      return false;
    }
    if (doseRangeHighValue != null ? !doseRangeHighValue.equals(that.doseRangeHighValue)
        : that.doseRangeHighValue != null) {
      return false;
    }
    if (doseRangeUnit != null ? !doseRangeUnit.equals(that.doseRangeUnit)
        : that.doseRangeUnit != null) {
      return false;
    }
    if (doseRangeSystem != null ? !doseRangeSystem.equals(that.doseRangeSystem)
        : that.doseRangeSystem != null) {
      return false;
    }
    if (rateNumeratorValue != null ? !rateNumeratorValue.equals(that.rateNumeratorValue)
        : that.rateNumeratorValue != null) {
      return false;
    }
    if (rateDenominatorValue != null ? !rateDenominatorValue.equals(that.rateDenominatorValue)
        : that.rateDenominatorValue != null) {
      return false;
    }
    if (rateUnit != null ? !rateUnit.equals(that.rateUnit) : that.rateUnit != null) {
      return false;
    }
    if (rateSystem != null ? !rateSystem.equals(that.rateSystem) : that.rateSystem != null) {
      return false;
    }
    if (maxDosePerPeriodNumeratorValue != null
        ? !maxDosePerPeriodNumeratorValue.equals(that.maxDosePerPeriodNumeratorValue)
        : that.maxDosePerPeriodNumeratorValue != null) {
      return false;
    }
    if (maxDosePerPeriodDenominatorValue != null
        ? !maxDosePerPeriodDenominatorValue.equals(that.maxDosePerPeriodDenominatorValue)
        : that.maxDosePerPeriodDenominatorValue != null) {
      return false;
    }
    if (maxDosePerPeriodUnit != null ? !maxDosePerPeriodUnit.equals(that.maxDosePerPeriodUnit)
        : that.maxDosePerPeriodUnit != null) {
      return false;
    }
    if (maxDosePerPeriodSystem != null ? !maxDosePerPeriodSystem.equals(that.maxDosePerPeriodSystem)
        : that.maxDosePerPeriodSystem != null) {
      return false;
    }
    return createdDateUtc != null ? createdDateUtc.equals(that.createdDateUtc)
        : that.createdDateUtc == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + renewalRequestId;
    result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
    result =
        31 * result + (instructionRelationship != null ? instructionRelationship.hashCode() : 0);
    result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
    result = 31 * result + (additionalInstructions != null ? additionalInstructions.hashCode() : 0);
    result = 31 * result + (timing != null ? timing.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsQuantityValue != null ? timingRepeatBoundsQuantityValue.hashCode()
            : 0);
    result = 31 * result
        + (timingRepeatBoundsQuantityUnit != null ? timingRepeatBoundsQuantityUnit.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsQuantitySystem != null ? timingRepeatBoundsQuantitySystem.hashCode()
            : 0);
    result = 31 * result
        + (timingRepeatBoundsRangeHigh != null ? timingRepeatBoundsRangeHigh.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsRangeLow != null ? timingRepeatBoundsRangeLow.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsRangeUnit != null ? timingRepeatBoundsRangeUnit.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsRangeSystem != null ? timingRepeatBoundsRangeSystem.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsPeriodStart != null ? timingRepeatBoundsPeriodStart.hashCode() : 0);
    result = 31 * result
        + (timingRepeatBoundsPeriodEnd != null ? timingRepeatBoundsPeriodEnd.hashCode() : 0);
    result = 31 * result + (timingRepeatDoseCount != null ? timingRepeatDoseCount.hashCode() : 0);
    result =
        31 * result + (timingRepeatDoseCountMax != null ? timingRepeatDoseCountMax.hashCode() : 0);
    result = 31 * result + (timingRepeatDuration != null ? timingRepeatDuration.hashCode() : 0);
    result =
        31 * result + (timingRepeatDurationMax != null ? timingRepeatDurationMax.hashCode() : 0);
    result =
        31 * result + (timingRepeatDurationUnit != null ? timingRepeatDurationUnit.hashCode() : 0);
    result = 31 * result + (timingRepeatFrequency != null ? timingRepeatFrequency.hashCode() : 0);
    result =
        31 * result + (timingRepeatFrequencyMax != null ? timingRepeatFrequencyMax.hashCode() : 0);
    result = 31 * result + (timingRepeatPeriod != null ? timingRepeatPeriod.hashCode() : 0);
    result = 31 * result + (timingRepeatPeriodMax != null ? timingRepeatPeriodMax.hashCode() : 0);
    result = 31 * result + (timingRepeatPeriodUnit != null ? timingRepeatPeriodUnit.hashCode() : 0);
    result = 31 * result + (asNeeded != null ? asNeeded.hashCode() : 0);
    result = 31 * result + (route != null ? route.hashCode() : 0);
    result = 31 * result + (doseQuantityValue != null ? doseQuantityValue.hashCode() : 0);
    result = 31 * result + (doseQuantityUnit != null ? doseQuantityUnit.hashCode() : 0);
    result = 31 * result + (doseQuantitySystem != null ? doseQuantitySystem.hashCode() : 0);
    result = 31 * result + (doseRangeLowValue != null ? doseRangeLowValue.hashCode() : 0);
    result = 31 * result + (doseRangeHighValue != null ? doseRangeHighValue.hashCode() : 0);
    result = 31 * result + (doseRangeUnit != null ? doseRangeUnit.hashCode() : 0);
    result = 31 * result + (doseRangeSystem != null ? doseRangeSystem.hashCode() : 0);
    result = 31 * result + (rateNumeratorValue != null ? rateNumeratorValue.hashCode() : 0);
    result = 31 * result + (rateDenominatorValue != null ? rateDenominatorValue.hashCode() : 0);
    result = 31 * result + (rateUnit != null ? rateUnit.hashCode() : 0);
    result = 31 * result + (rateSystem != null ? rateSystem.hashCode() : 0);
    result = 31 * result
        + (maxDosePerPeriodNumeratorValue != null ? maxDosePerPeriodNumeratorValue.hashCode() : 0);
    result = 31 * result
        + (maxDosePerPeriodDenominatorValue != null ? maxDosePerPeriodDenominatorValue.hashCode()
            : 0);
    result = 31 * result + (maxDosePerPeriodUnit != null ? maxDosePerPeriodUnit.hashCode() : 0);
    result = 31 * result + (maxDosePerPeriodSystem != null ? maxDosePerPeriodSystem.hashCode() : 0);
    result = 31 * result + (createdDateUtc != null ? createdDateUtc.hashCode() : 0);
    return result;
  }
}
