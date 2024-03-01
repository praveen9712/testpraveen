
package com.qhrtech.emr.restapi.models.dto.patientv2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.qhrtech.emr.restapi.config.serialization.JodaLocalDateDeserializer;
import com.qhrtech.emr.restapi.models.dto.ProviderEnrollmentTerminationReason;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * The Patient model object
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient V2 object", implementation = PatientDto.class, name = "PatientV2Dto")
public class PatientDto {

  @JsonProperty("enrolledProviderTerminationReason")
  @Schema(description = "The termination reason of the patient's enrolled provider")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  ProviderEnrollmentTerminationReason enrolledProviderTerminationReason;
  @JsonProperty("patientId")
  @Schema(description = "Unique id of a patient", example = "18911")
  private int patientId;
  @JsonProperty("demographics")
  @Schema(description = "Demographics objects of a patient", required = true)
  @Valid
  private DemographicsDto demographics;
  @JsonProperty("familyProviderId")
  @Schema(description = "The id of the patients family provider", example = "1",
      maximum = "999999999", nullable = true)
  private Integer familyProviderId;
  @JsonProperty("enrolledProviderId")
  @Schema(description = "The id of the patients enrolled provider", example = "1",
      maximum = "999999999", nullable = true)
  private Integer enrolledProviderId;
  @JsonProperty("officeProviderId")
  @Schema(description = "The id of the patients office provider", example = "2",
      maximum = "999999999", nullable = true)
  private Integer officeProviderId;

  @JsonProperty("referringProviderId")
  @Schema(description = "The id of the patients referring provider", example = "1",
      maximum = "999999999", nullable = true)
  private Integer referringProviderId;

  @JsonProperty("insurerId")
  @Schema(description = "The id for the patients insurer", example = "2", required = true,
      maximum = "999999999")
  private Integer insurerId;

  @JsonProperty("fileNumber")
  @Schema(description = " The patients file number", example = "22-12345", type = "string",
      maxLength = 30, nullable = true)
  @Size(max = 30, message = "FileNumber size should not be greater than 30 characters")
  private String fileNumber;

  @JsonProperty("uuid")
  @Schema(description = "The mapped uuid for the patient id",
      example = "dca20596-e35e-325f-56a4-3190281a020f", accessMode = AccessMode.READ_ONLY)
  private String uuid;

  @JsonProperty("registrationNumber")
  @Schema(description = "The patient registration number", type = "string", example = "123",
      maxLength = 20, nullable = true)
  @Size(max = 20, message = "Registration number size should not be greater than 20 characters")
  private String registrationNumber;

  @JsonProperty("paperChartNote")
  @Schema(description = "A note for the patients paper chart", example = " A note", type = "string",
      maxLength = 255, nullable = true)
  @Size(max = 255, message = "Paper chart note size should not be greater than 255 characters")
  private String paperChartNote;

  @JsonProperty("paperChart")
  @Schema(description = "Indication if the patient has a paper chart", example = "false")
  private boolean paperChart;

  @JsonProperty("patientStatusId")
  @Schema(description = "The patients status id", example = "1")
  @NotNull(message = "The patients status id")
  private Integer patientStatusId;

  @JsonProperty("gestationAge")
  @Schema(description = "The patients gestation age", example = "2017-11-08")
  @CheckLocalDateRange
  private LocalDate gestationAge;

  @JsonProperty("employerContactId")
  @Schema(description = "The employer contact id", example = "1", maximum = "999999999",
      nullable = true)
  private Integer employerContactId;

  @JsonProperty("pharmacyContactId")
  @Schema(description = "The pharmacy contact id", example = "1", maximum = "999999999",
      nullable = true)
  private Integer pharmacyContactId;

  @JsonProperty("referredDate")
  @Schema(description = "Date of being referred", example = "2020-09-10")
  @CheckLocalDateRange
  private LocalDate referredDate;

  @JsonProperty("onSocialAssistance")
  @Schema(description = "Indication if the patient is on social assistance", example = "false")
  private boolean onSocialAssistance;

