
package com.qhrtech.emr.restapi.security;

import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 *
 * @author Blake Dickie
 */
public class AccuroOAuthClient extends BaseClientDetails {

  private String displayName;

  public AccuroOAuthClient() {
  }

  public AccuroOAuthClient(String displayName, String clientId, String resourceIds, String scopes,
      String grantTypes, String authorities) {
    super(clientId, resourceIds, scopes, grantTypes, authorities);
    this.displayName = displayName;
  }

  public AccuroOAuthClient(String displayName, String clientId, String resourceIds, String scopes,
      String grantTypes, String authorities, String redirectUris) {
    super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUris);
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

}
