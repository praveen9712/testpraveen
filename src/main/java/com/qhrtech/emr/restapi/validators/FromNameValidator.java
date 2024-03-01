
package com.qhrtech.emr.restapi.validators;

import com.qhrtech.emr.restapi.models.enums.DocumentScope;
import com.qhrtech.emr.restapi.models.validators.DocumentValidator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;

public class FromNameValidator implements ConstraintValidator<CheckFrom, DocumentValidator> {
  public void initialize(CheckFrom constraint) {
  }

  public boolean isValid(DocumentValidator documentValidator, ConstraintValidatorContext context) {

    if (!documentValidator.getType().equalsIgnoreCase(DocumentScope.REFERRAL.toString())) {
      return true;
    }

    if (StringUtils.isBlank(documentValidator.getDocumentDto().getFromName())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("From Name can not be null or blank.")
          .addPropertyNode("from_name")
          .addConstraintViolation();

      return false;
    }

    return true;
  }

}
