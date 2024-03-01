
package com.qhrtech.emr.restapi.validators;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BigDecimalTypeValidator implements ConstraintValidator<CheckBigDecimal, BigDecimal> {
  CheckBigDecimal checkDouble;

  public void initialize(CheckBigDecimal checkDouble) {
    this.checkDouble = checkDouble;
  }

  public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext context) {
    if (bigDecimal == null) {
      return true;
    }

    int precision = checkDouble.precision();
    int scale = checkDouble.scale();
    String value = bigDecimal.toString();
    if (value.contains("E")) {
      return false;
    }

    boolean flag = true;

    // the numeric with dot will be handled separately
    if (value.contains(".")) {
      // the length of numeric should not be more than precision. Added +1 to precision to consider
      // dot.
      // precision = Integral part + decimal part. For. e.g 12.3 has precision as 3 and scale as 2.
      if (value.length() > (precision + 1)) {
        flag = false;
        // the decimal part of code should not be more than the scale.
      } else if ((value.substring(value.indexOf(".") + 1)).length() > scale) {
        flag = false;
      }


    } else {
      // If there is no dot in the value, the length should be less than or equal to precision
      // In the integral value, DB accepts value with two less than precision.
      if (value.length() > (precision - 2)) {
        flag = false;
      }
    }
    if (!flag) {

      // Setting the custom error message as precision and scale can vary for any field.
      String message =
          "Max. field length including decimal digits is " + precision + " . Max. digits after "
              + "decimal can be: " + scale;
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
          .addConstraintViolation();
      return false;
    }

    return true;

  }

}
