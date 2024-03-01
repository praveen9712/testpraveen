
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "No known allergy data transfer object model")
public class NoKnownAllergyDto {

  @JsonProperty("noKnownAllergyId")
  @Schema(description = "The unique id", example = "1")
  private int noKnownAllergyId;

  @JsonProperty("patientId")
  @Schema(description = "The patient id", example = "1")
  private int patientId;

  @JsonProperty("createdDate")
  @Schema(description = "The created date of the no known allergy", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime createdDate;

  @JsonProperty("modifiedDate")
  @Schema(description = "The modified date of the no known allergy", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime modifiedDate;

  @JsonProperty("allergyType")
  @Schema(description = "The type of the allergy", example = "DRUG_INTOLERANCE")
  private AllergyType allergyType;

  @JsonProperty("enteredInError")
  @Schema(description = "Flag which indicates if entered in error", example = "true")
  private boolean enteredInError;

  @JsonProperty("active")
  @Schema(description = "Flag which indicates if the allergy is active", example = "true")
  private boolean active;

  @JsonProperty("maskId")
  @Schema(description = "Mask id", example = "2")
  private Integer maskId;

  @JsonProperty("providerId")
  @Schema(description = "Provider id who diagnoses the allergy to the patient", example = "1500")
  private Integer providerId;

  @JsonProperty("providerInformation")
  @Schema(description = "Information which provider can refer to",
      example = "Extra attention is needed")
  private String providerInformation;

  /**
   * The unique id for the no-known allergy.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getNoKnownAllergyId() {
    return noKnownAllergyId;
  }

  public void setNoKnownAllergyId(int noKnownAllergyId) {
    this.noKnownAllergyId = noKnownAllergyId;
  }

  /**
   * The patient id of the patient who has the no-known allergy.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The created date of the no-known allergy.
   *
   * @return
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * The modified date of the no-known allergy.
   *
   * @return
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /**
   * The type of the allergy.
   *
   * <p>
   * The types are as follow:
   *
   * <ul>
   * <li>DRUG_ALLERGY</li>
   * <li>NON_DRUG_ALLERGY</li>
   * <li>DRUG_INTOLERANCE</li>
   * <li>NON_DRUG_INTOLERANCE</li>
   * </ul>
   * </p>
   *
   * @documentationExample DRUG_INTOLERANCE
   *
   * @return
   */
  public AllergyType getAllergyType() {
    return allergyType;
  }

  public void setAllergyType(AllergyType allergyType) {
    this.allergyType = allergyType;
  }

  /**
   * A flag if entered in error.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isEnteredInError() {
    return enteredInError;
  }

  public void setEnteredInError(boolean enteredInError) {
    this.enteredInError = enteredInError;
  }

  /**
   * A flag if the no-known allergy is active.
   *
   * @documentationExample true
   *
   * @return
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * The mask id. If the no-known allergy id not masked this id will be {@code -1}.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getMaskId() {
    return maskId;
  }

  public void setMaskId(Integer maskId) {
    this.maskId = maskId;
  }

  /**
   * The provider id of the provider who diagnoses the allergy to a patient.
   *
   * @documentationExample 1
   *
   * @return
   */
  public Integer getProviderId() {
    return providerId;
  }

  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * The information which the provider can refer to.
   *
   * @documentationExample ATTENTION!!!
   *
   * @return
   */
  public String getProviderInformation() {
    return providerInformation;
  }

  public void setProviderInformation(String providerInformation) {
    this.providerInformation = providerInformation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NoKnownAllergyDto)) {
      return false;
    }

    NoKnownAllergyDto that = (NoKnownAllergyDto) o;

    if (getNoKnownAllergyId() != that.getNoKnownAllergyId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (isEnteredInError() != that.isEnteredInError()) {
      return false;
    }
    if (isActive() != that.isActive()) {
      return false;
    }
    if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate())
        : that.getCreatedDate() != null) {
      return false;
    }
    if (getModifiedDate() != null ? !getModifiedDate().equals(that.getModifiedDate())
        : that.getModifiedDate() != null) {
      return false;
    }
    if (getAllergyType() != that.getAllergyType()) {
      return false;
    }
    if (getMaskId() != null ? !getMaskId().equals(that.getMaskId()) : that.getMaskId() != null) {
      return false;
    }
    if (getProviderId() != null ? !getProviderId().equals(that.getProviderId())
        : that.getProviderId() != null) {
      return false;
    }
    return getProviderInformation() != null ? getProviderInformation()
        .equals(that.getProviderInformation()) : that.getProviderInformation() == null;
  }

  @Override
  public int hashCode() {
    int result = getNoKnownAllergyId();
    result = 31 * result + getPatientId();
    result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
    result = 31 * result + (getModifiedDate() != null ? getModifiedDate().hashCode() : 0);
    result = 31 * result + (getAllergyType() != null ? getAllergyType().hashCode() : 0);
    result = 31 * result + (isEnteredInError() ? 1 : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result = 31 * result + (getMaskId() != null ? getMaskId().hashCode() : 0);
    result = 31 * result + (getProviderId() != null ? getProviderId().hashCode() : 0);
    result =
        31 * result + (getProviderInformation() != null ? getProviderInformation().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "NoKnownAllergyDto{"
        + "noKnownAllergyId=" + noKnownAllergyId
        + ", patientId=" + patientId
        + ", createdDate=" + createdDate
        + ", modifiedDate=" + modifiedDate
        + ", allergyType=" + allergyType
        + ", enteredInError=" + enteredInError
        + ", active=" + active
        + ", maskId=" + maskId
        + ", providerId=" + providerId
        + ", providerInformation='" + providerInformation + '\''
        + '}';
  }
}
