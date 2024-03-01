
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import org.springframework.stereotype.Service;

@Service
public interface BlobStorageService {

  byte[] downloadBlob(BlobIdentifier blobIdentifier) throws StorageServiceException;

  void uploadBlob(BlobIdentifier blobIdentifier, byte[] blobData) throws StorageServiceException;
}
