
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckFromType;
import com.qhrtech.emr.restapi.validators.CheckFutureDate;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

/**
 * Document model object.
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Document data transfer object model")
public final class DocumentDto implements Serializable {

  @JsonProperty("document_id")
  @Schema(name = "document_id", description = "The unique document id", example = "1")
  private int documentId;

  @JsonProperty("patient_id")
  @Schema(
      name = "patient_id",
      description = "The unique id for the patient associated with the document. "
          + "For the referral document, this field is mandatory. "
          + "It's optional for other document types.",
      example = "1")
  private int patientId;

  @JsonProperty("file_name")
  @Schema(
      name = "file_name",
      description = "Document file name. "
          + "This field has relevance in the document read request only. "
          + "With document write request, file name would be parsed from the document meta data "
          + "which is sent in the headers of multipart file request. "
          + "Valid file extensions are: PDF, JPEG or JPG.",
      example = "SampleFile.pdf")
  private String fileName;

  @Size(max = 500, message = "Description cannot be more than 500 characters.")
  @JsonProperty("description")
  @Schema(
      description = "Description of document. It is optional field and cannot be more than "
          + "500 characters.",
      example = "A description of the example file.")
  private String description;

  @JsonProperty("accuro_file_type_id")
  @Schema(
      name = "accuro_file_type_id",
      description = "ID of the Accuro file type for the Document. This field has relevance "
          + "only in the read request.",
      example = "8")
  private Integer accuroFileType;

  @JsonProperty("document_date")
  @CheckFutureDate
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(
      name = "document_date",
      description = "The date the document was originally uploaded. This field has relevance "
          + "only in the read request.",
      example = "2018-10-23T00:00:00.000", type = "string")
  private LocalDateTime documentDate;


  @CheckFutureDate
  @JsonProperty("received_date")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(
      name = "received_date",
      description = "The date the document was received. If the field is null, current system date "
          + "would be set corresponding to this field. This field shows up on the Accuro. "
          + "The value cannot be in future.",
      example = "2018-10-23T00:00:00.000", type = "string")
  private LocalDateTime receivedDate;


  @CheckFutureDate
  @JsonProperty("date_created")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(
      name = "date_created",
      description = "Date the document was created. For REFERRAL documents this field is set by "
          + "the system during the write request. For non-REFERRAL documents, system will take "
          + "the value sent by client or set to systems date if the value is null. The value "
          + "cannot be in future.",
      example = "2018-10-23", type = "string")
  private LocalDateTime dateCreated;


  @Min(value = 1, message = "Folder Id can not be null or zero.")
  @JsonProperty("folder_id")
  @Schema(
      name = "folder_id",
      description = "Id of the document folder name. This is mandatory field in the "
          + "document write request.",
      example = "1")
  private int folderId;

  @JsonProperty("folder")
  @Schema(
      name = "folder",
      description = "Folder name of the document. This field is relevant only for the read request."
          + "It is ignored during document write request.",
      example = "Documents")
  private String pathType;

  @JsonProperty("sub_folder_id")
  @Schema(
      name = "sub_folder_id",
      description = "Id of the document sub folder name(also called sub type). "
          + "This is optional field in the document write requests. "
          + "If this field is populated in the write request, "
          + "make sure to enable the preference to view sub folder in Accuro.",
      example = "1")
  private Integer subtypeId;

  @JsonProperty("sub_folder")
  @Schema(
      name = "sub_folder",
      description = "The sub folder name(also called sub type) of the document. "
          + "This field is relevant only for the read request. "
          + "It is ignored during document write request.",
      example = "Reports")
  private String subtype;

  @JsonProperty("from_id")
  @Schema(name = "from_id", description = "The Id of the documents sender", example = "1")
  private Integer fromId;

  @CheckFromType
  @JsonProperty("from_type")
  @Schema(
      name = "from_type",
      description = "The senders type. In the write request, this field can send null or any one "
          + "of these values: Physician, Insurer, Contact, Patient, OneTimeRecipient. "
          + "This field can be null but not blank.",
      example = "physician")
  private String fromType;

  @JsonProperty("from_name")
  @Schema(
      name = "from_name",
      description = "The name of the documents sender. For the referral documents, "
          + "this is mandatory field but optional for other document types. If this field is "
          + "populated in the write request, make sure to enable the preference to view **From**"
          + " field in Accuro.",
      example = "Dr. Jane Doe")
  private String fromName;


  @Valid
  @JsonProperty("reviews")
  @Schema(
      title = "List of DocumentReviewDto",
      description = "Collection of DocumentReviewDto object which contains physician Id and "
          + "reviewed date. For referral documents, at-least one physician Id is mandatory. "
          + "For other documents types, this collection can be left null. "
          + "If reviewed date is provided, physician Id becomes mandatory field. "
          + "If physician Id is provided in the request and review date is kept null, it means the "
          + "document has not been reviewed yet. For the referral documents, review date for the "
          + "corresponding physician date will always with set null by the system. ",
      example = "[{\"physician_id\" :12129, \"reviewDate\" : \"2018-12-29\"},"
          + " {\"physician_id\" :12130, \"reviewDate\" : \"2018-12-30\"}]",
      implementation = DocumentReviewDto.class)
  Set<DocumentReviewDto> reviews;


  @Valid
  @Min(value = 1, message = "Priority should be between 1 - 3.")
  @Max(value = 3, message = "Priority should be between 1 - 3.")
  @JsonProperty("priority")
  @Schema(
      description = "Priority of the document. This is optional field, 3 being the highest and "
          + "1 lowest. By default the priority of the document is normal i.e 1.",
      example = "2")
  private Integer priority;

  /**
   * Collection of {@link DocumentReviewDto} object which contains physician Id and reviewed Date.
   * For Referral documents, at-least one physician Id is mandatory. For other documents types, this
   * collection can be left null. If reviewed Date is provided, physician Id becomes mandatory
   * field. If physician Id is provided in the request and review Date is kept null, it means the
   * document has not been reviewed yet.
   *
   * @return Set of reviews {@link DocumentReviewDto}
   * @documentationExample [{"physician_id" :12129, "reviewDate" : "2018-12-29"}]
   */

  public Set<DocumentReviewDto> getReviews() {
    return reviews;
  }

  public void setReviews(Set<DocumentReviewDto> reviews) {
    this.reviews = reviews;
  }

  /**
   * Priority of the document. This is optional field, 3 being the highest and 1 lowest. By default
   * the priority of the document is normal i.e 1.
   *
   * @return Priority
   * @documentationExample 2
   */
  public Integer getPriority() {
    return priority;
  }


  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * Unique Document ID
   *
   * @return Document ID
   * @documentationExample 1
   */
  public int getDocumentId() {
    return documentId;
  }

  public void setDocumentId(int documentId) {
    this.documentId = documentId;
  }

  /**
   * Unique ID for the patient associated with the Document. For the referral document, this field
   * is mandatory and for rest of the document types, it is optional.
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
   * Document file name. This field has relevance in the read document read request only. While
   * document write request, file name would be parsed from the document meta date which is sent in
   * the headers of Multipart File request. File Name should only have .pdf extension.
   *
   * @return File name
   * @documentationExample SampleFile.pdf
   */
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Description of document. It is optional field and cannot be more than 500 characters.
   *
   * @return Description
   * @documentationExample A description of the example file.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * ID of the Accuro file type for the Document. This field has relevance only in the read request.
   *
   * @return File type ID
   * @documentationExample 8
   */
  public Integer getAccuroFileType() {
    return accuroFileType;
  }

  public void setAccuroFileType(Integer accuroFileType) {
    this.accuroFileType = accuroFileType;
  }

  /**
   * The date the document was originally uploaded. This field has relevance only in the read
   * request.
   *
   * @return Date
   * @documentationExample "2018-12-11"
   */

  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDocumentDate() {
    return documentDate;
  }

  public void setDocumentDate(LocalDateTime documentDate) {
    this.documentDate = documentDate;
  }

  /**
   * The date the document was received. If the field is null, current system date would be set
   * corresponding to this field. This field shows up on the Accuro. The value cannot be in future.
   *
   * @return Date
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getReceivedDate() {
    return receivedDate;
  }

  public void setReceivedDate(LocalDateTime receivedDate) {
    this.receivedDate = receivedDate;
  }

  /**
   * Date the document was created. For REFERRAL documents this field is set by the system during
   * the write request. For non-REFERRAL documents, system will take the value sent by client or set
   * to systems date if the value is null. The value cannot be in future.
   *
   * @return Date
   */
  @DocumentationExample("2018-12-19")
  @TypeHint(String.class)
  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }


  /**
   * The ID of the documents sender.
   *
   * @return From ID
   * @documentationExample 1
   */
  public Integer getFromId() {
    return fromId;
  }

  public void setFromId(Integer fromId) {
    this.fromId = fromId;
  }

  /**
   * The senders type. In the write request, this field can send null or any one of these these
   * values: Physician, Insurer, Contact, Patient, OneTimeRecipient. This field can be null but not
   * blank.
   *
   * @return From type
   * @documentationExample physician
   */
  public String getFromType() {
    return fromType;
  }

  public void setFromType(String fromType) {
    this.fromType = fromType;
  }

  /**
   * The name of the documents sender. For the referral documents, this is mandatory field but
   * optional field for the other document types. If this field is populated in the write request,
   * make sure to enable the preference to view From field in Accuro.
   *
   * @return From name
   * @documentationExample Dr. Jane Doe
   */
  public String getFromName() {
    return fromName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }


  /**
   * Folder Id of the Document Folder Name. This is mandatory field in the document write request.
   *
   * @return Folder Id
   * @documentationExample 1
   */
  public int getFolderId() {
    return folderId;
  }

  public void setFolderId(int folderId) {
    this.folderId = folderId;
  }


  /**
   * Id of the Document Sub Folder Name( also called SubType). This is optional field in the
   * document write requests. If this field is populated in the write request, make sure to enable
   * the preference to view sub folder in Accuro.
   *
   * @return Sub Folder Id
   * @documentationExample 1
   */
  public Integer getSubtypeId() {
    return subtypeId;
  }

  public void setSubtypeId(Integer subtypeId) {
    this.subtypeId = subtypeId;
  }

  /**
   * Folder Name of the document. This field is relevant only for the read request. It is ignored
   * during document write request.
   *
   * @return Folder Name
   * @documentationExample Documents
   */
  public String getPathType() {
    return pathType;
  }


  public void setPathType(String pathType) {
    this.pathType = pathType;
  }

  /**
   * Sub-folder name(also called subType) of the document. This field is relevant only for the read
   * request. It is ignored during document write request.
   *
   * @return Sub Folder Name
   * @documentationExample Reports
   */
  public String getSubtype() {
    return subtype;
  }

  public void setSubtype(String subtype) {
    this.subtype = subtype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DocumentDto)) {
      return false;
    }
    DocumentDto that = (DocumentDto) o;
    return getDocumentId() == that.getDocumentId()
        && getPatientId() == that.getPatientId()
        && getFolderId() == that.getFolderId()
        && Objects.equals(getFileName(), that.getFileName())
        && Objects.equals(getDescription(), that.getDescription())
        && Objects.equals(getAccuroFileType(), that.getAccuroFileType())
        && Objects.equals(getDocumentDate(), that.getDocumentDate())
        && Objects.equals(getReceivedDate(), that.getReceivedDate())
        && Objects.equals(getDateCreated(), that.getDateCreated())
        && Objects.equals(getSubtypeId(), that.getSubtypeId())
        && Objects.equals(getPathType(), that.getPathType())
        && Objects.equals(getSubtype(), that.getSubtype())
        && Objects.equals(getFromId(), that.getFromId())
        && Objects.equals(getFromType(), that.getFromType())
        && Objects.equals(getFromName(), that.getFromName())
        && Objects.equals(getReviews(), that.getReviews())
        && Objects.equals(getPriority(), that.getPriority());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getDocumentId(), getPatientId(), getFileName(), getDescription(), getAccuroFileType(),
            getDocumentDate(), getReceivedDate(), getDateCreated(), getFolderId(), getSubtypeId(),
            getPathType(), getSubtype(), getFromId(), getFromType(), getFromName(), getReviews(),
            getPriority());
  }

  @Override
  public String toString() {
    return "DocumentDto{"
        + "documentId=" + documentId
        + ", patientId=" + patientId
        + ", fileName='" + fileName + '\''
        + ", description='" + description + '\''
        + ", accuroFileType=" + accuroFileType
        + ", documentDate=" + documentDate
        + ", receivedDate=" + receivedDate
        + ", dateCreated=" + dateCreated
        + ", folderId=" + folderId
        + ", subtypeId=" + subtypeId
        + ", pathType='" + pathType + '\''
        + ", subtype='" + subtype + '\''
        + ", fromId=" + fromId
        + ", fromType='" + fromType + '\''
        + ", fromName='" + fromName + '\''
        + ", reviews=" + reviews
        + ", priority=" + priority
        + '}';
  }
}
