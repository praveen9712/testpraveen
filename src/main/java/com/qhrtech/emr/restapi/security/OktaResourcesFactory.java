
package com.qhrtech.emr.restapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerifiers;
import com.qhrtech.emr.restapi.models.dto.OktaMetadata;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

@Component
public class OktaResourcesFactory {

  public static final String METADATA_PATH =
      "/.well-known/oauth-authorization-server";

  private final Logger log = LoggerFactory.getLogger(getClass());
  private TokenStore jwkTokenStore;
  private long publicKeyExpirationTimeStamp;

  @Value("${okta.url.issuer:}")
  private String oktaIssuerUrl;
  @Value("${okta.url.aud:api://default}")
  private String aud;

  private long getPublicKeyExpirationTimeStamp() {
    return publicKeyExpirationTimeStamp;
  }

  private void setPublicKeyExpirationTimeStamp(long publicKeyExpirationTimeStamp) {
    this.publicKeyExpirationTimeStamp = publicKeyExpirationTimeStamp;
  }

  public TokenStore getJwkTokenStore() {
    return jwkTokenStore;
  }

  private void setJwkTokenStore(TokenStore jwkTokenStore) {
    this.jwkTokenStore = jwkTokenStore;
  }

  private void loadResources() throws IOException {

    URL url = new URL(oktaIssuerUrl + METADATA_PATH);
    HttpURLConnection con = null;
    StringBuilder content = new StringBuilder();
    OktaMetadata oktaMetadata;
    try {
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      try (BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
        }
      }
      ObjectMapper mapper = new ObjectMapper();
      oktaMetadata = mapper.readValue(content.toString(), OktaMetadata.class);
      URL pkUrl = new URL(oktaMetadata.getJwksUri());

      con.disconnect();
      con = (HttpURLConnection) pkUrl.openConnection();
      this.setPublicKeyExpirationTimeStamp(con.getExpiration());

    } finally {
      if (con != null) {
        con.disconnect();
      }
    }
    AccessTokenVerifier verifier = getAccessTokenVerifier(oktaMetadata);

    TokenStore tokenStore =
        new JwtTokenStore(new OktaJwtAccessTokenConverter(verifier));
    this.setJwkTokenStore(tokenStore);

  }

  public void validatePublicKeys() {

    if (oktaIssuerUrl.length() == 0) {
      log.error("No URL provided for JWT Authentication services");
      throw new InvalidTokenException("JWT Authentication services is not enabled.");
    }

    long expirationTimeStamp = this.getPublicKeyExpirationTimeStamp();
    Date date = new Date();
    if (date.before(new Date(expirationTimeStamp))) {
      return;
    }
    try {
      loadResources();
    } catch (IOException e) {
      String message = "Unable to authenticate the token at this moment. Please try again later.";
      log.error(message + ": " + e);
      throw new InvalidTokenException(message);
    }

  }

  private AccessTokenVerifier getAccessTokenVerifier(OktaMetadata oktaMetadata) {
    return JwtVerifiers.accessTokenVerifierBuilder()
        .setIssuer(oktaMetadata.getIssuer())
        .setAudience(aud)
        .setLeeway(Duration.ZERO)
        .setConnectionTimeout(Duration.ofSeconds(10)) // defaults to 1000ms
        .setReadTimeout(Duration.ofSeconds(10)) // defaults to 1000ms
        .build();

  }
}
