
package com.qhrtech.emr.restapi.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.impl.DefaultJwt;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OktaJwtAccessTokenConverterTest {

  @Mock
  private AccessTokenVerifier parser;

  @InjectMocks
  private OktaJwtAccessTokenConverter oktaJwtAccessTokenConverter;

  private final String token = TestUtilities.nextString(250);


  @Before
  public void setUp() throws Exception {
    oktaJwtAccessTokenConverter = spy(new OktaJwtAccessTokenConverter(parser));
    openMocks(this);
  }

  private static Map<String, Object> createClaims() {
    HashMap<String, Object> claims = new HashMap<>();
    long expiry = System.currentTimeMillis() + 10000000L;
    claims.put("scp", new ArrayList<>(Arrays.asList("PATIENT_DEMOGRAPHICS_WRITE",
        "SCHEDULING_WRITE", "REMINDER_API", "IMPLICIT_EXTERNAL_ACCESS", "DOCUMENTS_WRITE")));
    String username = "sandeep.singh@qhrtest.com";
    claims.put("accuro_user", username);
    claims.put("accuro_acronym_list", new ArrayList<>(Arrays.asList("JMIC")));
    claims.put("sub", username);
    claims.put("cid", "CLIENT_ID");
    claims.put("service_user_id", "100");
    claims.put("jti", "AT.DWZ6edtt1BLkGH0wdY8mY8NKEdrsgGGl7DsA6Y4LlMw");
    claims.put("uid", "00uoqj3rsccRVQMRD0h7");
    return claims;
  }

  @Test
  public void decode() throws JwtVerificationException {

    // Given
    Map<String, Object> claims = Collections.unmodifiableMap(createClaims());

    Jwt jwt = new DefaultJwt(token, Instant.now(), Instant.now(), claims);
    when(parser.decode(token)).thenReturn(jwt);

    // When
    Map<String, Object> actual = oktaJwtAccessTokenConverter.decode(token);

    // Then
    assertNotNull(actual);
    assertEquals(claims, actual);
  }

  @Test
  public void TestDecodeWhenJwtExpiredExpectErrorResponse() throws JwtVerificationException {

    // Given
    when(parser.decode(token)).thenThrow(
        new JwtVerificationException("JWT expired", new JwtVerificationException("JWT expired")));

    String actual = "";
    // When
    try {
      Map<String, Object> map = oktaJwtAccessTokenConverter.decode(token);
    } catch (InvalidTokenException e) {
      actual = e.getMessage();
    }

    // Then
    assertEquals("Access token expired: " + token, actual);
  }


  @Test
  public void extractAccessToken() {
    // When
    OAuth2AccessToken accessToken =
        oktaJwtAccessTokenConverter.extractAccessToken(token, new HashMap<String, Integer>() {
          {
            put("exp", 9000);
          }
        });

    // Then
    assertEquals(token, accessToken.getValue());
  }

  @Test(expected = InvalidTokenException.class)
  public void extractAuthenticationNullClientId() {
    // Given
    Map<String, Object> claims = createClaims();
    claims.put("cid", null);

    // When
    oktaJwtAccessTokenConverter.extractAuthentication(claims);
  }

  @Test(expected = InvalidTokenException.class)
  public void extractAuthenticationNullAcronym() {
    // Given
    Map<String, Object> claims = createClaims();
    claims.put("accuro_acronym_list", null);

    // When
    oktaJwtAccessTokenConverter.extractAuthentication(claims);
  }

  @Test
  public void extractAuthenticationWithServiceUserId() {
    // When
    OAuth2Authentication authentication =
        oktaJwtAccessTokenConverter.extractAuthentication(createClaims());

    // Then
    OktaUserDetails oktaUserDetails = (OktaUserDetails) authentication.getPrincipal();
    assertEquals("100", String.valueOf(oktaUserDetails.getUserIdentity()));
  }

  @Test
  public void extractAuthenticationWithUserIdentity() {

    // Given
    Map<String, Object> claims = createClaims();
    claims.put("service_user_id", null);
    claims.put("squid", "9c1cdc27-9dca-4b1f-94b0-db7a2b237e70");

    // When
    OAuth2Authentication authentication =
        oktaJwtAccessTokenConverter.extractAuthentication(claims);

    // Then
    OktaUserDetails oktaUserDetails = (OktaUserDetails) authentication.getPrincipal();
    assertEquals("9c1cdc27-9dca-4b1f-94b0-db7a2b237e70", oktaUserDetails.getUserIdentity());
  }

  @Test(expected = InvalidTokenException.class)
  public void extractAuthentication() {
    // Given
    Map<String, Object> claims = createClaims();
    claims.put("squid", "9c1cdc27-9dca-4b1f-94b0-db7a2b237e70");

    // When
    oktaJwtAccessTokenConverter.extractAuthentication(claims);
  }
}
