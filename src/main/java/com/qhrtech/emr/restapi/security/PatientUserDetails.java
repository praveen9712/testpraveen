
package com.qhrtech.emr.restapi.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PatientUserDetails implements UserDetails {

  private final int patientId;
  private final String username;
  private final String tenantId;

  private PatientUserDetails(int patientId, String username, String tenantId) {
    this.patientId = patientId;
    this.username = username;
    this.tenantId = tenantId;
  }

  public int getPatientId() {
    return patientId;
  }

  public String getTenantId() {
    return tenantId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public String getPassword() {
    return "n/a";
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static class Builder {

    private Integer patientId;
    private String username;
    private String tenantId;

    public Builder setPatientId(int patientId) {
      this.patientId = patientId;
      return this;
    }

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder setTenantId(String tenantId) {
      this.tenantId = tenantId;
      return this;
    }

    public PatientUserDetails build() {
      if (patientId == null) {
        throw new NullPointerException("Patient cannot be null.");
      } else if (username == null) {
        throw new NullPointerException("Username cannot be null.");
      }
      return new PatientUserDetails(patientId, username, tenantId);
    }
  }
}

