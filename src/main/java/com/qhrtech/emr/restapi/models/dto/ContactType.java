
package com.qhrtech.emr.restapi.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Objects;

/**
 * The ContactType enum. Defines the contact type for a phone number.
 *
 * @author jesse.pasos
 */
@Schema(description = "The ContactType enum. Defines the contact type for a phone number.")
public enum ContactType {

  HomePhone(1),
  WorkPhone(2),
  CellPhone(3),
  FaxPhone(4),
  OfficePhone(5);

  /**
   * Maps the element to the Protoss enum
   */
  private final int id;

  ContactType(int id) {
    this.id = id;
  }

  public static ContactType lookup(Integer id) {
    return Arrays.stream(values())
        .filter(c -> Objects.equals(c.id, id))
        .findFirst()
        .orElse(null);
  }
}
