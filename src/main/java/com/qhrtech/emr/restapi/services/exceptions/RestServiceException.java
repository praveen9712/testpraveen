
package com.qhrtech.emr.restapi.services.exceptions;

/**
 * Thrown when an IO exception occurs when calling an external REST endpoint.
 */
public class RestServiceException extends RuntimeException {

  public RestServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public RestServiceException(String message) {
    super(message);
  }

}
