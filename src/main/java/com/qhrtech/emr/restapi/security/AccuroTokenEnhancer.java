
package com.qhrtech.emr.restapi.security;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class AccuroTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {

    // If the authentication is not for a User we do not want to enhance the token
    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      return accessToken;
    }

    // Copy the supplied access token into an instance of DefaultOAuth2AccessToken
    DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);

    // Incase the additonal details are immutable lets make a copy even though
    // the copy constructor should already use a LinkedHashMap
    Map<String, Object> currentExtraDetails = result.getAdditionalInformation();
    Map<String, Object> extraDetails = new LinkedHashMap<>();
    if (currentExtraDetails != null) {
      extraDetails.putAll(currentExtraDetails);
    }
    result.setAdditionalInformation(extraDetails);

    // Extract office from the authentication request and store it in the
    // tokens extra details
    Map<String, Serializable> extensions = authentication.getOAuth2Request().getExtensions();
    Integer officeId = (Integer) extensions.get("office");
    extraDetails.put("office", officeId);

    // Return the new access token
    return result;
  }
}
