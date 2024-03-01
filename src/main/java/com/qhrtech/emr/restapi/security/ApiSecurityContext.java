
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import java.util.Set;
import java.util.UUID;

public class ApiSecurityContext {

  private String oauthClientId;
  private AuditLogUser user;
  private Integer patientId;
  private UUID sessionUuid;
  private String tenantId;
  private String computerInfo;
  private AccuroApiContext accuroApiContext;
  private AuthorizationContext authorizationContext;
  private Set<String> scopes;
  private String grantType;

  public ApiSecurityContext(String oauthClientId,
      AuditLogUser user, Integer patientId, UUID sessionUuid, String tenantId,
      String computerInfo,
      AccuroApiContext accuroApiContext,
      AuthorizationContext authorizationContext, Set<String> scopes, String grantType) {
    this.oauthClientId = oauthClientId;
    this.user = user;
    this.patientId = patientId;
    this.sessionUuid = sessionUuid;
    this.tenantId = tenantId;
    this.computerInfo = computerInfo;
    this.accuroApiContext = accuroApiContext;
    this.authorizationContext = authorizationContext;
    this.scopes = scopes;
    this.grantType = grantType;
  }

  public AuthorizationContext getAuthorizationContext() {
    return authorizationContext;
  }

  public void setAuthorizationContext(AuthorizationContext authorizationContext) {
    this.authorizationContext = authorizationContext;
  }

  public String getComputerInfo() {
    return computerInfo;
  }

  public void setComputerInfo(String computerInfo) {
    this.computerInfo = computerInfo;
  }

  public AccuroApiContext getAccuroApiContext() {
    return accuroApiContext;
  }

  public void setAccuroApiContext(
      AccuroApiContext accuroApiContext) {
    this.accuroApiContext = accuroApiContext;
  }

  public ApiSecurityContext() {
  }

  public String getOauthClientId() {
    return oauthClientId;
  }

  public void setOauthClientId(String oauthClientId) {
    this.oauthClientId = oauthClientId;
  }

  public AuditLogUser getUser() {
    return user;
  }

  public void setUser(AuditLogUser user) {
    this.user = user;
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public UUID getSessionUuid() {
    return sessionUuid;
  }

  public void setSessionUuid(UUID sessionUuid) {
    this.sessionUuid = sessionUuid;
  }

  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> scopes) {
    this.scopes = scopes;
  }

  public String getGrantType() {
    return grantType;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ApiSecurityContext{ user=");
    sb.append(user == null ? null : user.getUserId());
    sb.append(" }");
    return sb.toString();
  }
}
