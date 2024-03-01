
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.locks.ProtectionType;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

/**
 * Protection lock model object
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProtectionLockDto {

  @JsonProperty("type")
  private ProtectionType type;

  @JsonProperty("id")
  private String id;

  @JsonProperty("uuid")
  private UUID uuid;

  @JsonProperty("clientUUID")
  private UUID clientUuid;

  @JsonProperty("username")
  private String username;

  @JsonProperty("timestamp")
  private Calendar timestamp;

  @JsonProperty("expiration")
  private Calendar expiration;

  @JsonProperty("machine")
  private String machine;

  /**
   * Protection lock type.
   *
   * @documentationExample Appointment
   *
   * @return Lock type
   */
  public ProtectionType getType() {
    return type;
  }

  public void setType(ProtectionType type) {
    this.type = type;
  }

  /**
   * Protection lock ID.
   *
   * @documentationExample 1
   *
   * @return Lock ID
   */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Protection Lock UUID.
   *
   * @documentationExample ce423b77-6020-42d6-9ef3-8e1b9ef38697
   *
   * @return Lock UUID
   */
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Client UUID.
   *
   * @documentationExample av1212v1-7676-45h5-1212-322fv3213f45
   *
   * @return Client UUID
   */
  public UUID getClientUuid() {
    return clientUuid;
  }

  public void setClientUuid(UUID clientUuid) {
    this.clientUuid = clientUuid;
  }

  /**
   * Accuro User associated with the lock.
   *
   * @documentationExample qaprovider
   *
   * @return Username
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Timestamp of the lock's creation.
   *
   * @return Timestamp
   */
  @DocumentationExample("2018-01-12T15:54:05.570-0500")
  @TypeHint(String.class)
  public Calendar getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Calendar timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Protection lock expiration time.
   *
   * @return Timestamp
   */
  @DocumentationExample("2018-01-12T15:54:05.570-0500")
  @TypeHint(String.class)
  public Calendar getExpiration() {
    return expiration;
  }

  public void setExpiration(Calendar expiration) {
    this.expiration = expiration;
  }

  /**
   * Name of the machine from where the lock originated.
   *
   * @documentationExample EMRFlast/11.222.233.244
   *
   * @return Machine name
   */
  public String getMachine() {
    return machine;
  }

  public void setMachine(String machine) {
    this.machine = machine;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 41 * hash + Objects.hashCode(this.type);
    hash = 41 * hash + Objects.hashCode(this.id);
    hash = 41 * hash + Objects.hashCode(this.uuid);
    hash = 41 * hash + Objects.hashCode(this.clientUuid);
    hash = 41 * hash + Objects.hashCode(this.username);
    hash = 41 * hash + Objects.hashCode(this.timestamp);
    hash = 41 * hash + Objects.hashCode(this.expiration);
    hash = 41 * hash + Objects.hashCode(this.machine);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ProtectionLockDto other = (ProtectionLockDto) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.machine, other.machine)) {
      return false;
    }
    if (this.type != other.type) {
      return false;
    }
    if (!Objects.equals(this.uuid, other.uuid)) {
      return false;
    }
    if (!Objects.equals(this.clientUuid, other.clientUuid)) {
      return false;
    }
    if (!Objects.equals(this.timestamp, other.timestamp)) {
      return false;
    }
    if (!Objects.equals(this.expiration, other.expiration)) {
      return false;
    }
    return true;
  }

}
