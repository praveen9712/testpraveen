/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author kevin.kendall
 */
public class Registration {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("password")
  private String password;

  @JsonProperty("birthday")
  private Calendar birthDate;

  @JsonProperty("msp_number")
  private String phn;

  @JsonProperty("phone_number")
  private String phoneNumber;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Calendar getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Calendar birthDate) {
    this.birthDate = birthDate;
  }

  public String getPhn() {
    return phn;
  }

  public void setPhn(String phn) {
    this.phn = phn;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + Objects.hashCode(this.firstName);
    hash = 31 * hash + Objects.hashCode(this.lastName);
    hash = 31 * hash + Objects.hashCode(this.email);
    hash = 31 * hash + Objects.hashCode(this.password);
    hash = 31 * hash + Objects.hashCode(this.birthDate);
    hash = 31 * hash + Objects.hashCode(this.phn);
    hash = 31 * hash + Objects.hashCode(this.phoneNumber);
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
    final Registration other = (Registration) obj;
    if (!Objects.equals(this.firstName, other.firstName)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    if (!Objects.equals(this.birthDate, other.birthDate)) {
      return false;
    }
    if (!Objects.equals(this.phn, other.phn)) {
      return false;
    }
    return Objects.equals(this.phoneNumber, other.phoneNumber);
  }

}
