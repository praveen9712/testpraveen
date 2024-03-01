
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.Patient;

/**
 * Service for retrieving Accuro Patients for Patient Portal Authentication
 *
 * @author bryan.bergen
 */
public interface AccuroPatientService {

  /**
   * Retrieves an Accuro {@link Patient} by their idp user id.
   *
   * @param idpUserId IDP User Id of the patient
   *
   * @return The corresponding Accuro Patient object.
   *
   *         Will throw a 401 HttpStatus bearing error if the patient is not linked.
   */
  Patient getPatientByIdpUserId(long idpUserId) throws ProtossException;
}
