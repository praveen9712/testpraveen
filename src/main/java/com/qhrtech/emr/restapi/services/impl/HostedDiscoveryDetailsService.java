
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.models.service.AcronConfiguration;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.models.service.KeyVaultDetails;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.exceptions.RestServiceException;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HostedDiscoveryDetailsService implements AcronDetailsService {

  private static final Logger log = LoggerFactory.getLogger(HostedDiscoveryDetailsService.class);

  private RestTemplate discoveryServiceApi;
  private final String discoveryHost;

  public HostedDiscoveryDetailsService(String discoveryHost, RestTemplate discoveryServiceApi) {
    if ("".equals(discoveryHost)) {
      log.warn("discovery.url is not set in the configuration properties file.");
    }
    this.discoveryServiceApi = discoveryServiceApi;
    this.discoveryHost =
        discoveryHost.endsWith("/") ? discoveryHost.substring(0, discoveryHost.length() - 1)
            : discoveryHost;
  }

  @Override
  public CloudStorageAccount getCloudStorageAccount(String acronym) {
    AcronConfiguration acronymConfiguration = getAcronymConfiguration(acronym);
    if (null == acronymConfiguration.getCloudStorageAccount()) {
      throw new StorageServiceException(
          "There is no storage account which exists for the given acronym.");
    }
    return acronymConfiguration.getCloudStorageAccount();
  }

  @Override
  public AcronConfiguration getAcronymConfiguration(String acronym) {

    if (!StringUtils.hasText(acronym)) {
      throw new IllegalStateException("acronym is required.");
    }
    if (!StringUtils.hasText(discoveryHost)) {
      throw new IllegalStateException("discovery url must be configured.");
    }

    URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acronym);
    try {

      AcronConfiguration acronConfiguration =
          discoveryServiceApi.getForObject(uri, AcronConfiguration.class);

      if (null == acronConfiguration) {
        throw new RestServiceException("The discovery service did not return a response.");
      }

      if (!acronym.equals(acronConfiguration.getName())) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            "The discovery service response does not match the acronym provided. ");
        stringBuilder.append(
            String.format("name: %s, expected: %s, remote id: %s",
                acronConfiguration.getName(),
                acronym, acronConfiguration.getId()));
        log.error(stringBuilder.toString());
      }

      return acronConfiguration;
    } catch (RestClientException ex) {
      throw new RestServiceException("An error has occurred connecting to the discovery service",
          ex);
    }
  }

  @Override
  public KeyVaultDetails getAcronKeyVaultDetails(String acronym) {
    AcronConfiguration acronymConfiguration = getAcronymConfiguration(acronym);
    if (null == acronymConfiguration.getAcronKeyVault()) {
      throw new StorageServiceException(
          "There is no acron key vault which exists for the given acronym.");
    }
    return acronymConfiguration.getAcronKeyVault();
  }

  @Override
  public KeyVaultDetails getCommonKeyVaultDetails(String acronym) {
    AcronConfiguration acronymConfiguration = getAcronymConfiguration(acronym);
    if (null == acronymConfiguration.getCommonKeyVault()) {
      throw new StorageServiceException(
          "There is no common key vault which exists for the given acronym.");
    }
    return acronymConfiguration.getCommonKeyVault();
  }
}
