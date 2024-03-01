
package com.qhrtech.emr.restapi.models.waitlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * The consult status for waitlist object.
 * 
 * @see com.qhrtech.emr.accuro.model.waitlist.ConsultStatus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Waitlist consultStatus data transfer object")
public class ConsultStatusDto {

  @JsonProperty("statusId")
  @Schema(description = "The unique id of waitlist consult status.", example = "3")
  private int id;

  @JsonProperty("statusName")
  @Schema(description = "Name of the waitlist consult status.", example = "Booked")
  @NotNull
  @Size(max = 255)
  private String statusName;

  @JsonProperty("completed")
  @Schema(description = "Shows the wailtlist consult status is completed or not.", example = "true")
  private boolean completed;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }


  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConsultStatusDto that = (ConsultStatusDto) o;

    if (id != that.id) {
      return false;
    }
    if (completed != that.completed) {
      return false;
    }
    return Objects.equals(statusName, that.statusName);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (statusName != null ? statusName.hashCode() : 0);
    result = 31 * result + (completed ? 1 : 0);
    return result;
  }
}
