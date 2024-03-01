
package com.qhrtech.emr.restapi.endpoints.utilities;

import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;


public abstract class AbstractEndpointIntegrationTest<T extends AbstractEndpoint>
    extends AbstractEndpointTest<T> {

  protected final DataSource ds;

  public AbstractEndpointIntegrationTest(T endpoint, Class<T> endpointType) throws IOException {
    super(endpoint, endpointType);
    ds = getDs();
  }

  protected Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  protected AuditLogUser defaultUser() {
    return new AuditLogUser(1, 1, "", "Accuro API Integration Test", "");
  }

  protected AuditLogUser defaultUser(int userId) {
    return new AuditLogUser(userId, 1, "", "Accuro API Integration Test", "");
  }

  protected DataSource getDs() throws IOException {
    Properties p = new Properties();
    InputStream stream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
    if (stream != null) {
      p.load(stream);
    } else {
      throw new FileNotFoundException("no jdbc");
    }

    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClass(p.getProperty("driverClassName"));
    ds.setJdbcUrl(p.getProperty("db.url"));
    if (p.containsKey("username")) {
      ds.setUser(p.getProperty("username"));
      ds.setPassword(p.getProperty("password"));
    }
    return ds;
  }

  protected void executeLocalQuery(PreparedStatementBuffer query) throws SQLException {
    try (PreparedStatement statement = query.generateStatement(getConnection())) {
      statement.executeUpdate();
    }
  }

}
