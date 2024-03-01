
package com.qhrtech.emr.restapi.models.registry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantDataSourceDetails {

  private long hostId;
  private String tenantId;

  @JsonProperty("databaseName")
  private String medadminDatabaseName;
  @JsonProperty("documentDbCustomName")
  private String accdocsDatabaseName;
  private String dbUser;
  private String dbPassword;
  private String address;
  private String dbServersPort;

  public long getHostId() {
    return hostId;
  }

  public void setHostId(long hostId) {
    this.hostId = hostId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getAccdocsDatabaseName() {
    return accdocsDatabaseName;
  }

  public void setAccdocsDatabaseName(String accdocsDatabaseName) {
    this.accdocsDatabaseName = accdocsDatabaseName;
  }

  public String getMedadminDatabaseName() {
    return medadminDatabaseName;
  }

  public void setMedadminDatabaseName(String medadminDatabaseName) {
    this.medadminDatabaseName = medadminDatabaseName;
  }

  public String getDbUser() {
    return dbUser;
  }

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDbServersPort() {
    return dbServersPort;
  }

  public void setDbServersPort(String dbServersPort) {
    this.dbServersPort = dbServersPort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TenantDataSourceDetails that = (TenantDataSourceDetails) o;

    if (getHostId() != that.getHostId()) {
      return false;
    }
    if (getTenantId() != null ? !getTenantId().equals(that.getTenantId())
        : that.getTenantId() != null) {
      return false;
    }
    if (getMedadminDatabaseName() != null ? !getMedadminDatabaseName().equals(
        that.getMedadminDatabaseName()) : that.getMedadminDatabaseName() != null) {
      return false;
    }
    if (getAccdocsDatabaseName() != null ? !getAccdocsDatabaseName().equals(
        that.getAccdocsDatabaseName()) : that.getAccdocsDatabaseName() != null) {
      return false;
    }
    if (getDbUser() != null ? !getDbUser().equals(that.getDbUser()) : that.getDbUser() != null) {
      return false;
    }
    if (getDbPassword() != null ? !getDbPassword().equals(that.getDbPassword())
        : that.getDbPassword() != null) {
      return false;
    }
    if (getAddress() != null ? !getAddress().equals(that.getAddress())
        : that.getAddress() != null) {
      return false;
    }
    return getDbServersPort() != null ? getDbServersPort().equals(that.getDbServersPort())
        : that.getDbServersPort() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getHostId() ^ (getHostId() >>> 32));
    result = 31 * result + (getTenantId() != null ? getTenantId().hashCode() : 0);
    result =
        31 * result + (getMedadminDatabaseName() != null ? getMedadminDatabaseName().hashCode()
            : 0);
    result =
        31 * result + (getAccdocsDatabaseName() != null ? getAccdocsDatabaseName().hashCode() : 0);
    result = 31 * result + (getDbUser() != null ? getDbUser().hashCode() : 0);
    result = 31 * result + (getDbPassword() != null ? getDbPassword().hashCode() : 0);
    result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
    result = 31 * result + (getDbServersPort() != null ? getDbServersPort().hashCode() : 0);
    return result;
  }
}
