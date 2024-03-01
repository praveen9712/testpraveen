
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyType;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "No known allergy data transfer object model")
public class NoKnownAllergySummaryDto {

  @JsonProperty("noKnownAllergyId")
  @Schema(description = "The unique id", example = "1")
  private int noKnownAllergyId;

  @JsonProperty("createdDate")
  @Schema(description = "The created date of the no known allergy", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime createdDate;

  @JsonProperty("modifiedDate")
  @Schema(description = "The modified date of the no known allergy", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime modifiedDate;

  @JsonProperty("providerId")
  @Schema(description = "Provider id who diagnoses the allergy to the patient", example = "1500")
  private Integer providerId;

  @JsonProperty("allergyType")
  @Schema(description = "The type of the allergy", example = "DRUG_INTOLERANCE")
  private AllergyType allergyType;

  /**
   * A noKnownAllergyId
   *
   * @documentationExample 12
   * @return noKnownAllergyId
   */
  public int getNoKnownAllergyId() {
    return noKnownAllergyId;
  }

  public void setNoKnownAllergyId(int noKnownAllergyId) {
    this.noKnownAllergyId = noKnownAllergyId;
  }

  /**
   * Created Date
   *
   * @return LocalDateTIme
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Modified Date
   *
   * @return LocalDateTIme
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * A providerId who diagnoses the allergy of patient
   *
   * @documentationExample 12
   * @return providerId
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * The type of allergy
   *
   * @documentationExample DRUG_INTOLERANCE
   * @return allergyType
   */
  public AllergyType getAllergyType() {
    return allergyType;
  }

  public void setAllergyType(AllergyType allergyType) {
    this.allergyType = allergyType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NoKnownAllergySummaryDto that = (NoKnownAllergySummaryDto) o;

    if (noKnownAllergyId != that.noKnownAllergyId) {
      return false;
    }
    if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
      return false;
    }
    if (modifiedDate != null ? !modifiedDate.equals(that.modifiedDate)
        : that.modifiedDate != null) {
      return false;
    }
    if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) {
      return false;
    }
    return allergyType == that.allergyType;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("NoKnownAllergySummaryDto{");
    sb.append("noKnownAllergyId=").append(noKnownAllergyId);
    sb.append(", createdDate=").append(createdDate);
    sb.append(", modifiedDate=").append(modifiedDate);
    sb.append(", providerId=").append(providerId);
    sb.append(", allergyType=").append(allergyType);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = noKnownAllergyId;
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (modifiedDate != null ? modifiedDate.hashCode() : 0);
    result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
    result = 31 * result + (allergyType != null ? allergyType.hashCode() : 0);
    return result;
  }
}
