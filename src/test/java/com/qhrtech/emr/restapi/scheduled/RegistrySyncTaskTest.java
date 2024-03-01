/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.scheduled;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.services.impl.HostedOauthServiceImpl;
import com.qhrtech.util.SoftCache;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrySyncTaskTest {

  @Mock
  public SoftCache<String, AccuroOAuthClient> clientCache;

  @Mock
  private DataSource dataSource;

  @Mock
  public SoftCache<String, AccuroScope> scopeCache;

  @Mock
  private HostedOauthServiceImpl clientListService;

  @InjectMocks
  @Spy
  private RegistrySyncTask registrySyncTask;


  @Mock
  private Connection mockConnection;

  private PodamFactory podamFactory;

  private Set<AccuroScope> scopeFixtures;
  private Set<AccuroOAuthClient> clientFixtures;

  @Before
  public void setUp() throws SQLException {
    registrySyncTask = spy(new RegistrySyncTask());

    openMocks(this);

    podamFactory = new PodamFactoryImpl();
    scopeFixtures = Stream.generate(() -> podamFactory.manufacturePojo(AccuroScope.class))
        .limit(10)
        .collect(Collectors.toSet());

    clientFixtures = Stream.generate(() -> podamFactory.manufacturePojo(AccuroOAuthClient.class))
        .limit(5)
        .collect(Collectors.toSet());

    doReturn(clientFixtures).when(clientListService).getAllClients();
    doReturn(scopeFixtures).when(clientListService).getAllScopes();

  }


  @Test
  public void testRefreshMultipleCalls() throws Exception {

    registrySyncTask.refreshFromRegistry().get();
    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService, times(2)).getAllClients();
    verify(clientListService, times(2)).getAllScopes();


  }

  @Test
  public void testRefreshFromRegistry() throws Exception {

    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService).getAllClients();
    verify(clientListService).getAllScopes();


  }

  @Test
  public void testRefreshFromRegistryScopesError()
      throws SQLException, ExecutionException, InterruptedException {
    doThrow(new RuntimeException("test exception")).when(clientListService).getAllScopes();

    // test
    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService).getAllScopes();

    verify(clientCache, never()).clear();
    verify(scopeCache, never()).clear();
  }

  @Test
  public void testRefreshFromRegistryClientsDatabaseError()
      throws ExecutionException, InterruptedException {

    // test
    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService).getAllScopes();


  }

  @Test
  public void testRefreshFromRegistryScopesDatabaseError()
      throws SQLException, ExecutionException, InterruptedException {

    // test
    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService).getAllScopes();


  }

  @Test
  public void testRefreshFromRegistryClientsError()
      throws SQLException, ExecutionException, InterruptedException {

    doThrow(new RuntimeException("test exception")).when(clientListService).getAllClients();

    // test
    registrySyncTask.refreshFromRegistry().get();

    verify(clientListService).getAllClients();
    verify(clientCache, never()).clear();
    verify(scopeCache, never()).clear();
  }

  // ran in alphabetical order. After shutdown the sync task should no longer execute
  @Test(expected = RejectedExecutionException.class)
  public void ztestShutdown() {
    registrySyncTask.stop();
    registrySyncTask.refreshFromRegistry();
  }
}
