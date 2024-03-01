
package com.qhrtech.emr.restapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class LocalDateTimeValidator
    implements ConstraintValidator<CheckFutureDate, LocalDateTime> {


  @Override
  public void initialize(CheckFutureDate checkLocalDateTime) {

  }

  @Override
  public boolean isValid(LocalDateTime localDateTime,
      ConstraintValidatorContext constraintValidatorContext) {

    if (localDateTime == null) {
      return true;
    }

    if (!LocalDate.now().isEqual(localDateTime.toLocalDate())) {
      return LocalDate.now().isAfter(localDateTime.toLocalDate());
    }
    return true;

  }
}
