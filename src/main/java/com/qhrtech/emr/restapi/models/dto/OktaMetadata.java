
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OktaMetadata {

  @JsonProperty("issuer")
  private String issuer;

  @JsonProperty("jwks_uri")
  private String jwksUri;

  @JsonProperty("scopes_supported")
  private String[] scopesSupported;



  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getJwksUri() {
    return jwksUri;
  }

  public void setJwksUri(String jwksUri) {
    this.jwksUri = jwksUri;
  }

  public String[] getScopesSupported() {
    return scopesSupported;
  }

  public void setScopesSupported(String[] scopesSupported) {
    this.scopesSupported = scopesSupported;
  }



}
