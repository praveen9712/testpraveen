
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.util.AccapiScopes;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AccuroApiSecurityExpressionMethods {

  private final Authentication authentication;
  private final ApiSecurityContext context;

  public AccuroApiSecurityExpressionMethods(Authentication authentication) {
    this.authentication = authentication;
    context = ApiSecurityManager.getInstance().getCurrentSecurityContext();
  }

  /**
   * Checks for QHR First Party scope or QHR Authorized user scope.
   *
   * @return boolean indicating if this user is QHR First Party scope/QHR Authorized User
   */
  public boolean isQhrAuthorizedUser() {
    return ((OAuth2Authentication) authentication)
        .getOAuth2Request().getScope().contains(AccapiScopes.QHR_FIRST_PARTY.getApiScope())
        || ((OAuth2Authentication) authentication)
            .getOAuth2Request().getScope().contains(AccapiScopes.QHR_AUTHORIZED_USER.getApiScope());
  }

  /**
   * QHR First Party scope
   *
   * @return String for Qhr-first-party scope
   */
  public String getQhrFirstPartyScope() {
    return AccapiScopes.QHR_FIRST_PARTY.getApiScope();
  }

  /**
   * Checks if this OAuth client is Okta Client.
   *
   * @return boolean indicating if the request has come from Okta server or not.
   */
  public boolean isOktaClient() {

    return ((OAuth2Authentication) authentication)
        .getOAuth2Request()
        .getResourceIds()
        .contains(OktaJwtAccessTokenConverter.OKTA);
  }
}
