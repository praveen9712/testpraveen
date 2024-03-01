
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The MessageType enum. Defines the message type for conversation.
 */
@Schema(description = "The MessageType enum. Defines the message type for conversation.")
public enum MessageType {

  EREFERRAL,
  ECONSULT,
  INTERNAL,
  CROSS_OFFICE,
  MEDEO_MESSAGING,
  ERX_MESSAGING,
  PROVIDER_MESSAGING;

  /**
   * Lookup a Message type by name.
   *
   * @param name Name of Message type
   *
   * @return Message type or null
   */
  public static MessageType lookup(String name) {
    for (MessageType type : values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }
    return null;
  }

}
