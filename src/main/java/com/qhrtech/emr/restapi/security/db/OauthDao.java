/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.db;

import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author kevin.kendall
 */
public class OauthDao {

  private static final String[] CLIENT_FIELDS = {
      "client_id",
      "display_name",
      "resource_ids",
      "scope",
      "authorized_grant_types",
      "authorities",
      "web_server_redirect_uri",
      "client_secret",
      "access_token_validity",
      "refresh_token_validity"
  };
  private static final String[] SCOPE_FIELDS = {
      "scope_id",
      "scope_name",
      "scope_summary"
  };
  private final Connection connection;

  public OauthDao(Connection connection) {
    this.connection = connection;
  }

  private static String buildString(Collection collection) {
    if (collection == null || collection.isEmpty()) {
      return null;
    }
    return Arrays.toString(collection.toArray()).replaceAll("^\\[?|\\]?$", "").replaceAll(", ",
        ",");
  }

  public AccuroOAuthClient getClient(String clientId) throws SQLException {

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(CLIENT_FIELDS);
    query.append("FROM oauth_client_details ");
    query.append("WHERE client_id = ? ", clientId);

    try (ResultSet rSet = query.generateStatement(connection).executeQuery()) {
      if (rSet.next()) {
        AccuroOAuthClient client = getOAuthClientRs(rSet, 1);
        return client;
      }
    }

    return null;
  }

  public void saveClients(Collection<AccuroOAuthClient> clients) throws SQLException {

    int[] result = updateClients(connection, clients);

    Set<AccuroOAuthClient> newClients = new HashSet<>();
    Iterator<AccuroOAuthClient> it = clients.iterator();
    for (int i : result) {
      AccuroOAuthClient client = it.next();
      if (i == 0) {
        newClients.add(client);
      }
    }

    if (!newClients.isEmpty()) {
      insertClients(connection, newClients);
    }
  }

