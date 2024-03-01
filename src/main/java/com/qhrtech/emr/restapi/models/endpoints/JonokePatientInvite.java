/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.models.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.accuro.model.patient.PatientLoginDemographics;
import com.webcohesion.enunciate.metadata.Facet;
import java.util.Objects;

/**
 * This functionality has been deprecated.
 *
 * Jonoke Patient Invite
 *
 * @author kevin.kendall
 *
 * @deprecated No alternative, all Jonoke work flows are no longer in use.
 */
@Deprecated
@Facet("internal")
public class JonokePatientInvite {

  // Patient Demographics
  @JsonProperty("patient_demographics")
  private PatientLoginDemographics patientDemographics;

  // One Time Access Code
  private String accessCode;

  // IDP Phone Number
  private String phoneNumber;

  // IDP Email
  private String email;

  // IDP Password
  private String password;

  /**
   * Patient demographics for authentication
   *
   * @return
   */
  public PatientLoginDemographics getPatientDemographics() {
    return patientDemographics;
  }

  public void setPatientDemographics(PatientLoginDemographics patientDemographics) {
    this.patientDemographics = patientDemographics;
  }

  /**
   * One time access code from the Jonoke/Medeo sign up email
   *
   * @return
   */
  public String getAccessCode() {
    return accessCode;
  }

  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  /**
   * Phone number to use for IDP
   *
   * @return
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Email to use for IDP
   *
   * @return
   */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Password to use for IDP
   *
   * @return
   */
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + Objects.hashCode(this.patientDemographics);
    hash = 47 * hash + Objects.hashCode(this.accessCode);
    hash = 47 * hash + Objects.hashCode(this.phoneNumber);
    hash = 47 * hash + Objects.hashCode(this.email);
    hash = 47 * hash + Objects.hashCode(this.password);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final JonokePatientInvite other = (JonokePatientInvite) obj;
    if (!Objects.equals(this.patientDemographics, other.patientDemographics)) {
      return false;
    }
    if (!Objects.equals(this.accessCode, other.accessCode)) {
      return false;
    }
    if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    return Objects.equals(this.password, other.password);
  }

}
