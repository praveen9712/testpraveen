
package com.qhrtech.emr.restapi.models.dto.medications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDate;

/**
 * The manufactured drug transfer model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The manufactured drug transfer model")
public class ManufacturedDrugDto {

  @JsonProperty("drugIdentificationNumber")
  @Schema(description = "The unique drug identification number "
      + "as known as DIN of the manufacture drug",
      example = "00000001")
  private String drugIdentificationNumber;

  @JsonProperty("name")
  @Schema(description = "The name of the manufactured drug", example = "QHR SAMPLE 1MG TABLET")
  private String name;

  @JsonProperty("manufacturer")
  @Schema(description = "The manufacturer of the manufactured drug", example = "QHR PHARMA")
  private String manufacturer;

  @JsonProperty("effectiveDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "The effective date", example = "2017-11-29")
  private LocalDate effectiveDate;

  @JsonProperty("endDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "The end date", example = "2017-11-29")
  private LocalDate endDate;

  @JsonProperty("activeIngredientNumber")
  @Schema(description = "The number of the active ingredient as known as AIG",
      example = "0000000001")
  private String activeIngredientNumber;

  @JsonProperty("drugSchedule")
  @Schema(description = "The classification code of the manufactured drug", example = "P")
  private String drugSchedule;

  @JsonProperty("formulationId")
  @Schema(description = "The formulation id of the manufactured drug", example = "1")
  private Integer formulationId;

  @JsonProperty("commonForm")
  @Schema(description = "The common form of the manufactured drug.", example = "COMMON FORM")
  private String commonForm;

  @JsonProperty("triplicate")
  @Schema(description = "The flag if the manufactured drug is triplicate", example = "false")
  private boolean triplicate;

  @JsonProperty("asDirected")
  @Schema(description = "The flag if the manufactured drug is as directed", example = "true")
  private boolean asDirected;

  /**
   * A unique drug identification number as known as DIN of a manufacture drug.
   *
   * @documentationExample 00000001
   *
   * @return
   */
  public String getDrugIdentificationNumber() {
    return drugIdentificationNumber;
  }

  public void setDrugIdentificationNumber(String drugIdentificationNumber) {
    this.drugIdentificationNumber = drugIdentificationNumber;
  }

  /**
   * A name of a manufactured drug.
   *
   * @documentationExample QHR SAMPLE 1MG TABLET
   *
   * @return
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * A manufacturer of a manufactured drug.
   *
   * @documentationExample QHR PHARMA
   *
   * @return
   */
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  /**
   * An effective date
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDate effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  /**
   * An end date
   * 
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   * The number of an active ingredient as known as AIG.
   *
   * @documentationExample 0000000001
   *
   * @return
   */
  public String getActiveIngredientNumber() {
    return activeIngredientNumber;
  }

  public void setActiveIngredientNumber(String activeIngredientNumber) {
    this.activeIngredientNumber = activeIngredientNumber;
  }

  /**
   * A classification code of a manufactured drug.
   *
   * @documentationExample P
   *
   * @return
   */
  public String getDrugSchedule() {
    return drugSchedule;
  }

  public void setDrugSchedule(String drugSchedule) {
    this.drugSchedule = drugSchedule;
  }

  /**
   * A formulation id of a manufactured drug.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getFormulationId() {
    return formulationId;
  }

  public void setFormulationId(Integer formulationId) {
    this.formulationId = formulationId;
  }

  /**
   * A common form of a manufactured drug.
   *
   * @documentationExample COMMON FORM
   *
   * @return
   */
  public String getCommonForm() {
    return commonForm;
  }

  public void setCommonForm(String commonForm) {
    this.commonForm = commonForm;
  }

  /**
   * A flag if a manufactured drug is triplicate.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isTriplicate() {
    return triplicate;
  }

  public void setTriplicate(boolean triplicate) {
    this.triplicate = triplicate;
  }

  /**
   * A flag if a manufactured drug is as directed.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isAsDirected() {
    return asDirected;
  }

  public void setAsDirected(boolean asDirected) {
    this.asDirected = asDirected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ManufacturedDrugDto)) {
      return false;
    }

    ManufacturedDrugDto that = (ManufacturedDrugDto) o;

    if (isTriplicate() != that.isTriplicate()) {
      return false;
    }
    if (isAsDirected() != that.isAsDirected()) {
      return false;
    }
    if (getDrugIdentificationNumber() != null ? !getDrugIdentificationNumber()
        .equals(that.getDrugIdentificationNumber()) : that.getDrugIdentificationNumber() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getManufacturer() != null ? !getManufacturer().equals(that.getManufacturer())
        : that.getManufacturer() != null) {
      return false;
    }
    if (getEffectiveDate() != null ? !getEffectiveDate().equals(that.getEffectiveDate())
        : that.getEffectiveDate() != null) {
      return false;
    }
    if (getEndDate() != null ? !getEndDate().equals(that.getEndDate())
        : that.getEndDate() != null) {
      return false;
    }
    if (getActiveIngredientNumber() != null ? !getActiveIngredientNumber()
        .equals(that.getActiveIngredientNumber()) : that.getActiveIngredientNumber() != null) {
      return false;
    }
    if (getDrugSchedule() != null ? !getDrugSchedule().equals(that.getDrugSchedule())
        : that.getDrugSchedule() != null) {
      return false;
    }
    if (getFormulationId() != null ? !getFormulationId().equals(that.getFormulationId())
        : that.getFormulationId() != null) {
      return false;
    }
    return getCommonForm() != null ? getCommonForm().equals(that.getCommonForm())
        : that.getCommonForm() == null;
  }

  @Override
  public int hashCode() {
    int result =
        getDrugIdentificationNumber() != null ? getDrugIdentificationNumber().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getManufacturer() != null ? getManufacturer().hashCode() : 0);
    result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
    result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
    result =
        31 * result + (getActiveIngredientNumber() != null ? getActiveIngredientNumber().hashCode()
            : 0);
    result = 31 * result + (getDrugSchedule() != null ? getDrugSchedule().hashCode() : 0);
    result = 31 * result + (getFormulationId() != null ? getFormulationId().hashCode() : 0);
    result = 31 * result + (getCommonForm() != null ? getCommonForm().hashCode() : 0);
    result = 31 * result + (isTriplicate() ? 1 : 0);
    result = 31 * result + (isAsDirected() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ManufacturedDrugDto{"
        + "drugIdentificationNumber='" + drugIdentificationNumber + '\''
        + ", name='" + name + '\''
        + ", manufacturer='" + manufacturer + '\''
        + ", effectiveDate=" + effectiveDate
        + ", endDate=" + endDate
        + ", activeIngredientNumber='" + activeIngredientNumber + '\''
        + ", drugSchedule='" + drugSchedule + '\''
        + ", formulationId=" + formulationId
        + ", commonForm='" + commonForm + '\''
        + ", triplicate=" + triplicate
        + ", asDirected=" + asDirected
        + '}';
  }
}
