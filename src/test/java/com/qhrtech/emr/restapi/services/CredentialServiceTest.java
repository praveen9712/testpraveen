
package com.qhrtech.emr.restapi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.azure.identity.ClientSecretCredential;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.restapi.models.service.DatabaseCredentialsDto;
import com.qhrtech.emr.restapi.services.exceptions.CredentialException;
import com.qhrtech.emr.restapi.services.impl.KeyvaultCredentialService;
import com.qhrtech.util.RandomUtils;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;

public class CredentialServiceTest {

  private final String tenantIdKey = "accblob-tenant-id";
  private final String clientIdKey = "accblob-client-id";
  private final String clientSecretKey = "accblob-client-secret";

  private final String databaseKvId = "medadmin-json";
  private final String dbVaultName = "ACRON-kv";
  private ObjectMapper mapper;

  public CredentialServiceTest() {
  }

  @Before
  public void setup() throws Exception {
    mapper = new ObjectMapper();
  }

  private Supplier<SecretClientBuilder> clientBuilderFactory(SecretClientBuilder mockBuilder) {
    return () -> mockBuilder;
  }

  private CredentialService buildCredentialService(SecretClientBuilder mockClientBuilder) {
    String keyvaultUrl = "https://%s.keyvault:5001";
    return new KeyvaultCredentialService(clientBuilderFactory(mockClientBuilder), mapper,
        keyvaultUrl, tenantIdKey,
        clientIdKey, clientSecretKey, databaseKvId);
  }

  @Test
  public void testGetAccBlobCredentialNormalScenario() {
    String actualTenantId = "tenantIdVal";
    String actualClientId = "tenantIdVal";
    String actualClientSecret = "clientSecret";

    SecretClient client = mock(SecretClient.class);
    KeyVaultSecret tenantMock = mock(KeyVaultSecret.class);
    when(tenantMock.getValue()).thenReturn(actualTenantId);
    KeyVaultSecret clientIdMock = mock(KeyVaultSecret.class);
    when(clientIdMock.getValue()).thenReturn(actualClientId);
    KeyVaultSecret secretMock = mock(KeyVaultSecret.class);
    when(secretMock.getValue()).thenReturn(actualClientSecret);

    when(client.getSecret(tenantIdKey)).thenReturn(tenantMock);
    when(client.getSecret(clientIdKey)).thenReturn(clientIdMock);
    when(client.getSecret(clientSecretKey)).thenReturn(secretMock);

    SecretClientBuilder clientBuilder = mock(SecretClientBuilder.class);
    when(clientBuilder.vaultUrl(any())).thenReturn(clientBuilder);
    when(clientBuilder.buildClient()).thenReturn(client);

    CredentialService credentialService =
        buildCredentialService(clientBuilder);

    ClientSecretCredential accBlobCredentials =
        credentialService.getAccBlobCredentials(dbVaultName);

    assertNotNull(accBlobCredentials);
    verify(clientIdMock, times(1)).getValue();
    verify(secretMock, times(1)).getValue();
    verify(secretMock, times(1)).getValue();

  }

  @Test
  public void testGetAccBlobCredentialInvalidKeyvault() {

    SecretClientBuilder clientBuilder = mock(SecretClientBuilder.class);
    when(clientBuilder.vaultUrl(any())).thenReturn(clientBuilder);
    when(clientBuilder.buildClient()).thenThrow(new RuntimeException(""));

    CredentialService credentialService =
        buildCredentialService(clientBuilder);

    try {
      credentialService.getAccBlobCredentials(dbVaultName);
    } catch (Exception ex) {
      assertNotNull(ex);
      assertTrue(ex instanceof CredentialException);
    }
  }

  @Test
  public void testGetDatasourceCredential() throws JsonProcessingException {
    DatabaseCredentialsDto databaseCredentialsDto = new DatabaseCredentialsDto();
    databaseCredentialsDto.setMedadminDatabaseName(RandomUtils.getString(10));
    databaseCredentialsDto.setDbPassword(RandomUtils.getString(10));
    databaseCredentialsDto.setDbUser(RandomUtils.getString(10));
    databaseCredentialsDto.setAccdocsDatabaseName(RandomUtils.getString(10));
    databaseCredentialsDto.setDbServersPort(RandomUtils.getString(4));
    String jsonCredential = mapper.writeValueAsString(databaseCredentialsDto);

    SecretClient client = mock(SecretClient.class);
    KeyVaultSecret databaseCredentialSecret = mock(KeyVaultSecret.class);
    when(databaseCredentialSecret.getValue()).thenReturn(jsonCredential);

    when(client.getSecret(databaseKvId)).thenReturn(databaseCredentialSecret);

    SecretClientBuilder clientBuilder = mock(SecretClientBuilder.class);
    when(clientBuilder.vaultUrl(any())).thenReturn(clientBuilder);
    when(clientBuilder.buildClient()).thenReturn(client);

    CredentialService credentialService =
        buildCredentialService(clientBuilder);

    DatabaseCredentialsDto returnedCredentials =
        credentialService.getDatabaseCredentials(dbVaultName);

    assertNotNull(returnedCredentials);
    assertEquals(databaseCredentialsDto, returnedCredentials);

  }

  @Test
  public void testGetDatasourceCredentialInvalid() {

    SecretClientBuilder clientBuilder = mock(SecretClientBuilder.class);
    when(clientBuilder.vaultUrl(any())).thenReturn(clientBuilder);
    when(clientBuilder.buildClient()).thenThrow(new RuntimeException(""));
    try {
      CredentialService credentialService =
          buildCredentialService(clientBuilder);
      credentialService.getAccBlobCredentials(dbVaultName);
    } catch (Exception ex) {
      assertNotNull(ex);
      assertTrue(ex instanceof CredentialException);
    }
  }

}

