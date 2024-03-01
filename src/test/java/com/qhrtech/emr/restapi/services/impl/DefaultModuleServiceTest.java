
package com.qhrtech.emr.restapi.services.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.security.ModuleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.security.modules.Module;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import com.qhrtech.util.RandomUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class DefaultModuleServiceTest {

  private AccuroApiService api;
  private SecurityContextService securityContextService;
  private ModuleManager mockModuleManager;
  private ModuleService moduleService;

  @Before
  public void setup() {
    api = mock(AccuroApiService.class);
    securityContextService = mock(SecurityContextService.class);
    mockModuleManager = mock(ModuleManager.class);
    moduleService = new DefaultModuleService(api, securityContextService);
  }

  @Test
  public void isRestApiEnabled() throws ProtossException {
    String tenant = RandomUtils.getString(5);
    ApiSecurityContext apiSecurityContext = new ApiSecurityContext();
    apiSecurityContext.setTenantId(tenant);
    when(securityContextService.getSecurityContext()).thenReturn(apiSecurityContext);
    Map<Module, Boolean> expected = new HashMap();
    expected.put(RandomUtils.getEnumValue(Module.class), true);
    expected.put(Module.REST_API_ACCESS, true);
    when(mockModuleManager.getModules()).thenReturn(expected);
    when(api.getImpl(ModuleManager.class, tenant)).thenReturn(mockModuleManager);
    boolean accBlobEnabled = moduleService.isRestApiEnabled(tenant);
    assertTrue(accBlobEnabled);
  }

  @Test
  public void isAccblobEnabled()
      throws ProtossException, NoSuchFieldException, IllegalAccessException {

    Field enableAccblob = DefaultModuleService.class.getDeclaredField("enableAccblob");
    enableAccblob.setAccessible(true);
    enableAccblob.set(moduleService, true);

    String tenant = RandomUtils.getString(5);
    ApiSecurityContext apiSecurityContext = new ApiSecurityContext();
    apiSecurityContext.setTenantId(tenant);
    when(securityContextService.getSecurityContext()).thenReturn(apiSecurityContext);
    Map<Module, Boolean> expected = new HashMap();
    expected.put(RandomUtils.getEnumValue(Module.class), true);
    expected.put(Module.ACCBLOB, true);
    when(mockModuleManager.getModules()).thenReturn(expected);
    when(api.getImpl(ModuleManager.class, tenant)).thenReturn(mockModuleManager);
    boolean accBlobEnabled = moduleService.isAccBlobEnabled();
    assertTrue(accBlobEnabled);
  }

  @Test
  public void isAccblobEnabledWithEnableAccblobFalse()
      throws ProtossException {
    boolean accBlobEnabled = moduleService.isAccBlobEnabled();
    assertFalse(accBlobEnabled);
  }

}
