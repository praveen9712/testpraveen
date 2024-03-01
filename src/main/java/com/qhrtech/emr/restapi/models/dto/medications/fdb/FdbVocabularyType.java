
package com.qhrtech.emr.restapi.models.dto.medications.fdb;

import java.util.HashMap;
import java.util.Map;

public enum FdbVocabularyType {
  MedNameId("0001"),
  MedId("0003"),
  /**
   * Known as the GCN Sequence Number This value is specific to Strength, Form and Route This value
   * is known in MedAdmin as WellnetFormulationId This groups pharmaceutically equivalent products
   * The number itself is proprietary and has no significance outside of
   * <span data-markjs="true" class="terms-mark terms-term-selector tooltipstered">FDB</span>
   */
  GenericCodeNumber("0006"),
  FdbProductId("0101"),
  /**
   * Either HICL or HIC (not sure) This value is know in MedAdmin as WellnetGenericId Identifies the
   * active
   * <span data-markjs="true" class="terms-mark terms-term-selector tooltipstered">drug</span> in
   * the formulation
   */
  HierarchicalIngredientCode("0108"),
  /**
   * Legacy <span data-markjs="true" class="terms-mark terms-term-selector tooltipstered">FDB</span>
   * Product Id
   */
  Idc("0109");

  // lookup table
  private static final Map<String, FdbVocabularyType> lookup = new HashMap<>();

  // on loading time, populate the lookup map
  static {
    for (FdbVocabularyType fdbVocabularyType : FdbVocabularyType.values()) {
      lookup.put(fdbVocabularyType.getCode(), fdbVocabularyType);
    }
  }

  private String code;

  FdbVocabularyType(String code) {
    this.code = code;
  }

  public static FdbVocabularyType lookup(String code) {
    return lookup.get(code);
  }

  public String getCode() {
    return code;
  }
}
