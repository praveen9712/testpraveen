/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;

/**
 * @author kevin.kendall
 */
public class AccuroWebSecurityExpressionHandler extends OAuth2WebSecurityExpressionHandler {

  @Override
  protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
      FilterInvocation invocation) {
    StandardEvaluationContext ec =
        super.createEvaluationContextInternal(authentication, invocation);
    ec.setVariable("accuro", new AccuroSecurityExpressionMethods(authentication));
    ec.setVariable("accuroapi", new AccuroApiSecurityExpressionMethods(authentication));
    return ec;
  }

}
