
package com.qhrtech.emr.restapi.security.apicontext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.AuthorizedClient;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.restapi.security.AccuroLoginAuthenticationProvider;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import java.util.Collections;
import java.util.Set;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

public class DefaultAccuroApiContextServiceTest {


  public static final String SQUID = "SQUID";
  private AccuroLoginAuthenticationProvider mockAccuroLoginAuthenticationProvider;
  private AccuroApiService mockAccuroApiService;
  private AccuroApiContextManager mockAccuroApiContextManager;
  private AccuroApiTokenAdapter mockAccuroApiTokenAdapter;
  private AuthorizedClientManager mockAuthorizedClientManager;

  private AccuroApiContextService accuroApiContextService;

  // Test Values
  private final String tenantId = "1";
  private final String userId = "userIdentifier";
  private final String requestUri =
      "https://accuroapi.com/accapi/provider-portal/scheduler/statuses";

  @Before
  public void setUp() throws Exception {

    mockAccuroApiService = mock(AccuroApiService.class);
    mockAccuroLoginAuthenticationProvider = mock(AccuroLoginAuthenticationProvider.class);
    mockAccuroApiTokenAdapter = mock(AccuroApiTokenAdapter.class);
    mockAccuroApiContextManager = mock(AccuroApiContextManager.class);
    mockAuthorizedClientManager = mock(AuthorizedClientManager.class);

    when(mockAccuroApiService.getImpl(AccuroApiContextManager.class, tenantId)).thenReturn(
        mockAccuroApiContextManager);
    when(mockAccuroApiService.getImpl(AuthorizedClientManager.class, tenantId, true)).thenReturn(
        mockAuthorizedClientManager);

    accuroApiContextService = new DefaultAccuroApiContextService(mockAccuroApiService,
        mockAccuroLoginAuthenticationProvider);
  }

  @Test
  public void getAccuroApiUserContextClientCredentials()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    String tenantIdfromUri = "1";

    TokenRequestDetails tokenRequestDetails =
        new TokenRequestDetails(HttpMethod.GET, requestUri, tenantIdfromUri);

    setMocks(tokenRequestDetails, tenantId, AccuroApiTokenType.OKTA_CLIENT_CREDENTIALS,
        null);

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();

    when(mockAccuroApiContextManager.getAccuroApiContext(null, true)).thenReturn(
        expectedAccuroApiContext);
    when(mockAccuroApiTokenAdapter.getClientId()).thenReturn("1");
    when(mockAuthorizedClientManager.search("1", null, null,
        null, null, null)).thenReturn(
            getAuthorizedClients());

