/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.filter;

import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.restapi.security.ApiSecurityManager;
import com.qhrtech.emr.restapi.security.apicontext.AccuroApiContextService;
import com.qhrtech.emr.restapi.security.apicontext.AccuroApiTokenAdapter;
import com.qhrtech.emr.restapi.security.apicontext.ApiSecurityContextBuilder;
import com.qhrtech.emr.restapi.security.apicontext.TokenRequestDetails;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet filter responsible for loading all user environment information required for provider /
 * client credentials endpoint calls.
 */
public class ApiContextLoadFilter implements Filter {

  @Autowired
  private AccuroApiContextService accuroApiContextService;

  @Override
  public void init(FilterConfig fc) {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
        this,
        fc.getServletContext());
  }

  @Override
  public void destroy() {
  }


  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain fc) throws IOException, ServletException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (!(auth instanceof OAuth2Authentication)) {
      fc.doFilter(request, response);
      return;
    }

    OAuth2Authentication oauth = (OAuth2Authentication) auth;

    String url = ((SecurityContextHolderAwareRequestWrapper) request).getPathInfo();
    String requestType = ((SecurityContextHolderAwareRequestWrapper) request).getMethod();
    String tenantId = request.getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    TokenRequestDetails tokenRequestDetails =
        new TokenRequestDetails(HttpMethod.valueOf(requestType), url, tenantId);

    AccuroApiTokenAdapter accuroApiToken = new AccuroApiTokenAdapter(oauth, tokenRequestDetails);
    AccuroApiContext userContext;
    userContext =
        accuroApiContextService.getAccuroApiUserContext(accuroApiToken);

    ApiSecurityContextBuilder apiSecurityContextBuilder =
        ApiSecurityContextBuilder.build(accuroApiToken, userContext);

    try {
      ApiSecurityManager.getInstance().startSecurityContext(apiSecurityContextBuilder);
      fc.doFilter(request, response);
    } finally {
      ApiSecurityManager.getInstance().endSecurityContext();
    }
  }
}
