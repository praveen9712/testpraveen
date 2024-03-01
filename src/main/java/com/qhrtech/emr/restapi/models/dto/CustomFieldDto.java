
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.customfield.CustomFieldType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Custom fields which can used for the patients.")
public class CustomFieldDto {

  @JsonProperty("name")
  @Schema(description = "Custom field name", example = "Custom Field")
  private String name;
  @JsonProperty("type")
  @Schema(description = "Type is either TEXT or DROPDOWN", example = "TEXT")
  private CustomFieldType type;
  @JsonProperty("order")
  @Schema(description = "Custom field order", example = "1")
  private Integer order;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomFieldDto)) {
      return false;
    }
    CustomFieldDto that = (CustomFieldDto) o;
    return Objects.equals(getName(), that.getName())
        && getType() == that.getType()
        && Objects.equals(getOrder(), that.getOrder());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getType(), getOrder());
  }

  public CustomFieldType getType() {
    return type;
  }

  public void setType(CustomFieldType type) {
    this.type = type;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }


}
