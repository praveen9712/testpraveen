
package com.qhrtech.emr.restapi.util;

public enum AccapiScopes {
  /**
   * Ideally this scope has super admin access, which is as if user has both qhr-authorized-user and
   * qhr-authorized-client scopes
   */
  QHR_FIRST_PARTY("qhr-first-party"),

  /**
   * Ideally this scope provides access to provider portal URL's by bypassing the provider
   * permissions of the user. Even if the token doesn't have the user associated with it, still all
   * the provider permissions will be bypassed and the user can have access to all URL's
   */
  QHR_AUTHORIZED_USER("qhr-authorized-user"),

  /**
   * Ideally if this scope is present the authorized client table check should not be done. During
   * the presence of this scope if the token doesn't have a user associated with it then only public
   * endpoints can be accessible. If the token has user associated with it then regular provider
   * permissions based on the user are applied.
   */
  QHR_AUTHORIZED_CLIENT("qhr-authorized-client");


  private final String apiScope;

  AccapiScopes(String apiScope) {
    this.apiScope = apiScope;
  }

  public String getApiScope() {
    return apiScope;
  }

}
