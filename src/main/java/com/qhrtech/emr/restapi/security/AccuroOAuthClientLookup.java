
package com.qhrtech.emr.restapi.security;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 *
 * @author Blake Dickie
 */
public interface AccuroOAuthClientLookup extends ClientDetailsService {

  @Override
  public AccuroOAuthClient loadClientByClientId(String clientId) throws ClientRegistrationException;

}
