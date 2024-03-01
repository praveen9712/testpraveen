
package com.qhrtech.emr.restapi.security.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

/**
 * Caching layer for DataSource instances.
 *
 * @author james.michaud
 */
public class DataSourceCache implements CachingService<DataSource> {

  private final Map<String, DataSource> dataSourceMap;

  public DataSourceCache() {
    dataSourceMap = new HashMap<>();
  }

  @Override
  public DataSource get(String key) {
    return dataSourceMap.get(key);
  }

  @Override
  public void put(String key, DataSource value) {
    this.evict(key);
    dataSourceMap.put(key, value);
  }

  @Override
  public void evict(String key) {
    if (StringUtils.isNotBlank(key)) {
      DataSource ds = dataSourceMap.get(key);
      if (ds != null && ds instanceof HikariDataSource) {
        ((HikariDataSource) ds).close();
      }
      dataSourceMap.remove(key);
    }
  }
}
