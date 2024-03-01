/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.exceptions;

/**
 * UnChecked Exception thrown when a datasource cannot be validated due to invalid credentials or
 * url or other reason
 *
 * @author bryan.bergen
 */
public class InvalidDataSourceException extends RuntimeException {

  public InvalidDataSourceException(String message) {
    super(message);
  }

  public InvalidDataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

}
