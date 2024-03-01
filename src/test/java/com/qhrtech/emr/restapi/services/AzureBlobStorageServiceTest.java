
package com.qhrtech.emr.restapi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import com.azure.core.http.HttpResponse;
import com.azure.core.util.BinaryData;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.qhrtech.emr.restapi.services.BlobIdentifier.BlobIdentifierBuilder;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import com.qhrtech.emr.restapi.services.impl.AzureBlobStorageService;
import java.lang.reflect.Field;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class AzureBlobStorageServiceTest {

  private AzureBlobStorageService azureBlobStorageService;
  private BlobIdentifier blobIdentifier;

  private BlobClientBuilder clientBuilderMock;

  private BlobClient blobClient;
  private CredentialService credentialServiceMock;
  private ClientSecretCredential credentialMock;

  public AzureBlobStorageServiceTest() {

    credentialServiceMock = mock(CredentialService.class);
    azureBlobStorageService = spy(new AzureBlobStorageService(credentialServiceMock));

    blobIdentifier = new BlobIdentifierBuilder().withStorageAccount(randomString())
        .withContainerName(randomString())
        .withBlobName(randomString())
        .build();
  }

  @Before
  public void setup() throws Exception {
    Field endpoint = AzureBlobStorageService.class.getDeclaredField("blobEndpoint");
    endpoint.setAccessible(true);
    endpoint.set(azureBlobStorageService, "https://%s.blob.core.windows.net");

    Field fieldVaultName = AzureBlobStorageService.class.getDeclaredField("vaultName");
    String vaultName = randomString();
    fieldVaultName.setAccessible(true);
    fieldVaultName.set(azureBlobStorageService, vaultName);

    credentialMock = mock(ClientSecretCredential.class);

    when(credentialServiceMock.getAccBlobCredentials(vaultName)).thenReturn(credentialMock);

    blobClient = mock(BlobClient.class);
    clientBuilderMock = mock(BlobClientBuilder.class);

    when(azureBlobStorageService.getBlobClientBuilder()).thenReturn(clientBuilderMock);

    doReturn(clientBuilderMock).when(clientBuilderMock).endpoint(
        "https://" + blobIdentifier.getStorageAccount() + ".blob.core.windows.net");
    doReturn(clientBuilderMock).when(clientBuilderMock).credential(credentialMock);
    doReturn(clientBuilderMock).when(clientBuilderMock)
        .containerName(blobIdentifier.getContainerName());
    doReturn(clientBuilderMock).when(clientBuilderMock).blobName(blobIdentifier.getBlobName());
    doReturn(blobClient).when(clientBuilderMock).buildClient();
  }

  @Test
  public void testDownloadBlob() throws Exception {
    byte[] expected = randomString().getBytes();
    doReturn(BinaryData.fromBytes(expected)).when(blobClient).downloadContent();

    byte[] data = azureBlobStorageService.downloadBlob(blobIdentifier);
    assertNotNull(data);
    assertEquals(expected, data);
  }

  @Test(expected = StorageServiceException.class)
  public void testDownloadBlobNull() throws Exception {
    doReturn(null).when(blobClient).downloadContent();
    azureBlobStorageService.downloadBlob(blobIdentifier);
  }

  @Test(expected = StorageServiceException.class)
  public void testDownloadBlobError() throws Exception {
    doThrow(new BlobStorageException(randomString(), mock(HttpResponse.class),
        randomString())).when(blobClient).downloadContent();
    azureBlobStorageService.downloadBlob(blobIdentifier);
  }

  @Test
  public void testUpload() throws StorageServiceException {
    azureBlobStorageService.uploadBlob(blobIdentifier, randomString().getBytes());
  }

  @Test(expected = StorageServiceException.class)
  public void testUploadError() throws StorageServiceException {
    doThrow(new BlobStorageException(randomString(),
        mock(HttpResponse.class),
        randomString())).when(blobClient).upload(ArgumentMatchers.any(BinaryData.class),
            ArgumentMatchers.any(Boolean.class));
    azureBlobStorageService.uploadBlob(blobIdentifier, randomString().getBytes());
  }

  private String randomString() {
    return RandomStringUtils.randomAlphabetic(30);
  }
}
