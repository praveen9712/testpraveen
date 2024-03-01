
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public class MedicationDetails {

  @JsonProperty("manufacturedDrug")
  @Schema(description = "Manufactured drug details.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ManufacturedDrugSummaryDto manufacturedDrug;

  @JsonProperty("genericDrug")
  @Schema(description = "Generic drug details.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private GenericDrugDto genericDrug;

  @JsonProperty("formulation")
  @Schema(description = "Medication formulation details.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private FormulationSummaryDto formulation;

  @JsonProperty("naturalHealthProduct")
  @Schema(description = "Natural health product details.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private NaturalHealthProductSummaryDto naturalHealthProduct;

  @JsonProperty("alternativeHealthProduct")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The prescription has the alternative health product. "
      + "It shows when if the prescription has the alternative health product.")
  private AlternativeHealthProductSummaryDto alternativeHealthProduct;

  @JsonProperty("customCompound")
  @Schema(description = "Custom compound details.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private CustomCompoundSummaryDto customCompound;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MedicationDetails that = (MedicationDetails) o;

    if (!Objects.equals(manufacturedDrug, that.manufacturedDrug)) {
      return false;
    }
    if (!Objects.equals(genericDrug, that.genericDrug)) {
      return false;
    }
    if (!Objects.equals(formulation, that.formulation)) {
      return false;
    }
    if (!Objects.equals(naturalHealthProduct, that.naturalHealthProduct)) {
      return false;
    }
    if (!Objects.equals(alternativeHealthProduct, that.alternativeHealthProduct)) {
      return false;
    }
    return Objects.equals(customCompound, that.customCompound);
  }

  @Override
  public int hashCode() {
    int result = manufacturedDrug != null ? manufacturedDrug.hashCode() : 0;
    result = 31 * result + (genericDrug != null ? genericDrug.hashCode() : 0);
    result = 31 * result + (formulation != null ? formulation.hashCode() : 0);
    result = 31 * result + (naturalHealthProduct != null ? naturalHealthProduct.hashCode() : 0);
    result =
        31 * result + (alternativeHealthProduct != null ? alternativeHealthProduct.hashCode() : 0);
    result = 31 * result + (customCompound != null ? customCompound.hashCode() : 0);
    return result;
  }

  public ManufacturedDrugSummaryDto getManufacturedDrug() {
    return manufacturedDrug;
  }

  public void setManufacturedDrug(
      ManufacturedDrugSummaryDto manufacturedDrug) {
    this.manufacturedDrug = manufacturedDrug;
  }

  public GenericDrugDto getGenericDrug() {
    return genericDrug;
  }

  public void setGenericDrug(GenericDrugDto genericDrug) {
    this.genericDrug = genericDrug;
  }

  public FormulationSummaryDto getFormulation() {
    return formulation;
  }

  public void setFormulation(
      FormulationSummaryDto formulation) {
    this.formulation = formulation;
  }

  public NaturalHealthProductSummaryDto getNaturalHealthProduct() {
    return naturalHealthProduct;
  }

  public void setNaturalHealthProduct(
      NaturalHealthProductSummaryDto naturalHealthProduct) {
    this.naturalHealthProduct = naturalHealthProduct;
  }

  public AlternativeHealthProductSummaryDto getAlternativeHealthProduct() {
    return alternativeHealthProduct;
  }

  public void setAlternativeHealthProduct(
      AlternativeHealthProductSummaryDto alternativeHealthProduct) {
    this.alternativeHealthProduct = alternativeHealthProduct;
  }

  public CustomCompoundSummaryDto getCustomCompound() {
    return customCompound;
  }

  public void setCustomCompound(
      CustomCompoundSummaryDto customCompound) {
    this.customCompound = customCompound;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MedicationDetails{");
    sb.append("manufacturedDrug=").append(manufacturedDrug);
    sb.append(", genericDrug=").append(genericDrug);
    sb.append(", formulation=").append(formulation);
    sb.append(", naturalHealthProduct=").append(naturalHealthProduct);
    sb.append(", alternativeHealthProduct=").append(alternativeHealthProduct);
    sb.append(", customCompound=").append(customCompound);
    sb.append('}');
    return sb.toString();
  }
}
