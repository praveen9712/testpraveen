
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.services.BlobIdentifier;
import com.qhrtech.emr.restapi.services.BlobIdentifier.BlobIdentifierBuilder;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A consumer which can be used to upload data to a cloud storage provider.
 */
@FunctionalInterface
public interface BlobUploadStub extends Consumer<String> {

  default Function<byte[], BlobUploadStub> something(BlobStorageService blobStorageService,
      CloudStorageAccount cloudStorageAccount) {

    return (bytes) -> getUploader(blobStorageService, cloudStorageAccount, bytes);

  }

  static BlobUploadStub getUploader(BlobStorageService blobStorageService,
      CloudStorageAccount cloudStorageAccount, byte[] blobData) {

    BlobIdentifierBuilder blobIdentifierBuilder = new BlobIdentifierBuilder()
        .withContainerName(cloudStorageAccount.getContainerName())
        .withStorageAccount(cloudStorageAccount.getStorageAccountName());

    return (blobName) -> {
      BlobIdentifier identifier = blobIdentifierBuilder
          .withBlobName(blobName)
          .build();
      blobStorageService.uploadBlob(identifier, blobData);
    };
  }
}
