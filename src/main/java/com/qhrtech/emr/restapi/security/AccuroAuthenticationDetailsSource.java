/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 *
 * @author kevin.kendall
 */
public class AccuroAuthenticationDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, AccuroWebAuthenticationDetails> {

  @Override
  public AccuroWebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new AccuroWebAuthenticationDetails(context);
  }

}
