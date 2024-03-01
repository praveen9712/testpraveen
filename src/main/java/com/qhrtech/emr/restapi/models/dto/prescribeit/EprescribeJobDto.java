
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.qhrtech.emr.restapi.validators.CheckUuid;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "EprescribeJob Data transfer object")
public class EprescribeJobDto {

  @JsonProperty("ePrescribeJobId")
  @Schema(description = "The e-prescribe job id", example = "1")
  private int eprescribeJobId;

  @NotNull
  @JsonProperty("jobUuid")
  @Size(max = 36, message = "The UUID can not exceed 36 characters.")
  @Schema(description = "The e-prescribe job uuid",
      example = "F96CBE3C-5113-440C-BBE1-E77ADCA01B8B")
  @CheckUuid
  private String jobUuid;

  @NotNull
  @CheckLocalDateTimeRange
  @JsonProperty("queuedAt")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(description = "The date queued at", example = "2012-02-15T07:44:59.000")
  private LocalDateTime queuedAt;

  @JsonProperty("correlationId")
  @Size(max = 36, message = "The correlation id can not exceed 36 characters.")
  @Schema(description = "The correlation id")
  private String correlationId;

  @NotNull
  @JsonProperty("jobType")
  @Schema(description = "The e-prescribe job type")
  private EprescribeJobTypeDto jobType;

  @CheckLocalDateTimeRange
  @JsonProperty("processedDate")
  @Schema(description = "The processed date", example = "2012-02-15 07:44:59.000")
  private LocalDateTime processedDate;

  @NotNull
  @JsonProperty("processed")
  @Schema(description = "The flag if the job processed", example = "true")
  private Boolean processed;

  @JsonProperty("messageHeaderId")
  @Size(max = 36, message = "The message head id can not exceed 36 characters.")
  @Schema(description = "The message header id")
  private String messageHeaderId;

  @JsonProperty("conversationId")
  @Schema(description = "The conversation id")
  private Integer conversationId;

  @JsonProperty("conversationMessageId")
  @Schema(description = "The conversation message id")
  private Integer conversationMessageId;

  /**
   * The e-prescribe job id
   *
   * @return The e-prescribe job id
   * @documentationExample 1
   */
  public int getEprescribeJobId() {
    return eprescribeJobId;
  }

  public void setEprescribeJobId(int eprescribeJobId) {
    this.eprescribeJobId = eprescribeJobId;
  }

  /**
   * The e-prescribe job uuid
   *
   * @return The e-prescribe job uuid
   * @documentationExample F96CBE3C-5113-440C-BBE1-E77ADCA01B8B
   */
  public String getJobUuid() {
    return jobUuid;
  }

  public void setJobUuid(String jobUuid) {
    this.jobUuid = jobUuid;
  }

  /**
   * The date queued at
   *
   * @return The date queued at
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getQueuedAt() {
    return queuedAt;
  }

  public void setQueuedAt(LocalDateTime queuedAt) {
    this.queuedAt = queuedAt;
  }

  /**
   * The correlation id
   * 
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   *
   * @return The correlation id
   */
  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  /**
   * The e-prescribe job type
   * 
   * @documentationExample {@link EprescribeJobTypeDto}
   *
   * @return The e-prescribe job type
   */
  public EprescribeJobTypeDto getJobType() {
    return jobType;
  }

  public void setJobType(EprescribeJobTypeDto jobType) {
    this.jobType = jobType;
  }

  /**
   * The processed date
   *
   * @return The processed date
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getProcessedDate() {
    return processedDate;
  }

  public void setProcessedDate(LocalDateTime processedDate) {
    this.processedDate = processedDate;
  }

  /**
   * The flag if the job processed
   * 
   * @documentationExample true
   *
   * @return true if processed, otherwise false
   */
  public Boolean isProcessed() {
    return processed;
  }

  public void setProcessed(Boolean processed) {
    this.processed = processed;
  }

  /**
   * The message header id
   * 
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   *
   * @return The message header id
   */
  public String getMessageHeaderId() {
    return messageHeaderId;
  }

  public void setMessageHeaderId(String messageHeaderId) {
    this.messageHeaderId = messageHeaderId;
  }

  /**
   * The conversation id
   *
   * @return The conversation id
   * @documentationExample 1
   */
  public Integer getConversationId() {
    return conversationId;
  }

  public void setConversationId(Integer conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * The conversation message id
   *
   * @return The conversation message id
   * @documentationExample 1
   */
  public Integer getConversationMessageId() {
    return conversationMessageId;
  }

  public void setConversationMessageId(Integer conversationMessageId) {
    this.conversationMessageId = conversationMessageId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EprescribeJobDto that = (EprescribeJobDto) o;

    if (eprescribeJobId != that.eprescribeJobId) {
      return false;
    }
    if (!Objects.equals(jobUuid, that.jobUuid)) {
      return false;
    }
    if (!Objects.equals(queuedAt, that.queuedAt)) {
      return false;
    }
    if (!Objects.equals(correlationId, that.correlationId)) {
      return false;
    }
    if (!Objects.equals(jobType, that.jobType)) {
      return false;
    }
    if (!Objects.equals(processedDate, that.processedDate)) {
      return false;
    }
    if (!Objects.equals(processed, that.processed)) {
      return false;
    }
    if (!Objects.equals(messageHeaderId, that.messageHeaderId)) {
      return false;
    }
    if (!Objects.equals(conversationId, that.conversationId)) {
      return false;
    }
    return Objects.equals(conversationMessageId, that.conversationMessageId);
  }

  @Override
  public int hashCode() {
    int result = eprescribeJobId;
    result = 31 * result + (jobUuid != null ? jobUuid.hashCode() : 0);
    result = 31 * result + (queuedAt != null ? queuedAt.hashCode() : 0);
    result = 31 * result + (correlationId != null ? correlationId.hashCode() : 0);
    result = 31 * result + (jobType != null ? jobType.hashCode() : 0);
    result = 31 * result + (processedDate != null ? processedDate.hashCode() : 0);
    result = 31 * result + (processed != null ? processed.hashCode() : 0);
    result = 31 * result + (messageHeaderId != null ? messageHeaderId.hashCode() : 0);
    result = 31 * result + (conversationId != null ? conversationId.hashCode() : 0);
    result = 31 * result + (conversationMessageId != null ? conversationMessageId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("EprescribeJobDto{");
    sb.append("eprescribeJobId=").append(eprescribeJobId);
    sb.append(", jobUuid='").append(jobUuid).append('\'');
    sb.append(", queuedAt=").append(queuedAt);
    sb.append(", correlationId='").append(correlationId).append('\'');
    sb.append(", jobType=").append(jobType);
    sb.append(", processedDate=").append(processedDate);
    sb.append(", processed=").append(processed);
    sb.append(", messageHeaderId='").append(messageHeaderId).append('\'');
    sb.append(", conversationId=").append(conversationId);
    sb.append(", conversationMessageId=").append(conversationMessageId);
    sb.append('}');
    return sb.toString();
  }
}
