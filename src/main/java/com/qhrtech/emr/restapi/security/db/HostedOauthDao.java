/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.db;

import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.restapi.security.OauthClient;
import com.qhrtech.emr.restapi.security.OauthScope;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Praveen.ktheegala
 */
public class HostedOauthDao {

  private static final String[] CLIENT_FIELDS = {
      "id",
      "name",
      "secret",
      "scopes",
      "grant_types",
      "redirects",
      "access_token_validity",
      "refresh_token_validity"
  };
  private static final String[] SCOPE_FIELDS = {
      "id",
      "name",
      "summary"
  };
  private final Connection connection;

  public HostedOauthDao(Connection connection) {
    this.connection = connection;
  }


  public Set<OauthScope> getAllScopes(Collection<String> scopeIds) throws SQLException {
    Set<OauthScope> result = new HashSet<>();

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(SCOPE_FIELDS);
    query.append("FROM oauth_scopes ");

    if (CollectionUtils.isNotEmpty(scopeIds)) {
      query.append(" where id ").appendWhere(scopeIds);
    }

    try (ResultSet rSet = query.generateStatement(connection).executeQuery()) {
      while (rSet.next()) {
        OauthScope scope = getOAuthScopeRs(rSet, 1);
        result.add(scope);
      }
    }

    return result;
  }

  public Set<OauthClient> getAllClients(Collection<String> clientIds) throws SQLException {
    Set<OauthClient> result = new HashSet<>();

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(CLIENT_FIELDS);
    query.append(" FROM oauth_clients ");

    if (CollectionUtils.isNotEmpty(clientIds)) {
      query.append(" where id ").appendWhere(clientIds);
    }
    try (ResultSet rSet = query.generateStatement(connection).executeQuery()) {
      while (rSet.next()) {
        OauthClient client = getOAuthClientDetails(rSet, 1);
        result.add(client);
      }
    }
    return result;
  }

  private OauthClient getOAuthClientDetails(ResultSet resultSet, int index)
      throws SQLException {
    OauthClient oauthClient = new OauthClient();
    oauthClient.setId(resultSet.getString("id"));
    oauthClient.setName(resultSet.getString("name"));
    oauthClient.setSecret(resultSet.getString("secret"));
    oauthClient.setScopes(resultSet.getString("scopes"));
    oauthClient.setGrantTypes(resultSet.getString("grant_types"));
    oauthClient.setRedirects(resultSet.getString("redirects"));
    oauthClient.setAccessTokenValidity(resultSet.getLong("access_token_validity"));
    oauthClient.setRefreshTokenValidity(resultSet.getLong("refresh_token_validity"));

    return oauthClient;
  }

  private OauthScope getOAuthScopeRs(ResultSet resultSet, int index)
      throws SQLException {
    OauthScope oauthScope = new OauthScope();
    oauthScope.setId(resultSet.getString("id"));
    oauthScope.setName(resultSet.getString("name"));
    oauthScope.setSummary(resultSet.getString("summary"));

    return oauthScope;
  }
}
