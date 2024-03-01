
package com.qhrtech.emr.restapi.validators;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileExtensionValidator implements ConstraintValidator<CheckFileExtension, String> {

  String[] extensions;

  @Override
  public void initialize(CheckFileExtension checkFileExtension) {

    this.extensions = checkFileExtension.extensions();
  }

  @Override
  public boolean isValid(String fileName, ConstraintValidatorContext constraintValidatorContext) {

    if (!fileName.contains(".")) {
      return false;
    }

    return Arrays.stream(extensions).anyMatch(
        x -> x.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf("."))));
  }
}
