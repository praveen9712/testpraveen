
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qhrtech.emr.restapi.models.dto.serialization.EyeCodeDeserializer;
import com.qhrtech.emr.restapi.models.dto.serialization.EyeCodeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;

/**
 * Represents eye locations for diagnoses, history items, symptoms, etc.
 */
@JsonDeserialize(using = EyeCodeDeserializer.class)
@JsonSerialize(using = EyeCodeSerializer.class)
@Schema(description = "Represents eye locations for diagnoses, history items, symptoms, etc")
public enum EyeCode {

  /**
   * Oculus Sinister
   */
  LEFT_EYE("OS"),

  /**
   * Oculus Dexter
   */
  RIGHT_EYE("OD"),

  /**
   * Oculus Uterque
   */
  BOTH_EYES("OU"),

  /**
   * No Eyes, or Not Applicable in this instance.
   */
  NO_EYES("NA");

  /**
   * The Acronym, in Latin, for this Eye Code.
   */
  private final String abbreviation;

  EyeCode(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public static EyeCode lookup(String abbreviation) {
    return Arrays.stream(values())
        .filter(e -> e.getAbbreviation().equals(abbreviation))
        .findFirst()
        .orElse(NO_EYES);
  }

  public String getAbbreviation() {
    return abbreviation;
  }
}
