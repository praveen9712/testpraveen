/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.controllers;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroOAuthClientLookup;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.security.AccuroScopeLookup;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Blake Dickie
 */
@Controller
public class OAuthController {

  @Autowired
  private AccuroOAuthClientLookup clientLookup;
  @Autowired
  private AccuroScopeLookup scopeLookup;

  @RequestMapping("/oauth/display-code")
  public String displayCode(Model model, String code) {
    model.addAttribute("code", code);
    return "display-code";
  }

  @RequestMapping("/oauth/confirm_access")
  public String oauthConfirmAccess(Model model, HttpServletRequest request) {
    String clientId = (String) request.getAttribute("client_id");
    AccuroOAuthClient client = clientLookup.loadClientByClientId(clientId);

    if (client == null) {
      return "oauth_deny_access";
    }

    String scopeParam = request.getParameter("scope");
    Set<String> scopeIds;
    if (scopeParam == null || scopeParam.isEmpty()) {
      scopeIds = client.getScope();
    } else {
      scopeIds = OAuth2Utils.parseParameterList(scopeParam);
    }
    Set<AccuroScope> scopes = scopeLookup.getScopes(scopeIds);

    model.addAttribute("display_name", client.getDisplayName());
    model.addAttribute("scopes", scopes);
    return "oauth_confirm_access";
  }
}
