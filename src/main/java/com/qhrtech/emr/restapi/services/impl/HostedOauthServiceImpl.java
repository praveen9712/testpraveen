/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.security.OauthClient;
import com.qhrtech.emr.restapi.security.OauthScope;
import com.qhrtech.emr.restapi.security.db.HostedOauthDao;
import com.qhrtech.emr.restapi.services.ClientListService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Praveen.ktheegala
 */
@Component
public class HostedOauthServiceImpl implements ClientListService {

  private static final Logger log = Logger.getLogger(HostedOauthServiceImpl.class);

  @Autowired
  private DataSource hostedOauthDataSource;

  public HostedOauthDao getRegistryServDao(Connection c) {
    return new HostedOauthDao(c);
  }


  @Override
  public Collection<AccuroOAuthClient> getAllClients() {

    Collection<OauthClient> clients = null;
    try {
      Connection c = hostedOauthDataSource.getConnection();
      HostedOauthDao hostedOauthDao = getRegistryServDao(c);
      clients = hostedOauthDao.getAllClients(null);
    } catch (SQLException ex) {
      log.error("Error while getting the clients.", ex);
    }
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
    Collection<OauthClient> clients = null;
    try {
      Connection c = hostedOauthDataSource.getConnection();
      HostedOauthDao hostedOauthDao = getRegistryServDao(c);
      clients = hostedOauthDao.getAllClients(clientIds);
    } catch (SQLException ex) {
      log.error("Error while getting the clients.", ex);
    }
    return convertClients(clients);
  }

  @Override
  public Set<AccuroScope> getAllScopes() {
    Collection<OauthScope> scopes = null;
    try {
      Connection c = hostedOauthDataSource.getConnection();
      HostedOauthDao hostedOauthDao = getRegistryServDao(c);
      scopes = hostedOauthDao.getAllScopes(null);
    } catch (SQLException ex) {
      log.error("Error while getting the scopes.", ex);
    }
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
    Collection<OauthScope> scopes = null;
    try {
      Connection c = hostedOauthDataSource.getConnection();
      HostedOauthDao hostedOauthDao = getRegistryServDao(c);
      scopes = hostedOauthDao.getAllScopes(scopeIds);
    } catch (SQLException ex) {
      log.error("Error while getting the scopes.", ex);
    }
    return convertScopes(scopes);
  }


  private Set<AccuroOAuthClient> convertClients(Collection<OauthClient> clients) {
    Set<AccuroOAuthClient> set = new HashSet<>();
    for (OauthClient client : clients) {
      String clientId = client.getId() != null ? client.getId().trim() : client.getId();
      AccuroOAuthClient c =
          new AccuroOAuthClient(client.getName(), clientId, null,
              trimSpaces(client.getScopes()), trimSpaces(client.getGrantTypes()), null,
              trimSpaces(client.getRedirects()));
      c.setClientSecret(client.getSecret());
      if (client.getAccessTokenValidity() != null) {
        c.setAccessTokenValiditySeconds(Math.toIntExact(client.getAccessTokenValidity()));
      }
      if (client.getRefreshTokenValidity() != null) {
        c.setRefreshTokenValiditySeconds(Math.toIntExact(client.getRefreshTokenValidity()));
      }
      set.add(c);
    }
    return set;
  }

  private Set<AccuroScope> convertScopes(Collection<OauthScope> scopes) {
    Set<AccuroScope> set = new HashSet<>();
    for (OauthScope scope : scopes) {
      AccuroScope s = new AccuroScope(scope.getId(), scope.getName(), scope.getSummary());
      set.add(s);
    }
    return set;
  }

  /**
   * Trim spaces in comma seprated strings. Converts refresh_token, client_credentials, password,
   * authorization_code to refresh_token,client_credentials,password,authorization_code
   *
   * @param input
   * @return
   */
  private String trimSpaces(String input) {
    if (input == null) {
      return null;
    }

    List<String> tokenList = new ArrayList<>();
    String[] tokens = input.split(",");
    for (String token : tokens) {
      tokenList.add(token.trim());
    }
    return String.join(",", tokenList);
  }

}
