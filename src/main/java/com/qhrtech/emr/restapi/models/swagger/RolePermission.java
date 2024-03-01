
package com.qhrtech.emr.restapi.models.swagger;

import com.qhrtech.emr.accuro.permissions.FeatureType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermission {

  /**
   * The role type of this role permission.
   *
   * @return The role type of this role permission.
   */
  String type();



  String accessLevel() default "READ_ONLY";


}
