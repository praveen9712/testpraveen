
package com.qhrtech.emr.restapi.config;

import com.qhrtech.emr.restapi.security.AccuroResourceServerTokenServices;
import com.qhrtech.emr.restapi.security.AccuroWebSecurityExpressionHandler;
import com.qhrtech.emr.restapi.security.MultiTokenResourceServerTokenServices;
import com.qhrtech.emr.restapi.security.OktaResourceServerTokenServices;
import com.qhrtech.emr.restapi.security.OktaResourcesFactory;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * @author Blake Dickie
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Autowired
  OktaResourcesFactory metadata;
  @Autowired
  private TokenStore tokenStore;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources
        .expressionHandler(new AccuroWebSecurityExpressionHandler())
        .tokenServices(
            new MultiTokenResourceServerTokenServices(
                new OktaResourceServerTokenServices(getJwtTokenStore()),
                new AccuroResourceServerTokenServices(tokenStore)));
  }


  @Override
  public void configure(HttpSecurity http) throws Exception {

    http.requestMatchers()
        .antMatchers("/rest/**").and().authorizeRequests()
        .antMatchers("/rest/**/portal/**").permitAll()
        .antMatchers("/rest/**/provider-portal/**")
        .access("#oauth2.isOAuth() and ((!#oauth2.isClient() and #accuro.isProvider()) "
            + "or #accuroapi.isQhrAuthorizedUser())")
        .antMatchers("/rest/**/materials/**")
        .access("#oauth2.isOAuth() and ((!#oauth2.isClient() and #accuro.isProvider()) "
            + "or #accuroapi.isQhrAuthorizedUser())")
        .antMatchers("/rest/**/patient-portal/**")
        .access("#oauth2.isOAuth() and !#oauth2.isClient() and #accuro.isPatient()")
        .antMatchers("/rest/**").access("#oauth2.isOAuth()");
  }

  private OktaResourcesFactory getJwtTokenStore() throws IOException {
    return metadata;
  }
}
