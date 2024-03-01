
package com.qhrtech.emr.restapi.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Objects;

/**
 * Lab data type.
 */
@Schema(description = "Lab data type enum")
public enum LabDataType {

  Unidentified(-1),
  Numeric(1),
  Text(2);

  private final int typeId;

  LabDataType(int typeId) {
    this.typeId = typeId;
  }

  public static LabDataType lookup(Integer typeId) {
    return Arrays.stream(values())
        .filter(t -> Objects.equals(t.typeId, typeId))
        .findFirst()
        .orElse(null);
  }

  public int getTypeId() {
    return typeId;
  }
}
