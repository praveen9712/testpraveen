
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.provider.ProviderIdentifier;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The provider identifier data transfer object.")
public class ProviderIdentifierDto {

  @JsonProperty("type")
  @Schema(description = "Provider identifier type", example = "PractitionerNumber")
  private ProviderIdentifier.Type type;

  @JsonProperty("value")
  @Schema(description = "Provider identifier value", example = "11122")
  private String value;

  /**
   * Provider Identifier Type.
   *
   * @return ProviderIdentifier.Type
   * @documentationExample PractitionerNumber
   */
  public ProviderIdentifier.Type getType() {
    return type;
  }

  public void setType(ProviderIdentifier.Type type) {
    this.type = type;
  }

  /**
   * Value for Provider Identifier Type.
   *
   * @return String.
   * @documentationExample 11122
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProviderIdentifierDto that = (ProviderIdentifierDto) o;

    if (type != that.type) {
      return false;
    }
    return value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
