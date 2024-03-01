
package com.qhrtech.emr.restapi.services;

import java.util.Objects;

public class BlobIdentifier {

  private String storageAccount;
  private String containerName;
  private String blobName;

  private BlobIdentifier(String storageAccount, String containerName, String blobName) {
    this.storageAccount = storageAccount;
    this.containerName = containerName;
    this.blobName = blobName;
  }

  public String getStorageAccount() {
    return storageAccount;
  }

  public String getContainerName() {
    return containerName;
  }

  public String getBlobName() {
    return blobName;
  }

  public String toString() {
    return String.format("{ storageAccount=%s, containerName=%s, blobName=%s }",
        this.storageAccount,
        this.containerName,
        this.blobName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlobIdentifier that = (BlobIdentifier) o;
    return Objects.equals(storageAccount, that.storageAccount) && Objects.equals(
        containerName, that.containerName) && Objects.equals(blobName, that.blobName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storageAccount, containerName, blobName);
  }

  public static class BlobIdentifierBuilder {


    private String storageAccount;
    private String containerName;
    private String blobName;

    public BlobIdentifierBuilder withContainerName(String containerName) {
      this.containerName = containerName;
      return this;
    }

    public BlobIdentifierBuilder withStorageAccount(String storageAccount) {
      this.storageAccount = storageAccount;
      return this;
    }

    public BlobIdentifierBuilder withBlobName(String blobName) {
      this.blobName = blobName;
      return this;
    }

    public BlobIdentifier build() {
      return new BlobIdentifier(storageAccount, containerName, blobName);
    }
  }
}
