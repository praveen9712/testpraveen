
package com.qhrtech.emr.restapi.security;


import com.qhrtech.emr.restapi.security.apicontext.AccuroApiToken;
import com.qhrtech.emr.restapi.security.apicontext.AccuroApiTokenType;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class OktaUserDetails implements UserDetails, AccuroApiToken {

  private final String userIdentity;
  private final String tenant;
  private final AccuroApiTokenType tokenType;

  public OktaUserDetails(String userIdentity, String tenant, AccuroApiTokenType tokenType) {
    this.tenant = tenant;
    this.userIdentity = userIdentity;
    this.tokenType = tokenType;
  }

  /**
   * @param userIdentity the numeric id representing a third party client
   * @param tenant the tenant
   * @return an OktaUserDetails representing a third party token.
   */
  public static OktaUserDetails fromThirdPartyServiceUser(Integer userIdentity, String tenant) {
    return new OktaUserDetails(Integer.toString(userIdentity), tenant,
        AccuroApiTokenType.OKTA_THIRD_PARTY);
  }

  /**
   * @param userIdentity the alphanumeric identity tied to the SQUID identity system.
   * @param tenant the tenant
   * @return an OktaUserDetails representing a SQUID based identity
   */
  public static OktaUserDetails fromSquid(String userIdentity, String tenant) {
    return new OktaUserDetails(userIdentity, tenant, AccuroApiTokenType.OKTA_PROVIDER);
  }


  @Override
  public AccuroApiTokenType getTokenType() {
    return tokenType;
  }

  /**
   * @return the identifier which is stored in this token. What it represents depends on the value
   *         of {@link #getTokenType()}.
   */
  @Override
  public String getUserIdentity() {
    return userIdentity;
  }

  @Override
  public String getTenantId() {
    return tenant;
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
    return "n/a";
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

}
