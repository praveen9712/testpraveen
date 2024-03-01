/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.services.impl.HostedOauthServiceImpl;
import com.qhrtech.util.SoftCache;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author kevin.kendall
 */


@Component
public class AccuroScopeLookup {

  @Autowired
  private HostedOauthServiceImpl hostedOauthService;
  @Autowired
  @Qualifier("scopeCache")
  private SoftCache<String, AccuroScope> scopeCache;


  public Set<AccuroScope> getScopes(Set<String> scopeIds) {
    Set<AccuroScope> scopes = new HashSet<>();
    Set<String> newScopes = new HashSet<>();
    final SoftCache<String, AccuroScope> cache = scopeCache;
    synchronized (cache) {
      for (String id : scopeIds) {
        AccuroScope scope = cache.lookup(id);
        if (scope == null) {
          newScopes.add(id);
        } else {
          scopes.add(scope);
        }
      }
      if (!newScopes.isEmpty()) {
        Collection<AccuroScope> tempScopes = hostedOauthService.lookupScopes(newScopes);
        for (AccuroScope s : tempScopes) {
          cache.put(s.getId(), s);
        }
        scopes.addAll(tempScopes);
      }
    }
    return scopes;
  }
}
