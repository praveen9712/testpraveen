
package com.qhrtech.emr.restapi.validators;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FromTypeValidator implements ConstraintValidator<CheckFromType, String> {

  String[] validValues = {"Physician", "Insurer", "Contact", "Patient", "OneTimeRecipient"};

  @Override
  public void initialize(CheckFromType checkFromType) {

  }

  @Override
  public boolean isValid(String fromType, ConstraintValidatorContext constraintValidatorContext) {

    if (fromType == null) {
      return true;
    }

    List fromTypeList = Arrays.asList(validValues);
    if (fromTypeList.contains(fromType)) {
      return true;
    }

    return false;
  }
}
