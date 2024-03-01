
package com.qhrtech.emr.restapi.models.dto.waitlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Waitlist request object")
public class WaitlistRequestDto implements Serializable {

  @JsonProperty("id")
  @Schema(description = "Unique id for the waitlist request.")
  private int requestId;

  @JsonProperty("waitlistProviderId")
  @Schema(description = "Waitlist provider id. Only providers registered to waitlist.",
      example = "2")
  @NotNull
  private Integer waitlistProviderId;

  @JsonProperty("patientId")
  @Schema(description = "Unique id of the patient.", example = "12")
  @NotNull
  private Integer patientId;

  @JsonProperty("consultPriority")
  @Schema(description = "Valid values of consult priorities.", example = "Emergency")
  @Size(max = 255)
  @NotNull
  private String consultPriority;

  @JsonProperty("consultStatus")
  @Schema(description = "Valid values of consult statuses.", example = "Booked")
  @Size(max = 255)
  @NotNull
  private String consultStatus;

  @JsonProperty("consultType")
  @Schema(description = "Waitlist consult type", example = "EMG Consult")
  @Size(max = 255)
  @NotNull
  private String consultType;

  @JsonProperty("expedited")
  @Schema(description = "Request is expedited or not.", example = "false")
  private boolean expedited;

  @JsonProperty("providerTypeId")
  @Schema(description = "Provider type id", example = "12")
  private Integer providerTypeId;

  @JsonProperty("referralTriaged")
  private boolean referralTriaged;

  @JsonProperty("notes")
  @Schema(description = "Any notes for the waitlist request.")
  private String notes;

  @JsonProperty("complaint")
  @Schema(description = "Reason for referral")
  private String complaint;

  @JsonProperty("referringProviderId")
  private Integer referringProviderId;

  @JsonProperty("specificProviderRequested")
  @Schema(description = "Specific provider requested.", example = "true")
  private boolean providerRequested;

  @JsonProperty("firstAvailable")
  @Schema(description = "Book as first available", example = "false")
  private boolean firstAvailable;

  @JsonProperty("referredOut")
  @Schema(description = "If the patient is referred out.", example = "true")
  private boolean referredOut;

  @JsonProperty("consultRefusedReason")
  @Schema(description = "Refused reason for consult")
  @Size(max = 255)
  private String refusedReason;

  @JsonProperty("firstConsultDate")
  @Schema(description = "First consult date time", example = "2020-02-13T10:15:00.000")
  private LocalDateTime firstConsultDate;

  @JsonProperty("firstContactDate")
  @Schema(description = "First contact date time", example = "2020-02-12T13:50:00.000")
  private LocalDateTime firstContactDate;

  @JsonProperty("patientAvailableForConsult")
  @Schema(description = "Patient available date time for consult",
      example = "2020-02-13T00:00:00.000")
  private LocalDateTime consultPatientAvailableDate;

  @JsonProperty("tentativeDate")
  @Schema(description = "Tentative date time", example = "2020-02-14T14:20:00.000")
  private LocalDateTime tentativeDate;

  @JsonProperty("urgentDate")
  @Schema(description = "Urgent date time", example = "2020-02-15T13:30:00.000")
  private LocalDateTime urgentDate;

  @JsonProperty("referralDate")
  @Schema(description = "Referral date time", example = "2020-02-10T00:00:00.000")
  @NotNull
  private LocalDateTime referralDate;

  @JsonProperty("targetDate")
  @Schema(description = "Target date for waitlist", example = "2020-02-10T00:00:00.000")
  private LocalDateTime targetDate;

  @JsonProperty("requestDate")
  @Schema(description = "Request date for waitlist", example = "2020-02-10T00:00:00.000")
  private LocalDateTime bookedDate;

  @JsonProperty("altWaitlistProviderId")
  @Schema(description = "Alternative waitlist provider id", example = "1")
  private Integer altWaitlistProviderId;

  @JsonProperty("caseState")
  @Schema(description = "Case status", example = "Active")
  private String caseState;

  @JsonProperty("caseNumber")
  @Schema(description = "Case number", example = "1")
  private Integer caseNumber;

  @JsonProperty("caseCloseDate")
  @Schema(description = "Case closed date", example = "2020-02-10T00:00:00.000")
  private LocalDateTime caseCloseDate;

  @JsonProperty("followUp")
  @Schema(description = "Flag if request is follow-up or not", example = "false")
  private boolean followUp;

  @JsonProperty("bookedDate")
  @Schema(description = "Booked date for waitlist", example = "2020-02-10T00:00:00.000")
  private LocalDateTime confirmedDate;

  @JsonProperty("decisionDate")
  @Schema(description = "Decision date for waitlist", example = "2020-02-10T00:00:00.000")
  private LocalDateTime decisionDate;

