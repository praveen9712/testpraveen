
package com.qhrtech.emr.restapi.models.dto.prescribeit;

public enum RenewalRequestResponseType {

  UNDER_REVIEW,
  ACCEPTED_WITH_CHANGES,
  ACCEPTED,
  DENIED;

  public static RenewalRequestResponseType getResponseType(String responseType) {
    for (RenewalRequestResponseType type : RenewalRequestResponseType.values()) {
      if (type.name().equalsIgnoreCase(responseType)) {
        return type;
      }
    }
    return null;
  }

}
