
package com.qhrtech.emr.restapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.joda.time.LocalDateTime;

public class LocalDateTime2RangeValidator
    implements ConstraintValidator<CheckLocalDateTime2Range, LocalDateTime> {

  @Override
  public void initialize(CheckLocalDateTime2Range checkLocalDateRange) {

  }

  @Override
  public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    LocalDateTime minLocalDate = new LocalDateTime(0, 1, 1, 0, 0, 0, 0);

    LocalDateTime maxLocalDate = new LocalDateTime(10000, 1, 1, 0, 0);

    return value.isAfter(minLocalDate) && value.isBefore(maxLocalDate);
  }
}
