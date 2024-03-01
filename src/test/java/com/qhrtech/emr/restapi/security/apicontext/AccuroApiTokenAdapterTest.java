
package com.qhrtech.emr.restapi.security.apicontext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.qhrtech.util.RandomUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.thymeleaf.util.StringUtils;

public class AccuroApiTokenAdapterTest {

  OAuth2AuthenticationDetails oauth2AuthenticationDetails;
  OAuth2Authentication oauth2Authentication;
  OAuth2Request oauth2Request;

  TokenRequestDetails tokenRequestDetails;

  OAuth2Request oauthRequest;

  //
  AccuroApiToken mockToken;

  @Before
  public void setUp() throws Exception {
    oauth2AuthenticationDetails = mock(OAuth2AuthenticationDetails.class);

    oauth2Authentication = mock(OAuth2Authentication.class);
    oauthRequest = mock(OAuth2Request.class);
    mockToken = mock(AccuroApiToken.class);

    when(oauth2Authentication.getDetails()).thenReturn(oauth2AuthenticationDetails);
    when(oauth2Authentication.getOAuth2Request()).thenReturn(oauthRequest);
    when(oauth2Authentication.getPrincipal()).thenReturn(mockToken);
  }

  @Test(expected = InvalidTokenException.class)
  public void testGetTokenRequestDetailsNoAuth() {
    new AccuroApiTokenAdapter(null, tokenRequestDetails);
  }

  @Test
  public void testGetTokenRequestDetails() {
    tokenRequestDetails = new TokenRequestDetails(RandomUtils.getEnumValue(HttpMethod.class),
        RandomUtils.getString(20), RandomUtils.getString(20));
    when(oauth2Authentication.getDetails()).thenReturn(tokenRequestDetails);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    assertEquals(tokenRequestDetails, apiTokenAdapter.getTokenRequestDetails());
  }

  @Test
  public void testGetComputerInfo() {
    when(oauth2Authentication.getPrincipal()).thenReturn(null);
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    String remoteAddr = StringUtils.randomAlphanumeric(20);
    when(oauth2AuthenticationDetails.getRemoteAddress()).thenReturn(remoteAddr);
    assertEquals(remoteAddr, apiTokenAdapter.getComputerInfo());
  }


  @Test
  public void testGetClientId() {
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    String clientId = StringUtils.randomAlphanumeric(20);
    HashMap extension = new HashMap();
    extension.put("oauth_client_id", clientId);
    when(oauthRequest.getExtensions()).thenReturn(extension);
    assertEquals(clientId, apiTokenAdapter.getClientId());
  }

  @Test
  public void testGetUserIdentifier() {
    String userId = StringUtils.randomAlphanumeric(20);

    when(mockToken.getUserIdentity()).thenReturn(userId);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    assertEquals(userId, apiTokenAdapter.getUserIdentifier());
  }

  @Test
  public void testGetUserIdentifierNoUser() {
    mockToken = null;

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    assertEquals(null, apiTokenAdapter.getUserIdentifier());
  }


  @Test
  public void testGetTokenTypeUserAuth() {
    AccuroApiTokenType accuroApiTokenType = RandomUtils.getEnumValue(AccuroApiTokenType.class);
    when(mockToken.getTokenType()).thenReturn(accuroApiTokenType);
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    assertEquals(accuroApiTokenType, apiTokenAdapter.getTokenType());
  }

  @Test
  public void testGetTokenTypeNoUserAuthLegacy() {
    when(oauth2Authentication.getPrincipal()).thenReturn(null);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    assertEquals(AccuroApiTokenType.LEGACY_CLIENT_CREDENTIALS, apiTokenAdapter.getTokenType());
  }

  @Test
  public void testGetTokenTypeNoUserAuthOkta() {

    setOktaResource();
    when(oauth2Authentication.getPrincipal()).thenReturn(null);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);
    assertEquals(AccuroApiTokenType.OKTA_CLIENT_CREDENTIALS, apiTokenAdapter.getTokenType());
  }

  private void setOktaResource() {
    Set<String> resourceIds = new HashSet(Collections.singleton("Okta"));
    when(oauthRequest.getResourceIds()).thenReturn(resourceIds);
  }

  @Test
  public void testGetTenantIdClientCredentials() {
    String tenant = StringUtils.randomAlphanumeric(20);

    when(oauth2Authentication.getPrincipal()).thenReturn(null);
    tokenRequestDetails = new TokenRequestDetails(HttpMethod.GET, "", tenant);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    HashMap extension = new HashMap();
    extension.put("tenant", tenant);
    when(oauthRequest.getExtensions()).thenReturn(extension);
    assertEquals(tenant, apiTokenAdapter.getTenantId());
  }

  @Test
  public void testGetTenantIdOkta() {
    setOktaResource();
    when(oauth2Authentication.getPrincipal()).thenReturn(null);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    String tenant = StringUtils.randomAlphanumeric(20);
    HashMap extension = new HashMap();
    extension.put("tenant", tenant);
    when(oauthRequest.getExtensions()).thenReturn(extension);
    assertEquals(tenant, apiTokenAdapter.getTenantId());
  }

  @Test
  public void testGetTenantIdUserAuth() {
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    String tenant = StringUtils.randomAlphanumeric(20);
    when(mockToken.getTenantId()).thenReturn(tenant);
    assertEquals(tenant, apiTokenAdapter.getTenantId());
  }

  @Test
  public void testGetScopesNoAuth() {
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    when(oauthRequest.getScope()).thenReturn(null);
    assertEquals(Collections.emptySet(), apiTokenAdapter.getScopes());
  }

  @Test
  public void testGetScopes() {
    when(oauth2Authentication.getPrincipal()).thenReturn(null);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    String scope = StringUtils.randomAlphanumeric(20);
    Set<String> scopes = new HashSet<>();
    scopes.add(scope);
    when(oauthRequest.getScope()).thenReturn(scopes);
    assertEquals(scopes, apiTokenAdapter.getScopes());
  }

  @Test
  public void testGetGrantTypeNoAuth() {
    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    when(oauth2Authentication.getOAuth2Request()).thenReturn(null);
    assertEquals("", apiTokenAdapter.getGrantType());
  }

  @Test
  public void testGetGrantType() {
    when(oauth2Authentication.getPrincipal()).thenReturn(null);

    AccuroApiTokenAdapter apiTokenAdapter =
        new AccuroApiTokenAdapter(oauth2Authentication, tokenRequestDetails);

    String grantType = StringUtils.randomAlphanumeric(20);
    when(oauthRequest.getGrantType()).thenReturn(grantType);
    assertEquals(grantType, apiTokenAdapter.getGrantType());
  }
}
