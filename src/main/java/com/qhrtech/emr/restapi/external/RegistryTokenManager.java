/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.external;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.client.Consumer;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenGrant;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class for managing the Oauth2 Client Credentials Grant flow to the Registry Caches the access
 * token but will refresh the token automatically if it is less than one minute before the token is
 * set to expire.
 *
 * @author bryan.bergen
 */
@Component
public class RegistryTokenManager {

  private static final long EARLY_RENEW_PERIOD = TimeUnit.MINUTES.toMillis(1);
  private static final Logger log = LoggerFactory.getLogger(RegistryTokenManager.class);
  private ClientAccessToken accessToken;
  private long renewTime;

  @Value("${registry.url.token}")
  private String registryTokenUrl;

  @Value("${registry.client_id}")
  private String clientId;

  @Value("${registry.client_secret}")
  private String clientSecret;

  private static ClientAccessToken getAccessToken(
      String accessTokenServiceUrl,
      Consumer consumer,
      AccessTokenGrant grant) {
    OAuthJSONProvider provider = new OAuthJSONProvider();
    WebClient accessTokenService =
        WebClient.create(accessTokenServiceUrl, Collections.singletonList(provider));
    final HTTPConduit conduit = (HTTPConduit) WebClient.getConfig(accessTokenService).getConduit();
    TLSClientParameters tlsParams = new TLSClientParameters();
    tlsParams.setSecureSocketProtocol("TLSv1.2");
    tlsParams.setUseHttpsURLConnectionDefaultSslSocketFactory(true);
    tlsParams.setUseHttpsURLConnectionDefaultHostnameVerifier(true);
    conduit.setTlsClientParameters(tlsParams);
    accessTokenService.accept(MediaType.APPLICATION_JSON);
    return OAuthClientUtils.getAccessToken(accessTokenService, consumer, grant, true);
  }

  /**
   * Creates the Authentication Header Value for a Client Credentials Grant with the registry
   *
   * The returned value should be paired with {@link HttpHeaders.AUTHORIZATION}
   *
   * @return - Authorization header value
   */
  public synchronized String getAuthenticationHeader() {
    if (accessToken == null || renewTime < System.currentTimeMillis()) {
      ClientCredentialsGrant grant = new ClientCredentialsGrant("public");
      ExpiringToken token;
      try {
        token = obtainAccessToken(grant);
      } catch (OAuthServiceException e) {
        log.error("Error getting client access token.", e);
        return null;
      }
      accessToken = token.token;
      renewTime = token.expiry;
    }
    return OAuthClientUtils.createAuthorizationHeader(accessToken);
  }

  private ExpiringToken obtainAccessToken(AccessTokenGrant grant) {
    Consumer consumer = new Consumer(clientId, clientSecret);
    ClientAccessToken token = getAccessToken(registryTokenUrl, consumer, grant);
    long timeout =
        TimeUnit.SECONDS.toMillis(token.getIssuedAt() + token.getExpiresIn()) - EARLY_RENEW_PERIOD;
    return new ExpiringToken(token, timeout);
  }

  /**
   * Small immutable helper class for associating the expiry time with the access_token
   */
  private static class ExpiringToken {

    private final ClientAccessToken token;
    private final long expiry;

    public ExpiringToken(ClientAccessToken token, long expiry) {
      this.token = token;
      this.expiry = expiry;
    }

  }
}
