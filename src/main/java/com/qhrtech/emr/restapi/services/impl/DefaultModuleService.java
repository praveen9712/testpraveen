/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.accuro.api.security.ModuleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.security.modules.Module;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author jesse.pasos
 */
@Service
public class DefaultModuleService implements ModuleService {

  /**
   * This is disabled intentionally and will be enabled once AccBlob feature is ready to use.
   */
  @Value("${accblob.enabled:false}")
  private boolean enableAccblob;

  private final AccuroApiService api;
  private final SecurityContextService securityContextService;

  public DefaultModuleService(
      AccuroApiService api, SecurityContextService securityContextService) {
    this.api = api;
    this.securityContextService = securityContextService;
  }

  @Override
  public boolean isRestApiEnabled(String tenantId) throws ProtossException {
    return isModuleEnabled(Module.REST_API_ACCESS, tenantId);
  }

  @Override
  public boolean isAccBlobEnabled() throws ProtossException {
    if (enableAccblob) {
      return isModuleEnabled(Module.ACCBLOB);
    } else {
      return false;
    }
  }

  private boolean isModuleEnabled(Module module) throws ProtossException {
    if (null == securityContextService.getSecurityContext()) {
      return false;

    }
    String tenantId = securityContextService.getSecurityContext().getTenantId();
    return isModuleEnabled(module, tenantId);
  }

  private boolean isModuleEnabled(Module module, String tenantId) throws ProtossException {
    ModuleManager manager = api.getImpl(ModuleManager.class, tenantId);
    Map<Module, Boolean> modules = manager.getModules();
    return modules.containsKey(module)
        && modules.get(module);
  }
}
