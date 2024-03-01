
package com.qhrtech.emr.restapi.models.dto.prescribeit;

public enum MessageStatusCode {

  READ(0),
  UNREAD(1),
  ERROR(2),
  PROCESSED(3),
  UNKNOWN(4);

  private final int value;

  MessageStatusCode(int value) {
    this.value = value;
  }

  public static MessageStatusCode lookupCode(int value) {
    for (MessageStatusCode code : values()) {
      if (code.getValue() == value) {
        return code;
      }
    }
    return null;
  }

  public int getValue() {
    return value;
  }


}
