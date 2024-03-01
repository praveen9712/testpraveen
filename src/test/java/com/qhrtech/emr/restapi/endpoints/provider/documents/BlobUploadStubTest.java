
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.services.BlobIdentifier;
import com.qhrtech.emr.restapi.services.BlobIdentifier.BlobIdentifierBuilder;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import com.qhrtech.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;

public class BlobUploadStubTest {

  private BlobStorageService blobStorageService;

  @Before
  public void before() {
    blobStorageService = mock(BlobStorageService.class);

  }

  @Test
  public void getUploader() {
    byte[] data = RandomUtils.getString(10).getBytes();
    CloudStorageAccount cloudStorageAccount = new CloudStorageAccount();
    cloudStorageAccount.setStorageAccountName(RandomUtils.getString(10));
    cloudStorageAccount.setContainerName(RandomUtils.getString(10));
    BlobUploadStub blobUploadStub =
        BlobUploadStub.getUploader(blobStorageService, cloudStorageAccount,
            data);

    assertNotNull(blobUploadStub);

    String name = RandomUtils.getString(10);
    blobUploadStub.accept(name);

    BlobIdentifier blobIdentifier = new BlobIdentifierBuilder()
        .withContainerName(cloudStorageAccount.getContainerName())
        .withStorageAccount(cloudStorageAccount.getStorageAccountName())
        .withBlobName(name)
        .build();

    verify(blobStorageService).uploadBlob(blobIdentifier, data);
  }
}
