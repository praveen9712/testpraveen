/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.exceptions;


public class InvalidTenantException extends RuntimeException {

  public InvalidTenantException(String message) {
    super(message);
  }

  public InvalidTenantException(String message, Throwable cause) {
    super(message, cause);
  }

}
