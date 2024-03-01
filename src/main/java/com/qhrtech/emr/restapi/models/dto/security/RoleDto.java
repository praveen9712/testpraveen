
package com.qhrtech.emr.restapi.models.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Role data transfer object.
 */
public class RoleDto {
  @JsonProperty("roleId")
  @Schema(description = "Unique id of the role")
  private int roleId;

  @JsonProperty("name")
  @Schema(description = "Name of the role")
  private String name;

  @JsonProperty("builtIn")
  @Schema(description = "Is built in or custom role")
  private boolean builtIn;

  @JsonProperty("active")
  @Schema(description = "Is active or inactive role")
  private boolean active;

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isBuiltIn() {
    return builtIn;
  }

  public void setBuiltIn(boolean builtIn) {
    this.builtIn = builtIn;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RoleDto)) {
      return false;
    }

    RoleDto roleDto = (RoleDto) o;

    if (getRoleId() != roleDto.getRoleId()) {
      return false;
    }
    if (isBuiltIn() != roleDto.isBuiltIn()) {
      return false;
    }
    if (isActive() != roleDto.isActive()) {
      return false;
    }
    return getName() != null ? getName().equals(roleDto.getName()) : roleDto.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getRoleId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (isBuiltIn() ? 1 : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("RoleDto{");
    sb.append("roleId=").append(roleId);
    sb.append(", name='").append(name).append('\'');
    sb.append(", builtIn=").append(builtIn);
    sb.append(", active=").append(active);
    sb.append('}');
    return sb.toString();
  }
}
