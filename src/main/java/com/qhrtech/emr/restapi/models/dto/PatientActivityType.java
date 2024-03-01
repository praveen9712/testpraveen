
package com.qhrtech.emr.restapi.models.dto;

/**
 * Type of patient activity being logged.
 */
public enum PatientActivityType {

  /**
   * Represents an event where a patient has looked at their own lab
   */
  VIEWED;

  /**
   * Lookup a patient activity type by name.
   *
   * @param name Name of patient activity type
   *
   * @return Patient activity type or null
   */
  public static PatientActivityType lookup(String name) {
    for (PatientActivityType type : values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }
    return null;
  }
}
