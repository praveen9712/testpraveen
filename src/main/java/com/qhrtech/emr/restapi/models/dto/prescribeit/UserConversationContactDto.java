
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "UserConversationContact Data transfer object")
public class UserConversationContactDto {

  @NotNull
  @JsonProperty("userId")
  @Schema(description = "The user id", example = "20")
  private Integer userId;

  @NotNull
  @JsonProperty("contactId")
  @Schema(description = "The unique id of the conversation contact", example = "1")
  private Integer contactId;

  @JsonProperty("lastSyncTime")
  @Schema(description = "The last sync time", example = "2021-08-04T22:32:51.067")
  private LocalDateTime lastSyncTime;

  /**
   * The User id
   *
   * @return The userId
   * @documentationExample 1
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * The Unique Id of Conversation Contact
   *
   * @return The contactId
   * @documentationExample 1
   */
  public Integer getContactId() {
    return contactId;
  }

  public void setContactId(Integer contactId) {
    this.contactId = contactId;
  }

  /**
   * The Last Sync time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastSyncTime() {
    return lastSyncTime;
  }

  public void setLastSyncTime(LocalDateTime lastSyncTime) {
    this.lastSyncTime = lastSyncTime;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.userId);
    hash = 59 * hash + Objects.hashCode(this.contactId);
    hash = 59 * hash + Objects.hashCode(this.lastSyncTime);
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
    final UserConversationContactDto other = (UserConversationContactDto) obj;
    if (!Objects.equals(this.userId, other.userId)) {
      return false;
    }
    if (!Objects.equals(this.contactId, other.contactId)) {
      return false;
    }
    return Objects.equals(this.lastSyncTime, other.lastSyncTime);
  }
}
