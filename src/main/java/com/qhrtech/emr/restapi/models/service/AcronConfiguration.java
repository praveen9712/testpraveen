
package com.qhrtech.emr.restapi.models.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcronConfiguration {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("accdocs_sa")
  private CloudStorageAccount cloudStorageAccount;

  @JsonProperty("self_kv")
  private KeyVaultDetails acronKeyVault;

  @JsonProperty("common_kv")
  private KeyVaultDetails commonKeyVault;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CloudStorageAccount getCloudStorageAccount() {
    return cloudStorageAccount;
  }

  public void setCloudStorageAccount(
      CloudStorageAccount cloudStorageAccount) {
    this.cloudStorageAccount = cloudStorageAccount;
  }

  public KeyVaultDetails getAcronKeyVault() {
    return acronKeyVault;
  }

  public void setAcronKeyVault(KeyVaultDetails acronKeyVault) {
    this.acronKeyVault = acronKeyVault;
  }

  public KeyVaultDetails getCommonKeyVault() {
    return commonKeyVault;
  }

  public void setCommonKeyVault(KeyVaultDetails commonKeyVault) {
    this.commonKeyVault = commonKeyVault;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcronConfiguration that = (AcronConfiguration) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name)
        && Objects.equals(cloudStorageAccount, that.cloudStorageAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, cloudStorageAccount);
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AcronConfiguration{");
    sb.append("id='").append(id).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", cloudStorageAccount=").append(cloudStorageAccount);
    sb.append('}');
    return sb.toString();
  }
}
