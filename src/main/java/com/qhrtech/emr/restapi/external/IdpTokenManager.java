/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.external;

import com.qhrtech.emr.accuro.utils.Touple;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.client.Consumer;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenGrant;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrant;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author bdickie
 */
@Component
public class IdpTokenManager {

  private static final long EARLY_RENEW_PERIOD = TimeUnit.MINUTES.toMillis(1);
  private static final Logger LOG = LoggerFactory.getLogger(IdpTokenManager.class);
  private ClientAccessToken accessToken;
  private long renewTime;

  @Value("${medeo.idp.url}")
  private String idpUrl;

  @Value("${medeo.client_id}")
  private String clientId;

  @Value("${medeo.client_secret}")
  private String clientSecret;

  /**
   * This method is essentially a copy of OAuthClientUtils.getAccessToken() however the WebClient is
   * modified to enable TLS 1.2 connections.
   *
   * @param accessTokenServiceUri
   * @param consumer
   * @param grant
   *
   * @return
   *
   * @throws OAuthServiceException
   */
  private static ClientAccessToken getAccessToken(
      String accessTokenServiceUri,
      Consumer consumer,
      AccessTokenGrant grant) throws OAuthServiceException {
    OAuthJSONProvider provider = new OAuthJSONProvider();
    WebClient accessTokenService =
        WebClient.create(accessTokenServiceUri, Collections.singletonList(provider));
    HTTPConduit conduit = (HTTPConduit) WebClient.getConfig(accessTokenService).getConduit();
    TLSClientParameters tlsParams = new TLSClientParameters();
    tlsParams.setSecureSocketProtocol("TLSv1.2");
    conduit.setTlsClientParameters(tlsParams);
    accessTokenService.accept("application/json");
    return OAuthClientUtils.getAccessToken(accessTokenService, consumer, grant, true);
  }

  public synchronized String getAuthenticationHeader() {
    if (accessToken == null || renewTime < System.currentTimeMillis()) {
      ClientCredentialsGrant grant = new ClientCredentialsGrant("public");
      Touple<ClientAccessToken, Long> token = obtainAccessToken(grant);
      accessToken = token.getFirst();
      renewTime = token.getSecond();
    }
    return OAuthClientUtils.createAuthorizationHeader(accessToken);
  }

  public synchronized ClientAccessToken getPasswordToken(String username, String password) {
    ResourceOwnerGrant grant = new ResourceOwnerGrant(username, password);
    Touple<ClientAccessToken, Long> token;
    try {
      token = obtainAccessToken(grant);
    } catch (OAuthServiceException ex) {
      LOG.debug("Error getting client access token.", ex);
      return null;
    }
    return token.getFirst();
  }

  private Touple<ClientAccessToken, Long> obtainAccessToken(AccessTokenGrant grant) {
    String url = idpUrl.endsWith("/") ? idpUrl.substring(0, idpUrl.length() - 1) : idpUrl;
    Consumer consumer = new Consumer(clientId, clientSecret);
    ClientAccessToken token = getAccessToken(url + "/oauth/token", consumer, grant);
    Long timeout =
        TimeUnit.SECONDS.toMillis(token.getIssuedAt() + token.getExpiresIn()) - EARLY_RENEW_PERIOD;
    return new Touple<>(token, timeout);
  }
}
