/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.accuro.api.medeo.MedeoPatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.medeo.MedeoPatient;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.security.ApiSecurityManager;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.AccuroPatientService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bryan.bergen
 */
@Service
public class DefaultAccuroPatientService implements AccuroPatientService {

  private static final String ERROR_MESSAGE = "Accuro Patient Not Found";

  @Autowired
  private AccuroApiService serviceContainer;

  @Override
  public Patient getPatientByIdpUserId(long idpUserId) throws ProtossException {

    MedeoPatientManager medeoPatientManager =
        serviceContainer.getImpl(MedeoPatientManager.class, getTenantId());
    MedeoPatient medeoPatient =
        medeoPatientManager.getMedeoPatientByIdpId(new BigDecimal(idpUserId).intValueExact());
    if (medeoPatient == null) {
      throw Error.returnUnauthorizedResult(ERROR_MESSAGE);
    }

    if (medeoPatient.getPatientId() == null) {
      throw Error.returnUnauthorizedResult(ERROR_MESSAGE);
    }

    PatientManager patientManager = serviceContainer.getImpl(PatientManager.class, getTenantId());
    Patient patient = patientManager.getPatientById(medeoPatient.getPatientId());

    if (patient != null) {
      return patient;
    }
    throw Error.returnUnauthorizedResult(ERROR_MESSAGE);
  }

  private String getTenantId() {
    return ApiSecurityManager.getInstance().getCurrentSecurityContext().getTenantId();
  }
}
