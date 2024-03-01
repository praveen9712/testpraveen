
package com.qhrtech.emr.restapi.models.dto.authorizedclients;

import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class AuthorizedClientDto {

  private int id;

  private UUID clientUuid;

  @NotNull
  @Size(max = 256)
  private String clientId;

  @NotNull
  @Size(max = 256)
  private String clientName;

  private int serviceUserId;

  private LocalDateTime createdDateTimeUtc;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UUID getClientUuid() {
    return clientUuid;
  }

  public void setClientUuid(UUID clientUuid) {
    this.clientUuid = clientUuid;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public int getServiceUserId() {
    return serviceUserId;
  }

  public void setServiceUserId(int serviceUserId) {
    this.serviceUserId = serviceUserId;
  }

  public LocalDateTime getCreatedDateTimeUtc() {
    return createdDateTimeUtc;
  }

  public void setCreatedDateTimeUtc(LocalDateTime createdDateTimeUtc) {
    this.createdDateTimeUtc = createdDateTimeUtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AuthorizedClientDto that = (AuthorizedClientDto) o;

    if (id != that.id) {
      return false;
    }
    if (serviceUserId != that.serviceUserId) {
      return false;
    }
    if (!Objects.equals(clientUuid, that.clientUuid)) {
      return false;
    }
    if (!Objects.equals(clientId, that.clientId)) {
      return false;
    }
    if (!Objects.equals(clientName, that.clientName)) {
      return false;
    }
    return Objects.equals(createdDateTimeUtc, that.createdDateTimeUtc);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (clientUuid != null ? clientUuid.hashCode() : 0);
    result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
    result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
    result = 31 * result + serviceUserId;
    result = 31 * result + (createdDateTimeUtc != null ? createdDateTimeUtc.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("AuthorizedClientDto{");
    sb.append("id=").append(id);
    sb.append(", clientUuid=").append(clientUuid);
    sb.append(", clientId='").append(clientId).append('\'');
    sb.append(", clientName='").append(clientName).append('\'');
    sb.append(", serviceUserId=").append(serviceUserId);
    sb.append(", createdDateTimeUTC=").append(createdDateTimeUtc);
    sb.append('}');
    return sb.toString();
  }
}
