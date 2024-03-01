
package com.qhrtech.emr.restapi.models.dto;

import com.qhrtech.emr.accuro.model.security.AccuroProvince;
import java.util.Arrays;
import java.util.List;


/**
 * The enum constants are categorized for determining the reason of when the patient terminates the
 * provider enrollment. The codes, 1 to 8, are for only MB.
 *
 */

public enum ProviderEnrollmentTerminationReason {

  // MB Updated codes R-05661
  /**
   * Patient Deceased. This code is for MB only.
   */
  PATIENT_DECEASED(1, "Patient Deceased", "Patient Deceased", AccuroProvince.MB),

  /**
   * Patient Moved Out of Area. This code is for MB only.
   */
  PATIENT_MOVED(2, "Patient moved out of area", "Patient Moved Out of Area", AccuroProvince.MB),

  /**
   * Patient Left Jurisdiction (Province). This code is for MB only.
   */
  PATIENT_LEFT_PROVINCE(3, "Patient left province", "Patient Left Jurisdiction (Province)",
      AccuroProvince.MB),

  /**
   * Patient Added in Error. This code is for MB only.
   */
  PATIENT_ERROR(4, "Patient added in error", "Patient Added in Error", AccuroProvince.MB),

  /**
   * Patient No Longer in Primary Care. This code is for MB only.
   */
  PATIENT_LEFT_PRIMARY_CARE(5, "Patient no longer in Primary Care",
      "Patient No Longer in Primary Care", AccuroProvince.MB),

  /**
   * Clinic/Provider Initiated Termination. This code is for MB only.
   */
  CLINIC_PROVIDER_REQUEST(6, "Clinic/provider request", "Provider Initiated Termination",
      AccuroProvince.MB),

  /**
   * Patient Request to Initiate Termination. This code is for MB only.
   */
  PATIENT_REQUEST(7, "Patient request", "Patient Request", AccuroProvince.MB),

  /**
   * Other. This code is for MB only.
   */
  OTHER(8, "Other", "Other", AccuroProvince.MB),

  // ON Codes (Past MB Codes)
  /**
   * Health Number error. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_HN_ERROR(12, "Ended by Ministry of Health", "Health Number error"),

  /**
   * Patient identified as deceased in Ministry of Health database. This code is for every provinces
   * except for MB.
   */
  ENDED_BY_MOH_DECEASED(14, "Ended by Ministry of Health",
      "Patient identified as deceased in Ministry of Health database"),

  /**
   * Patient added to roster in error. This code is for every provinces except for MB.
   */
  ADDED_IN_ERROR(24, "Added in Error", "Patient added to roster in error"),

  /**
   * Pre-member/Assigned member ended; now enrolled or registered with red and white health card.
   * This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CHANGED_RED_WHITE(30, "Ended by Ministry of Health",
      "Pre-member/Assigned member ended;"
          + " now enrolled or registered with red and white health card"),

  /**
   * Pre-member/Assigned member ended; now enrolled or registered with photo health card. This code
   * is for every provinces except for MB.
   */
  ENDED_BY_MOH_CHANGED_PHOTO(32, "Ended by Ministry of Health",
      "Pre-member/Assigned member ended; now enrolled or registered with photo health card"),

  /**
   * Termination reason confidential. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CLASSIFIED(33, "Ended by Ministry of Health", "Termination reason confidential"),

  /**
   * Patient transferred from roster per physician request. This code is for every provinces except
   * for MB.
   */
  ROSTER_TRANSFER(35, "Patient Transferred",
      "Patient transferred from roster per physician request"),

  /**
   * Original enrolment ended; patient now re-enrolled. This code is for every provinces except for
   * MB.
   */
  REENROLLED(36, "Re-Enrolled", "Original enrolment ended; patient now re-enrolled"),

  /**
   * Original enrolment ended; patient now enrolled as Long Term Care. This code is for every
   * provinces except for MB.
   */
  ENTERED_LTC(37, "Entered Long Term Care",
      "Original enrolment ended; patient now enrolled as Long Term Care"),

  /**
   * Long Term Care enrolment ended; patient has left Long Term Care. This code is for every
   * provinces except for MB.
   */
  LEFT_LTC(38, "Left Long Term Care",
      "Long Term Care enrolment ended; patient has left Long Term Care"),

  /**
   * Assigned member status ended; roster transferred per physician request. This code is for every
   * provinces except for MB.
   */
  ASSIGNED_MEMBER_ENDED(39, "Assigned Status Ended",
      "Assigned member status ended; roster transferred per physician request"),

  /**
   * Physician reported patient as deceased. This code is for every provinces except for MB.
   */
  MEMBER_DECEASED(40, "Patient Deceased", "Physician reported patient as deceased"),

  /**
   * Patient no longer meets selection criteria for your roster – assigned to another physician.
   * This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CRITERIA_REASSIGNED(41, "Ended by Ministry of Health",
      "Patient no longer meets selection criteria for your roster – assigned to another physician"),

  /**
   * Physician ended enrolment; patient entered Long Term Care Facility. This code is for every
   * provinces except for MB.
   */
  ENTERED_LTC_PHYSICIAN(42, "Entered Long Term Care",
      "Physician ended enrolment; patient entered Long Term Care Facility"),

