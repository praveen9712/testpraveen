/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.db;

import com.qhrtech.emr.accuro.db.AbstractDao;
import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kevin.kendall
 */
public class HostedTenantDao extends AbstractDao {

  private static final String[] HOST_FIELDS = {
      "name",
      "database_name",
      "document_db_custom_name",
      "db_user",
      "db_password"
  };

  private static final String[] DB_SERVERS_FIELDS = {
      "address",
      "port"
  };

  public HostedTenantDao(Connection conn) throws SQLException {
    super(conn);
  }

  public HostedTenant getTenant(String tenantId) throws SQLException {

    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("SELECT ").appendFields(HOST_FIELDS, "h");
    query.append(", ").appendFields(DB_SERVERS_FIELDS, "db");
    query.append("FROM host h ");
    query.append("INNER JOIN db_servers db ON h.db_server_id = db.db_server_id ");
    query.append("WHERE h.name = ? ", tenantId);

    try (ResultSet rSet = query.generateStatement(conn).executeQuery()) {
      if (!rSet.next()) {
        return null;
      }
      HostedTenant tenant = getTenantRs(rSet, 1);
      return tenant;
    } catch (Exception ex) {
      // re-throw SQLExceptions
      if (ex instanceof SQLException) {
        throw (SQLException) ex;
      }
      // Handle exceptions resulting from CypherUtils decryption
      log.error("Error decoding username or password.", ex);
      return null;
    }
  }

  private HostedTenant getTenantRs(ResultSet resultSet, int col) throws Exception {
    HostedTenant tenant = new HostedTenant();
    tenant.setName(resultSet.getString(col++));
    tenant.setDatabaseName(resultSet.getString(col++));
    tenant.setDocDatabaseName(resultSet.getString(col++));

    // encrypted user and password
    String user;
    String password;
    user = CypherUtil.decryptToString(resultSet.getString(col++));
    password = CypherUtil.decryptToString(resultSet.getString(col++));
    tenant.setUser(user);
    tenant.setPassword(password);

    // db servers
    tenant.setServerAddress(resultSet.getString(col++));// address
    tenant.setServerPort(resultSet.getInt(col++));// port

    return tenant;
  }
}
