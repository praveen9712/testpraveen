
package com.qhrtech.emr.restapi.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qhrtech.emr.restapi.external.RegistryTokenManager;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceDetails;
import com.qhrtech.emr.restapi.models.registry.TenantDataSourceResult;
import com.qhrtech.emr.restapi.security.exceptions.InvalidTenantException;
import com.qhrtech.emr.restapi.services.RegistryServiceApi;
import com.qhrtech.emr.restapi.services.TenantDataSourceDetailsService;
import java.util.Arrays;
import javax.ws.rs.NotFoundException;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Implementation of {@link TenantDataSourceDetailsService} that uses the registry API to retrieve
 * the {@link TenantDataSourceDetails}
 *
 * @author bryan.bergen
 */
public class RegistryTenantDataSourceDetailsService implements TenantDataSourceDetailsService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  @Value("${registry.url:}")
  private String registryUrl;

  @Autowired
  private RegistryTokenManager oauthManager;

  private RegistryServiceApi registryService;

  private static String getAcron(String tenant) {
    if (tenant == null) {
      return null;
    }
    // for the scenario DEVBC_ACCURO or DEVBC_DATABASE
    if (tenant.contains("_")) {
      return tenant.substring(0, tenant.indexOf("_"));
    }
    return tenant;
  }

  @Override
  public TenantDataSourceDetails getDataSourceDetailsByAcronym(String tenant) {
    String auth = oauthManager.getAuthenticationHeader();
    TenantDataSourceResult tenantDataSourceResult;
    String acronym = getAcron(tenant);
    try {
      tenantDataSourceResult =
          registryService().getDataSourceByAcron(acronym, auth);
    } catch (NotFoundException ex) {
      log.debug(ex.getMessage(), ex);
      return null;
    }

    if (tenantDataSourceResult != null) {
      tenantDataSourceResult.getDataSource().setTenantId(acronym);
      return tenantDataSourceResult.getDataSource();
    }
    return null;
  }

  private RegistryServiceApi registryService() {
    if (registryService == null) {
      registryService = buildRegistryService();
    }
    return registryService;
  }

  /**
   * Ideally, this service would be a bean and injected. However, due to some quirky CXF issues, it
   * causes an ordering issue with the CXF bus and end points fail to be loaded properly. We should
   * investigate into this issue in the future so we can properly inject this service and decouple
   * its creation from this class
   *
   * More info can be found in this CXF user thread:
   * http://thread.gmane.org/gmane.comp.apache.cxf.user/24778
   */
  private RegistryServiceApi buildRegistryService() {
    RegistryServiceApi api = JAXRSClientFactory.create(
        registryUrl,
        RegistryServiceApi.class,
        Arrays.asList(
            new JacksonJsonProvider(new ObjectMapper())),
        true);
    ClientConfiguration config = WebClient.getConfig(api);
    TLSClientParameters clientParams = new TLSClientParameters();
    clientParams.setUseHttpsURLConnectionDefaultSslSocketFactory(true);
    clientParams.setUseHttpsURLConnectionDefaultHostnameVerifier(true);
    config.getHttpConduit().setTlsClientParameters(clientParams);

    WebClient.getConfig(api).getOutInterceptors().add(new LoggingOutInterceptor());
    WebClient.getConfig(api).getInInterceptors().add(new LoggingInInterceptor());
    return api;
  }
}
