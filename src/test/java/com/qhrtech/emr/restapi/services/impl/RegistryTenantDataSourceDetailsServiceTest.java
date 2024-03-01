
package com.qhrtech.emr.restapi.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.external.RegistryTokenManager;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceResult;
import com.qhrtech.emr.restapi.services.RegistryServiceApi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class RegistryTenantDataSourceDetailsServiceTest {


  @Mock
  private RegistryTokenManager registryTokenManager;

  @Mock
  private RegistryServiceApi registryService;

  @Spy
  @InjectMocks
  private RegistryTenantDataSourceDetailsService tenantDataSourceDetailsService;

  @Before
  public void setUp() throws Exception {

    tenantDataSourceDetailsService = spy(new RegistryTenantDataSourceDetailsService());
    openMocks(this);
  }

  @Test
  public void testWithAppendedDbType() throws Exception {

    String acronym = "DEVBC";
    TenantDataSourceDetails dataSource = mock(TenantDataSourceDetails.class);
    TenantDataSourceResult tenantDataSourceResult = new TenantDataSourceResult();
    tenantDataSourceResult.setDataSource(dataSource);
    tenantDataSourceResult.getDataSource().setTenantId(acronym);
    String auth = "authorization string";
    doReturn(auth).when(registryTokenManager).getAuthenticationHeader();
    doReturn(tenantDataSourceResult).when(registryService).getDataSourceByAcron(acronym, auth);
    String tenant = "DEVBC_ACCURO";
    TenantDataSourceDetails details =
        tenantDataSourceDetailsService.getDataSourceDetailsByAcronym(tenant);
    assertEquals(dataSource, details);


  }

  @Test
  public void testWithPlainAcronym() throws Exception {

    String acronym = "DEVBC";
    TenantDataSourceDetails dataSource = mock(TenantDataSourceDetails.class);
    TenantDataSourceResult tenantDataSourceResult = new TenantDataSourceResult();
    tenantDataSourceResult.setDataSource(dataSource);
    tenantDataSourceResult.getDataSource().setTenantId(acronym);
    String auth = "authorization string";
    doReturn(auth).when(registryTokenManager).getAuthenticationHeader();
    doReturn(tenantDataSourceResult).when(registryService).getDataSourceByAcron(acronym, auth);
    String tenant = "DEVBC";
    TenantDataSourceDetails details =
        tenantDataSourceDetailsService.getDataSourceDetailsByAcronym(tenant);
    assertEquals(dataSource, details);


  }

  @Test
  public void testWithNullTenant() throws Exception {

    String auth = "authorization string";
    doReturn(auth).when(registryTokenManager).getAuthenticationHeader();
    doReturn(null).when(registryService).getDataSourceByAcron(null, auth);
    TenantDataSourceDetails details =
        tenantDataSourceDetailsService.getDataSourceDetailsByAcronym(null);
    assertNull(details);


  }

}
