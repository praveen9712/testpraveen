/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.filter;

import com.qhrtech.emr.accuro.api.registry.RegistryEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.registry.RegistryEntry;
import com.qhrtech.emr.restapi.security.AccuroUserDetails;
import com.qhrtech.emr.restapi.security.OktaJwtAccessTokenConverter;
import com.qhrtech.emr.restapi.security.OktaUserDetails;
import com.qhrtech.emr.restapi.security.PatientUserDetails;
import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import com.qhrtech.emr.restapi.security.db.TenantType;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.TenantDataSourceDetailsService;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet Filter responsible for checking if the tenant id is present if required
 *
 * @author william.cameron
 */
public class UuidCheckFilter implements Filter {

  private static final String DBUUID_PARAM_KEY = "uuid";

  @Autowired
  private AccuroApiService api;

  @Autowired
  private TenantDataSourceDetailsService tenantDetailsService;

  @Autowired
  private DataSourceService cachingDataSourceService;

  @Value("${tenants.type:single}")
  private String tenantType;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
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

    if (TenantType.lookup(tenantType).requiresUuid()) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth instanceof OAuth2Authentication) {
        OAuth2Authentication oauth = (OAuth2Authentication) auth;
        String tenantId = getTenantId(oauth, request);
        OAuth2Request oauthRequest = ((OAuth2Authentication) auth).getOAuth2Request();
        boolean skipUuidCheck = false;
        // for okta token uuid check not request as Okta is the trusted source
        if (oauthRequest.getResourceIds().contains(OktaJwtAccessTokenConverter.OKTA)) {
          skipUuidCheck = true;
        }


        if ((auth.getPrincipal() instanceof OktaUserDetails) || skipUuidCheck) {

          if (StringUtils.isBlank(tenantId)) {
            throw new FilterException(HttpStatus.BAD_REQUEST.value(),
                "Tenant Id Missing.");
          }

        } else {
          String dbUuid = getUuid(request);
          if (tenantId == null || dbUuid == null) {
            throw new FilterException(HttpStatus.BAD_REQUEST.value(),
                "Required Parameter Missing.");
          }
          try {
            if (!verifyUuid(dbUuid, tenantId)) {
              throw new FilterException(HttpStatus.BAD_REQUEST.value(),
                  "The requested database uuid does not match one associated with your ACRON");
            }
          } catch (ProtossException e) {
            throw new FilterException(HttpStatus.BAD_REQUEST.value(),
                "Cannot connect to database.");
          }
        }
      }
    }
    chain.doFilter(request, response);
  }

  /**
   * Pulls the UUID from the request object
   *
   * @param request - ServletRequest the filter is operating on
   * @return - database uuid parameter as a string
   */
  private String getUuid(ServletRequest request) {
    return request.getParameter(DBUUID_PARAM_KEY);
  }

  /**
   * Extracts the tenant id from the JWT or Request depending on Grant type
   *
   * @param auth - OAuth value, containing the principle
   * @param request - ServletRequest the filter is operating on
   * @return - tenant acronym parameter as a string
   */
  private String getTenantId(OAuth2Authentication auth, ServletRequest request) {
    if (auth.isClientOnly()) {
      if (auth.getOAuth2Request().getResourceIds().contains(OktaJwtAccessTokenConverter.OKTA)) {
        return (String) auth.getOAuth2Request().getExtensions().get("tenant");
      } else {

        return request.getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
      }

    }

    if (auth.getPrincipal() instanceof AccuroUserDetails) {
      // provider authentication, tenant id is in the principle
      return ((AccuroUserDetails) auth.getPrincipal()).getTenantId();
    }

    if (auth.getPrincipal() instanceof PatientUserDetails) {
      // patient authentication, tenant id is in the principle
      return ((PatientUserDetails) auth.getPrincipal()).getTenantId();
    }
    if (auth.getPrincipal() instanceof OktaUserDetails) {
      // Okta authentication, tenant id is in the principle
      return ((OktaUserDetails) auth.getPrincipal()).getTenantId();
    }
    return null;
  }

  /**
   * Verifies that the passed uuid (as string) is the same as the database associated with the
   * tenant's
   *
   * @param providedUuidString - database uuid from request
   * @param tenantId - tenant if to compare against
   * @return - true if they match, false otherwise
   * @throws FilterException - if a uuid id cannot be obtained from the tenant id
   * @throws DataAccessException If there has been a database error.
   */
  private boolean verifyUuid(String providedUuidString, String tenantId)
      throws ProtossException {
    UUID providedUuid = null;
    try {
      providedUuid = UUID.fromString(providedUuidString);
    } catch (IllegalArgumentException e) {
      throw new FilterException(HttpStatus.BAD_REQUEST.value(), "Invalid UUID", e);
    }
    RegistryEntryManager manager = api.getImpl(RegistryEntryManager.class, tenantId);
    Set<RegistryEntry> dbUuids = manager.getEntriesByType(RegistryEntry.Type.Database);
    if (dbUuids == null || dbUuids.isEmpty()) {
      throw new FilterException(
          HttpStatus.BAD_REQUEST.value(),
          "Unknown Tenant");
    }
    RegistryEntry entry = dbUuids.iterator().next();
    UUID actualUuid = entry.getUuid();
    return providedUuid.equals(actualUuid);
  }

}
