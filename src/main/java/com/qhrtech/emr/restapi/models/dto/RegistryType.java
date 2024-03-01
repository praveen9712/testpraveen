
package com.qhrtech.emr.restapi.models.dto;

import com.qhrtech.emr.accuro.model.registry.RegistryEntry.Type;

public enum RegistryType {


  Database, Clinic, User, Provider;

  public static Type lookup(String name) {
    for (Type type : Type.values()) {
      if (type.name().equals(name)) {
        return type;
      }
    }
    return null;
  }
}
