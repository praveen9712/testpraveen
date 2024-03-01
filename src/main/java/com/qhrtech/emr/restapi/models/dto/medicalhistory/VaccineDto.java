
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import org.joda.time.LocalDateTime;

/**
 * The Vaccine data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.Vaccine
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Vaccine data transfer object model")
public class VaccineDto {

  @JsonProperty("vaccineId")
  @Schema(description = "Id of the vaccine", example = "1")
  private int vaccineId;

  @JsonProperty("vaccineName")
  @Schema(description = "Name of the vaccine", example = "Hepatitis A")
  private String vaccineName;

  @JsonProperty("immunizationAbbreviation")
  @Schema(description = "Abbreviation of the vaccine name", example = "HepA")
  private String immunizationAbbreviation;

  @JsonProperty("codeValue")
  @Schema(description = "Drug code of the vaccine", example = "02245677")
  private String codeValue;

  @JsonProperty("codeTable")
  @Schema(description = "Drug code table of the vaccine", example = "DIN")
  private String codeTable;

  @JsonProperty("manufacturer")
  @Schema(description = "The manufacturer of the vaccine", example = "GlaxoSmithKline")
  private String manufacturer;

  @JsonProperty("lotNumber")
  @Schema(description = "The vaccine lot number", example = "12")
  private String lotNumber;

  @JsonProperty("expiryDate")
  @Schema(description = "The expiry date", example = "2024-06-27T00:00:00.000-0700")
  private LocalDateTime expiryDate;

  @JsonProperty("route1")
  @Schema(description = "The first route of the vaccine", example = "IM")
  private String route1;

  @JsonProperty("route2")
  @Schema(description = "The second route of the vaccine", example = "SC")
  private String route2;

  @JsonProperty("dose")
  @Schema(description = "Dose of the vaccine", example = "2")
  private String dose;

  @JsonProperty("note")
  @Schema(description = "The note of the vaccine",
      example = "It contains thimerosal as a preservative")
  private String note;

  @JsonProperty("quantity")
  @Schema(description = "Quantity of the vaccine", example = "3")
  private Integer quantity;

  @JsonProperty("active")
  @Schema(description = "Indication if the vaccine is active", example = "true")
  private boolean active;

  @JsonProperty("vaccineTypes")
  @Schema(description = "The types of the vaccine", example = "[\"MMR\",\"Rotavirus\"]")
  private Set<String> vaccineTypes;

  /**
   * The types of this vaccine.
   *
   * @documentationExample MMR
   *
   * @return Vaccine ID
   */

  public Set<String> getVaccineTypes() {
    return vaccineTypes;
  }

  public void setVaccineTypes(Set<String> vaccineTypes) {
    this.vaccineTypes = vaccineTypes;
  }

  /**
   * The Vaccine ID
   *
   * @documentationExample 15
   *
   * @return Vaccine ID
   */
  public int getVaccineId() {
    return vaccineId;
  }

  public void setVaccineId(int vaccineId) {
    this.vaccineId = vaccineId;
  }

  /**
   * The vaccine name
   *
   * @documentationExample Varicella
   *
   * @return Vaccine name
   */
  public String getVaccineName() {
    return vaccineName;
  }

  public void setVaccineName(String vaccineName) {
    this.vaccineName = vaccineName;
  }

  /**
   * The abbreviation of the vaccine name
   *
   * @documentationExample Pneu-C-23
   *
   * @return Abbreviation of the vaccine name
   */
  public String getImmunizationAbbreviation() {
    return immunizationAbbreviation;
  }

  public void setImmunizationAbbreviation(String immunizationAbbreviation) {
    this.immunizationAbbreviation = immunizationAbbreviation;
  }

  /**
   * The drug code of the vaccine
   *
   * @documentationExample 02245677
   *
   * @return Drug code of the vaccine
   */
  public String getCodeValue() {
    return codeValue;
  }

  public void setCodeValue(String codeValue) {
    this.codeValue = codeValue;
  }

  /**
   * The drug code table of the vaccine
   *
   * @documentationExample DIN
   *
   * @return Drug code table of the vaccine
   */
  public String getCodeTable() {
    return codeTable;
  }

  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * The manufacturer of the vaccine
   *
   * @documentationExample GlaxoSmithKline
   *
   * @return Manufacturer of the vaccine
   */
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  /**
   * The vaccine lot number
   *
   * @documentationExample 12
   *
   * @return Vaccine lot number
   */
  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  /**
   * The expiry date of the vaccine.
   *
   * @return Expiry date
   */
  @DocumentationExample("2024-06-27T00:00:00.000-0700")
  @TypeHint(String.class)
  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * The first route of the vaccine
   *
   * @documentationExample IM
   *
   * @return The first route
   */
  public String getRoute1() {
    return route1;
  }

  public void setRoute1(String route1) {
    this.route1 = route1;
  }

  /**
   * The second route of the vaccine
   *
   * @documentationExample SC
   *
   * @return The second route
   */
  public String getRoute2() {
    return route2;
  }

  public void setRoute2(String route2) {
    this.route2 = route2;
  }

  /**
   * The dose of the vaccine
   *
   * @documentationExample 2
   *
   * @return Dose of the vaccine
   */
  public String getDose() {
    return dose;
  }

  public void setDose(String dose) {
    this.dose = dose;
  }

  /**
   * The note of the vaccine
   *
   * @documentationExample It contains thimerosal as a preservative.
   *
   * @return Note of the vaccine
   */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /**
   * The quantity of the vaccine
   *
   * @documentationExample 3
   *
   * @return Quantity of the vaccine
   */
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  /**
   * Indicates if the vaccine is active
   *
   * @documentationExample true
   *
   * @return <code>true</code> active, otherwise <code>false</code>
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VaccineDto that = (VaccineDto) o;

    if (getVaccineId() != that.getVaccineId()) {
      return false;
    }
    if (isActive() != that.isActive()) {
      return false;
    }
    if (getVaccineName() != null ? !getVaccineName().equals(that.getVaccineName())
        : that.getVaccineName() != null) {
      return false;
    }
    if (getImmunizationAbbreviation() != null ? !getImmunizationAbbreviation()
        .equals(that.getImmunizationAbbreviation()) : that.getImmunizationAbbreviation() != null) {
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
    if (getRoute1() != null ? !getRoute1().equals(that.getRoute1()) : that.getRoute1() != null) {
      return false;
    }
    if (getRoute2() != null ? !getRoute2().equals(that.getRoute2()) : that.getRoute2() != null) {
      return false;
    }
    if (getDose() != null ? !getDose().equals(that.getDose()) : that.getDose() != null) {
      return false;
    }
    if (getNote() != null ? !getNote().equals(that.getNote()) : that.getNote() != null) {
      return false;
    }
    if (getQuantity() != null ? !getQuantity().equals(that.getQuantity())
        : that.getQuantity() != null) {
      return false;
    }
    return getVaccineTypes() != null ? getVaccineTypes().equals(that.getVaccineTypes())
        : that.getVaccineTypes() == null;
  }

  @Override
  public int hashCode() {
    int result = getVaccineId();
    result = 31 * result + (getVaccineName() != null ? getVaccineName().hashCode() : 0);
    result = 31 * result + (getImmunizationAbbreviation() != null ? getImmunizationAbbreviation()
        .hashCode() : 0);
    result = 31 * result + (getCodeValue() != null ? getCodeValue().hashCode() : 0);
    result = 31 * result + (getCodeTable() != null ? getCodeTable().hashCode() : 0);
    result = 31 * result + (getManufacturer() != null ? getManufacturer().hashCode() : 0);
    result = 31 * result + (getLotNumber() != null ? getLotNumber().hashCode() : 0);
    result = 31 * result + (getExpiryDate() != null ? getExpiryDate().hashCode() : 0);
    result = 31 * result + (getRoute1() != null ? getRoute1().hashCode() : 0);
    result = 31 * result + (getRoute2() != null ? getRoute2().hashCode() : 0);
    result = 31 * result + (getDose() != null ? getDose().hashCode() : 0);
    result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
    result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
    result = 31 * result + (isActive() ? 1 : 0);
    result = 31 * result + (getVaccineTypes() != null ? getVaccineTypes().hashCode() : 0);
    return result;
  }
}
