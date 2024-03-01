
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "The RequestPriority enum "
        + "represents the priority for requests.")
public enum RequestPriority {

  ROUTINE("routine", 0),
  URGENT("urgent", 1),
  ASAP("asap", 2),
  STAT("stat", 3);

  private final String code;
  private final int value;

  RequestPriority(String code, int value) {
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public int getValue() {
    return value;
  }

  /**
   * Lookup a RequestPriority by name.
   *
   * @param name Name of RequestPriority Type
   *
   * @return RequestPriority or null
   */
  public static RequestPriority lookup(String name) {
    for (RequestPriority type : values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }

    return null;
  }
}
