
package com.qhrtech.emr.restapi.models.dto.medications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An alternative health product transfer model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The alternative health product transfer model")
public class AlternativeHealthProductDto {

  @JsonProperty("productId")
  @Schema(description = "The alternative health product id", example = "1")
  private int productId;

  @JsonProperty("name")
  @Schema(description = "The unique name of the alternative health product",
      example = "QHR SAMPLE PRODUCT 1MG")
  private String name;

  @JsonProperty("description")
  @Schema(description = "The description about the alternative health product",
      example = "THIS IS A SAMPLE PRODUCT")
  private String description;

  @JsonProperty("category")
  @Schema(description = "The category of the alternative health product",
      example = "TEMPORARY")
  private String category;

  @JsonProperty("productForm")
  @Schema(description = "The form of the alternative health product", example = "AEROSOL")
  private String productForm;

  @JsonProperty("dispenseUnit")
  @Schema(description = "The dispensed unit of the alternative health product", example = "AER")
  private String dispenseUnit;

  @JsonProperty("defaultDosageUnit")
  @Schema(description = "The dosage unit of the alternative health product", example = "AER")
  private String defaultDosageUnit;

  @JsonProperty("routeCode")
  @Schema(description = "The route code of the alternative health product",
      example = "Apply Externally")
  private String routeCode;

  /**
   * An alternative health product id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  /**
   * A unique name of an alternative health product.
   *
   * @documentationExample QHR SAMPLE PRODUCT 1MG
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
   * A description about an alternative health product.
   *
   * @documentationExample THIS IS A SAMPLE PRODUCT
   *
   * @return
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * A category of an alternative health product.
   *
   * @documentationExample TEMPORARY
   *
   * @return
   */
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * A form of an alternative health product.
   *
   * @documentationExample AEROSOL
   *
   * @return
   */
  public String getProductForm() {
    return productForm;
  }

  public void setProductForm(String productForm) {
    this.productForm = productForm;
  }

  /**
   * A dispensed unit of an alternative health product.
   *
   * @documentationExample AER
   *
   * @return
   */
  public String getDispenseUnit() {
    return dispenseUnit;
  }

  public void setDispenseUnit(String dispenseUnit) {
    this.dispenseUnit = dispenseUnit;
  }

  /**
   * A dosage unit of an alternative health product.
   *
   * @documentationExample AER
   *
   * @return
   */
  public String getDefaultDosageUnit() {
    return defaultDosageUnit;
  }

  public void setDefaultDosageUnit(String defaultDosageUnit) {
    this.defaultDosageUnit = defaultDosageUnit;
  }

  /**
   * A route code of an alternative health product.
   *
   * @documentationExample Apply Externally
   *
   * @return
   */
  public String getRouteCode() {
    return routeCode;
  }

  public void setRouteCode(String routeCode) {
    this.routeCode = routeCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AlternativeHealthProductDto)) {
      return false;
    }

    AlternativeHealthProductDto that = (AlternativeHealthProductDto) o;

    if (getProductId() != that.getProductId()) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getDescription() != null ? !getDescription().equals(that.getDescription())
        : that.getDescription() != null) {
      return false;
    }
    if (getCategory() != null ? !getCategory().equals(that.getCategory())
        : that.getCategory() != null) {
      return false;
    }
    if (getProductForm() != null ? !getProductForm().equals(that.getProductForm())
        : that.getProductForm() != null) {
      return false;
    }
    if (getDispenseUnit() != null ? !getDispenseUnit().equals(that.getDispenseUnit())
        : that.getDispenseUnit() != null) {
      return false;
    }
    if (getDefaultDosageUnit() != null ? !getDefaultDosageUnit().equals(that.getDefaultDosageUnit())
        : that.getDefaultDosageUnit() != null) {
      return false;
    }
    return getRouteCode() != null ? getRouteCode().equals(that.getRouteCode())
        : that.getRouteCode() == null;
  }

  @Override
  public int hashCode() {
    int result = getProductId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
    result = 31 * result + (getProductForm() != null ? getProductForm().hashCode() : 0);
    result = 31 * result + (getDispenseUnit() != null ? getDispenseUnit().hashCode() : 0);
    result = 31 * result + (getDefaultDosageUnit() != null ? getDefaultDosageUnit().hashCode() : 0);
    result = 31 * result + (getRouteCode() != null ? getRouteCode().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AlternativeHealthProductDto{"
        + "productId=" + productId
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + ", category='" + category + '\''
        + ", productForm='" + productForm + '\''
        + ", dispenseUnit='" + dispenseUnit + '\''
        + ", defaultDosageUnit='" + defaultDosageUnit + '\''
        + ", routeCode='" + routeCode + '\''
        + '}';
  }
}
