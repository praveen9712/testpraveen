/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.restapi.services.AccuroPatientService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AccuroPatientService} that caches the result preventing multiple calls
 * to the database
 *
 * If there is a cache miss, this implementation will fall back to its injected delegate
 *
 * @author bryan.bergen
 */
@Service
@Primary
public class CachingAccuroPatientService implements AccuroPatientService {

  private final Map<Long, Patient> simpleCache;
  private final AccuroPatientService delegate;

  @Autowired
  public CachingAccuroPatientService(
      @Qualifier("defaultAccuroPatientService") AccuroPatientService delegate) {
    this.delegate = delegate;
    this.simpleCache = new HashMap<>();
  }

  @Override
  public Patient getPatientByIdpUserId(long idpUserId) throws ProtossException {
    if (simpleCache.containsKey(idpUserId)) {
      return simpleCache.get(idpUserId);
    } else {
      Patient p = delegate.getPatientByIdpUserId(idpUserId);
      simpleCache.put(idpUserId, p);
      return p;
    }

  }

}
