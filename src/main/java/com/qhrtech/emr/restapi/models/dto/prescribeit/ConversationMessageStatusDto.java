
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Conversation message status data transfer object")
public class ConversationMessageStatusDto {

  @JsonProperty("messageId")
  @Schema(description = "Conversation message id", example = "1")
  private int messageId;

  @JsonProperty("contactId")
  @Schema(description = "Conversation contact id", example = "1")
  private int contactId;

  @JsonProperty("status")
  @Schema(description = "Message status", example = "PROCESSED")
  @NotNull
  private MessageStatusCode statusCode;

  @JsonProperty("updated")
  @Schema(description = "Updated time(Read-Only)", example = "2020-02-10T00:00:00.000")
  private LocalDateTime updated;

  /**
   * Conversation Message id
   * 
   * @return message id
   * @documentationExample 1
   */
  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  /**
   * Conversation contact id
   * 
   * @return contact id
   * @documentationExample 1
   */
  public int getContactId() {
    return contactId;
  }

  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * Message status code
   * 
   * @return Message status code
   * @documentationExample PROCESSED
   */
  public MessageStatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(MessageStatusCode statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Updated time(Read-Only)
   * 
   * @return Updated time
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationMessageStatusDto that = (ConversationMessageStatusDto) o;
    if (messageId != that.messageId) {
      return false;
    }
    if (contactId != that.contactId) {
      return false;
    }
    if (statusCode != that.statusCode) {
      return false;
    }
    return updated != null ? updated.equals(that.updated) : that.updated == null;
  }

  @Override
  public int hashCode() {
    int result = messageId;
    result = 31 * result + contactId;
    result = 31 * result + (statusCode != null ? statusCode.hashCode() : 0);
    result = 31 * result + (updated != null ? updated.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ConversationMessageStatus{"
        + "messageId=" + messageId
        + ", contactId=" + contactId
        + ", statusCode=" + statusCode
        + ", updated=" + updated
        + '}';
  }

}
