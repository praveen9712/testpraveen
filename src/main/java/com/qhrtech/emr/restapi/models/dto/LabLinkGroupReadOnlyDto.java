
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;

/**
 * The Lab Link Group model. This is read only object includes set of {@link LabResultDto}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab link group data transfer object model")
public class LabLinkGroupReadOnlyDto {

  @JsonProperty("linkGroupId")
  @Schema(description = "Unique id for lab link group.", example = "34")
  private int linkGroupId;

  @JsonProperty("primaryResultId")
  @Schema(description = "Id of the primary lab result.", example = "3")
  private int primaryResultId;

  @JsonProperty("linkedLabResults")
  @Schema(description = "Set of all the lab results linked to the group.")
  private Set<LabResultDto> linkedLabResults;

  public int getLinkGroupId() {
    return linkGroupId;
  }

  public void setLinkGroupId(int linkGroupId) {
    this.linkGroupId = linkGroupId;
  }

  public int getPrimaryResultId() {
    return primaryResultId;
  }

  public void setPrimaryResultId(int primaryResultId) {
    this.primaryResultId = primaryResultId;
  }

  public Set<LabResultDto> getLinkedLabResults() {
    return linkedLabResults;
  }

  public void setLinkedLabResults(
      Set<LabResultDto> linkedLabResults) {
    this.linkedLabResults = linkedLabResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LabLinkGroupReadOnlyDto that = (LabLinkGroupReadOnlyDto) o;

    if (linkGroupId != that.linkGroupId) {
      return false;
    }
    if (primaryResultId != that.primaryResultId) {
      return false;
    }
    return Objects.equals(linkedLabResults, that.linkedLabResults);
  }

  @Override
  public int hashCode() {
    int result = linkGroupId;
    result = 31 * result + primaryResultId;
    result = 31 * result + (linkedLabResults != null ? linkedLabResults.hashCode() : 0);
    return result;
  }
}
