
package com.qhrtech.emr.restapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
public @interface CheckNull {

  String message() default "may not be null";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