  @JsonProperty("patientAvailableForSurgery")
  @Schema(description = "Patient available date time for surgery.",
      example = "2020-02-10T00:00:00.000")
  private LocalDateTime surgicalPatientAvailableDate;

  /**
   * The Id of waitlist request
   *
   * @return The requestId
   * @documentationExample 12
   */
  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  /**
   * The WaitlistProvider id, Only Providers registered for waitlist
   *
   * @return The waitlistProviderId
   * @documentationExample 12
   */
  public Integer getWaitlistProviderId() {
    return waitlistProviderId;
  }

  public void setWaitlistProviderId(Integer waitlistProviderId) {
    this.waitlistProviderId = waitlistProviderId;
  }

  /**
   * The Patient id
   *
   * @return The patientId
   * @documentationExample 12
   */
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  /**
   * The Valid values of consult priority
   *
   * @return The consultPriority
   * @documentationExample Emergency
   */
  public String getConsultPriority() {
    return consultPriority;
  }

  public void setConsultPriority(String consultPriority) {
    this.consultPriority = consultPriority;
  }

  /**
   * The Valid value of consult status
   *
   * @return The consultStatus
   * @documentationExample Booked
   */
  public String getConsultStatus() {
    return consultStatus;
  }

  public void setConsultStatus(String consultStatus) {
    this.consultStatus = consultStatus;
  }

  /**
   * The Waitlist consult type
   *
   * @return The consultType
   * @documentationExample EMG consult
   */
  public String getConsultType() {
    return consultType;
  }

  public void setConsultType(String consultType) {
    this.consultType = consultType;
  }

  /**
   * The Request is expedited or not
   *
   * @return {@code true} or {@code false}
   * @documentationExample false
   */
  public boolean isExpedited() {
    return expedited;
  }

  public void setExpedited(boolean expedited) {
    this.expedited = expedited;
  }

  /**
   * The Provider type id
   *
   * @return The providerTypeId
   * @documentationExample 1
   */
  public Integer getProviderTypeId() {
    return providerTypeId;
  }

  public void setProviderTypeId(Integer providerTypeId) {
    this.providerTypeId = providerTypeId;
  }

  /**
   * The Referral triaged
   *
   * @return {@code true} or {@code false}
   * @documentationExample false
   */
  public boolean isReferralTriaged() {
    return referralTriaged;
  }

  public void setReferralTriaged(boolean referralTriaged) {
    this.referralTriaged = referralTriaged;
  }

  /**
   * The Notes for waitlist request if any
   *
   * @return The notes
   * @documentationExample A note
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * The Reason for Referral
   *
   * @return The complaint
   * @documentationExample String
   */
  public String getComplaint() {
    return complaint;
  }

  public void setComplaint(String complaint) {
    this.complaint = complaint;
  }

  /**
   * The Referring Provider id
   *
   * @return referringProviderId
   * @documentationExample 1
   */
  public Integer getReferringProviderId() {
    return referringProviderId;
  }

  public void setReferringProviderId(Integer referringProviderId) {
    this.referringProviderId = referringProviderId;
  }

  /**
   * The Specific Provider requested
   *
   * @return {@code true} or {@code false}
   * @documentationExample false
   */
  public boolean isProviderRequested() {
    return providerRequested;
  }

  public void setProviderRequested(boolean providerRequested) {
    this.providerRequested = providerRequested;
  }

  /**
   * Book as first available
   *
   * @return {@code true} or {@code false}
   * @documentationExample true
   */
  public boolean isFirstAvailable() {
    return firstAvailable;
  }

  public void setFirstAvailable(boolean firstAvailable) {
    this.firstAvailable = firstAvailable;
  }

  /**
   * If the Patient is referred out
   *
   * @return {@code true} or {@code false}
   * @documentationExample false
   */
  public boolean isReferredOut() {
    return referredOut;
  }

  public void setReferredOut(boolean referredOut) {
    this.referredOut = referredOut;
  }

  /**
   * The Refused reason for consult
   *
   * @return The refusedReason
   * @documentationExample String
   */
  public String getRefusedReason() {
    return refusedReason;
  }

  public void setRefusedReason(String refusedReason) {
    this.refusedReason = refusedReason;
  }

