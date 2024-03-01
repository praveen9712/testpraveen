
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.Arrays;
import javax.ws.rs.core.Response;

/**
 * The enum used to map with the Protoss enum
 * {@link com.qhrtech.emr.accuro.permissions.PermissionsType}
 */
public enum PermissionsType {
  READ_APPOINTMENTS,
  WRITE_APPOINTMENTS;

  public static PermissionsType lookup(String type) {
    for (PermissionsType permissionsType : values()) {
      if (permissionsType.name().equals(type)) {
        return permissionsType;
      }
    }
    return null;
  }

  @JsonCreator
  public static PermissionsType fromString(String permissionType) {
    return Arrays.stream(values()).filter(e -> e.name().equals(permissionType)).findFirst()
        .orElseThrow(() -> Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Invalid permission Type: " + permissionType));
  }

}

