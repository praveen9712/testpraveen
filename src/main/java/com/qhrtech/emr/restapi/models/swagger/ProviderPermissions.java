
package com.qhrtech.emr.restapi.models.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProviderPermissions {

  /**
   * The logical operation among these permissions.
   *
   * @return The logical operation
   */
  LogicalOperation operation() default LogicalOperation.AND;

  /**
   * The list of this provider permissions.
   *
   * @return The array of this provider permissions
   */
  ProviderPermission[] providerPermissions() default {};
}
