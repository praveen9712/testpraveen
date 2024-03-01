
package com.qhrtech.emr.restapi.models.swagger;

import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used on a method parameter
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProviderPermission {

  /**
   * The access type of this provider permission.
   *
   * @return The access type of this provider permission
   */
  AccessType type();

  /**
   * The access level of this provider permission.
   *
   * @return The access level of this provider permission
   */
  AccessLevel level();

  String description() default "";
}
