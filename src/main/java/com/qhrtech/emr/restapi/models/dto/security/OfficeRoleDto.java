
package com.qhrtech.emr.restapi.models.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public class OfficeRoleDto {

  @JsonProperty("officeId")
  @Schema(description = "The office id")
  private int officeId;

  @JsonProperty("roleIds")
  @Schema(description = "The role ids")
  private Set<Integer> roleIds;

  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  public Set<Integer> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(Set<Integer> roleIds) {
    this.roleIds = roleIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OfficeRoleDto)) {
      return false;
    }

    OfficeRoleDto that = (OfficeRoleDto) o;

    if (getOfficeId() != that.getOfficeId()) {
      return false;
    }
    return getRoleIds() != null ? getRoleIds().equals(that.getRoleIds())
        : that.getRoleIds() == null;
  }

  @Override
  public int hashCode() {
    int result = getOfficeId();
    result = 31 * result + (getRoleIds() != null ? getRoleIds().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("officeRoles{");
    sb.append("officeId=").append(officeId);
    sb.append(", roleIds=").append(roleIds);
    sb.append('}');
    return sb.toString();
  }
}
