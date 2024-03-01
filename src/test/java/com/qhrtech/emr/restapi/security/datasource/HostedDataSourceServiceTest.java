
package com.qhrtech.emr.restapi.security.datasource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.accuro.utils.Transformer;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceResult;
import com.qhrtech.emr.restapi.security.exceptions.InvalidDataSourceException;
import com.qhrtech.emr.restapi.security.exceptions.InvalidTenantException;
import com.qhrtech.emr.restapi.services.impl.RegistryTenantDataSourceDetailsService;
import com.qhrtech.emr.restapi.util.RateLimitedCache;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HostedDataSourceServiceTest {


  @Mock
  private Transformer<TenantDataSourceDetails, DataSource> medadminDataSourceTransformer;

  @Mock
  private Transformer<TenantDataSourceDetails, DataSource> accdocsDataSourceTransformer;

  @Mock
  private CachingService<DataSource> dataSourceCache;

  @Mock
  private RateLimitedCache<String, TenantDataSourceDetails> tenantDetailsCache;

  @Spy
  @InjectMocks
  private HostedDataSourceService hostedDataSourceService;

  @Before
  public void setUp() throws Exception {

    hostedDataSourceService = spy(new HostedDataSourceService());
    openMocks(this);
  }

  @Test(expected = InvalidTenantException.class)
  public void testWithInvalidTenanName() throws Exception {
    String tenantId = "DEVBC";
    hostedDataSourceService.getDataSource(tenantId, DatabaseType.Accuro);

  }

  @Test(expected = InvalidDataSourceException.class)
  public void testWithInvalidTenantValue() throws Exception {
    String tenantId = "DEVBC";
    TenantDataSourceDetails details = new TenantDataSourceDetails();
    doReturn(Optional.of(details)).when(tenantDetailsCache).get("DEVBC_ACCURO");
    hostedDataSourceService.getDataSource(tenantId, DatabaseType.Accuro);

  }

  @Test(expected = InvalidTenantException.class)
  public void testWithTenantFirstTime() throws Exception {
    String tenantId = "DEVBC";
    doReturn(true).when(tenantDetailsCache).update("DEVBC_ACCURO");
    hostedDataSourceService.getDataSource(tenantId, DatabaseType.Accuro);

  }

}
