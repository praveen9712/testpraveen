
package com.qhrtech.emr.restapi.security.exceptions;

/**
 * Exception thrown when there is an invalid state discovered in
 * {@link com.qhrtech.emr.restapi.services.impl.DefaultPrescriptionDetailsService}
 */
public class PrescriptionConversionException extends RuntimeException {

  public PrescriptionConversionException() {
  }

  public PrescriptionConversionException(String message) {
    super(message);
  }

  public PrescriptionConversionException(String message, Throwable cause) {
    super(message, cause);
  }
}
