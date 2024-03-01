
package com.qhrtech.emr.restapi.models.dto.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/**
 * The envelope data transfer object model used for pagination. This can be used if we want to have
 * a Map in contents
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Envelope data transfer object model used for pagination.")
public class EnvelopeMapDto<K, E> {

  @JsonProperty("contents")
  @Schema(description = "The paginated results of the query")
  private Map<K, E> contents;

  @JsonProperty("count")
  @Schema(description = "The count of records returned with this page", example = "100")
  private int count;

  @JsonProperty("total")
  @Schema(
      description = "The total number of records based on the parameters of the query. \n\n"
          + "Note that this number can vary as a client moves through pages, as elements are added "
          + "and removed externally.",
      example = "1340")
  private int total;

  @JsonProperty("lastId")
  @Schema(
      description = "The unique identifier of the last records in **EnvelopeDto#contents**.\n\n"
          + "Can be used to determine the starting id of the next page.",
      example = "10034")
  private Long lastId;

  /**
   * The paginated results of the query.
   *
   * @return All elements for the current page of the requested query.
   */
  public Map<K, E> getContents() {
    return contents;
  }

  public void setContents(Map<K, E> contents) {
    this.contents = contents;
  }

  /**
   * The count of records returned with this page.
   *
   * @documentationExample 100
   *
   * @return The count of records contained in {@link EnvelopeMapDto#contents}.
   */
  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  /**
   * The total number of records based on the parameters of the query. Note that this number can
   * vary as a client moves through pages, as elements are added and removed externally.
   *
   * @documentationExample 1340
   *
   * @return The total count of all matching records.
   */
  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  /**
   * The identifier of the last record in {@link EnvelopeMapDto#contents}. Can be used to determine
   * the starting id of the next page. This cannot be null. In the event that there are no items on
   * the current page this will be 0.
   *
   * @documentationExample 10034
   *
   * @return The id of the last record in the current page.
   */
  public Long getLastId() {
    return lastId;
  }

  public void setLastId(Long lastId) {
    this.lastId = lastId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EnvelopeMapDto that = (EnvelopeMapDto) o;

    if (getCount() != that.getCount()) {
      return false;
    }
    if (getTotal() != that.getTotal()) {
      return false;
    }
    if (getContents() != null ? !getContents().equals(that.getContents())
        : that.getContents() != null) {
      return false;
    }
    return getLastId() != null ? getLastId().equals(that.getLastId()) : that.getLastId() == null;
  }

  @Override
  public int hashCode() {
    int result = getContents() != null ? getContents().hashCode() : 0;
    result = 31 * result + getCount();
    result = 31 * result + getTotal();
    result = 31 * result + (getLastId() != null ? getLastId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EnvelopeDto{"
        + "contents=" + contents
        + ", count=" + count
        + ", total=" + total
        + ", lastId=" + lastId
        + '}';
  }
}
