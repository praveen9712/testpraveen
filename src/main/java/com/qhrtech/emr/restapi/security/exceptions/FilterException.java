/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.exceptions;

/**
 *
 * @author bryan.bergen
 */
public class FilterException extends RuntimeException {

  private final int status;

  public FilterException(int status, String message) {
    super(message);
    this.status = status;
  }

  public FilterException(int status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
