
package com.qhrtech.emr.restapi.security;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.restapi.util.AccapiScopes;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.thymeleaf.util.StringUtils;

public class AccuroApiSecurityExpresssionMethodsTest {

  OAuth2Authentication oauth2Authentication;
  OAuth2Request oauthRequest;
  ApiSecurityContext context;

  @Before
  public void setUp() throws Exception {
    oauth2Authentication = mock(OAuth2Authentication.class);

    context = mock(ApiSecurityContext.class);
    oauthRequest = mock(OAuth2Request.class);

    when(oauth2Authentication.getOAuth2Request()).thenReturn(oauthRequest);
  }

  @Test
  public void testIsQhrAuthorizedUser() {

    AccuroApiSecurityExpressionMethods accuroApiSecurityExpressionMethods =
        new AccuroApiSecurityExpressionMethods(oauth2Authentication);

    String scope = AccapiScopes.QHR_AUTHORIZED_USER.getApiScope();
    Set<String> scopes = new HashSet<>();
    scopes.add(scope);

    when(oauthRequest.getScope()).thenReturn(scopes);
    assertTrue(accuroApiSecurityExpressionMethods.isQhrAuthorizedUser());
  }

  @Test
  public void testIsQhrFirstParty() {

    AccuroApiSecurityExpressionMethods accuroApiSecurityExpressionMethods =
        new AccuroApiSecurityExpressionMethods(oauth2Authentication);

    String scope = AccapiScopes.QHR_FIRST_PARTY.getApiScope();
    Set<String> scopes = new HashSet<>();
    scopes.add(scope);

    when(oauthRequest.getScope()).thenReturn(scopes);
    assertTrue(accuroApiSecurityExpressionMethods.isQhrAuthorizedUser());
  }

  @Test
  public void testRandomScopes() {

    AccuroApiSecurityExpressionMethods accuroApiSecurityExpressionMethods =
        new AccuroApiSecurityExpressionMethods(oauth2Authentication);

    String scope = StringUtils.randomAlphanumeric(20);
    Set<String> scopes = new HashSet<>();
    scopes.add(scope);

    when(oauthRequest.getScope()).thenReturn(scopes);
    assertFalse(accuroApiSecurityExpressionMethods.isQhrAuthorizedUser());
  }

  @Test
  public void testQhrFirstPartyAndAuthorizedUser() {

    String scope = AccapiScopes.QHR_FIRST_PARTY.getApiScope();
    String scope1 = AccapiScopes.QHR_AUTHORIZED_USER.getApiScope();
    Set<String> scopes = new HashSet<>();
    scopes.add(scope);
    scopes.add(scope1);

    AccuroApiSecurityExpressionMethods accuroApiSecurityExpressionMethods =
        new AccuroApiSecurityExpressionMethods(oauth2Authentication);

    when(oauthRequest.getScope()).thenReturn(scopes);
    assertTrue(accuroApiSecurityExpressionMethods.isQhrAuthorizedUser());
  }

  @Test
  public void testOkta() {

    AccuroApiSecurityExpressionMethods accuroApiSecurityExpressionMethods =
        new AccuroApiSecurityExpressionMethods(oauth2Authentication);

    when(oauthRequest.getResourceIds()).thenReturn(
        Collections.singleton(OktaJwtAccessTokenConverter.OKTA));
    assertTrue(accuroApiSecurityExpressionMethods.isOktaClient());
  }

}
