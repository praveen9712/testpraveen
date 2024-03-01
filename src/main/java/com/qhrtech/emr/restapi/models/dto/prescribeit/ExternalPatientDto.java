
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "External Patient Data transfer object")
public class ExternalPatientDto {

  @JsonProperty("id")
  @Schema(description = "The identity of the external patient", type = "int", example = "1")
  private int id;

  @NotBlank
  @Size(max = 255)
  @Schema(description = "External patient identifier", type = "String", example = "STRING")
  @JsonProperty("externalPatientIdentifier")
  private String externalPatientIdentifier;

  @NotBlank
  @Size(max = 255)
  @JsonProperty("externalSystemIdentifier")
  @Schema(description = "External System identifier", type = "String", example = "PRESCRIBEIT")
  private String externalSystemIdentifier;

  @JsonProperty("accuroPatientId")
  @Schema(description = "Accuro patient id", type = "Integer", example = "3421")
  private Integer accuroPatientId;

  @NotBlank(message = "LastName cannot be empty")
  @Size(max = 255)
  @Schema(description = "The patient last/family name", type = "String", example = "FAMILYNAME")
  @JsonProperty("lastName")
  private String familyName;

  @NotBlank(message = "FirstName cannot be empty")
  @Size(max = 255)
  @Schema(description = "The patient first/given name", type = "String", example = "GIVENNAME")
  @JsonProperty("firstName")
  private String givenName;

  @NotNull
  @JsonProperty("birthDate")
  @Schema(description = "The patient birthdate", type = "LocalDate", example = "2020-06-09")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @CheckLocalDateRange
  private LocalDate birthDate;

  @NotBlank
  @Size(max = 255)
  @Schema(description = "The patient gender", type = "String", example = "Female")
  @JsonProperty("gender")
  private String gender;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getExternalPatientIdentifier() {
    return externalPatientIdentifier;
  }

  public void setExternalPatientIdentifier(String externalPatientIdentifier) {
    this.externalPatientIdentifier = externalPatientIdentifier;
  }

  public String getExternalSystemIdentifier() {
    return externalSystemIdentifier;
  }

  public void setExternalSystemIdentifier(String externalSystemIdentifier) {
    this.externalSystemIdentifier = externalSystemIdentifier;
  }

  public Integer getAccuroPatientId() {
    return accuroPatientId;
  }

  public void setAccuroPatientId(Integer accuroPatientId) {
    this.accuroPatientId = accuroPatientId;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExternalPatientDto that = (ExternalPatientDto) o;

    if (id != that.id) {
      return false;
    }
    if (!Objects.equals(externalPatientIdentifier, that.externalPatientIdentifier)) {
      return false;
    }
    if (!Objects.equals(externalSystemIdentifier, that.externalSystemIdentifier)) {
      return false;
    }
    if (!Objects.equals(accuroPatientId, that.accuroPatientId)) {
      return false;
    }
    if (!Objects.equals(familyName, that.familyName)) {
      return false;
    }
    if (!Objects.equals(givenName, that.givenName)) {
      return false;
    }
    if (!Objects.equals(birthDate, that.birthDate)) {
      return false;
    }
    return Objects.equals(gender, that.gender);
  }

  @Override
  public int hashCode() {
    int result = id;
    result =
        31 * result + (externalPatientIdentifier != null ? externalPatientIdentifier.hashCode()
            : 0);
    result =
        31 * result + (externalSystemIdentifier != null ? externalSystemIdentifier.hashCode() : 0);
    result = 31 * result + (accuroPatientId != null ? accuroPatientId.hashCode() : 0);
    result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
    result = 31 * result + (givenName != null ? givenName.hashCode() : 0);
    result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ExternalPatientDto{"
        + "id=" + id
        + ", externalPatientIdentifier='" + externalPatientIdentifier + '\''
        + ", externalSystemIdentifier='" + externalSystemIdentifier + '\''
        + ", accuroPatientId=" + accuroPatientId
        + ", familyName='" + familyName + '\''
        + ", givenName='" + givenName + '\''
        + ", birthDate=" + birthDate
        + ", gender='" + gender + '\''
        + '}';
  }
}
