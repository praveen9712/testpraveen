
package com.qhrtech.emr.restapi.services;


import com.qhrtech.emr.restapi.security.datasource.DataSourceService;

/**
 * Represents a function that accepts String and boolean as an arguments. This is a functional
 * interface, the implementation of which is responsible for creating the instance of the Protoss
 * managers using the datasource(s) and other parameters. The createInstance method accepts the
 * tenant and boolean. The tenant is required by the implementing class to get the datasource and
 * skipPermissions boolean which indicate if the provider permissions needs to be skipped or not.
 */
@FunctionalInterface
public interface ManagerConstructor<T> {

  T createInstance(DataSourceService dataSourceService,
      SecurityContextService securityContextService, ListenerPoolService listenerPoolService,
      String tenant, boolean skipPermissions);
}
