
package com.qhrtech.emr.restapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = FromTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckFromType {
  String message() default "From type should be either null or contain one of these values- "
      + " Physician, Insurer, Contact, Patient, OneTimeRecipient";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
