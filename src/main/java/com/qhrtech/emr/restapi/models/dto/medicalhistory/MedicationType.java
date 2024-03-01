
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

/**
 * The enum constants are categorized for determining the type of a drug. The one of the type's
 * COMPOUND defines it as a compound medication of a prescription.
 */
public enum MedicationType {

  /**
   * Manufactured Drug.
   */
  DIN,

  /**
   * Formulation.
   */
  GF,

  /**
   * Alternative Health Product.
   */
  AHP,

  /**
   * Natural Health Product.
   */
  NPN,

  /**
   * Generic Drug.
   */
  GD,

  /**
   * Custom Compound.
   */
  COMPOUND;

  public static MedicationType lookup(String medicationType) {
    for (MedicationType type : values()) {
      if (type.toString().equals(medicationType)) {
        return type;
      }
    }
    return null;
  }

}
