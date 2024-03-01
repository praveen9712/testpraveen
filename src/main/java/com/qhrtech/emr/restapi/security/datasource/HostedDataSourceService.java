
package com.qhrtech.emr.restapi.security.datasource;

import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.accuro.utils.Transformer;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.security.exceptions.InvalidDataSourceException;
import com.qhrtech.emr.restapi.security.exceptions.InvalidTenantException;
import com.qhrtech.emr.restapi.util.RateLimitedCache;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementation of {@link DataSourceService} that will retrieve the datasource from the Registry
 *
 * @author bryan.bergen
 */
public class HostedDataSourceService implements DataSourceService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  @Autowired
  @Qualifier("medadminDataSourceTransformer")
  private Transformer<TenantDataSourceDetails, DataSource> medadminDataSourceTransformer;

  @Autowired
  @Qualifier("accdocsDataSourceTransformer")
  private Transformer<TenantDataSourceDetails, DataSource> accdocsDataSourceTransformer;

  @Autowired
  private CachingService<DataSource> dataSourceCache;

  @Autowired
  private RateLimitedCache<String, TenantDataSourceDetails> tenantDetailsCache;

  private static String buildKey(String tenantId, DatabaseType type) {
    return (tenantId + "_" + type.name()).toUpperCase();
  }

  @Override
  public DataSource getDataSource(String tenantId) {
    return getDataSource(tenantId, DatabaseType.Accuro);
  }

  @Override
  public DataSource getDataSource(String acronym, DatabaseType type) {
    String key = buildKey(acronym, type);
    DataSource ds = dataSourceCache.get(key);

    if (validateDataSource(ds)) {
      return ds;
    }

    boolean updatedDetails = tenantDetailsCache.update(key);

    if (!updatedDetails) {
      // Handle the scenario where the DS is null and we want to throw 401 instead of 500
      if (!tenantDetailsCache.get(key).isPresent()) {
        log.error("Rate limit error: Accuro clinic not found");
        throw new InvalidTenantException(
            "Configuration error: Accuro clinic not found");
      }
      throw new InvalidDataSourceException(
          "Cannot establish a connection to the database for acron '" + acronym + "'");
    } else {
      TenantDataSourceDetails details = tenantDetailsCache.get(key)
          .orElseThrow(() -> new InvalidTenantException(
              "Configuration error: Accuro clinic not found with name '" + acronym + "'"));

      // There has been a change in registry. Evict from cache and regenerate
      ds = generateDataSource(type, details);
      return verifyAndCache(ds, key);
    }
  }

  private boolean validateDataSource(DataSource ds) {
    if (ds != null) {
      try {
        Connection conn = ds.getConnection();
        conn.close();
      } catch (SQLException e) {
        log.debug(e.getMessage(), e);
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * Verifies the datasource by checking out a connection and closing it. Adds the data source to
   * the data source cache and details hash cache.
   * <p>
   * If the details cannot be verified then an exception will be thrown.
   */
  private DataSource verifyAndCache(DataSource ds, String key) {
    if (validateDataSource(ds)) {
      dataSourceCache.put(key, ds);
      return ds;
    } else {
      if (ds instanceof HikariDataSource) {
        ((HikariDataSource) ds).close();
      }
      throw new InvalidDataSourceException("Cannot establish a connection to the database.");

    }
  }

  private DataSource generateDataSource(DatabaseType type, TenantDataSourceDetails details) {
    switch (type) {
      case Accuro:
        return medadminDataSourceTransformer.transform(details);
      case Documents:
        return accdocsDataSourceTransformer.transform(details);
      default:
        throw new IllegalStateException("Unsupported Database Type: " + type);
    }
  }
}
