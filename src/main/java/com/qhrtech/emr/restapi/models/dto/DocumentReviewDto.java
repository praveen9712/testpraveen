
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckDocumentReviews;
import com.qhrtech.emr.restapi.validators.CheckFutureDate;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@CheckDocumentReviews
@Schema(description = "DocumentReview data transfer object model")
public class DocumentReviewDto implements Serializable {

  @JsonProperty("physician_id")
  @Schema(
      description = "The id of physician who reviewed the document. During document write request, "
          + "this field becomes mandatory if review date is not null.",
      example = "12129")
  private int providerId;

  @CheckFutureDate
  @JsonProperty("reviewDate")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(
      description = "The date when the document is reviewed. This field indicates if the "
          + "document is reviewed or not. If this field is null, system will set the document "
          + "as un-reviewed. If valid date is provided, system will set the document as reviewed.",
      type = "string", example = "2018-10-23T00:00:00.000")
  private LocalDateTime reviewDate;

  /**
   * Id of physician who reviewed the document. During document write request, this field becomes
   * mandatory if reviewDate field is not null.
   *
   * @return physician id
   * @documentationExample 12129
   */
  public int getProviderId() {
    return providerId;
  }

  public void setProviderId(int providerId) {
    this.providerId = providerId;
  }

  /**
   * Date when the document is reviewed. This field indicates if the document is reviewed or not. If
   * this field is null, system will set the document as un-reviewed. If valid date is provided,
   * system will set the document as reviewed.
   *
   * @return Review Date
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getReviewDate() {
    return reviewDate;
  }

  public void setReviewDate(LocalDateTime reviewDate) {
    this.reviewDate = reviewDate;
  }

  @Override
  public String toString() {
    return "DocumentReviewDTO{"
        + "providerId=" + providerId
        + ", reviewDate=" + reviewDate
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DocumentReviewDto)) {
      return false;
    }
    DocumentReviewDto that = (DocumentReviewDto) o;
    return getProviderId() == that.getProviderId()
        && Objects.equals(getReviewDate(), that.getReviewDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProviderId(), getReviewDate());
  }
}
