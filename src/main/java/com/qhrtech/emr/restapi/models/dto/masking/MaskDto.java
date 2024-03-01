
package com.qhrtech.emr.restapi.models.dto.masking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Mask data transfer object model.")
public class MaskDto implements Serializable {

  @NotNull
  @JsonProperty("maskId")
  @Schema(description = "The id of the mask", example = "1", type = "integer")
  private int maskId;

  @Schema(description = "User id of the creator of mask", example = "24", type = "integer")
  @JsonProperty("userId")
  private Integer userId;

  @Schema(description = "The status of the mask if it is active or not.", example = "true",
      type = "boolean")
  @JsonProperty("masked")
  private boolean masked;

  @JsonProperty("maskedDate")
  @Schema(description = "Date time when the resource was masked.",
      example = "2020-02-13T00:00:00.000")
  private LocalDateTime maskedDate;

  @JsonProperty("lastModified")
  @Schema(description = "Date time when the mask of last modified.",
      example = "2020-02-13T00:00:00.000")
  private LocalDateTime lastModified;

  @JsonProperty("notes")
  @Schema(description = "Mask notes", type = "string", maxLength = 2000)
  @Size(max = 2000, message = "Maximum size allowed is 2000 characters.")
  private String notes;

  @Schema(description = "Patient id.", example = "45", type = "integer")
  @JsonProperty("patientId")
  private int patientId;

  @Schema(description = "A field name on which mask is applied. Example: STREET1 refers to mask "
      + "on street field of primary address, STREET2 refers to mask on Street field of secondary "
      + "address and so on. Appointment masks have this field as blank.", example = "STREET1",
      type = "string", maxLength = 150)
  @JsonProperty("fieldName")
  @Size(max = 150, message = "Maximum size allowed is 150 characters.")
  private String fieldName;

  @Schema(description = "List of authorized users and roles to whom mask is accessible."
      + " This is read only field.")
  @JsonProperty("maskAuthorizations")
  private List<MaskAuthorizationDto> maskAuthorizations;

  /**
   * A unique Mask id
   *
   * @return mask id
   * @documentationExample 12
   */
  public int getMaskId() {
    return maskId;
  }

  public void setMaskId(int maskId) {
    this.maskId = maskId;
  }

  /**
   * A unique Accuro User ID
   *
   * @return User ID
   * @documentationExample 6
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  /**
   * Boolean which identifies if the resource is still masked or not.
   *
   * @return boolean
   * @documentationExample true
   */
  public boolean isMasked() {
    return masked;
  }

  public void setMasked(boolean masked) {
    this.masked = masked;
  }

  /**
   * Date time when the data was masked
   *
   * @return LocalDateTime
   * @documentationExample 2017-11-29T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getMaskedDate() {
    return maskedDate;
  }

  public void setMaskedDate(LocalDateTime maskedDate) {
    this.maskedDate = maskedDate;
  }

  /**
   * Date time when the mask was last modified
   *
   * @return LocalDateTime
   * @documentationExample 2017-11-29T00:00:00.000
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * A note related to mask
   *
   * @return String
   * @documentationExample This is test note
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * A unique Patient ID
   *
   * @return Patient ID
   * @documentationExample 12
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * A field name on which mask is applied. Example: STREET1 refers to mask on street field of
   * primary address, STREET2 refers to mask on Street field of secondary address and so on.
   * Appointment masks have this field as blank.
   *
   * @return String
   * @documentationExample STREET1
   */
  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * A list of all the mask authorization related to the given mask.
   *
   * @return list of {@link MaskAuthorizationDto}
   * @documentationExample list {@link MaskAuthorizationDto}
   */
  public List<MaskAuthorizationDto> getMaskAuthorizations() {
    return maskAuthorizations;
  }

  public void setMaskAuthorizations(
      List<MaskAuthorizationDto> maskAuthorizations) {
    this.maskAuthorizations = maskAuthorizations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MaskDto maskDto = (MaskDto) o;

    if (maskId != maskDto.maskId) {
      return false;
    }
    if (masked != maskDto.masked) {
      return false;
    }
    if (patientId != maskDto.patientId) {
      return false;
    }
    if (!Objects.equals(userId, maskDto.userId)) {
      return false;
    }
    if (!Objects.equals(maskedDate, maskDto.maskedDate)) {
      return false;
    }
    if (!Objects.equals(lastModified, maskDto.lastModified)) {
      return false;
    }
    if (!Objects.equals(notes, maskDto.notes)) {
      return false;
    }
    if (!Objects.equals(fieldName, maskDto.fieldName)) {
      return false;
    }
    return Objects.equals(maskAuthorizations, maskDto.maskAuthorizations);
  }

  @Override
  public int hashCode() {
    int result = maskId;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (masked ? 1 : 0);
    result = 31 * result + (maskedDate != null ? maskedDate.hashCode() : 0);
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + patientId;
    result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
    result = 31 * result + (maskAuthorizations != null ? maskAuthorizations.hashCode() : 0);
    return result;
  }
}
