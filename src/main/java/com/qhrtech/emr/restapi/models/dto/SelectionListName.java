
package com.qhrtech.emr.restapi.models.dto;

/**
 * The selection list name enum.
 */
public enum SelectionListName {

  /**
   * Referral Order Status.
   */
  REFERRAL_STATUS("referral-statuses", "ReferralStatus"),

  /**
   * Referral Order Type.
   */
  REFERRAL_TYPE("referral-types", "ReferralType"),

  /**
   * Consult type for waitlist.
   */
  SWL_CONSULT_TYPE("consult-type", "SWLConsultType"),

  /**
   * Complaint list for waitlist.
   */
  COMPLAINT("complaint", "Complaint"),

  /**
   * Reason for mask removal.
   */
  MASK_REMOVAL_REASON("mask-removal-reasons", "MaskRemovalReason"),

  /**
   * nameSuffixes.
   */
  NAME_SUFFIXES("name-suffixes", "Suffix"),

  /**
   * Address Type.
   */
  ADDRESS_TYPE("address-types", "AddressType"),

  /**
   * Chart unlock reason.
   */
  CHART_UNLOCK_REASON("chart-unlock-reasons", "ChartUnlockReason");

  /**
   * The list name of the resource which the user uses.
   */
  private String resourceName;

  /**
   * The real list name which Accuro uses.
   */
  private String listName;


  SelectionListName(String resourceName, String listName) {

    this.resourceName = resourceName;
    this.listName = listName;

  }

  public static SelectionListName lookup(String resourceName) {
    for (SelectionListName list : values()) {
      if (list.getResourceName().equalsIgnoreCase(resourceName)) {
        return list;
      }
    }
    return null;
  }

  public String getListName() {
    return listName;
  }

  public String getResourceName() {
    return resourceName;
  }

}
