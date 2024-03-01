/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.util.SoftCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @author kevin.kendall
 */
public class AccuroOAuthClientCacheLookup implements AccuroOAuthClientLookup {

  private static final Logger log = Logger.getLogger(AccuroOAuthClientCacheLookup.class);
  @Autowired
  private SoftCache<String, AccuroOAuthClient> clientCache;

  @Override
  public AccuroOAuthClient loadClientByClientId(String clientId)
      throws ClientRegistrationException {
    AccuroOAuthClient client = clientCache.lookup(clientId);

    if (client == null) {
      log.error("OAuth Client not found with id: " + clientId);
      throw new ClientRegistrationException("Client not found.");
    }
    return client;
  }
}
