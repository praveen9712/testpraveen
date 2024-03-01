
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
public @interface FeaturePermission {

  /**
   * The feature type of this feature permission.
   *
   * @return The feature type of this feature permission
   */
  FeatureType type();

  /**
   * Description on how a feature applies for an endpoint.
   * 
   * @return description
   */
  String description() default "";
}
