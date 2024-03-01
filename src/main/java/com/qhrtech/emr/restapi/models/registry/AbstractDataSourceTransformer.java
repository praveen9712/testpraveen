
package com.qhrtech.emr.restapi.models.registry;

import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.accuro.utils.Transformer;
import com.qhrtech.emr.restapi.security.db.CypherUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;

/**
 * Abstract Base Transformer for Registry DTO to DataSouce This class will assume the user name and
 * password of in the DTO are encrypted.
 *
 * This class will return a valid {@link DataSource} that can be used for connections provided the
 * input {@link TenantDataSourceResult} contains valid credentials
 *
 * The transformation method will throw {@link RuntimeException} if the user name and password
 * credentials are not encrypted, or improperly encrypted
 *
 * @author bryan.bergen
 *
 * @see CypherUtil
 * @see DataSource
 * @see TenantDataSourceResult
 */
public abstract class AbstractDataSourceTransformer
    implements Transformer<TenantDataSourceDetails, DataSource> {

  @Value("${jdbc.driverClassName}")
  private String driverClass;

  public static String buildKey(String tenantId, DatabaseType type) {
    return (tenantId + "_" + type.name()).toUpperCase();
  }

  private static String buildJdbcUrl(
      String host,
      String databaseName,
      String port) {

    StringBuilder builder = new StringBuilder();
    builder.append("jdbc:sqlserver://");
    builder.append(host);
    builder.append(":");
    builder.append(port);
    builder.append(";databaseName=");
    builder.append(databaseName);
    return builder.toString();
  }

  protected DataSource buildDataSource(String address, String databaseName, String port,
      String user, String password, String key) {

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(buildJdbcUrl(address, databaseName, port));
    try {
      config.setUsername(CypherUtil.decryptToString(user));
      config.setPassword(CypherUtil.decryptToString(password));
    } catch (Exception ex) {
      throw new RuntimeException("Could not decrypt database credentials", ex);
    }
    config.setDriverClassName(driverClass);
    config.setMaximumPoolSize(100);
    config.setConnectionTimeout(120 * 1000); // 2 min
    config.setIdleTimeout(120 * 1000); // 2 min
    config.setPoolName(key);
    config.setMinimumIdle(0);
    return new HikariDataSource(config);
  }
}

