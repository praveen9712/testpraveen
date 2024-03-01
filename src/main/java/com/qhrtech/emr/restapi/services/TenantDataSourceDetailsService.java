
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.services.impl.RegistryTenantDataSourceDetailsService;

/**
 * Service for retrieving {@link TenantDataSourceDetails}.
 *
 * @author bryan.bergen
 *
 * @see RegistryTenantDataSourceDetailsService
 */
public interface TenantDataSourceDetailsService {

  /**
   * Fetches DataSourceDetails based on the passed acronym.
   *
   * @param acronym Unique 5 character acronym of a tenant
   *
   * @return Tenant details which can be used to construct valid datasources.
   */
  TenantDataSourceDetails getDataSourceDetailsByAcronym(String acronym);
}
