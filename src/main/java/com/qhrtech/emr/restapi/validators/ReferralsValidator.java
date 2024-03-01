
package com.qhrtech.emr.restapi.validators;

import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import com.qhrtech.emr.restapi.models.enums.DocumentScope;
import com.qhrtech.emr.restapi.models.validators.DocumentValidator;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReferralsValidator implements ConstraintValidator<CheckReferrals, DocumentValidator> {

  public void initialize(CheckReferrals constraint) {
  }

  public boolean isValid(DocumentValidator documentValidator, ConstraintValidatorContext context) {
    if (!documentValidator.getType().equalsIgnoreCase(DocumentScope.REFERRAL.toString())) {
      return true;
    }

    if (documentValidator.getDocumentDto().getPatientId() == 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Patient Id cannot be null or zero.")
          .addPropertyNode("patient_id")
          .addConstraintViolation();
      return false;
    }

    Set<DocumentReviewDto> documentReviewDtos = documentValidator.getDocumentDto().getReviews();
    if (documentReviewDtos == null || documentReviewDtos.size() == 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Physician Id cannot be null or zero.")
          .addPropertyNode("reviews.physician_id")
          .addConstraintViolation();
      return false;
    }
    boolean flag = false;
    if (documentReviewDtos != null && documentReviewDtos.size() > 0) {
      for (DocumentReviewDto x : documentReviewDtos) {
        if (x.getProviderId() > 0) {
          flag = true;
        }
      }

      if (!flag) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Physician Id cannot be null or zero.")
            .addPropertyNode("reviews.physician_id")
            .addConstraintViolation();
        return false;
      }


    }

    return true;
  }
}
