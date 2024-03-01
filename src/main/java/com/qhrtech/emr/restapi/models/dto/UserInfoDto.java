
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;

/**
 * The UserInfo data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.security.UserInfo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Data Transfer Object for user information")
public class UserInfoDto {

  @JsonProperty("patientCredentialDetail")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PatientCredentialDetail patientCredentialDetail;

  @JsonProperty("authGrantTypeDetail")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private AuthGrantTypeDetail authGrantTypeDetail;

  @JsonProperty("oauthClientId")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "OAuth Client ID")
  private String oauthClientId;

  @JsonProperty("scopes")
  @Schema(description = "Scopes")
  private Set<String> scopes;

  @JsonProperty("grantType")
  @Schema(description = "Grant Type")
  private String grantType;

  @JsonProperty("tenant")
  @Schema(description = "Tenant")
  private String tenant;

  @JsonProperty("province")
  @Schema(description = "Province")
  private String province;

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public PatientCredentialDetail getPatientCredentialDetail() {
    return patientCredentialDetail;
  }

  public void setPatientCredentialDetail(
      PatientCredentialDetail patientCredentialDetail) {
    this.patientCredentialDetail = patientCredentialDetail;
  }

  public AuthGrantTypeDetail getAuthGrantTypeDetail() {
    return authGrantTypeDetail;
  }

  public void setAuthGrantTypeDetail(
      AuthGrantTypeDetail authGrantTypeDetail) {
    this.authGrantTypeDetail = authGrantTypeDetail;
  }

  public String getOauthClientId() {
    return oauthClientId;
  }

  public void setOauthClientId(String oauthClientId) {
    this.oauthClientId = oauthClientId;
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

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserInfoDto that = (UserInfoDto) o;

    if (!Objects.equals(patientCredentialDetail, that.patientCredentialDetail)) {
      return false;
    }
    if (!Objects.equals(authGrantTypeDetail, that.authGrantTypeDetail)) {
      return false;
    }
    if (!Objects.equals(oauthClientId, that.oauthClientId)) {
      return false;
    }
    if (!Objects.equals(scopes, that.scopes)) {
      return false;
    }
    if (!Objects.equals(grantType, that.grantType)) {
      return false;
    }
    if (!Objects.equals(tenant, that.tenant)) {
      return false;
    }
    return Objects.equals(province, that.province);
  }

  @Override
  public int hashCode() {
    int result = patientCredentialDetail != null ? patientCredentialDetail.hashCode() : 0;
    result = 31 * result + (authGrantTypeDetail != null ? authGrantTypeDetail.hashCode() : 0);
    result = 31 * result + (oauthClientId != null ? oauthClientId.hashCode() : 0);
    result = 31 * result + (scopes != null ? scopes.hashCode() : 0);
    result = 31 * result + (grantType != null ? grantType.hashCode() : 0);
    result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
    result = 31 * result + (province != null ? province.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("UserInfoDto{");
    sb.append("patientCredentialDetail=").append(patientCredentialDetail);
    sb.append(", authGrantTypeDetail=").append(authGrantTypeDetail);
    sb.append(", oauthClientId='").append(oauthClientId).append('\'');
    sb.append(", scopes=").append(scopes);
    sb.append(", grantType='").append(grantType).append('\'');
    sb.append(", tenant='").append(tenant).append('\'');
    sb.append(", province='").append(province).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
