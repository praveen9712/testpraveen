
package com.qhrtech.emr.restapi.models.dto.prescribeit;

public enum DispenseNotificationStatus {
  PREPARATION,
  IN_PROGRESS,
  COMPLETED,
  STOPPED;

  public static DispenseNotificationStatus lookup(
      String code) {
    try {
      return DispenseNotificationStatus.valueOf(code);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}

