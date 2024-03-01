/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.config;

import com.qhrtech.emr.restapi.security.AccuroOAuthClientCacheLookup;
import com.qhrtech.emr.restapi.security.AccuroOAuthClientLookup;
import com.qhrtech.emr.restapi.security.AccuroRequestFactory;
import com.qhrtech.emr.restapi.security.AccuroScopeLookup;
import com.qhrtech.emr.restapi.security.AccuroTokenEnhancer;
import com.qhrtech.emr.restapi.security.AccuroUserApprovalHandler;
import com.qhrtech.emr.restapi.security.MultiTenantAuthenticationKeyGenerator;
import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Blake Dickie
 */
@Configuration
@EnableAuthorizationServer
@EnableTransactionManagement
@EnableScheduling
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  @Autowired
  @Qualifier(value = "authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  @Qualifier("tokenstore")
  private DataSource dataSource;

  @Autowired
  private DataSource hostedOauthDataSource;

  @Autowired
  private DataSourceService dsService;

  @Autowired
  private WebResponseExceptionTranslator exceptionTranslator;

  @Bean
  public AccuroOAuthClientLookup clientLookup() {
    AccuroOAuthClientLookup lookup = new AccuroOAuthClientCacheLookup();
    return lookup;
  }


  @Bean
  public PasswordEncoder clientSecretEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public ApprovalStore approvalStore() {
    return new JdbcApprovalStore(dataSource);
  }

  @Bean
  public AuthenticationKeyGenerator authKeyGenerator() {
    return new MultiTenantAuthenticationKeyGenerator();
  }

  @Bean
  public TokenStore tokenStore(AuthenticationKeyGenerator authKeyGenerator) {
    JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
    tokenStore.setAuthenticationKeyGenerator(authKeyGenerator);
    return tokenStore;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new AccuroTokenEnhancer();
  }

  @Bean
  public OAuth2RequestFactory requestFactory() {
    return new AccuroRequestFactory(clientLookup());
  }

  @Bean
  public PlatformTransactionManager dsTransactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }

  @Primary
  @Bean
  protected AuthorizationServerTokenServices tokenServices() throws Exception {
    CustomTokenServices tokenService = new CustomTokenServices();
    tokenService.setTokenStore(tokenStore(authKeyGenerator()));
    tokenService.setSupportRefreshToken(true);
    tokenService.setReuseRefreshToken(false);
    tokenService.setTokenEnhancer(tokenEnhancer());
    return tokenService;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientLookup());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    AuthorizationCodeServices s = new JdbcAuthorizationCodeServices(dataSource);
    UserApprovalHandler a = new AccuroUserApprovalHandler(dsService);
    endpoints.tokenStore(tokenStore(authKeyGenerator()))
        .tokenEnhancer(tokenEnhancer())
        .tokenServices(tokenServices())
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(s)
        .userApprovalHandler(a)
        .requestFactory(requestFactory())
        .exceptionTranslator(exceptionTranslator)
        .approvalStore(approvalStore())
        .reuseRefreshTokens(false);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.realm("Accuro EMR RestAPI");
  }
}

