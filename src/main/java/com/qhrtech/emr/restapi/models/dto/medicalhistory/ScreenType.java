
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import java.util.Arrays;

/**
 * The ScreenType enum. Determines different screening types for an allergy contraindication.
 * 
 * <li>{@link #AllergyToDrug}</li>
 * <li>{@link #ConcurrentDifferentUnits}</li>
 * <li>{@link #DiseaseToDrug}</li>
 * <li>{@link #DosageAlert}</li>
 * <li>{@link #DrugAllergen}</li>
 * <li>{@link #DrugIntolerance}</li>
 * <li>{@link #DrugToDisease}</li>
 * <li>{@link #DrugToDrug}</li>
 * <li>{@link #DrugToLab}</li>
 * <li>{@link #DuplicateTherapy}</li>
 * <li>{@link #LabToDrug}</li>
 * <li>{@link #ScreeningUnavailable}</li>
 * <li>{@link #MultipleIngredients}</li>
 * <li>{@link #RemovedInteraction}</li>
 */
public enum ScreenType {

  /**
   * Allergy To Drug
   */
  AllergyToDrug("ADI"),

  /**
   * Drug Allergen
   */
  DrugAllergen("DAI"),

  /**
   * Drug Intolerance
   */
  DrugIntolerance("DII"),

  /**
   * Drug TO Drug
   */
  DrugToDrug("DDI"),

  /**
   * Dosage Alert
   */
  DosageAlert("DOS"),

  /**
   * Duplicate Therapy
   */
  DuplicateTherapy("DUP"),

  /**
   * Drug To Disease
   */
  DrugToDisease("DID"),

  /**
   * Disease To Drug
   */
  DiseaseToDrug("DDE"),

  /**
   * Drug To Lab
   */
  DrugToLab("DLI"),

  /**
   * Lab To Drug
   */
  LabToDrug("LDI"),

  /**
   * Removed Interaction
   */
  RemovedInteraction("RDI"),

  /**
   * Concurrent Different Units
   */
  ConcurrentDifferentUnits("DU"),

  /**
   * Multiple Ingredients
   */
  MultipleIngredients("MI"),

  /**
   * Screening Unavailable
   */
  ScreeningUnavailable("NA");


  private final String type;

  ScreenType(String type) {
    this.type = type;
  }

  public static ScreenType lookup(String type) {
    return Arrays.stream(values()).filter(e -> e.type.equals(type)).findFirst()
        .orElse(null);
  }

  public String getCode() {
    return type;
  }

}
