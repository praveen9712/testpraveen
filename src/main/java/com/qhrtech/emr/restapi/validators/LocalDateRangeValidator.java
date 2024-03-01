
package com.qhrtech.emr.restapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.joda.time.LocalDate;

public class LocalDateRangeValidator
    implements ConstraintValidator<CheckLocalDateRange, LocalDate> {

  @Override
  public void initialize(CheckLocalDateRange checkLocalDateRange) {

  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

    if (value == null) {
      return true;
    }

    LocalDate minLocalDate = new LocalDate(1752, 12, 31);
    LocalDate maxLocalDate = new LocalDate(10000, 1, 1);

    return value.isAfter(minLocalDate) && value.isBefore(maxLocalDate);
  }
}
