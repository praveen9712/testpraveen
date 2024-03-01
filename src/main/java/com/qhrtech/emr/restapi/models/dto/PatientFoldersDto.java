
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;

/**
 * Patient Folder model object. This object represents all of the folders and subfolders
 * ('subtypes') associated with the patients documents.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "PatientFolder data transfer object model. "
    + "This object represents all of the folders and sub-folders.")
public class PatientFoldersDto {

  @JsonProperty("patient_id")
  @Schema(description = "Patient id associated with the patient folder", example = "1")
  private int patientId;

  @JsonProperty("folders")
  @Schema(title = "List of folders",
      type = "array",
      description = "A set of folder names",
      example = "[\"Document\",\"X-Ray\"]")
  private Set<String> folders;

  @JsonProperty("subtypes")
  @Schema(title = "List of sub-folders",
      type = "array",
      description = "A set of sub-folders",
      example = "[\"Allergy\", \"Family History\"]")
  private Set<String> subtypes;

  /**
   * Patient ID associated with the patient folder.
   *
   * @documentationExample 1
   *
   * @return Patient ID
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * A set of dfolder names
   *
   * @documentationExample Documents
   *
   * @return Folder names
   */
  public Set<String> getFolders() {
    return folders;
  }

  public void setFolders(Set<String> folders) {
    this.folders = folders;
  }

  /**
   * A set of folder subtypes.
   *
   * @documentationExample X-Rays
   *
   * @return Subtype names
   */
  public Set<String> getSubtypes() {
    return subtypes;
  }

  public void setSubtypes(Set<String> subtypes) {
    this.subtypes = subtypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PatientFoldersDto)) {
      return false;
    }
    PatientFoldersDto that = (PatientFoldersDto) o;
    return getPatientId() == that.getPatientId()
        && Objects.equals(getFolders(), that.getFolders())
        && Objects.equals(getSubtypes(), that.getSubtypes());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPatientId(), getFolders(), getSubtypes());
  }

}
