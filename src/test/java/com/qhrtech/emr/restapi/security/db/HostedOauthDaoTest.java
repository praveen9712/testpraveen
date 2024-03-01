
package com.qhrtech.emr.restapi.security.db;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.security.OauthClient;
import com.qhrtech.emr.restapi.security.OauthScope;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class HostedOauthDaoTest {


  @Mock
  private Connection mockConnection;
  @Mock
  private PreparedStatement mockPreparedStatement;
  @Mock
  private ResultSet mockResultSet;

  private HostedOauthDao registryDao;

  private PodamFactory podamFactory;

  private Set<OauthClient> clientFixtures;
  private Set<OauthScope> scopeFixtures;

  @Before
  public void setUp() throws SQLException {
    openMocks(this);
    podamFactory = new PodamFactoryImpl();
    mockConnection = mock(Connection.class);
    registryDao = new HostedOauthDao(mockConnection);

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    scopeFixtures = Stream.generate(() -> podamFactory.manufacturePojo(OauthScope.class))
        .limit(3)
        .collect(Collectors.toSet());

    clientFixtures = Stream.generate(() -> podamFactory.manufacturePojo(OauthClient.class))
        .limit(5)
        .collect(Collectors.toSet());
  }

  @Test
  public void testGetAllScopes() throws SQLException {
    List<OauthScope> expectedScopes = new ArrayList<>();
    expectedScopes.add(podamFactory.manufacturePojo(OauthScope.class));
    expectedScopes.add(podamFactory.manufacturePojo(OauthScope.class));
    expectedScopes.add(podamFactory.manufacturePojo(OauthScope.class));

    ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.next()).thenReturn(true, true, true, false);
    when(resultSet.getString(ArgumentMatchers.eq("id"))).thenReturn(
        expectedScopes.get(0).getId(), expectedScopes.get(1).getId(),
        expectedScopes.get(2).getId());
    when(resultSet.getString(ArgumentMatchers.eq("name"))).thenReturn(
        expectedScopes.get(0).getName(), expectedScopes.get(1).getName(),
        expectedScopes.get(2).getName());
    when(resultSet.getString(ArgumentMatchers.eq("summary"))).thenReturn(
        expectedScopes.get(0).getSummary(), expectedScopes.get(1).getSummary(),
        expectedScopes.get(2).getSummary());

    when(mockPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(mockConnection.prepareStatement(ArgumentMatchers.anyString())).thenReturn(
        mockPreparedStatement);

    Set<OauthScope> actualScopes = registryDao.getAllScopes(null);

    assertEquals(expectedScopes.size(), actualScopes.size());
  }

  @Test
  public void testGetAllClients() throws SQLException {
    List<OauthClient> expectedClients = new ArrayList<>();
    expectedClients.add(podamFactory.manufacturePojo(OauthClient.class));
    expectedClients.add(podamFactory.manufacturePojo(OauthClient.class));
    expectedClients.add(podamFactory.manufacturePojo(OauthClient.class));

    ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.next()).thenReturn(true, true, true, false);
    when(resultSet.getString(ArgumentMatchers.eq("id"))).thenReturn(
        expectedClients.get(0).getId(), expectedClients.get(1).getId(),
        expectedClients.get(2).getId());
    when(resultSet.getString(ArgumentMatchers.eq("name"))).thenReturn(
        expectedClients.get(0).getName(), expectedClients.get(1).getName(),
        expectedClients.get(2).getName());
    when(resultSet.getString(ArgumentMatchers.eq("secret"))).thenReturn(
        expectedClients.get(0).getSecret(), expectedClients.get(1).getSecret(),
        expectedClients.get(2).getSecret());
    when(resultSet.getString(ArgumentMatchers.eq("scopes"))).thenReturn(
        expectedClients.get(0).getScopes(), expectedClients.get(1).getScopes(),
        expectedClients.get(2).getScopes());
    when(resultSet.getString(ArgumentMatchers.eq("grant_types"))).thenReturn(
        expectedClients.get(0).getGrantTypes(),
        expectedClients.get(1).getGrantTypes(),
        expectedClients.get(2).getGrantTypes());
    when(resultSet.getString(ArgumentMatchers.eq("redirects"))).thenReturn(
        expectedClients.get(0).getRedirects(),
        expectedClients.get(1).getRedirects(),
        expectedClients.get(2).getRedirects());
    when(resultSet.getLong(ArgumentMatchers.eq("access_token_validity"))).thenReturn(
        expectedClients.get(0).getAccessTokenValidity(),
        expectedClients.get(1).getAccessTokenValidity(),
        expectedClients.get(2).getAccessTokenValidity());
    when(resultSet.getLong(ArgumentMatchers.eq("refresh_token_validity"))).thenReturn(
        expectedClients.get(0).getRefreshTokenValidity(),
        expectedClients.get(1).getRefreshTokenValidity(),
        expectedClients.get(2).getRefreshTokenValidity());

    when(mockPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(mockConnection.prepareStatement(ArgumentMatchers.anyString())).thenReturn(
        mockPreparedStatement);

    Set<OauthClient> actualClients = registryDao.getAllClients(null);
    assertEquals(expectedClients.size(), actualClients.size());

  }
}


