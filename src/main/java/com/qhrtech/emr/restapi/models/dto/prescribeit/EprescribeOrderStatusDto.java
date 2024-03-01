
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Eprescribe Order Status Data transfer object")
public class EprescribeOrderStatusDto {

  @JsonProperty("id")
  @Schema(description = "The e-prescribe order status id", example = "1")
  private int id;

  @JsonProperty("prescriptionId")
  @Schema(description = "The e-prescribe order status id", example = "1")
  @NotNull
  private Integer prescriptionId;

  @JsonProperty("system")
  @Schema(description = "The e-prescribe order status id", example = "PrescribeIt")
  @NotNull
  private EprescribeOrderStatusSystem system;

  @JsonProperty("status")
  @Schema(description = "The e-prescribe order status.",
      example = "Discontinued: Pharmacy denied request")
  @NotNull
  @Size(max = 255)
  private String status;

  @JsonProperty("createdDate")
  @Schema(description = "The e-prescribe order status date of creation in UTC. Read only field.",
      example = "1")
  private LocalDateTime createdDate;

  /**
   * The e-prescribe order status id
   *
   * @return {@link Integer} The e-prescribe order status id
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Prescription ID
   *
   * @return {@link Integer} Prescription ID
   * @documentationExample 1
   */
  public Integer getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(Integer prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * Eprescribe order status system
   *
   * @return {@link EprescribeOrderStatusSystem} Eprescribe order status system
   * @documentationExample prescribeIt
   */
  public EprescribeOrderStatusSystem getSystem() {
    return system;
  }

  public void setSystem(EprescribeOrderStatusSystem system) {
    this.system = system;
  }

  /**
   * Eprescribe order status
   *
   * @return {@link String} Eprescribe order status
   * @documentationExample Discontinued: Pharmacy denied request
   */
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Eprescribe order status created date
   *
   * @return {@link LocalDateTime} Eprescribe order status created date
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EprescribeOrderStatusDto that = (EprescribeOrderStatusDto) o;

    if (id != that.id) {
      return false;
    }
    if (!Objects.equals(prescriptionId, that.prescriptionId)) {
      return false;
    }
    if (system != that.system) {
      return false;
    }
    if (!Objects.equals(status, that.status)) {
      return false;
    }
    return Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (prescriptionId != null ? prescriptionId.hashCode() : 0);
    result = 31 * result + (system != null ? system.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("EprescribeOrderStatusDto{");
    sb.append("id=").append(id);
    sb.append(", prescriptionId=").append(prescriptionId);
    sb.append(", system=").append(system);
    sb.append(", status='").append(status).append('\'');
    sb.append(", createdDate=").append(createdDate);
    sb.append('}');
    return sb.toString();
  }
}
