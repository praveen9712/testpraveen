
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;

/**
 * Service for retrieving security contexts within the Api.
 *
 * @see ApiSecurityContext
 * @see com.qhrtech.emr.restapi.services.impl.CurrentSecurityContextService
 */
public interface SecurityContextService {

  /**
   * Returns a valid security context with some additional processing.
   *
   * If the client has the QHR_FIRST_PARTY scope then AuthorizationContext will be set to null.
   *
   * @return A valid {@link ApiSecurityContext}
   */
  ApiSecurityContext getSecurityContext();

  /**
   * Returns the users AuthorizationContext with some additional processing.
   *
   * Will set the authorization context to null indicating superuser if the client has the
   * QHR_FIRST_PARTY scope.
   *
   * @return A valid {@link AuthorizationContext} or NULL
   */
  AuthorizationContext getAuthorizationContext();
}
