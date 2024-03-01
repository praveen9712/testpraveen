
package com.qhrtech.emr.restapi.security.apicontext;

/**
 * Allows interpreting tokens from different authorization servers and identity systems.
 *
 * @author james.michaud
 */
public interface AccuroApiToken {

  /**
   * aka Acron
   */
  String getTenantId();

  AccuroApiTokenType getTokenType();

  String getUserIdentity();

}
