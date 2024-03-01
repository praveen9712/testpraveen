/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author kevin.kendall
 */
public class AccuroWebAuthenticationDetails extends WebAuthenticationDetails {

  private final String tenantId;

  public AccuroWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    tenantId = request.getParameter("tenant");
  }

  public String getTenantId() {
    return tenantId;
  }

}
