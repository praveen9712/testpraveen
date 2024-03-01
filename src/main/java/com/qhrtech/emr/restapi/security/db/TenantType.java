/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.db;

/**
 * Represents the core Tenant Configuration used by the Accuro API.
 *
 * @author kevin.kendall
 */
public enum TenantType {

  /**
   * Represents an Accuro API Configuration where there is a single tenant The tenant DataSource
   * details are configured in jdbc.properties
   */
  SINGLE,
  /**
   * Represents an Accuro API Configuration where there are multiple tenants All tenant DataSource
   * details are reachable by {@link RegistryServiceApi}
   */
  HOSTED;

  /**
   * Looks up a Tenant type by name
   *
   * @param name Case Insensitive String name of a Tenant Type.
   *
   * @return The matching Tenant Type if found, null otherwise
   */
  public static TenantType lookup(String name) {
    for (TenantType type : values()) {
      if (type.name().equalsIgnoreCase(name)) {
        return type;
      }
    }
    return null;
  }

  /**
   * Checks whether this {@link TenantType} requires a UUID as a query parameter or not
   *
   * @return true if this type requires the uuid, false otherwise
   */
  public boolean requiresUuid() {
    switch (this) {
      case SINGLE:
        return false;
      case HOSTED:
        return true;
      default:
        throw new IllegalStateException("You added a TenantType and did not add it to this list.");
    }
  }

  /**
   * Checks whether this {@link TenantType} requires a TenantId as a query parameter or not
   *
   * @return true if this type requires the TenantId, false otherwise
   */
  public boolean requiresTenantId() {
    switch (this) {
      case SINGLE:
        return false;
      case HOSTED:
        return true;
      default:
        throw new IllegalStateException("You added a TenantType and did not add it to this list.");
    }
  }
}
