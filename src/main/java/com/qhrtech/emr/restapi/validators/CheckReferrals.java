
package com.qhrtech.emr.restapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = ReferralsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReferrals {

  String message() default "Patient Id and Physician Id are mandatory fields";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
