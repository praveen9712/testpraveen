
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

/**
 * Represents clinical status for allergens.
 */
public enum ClinicalStatus {

  /**
   * Confirmed or verified the allergy for the patient.
   */
  ConfirmedOrVerified("C"),

  /**
   * Doubt the allergy is raised.
   */
  DoubtRaised("D"),

  /**
   * Erroneous allergy.
   */
  Erroneous("E"),

  /**
   * Confirmed but inactive allergy.
   */
  ConfirmedButInactive("I"),

  /**
   * Pending if the patient has the allergy.
   */
  Pending("P"),

  /**
   * Reputed allergy.
   */
  Reputed("R"),

  /**
   * Terminated and reclassified allergy.
   */
  TerminatedAndReclassified("T"),

  /**
   * Suspect allergy.
   */
  Suspect("S");

  /**
   * The clinical status for allergens.
   */
  private final String status;

  ClinicalStatus(String status) {
    this.status = status;
  }

  public static ClinicalStatus lookup(String status) {
    for (ClinicalStatus clinicalStatus : values()) {
      if (clinicalStatus.getStatus().equals(status)) {
        return clinicalStatus;
      }
    }
    return null;
  }

  public String getStatus() {
    return status;
  }
}
