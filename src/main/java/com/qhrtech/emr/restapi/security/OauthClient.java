
package com.qhrtech.emr.restapi.security;

public class OauthClient {

  private String id;

  private String name;

  private String secret;

  private String scopes;

  private String grantTypes;

  private String redirects;

  private Long accessTokenValidity;

  private Long refreshTokenValidity;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getScopes() {
    return scopes;
  }

  public void setScopes(String scopes) {
    this.scopes = scopes;
  }

  public String getGrantTypes() {
    return grantTypes;
  }

  public void setGrantTypes(String grantTypes) {
    this.grantTypes = grantTypes;
  }

  public String getRedirects() {
    return redirects;
  }

  public void setRedirects(String redirects) {
    this.redirects = redirects;
  }

  public Long getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(Long accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public Long getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  public void setRefreshTokenValidity(Long refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OauthClient that = (OauthClient) o;

    if (!getId().equals(that.getId())) {
      return false;
    }
    if (!getName().equals(that.getName())) {
      return false;
    }
    if (!getSecret().equals(that.getSecret())) {
      return false;
    }
    if (getScopes() != null ? !getScopes().equals(that.getScopes()) : that.getScopes() != null) {
      return false;
    }
    if (!getGrantTypes().equals(that.getGrantTypes())) {
      return false;
    }
    if (!getRedirects().equals(that.getRedirects())) {
      return false;
    }
    if (getAccessTokenValidity() != null ? !getAccessTokenValidity().equals(
        that.getAccessTokenValidity()) : that.getAccessTokenValidity() != null) {
      return false;
    }
    return getRefreshTokenValidity() != null ? getRefreshTokenValidity().equals(
        that.getRefreshTokenValidity()) : that.getRefreshTokenValidity() == null;
  }

  @Override
  public int hashCode() {
    int result = getId().hashCode();
    result = 31 * result + getName().hashCode();
    result = 31 * result + getSecret().hashCode();
    result = 31 * result + (getScopes() != null ? getScopes().hashCode() : 0);
    result = 31 * result + getGrantTypes().hashCode();
    result = 31 * result + getRedirects().hashCode();
    result =
        31 * result + (getAccessTokenValidity() != null ? getAccessTokenValidity().hashCode() : 0);
    result =
        31 * result + (getRefreshTokenValidity() != null ? getRefreshTokenValidity().hashCode()
            : 0);
    return result;
  }
}
