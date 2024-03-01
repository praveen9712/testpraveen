/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.security.AccuroOAuthClient;
import com.qhrtech.emr.restapi.security.AccuroScope;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * @author kevin.kendall
 */
@Service
public interface ClientListService {

  Collection<AccuroOAuthClient> getAllClients();

  AccuroOAuthClient lookupClient(String clientId);

  Collection<AccuroOAuthClient> lookupClients(Collection<String> clientIds);

  Collection<AccuroScope> getAllScopes();

  AccuroScope lookupScope(String scopeId);

  Collection<AccuroScope> lookupScopes(Collection<String> scopeIds);

}
