
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

/**
 * Schedule lock model object
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleLockDto {

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

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("date")
  private Calendar date;

  @JsonProperty("startTime")
  private int startTime;

  @JsonProperty("endTime")
  private int endTime;

  @JsonProperty("subColumn")
  private int subColumn;

  @JsonProperty("providerId")
  private Integer providerId;

  @JsonProperty("resourceId")
  private Integer resourceId;

  /**
   * Schedule lock UUID
   *
   * @documentationExample 0818d315-95d1-48dd-9613-14aaca9b8df1
   *
   * @return UUID
   */
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * Client UUID
   *
   * @documentationExample 517aaff5-e7d6-47d8-a206-41d7daf39830
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
   * Username associated with schedule lock
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
   * Schedule lock timestamp
   *
   * @return Timestamp
   */
  @DocumentationExample("2018-01-12T16:08:56.490-0500")
  @TypeHint(String.class)
  public Calendar getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Calendar timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Schedule lock expiration date
   *
   * @return Timestamp
   */
  @DocumentationExample("2018-01-12T16:08:56.490-0500")
  @TypeHint(String.class)
  public Calendar getExpiration() {
    return expiration;
  }

  public void setExpiration(Calendar expiration) {
    this.expiration = expiration;
  }

  /**
   * Date of the locked section of the schedule
   *
   * @return Calender
   */
  @DocumentationExample("2017-12-12")
  @TypeHint(String.class)
  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    this.date = date;
  }

  /**
   * Start time of the locked section of the schedule
   *
   * @documentationExample 800
   *
   * @return Time
   */
  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  /**
   * End time of the locked section of the schedule
   *
   * @documentationExample 830
   *
   * @return Time
   */
  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  /**
   * Sub-column of the locked section of the schedule
   *
   * @documentationExample 0
   *
   * @return Sub-column
   */
  public int getSubColumn() {
    return subColumn;
  }

  public void setSubColumn(int subColumn) {
    this.subColumn = subColumn;
  }

  /**
   * ID of the provider associated with the locked section of the schedule
   *
   * @documentationExample 1
   *
   * @return Provider ID
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * ID of the resource associated with the locked section of the schedule
   *
   * @documentationExample 1
   *
   * @return Resource ID
   */
  public Integer getResourceId() {
    return resourceId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.uuid);
    hash = 59 * hash + Objects.hashCode(this.clientUuid);
    hash = 59 * hash + Objects.hashCode(this.username);
    hash = 59 * hash + Objects.hashCode(this.timestamp);
    hash = 59 * hash + Objects.hashCode(this.expiration);
    hash = 59 * hash + Objects.hashCode(this.date);
    hash = 59 * hash + this.startTime;
    hash = 59 * hash + this.endTime;
    hash = 59 * hash + this.subColumn;
    hash = 59 * hash + Objects.hashCode(this.providerId);
    hash = 59 * hash + Objects.hashCode(this.resourceId);
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
    final ScheduleLockDto other = (ScheduleLockDto) obj;
    if (this.startTime != other.startTime) {
      return false;
    }
    if (this.endTime != other.endTime) {
      return false;
    }
    if (this.subColumn != other.subColumn) {
      return false;
    }
    if (!Objects.equals(this.username, other.username)) {
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
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.providerId, other.providerId)) {
      return false;
    }
    if (!Objects.equals(this.resourceId, other.resourceId)) {
      return false;
    }
    return true;
  }

}
