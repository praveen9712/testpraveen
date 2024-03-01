/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.models.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webcohesion.enunciate.metadata.Facet;
import java.util.Objects;

/**
 * This functionality has been deprecated.
 *
 * Jonoke Patient Link
 *
 * @author kevin.kendall
 *
 * @deprecated No alternative, all Jonoke work flows are no longer in use.
 */
@Deprecated
@Facet("internal")
public class JonokeLink {

  // Jonoke Username
  @JsonProperty("jonoke_login")
  private String username;

  // Jonoke Password
  @JsonProperty("jonoke_password")
  private String jonokePassword;

  // IDP Phone Number
  @JsonProperty("phone_number")
  private String phoneNumber;

  // IDP Email
  private String email;

  // IDP Password
  private String password;

  /**
   * Jonoke Username
   *
   * @return
   */
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Jonoke Password
   *
   * @return
   */
  public String getJonokePassword() {
    return jonokePassword;
  }

  public void setJonokePassword(String jonokePassword) {
    this.jonokePassword = jonokePassword;
  }

  /**
   * Phone Number to use for IDP
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
    hash = 89 * hash + Objects.hashCode(this.username);
    hash = 89 * hash + Objects.hashCode(this.jonokePassword);
    hash = 89 * hash + Objects.hashCode(this.phoneNumber);
    hash = 89 * hash + Objects.hashCode(this.email);
    hash = 89 * hash + Objects.hashCode(this.password);
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
    final JonokeLink other = (JonokeLink) obj;
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.jonokePassword, other.jonokePassword)) {
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
