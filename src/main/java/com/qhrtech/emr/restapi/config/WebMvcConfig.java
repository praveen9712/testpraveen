/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import javax.servlet.ServletContext;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author Blake Dickie
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  ServletContext context;

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Bean
  public ServletContextTemplateResolver templateResolver() {
    ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(context);

    resolver.setPrefix("/");
    resolver.setSuffix(".xhtml");
    resolver.setTemplateMode("XHTML");

    return resolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(templateResolver());
    engine.addDialect(new LayoutDialect());
    engine.addDialect(new SpringSecurityDialect());
    return engine;
  }

  @Bean
  public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
    ContentNegotiationManagerFactoryBean contentNegotiationManager =
        new ContentNegotiationManagerFactoryBean();
    contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine());

    MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
    defaultView.setObjectMapper(jsonObjectMapper);
    defaultView.setExtractValueFromSingleKeyModel(true);

    ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
    contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
    contentViewResolver.setViewResolvers(Arrays.<ViewResolver>asList(resolver));
    contentViewResolver.setDefaultViews(Arrays.<View>asList(defaultView));
    return contentViewResolver;
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setAlwaysUseMessageFormat(true);
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasenames("classpath:localization/messages");
    return messageSource;
  }

  @Override
  public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/resources/**")
        .addResourceLocations("/resources/");
  }
}
