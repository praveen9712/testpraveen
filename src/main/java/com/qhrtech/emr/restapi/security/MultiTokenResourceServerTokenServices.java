
package com.qhrtech.emr.restapi.security;

import java.util.UUID;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


public class MultiTokenResourceServerTokenServices implements ResourceServerTokenServices {
  private ResourceServerTokenServices oktaResourceServerTokenServices;
  private ResourceServerTokenServices accuroResourceServerTokenServices;


  public MultiTokenResourceServerTokenServices(
      ResourceServerTokenServices oktaResourceServerTokenServices,
      ResourceServerTokenServices accuroResourceServerTokenServices) {
    this.oktaResourceServerTokenServices = oktaResourceServerTokenServices;
    this.accuroResourceServerTokenServices = accuroResourceServerTokenServices;

  }

  @Override
  public OAuth2Authentication loadAuthentication(String accessToken) {
    try {
      // Check, if the token is of UUID type. We are not validating the UUID token here.
      UUID.fromString(accessToken);
      return accuroResourceServerTokenServices.loadAuthentication(accessToken);
    } catch (IllegalArgumentException exception) {
      return oktaResourceServerTokenServices.loadAuthentication(accessToken);
    }
  }

  @Override
  public OAuth2AccessToken readAccessToken(String accessToken) {
    try {
      UUID.fromString(accessToken);
      return accuroResourceServerTokenServices.readAccessToken(accessToken);
    } catch (IllegalArgumentException exception) {
      return oktaResourceServerTokenServices.readAccessToken(accessToken);
    }
  }
}
