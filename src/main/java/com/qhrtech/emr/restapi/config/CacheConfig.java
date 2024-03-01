/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.config;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import com.qhrtech.emr.restapi.services.impl.HostedOauthServiceImpl;
import com.qhrtech.util.SoftCache;
import com.qhrtech.util.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevin.kendall
 */
@Configuration
public class CacheConfig {

  @Autowired
  private HostedOauthServiceImpl hostedOauthServiceImpl;

  @Bean
  @Qualifier("clientCache")
  public SoftCache<String, AccuroOAuthClient> clientCache() {
    return new SoftCache<>(SoftCache.CacheType.WEAK, new TransformerImpl());
  }

  @Bean
  @Qualifier("scopeCache")
  public SoftCache<String, AccuroScope> scopeCache() {
    return new SoftCache<>(SoftCache.CacheType.WEAK);
  }

  private class TransformerImpl implements Transformer<String, AccuroOAuthClient> {

    @Override
    public AccuroOAuthClient transform(String input) {
      // Lookup client in database
      AccuroOAuthClient client = hostedOauthServiceImpl.lookupClient(input);
      return client;
    }
  }
}
