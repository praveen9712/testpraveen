
package com.qhrtech.emr.restapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.joda.time.LocalDateTime;

public class LocalDateTimeRangeValidator
    implements ConstraintValidator<CheckLocalDateTimeRange, LocalDateTime> {

  @Override
  public void initialize(CheckLocalDateTimeRange checkLocalDateRange) {

  }

  @Override
  public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    LocalDateTime minLocalDate = new LocalDateTime(1752, 12, 31, 23, 59, 59, 999);

    LocalDateTime maxLocalDate = new LocalDateTime(10000, 1, 1, 0, 0);

    return value.isAfter(minLocalDate) && value.isBefore(maxLocalDate);
  }
}
