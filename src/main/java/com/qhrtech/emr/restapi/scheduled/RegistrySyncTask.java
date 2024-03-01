/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.scheduled;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.services.impl.HostedOauthServiceImpl;
import com.qhrtech.util.SoftCache;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class RegistrySyncTask {

  private static final ExecutorService executor = Executors.newSingleThreadExecutor();
  private static final Logger log = Logger.getLogger(RegistrySyncTask.class);

  @Autowired
  @Qualifier("clientCache")
  public SoftCache<String, AccuroOAuthClient> clientCache;

  @Autowired
  @Qualifier("scopeCache")
  public SoftCache<String, AccuroScope> scopeCache;

  @Autowired
  private HostedOauthServiceImpl hostedOauthService;

  @Value("#{new Boolean('${oauth.nofetch:false}')}")
  private boolean oauthNoFetch;

  /**
   * @return a Future which can be used to determine if the refresh is complete.
   */
  @Scheduled(cron = "${registry.refresh.cron:0 0 0 * * *}")
  public Future<Void> refreshFromRegistry() {
    if (!oauthNoFetch) {
      log.info("updating oauth details from registry");
      return (Future<Void>) executor.submit(new RegistrySyncTask.ClientScopeUpdateTask());
    }
    return null;
  }

  public void stop() {
    executor.shutdown();
  }

  private class ClientScopeUpdateTask implements Runnable {

    @Override
    public void run() {

      // Get clients and scopes from remote service
      Collection<AccuroOAuthClient> clients;
      Collection<AccuroScope> scopes;

      try {
        clients = hostedOauthService.getAllClients();
        scopes = hostedOauthService.getAllScopes();
      } catch (RuntimeException ex) {
        log.error("Error retrieving clients and scopes from central registry.", ex);
        return;
      }

      // Update clients and clear client cache
      final SoftCache<String, AccuroOAuthClient> cCache = clientCache;
      synchronized (cCache) {
        for (AccuroOAuthClient authClient : clients) {
          clientCache.put(authClient.getClientId(), authClient);
        }
      }

      // Update scopes and clear scope cache
      final SoftCache<String, AccuroScope> sCache = scopeCache;
      synchronized (sCache) {
        for (AccuroScope scope : scopes) {
          scopeCache.put(scope.getId(), scope);
        }

      }
    }
  }
}
