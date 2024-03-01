/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.security.apicontext.AccuroApiToken;
import com.qhrtech.emr.restapi.security.apicontext.AccuroApiTokenType;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author kevin.kendall
 */
public class AccuroUserDetails implements UserDetails, AccuroApiToken {

  private static final long serialVersionUID = -9152359054264088592L;

  private final int userId;
  private final String username;
  private final String tenantId;

  private AccuroUserDetails(
      int userId,
      String username,
      String tenantId) {
    this.userId = userId;
    this.username = username;
    this.tenantId = tenantId;
  }

  public int getUserId() {
    return userId;
  }

  @Override
  public String getTenantId() {
    return tenantId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
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

  @Override
  public AccuroApiTokenType getTokenType() {
    return AccuroApiTokenType.LEGACY_PROVIDER;
  }

  @Override
  public String getUserIdentity() {
    return Integer.toString(getUserId());
  }

  public static class Builder {

    private Integer userId;
    private String username;
    private String tenantId;

    public Builder setUserId(int userId) {
      this.userId = userId;
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

    public AccuroUserDetails build() {
      if (userId == null) {
        throw new NullPointerException("User cannot be null.");
      } else if (username == null) {
        throw new NullPointerException("Username cannot be null.");
      }
      return new AccuroUserDetails(userId, username, tenantId);
    }
  }
}
