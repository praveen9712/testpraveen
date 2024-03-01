
package com.qhrtech.emr.restapi.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * @author Blake Dickie
 */
public class AccuroSecurityExpressionHandler extends OAuth2MethodSecurityExpressionHandler {

  @Override
  public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
      MethodInvocation mi) {
    StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, mi);
    ec.setVariable("accuro", new AccuroSecurityExpressionMethods(authentication));
    ec.setVariable("oauth2", new CustomOAuth2SecurityExpressionMethods(authentication));
    ec.setVariable("accuroapi", new AccuroApiSecurityExpressionMethods(authentication));
    return ec;
  }

}
