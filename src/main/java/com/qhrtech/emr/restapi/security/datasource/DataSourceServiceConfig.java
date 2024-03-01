/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.datasource;

import com.qhrtech.emr.restapi.security.db.TenantType;
import com.qhrtech.emr.restapi.services.TenantDataSourceDetailsService;
import com.qhrtech.emr.restapi.services.impl.RegistryTenantDataSourceDetailsService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author bryan.bergen
 */
@Configuration
public class DataSourceServiceConfig {

  @Value("${tenants.type:SINGLE}")
  private String tenantType;

  @Autowired
  private AbstractEnvironment env;

  @Autowired
  private ApplicationContext context;

  private static String errorMessage(String path) {
    StringBuilder builder = new StringBuilder();
    builder.append("DataSource resource is not configured ");
    builder.append("( ");
    builder.append(path);
    builder.append(")");
    return builder.toString();
  }

  @Primary
  @Bean(name = "registryTenantDetailsService")
  public TenantDataSourceDetailsService registryTenantDetailsService() {
    return new RegistryTenantDataSourceDetailsService();
  }

  @Bean
  @Primary
  public DataSourceService dataSourceService() {
    switch (databaseType()) {
      case SINGLE:
        return new SingleTenantDataSourceService();
      case HOSTED:
        return new HostedDataSourceService();
      default:
        throw new UnsupportedOperationException("Unsupported Tenant Type.");
    }
  }

  /**
   * Returns a DataSource configured to the accdocs database schema.
   *
   * If no accdocs database is configured in the jdbc.properties file, then the medadmin datasource
   * will be returned as a default.
   *
   * @return DataSource configured for the accdocs database schema.
   */
  @Bean(name = "accdocs")
  public DataSource getAccdocsDataSource() throws IOException {

    if (databaseType() == TenantType.HOSTED) {
      // This configuration is not valid in HOSTED environments
      return null;
    }

    DataSource ds = getDataSource("accdocs.jdbc");
    return ds == null ? getDataSource() : ds; // if accdocs db is not configured, use medadmin db
  }

  /**
   * Returns a DataSource to the configured oauth database.
   *
   * If no oauth database is configured in the jdbc.properties file, then the medadmin datasource
   * will be returned as a default.
   *
   * @return DataSource configured for the tokenstore database schema.
   */
  @Bean(name = "tokenstore")
  public DataSource getTokenStoreDataSource() throws IOException {
    DataSource ds = getDataSource("token-store.jdbc");
    return ds == null ? getDataSource() : ds;
  }

  /**
   * Returns a DataSource configured to the medadmin database.
   *
   * @return DataSource configured for the primary accuro schema.
   */
  @Bean(name = "medadmin")
  public DataSource getDataSource() throws IOException {

    if (databaseType() == TenantType.HOSTED) {
      // This configuration is not valid in HOSTED environments
      return null;
    }
    return getDataSource("jdbc");
  }

  private DataSource getDataSource(String propertyPrefix) throws IOException {
    Resource resource = getJdbcResource("jdbc.resource");
    Properties props = new Properties();
    try (InputStream is = resource.getInputStream()) {
      props.load(is);
    }
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(props.getProperty("jdbc.driverClassName"));
    String url = props.getProperty(propertyPrefix + ".url");
    if (StringUtils.isBlank(url)) {
      return null; // no database configured for this prefix
    }
    dataSource.setUrl(url);
    dataSource.setUsername(props.getProperty(propertyPrefix + ".username"));
    dataSource.setPassword(props.getProperty(propertyPrefix + ".password"));
    return dataSource;
  }

  /**
   * Returns a DataSource to the configured hostedoauth database.
   *
   * If no hostedoauth database is configured in the jdbc.properties file, then the medadmin
   * datasource will be returned as a default.
   *
   * @return DataSource configured for the oauth database schema.
   */
  @Bean
  public DataSource hostedOauthDataSource() throws IOException {
    DataSource ds = getDataSource("oauth.jdbc");
    return ds == null ? getDataSource() : ds;
  }

  private Resource getJdbcResource(String path) {
    String resourcePath = env.getProperty(path);
    if (StringUtils.isEmpty(path)) {
      throw new IllegalStateException(errorMessage(path));
    }
    Resource resource = context.getResource(resourcePath);
    if (resource == null || !resource.exists()) {
      throw new IllegalStateException(errorMessage(path));
    }
    return resource;
  }

  private TenantType databaseType() {
    return TenantType.lookup(tenantType);
  }

}

