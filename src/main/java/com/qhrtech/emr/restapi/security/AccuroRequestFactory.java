
package com.qhrtech.emr.restapi.security;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

/**
 * This class is used to include tenant and office as part of the OAuth2 authorization request.
 * <p>
 * These values are used by the authentication key generator so unique keys are created for
 * different tenants and different offices.
 *
 * @see MultiTenantAuthenticationKeyGenerator
 */
public class AccuroRequestFactory extends DefaultOAuth2RequestFactory {

  private static final String KEY_TENANT = "tenant";
  private static final String KEY_OFFICE = "office";
  private static final String KEY_OAUTH_CLIENT_ID = "oauth_client_id";

  public AccuroRequestFactory(ClientDetailsService clientDetailsService) {
    super(clientDetailsService);
  }

  @Override
  public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
    OAuth2Request request = super.createOAuth2Request(client, tokenRequest);
    String officeId = tokenRequest.getRequestParameters().get(KEY_OFFICE);
    if (officeId != null && !officeId.isEmpty()) {
      request.getExtensions().put(KEY_OFFICE, Integer.parseInt(officeId));
    }
    String tenant = tokenRequest.getRequestParameters().get(KEY_TENANT);
    if (tenant != null && !tenant.isEmpty()) {
      request.getExtensions().put(KEY_TENANT, tenant);
    }

    // oauth_client_id
    request.getExtensions().put(KEY_OAUTH_CLIENT_ID, client.getClientId());

    return request;
  }

}
