
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "Conversation message attachement data transfer object")
public class ConversationMessageAttachmentDto {

  @JsonProperty("id")
  @Schema(description = "The id of the Conversation Message Attachment", example = "1")
  private Integer id;

  @NotNull
  @JsonProperty("conversationMessageId")
  @Schema(description = "The id of the Conversation Message", example = "1")
  private Integer conversationMessageId;

  @Size(max = 256)
  @JsonProperty("fileName")
  @Schema(description = "The file name", example = "Test file")
  private String fileName;

  @Size(max = 100)
  @JsonProperty("mimeType")
  @Schema(description = "The mime type", example = "text/plain")
  private String mimeType;

  @Size(max = 25)
  @JsonProperty("extension")
  @Schema(description = "The file extension", example = "pdf")
  private String extension;

  @Size(max = 50)
  @JsonProperty("externalFileId")
  @Schema(description = "The external file id", example = "1")
  private String externalFileId;

  @JsonProperty("uuid")
  @Schema(description = "The uuid", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID uuid;

  @JsonProperty("contentLengthBytes")
  @Schema(
      description = "The content length of the healthmail Attachment in bytes. Read only field.",
      example = "4354358903845")
  private Integer contentLengthBytes;


  /**
   * The id of the Conversation Message Attachment.
   *
   * @documentationExample 1
   *
   * @return The id of the Conversation Message Attachment
   */
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * The id of the Conversation Message.
   *
   * @documentationExample 1
   *
   * @return The id of the Conversation Message
   */
  public Integer getConversationMessageId() {
    return conversationMessageId;
  }

  public void setConversationMessageId(Integer conversationMessageId) {
    this.conversationMessageId = conversationMessageId;
  }

  /**
   * The file name.
   *
   * @documentationExample Test file
   *
   * @return The file name
   */
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * The mime type.
   *
   * @documentationExample text/plain
   *
   * @return The mime type
   */
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * The file extension.
   *
   * @documentationExample pdf
   *
   * @return The file extension
   */
  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  /**
   * The external file id.
   *
   * @documentationExample 1
   *
   * @return The external file id
   */
  public String getExternalFileId() {
    return externalFileId;
  }

  public void setExternalFileId(String externalFileId) {
    this.externalFileId = externalFileId;
  }

  /**
   * The UUID of the Conversation Message Attachment.
   *
   * @documentationExample 07683D1D-487F-4845-86B9-C67AEF2A65BE
   *
   * @return The UUID of the Conversation Message Attachment
   */
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /**
   * The content length of the Conversation Message Attachment. Read only field.
   *
   * @return The content length of the Conversation Message Attachment
   * @documentationExample 34353453454
   */
  public Integer getContentLengthBytes() {
    return contentLengthBytes;
  }

  public void setContentLengthBytes(Integer contentLengthBytes) {
    this.contentLengthBytes = contentLengthBytes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConversationMessageAttachmentDto that = (ConversationMessageAttachmentDto) o;

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(conversationMessageId, that.conversationMessageId)) {
      return false;
    }
    if (!Objects.equals(fileName, that.fileName)) {
      return false;
    }
    if (!Objects.equals(mimeType, that.mimeType)) {
      return false;
    }
    if (!Objects.equals(extension, that.extension)) {
      return false;
    }
    if (!Objects.equals(externalFileId, that.externalFileId)) {
      return false;
    }
    if (!Objects.equals(uuid, that.uuid)) {
      return false;
    }
    return Objects.equals(contentLengthBytes, that.contentLengthBytes);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (conversationMessageId != null ? conversationMessageId.hashCode() : 0);
    result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
    result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
    result = 31 * result + (extension != null ? extension.hashCode() : 0);
    result = 31 * result + (externalFileId != null ? externalFileId.hashCode() : 0);
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (contentLengthBytes != null ? contentLengthBytes.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ConversationMessageAttachmentDto{");
    sb.append("id=").append(id);
    sb.append(", conversationMessageId=").append(conversationMessageId);
    sb.append(", fileName='").append(fileName).append('\'');
    sb.append(", mimeType='").append(mimeType).append('\'');
    sb.append(", extension='").append(extension).append('\'');
    sb.append(", externalFileId='").append(externalFileId).append('\'');
    sb.append(", uuid=").append(uuid);
    sb.append(", contentLengthBytes=").append(contentLengthBytes);
    sb.append('}');
    return sb.toString();
  }
}
