
package com.qhrtech.emr.restapi.models.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudStorageAccount {

  @JsonProperty("sa_name")
  private String storageAccountName;

  @JsonProperty("cont_name")
  private String containerName;

  public String getStorageAccountName() {
    return storageAccountName;
  }

  public void setStorageAccountName(String storageAccountName) {
    this.storageAccountName = storageAccountName;
  }

  public String getContainerName() {
    return containerName;
  }

  public void setContainerName(String containerName) {
    this.containerName = containerName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloudStorageAccount that = (CloudStorageAccount) o;
    return Objects.equals(storageAccountName, that.storageAccountName)
        && Objects.equals(containerName, that.containerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storageAccountName, containerName);
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("CloudStorageAccount{");
    sb.append("storageAccountName='").append(storageAccountName).append('\'');
    sb.append(", containerName='").append(containerName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
