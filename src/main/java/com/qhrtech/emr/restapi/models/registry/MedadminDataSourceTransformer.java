
package com.qhrtech.emr.restapi.models.registry;

import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.restapi.security.datasource.HostedDataSourceService;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AbstractDataSourceTransformer} that will convert the
 * {@link TenantDataSourceDetails} into a valid Medadmin DataSource.
 *
 * @author bryan.bergen
 *
 * @see HostedDataSourceService
 */
@Service
public class MedadminDataSourceTransformer extends AbstractDataSourceTransformer {

  @Override
  public DataSource transform(TenantDataSourceDetails details) {
    return buildDataSource(details.getAddress(),
        details.getMedadminDatabaseName(),
        details.getDbServersPort(),
        details.getDbUser(),
        details.getDbPassword(),
        buildKey(details.getTenantId(), DatabaseType.Accuro));

  }

}
