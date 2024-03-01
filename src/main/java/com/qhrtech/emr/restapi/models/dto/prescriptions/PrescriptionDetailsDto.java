
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medications.AlternativeHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.FormulationDto;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.ManufacturedDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.NaturalHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.compounds.CustomCompoundDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The prescription details transfer model.
 */
@Schema(description = "The prescription details transfer model")
public class PrescriptionDetailsDto {
  @JsonProperty("manufacturedDrug")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the manufactured drug. "
      + "It shows when if the prescription has the manufactured drug.")
  private ManufacturedDrugDto manufacturedDrug;

  @JsonProperty("genericDrug")
  @Schema(description = "The prescription has the generic drug. "
      + "It shows when if the prescription has the generic drug.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private GenericDrugDto genericDrug;

  @JsonProperty("naturalHealthProduct")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the natural health product. "
      + "It shows when if the prescription has the natural health product.")
  private NaturalHealthProductDto naturalHealthProduct;

  @JsonProperty("alternativeHealthProduct")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the alternative health product. "
      + "It shows when if the prescription has the alternative health product.")
  private AlternativeHealthProductDto alternativeHealthProduct;

  @JsonProperty("formulation")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the formulation. "
      + "It shows when if the prescription has the formulation.")
  private FormulationDto formulationDto;

  @JsonProperty("customCompound")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the custom compound. "
      + "It shows when if the prescription has the custom compound.")
  private CustomCompoundDto customCompound;

  /**
   * A manufactured drug.
   * 
   * @return
   */
  public ManufacturedDrugDto getManufacturedDrug() {
    return manufacturedDrug;
  }

  public void setManufacturedDrug(
      ManufacturedDrugDto manufacturedDrug) {
    this.manufacturedDrug = manufacturedDrug;
  }

  /**
   * A generic drug.
   *
   * @return
   */
  public GenericDrugDto getGenericDrug() {
    return genericDrug;
  }

  public void setGenericDrug(GenericDrugDto genericDrug) {
    this.genericDrug = genericDrug;
  }

  /**
   * A natural health product.
   *
   * @return
   */
  public NaturalHealthProductDto getNaturalHealthProduct() {
    return naturalHealthProduct;
  }

  public void setNaturalHealthProduct(
      NaturalHealthProductDto naturalHealthProduct) {
    this.naturalHealthProduct = naturalHealthProduct;
  }

  /**
   * An alternative health product.
   *
   * @return
   */
  public AlternativeHealthProductDto getAlternativeHealthProduct() {
    return alternativeHealthProduct;
  }

  public void setAlternativeHealthProduct(
      AlternativeHealthProductDto alternativeHealthProduct) {
    this.alternativeHealthProduct = alternativeHealthProduct;
  }

  /**
   * A formulation.
   *
   * <p>
   * {@code null} if the prescribed medication is not a formulation.
   * </p>
   *
   * @return
   */
  public FormulationDto getFormulation() {
    return formulationDto;
  }

  public void setFormulation(
      FormulationDto formulationDto) {
    this.formulationDto = formulationDto;
  }

  /**
   * A custom compound.
   *
   * @return
   */
  public CustomCompoundDto getCustomCompound() {
    return customCompound;
  }

  public void setCustomCompound(
      CustomCompoundDto customCompound) {
    this.customCompound = customCompound;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PrescriptionDetailsDto)) {
      return false;
    }

    PrescriptionDetailsDto that = (PrescriptionDetailsDto) o;

    if (getManufacturedDrug() != null ? !getManufacturedDrug().equals(that.getManufacturedDrug())
        : that.getManufacturedDrug() != null) {
      return false;
    }
    if (getGenericDrug() != null ? !getGenericDrug().equals(that.getGenericDrug())
        : that.getGenericDrug() != null) {
      return false;
    }
    if (getNaturalHealthProduct() != null ? !getNaturalHealthProduct()
        .equals(that.getNaturalHealthProduct()) : that.getNaturalHealthProduct() != null) {
      return false;
    }
    if (getAlternativeHealthProduct() != null ? !getAlternativeHealthProduct()
        .equals(that.getAlternativeHealthProduct()) : that.getAlternativeHealthProduct() != null) {
      return false;
    }
    if (getFormulation() != null ? !getFormulation().equals(that.getFormulation())
        : that.getFormulation() != null) {
      return false;
    }
    return getCustomCompound() != null ? getCustomCompound().equals(that.getCustomCompound())
        : that.getCustomCompound() == null;
  }

  @Override
  public int hashCode() {
    int result = getManufacturedDrug() != null ? getManufacturedDrug().hashCode() : 0;
    result = 31 * result + (getGenericDrug() != null ? getGenericDrug().hashCode() : 0);
    result =
        31 * result + (getNaturalHealthProduct() != null ? getNaturalHealthProduct().hashCode()
            : 0);
    result = 31 * result + (getAlternativeHealthProduct() != null ? getAlternativeHealthProduct()
        .hashCode() : 0);
    result = 31 * result + (getFormulation() != null ? getFormulation().hashCode() : 0);
    result = 31 * result + (getCustomCompound() != null ? getCustomCompound().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PrescriptionDetailsDto{"
        + "manufacturedDrug=" + manufacturedDrug
        + ", genericDrug=" + genericDrug
        + ", naturalHealthProduct=" + naturalHealthProduct
        + ", alternativeHealthProduct=" + alternativeHealthProduct
        + ", formulationDto=" + formulationDto
        + ", customCompound=" + customCompound
        + '}';
  }
}
