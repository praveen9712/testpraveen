
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import java.util.Arrays;

/**
 * The SeverityCode enum. Determines different levels of severity for an allergy.
 *
 * <li>{@link #Unknown}</li>
 * <li>{@link #Severe}</li>
 * <li>{@link #Moderate}</li>
 * <li>{@link #Mild}</li>
 */
public enum SeverityCode {

  /**
   * Unknown
   */
  Unknown("UN"),

  /**
   * Severe
   */
  Severe("SV"),

  /**
   * Moderate
   */
  Moderate("MO"),

  /**
   * Mild
   */
  Mild("MI");

  private final String abbreviation;

  SeverityCode(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public static SeverityCode lookup(String abbreviation) {
    return Arrays.stream(values()).filter(e -> e.abbreviation.equals(abbreviation)).findFirst()
        .orElse(null);
  }

  public String getAbbreviation() {
    return abbreviation;
  }

}