    AccuroApiContext accuroApiUserContext =
        accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);

    assertEquals(expectedAccuroApiContext, accuroApiUserContext);
    verify(mockAccuroApiContextManager).getAccuroApiContext(null, true);
  }

  @Test
  public void getAccuroApiUserContextOktaProviderQhrFirstParty()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    mockOktaProviderTokenQhrFirstParty();

    AccuroUser expectedUser = new AccuroUser();

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();
    expectedAccuroApiContext.setIdentityValidFrom(new LocalDateTime().minusDays(1));
    expectedAccuroApiContext.setIdentityValidTo(new LocalDateTime().plusDays(1));
    expectedAccuroApiContext.setAccuroUser(expectedUser);

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID, true)).thenReturn(
        expectedAccuroApiContext);

    AccuroApiContext accuroApiUserContext =
        accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);

    assertEquals(expectedAccuroApiContext, accuroApiUserContext);
    verify(mockAccuroApiContextManager).getAccuroApiContext(userId, SQUID, true);
  }

  @Test
  public void getAccuroApiUserContextOktaProvider()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    mockOktaProviderToken();

    AccuroUser expectedUser = new AccuroUser();

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();
    expectedAccuroApiContext.setIdentityValidFrom(new LocalDateTime().minusDays(1));
    expectedAccuroApiContext.setIdentityValidTo(new LocalDateTime().plusDays(1));
    expectedAccuroApiContext.setAccuroUser(expectedUser);

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID, true)).thenReturn(
        expectedAccuroApiContext);
    when(mockAccuroApiTokenAdapter.getClientId()).thenReturn("1");
    when(mockAuthorizedClientManager.search("1", null, null,
        null, null, null)).thenReturn(
            getAuthorizedClients());

    AccuroApiContext accuroApiUserContext =
        accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);

    assertEquals(expectedAccuroApiContext, accuroApiUserContext);
    verify(mockAccuroApiContextManager).getAccuroApiContext(userId, SQUID, true);
  }

  @Test(expected = FilterException.class)
  public void getAccuroApiUserContextOktaProviderNoAuthorzedClient()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    mockOktaProviderToken();

    AccuroUser expectedUser = new AccuroUser();

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();
    expectedAccuroApiContext.setIdentityValidFrom(new LocalDateTime().minusDays(1));
    expectedAccuroApiContext.setIdentityValidTo(new LocalDateTime().plusDays(1));
    expectedAccuroApiContext.setAccuroUser(expectedUser);

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID, true)).thenReturn(
        expectedAccuroApiContext);
    when(mockAccuroApiTokenAdapter.getClientId()).thenReturn("1");
    when(mockAuthorizedClientManager.search("1", null, null,
        null, null, null)).thenReturn(null);

    accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);
  }

  @Test(expected = FilterException.class)
  public void getAccuroApiUserContextOktaProviderCantAuthenticate()
      throws Exception {
    mockOktaProviderToken();

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID, true))
        .thenThrow(new DatabaseInteractionException(""));

    accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);
  }

  @Test(expected = FilterException.class)
  public void getAccuroApiUserContextOktaProviderNoContext()
      throws Exception {
    mockOktaProviderToken();

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID, true))
        .thenReturn(null);

    accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);
  }

  @Test(expected = FilterException.class)
  public void getAccuroApiUserContextOktaProviderInvalidUserExpiredBefore()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    mockOktaProviderToken();

    AccuroUser expectedUser = new AccuroUser();

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();
    expectedAccuroApiContext.setIdentityValidFrom(new LocalDateTime().minusDays(100));
    expectedAccuroApiContext.setIdentityValidTo(new LocalDateTime().minusDays(10));
    expectedAccuroApiContext.setAccuroUser(expectedUser);

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID,
        true)).thenReturn(expectedAccuroApiContext);

    accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);
  }

  @Test(expected = FilterException.class)
  public void getAccuroApiUserContextOktaProviderInvalidUserExpiredAfter()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {
    mockOktaProviderToken();

    AccuroUser expectedUser = new AccuroUser();

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();
    expectedAccuroApiContext.setIdentityValidFrom(new LocalDateTime().plusDays(100));
    expectedAccuroApiContext.setIdentityValidTo(new LocalDateTime().plusDays(10));
    expectedAccuroApiContext.setAccuroUser(expectedUser);

    when(mockAccuroApiContextManager.getAccuroApiContext(userId, SQUID,
        true)).thenReturn(expectedAccuroApiContext);

    accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);
  }

  @Test
  public void getAccuroApiUserContextLegacyProvider()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      TimeZoneNotFoundException {

    Integer userIdentifier = 1;
    TokenRequestDetails tokenRequestDetails =
        new TokenRequestDetails(HttpMethod.GET, requestUri, null);
    setMocks(tokenRequestDetails, tenantId, AccuroApiTokenType.LEGACY_PROVIDER,
        Integer.toString(userIdentifier));

    AccuroApiContext expectedAccuroApiContext = new AccuroApiContext();

    when(mockAccuroApiContextManager.getAccuroApiContext(userIdentifier, true)).thenReturn(
        expectedAccuroApiContext);

    AccuroApiContext accuroApiUserContext =
        accuroApiContextService.getAccuroApiUserContext(mockAccuroApiTokenAdapter);

    assertEquals(expectedAccuroApiContext, accuroApiUserContext);
    verify(mockAccuroApiContextManager).getAccuroApiContext(userIdentifier, true);
  }

  private void mockOktaProviderTokenQhrFirstParty() {
    TokenRequestDetails tokenRequestDetails =
        new TokenRequestDetails(HttpMethod.GET, requestUri, null);

    setMocks(tokenRequestDetails, tenantId, AccuroApiTokenType.OKTA_PROVIDER,
        userId, Collections.singleton("qhr-first-party"));
  }

  private void mockOktaProviderToken() {
    TokenRequestDetails tokenRequestDetails =
        new TokenRequestDetails(HttpMethod.GET, requestUri, null);

    setMocks(tokenRequestDetails, tenantId, AccuroApiTokenType.OKTA_PROVIDER,
        userId, Collections.emptySet());
  }

  private void setMocks(TokenRequestDetails tokenRequestDetails, String tenantId,
      AccuroApiTokenType oktaClientCredentials, String userIdentifier) {
    when(mockAccuroApiTokenAdapter.getTokenType()).thenReturn(
        oktaClientCredentials);
    when(mockAccuroApiTokenAdapter.getTenantId()).thenReturn(tenantId);
    when(mockAccuroApiTokenAdapter.getTokenRequestDetails()).thenReturn(tokenRequestDetails);
    when(mockAccuroApiTokenAdapter.getUserIdentifier()).thenReturn(userIdentifier);
  }

  private void setMocks(TokenRequestDetails tokenRequestDetails, String tenantId,
      AccuroApiTokenType oktaClientCredentials, String userIdentifier, Set<String> scope) {
    when(mockAccuroApiTokenAdapter.getTokenType()).thenReturn(
        oktaClientCredentials);
    when(mockAccuroApiTokenAdapter.getScopes()).thenReturn(
        scope);
    when(mockAccuroApiTokenAdapter.getTenantId()).thenReturn(tenantId);
    when(mockAccuroApiTokenAdapter.getTokenRequestDetails()).thenReturn(tokenRequestDetails);
    when(mockAccuroApiTokenAdapter.getUserIdentifier()).thenReturn(userIdentifier);
  }

  private Envelope<AuthorizedClient> getAuthorizedClients() {
    Envelope<AuthorizedClient> authorizedClients = new Envelope<>();
    AuthorizedClient authorizedClient = new AuthorizedClient();
    authorizedClient.setClientId("1");
    authorizedClients.setContents(Collections.singletonList(authorizedClient));
    return authorizedClients;
  }
}
