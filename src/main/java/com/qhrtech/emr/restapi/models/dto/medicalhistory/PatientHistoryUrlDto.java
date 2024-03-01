
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * The PatientHistoryUrl data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrl
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientHistoryUrl data transfer object model")
public class PatientHistoryUrlDto extends AbstractPatientHistoryItemDto
    implements Serializable {

  @JsonProperty("historyTypeId")
  @Schema(description = "The history type id", example = "15")
  private int historyTypeId;

  @JsonProperty("historyDate")
  @Schema(type = "string", description = "The history date", example = "2018-09-11")
  private AccuroCalendar historyDate;

  @JsonProperty("url")
  @Schema(description = "The URL", example = "www.example.com")
  private String url;

  @JsonProperty("urlName")
  @Schema(description = "The URL name", example = "Sample")
  private String urlName;

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  /**
   * The History type id.
   *
   * @documentationExample 15
   *
   * @return History type id
   */
  public int getHistoryTypeId() {
    return historyTypeId;
  }

  public void setHistoryTypeId(int historyTypeId) {
    this.historyTypeId = historyTypeId;
  }

  /**
   * The history date.
   *
   * @return History date
   */
  @DocumentationExample("2018-09-11")
  @TypeHint(String.class)
  public AccuroCalendar getHistoryDate() {
    return historyDate;
  }

  public void setHistoryDate(AccuroCalendar historyDate) {
    this.historyDate = historyDate;
  }

  /**
   * The URL.
   *
   * @documentationExample www.example.com
   *
   * @return URL
   */
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * The URL name.
   *
   * @documentationExample sample name
   *
   * @return URL name
   */
  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }

  /**
   * A flag to indicate the delete state of this patient history item.
   *
   * @documentationExample true
   *
   * @return The delete state
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 61 * hash + this.historyTypeId;
    hash = 61 * hash + Objects.hashCode(this.historyDate);
    hash = 61 * hash + Objects.hashCode(this.url);
    hash = 61 * hash + Objects.hashCode(this.urlName);
    hash = 61 * hash + (this.deleted ? 1 : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PatientHistoryUrlDto other = (PatientHistoryUrlDto) obj;
    if (this.historyTypeId != other.historyTypeId) {
      return false;
    }
    if (!Objects.equals(this.url, other.url)) {
      return false;
    }
    if (!Objects.equals(this.urlName, other.urlName)) {
      return false;
    }
    if (this.deleted != other.deleted) {
      return false;
    }
    return Objects.equals(this.historyDate, other.historyDate);
  }

  @Override
  public String toString() {
    return "PatientHistoryUrlDto{" + "historyTypeId=" + historyTypeId + ", historyDate="
        + historyDate + ", url=" + url + ", urlName=" + urlName + '}';
  }

}
