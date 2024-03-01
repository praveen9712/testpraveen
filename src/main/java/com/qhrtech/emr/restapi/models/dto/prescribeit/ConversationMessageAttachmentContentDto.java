
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class ConversationMessageAttachmentContentDto {

  @NotNull
  @JsonProperty("id")
  @Schema(description = "The id of the Conversation Message Attachment", example = "1")
  private Integer attachmentId;

  @NotNull
  @JsonProperty("content")
  @Schema(description = "The content of the attachment")
  private byte[] content;

  /**
   * The id of the Conversation Message Attachment.
   * 
   * @documentationExample 1
   * 
   * @return The id of the Conversation Message Attachment
   */
  public Integer getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(Integer attachmentId) {
    this.attachmentId = attachmentId;
  }

  /**
   * The content of the attachment.
   * 
   * @return The content of the attachment
   */
  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationMessageAttachmentContentDto that = (ConversationMessageAttachmentContentDto) o;
    return Objects.equals(attachmentId, that.attachmentId)
        && Arrays.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(attachmentId);
    result = 31 * result + Arrays.hashCode(content);
    return result;
  }

  @Override
  public String toString() {
    return "ConversationMessageAttachmentContent{"
        + "attachmentId=" + attachmentId
        + ", content=" + Arrays.toString(content)
        + '}';
  }
}
