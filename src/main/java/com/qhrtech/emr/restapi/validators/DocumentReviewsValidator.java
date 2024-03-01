
package com.qhrtech.emr.restapi.validators;

import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DocumentReviewsValidator
    implements ConstraintValidator<CheckDocumentReviews, DocumentReviewDto> {
  public void initialize(CheckDocumentReviews constraint) {
  }

  public boolean isValid(DocumentReviewDto obj, ConstraintValidatorContext context) {
    if (obj == null) {
      return true;
    }

    if (!(obj.getReviewDate() == null) && (obj.getProviderId() == 0)) {
      return false;
    }


    return true;
  }

}
