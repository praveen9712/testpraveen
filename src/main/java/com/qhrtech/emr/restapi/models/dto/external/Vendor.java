
package com.qhrtech.emr.restapi.models.dto.external;

import java.util.Arrays;

public enum Vendor {

  QHR("QHR"),
  OKTA("Okta"),
  CLOUD_AD("CloudAD");

  private final String vendorName;

  Vendor(String vendorName) {
    this.vendorName = vendorName;
  }

  public static Vendor lookup(String name) {
    return Arrays.stream(values())
        .filter(e -> e.getVendorName().equals(name))
        .findFirst()
        .orElse(null);
  }

  public String getVendorName() {
    return vendorName;
  }
}
