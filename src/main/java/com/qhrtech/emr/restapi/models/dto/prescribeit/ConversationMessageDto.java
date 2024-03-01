
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Conversatoin Message data transfer object")
public class ConversationMessageDto {

  @JsonProperty("messageId")
  @Schema(description = "The id of the Conversation Message", example = "1")
  private int messageId;

  @JsonProperty("conversationId")
  @Schema(description = "The conversation id", example = "1")
  private int conversationId;

  @JsonProperty("sender")
  @Schema(description = "The conversation contact id for a sender", example = "1")
  private int sender;

  @JsonProperty("messageBody")
  @Schema(description = "The message body", example = "Hello")
  private String messageBody;

  @JsonProperty("timestamp")
  @Schema(description = "The time stamp", example = "2020-02-10T00:00:00.000")
  private LocalDateTime timestamp;

  @JsonProperty("draft")
  @Schema(description = "Is conversation message drafted", example = "false")
  private boolean draft;

  @JsonProperty("deleted")
  @Schema(description = "Deleted or active", example = "true")
  private boolean deleted;

  @JsonProperty("delegator")
  @Schema(description = "The conversation contact id for a delegator", example = "1")
  private Integer delegator;

  @JsonProperty("messageSubtype")
  @Schema(description = "Sub type of the message", example = "ERX_RENEWAL")
  private MessagingSubtype messageSubtype;

  @JsonProperty("priority")
  @Schema(description = "Priority level", example = "2")
  private RequestPriority priority;

  /**
   * The id of Conversation message.
   *
   * @return message ID
   * @documentationExample 1
   */
  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  /**
   * The id of Conversation.
   *
   * @return {@link ConversationDto} ID
   * @documentationExample 1
   */
  public int getConversationId() {
    return conversationId;
  }

  public void setConversationId(int conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * The id of Conversation contact as sender.
   *
   * @return sender ID
   * @documentationExample 1
   */
  public int getSender() {
    return sender;
  }

  public void setSender(int sender) {
    this.sender = sender;
  }

  /**
   * The message body.
   *
   * @return message body
   * @documentationExample Hello
   */
  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  /**
   * The time stamp of Conversation message.
   *
   * @return Timestamp date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Is Conversation message drafted.
   *
   * @return True or false
   * @documentationExample True
   */
  public boolean isDraft() {
    return draft;
  }

  public void setDraft(boolean draft) {
    this.draft = draft;
  }

  /**
   * Is Conversation message deleted.
   *
   * @return True or false
   * @documentationExample True
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * The id of Conversation contact as delegator.
   *
   * @return delegator ID
   * @documentationExample 1
   */
  public Integer getDelegator() {
    return delegator;
  }

  public void setDelegator(Integer delegator) {
    this.delegator = delegator;
  }

  /**
   * The Messaging sub type of Conversation message.
   *
   * @return {@link MessagingSubtype}
   * @documentationExample ERX_RENEWAL
   */
  public MessagingSubtype getMessageSubtype() {
    return messageSubtype;
  }

  public void setMessageSubtype(MessagingSubtype messageSubtype) {
    this.messageSubtype = messageSubtype;
  }

  /**
   * Priority level of the Conversation message.
   *
   * @return RequestPriority
   * @documentationExample 2
   */
  public RequestPriority getPriority() {
    return priority;
  }

  public void setPriority(RequestPriority priority) {
    this.priority = priority;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConversationMessageDto that = (ConversationMessageDto) o;

    if (messageId != that.messageId) {
      return false;
    }
    if (conversationId != that.conversationId) {
      return false;
    }
    if (sender != that.sender) {
      return false;
    }
    if (draft != that.draft) {
      return false;
    }
    if (deleted != that.deleted) {
      return false;
    }
    if (!Objects.equals(messageBody, that.messageBody)) {
      return false;
    }
    if (!Objects.equals(timestamp, that.timestamp)) {
      return false;
    }
    if (!Objects.equals(delegator, that.delegator)) {
      return false;
    }
    if (messageSubtype != that.messageSubtype) {
      return false;
    }
    return Objects.equals(priority, that.priority);
  }

  @Override
  public int hashCode() {
    int result = messageId;
    result = 31 * result + conversationId;
    result = 31 * result + sender;
    result = 31 * result + (messageBody != null ? messageBody.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (draft ? 1 : 0);
    result = 31 * result + (deleted ? 1 : 0);
    result = 31 * result + (delegator != null ? delegator.hashCode() : 0);
    result = 31 * result + (messageSubtype != null ? messageSubtype.hashCode() : 0);
    result = 31 * result + (priority != null ? priority.hashCode() : 0);
    return result;
  }
}
