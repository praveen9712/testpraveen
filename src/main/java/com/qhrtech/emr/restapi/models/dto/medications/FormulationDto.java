
package com.qhrtech.emr.restapi.models.dto.medications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.medications.fdb.FdbCcddMapping;
import com.qhrtech.emr.restapi.models.dto.medications.fdb.FdbCcddMappingDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * The formulation transfer model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The formulation transfer model")
public class FormulationDto {

  @JsonProperty("formulationId")
  @Schema(description = "The formulation id", example = "1")
  private int formulationId;

  @JsonProperty("genericId")
  @Schema(description = "The generic id", example = "1")
  private int genericId;

  @JsonProperty("name")
  @Schema(description = "The formulation name", example = "250 mg Oral Capsule")
  private String name;

  @JsonProperty("systemicCode")
  @Schema(description = "The systemic code", example = "S")
  private String systemicCode;

  @JsonProperty("routeCode")
  @Schema(description = "The route code. "
      + "For the details of code, look up values of code table, 99P0101.", example = "IJ")
  private String routeCode;

  @JsonProperty("formCode")
  @Schema(description = "The form code. "
      + "For the details of code, look up values of code table, 99P0101.", example = "IJ")
  private String formCode;

  @JsonProperty("commonForm")
  @Schema(description = "The common form", example = "AER")
  private String commonForm;

  @JsonProperty("strengthDescription")
  @Schema(description = "The strength description", example = "2 mg/mL")
  private String strengthDescription;

  @JsonProperty("fdbCcddMappings")
  @Schema(description = "The list of FDB CCDD mappings")
  private List<FdbCcddMappingDto> fdbCcddMappings;

  /**
   * A formulation id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getFormulationId() {
    return formulationId;
  }

  public void setFormulationId(int formulationId) {
    this.formulationId = formulationId;
  }

  /**
   * A generic id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getGenericId() {
    return genericId;
  }

  public void setGenericId(int genericId) {
    this.genericId = genericId;
  }

  /**
   * A formulation name.
   *
   * @documentationExample metyrapone 250 mg Oral Capsule
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
   * A systemic code.
   * 
   * @documentationExample S
   *
   * @return
   */
  public String getSystemicCode() {
    return systemicCode;
  }

  public void setSystemicCode(String systemicCode) {
    this.systemicCode = systemicCode;
  }

  /**
   * A route code.
   *
   * <p>
   * For the details of code, look up values of code table, {@code 99P0101}.
   * </p>
   *
   * @documentationExample IJ
   *
   * @return
   */
  public String getRouteCode() {
    return routeCode;
  }

  public void setRouteCode(String routeCode) {
    this.routeCode = routeCode;
  }

  /**
   * A form code.
   *
   * <p>
   * For the details of code, look up values of code table, {@code 99P0101}.
   * </p>
   *
   * @documentationExample CA
   *
   * @return
   */
  public String getFormCode() {
    return formCode;
  }

  public void setFormCode(String formCode) {
    this.formCode = formCode;
  }

  /**
   * A common form.
   *
   * @documentationExample AER
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
   * A strength description
   *
   * @documentationExample 2 mg/mL
   *
   * @return
   */
  public String getStrengthDescription() {
    return strengthDescription;
  }

  public void setStrengthDescription(String strengthDescription) {
    this.strengthDescription = strengthDescription;
  }

  /**
   * The list of FDB CCDD mappings and this is read-only entity
   * 
   * @return The list of FDB CCDD mappings
   */
  public List<FdbCcddMappingDto> getFdbCcddMappings() {
    return fdbCcddMappings;
  }

  public void setFdbCcddMappings(
      List<FdbCcddMappingDto> fdbCcddMappings) {
    this.fdbCcddMappings = fdbCcddMappings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FormulationDto)) {
      return false;
    }

    FormulationDto that = (FormulationDto) o;

    if (getFormulationId() != that.getFormulationId()) {
      return false;
    }
    if (getGenericId() != that.getGenericId()) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getSystemicCode() != null ? !getSystemicCode().equals(that.getSystemicCode())
        : that.getSystemicCode() != null) {
      return false;
    }
    if (getRouteCode() != null ? !getRouteCode().equals(that.getRouteCode())
        : that.getRouteCode() != null) {
      return false;
    }
    if (getFormCode() != null ? !getFormCode().equals(that.getFormCode())
        : that.getFormCode() != null) {
      return false;
    }
    if (getCommonForm() != null ? !getCommonForm().equals(that.getCommonForm())
        : that.getCommonForm() != null) {
      return false;
    }
    if (getStrengthDescription() != null
        ? !getStrengthDescription().equals(that.getStrengthDescription())
        : that.getStrengthDescription() != null) {
      return false;
    }
    return getFdbCcddMappings() != null ? getFdbCcddMappings()
        .equals(that.getFdbCcddMappings()) : that.getFdbCcddMappings() == null;
  }

  @Override
  public int hashCode() {
    int result = getFormulationId();
    result = 31 * result + getGenericId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getSystemicCode() != null ? getSystemicCode().hashCode() : 0);
    result = 31 * result + (getRouteCode() != null ? getRouteCode().hashCode() : 0);
    result = 31 * result + (getFormCode() != null ? getFormCode().hashCode() : 0);
    result = 31 * result + (getCommonForm() != null ? getCommonForm().hashCode() : 0);
    result =
        31 * result + (getStrengthDescription() != null ? getStrengthDescription().hashCode() : 0);
    result =
        31 * result + (getFdbCcddMappings() != null ? getFdbCcddMappings().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "FormulationDto{"
        + "formulationId=" + formulationId
        + ", genericId=" + genericId
        + ", name='" + name + '\''
        + ", systemicCode='" + systemicCode + '\''
        + ", routeCode='" + routeCode + '\''
        + ", formCode='" + formCode + '\''
        + ", commonForm='" + commonForm + '\''
        + ", strengthDescription='" + strengthDescription + '\''
        + ", fdbCcddMappings='" + fdbCcddMappings + '\''
        + '}';
  }
}
