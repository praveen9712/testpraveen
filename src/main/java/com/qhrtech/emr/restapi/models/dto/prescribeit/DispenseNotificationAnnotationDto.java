
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

public class DispenseNotificationAnnotationDto {

  @JsonProperty("dispenseNotificationId")
  @Schema(description = "The identity", example = "10")
  private int dispenseNotificationId;

  @JsonProperty("time")
  @Schema(description = "Time", example = "2021-02-21T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime time;

  @JsonProperty("author")
  @Schema(description = "Author as identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID author;

  @NotNull
  @Size(max = 500)
  @JsonProperty("text")
  @Schema(description = "Text", example = "String")
  private String text;

  /**
   * The identity
   * 
   * @return DispenseNotification ID
   * @documentationExample 1
   */
  public int getDispenseNotificationId() {
    return dispenseNotificationId;
  }

  public void setDispenseNotificationId(int dispenseNotificationId) {
    this.dispenseNotificationId = dispenseNotificationId;
  }

  /**
   * The date time
   * 
   * @return LocalDateTime - date time
   * @documentationExample 2021-02-21T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

  /**
   * Dispense notification notes author uuid
   * 
   * @return UUID - Unique identifier of the author
   * @documentationExample 123e4567-e89b-12d3-a456-426614174000
   */
  public UUID getAuthor() {
    return author;
  }

  public void setAuthor(UUID author) {
    this.author = author;
  }

  /**
   * Dispense notification notes text
   * 
   * @return String - text details
   * @documentationExample details
   */
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DispenseNotificationAnnotationDto that = (DispenseNotificationAnnotationDto) o;
    if (dispenseNotificationId != that.dispenseNotificationId) {
      return false;
    }
    if (time != null ? !time.equals(that.time) : that.time != null) {
      return false;
    }
    if (author != null ? !author.equals(that.author) : that.author != null) {
      return false;
    }
    return text != null ? text.equals(that.text) : that.text == null;
  }

  @Override
  public int hashCode() {
    int result = dispenseNotificationId;
    result = 31 * result + (time != null ? time.hashCode() : 0);
    result = 31 * result + (author != null ? author.hashCode() : 0);
    result = 31 * result + (text != null ? text.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DispenseNotificationAnnotationDto{"
        + "dispenseNotificationId=" + dispenseNotificationId
        + ", time=" + time
        + ", author=" + author
        + ", text='" + text + '\''
        + '}';
  }
}
