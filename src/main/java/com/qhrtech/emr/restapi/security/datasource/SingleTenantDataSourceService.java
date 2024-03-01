/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.datasource;

import com.qhrtech.emr.accuro.db.DatabaseType;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementation of {@link DataSourceService} that simply returns the datasource configured in the
 * jdbc.properties file
 *
 * @author bryan.bergen
 */
public class SingleTenantDataSourceService implements DataSourceService {

  @Autowired
  @Qualifier("medadmin")
  private DataSource medAdminDataSource;

  @Autowired
  @Qualifier("accdocs")
  private DataSource accDocsDataSource;

  @Override
  public DataSource getDataSource(String tenantId) {
    return getDataSource(tenantId, DatabaseType.Accuro);
  }

  @Override
  public DataSource getDataSource(String tenantId, DatabaseType type) {

    switch (type) {
      case Accuro:
        return medAdminDataSource;
      case Documents:
        return accDocsDataSource;
      default:
        throw new IllegalArgumentException(
            "Unsupported Database Connection Type: " + type.toString());

    }
  }

}
