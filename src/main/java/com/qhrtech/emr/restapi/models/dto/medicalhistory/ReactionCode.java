
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import java.util.Arrays;

/*
 * The ReactionCode enum. Determines different types of reactions for an allergy.
 */
public enum ReactionCode {

  /**
   * Rash Hives
   */
  RashHives("01"),

  /**
   * Maculopapular Rash
   */
  RashMaculopapular("02"),

  /**
   * Contact Rash
   */
  RashLocalContact("03"),

  /**
   * Other Rash
   */
  RashOther("04"),

  /**
   * Anaphylaxis reaction
   */
  Anaphylaxis("05"),

  /**
   * Angioedema
   */
  Angiodema("06"),

  /**
   * Malignant hyperthermia
   */
  MalignantHyperthermia("07"),

  /**
   * Serum sickness
   */
  SerumSickness("08"),

  /**
   * Stevens–Johnson
   */
  StevensJohnson("09"),

  /**
   * Rash
   */
  Rash("10"),

  /**
   * Other reaction
   */
  Other("99");

  private final String code;

  ReactionCode(String code) {
    this.code = code;
  }

  public static ReactionCode lookup(String code) {
    return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst()
        .orElse(null);
  }

  public String getCode() {
    return code;
  }

}
