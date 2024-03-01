/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.datasource;

import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.services.TenantDataSourceDetailsService;
import com.qhrtech.emr.restapi.util.RateLimitedCache;
import java.time.Duration;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author james.michaud
 */
@Configuration
public class CachingServiceConfig {
  private final Long dataSourceDetailsRateLimit;
  private final TenantDataSourceDetailsService tenantDetailsService;

  public CachingServiceConfig(
      @Value("${datasource.details.rate-limit:60000}") Long dataSourceDetailsRateLimit,
      TenantDataSourceDetailsService tenantDetailsService) {
    this.dataSourceDetailsRateLimit = dataSourceDetailsRateLimit;
    this.tenantDetailsService = tenantDetailsService;
  }

  @Bean(name = "dataSourceCache")
  public CachingService<DataSource> dataSourceCache() {
    return new DataSourceCache();
  }

  @Bean
  public RateLimitedCache<String, TenantDataSourceDetails> tenantDetailsCache() {
    return new RateLimitedCache<>(tenantDetailsService::getDataSourceDetailsByAcronym,
        Duration.ofMillis(dataSourceDetailsRateLimit));
  }

}

