
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The MessagingSubtype enum. Defines the messaging sub type for conversation message.
 */
@Schema(
    description = "The MessagingSubtype enum. Defines the "
        + "messaging sub type for conversation message.")
public enum MessagingSubtype {

  NONE,
  ERX_RENEWAL;

  /**
   * Lookup a Messaging Subtype by name.
   *
   * @param name Name of Messaging Subtype
   *
   * @return Messaging Subtype or null
   */
  public static MessagingSubtype lookup(String name) {
    for (MessagingSubtype type : values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }
    return null;
  }

}
