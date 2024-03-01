
package com.qhrtech.emr.restapi.config;

import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.security.db.TenantType;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.impl.HostedDiscoveryDetailsService;
import com.qhrtech.emr.restapi.services.impl.SingleDiscoveryDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DiscoveryConfig {

  @Value("${tenants.type:SINGLE}")
  private String tenantType;

  @Value("${discovery.url:}")
  private String discoveryHost;

  // Single tenant parameters
  @Value("${single.discovery.storage_account:}")
  private String storageAccount;

  @Value("${single.discovery.container_name:}")
  private String containerName;

  @Bean(name = "discoveryServiceApi")
  public RestTemplate getDiscoveryServiceApi() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }

  private CloudStorageAccount singleTenantCloudStorageAccount() {
    CloudStorageAccount cloudStorageAccount = new CloudStorageAccount();
    cloudStorageAccount.setStorageAccountName(storageAccount);
    cloudStorageAccount.setContainerName(containerName);
    return cloudStorageAccount;
  }


  @Bean
  public AcronDetailsService discoveryDetailsService() {
    switch (getAccapiMode()) {
      case SINGLE:
        return new SingleDiscoveryDetailsService(singleTenantCloudStorageAccount());
      case HOSTED:
        return new HostedDiscoveryDetailsService(discoveryHost, getDiscoveryServiceApi());
      default:
        throw new UnsupportedOperationException("Unsupported Tenant Type.");
    }
  }

  private TenantType getAccapiMode() {
    return TenantType.lookup(tenantType);
  }
}
