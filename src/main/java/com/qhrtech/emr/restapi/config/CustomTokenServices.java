
package com.qhrtech.emr.restapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomTokenServices extends DefaultTokenServices {

  private final Logger log = LoggerFactory.getLogger(CustomTokenServices.class);

  @Override
  public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication)
      throws AuthenticationException {
    try {
      return super.createAccessToken(authentication);
    } catch (DuplicateKeyException dke) {

      log.info("Requests for the same token are being issued too quickly. Please try again.", dke);
      throw new InvalidGrantException(
          "Requests for the same token are being issued too quickly. Please try again");
    }
  }

  @Override
  public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
      throws AuthenticationException {
    try {
      return super.refreshAccessToken(refreshTokenValue, tokenRequest);
    } catch (DuplicateKeyException dke) {
      log.info("Requests for the same token are being issued too quickly. Please try again.", dke);
      throw new InvalidGrantException(
          "Requests for the same token are being issued too quickly. Please try again");
    }
  }
}
