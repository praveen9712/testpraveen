
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.qhrtech.emr.accuro.model.medications.AlternativeHealthProduct;
import com.qhrtech.emr.accuro.model.medications.Formulation;
import com.qhrtech.emr.accuro.model.medications.GenericDrug;
import com.qhrtech.emr.accuro.model.medications.ManufacturedDrug;
import com.qhrtech.emr.accuro.model.prescription.Ingredient;

public enum MedicationTypeApi {
  /**
   * Manufactured Drug
   *
   * @see ManufacturedDrug
   */
  DIN,

  /**
   * Manufactured Drug, active ingredient
   *
   * @see ManufacturedDrug
   */
  AIG,

  /**
   * Formulation
   *
   * @see Formulation
   */
  GCN,

  /**
   * Alternative Health Product
   *
   * @see AlternativeHealthProduct
   */
  AHP,

  /**
   * Natural Health Product
   */
  NPN,

  /**
   * Generic Drug
   *
   * @see GenericDrug
   */
  GD,

  /**
   * Custom Compound
   *
   * @see Ingredient
   */
  HIC;

  public static MedicationTypeApi lookup(String medicationType) {
    for (MedicationTypeApi type : values()) {
      if (type.toString().equals(medicationType)) {
        return type;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    switch (this) {
      case AHP:
        return "";
      default:
        return name();
    }
  }


}
