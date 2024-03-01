
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class DispenseRequestDto {

  @JsonProperty("id")
  @Schema(description = "The identity", example = "10")
  private Integer id;

  @JsonProperty("dispenseIntervalMin")
  @Schema(description = "Dispense interval min", example = "10")
  private Integer dispenseIntervalMin;

  @JsonProperty("dispenseIntervalMax")
  @Schema(description = "Dispense interval max", example = "10")
  private Integer dispenseIntervalMax;

  @JsonProperty("dispenseIntervalUnit")
  @Schema(description = "Dispense interval unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String dispenseIntervalUnit;

  @JsonProperty("dispenseIntervalSystem")
  @Schema(description = "Dispense interval system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String dispenseIntervalSystem;

  @JsonProperty("trialEligibility")
  @Schema(description = "Is trial eligibility", example = "True")
  private Boolean trialEligibility;

  @JsonProperty("firstFillDate")
  @Schema(description = "The first fill date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime firstFillDate;

  @JsonProperty("firstFillQuantity")
  @Schema(description = "First fill quantity ", example = "10")
  private Integer firstFillQuantity;

  @JsonProperty("firstFillQuantityUnit")
  @Schema(description = "First fill quantity unit", example = "unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String firstFillQuantityUnit;

  @JsonProperty("firstFillQuantitySystem")
  @Schema(description = "First fill quantity system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String firstFillQuantitySystem;

  @JsonProperty("firstFillSupplyDuration")
  @Schema(description = "First fill supply duration", example = "10")
  private Integer firstFillSupplyDuration;

  @JsonProperty("firstFillSupplyDurationUnit")
  @Schema(description = "First fill supply duration unit", example = "unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String firstFillSupplyDurationUnit;

  @JsonProperty("firstFillSupplyDurationSystem")
  @Schema(description = "First fill supply duration system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String firstFillSupplyDurationSystem;

  @JsonProperty("maxDispenseQuantity")
  @Schema(description = "Max dispense quantity", example = "10")
  private Integer maxDispenseQuantity;

  @JsonProperty("maxDispenseQuantityUnit")
  @Schema(description = "Max dispense quantity unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String maxDispenseQuantityUnit;

  @JsonProperty("maxDispenseQuantitySystem")
  @Schema(description = "Max dispense quantity system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String maxDispenseQuantitySystem;

  @JsonProperty("totalPrescribedQuantity")
  @Schema(description = "Total prescribed quantity", example = "10")
  private Integer totalPrescribedQuantity;

  @JsonProperty("totalPrescribedQuantityUnit")
  @Schema(description = "Total prescribed quantity unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String totalPrescribedQuantityUnit;

  @JsonProperty("totalPrescribedQuantitySystem")
  @Schema(description = "Total prescribed quantity system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String totalPrescribedQuantitySystem;

  @JsonProperty("validityStart")
  @Schema(description = "Validity start date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime validityStart;

  @JsonProperty("validityEnd")
  @Schema(description = "Validity end date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime validityEnd;

  @JsonProperty("totalDaysSupply")
  @Schema(description = "Total days supply", example = "10")
  private Integer totalDaysSupply;

  @JsonProperty("totalQuantityValue")
  @Schema(description = "Total quantity value", example = "10")
  private Integer totalQuantityValue;

  @JsonProperty("totalQuantityUnit")
  @Schema(description = "Total quantity unit", example = "10")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String totalQuantityUnit;

  @JsonProperty("totalQuantitySystem")
  @Schema(description = "Total quantity system", example = "10")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String totalQuantitySystem;

  @JsonProperty("numberOfRepeatsAllows")
  @Schema(description = "Number repeats allowed", example = "10")
  private Integer numberOfRepeatsAllows;

  @JsonProperty("supplyDurationValue")
  @Schema(description = "Supply duration value", example = "10")
  private Integer supplyDurationValue;

  @JsonProperty("supplyDurationUnit")
  @Schema(description = "Supply duration unit", example = "Unit")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String supplyDurationUnit;

  @JsonProperty("supplyDurationSystem")
  @Schema(description = "Supply duration system", example = "System")
  @Size(max = 100, message = "Maximum length allowed is 100 characters.")
  private String supplyDurationSystem;

  @JsonProperty("createdDateUtc")
  @Schema(description = "Created date time ", example = "2020-02-10T00:00:00.000")
  private LocalDateTime createdDateUtc;

  /**
   * The unique identity
   *
   * @return Id
   * @documentationExample 10
   */
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Dispense interval min
   *
   * @return Dispense interval min value
   * @documentationExample 10
   */
  public Integer getDispenseIntervalMin() {
    return dispenseIntervalMin;
  }

  public void setDispenseIntervalMin(Integer dispenseIntervalMin) {
    this.dispenseIntervalMin = dispenseIntervalMin;
  }

  /**
   * Dispense interval max
   *
   * @return Dispense interval max value
   * @documentationExample 10
   */
  public Integer getDispenseIntervalMax() {
    return dispenseIntervalMax;
  }

  public void setDispenseIntervalMax(Integer dispenseIntervalMax) {
    this.dispenseIntervalMax = dispenseIntervalMax;
  }

  /**
   * Dispense interval unit
   *
   * @return Dispense interval unit name
   * @documentationExample Unit name
   */
  public String getDispenseIntervalUnit() {
    return dispenseIntervalUnit;
  }

  public void setDispenseIntervalUnit(String dispenseIntervalUnit) {
    this.dispenseIntervalUnit = dispenseIntervalUnit;
  }

  /**
   * Dispense interval system
   *
   * @return Dispense interval system name
   * @documentationExample System name
   */
  public String getDispenseIntervalSystem() {
    return dispenseIntervalSystem;
  }

  public void setDispenseIntervalSystem(String dispenseIntervalSystem) {
    this.dispenseIntervalSystem = dispenseIntervalSystem;
  }

  /**
   * Is trial eligibility
   *
   * @return True(or false) on trial eligibility
   * @documentationExample True
   */
  public Boolean getTrialEligibility() {
    return trialEligibility;
  }

  public void setTrialEligibility(Boolean trialEligibility) {
    this.trialEligibility = trialEligibility;
  }

  /**
   * First fill date
   *
   * @return First fill date time
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getFirstFillDate() {
    return firstFillDate;
  }

  public void setFirstFillDate(LocalDateTime firstFillDate) {
    this.firstFillDate = firstFillDate;
  }

  /**
   * First fill quantity
   *
   * @return First fill quantity
   * @documentationExample 10
   */
  public Integer getFirstFillQuantity() {
    return firstFillQuantity;
  }

  public void setFirstFillQuantity(Integer firstFillQuantity) {
    this.firstFillQuantity = firstFillQuantity;
  }

  /**
   * First fill quantity unit
   *
   * @return First fill quantity unit name
   * @documentationExample Unit name
   */
  public String getFirstFillQuantityUnit() {
    return firstFillQuantityUnit;
  }

  public void setFirstFillQuantityUnit(String firstFillQuantityUnit) {
    this.firstFillQuantityUnit = firstFillQuantityUnit;
  }

  /**
   * First fill quantity system
   *
   * @return First fill quantity system name
   * @documentationExample System name
   */
  public String getFirstFillQuantitySystem() {
    return firstFillQuantitySystem;
  }

  public void setFirstFillQuantitySystem(String firstFillQuantitySystem) {
    this.firstFillQuantitySystem = firstFillQuantitySystem;
  }

  /**
   * First fill supply duration
   *
   * @return First fill supply duration
   * @documentationExample 10
   */
  public Integer getFirstFillSupplyDuration() {
    return firstFillSupplyDuration;
  }

  public void setFirstFillSupplyDuration(Integer firstFillSupplyDuration) {
    this.firstFillSupplyDuration = firstFillSupplyDuration;
  }

  /**
   * First fill supply duration unit
   *
   * @return First fill supply duration unit name
   * @documentationExample Unit name
   */
  public String getFirstFillSupplyDurationUnit() {
    return firstFillSupplyDurationUnit;
  }

  public void setFirstFillSupplyDurationUnit(String firstFillSupplyDurationUnit) {
    this.firstFillSupplyDurationUnit = firstFillSupplyDurationUnit;
  }

  /**
   * First fill supply duration system
   *
   * @return First fill supply duration system name
   * @documentationExample System name
   */
  public String getFirstFillSupplyDurationSystem() {
    return firstFillSupplyDurationSystem;
  }

  public void setFirstFillSupplyDurationSystem(String firstFillSupplyDurationSystem) {
    this.firstFillSupplyDurationSystem = firstFillSupplyDurationSystem;
  }

  /**
   * Max dispense quantity
   *
   * @return Max dispense quantity
   * @documentationExample 10
   */
  public Integer getMaxDispenseQuantity() {
    return maxDispenseQuantity;
  }

  public void setMaxDispenseQuantity(Integer maxDispenseQuantity) {
    this.maxDispenseQuantity = maxDispenseQuantity;
  }

  /**
   * Max dispense quantity unit
   *
   * @return Max dispense quantity unit name
   * @documentationExample Ubnit name
   */
  public String getMaxDispenseQuantityUnit() {
    return maxDispenseQuantityUnit;
  }

  public void setMaxDispenseQuantityUnit(String maxDispenseQuantityUnit) {
    this.maxDispenseQuantityUnit = maxDispenseQuantityUnit;
  }

  /**
   * Max dispense quantity system name
   *
   * @return Max dispense quantity system name
   * @documentationExample System name
   */
  public String getMaxDispenseQuantitySystem() {
    return maxDispenseQuantitySystem;
  }

  public void setMaxDispenseQuantitySystem(String maxDispenseQuantitySystem) {
    this.maxDispenseQuantitySystem = maxDispenseQuantitySystem;
  }

  /**
   * Total prescribed quantity
   *
   * @return Total prescribed quantity
   * @documentationExample 10
   */
  public Integer getTotalPrescribedQuantity() {
    return totalPrescribedQuantity;
  }

  public void setTotalPrescribedQuantity(Integer totalPrescribedQuantity) {
    this.totalPrescribedQuantity = totalPrescribedQuantity;
  }

  /**
   * Total prescribed quantity unit name
   *
   * @return Total prescribed quantity unit name
   * @documentationExample Unit name
   */
  public String getTotalPrescribedQuantityUnit() {
    return totalPrescribedQuantityUnit;
  }

  public void setTotalPrescribedQuantityUnit(String totalPrescribedQuantityUnit) {
    this.totalPrescribedQuantityUnit = totalPrescribedQuantityUnit;
  }

  /**
   * Total prescribed quantity system name
   *
   * @return Total prescribed quantity system name
   * @documentationExample System name
   */
  public String getTotalPrescribedQuantitySystem() {
    return totalPrescribedQuantitySystem;
  }

  public void setTotalPrescribedQuantitySystem(String totalPrescribedQuantitySystem) {
    this.totalPrescribedQuantitySystem = totalPrescribedQuantitySystem;
  }

  /**
   * Validity start date
   *
   * @return Validity start date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getValidityStart() {
    return validityStart;
  }

  public void setValidityStart(LocalDateTime validityStart) {
    this.validityStart = validityStart;
  }

  /**
   * Validity end date
   *
   * @return Validity end date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getValidityEnd() {
    return validityEnd;
  }

  public void setValidityEnd(LocalDateTime validityEnd) {
    this.validityEnd = validityEnd;
  }

  /**
   * Total days supply
   *
   * @return ITotal days supply
   * @documentationExample 10
   */
  public Integer getTotalDaysSupply() {
    return totalDaysSupply;
  }

  public void setTotalDaysSupply(Integer totalDaysSupply) {
    this.totalDaysSupply = totalDaysSupply;
  }

  /**
   * Total quantity value
   *
   * @return Total quantity value
   * @documentationExample 10
   */
  public Integer getTotalQuantityValue() {
    return totalQuantityValue;
  }

  public void setTotalQuantityValue(Integer totalQuantityValue) {
    this.totalQuantityValue = totalQuantityValue;
  }

  /**
   * Total quantity unit name
   *
   * @return Total quantity unit name
   * @documentationExample Unit name
   */
  public String getTotalQuantityUnit() {
    return totalQuantityUnit;
  }

  public void setTotalQuantityUnit(String totalQuantityUnit) {
    this.totalQuantityUnit = totalQuantityUnit;
  }

  /**
   * Total quantity system name
   *
   * @return Total quantity system name
   * @documentationExample System name
   */
  public String getTotalQuantitySystem() {
    return totalQuantitySystem;
  }

  public void setTotalQuantitySystem(String totalQuantitySystem) {
    this.totalQuantitySystem = totalQuantitySystem;
  }

  /**
   * Number of repeats allowed
   *
   * @return Number of repeats allowed
   * @documentationExample 10
   */
  public Integer getNumberOfRepeatsAllows() {
    return numberOfRepeatsAllows;
  }

  public void setNumberOfRepeatsAllows(Integer numberOfRepeatsAllows) {
    this.numberOfRepeatsAllows = numberOfRepeatsAllows;
  }

  /**
   * Supply duration value
   *
   * @return Supply duration value
   * @documentationExample 10
   */
  public Integer getSupplyDurationValue() {
    return supplyDurationValue;
  }

  public void setSupplyDurationValue(Integer supplyDurationValue) {
    this.supplyDurationValue = supplyDurationValue;
  }

  /**
   * Supply duration system unit name
   *
   * @return Supply duration system unit name
   * @documentationExample Unit name
   */
  public String getSupplyDurationUnit() {
    return supplyDurationUnit;
  }

  public void setSupplyDurationUnit(String supplyDurationUnit) {
    this.supplyDurationUnit = supplyDurationUnit;
  }

  /**
   * Supply duration system name
   *
   * @return Supply duration system name
   * @documentationExample System name
   */
  public String getSupplyDurationSystem() {
    return supplyDurationSystem;
  }

  public void setSupplyDurationSystem(String supplyDurationSystem) {
    this.supplyDurationSystem = supplyDurationSystem;
  }

  /**
   * Created date time
   *
   * @return Created date time
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
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
    DispenseRequestDto that = (DispenseRequestDto) o;
    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (dispenseIntervalMin != null ? !dispenseIntervalMin.equals(that.dispenseIntervalMin)
        : that.dispenseIntervalMin != null) {
      return false;
    }
    if (dispenseIntervalMax != null ? !dispenseIntervalMax.equals(that.dispenseIntervalMax)
        : that.dispenseIntervalMax != null) {
      return false;
    }
    if (dispenseIntervalUnit != null ? !dispenseIntervalUnit.equals(that.dispenseIntervalUnit)
        : that.dispenseIntervalUnit != null) {
      return false;
    }
    if (dispenseIntervalSystem != null ? !dispenseIntervalSystem.equals(that.dispenseIntervalSystem)
        : that.dispenseIntervalSystem != null) {
      return false;
    }
    if (trialEligibility != null ? !trialEligibility.equals(that.trialEligibility)
        : that.trialEligibility != null) {
      return false;
    }
    if (firstFillDate != null ? !firstFillDate.equals(that.firstFillDate)
        : that.firstFillDate != null) {
      return false;
    }
    if (firstFillQuantity != null ? !firstFillQuantity.equals(that.firstFillQuantity)
        : that.firstFillQuantity != null) {
      return false;
    }
    if (firstFillQuantityUnit != null ? !firstFillQuantityUnit.equals(that.firstFillQuantityUnit)
        : that.firstFillQuantityUnit != null) {
      return false;
    }
    if (firstFillQuantitySystem != null
        ? !firstFillQuantitySystem.equals(that.firstFillQuantitySystem)
        : that.firstFillQuantitySystem != null) {
      return false;
    }
    if (firstFillSupplyDuration != null
        ? !firstFillSupplyDuration.equals(that.firstFillSupplyDuration)
        : that.firstFillSupplyDuration != null) {
      return false;
    }
    if (firstFillSupplyDurationUnit != null
        ? !firstFillSupplyDurationUnit.equals(that.firstFillSupplyDurationUnit)
        : that.firstFillSupplyDurationUnit != null) {
      return false;
    }
    if (firstFillSupplyDurationSystem != null
        ? !firstFillSupplyDurationSystem.equals(that.firstFillSupplyDurationSystem)
        : that.firstFillSupplyDurationSystem != null) {
      return false;
    }
    if (maxDispenseQuantity != null ? !maxDispenseQuantity.equals(that.maxDispenseQuantity)
        : that.maxDispenseQuantity != null) {
      return false;
    }
    if (maxDispenseQuantityUnit != null
        ? !maxDispenseQuantityUnit.equals(that.maxDispenseQuantityUnit)
        : that.maxDispenseQuantityUnit != null) {
      return false;
    }
    if (maxDispenseQuantitySystem != null
        ? !maxDispenseQuantitySystem.equals(that.maxDispenseQuantitySystem)
        : that.maxDispenseQuantitySystem != null) {
      return false;
    }
    if (totalPrescribedQuantity != null
        ? !totalPrescribedQuantity.equals(that.totalPrescribedQuantity)
        : that.totalPrescribedQuantity != null) {
      return false;
    }
    if (totalPrescribedQuantityUnit != null
        ? !totalPrescribedQuantityUnit.equals(that.totalPrescribedQuantityUnit)
        : that.totalPrescribedQuantityUnit != null) {
      return false;
    }
    if (totalPrescribedQuantitySystem != null
        ? !totalPrescribedQuantitySystem.equals(that.totalPrescribedQuantitySystem)
        : that.totalPrescribedQuantitySystem != null) {
      return false;
    }
    if (validityStart != null ? !validityStart.equals(that.validityStart)
        : that.validityStart != null) {
      return false;
    }
    if (validityEnd != null ? !validityEnd.equals(that.validityEnd) : that.validityEnd != null) {
      return false;
    }
    if (totalDaysSupply != null ? !totalDaysSupply.equals(that.totalDaysSupply)
        : that.totalDaysSupply != null) {
      return false;
    }
    if (totalQuantityValue != null ? !totalQuantityValue.equals(that.totalQuantityValue)
        : that.totalQuantityValue != null) {
      return false;
    }
    if (totalQuantityUnit != null ? !totalQuantityUnit.equals(that.totalQuantityUnit)
        : that.totalQuantityUnit != null) {
      return false;
    }
    if (totalQuantitySystem != null ? !totalQuantitySystem.equals(that.totalQuantitySystem)
        : that.totalQuantitySystem != null) {
      return false;
    }
    if (numberOfRepeatsAllows != null ? !numberOfRepeatsAllows.equals(that.numberOfRepeatsAllows)
        : that.numberOfRepeatsAllows != null) {
      return false;
    }
    if (supplyDurationValue != null ? !supplyDurationValue.equals(that.supplyDurationValue)
        : that.supplyDurationValue != null) {
      return false;
    }
    if (supplyDurationUnit != null ? !supplyDurationUnit.equals(that.supplyDurationUnit)
        : that.supplyDurationUnit != null) {
      return false;
    }
    if (supplyDurationSystem != null ? !supplyDurationSystem.equals(that.supplyDurationSystem)
        : that.supplyDurationSystem != null) {
      return false;
    }
    return createdDateUtc != null ? createdDateUtc.equals(that.createdDateUtc)
        : that.createdDateUtc == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (dispenseIntervalMin != null ? dispenseIntervalMin.hashCode() : 0);
    result = 31 * result + (dispenseIntervalMax != null ? dispenseIntervalMax.hashCode() : 0);
    result = 31 * result + (dispenseIntervalUnit != null ? dispenseIntervalUnit.hashCode() : 0);
    result = 31 * result + (dispenseIntervalSystem != null ? dispenseIntervalSystem.hashCode() : 0);
    result = 31 * result + (trialEligibility != null ? trialEligibility.hashCode() : 0);
    result = 31 * result + (firstFillDate != null ? firstFillDate.hashCode() : 0);
    result = 31 * result + (firstFillQuantity != null ? firstFillQuantity.hashCode() : 0);
    result = 31 * result + (firstFillQuantityUnit != null ? firstFillQuantityUnit.hashCode() : 0);
    result =
        31 * result + (firstFillQuantitySystem != null ? firstFillQuantitySystem.hashCode() : 0);
    result =
        31 * result + (firstFillSupplyDuration != null ? firstFillSupplyDuration.hashCode() : 0);
    result = 31 * result
        + (firstFillSupplyDurationUnit != null ? firstFillSupplyDurationUnit.hashCode() : 0);
    result = 31 * result
        + (firstFillSupplyDurationSystem != null ? firstFillSupplyDurationSystem.hashCode() : 0);
    result = 31 * result + (maxDispenseQuantity != null ? maxDispenseQuantity.hashCode() : 0);
    result =
        31 * result + (maxDispenseQuantityUnit != null ? maxDispenseQuantityUnit.hashCode() : 0);
    result = 31 * result
        + (maxDispenseQuantitySystem != null ? maxDispenseQuantitySystem.hashCode() : 0);
    result =
        31 * result + (totalPrescribedQuantity != null ? totalPrescribedQuantity.hashCode() : 0);
    result = 31 * result
        + (totalPrescribedQuantityUnit != null ? totalPrescribedQuantityUnit.hashCode() : 0);
    result = 31 * result
        + (totalPrescribedQuantitySystem != null ? totalPrescribedQuantitySystem.hashCode() : 0);
    result = 31 * result + (validityStart != null ? validityStart.hashCode() : 0);
    result = 31 * result + (validityEnd != null ? validityEnd.hashCode() : 0);
    result = 31 * result + (totalDaysSupply != null ? totalDaysSupply.hashCode() : 0);
    result = 31 * result + (totalQuantityValue != null ? totalQuantityValue.hashCode() : 0);
    result = 31 * result + (totalQuantityUnit != null ? totalQuantityUnit.hashCode() : 0);
    result = 31 * result + (totalQuantitySystem != null ? totalQuantitySystem.hashCode() : 0);
    result = 31 * result + (numberOfRepeatsAllows != null ? numberOfRepeatsAllows.hashCode() : 0);
    result = 31 * result + (supplyDurationValue != null ? supplyDurationValue.hashCode() : 0);
    result = 31 * result + (supplyDurationUnit != null ? supplyDurationUnit.hashCode() : 0);
    result = 31 * result + (supplyDurationSystem != null ? supplyDurationSystem.hashCode() : 0);
    result = 31 * result + (createdDateUtc != null ? createdDateUtc.hashCode() : 0);
    return result;
  }
}
