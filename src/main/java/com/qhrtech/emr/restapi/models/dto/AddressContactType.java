
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.Arrays;
import java.util.Objects;
import javax.ws.rs.core.Response;

/**
 * The AddressContactType enum. Defines the contact type of an address book contact.
 */
public enum AddressContactType {

  /**
   * Address book contact type.
   */
  Contact(1),
  /**
   * Address book pharmacy type.
   */
  Pharmacy(2);

  /**
   * Maps the element to the Protoss enum
   */
  private final int id;

  AddressContactType(int id) {
    this.id = id;
  }

  public static AddressContactType lookup(Integer id) {
    return Arrays.stream(values())
        .filter(c -> Objects.equals(c.id, id))
        .findFirst()
        .orElse(null);
  }

  public int getTypeId() {
    return id;
  }

  @JsonCreator
  public static AddressContactType fromString(String type) {
    return Arrays.stream(values()).filter(e -> e.name().equals(type)).findFirst()
        .orElseThrow(() -> Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Invalid Address Contact Type: " + type));
  }

}
