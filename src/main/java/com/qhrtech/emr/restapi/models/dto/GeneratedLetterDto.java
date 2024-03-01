
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Generated letter data transfer object model")
public class GeneratedLetterDto {

  @JsonProperty("id")
  @Schema(description = "Unique identifier of the generated letter", example = "1")
  int id;

  @JsonProperty("letterId")
  @Schema(description = "The id from which the generated letter is generated", example = "1")
  int letterId;


  @JsonProperty("referralOrderId")
  @Schema(description = "The eReferral order id associated with this generated letter."
      + " Null if no eReferral order id associated", example = "12")
  Integer referralOrderId;

  @JsonProperty("letterVersion")
  @Schema(description = "The version of the letter", example = "2")
  int letterVersion;

  @JsonProperty("physicianId")
  @Schema(description = "The physician id", example = "15002")
  int physicianId;

  @JsonProperty("officeId")
  @Schema(description = "The office id", example = "10")
  Integer officeId;

  @JsonProperty("userId")
  @Schema(description = "The id of accuro user who creates the generated letter", example = "9")
  int userId;

  @JsonProperty("targetId")
  @Schema(description = "The target id for the generated letter", example = "8")
  int targetId;

  @JsonProperty("appointmentId")
  @Schema(description = "The appointment id", example = "11")
  Integer appointmentId;

  @JsonProperty("patientId")
  @Schema(description = "The patient id", example = "10")
  Integer patientId;

  @JsonProperty("cc")
  @Schema(description = "The flag indicating if the letter is copied to the target", example = "1")
  int cc;

  @JsonProperty("withCoverSheet")
  @Schema(description = "Indicates if the letter is with a cover sheet", example = "true")
  boolean withCoverSheet;

  @JsonProperty("finalized")
  @Schema(description = "Indicates if the letter is finalized", example = "false")
  boolean finalized;

  @JsonProperty("withAttachments")
  @Schema(description = "Indicates if the letter is with attachments", example = "true")
  boolean withAttachments;

  @JsonProperty("queue")
  @Schema(description = "Indicates if the letter is queued", example = "true")
  boolean queue;

  @JsonProperty("status")
  @Schema(description = "The status of the generated letter", example = "NO_FILE")
  ReferralStatus status;

  @JsonProperty("targetType")
  @Schema(description = "The type of the target", example = "Physician")
  String targetType;

  @JsonProperty("title")
  @Schema(description = "The title of the generated letter")
  String title;

  @JsonProperty("generateTo")
  @Schema(description = "The recipient name of the generated letter", example = "Doctor David")
  String generateTo;

  @JsonProperty("generatedTime")
  @Schema(description = "The timestamp of the generated Letter", type = "string",
      example = "2018-06-27T00:00:00.000")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  LocalDateTime generatedTime;

  @JsonProperty("username")
  @Schema(description = "The user name of the generated letter", example = "Doctor Bob")
  String username;



  @JsonProperty("extension")
  @Schema(description = "The extension of the generated letter", example = "pdf")
  String extension;

  @JsonIgnore
  @JsonProperty("letterContent")
  @JsonInclude(Include.NON_NULL)
  @Schema(description = "The binary content of the generated letter")
  byte[] letterContent;

  /**
   * Unique identifier for a Generated letter
   *
   * @documentationExample 11
   *
   * @return A unique Id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * A letter id which a generated letter is generated from.
   *
   * @documentationExample 12
   *
   * @return letter id
   */
  public int getLetterId() {
    return letterId;
  }

  public void setLetterId(int letterId) {
    this.letterId = letterId;
  }

  /**
   * The eReferral order id associated with this generated letter. Null if no eReferral order id
   * associated.
   *
   * @return referral order id
   * @documentationExample 12
   */
  public Integer getReferralOrderId() {
    return referralOrderId;
  }

  public void setReferralOrderId(Integer referralOrderId) {
    this.referralOrderId = referralOrderId;
  }

