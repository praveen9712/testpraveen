
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * Conversation participant data transfer object
 */
public class ConversationParticipantDto implements Serializable {

  @JsonProperty("conversationId")
  @Schema(description = "The unique id of the conversation.", example = "1")
  private int conversationId;

  @JsonProperty("conversationContactId")
  @Schema(description = "The unique id of the conversation contact.", example = "1")
  private int conversationContactId;

  @JsonProperty("role")
  @Schema(description = "The role id.", example = "1")
  private Integer role;

  /**
   * Unique Conversation ID
   *
   * @return Conversation ID
   * @documentationExample 10
   */
  public int getConversationId() {
    return conversationId;
  }

  public void setConversationId(int conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * Unique conversation contact ID
   *
   * @return conversation contact ID
   * @documentationExample 10
   */
  public int getConversationContactId() {
    return conversationContactId;
  }

  public void setConversationContactId(int conversationContactId) {
    this.conversationContactId = conversationContactId;
  }

  /**
   * Role ID
   *
   * @return role ID
   * @documentationExample 10
   */
  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConversationParticipantDto that = (ConversationParticipantDto) o;

    if (conversationId != that.conversationId) {
      return false;
    }
    if (conversationContactId != that.conversationContactId) {
      return false;
    }
    return Objects.equals(role, that.role);
  }

  @Override
  public int hashCode() {
    int result = conversationId;
    result = 31 * result + conversationContactId;
    result = 31 * result + (role != null ? role.hashCode() : 0);
    return result;
  }
}
