
package com.qhrtech.emr.restapi.config;

import com.qhrtech.emr.restapi.security.AccuroAuthenticationDetailsSource;
import com.qhrtech.emr.restapi.security.AccuroLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public AccuroLoginAuthenticationProvider accuroLoginAuthenticationProvider() {
    return new AccuroLoginAuthenticationProvider();
  }

  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(accuroLoginAuthenticationProvider());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(WebSecurity web) {
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        // Permit public access to these paths
        .antMatchers(
            "/login",
            "/login-error",
            "/resources/**")
        .permitAll()
        // Any other request must be authenticated
        .anyRequest()
        .authenticated()
        .and()
        // Handle authentication exceptions
        .exceptionHandling()
        .accessDeniedPage("/login?error=denied")
        .and()
        // Cross site request forgery
        .csrf()
        .requireCsrfProtectionMatcher(
            new AntPathRequestMatcher("/oauth/authorize"))
        .disable()
        // Logout page
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .and()
        // Login page
        .formLogin()
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=failure")
        .loginPage("/login")
        // Authentication details source
        .authenticationDetailsSource(
            new AccuroAuthenticationDetailsSource());
  }
}
