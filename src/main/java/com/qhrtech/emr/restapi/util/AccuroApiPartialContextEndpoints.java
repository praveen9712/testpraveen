
package com.qhrtech.emr.restapi.util;

/**
 * The http requests which have exception of having full context The ENUM should be of the given
 * example type following three parameters:
 * <p>
 * url: The url should be of type regex, requestType: can be GET, POST, PUT, DELETE
 * isPartialContext: true if this perticular request require only partial accuro context.
 * </p>
 * For example:
 *
 * EXAMPLE("\\/v1\\/provider-portal\\/patients\\/-?\\d+\\/profile-picture", "GET", true); The
 * example url will match with any patient id including negative numbers.
 */
public enum AccuroApiPartialContextEndpoints {
  GET_APPOINTMENT_STATUSES("\\/v\\d+\\/provider-portal\\/scheduler\\/statuses", "POST", true),
  GET_INSURERS("\\/v\\d+\\/provider-portal\\/insurers", "POST", true),
  GET_ACTIVITIES("\\/v\\d+\\/provider-portal\\/patients\\/-?\\d+\\/activity\\/search", "POST",
      true),
  GET_BILLING_DETAILS("\\/v\\d+\\/provider-portal\\/scheduler\\/appointments\\/billing_details",
      "POST", true),
  PORTAL_ENDPOINT("\\/v\\d+\\/portal\\/token", "POST", true),
  SPADE_ALL_REQUESTS("\\/v\\d+\\/spade\\/.*", "ALL", true),
  PATIENT_PORTAL("\\/v\\d+\\/patient-portal\\/.*", "ALL", true),
  GET_ACCESSIBLE_PROVIDERS("\\/v\\d+\\/accessible-providers",
      "GET", false);

  private final String url;
  private final String requestType;
  private final Boolean isPartialContext;

  AccuroApiPartialContextEndpoints(String url, String requestType,
      Boolean isPartialContext) {
    this.url = url;
    this.requestType = requestType;
    this.isPartialContext = isPartialContext;
  }

  public static Boolean lookUpByUrlAndRequestType(String url, String requestType) {
    for (AccuroApiPartialContextEndpoints partialContextEndpoints : values()) {
      if ((url.matches(partialContextEndpoints.url)
          && requestType.equals(partialContextEndpoints.requestType))
          || (url.matches(partialContextEndpoints.url)
              && partialContextEndpoints.requestType.equals("ALL"))) {
        return partialContextEndpoints.isPartialContext;
      }
    }
    return null;
  }

  public String getUrl() {
    return url;
  }

  public String getRequestType() {
    return requestType;
  }

  public Boolean getIsPartialContext() {
    return isPartialContext;
  }
}
