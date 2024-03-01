/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.services.ClientListService;
import com.qhrtechnologies.ws.registry.OAuth2ClientDetails;
import com.qhrtechnologies.ws.registry.OAuth2Scope;
import com.qhrtechnologies.ws.registry.serializer.RegistrySerializationManager;
import com.qhrtechnologies.ws.registry.services.OAuth2ServiceInterface;
import com.qhrtechnologies.ws.registry.services.ServiceFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kevin.kendall
 */
@Component
public class ClientListServiceImpl implements ClientListService {

  private OAuth2ServiceInterface remoteService;
  @Value("${registry.url}")
  private String registryUrl;
  @Value("${registry.key}")
  private String registryKey;


  @Override
  public Collection<AccuroOAuthClient> getAllClients() {
    checkService();
    Collection<OAuth2ClientDetails> clients = remoteService.getClients();
    return convertClients(clients);
  }

  @Override
  public AccuroOAuthClient lookupClient(String clientId) {
    Collection<AccuroOAuthClient> clients = lookupClients(Collections.singleton(clientId));
    Iterator<AccuroOAuthClient> it = clients.iterator();
    return it.hasNext() ? it.next() : null;
  }

  @Override
  public Collection<AccuroOAuthClient> lookupClients(Collection<String> clientIds) {
    checkService();
    Collection<OAuth2ClientDetails> clients = remoteService.lookupClients(clientIds);
    return convertClients(clients);
  }

  @Override
  public Set<AccuroScope> getAllScopes() {
    checkService();
    Collection<OAuth2Scope> scopes = remoteService.getScopes();
    return convertScopes(scopes);
  }

  @Override
  public AccuroScope lookupScope(String scopeId) {
    Collection<AccuroScope> scopes = lookupScopes(Collections.singleton(scopeId));
    Iterator<AccuroScope> it = scopes.iterator();
    return it.hasNext() ? it.next() : null;
  }

  @Override
  public Collection<AccuroScope> lookupScopes(Collection<String> scopeIds) {
    checkService();
    Collection<OAuth2Scope> scopes = remoteService.lookupScopes(scopeIds);
    return convertScopes(scopes);
  }

  private synchronized void checkService() {
    if (remoteService != null) {
      return;
    }
    ServiceFactory serviceFactory = new ServiceFactory(registryUrl + "rest/", registryKey);
    remoteService = serviceFactory.createFactory(
        OAuth2ServiceInterface.class,
        RegistrySerializationManager.Format.JSON,
        (SSLSocketFactory) SSLSocketFactory.getDefault());
  }

  private Set<AccuroOAuthClient> convertClients(Collection<OAuth2ClientDetails> clients) {
    Set<AccuroOAuthClient> set = new HashSet<>();
    for (OAuth2ClientDetails client : clients) {
      AccuroOAuthClient c = new AccuroOAuthClient();
      c.setClientId(client.getClientId());
      c.setDisplayName(client.getDisplay());
      c.setClientSecret(client.getClientSecret());
      c.setScope(client.getScopes());
      c.setAuthorizedGrantTypes(client.getGrantTypes());
      c.setRegisteredRedirectUri(new HashSet<>(client.getRedirects()));
      if (client.getAccessExpiry() != null) {
        c.setAccessTokenValiditySeconds(client.getAccessExpiry());
      }
      if (client.getRefreshExpiry() != null) {
        c.setRefreshTokenValiditySeconds(client.getRefreshExpiry());
      }
      set.add(c);
    }
    return set;
  }

  private Set<AccuroScope> convertScopes(Collection<OAuth2Scope> scopes) {
    Set<AccuroScope> set = new HashSet<>();
    for (OAuth2Scope scope : scopes) {
      AccuroScope s = new AccuroScope(scope.getId(), scope.getName(), scope.getSummary());
      set.add(s);
    }
    return set;
  }

}
