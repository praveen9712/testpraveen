/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.security.apicontext.ApiSecurityContextBuilder;

/**
 * @author Blake Dickie
 */
public class ApiSecurityManager {

  private final ThreadLocal<ApiSecurityContext> securityContexts = new ThreadLocal<>();

  private ApiSecurityManager() {
  }
  // </editor-fold>

  public static ApiSecurityManager getInstance() {
    return SingletonInstance.instance;
  }

  public ApiSecurityContext startSecurityContext(ApiSecurityContextBuilder builder) {
    ApiSecurityContext result = builder.createApiSecurityContext();
    securityContexts.set(result);
    return result;
  }

  public void endSecurityContext() {
    securityContexts.remove();
  }

  public ApiSecurityContext getCurrentSecurityContext() {
    return securityContexts.get();
  }

  // <editor-fold defaultstate="collapsed" desc=" Singleton ">
  // The following is appearently the best way to make a thread-safe singleton.
  private static class SingletonInstance {

    private static final ApiSecurityManager instance = new ApiSecurityManager();
  }
}
