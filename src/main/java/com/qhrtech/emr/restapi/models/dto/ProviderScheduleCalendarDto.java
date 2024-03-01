
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDate;

/**
 * The Provider schedule calendar model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The provider schedule calendar data transfer object.")
public class ProviderScheduleCalendarDto {

  @JsonProperty("providerId")
  @Schema(description = "Unique id of the provider", example = "1")
  private int providerId;

  @JsonProperty("title")
  @Schema(description = "Title for the note")
  @Size(max = 150)
  @NotNull
  private String title;

  @JsonProperty("note")
  @Schema(description = "Note for the provider")
  @NotNull
  @Size(max = 500)
  private String note;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("date")
  @NotNull
  @Schema(description = "Date for the provider note",
      type = "string",
      example = "2022-11-29")
  private LocalDate date;

  public int getProviderId() {
    return providerId;
  }

  public void setProviderId(int providerId) {
    this.providerId = providerId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProviderScheduleCalendarDto that = (ProviderScheduleCalendarDto) o;

    if (providerId != that.providerId) {
      return false;
    }
    if (!Objects.equals(title, that.title)) {
      return false;
    }
    if (!Objects.equals(note, that.note)) {
      return false;
    }
    return Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    int result = providerId;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (note != null ? note.hashCode() : 0);
    result = 31 * result + (date != null ? date.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ProviderScheduleCalendarDto{");
    sb.append("providerId=").append(providerId);
    sb.append(", title='").append(title).append('\'');
    sb.append(", note='").append(note).append('\'');
    sb.append(", date=").append(date);
    sb.append('}');
    return sb.toString();
  }
}
