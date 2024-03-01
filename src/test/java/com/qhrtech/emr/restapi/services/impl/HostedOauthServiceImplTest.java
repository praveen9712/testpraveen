/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.security.OauthClient;
import com.qhrtech.emr.restapi.security.OauthScope;
import com.qhrtech.emr.restapi.security.db.HostedOauthDao;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class HostedOauthServiceImplTest {

  private static final String CLIENT_ID = "clientId";
  private static final String SCOPE_ID = "scopeId";

  @Mock
  private DataSource hostedOauthDataSource;

  @Mock
  HostedOauthDao hostedOauthDao;

  @InjectMocks
  @Spy
  private HostedOauthServiceImpl registryServ;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    Connection connection = mock(Connection.class);

    registryServ = spy(new HostedOauthServiceImpl());
    openMocks(this);
    when(hostedOauthDataSource.getConnection()).thenReturn(connection);
    doReturn(hostedOauthDao).when(registryServ).getRegistryServDao(connection);


    when(hostedOauthDao.getAllClients(Collections.singleton(CLIENT_ID)))
        .thenReturn(Collections.singleton(new OauthClient()));
    when(hostedOauthDao.getAllScopes(Collections.singleton(SCOPE_ID)))
        .thenReturn(Collections.singleton(new OauthScope()));
    when(hostedOauthDao.getAllClients(null))
        .thenReturn(Collections.singleton(new OauthClient()));
    when(hostedOauthDao.getAllScopes(null))
        .thenReturn(Collections.singleton(new OauthScope()));

  }

  private void setFieldValue(Object object, String fieldName, Object value) throws Exception {
    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  @Test
  public void testGetAllClients() throws Exception {
    OauthClient client = new OauthClient();
    client.setGrantTypes("refresh_token, client_credentials, password, authorization_code ");
    client.setScopes(" scope1, scope2,  scope3 ");
    client.setRedirects(" URL1 , URL2 ,  URL3 ");
    client.setId(" clientId ");
    when(hostedOauthDao.getAllClients(null))
        .thenReturn(Collections.singleton(client));
    Collection<AccuroOAuthClient> clients = registryServ.getAllClients();
    assertEquals(1, clients.size());
    Set<String> scopes = clients.stream().findFirst().get().getAuthorizedGrantTypes();

    String clientId = clients.stream().findFirst().get().getClientId();

    assertEquals("clientId", clientId);
    assertTrue(scopes.contains("refresh_token"));
    assertTrue(scopes.contains("client_credentials"));
    assertTrue(scopes.contains("password"));
    assertTrue(scopes.contains("password"));

    Set<String> redirectUri = clients.stream().findFirst().get().getRegisteredRedirectUri();
    assertTrue(redirectUri.contains("URL1"));
    assertTrue(redirectUri.contains("URL2"));
    assertTrue(redirectUri.contains("URL3"));



  }

  @Test
  public void testLookupClient() {
    AccuroOAuthClient client = registryServ.lookupClient(CLIENT_ID);
    assertNotNull(client);
  }

  @Test
  public void testLookupClients() {
    Collection<AccuroOAuthClient> clients =
        registryServ.lookupClients(Collections.singleton(CLIENT_ID));
    assertEquals(1, clients.size());
  }

  @Test
  public void testGetAllScopes() {
    Set<AccuroScope> scopes = registryServ.getAllScopes();
    assertEquals(1, scopes.size());
  }

  @Test
  public void testLookupScope() {
    AccuroScope scope = registryServ.lookupScope(SCOPE_ID);
    assertNotNull(scope);
  }

  @Test
  public void testLookupScopes() {
    Collection<AccuroScope> scopes = registryServ.lookupScopes(Collections.singleton(SCOPE_ID));
    assertEquals(1, scopes.size());
  }
}
