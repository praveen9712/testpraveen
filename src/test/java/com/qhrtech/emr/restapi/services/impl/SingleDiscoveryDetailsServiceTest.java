
package com.qhrtech.emr.restapi.services.impl;

import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import com.qhrtech.util.RandomUtils;
import org.junit.Test;

public class SingleDiscoveryDetailsServiceTest {

  private SingleDiscoveryDetailsService singleDiscoveryDetailsService;
  private CloudStorageAccount expectedCloudStorageAccount;

  public SingleDiscoveryDetailsServiceTest() {

    CloudStorageAccount expectedCloudStorageAccount = new CloudStorageAccount();
    expectedCloudStorageAccount.setStorageAccountName(RandomUtils.getString(20));
    expectedCloudStorageAccount.setContainerName(RandomUtils.getString(20));
    this.expectedCloudStorageAccount = expectedCloudStorageAccount;
  }

  @Test(expected = StorageServiceException.class)
  public void getCloudStorageAccountEmptyContainerName() {
    expectedCloudStorageAccount.setContainerName("");
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);

    singleDiscoveryDetailsService.getCloudStorageAccount(RandomUtils.getString(5));
  }

  @Test(expected = StorageServiceException.class)
  public void getCloudStorageAccountEmptyStorageAccount() {
    expectedCloudStorageAccount.setStorageAccountName("");
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);

    singleDiscoveryDetailsService.getCloudStorageAccount(RandomUtils.getString(5));
  }

  @Test
  public void getCloudStorageAccount() {
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);
    CloudStorageAccount cloudStorageAccount =
        singleDiscoveryDetailsService.getCloudStorageAccount(RandomUtils.getString(5));
    assertEquals(expectedCloudStorageAccount, cloudStorageAccount);
  }

  @Test(expected = StorageServiceException.class)
  public void getAcronymConfiguration() {
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);
    singleDiscoveryDetailsService.getAcronymConfiguration(RandomUtils.getString(5));
  }

  @Test(expected = StorageServiceException.class)
  public void getAcronKeyVault() {
    expectedCloudStorageAccount.setContainerName("");
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);

    singleDiscoveryDetailsService.getAcronKeyVaultDetails(RandomUtils.getString(5));
  }

  @Test(expected = StorageServiceException.class)
  public void getCommonKeyVault() {
    expectedCloudStorageAccount.setStorageAccountName("");
    singleDiscoveryDetailsService = new SingleDiscoveryDetailsService(expectedCloudStorageAccount);

    singleDiscoveryDetailsService.getCommonKeyVaultDetails(RandomUtils.getString(5));
  }

}
