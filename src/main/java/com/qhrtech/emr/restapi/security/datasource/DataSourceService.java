
package com.qhrtech.emr.restapi.security.datasource;

import com.qhrtech.emr.accuro.db.DatabaseType;
import javax.sql.DataSource;

/**
 * Service for dynamically retrieving Datasources
 *
 * @author bryan.bergen
 *
 * @see SingleTenantDataSourceService
 * @see HostedDataSourceService
 * @see DataSourceCache
 */
public interface DataSourceService {

  /**
   * Retrieve the datasource of for the tenant id
   *
   * @param tenantId - Unique 5 character acronym for tenant
   * @param type - Type of database connection to create
   *
   * @return - a live {@link DataSource} connecting to the tenant's MedAdmin database.
   *
   * @see DatabaseType
   */
  DataSource getDataSource(String tenantId, DatabaseType type);

  /**
   * Retrieve the datasource for the tenant id This call will automatically configure the
   * {@link DatabaseType}
   *
   * @param tenantId - Unique 5 character acronym for tenant
   *
   * @return - a live {@link DataSource} connecting to the tenant's MedAdmin database
   */
  DataSource getDataSource(String tenantId);

}
