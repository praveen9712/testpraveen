/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.apicontext;

import com.qhrtech.emr.restapi.security.OktaJwtAccessTokenConverter;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;


/**
 * @author james.michaud
 */
public class AccuroApiTokenAdapter {

  private final OAuth2Authentication authentication;
  private final AccuroApiToken authenticationUser;
  private final TokenRequestDetails tokenRequestDetails;

  public AccuroApiTokenAdapter(OAuth2Authentication oauthAuthentication,
      TokenRequestDetails tokenRequestDetails) {
    if (null == oauthAuthentication) {
      throw new InvalidTokenException("There is no valid authentication in this request");
    }

    this.tokenRequestDetails = tokenRequestDetails;
    this.authentication = oauthAuthentication;

    if (authentication.getPrincipal() instanceof AccuroApiToken) {
      authenticationUser = (AccuroApiToken) oauthAuthentication.getPrincipal();
    } else {
      authenticationUser = null;
    }
  }

  public TokenRequestDetails getTokenRequestDetails() {
    if (null == tokenRequestDetails) {
      throw new IllegalStateException("RequestDetails must be set.");
    }

    return tokenRequestDetails;
  }

  /**
   * @return the computer name, or ip address of the remote client.
   */
  public String getComputerInfo() {
    if ((null == authentication)) {
      throw new IllegalStateException("Authentication must be set.");
    }

    if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
      return ((OAuth2AuthenticationDetails) authentication.getDetails()).getRemoteAddress();
    } else {
      return "";
    }
  }

  public String getClientId() {
    if ((null == authentication)) {
      throw new IllegalStateException("Authentication must be set.");
    }

    return (String) authentication.getOAuth2Request().getExtensions().get("oauth_client_id");
  }

  /**
   * @return an Identifier that references the Accuro User. Most often it is user ID but may hold
   *         another identifier depending on {@link #getTokenType()}.
   */
  public String getUserIdentifier() {
    if ((null == authenticationUser)) {
      return null;
    }

    return authenticationUser.getUserIdentity();
  }

  /**
   * @return determine the type of token based on available fields.
   */
  public AccuroApiTokenType getTokenType() {
    if ((null != authenticationUser)) {
      return authenticationUser.getTokenType();
    }

    return isOkta() ? AccuroApiTokenType.OKTA_CLIENT_CREDENTIALS
        : AccuroApiTokenType.LEGACY_CLIENT_CREDENTIALS;
  }


  private boolean isOkta() {
    if (null == authentication.getOAuth2Request()
        || null == authentication.getOAuth2Request().getResourceIds()) {
      return false;
    }
    return (authentication.getOAuth2Request()
        .getResourceIds().contains(OktaJwtAccessTokenConverter.OKTA));
  }

  public String getTenantId() {
    if ((null == authenticationUser)) {
      if (isOkta()) {
        return (String) authentication.getOAuth2Request().getExtensions().get("tenant");
      } else {
        return tokenRequestDetails.getRequestParamTenantId();
      }
    }

    return authenticationUser.getTenantId();
  }

  public Set<String> getScopes() {
    if ((null == authentication
        || null == authentication.getOAuth2Request())
        || null == authentication.getOAuth2Request().getScope()) {

      return Collections.emptySet();
    }
    return authentication.getOAuth2Request().getScope();
  }

  public String getGrantType() {
    if ((null == authentication || null == authentication.getOAuth2Request())) {
      return "";
    }

    return authentication.getOAuth2Request().getGrantType();
  }

  public OAuth2Authentication getAuth() {
    return this.authentication;
  }

}
