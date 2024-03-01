
package com.qhrtech.emr.restapi.models.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a constraint violation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstraintViolation {

  @JsonProperty("field")
  private String field;
  @JsonProperty("value")
  private String value;
  @JsonProperty("constraint")
  private String constraint;

  /**
   * The field where the violation occurred.
   *
   * @documentationExample lastName
   *
   * @return The field where the violation occurred.
   */
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  /**
   * The constraint that was violated.
   *
   * @documentationExample may not be null
   *
   * @return The constraint that was violated.
   */
  public String getConstraint() {
    return constraint;
  }

  public void setConstraint(String constraint) {
    this.constraint = constraint;
  }

  /**
   * The value that violated the constraint.
   *
   * @documentationExample (null)
   *
   * @return The value that violated the constraint.
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConstraintViolation constraintViolation = (ConstraintViolation) o;

    if (field != null ? !field.equals(constraintViolation.field)
        : constraintViolation.field != null) {
      return false;
    }
    if (value != null ? !value.equals(constraintViolation.value)
        : constraintViolation.value != null) {
      return false;
    }
    return constraint != null ? constraint.equals(constraintViolation.constraint)
        : constraintViolation.constraint == null;
  }

  @Override
  public int hashCode() {
    int result = field != null ? field.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (constraint != null ? constraint.hashCode() : 0);
    return result;
  }
}
