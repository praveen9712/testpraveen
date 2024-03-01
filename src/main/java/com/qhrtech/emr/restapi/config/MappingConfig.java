
package com.qhrtech.emr.restapi.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bryan.bergen
 */
@Configuration
public class MappingConfig {

  @Bean
  public Mapper modelMapper() {
    DozerBeanMapper mapper = new DozerBeanMapper();
    mapper.addMapping(MappingConfig.class.getResourceAsStream("/dozer-mappings.xml"));
    return mapper;
  }
}
