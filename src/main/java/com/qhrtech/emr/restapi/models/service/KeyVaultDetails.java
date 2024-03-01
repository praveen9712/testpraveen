
package com.qhrtech.emr.restapi.models.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyVaultDetails {

  @JsonProperty("kv_name")
  private String name;

  @JsonProperty("rg_name")
  private String resourceGroupName;

  @JsonProperty("sub_name")
  private String subscriptionName;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResourceGroupName() {
    return resourceGroupName;
  }

  public void setResourceGroupName(String resourceGroupName) {
    this.resourceGroupName = resourceGroupName;
  }

  public String getSubscriptionName() {
    return subscriptionName;
  }

  public void setSubscriptionName(String subscriptionName) {
    this.subscriptionName = subscriptionName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    KeyVaultDetails that = (KeyVaultDetails) o;

    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(resourceGroupName, that.resourceGroupName)) {
      return false;
    }
    return Objects.equals(subscriptionName, that.subscriptionName);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (resourceGroupName != null ? resourceGroupName.hashCode() : 0);
    result = 31 * result + (subscriptionName != null ? subscriptionName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "KeyVaultDetails{"
        + "name='" + name + '\''
        + ", resourceGroupName='" + resourceGroupName + '\''
        + ", subscriptionName='" + subscriptionName + '\''
        + '}';
  }
}
