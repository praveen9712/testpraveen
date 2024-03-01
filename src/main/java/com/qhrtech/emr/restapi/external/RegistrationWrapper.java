/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.external;

/**
 *
 * @author kevin.kendall
 */
public class RegistrationWrapper {

  private final Registration registration;

  private RegistrationWrapper(Registration registration) {
    this.registration = registration;
  }

  public static RegistrationWrapper wrap(Registration registration) {
    return new RegistrationWrapper(registration);
  }

  public Registration getRegistration() {
    return registration;
  }

}
