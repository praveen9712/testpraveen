
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDateTime;

/**
 * <p>
 * The Lab Group model
 * </p>
 * <p>
 * A Lab Group represents an instance of a {@code LabTest}
 * </p>
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Lab group data transfer object model")
public class LabGroupDto {

  @JsonProperty("groupId")
  @Schema(description = "The unique identifier of the lab group", example = "42")
  private int groupId;

  @JsonProperty("baseGroupId")
  @Schema(
      description = "The base group to which this lab group belongs to.\n\n Lab groups are never "
          + "updated. Rather, when their state changes a new lab group is created.\n\n "
          + "The new lab group becomes the next in a series of lab groups called the base group. "
          + "The id of the initial lab group becomes the base group id.",
      example = "512")
  private Integer baseGroupId;

  @JsonProperty("patientId")
  @Schema(description = "Id of the Accuro patient the lab is associated with", example = "15")
  private Integer patientId;

  @JsonProperty("testId")
  @Schema(description = "Id of the lab test this observation group is associated with",
      example = "20")
  private Integer testId;

  @JsonProperty("sourceId")
  @Schema(description = "Id of the lab source for this Lab", example = "5")
  private Integer sourceId;

  @JsonProperty("orderingProviderId")
  @Schema(description = "Id of the Accuro provider ordering this lab", example = "9")
  private Integer orderingProviderId;

  @JsonProperty("active")
  @Schema(
      description = "Indication if this is the active lab group in the base Group.\n\n"
          + "The latest lab group in a series is the current state of the lab group. "
          + "This is marked by the active flag.\n\n Only one lab group in a base group "
          + "will be active.",
      example = "true")
  private boolean active;

  @JsonProperty("internalNote")
  @Schema(description = "The internal note used for display purposes in Accuro",
      example = "An example note")
  private String internalNote;

  @JsonProperty("externalNote")
  @Schema(description = "The external note for the lab", example = "An example note")
  private String externalNote;

  @JsonProperty("collectionDate")
  @Schema(description = "The date, the lab group was collected. Date of collection.",
      example = "1999-12-11T18:11:25.340")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime collectionDate;

  @JsonProperty("transactionDate")
  @Schema(description = "Date of lab group transaction", example = "2017-12-07T13:38:00.000")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime transactionDate;

  @JsonProperty("observationDate")
  @Schema(description = "Date of lab group observation", example = "2017-12-07T13:38:00.000")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime observationDate;

  @JsonProperty("versionDate")
  @Schema(description = "Date of lab group version", example = "2017-12-07T13:38:00.000")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime versionDate;

  @JsonProperty("reviewWithPatient")
  @Schema(
      description = "Indication of the lab will be reviewed with the patient",
      example = "true")
  private boolean reviewWithPatient;

  @JsonProperty("observations")
  @Schema(description = "A list of lab observation DTOs")
  private List<LabObservationDto> observations;

  @JsonProperty("metadata")
  @Schema(description = "The map of lab group metadata")
  private Map<String, String> metadata;

  /**
   * <p>
   * The Base Group that this Lab Group belongs to.
   * </p>
   * <p>
   * Lab Groups are never updated, rather when their state changes a new lab group is created. The
   * new lab group becomes the next in a series of lab groups called the base group. The id of the
   * initial lab group becomes the base group id.
   * </p>
   *
   * @documentationExample 512
   *
   * @return The Id of the Base Group of this Lab Group.
   */
  public Integer getBaseGroupId() {
    return baseGroupId;
  }

  public void setBaseGroupId(Integer baseGroupId) {
    this.baseGroupId = baseGroupId;
  }

  /**
   * <p>
   * Whether this is the active Lab Group in the Base Group.
   * </p>
   * <p>
   * The latest lab group in a series is the current state of the lab group. This is marked by the
   * active flag. Only one lab group in a base group will be active.
   * </p>
   *
   * @documentationExample true
   *
   * @return true if this is the active group, false otherwise.
   */
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * The unique identifier of the Lab Group.
   *
   * @documentationExample 42
   *
   * @return Lab Group ID
   */
  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  /**
   * Id of the Accuro Patient the Lab is associated with.
   *
   * @documentationExample 15
   *
   * @return Patient ID
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * Id of the Lab Test this observation group is associated with.
   *
   * @documentationExample 20
   *
   * @return Lab Test ID
   */
  public Integer getTestId() {
    return testId;
  }

  public void setTestId(Integer testId) {
    this.testId = testId;
  }

  /**
   * Id of the Lab Source for this Lab.
   *
   * @documentationExample 5
   *
   * @return Lab Source ID
   */
  public Integer getSourceId() {
    return sourceId;
  }

  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Id of the Accuro Provider ordering this lab.
   *
   * @documentationExample 9
   *
   * @return Provider ID
   */
  public Integer getOrderingProviderId() {
    return orderingProviderId;
  }

  public void setOrderingProviderId(Integer orderingProviderId) {
    this.orderingProviderId = orderingProviderId;
  }

  /**
   * An internal note used for display purposes in Accuro
   *
   * @documentationExample An example note.
   *
   * @return Internal note
   */
  public String getInternalNote() {
    return internalNote;
  }

  public void setInternalNote(String internalNote) {
    this.internalNote = internalNote;
  }

  /**
   * An external note for the lab.
   *
   * @documentationExample An example note.
   *
   * @return External note
   */
  public String getExternalNote() {
    return externalNote;
  }

  public void setExternalNote(String externalNote) {
    this.externalNote = externalNote;
  }

  /**
   * The date the Lab Group was collected.
   *
   * Date of collection.
   *
   * @return A collection date
   */
  @DocumentationExample("1999-12-11T18:11:25.343-0800")
  @TypeHint(String.class)
  public LocalDateTime getCollectionDate() {
    return collectionDate;
  }

  public void setCollectionDate(LocalDateTime collectionDate) {
    this.collectionDate = collectionDate;
  }

  /**
   * Date of Lab Group transaction
   *
   * @return Datetime
   */
  @DocumentationExample("2017-12-07T13:38:00.000-0800")
  @TypeHint(String.class)
  public LocalDateTime getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(LocalDateTime transactionDate) {
    this.transactionDate = transactionDate;
  }

  /**
   * Date of Lab Group observation
   *
   * @return Datetime
   */
  @DocumentationExample("2017-12-07T13:38:00.000-0800")
  @TypeHint(String.class)
  public LocalDateTime getObservationDate() {
    return observationDate;
  }

  public void setObservationDate(LocalDateTime observationDate) {
    this.observationDate = observationDate;
  }

  /**
   * Date of Lab Group version
   *
   * @return Datetime
   */
  @DocumentationExample("2017-12-07T13:38:00.000-0800")
  @TypeHint(String.class)
  public LocalDateTime getVersionDate() {
    return versionDate;
  }

  public void setVersionDate(LocalDateTime versionDate) {
    this.versionDate = versionDate;
  }

  /**
   * Indication of the lab will be reviewed with the patient.
   *
   * @documentationExample true
   *
   * @return <code>true</code> if the patient should be contacted about the lab, or else
   *         <code>false</code>
   */
  public boolean isReviewWithPatient() {
    return reviewWithPatient;
  }

  public void setReviewWithPatient(boolean reviewWithPatient) {
    this.reviewWithPatient = reviewWithPatient;
  }

  /**
   * A list of Lab Observation DTOs.
   *
   * @return Lab Observation DTO
   */
  public List<LabObservationDto> getObservations() {
    return observations;
  }

  public void setObservations(List<LabObservationDto> observations) {
    this.observations = observations;
  }

  /**
   * A map of Lab Group metadata.
   *
   * @return A map of meta data.
   */
  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LabGroupDto that = (LabGroupDto) o;

    if (groupId != that.groupId) {
      return false;
    }
    if (active != that.active) {
      return false;
    }
    if (reviewWithPatient != that.reviewWithPatient) {
      return false;
    }
    if (baseGroupId != null ? !baseGroupId.equals(that.baseGroupId) : that.baseGroupId != null) {
      return false;
    }
    if (patientId != null ? !patientId.equals(that.patientId) : that.patientId != null) {
      return false;
    }
    if (testId != null ? !testId.equals(that.testId) : that.testId != null) {
      return false;
    }
    if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) {
      return false;
    }
    if (orderingProviderId != null ? !orderingProviderId.equals(that.orderingProviderId)
        : that.orderingProviderId != null) {
      return false;
    }
    if (internalNote != null ? !internalNote.equals(that.internalNote)
        : that.internalNote != null) {
      return false;
    }
    if (externalNote != null ? !externalNote.equals(that.externalNote)
        : that.externalNote != null) {
      return false;
    }
    if (collectionDate != null ? !collectionDate.equals(that.collectionDate)
        : that.collectionDate != null) {
      return false;
    }
    if (transactionDate != null ? !transactionDate.equals(that.transactionDate)
        : that.transactionDate != null) {
      return false;
    }
    if (observationDate != null ? !observationDate.equals(that.observationDate)
        : that.observationDate != null) {
      return false;
    }
    if (versionDate != null ? !versionDate.equals(that.versionDate) : that.versionDate != null) {
      return false;
    }
    if (observations != null ? !observations.equals(that.observations)
        : that.observations != null) {
      return false;
    }
    return metadata != null ? metadata.equals(that.metadata) : that.metadata == null;
  }

  @Override
  public int hashCode() {
    int result = groupId;
    result = 31 * result + (baseGroupId != null ? baseGroupId.hashCode() : 0);
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (testId != null ? testId.hashCode() : 0);
    result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
    result = 31 * result + (orderingProviderId != null ? orderingProviderId.hashCode() : 0);
    result = 31 * result + (active ? 1 : 0);
    result = 31 * result + (internalNote != null ? internalNote.hashCode() : 0);
    result = 31 * result + (externalNote != null ? externalNote.hashCode() : 0);
    result = 31 * result + (collectionDate != null ? collectionDate.hashCode() : 0);
    result = 31 * result + (transactionDate != null ? transactionDate.hashCode() : 0);
    result = 31 * result + (observationDate != null ? observationDate.hashCode() : 0);
    result = 31 * result + (versionDate != null ? versionDate.hashCode() : 0);
    result = 31 * result + (reviewWithPatient ? 1 : 0);
    result = 31 * result + (observations != null ? observations.hashCode() : 0);
    result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
    return result;
  }
}
