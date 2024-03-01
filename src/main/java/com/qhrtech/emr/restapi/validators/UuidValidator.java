
package com.qhrtech.emr.restapi.validators;

import static com.qhrtech.emr.restapi.validators.RegexValidator.UUID_PATTERN;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UuidValidator implements ConstraintValidator<CheckUuid, String> {

  private static final Pattern UUID_REGEX_PATTERN =
      Pattern.compile(UUID_PATTERN);

  @Override
  public void initialize(CheckUuid checkUuid) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

    if (Objects.isNull(value)) {
      return true;
    }
    return UUID_REGEX_PATTERN.matcher(value).matches();
  }
}
