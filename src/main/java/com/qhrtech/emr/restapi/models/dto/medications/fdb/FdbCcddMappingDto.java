
package com.qhrtech.emr.restapi.models.dto.medications.fdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@Schema(description = "The FDB CCDD mapping data transfer model")
public class FdbCcddMappingDto {

  @NotNull
  @Size(max = 20, message = "The FDB vocabulary ID cannot be over 20.")
  @JsonProperty("fdbVocabId")
  @Schema(description = "The FDB vocabulary ID", example = "1")
  private String fdbVocabId;

  @NotNull
  @JsonProperty("fdbVocabType")
  @Schema(description = "The FDB vocabulary type", example = "GenericCodeNumber")
  private FdbVocabularyType fdbVocabType;

  @NotNull
  @Size(max = 50, message = "The external vocabulary ID cannot be over 50.")
  @JsonProperty("externalVocabId")
  @Schema(description = "The external(CCDD) vocabulary ID", example = "1")
  private String externalVocabId;

  @NotNull
  @JsonProperty("externalVocabType")
  @Schema(description = "The external(CCDD) vocabulary type",
      example = "NonProprietaryTherapeuticProduct")
  private ExternalVocabularyType externalVocabType;

  @JsonProperty("fdbLinkActivateDateUtc")
  @Schema(description = "The activate date(UTC) for the FDB link",
      type = "string",
      example = "2000-05-31T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime fdbLinkActivateDateUtc;

  @JsonProperty("externalLinkPreferred")
  @Schema(description = "The flag if the external link is preferred", example = "true")
  private Boolean externalLinkPreferred;

  @JsonProperty("externalLinkRelated")
  @Schema(description = "The flag if the external link is related", example = "true")
  private Boolean externalLinkRelated;

  @JsonProperty("fdbLinkInactivateDateUtc")
  @Schema(description = "The inactivate date(UTC) for the FDB link",
      type = "string",
      example = "2000-05-31T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime fdbLinkInactivateDateUtc;

  /**
   * The FDB vocabulary ID
   * 
   * @documentationExample 1
   * 
   * @return The FDB vocabulary ID
   */
  public String getFdbVocabId() {
    return fdbVocabId;
  }

  public void setFdbVocabId(String fdbVocabId) {
    this.fdbVocabId = fdbVocabId;
  }


  /**
   * The FDB vocabulary type
   * 
   * @documentationExample GenericCodeNumber
   * 
   * @return The FDB vocabulary type
   */
  public FdbVocabularyType getFdbVocabType() {
    return fdbVocabType;
  }

  public void setFdbVocabType(FdbVocabularyType fdbVocabType) {
    this.fdbVocabType = fdbVocabType;
  }

  /**
   * The external(CCDD) vocabulary ID
   * 
   * @documentationExample 1
   * 
   * @return The external(CCDD) vocabulary ID
   */
  public String getExternalVocabId() {
    return externalVocabId;
  }

  public void setExternalVocabId(String externalVocabId) {
    this.externalVocabId = externalVocabId;
  }

  /**
   * The external(CCDD) vocabulary type
   * 
   * @documentationExample NonProprietaryTherapeuticProduct
   * 
   * @return The external(CCDD) vocabulary type
   */
  public ExternalVocabularyType getExternalVocabType() {
    return externalVocabType;
  }

  public void setExternalVocabType(ExternalVocabularyType externalVocabType) {
    this.externalVocabType = externalVocabType;
  }

  /**
   * The activate date(UTC) for the FDB link
   * 
   * @documentationExample 2000-05-31T00:00:00.000
   * 
   * @return The activate date(UTC) for the FDB link
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getFdbLinkActivateDateUtc() {
    return fdbLinkActivateDateUtc;
  }

  public void setFdbLinkActivateDateUtc(LocalDateTime fdbLinkActivateDateUtc) {
    this.fdbLinkActivateDateUtc = fdbLinkActivateDateUtc;
  }

  /**
   * The flag if the external link is preferred
   * 
   * @documentationExample true
   * 
   * @return The flag if the external link is preferred
   */
  public Boolean getExternalLinkPreferred() {
    return externalLinkPreferred;
  }

  public void setExternalLinkPreferred(Boolean externalLinkPreferred) {
    this.externalLinkPreferred = externalLinkPreferred;
  }

  /**
   * The flag if the external link is related
   *
   * @documentationExample true
   *
   * @return The flag if the external link is related
   */
  public Boolean getExternalLinkRelated() {
    return externalLinkRelated;
  }

  public void setExternalLinkRelated(Boolean externalLinkRelated) {
    this.externalLinkRelated = externalLinkRelated;
  }

  /**
   * The inactivate date(UTC) for the FDB link
   * 
   * @documentationExample 2000-05-31T00:00:00.000
   * 
   * @return The inactivate date(UTC) for the FDB link
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getFdbLinkInactivateDateUtc() {
    return fdbLinkInactivateDateUtc;
  }

  public void setFdbLinkInactivateDateUtc(LocalDateTime fdbLinkInactivateDateUtc) {
    this.fdbLinkInactivateDateUtc = fdbLinkInactivateDateUtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FdbCcddMappingDto that = (FdbCcddMappingDto) o;

    if (!Objects.equals(fdbVocabId, that.fdbVocabId)) {
      return false;
    }
    if (!Objects.equals(fdbVocabType, that.fdbVocabType)) {
      return false;
    }
    if (!Objects.equals(externalVocabId, that.externalVocabId)) {
      return false;
    }
    if (!Objects.equals(externalVocabType, that.externalVocabType)) {
      return false;
    }
    if (!Objects.equals(fdbLinkActivateDateUtc, that.fdbLinkActivateDateUtc)) {
      return false;
    }
    if (!Objects.equals(externalLinkPreferred, that.externalLinkPreferred)) {
      return false;
    }
    if (!Objects.equals(externalLinkRelated, that.externalLinkRelated)) {
      return false;
    }
    return Objects.equals(fdbLinkInactivateDateUtc, that.fdbLinkInactivateDateUtc);
  }

  @Override
  public int hashCode() {
    int result = fdbVocabId != null ? fdbVocabId.hashCode() : 0;
    result = 31 * result + (fdbVocabType != null ? fdbVocabType.hashCode() : 0);
    result = 31 * result + (externalVocabId != null ? externalVocabId.hashCode() : 0);
    result = 31 * result + (externalVocabType != null ? externalVocabType.hashCode() : 0);
    result = 31 * result + (fdbLinkActivateDateUtc != null ? fdbLinkActivateDateUtc.hashCode() : 0);
    result = 31 * result + (externalLinkPreferred != null ? externalLinkPreferred.hashCode() : 0);
    result = 31 * result + (externalLinkRelated != null ? externalLinkRelated.hashCode() : 0);
    result =
        31 * result + (fdbLinkInactivateDateUtc != null ? fdbLinkInactivateDateUtc.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("FdbCcddMappingDto{");
    sb.append("fdbVocabId='").append(fdbVocabId).append('\'');
    sb.append(", fdbVocabType='").append(fdbVocabType).append('\'');
    sb.append(", externalVocabId='").append(externalVocabId).append('\'');
    sb.append(", externalVocabType='").append(externalVocabType).append('\'');
    sb.append(", fdbLinkActivateDateUtc=").append(fdbLinkActivateDateUtc);
    sb.append(", externalLinkPreferred=").append(externalLinkPreferred);
    sb.append(", externalLinkRelated=").append(externalLinkRelated);
    sb.append(", fdbLinkInactivateDateUtc=").append(fdbLinkInactivateDateUtc);
    sb.append('}');
    return sb.toString();
  }
}
