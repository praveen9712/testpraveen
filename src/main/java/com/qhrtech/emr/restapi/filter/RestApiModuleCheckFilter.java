/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.filter;

import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.ApiSecurityManager;
import com.qhrtech.emr.restapi.security.PatientUserDetails;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.services.ModuleService;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet Filter responsible solely for checking if the Rest API Module is on
 *
 * @author jesse.pasos
 */
public class RestApiModuleCheckFilter implements Filter {

  private static final String TENANT_PARAM_KEY = "tenant";
  public static final String IMPLICIT_EXTERNAL_ACCESS = "IMPLICIT_EXTERNAL_ACCESS";

  @Autowired
  private ModuleService moduleService;

  @Override
  public void init(FilterConfig fc) throws ServletException {
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
    ApiSecurityContext context = ApiSecurityManager.getInstance().getCurrentSecurityContext();
    Set<String> scopes =
        Optional.ofNullable(context.getScopes()).orElse(Collections.emptySet());

    if (auth.getPrincipal() instanceof PatientUserDetails) {
      try {
        if (!scopes.contains(IMPLICIT_EXTERNAL_ACCESS)
            && !moduleService.isRestApiEnabled(context.getTenantId())) {
          throw new FilterException(
              HttpStatus.FORBIDDEN.value(),
              "Rest API Service is not enabled for this client.");
        }
      } catch (ProtossException e) {
        throw new FilterException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error checking if Rest API Service is enabled.", e);
      }
    } else {
      if (!scopes.contains(IMPLICIT_EXTERNAL_ACCESS)
          && !context.getAccuroApiContext().isHasAccuroApi()) {
        throw new FilterException(
            HttpStatus.FORBIDDEN.value(),
            "Rest API Service is not enabled for this client.");
      }
    }

    fc.doFilter(request, response);
  }

}
