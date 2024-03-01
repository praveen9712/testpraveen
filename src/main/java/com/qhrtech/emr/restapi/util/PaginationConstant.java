
package com.qhrtech.emr.restapi.util;

/**
 * This enum defines the constants which are used for pagination endpoints.
 */
public enum PaginationConstant {

  DEFAULT_PAGE_SIZE(25),
  MAX_PAGE_SIZE(50);

  private final int size;

  PaginationConstant(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }
}
