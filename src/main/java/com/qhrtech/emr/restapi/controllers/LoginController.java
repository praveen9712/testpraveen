/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.controllers;

import com.qhrtech.emr.restapi.security.db.TenantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author bdickie
 */
@Controller
@PropertySource("${jdbc.resource}")
public class LoginController {

  @Value("${tenants.type:single}")
  private String tenantType;

  @RequestMapping("/login")
  public String login(Model model, String error) {

    if (error != null) {
      switch (error) {
        case "failure":
          model.addAttribute("loginFailure", true);
          break;
        case "denied":
          model.addAttribute("accessDenied", true);
          break;
        default:
          model.addAttribute("otherError", true);
          break;
      }
    }

    TenantType t = TenantType.lookup(tenantType);
    model.addAttribute("tenantType", t);
    return "login";
  }

}
