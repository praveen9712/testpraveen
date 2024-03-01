
package com.qhrtech.emr.restapi.models.dto.medicalsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.prescriptions.DosageDto;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class PrescriptionDto {

  @JsonProperty("prescriptionId")
  @Schema(description = "The unique prescription id.", example = "1")
  private int prescriptionId;

  @JsonProperty("medicationDisplayText")
  @Schema(description = "Displays top-level summary for the prescription.",
      example = "BENZEL SPOT-ON ACNE 2.5% GEL")
  private String medicationDisplayText;

  @JsonProperty("instructionsDisplayText")
  @Schema(description = "Displays top-level instruction for dosage.",
      example = "1 Application(s) Two times daily * 1 Mth30 with 1 refills.")
  private String instructionsDisplayText;

  @JsonProperty("prescribingProvider")
  @Schema(description = "The provider id of the provider prescribing.", example = "1")
  private Integer prescribingProvider;

  @JsonProperty("authorizingProvider")
  @Schema(description = "The authorizing provider id.")
  private Integer authorizingProvider;

  @JsonProperty("form")
  @Schema(description = "The dosage form", example = "TAB")
  private String dosageForm;

  @JsonProperty("external")
  @Schema(description = "The indication if the prescription is an external prescription",
      example = "true")
  private boolean externalRx; // A Rx from another provider

  @JsonProperty("sigInstructions")
  @Schema(description = "The sig instructions which is labeled in prescription",
      example = "TAKE 1 PILL, BY MOUTH, AT BEDTIME")
  private String sigInstructions; // Patient Instructions

  @JsonProperty("route")
  @Schema(description = "The route to take the medication", example = "Oral")
  private String route;

  @JsonProperty("status")
  @Schema(
      description = "Status of the prescription",
      example = "Recently Active",
      allowableValues = {"Active", "Recently Active", "Inactive"})
  private String medStatus; // Active, Recently Active, Inactive

  @JsonProperty("typeOfUse")
  @Schema(description = "The drug use on Accuro", example = "One Time")
  private String drugUse;

  @JsonProperty("note")
  @Schema(description = "The note", example = "TAKE 2 PILLS AT ONCE")
  private String note; // comments

  @JsonProperty("writtenDate")
  @Schema(description = "The written date of the prescription", example = "2017/11/29T00:00:00",
      type = "string")
  private LocalDateTime writtenDate;

  @JsonProperty("expiryDate")
  @Schema(description = "The expired date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate expiryDate;
  @JsonProperty("effectiveDate")
  @Schema(description = "The effective date of the prescription", type = "string",
      example = "2017-11-29")
  private LocalDate effectiveDate;

  @JsonProperty("createdDate")
  @Schema(description = "The created date of the prescription", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime createdDate;

  @JsonProperty("lastModified")
  @Schema(description = "The last modified date of the prescription", type = "string",
      example = "2017-11-29T00:00:00.000")
  private LocalDateTime lastModified;

  @JsonProperty("medicationDetails")
  @Schema(description = "Medication details.")
  private MedicationDetails medicationDetails;

  @JsonProperty("dosages")
  @Schema(description = "A set of dosages")
  private List<DosageDto> dosages;

  /**
   * A unique Prescription ID.
   *
   * @return Prescription ID
   * @documentationExample 12
   */
  public int getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(int prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * The Displayed top level summary of the Prescription
   *
   * @return The medicationDisplayText
   * @documentationExample BENZEL SPOT-ON ACNE 2.5% GEL
   */
  public String getMedicationDisplayText() {
    return medicationDisplayText;
  }

  public void setMedicationDisplayText(String medicationDisplayText) {
    this.medicationDisplayText = medicationDisplayText;
  }

  /**
   * Instructions Display Text.
   *
   * @return Instructions Display Text
   * @documentationExample {@link String} instructions display text
   */
  public String getInstructionsDisplayText() {
    return instructionsDisplayText;
  }

  public void setInstructionsDisplayText(String instructionsDisplayText) {
    this.instructionsDisplayText = instructionsDisplayText;
  }

  /**
   * Prescribing Provider ID.
   *
   * @return {@link Integer} Prescribing Provider
   * @documentationExample 12
   */
  public Integer getPrescribingProvider() {
    return prescribingProvider;
  }

  public void setPrescribingProvider(Integer prescribingProvider) {
    this.prescribingProvider = prescribingProvider;
  }

  /**
   * The Authorizingprovider id
   *
   * @return The authorizingProviderId
   * @documentationExample 68520
   */
  public Integer getAuthorizingProvider() {
    return authorizingProvider;
  }

  public void setAuthorizingProvider(Integer authorizingProvider) {
    this.authorizingProvider = authorizingProvider;
  }

  /**
   * The Dosage Form
   *
   * @return The dosageForm
   * @documentationExample TAB
   */
  public String getDosageForm() {
    return dosageForm;
  }

  public void setDosageForm(String dosageForm) {
    this.dosageForm = dosageForm;
  }

  /**
   * The indication if the Prescription is from External Provider
   *
   * @return {@code true} or {@code false}
   * @documentationExample true
   */
  public boolean isExternalRx() {
    return externalRx;
  }

  public void setExternalRx(boolean externalRx) {
    this.externalRx = externalRx;
  }

  /**
   * The SigInstructions labelled on the Prescription
   *
   * @return The sigInstructions
   * @documentationExample TAKE 1 PILL, BY MOUTH, AT BEDTIME
   */
  public String getSigInstructions() {
    return sigInstructions;
  }

  public void setSigInstructions(String sigInstructions) {
    this.sigInstructions = sigInstructions;
  }

  /**
   * The Route to take the Medication
   *
   * @return the route
   * @documentationExample oral
   */
  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  /**
   * The Status of the Prescription
   *
   * @return medStatus
   * @documentationExample RecentlyActive
   */
  public String getMedStatus() {
    return medStatus;
  }

  public void setMedStatus(String medStatus) {
    this.medStatus = medStatus;
  }

  /**
   * The Type of use
   *
   * @return drugUse
   * @documentationExample One Time
   */
  public String getDrugUse() {
    return drugUse;
  }

  public void setDrugUse(String drugUse) {
    this.drugUse = drugUse;
  }

  /**
   * The Notes
   *
   * @return A Note
   * @documentationExample the notes
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * The Written Date of Prescription
   *
   * @return The Datetime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getWrittenDate() {
    return writtenDate;
  }

  public void setWrittenDate(LocalDateTime writtenDate) {
    this.writtenDate = writtenDate;
  }

  /**
   * The Expired date of Prescription
   *
   * @return The Date
   */
  @DocumentationExample("2017-11-08")
  @TypeHint(String.class)
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * The Effective date of Prescription
   *
   * @return The Date
   */
  @DocumentationExample("2017-11-08")
  @TypeHint(String.class)
  public LocalDate getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDate effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  /**
   * The Created Date of Prescription
   *
   * @return Datetime
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
   * The Last Updated date of Prescription
   *
   * @return The Datetime
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
   * The Medication details
   *
   * documentationExample {@link MedicationDetails}
   *
   * @return {@link MedicationDetails}
   */
  public MedicationDetails getMedicationDetails() {
    return medicationDetails;
  }

  public void setMedicationDetails(
      MedicationDetails medicationDetails) {
    this.medicationDetails = medicationDetails;
  }

  /**
   * The Collection of Dosages
   *
   * @return list of {@link DosageDto}
   * @documentationExample {@link DosageDto}
   */
  public List<DosageDto> getDosages() {
    return dosages;
  }

  public void setDosages(List<DosageDto> dosages) {
    this.dosages = dosages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PrescriptionDto that = (PrescriptionDto) o;

    if (prescriptionId != that.prescriptionId) {
      return false;
    }
    if (externalRx != that.externalRx) {
      return false;
    }
    if (!Objects.equals(medicationDisplayText, that.medicationDisplayText)) {
      return false;
    }
    if (!Objects.equals(instructionsDisplayText, that.instructionsDisplayText)) {
      return false;
    }
    if (!Objects.equals(prescribingProvider, that.prescribingProvider)) {
      return false;
    }
    if (!Objects.equals(authorizingProvider, that.authorizingProvider)) {
      return false;
    }
    if (!Objects.equals(dosageForm, that.dosageForm)) {
      return false;
    }
    if (!Objects.equals(sigInstructions, that.sigInstructions)) {
      return false;
    }
    if (!Objects.equals(route, that.route)) {
      return false;
    }
    if (!Objects.equals(medStatus, that.medStatus)) {
      return false;
    }
    if (!Objects.equals(drugUse, that.drugUse)) {
      return false;
    }
    if (!Objects.equals(note, that.note)) {
      return false;
    }
    if (!Objects.equals(writtenDate, that.writtenDate)) {
      return false;
    }
    if (!Objects.equals(expiryDate, that.expiryDate)) {
      return false;
    }
    if (!Objects.equals(effectiveDate, that.effectiveDate)) {
      return false;
    }
    if (!Objects.equals(createdDate, that.createdDate)) {
      return false;
    }
    if (!Objects.equals(lastModified, that.lastModified)) {
      return false;
    }
    if (!Objects.equals(medicationDetails, that.medicationDetails)) {
      return false;
    }
    return Objects.equals(dosages, that.dosages);
  }

  @Override
  public int hashCode() {
    int result = prescriptionId;
    result = 31 * result + (medicationDisplayText != null ? medicationDisplayText.hashCode() : 0);
    result =
        31 * result + (instructionsDisplayText != null ? instructionsDisplayText.hashCode() : 0);
    result = 31 * result + (prescribingProvider != null ? prescribingProvider.hashCode() : 0);
    result = 31 * result + (authorizingProvider != null ? authorizingProvider.hashCode() : 0);
    result = 31 * result + (dosageForm != null ? dosageForm.hashCode() : 0);
    result = 31 * result + (externalRx ? 1 : 0);
    result = 31 * result + (sigInstructions != null ? sigInstructions.hashCode() : 0);
    result = 31 * result + (route != null ? route.hashCode() : 0);
    result = 31 * result + (medStatus != null ? medStatus.hashCode() : 0);
    result = 31 * result + (drugUse != null ? drugUse.hashCode() : 0);
    result = 31 * result + (note != null ? note.hashCode() : 0);
    result = 31 * result + (writtenDate != null ? writtenDate.hashCode() : 0);
    result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
    result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
    result = 31 * result + (medicationDetails != null ? medicationDetails.hashCode() : 0);
    result = 31 * result + (dosages != null ? dosages.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("PrescriptionDto{");
    sb.append("prescriptionId=").append(prescriptionId);
    sb.append(", medicationDisplayText='").append(medicationDisplayText).append('\'');
    sb.append(", instructionsDisplayText='").append(instructionsDisplayText).append('\'');
    sb.append(", prescribingProvider=").append(prescribingProvider);
    sb.append(", authorizingProvider=").append(authorizingProvider);
    sb.append(", dosageForm='").append(dosageForm).append('\'');
    sb.append(", externalRx=").append(externalRx);
    sb.append(", sigInstructions='").append(sigInstructions).append('\'');
    sb.append(", route='").append(route).append('\'');
    sb.append(", medStatus='").append(medStatus).append('\'');
    sb.append(", drugUse='").append(drugUse).append('\'');
    sb.append(", note='").append(note).append('\'');
    sb.append(", writtenDate=").append(writtenDate);
    sb.append(", expiryDate=").append(expiryDate);
    sb.append(", effectiveDate=").append(effectiveDate);
    sb.append(", createdDate=").append(createdDate);
    sb.append(", lastModified=").append(lastModified);
    sb.append(", medicationDetails=").append(medicationDetails);
    sb.append(", dosages=").append(dosages);
    sb.append('}');
    return sb.toString();
  }
}