  /**
   * Physician ended patient enrolment. This code is for every provinces except for MB.
   */
  ENDED_BY_PHYSICIAN(44, "Ended by Physician", "Physician ended patient enrolment"),

  /**
   * Time Limited Transfer enrolment ended. This code is for every provinces except for MB.
   */
  ENDED_BY_TIME_LIMITED_TRANSFER(45, "Ended by Ministry of Health",
      "Time Limited Transfer enrolment ended"),

  /**
   * Patient no longer meets selection requirement for your roster. This code is for every provinces
   * except for MB.
   */
  ENDED_BY_MOH_CRITERIA(51, "Ended by Ministry of Health",
      "Patient no longer meets selection requirement for your roster"),

  /**
   * Patient moved out of geographic area. This code is for every provinces except for MB.
   */
  ENDED_BY_PHYSICIAN_PATIENT_MOVED(53, "Ended by Physician",
      "Patient moved out of geographic area"),

  /**
   * Patient left province. This code is for every provinces except for MB.
   */
  ENDED_BY_PHYSICIAN_PATIENT_MOVED_PROV(54, "Ended by Physician", "Patient left province"),

  /**
   * Patient requested enrolment termination. This code is for every provinces except for MB.
   */
  ENDED_BY_PHYSICIAN_PATIENT_REQUEST(56, "Ended by Physician",
      "Patient requested enrolment termination"),

  /**
   * Patient requested enrolment termination. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_PATIENT_REQUEST(57, "Ended by Ministry of Health",
      "Patient requested enrolment termination"),

  /**
   * Patient out of geographic area. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_OUT_OF_AREA(59, "Ended by Ministry of Health", "Patient out of geographic area"),

  /**
   * No current eligibility. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_NOT_ELIGIBLE(60, "Ended by Ministry of Health", "No current eligibility"),

  /**
   * Address override applied. This code is for every provinces except for MB.
   */
  GEO_ACTIVATED(61, "Patient out of Geographic Area", "Address override applied"),

  /**
   * Address override removed. This code is for every provinces except for MB.
   */
  GEO_DEACTIVATED(62, "Patient out of Geographic Area", "Address override removed"),

  /**
   * No current eligibility 2. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_NOT_ELIGABLE2(73, "Ended by Ministry of Health", "No current eligibility"),

  /**
   * No current eligibility 3. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_NOT_ELIGABLE3(74, "Ended by Ministry of Health", "No current eligibility"),

  /**
   * Unconfirmed members ended. This code is for every provinces except for MB.
   */
  ENDED_BY_ROSTER_TRANSFER(81, "Ended by Ministry of Health", "Unconfirmed members ended"),

  /**
   * Ministry has not received the Enrolment/Consent form. This code is for every provinces except
   * for MB.
   */
  NO_CONFIRMATION(82, "No Confirmation Received",
      "Ministry has not received the Enrolment/Consent form"),

  /**
   * Termination reason confidential 2. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CLASSIFIED2(84, "Ended by Ministry of Health", "Termination reason confidential"),

  /**
   * Termination reason confidential 3. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CLASSIFIED3(90, "Ended by Ministry of Health", "Termination reason confidential"),

  /**
   * Termination reason confidential 4. This code is for every provinces except for MB.
   */
  ENDED_BY_MOH_CLASSIFIED4(91, "Ended by Ministry of Health", "Termination reason confidential");

  private int code;
  private String reason;
  private String explanation;
  private List<AccuroProvince> activeProvinces;

  ProviderEnrollmentTerminationReason(int code, String reason, String explanation) {
    this(code, reason, explanation, AccuroProvince.ON, AccuroProvince.BC, AccuroProvince.AB,
        AccuroProvince.SK, AccuroProvince.NS, AccuroProvince.Unknown);
  }

  ProviderEnrollmentTerminationReason(int code, String reason, String explanation,
      AccuroProvince... activeProvinces) {
    this.reason = reason;
    this.explanation = explanation;
    this.code = code;
    this.activeProvinces = Arrays.asList(activeProvinces);
  }

  public int getCode() {
    return code;
  }

  public String getReason() {
    return reason;
  }

  public String getExplanation() {
    return explanation;
  }

  public List<AccuroProvince> getActiveProvinces() {
    return activeProvinces;
  }

  public static ProviderEnrollmentTerminationReason lookup(String reasonName) {
    for (ProviderEnrollmentTerminationReason reason : values()) {
      if (reason.toString().equals(reasonName)) {
        return reason;
      }
    }
    return null;
  }

  public static ProviderEnrollmentTerminationReason lookupByCode(int code) {
    for (ProviderEnrollmentTerminationReason reason : values()) {
      if (reason.getCode() == code) {
        return reason;
      }
    }
    return null;
  }
}

