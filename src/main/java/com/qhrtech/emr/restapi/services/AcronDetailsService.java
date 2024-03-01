
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.models.service.AcronConfiguration;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.models.service.KeyVaultDetails;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;

public interface AcronDetailsService {

  /**
   * Uses the QHR Discovery Service to the cloud storage account information for the acronym.
   *
   * @param acronym the Acronym.
   * @throws StorageServiceException when no storage account information exists.
   * @return an CloudStorageAccount
   */
  CloudStorageAccount getCloudStorageAccount(String acronym);

  /**
   * Uses the QHR Discovery Service to get environment and other miscellaneous information about an
   * Acronym.
   *
   * @param acronym the Acronym.
   * @return an AcronConfiguration.
   */
  AcronConfiguration getAcronymConfiguration(String acronym);

  KeyVaultDetails getAcronKeyVaultDetails(String acronym);

  KeyVaultDetails getCommonKeyVaultDetails(String acronym);


}