  @JsonProperty("hasArchivedRecords")
  @Schema(description = "Indication if the patient has archived records", example = "false")
  private boolean hasArchivedRecords;

  @JsonProperty("deceased")
  @Schema(description = "Indication if the patient is deceased", example = "false")
  private boolean deceased;

  @JsonProperty("deceasedDate")
  @Schema(description = "Patient deceased date", example = "2017-11-08T00:00:00.000")
  @CheckLocalDateTimeRange
  private LocalDateTime deceasedDate;

  @JsonProperty("occupation")
  @Schema(description = "Patient occupation", example = "Lawyer", nullable = true, maxLength = 50)
  @Size(max = 50, message = "The occupation  size should not be greater than 50 characters")
  private String occupation;

  @JsonProperty("alert")
  @Schema(description = "Patient alert flag", nullable = true)
  @Valid
  private PatientFlagDto alert;

  @JsonProperty("albertaDetails")
  @Schema(description = "Alberta specific details. "
      + "If the current province is Alberta it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private AlbertaDetailsDto albertaDetails;

  @JsonProperty("ontarioDetails")
  @Schema(description = "Ontario specific details."
      + "If the current province is Ontario it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private OntarioDetailsDto ontarioDetails;

  @JsonProperty("novaScotiaDetails")
  @Schema(description = "Nova Scotia specific details"
      + "If the current province is Nova Scotia it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private NovaScotiaDetailsDto novaScotiaDetails;

  @JsonProperty("manitobaDetails")
  @Schema(description = "Manitoba specific details"
      + "If the current province is Manitoba it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private ManitobaDetailsDto manitobaDetails;


  /**
   * A unique Patient ID.
   *
   * @return Patient ID
   * @documentationExample 1
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * A Demographic DTO for the Patient
   *
   * @return A Demographic DTO
   */
  public DemographicsDto getDemographics() {
    return demographics;
  }

  public void setDemographics(DemographicsDto demographics) {
    this.demographics = demographics;
  }

  /**
   * The ID of the patients family Provider.
   *
   * @return Provider ID
   * @documentationExample 1
   */
  public Integer getFamilyProviderId() {
    return familyProviderId;
  }

  public void setFamilyProviderId(Integer familyProviderId) {
    this.familyProviderId = familyProviderId;
  }

  /**
   * The ID of the patients enrolled Provider.
   * <p>
   * While updating enrolledProviderId field, do consider these points:
   * <ul>
   * <li>While updating enrolledProviderId from null to a particular value,
   * enrolledProvideTerminationReason must be null.</li>
   * <li>While updating enrolledProviderId from particular value to null,
   * enrolledProvideTerminationReason must also be provided.</li>
   * <li>While updating enrolledProviderId from particular value to other value,
   * enrolledProvideTerminationReason must also be provided.</li>
   * <li>While there is no change in the enrolledProviderId field , enrolledProvideTerminationReason
   * must be null.</li>
   * </ul>
   *
   * <p/>
   *
   * @return Provider ID
   * @documentationExample 1
   */
  public Integer getEnrolledProviderId() {
    return enrolledProviderId;
  }

  public void setEnrolledProviderId(Integer enrolledProviderId) {
    this.enrolledProviderId = enrolledProviderId;
  }


  /**
   * Termination reason of the enrolled provider. This field is enum and should have values which
   * are part of {@link ProviderEnrollmentTerminationReason}. This field is relevant only during
   * update patient request. During GET patient requests, this field remains null.
   *
   * @return Termination Reason of the enrolled provider.
   * @documentationExample PATIENT_MOVED
   * @see ProviderEnrollmentTerminationReason
   */
  public ProviderEnrollmentTerminationReason getEnrolledProvideTerminationReason() {
    return enrolledProviderTerminationReason;
  }

  public void setEnrolledProvideTerminationReason(
      ProviderEnrollmentTerminationReason enrolledProvideTerminationReason) {
    this.enrolledProviderTerminationReason = enrolledProvideTerminationReason;
  }

  /**
   * The ID of the patients office Provider
   *
   * @return Provider ID
   * @documentationExample 1
   */
  public Integer getOfficeProviderId() {
    return officeProviderId;
  }

  public void setOfficeProviderId(Integer officeProviderId) {
    this.officeProviderId = officeProviderId;
  }

  /**
   * The ID of the patients referring Provider.
   *
   * @return Provider ID
   * @documentationExample 1
   */
  public Integer getReferringProviderId() {
    return referringProviderId;
  }

  public void setReferringProviderId(Integer referringProviderId) {
    this.referringProviderId = referringProviderId;
  }

  /**
   * The ID for the patients Insurer.
   *
   * @return Insurer ID
   * @documentationExample 1
   */
  public Integer getInsurerId() {
    return insurerId;
  }

  public void setInsurerId(Integer insurerId) {
    this.insurerId = insurerId;
  }

  /**
   * The patients File Number
   *
   * @return The patient file number
   * @documentationExample 22-12345
   */
  public String getFileNumber() {
    return fileNumber;
  }

  public void setFileNumber(String fileNumber) {
    this.fileNumber = fileNumber;
  }

  /**
   * The patients UUID Number
   *
   * @return The patient UUID number
   * @documentationExample dca20596-e35e-325f-56a4-3190281a020f
   */
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * The patient Registration Number
   *
   * @return A registration number
   * @documentationExample 123
   */
  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  /**
   * A note for the patients paper chart.
   *
   * @return Paper chart note
   * @documentationExample A note
   */
  public String getPaperChartNote() {
    return paperChartNote;
  }

  public void setPaperChartNote(String paperChartNote) {
    this.paperChartNote = paperChartNote;
  }

  /**
   * Indication if the patient has a paper chart.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isPaperChart() {
    return paperChart;
  }

  public void setPaperChart(boolean paperChart) {
    this.paperChart = paperChart;
  }

  /**
   * The patients Status ID
   *
   * @return Patient status ID
   * @documentationExample 1
   */
  public Integer getPatientStatusId() {
    return patientStatusId;
  }

  public void setPatientStatusId(Integer patientStatusId) {
    this.patientStatusId = patientStatusId;
  }

  /**
   * The patients gestation age
   *
   * @return Gestation age
   */
  @DocumentationExample("2017-11-08")
  @TypeHint(String.class)
  @JsonDeserialize(using = JodaLocalDateDeserializer.class)
  public LocalDate getGestationAge() {
    return gestationAge;
  }

  public void setGestationAge(LocalDate gestationAge) {
    this.gestationAge = gestationAge;
  }

  /**
   * The employer contact ID
   *
   * @return Employer contact ID
   * @documentationExample 1
   */
  public Integer getEmployerContactId() {
    return employerContactId;
  }

  public void setEmployerContactId(Integer employerContactId) {
    this.employerContactId = employerContactId;
  }

  /**
   * The pharmacy contact ID
   *
   * @return Pharmacy contact ID
   * @documentationExample 1
   */
  public Integer getPharmacyContactId() {
    return pharmacyContactId;
  }

  public void setPharmacyContactId(Integer pharmacyContactId) {
    this.pharmacyContactId = pharmacyContactId;
  }

  /**
   * Patient referred date
   *
   * @return Referred date
   */
  @DocumentationExample("2017-11-08")
  @TypeHint(String.class)
  public LocalDate getReferredDate() {
    return referredDate;
  }

  public void setReferredDate(LocalDate referredDate) {
    this.referredDate = referredDate;
  }

  /**
   * Indication if the patient is on social assistance.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isOnSocialAssistance() {
    return onSocialAssistance;
  }

  public void setOnSocialAssistance(boolean onSocialAssistance) {
    this.onSocialAssistance = onSocialAssistance;
  }

  /**
   * Indication if the patient has archived records.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isHasArchivedRecords() {
    return hasArchivedRecords;
  }

  public void setHasArchivedRecords(boolean hasArchivedRecords) {
    this.hasArchivedRecords = hasArchivedRecords;
  }

  /**
   * Indication if the patient is deceased.
   *
   * @return <code>true</code> or <code>false</code>
   * @documentationExample true
   */
  public boolean isDeceased() {
    return deceased;
  }

  public void setDeceased(boolean deceased) {
    this.deceased = deceased;
  }

  /**
   * Patient deceased date.
   *
   * @return Patient deceased date
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDeceasedDate() {
    return deceasedDate;
  }

  public void setDeceasedDate(LocalDateTime deceasedDate) {
    this.deceasedDate = deceasedDate;
  }

  /**
   * Patient occupation
   *
   * @return Occupation
   * @documentationExample Nurse
   */
  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Patient alert flag
   *
   * @return Patient alert
   */
  public PatientFlagDto getAlert() {
    return alert;
  }

  public void setAlert(PatientFlagDto alert) {
    this.alert = alert;
  }

  /**
   * Alberta specific details
   *
   * @return Alberta Details DTO
   */
  public AlbertaDetailsDto getAlbertaDetails() {
    return albertaDetails;
  }

  public void setAlbertaDetails(AlbertaDetailsDto albertaDetails) {
    this.albertaDetails = albertaDetails;
  }

  /**
   * Ontario specific details
   *
   * @return Ontario Details DTO
   */
  public OntarioDetailsDto getOntarioDetails() {
    return ontarioDetails;
  }

  public void setOntarioDetails(OntarioDetailsDto ontarioDetails) {
    this.ontarioDetails = ontarioDetails;
  }

  /**
   * Nova Scotia specific details
   *
   * @return Nova Scotia Details DTO
   */
  public NovaScotiaDetailsDto getNovaScotiaDetails() {
    return novaScotiaDetails;
  }

  public void setNovaScotiaDetails(NovaScotiaDetailsDto novaScotiaDetails) {
    this.novaScotiaDetails = novaScotiaDetails;
  }

  /**
   * Manitoba specific details
   *
   * @return Manitoba Details DTO
   */
  public ManitobaDetailsDto getManitobaDetails() {
    return manitobaDetails;
  }

  public void setManitobaDetails(ManitobaDetailsDto manitobaDetails) {
    this.manitobaDetails = manitobaDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientDto that = (PatientDto) o;

    if (patientId != that.patientId) {
      return false;
    }
    if (paperChart != that.paperChart) {
      return false;
    }
    if (onSocialAssistance != that.onSocialAssistance) {
      return false;
    }
    if (hasArchivedRecords != that.hasArchivedRecords) {
      return false;
    }
    if (deceased != that.deceased) {
      return false;
    }
    if (demographics != null ? !demographics.equals(that.demographics)
        : that.demographics != null) {
      return false;
    }
    if (familyProviderId != null ? !familyProviderId
        .equals(that.familyProviderId)
        : that.familyProviderId != null) {
      return false;
    }
    if (enrolledProviderId != null ? !enrolledProviderId
        .equals(that.enrolledProviderId)
        : that.enrolledProviderId != null) {
      return false;
    }
    if (enrolledProviderTerminationReason != that.enrolledProviderTerminationReason) {
      return false;
    }
    if (officeProviderId != null ? !officeProviderId
        .equals(that.officeProviderId)
        : that.officeProviderId != null) {
      return false;
    }
    if (referringProviderId != null ? !referringProviderId
        .equals(
            that.referringProviderId)
        : that.referringProviderId != null) {
      return false;
    }
    if (insurerId != null ? !insurerId.equals(that.insurerId) : that.insurerId != null) {
      return false;
    }
    if (fileNumber != null ? !fileNumber.equals(that.fileNumber) : that.fileNumber != null) {
      return false;
    }
    if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) {
      return false;
    }
    if (registrationNumber != null ? !registrationNumber
        .equals(that.registrationNumber)
        : that.registrationNumber != null) {
      return false;
    }
    if (paperChartNote != null ? !paperChartNote.equals(that.paperChartNote)
        : that.paperChartNote != null) {
      return false;
    }
    if (patientStatusId != null ? !patientStatusId.equals(that.patientStatusId)
        : that.patientStatusId != null) {
      return false;
    }
    if (gestationAge != null ? !gestationAge.equals(that.gestationAge)
        : that.gestationAge != null) {
      return false;
    }
    if (employerContactId != null ? !employerContactId
        .equals(that.employerContactId)
        : that.employerContactId != null) {
      return false;
    }
    if (pharmacyContactId != null ? !pharmacyContactId
        .equals(that.pharmacyContactId)
        : that.pharmacyContactId != null) {
      return false;
    }
    if (referredDate != null ? !referredDate.equals(that.referredDate)
        : that.referredDate != null) {
      return false;
    }
    if (deceasedDate != null ? !deceasedDate.equals(that.deceasedDate)
        : that.deceasedDate != null) {
      return false;
    }
    if (occupation != null ? !occupation.equals(that.occupation) : that.occupation != null) {
      return false;
    }
    if (alert != null ? !alert.equals(that.alert) : that.alert != null) {
      return false;
    }
    if (albertaDetails != null ? !albertaDetails.equals(that.albertaDetails)
        : that.albertaDetails != null) {
      return false;
    }
    if (ontarioDetails != null ? !ontarioDetails.equals(that.ontarioDetails)
        : that.ontarioDetails != null) {
      return false;
    }
    if (novaScotiaDetails != null ? !novaScotiaDetails
        .equals(that.novaScotiaDetails)
        : that.novaScotiaDetails != null) {
      return false;
    }
    return manitobaDetails != null ? manitobaDetails.equals(that.manitobaDetails)
        : that.manitobaDetails == null;
  }

  @Override
  public int hashCode() {
    int result = patientId;
    result = 31 * result + (demographics != null ? demographics.hashCode() : 0);
    result = 31 * result + (familyProviderId != null ? familyProviderId.hashCode() : 0);
    result = 31 * result + (enrolledProviderId != null ? enrolledProviderId.hashCode() : 0);
    result =
        31 * result + (enrolledProviderTerminationReason != null ? enrolledProviderTerminationReason
            .hashCode() : 0);
    result = 31 * result + (officeProviderId != null ? officeProviderId.hashCode() : 0);
    result = 31 * result + (referringProviderId != null ? referringProviderId.hashCode() : 0);
    result = 31 * result + (insurerId != null ? insurerId.hashCode() : 0);
    result = 31 * result + (fileNumber != null ? fileNumber.hashCode() : 0);
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
    result = 31 * result + (paperChartNote != null ? paperChartNote.hashCode() : 0);
    result = 31 * result + (paperChart ? 1 : 0);
    result = 31 * result + (patientStatusId != null ? patientStatusId.hashCode() : 0);
    result = 31 * result + (gestationAge != null ? gestationAge.hashCode() : 0);
    result = 31 * result + (employerContactId != null ? employerContactId.hashCode() : 0);
    result = 31 * result + (pharmacyContactId != null ? pharmacyContactId.hashCode() : 0);
    result = 31 * result + (referredDate != null ? referredDate.hashCode() : 0);
    result = 31 * result + (onSocialAssistance ? 1 : 0);
    result = 31 * result + (hasArchivedRecords ? 1 : 0);
    result = 31 * result + (deceased ? 1 : 0);
    result = 31 * result + (deceasedDate != null ? deceasedDate.hashCode() : 0);
    result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
    result = 31 * result + (alert != null ? alert.hashCode() : 0);
    result = 31 * result + (albertaDetails != null ? albertaDetails.hashCode() : 0);
    result = 31 * result + (ontarioDetails != null ? ontarioDetails.hashCode() : 0);
    result = 31 * result + (novaScotiaDetails != null ? novaScotiaDetails.hashCode() : 0);
    result = 31 * result + (manitobaDetails != null ? manitobaDetails.hashCode() : 0);
    return result;
  }
}
