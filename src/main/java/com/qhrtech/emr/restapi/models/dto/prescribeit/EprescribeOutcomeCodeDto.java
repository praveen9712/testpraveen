
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTime2Range;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

/**
 * The Eprescribe Outcome Code data transfer object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Eprescribe Outcome Code object.")
public class EprescribeOutcomeCodeDto {

  @JsonProperty("id")
  @Schema(description = "The Eprescribe Outcome Code id", example = "1")
  private int id;

  @NotBlank
  @JsonProperty("identifier")
  @Schema(description = "The identifier of Eprescribe Outcome Code",
      example = "PIT_RECIPIENT_ERROR")
  @Size(max = 255, message = "Maximum size allowed is 255 characters.")
  private String identifier;

  @JsonProperty("displayName")
  @Schema(description = "The display name", example = "Recipient Asynchronous Reject")
  @Size(max = 255, message = "Maximum size allowed is 255 characters.")
  private String displayName;

  @JsonProperty("code")
  @Schema(description = "The code", example = "990")
  @Size(max = 100, message = "Maximum size allowed is 100 characters.")
  private String code;

  @JsonProperty("outcomeTypeId")
  @Schema(description = "The Outcome Type id", example = "2")
  @NotNull
  private Integer outcomeTypeId;

  @JsonProperty("createdAt")
  @CheckLocalDateTime2Range
  @Schema(description = "The created datetime(read-only)", example = "2021-07-01T07:45:59.000")
  private LocalDateTime createdAt;

  /**
   * Eprescribe Outcome Code Id
   *
   * @documentationExample 1
   *
   * @return Eprescribe Outcome Code Id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Eprescribe Outcome Code identifier
   *
   * @documentationExample PIT_RECIPIENT_ERROR
   *
   * @return Eprescribe Outcome Code identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Eprescribe Outcome Code display name
   *
   * @documentationExample Recipient Asynchronous Reject
   *
   * @return Eprescribe Outcome Code display name
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Eprescribe Outcome Code
   *
   * @documentationExample 990
   *
   * @return Eprescribe Outcome Code
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Eprescribe Outcome Code type id
   *
   * @documentationExample 1
   *
   * @return Eprescribe Outcome Code type id
   */
  public Integer getOutcomeTypeId() {
    return outcomeTypeId;
  }

  public void setOutcomeTypeId(Integer outcomeTypeId) {
    this.outcomeTypeId = outcomeTypeId;
  }

  /**
   * Datetime when created
   *
   * @documentationExample 2021-07-01T07:45:59.000
   *
   * @return Datetime when created
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EprescribeOutcomeCodeDto that = (EprescribeOutcomeCodeDto) o;
    return getId() == that.getId()
        && Objects.equals(getOutcomeTypeId(), that.getOutcomeTypeId())
        && getIdentifier().equals(that.getIdentifier())
        && Objects.equals(getDisplayName(), that.getDisplayName())
        && Objects.equals(getCode(), that.getCode())
        && getCreatedAt().equals(that.getCreatedAt());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getIdentifier(), getDisplayName(), getCode(), getOutcomeTypeId(),
        getCreatedAt());
  }

  @Override
  public String toString() {
    return "EprescribeOutcomeCodeDto{"
        + "id=" + id
        + ", identifier='" + identifier + '\''
        + ", displayName='" + displayName + '\''
        + ", code='" + code + '\''
        + ", outcomeTypeId=" + outcomeTypeId
        + ", createdAt=" + createdAt
        + '}';
  }
}
