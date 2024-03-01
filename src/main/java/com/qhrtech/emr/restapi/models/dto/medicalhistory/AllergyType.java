
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.Arrays;
import javax.ws.rs.core.Response.Status;

/**
 * The AllergyType Enum. Determines the type of as patient allergy by categorizing it as a drug,
 * non-drug, allergy, or intolerance.
 */

public enum AllergyType {

  /**
   * Represents an allergy to a drug.
   */
  DRUG_ALLERGY(true, "AL"),
  /**
   * Represents an allergy to something that is not a drug.
   */
  NON_DRUG_ALLERGY(false, "AL"),
  /**
   * Represents an intolerance to a drug.
   */
  DRUG_INTOLERANCE(true, "IN"),
  /**
   * Represents an intolerance to something that is not a drug.
   */
  NON_DRUG_INTOLERANCE(false, "IN");

  private final boolean drug;
  private final String responseCategory;

  AllergyType(boolean drug, String responseCategory) {
    this.drug = drug;
    this.responseCategory = responseCategory;
  }

  public static AllergyType getType(boolean drug, String responseCategory) {
    for (AllergyType type : AllergyType.values()) {
      if (drug == type.isDrug() && responseCategory.equals(type.getResponseCategory())) {
        return type;
      }
    }
    return null;
  }

  public static AllergyType fromString(String name) {
    return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(name)).findFirst()
        .orElseThrow(() -> Error.webApplicationException(Status.BAD_REQUEST,
            "Invalid Allergy Type: " + name));
  }

  public boolean isDrug() {
    return drug;
  }

  public String getResponseCategory() {
    return responseCategory;
  }


}
