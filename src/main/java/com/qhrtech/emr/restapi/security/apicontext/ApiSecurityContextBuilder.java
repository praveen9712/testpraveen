
package com.qhrtech.emr.restapi.security.apicontext;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.security.AccuroAuthorizationContext;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.PatientUserDetails;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class ApiSecurityContextBuilder {

  public static final String ACCURO_REST_API = "Accuro Rest API";
  public static final String OAUTH = "oauth:";

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
  private AccuroApiTokenType tokenType;

  private ApiSecurityContextBuilder withOauthClientId(String oauthClientId) {
    this.oauthClientId = oauthClientId;
    return this;
  }

  private ApiSecurityContextBuilder withUser(AuditLogUser user) {
    this.user = user;
    return this;
  }

  private ApiSecurityContextBuilder withPatientId(Integer patientId) {
    this.patientId = patientId;
    return this;
  }

  private ApiSecurityContextBuilder withSessionUuid(UUID sessionUuid) {
    this.sessionUuid = sessionUuid;
    return this;
  }

  private ApiSecurityContextBuilder withTenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  private ApiSecurityContextBuilder withComputerInfo(String computerInfo) {
    this.computerInfo = computerInfo;
    return this;
  }

  private ApiSecurityContextBuilder withAccuroApiContext(AccuroApiContext accuroApiContext) {
    this.accuroApiContext = accuroApiContext;
    return this;
  }

  private ApiSecurityContextBuilder withAuthorizationContext(
      AuthorizationContext authorizationContext) {
    this.authorizationContext = authorizationContext;
    return this;
  }

  private ApiSecurityContextBuilder withScopes(Set<String> scopes) {
    this.scopes = Collections.unmodifiableSet(scopes);
    return this;
  }

  private ApiSecurityContextBuilder withGrantType(String grantType) {
    this.grantType = grantType;
    return this;
  }

  private ApiSecurityContextBuilder withTokenType(AccuroApiTokenType tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  public ApiSecurityContext createApiSecurityContext() {
    return new ApiSecurityContext(oauthClientId, user, patientId, sessionUuid, tenantId,
        computerInfo, accuroApiContext, authorizationContext, scopes, grantType);
  }

  private static AuditLogUser buildAuditLogUser(Integer userId, String clientId,
      String computerInfo) {
    AuditLogUser user = new AuditLogUser(userId, 0, ACCURO_REST_API,
        OAUTH + clientId, computerInfo);

    return user;
  }

  private static UUID buildSessionUuid(String clientId, Integer userId) {
    String nameBase =
        clientId.concat(String.format("%020d", userId));

    return UUID.nameUUIDFromBytes(nameBase.getBytes());
  }

  public static ApiSecurityContextBuilder build(AccuroApiTokenAdapter accuroApiToken,
      AccuroApiContext accuroApiContext) {
    ApiSecurityContextBuilder apiSecurityContextBuilder = new ApiSecurityContextBuilder();
    apiSecurityContextBuilder.withComputerInfo(accuroApiToken.getComputerInfo());
    apiSecurityContextBuilder.withTenantId(accuroApiToken.getTenantId());
    apiSecurityContextBuilder.withOauthClientId(accuroApiToken.getClientId());

    Integer userId = null;
    if (null != accuroApiContext && null != accuroApiContext.getAccuroUser()) {
      userId = accuroApiContext.getAccuroUser().getUserId();
    }

    // these fields are context specific
    apiSecurityContextBuilder.withUser(buildAuditLogUser(userId,
        accuroApiToken.getClientId(), accuroApiToken.getComputerInfo()));
    apiSecurityContextBuilder.withSessionUuid(buildSessionUuid(accuroApiToken.getClientId(),
        userId));
    if (null != accuroApiContext && accuroApiContext.getUserPermissions() != null) {
      apiSecurityContextBuilder.withAuthorizationContext(
          new AccuroAuthorizationContext(accuroApiContext.getUserPermissions()));
    }
    apiSecurityContextBuilder.withAccuroApiContext(accuroApiContext);
    // end of user only things

    apiSecurityContextBuilder.withScopes(accuroApiToken.getScopes());
    apiSecurityContextBuilder.withGrantType(accuroApiToken.getGrantType());
    apiSecurityContextBuilder.withTokenType(accuroApiToken.getTokenType());
    // need scopes still

    OAuth2Authentication auth = accuroApiToken.getAuth();
    if (auth.getPrincipal() instanceof PatientUserDetails) {
      PatientUserDetails patientDetails = (PatientUserDetails) auth.getPrincipal();
      apiSecurityContextBuilder.withPatientId(patientDetails.getPatientId());
    }

    return apiSecurityContextBuilder;
  }
}
