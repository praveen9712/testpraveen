
package com.qhrtech.emr.restapi.models.dto.medications.fdb;

import java.util.HashMap;
import java.util.Map;

public enum ExternalVocabularyType {
  /**
   * Codifies a set of active ingredient substances, their strengths, dosage form, product name and
   * manufacturer company. MPs share a many to 1 relationship with NTPs. That is, each MP has one
   * associated NTP. MPs also typically align with DINs - in that the MP IS the
   * <span data-markjs="true" class="terms-mark terms-term-selector tooltipstered">DIN</span>.
   */
  ManufacturedProduct("1001"),
  /**
   * Codifies a set of active incredient substances, their strengths, dosage form independent of any
   * manufacturer. NTPs share a 0 to many relationship with MPs. That is, each NTP has 0 to many
   * MPs. NTPs share a 0 to 1 relationship with TMs. That is, each NTP can have 0 or 1 TM. NTPs can
   * be mapped to
   * <span data-markjs="true" class="terms-mark terms-term-selector tooltipstered">FDB</span> values
   * via the GCN Sequence Number
   */
  NonProprietaryTherapeuticProduct("1002"),
  /**
   * Codifies a set of active ingredient substances TMs share a 1 to many relationship with NTPs.
   * That is, each TM can have 1 to many NTPs
   */
  TherapeuticMoiety("1003"),
  /**
   * Codifies a set non-manufacturer-specific device products.
   */
  DeviceNonProprietaryTherapeuticProduct("1004");


  // lookup table
  private static final Map<String, ExternalVocabularyType> lookup = new HashMap<>();

  // on loading time, populate the lookup map
  static {
    for (ExternalVocabularyType externalVocabularyType : ExternalVocabularyType.values()) {
      lookup.put(externalVocabularyType.getCode(), externalVocabularyType);
    }
  }

  private final String code;

  ExternalVocabularyType(String code) {
    this.code = code;
  }

  public static ExternalVocabularyType lookup(String code) {
    return lookup.get(code);
  }

  public String getCode() {
    return code;
  }
}
