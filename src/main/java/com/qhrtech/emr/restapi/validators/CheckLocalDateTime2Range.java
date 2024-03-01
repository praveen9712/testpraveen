
package com.qhrtech.emr.restapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = LocalDateTime2RangeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLocalDateTime2Range {
  /*
   * The data type, datetime, only allows to insert date between 1753-01-01(include) and
   * 9999-12-31(include)
   */
  String message() default "The date time must"
      + " be between 01-01-01(include) and 9999-12-31(include)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
