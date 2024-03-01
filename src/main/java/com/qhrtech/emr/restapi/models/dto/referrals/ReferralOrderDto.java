
package com.qhrtech.emr.restapi.models.dto.referrals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

/**
 * Referral Order Data Transfer Object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferralOrderDto implements Serializable {

  @JsonProperty("id")
  private int id;

  @JsonProperty("patientId")
  private int patientId;

  @JsonProperty("physicianId")
  private int physicianId;

  @JsonProperty("order")
  private String order;

  @JsonProperty("primaryRecipient")
  private String primaryRecipient;

  @JsonProperty("ccRecipients")
  private String ccRecipients;

  @JsonProperty("chartItemId")
  private Integer chartItemId;

  @JsonProperty("chartItemType")
  private Integer chartItemType;

  @JsonProperty("date")
  private LocalDateTime date;

  @NotBlank
  @JsonProperty("orderStatus")
  private String orderStatus;

  @NotBlank
  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  @JsonProperty("specific")
  private String specific;

  @JsonProperty("location")
  private String location;

  @JsonProperty("lastModified")
  private LocalDateTime lastModified;

  @JsonProperty("repliformLastModified")
  private LocalDateTime repliformLastModified;

  @JsonProperty("modifiedByUser")
  private Integer modifiedByUser;

  @JsonProperty("reconciled")
  private boolean reconciled;

  @JsonProperty("bookedDate")
  private LocalDateTime bookedDate;

  @JsonProperty("contactType")
  private String contactType;

  @JsonProperty("contactId")
  private Integer contactId;

  @JsonProperty("erefOutboundId")
  private Integer erefOutboundId;

  @JsonProperty("appointmentDateTime")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime appointmentDateTime;

  @JsonProperty("appointmentTime")
  private String appointmentTime;

  @JsonProperty("faxLogId")
  private Integer faxLogId;

  @JsonProperty("erefApptStatus")
  private String erefApptStatus;

  @JsonProperty("appointmentId")
  private Integer appointmentId;

  /**
   * The unique id of the referral order.
   *
   * @return the id.
   * @documentationExample 1
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The patient id.
   *
   * @return the patient id.
   * @documentationExample 1
   */
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  /**
   * The physician id.
   *
   * @return the physician id.
   * @documentationExample 1
   */
  public int getPhysicianId() {
    return physicianId;
  }

  public void setPhysicianId(int physicianId) {
    this.physicianId = physicianId;
  }

  /**
   * This will be the list of these:
   * <ul>
   * <li>The recipient's name</li>
   * <li>The contact number</li>
   * <li>The referral order's name</li>
   * </ul>
   *
   * <p>
   * This will be separated by comma(,)
   * </p>
   *
   * @return The order.
   * @documentationExample David, 700-123-4567, test referral.
   */
  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  /**
   * The primary recipient. This will be one of these according to contact type.
   *
   * <ul>
   * <li>PHYSICIAN: physician id</li>
   * <li>CONTACT: contact id</li>
   * <li>INSURER: insurer id</li>
   * <li>0</li>
   * <li>-1</li>
   * </ul>
   *
   * @return The id of the primary recipient.
   * @documentationExample 1
   */
  public String getPrimaryRecipient() {
    return primaryRecipient;
  }

  public void setPrimaryRecipient(String primaryRecipient) {
    this.primaryRecipient = primaryRecipient;
  }

  /**
   * The list of CC's ids. The ids will be separated by comma(,)
   *
   * @return The CC's ids
   * @documentationExample 12345, 15444
   */
  public String getCcRecipients() {
    return ccRecipients;
  }

  public void setCcRecipients(String ccRecipients) {
    this.ccRecipients = ccRecipients;
  }

  /**
   * The chart item id related to generated letter(e-referral).
   *
   * @return The chart item id.
   * @documentationExample 1
   */
  public Integer getChartItemId() {
    return chartItemId;
  }

  public void setChartItemId(Integer chartItemId) {
    this.chartItemId = chartItemId;
  }

  /**
   * The chart item type.
   *
   * @return The chart item type.
   * @documentationExample 6
   */
  public Integer getChartItemType() {
    return chartItemType;
  }

  public void setChartItemType(Integer chartItemType) {
    this.chartItemType = chartItemType;
  }

  /**
   * The created date.
   *
   * @return The created date.
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * The referral order's status.
   *
   * @return the referral order's status.
   * @documentationExample ORDERED
   */
  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  /**
   * The referral order's type.
   *
   * @return the type.
   * @documentationExample Referral
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * The description of the referral order.
   *
   * @return the description.
   * @documentationExample Test Referral
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The specific details of the description.
   *
   * @return the specific details.
   * @documentationExample This order is for the test usage.
   */
  public String getSpecific() {
    return specific;
  }

  public void setSpecific(String specific) {
    this.specific = specific;
  }

  /**
   * The location.
   *
   * @return the location
   * @documentationExample location
   */
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * The last modified date.
   *
   * @return the last modified date.
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * The last modified date from the patient repliform table ( if Chart Item type is 3).
   *
   * @return the id.
   * @documentationExample 1
   */
  @DocumentationExample("2022-10-23T15:02:24.020")
  @TypeHint(String.class)
  public LocalDateTime getRepliformLastModified() {
    return repliformLastModified;
  }

  public void setRepliformLastModified(LocalDateTime repliformLastModified) {
    this.repliformLastModified = repliformLastModified;
  }

  /**
   * The user id who create/modify the referral order.
   *
   * @return the user id.
   * @documentationExample 1
   */
  public Integer getModifiedByUser() {
    return modifiedByUser;
  }

  public void setModifiedByUser(Integer modifiedByUser) {
    this.modifiedByUser = modifiedByUser;
  }

  /**
   * The flag if the referral order is reconciled.
   *
   * @return true if it is reconciled.
   * @documentationExample true
   */
  public boolean isReconciled() {
    return reconciled;
  }

  public void setReconciled(boolean reconciled) {
    this.reconciled = reconciled;
  }

  /**
   * The booked date.
   *
   * @return the booked date.
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getBookedDate() {
    return bookedDate;
  }

  public void setBookedDate(LocalDateTime bookedDate) {
    this.bookedDate = bookedDate;
  }

  /**
   * The contact type.
   * <ul>
   * <li>PHYSICIAN</li>
   * <li>CONTACT</li>
   * <li>INSURER</li>
   * </ul>
   *
   * @return the contact type.
   * @documentationExample PHYSICIAN
   */
  public String getContactType() {
    return contactType;
  }

  public void setContactType(String contactType) {
    this.contactType = contactType;
  }

  /**
   * The unique id of the contact.
   *
   * @return the contact id.
   * @documentationExample 1
   */
  public Integer getContactId() {
    return contactId;
  }

  public void setContactId(Integer contactId) {
    this.contactId = contactId;
  }

  /**
   * The unique id of the outbound e-referral.
   *
   * @return the outbound e-referral id.
   * @documentationExample 1
   */
  public Integer getErefOutboundId() {
    return erefOutboundId;
  }

  public void setErefOutboundId(Integer erefOutboundId) {
    this.erefOutboundId = erefOutboundId;
  }

  /**
   * The appointment datetime.
   *
   * @return the appointment datetime.
   */
  @DocumentationExample("2018-10-23T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getAppointmentDateTime() {
    return appointmentDateTime;
  }

  public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
    this.appointmentDateTime = appointmentDateTime;
  }

  /**
   * The appointment time.
   *
   * @return the appointment time.
   * @documentationExample 13:40
   */
  public String getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(String appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  /**
   * The fax log id.
   *
   * @return the fax log id.
   * @documentationExample 1
   */
  public Integer getFaxLogId() {
    return faxLogId;
  }

  public void setFaxLogId(Integer faxLogId) {
    this.faxLogId = faxLogId;
  }

  /**
   * The referral appointment status. It is to represent as 'eRef. Appt. Status'
   *
   * @return the appointment status.
   * @documentationExample BOOKED
   */
  public String getErefApptStatus() {
    return erefApptStatus;
  }

  public void setErefApptStatus(String erefApptStatus) {
    this.erefApptStatus = erefApptStatus;
  }

  /**
   * The referral appointment Id.'
   *
   * @return the appointment id of the referral order.
   * @documentationExample 1
   */
  public Integer getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Integer appointmentId) {
    this.appointmentId = appointmentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReferralOrderDto that = (ReferralOrderDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPatientId() != that.getPatientId()) {
      return false;
    }
    if (getPhysicianId() != that.getPhysicianId()) {
      return false;
    }
    if (isReconciled() != that.isReconciled()) {
      return false;
    }
    if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null) {
      return false;
    }
    if (getPrimaryRecipient() != null ? !getPrimaryRecipient().equals(that.getPrimaryRecipient())
        : that.getPrimaryRecipient() != null) {
      return false;
    }
    if (getCcRecipients() != null ? !getCcRecipients().equals(that.getCcRecipients())
        : that.getCcRecipients() != null) {
      return false;
    }
    if (getChartItemId() != null ? !getChartItemId().equals(that.getChartItemId())
        : that.getChartItemId() != null) {
      return false;
    }
    if (getChartItemType() != null ? !getChartItemType().equals(that.getChartItemType())
        : that.getChartItemType() != null) {
      return false;
    }
    if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) {
      return false;
    }
    if (getOrderStatus() != null ? !getOrderStatus().equals(that.getOrderStatus())
        : that.getOrderStatus() != null) {
      return false;
    }
    if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) {
      return false;
    }
    if (getDescription() != null ? !getDescription().equals(that.getDescription())
        : that.getDescription() != null) {
      return false;
    }
    if (getSpecific() != null ? !getSpecific().equals(that.getSpecific())
        : that.getSpecific() != null) {
      return false;
    }
    if (getLocation() != null ? !getLocation().equals(that.getLocation())
        : that.getLocation() != null) {
      return false;
    }
    if (getLastModified() != null ? !getLastModified().equals(that.getLastModified())
        : that.getLastModified() != null) {
      return false;
    }
    if (getRepliformLastModified() != null ? !getRepliformLastModified().equals(
        that.getRepliformLastModified()) : that.getRepliformLastModified() != null) {
      return false;
    }
    if (getModifiedByUser() != null ? !getModifiedByUser().equals(that.getModifiedByUser())
        : that.getModifiedByUser() != null) {
      return false;
    }
    if (getBookedDate() != null ? !getBookedDate().equals(that.getBookedDate())
        : that.getBookedDate() != null) {
      return false;
    }
    if (getContactType() != null ? !getContactType().equals(that.getContactType())
        : that.getContactType() != null) {
      return false;
    }
    if (getContactId() != null ? !getContactId().equals(that.getContactId())
        : that.getContactId() != null) {
      return false;
    }
    if (getErefOutboundId() != null ? !getErefOutboundId().equals(that.getErefOutboundId())
        : that.getErefOutboundId() != null) {
      return false;
    }
    if (getAppointmentDateTime() != null ? !getAppointmentDateTime().equals(
        that.getAppointmentDateTime()) : that.getAppointmentDateTime() != null) {
      return false;
    }
    if (getAppointmentTime() != null ? !getAppointmentTime().equals(that.getAppointmentTime())
        : that.getAppointmentTime() != null) {
      return false;
    }
    if (getFaxLogId() != null ? !getFaxLogId().equals(that.getFaxLogId())
        : that.getFaxLogId() != null) {
      return false;
    }
    if (getErefApptStatus() != null ? !getErefApptStatus().equals(that.getErefApptStatus())
        : that.getErefApptStatus() != null) {
      return false;
    }
    return getAppointmentId() != null ? getAppointmentId().equals(that.getAppointmentId())
        : that.getAppointmentId() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPatientId();
    result = 31 * result + getPhysicianId();
    result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
    result = 31 * result + (getPrimaryRecipient() != null ? getPrimaryRecipient().hashCode() : 0);
    result = 31 * result + (getCcRecipients() != null ? getCcRecipients().hashCode() : 0);
    result = 31 * result + (getChartItemId() != null ? getChartItemId().hashCode() : 0);
    result = 31 * result + (getChartItemType() != null ? getChartItemType().hashCode() : 0);
    result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
    result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getSpecific() != null ? getSpecific().hashCode() : 0);
    result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
    result = 31 * result + (getLastModified() != null ? getLastModified().hashCode() : 0);
    result =
        31 * result + (getRepliformLastModified() != null ? getRepliformLastModified().hashCode()
            : 0);
    result = 31 * result + (getModifiedByUser() != null ? getModifiedByUser().hashCode() : 0);
    result = 31 * result + (isReconciled() ? 1 : 0);
    result = 31 * result + (getBookedDate() != null ? getBookedDate().hashCode() : 0);
    result = 31 * result + (getContactType() != null ? getContactType().hashCode() : 0);
    result = 31 * result + (getContactId() != null ? getContactId().hashCode() : 0);
    result = 31 * result + (getErefOutboundId() != null ? getErefOutboundId().hashCode() : 0);
    result =
        31 * result + (getAppointmentDateTime() != null ? getAppointmentDateTime().hashCode() : 0);
    result = 31 * result + (getAppointmentTime() != null ? getAppointmentTime().hashCode() : 0);
    result = 31 * result + (getFaxLogId() != null ? getFaxLogId().hashCode() : 0);
    result = 31 * result + (getErefApptStatus() != null ? getErefApptStatus().hashCode() : 0);
    result = 31 * result + (getAppointmentId() != null ? getAppointmentId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ReferralOrderDto{");
    sb.append("id=").append(id);
    sb.append(", patientId=").append(patientId);
    sb.append(", physicianId=").append(physicianId);
    sb.append(", order='").append(order).append('\'');
    sb.append(", primaryRecipient='").append(primaryRecipient).append('\'');
    sb.append(", ccRecipients='").append(ccRecipients).append('\'');
    sb.append(", chartItemId=").append(chartItemId);
    sb.append(", chartItemType=").append(chartItemType);
    sb.append(", date=").append(date);
    sb.append(", orderStatus='").append(orderStatus).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", specific='").append(specific).append('\'');
    sb.append(", location='").append(location).append('\'');
    sb.append(", lastModified=").append(lastModified);
    sb.append(", repliformLastModified=").append(repliformLastModified);
    sb.append(", modifiedByUser=").append(modifiedByUser);
    sb.append(", reconciled=").append(reconciled);
    sb.append(", bookedDate=").append(bookedDate);
    sb.append(", contactType='").append(contactType).append('\'');
    sb.append(", contactId=").append(contactId);
    sb.append(", erefOutboundId=").append(erefOutboundId);
    sb.append(", appointmentDateTime=").append(appointmentDateTime);
    sb.append(", appointmentTime='").append(appointmentTime).append('\'');
    sb.append(", faxLogId=").append(faxLogId);
    sb.append(", erefApptStatus='").append(erefApptStatus).append('\'');
    sb.append(", appointmentId=").append(appointmentId);
    sb.append('}');
    return sb.toString();
  }
}
