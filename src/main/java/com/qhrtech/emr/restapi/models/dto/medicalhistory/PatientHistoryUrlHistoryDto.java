
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.restapi.endpoints.provider.medicalhistory.HistoryTypeEndpoint;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * Patient history URL history data transfer object and this model extends
 * {@link AbstractPatientHistoryItemHistoryDto}.
 *
 * @See {@link com.qhrtech.emr.accuro.model.medicalhistory.PatientHistoryUrlHistory}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientHistoryUrlHistoryDto extends AbstractPatientHistoryItemHistoryDto implements
    Serializable {

  @JsonProperty("historyTypeId")
  @Schema(description = "The unique id of the history type", example = "15")
  private int historyTypeId;

  @JsonProperty("historyDate")
  @Schema(
      type = "string",
      description = "The created date of this patient history url at the very beginning",
      example = "2018-09-11")
  private AccuroCalendar historyDate;

  @JsonProperty("url")
  @Schema(description = "The URL", example = "www.example.com")
  private String url;

  @JsonProperty("urlName")
  @Schema(description = "The URL name in the patient history url record", example = "Sample")
  private String urlName;

  @JsonProperty("deleted")
  @Schema(description = "Flag indicating if this record is deleted", example = "true")
  private boolean deleted;

  @JsonProperty("patientHistoryUrlId")
  @Schema(description = "The unique id of the patient history url record", example = "1")
  private int patientHistoryUrlId;

  @JsonProperty("historyUserId")
  @Schema(
      description = "The id of the Accuro user who created/updated the patient history URL record",
      example = "1")
  private Integer historyUserId;

  /**
   * The unique id of the history type.
   *
   * @see HistoryTypeEndpoint
   *
   * @documentationExample 1
   *
   * @return The history type id.
   */
  public int getHistoryTypeId() {
    return historyTypeId;
  }

  public void setHistoryTypeId(int historyTypeId) {
    this.historyTypeId = historyTypeId;
  }

  /**
   * The created date of this patient history url at the very beginning.
   *
   * @return The created date of this patient history url at the very beginning.
   */
  @DocumentationExample("2018-09-25T00:00:00.000-0700")
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
   * @documentationExample http://www.url-two.com
   *
   * @return The URL.
   */
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * The free URL name on the patient history url record.
   *
   * @documentationExample url two
   *
   * @return The URL name.
   */
  public String getUrlName() {
    return urlName;
  }

  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }

  /**
   * Whether this is deleted as marked by Accuro.
   *
   * @documentationExample true
   *
   * @return If this is an active record or not.
   */
  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * The unique id of the patient history url record.
   *
   * @documentationExample 1
   *
   * @return The patient history url id.
   */
  public int getPatientHistoryUrlId() {
    return patientHistoryUrlId;
  }

  public void setPatientHistoryUrlId(int patientHistoryUrlId) {
    this.patientHistoryUrlId = patientHistoryUrlId;
  }

  /**
   * The id of the Accuro user who created/updated the patient history URL record.
   *
   * @documentationExample 1
   *
   * @return The user id of the creator/updater.
   */
  public Integer getHistoryUserId() {
    return historyUserId;
  }

  public void setHistoryUserId(Integer historyUserId) {
    this.historyUserId = historyUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientHistoryUrlHistoryDto)) {
      return false;
    }

    PatientHistoryUrlHistoryDto that = (PatientHistoryUrlHistoryDto) o;

    if (getHistoryTypeId() != that.getHistoryTypeId()) {
      return false;
    }
    if (isDeleted() != that.isDeleted()) {
      return false;
    }
    if (getPatientHistoryUrlId() != that.getPatientHistoryUrlId()) {
      return false;
    }
    if (getHistoryDate() != null ? !getHistoryDate().equals(that.getHistoryDate())
        : that.getHistoryDate() != null) {
      return false;
    }
    if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) {
      return false;
    }
    if (getUrlName() != null ? !getUrlName().equals(that.getUrlName())
        : that.getUrlName() != null) {
      return false;
    }
    return getHistoryUserId() != null ? getHistoryUserId().equals(that.getHistoryUserId())
        : that.getHistoryUserId() == null;
  }

  @Override
  public int hashCode() {
    int result = getHistoryTypeId();
    result = 31 * result + (getHistoryDate() != null ? getHistoryDate().hashCode() : 0);
    result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
    result = 31 * result + (getUrlName() != null ? getUrlName().hashCode() : 0);
    result = 31 * result + (isDeleted() ? 1 : 0);
    result = 31 * result + getPatientHistoryUrlId();
    result = 31 * result + (getHistoryUserId() != null ? getHistoryUserId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PatientHistoryUrlHistoryDto{");
    sb.append(super.toString());
    sb.append(", historyTypeId=").append(historyTypeId);
    sb.append(", historyDate=").append(historyDate);
    sb.append(", url='").append(url).append('\'');
    sb.append(", urlName='").append(urlName).append('\'');
    sb.append(", deleted=").append(deleted);
    sb.append(", patientHistoryUrlId=").append(patientHistoryUrlId);
    sb.append(", historyUserId=").append(historyUserId);
    sb.append('}');
    return sb.toString();
  }
}
