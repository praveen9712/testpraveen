
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.util.Calendar;
import java.util.Objects;
import javax.validation.Valid;

/**
 * The Patient model object
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Patient V1 object", implementation = PatientDto.class, name = "PatientDto")
public class PatientDto {

  @JsonProperty("enrolledProviderTerminationReason")
  @Schema(description = "The termination reason of the patient's enrolled provider")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  ProviderEnrollmentTerminationReason enrolledProviderTerminationReason;
  @JsonProperty("patientId")
  @Schema(description = "Unique id of a patient", type = "integer", example = "18911")
  private int patientId;
  @JsonProperty("demographics")
  @Schema(description = "Demographics objects of a patient", required = true)
  @Valid
  private DemographicsDto demographics;
  @JsonProperty("familyProviderId")
  @Schema(description = "The id of the patients family provider", type = "integer", example = "1",
      maximum = "999999999", nullable = true)
  private Integer familyProviderId;
  @JsonProperty("enrolledProviderId")
  @Schema(description = "The id of the patients enrolled provider", type = "integer", example = "1",
      maximum = "999999999", nullable = true)
  private Integer enrolledProviderId;
  @JsonProperty("officeProviderId")
  @Schema(description = "The id of the patients office provider", type = "integer", example = "2",
      maximum = "999999999", nullable = true)
  private Integer officeProviderId;

  @JsonProperty("referringProviderId")
  @Schema(description = "The id of the patients referring provider", type = "integer",
      example = "1",
      maximum = "999999999", nullable = true)
  private Integer referringProviderId;

  @JsonProperty("insurerId")
  @Schema(required = true, description = "The id for the patients insurer", type = "integer",
      example = "2",
      maximum = "999999999")
  private Integer insurerId;

  @JsonProperty("fileNumber")
  @Schema(description = " The patients file number", type = "string", example = "22-12345",
      maxLength = 30, nullable = true)
  private String fileNumber;

  @JsonProperty("uuid")
  @Schema(description = "The mapped uuid for the patient id. Read only.",
      example = "dca20596-e35e-325f-56a4-3190281a020f", type = "string",
      accessMode = AccessMode.READ_ONLY)
  private String uuid;

  @JsonProperty("registrationNumber")
  @Schema(description = "The patient registration number", type = "string", example = "123",
      maxLength = 20, nullable = true)
  private String registrationNumber;

  @JsonProperty("paperChartNote")
  @Schema(description = "A note for the patients paper chart", type = "string", example = " A note",
      maxLength = 255, nullable = true)
  private String paperChartNote;

  @JsonProperty("paperChart")
  @Schema(description = "Indication if the patient has a paper chart", type = "boolean",
      example = "false")
  private boolean paperChart;

  @JsonProperty("patientStatusId")
  @Schema(description = "The patients status id", type = "integer", example = "1", nullable = true)
  private Integer patientStatusId;

  @JsonProperty("gestationAge")
  @Schema(description = "The patients gestation age", type = "string",
      example = "2017-11-08 00:00:00.000")
  private Calendar gestationAge;

  @JsonProperty("employerContactId")
  @Schema(description = "The employer contact id", type = "integer", example = "1",
      maximum = "999999999", nullable = true)
  private Integer employerContactId;

  @JsonProperty("pharmacyContactId")
  @Schema(description = "The pharmacy contact id", type = "integer", example = "1",
      maximum = "999999999", nullable = true)
  private Integer pharmacyContactId;

  @JsonProperty("referredDate")
  @Schema(description = "Patient referred date", type = "string",
      example = "2017-11-08 00:00:00.000")
  private Calendar referredDate;

  @JsonProperty("onSocialAssistance")
  @Schema(description = "Indication if the patient is on social assistance", type = "boolean",
      example = "false")
  private boolean onSocialAssistance;

  @JsonProperty("hasArchivedRecords")
  @Schema(description = "Indication if the patient has archived records", type = "boolean",
      example = "false")
  private boolean hasArchivedRecords;

  @JsonProperty("deceased")
  @Schema(description = "Indication if the patient is deceased", type = "boolean",
      example = "false")
  private boolean deceased;

  @JsonProperty("deceasedDate")
  @Schema(description = "Patient deceased date", type = "string",
      example = "2017-11-08 00:00:00.000")
  private AccuroCalendar deceasedDate;

  @JsonProperty("occupation")
  @Schema(description = "Patient occupation", type = "string", example = "Lawyer", maxLength = 50,
      nullable = true)
  private String occupation;

  @JsonProperty("alert")
  @Schema(description = "Patient alert flag", nullable = true)
  private PatientFlagDto alert;

  @JsonProperty("albertaDetails")
  @Schema(description = "Alberta specific details. "
      + "If the current province is Alberta it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private AlbertaDetailsDto albertaDetails;

  @JsonProperty("ontarioDetails")
  @Schema(description = "Ontario specific details."
      + "If the current province is Ontario it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private OntarioDetailsDto ontarioDetails;

  @JsonProperty("novaScotiaDetails")
  @Schema(description = "Nova Scotia specific details"
      + "If the current province is Nova Scotia it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private NovaScotiaDetailsDto novaScotiaDetails;

  @JsonProperty("manitobaDetails")
  @Schema(description = "Manitoba specific details"
      + "If the current province is Manitoba it will be displayed.")
  @JsonInclude(JsonInclude.Include.NON_NULL)
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
   * The patients UUID Number. This parameter is read only.
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
  @DocumentationExample("2017-11-08 00:00:00.000")
  @TypeHint(String.class)
  public Calendar getGestationAge() {
    return gestationAge;
  }

  public void setGestationAge(Calendar gestationAge) {
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
  @DocumentationExample("2017-11-08 00:00:00.000")
  @TypeHint(String.class)
  public Calendar getReferredDate() {
    return referredDate;
  }

  public void setReferredDate(Calendar referredDate) {
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
  @DocumentationExample("2017-11-08 00:00:00.000")
  @TypeHint(String.class)
  public AccuroCalendar getDeceasedDate() {
    return deceasedDate;
  }

  public void setDeceasedDate(AccuroCalendar deceasedDate) {
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
  public int hashCode() {
    int hash = 3;
    hash = 11 * hash + this.patientId;
    hash = 11 * hash + Objects.hashCode(this.demographics);
    hash = 11 * hash + Objects.hashCode(this.familyProviderId);
    hash = 11 * hash + Objects.hashCode(this.enrolledProviderId);
    hash = 11 * hash + Objects.hashCode(this.enrolledProviderTerminationReason);
    hash = 11 * hash + Objects.hashCode(this.officeProviderId);
    hash = 11 * hash + Objects.hashCode(this.referringProviderId);
    hash = 11 * hash + Objects.hashCode(this.insurerId);
    hash = 11 * hash + Objects.hashCode(this.fileNumber);
    hash = 11 * hash + Objects.hashCode(this.uuid);
    hash = 11 * hash + Objects.hashCode(this.registrationNumber);
    hash = 11 * hash + Objects.hashCode(this.paperChartNote);
    hash = 11 * hash + (this.paperChart ? 1 : 0);
    hash = 11 * hash + Objects.hashCode(this.patientStatusId);
    hash = 11 * hash + Objects.hashCode(this.gestationAge);
    hash = 11 * hash + Objects.hashCode(this.employerContactId);
    hash = 11 * hash + Objects.hashCode(this.pharmacyContactId);
    hash = 11 * hash + Objects.hashCode(this.referredDate);
    hash = 11 * hash + (this.onSocialAssistance ? 1 : 0);
    hash = 11 * hash + (this.hasArchivedRecords ? 1 : 0);
    hash = 11 * hash + (this.deceased ? 1 : 0);
    hash = 11 * hash + Objects.hashCode(this.deceasedDate);
    hash = 11 * hash + Objects.hashCode(this.occupation);
    hash = 11 * hash + Objects.hashCode(this.alert);
    hash = 11 * hash + Objects.hashCode(this.albertaDetails);
    hash = 11 * hash + Objects.hashCode(this.ontarioDetails);
    hash = 11 * hash + Objects.hashCode(this.novaScotiaDetails);
    hash = 11 * hash + Objects.hashCode(this.manitobaDetails);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PatientDto other = (PatientDto) obj;
    if (this.patientId != other.patientId) {
      return false;
    }
    if (this.paperChart != other.paperChart) {
      return false;
    }
    if (this.onSocialAssistance != other.onSocialAssistance) {
      return false;
    }
    if (this.hasArchivedRecords != other.hasArchivedRecords) {
      return false;
    }
    if (this.deceased != other.deceased) {
      return false;
    }
    if (!Objects.equals(this.fileNumber, other.fileNumber)) {
      return false;
    }
    if (!Objects.equals(this.uuid, other.uuid)) {
      return false;
    }
    if (!Objects.equals(this.registrationNumber, other.registrationNumber)) {
      return false;
    }
    if (!Objects.equals(this.paperChartNote, other.paperChartNote)) {
      return false;
    }
    if (!Objects.equals(this.occupation, other.occupation)) {
      return false;
    }
    if (!Objects.equals(this.demographics, other.demographics)) {
      return false;
    }
    if (!Objects.equals(this.familyProviderId, other.familyProviderId)) {
      return false;
    }
    if (!Objects.equals(this.enrolledProviderId, other.enrolledProviderId)) {
      return false;
    }
    if (!Objects.equals(this.enrolledProviderTerminationReason,
        other.enrolledProviderTerminationReason)) {
      return false;
    }
    if (!Objects.equals(this.officeProviderId, other.officeProviderId)) {
      return false;
    }
    if (!Objects.equals(this.referringProviderId, other.referringProviderId)) {
      return false;
    }
    if (!Objects.equals(this.insurerId, other.insurerId)) {
      return false;
    }
    if (!Objects.equals(this.patientStatusId, other.patientStatusId)) {
      return false;
    }
    if (!Objects.equals(this.gestationAge, other.gestationAge)) {
      return false;
    }
    if (!Objects.equals(this.employerContactId, other.employerContactId)) {
      return false;
    }
    if (!Objects.equals(this.pharmacyContactId, other.pharmacyContactId)) {
      return false;
    }
    if (!Objects.equals(this.referredDate, other.referredDate)) {
      return false;
    }
    if (!Objects.equals(this.deceasedDate, other.deceasedDate)) {
      return false;
    }
    if (!Objects.equals(this.alert, other.alert)) {
      return false;
    }
    if (!Objects.equals(this.albertaDetails, other.albertaDetails)) {
      return false;
    }
    if (!Objects.equals(this.ontarioDetails, other.ontarioDetails)) {
      return false;
    }
    if (!Objects.equals(this.novaScotiaDetails, other.novaScotiaDetails)) {
      return false;
    }
    return Objects.equals(this.manitobaDetails, other.manitobaDetails);
  }


}