  public void clearClients(Connection c) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM oauth_client_details ");
    query.generateStatement(c).execute();
  }

  private int[] updateClients(Connection c, Collection<AccuroOAuthClient> clients)
      throws SQLException {

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("UPDATE oauth_client_details ");
    query.append("SET ").appendSet(CLIENT_FIELDS, 1);
    query.append("WHERE client_id = ? ");

    PreparedStatement stmt = query.generateStatement(c);
    for (AccuroOAuthClient client : clients) {
      int col = 1;
      stmt.setString(col++, client.getDisplayName());
      stmt.setString(col++, buildString(client.getResourceIds()));
      stmt.setString(col++, buildString(client.getScope()));
      stmt.setString(col++, buildString(client.getAuthorizedGrantTypes()));
      stmt.setString(col++, buildString(client.getAuthorities()));
      stmt.setString(col++, buildString(client.getRegisteredRedirectUri()));
      stmt.setString(col++, client.getClientSecret());
      if (client.getAccessTokenValiditySeconds() == null) {
        stmt.setNull(col++, Types.NUMERIC);
      } else {
        stmt.setInt(col++, client.getAccessTokenValiditySeconds());
      }
      if (client.getRefreshTokenValiditySeconds() == null) {
        stmt.setNull(col++, Types.NUMERIC);
      } else {
        stmt.setInt(col++, client.getRefreshTokenValiditySeconds());
      }
      stmt.setString(col++, client.getClientId());
      stmt.addBatch();
    }
    int[] result = stmt.executeBatch();
    return result;
  }

  public void insertClients(Connection c, Collection<AccuroOAuthClient> clients)
      throws SQLException {

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("INSERT INTO oauth_client_details ( ");
    query.appendFields(CLIENT_FIELDS);
    query.append(") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

    PreparedStatement stmt = query.generateStatement(c);
    for (AccuroOAuthClient client : clients) {
      int col = 1;
      stmt.setString(col++, client.getClientId());
      stmt.setString(col++, client.getDisplayName());
      stmt.setString(col++, buildString(client.getResourceIds()));
      stmt.setString(col++, buildString(client.getScope()));
      stmt.setString(col++, buildString(client.getAuthorizedGrantTypes()));
      stmt.setString(col++, buildString(client.getAuthorities()));
      stmt.setString(col++, buildString(client.getRegisteredRedirectUri()));
      stmt.setString(col++, client.getClientSecret());
      if (client.getAccessTokenValiditySeconds() == null) {
        stmt.setNull(col++, Types.NUMERIC);
      } else {
        stmt.setInt(col++, client.getAccessTokenValiditySeconds());
      }
      if (client.getRefreshTokenValiditySeconds() == null) {
        stmt.setNull(col++, Types.NUMERIC);
      } else {
        stmt.setInt(col++, client.getRefreshTokenValiditySeconds());
      }
      stmt.addBatch();
    }
    stmt.executeBatch();
  }

  public Set<AccuroScope> getScopes(Set<String> scopes) throws SQLException {
    Set<AccuroScope> result = new HashSet<>();

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(SCOPE_FIELDS);
    query.append("FROM oauth_scopes ");
    query.append("WHERE scope_id ");
    query.appendWhere(scopes);

    try (ResultSet rSet = query.generateStatement(connection).executeQuery()) {
      while (rSet.next()) {
        AccuroScope scope = getAccuroScopeRs(rSet, 1);
        result.add(scope);
      }
    }

    return result;
  }

  public Set<AccuroScope> getAllScopes() throws SQLException {
    Set<AccuroScope> result = new HashSet<>();

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(SCOPE_FIELDS);
    query.append("FROM oauth_scopes ");

    try (ResultSet rSet = query.generateStatement(connection).executeQuery()) {
      while (rSet.next()) {
        AccuroScope scope = getAccuroScopeRs(rSet, 1);
        result.add(scope);
      }
    }

    return result;
  }

  public void clearScopes(Connection c) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM oauth_scopes ");
    query.generateStatement(c).execute();
  }

  public void insertScopes(Connection c, Collection<AccuroScope> scopes) throws SQLException {

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("INSERT INTO oauth_scopes ( ");
    query.appendFields(SCOPE_FIELDS);
    query.append(") VALUES ( ?, ?, ? ) ");

    PreparedStatement stmt = query.generateStatement(c);
    for (AccuroScope scope : scopes) {
      int col = 1;
      stmt.setString(col++, scope.getId());
      stmt.setString(col++, scope.getName());
      stmt.setString(col++, scope.getSummary());
      stmt.addBatch();
    }
    stmt.executeBatch();
  }

  private AccuroOAuthClient getOAuthClientRs(ResultSet resultSet, int index) throws SQLException {
    String clientId = resultSet.getString(index++);
    String displayName = resultSet.getString(index++);
    String resourceIds = resultSet.getString(index++);
    String scopes = resultSet.getString(index++);
    String grantTypes = resultSet.getString(index++);
    String authorities = resultSet.getString(index++);
    String redirectUris = resultSet.getString(index++);
    String secret = resultSet.getString(index++);
    AccuroOAuthClient client = new AccuroOAuthClient(displayName, clientId, resourceIds, scopes,
        grantTypes, authorities, redirectUris);
    client.setClientSecret(secret);
    int accesTokenValidity = resultSet.getInt(index++);
    if (!resultSet.wasNull()) {
      client.setAccessTokenValiditySeconds(accesTokenValidity);
    }
    int refreshTokenValidity = resultSet.getInt(index++);
    if (!resultSet.wasNull()) {
      client.setRefreshTokenValiditySeconds(refreshTokenValidity);
    }
    return client;
  }

  private AccuroScope getAccuroScopeRs(ResultSet resultSet, int index) throws SQLException {
    String id = resultSet.getString(index++);
    String name = resultSet.getString(index++);
    String summary = resultSet.getString(index++);
    AccuroScope scope = new AccuroScope(id, name, summary);
    return scope;
  }
}