  /**
   * The First consult date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getFirstConsultDate() {
    return firstConsultDate;
  }

  public void setFirstConsultDate(LocalDateTime firstConsultDate) {
    this.firstConsultDate = firstConsultDate;
  }

  /**
   * The First contact date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getFirstContactDate() {
    return firstContactDate;
  }

  public void setFirstContactDate(LocalDateTime firstContactDate) {
    this.firstContactDate = firstContactDate;
  }

  /**
   * The Patient available date time for consult
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getConsultPatientAvailableDate() {
    return consultPatientAvailableDate;
  }

  public void setConsultPatientAvailableDate(LocalDateTime consultPatientAvailableDate) {
    this.consultPatientAvailableDate = consultPatientAvailableDate;
  }

  /**
   * The Tentative date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTentativeDate() {
    return tentativeDate;
  }

  public void setTentativeDate(LocalDateTime tentativeDate) {
    this.tentativeDate = tentativeDate;
  }

  /**
   * The urgent date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getUrgentDate() {
    return urgentDate;
  }

  public void setUrgentDate(LocalDateTime urgentDate) {
    this.urgentDate = urgentDate;
  }

  /**
   * The Referral date time
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getReferralDate() {
    return referralDate;
  }

  public void setReferralDate(LocalDateTime referralDate) {
    this.referralDate = referralDate;
  }

  /**
   * The Target date for waitlist
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTargetDate() {
    return targetDate;
  }

  public void setTargetDate(LocalDateTime targetDate) {
    this.targetDate = targetDate;
  }

  /**
   * The Request date time for waitlist
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getBookedDate() {
    return bookedDate;
  }

  public void setBookedDate(LocalDateTime bookedDate) {
    this.bookedDate = bookedDate;
  }

  /**
   * The Alternative waitlist provider id
   *
   * @return The altWaitlistProviderId
   * @documentationExample 1
   */
  public Integer getAltWaitlistProviderId() {
    return altWaitlistProviderId;
  }

  public void setAltWaitlistProviderId(Integer altWaitlistProviderId) {
    this.altWaitlistProviderId = altWaitlistProviderId;
  }

  /**
   * The Case status
   *
   * @return The caseState
   * @documentationExample Active
   */
  public String getCaseState() {
    return caseState;
  }

  public void setCaseState(String caseState) {
    this.caseState = caseState;
  }

  /**
   * The Case number
   *
   * @return The caseNumber
   * @documentationExample 1
   */
  public Integer getCaseNumber() {
    return caseNumber;
  }

  public void setCaseNumber(Integer caseNumber) {
    this.caseNumber = caseNumber;
  }

  /**
   * The Case closed date
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getCaseCloseDate() {
    return caseCloseDate;
  }

  public void setCaseCloseDate(LocalDateTime caseCloseDate) {
    this.caseCloseDate = caseCloseDate;
  }

  /**
   * Flag if request is follow-up or not
   *
   * @return {@code true} or {@code false}
   * @documentationExample false
   */
  public boolean isFollowUp() {
    return followUp;
  }

  public void setFollowUp(boolean followUp) {
    this.followUp = followUp;
  }

