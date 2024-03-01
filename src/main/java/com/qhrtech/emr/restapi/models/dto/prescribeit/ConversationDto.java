
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Conversation Data transfer object")
public class ConversationDto {

  @JsonProperty("conversationId")
  @Schema(description = "The id of the Conversation", example = "1")
  private int conversationId;

  @JsonProperty("subject")
  @Schema(description = "The conversation subject", example = "Hello")
  private String subject;

  @JsonProperty("owner")
  @Schema(description = "The owner of the Conversation", example = "1")
  private int owner;

  @CheckLocalDateTimeRange
  @JsonProperty("archived")
  @Schema(description = "The archived date in UTC", example = "2020-02-10T00:00:00.000")
  private LocalDateTime archived;

  @JsonProperty("conversationType")
  @Schema(description = "The conversation type", example = "EREFERRAL")
  @NotNull(message = "conversation type is a required field.")
  private MessageType conversationType;

  @JsonProperty("externalIdentifier")
  @Size(max = 256, message = "Maximum length allowed is 256 characters.")
  private String externalIdentifier;

  @JsonProperty("conversationSubType")
  @Size(max = 50, message = "Maximum length allowed is 50 characters.")
  private String conversationSubType;

  /**
   * Unique Conversation ID
   *
   * @return Conversation ID
   * @documentationExample 1
   */
  public int getConversationId() {
    return conversationId;
  }

  public void setConversationId(int conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * The subject of Conversation.
   *
   * @return Conversation subject
   * @documentationExample subject
   */
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * The owner of Conversation.
   *
   * @return Conversation owner . The owner is a conversation contact Id
   * @documentationExample 1
   */
  public int getOwner() {
    return owner;
  }

  public void setOwner(int owner) {
    this.owner = owner;
  }

  /**
   * The archived date of Conversation in UTC.
   *
   * @return Archived date
   * @documentationExample 2020-02-10T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getArchived() {
    return archived;
  }

  public void setArchived(LocalDateTime archived) {
    this.archived = archived;
  }

  /**
   * The Message type of Conversation.
   *
   * @return {@link MessageType}
   * @documentationExample EREFERRAL
   */
  public MessageType getConversationType() {
    return conversationType;
  }

  public void setConversationType(MessageType messageType) {
    this.conversationType = messageType;
  }

  /**
   * The external identifier of conversation.
   *
   * @return external identifier
   * @documentationExample KkHxdzrtIdbsGMOpu7E2X88CuUhOuSAOR9k
   */
  public String getExternalIdentifier() {
    return externalIdentifier;
  }

  public void setExternalIdentifier(String externalIdentifier) {
    this.externalIdentifier = externalIdentifier;
  }

  /**
   * The conversation sub type.
   *
   * @return Sub type
   * @documentationExample String
   */
  public String getConversationSubType() {
    return conversationSubType;
  }

  public void setConversationSubType(String conversationSubType) {
    this.conversationSubType = conversationSubType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationDto that = (ConversationDto) o;
    if (conversationId != that.conversationId) {
      return false;
    }
    if (owner != that.owner) {
      return false;
    }
    if (subject != null ? !subject.equals(that.subject) : that.subject != null) {
      return false;
    }
    if (archived != null ? !archived.equals(that.archived) : that.archived != null) {
      return false;
    }
    if (conversationType != that.conversationType) {
      return false;
    }
    if (externalIdentifier != null ? !externalIdentifier.equals(that.externalIdentifier)
        : that.externalIdentifier != null) {
      return false;
    }
    return conversationSubType != null ? conversationSubType.equals(that.conversationSubType)
        : that.conversationSubType == null;
  }

  @Override
  public int hashCode() {
    int result = conversationId;
    result = 31 * result + (subject != null ? subject.hashCode() : 0);
    result = 31 * result + owner;
    result = 31 * result + (archived != null ? archived.hashCode() : 0);
    result = 31 * result + (conversationType != null ? conversationType.hashCode() : 0);
    result = 31 * result + (externalIdentifier != null ? externalIdentifier.hashCode() : 0);
    result = 31 * result + (conversationSubType != null ? conversationSubType.hashCode() : 0);
    return result;
  }
}
