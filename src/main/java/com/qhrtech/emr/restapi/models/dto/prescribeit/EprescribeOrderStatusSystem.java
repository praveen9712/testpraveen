
package com.qhrtech.emr.restapi.models.dto.prescribeit;

public enum EprescribeOrderStatusSystem {

  PRESCRIBE_IT("PrescribeIt");

  private final String orderStatusSystem;

  EprescribeOrderStatusSystem(String system) {
    this.orderStatusSystem = system;
  }

  public static EprescribeOrderStatusSystem lookup(String orderStatusSystem) {
    for (EprescribeOrderStatusSystem system : values()) {
      if (system.getOrderStatusSystem().equals(orderStatusSystem)) {
        return system;
      }
    }
    return null;
  }

  public String getOrderStatusSystem() {
    return orderStatusSystem;
  }
}
