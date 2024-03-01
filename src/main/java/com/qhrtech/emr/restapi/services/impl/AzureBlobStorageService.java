
package com.qhrtech.emr.restapi.services.impl;

import com.azure.core.util.BinaryData;
import com.azure.identity.ClientSecretCredential;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.microsoft.aad.msal4j.MsalServiceException;
import com.qhrtech.emr.restapi.services.BlobIdentifier;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import com.qhrtech.emr.restapi.services.CredentialService;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureBlobStorageService implements BlobStorageService {

  @Value("${azure.blob.endpoint:}")
  private String blobEndpoint;

  @Value("${azure.blob.vault-name:}")
  private String vaultName;

  private CredentialService credentialService;

  public AzureBlobStorageService(@Autowired CredentialService credentialService) {
    this.credentialService = credentialService;
  }

  public BlobClientBuilder getBlobClientBuilder() {
    return new BlobClientBuilder();
  }

  @Override
  public byte[] downloadBlob(BlobIdentifier blobIdentifier) throws StorageServiceException {
    try {
      BlobClient blobClient = createBlobClient(blobIdentifier);
      BinaryData data = blobClient.downloadContent();

      if (data != null) {
        return data.toBytes();
      } else {
        throw new StorageServiceException("No data found.");
      }
    } catch (MsalServiceException | BlobStorageException exp) {
      throw new StorageServiceException("Error downloading blob content", exp);
    }
  }

  @Override
  public void uploadBlob(BlobIdentifier blobIdentifier, byte[] blobData)
      throws StorageServiceException {
    try {
      BlobClient blobClient = createBlobClient(blobIdentifier);
      blobClient.upload(BinaryData.fromBytes(blobData), true);
    } catch (MsalServiceException | BlobStorageException exp) {
      throw new StorageServiceException("Error uploading blob content", exp);
    }
  }

  private BlobClient createBlobClient(BlobIdentifier blobIdentifier) {

    ClientSecretCredential credential = credentialService.getAccBlobCredentials(vaultName);

    // Storage account can contain only letters and numbers
    String storageAccount = blobIdentifier.getStorageAccount();

    String url = String.format(blobEndpoint, storageAccount);
    return getBlobClientBuilder()
        .endpoint(url)
        .credential(credential)
        .containerName(blobIdentifier.getContainerName())
        .blobName(blobIdentifier.getBlobName())
        .buildClient();
  }
}
