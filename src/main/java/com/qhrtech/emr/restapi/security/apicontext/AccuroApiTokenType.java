
package com.qhrtech.emr.restapi.security.apicontext;

/**
 * @author james.michaud
 */
public enum AccuroApiTokenType {

  /**
   * Very limited access to a select set of mostly uninteresting endpoints (e.g. Appointment Types).
   *
   * Limited by OAuth2 Scopes granted to the client.
   */
  LEGACY_CLIENT_CREDENTIALS,

  /**
   * Access to all endpoints, but filtered by Accuro Provider Permissions.
   *
   * Limited by Oauth2 Scopes granted to the client.
   */
  LEGACY_PROVIDER,

  /**
   * Access to all endpoints, but filtered by Accuro Provider Permissions.
   *
   * Limited by OAuth2 Scopes granted to the client.
   */
  OKTA_PROVIDER,

  /**
   * Very limited access to a select set of mostly uninteresting endpoints (e.g. Appointment Types).
   *
   * Limited by OAuth2 Scopes granted to the client.
   */
  OKTA_CLIENT_CREDENTIALS,

  /**
   * Access to all endpoints, but filtered by Accuro Provider Permissions.
   *
   * Limited by OAuth2 Scopes granted to the client.
   */
  OKTA_THIRD_PARTY

}
