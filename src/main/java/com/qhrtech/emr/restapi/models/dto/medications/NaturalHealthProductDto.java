
package com.qhrtech.emr.restapi.models.dto.medications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDate;

/**
 * The natural health product transfer model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The natural health product transfer model")
public class NaturalHealthProductDto {

  @JsonProperty("productId")
  @Schema(description = "The unique id of the natural health product", example = "1")
  private int productId;

  @JsonProperty("name")
  @Schema(description = "The name of the natural health product", example = "Melatonin")
  private String name;

  @JsonProperty("dosageForm")
  @Schema(description = "The dosage form of the natural health product", example = "Capsule")
  private String dosageForm;

  @JsonProperty("routeCode")
  @Schema(description = "The route code of the natural health product", example = "Oral")
  private String routeCode;

  @JsonProperty("licenceNumber")
  @Schema(description = "The licence number of the natural health product", example = "80000370")
  private String licenceNumber;

  @JsonProperty("licenceDate")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "The license date of the natural health product", example = "2017-11-29")
  private LocalDate licenceDate;

  /**
   * A unique id of a natural health product.
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
   * A name of a natural health product.
   *
   * @documentationExample Melatonin
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
   * A dosage form of a natural health product.
   *
   * @documentationExample Capsule
   *
   * @return
   */
  public String getDosageForm() {
    return dosageForm;
  }

  public void setDosageForm(String dosageForm) {
    this.dosageForm = dosageForm;
  }

  /**
   * A route code of a natural health product.
   *
   * @documentationExample Oral
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
   * A licence number of a natural health product.
   *
   * @documentationExample 80000370
   *
   * @return
   */
  public String getLicenceNumber() {
    return licenceNumber;
  }

  public void setLicenceNumber(String licenceNumber) {
    this.licenceNumber = licenceNumber;
  }

  /**
   * A license date of a natural health product.
   *
   * @return
   */
  @DocumentationExample("2017-11-29")
  @TypeHint(String.class)
  public LocalDate getLicenceDate() {
    return licenceDate;
  }

  public void setLicenceDate(LocalDate licenceDate) {
    this.licenceDate = licenceDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NaturalHealthProductDto)) {
      return false;
    }

    NaturalHealthProductDto that = (NaturalHealthProductDto) o;

    if (getProductId() != that.getProductId()) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getDosageForm() != null ? !getDosageForm().equals(that.getDosageForm())
        : that.getDosageForm() != null) {
      return false;
    }
    if (getRouteCode() != null ? !getRouteCode().equals(that.getRouteCode())
        : that.getRouteCode() != null) {
      return false;
    }
    if (getLicenceNumber() != null ? !getLicenceNumber().equals(that.getLicenceNumber())
        : that.getLicenceNumber() != null) {
      return false;
    }
    return getLicenceDate() != null ? getLicenceDate().equals(that.getLicenceDate())
        : that.getLicenceDate() == null;
  }

  @Override
  public int hashCode() {
    int result = getProductId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getDosageForm() != null ? getDosageForm().hashCode() : 0);
    result = 31 * result + (getRouteCode() != null ? getRouteCode().hashCode() : 0);
    result = 31 * result + (getLicenceNumber() != null ? getLicenceNumber().hashCode() : 0);
    result = 31 * result + (getLicenceDate() != null ? getLicenceDate().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "NaturalHealthProductDto{"
        + "productId=" + productId
        + ", name='" + name + '\''
        + ", dosageForm='" + dosageForm + '\''
        + ", routeCode='" + routeCode + '\''
        + ", licenceNumber='" + licenceNumber + '\''
        + ", licenceDate=" + licenceDate
        + '}';
  }
}
