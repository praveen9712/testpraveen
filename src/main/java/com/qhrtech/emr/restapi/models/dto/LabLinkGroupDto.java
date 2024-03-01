
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;

/**
 * The Lab Link Group model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab link group data transfer object model")
public class LabLinkGroupDto {

  @JsonProperty("linkGroupId")
  @Schema(description = "Unique id for lab link group.", example = "34")
  private int linkGroupId;

  @JsonProperty("primaryResultId")
  @Schema(description = "Id of the primary lab result.", example = "3")
  private int primaryResultId;

  @JsonProperty("linkedLabResultIds")
  @Schema(
      description = "Set of all the lab results ids linked to the group including"
          + " the primary result id.")
  private Set<Integer> linkedLabResultIds;

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

  public Set<Integer> getLinkedLabResultIds() {
    return linkedLabResultIds;
  }

  public void setLinkedLabResultIds(Set<Integer> linkedLabResultIds) {
    this.linkedLabResultIds = linkedLabResultIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LabLinkGroupDto that = (LabLinkGroupDto) o;

    if (linkGroupId != that.linkGroupId) {
      return false;
    }
    if (primaryResultId != that.primaryResultId) {
      return false;
    }
    return Objects.equals(linkedLabResultIds, that.linkedLabResultIds);
  }

  @Override
  public int hashCode() {
    int result = linkGroupId;
    result = 31 * result + primaryResultId;
    result = 31 * result + (linkedLabResultIds != null ? linkedLabResultIds.hashCode() : 0);
    return result;
  }
}
