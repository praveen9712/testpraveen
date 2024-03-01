
package com.qhrtech.emr.restapi.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = BigDecimalTypeValidator.class)
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBigDecimal {
  int precision() default 36;

  int scale() default 18;

  String message() default "Invalid value.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