  /**
   * A version of a letter which a generated letter is generated from.
   *
   * @documentationExample 2
   *
   * @return letter version.
   */
  public int getLetterVersion() {
    return letterVersion;
  }

  public void setLetterVersion(int letterVersion) {
    this.letterVersion = letterVersion;
  }

  /**
   * A Physician whom a generated letter is from.
   *
   * @documentationExample 11
   *
   * @return physician id
   */
  public int getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(int physicianId) {
    this.physicianId = physicianId;
  }

  /**
   * Office id for a generated letter
   *
   * @documentationExample 10
   *
   * @return Office id
   */
  public Integer getOfficeId() {
    return officeId;
  }

  public void setOfficeId(Integer officeId) {
    this.officeId = officeId;
  }

  /**
   * An accuro user who create a generated letter
   *
   * @documentationExample 9
   *
   * @return user id
   */
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * A target source id for a generated letter.
   *
   * @documentationExample 11
   *
   * @return target id
   */
  public int getTargetId() {
    return targetId;
  }

  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }

  /**
   * An appointment id for a generated letter.
   *
   * @documentationExample 10
   *
   * @return appointment id
   */
  public Integer getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Integer appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * A patient id of a generated letter.
   *
   * @documentationExample 11
   *
   * @return patient id.
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * CC
   *
   * @documentationExample 0
   *
   * @return CC
   */
  public int getCc() {
    return cc;
  }

  public void setCc(int cc) {
    this.cc = cc;
  }

  /**
   * Is a generated letter with cover sheet.
   *
   * @documentationExample true
   *
   * @return true if is with cover sheet otherwise false.
   */
  public boolean isWithCoverSheet() {
    return withCoverSheet;
  }

  public void setWithCoverSheet(boolean withCoverSheet) {
    this.withCoverSheet = withCoverSheet;
  }

  /**
   * Is a generated letter finalized.
   *
   * @documentationExample 11
   *
   * @return true if it is finalized otherwise false.
   */
  public boolean isFinalized() {
    return finalized;
  }

  public void setFinalized(boolean finalized) {
    this.finalized = finalized;
  }

  /**
   * Is a generated letter with attachments.
   *
   * @documentationExample true
   *
   * @return true if is with Attachments otherwise false.
   */
  public boolean isWithAttachments() {
    return withAttachments;
  }

  public void setWithAttachments(boolean withAttachments) {
    this.withAttachments = withAttachments;
  }

  /**
   * Is queue for a generated letter .
   *
   * @documentationExample true
   *
   * @return true if it has a queue otherwise false.
   */
  public boolean isQueue() {
    return queue;
  }

  public void setQueue(boolean queue) {
    this.queue = queue;
  }

  /**
   * The status of a generated letter.
   *
   * @documentationExample sent/waiting/
   * @see GeneratedLetterStatus
   *
   * @return Status
   */
  public ReferralStatus getStatus() {
    return status;
  }

  public void setStatus(ReferralStatus status) {
    this.status = status;
  }

  /**
   * Type of target source.
   *
   * @documentationExample physician
   *
   * @return Target type
   */
  public String getTargetType() {
    return targetType;
  }

  public void setTargetType(String targetType) {
    this.targetType = targetType;
  }

  /**
   * Title of a generated letter
   *
   * @documentationExample A good title
   *
   * @return Title.
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Recipient name of a generated letter.
   *
   * @documentationExample Doctor David
   *
   * @return Name
   */
  public String getGenerateTo() {
    return generateTo;
  }

  public void setGenerateTo(String generateTo) {
    this.generateTo = generateTo;
  }

  /**
   * The timestamp of a generated Letter
   *
   * @return Generated time
   */
  @DocumentationExample("2024-06-27T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getGeneratedTime() {
    return generatedTime;
  }

  public void setGeneratedTime(LocalDateTime generatedTime) {
    this.generatedTime = generatedTime;
  }

  /**
   * User name for a generated letter.
   *
   * @documentationExample Doctor David
   *
   * @return user name
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * The file content of a generated letter
   *
   * @return letter file content.
   */
  public byte[] getLetterContent() {
    return letterContent;
  }

  public void setLetterContent(byte[] letterContent) {
    this.letterContent = letterContent;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GeneratedLetterDto)) {
      return false;
    }
    GeneratedLetterDto that = (GeneratedLetterDto) o;
    if (id != that.id) {
      return false;
    }
    if (letterId != that.letterId) {
      return false;
    }
    if (letterVersion != that.letterVersion) {
      return false;
    }
    if (physicianId != that.physicianId) {
      return false;
    }
    if (userId != that.userId) {
      return false;
    }
    if (targetId != that.targetId) {
      return false;
    }
    if (cc != that.cc) {
      return false;
    }
    if (withCoverSheet != that.withCoverSheet) {
      return false;
    }
    if (finalized != that.finalized) {
      return false;
    }
    if (withAttachments != that.withAttachments) {
      return false;
    }
    if (queue != that.queue) {
      return false;
    }
    if (referralOrderId != null ? !referralOrderId.equals(that.referralOrderId)
        : that.referralOrderId != null) {
      return false;
    }
    if (officeId != null ? !officeId.equals(that.officeId) : that.officeId != null) {
      return false;
    }
    if (appointmentId != null ? !appointmentId.equals(that.appointmentId)
        : that.appointmentId != null) {
      return false;
    }
    if (patientId != null ? !patientId.equals(that.patientId) : that.patientId != null) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (targetType != null ? !targetType.equals(that.targetType) : that.targetType != null) {
      return false;
    }
    if (title != null ? !title.equals(that.title) : that.title != null) {
      return false;
    }
    if (generateTo != null ? !generateTo.equals(that.generateTo) : that.generateTo != null) {
      return false;
    }
    if (generatedTime != null ? !generatedTime.equals(that.generatedTime)
        : that.generatedTime != null) {
      return false;
    }
    if (username != null ? !username.equals(that.username) : that.username != null) {
      return false;
    }
    if (extension != null ? !extension.equals(that.extension) : that.extension != null) {
      return false;
    }
    return Arrays.equals(letterContent, that.letterContent);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + letterId;
    result = 31 * result + letterVersion;
    result = 31 * result + physicianId;
    result = 31 * result + (referralOrderId != null ? referralOrderId.hashCode() : 0);
    result = 31 * result + (officeId != null ? officeId.hashCode() : 0);
    result = 31 * result + userId;
    result = 31 * result + targetId;
    result = 31 * result + (appointmentId != null ? appointmentId.hashCode() : 0);
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + cc;
    result = 31 * result + (withCoverSheet ? 1 : 0);
    result = 31 * result + (finalized ? 1 : 0);
    result = 31 * result + (withAttachments ? 1 : 0);
    result = 31 * result + (queue ? 1 : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (targetType != null ? targetType.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (generateTo != null ? generateTo.hashCode() : 0);
    result = 31 * result + (generatedTime != null ? generatedTime.hashCode() : 0);
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (extension != null ? extension.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(letterContent);
    return result;
  }

  @Override
  public String toString() {
    return "GeneratedLetterDto{"
        + "id=" + id
        + ", letterId=" + letterId
        + ", referralOrderId=" + referralOrderId
        + ", letterVersion=" + letterVersion
        + ", physicianId=" + physicianId
        + ", officeId=" + officeId
        + ", userId=" + userId
        + ", targetId=" + targetId
        + ", appointmentId=" + appointmentId
        + ", patientId=" + patientId
        + ", cc=" + cc
        + ", withCoverSheet=" + withCoverSheet
        + ", finalized=" + finalized
        + ", withAttachments=" + withAttachments
        + ", queue=" + queue
        + ", status=" + status
        + ", targetType='" + targetType + '\''
        + ", title='" + title + '\''
        + ", generateTo='" + generateTo + '\''
        + ", generatedTime=" + generatedTime
        + ", username='" + username + '\''
        + ", extension='" + extension + '\''
        + ", letterContent=" + Arrays.toString(letterContent)
        + '}';
  }
}
