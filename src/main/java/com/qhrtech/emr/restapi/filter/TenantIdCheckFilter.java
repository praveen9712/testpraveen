/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.filter;

import com.qhrtech.emr.restapi.security.OktaJwtAccessTokenConverter;
import com.qhrtech.emr.restapi.security.db.TenantType;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet Filter responsible for checking if the tenant id is present if required
 *
 * @author bryan.bergen
 */
public class TenantIdCheckFilter implements Filter {

  protected static final String TENANT_PARAM_KEY = "tenant";

  @Value("${tenants.type:single}")
  private String tenantType;

  @Override
  public void init(FilterConfig filterConfig) {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
        this,
        filterConfig.getServletContext());
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof OAuth2Authentication) {
      OAuth2Authentication oauth = (OAuth2Authentication) auth;
      if (oauth.isClientOnly()
          && (TenantType.lookup(tenantType) != null
              && TenantType.lookup(tenantType).requiresTenantId())) {
        if (isTenantMissing(request, oauth)) {
          throw new FilterException(
              HttpStatus.BAD_REQUEST.value(),
              "Required Tenant Id Parameter Missing");
        }
      }
    }
    chain.doFilter(request, response);
  }

  private boolean isTenantMissing(ServletRequest request, OAuth2Authentication oauth) {
    if (oauth.getOAuth2Request().getResourceIds().contains(OktaJwtAccessTokenConverter.OKTA)) {
      String tenant = (String) oauth.getOAuth2Request().getExtensions().get("tenant");
      return StringUtils.isBlank(tenant);
    } else {
      return request.getParameter(TENANT_PARAM_KEY) == null;
    }

  }

}
