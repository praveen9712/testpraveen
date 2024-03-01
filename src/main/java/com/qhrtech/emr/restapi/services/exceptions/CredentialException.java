
package com.qhrtech.emr.restapi.services.exceptions;

/**
 * Thrown where there is an issue accessing credentials such as with Key Vault.
 */
public class CredentialException extends RuntimeException {

  public CredentialException(String message) {
    super(message);
  }

  public CredentialException(String message, Throwable cause) {
    super(message, cause);
  }
}
