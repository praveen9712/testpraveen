/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.filter;

import com.qhrtech.emr.restapi.security.AccuroUserDetails;
import com.qhrtech.emr.restapi.security.OktaJwtAccessTokenConverter;
import com.qhrtech.emr.restapi.security.PatientUserDetails;
import com.qhrtech.emr.restapi.security.db.TenantType;
import io.opentelemetry.api.trace.Span;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Filter that sets the Tenant Id in the slf4j Mapping Diagnostic Context
 *
 * @author bryan.bergen
 */
public class MultiTenantLoggingFilter implements Filter {

  private static final String MDC_TENANT_ID_KEY = "tenant_id";
  private static final String NO_TENANT_ID = "BAD REQUEST";

  @Value("${tenants.type:single}")
  private String tenantType;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        filterConfig.getServletContext());
  }

  private void addTenantToTelemetry(ServletRequest request) {
    if (!isTenantRequired()) {
      return;
    }
    String tenantId = getTenantId(request);
    if (tenantId != null && !tenantId.isEmpty()) {
      Span currentSpan = Span.current();
      currentSpan.setAttribute("deployment.acron", tenantId);
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      MDC.put(MDC_TENANT_ID_KEY, tenantKey(request));
      addTenantToTelemetry(request);
      chain.doFilter(request, response);
    } finally {
      MDC.remove(MDC_TENANT_ID_KEY);
    }
  }

  @Override
  public void destroy() {
    // ignore
  }

  public boolean isTenantRequired() {
    return TenantType.lookup(tenantType).requiresTenantId();
  }

  public String tenantKey(ServletRequest request) {
    // If tenant id is not required return an empty string
    if (!isTenantRequired()) {
      return "";
    }

    return brace(getTenantId(request));
  }

  private String brace(String s) {
    return "[" + (s != null ? s : NO_TENANT_ID) + "]";
  }

  private String getTenantId(ServletRequest request) {
    // get tenant for the okta clients
    // we are setting identifier for the okta tokens as : Okta. Check OktaJwtAccessTokenConverter
    // for details.
    if (!(SecurityContextHolder.getContext()
        .getAuthentication() instanceof OAuth2Authentication)) {
      return null;
    }
    OAuth2Authentication auth =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

    if (auth.getOAuth2Request().getResourceIds().contains(OktaJwtAccessTokenConverter.OKTA)) {
      // ACRON is set in the tenant key as one of the oauth request extensions
      return (String) auth.getOAuth2Request().getExtensions().get("tenant");
    }
    if (auth.isClientOnly()) {
      // client credentials grant, tenant id must come in the url
      return request.getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    } else {
      // User authentication, tenant id is in the user details
      if (auth.getPrincipal() instanceof AccuroUserDetails) {
        AccuroUserDetails userDetails = (AccuroUserDetails) auth.getPrincipal();
        return userDetails.getTenantId();
      } else if (auth.getPrincipal() instanceof PatientUserDetails) {
        PatientUserDetails userDetails = (PatientUserDetails) auth.getPrincipal();
        return userDetails.getTenantId();
      } else {
        return null;
      }
    }
  }
}
