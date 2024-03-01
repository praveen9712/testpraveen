
package com.qhrtech.emr.restapi.services;

import com.azure.identity.ClientSecretCredential;
import com.qhrtech.emr.restapi.models.service.DatabaseCredentialsDto;

public interface CredentialService {

  /**
   * Retrieves blob credentials from the provided keyvault.
   *
   * @param vaultName The name of the keyvault that contains the datasource credentials
   * @return A populated ClientSecretCredentialBuilder.
   */
  ClientSecretCredential getAccBlobCredentials(String vaultName);

  /**
   * Retrieves datasource credentials from the provided keyvault.
   *
   * @param vaultName The name of the keyvault that contains the datasource credentials
   * @return A populated ClientSecretCredentialBuilder.
   *
   */
  DatabaseCredentialsDto getDatabaseCredentials(String vaultName);
}
