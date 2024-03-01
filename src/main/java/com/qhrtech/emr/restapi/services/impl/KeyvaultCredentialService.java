
package com.qhrtech.emr.restapi.services.impl;

import com.azure.core.exception.ResourceNotFoundException;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.restapi.models.service.DatabaseCredentialsDto;
import com.qhrtech.emr.restapi.services.CredentialService;
import com.qhrtech.emr.restapi.services.exceptions.CredentialException;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyvaultCredentialService implements CredentialService {

  private final Supplier<SecretClientBuilder> clientFactory;
  private final ObjectMapper mapper;
  private final String keyvaultBaseUrl;
  private final String accBlobTenantIdKeyName;
  private final String accBlobClientIdKeyName;
  private final String accBlobClientSecretKeyName;
  private final String dataSourceKeyName;

  public KeyvaultCredentialService(Supplier<SecretClientBuilder> clientBuilderFactory,
      ObjectMapper objectMapper,
      @Value("${keyvault.base-url:https://%s.vault.azure.net}") String keyvaultBaseUrl,
      @Value("${keyvault.accapi.tenant-id-key-name"
          + ":ACCUROAPI-ACC-SP-TENANT-ID}") String accBlobTenantIdKeyName,
      @Value("${keyvault.accapi.client-id-key-name"
          + ":ACCUROAPI-ACC-SP-CLIENT-ID}") String accBlobClientIdKeyName,
      @Value("${keyvault.accapi.client-secret-key-name"
          + ":ACCUROAPI-ACC-SP-CLIENT-SECRET}") String accBlobClientSecretKeyName,
      @Value("${keyvault.common.datasource-key-name:medadmin-json}") String dataSourceKeyName) {
    this.accBlobTenantIdKeyName = accBlobTenantIdKeyName;
    this.accBlobClientIdKeyName = accBlobClientIdKeyName;
    this.accBlobClientSecretKeyName = accBlobClientSecretKeyName;
    this.dataSourceKeyName = dataSourceKeyName;
    this.clientFactory = clientBuilderFactory;
    this.mapper = objectMapper;
    this.keyvaultBaseUrl = keyvaultBaseUrl;
  }

  private String getKeyvaultUrl(String keyvault) {
    return String.format(keyvaultBaseUrl, keyvault);
  }

  private SecretClient getSecretClient(String keyvaultId) {
    String keyvaultUrl = getKeyvaultUrl(keyvaultId);
    return clientFactory.get()
        .vaultUrl(keyvaultUrl)
        .buildClient();
  }

  @Override
  public ClientSecretCredential getAccBlobCredentials(String vaultName) {
    try {
      SecretClient accBlobClient = getSecretClient(vaultName);

      String tenantId = accBlobClient.getSecret(accBlobTenantIdKeyName).getValue();
      String clientId = accBlobClient.getSecret(accBlobClientIdKeyName).getValue();
      String clientSecret = accBlobClient.getSecret(accBlobClientSecretKeyName).getValue();
      return new ClientSecretCredentialBuilder()
          .tenantId(tenantId)
          .clientId(clientId)
          .clientSecret(clientSecret)
          .build();
    } catch (ResourceNotFoundException e) {
      throw new CredentialException("Credentials not found.", e);
    } catch (RuntimeException e) {
      throw new CredentialException("Failed to get Blob Storage credentials.", e);
    }
  }

  @Override
  public DatabaseCredentialsDto getDatabaseCredentials(String vaultName) {
    String keyvaultUrl = getKeyvaultUrl(vaultName);
    try {
      SecretClient acronClient = getSecretClient(keyvaultUrl);

      KeyVaultSecret secret = acronClient.getSecret(dataSourceKeyName);

      return mapper.readValue(secret.getValue(), DatabaseCredentialsDto.class);
    } catch (JsonProcessingException e) {
      throw new CredentialException("Invalid datasource credentials.", e);
    } catch (ResourceNotFoundException e) {
      throw new CredentialException("Credentials not found.", e);
    }
  }
}
