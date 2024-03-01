
package com.qhrtech.emr.restapi.models.registry;

import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.restapi.security.datasource.HostedDataSourceService;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AbstractDataSourceTransformer} that will convert the
 * {@link TenantDataSourceDetails} into a valid AccDocs DataSource.
 *
 * If the AccDocs database name is not set on the {@link TenantDataSourceDetails} input to this
 * transformer, a DataSource configured for the MedAdmin database will be returned instead. That is
 * to say, this implementation will then assume that the Medadmin database <i>is</i> the AccDocs
 * database.
 *
 * @author bryan.bergen
 *
 * @see HostedDataSourceService
 */
@Service
public class AccdocsDataSourceTransformer extends AbstractDataSourceTransformer {

  @Override
  public DataSource transform(TenantDataSourceDetails details) {
    String databaseName;
    if (StringUtils.isNotBlank(details.getAccdocsDatabaseName())) {
      databaseName = details.getAccdocsDatabaseName();
    } else {
      databaseName = details.getMedadminDatabaseName();
    }

    return buildDataSource(details.getAddress(),
        databaseName,
        details.getDbServersPort(),
        details.getDbUser(),
        details.getDbPassword(),
        buildKey(details.getTenantId(), DatabaseType.Documents));
  }

}
