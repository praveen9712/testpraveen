
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.accuro.api.security.UserSecurity;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionDetailsDto;
import com.qhrtech.emr.restapi.security.exceptions.PrescriptionConversionException;

/**
 * Extracts the medical ingredients of a Prescription, be it in the form of a Natural Health
 * Product, Alternative Health Product, Generic or Manufactured Drug etc, for a Prescription
 * Medication.
 *
 * Depending on the DIN system of the prescription, the constituent ingredients will be extracted in
 * different ways.
 */
public interface PrescriptionDetailsService {

  /**
   *
   * @param p The Prescription record to extract the details for.
   * @param includeCcddMapping The flag if the prescription includes CCDD mapping
   *
   * @return A wrapper class for the constituent parts of the prescription.
   *
   * @throws DataAccessException If there was an error at the database level (SQL Error).
   * @throws PrescriptionConversionException If the passed prescription was in an invalid state.
   */
  PrescriptionDetailsDto getPrescriptionDetails(PrescriptionMedication p,
      boolean includeCcddMapping)

      throws ProtossException;


  /**
   * This returns the UserSecurity object with roles and permissions for read access to medical
   * summary
   *
   * @return User security service
   */
  UserSecurity getPermissionsForMedicalSummary();

}
