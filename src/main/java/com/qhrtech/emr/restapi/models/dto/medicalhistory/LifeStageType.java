
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qhrtech.emr.restapi.models.dto.serialization.LifeStageDeserializer;
import com.qhrtech.emr.restapi.models.dto.serialization.LifeStageSerializer;
import java.util.Arrays;

@JsonSerialize(using = LifeStageSerializer.class)
@JsonDeserialize(using = LifeStageDeserializer.class)
public enum LifeStageType {

  /**
   * Newborn: Birth - 28 days
   */
  Newborn("Newborn: Birth - 28 days"),

  /**
   * Infant: 29 days to less than 2 years"
   */
  Infant("Infant: 29 days to less than 2 years"),

  /**
   * Child: 2 years to 15 years
   */
  Child("Child: 2 years to 15 years"),

  /**
   * Adolescent: 16 to 17 years
   */
  Adolescent("Adolescent: 16 to 17 years"),

  /**
   * Adult: 18 years or older
   */
  Adult("Adult: 18 years or older");

  private final String description;

  LifeStageType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static LifeStageType lookup(String description) {
    return Arrays.stream(values()).filter(e -> e.description.equals(description)).findFirst()
        .orElse(null);
  }
}
