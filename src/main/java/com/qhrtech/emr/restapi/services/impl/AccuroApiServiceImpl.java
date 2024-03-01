
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.ListenerPoolService;
import com.qhrtech.emr.restapi.services.ManagerConstructor;
import com.qhrtech.emr.restapi.services.ManagerMapping;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccuroApiServiceImpl implements AccuroApiService {

  @Autowired
  private ListenerPoolService listenerPoolService;

  @Autowired
  private DataSourceService dataSourceService;

  @Autowired
  private SecurityContextService securityContextService;

  @Override
  public <D extends T, T> D getImpl(Class<T> interfaceClass, String tenantId) {
    return getImpl(interfaceClass, tenantId, false);
  }

  @Override
  public <D extends T, T> D getImpl(Class<T> interfaceClass, String tenantId,
      boolean skipPermissionsCheck) {
    if (null == interfaceClass) {
      throw new NullPointerException("Interface class must be specified.");
    }

    ManagerConstructor<D> managerConstructor = ManagerMapping.getConstructor(interfaceClass);
    if (managerConstructor == null) {
      throw new ImplementationNotFoundException(
          "Implementation not defined for " + interfaceClass.getName() + ".");
    }

    return managerConstructor.createInstance(dataSourceService,
        securityContextService, listenerPoolService,
        tenantId, skipPermissionsCheck);
  }
}
