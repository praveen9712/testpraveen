/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.models.endpoints;

import com.webcohesion.enunciate.metadata.Facet;
import java.util.Objects;

/**
 * This functionality has been deprecated.
 *
 * Jonoke Patient Login
 *
 * @author kevin.kendall
 *
 * @deprecated No alternative, all Jonoke work flows are no longer in use.
 */
@Deprecated
@Facet("internal")
public class JonokeLogin {

  // Jonoke or IDP Username
  private String username;

  // Jonoke or IDP Password
  private String password;

  /**
   * Jonoke or IDP username
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
   * Jonoke or IDP password
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
    hash = 73 * hash + Objects.hashCode(this.username);
    hash = 73 * hash + Objects.hashCode(this.password);
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
    final JonokeLogin other = (JonokeLogin) obj;
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    return Objects.equals(this.password, other.password);
  }

}
