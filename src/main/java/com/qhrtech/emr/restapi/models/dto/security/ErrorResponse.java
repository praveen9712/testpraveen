
package com.qhrtech.emr.restapi.models.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/**
 * Encapsulates information for the client regarding errors that have occurred.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

  private String error;
  private List<ConstraintViolation> constraintViolations;

  public ErrorResponse(String error) {
    this.error = error;
  }

  public ErrorResponse() {
  }

  /**
   * A message describing the error that occurred.
   *
   * @documentationExample Request body is null.
   * 
   * @return The error message.
   */
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  /**
   * If the error is the result of a constraint violation, then this list will contain each
   * violation that occured.
   * <p>
   * If the error was not due to a constraint violation, this element will not be present.
   *
   * @return List of all constraintViolations.
   */
  public List<ConstraintViolation> getConstraintViolations() {
    return constraintViolations;
  }

  public void setConstraintViolations(
      List<ConstraintViolation> constraintViolations) {
    this.constraintViolations = constraintViolations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ErrorResponse that = (ErrorResponse) o;

    if (error != null ? !error.equals(that.error) : that.error != null) {
      return false;
    }
    return constraintViolations != null ? constraintViolations
        .equals(that.constraintViolations) : that.constraintViolations == null;
  }

  @Override
  public int hashCode() {
    int result = error != null ? error.hashCode() : 0;
    result = 31 * result + (constraintViolations != null ? constraintViolations.hashCode() : 0);
    return result;
  }
}
