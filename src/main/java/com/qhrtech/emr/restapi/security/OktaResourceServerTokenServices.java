
package com.qhrtech.emr.restapi.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


public class OktaResourceServerTokenServices implements ResourceServerTokenServices {

  private OktaResourcesFactory resourcesFactory;


  public OktaResourceServerTokenServices(OktaResourcesFactory resourcesFactory) {

    this.resourcesFactory = resourcesFactory;

  }

  @Override
  public OAuth2Authentication loadAuthentication(String accessToken) {

    // Check if the JWKs URL is expired or not.
    resourcesFactory.validatePublicKeys();
    /**
     * Just for signature verification use spring-oauth2, we can use below method for that:
     * resourcesFactory.getSignatureVerifier().readAuthentication(accessToken);
     */
    return resourcesFactory.getJwkTokenStore().readAuthentication(accessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String accessToken) {
    resourcesFactory.validatePublicKeys();
    return resourcesFactory.getJwkTokenStore().readAccessToken(accessToken);
  }

}
