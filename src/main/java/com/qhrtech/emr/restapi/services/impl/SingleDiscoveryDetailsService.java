
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.models.service.AcronConfiguration;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.models.service.KeyVaultDetails;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;

public class SingleDiscoveryDetailsService implements AcronDetailsService {

  private final CloudStorageAccount cloudStorageAccount;


  public SingleDiscoveryDetailsService(CloudStorageAccount cloudStorageAccount) {
    this.cloudStorageAccount = cloudStorageAccount;
  }

  @Override
  public CloudStorageAccount getCloudStorageAccount(String acronym) {
    if ("".equals(cloudStorageAccount.getStorageAccountName())
        || "".equals(cloudStorageAccount.getContainerName())) {
      throw new StorageServiceException(
          "Cloud storage configuration is not configured for SINGLE tenant mode.");
    }

    return cloudStorageAccount;
  }

  @Override
  public AcronConfiguration getAcronymConfiguration(String acronym) {
    throw new StorageServiceException("There is no Acron Configuration in SINGLE tenant mode.");
  }

  @Override
  public KeyVaultDetails getAcronKeyVaultDetails(String acronym) {
    throw new StorageServiceException("There is no Acron Configuration in SINGLE tenant mode.");
  }

  @Override
  public KeyVaultDetails getCommonKeyVaultDetails(String acronym) {
    throw new StorageServiceException("There is no Acron Configuration in SINGLE tenant mode.");
  }
}
