
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The code value data transfer object")
public class CodeValueDto {

  @JsonProperty(value = "code")
  @Schema(description = "A code of a codeValue", example = "OTH, SYNC")
  private String code;

  @JsonProperty(value = "description")
  @Schema(description = "A description of a codeValue code",
      example = "Other(OTH), Unregistered Provider Detail(UPD)")
  private String description;

  @JsonProperty(value = "deleted")
  @Schema(description = "Indication if codeValue is deleted or not", example = "false")
  private boolean deleted;

  @JsonProperty(value = "codeOrder")
  @Schema(description = "An order value of a list codevalues of same category", example = "1")
  private int codeOrder;

  @JsonProperty(value = "tableId")
  @Schema(description = "An Id which represents a category for codeValues", example = "99P0092")
  private String tableId;

  /**
   * A code of a codevalue.
   *
   * @documentationExample OTH, SYNC
   *
   * @return The codevalue code.
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * A description of a codevalue code.
   *
   * @documentationExample Other(OTH), Unregistered Provider Detail(UPD)
   *
   * @return The codevalue code.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * A flag to indicate the delete state of a codevalue.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * An order value of a list codevalues of same category.
   *
   * @documentationExample 1
   *
   * @return The code order
   */
  public int getCodeOrder() {
    return codeOrder;
  }

  public void setCodeOrder(int codeOrder) {
    this.codeOrder = codeOrder;
  }

  /**
   * An Id represents a category for codevalues.
   *
   * @documentationExample 99P0092
   *
   * @return The table id to which the codevalue belongs to.
   */
  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CodeValueDto)) {
      return false;
    }

    CodeValueDto that = (CodeValueDto) o;

    if (deleted != that.deleted) {
      return false;
    }
    if (codeOrder != that.codeOrder) {
      return false;
    }
    if (code != null ? !code.equals(that.code) : that.code != null) {
      return false;
    }
    if (description != null ? !description.equals(that.description) : that.description != null) {
      return false;
    }
    return tableId != null ? tableId.equals(that.tableId) : that.tableId == null;
  }

  @Override
  public int hashCode() {
    int result = code != null ? code.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (deleted ? 1 : 0);
    result = 31 * result + codeOrder;
    result = 31 * result + (tableId != null ? tableId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CodeValueDto{"
        + "code='" + code + '\''
        + ", description='" + description + '\''
        + ", deleted=" + deleted
        + ", codeOrder=" + codeOrder
        + ", tableId='" + tableId + '\''
        + '}';
  }
}
