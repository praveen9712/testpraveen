
package com.qhrtech.emr.restapi.security;

import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class AccuroResourceServerTokenServices extends DefaultTokenServices {

  public AccuroResourceServerTokenServices(TokenStore tokenStore) {
    super();
    this.setTokenStore(tokenStore);
  }

}
