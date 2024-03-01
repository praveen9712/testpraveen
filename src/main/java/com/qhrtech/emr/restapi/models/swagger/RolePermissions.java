
package com.qhrtech.emr.restapi.models.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermissions {

  /**
   * The logical operation among these permissions.
   *
   * @return The logical operation
   */
  LogicalOperation operation() default LogicalOperation.AND;

  /**
   * The list of this feature permissions.
   *
   * @return The array of this feature permissions
   */
  RolePermission[] rolePermissions() default {};
}