  /**
   * The Booked Date for Waitlist
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getConfirmedDate() {
    return confirmedDate;
  }

  public void setConfirmedDate(LocalDateTime confirmedDate) {
    this.confirmedDate = confirmedDate;
  }

  /**
   * The Decision date for Waitlist
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDecisionDate() {
    return decisionDate;
  }

  public void setDecisionDate(LocalDateTime decisionDate) {
    this.decisionDate = decisionDate;
  }

  /**
   * The Patient availabe date time for surgery
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getSurgicalPatientAvailableDate() {
    return surgicalPatientAvailableDate;
  }

  public void setSurgicalPatientAvailableDate(LocalDateTime surgicalPatientAvailableDate) {
    this.surgicalPatientAvailableDate = surgicalPatientAvailableDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WaitlistRequestDto)) {
      return false;
    }

    WaitlistRequestDto that = (WaitlistRequestDto) o;

    if (getRequestId() != that.getRequestId()) {
      return false;
    }
    if (isExpedited() != that.isExpedited()) {
      return false;
    }
    if (isReferralTriaged() != that.isReferralTriaged()) {
      return false;
    }
    if (isProviderRequested() != that.isProviderRequested()) {
      return false;
    }
    if (isFirstAvailable() != that.isFirstAvailable()) {
      return false;
    }
    if (isReferredOut() != that.isReferredOut()) {
      return false;
    }
    if (isFollowUp() != that.isFollowUp()) {
      return false;
    }
    if (!Objects.equals(getWaitlistProviderId(), that.getWaitlistProviderId())) {
      return false;
    }
    if (!Objects.equals(getPatientId(), that.getPatientId())) {
      return false;
    }
    if (!Objects.equals(getConsultPriority(), that.getConsultPriority())) {
      return false;
    }
    if (!Objects.equals(getConsultStatus(), that.getConsultStatus())) {
      return false;
    }
    if (!Objects.equals(getConsultType(), that.getConsultType())) {
      return false;
    }
    if (!Objects.equals(getProviderTypeId(), that.getProviderTypeId())) {
      return false;
    }
    if (!Objects.equals(getNotes(), that.getNotes())) {
      return false;
    }
    if (!Objects.equals(getComplaint(), that.getComplaint())) {
      return false;
    }
    if (!Objects.equals(getReferringProviderId(), that.getReferringProviderId())) {
      return false;
    }
    if (!Objects.equals(getRefusedReason(), that.getRefusedReason())) {
      return false;
    }
    if (!Objects.equals(getFirstConsultDate(), that.getFirstConsultDate())) {
      return false;
    }
    if (!Objects.equals(getFirstContactDate(), that.getFirstContactDate())) {
      return false;
    }
    if (!Objects
        .equals(getConsultPatientAvailableDate(), that.getConsultPatientAvailableDate())) {
      return false;
    }
    if (!Objects.equals(getTentativeDate(), that.getTentativeDate())) {
      return false;
    }
    if (!Objects.equals(getUrgentDate(), that.getUrgentDate())) {
      return false;
    }
    if (!Objects.equals(getReferralDate(), that.getReferralDate())) {
      return false;
    }
    if (!Objects.equals(getTargetDate(), that.getTargetDate())) {
      return false;
    }
    if (!Objects.equals(getBookedDate(), that.getBookedDate())) {
      return false;
    }
    if (!Objects.equals(getAltWaitlistProviderId(), that.getAltWaitlistProviderId())) {
      return false;
    }
    if (!Objects.equals(getCaseState(), that.getCaseState())) {
      return false;
    }
    if (!Objects.equals(getCaseNumber(), that.getCaseNumber())) {
      return false;
    }
    if (!Objects.equals(getCaseCloseDate(), that.getCaseCloseDate())) {
      return false;
    }
    if (!Objects.equals(getConfirmedDate(), that.getConfirmedDate())) {
      return false;
    }
    if (!Objects.equals(getDecisionDate(), that.getDecisionDate())) {
      return false;
    }
    return Objects
        .equals(getSurgicalPatientAvailableDate(), that.getSurgicalPatientAvailableDate());
  }

  @Override
  public int hashCode() {
    int result = getRequestId();
    result =
        31 * result + (getWaitlistProviderId() != null ? getWaitlistProviderId().hashCode() : 0);
    result = 31 * result + (getPatientId() != null ? getPatientId().hashCode() : 0);
    result = 31 * result + (getConsultPriority() != null ? getConsultPriority().hashCode() : 0);
    result = 31 * result + (getConsultStatus() != null ? getConsultStatus().hashCode() : 0);
    result = 31 * result + (getConsultType() != null ? getConsultType().hashCode() : 0);
    result = 31 * result + (isExpedited() ? 1 : 0);
    result = 31 * result + (getProviderTypeId() != null ? getProviderTypeId().hashCode() : 0);
    result = 31 * result + (isReferralTriaged() ? 1 : 0);
    result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
    result = 31 * result + (getComplaint() != null ? getComplaint().hashCode() : 0);
    result =
        31 * result + (getReferringProviderId() != null ? getReferringProviderId().hashCode() : 0);
    result = 31 * result + (isProviderRequested() ? 1 : 0);
    result = 31 * result + (isFirstAvailable() ? 1 : 0);
    result = 31 * result + (isReferredOut() ? 1 : 0);
    result = 31 * result + (getRefusedReason() != null ? getRefusedReason().hashCode() : 0);
    result = 31 * result + (getFirstConsultDate() != null ? getFirstConsultDate().hashCode() : 0);
    result = 31 * result + (getFirstContactDate() != null ? getFirstContactDate().hashCode() : 0);
    result =
        31 * result + (getConsultPatientAvailableDate() != null ? getConsultPatientAvailableDate()
            .hashCode() : 0);
    result = 31 * result + (getTentativeDate() != null ? getTentativeDate().hashCode() : 0);
    result = 31 * result + (getUrgentDate() != null ? getUrgentDate().hashCode() : 0);
    result = 31 * result + (getReferralDate() != null ? getReferralDate().hashCode() : 0);
    result = 31 * result + (getTargetDate() != null ? getTargetDate().hashCode() : 0);
    result = 31 * result + (getBookedDate() != null ? getBookedDate().hashCode() : 0);
    result =
        31 * result + (getAltWaitlistProviderId() != null ? getAltWaitlistProviderId().hashCode()
            : 0);
    result = 31 * result + (getCaseState() != null ? getCaseState().hashCode() : 0);
    result = 31 * result + (getCaseNumber() != null ? getCaseNumber().hashCode() : 0);
    result = 31 * result + (getCaseCloseDate() != null ? getCaseCloseDate().hashCode() : 0);
    result = 31 * result + (isFollowUp() ? 1 : 0);
    result = 31 * result + (getConfirmedDate() != null ? getConfirmedDate().hashCode() : 0);
    result = 31 * result + (getDecisionDate() != null ? getDecisionDate().hashCode() : 0);
    result =
        31 * result + (getSurgicalPatientAvailableDate() != null ? getSurgicalPatientAvailableDate()
            .hashCode() : 0);
    return result;
  }
}
