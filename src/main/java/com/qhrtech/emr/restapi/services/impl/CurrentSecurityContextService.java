
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.ApiSecurityManager;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import com.qhrtech.emr.restapi.util.AccapiScopes;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link SecurityContextService} that will return the {@link ApiSecurityContext}
 * of the current request.
 *
 * This security context will be thread local, and invalid once the request has completed. Clients
 * of this class should not hold reference to, or re-use the {@link ApiSecurityContext}, rather they
 * should always request a fresh context when they need it.
 *
 * This implementation is essentially a wrapper for the {@link ApiSecurityManager} singleton to make
 * testing easier.
 *
 * @see ApiSecurityContext
 */
@Service
public class CurrentSecurityContextService implements SecurityContextService {

  @Override
  public ApiSecurityContext getSecurityContext() {
    return ApiSecurityManager.getInstance().getCurrentSecurityContext();
  }

  @Override
  public AuthorizationContext getAuthorizationContext() {
    ApiSecurityContext currentSecurityContext =
        ApiSecurityManager.getInstance().getCurrentSecurityContext();
    if (currentSecurityContext == null) {
      return null;
    }

    // to skip provider permission check for qhr-first-party clients or qhr-authorized-users
    if (currentSecurityContext.getScopes() != null
        && (currentSecurityContext.getScopes()
            .contains(AccapiScopes.QHR_FIRST_PARTY.getApiScope())
            || currentSecurityContext.getScopes()
                .contains(AccapiScopes.QHR_AUTHORIZED_USER.getApiScope()))) {
      return null;
    }
    return currentSecurityContext.getAuthorizationContext();
  }

}
