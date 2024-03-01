
package com.qhrtech.emr.restapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qhrtech.emr.restapi.config.serialization.AccuroObjectMapperFactory;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.security.AccuroSecurityRsInvoker;
import com.qhrtech.emr.restapi.security.exceptions.ValidationExceptionMapper;
import com.qhrtech.emr.restapi.security.exceptions.WebApplicationExceptionHandler;
import com.qhrtech.emr.restapi.util.CalendarParamConverterProvider;
import com.qhrtech.emr.restapi.util.PatchInInterceptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.MultipartProvider;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author bdickie
 */
@Configuration
public class CxfConfig {

  @Autowired
  private List<AbstractEndpoint> restServices;

  @Autowired
  public MultipartProvider conversationsMultipartProvider;

  @Bean
  public ObjectMapper jsonObjectMapper() {
    return AccuroObjectMapperFactory.newJsonObjectMapper();
  }

  @Bean
  public JacksonJsonProvider jacksonJsonProvider() {
    return new JacksonJsonProvider(jsonObjectMapper());
  }

  @Bean
  public CalendarParamConverterProvider calendarProvider() {
    return new CalendarParamConverterProvider();
  }

  @Bean
  public AccuroSecurityRsInvoker accuroSecurityRsInvoker() {
    return new AccuroSecurityRsInvoker();
  }

  @Bean
  public WebApplicationExceptionHandler webApplicationExceptionHandler() {
    return new WebApplicationExceptionHandler();
  }

  @Bean
  public Server restServices() {
    JAXRSServerFactoryBean bean =
        new JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean();
    bean.setServiceBeans(new ArrayList<>(restServices));
    bean.setInvoker(accuroSecurityRsInvoker());
    bean.setProviders(
        Arrays.asList(
            jacksonJsonProvider(),
            calendarProvider(),
            webApplicationExceptionHandler(),
            conversationsMultipartProvider,
            new ValidationExceptionMapper()));
    bean.setInInterceptors(Collections.singletonList(new JAXRSBeanValidationInInterceptor()));
    bean.setAddress("/");
    bean.setProperties(getProperties());
    Server server = bean.create();

    PatchInInterceptor patchInterceptor = new PatchInInterceptor();
    server.getEndpoint().getInInterceptors().add(patchInterceptor);

    return server;
  }

  private Map<String, Object> getProperties() {
    Map<String, Object> properies = new HashMap<>();
    // 20 MB
    properies.put("attachment-max-size", "20971520");
    return properies;
  }

}
