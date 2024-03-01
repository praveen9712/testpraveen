
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.LocalDateTime;

/**
 * The PatientImmunization data transfer object model. This contains information regarding a vaccine
 * which has been administered to a patient.
 *
 * @see com.qhrtech.emr.accuro.model.immunization.PatientImmunization
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientImmunization data transfer object model. "
    + "This contains information regarding the vaccine which has been administered to the patient.")
public class PatientImmunizationDto {

  @JsonProperty("patientId")
  @Schema(description = "ID of the patient", example = "1000")
  private int patientId;

  @JsonProperty("immunizationAge")
  @Schema(description = "The age when this immunization was given", example = "00013")
  private ImmunizationAgeDto immunizationAge;

  @JsonProperty("derivedAge")
  @Schema(description = "The derived age is the age the immunization was applied on "
      + "in the format yyymm. This denotes years and months after the patient was born.",
      example = "01710")
  private String derivedAge;

  @JsonProperty("vaccineId")
  @Schema(
      description = "Vaccine id which identifies the vaccine that this immunization administered",
      example = "151")
  private int vaccineId;

  @JsonProperty("vaccineName")
  @Schema(description = "The name of the vaccine which was administered",
      example = "Licodermicaine")
  private String vaccineName;

  @JsonProperty("codeValue")
  @Schema(description = "The code value", example = "00431648")
  private String codeValue; // DIN ID

  @JsonProperty("codeTable")
  @Schema(description = "Code table", example = "DIN")
  private String codeTable; // "DIN"

  @JsonProperty("manufacturer")
  @Schema(description = "The manufacturer", example = "SANOFI PASTEUR")
  private String manufacturer;

  @JsonProperty("lotNumber")
  @Schema(description = "The lot number", example = "544525N")
  private String lotNumber;

  @JsonProperty("expiryDate")
  @Schema(description = "The expiry date", example = "2017-11-29T00:00:00.000", type = "string")
  private LocalDateTime expiryDate;

  @JsonProperty("dose")
  @Schema(description = "The dose given", example = "2.0ml")
  private String dose;

  @JsonProperty("note")
  @Schema(description = "The note", example = "Subcutaneous injection")
  private String note;

  @JsonProperty("administeredDate")
  @Schema(description = "The date when this immunization was administered",
      example = "2017-11-29", type = "string")
  private AccuroCalendar administeredDate;

  @JsonProperty("site")
  @Schema(description = "The site this immunization was administered", example = "Buttocks")
  private String site;

  @JsonProperty("route")
  @Schema(description = "The route", example = "IM")
  private String route;

  @JsonProperty("status")
  @Schema(description = "The status of this immunization", example = "refused")
  private String status;

  @JsonProperty("statusReason")
  @Schema(description = "The reason for the status")
  private String statusReason;

  @JsonProperty("administeredInClinic")
  @Schema(description = "Flag indicating if this immunization was administered in clinic",
      example = "true")
  private Boolean administeredInClinic;

  @JsonProperty("administeredLocation")
  @Schema(description = "The location this immunization was given", example = "Medical van")
  private String administeredLocation;

  @JsonProperty("reaction")
  @Schema(description = "The reaction to the immunization", example = "Hives")
  private String reaction;

  @JsonProperty("contactInformation")
  @Schema(description = "Contact information with this immunization",
      example = "James Cell: 234-243-2345")
  private String contactInformation;

  @JsonProperty("instructions")
  @Schema(description = "Instructions with this immunization",
      example = "For the treatment of Bob's disease")
  private String instructions;

  @JsonProperty("customUuid")
  @Schema(description = "Custom uuid to this immunization",
      example = "d22d6188-9d6b-4b52-a34b-cfc5cf4641b2")
  private String customUuid;

  @JsonProperty("immunizationId")
  @Schema(description = "The id of this object", example = "12")
  private int immunizationId;

  /**
   * The Patient ID that this immunization belongs to.
   *
   * @documentationExample +00014
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
   * The age this immunization was given.
   *
   * @return
   */
  public ImmunizationAgeDto getImmunizationAge() {
    return immunizationAge;
  }

  public void setImmunizationAge(ImmunizationAgeDto immunizationAge) {
    this.immunizationAge = immunizationAge;
  }

  /**
   * The derived age is the age the immunization was applied on in the format yyy-mm. This denotes
   * years and months after the patient is born.
   *
   * @documentationExample 01710
   *
   * @return
   */
  public String getDerivedAge() {
    return derivedAge;
  }

  public void setDerivedAge(String derivedAge) {
    this.derivedAge = derivedAge;
  }

  /**
   * The vaccine ID which identifies the vaccine that this immunization administered.
   *
   * @documentationExample 151
   *
   * @return
   */
  public int getVaccineId() {
    return vaccineId;
  }

  public void setVaccineId(int vaccineId) {
    this.vaccineId = vaccineId;
  }

  /**
   * The Name of the vaccine which was administered.
   *
   * @documentationExample Licodermicaine
   *
   * @return
   */
  public String getVaccineName() {
    return vaccineName;
  }

  public void setVaccineName(String vaccineName) {
    this.vaccineName = vaccineName;
  }

  /**
   * The Code value, which when taken with code table can get identifying information of this
   * immunization. Generally references the DIN ID value.
   *
   * @documentationExample 151
   *
   * @return
   */
  public String getCodeValue() {
    return codeValue;
  }

  public void setCodeValue(String codeValue) {
    this.codeValue = codeValue;
  }

  /**
   * The Code Table, which when taken with code value can get identifying information of this
   * immunization. Generally references the DIN code table.
   *
   * @documentationExample DIN
   *
   * @return the string table to reference.
   */
  public String getCodeTable() {
    return codeTable;
  }

  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * The manufacturer of the vaccine administered.
   *
   * @documentationExample Maerk.
   *
   * @return the manufacturer.
   */
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  /**
   * The lot number.
   *
   * @documentationExample 544525N.
   *
   * @return the lot number
   */
  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  /**
   * The expiry date.
   *
   * @return
   */
  @DocumentationExample("2017-11-29T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * The dose given.
   *
   * @documentationExample 5 mg
   *
   * @return the dose.
   */
  public String getDose() {
    return dose;
  }

  public void setDose(String dose) {
    this.dose = dose;
  }

  /**
   * A note associated with this immunization.
   *
   * @documentationExample Given early due to reasons.
   *
   * @return the note.
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * The date this immunization was administered.
   *
   * @return the date.
   */
  @DocumentationExample("2017-11-29T00:00:00.000-0800")
  @TypeHint(String.class)
  public AccuroCalendar getAdministeredDate() {
    return administeredDate;
  }

  public void setAdministeredDate(AccuroCalendar administeredDate) {
    this.administeredDate = administeredDate;
  }

  /**
   * The site this immunization was administered.
   *
   * @documentationExample Buttocks.
   *
   * @return the site.
   */
  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  /**
   * The route in which this immunization was administered.
   *
   * @documentationExample IM
   *
   * @return the route of administration.
   */
  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  /**
   * The status of this immunization.
   *
   * @documentationExample refused
   *
   * @return the status.
   */
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * The reason for the status.
   *
   * @documentationExample does not like needles
   *
   * @return the reason.
   */
  public String getStatusReason() {
    return statusReason;
  }

  public void setStatusReason(String statusReason) {
    this.statusReason = statusReason;
  }

  /**
   * True if this immunization was administered in clinic.
   *
   * @documentationExample false
   *
   * @return
   */
  public Boolean getAdministeredInClinic() {
    return administeredInClinic;
  }

  public void setAdministeredInClinic(Boolean administeredInClinic) {
    this.administeredInClinic = administeredInClinic;
  }

  /**
   * The location this immunization was given.
   *
   * @documentationExample Medical van.
   *
   * @return the location.
   */
  public String getAdministeredLocation() {
    return administeredLocation;
  }

  public void setAdministeredLocation(String administeredLocation) {
    this.administeredLocation = administeredLocation;
  }

  /**
   * The reaction to the immunization.
   *
   * @documentationExample Hives.
   *
   * @return the reaction.
   */
  public String getReaction() {
    return reaction;
  }

  public void setReaction(String reaction) {
    this.reaction = reaction;
  }

  /**
   * Contact information associated with this immunization.
   *
   * @documentationExample James Cell: 234-243-2345
   *
   * @return The contact information.
   */
  public String getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(String contactInformation) {
    this.contactInformation = contactInformation;
  }

  /**
   * Instructions associated with this immunization.
   *
   * @documentationExample For the treatment of Paget's disease.
   *
   * @return the instructions.
   */
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  /**
   * A key which is unique to this immunization.
   *
   * @return the uuid key.
   */
  @DocumentationExample("d22d6188-9d6b-4b52-a34b-cfc5cf4641b2")
  @TypeHint(String.class)
  public String getCustomUuid() {
    return customUuid;
  }

  public void setCustomUuid(String customUuid) {
    this.customUuid = customUuid;
  }

  /**
   * The Id which id uniquely identifies this object.
   *
   * @documentationExample 1
   *
   * @return the id.
   */
  public int getImmunizationId() {
    return immunizationId;
  }

  public void setImmunizationId(int immunizationId) {
    this.immunizationId = immunizationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientImmunizationDto that = (PatientImmunizationDto) o;

    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getVaccineId() != that.getVaccineId()) {
      return false;
    }
    if (getImmunizationId() != that.getImmunizationId()) {
      return false;
    }
    if (getImmunizationAge() != null ? !getImmunizationAge().equals(that.getImmunizationAge())
        : that.getImmunizationAge() != null) {
      return false;
    }
    if (getDerivedAge() != null ? !getDerivedAge().equals(that.getDerivedAge())
        : that.getDerivedAge() != null) {
      return false;
    }
    if (getVaccineName() != null ? !getVaccineName().equals(that.getVaccineName())
        : that.getVaccineName() != null) {
      return false;
    }
    if (getCodeValue() != null ? !getCodeValue().equals(that.getCodeValue())
        : that.getCodeValue() != null) {
      return false;
    }
    if (getCodeTable() != null ? !getCodeTable().equals(that.getCodeTable())
        : that.getCodeTable() != null) {
      return false;
    }
    if (getManufacturer() != null ? !getManufacturer().equals(that.getManufacturer())
        : that.getManufacturer() != null) {
      return false;
    }
    if (getLotNumber() != null ? !getLotNumber().equals(that.getLotNumber())
        : that.getLotNumber() != null) {
      return false;
    }
    if (getExpiryDate() != null ? !getExpiryDate().equals(that.getExpiryDate())
        : that.getExpiryDate() != null) {
      return false;
    }
    if (getDose() != null ? !getDose().equals(that.getDose()) : that.getDose() != null) {
      return false;
    }
    if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null) {
      return false;
    }
    if (getAdministeredDate() != null ? !getAdministeredDate().equals(that.getAdministeredDate())
        : that.getAdministeredDate() != null) {
      return false;
    }
    if (getSite() != null ? !getSite().equals(that.getSite()) : that.getSite() != null) {
      return false;
    }
    if (getRoute() != null ? !getRoute().equals(that.getRoute()) : that.getRoute() != null) {
      return false;
    }
    if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) {
      return false;
    }
    if (getStatusReason() != null ? !getStatusReason().equals(that.getStatusReason())
        : that.getStatusReason() != null) {
      return false;
    }
    if (getAdministeredInClinic() != null ? !getAdministeredInClinic()
        .equals(that.getAdministeredInClinic()) : that.getAdministeredInClinic() != null) {
      return false;
    }
    if (getAdministeredLocation() != null ? !getAdministeredLocation()
        .equals(that.getAdministeredLocation()) : that.getAdministeredLocation() != null) {
      return false;
    }
    if (getReaction() != null ? !getReaction().equals(that.getReaction())
        : that.getReaction() != null) {
      return false;
    }
    if (getContactInformation() != null ? !getContactInformation()
        .equals(that.getContactInformation()) : that.getContactInformation() != null) {
      return false;
    }
    if (getInstructions() != null ? !getInstructions().equals(that.getInstructions())
        : that.getInstructions() != null) {
      return false;
    }
    return getCustomUuid() != null ? getCustomUuid().equals(that.getCustomUuid())
        : that.getCustomUuid() == null;
  }

  @Override
  public int hashCode() {
    int result = getPatientId();
    result = 31 * result + (getImmunizationAge() != null ? getImmunizationAge().hashCode() : 0);
    result = 31 * result + (getDerivedAge() != null ? getDerivedAge().hashCode() : 0);
    result = 31 * result + getVaccineId();
    result = 31 * result + (getVaccineName() != null ? getVaccineName().hashCode() : 0);
    result = 31 * result + (getCodeValue() != null ? getCodeValue().hashCode() : 0);
    result = 31 * result + (getCodeTable() != null ? getCodeTable().hashCode() : 0);
    result = 31 * result + (getManufacturer() != null ? getManufacturer().hashCode() : 0);
    result = 31 * result + (getLotNumber() != null ? getLotNumber().hashCode() : 0);
    result = 31 * result + (getExpiryDate() != null ? getExpiryDate().hashCode() : 0);
    result = 31 * result + (getDose() != null ? getDose().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (getAdministeredDate() != null ? getAdministeredDate().hashCode() : 0);
    result = 31 * result + (getSite() != null ? getSite().hashCode() : 0);
    result = 31 * result + (getRoute() != null ? getRoute().hashCode() : 0);
    result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
    result = 31 * result + (getStatusReason() != null ? getStatusReason().hashCode() : 0);
    result =
        31 * result + (getAdministeredInClinic() != null ? getAdministeredInClinic().hashCode()
            : 0);
    result =
        31 * result + (getAdministeredLocation() != null ? getAdministeredLocation().hashCode()
            : 0);
    result = 31 * result + (getReaction() != null ? getReaction().hashCode() : 0);
    result =
        31 * result + (getContactInformation() != null ? getContactInformation().hashCode() : 0);
    result = 31 * result + (getInstructions() != null ? getInstructions().hashCode() : 0);
    result = 31 * result + (getCustomUuid() != null ? getCustomUuid().hashCode() : 0);
    result = 31 * result + getImmunizationId();
    return result;
  }

  @Override
  public String toString() {
    return "PatientImmunizationDto{"
        + "patientId=" + patientId
        + ", immunizationAge='" + immunizationAge + '\''
        + ", derivedAge='" + derivedAge + '\''
        + ", vaccineId=" + vaccineId
        + ", vaccineName='" + vaccineName + '\''
        + ", codeValue='" + codeValue + '\''
        + ", codeTable='" + codeTable + '\''
        + ", manufacturer='" + manufacturer + '\''
        + ", lotNumber='" + lotNumber + '\''
        + ", expiryDate=" + expiryDate
        + ", dose='" + dose + '\''
        + ", note='" + note + '\''
        + ", administeredDate=" + administeredDate
        + ", site='" + site + '\''
        + ", route='" + route + '\''
        + ", status='" + status + '\''
        + ", statusReason='" + statusReason + '\''
        + ", administeredInClinic=" + administeredInClinic
        + ", administeredLocation='" + administeredLocation + '\''
        + ", reaction='" + reaction + '\''
        + ", contactInformation='" + contactInformation + '\''
        + ", instructions='" + instructions + '\''
        + ", customUuid='" + customUuid + '\''
        + ", immunizationId=" + immunizationId
        + '}';
  }
}
